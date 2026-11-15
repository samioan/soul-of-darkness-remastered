// Minimal Java runtime for the translated Soul of Darkness engine (see tools/j2cpp).
// Reference counted objects/arrays, Java integer semantics, and the few java.* classes the game uses.
#pragma once
#include <cstdint>
#include <cstring>
#include <initializer_list>
#include <string>
#include <type_traits>
#include <utility>
#include <vector>

namespace sod {

typedef std::int32_t jint;
typedef std::int64_t jlong;
typedef std::int16_t jshort;
typedef std::int8_t jbyte;
typedef char16_t jchar;
typedef bool jboolean;

struct String;
struct Class;
template<class T> struct Ref;

// Diagnostic hook: called with the exception kind just before a runtime-raised Java exception is thrown.
extern void (*g_throwHook)(const char* kind);
[[noreturn]] void throwNullPointer();
[[noreturn]] void throwIndex(jint index, jint length);
[[noreturn]] void throwArithmetic();
[[noreturn]] void throwNegativeSize();
[[noreturn]] void throwClassCast();

struct Object {
    int rc = 1; // creation hold, adopted by jnew
    Object() = default;
    Object(const Object&) = delete;
    Object& operator=(const Object&) = delete;
    virtual ~Object() = default;
    virtual jint hashCode();
    virtual jboolean equals(const Ref<Object>& other);
    virtual Ref<String> toString();
};

template<class T> struct Ref {
    Object* p;
    constexpr Ref() : p(nullptr) {}
    constexpr Ref(std::nullptr_t) : p(nullptr) {}
    Ref(T* raw) : p(raw) { if (p) ++p->rc; }
    Ref(const Ref& o) : p(o.p) { if (p) ++p->rc; }
    Ref(Ref&& o) noexcept : p(o.p) { o.p = nullptr; }
    template<class U, class = std::enable_if_t<std::is_convertible<U*, T*>::value>>
    Ref(const Ref<U>& o) : p(o.p) { if (p) ++p->rc; }
    ~Ref() { release(); }
    Ref& operator=(const Ref& o) {
        if (o.p) ++o.p->rc;
        Object* old = p;
        p = o.p;
        if (old && --old->rc == 0) delete old;
        return *this;
    }
    Ref& operator=(Ref&& o) noexcept {
        if (this != &o) {
            Object* old = p;
            p = o.p;
            o.p = nullptr;
            if (old && --old->rc == 0) delete old;
        }
        return *this;
    }
    Ref& operator=(std::nullptr_t) { release(); p = nullptr; return *this; }
    void release() { if (p && --p->rc == 0) delete p; }
    T* get() const { return static_cast<T*>(p); }
    T* operator->() const { if (!p) throwNullPointer(); return static_cast<T*>(p); }
    explicit operator bool() const { return p != nullptr; }
    static Ref adopt(T* raw) { Ref r; r.p = raw; return r; }
};

template<class A, class B> inline bool operator==(const Ref<A>& a, const Ref<B>& b) { return a.p == b.p; }
template<class A, class B> inline bool operator!=(const Ref<A>& a, const Ref<B>& b) { return a.p != b.p; }
template<class A> inline bool operator==(const Ref<A>& a, std::nullptr_t) { return a.p == nullptr; }
template<class A> inline bool operator!=(const Ref<A>& a, std::nullptr_t) { return a.p != nullptr; }
template<class A> inline bool operator==(std::nullptr_t, const Ref<A>& a) { return a.p == nullptr; }
template<class A> inline bool operator!=(std::nullptr_t, const Ref<A>& a) { return a.p != nullptr; }
template<class A> inline bool operator==(const Ref<A>& a, const Object* b) { return a.p == b; }
template<class A> inline bool operator!=(const Ref<A>& a, const Object* b) { return a.p != b; }
template<class A> inline bool operator==(const Object* b, const Ref<A>& a) { return a.p == b; }
template<class A> inline bool operator!=(const Object* b, const Ref<A>& a) { return a.p != b; }

template<class T, class... Args> inline Ref<T> jnew(Args&&... args) {
    return Ref<T>::adopt(new T(std::forward<Args>(args)...));
}

// ---------------------------------------------------------------- arrays

template<class T> struct JArray : Object {
    jint len;
    T* data;
    explicit JArray(jint n) : len(n), data(new T[static_cast<std::size_t>(n)]()) {}
    ~JArray() override { delete[] data; }
};

template<class T> struct Arr {
    typedef T elem_type;
    JArray<T>* p;
    constexpr Arr() : p(nullptr) {}
    constexpr Arr(std::nullptr_t) : p(nullptr) {}
    Arr(const Arr& o) : p(o.p) { if (p) ++p->rc; }
    Arr(Arr&& o) noexcept : p(o.p) { o.p = nullptr; }
    ~Arr() { release(); }
    Arr& operator=(const Arr& o) {
        if (o.p) ++o.p->rc;
        JArray<T>* old = p;
        p = o.p;
        if (old && --old->rc == 0) delete old;
        return *this;
    }
    Arr& operator=(Arr&& o) noexcept {
        if (this != &o) {
            JArray<T>* old = p;
            p = o.p;
            o.p = nullptr;
            if (old && --old->rc == 0) delete old;
        }
        return *this;
    }
    Arr& operator=(std::nullptr_t) { release(); p = nullptr; return *this; }
    void release() { if (p && --p->rc == 0) delete p; }
    static Arr make(jint n) {
        if (n < 0) throwNegativeSize();
        Arr a;
        a.p = new JArray<T>(n);
        return a;
    }
    static Arr of(std::initializer_list<T> items) {
        Arr a = make(static_cast<jint>(items.size()));
        jint i = 0;
        for (const T& item : items) a.p->data[i++] = item;
        return a;
    }
    T& operator[](jint i) const {
        if (!p) throwNullPointer();
        if (static_cast<std::uint32_t>(i) >= static_cast<std::uint32_t>(p->len)) throwIndex(i, p->len);
        return p->data[i];
    }
    jint length() const { if (!p) throwNullPointer(); return p->len; }
    T* raw() const { return p ? p->data : nullptr; }
    explicit operator bool() const { return p != nullptr; }
    operator Ref<Object>() const { return Ref<Object>(static_cast<Object*>(p)); }
};

template<class T> inline bool operator==(const Arr<T>& a, const Arr<T>& b) { return a.p == b.p; }
template<class T> inline bool operator!=(const Arr<T>& a, const Arr<T>& b) { return a.p != b.p; }
template<class T> inline bool operator==(const Arr<T>& a, std::nullptr_t) { return a.p == nullptr; }
template<class T> inline bool operator!=(const Arr<T>& a, std::nullptr_t) { return a.p != nullptr; }
template<class T> inline bool operator==(std::nullptr_t, const Arr<T>& a) { return a.p == nullptr; }
template<class T> inline bool operator!=(std::nullptr_t, const Arr<T>& a) { return a.p != nullptr; }

template<class A> inline A jnewarray(jint n) { return A::make(n); }
template<class A, class... Rest> inline A jnewarray(jint n, jint next, Rest... rest) {
    A a = A::make(n);
    for (jint i = 0; i < n; ++i) a.p->data[i] = jnewarray<typename A::elem_type>(next, rest...);
    return a;
}

// reference casts
template<class T> struct CastTo;
template<class T> struct CastTo<Ref<T>> {
    template<class U> static Ref<T> from(const Ref<U>& v) {
        if (!v.p) return nullptr;
        T* t = dynamic_cast<T*>(v.p);
        if (!t) throwClassCast();
        return Ref<T>(t);
    }
    static Ref<T> from(std::nullptr_t) { return nullptr; }
};
template<class T> struct CastTo<Arr<T>> {
    template<class U> static Arr<T> from(const Ref<U>& v) {
        if (!v.p) return nullptr;
        JArray<T>* t = dynamic_cast<JArray<T>*>(v.p);
        if (!t) throwClassCast();
        Arr<T> a;
        a.p = t;
        ++t->rc;
        return a;
    }
    static Arr<T> from(const Arr<T>& v) { return v; }
    static Arr<T> from(std::nullptr_t) { return nullptr; }
};
template<class To, class From> inline To jcast(const From& v) { return CastTo<To>::from(v); }
template<class T, class U> inline bool jinstanceof(const Ref<U>& v) { return v.p && dynamic_cast<T*>(v.p) != nullptr; }

// ---------------------------------------------------------------- integer semantics

inline jint jdiv32(jint a, jint b) {
    if (b == 0) throwArithmetic();
    if (b == -1) return static_cast<jint>(0u - static_cast<std::uint32_t>(a));
    return a / b;
}
inline jint jrem32(jint a, jint b) {
    if (b == 0) throwArithmetic();
    if (b == -1) return 0;
    return a % b;
}
inline jlong jdiv64(jlong a, jlong b) {
    if (b == 0) throwArithmetic();
    if (b == -1) return static_cast<jlong>(0ull - static_cast<std::uint64_t>(a));
    return a / b;
}
inline jlong jrem64(jlong a, jlong b) {
    if (b == 0) throwArithmetic();
    if (b == -1) return 0;
    return a % b;
}
inline jint jshl32(jint a, jint n) { return static_cast<jint>(static_cast<std::uint32_t>(a) << (n & 31)); }
inline jint jshr32(jint a, jint n) { return a >> (n & 31); }
inline jint jushr32(jint a, jint n) { return static_cast<jint>(static_cast<std::uint32_t>(a) >> (n & 31)); }
inline jlong jshl64(jlong a, jint n) { return static_cast<jlong>(static_cast<std::uint64_t>(a) << (n & 63)); }
inline jlong jshr64(jlong a, jint n) { return a >> (n & 63); }
inline jlong jushr64(jlong a, jint n) { return static_cast<jlong>(static_cast<std::uint64_t>(a) >> (n & 63)); }

// ---------------------------------------------------------------- java.lang

struct String : Object {
    std::u16string s;
    String() = default;
    explicit String(std::u16string v) : s(std::move(v)) {}
    jint length() { return static_cast<jint>(s.size()); }
    jchar charAt(jint i);
    Ref<String> substring(jint begin);
    Ref<String> substring(jint begin, jint end);
    jint indexOf(const Ref<String>& needle);
    jint indexOf(const Ref<String>& needle, jint from);
    jint indexOf(jint ch);
    jint indexOf(jint ch, jint from);
    jint lastIndexOf(jint ch);
    Ref<String> replace(jchar from, jchar to);
    Ref<String> trim();
    Ref<String> toUpperCase();
    Ref<String> toLowerCase();
    jint compareTo(const Ref<String>& other);
    jboolean startsWith(const Ref<String>& prefix);
    jboolean endsWith(const Ref<String>& suffix);
    Arr<jbyte> getBytes();
    Arr<jchar> toCharArray();
    Ref<Class> getClass();
    jint hashCode() override;
    jboolean equals(const Ref<Object>& other) override;
    Ref<String> toString() override;
};

Ref<String> jstr(const char16_t* literal);
Ref<String> jlit(const char16_t* literal); // interned, like Java string literals (the game compares with ==)
Ref<String> jstr(const std::u16string& value);
Ref<String> jstr(const std::string& ascii);
Ref<String> jstr(jint v);
Ref<String> jstr(jlong v);
Ref<String> jstr(jchar v);
Ref<String> jstr(jboolean v);
Ref<String> jstr(std::nullptr_t);
template<class T> inline Ref<String> jstr(const Ref<T>& v);
Ref<String> jcat(const Ref<String>& a, const Ref<String>& b);
std::string jutf8(const Ref<String>& s);

template<class T> inline Ref<String> jstr(const Ref<T>& v) {
    if (!v.p) return jstr(u"null");
    return static_cast<Object*>(v.p)->toString();
}

struct StringBuffer : Object {
    std::u16string s;
    StringBuffer() = default;
    explicit StringBuffer(const Ref<String>& v);
    explicit StringBuffer(jint) {}
    Ref<StringBuffer> append(const Ref<String>& v);
    Ref<StringBuffer> append(jint v);
    Ref<StringBuffer> append(jlong v);
    Ref<StringBuffer> append(jchar v);
    Ref<StringBuffer> append(jboolean v);
    Ref<StringBuffer> insert(jint at, const Ref<String>& v);
    Ref<StringBuffer> insert(jint at, jchar v);
    Ref<StringBuffer> insert(jint at, jint v);
    Ref<StringBuffer> deleteCharAt(jint at);
    Ref<StringBuffer> delete_(jint from, jint to);
    void setLength(jint n);
    jint length() { return static_cast<jint>(s.size()); }
    jchar charAt(jint i);
    Ref<String> toString() override;
};

struct Throwable : Object {
    Ref<String> message;
    const char* kind = "java.lang.Throwable";
    Throwable() = default;
    explicit Throwable(const Ref<String>& m) : message(m) {}
    Ref<String> getMessage() { return message; }
    void printStackTrace();
    Ref<String> toString() override;
};
struct Exception : Throwable {
    Exception() { kind = "java.lang.Exception"; }
    explicit Exception(const Ref<String>& m) : Throwable(m) { kind = "java.lang.Exception"; }
};
struct RuntimeException : Exception {
    RuntimeException() { kind = "java.lang.RuntimeException"; }
    explicit RuntimeException(const Ref<String>& m) : Exception(m) { kind = "java.lang.RuntimeException"; }
};
struct IOException : Exception {
    IOException() { kind = "java.io.IOException"; }
    explicit IOException(const Ref<String>& m) : Exception(m) { kind = "java.io.IOException"; }
};

struct JavaThrow {
    Ref<Throwable> ex;
    template<class T> explicit JavaThrow(const Ref<T>& e) : ex(e) {}
};
template<class T> inline Ref<T> jcaught(JavaThrow& t) { return Ref<T>(static_cast<T*>(t.ex.get())); }
[[noreturn]] void throwJava(const char* kind, const std::string& message);

struct Integer : Object {
    jint value;
    explicit Integer(jint v) : value(v) {}
    jint intValue() { return value; }
    jint hashCode() override { return value; }
    jboolean equals(const Ref<Object>& other) override;
    Ref<String> toString() override { return jstr(value); }
    static jint parseInt(const Ref<String>& s);
    static Ref<String> toString(jint v) { return jstr(v); }
};

struct Math {
    static jint abs(jint v) { return v < 0 ? static_cast<jint>(0u - static_cast<std::uint32_t>(v)) : v; }
    static jlong abs(jlong v) { return v < 0 ? static_cast<jlong>(0ull - static_cast<std::uint64_t>(v)) : v; }
    static jint min(jint a, jint b) { return a < b ? a : b; }
    static jlong min(jlong a, jlong b) { return a < b ? a : b; }
    static jint max(jint a, jint b) { return a > b ? a : b; }
    static jlong max(jlong a, jlong b) { return a > b ? a : b; }
};

struct PrintStream : Object {
    void println(const Ref<String>& s);
    void println(jint v) { println(jstr(v)); }
    void println(jlong v) { println(jstr(v)); }
    void println(jchar v) { println(jstr(v)); }
    void println(jboolean v) { println(jstr(v)); }
    void println() { println(jstr(u"")); }
    void print(const Ref<String>& s);
    void print(jint v) { print(jstr(v)); }
    template<class T> void println(const Ref<T>& v) { println(jstr(v)); }
};

struct System {
    static Ref<PrintStream> out;
    static jlong currentTimeMillis();
    static void gc() {}
    template<class A, class B> static void arraycopy(const A& src, jint srcPos, const B& dst, jint dstPos, jint n) {
        if (!src.p || !dst.p) throwNullPointer();
        if (n < 0 || srcPos < 0 || dstPos < 0 || srcPos > src.p->len - n || dstPos > dst.p->len - n) throwIndex(n, src.p->len);
        auto* s = src.p->data;
        auto* d = dst.p->data;
        if (static_cast<const void*>(s) == static_cast<const void*>(d) && srcPos < dstPos) {
            for (jint i = n - 1; i >= 0; --i) d[dstPos + i] = s[srcPos + i];
        } else {
            for (jint i = 0; i < n; ++i) d[dstPos + i] = s[srcPos + i];
        }
    }
};

struct Runtime : Object {
    static Ref<Runtime> getRuntime();
    jlong freeMemory() { return 64 * 1024 * 1024; }
    jlong totalMemory() { return 128 * 1024 * 1024; }
    void gc() {}
};

struct Thread {
    static void sleep(jlong ms);
    static void yield() {}
};

// ---------------------------------------------------------------- java.util

struct Random : Object {
    std::uint64_t seed = 0;
    Random();
    explicit Random(jlong s) { setSeed(s); }
    void setSeed(jlong s) { seed = (static_cast<std::uint64_t>(s) ^ 0x5DEECE66DULL) & ((1ULL << 48) - 1); }
    jint next(jint bits) {
        seed = (seed * 0x5DEECE66DULL + 0xBULL) & ((1ULL << 48) - 1);
        return static_cast<jint>(static_cast<std::int64_t>(seed) >> (48 - bits));
    }
    jint nextInt() { return next(32); }
    jint nextInt(jint bound);
    jlong nextLong() { jlong hi = next(32); return static_cast<jlong>(static_cast<std::uint64_t>(hi) << 32) + next(32); }
};

struct Hashtable : Object {
    std::vector<std::pair<Ref<Object>, Ref<Object>>> entries;
    Ref<Object> put(const Ref<Object>& key, const Ref<Object>& value);
    Ref<Object> get(const Ref<Object>& key);
    Ref<Object> remove(const Ref<Object>& key);
    jboolean containsKey(const Ref<Object>& key);
    void clear() { entries.clear(); }
    jint size() { return static_cast<jint>(entries.size()); }
    jboolean isEmpty() { return entries.empty(); }
};

struct Vector : Object {
    std::vector<Ref<Object>> items;
    jint size() { return static_cast<jint>(items.size()); }
    jboolean isEmpty() { return items.empty(); }
    Ref<Object> elementAt(jint i);
    Ref<Object> firstElement() { return elementAt(0); }
    Ref<Object> lastElement() { return elementAt(size() - 1); }
    void addElement(const Ref<Object>& o) { items.push_back(o); }
    void insertElementAt(const Ref<Object>& o, jint i);
    void setElementAt(const Ref<Object>& o, jint i);
    void removeElementAt(jint i);
    jboolean removeElement(const Ref<Object>& o);
    void removeAllElements() { items.clear(); }
    jint indexOf(const Ref<Object>& o);
    jboolean contains(const Ref<Object>& o) { return indexOf(o) >= 0; }
};

// ---------------------------------------------------------------- java.io

struct InputStream : Object {
    virtual jint read() = 0;
    virtual jint read(Arr<jbyte> buffer, jint offset, jint length);
    jint read(Arr<jbyte> buffer) { return read(buffer, 0, buffer.length()); }
    virtual jlong skip(jlong n);
    virtual jint available() { return 0; }
    virtual void close() {}
    virtual void reset() {}
    virtual void mark(jint) {}
};

struct ByteArrayInputStream : InputStream {
    Arr<jbyte> buffer;
    jint pos = 0, markPos = 0, count = 0;
    explicit ByteArrayInputStream(const Arr<jbyte>& b) : buffer(b), count(b.length()) {}
    ByteArrayInputStream(const Arr<jbyte>& b, jint offset, jint length);
    jint read() override { return pos < count ? (buffer.p->data[pos++] & 0xff) : -1; }
    jint read(Arr<jbyte> out, jint offset, jint length) override;
    jlong skip(jlong n) override;
    jint available() override { return count - pos; }
    void reset() override { pos = markPos; }
    void mark(jint) override { markPos = pos; }
};

struct DataInputStream : InputStream {
    Ref<InputStream> in;
    explicit DataInputStream(const Ref<InputStream>& i) : in(i) {}
    jint read() override { return in->read(); }
    jint read(Arr<jbyte> out, jint offset, jint length) override { return in->read(out, offset, length); }
    jlong skip(jlong n) override { return in->skip(n); }
    jint available() override { return in->available(); }
    void close() override { in->close(); }
    jint skipBytes(jint n) { return static_cast<jint>(in->skip(n)); }
    jint need();
    jbyte readByte() { return static_cast<jbyte>(need()); }
    jint readUnsignedByte() { return need(); }
    jboolean readBoolean() { return need() != 0; }
    jshort readShort();
    jint readUnsignedShort();
    jchar readChar() { return static_cast<jchar>(readUnsignedShort()); }
    jint readInt();
    jlong readLong();
    Ref<String> readUTF();
    void readFully(Arr<jbyte> out);
    void readFully(Arr<jbyte> out, jint offset, jint length);
};

struct Class : Object {
    Ref<InputStream> getResourceAsStream(const Ref<String>& name);
};

// ---------------------------------------------------------------- state capture

// Order-sensitive fingerprint of game fields; tools/reference_checks/EngineOracle.java computes the
// same values from the original class files by reflection.
struct StateSink {
    static constexpr std::uint64_t kSeed = 1469598103934665603ULL, kPrime = 1099511628211ULL;
    std::uint64_t hash = kSeed;
    std::vector<std::string>* lines = nullptr;
    std::string prefix;
    static std::uint64_t mix(std::uint64_t h, std::uint64_t v) { return (h ^ v) * kPrime; }
    static std::uint64_t value(jint v) { return static_cast<std::uint64_t>(static_cast<std::int64_t>(v)); }
    static std::uint64_t value(jlong v) { return static_cast<std::uint64_t>(v); }
    static std::uint64_t value(jshort v) { return static_cast<std::uint64_t>(static_cast<std::int64_t>(v)); }
    static std::uint64_t value(jbyte v) { return static_cast<std::uint64_t>(static_cast<std::int64_t>(v)); }
    static std::uint64_t value(jchar v) { return static_cast<std::uint64_t>(v); }
    static std::uint64_t value(jboolean v) { return v ? 1 : 0; }
    static std::uint64_t value(const Ref<String>& v) {
        if (!v.p) return 0;
        std::uint64_t h = mix(kSeed, v.get()->s.size());
        for (char16_t c : v.get()->s) h = mix(h, c);
        return h;
    }
    template<class T> static std::uint64_t value(const Ref<T>& v) { return v.p ? 1 : 0; }
    template<class T> static std::uint64_t value(const Arr<T>& v) {
        if (!v.p) return 0;
        std::uint64_t h = mix(kSeed, static_cast<std::uint64_t>(v.p->len));
        for (jint i = 0; i < v.p->len; ++i) h = mix(h, value(v.p->data[i]));
        return h;
    }
    // Dumps also list the elements of small primitive arrays, so a divergence shows which cell differs.
    template<class T> static std::string elements(const T&) { return std::string(); }
    template<class T> static std::string elements(const Arr<T>& v, std::enable_if_t<std::is_arithmetic<T>::value>* = nullptr) {
        if (!v.p || v.p->len > 4096) return std::string();
        std::string s = " [";
        for (jint i = 0; i < v.p->len; ++i) { if (i) s += ','; s += std::to_string(static_cast<long long>(v.p->data[i])); }
        return s + "]";
    }
    template<class T> void put(const char* name, const T& v) {
        std::uint64_t x = value(v);
        hash = mix(hash, x);
        if (lines) lines->push_back(prefix + name + "=" + std::to_string(x) + elements(v));
    }
};

// Host hooks (implemented by the platform layer).
bool hostReadResource(const std::string& name, std::vector<std::uint8_t>& out);
void hostLog(const std::string& line);

} // namespace sod
