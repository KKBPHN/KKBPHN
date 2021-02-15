package defpackage;

/* renamed from: dz reason: default package */
final class dz implements et {
    private static final ef b = new dx();
    public final ef a;

    public dz() {
        ef efVar;
        ef[] efVarArr = new ef[2];
        efVarArr[0] = cz.a;
        try {
            efVar = (ef) Class.forName("com.google.protobuf.DescriptorMessageInfoFactory").getDeclaredMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
        } catch (Exception unused) {
            efVar = b;
        }
        efVarArr[1] = efVar;
        dy dyVar = new dy(efVarArr);
        dj.a(dyVar, "messageInfoFactory");
        this.a = dyVar;
    }

    public static boolean O000000o(ee eeVar) {
        return eeVar.c() == 1;
    }
}
