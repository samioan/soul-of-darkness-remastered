#include "jrt.h"
#include <chrono>
#include <map>
#include <thread>

namespace sod {

void (*g_throwHook)(const char* kind) = nullptr;

void throwJava(const char* kind, const std::string& message) {
    if (g_throwHook) g_throwHook(kind);
    Ref<RuntimeException> e = jnew<RuntimeException>(jstr(message));
    e->kind = kind;
    throw JavaThrow(e);
}
void throwNullPointer() { throwJava("java.lang.NullPointerException", ""); }
void throwIndex(jint index, jint length) {
    throwJava("java.lang.ArrayIndexOutOfBoundsException", std::to_string(index) + " (length " + std::to_string(length) + ")");
}
void throwArithmetic() { throwJava("java.lang.ArithmeticException", "/ by zero"); }
void throwNegativeSize() { throwJava("java.lang.NegativeArraySizeException", ""); }
void throwClassCast() { throwJava("java.lang.ClassCastException", ""); }

jint Object::hashCode() { return static_cast<jint>(reinterpret_cast<std::uintptr_t>(this) >> 4); }
jboolean Object::equals(const Ref<Object>& other) { return other.p == this; }
Ref<String> Object::toString() { return jstr(u"Object"); }

// ---------------------------------------------------------------- String

Ref<String> jstr(const char16_t* literal) { return jnew<String>(std::u16string(literal)); }
Ref<String> jstr(const std::u16string& value) { return jnew<String>(value); }
Ref<String> jlit(const char16_t* literal) {
    static std::map<std::u16string, Ref<String>> pool;
    std::u16string key(literal);
    auto it = pool.find(key);
    if (it != pool.end()) return it->second;
    Ref<String> s = jnew<String>(key);
    pool.emplace(std::move(key), s);
    return s;
}
Ref<String> jstr(const std::string& ascii) {
    std::u16string s;
    for (unsigned char c : ascii) s.push_back(static_cast<char16_t>(c));
    return jnew<String>(std::move(s));
}
Ref<String> jstr(jint v) { return jstr(std::to_string(v)); }
Ref<String> jstr(jlong v) { return jstr(std::to_string(v)); }
Ref<String> jstr(jchar v) { return jnew<String>(std::u16string(1, v)); }
Ref<String> jstr(jboolean v) { return jstr(v ? u"true" : u"false"); }
Ref<String> jstr(std::nullptr_t) { return jstr(u"null"); }
Ref<String> jcat(const Ref<String>& a, const Ref<String>& b) {
    static const std::u16string null = u"null";
    return jnew<String>((a.p ? a.get()->s : null) + (b.p ? b.get()->s : null));
}
std::string jutf8(const Ref<String>& s) {
    std::string out;
    if (!s.p) return "null";
    for (char16_t c : s.get()->s) {
        if (c < 0x80) out.push_back(static_cast<char>(c));
        else if (c < 0x800) { out.push_back(static_cast<char>(0xc0 | (c >> 6))); out.push_back(static_cast<char>(0x80 | (c & 0x3f))); }
        else { out.push_back(static_cast<char>(0xe0 | (c >> 12))); out.push_back(static_cast<char>(0x80 | ((c >> 6) & 0x3f))); out.push_back(static_cast<char>(0x80 | (c & 0x3f))); }
    }
    return out;
}

jchar String::charAt(jint i) {
    if (i < 0 || i >= length()) throwJava("java.lang.StringIndexOutOfBoundsException", std::to_string(i));
    return s[static_cast<std::size_t>(i)];
}
Ref<String> String::substring(jint begin) { return substring(begin, length()); }
Ref<String> String::substring(jint begin, jint end) {
    if (begin < 0 || end > length() || begin > end) throwJava("java.lang.StringIndexOutOfBoundsException", std::to_string(begin));
    return jnew<String>(s.substr(static_cast<std::size_t>(begin), static_cast<std::size_t>(end - begin)));
}
jint String::indexOf(const Ref<String>& needle) { return indexOf(needle, 0); }
jint String::indexOf(const Ref<String>& needle, jint from) {
    if (!needle.p) throwNullPointer();
    if (from < 0) from = 0;
    if (from > length()) return needle.get()->s.empty() ? length() : -1;
    auto at = s.find(needle.get()->s, static_cast<std::size_t>(from));
    return at == std::u16string::npos ? -1 : static_cast<jint>(at);
}
jint String::indexOf(jint ch) { return indexOf(ch, 0); }
jint String::indexOf(jint ch, jint from) {
    if (from < 0) from = 0;
    for (jint i = from; i < length(); ++i) if (s[static_cast<std::size_t>(i)] == static_cast<char16_t>(ch)) return i;
    return -1;
}
jint String::lastIndexOf(jint ch) {
    for (jint i = length() - 1; i >= 0; --i) if (s[static_cast<std::size_t>(i)] == static_cast<char16_t>(ch)) return i;
    return -1;
}
Ref<String> String::replace(jchar from, jchar to) {
    std::u16string r = s;
    for (auto& c : r) if (c == from) c = to;
    return jnew<String>(std::move(r));
}
Ref<String> String::trim() {
    std::size_t a = 0, b = s.size();
    while (a < b && s[a] <= u' ') ++a;
    while (b > a && s[b - 1] <= u' ') --b;
    return jnew<String>(s.substr(a, b - a));
}
static char16_t upperChar(char16_t c) {
    if (c >= u'a' && c <= u'z') return static_cast<char16_t>(c - 32);
    if (c == 0x00b5) return 0x039c;
    if (c >= 0x00e0 && c <= 0x00fe && c != 0x00f7) return static_cast<char16_t>(c - 32);
    if (c == 0x00ff) return 0x0178;
    if (c >= 0x0100 && c <= 0x017f) {
        if (c == 0x0131) return u'I';
        if (c == 0x017f) return u'S';
        if ((c >= 0x0139 && c <= 0x0148) || (c >= 0x0179 && c <= 0x017e)) return (c & 1) ? c : static_cast<char16_t>(c - 1);
        if (c == 0x0130 || c == 0x0138 || c == 0x0149 || c == 0x0178) return c;
        return (c & 1) ? static_cast<char16_t>(c - 1) : c;
    }
    if (c >= 0x0430 && c <= 0x044f) return static_cast<char16_t>(c - 32);
    if (c >= 0x0450 && c <= 0x045f) return static_cast<char16_t>(c - 80);
    if (c >= 0x03b1 && c <= 0x03c9 && c != 0x03c2) return static_cast<char16_t>(c - 32);
    return c;
}
static char16_t lowerChar(char16_t c) {
    if (c >= u'A' && c <= u'Z') return static_cast<char16_t>(c + 32);
    if (c >= 0x00c0 && c <= 0x00de && c != 0x00d7) return static_cast<char16_t>(c + 32);
    return c;
}
Ref<String> String::toUpperCase() {
    std::u16string r;
    for (char16_t c : s) {
        if (c == 0x00df) { r += u"SS"; continue; }
        r.push_back(upperChar(c));
    }
    return jnew<String>(std::move(r));
}
Ref<String> String::toLowerCase() {
    std::u16string r = s;
    for (auto& c : r) c = lowerChar(c);
    return jnew<String>(std::move(r));
}
jint String::compareTo(const Ref<String>& other) {
    if (!other.p) throwNullPointer();
    const std::u16string& o = other.get()->s;
    std::size_t n = s.size() < o.size() ? s.size() : o.size();
    for (std::size_t i = 0; i < n; ++i) if (s[i] != o[i]) return static_cast<jint>(s[i]) - static_cast<jint>(o[i]);
    return static_cast<jint>(s.size()) - static_cast<jint>(o.size());
}
jboolean String::startsWith(const Ref<String>& prefix) {
    if (!prefix.p) throwNullPointer();
    return s.compare(0, prefix.get()->s.size(), prefix.get()->s) == 0;
}
jboolean String::endsWith(const Ref<String>& suffix) {
    if (!suffix.p) throwNullPointer();
    const auto& o = suffix.get()->s;
    return s.size() >= o.size() && s.compare(s.size() - o.size(), o.size(), o) == 0;
}
Arr<jbyte> String::getBytes() {
    Arr<jbyte> a = Arr<jbyte>::make(length());
    for (jint i = 0; i < length(); ++i) a[i] = static_cast<jbyte>(s[static_cast<std::size_t>(i)]);
    return a;
}
Arr<jchar> String::toCharArray() {
    Arr<jchar> a = Arr<jchar>::make(length());
    for (jint i = 0; i < length(); ++i) a[i] = s[static_cast<std::size_t>(i)];
    return a;
}
Ref<Class> String::getClass() { return jnew<Class>(); }
jint String::hashCode() {
    jint h = 0;
    for (char16_t c : s) h = static_cast<jint>(31u * static_cast<std::uint32_t>(h) + c);
    return h;
}
jboolean String::equals(const Ref<Object>& other) {
    String* o = dynamic_cast<String*>(other.p);
    return o && o->s == s;
}
Ref<String> String::toString() { return Ref<String>(this); }

StringBuffer::StringBuffer(const Ref<String>& v) { if (!v.p) throwNullPointer(); s = v.get()->s; }
Ref<StringBuffer> StringBuffer::append(const Ref<String>& v) { s += v.p ? v.get()->s : std::u16string(u"null"); return Ref<StringBuffer>(this); }
Ref<StringBuffer> StringBuffer::append(jint v) { return append(jstr(v)); }
Ref<StringBuffer> StringBuffer::append(jlong v) { return append(jstr(v)); }
Ref<StringBuffer> StringBuffer::append(jchar v) { s.push_back(v); return Ref<StringBuffer>(this); }
Ref<StringBuffer> StringBuffer::append(jboolean v) { return append(jstr(v)); }
Ref<StringBuffer> StringBuffer::insert(jint at, const Ref<String>& v) {
    if (at < 0 || at > length()) throwJava("java.lang.StringIndexOutOfBoundsException", std::to_string(at));
    s.insert(static_cast<std::size_t>(at), v.p ? v.get()->s : std::u16string(u"null"));
    return Ref<StringBuffer>(this);
}
Ref<StringBuffer> StringBuffer::insert(jint at, jchar v) { return insert(at, jstr(v)); }
Ref<StringBuffer> StringBuffer::insert(jint at, jint v) { return insert(at, jstr(v)); }
Ref<StringBuffer> StringBuffer::deleteCharAt(jint at) {
    if (at < 0 || at >= length()) throwJava("java.lang.StringIndexOutOfBoundsException", std::to_string(at));
    s.erase(static_cast<std::size_t>(at), 1);
    return Ref<StringBuffer>(this);
}
Ref<StringBuffer> StringBuffer::delete_(jint from, jint to) {
    if (to > length()) to = length();
    if (from < 0 || from > to) throwJava("java.lang.StringIndexOutOfBoundsException", std::to_string(from));
    s.erase(static_cast<std::size_t>(from), static_cast<std::size_t>(to - from));
    return Ref<StringBuffer>(this);
}
void StringBuffer::setLength(jint n) { s.resize(static_cast<std::size_t>(n < 0 ? 0 : n)); }
jchar StringBuffer::charAt(jint i) {
    if (i < 0 || i >= length()) throwJava("java.lang.StringIndexOutOfBoundsException", std::to_string(i));
    return s[static_cast<std::size_t>(i)];
}
Ref<String> StringBuffer::toString() { return jnew<String>(s); }

// ---------------------------------------------------------------- Throwable and friends

void Throwable::printStackTrace() { hostLog("exception: " + jutf8(toString())); }
Ref<String> Throwable::toString() {
    std::string text = kind;
    if (message.p && !message.get()->s.empty()) text += ": " + jutf8(message);
    return jstr(text);
}

jboolean Integer::equals(const Ref<Object>& other) {
    Integer* o = dynamic_cast<Integer*>(other.p);
    return o && o->value == value;
}
jint Integer::parseInt(const Ref<String>& s) {
    if (!s.p || s.get()->s.empty()) throwJava("java.lang.NumberFormatException", "");
    const std::u16string& t = s.get()->s;
    std::size_t i = 0;
    bool negative = false;
    if (t[0] == u'-') { negative = true; i = 1; }
    if (i >= t.size()) throwJava("java.lang.NumberFormatException", "");
    std::int64_t v = 0;
    for (; i < t.size(); ++i) {
        if (t[i] < u'0' || t[i] > u'9') throwJava("java.lang.NumberFormatException", jutf8(s));
        v = v * 10 + (t[i] - u'0');
        if (v > 2147483648LL) throwJava("java.lang.NumberFormatException", jutf8(s));
    }
    if (negative) v = -v;
    if (v > 2147483647LL) throwJava("java.lang.NumberFormatException", jutf8(s));
    return static_cast<jint>(v);
}

Ref<PrintStream> System::out = jnew<PrintStream>();
void PrintStream::println(const Ref<String>& s) { hostLog(jutf8(s)); }
void PrintStream::print(const Ref<String>& s) { hostLog(jutf8(s)); }

jlong System::currentTimeMillis() {
    using namespace std::chrono;
    return duration_cast<milliseconds>(system_clock::now().time_since_epoch()).count();
}
Ref<Runtime> Runtime::getRuntime() { return jnew<Runtime>(); }
void Thread::sleep(jlong ms) { if (ms > 0) std::this_thread::sleep_for(std::chrono::milliseconds(ms)); }

Random::Random() { setSeed(System::currentTimeMillis()); }
jint Random::nextInt(jint bound) {
    if (bound <= 0) throwJava("java.lang.IllegalArgumentException", "n must be positive");
    if ((bound & -bound) == bound) return static_cast<jint>((static_cast<jlong>(bound) * static_cast<jlong>(next(31))) >> 31);
    jint bits, val;
    do {
        bits = next(31);
        val = bits % bound;
    } while (static_cast<jint>(static_cast<std::uint32_t>(bits) - static_cast<std::uint32_t>(val) + static_cast<std::uint32_t>(bound - 1)) < 0);
    return val;
}

Ref<Object> Hashtable::put(const Ref<Object>& key, const Ref<Object>& value) {
    if (!key.p || !value.p) throwNullPointer();
    for (auto& e : entries)
        if (e.first->equals(key)) {
            Ref<Object> old = e.second;
            e.second = value;
            return old;
        }
    entries.emplace_back(key, value);
    return nullptr;
}
Ref<Object> Hashtable::get(const Ref<Object>& key) {
    if (!key.p) throwNullPointer();
    for (auto& e : entries) if (e.first->equals(key)) return e.second;
    return nullptr;
}
Ref<Object> Hashtable::remove(const Ref<Object>& key) {
    if (!key.p) throwNullPointer();
    for (std::size_t i = 0; i < entries.size(); ++i)
        if (entries[i].first->equals(key)) {
            Ref<Object> old = entries[i].second;
            entries.erase(entries.begin() + static_cast<std::ptrdiff_t>(i));
            return old;
        }
    return nullptr;
}
jboolean Hashtable::containsKey(const Ref<Object>& key) { return get(key).p != nullptr; }

Ref<Object> Vector::elementAt(jint i) {
    if (i < 0 || i >= size()) throwIndex(i, size());
    return items[static_cast<std::size_t>(i)];
}
void Vector::insertElementAt(const Ref<Object>& o, jint i) {
    if (i < 0 || i > size()) throwIndex(i, size());
    items.insert(items.begin() + i, o);
}
void Vector::setElementAt(const Ref<Object>& o, jint i) {
    if (i < 0 || i >= size()) throwIndex(i, size());
    items[static_cast<std::size_t>(i)] = o;
}
void Vector::removeElementAt(jint i) {
    if (i < 0 || i >= size()) throwIndex(i, size());
    items.erase(items.begin() + i);
}
jint Vector::indexOf(const Ref<Object>& o) {
    for (jint i = 0; i < size(); ++i) {
        const Ref<Object>& e = items[static_cast<std::size_t>(i)];
        if (o.p == nullptr ? e.p == nullptr : (e.p != nullptr && o.get()->equals(e))) return i;
    }
    return -1;
}
jboolean Vector::removeElement(const Ref<Object>& o) {
    jint i = indexOf(o);
    if (i < 0) return false;
    removeElementAt(i);
    return true;
}

// ---------------------------------------------------------------- streams

jint InputStream::read(Arr<jbyte> buffer, jint offset, jint length) {
    if (!buffer.p) throwNullPointer();
    if (offset < 0 || length < 0 || length > buffer.length() - offset) throwIndex(offset, buffer.length());
    if (length == 0) return 0;
    jint n = 0;
    for (; n < length; ++n) {
        jint b = read();
        if (b < 0) break;
        buffer.p->data[offset + n] = static_cast<jbyte>(b);
    }
    return n == 0 ? -1 : n;
}
jlong InputStream::skip(jlong n) {
    jlong done = 0;
    while (done < n && read() >= 0) ++done;
    return done;
}
ByteArrayInputStream::ByteArrayInputStream(const Arr<jbyte>& b, jint offset, jint length)
    : buffer(b), pos(offset), markPos(offset), count(offset + length < b.length() ? offset + length : b.length()) {}
jint ByteArrayInputStream::read(Arr<jbyte> out, jint offset, jint length) {
    if (!out.p) throwNullPointer();
    if (offset < 0 || length < 0 || length > out.length() - offset) throwIndex(offset, out.length());
    if (pos >= count) return -1;
    if (length > count - pos) length = count - pos;
    if (length <= 0) return 0;
    std::memcpy(out.p->data + offset, buffer.p->data + pos, static_cast<std::size_t>(length));
    pos += length;
    return length;
}
jlong ByteArrayInputStream::skip(jlong n) {
    jlong k = count - pos;
    if (n < k) k = n < 0 ? 0 : n;
    pos += static_cast<jint>(k);
    return k;
}
jint DataInputStream::need() {
    jint b = in->read();
    if (b < 0) throw JavaThrow(([] { Ref<IOException> e = jnew<IOException>(jstr(u"EOF")); e->kind = "java.io.EOFException"; return e; })());
    return b;
}
jshort DataInputStream::readShort() { jint a = need(); jint b = need(); return static_cast<jshort>((a << 8) | b); }
jint DataInputStream::readUnsignedShort() { jint a = need(); jint b = need(); return (a << 8) | b; }
jint DataInputStream::readInt() {
    jint a = need(); jint b = need(); jint c = need(); jint d = need();
    return static_cast<jint>((static_cast<std::uint32_t>(a) << 24) | (b << 16) | (c << 8) | d);
}
jlong DataInputStream::readLong() {
    jlong hi = readInt();
    jlong lo = readInt() & 0xffffffffLL;
    return static_cast<jlong>(static_cast<std::uint64_t>(hi) << 32) | lo;
}
void DataInputStream::readFully(Arr<jbyte> out) { readFully(out, 0, out.length()); }
void DataInputStream::readFully(Arr<jbyte> out, jint offset, jint length) {
    if (!out.p) throwNullPointer();
    if (offset < 0 || length < 0 || length > out.length() - offset) throwIndex(offset, out.length());
    for (jint i = 0; i < length; ++i) out.p->data[offset + i] = static_cast<jbyte>(need());
}
Ref<String> DataInputStream::readUTF() {
    jint n = readUnsignedShort();
    std::vector<std::uint8_t> bytes(static_cast<std::size_t>(n));
    for (jint i = 0; i < n; ++i) bytes[static_cast<std::size_t>(i)] = static_cast<std::uint8_t>(need());
    std::u16string s;
    for (std::size_t i = 0; i < bytes.size();) {
        std::uint8_t c = bytes[i];
        if (c < 0x80) { s.push_back(c); i += 1; }
        else if ((c & 0xe0) == 0xc0 && i + 1 < bytes.size()) { s.push_back(static_cast<char16_t>(((c & 0x1f) << 6) | (bytes[i + 1] & 0x3f))); i += 2; }
        else if ((c & 0xf0) == 0xe0 && i + 2 < bytes.size()) { s.push_back(static_cast<char16_t>(((c & 0x0f) << 12) | ((bytes[i + 1] & 0x3f) << 6) | (bytes[i + 2] & 0x3f))); i += 3; }
        else throwJava("java.io.UTFDataFormatException", "");
    }
    return jnew<String>(std::move(s));
}

Ref<InputStream> Class::getResourceAsStream(const Ref<String>& name) {
    if (!name.p) throwNullPointer();
    std::string path = jutf8(name);
    while (!path.empty() && path[0] == '/') path.erase(0, 1);
    std::vector<std::uint8_t> bytes;
    if (!hostReadResource(path, bytes)) return nullptr;
    Arr<jbyte> data = Arr<jbyte>::make(static_cast<jint>(bytes.size()));
    if (!bytes.empty()) std::memcpy(data.p->data, bytes.data(), bytes.size());
    return jnew<ByteArrayInputStream>(data);
}

} // namespace sod
