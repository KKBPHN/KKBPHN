package defpackage;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;

/* renamed from: cd reason: default package */
final class cd extends ci {
    private static final long serialVersionUID = 1;
    private final int d;
    private final int e;

    public cd(byte[] bArr, int i, int i2) {
        super(bArr);
        ck.O000000o(i, i + i2, bArr.length);
        this.d = i;
        this.e = i2;
    }

    private void readObject(ObjectInputStream objectInputStream) {
        throw new InvalidObjectException("BoundedByteStream instances are not to be serialized directly");
    }

    /* access modifiers changed from: protected */
    public final void O000000o(byte[] bArr, int i) {
        System.arraycopy(this.a, this.d, bArr, 0, i);
    }

    public final byte a(int i) {
        ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException;
        int i2 = this.e;
        if (((i2 - (i + 1)) | i) >= 0) {
            return this.a[this.d + i];
        }
        if (i >= 0) {
            StringBuilder sb = new StringBuilder(40);
            sb.append("Index > length: ");
            sb.append(i);
            sb.append(", ");
            sb.append(i2);
            arrayIndexOutOfBoundsException = new ArrayIndexOutOfBoundsException(sb.toString());
            throw arrayIndexOutOfBoundsException;
        }
        StringBuilder sb2 = new StringBuilder(22);
        sb2.append("Index < 0: ");
        sb2.append(i);
        arrayIndexOutOfBoundsException = new ArrayIndexOutOfBoundsException(sb2.toString());
        throw arrayIndexOutOfBoundsException;
    }

    public final int a() {
        return this.e;
    }

    public final byte b(int i) {
        return this.a[this.d + i];
    }

    /* access modifiers changed from: protected */
    public final int b() {
        return this.d;
    }

    /* access modifiers changed from: 0000 */
    public Object writeReplace() {
        byte[] bArr;
        int a = a();
        if (a == 0) {
            bArr = dj.b;
        } else {
            byte[] bArr2 = new byte[a];
            O000000o(bArr2, a);
            bArr = bArr2;
        }
        return new ci(bArr);
    }
}
