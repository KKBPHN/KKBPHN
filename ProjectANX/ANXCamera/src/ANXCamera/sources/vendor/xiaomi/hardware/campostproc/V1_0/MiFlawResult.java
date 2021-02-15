package vendor.xiaomi.hardware.campostproc.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class MiFlawResult {
    public byte eye_close;
    public byte face_blur;
    public int flaw_result;
    public byte occlusion;
    public int person_cls;
    public float prob_eyeclose;
    public float prob_faceblur;
    public float prob_occlusion;

    public static final ArrayList readVectorFromParcel(HwParcel hwParcel) {
        ArrayList arrayList = new ArrayList();
        HwBlob readBuffer = hwParcel.readBuffer(16);
        int int32 = readBuffer.getInt32(8);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer((long) (int32 * 24), readBuffer.handle(), 0, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            MiFlawResult miFlawResult = new MiFlawResult();
            miFlawResult.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, (long) (i * 24));
            arrayList.add(miFlawResult);
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
            ((MiFlawResult) arrayList.get(i)).writeEmbeddedToBlob(hwBlob2, (long) (i * 24));
        }
        hwBlob.putBlob(0, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != MiFlawResult.class) {
            return false;
        }
        MiFlawResult miFlawResult = (MiFlawResult) obj;
        return this.face_blur == miFlawResult.face_blur && this.eye_close == miFlawResult.eye_close && this.occlusion == miFlawResult.occlusion && this.person_cls == miFlawResult.person_cls && this.prob_eyeclose == miFlawResult.prob_eyeclose && this.prob_occlusion == miFlawResult.prob_occlusion && this.prob_faceblur == miFlawResult.prob_faceblur && this.flaw_result == miFlawResult.flaw_result;
    }

    public final int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.face_blur))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.eye_close))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.occlusion))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.person_cls))), Integer.valueOf(HidlSupport.deepHashCode(Float.valueOf(this.prob_eyeclose))), Integer.valueOf(HidlSupport.deepHashCode(Float.valueOf(this.prob_occlusion))), Integer.valueOf(HidlSupport.deepHashCode(Float.valueOf(this.prob_faceblur))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.flaw_result)))});
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.face_blur = hwBlob.getInt8(0 + j);
        this.eye_close = hwBlob.getInt8(1 + j);
        this.occlusion = hwBlob.getInt8(2 + j);
        this.person_cls = hwBlob.getInt32(4 + j);
        this.prob_eyeclose = hwBlob.getFloat(8 + j);
        this.prob_occlusion = hwBlob.getFloat(12 + j);
        this.prob_faceblur = hwBlob.getFloat(16 + j);
        this.flaw_result = hwBlob.getInt32(j + 20);
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(24), 0);
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(".face_blur = ");
        sb.append(this.face_blur);
        sb.append(", .eye_close = ");
        sb.append(this.eye_close);
        sb.append(", .occlusion = ");
        sb.append(this.occlusion);
        sb.append(", .person_cls = ");
        sb.append(this.person_cls);
        sb.append(", .prob_eyeclose = ");
        sb.append(this.prob_eyeclose);
        sb.append(", .prob_occlusion = ");
        sb.append(this.prob_occlusion);
        sb.append(", .prob_faceblur = ");
        sb.append(this.prob_faceblur);
        sb.append(", .flaw_result = ");
        sb.append(this.flaw_result);
        sb.append("}");
        return sb.toString();
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        hwBlob.putInt8(0 + j, this.face_blur);
        hwBlob.putInt8(1 + j, this.eye_close);
        hwBlob.putInt8(2 + j, this.occlusion);
        hwBlob.putInt32(4 + j, this.person_cls);
        hwBlob.putFloat(8 + j, this.prob_eyeclose);
        hwBlob.putFloat(12 + j, this.prob_occlusion);
        hwBlob.putFloat(16 + j, this.prob_faceblur);
        hwBlob.putInt32(j + 20, this.flaw_result);
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(24);
        writeEmbeddedToBlob(hwBlob, 0);
        hwParcel.writeBuffer(hwBlob);
    }
}
