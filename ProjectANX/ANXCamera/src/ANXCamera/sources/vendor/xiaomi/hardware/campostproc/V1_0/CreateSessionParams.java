package vendor.xiaomi.hardware.campostproc.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CreateSessionParams {
    public int cameraMode;
    public BufferParams input = new BufferParams();
    public int operationMode;
    public BufferParams output = new BufferParams();
    public ArrayList sessionParams = new ArrayList();

    public static final ArrayList readVectorFromParcel(HwParcel hwParcel) {
        ArrayList arrayList = new ArrayList();
        HwBlob readBuffer = hwParcel.readBuffer(16);
        int int32 = readBuffer.getInt32(8);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer((long) (int32 * 48), readBuffer.handle(), 0, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            CreateSessionParams createSessionParams = new CreateSessionParams();
            createSessionParams.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, (long) (i * 48));
            arrayList.add(createSessionParams);
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
            ((CreateSessionParams) arrayList.get(i)).writeEmbeddedToBlob(hwBlob2, (long) (i * 48));
        }
        hwBlob.putBlob(0, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != CreateSessionParams.class) {
            return false;
        }
        CreateSessionParams createSessionParams = (CreateSessionParams) obj;
        return this.operationMode == createSessionParams.operationMode && this.cameraMode == createSessionParams.cameraMode && HidlSupport.deepEquals(this.input, createSessionParams.input) && HidlSupport.deepEquals(this.output, createSessionParams.output) && HidlSupport.deepEquals(this.sessionParams, createSessionParams.sessionParams);
    }

    public final int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.operationMode))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.cameraMode))), Integer.valueOf(HidlSupport.deepHashCode(this.input)), Integer.valueOf(HidlSupport.deepHashCode(this.output)), Integer.valueOf(HidlSupport.deepHashCode(this.sessionParams))});
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        HwParcel hwParcel2 = hwParcel;
        HwBlob hwBlob2 = hwBlob;
        this.operationMode = hwBlob.getInt32(j + 0);
        this.cameraMode = hwBlob.getInt32(j + 4);
        this.input.readEmbeddedFromParcel(hwParcel, hwBlob, j + 8);
        this.output.readEmbeddedFromParcel(hwParcel, hwBlob, j + 20);
        long j2 = j + 32;
        int int32 = hwBlob.getInt32(8 + j2);
        HwBlob readEmbeddedBuffer = hwParcel2.readEmbeddedBuffer((long) (int32 * 1), hwBlob.handle(), j2 + 0, true);
        this.sessionParams.clear();
        for (int i = 0; i < int32; i++) {
            this.sessionParams.add(Byte.valueOf(readEmbeddedBuffer.getInt8((long) (i * 1))));
        }
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(48), 0);
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(".operationMode = ");
        sb.append(this.operationMode);
        sb.append(", .cameraMode = ");
        sb.append(this.cameraMode);
        sb.append(", .input = ");
        sb.append(this.input);
        sb.append(", .output = ");
        sb.append(this.output);
        sb.append(", .sessionParams = ");
        sb.append(this.sessionParams);
        sb.append("}");
        return sb.toString();
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        hwBlob.putInt32(j + 0, this.operationMode);
        hwBlob.putInt32(4 + j, this.cameraMode);
        this.input.writeEmbeddedToBlob(hwBlob, j + 8);
        this.output.writeEmbeddedToBlob(hwBlob, 20 + j);
        int size = this.sessionParams.size();
        long j2 = j + 32;
        hwBlob.putInt32(8 + j2, size);
        hwBlob.putBool(12 + j2, false);
        HwBlob hwBlob2 = new HwBlob(size * 1);
        for (int i = 0; i < size; i++) {
            hwBlob2.putInt8((long) (i * 1), ((Byte) this.sessionParams.get(i)).byteValue());
        }
        hwBlob.putBlob(j2 + 0, hwBlob2);
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(48);
        writeEmbeddedToBlob(hwBlob, 0);
        hwParcel.writeBuffer(hwBlob);
    }
}
