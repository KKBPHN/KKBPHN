package com.android.camera2.vendortag.struct;

import android.hardware.camera2.CaptureResult;
import com.android.camera2.vendortag.CaptureResultVendorTags;
import com.android.camera2.vendortag.VendorTagHelper;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MarshalQueryableSuperNightExif {
    private static int BUFFER_SIZE = 20;
    /* access modifiers changed from: private */
    public static int BUFFER_SIZE_CONTAIN_LUXTHRESHOLD = 24;
    private static final int SIZEOF_FLOAT = 4;

    public class MarshalerSuperNightExif {
        public void marshal(SuperNightExif superNightExif, ByteBuffer byteBuffer) {
            byteBuffer.putFloat(superNightExif.luxIndex);
            byteBuffer.putFloat(superNightExif.light);
            byteBuffer.putFloat(superNightExif.darkRatio);
            byteBuffer.putFloat(superNightExif.middleRatio);
            byteBuffer.putFloat(superNightExif.brightRatio);
        }

        public SuperNightExif unmarshal(ByteBuffer byteBuffer) {
            SuperNightExif superNightExif = new SuperNightExif();
            superNightExif.luxIndex = byteBuffer.getFloat();
            superNightExif.light = byteBuffer.getFloat();
            superNightExif.darkRatio = byteBuffer.getFloat();
            superNightExif.middleRatio = byteBuffer.getFloat();
            superNightExif.brightRatio = byteBuffer.getFloat();
            if (byteBuffer.capacity() >= MarshalQueryableSuperNightExif.BUFFER_SIZE_CONTAIN_LUXTHRESHOLD) {
                superNightExif.luxThreshold = byteBuffer.getFloat();
            }
            return superNightExif;
        }
    }

    public class SuperNightExif {
        public float brightRatio;
        public float darkRatio;
        public float light;
        public float luxIndex;
        public float luxThreshold = -1.0f;
        public float middleRatio;
        public float result;

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("luxIndex:");
            sb.append(this.luxIndex);
            sb.append(",light:");
            sb.append(this.light);
            sb.append(",darkRatio:");
            sb.append(this.darkRatio);
            sb.append(",middleRatio:");
            sb.append(this.middleRatio);
            sb.append(",brightRatio:");
            sb.append(this.brightRatio);
            sb.append(",result:");
            sb.append(this.result);
            sb.append(",luxThreshold:");
            sb.append(this.luxThreshold);
            return sb.toString();
        }
    }

    public static SuperNightExif getSuperNightExif(CaptureResult captureResult, boolean z) {
        byte[] bArr = new byte[0];
        if (captureResult == null) {
            return getSuperNightExif(bArr);
        }
        if (z) {
            bArr = (byte[]) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.SUPER_NIGHT_EXIF);
        }
        return getSuperNightExif(bArr);
    }

    public static SuperNightExif getSuperNightExif(byte[] bArr) {
        return (bArr == null || bArr.length <= 0) ? new SuperNightExif() : new MarshalerSuperNightExif().unmarshal(ByteBuffer.wrap(bArr).order(ByteOrder.nativeOrder()));
    }
}
