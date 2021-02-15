package com.android.camera.data.data.config;

import androidx.annotation.NonNull;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera2.CameraCapabilities;
import java.util.ArrayList;
import java.util.List;

public class ComponentConfigAuxiliary extends ComponentData {
    public static final String A_CLOSE = "close";
    public static final String A_EXPOSURE_FEEDBACK = "exposure_feedback";
    public static final String A_FOCUS_PEAK = "peak_focus";

    public ComponentConfigAuxiliary(DataItemConfig dataItemConfig) {
        super(dataItemConfig);
    }

    private int getAuxiliaryRes() {
        return R.drawable.ic_config_auxiliary;
    }

    private int getExposurceFeedbackRes() {
        return R.drawable.ic_config_exposure_feedback_on;
    }

    private int getFocusPeakRes() {
        return R.drawable.ic_config_focus_peak_on;
    }

    @NonNull
    public String getDefaultValue(int i) {
        return "close";
    }

    public int getDisplayTitleString() {
        return R.string.pref_camera_auxiliary_title;
    }

    public List getItems() {
        return this.mItems;
    }

    public String getKey(int i) {
        if (i == 167) {
            return CameraSettings.KEY_CAMERA_PRO_PHOTO_AUXILIARY;
        }
        if (i != 180) {
            return null;
        }
        return CameraSettings.KEY_CAMERA_PRO_VIDEO_AUXILIARY;
    }

    public int getValueSelectedDrawable(int i) {
        String componentValue = getComponentValue(i);
        return A_FOCUS_PEAK.equals(componentValue) ? getFocusPeakRes() : "exposure_feedback".equals(componentValue) ? getExposurceFeedbackRes() : getAuxiliaryRes();
    }

    public int getValueSelectedStringIdIgnoreClose(int i) {
        String componentValue = getComponentValue(i);
        return A_FOCUS_PEAK.equals(componentValue) ? R.string.auxiliary_focus_peak_status : "exposure_feedback".equals(componentValue) ? R.string.auxiliary_exposure_feedback_status : R.string.auxiliary_close_status;
    }

    public List reInit(int i, CameraCapabilities cameraCapabilities) {
        if (this.mItems == null) {
            this.mItems = new ArrayList();
        } else {
            this.mItems.clear();
        }
        if (i == 167 || i == 180) {
            this.mItems.add(new ComponentDataItem(getAuxiliaryRes(), getAuxiliaryRes(), (int) R.string.pref_camera_auxiliary_close, "close"));
            this.mItems.add(new ComponentDataItem(getFocusPeakRes(), getFocusPeakRes(), (int) R.string.pref_camera_auxiliary_focus_peak, A_FOCUS_PEAK));
            this.mItems.add(new ComponentDataItem(getExposurceFeedbackRes(), getExposurceFeedbackRes(), (int) R.string.pref_camera_auxiliary_exposure_feedback, "exposure_feedback"));
        }
        return this.mItems;
    }
}
