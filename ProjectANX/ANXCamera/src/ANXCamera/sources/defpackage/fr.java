package defpackage;

import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.Unsafe;

/* renamed from: fr reason: default package */
final class fr {
    public static final boolean a;
    public static final boolean b;
    static final long c = ((long) O000000o(byte[].class));
    static final boolean d;
    private static final Logger e = Logger.getLogger(fr.class.getName());
    private static final Unsafe f = a();
    private static final Class g = bx.a;
    private static final boolean h = O00000o0(Long.TYPE);
    private static final boolean i = O00000o0(Integer.TYPE);
    private static final fq j;

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0134 A[SYNTHETIC, Splitter:B:25:0x0134] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x02c6  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x02d6  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static {
        boolean z;
        Unsafe unsafe;
        boolean z2;
        Field b2;
        fq fqVar = null;
        if (f != null) {
            if (!bx.a()) {
                fqVar = new fp(f);
            } else if (h) {
                fqVar = new fo(f);
            } else if (i) {
                fqVar = new fn(f);
            }
        }
        j = fqVar;
        Unsafe unsafe2 = f;
        String str = "putLong";
        String str2 = "putInt";
        String str3 = "putByte";
        String str4 = "getInt";
        String str5 = "getByte";
        String str6 = "objectFieldOffset";
        String str7 = "com.google.protobuf.UnsafeUtil";
        String str8 = "platform method missing - proto runtime falling back to safer methods: ";
        String str9 = "getLong";
        boolean z3 = true;
        if (unsafe2 != null) {
            try {
                Class cls = unsafe2.getClass();
                cls.getMethod(str6, new Class[]{Field.class});
                cls.getMethod(str9, new Class[]{Object.class, Long.TYPE});
                if (b() != null) {
                    if (!bx.a()) {
                        cls.getMethod(str5, new Class[]{Long.TYPE});
                        cls.getMethod(str3, new Class[]{Long.TYPE, Byte.TYPE});
                        cls.getMethod(str4, new Class[]{Long.TYPE});
                        cls.getMethod(str2, new Class[]{Long.TYPE, Integer.TYPE});
                        cls.getMethod(str9, new Class[]{Long.TYPE});
                        cls.getMethod(str, new Class[]{Long.TYPE, Long.TYPE});
                        cls.getMethod("copyMemory", new Class[]{Long.TYPE, Long.TYPE, Long.TYPE});
                        cls.getMethod("copyMemory", new Class[]{Object.class, Long.TYPE, Object.class, Long.TYPE, Long.TYPE});
                    }
                    z = true;
                    a = z;
                    unsafe = f;
                    if (unsafe != null) {
                        try {
                            Class cls2 = unsafe.getClass();
                            cls2.getMethod(str6, new Class[]{Field.class});
                            cls2.getMethod("arrayBaseOffset", new Class[]{Class.class});
                            cls2.getMethod("arrayIndexScale", new Class[]{Class.class});
                            cls2.getMethod(str4, new Class[]{Object.class, Long.TYPE});
                            cls2.getMethod(str2, new Class[]{Object.class, Long.TYPE, Integer.TYPE});
                            cls2.getMethod(str9, new Class[]{Object.class, Long.TYPE});
                            cls2.getMethod(str, new Class[]{Object.class, Long.TYPE, Long.TYPE});
                            cls2.getMethod("getObject", new Class[]{Object.class, Long.TYPE});
                            cls2.getMethod("putObject", new Class[]{Object.class, Long.TYPE, Object.class});
                            if (!bx.a()) {
                                cls2.getMethod(str5, new Class[]{Object.class, Long.TYPE});
                                cls2.getMethod(str3, new Class[]{Object.class, Long.TYPE, Byte.TYPE});
                                cls2.getMethod("getBoolean", new Class[]{Object.class, Long.TYPE});
                                cls2.getMethod("putBoolean", new Class[]{Object.class, Long.TYPE, Boolean.TYPE});
                                cls2.getMethod("getFloat", new Class[]{Object.class, Long.TYPE});
                                cls2.getMethod("putFloat", new Class[]{Object.class, Long.TYPE, Float.TYPE});
                                cls2.getMethod("getDouble", new Class[]{Object.class, Long.TYPE});
                                cls2.getMethod("putDouble", new Class[]{Object.class, Long.TYPE, Double.TYPE});
                            }
                            z2 = true;
                        } catch (Throwable th) {
                            Logger logger = e;
                            Level level = Level.WARNING;
                            String valueOf = String.valueOf(th);
                            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 71);
                            sb.append(str8);
                            sb.append(valueOf);
                            logger.logp(level, str7, "supportsUnsafeArrayOperations", sb.toString());
                        }
                        b = z2;
                        O000000o(boolean[].class);
                        O00000o(boolean[].class);
                        O000000o(int[].class);
                        O00000o(int[].class);
                        O000000o(long[].class);
                        O00000o(long[].class);
                        O000000o(float[].class);
                        O00000o(float[].class);
                        O000000o(double[].class);
                        O00000o(double[].class);
                        O000000o(Object[].class);
                        O00000o(Object[].class);
                        b2 = b();
                        if (b2 != null) {
                            fq fqVar2 = j;
                            if (fqVar2 != null) {
                                fqVar2.O000000o(b2);
                            }
                        }
                        if (ByteOrder.nativeOrder() != ByteOrder.BIG_ENDIAN) {
                            z3 = false;
                        }
                        d = z3;
                    }
                    z2 = false;
                    b = z2;
                    O000000o(boolean[].class);
                    O00000o(boolean[].class);
                    O000000o(int[].class);
                    O00000o(int[].class);
                    O000000o(long[].class);
                    O00000o(long[].class);
                    O000000o(float[].class);
                    O00000o(float[].class);
                    O000000o(double[].class);
                    O00000o(double[].class);
                    O000000o(Object[].class);
                    O00000o(Object[].class);
                    b2 = b();
                    if (b2 != null) {
                    }
                    if (ByteOrder.nativeOrder() != ByteOrder.BIG_ENDIAN) {
                    }
                    d = z3;
                }
            } catch (Throwable th2) {
                Logger logger2 = e;
                Level level2 = Level.WARNING;
                String valueOf2 = String.valueOf(th2);
                StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf2).length() + 71);
                sb2.append(str8);
                sb2.append(valueOf2);
                logger2.logp(level2, str7, "supportsUnsafeByteBufferOperations", sb2.toString());
            }
        }
        z = false;
        a = z;
        unsafe = f;
        if (unsafe != null) {
        }
        z2 = false;
        b = z2;
        O000000o(boolean[].class);
        O00000o(boolean[].class);
        O000000o(int[].class);
        O00000o(int[].class);
        O000000o(long[].class);
        O00000o(long[].class);
        O000000o(float[].class);
        O00000o(float[].class);
        O000000o(double[].class);
        O00000o(double[].class);
        O000000o(Object[].class);
        O00000o(Object[].class);
        b2 = b();
        if (b2 != null) {
        }
        if (ByteOrder.nativeOrder() != ByteOrder.BIG_ENDIAN) {
        }
        d = z3;
    }

    private fr() {
    }

    static byte O000000o(byte[] bArr, long j2) {
        return j.O000000o(bArr, c + j2);
    }

    private static int O000000o(Class cls) {
        if (b) {
            return j.O00000Oo(cls);
        }
        return -1;
    }

    static int O000000o(Object obj, long j2) {
        return j.O00000oO(obj, j2);
    }

    public static void O000000o(Object obj, long j2, byte b2) {
        int i2 = ((~((int) j2)) & 3) << 3;
        long j3 = j2 & -4;
        O000000o(obj, j3, ((b2 & -1) << i2) | (O000000o(obj, j3) & (~(255 << i2))));
    }

    static void O000000o(Object obj, long j2, double d2) {
        j.O000000o(obj, j2, d2);
    }

    static void O000000o(Object obj, long j2, float f2) {
        j.O000000o(obj, j2, f2);
    }

    static void O000000o(Object obj, long j2, int i2) {
        j.O000000o(obj, j2, i2);
    }

    static void O000000o(Object obj, long j2, long j3) {
        j.O000000o(obj, j2, j3);
    }

    static void O000000o(Object obj, long j2, Object obj2) {
        j.O000000o(obj, j2, obj2);
    }

    static void O000000o(Object obj, long j2, boolean z) {
        j.O000000o(obj, j2, z);
    }

    static void O000000o(byte[] bArr, long j2, byte b2) {
        j.O000000o((Object) bArr, c + j2, b2);
    }

    static long O00000Oo(Object obj, long j2) {
        return j.O00000oo(obj, j2);
    }

    static Object O00000Oo(Class cls) {
        try {
            return f.allocateInstance(cls);
        } catch (InstantiationException e2) {
            throw new IllegalStateException(e2);
        }
    }

    public static void O00000Oo(Object obj, long j2, byte b2) {
        int i2 = (((int) j2) & 3) << 3;
        long j3 = j2 & -4;
        O000000o(obj, j3, ((b2 & -1) << i2) | (O000000o(obj, j3) & (~(255 << i2))));
    }

    public static void O00000Oo(Object obj, long j2, boolean z) {
        O000000o(obj, j2, z ? (byte) 1 : 0);
    }

    static float O00000o(Object obj, long j2) {
        return j.O00000o0(obj, j2);
    }

    private static void O00000o(Class cls) {
        if (b) {
            j.O000000o(cls);
        }
    }

    public static void O00000o0(Object obj, long j2, boolean z) {
        O00000Oo(obj, j2, z ? (byte) 1 : 0);
    }

    private static boolean O00000o0(Class cls) {
        if (bx.a()) {
            try {
                Class cls2 = g;
                cls2.getMethod("peekLong", new Class[]{cls, Boolean.TYPE});
                cls2.getMethod("pokeLong", new Class[]{cls, Long.TYPE, Boolean.TYPE});
                cls2.getMethod("pokeInt", new Class[]{cls, Integer.TYPE, Boolean.TYPE});
                cls2.getMethod("peekInt", new Class[]{cls, Boolean.TYPE});
                cls2.getMethod("pokeByte", new Class[]{cls, Byte.TYPE});
                cls2.getMethod("peekByte", new Class[]{cls});
                cls2.getMethod("pokeByteArray", new Class[]{cls, byte[].class, Integer.TYPE, Integer.TYPE});
                cls2.getMethod("peekByteArray", new Class[]{cls, byte[].class, Integer.TYPE, Integer.TYPE});
                return true;
            } catch (Throwable unused) {
            }
        }
        return false;
    }

    static boolean O00000o0(Object obj, long j2) {
        return j.O00000Oo(obj, j2);
    }

    static double O00000oO(Object obj, long j2) {
        return j.O00000o(obj, j2);
    }

    static Object O00000oo(Object obj, long j2) {
        return j.O0000O0o(obj, j2);
    }

    public static byte O0000O0o(Object obj, long j2) {
        return (byte) ((O000000o(obj, -4 & j2) >>> ((int) (((~j2) & 3) << 3))) & 255);
    }

    public static byte O0000OOo(Object obj, long j2) {
        return (byte) ((O000000o(obj, -4 & j2) >>> ((int) ((j2 & 3) << 3))) & 255);
    }

    public static boolean O0000Oo(Object obj, long j2) {
        return O0000OOo(obj, j2) != 0;
    }

    public static boolean O0000Oo0(Object obj, long j2) {
        return O0000O0o(obj, j2) != 0;
    }

    private static Field a(Class cls, String str) {
        try {
            return cls.getDeclaredField(str);
        } catch (Throwable unused) {
            return null;
        }
    }

    static Unsafe a() {
        try {
            return (Unsafe) AccessController.doPrivileged(new fm());
        } catch (Throwable unused) {
            return null;
        }
    }

    private static Field b() {
        if (bx.a()) {
            Field a2 = a(Buffer.class, "effectiveDirectAddress");
            if (a2 != null) {
                return a2;
            }
        }
        Field a3 = a(Buffer.class, "address");
        if (a3 == null || a3.getType() != Long.TYPE) {
            return null;
        }
        return a3;
    }
}
