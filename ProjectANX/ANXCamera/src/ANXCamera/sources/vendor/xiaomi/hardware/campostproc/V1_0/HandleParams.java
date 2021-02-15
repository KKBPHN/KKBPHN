package vendor.xiaomi.hardware.campostproc.V1_0;

import android.os.HwBlob;
import android.os.HwParcel;
import android.os.NativeHandle;
import java.util.ArrayList;

public final class HandleParams {
    public NativeHandle bufHandle = new NativeHandle();
    public int format;
    public int height;
    public ArrayList metadata = new ArrayList();
    public int width;

    public static final ArrayList readVectorFromParcel(HwParcel hwParcel) {
        ArrayList arrayList = new ArrayList();
        HwBlob readBuffer = hwParcel.readBuffer(16);
        int int32 = readBuffer.getInt32(8);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer((long) (int32 * 48), readBuffer.handle(), 0, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            HandleParams handleParams = new HandleParams();
            handleParams.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, (long) (i * 48));
            arrayList.add(handleParams);
        }
        return arrayList;
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList arrayList) {
        HwBlob hwBlob = new HwBlob(16);
        int size = arrayList.size();
        hwBlob.putInt32(8, size);
        hwBlob.putBool(12, false);
        HwBlob hwBlob2 = new HwBlob(size * 48);
        for (int i = 0; i < size; i++) {
            ((HandleParams) arrayList.get(i)).writeEmbeddedToBlob(hwBlob2, (long) (i * 48));
        }
        hwBlob.putBlob(0, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        HwBlob hwBlob2 = hwBlob;
        this.format = hwBlob2.getInt32(j + 0);
        this.width = hwBlob2.getInt32(j + 4);
        this.height = hwBlob2.getInt32(j + 8);
        HwParcel hwParcel2 = hwParcel;
        this.bufHandle = hwParcel2.readEmbeddedNativeHandle(hwBlob.handle(), j + 16 + 0);
        long j2 = j + 32;
        int int32 = hwBlob2.getInt32(8 + j2);
        HwBlob readEmbeddedBuffer = hwParcel2.readEmbeddedBuffer((long) (int32 * 1), hwBlob.handle(), j2 + 0, true);
        this.metadata.clear();
        for (int i = 0; i < int32; i++) {
            this.metadata.add(Byte.valueOf(readEmbeddedBuffer.getInt8((long) (i * 1))));
        }
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(48), 0);
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
        sb.append(", .bufHandle = ");
        sb.append(this.bufHandle);
        sb.append(", .metadata = ");
        sb.append(this.metadata);
        sb.append("}");
        return sb.toString();
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        hwBlob.putInt32(j + 0, this.format);
        hwBlob.putInt32(4 + j, this.width);
        hwBlob.putInt32(j + 8, this.height);
        hwBlob.putNativeHandle(16 + j, this.bufHandle);
        int size = this.metadata.size();
        long j2 = j + 32;
        hwBlob.putInt32(8 + j2, size);
        hwBlob.putBool(12 + j2, false);
        HwBlob hwBlob2 = new HwBlob(size * 1);
        for (int i = 0; i < size; i++) {
            hwBlob2.putInt8((long) (i * 1), ((Byte) this.metadata.get(i)).byteValue());
        }
        hwBlob.putBlob(j2 + 0, hwBlob2);
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(48);
        writeEmbeddedToBlob(hwBlob, 0);
        hwParcel.writeBuffer(hwBlob);
    }
}
