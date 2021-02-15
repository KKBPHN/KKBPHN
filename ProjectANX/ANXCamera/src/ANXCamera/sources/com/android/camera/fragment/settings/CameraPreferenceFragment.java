package com.android.camera.fragment.settings;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.camera.ActivityLauncher;
import com.android.camera.AudioManagerAudioDeviceCallback;
import com.android.camera.AudioManagerAudioDeviceCallback.OnAudioDeviceChangeListener;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraSettings;
import com.android.camera.PhotoAssistanceTipsActivity;
import com.android.camera.ProfessionalDisplayActivity;
import com.android.camera.R;
import com.android.camera.RotateDialogController;
import com.android.camera.SettingUiState;
import com.android.camera.SoundSettingActivity;
import com.android.camera.ThermalHelper;
import com.android.camera.Util;
import com.android.camera.VideoDenoiseActivity;
import com.android.camera.WatermarkActivity;
import com.android.camera.customization.CustomizationActivity;
import com.android.camera.customization.ShutterSound;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.config.DataItemConfig;
import com.android.camera.data.data.global.DataItemGlobal;
import com.android.camera.lib.compatibility.util.CompatibilityUtils.PackageInstallerListener;
import com.android.camera.log.Log;
import com.android.camera.permission.PermissionManager;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.FeatureName;
import com.android.camera.statistic.MistatsConstants.Manual;
import com.android.camera.statistic.MistatsConstants.Setting;
import com.android.camera.statistic.MistatsConstants.VideoAttr;
import com.android.camera.statistic.MistatsWrapper;
import com.android.camera.storage.PriorityStorageBroadcastReceiver;
import com.android.camera.storage.Storage;
import com.android.camera.ui.PreviewListPreference;
import com.android.camera.ui.ValuePreference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class CameraPreferenceFragment extends BasePreferenceFragment {
    public static final String INTENT_TYPE = "intent_type";
    public static final String INTENT_VIDEO_QUALITY = "intent_video_quality";
    public static final String PREF_KEY_POPUP_CAMERA = "pref_popup_camera";
    public static final String PREF_KEY_PRIVACY = "pref_privacy";
    public static final String PREF_KEY_RESTORE = "pref_restore";
    public static final String REMOVE_KEYS = "remove_keys";
    public static final String TAG = "CameraPreferenceFragment";
    /* access modifiers changed from: private */
    public AlertDialog mAlertDialog;
    /* access modifiers changed from: private */
    public boolean mAllowLocationAccess = false;
    /* access modifiers changed from: private */
    public PackageInstallerListener mAppInstalledListener = new PackageInstallerListener() {
        public void onPackageInstalled(String str, boolean z) {
            if (z && TextUtils.equals(str, "com.xiaomi.scanner")) {
                final CheckBoxPreference checkBoxPreference = (CheckBoxPreference) CameraPreferenceFragment.this.mPreferenceGroup.findPreference(CameraSettings.KEY_SCAN_QRCODE);
                if (checkBoxPreference != null) {
                    CameraPreferenceFragment.this.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            checkBoxPreference.setChecked(true);
                            CameraPreferenceFragment.this.onPreferenceChange(checkBoxPreference, Boolean.TRUE);
                        }
                    });
                }
            }
        }
    };
    private OnAudioDeviceChangeListener mAudioDeviceChangeListener = new O000000o(this);
    private AudioManager mAudioManager;
    private AudioManagerAudioDeviceCallback mAudioManagerAudioDeviceCallback;
    private Preference mCustomization;
    /* access modifiers changed from: private */
    public AlertDialog mDoubleConfirmActionChooseDialog = null;
    private int mFromWhere;
    private boolean mGoToActivity;
    private boolean mHasReset;
    private boolean mIsFrontCamera;
    private boolean mKeyguardSecureLocked = false;
    /* access modifiers changed from: private */
    public AlertDialog mLocationCTADialog = null;
    private Preference mNoiseSetting;
    private AlertDialog mPermissionNotAskDialog = null;
    private Preference mPhotoAssistanceTips;
    protected PreferenceScreen mPreferenceGroup;
    private Preference mProfessionDisplay;
    /* access modifiers changed from: private */
    public AlertDialog mScanAlertDialog;
    private Preference mVideoDenoise;
    private int mVideoIntentQuality;
    private Preference mWatermark;

    private void bringUpDoubleConfirmDlg(final Preference preference, final String str) {
        if (this.mDoubleConfirmActionChooseDialog == null) {
            final boolean snapBoolValue = getSnapBoolValue(str);
            this.mDoubleConfirmActionChooseDialog = RotateDialogController.showSystemAlertDialog(getContext(), getString(R.string.title_snap_double_confirm), getString(R.string.message_snap_double_confirm), getString(R.string.snap_confirmed), new Runnable() {
                public void run() {
                    String str = CameraPreferenceFragment.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("bringUpDoubleConfirmDlg onClick pref_camera_snap_key:");
                    sb.append(str);
                    Log.u(str, sb.toString());
                    MistatsWrapper.settingClickEvent(CameraSettings.KEY_CAMERA_SNAP, str);
                    Preference preference = preference;
                    if (preference instanceof CheckBoxPreference) {
                        ((CheckBoxPreference) preference).setChecked(snapBoolValue);
                    } else if (preference instanceof PreviewListPreference) {
                        ((PreviewListPreference) preference).setValue(str);
                    }
                    Secure.putString(CameraPreferenceFragment.this.getActivity().getContentResolver(), "key_long_press_volume_down", CameraSettings.getMiuiSettingsKeyForStreetSnap(str));
                }
            }, null, null, getString(R.string.snap_cancel), new Runnable() {
                public void run() {
                    Log.u(CameraPreferenceFragment.TAG, "bringUpDoubleConfirmDlg onClick negative");
                    CameraPreferenceFragment.this.mDoubleConfirmActionChooseDialog.dismiss();
                }
            });
            this.mDoubleConfirmActionChooseDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    CameraPreferenceFragment.this.mDoubleConfirmActionChooseDialog = null;
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void closeLocationPreference() {
        ((CheckBoxPreference) this.mPreferenceGroup.findPreference(CameraSettings.KEY_RECORD_LOCATION)).setChecked(false);
        CameraSettings.updateRecordLocationPreference(false);
    }

    private void filterByDeviceCapability() {
    }

    /* JADX WARNING: Code restructure failed: missing block: B:145:0x03f3, code lost:
        if (r9.mIsFrontCamera == false) goto L_0x03bc;
     */
    /* JADX WARNING: Removed duplicated region for block: B:101:0x02df  */
    /* JADX WARNING: Removed duplicated region for block: B:104:0x02f0  */
    /* JADX WARNING: Removed duplicated region for block: B:112:0x0359  */
    /* JADX WARNING: Removed duplicated region for block: B:115:0x036a  */
    /* JADX WARNING: Removed duplicated region for block: B:118:0x037e  */
    /* JADX WARNING: Removed duplicated region for block: B:121:0x0389  */
    /* JADX WARNING: Removed duplicated region for block: B:124:0x0395  */
    /* JADX WARNING: Removed duplicated region for block: B:127:0x03a5  */
    /* JADX WARNING: Removed duplicated region for block: B:136:0x03c6  */
    /* JADX WARNING: Removed duplicated region for block: B:148:0x0400  */
    /* JADX WARNING: Removed duplicated region for block: B:151:0x0420  */
    /* JADX WARNING: Removed duplicated region for block: B:153:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x017c  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0189  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0196  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x01a3  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x01b4  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x01c1  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x01d9  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x01ec  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x01fb  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x020a  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0219  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x022d  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x0253  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x025e  */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x0271  */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x027e  */
    /* JADX WARNING: Removed duplicated region for block: B:98:0x0289  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void filterByDeviceID() {
        boolean z;
        String str;
        String str2;
        String str3;
        String str4;
        boolean isSupportedOpticalZoom;
        boolean OOo000O;
        boolean isSupportQuickShot;
        int i;
        PreferenceScreen preferenceScreen;
        if (CameraSettings.getRetainCameraModeSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
            removePreference(this.mPreferenceGroup, CameraSettings.KEY_RETAIN_CAMERA_MODE);
        }
        if (CameraSettings.getFocusShootSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
            removePreference(this.mPreferenceGroup, "pref_camera_focus_shoot_key");
        }
        dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_MOVIE_SOLID, CameraSettings.getMovieSolidSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera, this.mVideoIntentQuality));
        dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_WIND_DENOISE, CameraSettings.getWindDenoiseSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera));
        dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_VIDEO_DENOISE, CameraSettings.getVideoDenoiseSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera));
        dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_AI_SHUTTER, CameraSettings.getAiShutterSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera));
        SettingUiState videoTagSettingNeedRemove = CameraSettings.getVideoTagSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera);
        videoTagSettingNeedRemove.toString("videoTagSetting");
        dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_VIDEO_TAG, videoTagSettingNeedRemove);
        SettingUiState autoHibernationSettingNeedRemove = CameraSettings.getAutoHibernationSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera);
        PreferenceScreen preferenceScreen2 = this.mPreferenceGroup;
        String str5 = CameraSettings.KEY_AUTO_HIBERNATION;
        Preference findPreference = preferenceScreen2.findPreference(str5);
        int i2 = 0;
        if (findPreference != null) {
            findPreference.setSummary((CharSequence) String.format(getString(R.string.pref_camera_auto_hibernation_description), new Object[]{Integer.valueOf(3)}));
        }
        dealPreferenceUiState(this.mPreferenceGroup, str5, autoHibernationSettingNeedRemove);
        PreferenceScreen preferenceScreen3 = this.mPreferenceGroup;
        String str6 = CameraSettings.KEY_VIDEO_DYNAMIC_FRAME_RATE;
        preferenceScreen3.findPreference(str6).setSummary((CharSequence) String.format(getResources().getString(R.string.pref_camera_dynamic_frame_rate_summary), new Object[]{Integer.valueOf(60), Integer.valueOf(33)}));
        dealPreferenceUiState(this.mPreferenceGroup, str6, CameraSettings.getVideoDynamic60fpsSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera));
        boolean dualCameraWaterMarkState = CameraSettings.getDualCameraWaterMarkState(this.mFromWhere, this.mIsFrontCamera);
        boolean timeWaterMarkState = CameraSettings.getTimeWaterMarkState(this.mFromWhere, this.mIsFrontCamera);
        boolean customWaterMarkState = CameraSettings.getCustomWaterMarkState(this.mFromWhere, this.mIsFrontCamera);
        int i3 = this.mFromWhere;
        if (!(i3 == 186 || i3 == 166 || i3 == 176 || CameraSettings.isInAllRecordModeSet(i3))) {
            int i4 = this.mFromWhere;
            if (!(i4 == 210 || i4 == 185 || i4 == 213 || i4 == 187 || i4 == 205)) {
                z = false;
                str = "pref_time_watermark_key";
                str2 = "pref_dualcamera_watermark_key";
                str3 = "pref_watermark_key";
                if ((!timeWaterMarkState && dualCameraWaterMarkState) || z) {
                    removePreference(this.mPreferenceGroup, str3);
                    removePreference(this.mPreferenceGroup, str2);
                } else if (timeWaterMarkState && dualCameraWaterMarkState) {
                    removePreference(this.mPreferenceGroup, str3);
                    removePreference(this.mPreferenceGroup, str2);
                    if (!C0124O00000oO.Oo00o0O()) {
                    }
                    if (!C0124O00000oO.Oo00OoO()) {
                    }
                    if (!Storage.hasSecondaryStorage()) {
                    }
                    if (!C0124O00000oO.Oo00Oo0()) {
                    }
                    if (CameraSettings.getLongPressShutterSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
                    }
                    if (!C0124O00000oO.Oo00o()) {
                    }
                    removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_ASD_NIGHT);
                    if (CameraSettings.getCameraSnapSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
                    }
                    if (CameraSettings.getCameraSoundSettingNeedRemove(getContext(), this.mFromWhere)) {
                    }
                    if (CameraSettings.getCameraProfessionalDisplayNeedRemove(this.mFromWhere)) {
                    }
                    if (CameraSettings.getCameraOnlyVideoHistogramNeedRemove(this.mFromWhere)) {
                    }
                    if (CameraSettings.getCameraPhotoHistogramNeedRemove(this.mFromWhere)) {
                    }
                    removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_GROUPSHOT_PRIMITIVE);
                    if (!CameraSettings.isSupportedPortrait()) {
                    }
                    boolean isSupportedOpticalZoom2 = C0124O00000oO.isSupportedOpticalZoom();
                    str4 = CameraSettings.KEY_CAMERA_DUAL_ENABLE;
                    removePreference(this.mPreferenceGroup, str4);
                    isSupportedOpticalZoom = C0124O00000oO.isSupportedOpticalZoom();
                    String str7 = CameraSettings.KEY_CAMERA_DUAL_SAT_ENABLE;
                    if (!isSupportedOpticalZoom) {
                    }
                    if (!C0124O00000oO.isSupportSuperResolution()) {
                    }
                    OOo000O = C0122O00000o.instance().OOo000O();
                    String str8 = CameraSettings.KEY_CAMERA_PARALLEL_PROCESS_ENABLE;
                    if (!OOo000O) {
                    }
                    isSupportQuickShot = CameraSettings.isSupportQuickShot();
                    String str9 = CameraSettings.KEY_CAMERA_QUICK_SHOT_ENABLE;
                    if (!isSupportQuickShot) {
                    }
                    if (C0124O00000oO.Oo0O0O0()) {
                    }
                    if (CameraSettings.getCameraProximityLockSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
                    }
                    if (CameraSettings.getFingerprintCaptureSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
                    }
                    SettingUiState normalWideLDCNeedRemove = CameraSettings.getNormalWideLDCNeedRemove(this.mFromWhere, this.mIsFrontCamera);
                    normalWideLDCNeedRemove.toString("KEY_NORMAL_WIDE_LDC");
                    dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_NORMAL_WIDE_LDC, normalWideLDCNeedRemove);
                    dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_ULTRA_WIDE_LDC, CameraSettings.getUltraWideLDCNeedRemove(this.mFromWhere, this.mIsFrontCamera));
                    dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_HIGH_QUALITY_PREFERRED, CameraSettings.getQualityPreferredNeedRemove(this.mFromWhere, this.mIsFrontCamera));
                    dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_JPEG_QUALITY, CameraSettings.getJpegQualityNeedRemove(this.mFromWhere, this.mIsFrontCamera));
                    removePreference(this.mPreferenceGroup, CameraSettings.KEY_ULTRA_WIDE_VIDEO_LDC);
                    if (CameraSettings.getScanQrcodeSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
                    }
                    if (!C0122O00000o.instance().OO0OOOo()) {
                    }
                    removeIncompatibleAdvancePreference();
                    if (C0122O00000o.instance().OOOO0oO()) {
                    }
                    if (C0122O00000o.instance().OOo0o0()) {
                    }
                    if (C0122O00000o.instance().OOO0o00()) {
                    }
                    i = this.mFromWhere;
                    String str10 = CameraSettings.KEY_PHOTO_ASSISTANCE_TIPS;
                    String str11 = CameraSettings.KEY_CAMERA_LYING_TIP_SWITCH;
                    String str12 = CameraSettings.KEY_LENS_DIRTY_TIP;
                    String str13 = CameraSettings.KEY_PICTURE_FLAW_TIP;
                    if (i != 163) {
                    }
                    preferenceScreen = this.mPreferenceGroup;
                    if (i2 <= 1) {
                    }
                    removePreference(preferenceScreen, str13);
                    removePreference(this.mPreferenceGroup, str12);
                    removePreference(this.mPreferenceGroup, str11);
                    if (!C0122O00000o.instance().OOoOo()) {
                    }
                    dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_VIDEO_ENCODER, CameraSettings.isH265EncoderNeedRemove(this.mFromWhere, this.mIsFrontCamera));
                    if (C0122O00000o.instance().OOOOoo()) {
                    }
                } else if (timeWaterMarkState || !customWaterMarkState || dualCameraWaterMarkState) {
                    removePreference(this.mPreferenceGroup, str2);
                    removePreference(this.mPreferenceGroup, str);
                    SettingUiState waterMarkSettingNeedRemove = CameraSettings.getWaterMarkSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera);
                    waterMarkSettingNeedRemove.toString(str3);
                    dealPreferenceUiState(this.mPreferenceGroup, str3, waterMarkSettingNeedRemove);
                    if (!C0124O00000oO.Oo00o0O()) {
                        removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_SOUND);
                    }
                    if (!C0124O00000oO.Oo00OoO()) {
                        removePreference(this.mPreferenceGroup, CameraSettings.KEY_RECORD_LOCATION);
                    }
                    if (!Storage.hasSecondaryStorage()) {
                        removePreference(this.mPreferenceGroup, CameraSettings.KEY_PRIORITY_STORAGE);
                    }
                    if (!C0124O00000oO.Oo00Oo0()) {
                        removePreference(this.mPreferenceGroup, CameraSettings.KEY_AUTO_CHROMA_FLASH);
                    }
                    if (CameraSettings.getLongPressShutterSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
                        removePreference(this.mPreferenceGroup, CameraSettings.KEY_LONG_PRESS_SHUTTER_FEATURE);
                    }
                    if (!C0124O00000oO.Oo00o()) {
                        removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAPTURE_WHEN_STABLE);
                    }
                    removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_ASD_NIGHT);
                    if (CameraSettings.getCameraSnapSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
                        removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_SNAP);
                    }
                    if (CameraSettings.getCameraSoundSettingNeedRemove(getContext(), this.mFromWhere)) {
                        removePreference(this.mPreferenceGroup, CameraSettings.KEY_SOUND_SETTING);
                    }
                    if (CameraSettings.getCameraProfessionalDisplayNeedRemove(this.mFromWhere)) {
                        removePreference(this.mPreferenceGroup, CameraSettings.KEY_PROFESSION_DISPLAY);
                    }
                    if (CameraSettings.getCameraOnlyVideoHistogramNeedRemove(this.mFromWhere)) {
                        removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_PRO_ONLY_VIDEO_HISTOGRAM);
                    }
                    if (CameraSettings.getCameraPhotoHistogramNeedRemove(this.mFromWhere)) {
                        removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_PRO_HISTOGRAM);
                    }
                    removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_GROUPSHOT_PRIMITIVE);
                    if (!CameraSettings.isSupportedPortrait()) {
                        removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_PORTRAIT_WITH_FACEBEAUTY);
                    }
                    boolean isSupportedOpticalZoom22 = C0124O00000oO.isSupportedOpticalZoom();
                    str4 = CameraSettings.KEY_CAMERA_DUAL_ENABLE;
                    if (!isSupportedOpticalZoom22 && !C0122O00000o.instance().OOOo00o()) {
                        removePreference(this.mPreferenceGroup, str4);
                    }
                    isSupportedOpticalZoom = C0124O00000oO.isSupportedOpticalZoom();
                    String str72 = CameraSettings.KEY_CAMERA_DUAL_SAT_ENABLE;
                    if (!isSupportedOpticalZoom) {
                        removePreference(this.mPreferenceGroup, str72);
                    }
                    if (!C0124O00000oO.isSupportSuperResolution()) {
                        removePreference(this.mPreferenceGroup, "pref_camera_super_resolution_key");
                    }
                    OOo000O = C0122O00000o.instance().OOo000O();
                    String str82 = CameraSettings.KEY_CAMERA_PARALLEL_PROCESS_ENABLE;
                    if (!OOo000O) {
                        removePreference(this.mPreferenceGroup, str82);
                    }
                    isSupportQuickShot = CameraSettings.isSupportQuickShot();
                    String str92 = CameraSettings.KEY_CAMERA_QUICK_SHOT_ENABLE;
                    if (!isSupportQuickShot) {
                        removePreference(this.mPreferenceGroup, str92);
                    }
                    if (C0124O00000oO.Oo0O0O0()) {
                        removePreference(this.mPreferenceGroup, CameraSettings.KEY_FACE_DETECTION);
                        removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_FACE_DETECTION_AUTO_HIDDEN);
                        removePreference(this.mPreferenceGroup, str82);
                        removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_QUICK_SHOT_ANIM_ENABLE);
                        removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_TOUCH_FOCUS_DELAY_ENABLE);
                        removePreference(this.mPreferenceGroup, str92);
                        removePreference(this.mPreferenceGroup, str4);
                        removePreference(this.mPreferenceGroup, str72);
                        removePreference(this.mPreferenceGroup, CameraSettings.KEY_QC_SHARPNESS);
                        removePreference(this.mPreferenceGroup, CameraSettings.KEY_QC_CONTRAST);
                        removePreference(this.mPreferenceGroup, CameraSettings.KEY_QC_SATURATION);
                        removePreference(this.mPreferenceGroup, CameraSettings.KEY_AUTOEXPOSURE);
                    }
                    if (CameraSettings.getCameraProximityLockSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
                        removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_PROXIMITY_LOCK);
                    }
                    if (CameraSettings.getFingerprintCaptureSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
                        removePreference(this.mPreferenceGroup, CameraSettings.KEY_FINGERPRINT_CAPTURE);
                    }
                    SettingUiState normalWideLDCNeedRemove2 = CameraSettings.getNormalWideLDCNeedRemove(this.mFromWhere, this.mIsFrontCamera);
                    normalWideLDCNeedRemove2.toString("KEY_NORMAL_WIDE_LDC");
                    dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_NORMAL_WIDE_LDC, normalWideLDCNeedRemove2);
                    dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_ULTRA_WIDE_LDC, CameraSettings.getUltraWideLDCNeedRemove(this.mFromWhere, this.mIsFrontCamera));
                    dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_HIGH_QUALITY_PREFERRED, CameraSettings.getQualityPreferredNeedRemove(this.mFromWhere, this.mIsFrontCamera));
                    dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_JPEG_QUALITY, CameraSettings.getJpegQualityNeedRemove(this.mFromWhere, this.mIsFrontCamera));
                    if (!CameraSettings.shouldUltraWideVideoLDCBeVisibleInMode(this.mFromWhere) || !CameraSettings.isUltraWideConfigOpen(this.mFromWhere)) {
                        removePreference(this.mPreferenceGroup, CameraSettings.KEY_ULTRA_WIDE_VIDEO_LDC);
                    }
                    if (CameraSettings.getScanQrcodeSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
                        removePreference(this.mPreferenceGroup, CameraSettings.KEY_SCAN_QRCODE);
                    }
                    if (!C0122O00000o.instance().OO0OOOo()) {
                        removePreference(this.mPreferenceGroup, PREF_KEY_POPUP_CAMERA);
                    }
                    removeIncompatibleAdvancePreference();
                    if (C0122O00000o.instance().OOOO0oO()) {
                        i2 = 1;
                    }
                    if (C0122O00000o.instance().OOo0o0()) {
                        i2++;
                    }
                    if (C0122O00000o.instance().OOO0o00()) {
                        i2++;
                    }
                    i = this.mFromWhere;
                    String str102 = CameraSettings.KEY_PHOTO_ASSISTANCE_TIPS;
                    String str112 = CameraSettings.KEY_CAMERA_LYING_TIP_SWITCH;
                    String str122 = CameraSettings.KEY_LENS_DIRTY_TIP;
                    String str132 = CameraSettings.KEY_PICTURE_FLAW_TIP;
                    if ((i != 163 || i == 165) && !this.mIsFrontCamera) {
                        preferenceScreen = this.mPreferenceGroup;
                        if (i2 <= 1) {
                            removePreference(preferenceScreen, str102);
                            if (!C0122O00000o.instance().OOOO0oO()) {
                                removePreference(this.mPreferenceGroup, str132);
                            }
                            if (!C0122O00000o.instance().OOO0o00()) {
                                removePreference(this.mPreferenceGroup, str122);
                            }
                            if (!C0122O00000o.instance().OOo0o0()) {
                            }
                            if (!C0122O00000o.instance().OOoOo()) {
                                removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_VIDEO_SAT_ENABLE);
                            }
                            dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_VIDEO_ENCODER, CameraSettings.isH265EncoderNeedRemove(this.mFromWhere, this.mIsFrontCamera));
                            if (C0122O00000o.instance().OOOOoo()) {
                                removePreference(this.mPreferenceGroup, CameraSettings.KEY_CUSTOMIZATION);
                                return;
                            }
                            return;
                        }
                    } else {
                        removePreference(this.mPreferenceGroup, str102);
                        preferenceScreen = this.mPreferenceGroup;
                    }
                    removePreference(preferenceScreen, str132);
                    removePreference(this.mPreferenceGroup, str122);
                    removePreference(this.mPreferenceGroup, str112);
                    if (!C0122O00000o.instance().OOoOo()) {
                    }
                    dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_VIDEO_ENCODER, CameraSettings.isH265EncoderNeedRemove(this.mFromWhere, this.mIsFrontCamera));
                    if (C0122O00000o.instance().OOOOoo()) {
                    }
                } else {
                    removePreference(this.mPreferenceGroup, str3);
                }
                removePreference(this.mPreferenceGroup, str);
                if (!C0124O00000oO.Oo00o0O()) {
                }
                if (!C0124O00000oO.Oo00OoO()) {
                }
                if (!Storage.hasSecondaryStorage()) {
                }
                if (!C0124O00000oO.Oo00Oo0()) {
                }
                if (CameraSettings.getLongPressShutterSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
                }
                if (!C0124O00000oO.Oo00o()) {
                }
                removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_ASD_NIGHT);
                if (CameraSettings.getCameraSnapSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
                }
                if (CameraSettings.getCameraSoundSettingNeedRemove(getContext(), this.mFromWhere)) {
                }
                if (CameraSettings.getCameraProfessionalDisplayNeedRemove(this.mFromWhere)) {
                }
                if (CameraSettings.getCameraOnlyVideoHistogramNeedRemove(this.mFromWhere)) {
                }
                if (CameraSettings.getCameraPhotoHistogramNeedRemove(this.mFromWhere)) {
                }
                removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_GROUPSHOT_PRIMITIVE);
                if (!CameraSettings.isSupportedPortrait()) {
                }
                boolean isSupportedOpticalZoom222 = C0124O00000oO.isSupportedOpticalZoom();
                str4 = CameraSettings.KEY_CAMERA_DUAL_ENABLE;
                removePreference(this.mPreferenceGroup, str4);
                isSupportedOpticalZoom = C0124O00000oO.isSupportedOpticalZoom();
                String str722 = CameraSettings.KEY_CAMERA_DUAL_SAT_ENABLE;
                if (!isSupportedOpticalZoom) {
                }
                if (!C0124O00000oO.isSupportSuperResolution()) {
                }
                OOo000O = C0122O00000o.instance().OOo000O();
                String str822 = CameraSettings.KEY_CAMERA_PARALLEL_PROCESS_ENABLE;
                if (!OOo000O) {
                }
                isSupportQuickShot = CameraSettings.isSupportQuickShot();
                String str922 = CameraSettings.KEY_CAMERA_QUICK_SHOT_ENABLE;
                if (!isSupportQuickShot) {
                }
                if (C0124O00000oO.Oo0O0O0()) {
                }
                if (CameraSettings.getCameraProximityLockSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
                }
                if (CameraSettings.getFingerprintCaptureSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
                }
                SettingUiState normalWideLDCNeedRemove22 = CameraSettings.getNormalWideLDCNeedRemove(this.mFromWhere, this.mIsFrontCamera);
                normalWideLDCNeedRemove22.toString("KEY_NORMAL_WIDE_LDC");
                dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_NORMAL_WIDE_LDC, normalWideLDCNeedRemove22);
                dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_ULTRA_WIDE_LDC, CameraSettings.getUltraWideLDCNeedRemove(this.mFromWhere, this.mIsFrontCamera));
                dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_HIGH_QUALITY_PREFERRED, CameraSettings.getQualityPreferredNeedRemove(this.mFromWhere, this.mIsFrontCamera));
                dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_JPEG_QUALITY, CameraSettings.getJpegQualityNeedRemove(this.mFromWhere, this.mIsFrontCamera));
                removePreference(this.mPreferenceGroup, CameraSettings.KEY_ULTRA_WIDE_VIDEO_LDC);
                if (CameraSettings.getScanQrcodeSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
                }
                if (!C0122O00000o.instance().OO0OOOo()) {
                }
                removeIncompatibleAdvancePreference();
                if (C0122O00000o.instance().OOOO0oO()) {
                }
                if (C0122O00000o.instance().OOo0o0()) {
                }
                if (C0122O00000o.instance().OOO0o00()) {
                }
                i = this.mFromWhere;
                String str1022 = CameraSettings.KEY_PHOTO_ASSISTANCE_TIPS;
                String str1122 = CameraSettings.KEY_CAMERA_LYING_TIP_SWITCH;
                String str1222 = CameraSettings.KEY_LENS_DIRTY_TIP;
                String str1322 = CameraSettings.KEY_PICTURE_FLAW_TIP;
                if (i != 163) {
                }
                preferenceScreen = this.mPreferenceGroup;
                if (i2 <= 1) {
                }
                removePreference(preferenceScreen, str1322);
                removePreference(this.mPreferenceGroup, str1222);
                removePreference(this.mPreferenceGroup, str1122);
                if (!C0122O00000o.instance().OOoOo()) {
                }
                dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_VIDEO_ENCODER, CameraSettings.isH265EncoderNeedRemove(this.mFromWhere, this.mIsFrontCamera));
                if (C0122O00000o.instance().OOOOoo()) {
                }
            }
        }
        z = true;
        str = "pref_time_watermark_key";
        str2 = "pref_dualcamera_watermark_key";
        str3 = "pref_watermark_key";
        if (!timeWaterMarkState) {
        }
        if (timeWaterMarkState) {
        }
        if (timeWaterMarkState) {
        }
        removePreference(this.mPreferenceGroup, str2);
        removePreference(this.mPreferenceGroup, str);
        SettingUiState waterMarkSettingNeedRemove2 = CameraSettings.getWaterMarkSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera);
        waterMarkSettingNeedRemove2.toString(str3);
        dealPreferenceUiState(this.mPreferenceGroup, str3, waterMarkSettingNeedRemove2);
        if (!C0124O00000oO.Oo00o0O()) {
        }
        if (!C0124O00000oO.Oo00OoO()) {
        }
        if (!Storage.hasSecondaryStorage()) {
        }
        if (!C0124O00000oO.Oo00Oo0()) {
        }
        if (CameraSettings.getLongPressShutterSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
        }
        if (!C0124O00000oO.Oo00o()) {
        }
        removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_ASD_NIGHT);
        if (CameraSettings.getCameraSnapSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
        }
        if (CameraSettings.getCameraSoundSettingNeedRemove(getContext(), this.mFromWhere)) {
        }
        if (CameraSettings.getCameraProfessionalDisplayNeedRemove(this.mFromWhere)) {
        }
        if (CameraSettings.getCameraOnlyVideoHistogramNeedRemove(this.mFromWhere)) {
        }
        if (CameraSettings.getCameraPhotoHistogramNeedRemove(this.mFromWhere)) {
        }
        removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_GROUPSHOT_PRIMITIVE);
        if (!CameraSettings.isSupportedPortrait()) {
        }
        boolean isSupportedOpticalZoom2222 = C0124O00000oO.isSupportedOpticalZoom();
        str4 = CameraSettings.KEY_CAMERA_DUAL_ENABLE;
        removePreference(this.mPreferenceGroup, str4);
        isSupportedOpticalZoom = C0124O00000oO.isSupportedOpticalZoom();
        String str7222 = CameraSettings.KEY_CAMERA_DUAL_SAT_ENABLE;
        if (!isSupportedOpticalZoom) {
        }
        if (!C0124O00000oO.isSupportSuperResolution()) {
        }
        OOo000O = C0122O00000o.instance().OOo000O();
        String str8222 = CameraSettings.KEY_CAMERA_PARALLEL_PROCESS_ENABLE;
        if (!OOo000O) {
        }
        isSupportQuickShot = CameraSettings.isSupportQuickShot();
        String str9222 = CameraSettings.KEY_CAMERA_QUICK_SHOT_ENABLE;
        if (!isSupportQuickShot) {
        }
        if (C0124O00000oO.Oo0O0O0()) {
        }
        if (CameraSettings.getCameraProximityLockSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
        }
        if (CameraSettings.getFingerprintCaptureSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
        }
        SettingUiState normalWideLDCNeedRemove222 = CameraSettings.getNormalWideLDCNeedRemove(this.mFromWhere, this.mIsFrontCamera);
        normalWideLDCNeedRemove222.toString("KEY_NORMAL_WIDE_LDC");
        dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_NORMAL_WIDE_LDC, normalWideLDCNeedRemove222);
        dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_ULTRA_WIDE_LDC, CameraSettings.getUltraWideLDCNeedRemove(this.mFromWhere, this.mIsFrontCamera));
        dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_HIGH_QUALITY_PREFERRED, CameraSettings.getQualityPreferredNeedRemove(this.mFromWhere, this.mIsFrontCamera));
        dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_JPEG_QUALITY, CameraSettings.getJpegQualityNeedRemove(this.mFromWhere, this.mIsFrontCamera));
        removePreference(this.mPreferenceGroup, CameraSettings.KEY_ULTRA_WIDE_VIDEO_LDC);
        if (CameraSettings.getScanQrcodeSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
        }
        if (!C0122O00000o.instance().OO0OOOo()) {
        }
        removeIncompatibleAdvancePreference();
        if (C0122O00000o.instance().OOOO0oO()) {
        }
        if (C0122O00000o.instance().OOo0o0()) {
        }
        if (C0122O00000o.instance().OOO0o00()) {
        }
        i = this.mFromWhere;
        String str10222 = CameraSettings.KEY_PHOTO_ASSISTANCE_TIPS;
        String str11222 = CameraSettings.KEY_CAMERA_LYING_TIP_SWITCH;
        String str12222 = CameraSettings.KEY_LENS_DIRTY_TIP;
        String str13222 = CameraSettings.KEY_PICTURE_FLAW_TIP;
        if (i != 163) {
        }
        preferenceScreen = this.mPreferenceGroup;
        if (i2 <= 1) {
        }
        removePreference(preferenceScreen, str13222);
        removePreference(this.mPreferenceGroup, str12222);
        removePreference(this.mPreferenceGroup, str11222);
        if (!C0122O00000o.instance().OOoOo()) {
        }
        dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_VIDEO_ENCODER, CameraSettings.isH265EncoderNeedRemove(this.mFromWhere, this.mIsFrontCamera));
        if (C0122O00000o.instance().OOOOoo()) {
        }
    }

    private void filterByFrom() {
        SettingUiState isHeicImageFormatSelectable = CameraSettings.isHeicImageFormatSelectable(this.mFromWhere, this.mIsFrontCamera);
        String str = CameraSettings.KEY_HEIC_FORMAT;
        isHeicImageFormatSelectable.toString(str);
        dealPreferenceUiState(this.mPreferenceGroup, str, isHeicImageFormatSelectable);
        if (CameraSettings.getVolumeCameraSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
            removePreference(this.mPreferenceGroup, CameraSettings.KEY_VOLUME_CAMERA_FUNCTION);
        }
        if (CameraSettings.getVolumeVideoSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
            removePreference(this.mPreferenceGroup, CameraSettings.KEY_VOLUME_VIDEO_FUNCTION);
        }
        if (CameraSettings.getVolumeProVideoSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
            removePreference(this.mPreferenceGroup, CameraSettings.KEY_VOLUME_PRO_VIDEO_FUNCTION);
        }
        if (CameraSettings.getVolumeLiveSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
            removePreference(this.mPreferenceGroup, CameraSettings.KEY_VOLUME_LIVE_FUNCTION);
        }
        if (CameraSettings.getVideoTimeLapseFrameIntervalNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
            removeFromGroup(this.mPreferenceGroup, CameraSettings.KEY_VIDEO_TIME_LAPSE_FRAME_INTERVAL);
        }
        if (CameraSettings.getMirrorSettingUiNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
            removeFromGroup(this.mPreferenceGroup, CameraSettings.KEY_FRONT_MIRROR);
        }
        if (CameraSettings.isNearRangeUiNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
            removeFromGroup(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_NEAR_RANGE);
        }
        dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_HDR10_VIDEO_ENCODER, CameraSettings.getHDR10UiNeedRemove(this.mFromWhere, this.mIsFrontCamera));
        dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_HDR10PLUS_VIDEO_ENCODER, CameraSettings.getHDR10PlusUiNeedRemove(this.mFromWhere, this.mIsFrontCamera));
    }

    private void filterByPreference() {
        if (!Util.isLabOptionsVisible()) {
            removePreference(this.mPreferenceGroup, CameraSettings.KEY_FACE_DETECTION);
            removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_PORTRAIT_WITH_FACEBEAUTY);
            removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_FACE_DETECTION_AUTO_HIDDEN);
            removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_DUAL_ENABLE);
            removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_DUAL_SAT_ENABLE);
            removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_MFNR_SAT_ENABLE);
            removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_SR_ENABLE);
            removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_PARALLEL_PROCESS_ENABLE);
            removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_QUICK_SHOT_ANIM_ENABLE);
            removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_TOUCH_FOCUS_DELAY_ENABLE);
            removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_QUICK_SHOT_ENABLE);
            removePreference(this.mPreferenceGroup, CameraSettings.KEY_LIVE_STICKER_INTERNAL);
            removePreference(this.mPreferenceGroup, CameraSettings.KEY_AUTOEXPOSURE);
            removePreference(this.mPreferenceGroup, CameraSettings.KEY_VIDEO_AUTOEXPOSURE);
            removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_VIDEO_SAT_ENABLE);
            removePreference(this.mPreferenceGroup, CameraSettings.KEY_VIDEO_SHOW_FACE_VIEW);
        }
        if (CameraSettings.getLongPressViewFinderSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera, this.mKeyguardSecureLocked)) {
            removePreference(this.mPreferenceGroup, CameraSettings.KEY_LONG_PRESS_VIEWFINDER);
        }
        if (CameraSettings.getGoogleLensSuggestionsSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera)) {
            removePreference(this.mPreferenceGroup, CameraSettings.KEY_GOOGLE_LENS_SUGGESTIONS);
        }
    }

    private void filterGroup() {
        filterGroupIfEmpty(CameraSettings.KEY_CATEGORY_DEVICE_SETTING);
        filterGroupIfEmpty(CameraSettings.KEY_CATEGORY_ADVANCE_SETTING);
    }

    private void filterGroupIfEmpty(String str) {
        Preference findPreference = this.mPreferenceGroup.findPreference(str);
        if (findPreference != null && (findPreference instanceof PreferenceGroup) && ((PreferenceGroup) findPreference).getPreferenceCount() == 0) {
            removePreference(this.mPreferenceGroup, str);
        }
    }

    private String getFilterValue(PreviewListPreference previewListPreference, SharedPreferences sharedPreferences) {
        String value = previewListPreference.getValue();
        if (sharedPreferences == null) {
            return value;
        }
        if (value == null) {
            value = previewListPreference.getStoredDefaultValue();
        }
        String key = previewListPreference.getKey();
        char c = 65535;
        int hashCode = key.hashCode();
        if (hashCode != -44500048) {
            if (hashCode == 2006116105 && key.equals(CameraSettings.KEY_VOLUME_LIVE_FUNCTION)) {
                c = 1;
            }
        } else if (key.equals(CameraSettings.KEY_VOLUME_CAMERA_FUNCTION)) {
            c = 0;
        }
        String str = c != 0 ? c != 1 ? sharedPreferences.getString(previewListPreference.getKey(), value) : CameraSettings.getKeyVolumeLiveFunction(value) : CameraSettings.getVolumeCameraFunction(value);
        if (!Util.isStringValueContained((Object) str, previewListPreference.getEntryValues())) {
            Editor edit = sharedPreferences.edit();
            edit.putString(previewListPreference.getKey(), value);
            edit.apply();
        } else {
            value = str;
        }
        return value;
    }

    private boolean getSnapBoolValue(String str) {
        return str.equals(getString(R.string.pref_camera_snap_value_take_picture)) || str.equals(getString(R.string.pref_camera_snap_value_take_movie));
    }

    private String getSnapStringValue(boolean z) {
        return getString(z ? R.string.pref_camera_snap_value_take_picture : R.string.pref_camera_snap_value_off);
    }

    private void handleAiAudioUIState() {
        if (this.mPreferenceGroup != null) {
            dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_WIND_DENOISE, CameraSettings.getWindDenoiseSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera));
            dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_VIDEO_DENOISE, CameraSettings.getVideoDenoiseSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera));
        }
    }

    private void initializeActivity() {
        this.mPreferenceGroup = getPreferenceScreen();
        PreferenceScreen preferenceScreen = this.mPreferenceGroup;
        if (preferenceScreen != null) {
            preferenceScreen.removeAll();
        }
        addPreferencesFromResource(getPreferenceXml());
        this.mPreferenceGroup = getPreferenceScreen();
        if (this.mPreferenceGroup == null) {
            Log.e(TAG, "fail to init PreferenceGroup");
            getActivity().finish();
        }
        registerListener();
        filterByPreference();
        filterByFrom();
        filterByDeviceID();
        filterByIntent();
        filterGroup();
        PreferenceScreen preferenceScreen2 = this.mPreferenceGroup;
        String str = CameraSettings.KEY_CATEGORY_QUICK_SETTING;
        Preference findPreference = preferenceScreen2.findPreference(str);
        PreferenceScreen preferenceScreen3 = this.mPreferenceGroup;
        String str2 = CameraSettings.KEY_CATEGORY_MODULE_SETTING;
        Preference findPreference2 = preferenceScreen3.findPreference(str2);
        if (findPreference2 != null && (findPreference2 instanceof PreferenceGroup) && ((PreferenceGroup) findPreference2).getPreferenceCount() == 0) {
            removePreference(this.mPreferenceGroup, str2);
            if (findPreference != null && (findPreference instanceof PreferenceGroup)) {
                findPreference.setTitle((int) R.string.pref_module_settings);
            }
        } else if (findPreference != null && (findPreference instanceof PreferenceGroup)) {
            findPreference.setTitle((CharSequence) "");
        }
        filterGroupIfEmpty(str);
        updateEntries();
        updatePreferences(this.mPreferenceGroup, this.mPreferences);
        updateConflictPreference(null);
    }

    /* access modifiers changed from: private */
    public void installQRCodeReceiver() {
        new AsyncTask() {
            /* access modifiers changed from: protected */
            public Void doInBackground(Void... voidArr) {
                Log.u(CameraPreferenceFragment.TAG, "installQRCode...");
                Util.installPackage(CameraPreferenceFragment.this.getActivity(), "com.xiaomi.scanner", CameraPreferenceFragment.this.mAppInstalledListener, false, true);
                return null;
            }
        }.execute(new Void[0]);
    }

    private static HashMap readKeptValues(boolean z) {
        HashMap hashMap = new HashMap(6);
        DataItemGlobal dataItemGlobal = DataRepository.dataItemGlobal();
        String str = CameraSettings.KEY_CAMERA_FIRST_USE_PERMISSION_SHOWN;
        hashMap.put(str, Boolean.valueOf(dataItemGlobal.getBoolean(str, true)));
        if (z) {
            for (String str2 : DataItemGlobal.sUseHints) {
                if (DataRepository.dataItemGlobal().contains(str2)) {
                    hashMap.put(str2, Boolean.valueOf(DataRepository.dataItemGlobal().getBoolean(str2, false)));
                }
            }
        }
        return hashMap;
    }

    private void registerListener() {
        registerListener(this.mPreferenceGroup, this);
        Preference findPreference = this.mPreferenceGroup.findPreference(PREF_KEY_RESTORE);
        if (findPreference != null) {
            findPreference.setOnPreferenceClickListener(this);
        }
        Preference findPreference2 = this.mPreferenceGroup.findPreference(PREF_KEY_PRIVACY);
        if (findPreference2 != null) {
            findPreference2.setOnPreferenceClickListener(this);
        }
        Preference findPreference3 = this.mPreferenceGroup.findPreference(PREF_KEY_POPUP_CAMERA);
        if (findPreference3 != null) {
            findPreference3.setOnPreferenceClickListener(this);
        }
        Preference findPreference4 = this.mPreferenceGroup.findPreference(CameraSettings.KEY_PRIORITY_STORAGE);
        if (findPreference4 != null) {
            findPreference4.setOnPreferenceChangeListener(this);
        }
        Preference findPreference5 = this.mPreferenceGroup.findPreference(CameraSettings.KEY_FACE_DETECTION);
        if (findPreference5 != null) {
            findPreference5.setOnPreferenceClickListener(this);
        }
        Preference findPreference6 = this.mPreferenceGroup.findPreference(CameraSettings.KEY_SCAN_QRCODE);
        if (findPreference6 != null) {
            findPreference6.setOnPreferenceClickListener(this);
        }
        this.mWatermark = this.mPreferenceGroup.findPreference("pref_watermark_key");
        Preference preference = this.mWatermark;
        if (preference != null) {
            preference.setOnPreferenceClickListener(this);
        }
        this.mNoiseSetting = this.mPreferenceGroup.findPreference(CameraSettings.KEY_SOUND_SETTING);
        Preference preference2 = this.mNoiseSetting;
        if (preference2 != null) {
            preference2.setOnPreferenceClickListener(this);
        }
        this.mProfessionDisplay = this.mPreferenceGroup.findPreference(CameraSettings.KEY_PROFESSION_DISPLAY);
        Preference preference3 = this.mProfessionDisplay;
        if (preference3 != null) {
            preference3.setOnPreferenceClickListener(this);
        }
        this.mCustomization = this.mPreferenceGroup.findPreference(CameraSettings.KEY_CUSTOMIZATION);
        Preference preference4 = this.mCustomization;
        if (preference4 != null) {
            preference4.setOnPreferenceClickListener(this);
        }
        this.mPhotoAssistanceTips = this.mPreferenceGroup.findPreference(CameraSettings.KEY_PHOTO_ASSISTANCE_TIPS);
        Preference preference5 = this.mPhotoAssistanceTips;
        if (preference5 != null) {
            preference5.setOnPreferenceClickListener(this);
        }
        this.mVideoDenoise = this.mPreferenceGroup.findPreference(CameraSettings.KEY_VIDEO_DENOISE);
        Preference preference6 = this.mVideoDenoise;
        if (preference6 != null) {
            preference6.setOnPreferenceClickListener(this);
        }
    }

    private void removeIncompatibleAdvancePreference() {
        removePreference(this.mPreferenceGroup, CameraSettings.KEY_QC_CONTRAST);
        removePreference(this.mPreferenceGroup, CameraSettings.KEY_QC_SATURATION);
        removePreference(this.mPreferenceGroup, CameraSettings.KEY_QC_SHARPNESS);
    }

    private void removeNonVideoPreference() {
        removePreference(this.mPreferenceGroup, CameraSettings.KEY_CATEGORY_CAPTURE_SETTING);
    }

    public static void resetPreferences(boolean z) {
        HashMap readKeptValues = readKeptValues(z);
        DataItemGlobal dataItemGlobal = DataRepository.dataItemGlobal();
        int intentType = dataItemGlobal.getIntentType();
        dataItemGlobal.resetAll();
        ((DataItemConfig) DataRepository.provider().dataConfig(0, intentType)).resetAll();
        ((DataItemConfig) DataRepository.provider().dataConfig(1, intentType)).resetAll();
        DataRepository.dataItemLive().resetAll();
        DataRepository.dataItemRunning().clearArrayMap();
        DataRepository.getInstance().backUp().clearBackUp();
        rewriteKeptValues(readKeptValues);
        Util.generateWatermark2File();
    }

    private void resetSnapSetting() {
        String str = "key_long_press_volume_down";
        String string = Secure.getString(getActivity().getContentResolver(), str);
        if ("Street-snap-picture".equals(string) || "Street-snap-movie".equals(string)) {
            Secure.putString(getActivity().getContentResolver(), str, "none");
        }
    }

    private void resetTimeOutFlag() {
        if (!this.mHasReset) {
            DataRepository.dataItemGlobal().resetTimeOut();
        }
    }

    /* access modifiers changed from: private */
    public void restorePreferences() {
        Log.u(TAG, "restorePreferences onClick positive");
        this.mHasReset = true;
        resetPreferences(false);
        resetSnapSetting();
        ShutterSound.release();
        initializeActivity();
        PriorityStorageBroadcastReceiver.setPriorityStorage(getResources().getBoolean(R.bool.priority_storage));
        onSettingChanged(3);
    }

    private static void rewriteKeptValues(HashMap hashMap) {
        for (String str : hashMap.keySet()) {
            DataRepository.dataItemGlobal().putBoolean(str, ((Boolean) hashMap.get(str)).booleanValue());
        }
    }

    private void showCTADialog(final Fragment fragment) {
        if (this.mLocationCTADialog == null) {
            this.mLocationCTADialog = RotateDialogController.showCTADialog(getActivity(), getActivity().getString(R.string.user_agree), new Runnable() {
                public void run() {
                    Log.u(CameraPreferenceFragment.TAG, "onClick LocationAccess PositiveButton");
                    CameraStatUtils.trackCTADialogAgree();
                    CameraPreferenceFragment.this.mAllowLocationAccess = true;
                    CameraSettings.updateCTAPreference(true);
                }
            }, getActivity().getString(R.string.user_disagree), new Runnable() {
                public void run() {
                    Log.u(CameraPreferenceFragment.TAG, "onClick LocationAccess NegativeButton");
                    CameraStatUtils.trackCTADialogDisagree();
                    CameraPreferenceFragment.this.mAllowLocationAccess = false;
                    CameraSettings.updateCTAPreference(false);
                }
            }, getActivity().getLayoutInflater().inflate(R.layout.dialog_cta, null));
            this.mLocationCTADialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    String str = CameraPreferenceFragment.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onDismiss LocationAccess ");
                    sb.append(CameraPreferenceFragment.this.mAllowLocationAccess);
                    Log.u(str, sb.toString());
                    if (!CameraPreferenceFragment.this.mAllowLocationAccess) {
                        CameraPreferenceFragment.this.closeLocationPreference();
                    } else if (!PermissionManager.checkCameraLocationPermissions()) {
                        PermissionManager.requestCameraLocationPermissionsByFragment(fragment);
                    }
                    CameraPreferenceFragment.this.mLocationCTADialog.setOnDismissListener(null);
                    CameraPreferenceFragment.this.mLocationCTADialog = null;
                }
            });
        }
    }

    private void updateCameraSound() {
        String str = CameraSettings.KEY_CAMERA_SOUND;
        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference(str);
        if (checkBoxPreference != null) {
            checkBoxPreference.setChecked(DataRepository.dataItemGlobal().getBoolean(str, true));
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:118:0x025f, code lost:
        if (r4 != 182) goto L_0x0288;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:132:0x02a4, code lost:
        if (r3 != 188) goto L_0x02d4;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateEntries() {
        int i;
        Resources resources;
        PreferenceScreen preferenceScreen = this.mPreferenceGroup;
        String str = CameraSettings.KEY_ANTIBANDING;
        PreviewListPreference previewListPreference = (PreviewListPreference) preferenceScreen.findPreference(str);
        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) this.mPreferenceGroup.findPreference(CameraSettings.KEY_AUTO_CHROMA_FLASH);
        PreferenceScreen preferenceScreen2 = this.mPreferenceGroup;
        String str2 = CameraSettings.KEY_CAMERA_SNAP;
        PreviewListPreference previewListPreference2 = (PreviewListPreference) preferenceScreen2.findPreference(str2);
        PreviewListPreference previewListPreference3 = (PreviewListPreference) this.mPreferenceGroup.findPreference(CameraSettings.KEY_VOLUME_CAMERA_FUNCTION);
        PreviewListPreference previewListPreference4 = (PreviewListPreference) this.mPreferenceGroup.findPreference(CameraSettings.KEY_VOLUME_VIDEO_FUNCTION);
        PreviewListPreference previewListPreference5 = (PreviewListPreference) this.mPreferenceGroup.findPreference(CameraSettings.KEY_VOLUME_LIVE_FUNCTION);
        if (previewListPreference != null) {
            String defaultValueByKey = CameraSettings.getDefaultValueByKey(str);
            previewListPreference.setValue(defaultValueByKey);
            previewListPreference.setDefaultValue(defaultValueByKey);
        }
        if (checkBoxPreference != null) {
            checkBoxPreference.setChecked(getResources().getBoolean(R.bool.pref_camera_auto_chroma_flash_default));
        }
        if (previewListPreference2 != null && C0124O00000oO.Oo00oOO()) {
            String string = getString(R.string.pref_camera_snap_default);
            previewListPreference2.setDefaultValue(string);
            previewListPreference2.setValue(string);
            String str3 = "key_long_press_volume_down";
            String string2 = Secure.getString(getActivity().getContentResolver(), str3);
            if ("public_transportation_shortcuts".equals(string2) || "none".equals(string2)) {
                previewListPreference2.setValue(getString(R.string.pref_camera_snap_value_off));
            } else {
                String string3 = DataRepository.dataItemGlobal().getString(str2, null);
                if (string3 != null) {
                    Secure.putString(getActivity().getContentResolver(), str3, CameraSettings.getMiuiSettingsKeyForStreetSnap(string3));
                    DataRepository.dataItemGlobal().editor().remove(str2).apply();
                    previewListPreference2.setValue(string3);
                } else if ("Street-snap-picture".equals(string2)) {
                    previewListPreference2.setValue(getString(R.string.pref_camera_snap_value_take_picture));
                } else if ("Street-snap-movie".equals(string2)) {
                    previewListPreference2.setValue(getString(R.string.pref_camera_snap_value_take_movie));
                }
            }
        }
        if (previewListPreference4 != null && ((this.mFromWhere == 162 && this.mIsFrontCamera) || ((this.mFromWhere == 184 && this.mIsFrontCamera) || ((this.mFromWhere == 177 && this.mIsFrontCamera) || ((this.mFromWhere == 168 && this.mIsFrontCamera) || ((this.mFromWhere == 172 && this.mIsFrontCamera) || ((this.mFromWhere == 169 && this.mIsFrontCamera) || ((this.mFromWhere == 161 && this.mIsFrontCamera) || this.mFromWhere == 211)))))))) {
            String string4 = getString(R.string.pref_camera_volumekey_function_entry_shutter);
            String string5 = getString(R.string.pref_camera_volumekey_function_entry_volume);
            String string6 = getString(R.string.pref_camera_volumekey_function_entryvalue_shutter);
            String string7 = getString(R.string.pref_camera_volumekey_function_entryvalue_volume);
            previewListPreference4.setEntries(new CharSequence[]{string4, string5});
            previewListPreference4.setEntryValues(new CharSequence[]{string6, string7});
            previewListPreference4.setDefaultValue(string6);
            previewListPreference4.setValue(string6);
        }
        if (previewListPreference5 != null) {
            int i2 = this.mFromWhere;
            if (i2 == 183 || i2 == 161 || i2 == 174 || i2 == 179 || i2 == 209 || i2 == 211 || (i2 == 184 && !DataRepository.dataItemLive().getMimojiStatusManager2().isInMimojiPhoto())) {
                String[] stringArray = this.mIsFrontCamera ? getResources().getStringArray(R.array.pref_camera_volumekey_function_front_entries) : getResources().getStringArray(R.array.pref_camera_volumekey_function_night_entries);
                if (this.mIsFrontCamera) {
                    resources = getResources();
                    i = R.array.pref_camera_volumekey_function_front_entryvalues;
                } else {
                    resources = getResources();
                    i = R.array.pref_camera_volumekey_function_night_entryvalues;
                }
                String[] stringArray2 = resources.getStringArray(i);
                previewListPreference5.setEntries((CharSequence[]) stringArray);
                previewListPreference5.setEntryValues((CharSequence[]) stringArray2);
                previewListPreference5.setValue(CameraSettings.getKeyVolumeLiveFunction(getString(R.string.pref_camera_volumekey_function_entryvalue_volume)));
            }
        }
        if (previewListPreference3 != null) {
            int i3 = this.mFromWhere;
            if (i3 == 176 || i3 == 166 || ((i3 == 162 && this.mIsFrontCamera) || ((this.mFromWhere == 184 && this.mIsFrontCamera) || ((this.mFromWhere == 177 && this.mIsFrontCamera) || (this.mFromWhere == 173 && this.mIsFrontCamera))))) {
                String string8 = getString(R.string.pref_camera_volumekey_function_entry_shutter);
                String string9 = getString(R.string.pref_camera_volumekey_function_entry_volume);
                String string10 = getString(R.string.pref_camera_volumekey_function_entryvalue_shutter);
                String string11 = getString(R.string.pref_camera_volumekey_function_entryvalue_volume);
                previewListPreference3.setEntries(new CharSequence[]{string8, string9});
                previewListPreference3.setEntryValues(new CharSequence[]{string10, string11});
                previewListPreference3.setDefaultValue(string10);
                previewListPreference3.setValue(string10);
            }
        }
        if (previewListPreference3 != null) {
            if ((this.mFromWhere != 177 || this.mIsFrontCamera) && ((this.mFromWhere != 184 || this.mIsFrontCamera) && (this.mFromWhere != 173 || this.mIsFrontCamera))) {
                int i4 = this.mFromWhere;
                if (i4 != 186) {
                }
            }
            String[] stringArray3 = getResources().getStringArray(R.array.pref_camera_volumekey_function_night_entries);
            String[] stringArray4 = getResources().getStringArray(R.array.pref_camera_volumekey_function_night_entryvalues);
            previewListPreference3.setEntries((CharSequence[]) stringArray3);
            previewListPreference3.setEntryValues((CharSequence[]) stringArray4);
            previewListPreference3.setValue(CameraSettings.getVolumeCameraFunction(getString(R.string.pref_camera_volumekey_function_entryvalue_shutter)));
        }
        if (previewListPreference3 == null || this.mFromWhere != 163) {
            int i5 = this.mFromWhere;
            if (i5 != 175) {
                if (i5 != 167) {
                    if (i5 != 165) {
                        if (i5 != 205) {
                        }
                    }
                }
            }
        }
        if (this.mIsFrontCamera) {
            String[] stringArray5 = getResources().getStringArray(R.array.pref_camera_volumekey_function_portrait_entries);
            String[] stringArray6 = getResources().getStringArray(R.array.pref_camera_volumekey_function_portrait_entryvalues);
            previewListPreference3.setEntries((CharSequence[]) stringArray5);
            previewListPreference3.setEntryValues((CharSequence[]) stringArray6);
        }
        previewListPreference3.setValue(CameraSettings.getVolumeCameraFunction(getString(R.string.pref_camera_volumekey_function_default)));
        if (previewListPreference3 != null && this.mFromWhere == 171) {
            String[] stringArray7 = getResources().getStringArray(R.array.pref_camera_volumekey_function_portrait_entries);
            String[] stringArray8 = getResources().getStringArray(R.array.pref_camera_volumekey_function_portrait_entryvalues);
            previewListPreference3.setEntries((CharSequence[]) stringArray7);
            previewListPreference3.setEntryValues((CharSequence[]) stringArray8);
            previewListPreference3.setValue(CameraSettings.getVolumeCameraFunction(""));
        }
        CheckBoxPreference checkBoxPreference2 = (CheckBoxPreference) this.mPreferenceGroup.findPreference("pref_dualcamera_watermark_key");
        if (checkBoxPreference2 != null) {
            checkBoxPreference2.setDefaultValue(Boolean.valueOf(C0124O00000oO.O000O0o(C0122O00000o.instance().O0oo000())));
            checkBoxPreference2.setChecked(C0124O00000oO.O000O0o(C0122O00000o.instance().O0oo000()));
        }
        PreviewListPreference previewListPreference6 = (PreviewListPreference) this.mPreferenceGroup.findPreference(CameraSettings.KEY_LONG_PRESS_SHUTTER_FEATURE);
        if (previewListPreference6 == null) {
            return;
        }
        if (!C0124O00000oO.Oo00Ooo() || !C0122O00000o.instance().OOO0oOo()) {
            String string12 = getString(R.string.pref_camera_long_pressing_burst_shooting_entry_record);
            String string13 = getString(R.string.pref_camera_long_pressing_burst_shooting_entry_burst);
            String string14 = getString(R.string.pref_camera_long_pressing_burst_shooting_entry_focus);
            String string15 = getString(R.string.pref_camera_long_pressing_burst_shooting_entryvalue_record);
            String string16 = getString(R.string.pref_camera_long_pressing_burst_shooting_entryvalue_burst);
            String string17 = getString(R.string.pref_camera_long_pressing_burst_shooting_entryvalue_focus);
            boolean Oo00Ooo = C0124O00000oO.Oo00Ooo();
            boolean OOO0oOo = C0122O00000o.instance().OOO0oOo();
            if (!Oo00Ooo && OOO0oOo) {
                previewListPreference6.setEntries((CharSequence[]) new String[]{string12, string14});
                previewListPreference6.setEntryValues((CharSequence[]) new String[]{string15, string17});
                previewListPreference6.setDefaultValue(string15);
            } else if (!Oo00Ooo || OOO0oOo) {
                previewListPreference6.setEntries((CharSequence[]) new String[]{string14});
                previewListPreference6.setEntryValues((CharSequence[]) new String[]{string17});
                previewListPreference6.setDefaultValue(string17);
            } else {
                previewListPreference6.setEntries((CharSequence[]) new String[]{string13, string14});
                previewListPreference6.setEntryValues((CharSequence[]) new String[]{string16, string17});
                previewListPreference6.setDefaultValue(string16);
            }
        }
    }

    private void updatePhotoAssistanceTips(SharedPreferences sharedPreferences, ValuePreference valuePreference) {
        if (!(sharedPreferences == null || valuePreference == null)) {
            if (!C0122O00000o.instance().OOo0o0() || !sharedPreferences.getBoolean(CameraSettings.KEY_CAMERA_LYING_TIP_SWITCH, false)) {
                if (C0122O00000o.instance().OOOO0oO()) {
                    if (sharedPreferences.getBoolean(CameraSettings.KEY_PICTURE_FLAW_TIP, getResources().getBoolean(R.bool.pref_pic_flaw_tip_default))) {
                        valuePreference.setValue(getString(R.string.pref_photo_assistance_tips_on));
                        return;
                    }
                }
                if (C0122O00000o.instance().OOO0o00()) {
                    if (sharedPreferences.getBoolean(CameraSettings.KEY_LENS_DIRTY_TIP, C0122O00000o.instance().O0oo00())) {
                        valuePreference.setValue(getString(R.string.pref_photo_assistance_tips_on));
                        valuePreference.setDefaultValue(getString(R.string.pref_photo_assistance_tips_on));
                        return;
                    }
                }
                valuePreference.setValue(getString(R.string.pref_photo_assistance_tips_off));
            } else {
                valuePreference.setValue(getString(R.string.pref_photo_assistance_tips_on));
            }
        }
    }

    private void updateProfessionalDisplay(SharedPreferences sharedPreferences, ValuePreference valuePreference) {
        int i;
        if (!sharedPreferences.getBoolean(CameraSettings.KEY_CAMERA_AUDIO_MAP, CameraAppImpl.getAndroidContext().getResources().getBoolean(R.bool.pref_camera_professional_audio_map_default))) {
            if (!sharedPreferences.getBoolean(CameraSettings.KEY_CAMERA_PRO_HISTOGRAM, CameraAppImpl.getAndroidContext().getResources().getBoolean(R.bool.pref_camera_professional_audio_map_default))) {
                i = R.string.pref_n_s_o_m;
                valuePreference.setValue(getString(i));
            }
        }
        i = R.string.pref_n_s_o_l;
        valuePreference.setValue(getString(i));
    }

    private void updateQRCodeEntry() {
        PreferenceScreen preferenceScreen = this.mPreferenceGroup;
        String str = CameraSettings.KEY_SCAN_QRCODE;
        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) preferenceScreen.findPreference(str);
        if (checkBoxPreference != null && this.mPreferences.getBoolean(str, checkBoxPreference.isChecked()) && !CameraSettings.isQRCodeReceiverAvailable(getContext())) {
            Log.v(TAG, "disable QRCodeScan");
            Editor edit = this.mPreferences.edit();
            edit.putBoolean(str, false);
            edit.apply();
            checkBoxPreference.setChecked(false);
        }
    }

    private void updateVideoDenoise(SharedPreferences sharedPreferences, ValuePreference valuePreference) {
        if (C0122O00000o.instance().OO0oO0O()) {
            int i = (CameraSettings.isWindDenoiseOn() || CameraSettings.isFrontDenoiseOn()) ? R.string.pref_video_denoise_on : R.string.pref_video_denoise_off;
            valuePreference.setValue(getString(i));
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0018, code lost:
        if (r3.getBoolean("pref_dualcamera_watermark_key", O00000Oo.O00000oO.O000000o.C0124O00000oO.O000O0o(O00000Oo.O00000oO.O000000o.C0122O00000o.instance().O0oo000())) == false) goto L_0x001a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateWaterMark(SharedPreferences sharedPreferences, ValuePreference valuePreference) {
        int i;
        if (CameraSettings.isSupportedDualCameraWaterMark()) {
        }
        if (!sharedPreferences.getBoolean("pref_time_watermark_key", false)) {
            i = R.string.pref_watermark_off;
            valuePreference.setValue(getString(i));
        }
        i = R.string.pref_watermark_on;
        valuePreference.setValue(getString(i));
    }

    public /* synthetic */ void O00000o(boolean z) {
        if (z) {
            handleAiAudioUIState();
        }
    }

    /* access modifiers changed from: protected */
    public void filterByIntent() {
        ArrayList stringArrayListExtra = getActivity().getIntent().getStringArrayListExtra(REMOVE_KEYS);
        if (stringArrayListExtra != null) {
            Iterator it = stringArrayListExtra.iterator();
            while (it.hasNext()) {
                removePreference(this.mPreferenceGroup, (String) it.next());
            }
        }
    }

    /* access modifiers changed from: protected */
    public int getPreferenceXml() {
        return R.xml.camera_other_preferences_new;
    }

    public void onBackPressed() {
        resetTimeOutFlag();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mIsFrontCamera = CameraSettings.isFrontCamera();
        this.mFromWhere = getActivity().getIntent().getIntExtra("from_where", 0);
        this.mVideoIntentQuality = getActivity().getIntent().getIntExtra(INTENT_VIDEO_QUALITY, -1);
        if (startFromKeyGuard()) {
            getActivity().setShowWhenLocked(true);
            this.mKeyguardSecureLocked = isFromSecureKeyguard();
        } else {
            this.mKeyguardSecureLocked = false;
        }
        CameraSettings.upgradeGlobalPreferences();
        Storage.initStorage(getContext());
        initializeActivity();
        if (getActivity().getIntent().getCharSequenceExtra(":miui:starting_window_label") != null) {
            ActionBar actionBar = getActivity().getActionBar();
            if (actionBar != null) {
                actionBar.setTitle(R.string.pref_camera_settings_category);
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        resetTimeOutFlag();
        getActivity().finish();
        return true;
    }

    public void onPause() {
        super.onPause();
        if (C0124O00000oO.Oo0O0oo()) {
            ThermalHelper.updateDisplayFrameRate(false, getActivity().getApplication());
        }
        AudioManager audioManager = this.mAudioManager;
        if (audioManager != null) {
            AudioManagerAudioDeviceCallback audioManagerAudioDeviceCallback = this.mAudioManagerAudioDeviceCallback;
            if (audioManagerAudioDeviceCallback != null) {
                audioManager.unregisterAudioDeviceCallback(audioManagerAudioDeviceCallback);
                this.mAudioManagerAudioDeviceCallback.setOnAudioDeviceChangeListener(null);
            }
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        String key = preference.getKey();
        if (TextUtils.isEmpty(key)) {
            return true;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onPreferenceChange: key=");
        sb.append(key);
        sb.append(", newValue=");
        sb.append(obj);
        Log.u(str, sb.toString());
        char c = 65535;
        int hashCode = key.hashCode();
        String str2 = CameraSettings.KEY_MOVIE_SOLID;
        switch (hashCode) {
            case -1153050370:
                if (key.equals(str2)) {
                    c = 2;
                    break;
                }
                break;
            case -44500048:
                if (key.equals(CameraSettings.KEY_VOLUME_CAMERA_FUNCTION)) {
                    c = 3;
                    break;
                }
                break;
            case 829778300:
                if (key.equals(CameraSettings.KEY_PRIORITY_STORAGE)) {
                    c = 5;
                    break;
                }
                break;
            case 852574760:
                if (key.equals(CameraSettings.KEY_CAMERA_SNAP)) {
                    c = 0;
                    break;
                }
                break;
            case 2006116105:
                if (key.equals(CameraSettings.KEY_VOLUME_LIVE_FUNCTION)) {
                    c = 4;
                    break;
                }
                break;
            case 2069752292:
                if (key.equals(CameraSettings.KEY_RECORD_LOCATION)) {
                    c = 1;
                    break;
                }
                break;
        }
        if (c != 0) {
            if (c == 1) {
                String str3 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("onPreferenceChange: KEY_RECORD_LOCATION ");
                sb2.append(obj);
                Log.d(str3, sb2.toString());
                if (((Boolean) obj).booleanValue()) {
                    if (!CameraSettings.isAllowCTA()) {
                        showCTADialog(this);
                    } else if (!PermissionManager.checkCameraLocationPermissions()) {
                        PermissionManager.requestCameraLocationPermissionsByFragment((Fragment) this);
                    }
                }
            } else if (c == 2) {
                if (DataRepository.dataItemGlobal().getCurrentMode() == 180) {
                    str2 = CameraSettings.KEY_PRO_MODE_MOVIE_SOLID;
                }
                DataRepository.dataItemGlobal().putBoolean(str2, ((Boolean) obj).booleanValue());
            } else if (c == 3) {
                int i = this.mFromWhere;
                if (i == 0) {
                    i = 163;
                }
                if (i != 163 && i != 165 && i != 167 && i != 171 && i != 175) {
                    CameraSettings.setVolumeCameraFunction(i, (String) obj);
                } else if (obj.equals(getString(R.string.pref_camera_volumekey_function_entryvalue_shutter)) || obj.equals(getString(R.string.pref_camera_volumekey_function_entryvalue_volume)) || obj.equals(getString(R.string.pref_camera_volumekey_function_entryvalue_timer))) {
                    String str4 = (String) obj;
                    CameraSettings.setVolumeCameraFunction(163, str4);
                    CameraSettings.setVolumeCameraFunction(171, str4);
                } else if (obj.equals(getString(R.string.pref_camera_volumekey_function_entryvalue_zoom))) {
                    CameraSettings.setVolumeCameraFunction(163, (String) obj);
                }
            } else if (c == 4) {
                CameraSettings.setVolumeLiveCameraFunction((String) obj);
            } else if (c == 5) {
                PriorityStorageBroadcastReceiver.setPriorityStorage(((Boolean) obj).booleanValue());
                return true;
            }
        } else if (obj != null) {
            String string = getString(R.string.pref_camera_snap_value_off);
            if (obj instanceof Boolean) {
                string = getSnapStringValue(((Boolean) obj).booleanValue());
            } else if (obj instanceof String) {
                string = (String) obj;
            }
            String str5 = "key_long_press_volume_down";
            if (string.equals(getString(R.string.pref_camera_snap_value_take_picture)) || string.equals(getString(R.string.pref_camera_snap_value_take_movie))) {
                if ("public_transportation_shortcuts".equals(Secure.getString(getActivity().getContentResolver(), str5))) {
                    bringUpDoubleConfirmDlg(preference, string);
                    return false;
                }
            }
            Secure.putString(getActivity().getContentResolver(), str5, CameraSettings.getMiuiSettingsKeyForStreetSnap(string));
            MistatsWrapper.settingClickEvent(Setting.PARAM_CAMERA_SNAP, string);
            return true;
        }
        return super.onPreferenceChange(preference, obj);
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onPreferenceClick(Preference preference) {
        char c;
        String key = preference.getKey();
        boolean isEmpty = TextUtils.isEmpty(key);
        Integer valueOf = Integer.valueOf(1);
        if (isEmpty) {
            return true;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onPreferenceClick: key=");
        sb.append(key);
        Log.u(str, sb.toString());
        switch (key.hashCode()) {
            case -1717659284:
                if (key.equals(PREF_KEY_PRIVACY)) {
                    c = 1;
                    break;
                }
            case -1620641004:
                if (key.equals(CameraSettings.KEY_SCAN_QRCODE)) {
                    c = 9;
                    break;
                }
            case -1590444841:
                if (key.equals(CameraSettings.KEY_CUSTOMIZATION)) {
                    c = 7;
                    break;
                }
            case -445143644:
                if (key.equals(CameraSettings.KEY_SOUND_SETTING)) {
                    c = 4;
                    break;
                }
            case -305641358:
                if (key.equals(PREF_KEY_RESTORE)) {
                    c = 0;
                    break;
                }
            case 76287668:
                if (key.equals(PREF_KEY_POPUP_CAMERA)) {
                    c = 2;
                    break;
                }
            case 1069539048:
                if (key.equals("pref_watermark_key")) {
                    c = 3;
                    break;
                }
            case 1578520153:
                if (key.equals(CameraSettings.KEY_VIDEO_DENOISE)) {
                    c = 6;
                    break;
                }
            case 1690975718:
                if (key.equals(CameraSettings.KEY_PROFESSION_DISPLAY)) {
                    c = 5;
                    break;
                }
            case 2047422134:
                if (key.equals(CameraSettings.KEY_PHOTO_ASSISTANCE_TIPS)) {
                    c = 8;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        String str2 = "from_where";
        String str3 = ":miui:starting_window_label";
        String str4 = "StartActivityWhenLocked";
        switch (c) {
            case 0:
                if (this.mAlertDialog != null) {
                    return true;
                }
                MistatsWrapper.settingClickEvent(Setting.PREF_KEY_RESTORE, null);
                this.mAlertDialog = RotateDialogController.showSystemAlertDialog(getContext(), getString(R.string.confirm_restore_title), getString(R.string.confirm_restore_message), getString(17039370), new O00000Oo(this), null, null, getString(17039360), C0315O00000oO.INSTANCE);
                this.mAlertDialog.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialogInterface) {
                        CameraPreferenceFragment.this.mAlertDialog = null;
                    }
                });
                return true;
            case 1:
                ActivityLauncher.launchPrivacyPolicyWebpage(getActivity());
                MistatsWrapper.settingClickEvent(Setting.PREF_KEY_PRIVACY, null);
                return true;
            case 2:
                this.mGoToActivity = true;
                ActivityLauncher.launchPopupCameraSetting(getActivity());
                MistatsWrapper.settingClickEvent(Setting.PREF_KEY_POPUP_CAMERA, null);
                return true;
            case 3:
                Intent intent = new Intent(getActivity(), WatermarkActivity.class);
                intent.putExtra(str2, this.mFromWhere);
                intent.putExtra(str3, getResources().getString(R.string.pref_watermark_title));
                if (getActivity().getIntent().getBooleanExtra(str4, false)) {
                    intent.putExtra(str4, true);
                }
                this.mGoToActivity = true;
                startActivity(intent);
                return true;
            case 4:
                Log.d("NoiseReduction", "onPreferenceClick: NoiseReduction  ");
                Intent intent2 = new Intent(getActivity(), SoundSettingActivity.class);
                intent2.putExtra(str2, this.mFromWhere);
                intent2.putExtra(str3, getResources().getString(R.string.pref_n_s_a));
                if (getActivity().getIntent().getBooleanExtra(str4, false)) {
                    intent2.putExtra(str4, true);
                }
                this.mGoToActivity = true;
                startActivity(intent2);
                HashMap hashMap = new HashMap();
                hashMap.put(VideoAttr.PARAM_SOUND_SETTING, valueOf);
                MistatsWrapper.mistatEvent(VideoAttr.KEY_VIDEO_COMMON_CLICK, hashMap);
                return true;
            case 5:
                Log.d("ProfessionalDisplay", "onPreferenceClick: ProfessionalDisplay  ");
                Intent intent3 = new Intent(getActivity(), ProfessionalDisplayActivity.class);
                intent3.putExtra(str2, this.mFromWhere);
                intent3.putExtra(str3, getResources().getString(R.string.pref_p_d_e));
                if (getActivity().getIntent().getBooleanExtra(str4, false)) {
                    intent3.putExtra(str4, true);
                }
                this.mGoToActivity = true;
                startActivity(intent3);
                HashMap hashMap2 = new HashMap();
                hashMap2.put(Manual.VALUE_PREFERENCE_DISPLAY, valueOf);
                MistatsWrapper.mistatEvent("M_proVideo_", hashMap2);
                return true;
            case 6:
                Intent intent4 = new Intent(getActivity(), VideoDenoiseActivity.class);
                intent4.putExtra(str2, this.mFromWhere);
                intent4.putExtra(str3, getResources().getString(R.string.pref_audio_denoise_title));
                if (getActivity().getIntent().getBooleanExtra(str4, false)) {
                    intent4.putExtra(str4, true);
                }
                this.mGoToActivity = true;
                startActivity(intent4);
                return true;
            case 7:
                Intent intent5 = new Intent(getActivity(), CustomizationActivity.class);
                intent5.putExtra(str2, this.mFromWhere);
                intent5.putExtra(str3, getResources().getString(R.string.pref_customization_title));
                if (getActivity().getIntent().getBooleanExtra(str4, false)) {
                    intent5.putExtra(str4, true);
                }
                this.mGoToActivity = true;
                startActivity(intent5);
                String str5 = "attr_custom_camera";
                MistatsWrapper.settingClickEvent(str5, null);
                MistatsWrapper.customizeCameraSettingClick(str5);
                return true;
            case 8:
                Intent intent6 = new Intent(getActivity(), PhotoAssistanceTipsActivity.class);
                intent6.putExtra(str3, getResources().getString(R.string.photo_assistance_tips_title));
                try {
                    if (getActivity().getIntent().getBooleanExtra(str4, false)) {
                        intent6.putExtra(str4, true);
                    }
                    this.mGoToActivity = true;
                    startActivity(intent6);
                    HashMap hashMap3 = new HashMap();
                    hashMap3.put(FeatureName.VALUE_PHOTO_ASSISTANCE_TIP_CLICK, valueOf);
                    MistatsWrapper.mistatEvent(FeatureName.KEY_COMMON_TIPS, hashMap3);
                } catch (Exception unused) {
                }
                return true;
            case 9:
                if (!CameraSettings.isQRCodeReceiverAvailable(getActivity())) {
                    if (this.mScanAlertDialog != null) {
                        return true;
                    }
                    this.mScanAlertDialog = RotateDialogController.showSystemAlertDialog(getActivity(), getString(R.string.confirm_install_scanner_title), getString(R.string.confirm_install_scanner_message), getString(R.string.install_confirmed), new O00000o(this), null, null, getString(17039360), O00000o0.INSTANCE);
                    this.mScanAlertDialog.setOnDismissListener(new OnDismissListener() {
                        public void onDismiss(DialogInterface dialogInterface) {
                            CameraPreferenceFragment.this.mScanAlertDialog = null;
                        }
                    });
                    return true;
                }
                break;
        }
        return false;
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onRequestPermissionsResult: requestCode = ");
        sb.append(i);
        Log.d(str, sb.toString());
        if (i == 101) {
            if (PermissionManager.checkCameraLocationPermissions()) {
                Log.u(TAG, "onRequestPermissionsResult: is location granted = true");
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), "android.permission.ACCESS_FINE_LOCATION") || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), "android.permission.ACCESS_COARSE_LOCATION")) {
                Log.u(TAG, "onRequestPermissionsResult: is location granted = false");
                closeLocationPreference();
            } else {
                Log.u(TAG, "onRequestPermissionsResult: not ask again!");
                if (this.mPermissionNotAskDialog == null) {
                    this.mPermissionNotAskDialog = RotateDialogController.showSystemAlertDialog(getContext(), null, getString(R.string.location_permission_not_ask_again), getString(R.string.location_permission_not_ask_again_go_to_settings), new Runnable() {
                        public void run() {
                            Log.u(CameraPreferenceFragment.TAG, "onClick PermissionNotAskDialog allow");
                            CameraPreferenceFragment cameraPreferenceFragment = CameraPreferenceFragment.this;
                            StringBuilder sb = new StringBuilder();
                            sb.append("package:");
                            sb.append(CameraPreferenceFragment.this.getActivity().getPackageName());
                            cameraPreferenceFragment.startActivity(new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.parse(sb.toString())));
                        }
                    }, null, null, getString(17039360), new Runnable() {
                        public void run() {
                            Log.u(CameraPreferenceFragment.TAG, "onClick PermissionNotAskDialog cancel");
                            CameraPreferenceFragment.this.closeLocationPreference();
                        }
                    });
                }
            }
        }
    }

    public void onRestart() {
        if (this.mGoToActivity) {
            updateWaterMark(this.mPreferences, (ValuePreference) this.mWatermark);
            updateProfessionalDisplay(this.mPreferences, (ValuePreference) this.mProfessionDisplay);
            updatePhotoAssistanceTips(this.mPreferences, (ValuePreference) this.mPhotoAssistanceTips);
            updateVideoDenoise(this.mPreferences, (ValuePreference) this.mVideoDenoise);
            this.mGoToActivity = false;
        } else if (getActivity().getReferrer() == null || !TextUtils.equals(getActivity().getReferrer().getHost(), getActivity().getPackageName())) {
            initializeActivity();
        } else {
            getActivity().finish();
        }
    }

    public void onResume() {
        super.onResume();
        if (C0124O00000oO.Oo0O0oo()) {
            ThermalHelper.updateDisplayFrameRate(true, getActivity().getApplication());
        }
        updateQRCodeEntry();
        updateCameraSound();
        if (Util.isLabOptionsVisible()) {
            Toast.makeText(getContext(), R.string.camera_facedetection_sub_option_hint, 1).show();
        }
        if (C0122O00000o.instance().OO0oO0O()) {
            this.mAudioManager = (AudioManager) getActivity().getSystemService("audio");
            this.mAudioManagerAudioDeviceCallback = new AudioManagerAudioDeviceCallback();
            this.mAudioManager.registerAudioDeviceCallback(this.mAudioManagerAudioDeviceCallback, null);
            this.mAudioManagerAudioDeviceCallback.setOnAudioDeviceChangeListener(this.mAudioDeviceChangeListener);
        }
    }

    public void updateConflictPreference(Preference preference) {
        dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_MOVIE_SOLID, CameraSettings.getMovieSolidSettingNeedRemove(this.mFromWhere, this.mIsFrontCamera, this.mVideoIntentQuality));
        dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_VIDEO_ENCODER, CameraSettings.isH265EncoderNeedRemove(this.mFromWhere, this.mIsFrontCamera));
    }

    public void updatePreferences(PreferenceGroup preferenceGroup, SharedPreferences sharedPreferences) {
        boolean z;
        if (preferenceGroup != null) {
            int preferenceCount = preferenceGroup.getPreferenceCount();
            for (int i = 0; i < preferenceCount; i++) {
                Preference preference = preferenceGroup.getPreference(i);
                if (preference instanceof ValuePreference) {
                    if (preference.getKey().equals("pref_watermark_key")) {
                        updateWaterMark(sharedPreferences, (ValuePreference) preference);
                    }
                    if (preference.getKey().equals(CameraSettings.KEY_PROFESSION_DISPLAY)) {
                        updateProfessionalDisplay(sharedPreferences, (ValuePreference) preference);
                    }
                    if (preference.getKey().equals(CameraSettings.KEY_PHOTO_ASSISTANCE_TIPS)) {
                        updatePhotoAssistanceTips(sharedPreferences, (ValuePreference) preference);
                    }
                    if (preference.getKey().equals(CameraSettings.KEY_VIDEO_DENOISE)) {
                        updateVideoDenoise(sharedPreferences, (ValuePreference) preference);
                    }
                } else if (preference instanceof PreviewListPreference) {
                    PreviewListPreference previewListPreference = (PreviewListPreference) preference;
                    previewListPreference.setValue(getFilterValue(previewListPreference, sharedPreferences));
                    preference.setPersistent(false);
                    if (Util.isAccessible()) {
                        previewListPreference.setSummary((CharSequence) previewListPreference.getEntryByIndex(previewListPreference.getValueIndex()));
                    }
                } else if (preference instanceof CheckBoxPreference) {
                    CheckBoxPreference checkBoxPreference = (CheckBoxPreference) preference;
                    String key = checkBoxPreference.getKey();
                    boolean isChecked = checkBoxPreference.isChecked();
                    if (CameraSettings.KEY_MOVIE_SOLID.equals(key)) {
                        z = CameraSettings.isMovieSolidOn();
                        if (CameraSettings.isVideoEisBeautyMeanwhileEnable()) {
                            z = true;
                        }
                    } else {
                        z = sharedPreferences.getBoolean(key, isChecked);
                    }
                    checkBoxPreference.setChecked(z);
                    preference.setPersistent(false);
                    if (CameraSettings.KEY_RECORD_LOCATION.equals(key)) {
                        preference.setEnabled(!this.mKeyguardSecureLocked);
                        if (!PermissionManager.checkCameraLocationPermissions() && CameraSettings.isRecordLocation()) {
                            checkBoxPreference.setChecked(false);
                            CameraSettings.updateRecordLocationPreference(false);
                        }
                    }
                    if (CameraSettings.KEY_FRONT_MIRROR.equals(key) && C0124O00000oO.OOooOoO()) {
                        checkBoxPreference.setChecked(CameraSettings.isFrontMirror());
                    }
                } else if (preference instanceof PreferenceGroup) {
                    updatePreferences((PreferenceGroup) preference, sharedPreferences);
                } else {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("no need update preference for ");
                    sb.append(preference.getKey());
                    Log.v(str, sb.toString());
                }
            }
        }
    }
}
