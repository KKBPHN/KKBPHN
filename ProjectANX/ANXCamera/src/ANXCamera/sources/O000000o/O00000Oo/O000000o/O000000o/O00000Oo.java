package O000000o.O00000Oo.O000000o.O000000o;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class O00000Oo {
    public int arch;
    public int pid;
    public long ptr;

    public static final ArrayList readVectorFromParcel(HwParcel hwParcel) {
        ArrayList arrayList = new ArrayList();
        HwBlob readBuffer = hwParcel.readBuffer(16);
        int int32 = readBuffer.getInt32(8);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer((long) (int32 * 24), readBuffer.handle(), 0, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            O00000Oo o00000Oo = new O00000Oo();
            o00000Oo.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, (long) (i * 24));
            arrayList.add(o00000Oo);
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
            ((O00000Oo) arrayList.get(i)).writeEmbeddedToBlob(hwBlob2, (long) (i * 24));
        }
        hwBlob.putBlob(0, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != O00000Oo.class) {
            return false;
        }
        O00000Oo o00000Oo = (O00000Oo) obj;
        return this.pid == o00000Oo.pid && this.ptr == o00000Oo.ptr && this.arch == o00000Oo.arch;
    }

    public final int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.pid))), Integer.valueOf(HidlSupport.deepHashCode(Long.valueOf(this.ptr))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.arch)))});
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.pid = hwBlob.getInt32(0 + j);
        this.ptr = hwBlob.getInt64(8 + j);
        this.arch = hwBlob.getInt32(j + 16);
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(24), 0);
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(".pid = ");
        sb.append(this.pid);
        sb.append(", .ptr = ");
        sb.append(this.ptr);
        sb.append(", .arch = ");
        sb.append(O000000o.toString(this.arch));
        sb.append("}");
        return sb.toString();
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        hwBlob.putInt32(0 + j, this.pid);
        hwBlob.putInt64(8 + j, this.ptr);
        hwBlob.putInt32(j + 16, this.arch);
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(24);
        writeEmbeddedToBlob(hwBlob, 0);
        hwParcel.writeBuffer(hwBlob);
    }
}
