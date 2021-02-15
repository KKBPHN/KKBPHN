package defpackage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* renamed from: de reason: default package */
public abstract class de extends bu {
    public static final Map j = new ConcurrentHashMap();
    protected fi h = fi.a;
    protected int i = -1;

    public static cq O000000o(cq cqVar) {
        return cqVar;
    }

    public static cq O000000o(eh ehVar, Object obj, eh ehVar2, int i2, fy fyVar, Class cls) {
        return new cq(ehVar, obj, ehVar2, new dd(i2, fyVar));
    }

    public static de O000000o(de deVar, byte[] bArr, int i2, cs csVar) {
        de deVar2 = (de) deVar.b(4);
        try {
            es O00000oO = ep.a.O00000oO(deVar2);
            O00000oO.O000000o(deVar2, bArr, 0, i2, new by(csVar));
            O00000oO.O00000o(deVar2);
            if (deVar2.g == 0) {
                return deVar2;
            }
            throw new RuntimeException();
        } catch (IOException e) {
            if (e.getCause() instanceof dl) {
                throw ((dl) e.getCause());
            }
            throw new dl(e.getMessage());
        } catch (IndexOutOfBoundsException unused) {
            throw dl.a();
        }
    }

    public static de O000000o(de deVar, byte[] bArr, cs csVar) {
        de O000000o2 = O000000o(deVar, bArr, bArr.length, csVar);
        O000000o(O000000o2);
        return O000000o2;
    }

    protected static Object O000000o(eh ehVar, String str, Object[] objArr) {
        return new er(ehVar, str, objArr);
    }

    static Object O000000o(Method method, Object obj, Object... objArr) {
        try {
            return method.invoke(obj, objArr);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Couldn't use Java reflection to implement protocol message reflection.", e);
        } catch (InvocationTargetException e2) {
            Throwable cause = e2.getCause();
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            } else if (cause instanceof Error) {
                throw ((Error) cause);
            } else {
                throw new RuntimeException("Unexpected exception thrown by generated accessor method.", cause);
            }
        }
    }

    public static void O000000o(de deVar) {
        if (deVar != null && !deVar.c()) {
            throw new dl(new fg().getMessage());
        }
    }

    protected static void O000000o(Class cls, de deVar) {
        j.put(cls, deVar);
    }

    public abstract Object O000000o(int i2, Object obj);

    public final void O000000o(cn cnVar) {
        es O00000oO = ep.a.O00000oO(this);
        co coVar = cnVar.b;
        if (coVar == null) {
            coVar = new co(cnVar);
        }
        O00000oO.O000000o((Object) this, (gb) coVar);
    }

    public final void a(int i2) {
        this.i = i2;
    }

    public final /* bridge */ /* synthetic */ eg b() {
        da daVar = (da) b(5);
        daVar.O000000o(this);
        return daVar;
    }

    public final Object b(int i2) {
        return O000000o(i2, (Object) null);
    }

    public final boolean c() {
        boolean booleanValue = Boolean.TRUE.booleanValue();
        boolean z = true;
        byte byteValue = ((Byte) b(1)).byteValue();
        if (byteValue != 1) {
            if (byteValue != 0) {
                boolean O00000o02 = ep.a.O00000oO(this).O00000o0(this);
                if (!booleanValue) {
                    z = O00000o02;
                } else {
                    O000000o(2, (Object) true != O00000o02 ? null : this);
                    return O00000o02;
                }
            } else {
                z = false;
            }
        }
        return z;
    }

    public final da e() {
        return (da) b(5);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return ep.a.O00000oO(this).O000000o((Object) this, (Object) (de) obj);
    }

    public final int f() {
        int i2 = this.i;
        if (i2 != -1) {
            return i2;
        }
        int O00000Oo2 = ep.a.O00000oO(this).O00000Oo(this);
        this.i = O00000Oo2;
        return O00000Oo2;
    }

    public final int hashCode() {
        int i2 = this.g;
        if (i2 != 0) {
            return i2;
        }
        int O00000oO = ep.a.O00000oO(this).O00000oO(this);
        this.g = O00000oO;
        return O00000oO;
    }

    public final /* bridge */ /* synthetic */ eh i() {
        return (de) b(6);
    }

    public final int k() {
        return this.i;
    }

    public final /* bridge */ /* synthetic */ eg l() {
        return (da) b(5);
    }

    public final String toString() {
        String obj = super.toString();
        StringBuilder sb = new StringBuilder();
        sb.append("# ");
        sb.append(obj);
        ej.O000000o((eh) this, sb, 0);
        return sb.toString();
    }
}
