package vendor.xiaomi.hardware.campostproc.V1_0;

import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;

public final class SessionProcessParams {
    public int frameNum;
    public ArrayList input = new ArrayList();
    public byte isSync;
    public ArrayList output = new ArrayList();
    public int streamId;
    public long timeStampUs;
    public int type;

    public static final ArrayList readVectorFromParcel(HwParcel hwParcel) {
        ArrayList arrayList = new ArrayList();
        HwBlob readBuffer = hwParcel.readBuffer(16);
        int int32 = readBuffer.getInt32(8);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer((long) (int32 * 56), readBuffer.handle(), 0, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            SessionProcessParams sessionProcessParams = new SessionProcessParams();
            sessionProcessParams.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, (long) (i * 56));
            arrayList.add(sessionProcessParams);
        }
        return arrayList;
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList arrayList) {
        HwBlob hwBlob = new HwBlob(16);
        int size = arrayList.size();
        hwBlob.putInt32(8, size);
        hwBlob.putBool(12, false);
        HwBlob hwBlob2 = new HwBlob(size * 56);
        for (int i = 0; i < size; i++) {
            ((SessionProcessParams) arrayList.get(i)).writeEmbeddedToBlob(hwBlob2, (long) (i * 56));
        }
        hwBlob.putBlob(0, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        HwParcel hwParcel2 = hwParcel;
        HwBlob hwBlob2 = hwBlob;
        this.frameNum = hwBlob2.getInt32(j + 0);
        this.streamId = hwBlob2.getInt32(j + 4);
        this.timeStampUs = hwBlob2.getInt64(j + 8);
        long j2 = j + 16;
        int int32 = hwBlob2.getInt32(j2 + 8);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer((long) (int32 * 48), hwBlob.handle(), j2 + 0, true);
        this.input.clear();
        for (int i = 0; i < int32; i++) {
            HandleParams handleParams = new HandleParams();
            handleParams.readEmbeddedFromParcel(hwParcel2, readEmbeddedBuffer, (long) (i * 48));
            this.input.add(handleParams);
        }
        long j3 = j + 32;
        int int322 = hwBlob2.getInt32(8 + j3);
        HwBlob readEmbeddedBuffer2 = hwParcel.readEmbeddedBuffer((long) (int322 * 48), hwBlob.handle(), j3 + 0, true);
        this.output.clear();
        for (int i2 = 0; i2 < int322; i2++) {
            HandleParams handleParams2 = new HandleParams();
            handleParams2.readEmbeddedFromParcel(hwParcel2, readEmbeddedBuffer2, (long) (i2 * 48));
            this.output.add(handleParams2);
        }
        this.isSync = hwBlob2.getInt8(j + 48);
        this.type = hwBlob2.getInt32(j + 52);
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(56), 0);
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(".frameNum = ");
        sb.append(this.frameNum);
        sb.append(", .streamId = ");
        sb.append(this.streamId);
        sb.append(", .timeStampUs = ");
        sb.append(this.timeStampUs);
        sb.append(", .input = ");
        sb.append(this.input);
        sb.append(", .output = ");
        sb.append(this.output);
        sb.append(", .isSync = ");
        sb.append(this.isSync);
        sb.append(", .type = ");
        sb.append(PostProcType.toString(this.type));
        sb.append("}");
        return sb.toString();
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        HwBlob hwBlob2 = hwBlob;
        hwBlob2.putInt32(j + 0, this.frameNum);
        hwBlob2.putInt32(j + 4, this.streamId);
        hwBlob2.putInt64(j + 8, this.timeStampUs);
        int size = this.input.size();
        long j2 = j + 16;
        hwBlob2.putInt32(j2 + 8, size);
        hwBlob2.putBool(j2 + 12, false);
        HwBlob hwBlob3 = new HwBlob(size * 48);
        for (int i = 0; i < size; i++) {
            ((HandleParams) this.input.get(i)).writeEmbeddedToBlob(hwBlob3, (long) (i * 48));
        }
        hwBlob2.putBlob(j2 + 0, hwBlob3);
        int size2 = this.output.size();
        long j3 = j + 32;
        hwBlob2.putInt32(8 + j3, size2);
        hwBlob2.putBool(j3 + 12, false);
        HwBlob hwBlob4 = new HwBlob(size2 * 48);
        for (int i2 = 0; i2 < size2; i2++) {
            ((HandleParams) this.output.get(i2)).writeEmbeddedToBlob(hwBlob4, (long) (i2 * 48));
        }
        hwBlob2.putBlob(j3 + 0, hwBlob4);
        hwBlob2.putInt8(j + 48, this.isSync);
        hwBlob2.putInt32(j + 52, this.type);
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(56);
        writeEmbeddedToBlob(hwBlob, 0);
        hwParcel.writeBuffer(hwBlob);
    }
}
