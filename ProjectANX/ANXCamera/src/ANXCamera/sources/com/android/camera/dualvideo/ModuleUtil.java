package com.android.camera.dualvideo;

import com.android.camera.CameraSettings;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.dualvideo.recorder.RecordType;

public class ModuleUtil {

    /* renamed from: com.android.camera.dualvideo.ModuleUtil$1 reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$camera$dualvideo$recorder$RecordType = new int[RecordType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$com$android$camera$dualvideo$recorder$RecordType[RecordType.MERGED.ordinal()] = 1;
            $SwitchMap$com$android$camera$dualvideo$recorder$RecordType[RecordType.STANDALONE.ordinal()] = 2;
        }
    }

    private ModuleUtil() {
    }

    public static int getTopTipRes() {
        int i = AnonymousClass1.$SwitchMap$com$android$camera$dualvideo$recorder$RecordType[CameraSettings.getDualVideoConfig().getRecordType().ordinal()];
        return (i == 1 || i != 2) ? R.string.dual_video_record_merged : R.string.dual_video_record_standalone;
    }

    public static int getUIStyle() {
        if (CameraSettings.getDualVideoConfig().ismDrawSelectWindow()) {
            float windowHeight = (float) (Display.getWindowHeight() / Display.getWindowWidth());
            if (windowHeight >= 1.6777778f && windowHeight <= 2.5f) {
                return 0;
            }
        }
        return 1;
    }

    public static boolean isFatScreen() {
        return ((float) (Display.getWindowHeight() / Display.getWindowWidth())) < 1.6777778f;
    }
}
