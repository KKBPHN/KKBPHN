package O000000o.O000000o.O000000o.O000000o.O000000o;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class O00000o0 {
    public int O00oOooO;
    public ArrayList O00oOooo = new ArrayList();

    public static final ArrayList readVectorFromParcel(HwParcel hwParcel) {
        ArrayList arrayList = new ArrayList();
        HwBlob readBuffer = hwParcel.readBuffer(16);
        int int32 = readBuffer.getInt32(8);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer((long) (int32 * 24), readBuffer.handle(), 0, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            O00000o0 o00000o0 = new O00000o0();
            o00000o0.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, (long) (i * 24));
            arrayList.add(o00000o0);
        }
        return arrayList;
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList arrayList) {
        HwBlob hwBlob = new HwBlob(16);
        int size = arrayList.size();
        hwBlob.putInt32(8, size);
        hwBlob.putBool(12, false);
        HwBlob hwBlob2 = new HwBlob(size * 24);
        for (int i = 0; i < size; i++) {
            ((O00000o0) arrayList.get(i)).writeEmbeddedToBlob(hwBlob2, (long) (i * 24));
        }
        hwBlob.putBlob(0, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != O00000o0.class) {
            return false;
        }
        O00000o0 o00000o0 = (O00000o0) obj;
        return this.O00oOooO == o00000o0.O00oOooO && HidlSupport.deepEquals(this.O00oOooo, o00000o0.O00oOooo);
    }

    public final int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.O00oOooO))), Integer.valueOf(HidlSupport.deepHashCode(this.O00oOooo))});
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        HwBlob hwBlob2 = hwBlob;
        this.O00oOooO = hwBlob2.getInt32(j + 0);
        long j2 = j + 8;
        int int32 = hwBlob2.getInt32(8 + j2);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer((long) (int32 * 16), hwBlob.handle(), j2 + 0, true);
        this.O00oOooo.clear();
        for (int i = 0; i < int32; i++) {
            new String();
            int i2 = i * 16;
            String string = readEmbeddedBuffer.getString((long) i2);
            hwParcel.readEmbeddedBuffer((long) (string.getBytes().length + 1), readEmbeddedBuffer.handle(), (long) (i2 + 0), false);
            this.O00oOooo.add(string);
        }
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(24), 0);
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(".resourceCost = ");
        sb.append(this.O00oOooO);
        sb.append(", .conflictingDevices = ");
        sb.append(this.O00oOooo);
        sb.append("}");
        return sb.toString();
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        hwBlob.putInt32(j + 0, this.O00oOooO);
        int size = this.O00oOooo.size();
        long j2 = j + 8;
        hwBlob.putInt32(8 + j2, size);
        hwBlob.putBool(12 + j2, false);
        HwBlob hwBlob2 = new HwBlob(size * 16);
        for (int i = 0; i < size; i++) {
            hwBlob2.putString((long) (i * 16), (String) this.O00oOooo.get(i));
        }
        hwBlob.putBlob(j2 + 0, hwBlob2);
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(24);
        writeEmbeddedToBlob(hwBlob, 0);
        hwParcel.writeBuffer(hwBlob);
    }
}
