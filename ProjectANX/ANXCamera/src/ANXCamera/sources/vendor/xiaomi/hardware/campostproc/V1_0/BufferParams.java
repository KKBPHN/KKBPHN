package vendor.xiaomi.hardware.campostproc.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class BufferParams {
    public int format;
    public int height;
    public int width;

    public static final ArrayList readVectorFromParcel(HwParcel hwParcel) {
        ArrayList arrayList = new ArrayList();
        HwBlob readBuffer = hwParcel.readBuffer(16);
        int int32 = readBuffer.getInt32(8);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer((long) (int32 * 12), readBuffer.handle(), 0, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            BufferParams bufferParams = new BufferParams();
            bufferParams.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, (long) (i * 12));
            arrayList.add(bufferParams);
        }
        return arrayList;
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList arrayList) {
        HwBlob hwBlob = new HwBlob(16);
        int size = arrayList.size();
        hwBlob.putInt32(8, size);
        hwBlob.putBool(12, false);
        HwBlob hwBlob2 = new HwBlob(size * 12);
        for (int i = 0; i < size; i++) {
            ((BufferParams) arrayList.get(i)).writeEmbeddedToBlob(hwBlob2, (long) (i * 12));
        }
        hwBlob.putBlob(0, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != BufferParams.class) {
            return false;
        }
        BufferParams bufferParams = (BufferParams) obj;
        return this.format == bufferParams.format && this.width == bufferParams.width && this.height == bufferParams.height;
    }

    public final int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.format))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.width))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.height)))});
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.format = hwBlob.getInt32(0 + j);
        this.width = hwBlob.getInt32(4 + j);
        this.height = hwBlob.getInt32(j + 8);
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(12), 0);
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(".format = ");
        sb.append(this.format);
        sb.append(", .width = ");
        sb.append(this.width);
        sb.append(", .height = ");
        sb.append(this.height);
        sb.append("}");
        return sb.toString();
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        hwBlob.putInt32(0 + j, this.format);
        hwBlob.putInt32(4 + j, this.width);
        hwBlob.putInt32(j + 8, this.height);
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(12);
        writeEmbeddedToBlob(hwBlob, 0);
        hwParcel.writeBuffer(hwBlob);
    }
}
