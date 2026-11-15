import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.api.JavacTool;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Kinds;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.TreeInfo;
import com.sun.tools.javac.tree.TreeScanner;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

/**
 * Java -> C++17 translator for the Soul of Darkness reference sources.
 *
 * It walks javac's attributed syntax trees, so every expression's static type and every
 * identifier's symbol come from the Java compiler itself rather than from guesses.
 * Java semantics that differ from C++ are made explicit in the output:
 *   - left-to-right operand/argument evaluation (sequenced lambdas where order is observable)
 *   - compound assignment reading its target before evaluating the right-hand side
 *   - masked shift counts, >>> , division by zero / INT_MIN, array bounds and null checks
 *   - labeled break/continue, switch-scoped locals, static initialization order
 *
 * usage: J2Cpp <classpath> <outDir> <emitClass,emitClass,...> <file.java>...
 */
public final class J2Cpp {
    static final Set<String> CPP_RESERVED = new HashSet<>(Arrays.asList(
        "and", "or", "xor", "not", "bitand", "bitor", "compl", "and_eq", "or_eq", "xor_eq", "not_eq",
        "auto", "delete", "friend", "namespace", "operator", "template", "typename", "using", "virtual",
        "inline", "struct", "typedef", "bool", "asm", "union", "register", "signed", "unsigned", "extern",
        "explicit", "export", "mutable", "sizeof", "typeid", "wchar_t", "near", "far", "small",
        "errno", "stdin", "stdout", "stderr", "NULL", "EOF", "TRUE", "FALSE", "IN", "OUT", "interface",
        "constexpr", "decltype", "noexcept", "nullptr", "alignas", "alignof", "concept", "requires",
        "int8_t", "int16_t", "int32_t", "int64_t", "main", "environ", "cdecl", "pascal", "huge"));

    final Set<String> emit = new LinkedHashSet<>();
    final Map<MethodSymbol, String> methodNames = new HashMap<>();
    final StringBuilder out = new StringBuilder();
    int indent = 0;
    int tmpCounter = 0;
    ClassSymbol currentClass;

    public static void main(String[] args) throws Exception {
        String classpath = args[0];
        Path outDir = Paths.get(args[1]);
        J2Cpp j = new J2Cpp();
        j.emit.addAll(Arrays.asList(args[2].split(",")));
        List<File> files = new ArrayList<>();
        for (int i = 3; i < args.length; i++) files.add(new File(args[i]));
        JavacTool tool = JavacTool.create();
        StandardJavaFileManager fm = tool.getStandardFileManager(null, null, StandardCharsets.UTF_8);
        Iterable<? extends JavaFileObject> units = fm.getJavaFileObjectsFromFiles(files);
        List<String> options = Arrays.asList("-proc:none", "-nowarn", "-encoding", "UTF-8", "--release", "8", "-cp", classpath);
        JavacTaskImpl task = (JavacTaskImpl) tool.getTask(null, fm, null, options, null, units);
        List<JCCompilationUnit> cus = new ArrayList<>();
        for (Object cu : task.parse()) cus.add((JCCompilationUnit) cu);
        task.analyze();
        List<JCClassDecl> classes = new ArrayList<>();
        for (JCCompilationUnit cu : cus)
            for (JCTree def : cu.defs)
                if (def instanceof JCClassDecl && j.emit.contains(((JCClassDecl) def).name.toString()))
                    classes.add((JCClassDecl) def);
        if (classes.size() != j.emit.size()) throw new IllegalStateException("missing classes to emit");
        classes.sort(Comparator.comparingInt(c -> new ArrayList<>(j.emit).indexOf(c.name.toString())));
        for (JCClassDecl c : classes) j.assignMethodNames(c);
        Files.createDirectories(outDir);
        Files.write(outDir.resolve("sod_gen.h"), j.header(classes).getBytes(StandardCharsets.UTF_8));
        for (JCClassDecl c : classes)
            Files.write(outDir.resolve(c.name + ".cpp"), j.source(c).getBytes(StandardCharsets.UTF_8));
        System.out.println("j2cpp: wrote " + classes.size() + " classes -> " + outDir);
    }

    // ---------------------------------------------------------------- names and types

    static String safe(String n) {
        return CPP_RESERVED.contains(n) ? n + "_" : n;
    }

    static String sigCode(Type t) {
        switch (t.getTag()) {
            case INT: return "I";
            case BOOLEAN: return "Z";
            case BYTE: return "B";
            case SHORT: return "S";
            case CHAR: return "C";
            case LONG: return "J";
            case ARRAY: return "A" + sigCode(((Type.ArrayType) t).elemtype);
            default: return t.tsym.name.toString();
        }
    }

    void assignMethodNames(JCClassDecl c) {
        Map<String, Integer> counts = new HashMap<>();
        Set<String> fields = new HashSet<>();
        for (JCTree d : c.defs) {
            if (d instanceof JCMethodDecl) counts.merge(((JCMethodDecl) d).name.toString(), 1, Integer::sum);
            if (d instanceof JCVariableDecl) fields.add(((JCVariableDecl) d).name.toString());
        }
        for (JCTree d : c.defs) {
            if (!(d instanceof JCMethodDecl)) continue;
            JCMethodDecl m = (JCMethodDecl) d;
            String n = m.name.toString();
            if (n.equals("<init>")) continue;
            String name = safe(n);
            if (counts.get(n) > 1 || fields.contains(n)) {
                StringBuilder sb = new StringBuilder("m_" + n + "_");
                for (JCVariableDecl p : m.params) sb.append(sigCode(p.sym.type));
                if (m.params.isEmpty()) sb.append("V");
                name = sb.toString();
            }
            methodNames.put(m.sym, name);
        }
    }

    boolean generated(Symbol owner) {
        return owner != null && emit.contains(owner.name.toString());
    }

    String methodName(MethodSymbol m) {
        String n = methodNames.get(m);
        if (n != null) return n;
        if (generated(m.owner)) throw new IllegalStateException("unnamed generated method " + m);
        return safe(m.name.toString());
    }

    static boolean isString(Type t) {
        return t != null && t.getTag() == TypeTag.CLASS && t.tsym.getQualifiedName().contentEquals("java.lang.String");
    }

    String type(Type t) {
        switch (t.getTag()) {
            case INT: return "jint";
            case BOOLEAN: return "jboolean";
            case BYTE: return "jbyte";
            case SHORT: return "jshort";
            case CHAR: return "jchar";
            case LONG: return "jlong";
            case VOID: return "void";
            case ARRAY: return "Arr<" + type(((Type.ArrayType) t).elemtype) + ">";
            case CLASS: return "Ref<" + className(t.tsym) + ">";
            case BOT: return "std::nullptr_t";
            default: throw new IllegalStateException("unsupported type " + t + " " + t.getTag());
        }
    }

    static String className(Symbol s) {
        return safe(s.name.toString());
    }

    static boolean isPrimitive(Type t) {
        switch (t.getTag()) {
            case INT: case BOOLEAN: case BYTE: case SHORT: case CHAR: case LONG: return true;
            default: return false;
        }
    }

    // ---------------------------------------------------------------- output helpers

    void line(String s) {
        for (int i = 0; i < indent; i++) out.append("   ");
        out.append(s).append('\n');
    }

    String tmp(String base) {
        return "_" + base + (tmpCounter++);
    }

    // ---------------------------------------------------------------- header

    String header(List<JCClassDecl> classes) {
        StringBuilder h = new StringBuilder();
        h.append("// Generated by tools/j2cpp from the port sources. Do not edit; run tools/gen_port.py.\n");
        h.append("#pragma once\n#include \"jrt.h\"\n#include \"midp.h\"\n\nnamespace sod {\n\n");
        for (JCClassDecl c : classes) h.append("struct ").append(c.name).append(";\n");
        h.append('\n');
        for (JCClassDecl c : classes) {
            String base = c.extending == null ? "Object" : className(((JCExpression) c.extending).type.tsym);
            h.append("struct ").append(c.name).append(" : ").append(base).append(" {\n");
            for (JCTree d : c.defs) {
                if (d instanceof JCVariableDecl) {
                    JCVariableDecl v = (JCVariableDecl) d;
                    String t = type(v.sym.type);
                    if (v.sym.isStatic()) h.append("   static inline ").append(t).append(' ').append(safe(v.name.toString())).append("{};\n");
                    else h.append("   ").append(t).append(' ').append(safe(v.name.toString())).append("{};\n");
                }
            }
            boolean hasCtor = false;
            for (JCTree d : c.defs) {
                if (!(d instanceof JCMethodDecl)) continue;
                JCMethodDecl m = (JCMethodDecl) d;
                if (m.name.contentEquals("<init>")) {
                    hasCtor = true;
                    h.append("   ").append(c.name).append('(').append(params(m)).append(");\n");
                } else {
                    h.append("   ").append(m.sym.isStatic() ? "static " : "virtual ").append(type(m.sym.getReturnType())).append(' ')
                        .append(methodName(m.sym)).append('(').append(params(m)).append(");\n");
                }
            }
            if (!hasCtor) h.append("   ").append(c.name).append("();\n");
            h.append("   static void clinit();\n");
            h.append("   static void portDumpStatics(StateSink& sink);\n");
            h.append("   void portDumpFields(StateSink& sink);\n");
            h.append("};\n\n");
        }
        h.append("} // namespace sod\n");
        return h.toString();
    }

    String params(JCMethodDecl m) {
        StringBuilder sb = new StringBuilder();
        for (JCVariableDecl p : m.params) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(type(p.sym.type)).append(' ').append(safe(p.name.toString()));
        }
        return sb.toString();
    }

    // ---------------------------------------------------------------- class bodies

    String source(JCClassDecl c) {
        out.setLength(0);
        currentClass = c.sym;
        line("// Generated by tools/j2cpp from the port sources. Do not edit; run tools/gen_port.py.");
        line("#include \"sod_gen.h\"");
        line("");
        line("namespace sod {");
        line("");
        List<JCVariableDecl> instanceInits = new ArrayList<>();
        // static initialization, in textual order
        line("void " + c.name + "::clinit() {");
        indent++;
        for (JCTree d : c.defs) {
            if (d instanceof JCVariableDecl) {
                JCVariableDecl v = (JCVariableDecl) d;
                if (v.init == null) continue;
                if (v.sym.isStatic()) line(c.name + "::" + safe(v.name.toString()) + " = " + initExpr(v.init, v.sym.type) + ";");
                else instanceInits.add(v);
            } else if (d instanceof JCBlock && (((JCBlock) d).flags & Flags.STATIC) != 0) {
                stmt((JCBlock) d);
            }
        }
        indent--;
        line("}");
        line("");
        boolean hasCtor = false;
        for (JCTree d : c.defs) {
            if (!(d instanceof JCMethodDecl)) continue;
            JCMethodDecl m = (JCMethodDecl) d;
            tmpCounter = 0;
            if (m.name.contentEquals("<init>")) {
                hasCtor = true;
                line(c.name + "::" + c.name + "(" + params(m) + ") {");
                indent++;
                boolean chained = false;
                for (JCStatement s : m.body.stats) {
                    if (isSuperOrThisCall(s)) {
                        if (TreeInfo.name(((JCMethodInvocation) ((JCExpressionStatement) s).expr).meth).contentEquals("this"))
                            throw new IllegalStateException("this(...) constructor chaining is not supported");
                        chained = true;
                    }
                }
                for (JCVariableDecl v : instanceInits) line("this->" + safe(v.name.toString()) + " = " + initExpr(v.init, v.sym.type) + ";");
                for (JCStatement s : m.body.stats) if (!isSuperOrThisCall(s)) stmt(s);
                indent--;
                line("}");
            } else {
                line(type(m.sym.getReturnType()) + " " + c.name + "::" + methodName(m.sym) + "(" + params(m) + ") {");
                indent++;
                // Java keeps the receiver alive for the whole call even if the last stored reference is cleared.
                if (!m.sym.isStatic()) line("Ref<" + c.name + "> _self(this);");
                for (JCStatement s : m.body.stats) stmt(s);
                indent--;
                line("}");
            }
            line("");
        }
        if (!hasCtor) {
            line(c.name + "::" + c.name + "() {");
            indent++;
            for (JCVariableDecl v : instanceInits) line("this->" + safe(v.name.toString()) + " = " + initExpr(v.init, v.sym.type) + ";");
            indent--;
            line("}");
            line("");
        }
        // Field-by-field state capture in class-file order, for the original-bytecode equivalence check.
        line("void " + c.name + "::portDumpStatics(StateSink& sink) {");
        indent++;
        for (JCTree d : c.defs)
            if (d instanceof JCVariableDecl && ((JCVariableDecl) d).sym.isStatic())
                line("sink.put(\"" + ((JCVariableDecl) d).name + "\", " + c.name + "::" + safe(((JCVariableDecl) d).name.toString()) + ");");
        indent--;
        line("}");
        line("");
        line("void " + c.name + "::portDumpFields(StateSink& sink) {");
        indent++;
        for (JCTree d : c.defs)
            if (d instanceof JCVariableDecl && !((JCVariableDecl) d).sym.isStatic())
                line("sink.put(\"" + ((JCVariableDecl) d).name + "\", this->" + safe(((JCVariableDecl) d).name.toString()) + ");");
        indent--;
        line("}");
        line("");
        line("} // namespace sod");
        return out.toString();
    }

    static boolean isSuperOrThisCall(JCStatement s) {
        if (!(s instanceof JCExpressionStatement)) return false;
        JCExpression e = ((JCExpressionStatement) s).expr;
        if (!(e instanceof JCMethodInvocation)) return false;
        JCExpression meth = ((JCMethodInvocation) e).meth;
        return meth instanceof JCIdent && (((JCIdent) meth).name.contentEquals("super") || ((JCIdent) meth).name.contentEquals("this"));
    }

    // ---------------------------------------------------------------- statements

    void block(JCStatement s) {
        if (s instanceof JCBlock) {
            for (JCStatement t : ((JCBlock) s).stats) stmt(t);
        } else {
            stmt(s);
        }
    }

    void stmt(JCStatement s) {
        if (s instanceof JCBlock) {
            line("{");
            indent++;
            for (JCStatement t : ((JCBlock) s).stats) stmt(t);
            indent--;
            line("}");
        } else if (s instanceof JCVariableDecl) {
            JCVariableDecl v = (JCVariableDecl) s;
            String t = type(v.sym.type);
            if (v.init == null) line(t + " " + safe(v.name.toString()) + "{};");
            else line(t + " " + safe(v.name.toString()) + " = " + initExpr(v.init, v.sym.type) + ";");
        } else if (s instanceof JCExpressionStatement) {
            line(expr(((JCExpressionStatement) s).expr) + ";");
        } else if (s instanceof JCIf) {
            JCIf i = (JCIf) s;
            line("if (" + expr(i.cond) + ") {");
            indent++;
            block(i.thenpart);
            indent--;
            JCStatement e = i.elsepart;
            while (e instanceof JCIf) {
                JCIf ei = (JCIf) e;
                // nested else-if needs its own scope when the condition is sequenced, so keep it simple
                line("} else if (" + expr(ei.cond) + ") {");
                indent++;
                block(ei.thenpart);
                indent--;
                e = ei.elsepart;
            }
            if (e != null) {
                line("} else {");
                indent++;
                block(e);
                indent--;
            }
            line("}");
        } else if (s instanceof JCWhileLoop) {
            loop("while (" + expr(((JCWhileLoop) s).cond) + ") {", ((JCWhileLoop) s).body, "}", null);
        } else if (s instanceof JCDoWhileLoop) {
            loop("do {", ((JCDoWhileLoop) s).body, "} while (" + expr(((JCDoWhileLoop) s).cond) + ");", null);
        } else if (s instanceof JCForLoop) {
            JCForLoop f = (JCForLoop) s;
            line("{");
            indent++;
            for (JCStatement i : f.init) stmt(i);
            StringBuilder step = new StringBuilder();
            for (JCExpressionStatement e : f.step) {
                if (step.length() > 0) step.append(", ");
                step.append(expr(e.expr));
            }
            loop("for (; " + (f.cond == null ? "" : expr(f.cond)) + "; " + step + ") {", f.body, "}", null);
            indent--;
            line("}");
        } else if (s instanceof JCLabeledStatement) {
            JCLabeledStatement l = (JCLabeledStatement) s;
            String label = l.label.toString();
            JCStatement body = l.body;
            if (body instanceof JCWhileLoop) {
                loop("while (" + expr(((JCWhileLoop) body).cond) + ") {", ((JCWhileLoop) body).body, "}", label);
            } else if (body instanceof JCDoWhileLoop) {
                loop("do {", ((JCDoWhileLoop) body).body, "} while (" + expr(((JCDoWhileLoop) body).cond) + ");", label);
            } else if (body instanceof JCForLoop) {
                JCForLoop f = (JCForLoop) body;
                line("{");
                indent++;
                for (JCStatement i : f.init) stmt(i);
                StringBuilder step = new StringBuilder();
                for (JCExpressionStatement e : f.step) {
                    if (step.length() > 0) step.append(", ");
                    step.append(expr(e.expr));
                }
                loop("for (; " + (f.cond == null ? "" : expr(f.cond)) + "; " + step + ") {", f.body, "}", label);
                indent--;
                line("}");
            } else {
                line("{");
                indent++;
                block(body);
                indent--;
                line("}");
            }
            line("brk_" + label + ":;");
        } else if (s instanceof JCSwitch) {
            switchStmt((JCSwitch) s);
        } else if (s instanceof JCReturn) {
            JCReturn r = (JCReturn) s;
            line(r.expr == null ? "return;" : "return " + expr(r.expr) + ";");
        } else if (s instanceof JCBreak) {
            JCBreak b = (JCBreak) s;
            line(b.label == null ? "break;" : "goto brk_" + b.label + ";");
        } else if (s instanceof JCContinue) {
            JCContinue c = (JCContinue) s;
            line(c.label == null ? "continue;" : "goto cont_" + c.label + ";");
        } else if (s instanceof JCTry) {
            JCTry t = (JCTry) s;
            if (t.finalizer != null || (t.resources != null && !t.resources.isEmpty())) throw new IllegalStateException("finally/resources unsupported");
            line("try {");
            indent++;
            block(t.body);
            indent--;
            for (JCCatch c : t.catchers) {
                String ex = tmp("ex");
                line("} catch (JavaThrow& " + ex + ") {");
                indent++;
                line(type(c.param.sym.type) + " " + safe(c.param.name.toString()) + " = jcaught<" + className(c.param.sym.type.tsym) + ">(" + ex + ");");
                block(c.body);
                indent--;
            }
            line("}");
        } else if (s instanceof JCThrow) {
            line("throw JavaThrow(" + expr(((JCThrow) s).expr) + ");");
        } else if (s instanceof JCSynchronized) {
            stmt(((JCSynchronized) s).body);
        } else if (s instanceof JCSkip) {
            line(";");
        } else {
            throw new IllegalStateException("unsupported statement " + s.getClass() + ": " + s);
        }
    }

    void loop(String head, JCStatement body, String tail, String label) {
        line(head);
        indent++;
        if (label != null) {
            line("{");
            indent++;
            block(body);
            indent--;
            line("}");
            line("cont_" + label + ":;");
        } else {
            block(body);
        }
        indent--;
        line(tail);
    }

    void switchStmt(JCSwitch sw) {
        // Java case groups share one scope; hoist their direct declarations so C++ jumps do not bypass initialization.
        line("{");
        indent++;
        for (JCCase c : sw.cases)
            for (JCStatement st : c.stats)
                if (st instanceof JCVariableDecl) {
                    JCVariableDecl v = (JCVariableDecl) st;
                    line(type(v.sym.type) + " " + safe(v.name.toString()) + "{};");
                }
        line("switch (" + expr(sw.selector) + ") {");
        indent++;
        for (JCCase c : sw.cases) {
            for (JCCaseLabel l : c.labels) {
                if (l instanceof JCConstantCaseLabel) line("case " + expr(((JCConstantCaseLabel) l).expr) + ":");
                else if (l instanceof JCDefaultCaseLabel) line("default:");
                else throw new IllegalStateException("unsupported case label");
            }
            indent++;
            for (JCStatement st : c.stats) {
                if (st instanceof JCVariableDecl) {
                    JCVariableDecl v = (JCVariableDecl) st;
                    if (v.init != null) line(safe(v.name.toString()) + " = " + initExpr(v.init, v.sym.type) + ";");
                } else {
                    stmt(st);
                }
            }
            if (c.stats.isEmpty()) line(";");
            indent--;
        }
        indent--;
        line("}");
        indent--;
        line("}");
    }

    // ---------------------------------------------------------------- side-effect analysis

    static final class Fx {
        boolean call, writesShared, readsShared;
        final Set<Symbol> readsLocal = new HashSet<>(), writesLocal = new HashSet<>();

        boolean pure() {
            return !call && !writesShared && writesLocal.isEmpty();
        }
    }

    static boolean isLocal(Symbol s) {
        return s != null && s.kind == Kinds.Kind.VAR && s.owner != null && s.owner.kind != Kinds.Kind.TYP;
    }

    static boolean isConstant(Symbol s) {
        return s instanceof VarSymbol && ((VarSymbol) s).getConstValue() != null;
    }

    static boolean pureCall(MethodSymbol m) {
        String owner = m.owner.getQualifiedName().toString();
        if (owner.equals("java.lang.Math")) return true;
        if (owner.equals("java.lang.String")) return !m.name.contentEquals("getClass");
        return false;
    }

    static Fx fx(JCTree tree) {
        Fx f = new Fx();
        if (tree == null) return f;
        new TreeScanner() {
            void target(JCExpression lhs, boolean alsoRead) {
                lhs = TreeInfo.skipParens(lhs);
                if (lhs instanceof JCIdent && isLocal(((JCIdent) lhs).sym)) {
                    f.writesLocal.add(((JCIdent) lhs).sym);
                    if (alsoRead) f.readsLocal.add(((JCIdent) lhs).sym);
                } else {
                    f.writesShared = true;
                    if (alsoRead) f.readsShared = true;
                    if (lhs instanceof JCFieldAccess) scan(((JCFieldAccess) lhs).selected);
                    else if (lhs instanceof JCArrayAccess) {
                        scan(((JCArrayAccess) lhs).indexed);
                        scan(((JCArrayAccess) lhs).index);
                    }
                }
            }

            @Override public void visitApply(JCMethodInvocation t) {
                Symbol s = TreeInfo.symbol(t.meth);
                if (!(s instanceof MethodSymbol && pureCall((MethodSymbol) s))) f.call = true;
                if (t.meth instanceof JCFieldAccess) scan(((JCFieldAccess) t.meth).selected);
                scan(t.args);
            }

            @Override public void visitNewClass(JCNewClass t) {
                f.call = true;
                scan(t.args);
            }

            @Override public void visitIdent(JCIdent t) {
                if (t.sym == null || t.sym.kind != Kinds.Kind.VAR || isConstant(t.sym)) return;
                if (isLocal(t.sym)) f.readsLocal.add(t.sym);
                else f.readsShared = true;
            }

            @Override public void visitSelect(JCFieldAccess t) {
                if (t.sym != null && t.sym.kind == Kinds.Kind.VAR && !isConstant(t.sym) && !t.name.contentEquals("length")) f.readsShared = true;
                if (t.sym != null && t.sym.kind == Kinds.Kind.VAR && !t.sym.isStatic()) scan(t.selected);
            }

            @Override public void visitIndexed(JCArrayAccess t) {
                f.readsShared = true;
                super.visitIndexed(t);
            }

            @Override public void visitAssign(JCAssign t) {
                target(t.lhs, false);
                scan(t.rhs);
            }

            @Override public void visitAssignop(JCAssignOp t) {
                target(t.lhs, true);
                scan(t.rhs);
            }

            @Override public void visitUnary(JCUnary t) {
                switch (t.getTag()) {
                    case PREINC: case PREDEC: case POSTINC: case POSTDEC:
                        target(t.arg, true);
                        break;
                    default:
                        scan(t.arg);
                }
            }
        }.scan(tree);
        return f;
    }

    /** True when evaluating b before a (instead of Java's a-then-b) could change the result. */
    static boolean conflict(Fx a, Fx b) {
        if (a.pure() && b.pure()) return false;
        if (a.call && (b.readsShared || b.writesShared || b.call)) return true;
        if (b.call && (a.readsShared || a.writesShared)) return true;
        if (a.writesShared && (b.readsShared || b.writesShared)) return true;
        if (b.writesShared && a.readsShared) return true;
        for (Symbol s : a.writesLocal) if (b.readsLocal.contains(s) || b.writesLocal.contains(s)) return true;
        for (Symbol s : b.writesLocal) if (a.readsLocal.contains(s)) return true;
        return false;
    }

    static boolean anyConflict(List<? extends JCTree> parts) {
        List<Fx> fxs = new ArrayList<>();
        for (JCTree p : parts) fxs.add(fx(p));
        for (int i = 0; i < fxs.size(); i++)
            for (int k = i + 1; k < fxs.size(); k++)
                if (conflict(fxs.get(i), fxs.get(k))) return true;
        return false;
    }

    // ---------------------------------------------------------------- expressions

    String initExpr(JCExpression init, Type declared) {
        if (init instanceof JCNewArray && ((JCNewArray) init).elemtype == null) return arrayInit((JCNewArray) init, declared);
        return expr(init);
    }

    String constant(Object v, Type t) {
        switch (t.getTag()) {
            case BOOLEAN: return ((v instanceof Boolean) ? (Boolean) v : ((Number) v).intValue() != 0) ? "true" : "false";
            case CHAR: {
                int c = (v instanceof Character) ? (Character) v : ((Number) v).intValue();
                return "((jchar)" + c + ")";
            }
            case BYTE: return "((jbyte)" + ((Number) v).intValue() + ")";
            case SHORT: return "((jshort)" + ((Number) v).intValue() + ")";
            case INT: {
                int i = ((Number) v).intValue();
                return i == Integer.MIN_VALUE ? "(-2147483647 - 1)" : Integer.toString(i);
            }
            case LONG: {
                long l = ((Number) v).longValue();
                return l == Long.MIN_VALUE ? "(-INT64_C(9223372036854775807) - 1)" : "INT64_C(" + l + ")";
            }
            case CLASS:
                if (v instanceof String) return stringLiteral((String) v);
                throw new IllegalStateException("constant " + v);
            default:
                throw new IllegalStateException("constant type " + t);
        }
    }

    static String stringLiteral(String s) {
        StringBuilder sb = new StringBuilder("jlit(u\"");
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"' || c == '\\') sb.append('\\').append(c);
            else if (c == '\n') sb.append("\\n");
            else if (c == '\r') sb.append("\\r");
            else if (c == '\t') sb.append("\\t");
            else if (c >= 32 && c < 127) sb.append(c);
            else sb.append(String.format("\\u%04x\" u\"", (int) c));
        }
        return sb.append("\")").toString();
    }

    String arrayInit(JCNewArray n, Type arrayType) {
        Type elem = ((Type.ArrayType) arrayType).elemtype;
        StringBuilder sb = new StringBuilder(type(arrayType) + "::of({");
        boolean first = true;
        for (JCExpression e : n.elems) {
            if (!first) sb.append(", ");
            first = false;
            if (e instanceof JCNewArray && ((JCNewArray) e).elemtype == null) sb.append(arrayInit((JCNewArray) e, elem));
            else if (isPrimitive(elem)) sb.append('(').append(type(elem)).append(")(").append(expr(e)).append(')');
            else sb.append(type(elem)).append('(').append(expr(e)).append(')');
        }
        return sb.append("})").toString();
    }

    /** Wraps parts in a lambda that evaluates them strictly left to right. */
    String sequenced(List<String> parts, java.util.function.Function<List<String>, String> build) {
        StringBuilder sb = new StringBuilder("([&]{ ");
        List<String> names = new ArrayList<>();
        for (String p : parts) {
            String n = tmp("t");
            names.add(n);
            sb.append("auto ").append(n).append(" = ").append(p).append("; ");
        }
        return sb.append("return ").append(build.apply(names)).append("; }())").toString();
    }

    String expr(JCExpression e) {
        if (e instanceof JCParens) return "(" + expr(((JCParens) e).expr) + ")";
        if (e instanceof JCLiteral) {
            JCLiteral l = (JCLiteral) e;
            if (l.typetag == TypeTag.BOT) return "nullptr";
            return constant(l.value, l.type);
        }
        if (e instanceof JCIdent) {
            JCIdent id = (JCIdent) e;
            Symbol s = id.sym;
            if (s.kind == Kinds.Kind.TYP) return className(s);
            if (id.name.contentEquals("this")) return "this";
            if (s.kind == Kinds.Kind.VAR) {
                if (isConstant(s)) return constant(((VarSymbol) s).getConstValue(), s.type);
                if (isLocal(s)) return safe(id.name.toString());
                if (s.isStatic()) return className(s.owner) + "::" + safe(id.name.toString());
                return "this->" + safe(id.name.toString());
            }
            throw new IllegalStateException("ident " + e);
        }
        if (e instanceof JCFieldAccess) {
            JCFieldAccess fa = (JCFieldAccess) e;
            Symbol s = fa.sym;
            if (s.kind == Kinds.Kind.TYP) return className(s);
            if (s.kind == Kinds.Kind.VAR) {
                if (isConstant(s)) return constant(((VarSymbol) s).getConstValue(), s.type);
                if (fa.name.contentEquals("length") && fa.selected.type.getTag() == TypeTag.ARRAY) return "(" + expr(fa.selected) + ").length()";
                if (s.isStatic()) return className(s.owner) + "::" + safe(fa.name.toString());
                return "(" + expr(fa.selected) + ")->" + safe(fa.name.toString());
            }
            throw new IllegalStateException("select " + e);
        }
        if (e instanceof JCArrayAccess) {
            JCArrayAccess a = (JCArrayAccess) e;
            if (conflict(fx(a.indexed), fx(a.index)))
                return sequenced(Arrays.asList(expr(a.indexed), expr(a.index)), n -> n.get(0) + "[" + n.get(1) + "]");
            return "(" + expr(a.indexed) + ")[" + expr(a.index) + "]";
        }
        if (e instanceof JCMethodInvocation) return call((JCMethodInvocation) e);
        if (e instanceof JCNewClass) {
            JCNewClass n = (JCNewClass) e;
            List<String> args = new ArrayList<>();
            for (JCExpression a : n.args) args.add(expr(a));
            String cls = className(n.type.tsym);
            if (anyConflict(n.args)) return sequenced(args, names -> "jnew<" + cls + ">(" + String.join(", ", names) + ")");
            return "jnew<" + cls + ">(" + String.join(", ", args) + ")";
        }
        if (e instanceof JCNewArray) {
            JCNewArray n = (JCNewArray) e;
            if (n.elems != null) return arrayInit(n, n.type);
            List<String> dims = new ArrayList<>();
            for (JCExpression d : n.dims) dims.add(expr(d));
            if (anyConflict(n.dims)) return sequenced(dims, names -> "jnewarray<" + type(n.type) + ">(" + String.join(", ", names) + ")");
            return "jnewarray<" + type(n.type) + ">(" + String.join(", ", dims) + ")";
        }
        if (e instanceof JCTypeCast) {
            JCTypeCast c = (JCTypeCast) e;
            if (isPrimitive(c.type)) return "((" + type(c.type) + ")(" + expr(c.expr) + "))";
            return "jcast<" + type(c.type) + ">(" + expr(c.expr) + ")";
        }
        if (e instanceof JCConditional) {
            JCConditional c = (JCConditional) e;
            String t = expr(c.truepart), f = expr(c.falsepart);
            if (!isPrimitive(c.type)) {
                t = type(c.type) + "(" + t + ")";
                f = type(c.type) + "(" + f + ")";
            } else if (c.truepart.type.getTag() != c.falsepart.type.getTag()) {
                t = "(" + type(c.type) + ")(" + t + ")";
                f = "(" + type(c.type) + ")(" + f + ")";
            }
            return "(" + expr(c.cond) + " ? " + t + " : " + f + ")";
        }
        if (e instanceof JCInstanceOf) {
            JCInstanceOf i = (JCInstanceOf) e;
            return "jinstanceof<" + className(((JCExpression) i.pattern).type.tsym) + ">(" + expr(i.expr) + ")";
        }
        if (e instanceof JCUnary) return unary((JCUnary) e);
        if (e instanceof JCBinary) return binary((JCBinary) e);
        if (e instanceof JCAssign) return assign((JCAssign) e);
        if (e instanceof JCAssignOp) return assignOp((JCAssignOp) e);
        throw new IllegalStateException("unsupported expression " + e.getClass() + ": " + e);
    }

    String call(JCMethodInvocation inv) {
        MethodSymbol m = (MethodSymbol) TreeInfo.symbol(inv.meth);
        String name = methodName(m);
        List<JCTree> parts = new ArrayList<>();
        List<String> strs = new ArrayList<>();
        final String prefix;
        boolean hasReceiver = false;
        if (m.isStatic()) {
            prefix = className(m.owner) + "::" + name;
        } else if (inv.meth instanceof JCIdent) {
            prefix = "this->" + name;
        } else {
            JCExpression recv = ((JCFieldAccess) inv.meth).selected;
            if (recv instanceof JCIdent && ((JCIdent) recv).name.contentEquals("this")) {
                prefix = "this->" + name;
            } else if (recv instanceof JCIdent && ((JCIdent) recv).name.contentEquals("super")) {
                prefix = className(m.owner) + "::" + name;
            } else {
                hasReceiver = true;
                parts.add(recv);
                strs.add(expr(recv));
                prefix = "->" + name;
            }
        }
        List<? extends Type> paramTypes = m.type.getParameterTypes();
        int i = 0;
        for (JCExpression a : inv.args) {
            parts.add(a);
            String s = expr(a);
            // keep overload selection identical to javac's when the runtime overloads a name
            Type pt = i < paramTypes.size() ? paramTypes.get(i) : null;
            if (pt != null && !generated(m.owner) && isPrimitive(pt) && a.type.getTag() != pt.getTag()) s = "(" + type(pt) + ")(" + s + ")";
            strs.add(s);
            i++;
        }
        final boolean recv = hasReceiver;
        if (anyConflict(parts)) {
            return sequenced(strs, n -> recv
                ? n.get(0) + prefix + "(" + String.join(", ", n.subList(1, n.size())) + ")"
                : prefix + "(" + String.join(", ", n) + ")");
        }
        return recv
            ? "(" + strs.get(0) + ")" + prefix + "(" + String.join(", ", strs.subList(1, strs.size())) + ")"
            : prefix + "(" + String.join(", ", strs) + ")";
    }

    /** Java int/long +, -, * and negation wrap; plain C++ signed arithmetic would be undefined on overflow. */
    static String wrapBits(Type t) {
        TypeTag tag = t.getTag();
        return tag == TypeTag.LONG ? "64" : tag == TypeTag.INT ? "32" : null;
    }

    String unary(JCUnary u) {
        if (u.type.constValue() != null && isPrimitive(u.type)) return constant(u.type.constValue(), u.type);
        String a = expr(u.arg);
        switch (u.getTag()) {
            case NEG: return wrapBits(u.type) != null ? "jneg" + wrapBits(u.type) + "(" + a + ")" : "(-" + a + ")";
            case POS: return "(+" + a + ")";
            case NOT: return "(!" + a + ")";
            case COMPL: return "(~" + a + ")";
            case PREINC: case PREDEC: case POSTINC: case POSTDEC: {
                String op = (u.getTag() == JCTree.Tag.PREINC || u.getTag() == JCTree.Tag.POSTINC) ? "++" : "--";
                boolean pre = u.getTag() == JCTree.Tag.PREINC || u.getTag() == JCTree.Tag.PREDEC;
                JCExpression arg = TreeInfo.skipParens(u.arg);
                if (arg instanceof JCArrayAccess) {
                    JCArrayAccess aa = (JCArrayAccess) arg;
                    if (conflict(fx(aa.indexed), fx(aa.index)))
                        return sequenced(Arrays.asList(expr(aa.indexed), expr(aa.index)),
                            n -> pre ? op + n.get(0) + "[" + n.get(1) + "]" : n.get(0) + "[" + n.get(1) + "]" + op);
                }
                return pre ? "(" + op + a + ")" : "(" + a + op + ")";
            }
            default:
                throw new IllegalStateException("unary " + u);
        }
    }

    static boolean nonZeroLiteral(JCExpression e) {
        Object v = e.type.constValue();
        if (!(v instanceof Number)) return false;
        long l = ((Number) v).longValue();
        return l != 0 && l != -1;
    }

    /** op applied to already evaluated operand strings, with Java semantics for the given result type. */
    String apply(JCTree.Tag tag, Type result, String a, String b, boolean safeDivisor) {
        boolean wide = result.getTag() == TypeTag.LONG;
        String bits = wide ? "64" : "32";
        String cast = wide ? "(jlong)" : "(jint)";
        switch (tag) {
            case PLUS: return wrapBits(result) != null ? "jadd" + bits + "(" + a + ", " + b + ")" : "(" + a + " + " + b + ")";
            case MINUS: return wrapBits(result) != null ? "jsub" + bits + "(" + a + ", " + b + ")" : "(" + a + " - " + b + ")";
            case MUL: return wrapBits(result) != null ? "jmul" + bits + "(" + a + ", " + b + ")" : "(" + a + " * " + b + ")";
            case DIV: return safeDivisor ? "(" + a + " / " + b + ")" : "jdiv" + bits + "(" + a + ", " + b + ")";
            case MOD: return safeDivisor ? "(" + a + " % " + b + ")" : "jrem" + bits + "(" + a + ", " + b + ")";
            case SL: return "jshl" + bits + "(" + cast + "(" + a + "), (jint)(" + b + "))";
            case SR: return "jshr" + bits + "(" + cast + "(" + a + "), (jint)(" + b + "))";
            case USR: return "jushr" + bits + "(" + cast + "(" + a + "), (jint)(" + b + "))";
            case BITAND: return "(" + a + " & " + b + ")";
            case BITOR: return "(" + a + " | " + b + ")";
            case BITXOR: return "(" + a + " ^ " + b + ")";
            case EQ: return "(" + a + " == " + b + ")";
            case NE: return "(" + a + " != " + b + ")";
            case LT: return "(" + a + " < " + b + ")";
            case GT: return "(" + a + " > " + b + ")";
            case LE: return "(" + a + " <= " + b + ")";
            case GE: return "(" + a + " >= " + b + ")";
            default: throw new IllegalStateException("binary op " + tag);
        }
    }

    String binary(JCBinary b) {
        JCTree.Tag tag = b.getTag();
        if (b.type.constValue() != null && isPrimitive(b.type)) return constant(b.type.constValue(), b.type);
        if (tag == JCTree.Tag.AND) return "(" + expr(b.lhs) + " && " + expr(b.rhs) + ")";
        if (tag == JCTree.Tag.OR) return "(" + expr(b.lhs) + " || " + expr(b.rhs) + ")";
        String l = expr(b.lhs), r = expr(b.rhs);
        boolean needSeq = conflict(fx(b.lhs), fx(b.rhs));
        if (tag == JCTree.Tag.PLUS && isString(b.type)) {
            if (needSeq) return sequenced(Arrays.asList("jstr(" + l + ")", "jstr(" + r + ")"), n -> "jcat(" + n.get(0) + ", " + n.get(1) + ")");
            return "jcat(jstr(" + l + "), jstr(" + r + "))";
        }
        boolean safeDiv = nonZeroLiteral(b.rhs);
        if (needSeq) return sequenced(Arrays.asList(l, r), n -> apply(tag, b.type, n.get(0), n.get(1), safeDiv));
        return apply(tag, b.type, l, r, safeDiv);
    }

    String assign(JCAssign a) {
        JCExpression lhs = TreeInfo.skipParens(a.lhs);
        String r = initExpr(a.rhs, a.lhs.type);
        if (lhs instanceof JCArrayAccess) {
            JCArrayAccess aa = (JCArrayAccess) lhs;
            if (anyConflict(Arrays.asList(aa.indexed, aa.index, a.rhs))) {
                String elem = type(aa.type);
                return sequenced(Arrays.asList(expr(aa.indexed), expr(aa.index), "(" + elem + ")(" + r + ")"),
                    n -> n.get(0) + "[" + n.get(1) + "] = " + n.get(2));
            }
        } else if (lhs instanceof JCFieldAccess && !((JCFieldAccess) lhs).sym.isStatic()) {
            JCFieldAccess fa = (JCFieldAccess) lhs;
            if (conflict(fx(fa.selected), fx(a.rhs))) {
                String t = type(fa.type);
                return sequenced(Arrays.asList(expr(fa.selected), "(" + t + ")(" + r + ")"),
                    n -> n.get(0) + "->" + safe(fa.name.toString()) + " = " + n.get(1));
            }
        }
        return "(" + expr(a.lhs) + " = " + r + ")";
    }

    static JCTree.Tag baseOp(JCTree.Tag asg) {
        switch (asg) {
            case PLUS_ASG: return JCTree.Tag.PLUS;
            case MINUS_ASG: return JCTree.Tag.MINUS;
            case MUL_ASG: return JCTree.Tag.MUL;
            case DIV_ASG: return JCTree.Tag.DIV;
            case MOD_ASG: return JCTree.Tag.MOD;
            case SL_ASG: return JCTree.Tag.SL;
            case SR_ASG: return JCTree.Tag.SR;
            case USR_ASG: return JCTree.Tag.USR;
            case BITAND_ASG: return JCTree.Tag.BITAND;
            case BITOR_ASG: return JCTree.Tag.BITOR;
            case BITXOR_ASG: return JCTree.Tag.BITXOR;
            default: throw new IllegalStateException("assignop " + asg);
        }
    }

    String assignOp(JCAssignOp a) {
        JCTree.Tag op = baseOp(a.getTag());
        JCExpression lhs = TreeInfo.skipParens(a.lhs);
        Type lt = a.lhs.type;
        String r = expr(a.rhs);
        boolean str = isString(lt);
        // Java computes in the binary-promoted type of the operands, then narrows to the target type.
        Type opType = a.operator != null ? a.operator.type.getReturnType() : lt;
        boolean safeDiv = nonZeroLiteral(a.rhs);
        java.util.function.BiFunction<String, String, String> compute = (cur, rhs) ->
            str ? "jcat(jstr(" + cur + "), jstr(" + rhs + "))"
                : "(" + type(lt) + ")(" + apply(op, opType, cur, rhs, safeDiv) + ")";
        if (lhs instanceof JCArrayAccess) {
            JCArrayAccess aa = (JCArrayAccess) lhs;
            Fx target = fx(aa);
            if (anyConflict(Arrays.asList(aa.indexed, aa.index)) || conflict(target, fx(a.rhs)) || !target.pure()) {
                StringBuilder sb = new StringBuilder("([&]{ ");
                String arr = tmp("a"), idx = tmp("i"), old = tmp("o"), rv = tmp("r");
                sb.append("auto ").append(arr).append(" = ").append(expr(aa.indexed)).append("; ");
                sb.append("auto ").append(idx).append(" = ").append(expr(aa.index)).append("; ");
                sb.append("auto ").append(old).append(" = ").append(arr).append('[').append(idx).append("]; ");
                sb.append("auto ").append(rv).append(" = ").append(r).append("; ");
                sb.append("return ").append(arr).append('[').append(idx).append("] = ").append(compute.apply(old, rv)).append("; }())");
                return sb.toString();
            }
            String l = expr(a.lhs);
            return "(" + l + " = " + compute.apply(l, r) + ")";
        }
        if (lhs instanceof JCFieldAccess && !((JCFieldAccess) lhs).sym.isStatic()) {
            JCFieldAccess fa = (JCFieldAccess) lhs;
            Fx sel = fx(fa.selected);
            if (!sel.pure() || conflict(fx(fa), fx(a.rhs))) {
                StringBuilder sb = new StringBuilder("([&]{ ");
                String obj = tmp("s"), old = tmp("o"), rv = tmp("r");
                String field = "->" + safe(fa.name.toString());
                sb.append("auto ").append(obj).append(" = ").append(expr(fa.selected)).append("; ");
                sb.append("auto ").append(old).append(" = ").append(obj).append(field).append("; ");
                sb.append("auto ").append(rv).append(" = ").append(r).append("; ");
                sb.append("return ").append(obj).append(field).append(" = ").append(compute.apply(old, rv)).append("; }())");
                return sb.toString();
            }
            String l = expr(a.lhs);
            return "(" + l + " = " + compute.apply(l, r) + ")";
        }
        // plain variable (local, static field or this.field)
        String l = expr(a.lhs);
        if (conflict(fx(a.lhs), fx(a.rhs))) {
            String old = tmp("o"), rv = tmp("r");
            return "([&]{ auto " + old + " = " + l + "; auto " + rv + " = " + r + "; return " + l + " = " + compute.apply(old, rv) + "; }())";
        }
        return "(" + l + " = " + compute.apply(l, r) + ")";
    }
}
