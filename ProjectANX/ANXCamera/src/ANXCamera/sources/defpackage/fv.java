package defpackage;

/* renamed from: fv reason: default package */
final class fv extends IllegalArgumentException {
    public fv(int i, int i2) {
        StringBuilder sb = new StringBuilder(54);
        sb.append("Unpaired surrogate at index ");
        sb.append(i);
        sb.append(" of ");
        sb.append(i2);
        super(sb.toString());
    }
}
