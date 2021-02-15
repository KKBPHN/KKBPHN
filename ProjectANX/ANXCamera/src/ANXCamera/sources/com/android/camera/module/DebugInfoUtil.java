package com.android.camera.module;

import android.graphics.Rect;
import android.graphics.RectF;
import com.android.camera2.CameraHardwareFace;
import com.android.camera2.vendortag.struct.MarshalQueryableSuperNightExif.SuperNightExif;

public class DebugInfoUtil {
    public static final String TAG = "DebugInfoUtil";

    public static String getFaceInfoString(CameraHardwareFace[] cameraHardwareFaceArr) {
        if (cameraHardwareFaceArr == null || cameraHardwareFaceArr.length <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder(128);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("size:");
        sb2.append(cameraHardwareFaceArr.length);
        sb2.append("  value:");
        sb.append(sb2.toString());
        for (CameraHardwareFace cameraHardwareFace : cameraHardwareFaceArr) {
            Rect rect = cameraHardwareFace.rect;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("[");
            sb3.append(rect.left);
            String str = ",";
            sb3.append(str);
            sb3.append(rect.top);
            sb3.append(str);
            sb3.append(rect.right);
            sb3.append(str);
            sb3.append(rect.bottom);
            sb3.append("] ");
            sb.append(sb3.toString());
        }
        return sb.toString();
    }

    public static String getRetriveFaceInfo(RectF[] rectFArr) {
        if (rectFArr == null || rectFArr.length <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder(128);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("size:");
        sb2.append(rectFArr.length);
        sb2.append("  value:");
        sb.append(sb2.toString());
        for (RectF rectF : rectFArr) {
            if (rectF != null) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("[");
                sb3.append(Math.abs(rectF.left));
                String str = ",";
                sb3.append(str);
                sb3.append(Math.abs(rectF.top));
                sb3.append(str);
                sb3.append(Math.abs(rectF.right));
                sb3.append(str);
                sb3.append(Math.abs(rectF.bottom));
                sb3.append("] ");
                sb.append(sb3.toString());
            }
        }
        return sb.toString();
    }

    public static String getSuperNightExif(SuperNightExif superNightExif) {
        if (superNightExif == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(" luxIndex: ");
        sb2.append(superNightExif.luxIndex);
        sb.append(sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append(" light: ");
        sb3.append(superNightExif.light);
        sb.append(sb3.toString());
        StringBuilder sb4 = new StringBuilder();
        sb4.append(" darkRatio: ");
        sb4.append(superNightExif.darkRatio);
        sb.append(sb4.toString());
        StringBuilder sb5 = new StringBuilder();
        sb5.append(" middleRatio: ");
        sb5.append(superNightExif.middleRatio);
        sb.append(sb5.toString());
        StringBuilder sb6 = new StringBuilder();
        sb6.append(" brightRatio: ");
        sb6.append(superNightExif.brightRatio);
        sb.append(sb6.toString());
        StringBuilder sb7 = new StringBuilder();
        sb7.append(" result: ");
        sb7.append(superNightExif.result);
        sb.append(sb7.toString());
        return sb.toString();
    }
}
