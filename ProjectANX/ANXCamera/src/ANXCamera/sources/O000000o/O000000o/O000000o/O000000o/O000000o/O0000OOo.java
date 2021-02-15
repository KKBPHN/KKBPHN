package O000000o.O000000o.O000000o.O000000o.O000000o;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class O0000OOo {
    public int O000O0oo;
    public int tagId;
    public String tagName = new String();

    public static final ArrayList readVectorFromParcel(HwParcel hwParcel) {
        ArrayList arrayList = new ArrayList();
        HwBlob readBuffer = hwParcel.readBuffer(16);
        int int32 = readBuffer.getInt32(8);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer((long) (int32 * 32), readBuffer.handle(), 0, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            O0000OOo o0000OOo = new O0000OOo();
            o0000OOo.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, (long) (i * 32));
            arrayList.add(o0000OOo);
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
            ((O0000OOo) arrayList.get(i)).writeEmbeddedToBlob(hwBlob2, (long) (i * 32));
        }
        hwBlob.putBlob(0, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != O0000OOo.class) {
            return false;
        }
        O0000OOo o0000OOo = (O0000OOo) obj;
        return this.tagId == o0000OOo.tagId && HidlSupport.deepEquals(this.tagName, o0000OOo.tagName) && this.O000O0oo == o0000OOo.O000O0oo;
    }

    public final int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.tagId))), Integer.valueOf(HidlSupport.deepHashCode(this.tagName)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.O000O0oo)))});
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        HwBlob hwBlob2 = hwBlob;
        this.tagId = hwBlob2.getInt32(j + 0);
        long j2 = j + 8;
        this.tagName = hwBlob2.getString(j2);
        hwParcel.readEmbeddedBuffer((long) (this.tagName.getBytes().length + 1), hwBlob.handle(), j2 + 0, false);
        this.O000O0oo = hwBlob2.getInt32(j + 24);
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(32), 0);
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(".tagId = ");
        sb.append(this.tagId);
        sb.append(", .tagName = ");
        sb.append(this.tagName);
        sb.append(", .tagType = ");
        sb.append(O00000Oo.toString(this.O000O0oo));
        sb.append("}");
        return sb.toString();
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        hwBlob.putInt32(0 + j, this.tagId);
        hwBlob.putString(8 + j, this.tagName);
        hwBlob.putInt32(j + 24, this.O000O0oo);
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(32);
        writeEmbeddedToBlob(hwBlob, 0);
        hwParcel.writeBuffer(hwBlob);
    }
}
