package O000000o.O000000o.O000000o.O000000o.O000000o;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class O0000Oo0 {
    public String O000OO00 = new String();
    public ArrayList tags = new ArrayList();

    public static final ArrayList readVectorFromParcel(HwParcel hwParcel) {
        ArrayList arrayList = new ArrayList();
        HwBlob readBuffer = hwParcel.readBuffer(16);
        int int32 = readBuffer.getInt32(8);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer((long) (int32 * 32), readBuffer.handle(), 0, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            O0000Oo0 o0000Oo0 = new O0000Oo0();
            o0000Oo0.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, (long) (i * 32));
            arrayList.add(o0000Oo0);
        }
        return arrayList;
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList arrayList) {
        HwBlob hwBlob = new HwBlob(16);
        int size = arrayList.size();
        hwBlob.putInt32(8, size);
        hwBlob.putBool(12, false);
        HwBlob hwBlob2 = new HwBlob(size * 32);
        for (int i = 0; i < size; i++) {
            ((O0000Oo0) arrayList.get(i)).writeEmbeddedToBlob(hwBlob2, (long) (i * 32));
        }
        hwBlob.putBlob(0, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != O0000Oo0.class) {
            return false;
        }
        O0000Oo0 o0000Oo0 = (O0000Oo0) obj;
        return HidlSupport.deepEquals(this.O000OO00, o0000Oo0.O000OO00) && HidlSupport.deepEquals(this.tags, o0000Oo0.tags);
    }

    public final int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(HidlSupport.deepHashCode(this.O000OO00)), Integer.valueOf(HidlSupport.deepHashCode(this.tags))});
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        HwBlob hwBlob2 = hwBlob;
        long j2 = j + 0;
        this.O000OO00 = hwBlob2.getString(j2);
        hwParcel.readEmbeddedBuffer((long) (this.O000OO00.getBytes().length + 1), hwBlob.handle(), j2 + 0, false);
        long j3 = j + 16;
        int int32 = hwBlob2.getInt32(8 + j3);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer((long) (int32 * 32), hwBlob.handle(), j3 + 0, true);
        this.tags.clear();
        for (int i = 0; i < int32; i++) {
            O0000OOo o0000OOo = new O0000OOo();
            o0000OOo.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, (long) (i * 32));
            this.tags.add(o0000OOo);
        }
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(32), 0);
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(".sectionName = ");
        sb.append(this.O000OO00);
        sb.append(", .tags = ");
        sb.append(this.tags);
        sb.append("}");
        return sb.toString();
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        hwBlob.putString(j + 0, this.O000OO00);
        int size = this.tags.size();
        long j2 = j + 16;
        hwBlob.putInt32(8 + j2, size);
        hwBlob.putBool(12 + j2, false);
        HwBlob hwBlob2 = new HwBlob(size * 32);
        for (int i = 0; i < size; i++) {
            ((O0000OOo) this.tags.get(i)).writeEmbeddedToBlob(hwBlob2, (long) (i * 32));
        }
        hwBlob.putBlob(j2 + 0, hwBlob2);
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(32);
        writeEmbeddedToBlob(hwBlob, 0);
        hwParcel.writeBuffer(hwBlob);
    }
}
