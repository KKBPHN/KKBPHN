package vendor.xiaomi.hardware.campostproc.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class PostProcResult {
    public MiFlawResult flawData = new MiFlawResult();
    public int resultId;
    public long timeStampUs;
    public int type;

    public static final ArrayList readVectorFromParcel(HwParcel hwParcel) {
        ArrayList arrayList = new ArrayList();
        HwBlob readBuffer = hwParcel.readBuffer(16);
        int int32 = readBuffer.getInt32(8);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer((long) (int32 * 40), readBuffer.handle(), 0, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            PostProcResult postProcResult = new PostProcResult();
            postProcResult.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, (long) (i * 40));
            arrayList.add(postProcResult);
        }
        return arrayList;
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList arrayList) {
        HwBlob hwBlob = new HwBlob(16);
        int size = arrayList.size();
        hwBlob.putInt32(8, size);
        hwBlob.putBool(12, false);
        HwBlob hwBlob2 = new HwBlob(size * 40);
        for (int i = 0; i < size; i++) {
            ((PostProcResult) arrayList.get(i)).writeEmbeddedToBlob(hwBlob2, (long) (i * 40));
        }
        hwBlob.putBlob(0, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != PostProcResult.class) {
            return false;
        }
        PostProcResult postProcResult = (PostProcResult) obj;
        return this.type == postProcResult.type && this.resultId == postProcResult.resultId && this.timeStampUs == postProcResult.timeStampUs && HidlSupport.deepEquals(this.flawData, postProcResult.flawData);
    }

    public final int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.type))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.resultId))), Integer.valueOf(HidlSupport.deepHashCode(Long.valueOf(this.timeStampUs))), Integer.valueOf(HidlSupport.deepHashCode(this.flawData))});
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.type = hwBlob.getInt32(0 + j);
        this.resultId = hwBlob.getInt32(4 + j);
        this.timeStampUs = hwBlob.getInt64(8 + j);
        this.flawData.readEmbeddedFromParcel(hwParcel, hwBlob, j + 16);
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(40), 0);
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(".type = ");
        sb.append(NotifyType.toString(this.type));
        sb.append(", .resultId = ");
        sb.append(this.resultId);
        sb.append(", .timeStampUs = ");
        sb.append(this.timeStampUs);
        sb.append(", .flawData = ");
        sb.append(this.flawData);
        sb.append("}");
        return sb.toString();
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        hwBlob.putInt32(0 + j, this.type);
        hwBlob.putInt32(4 + j, this.resultId);
        hwBlob.putInt64(8 + j, this.timeStampUs);
        this.flawData.writeEmbeddedToBlob(hwBlob, j + 16);
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(40);
        writeEmbeddedToBlob(hwBlob, 0);
        hwParcel.writeBuffer(hwBlob);
    }
}
