package com.ss.android.vesdk;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.IntRange;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import com.ss.android.vesdk.runtime.VERuntime;
import com.ss.android.vesdk.runtime.cloudconfig.PerformanceConfig;
import com.ss.android.vesdk.runtime.cloudconfig.VECloudConfig;
import com.ss.android.vesdk.settings.VEVideoCompileEncodeSettings;
import com.ss.android.vesdk.settings.VEVideoEncodeProfile;
import com.ss.android.vesdk.settings.VEVideoHWEncodeSettings;
import com.ss.android.vesdk.settings.VEVideoSWEncodeSettings;
import org.json.JSONException;
import org.json.JSONObject;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

@Keep
public class VEVideoEncodeSettings implements Parcelable {
    public static final Creator CREATOR = new Creator() {
        public VEVideoEncodeSettings createFromParcel(Parcel parcel) {
            return new VEVideoEncodeSettings(parcel);
        }

        public VEVideoEncodeSettings[] newArray(int i) {
            return new VEVideoEncodeSettings[i];
        }
    };
    private static final String TAG = "VEVideoEncodeSettings";
    public static final int USAGE_COMPILE = 2;
    public static final int USAGE_IMPORT = 3;
    public static final int USAGE_RECORD = 1;
    /* access modifiers changed from: private */
    public ENCODE_BITRATE_MODE bitrateMode;
    /* access modifiers changed from: private */
    public int bps;
    public COMPILE_TYPE compileType;
    private boolean enableInterLeave;
    /* access modifiers changed from: private */
    public boolean enableRemuxVideo;
    /* access modifiers changed from: private */
    public int encodeProfile;
    /* access modifiers changed from: private */
    public int encodeStandard;
    /* access modifiers changed from: private */
    public String externalSettingsJsonStr;
    /* access modifiers changed from: private */
    public int fps;
    /* access modifiers changed from: private */
    public int gopSize;
    /* access modifiers changed from: private */
    public boolean hasBFrame;
    /* access modifiers changed from: private */
    public VEVideoCompileEncodeSettings mVideoCompileEncodeSetting;
    /* access modifiers changed from: private */
    public VEVideoCompileEncodeSettings mVideoWatermarkCompileEncodeSetting;
    /* access modifiers changed from: private */
    public VEWatermarkParam mWatermarkParam;
    /* access modifiers changed from: private */
    public VESize outputSize;
    /* access modifiers changed from: private */
    public int resizeMode;
    /* access modifiers changed from: private */
    public float resizeX;
    /* access modifiers changed from: private */
    public float resizeY;
    /* access modifiers changed from: private */
    public int rotate;
    /* access modifiers changed from: private */
    public float speed;
    /* access modifiers changed from: private */
    public int swCRF;
    /* access modifiers changed from: private */
    public long swMaxrate;
    /* access modifiers changed from: private */
    public int swPreset;
    /* access modifiers changed from: private */
    public int swQP;
    /* access modifiers changed from: private */
    public boolean useHWEncoder;

    /* renamed from: com.ss.android.vesdk.VEVideoEncodeSettings$2 reason: invalid class name */
    /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$ss$android$vesdk$VEVideoEncodeSettings$ENCODE_BITRATE_MODE = new int[ENCODE_BITRATE_MODE.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$com$ss$android$vesdk$VEVideoEncodeSettings$ENCODE_BITRATE_MODE[ENCODE_BITRATE_MODE.ENCODE_BITRATE_ABR.ordinal()] = 1;
            $SwitchMap$com$ss$android$vesdk$VEVideoEncodeSettings$ENCODE_BITRATE_MODE[ENCODE_BITRATE_MODE.ENCODE_BITRATE_CRF.ordinal()] = 2;
            $SwitchMap$com$ss$android$vesdk$VEVideoEncodeSettings$ENCODE_BITRATE_MODE[ENCODE_BITRATE_MODE.ENCODE_BITRATE_QP.ordinal()] = 3;
            try {
                $SwitchMap$com$ss$android$vesdk$VEVideoEncodeSettings$ENCODE_BITRATE_MODE[ENCODE_BITRATE_MODE.ENCODE_BITRATE_VBR.ordinal()] = 4;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public class Builder {
        private VEVideoEncodeSettings exportVideoEncodeSettings;
        private int mUsage;

        public Builder(@IntRange(from = 1, to = 3) int i) {
            this.mUsage = i;
            this.exportVideoEncodeSettings = new VEVideoEncodeSettings();
        }

        public Builder(@IntRange(from = 1, to = 3) int i, @NonNull VEVideoEncodeSettings vEVideoEncodeSettings) {
            this.mUsage = i;
            this.exportVideoEncodeSettings = vEVideoEncodeSettings;
        }

        private ENCODE_BITRATE_MODE getCompileHardwareBitrateModeFromCloud() {
            return ENCODE_BITRATE_MODE.ENCODE_BITRATE_ABR;
        }

        private ENCODE_BITRATE_MODE getImportHardwareBitrateModeFromCloud() {
            return ENCODE_BITRATE_MODE.ENCODE_BITRATE_ABR;
        }

        private ENCODE_BITRATE_MODE getRecordHardwareBitrateModeFromCloud() {
            return ENCODE_BITRATE_MODE.ENCODE_BITRATE_ABR;
        }

        private void overrideWithCloudConfigForCompile() {
            VECloudConfig vECloudConfig = PerformanceConfig.sVECloudConfig;
            boolean z = true;
            if (vECloudConfig.mCompileEncodeMode != 1) {
                z = false;
            }
            this.exportVideoEncodeSettings.useHWEncoder = z;
            this.exportVideoEncodeSettings.swCRF = vECloudConfig.mCompileEncodeSWCRF;
            this.exportVideoEncodeSettings.swPreset = vECloudConfig.mCompileEncodeSWCRFPreset;
            this.exportVideoEncodeSettings.bitrateMode = z ? getCompileHardwareBitrateModeFromCloud() : ENCODE_BITRATE_MODE.fromInteger(vECloudConfig.mCompileSWBitrateMode);
            VEVideoEncodeSettings vEVideoEncodeSettings = this.exportVideoEncodeSettings;
            vEVideoEncodeSettings.bps = z ? vECloudConfig.mCompileEncodeHWBPS : vEVideoEncodeSettings.bps;
            VEVideoEncodeSettings vEVideoEncodeSettings2 = this.exportVideoEncodeSettings;
            vEVideoEncodeSettings2.gopSize = z ? vEVideoEncodeSettings2.gopSize : vECloudConfig.mCompileEncodeSWGOP;
            VEVideoEncodeSettings vEVideoEncodeSettings3 = this.exportVideoEncodeSettings;
            vEVideoEncodeSettings3.encodeProfile = z ? vECloudConfig.mCompileHwProfile : vEVideoEncodeSettings3.encodeProfile;
            VEVideoEncodeSettings vEVideoEncodeSettings4 = this.exportVideoEncodeSettings;
            vEVideoEncodeSettings4.swMaxrate = z ? vEVideoEncodeSettings4.swMaxrate : (long) vECloudConfig.mCompileEncodeSWMaxrate;
            VEVideoEncodeSettings vEVideoEncodeSettings5 = this.exportVideoEncodeSettings;
            vEVideoEncodeSettings5.swQP = z ? vEVideoEncodeSettings5.swQP : vECloudConfig.mCompileVideoSWQP;
        }

        private void overrideWithCloudConfigForImport() {
            VECloudConfig vECloudConfig = PerformanceConfig.sVECloudConfig;
            boolean z = true;
            if (vECloudConfig.mImportEncodeMode != 1) {
                z = false;
            }
            this.exportVideoEncodeSettings.useHWEncoder = z;
            this.exportVideoEncodeSettings.swCRF = vECloudConfig.mImportSWEncodeCRF;
            this.exportVideoEncodeSettings.swPreset = vECloudConfig.mImportVideoSWPreset;
            this.exportVideoEncodeSettings.bitrateMode = z ? getImportHardwareBitrateModeFromCloud() : ENCODE_BITRATE_MODE.fromInteger(vECloudConfig.mImportSWBitrateMode);
            VEVideoEncodeSettings vEVideoEncodeSettings = this.exportVideoEncodeSettings;
            vEVideoEncodeSettings.bps = z ? vECloudConfig.mImportHWEncodeBPS : vEVideoEncodeSettings.bps;
            VEVideoEncodeSettings vEVideoEncodeSettings2 = this.exportVideoEncodeSettings;
            vEVideoEncodeSettings2.gopSize = z ? vEVideoEncodeSettings2.gopSize : vECloudConfig.mImportVideoSWGop;
            VEVideoEncodeSettings vEVideoEncodeSettings3 = this.exportVideoEncodeSettings;
            vEVideoEncodeSettings3.encodeProfile = z ? vECloudConfig.mImportHwProfile : vEVideoEncodeSettings3.encodeProfile;
            VEVideoEncodeSettings vEVideoEncodeSettings4 = this.exportVideoEncodeSettings;
            vEVideoEncodeSettings4.swMaxrate = z ? vEVideoEncodeSettings4.swMaxrate : (long) vECloudConfig.mImportVideoSWMaxrate;
            VEVideoEncodeSettings vEVideoEncodeSettings5 = this.exportVideoEncodeSettings;
            vEVideoEncodeSettings5.swQP = z ? vEVideoEncodeSettings5.swQP : vECloudConfig.mImportVideoSWQP;
        }

        private void overrideWithCloudConfigForRecord() {
            VECloudConfig vECloudConfig = PerformanceConfig.sVECloudConfig;
            boolean z = true;
            if (vECloudConfig.mRecordEncodeMode != 1) {
                z = false;
            }
            this.exportVideoEncodeSettings.useHWEncoder = z;
            this.exportVideoEncodeSettings.outputSize = new VESize(vECloudConfig.mRecordResolutionWidth, vECloudConfig.mRecordResolutionHeight);
            this.exportVideoEncodeSettings.swCRF = vECloudConfig.mRecordSWEncodeCRF;
            this.exportVideoEncodeSettings.swPreset = vECloudConfig.mRecordVideoSWPreset;
            this.exportVideoEncodeSettings.bitrateMode = z ? getRecordHardwareBitrateModeFromCloud() : ENCODE_BITRATE_MODE.fromInteger(vECloudConfig.mRecordSWBitrateMode);
            VEVideoEncodeSettings vEVideoEncodeSettings = this.exportVideoEncodeSettings;
            vEVideoEncodeSettings.bps = z ? vECloudConfig.mRecordHWEncodeBPS : vEVideoEncodeSettings.bps;
            VEVideoEncodeSettings vEVideoEncodeSettings2 = this.exportVideoEncodeSettings;
            vEVideoEncodeSettings2.gopSize = z ? vEVideoEncodeSettings2.gopSize : vECloudConfig.mRecordVideoSWGop;
            VEVideoEncodeSettings vEVideoEncodeSettings3 = this.exportVideoEncodeSettings;
            vEVideoEncodeSettings3.encodeProfile = z ? vECloudConfig.mRecordHwProfile : vEVideoEncodeSettings3.encodeProfile;
            VEVideoEncodeSettings vEVideoEncodeSettings4 = this.exportVideoEncodeSettings;
            vEVideoEncodeSettings4.swMaxrate = z ? vEVideoEncodeSettings4.swMaxrate : (long) vECloudConfig.mRecordVideoSWMaxrate;
            VEVideoEncodeSettings vEVideoEncodeSettings5 = this.exportVideoEncodeSettings;
            vEVideoEncodeSettings5.swQP = z ? vEVideoEncodeSettings5.swQP : vECloudConfig.mRecordVideoSWQP;
        }

        private void overrideWithUserConfig() {
            VEVideoCompileEncodeSettings vEVideoCompileEncodeSettings = new VEVideoCompileEncodeSettings();
            vEVideoCompileEncodeSettings.useHWEncoder = this.exportVideoEncodeSettings.useHWEncoder;
            if (this.exportVideoEncodeSettings.useHWEncoder) {
                vEVideoCompileEncodeSettings.mHWEncodeSetting.mBitrate = (long) this.exportVideoEncodeSettings.bps;
                vEVideoCompileEncodeSettings.mHWEncodeSetting.mProfile = this.exportVideoEncodeSettings.encodeProfile;
                vEVideoCompileEncodeSettings.mHWEncodeSetting.mGop = this.exportVideoEncodeSettings.gopSize;
            } else {
                vEVideoCompileEncodeSettings.mSWEncodeSetting.mBitrateMode = this.exportVideoEncodeSettings.bitrateMode.ordinal();
                vEVideoCompileEncodeSettings.mSWEncodeSetting.mBps = this.exportVideoEncodeSettings.bps;
                vEVideoCompileEncodeSettings.mSWEncodeSetting.mCrf = this.exportVideoEncodeSettings.swCRF;
                vEVideoCompileEncodeSettings.mSWEncodeSetting.mMaxRate = this.exportVideoEncodeSettings.swMaxrate;
                vEVideoCompileEncodeSettings.mSWEncodeSetting.mPreset = this.exportVideoEncodeSettings.swPreset;
                vEVideoCompileEncodeSettings.mSWEncodeSetting.mProfile = this.exportVideoEncodeSettings.encodeProfile;
                vEVideoCompileEncodeSettings.mSWEncodeSetting.mGop = this.exportVideoEncodeSettings.gopSize;
            }
            this.exportVideoEncodeSettings.mVideoCompileEncodeSetting = vEVideoCompileEncodeSettings;
            this.exportVideoEncodeSettings.mVideoWatermarkCompileEncodeSetting = vEVideoCompileEncodeSettings;
        }

        private void parseExternalSettingsJsonStr(String str) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                this.exportVideoEncodeSettings.mVideoCompileEncodeSetting = parseJsonToSetting(jSONObject.getJSONObject("compile"));
                this.exportVideoEncodeSettings.mVideoWatermarkCompileEncodeSetting = parseJsonToSetting(jSONObject.getJSONObject("watermark_compile"));
            } catch (JSONException e) {
                e.printStackTrace();
                StringBuilder sb = new StringBuilder();
                sb.append("external json str parse error : ");
                sb.append(e.getLocalizedMessage());
                VELogUtil.e(VEVideoEncodeSettings.TAG, sb.toString());
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:14:0x0031 A[SYNTHETIC, Splitter:B:14:0x0031] */
        /* JADX WARNING: Removed duplicated region for block: B:26:0x0063 A[SYNTHETIC, Splitter:B:26:0x0063] */
        /* JADX WARNING: Removed duplicated region for block: B:37:0x009f A[SYNTHETIC, Splitter:B:37:0x009f] */
        /* JADX WARNING: Removed duplicated region for block: B:48:0x00db A[SYNTHETIC, Splitter:B:48:0x00db] */
        /* JADX WARNING: Removed duplicated region for block: B:59:0x010b A[SYNTHETIC, Splitter:B:59:0x010b] */
        /* JADX WARNING: Removed duplicated region for block: B:70:0x013c A[SYNTHETIC, Splitter:B:70:0x013c] */
        /* JADX WARNING: Removed duplicated region for block: B:80:0x016a A[Catch:{ JSONException -> 0x01cd }] */
        /* JADX WARNING: Removed duplicated region for block: B:89:0x01a4 A[Catch:{ JSONException -> 0x01cd }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private VEVideoCompileEncodeSettings parseJsonToSetting(JSONObject jSONObject) {
            boolean equals;
            int i;
            long j;
            String str;
            int i2;
            String str2;
            int i3;
            String str3;
            int i4;
            long j2;
            int i5;
            int i6;
            String str4 = "unknown";
            VEVideoCompileEncodeSettings vEVideoCompileEncodeSettings = new VEVideoCompileEncodeSettings();
            try {
                String string = jSONObject.getString("encode_mode");
                String str5 = "hw";
                if (str4.equals(string)) {
                    if (this.mUsage == 2) {
                        equals = this.exportVideoEncodeSettings.useHWEncoder;
                        vEVideoCompileEncodeSettings.useHWEncoder = equals;
                        VEVideoHWEncodeSettings vEVideoHWEncodeSettings = vEVideoCompileEncodeSettings.mHWEncodeSetting;
                        i = this.mUsage;
                        String str6 = IjkMediaMeta.IJKM_KEY_BITRATE;
                        if (i == 2) {
                            if (jSONObject.getJSONObject(str5).getInt(str6) == -1) {
                                if (this.exportVideoEncodeSettings.useHWEncoder) {
                                    i6 = this.exportVideoEncodeSettings.bps;
                                    j = (long) i6;
                                    vEVideoHWEncodeSettings.mBitrate = j;
                                    VEVideoHWEncodeSettings vEVideoHWEncodeSettings2 = vEVideoCompileEncodeSettings.mHWEncodeSetting;
                                    str = "profile";
                                    if (this.mUsage == 2) {
                                        if (str4.equals(jSONObject.getJSONObject(str5).getString(str))) {
                                            i2 = this.exportVideoEncodeSettings.useHWEncoder ? this.exportVideoEncodeSettings.encodeProfile : vEVideoCompileEncodeSettings.mHWEncodeSetting.mProfile;
                                            vEVideoHWEncodeSettings2.mProfile = i2;
                                            VEVideoHWEncodeSettings vEVideoHWEncodeSettings3 = vEVideoCompileEncodeSettings.mHWEncodeSetting;
                                            str2 = "gop";
                                            if (this.mUsage == 2) {
                                                if (jSONObject.getJSONObject(str5).getInt(str2) == -1) {
                                                    i3 = this.exportVideoEncodeSettings.useHWEncoder ? this.exportVideoEncodeSettings.gopSize : vEVideoCompileEncodeSettings.mHWEncodeSetting.mGop;
                                                    vEVideoHWEncodeSettings3.mGop = i3;
                                                    vEVideoCompileEncodeSettings.mSWEncodeSetting.mBitrateMode = ENCODE_BITRATE_MODE.ENCODE_BITRATE_CRF.ordinal();
                                                    VEVideoSWEncodeSettings vEVideoSWEncodeSettings = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                                                    String str7 = "crf";
                                                    str3 = "sw";
                                                    if (this.mUsage == 2) {
                                                        if (jSONObject.getJSONObject(str3).getInt(str7) == -1) {
                                                            i4 = this.exportVideoEncodeSettings.useHWEncoder ? vEVideoCompileEncodeSettings.mSWEncodeSetting.mCrf : this.exportVideoEncodeSettings.swCRF;
                                                            vEVideoSWEncodeSettings.mCrf = i4;
                                                            VEVideoSWEncodeSettings vEVideoSWEncodeSettings2 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                                                            String str8 = "maxrate";
                                                            if (this.mUsage == 2) {
                                                                if (jSONObject.getJSONObject(str3).getInt(str8) == -1) {
                                                                    j2 = this.exportVideoEncodeSettings.useHWEncoder ? vEVideoCompileEncodeSettings.mSWEncodeSetting.mMaxRate : this.exportVideoEncodeSettings.swMaxrate;
                                                                    vEVideoSWEncodeSettings2.mMaxRate = j2;
                                                                    VEVideoSWEncodeSettings vEVideoSWEncodeSettings3 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                                                                    String str9 = "preset";
                                                                    if (this.mUsage == 2) {
                                                                        if (jSONObject.getJSONObject(str3).getInt(str9) == -1) {
                                                                            i5 = this.exportVideoEncodeSettings.useHWEncoder ? vEVideoCompileEncodeSettings.mSWEncodeSetting.mPreset : this.exportVideoEncodeSettings.swPreset;
                                                                            vEVideoSWEncodeSettings3.mPreset = i5;
                                                                            VEVideoSWEncodeSettings vEVideoSWEncodeSettings4 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                                                                            int i7 = (this.mUsage == 2 || !str4.equals(jSONObject.getJSONObject(str3).getString(str))) ? VEVideoEncodeProfile.valueOfString(jSONObject.getJSONObject(str3).getString(str)).ordinal() : this.exportVideoEncodeSettings.useHWEncoder ? vEVideoCompileEncodeSettings.mSWEncodeSetting.mProfile : this.exportVideoEncodeSettings.encodeProfile;
                                                                            vEVideoSWEncodeSettings4.mProfile = i7;
                                                                            VEVideoSWEncodeSettings vEVideoSWEncodeSettings5 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                                                                            int i8 = (this.mUsage == 2 || jSONObject.getJSONObject(str3).getInt(str2) != -1) ? jSONObject.getJSONObject(str3).getInt(str2) : this.exportVideoEncodeSettings.useHWEncoder ? vEVideoCompileEncodeSettings.mSWEncodeSetting.mGop : this.exportVideoEncodeSettings.gopSize;
                                                                            vEVideoSWEncodeSettings5.mGop = i8;
                                                                            return vEVideoCompileEncodeSettings;
                                                                        }
                                                                    }
                                                                    i5 = jSONObject.getJSONObject(str3).getInt(str9);
                                                                    vEVideoSWEncodeSettings3.mPreset = i5;
                                                                    VEVideoSWEncodeSettings vEVideoSWEncodeSettings42 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                                                                    if (this.mUsage == 2) {
                                                                    }
                                                                    vEVideoSWEncodeSettings42.mProfile = i7;
                                                                    VEVideoSWEncodeSettings vEVideoSWEncodeSettings52 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                                                                    if (this.mUsage == 2) {
                                                                    }
                                                                    vEVideoSWEncodeSettings52.mGop = i8;
                                                                    return vEVideoCompileEncodeSettings;
                                                                }
                                                            }
                                                            j2 = (long) jSONObject.getJSONObject(str3).getInt(str8);
                                                            vEVideoSWEncodeSettings2.mMaxRate = j2;
                                                            VEVideoSWEncodeSettings vEVideoSWEncodeSettings32 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                                                            String str92 = "preset";
                                                            if (this.mUsage == 2) {
                                                            }
                                                            i5 = jSONObject.getJSONObject(str3).getInt(str92);
                                                            vEVideoSWEncodeSettings32.mPreset = i5;
                                                            VEVideoSWEncodeSettings vEVideoSWEncodeSettings422 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                                                            if (this.mUsage == 2) {
                                                            }
                                                            vEVideoSWEncodeSettings422.mProfile = i7;
                                                            VEVideoSWEncodeSettings vEVideoSWEncodeSettings522 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                                                            if (this.mUsage == 2) {
                                                            }
                                                            vEVideoSWEncodeSettings522.mGop = i8;
                                                            return vEVideoCompileEncodeSettings;
                                                        }
                                                    }
                                                    i4 = jSONObject.getJSONObject(str3).getInt(str7);
                                                    vEVideoSWEncodeSettings.mCrf = i4;
                                                    VEVideoSWEncodeSettings vEVideoSWEncodeSettings22 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                                                    String str82 = "maxrate";
                                                    if (this.mUsage == 2) {
                                                    }
                                                    j2 = (long) jSONObject.getJSONObject(str3).getInt(str82);
                                                    vEVideoSWEncodeSettings22.mMaxRate = j2;
                                                    VEVideoSWEncodeSettings vEVideoSWEncodeSettings322 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                                                    String str922 = "preset";
                                                    if (this.mUsage == 2) {
                                                    }
                                                    i5 = jSONObject.getJSONObject(str3).getInt(str922);
                                                    vEVideoSWEncodeSettings322.mPreset = i5;
                                                    VEVideoSWEncodeSettings vEVideoSWEncodeSettings4222 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                                                    if (this.mUsage == 2) {
                                                    }
                                                    vEVideoSWEncodeSettings4222.mProfile = i7;
                                                    VEVideoSWEncodeSettings vEVideoSWEncodeSettings5222 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                                                    if (this.mUsage == 2) {
                                                    }
                                                    vEVideoSWEncodeSettings5222.mGop = i8;
                                                    return vEVideoCompileEncodeSettings;
                                                }
                                            }
                                            i3 = jSONObject.getJSONObject(str5).getInt(str2);
                                            vEVideoHWEncodeSettings3.mGop = i3;
                                            vEVideoCompileEncodeSettings.mSWEncodeSetting.mBitrateMode = ENCODE_BITRATE_MODE.ENCODE_BITRATE_CRF.ordinal();
                                            VEVideoSWEncodeSettings vEVideoSWEncodeSettings6 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                                            String str72 = "crf";
                                            str3 = "sw";
                                            if (this.mUsage == 2) {
                                            }
                                            i4 = jSONObject.getJSONObject(str3).getInt(str72);
                                            vEVideoSWEncodeSettings6.mCrf = i4;
                                            VEVideoSWEncodeSettings vEVideoSWEncodeSettings222 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                                            String str822 = "maxrate";
                                            if (this.mUsage == 2) {
                                            }
                                            j2 = (long) jSONObject.getJSONObject(str3).getInt(str822);
                                            vEVideoSWEncodeSettings222.mMaxRate = j2;
                                            VEVideoSWEncodeSettings vEVideoSWEncodeSettings3222 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                                            String str9222 = "preset";
                                            if (this.mUsage == 2) {
                                            }
                                            i5 = jSONObject.getJSONObject(str3).getInt(str9222);
                                            vEVideoSWEncodeSettings3222.mPreset = i5;
                                            VEVideoSWEncodeSettings vEVideoSWEncodeSettings42222 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                                            if (this.mUsage == 2) {
                                            }
                                            vEVideoSWEncodeSettings42222.mProfile = i7;
                                            VEVideoSWEncodeSettings vEVideoSWEncodeSettings52222 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                                            if (this.mUsage == 2) {
                                            }
                                            vEVideoSWEncodeSettings52222.mGop = i8;
                                            return vEVideoCompileEncodeSettings;
                                        }
                                    }
                                    i2 = VEVideoEncodeProfile.valueOfString(jSONObject.getJSONObject(str5).getString(str)).ordinal();
                                    vEVideoHWEncodeSettings2.mProfile = i2;
                                    VEVideoHWEncodeSettings vEVideoHWEncodeSettings32 = vEVideoCompileEncodeSettings.mHWEncodeSetting;
                                    str2 = "gop";
                                    if (this.mUsage == 2) {
                                    }
                                    i3 = jSONObject.getJSONObject(str5).getInt(str2);
                                    vEVideoHWEncodeSettings32.mGop = i3;
                                    vEVideoCompileEncodeSettings.mSWEncodeSetting.mBitrateMode = ENCODE_BITRATE_MODE.ENCODE_BITRATE_CRF.ordinal();
                                    VEVideoSWEncodeSettings vEVideoSWEncodeSettings62 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                                    String str722 = "crf";
                                    str3 = "sw";
                                    if (this.mUsage == 2) {
                                    }
                                    i4 = jSONObject.getJSONObject(str3).getInt(str722);
                                    vEVideoSWEncodeSettings62.mCrf = i4;
                                    VEVideoSWEncodeSettings vEVideoSWEncodeSettings2222 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                                    String str8222 = "maxrate";
                                    if (this.mUsage == 2) {
                                    }
                                    j2 = (long) jSONObject.getJSONObject(str3).getInt(str8222);
                                    vEVideoSWEncodeSettings2222.mMaxRate = j2;
                                    VEVideoSWEncodeSettings vEVideoSWEncodeSettings32222 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                                    String str92222 = "preset";
                                    if (this.mUsage == 2) {
                                    }
                                    i5 = jSONObject.getJSONObject(str3).getInt(str92222);
                                    vEVideoSWEncodeSettings32222.mPreset = i5;
                                    VEVideoSWEncodeSettings vEVideoSWEncodeSettings422222 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                                    if (this.mUsage == 2) {
                                    }
                                    vEVideoSWEncodeSettings422222.mProfile = i7;
                                    VEVideoSWEncodeSettings vEVideoSWEncodeSettings522222 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                                    if (this.mUsage == 2) {
                                    }
                                    vEVideoSWEncodeSettings522222.mGop = i8;
                                    return vEVideoCompileEncodeSettings;
                                }
                                j = vEVideoCompileEncodeSettings.mHWEncodeSetting.mBitrate;
                                vEVideoHWEncodeSettings.mBitrate = j;
                                VEVideoHWEncodeSettings vEVideoHWEncodeSettings22 = vEVideoCompileEncodeSettings.mHWEncodeSetting;
                                str = "profile";
                                if (this.mUsage == 2) {
                                }
                                i2 = VEVideoEncodeProfile.valueOfString(jSONObject.getJSONObject(str5).getString(str)).ordinal();
                                vEVideoHWEncodeSettings22.mProfile = i2;
                                VEVideoHWEncodeSettings vEVideoHWEncodeSettings322 = vEVideoCompileEncodeSettings.mHWEncodeSetting;
                                str2 = "gop";
                                if (this.mUsage == 2) {
                                }
                                i3 = jSONObject.getJSONObject(str5).getInt(str2);
                                vEVideoHWEncodeSettings322.mGop = i3;
                                vEVideoCompileEncodeSettings.mSWEncodeSetting.mBitrateMode = ENCODE_BITRATE_MODE.ENCODE_BITRATE_CRF.ordinal();
                                VEVideoSWEncodeSettings vEVideoSWEncodeSettings622 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                                String str7222 = "crf";
                                str3 = "sw";
                                if (this.mUsage == 2) {
                                }
                                i4 = jSONObject.getJSONObject(str3).getInt(str7222);
                                vEVideoSWEncodeSettings622.mCrf = i4;
                                VEVideoSWEncodeSettings vEVideoSWEncodeSettings22222 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                                String str82222 = "maxrate";
                                if (this.mUsage == 2) {
                                }
                                j2 = (long) jSONObject.getJSONObject(str3).getInt(str82222);
                                vEVideoSWEncodeSettings22222.mMaxRate = j2;
                                VEVideoSWEncodeSettings vEVideoSWEncodeSettings322222 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                                String str922222 = "preset";
                                if (this.mUsage == 2) {
                                }
                                i5 = jSONObject.getJSONObject(str3).getInt(str922222);
                                vEVideoSWEncodeSettings322222.mPreset = i5;
                                VEVideoSWEncodeSettings vEVideoSWEncodeSettings4222222 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                                if (this.mUsage == 2) {
                                }
                                vEVideoSWEncodeSettings4222222.mProfile = i7;
                                VEVideoSWEncodeSettings vEVideoSWEncodeSettings5222222 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                                if (this.mUsage == 2) {
                                }
                                vEVideoSWEncodeSettings5222222.mGop = i8;
                                return vEVideoCompileEncodeSettings;
                            }
                        }
                        i6 = jSONObject.getJSONObject(str5).getInt(str6);
                        j = (long) i6;
                        vEVideoHWEncodeSettings.mBitrate = j;
                        VEVideoHWEncodeSettings vEVideoHWEncodeSettings222 = vEVideoCompileEncodeSettings.mHWEncodeSetting;
                        str = "profile";
                        if (this.mUsage == 2) {
                        }
                        i2 = VEVideoEncodeProfile.valueOfString(jSONObject.getJSONObject(str5).getString(str)).ordinal();
                        vEVideoHWEncodeSettings222.mProfile = i2;
                        VEVideoHWEncodeSettings vEVideoHWEncodeSettings3222 = vEVideoCompileEncodeSettings.mHWEncodeSetting;
                        str2 = "gop";
                        if (this.mUsage == 2) {
                        }
                        i3 = jSONObject.getJSONObject(str5).getInt(str2);
                        vEVideoHWEncodeSettings3222.mGop = i3;
                        vEVideoCompileEncodeSettings.mSWEncodeSetting.mBitrateMode = ENCODE_BITRATE_MODE.ENCODE_BITRATE_CRF.ordinal();
                        VEVideoSWEncodeSettings vEVideoSWEncodeSettings6222 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                        String str72222 = "crf";
                        str3 = "sw";
                        if (this.mUsage == 2) {
                        }
                        i4 = jSONObject.getJSONObject(str3).getInt(str72222);
                        vEVideoSWEncodeSettings6222.mCrf = i4;
                        VEVideoSWEncodeSettings vEVideoSWEncodeSettings222222 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                        String str822222 = "maxrate";
                        if (this.mUsage == 2) {
                        }
                        j2 = (long) jSONObject.getJSONObject(str3).getInt(str822222);
                        vEVideoSWEncodeSettings222222.mMaxRate = j2;
                        VEVideoSWEncodeSettings vEVideoSWEncodeSettings3222222 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                        String str9222222 = "preset";
                        if (this.mUsage == 2) {
                        }
                        i5 = jSONObject.getJSONObject(str3).getInt(str9222222);
                        vEVideoSWEncodeSettings3222222.mPreset = i5;
                        VEVideoSWEncodeSettings vEVideoSWEncodeSettings42222222 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                        if (this.mUsage == 2) {
                        }
                        vEVideoSWEncodeSettings42222222.mProfile = i7;
                        VEVideoSWEncodeSettings vEVideoSWEncodeSettings52222222 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                        if (this.mUsage == 2) {
                        }
                        vEVideoSWEncodeSettings52222222.mGop = i8;
                        return vEVideoCompileEncodeSettings;
                    }
                }
                equals = str5.equals(string);
                vEVideoCompileEncodeSettings.useHWEncoder = equals;
                VEVideoHWEncodeSettings vEVideoHWEncodeSettings4 = vEVideoCompileEncodeSettings.mHWEncodeSetting;
                i = this.mUsage;
                String str62 = IjkMediaMeta.IJKM_KEY_BITRATE;
                if (i == 2) {
                }
                i6 = jSONObject.getJSONObject(str5).getInt(str62);
                j = (long) i6;
                vEVideoHWEncodeSettings4.mBitrate = j;
                VEVideoHWEncodeSettings vEVideoHWEncodeSettings2222 = vEVideoCompileEncodeSettings.mHWEncodeSetting;
                str = "profile";
                if (this.mUsage == 2) {
                }
                i2 = VEVideoEncodeProfile.valueOfString(jSONObject.getJSONObject(str5).getString(str)).ordinal();
                vEVideoHWEncodeSettings2222.mProfile = i2;
                VEVideoHWEncodeSettings vEVideoHWEncodeSettings32222 = vEVideoCompileEncodeSettings.mHWEncodeSetting;
                str2 = "gop";
                if (this.mUsage == 2) {
                }
                i3 = jSONObject.getJSONObject(str5).getInt(str2);
                vEVideoHWEncodeSettings32222.mGop = i3;
                vEVideoCompileEncodeSettings.mSWEncodeSetting.mBitrateMode = ENCODE_BITRATE_MODE.ENCODE_BITRATE_CRF.ordinal();
                VEVideoSWEncodeSettings vEVideoSWEncodeSettings62222 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                String str722222 = "crf";
                str3 = "sw";
                if (this.mUsage == 2) {
                }
                i4 = jSONObject.getJSONObject(str3).getInt(str722222);
                vEVideoSWEncodeSettings62222.mCrf = i4;
                VEVideoSWEncodeSettings vEVideoSWEncodeSettings2222222 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                String str8222222 = "maxrate";
                if (this.mUsage == 2) {
                }
                j2 = (long) jSONObject.getJSONObject(str3).getInt(str8222222);
                vEVideoSWEncodeSettings2222222.mMaxRate = j2;
                VEVideoSWEncodeSettings vEVideoSWEncodeSettings32222222 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                String str92222222 = "preset";
                if (this.mUsage == 2) {
                }
                i5 = jSONObject.getJSONObject(str3).getInt(str92222222);
                vEVideoSWEncodeSettings32222222.mPreset = i5;
                VEVideoSWEncodeSettings vEVideoSWEncodeSettings422222222 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                if (this.mUsage == 2) {
                }
                vEVideoSWEncodeSettings422222222.mProfile = i7;
                VEVideoSWEncodeSettings vEVideoSWEncodeSettings522222222 = vEVideoCompileEncodeSettings.mSWEncodeSetting;
                if (this.mUsage == 2) {
                }
                vEVideoSWEncodeSettings522222222.mGop = i8;
            } catch (JSONException e) {
                e.printStackTrace();
                StringBuilder sb = new StringBuilder();
                sb.append("parseJsonToSetting : external json str parse error : ");
                sb.append(e.getLocalizedMessage());
                VELogUtil.e(VEVideoEncodeSettings.TAG, sb.toString());
            }
            return vEVideoCompileEncodeSettings;
        }

        public VEVideoEncodeSettings build() {
            if (!TextUtils.isEmpty(this.exportVideoEncodeSettings.externalSettingsJsonStr)) {
                parseExternalSettingsJsonStr(this.exportVideoEncodeSettings.externalSettingsJsonStr);
            } else {
                overrideWithUserConfig();
            }
            return this.exportVideoEncodeSettings;
        }

        public Builder overrideWithCloudConfig() {
            String simpleName;
            String str;
            if (!VERuntime.getInstance().isUseCloudConfig()) {
                simpleName = VEVideoEncodeSettings.TAG;
                str = "UseCloudConfig is disabled. VEVideoEncodeSettings.overrideWithCloudConfig will do nothing!";
            } else if (PerformanceConfig.sVECloudConfig == null) {
                simpleName = Builder.class.getSimpleName();
                str = "Override with Cloud Configs failed. CloudConfig == null";
            } else {
                int i = this.mUsage;
                if (i == 1) {
                    overrideWithCloudConfigForRecord();
                } else if (i == 2) {
                    overrideWithCloudConfigForCompile();
                } else if (i == 3) {
                    overrideWithCloudConfigForImport();
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("CompileTime BUG, Unexpected usage = ");
                    sb.append(this.mUsage);
                    throw new IllegalStateException(sb.toString());
                }
                return this;
            }
            Log.e(simpleName, str);
            return this;
        }

        public Builder setBps(int i) {
            this.exportVideoEncodeSettings.bitrateMode = ENCODE_BITRATE_MODE.ENCODE_BITRATE_ABR;
            this.exportVideoEncodeSettings.bps = i;
            return this;
        }

        public Builder setCompileType(@NonNull COMPILE_TYPE compile_type) {
            this.exportVideoEncodeSettings.compileType = compile_type;
            return this;
        }

        public Builder setEnableRemuxVideo(boolean z) {
            this.exportVideoEncodeSettings.enableRemuxVideo = z;
            return this;
        }

        public Builder setEncodePreset(@NonNull ENCODE_PRESET encode_preset) {
            this.exportVideoEncodeSettings.swPreset = encode_preset.ordinal();
            return this;
        }

        public Builder setEncodeProfile(@NonNull ENCODE_PROFILE encode_profile) {
            this.exportVideoEncodeSettings.encodeProfile = encode_profile.ordinal();
            return this;
        }

        public Builder setEncodeStandard(ENCODE_STANDARD encode_standard) {
            this.exportVideoEncodeSettings.encodeStandard = encode_standard.ordinal();
            return this;
        }

        public Builder setExternalSettings(String str) {
            this.exportVideoEncodeSettings.externalSettingsJsonStr = str;
            return this;
        }

        public Builder setFps(int i) {
            this.exportVideoEncodeSettings.fps = i;
            return this;
        }

        public Builder setGopSize(int i) {
            this.exportVideoEncodeSettings.gopSize = i;
            return this;
        }

        public Builder setHasBFrame(boolean z) {
            this.exportVideoEncodeSettings.hasBFrame = z;
            return this;
        }

        public Builder setHwEnc(boolean z) {
            this.exportVideoEncodeSettings.useHWEncoder = z;
            return this;
        }

        public Builder setQP(int i) {
            this.exportVideoEncodeSettings.bitrateMode = ENCODE_BITRATE_MODE.ENCODE_BITRATE_QP;
            this.exportVideoEncodeSettings.swQP = i;
            return this;
        }

        public Builder setResizeMode(int i) {
            this.exportVideoEncodeSettings.resizeMode = i;
            return this;
        }

        public Builder setResizeX(float f) {
            this.exportVideoEncodeSettings.resizeX = f;
            return this;
        }

        public Builder setResizeY(float f) {
            this.exportVideoEncodeSettings.resizeY = f;
            return this;
        }

        public Builder setRotate(int i) {
            this.exportVideoEncodeSettings.rotate = i;
            return this;
        }

        public Builder setSWCRF(int i) {
            this.exportVideoEncodeSettings.bitrateMode = ENCODE_BITRATE_MODE.ENCODE_BITRATE_CRF;
            this.exportVideoEncodeSettings.swCRF = i;
            return this;
        }

        public Builder setSpeed(float f) {
            this.exportVideoEncodeSettings.speed = f;
            return this;
        }

        public Builder setSwMaxrate(long j) {
            this.exportVideoEncodeSettings.swMaxrate = j;
            return this;
        }

        public Builder setVideoBitrate(ENCODE_BITRATE_MODE encode_bitrate_mode, int i) {
            this.exportVideoEncodeSettings.bitrateMode = encode_bitrate_mode;
            int i2 = AnonymousClass2.$SwitchMap$com$ss$android$vesdk$VEVideoEncodeSettings$ENCODE_BITRATE_MODE[encode_bitrate_mode.ordinal()];
            if (i2 != 1) {
                if (i2 == 2) {
                    this.exportVideoEncodeSettings.swCRF = i;
                } else if (i2 == 3) {
                    this.exportVideoEncodeSettings.swQP = i;
                } else if (i2 != 4) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("CompileTime BUG. Unhandled enum value ");
                    sb.append(encode_bitrate_mode.toString());
                    throw new IllegalStateException(sb.toString());
                }
                return this;
            }
            this.exportVideoEncodeSettings.bps = i;
            return this;
        }

        public Builder setVideoBitrateMode(ENCODE_BITRATE_MODE encode_bitrate_mode) {
            this.exportVideoEncodeSettings.bitrateMode = encode_bitrate_mode;
            return this;
        }

        public Builder setVideoRes(int i, int i2) {
            this.exportVideoEncodeSettings.outputSize.width = i;
            this.exportVideoEncodeSettings.outputSize.height = i2;
            return this;
        }

        public Builder setWatermarkParam(VEWatermarkParam vEWatermarkParam) {
            this.exportVideoEncodeSettings.mWatermarkParam = vEWatermarkParam;
            return this;
        }
    }

    public enum COMPILE_TYPE implements Parcelable {
        COMPILE_TYPE_MP4,
        COMPILE_TYPE_GIF,
        COMPILE_TYPE_HIGH_GIF;
        
        public static final Creator CREATOR = null;

        static {
            CREATOR = new Creator() {
                public COMPILE_TYPE createFromParcel(Parcel parcel) {
                    return COMPILE_TYPE.values()[parcel.readInt()];
                }

                public COMPILE_TYPE[] newArray(int i) {
                    return new COMPILE_TYPE[i];
                }
            };
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(ordinal());
        }
    }

    public enum ENCODE_BITRATE_MODE implements Parcelable {
        ENCODE_BITRATE_ABR,
        ENCODE_BITRATE_CRF,
        ENCODE_BITRATE_QP,
        ENCODE_BITRATE_VBR;
        
        public static final Creator CREATOR = null;
        private static final ENCODE_BITRATE_MODE[] values = null;

        static {
            values = values();
            CREATOR = new Creator() {
                public ENCODE_BITRATE_MODE createFromParcel(Parcel parcel) {
                    return ENCODE_BITRATE_MODE.values()[parcel.readInt()];
                }

                public ENCODE_BITRATE_MODE[] newArray(int i) {
                    return new ENCODE_BITRATE_MODE[i];
                }
            };
        }

        public static ENCODE_BITRATE_MODE fromInteger(int i) {
            return values[i];
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(ordinal());
        }
    }

    public enum ENCODE_PRESET implements Parcelable {
        ENCODE_LEVEL_ULTRAFAST,
        ENCODE_LEVEL_SUPERFAST,
        ENCODE_LEVEL_VERYFAST,
        ENCODE_LEVEL_FASTER,
        ENCODE_LEVEL_FAST,
        ENCODE_LEVEL_MEDIUM,
        ENCODE_LEVEL_SLOW,
        ENCODE_LEVEL_SLOWER,
        ENCODE_LEVEL_VERYSLOW,
        ENCODE_LEVEL_PLACEBO;
        
        public static final Creator CREATOR = null;

        static {
            CREATOR = new Creator() {
                public ENCODE_PRESET createFromParcel(Parcel parcel) {
                    return ENCODE_PRESET.values()[parcel.readInt()];
                }

                public ENCODE_PRESET[] newArray(int i) {
                    return new ENCODE_PRESET[i];
                }
            };
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(ordinal());
        }
    }

    public enum ENCODE_PROFILE implements Parcelable {
        ENCODE_PROFILE_UNKNOWN,
        ENCODE_PROFILE_BASELINE,
        ENCODE_PROFILE_MAIN,
        ENCODE_PROFILE_HIGH;
        
        public static final Creator CREATOR = null;

        static {
            CREATOR = new Creator() {
                public ENCODE_PROFILE createFromParcel(Parcel parcel) {
                    return ENCODE_PROFILE.values()[parcel.readInt()];
                }

                public ENCODE_PROFILE[] newArray(int i) {
                    return new ENCODE_PROFILE[i];
                }
            };
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(ordinal());
        }
    }

    public enum ENCODE_STANDARD implements Parcelable {
        ENCODE_STANDARD_H264,
        ENCODE_STANDARD_H265,
        ENCODE_STANDARD_MPEG4;
        
        public static final Creator CREATOR = null;

        static {
            CREATOR = new Creator() {
                public ENCODE_STANDARD createFromParcel(Parcel parcel) {
                    return ENCODE_STANDARD.values()[parcel.readInt()];
                }

                public ENCODE_STANDARD[] newArray(int i) {
                    return new ENCODE_STANDARD[i];
                }
            };
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(ordinal());
        }
    }

    private VEVideoEncodeSettings() {
        this.rotate = 0;
        this.resizeMode = 2;
        this.resizeX = 0.0f;
        this.resizeY = 0.0f;
        this.speed = 1.0f;
        this.outputSize = new VESize(576, 1024);
        this.externalSettingsJsonStr = null;
        this.bitrateMode = ENCODE_BITRATE_MODE.ENCODE_BITRATE_ABR;
        this.bps = 4194304;
        this.swCRF = 15;
        this.swQP = 15;
        this.fps = -1;
        this.gopSize = 60;
        this.swPreset = ENCODE_PRESET.ENCODE_LEVEL_ULTRAFAST.ordinal();
        this.encodeStandard = ENCODE_STANDARD.ENCODE_STANDARD_H264.ordinal();
        this.encodeProfile = ENCODE_PROFILE.ENCODE_PROFILE_UNKNOWN.ordinal();
        this.swMaxrate = 15000000;
        this.hasBFrame = false;
        VESize vESize = this.outputSize;
        vESize.width = 576;
        vESize.height = 1024;
        this.fps = -1;
        this.bps = 4194304;
        this.useHWEncoder = true;
        this.compileType = COMPILE_TYPE.COMPILE_TYPE_MP4;
    }

    protected VEVideoEncodeSettings(Parcel parcel) {
        boolean z = false;
        this.rotate = 0;
        this.resizeMode = 2;
        this.resizeX = 0.0f;
        this.resizeY = 0.0f;
        this.speed = 1.0f;
        this.outputSize = new VESize(576, 1024);
        this.externalSettingsJsonStr = null;
        this.bitrateMode = ENCODE_BITRATE_MODE.ENCODE_BITRATE_ABR;
        this.bps = 4194304;
        this.swCRF = 15;
        this.swQP = 15;
        this.fps = -1;
        this.gopSize = 60;
        this.swPreset = ENCODE_PRESET.ENCODE_LEVEL_ULTRAFAST.ordinal();
        this.encodeStandard = ENCODE_STANDARD.ENCODE_STANDARD_H264.ordinal();
        this.encodeProfile = ENCODE_PROFILE.ENCODE_PROFILE_UNKNOWN.ordinal();
        this.swMaxrate = 15000000;
        this.hasBFrame = false;
        this.compileType = (COMPILE_TYPE) parcel.readParcelable(COMPILE_TYPE.class.getClassLoader());
        this.rotate = parcel.readInt();
        this.resizeMode = parcel.readInt();
        this.resizeX = parcel.readFloat();
        this.resizeY = parcel.readFloat();
        this.speed = parcel.readFloat();
        this.outputSize = (VESize) parcel.readParcelable(VESize.class.getClassLoader());
        this.bitrateMode = (ENCODE_BITRATE_MODE) parcel.readParcelable(ENCODE_BITRATE_MODE.class.getClassLoader());
        this.bps = parcel.readInt();
        this.fps = parcel.readInt();
        this.swCRF = parcel.readInt();
        this.swQP = parcel.readInt();
        this.gopSize = parcel.readInt();
        this.swPreset = parcel.readInt();
        this.encodeStandard = parcel.readInt();
        this.encodeProfile = parcel.readInt();
        this.useHWEncoder = parcel.readByte() != 0;
        this.enableRemuxVideo = parcel.readByte() != 0;
        this.enableInterLeave = parcel.readByte() != 0;
        if (parcel.readByte() != 0) {
            z = true;
        }
        this.hasBFrame = z;
        this.swMaxrate = parcel.readLong();
        this.mWatermarkParam = (VEWatermarkParam) parcel.readParcelable(VEWatermarkParam.class.getClassLoader());
        this.mVideoWatermarkCompileEncodeSetting = (VEVideoCompileEncodeSettings) parcel.readParcelable(VEVideoCompileEncodeSettings.class.getClassLoader());
        this.mVideoCompileEncodeSetting = (VEVideoCompileEncodeSettings) parcel.readParcelable(VEVideoCompileEncodeSettings.class.getClassLoader());
        this.externalSettingsJsonStr = parcel.readString();
    }

    @Deprecated
    public VEVideoEncodeSettings(VESize vESize, boolean z) {
        this.rotate = 0;
        this.resizeMode = 2;
        this.resizeX = 0.0f;
        this.resizeY = 0.0f;
        this.speed = 1.0f;
        this.outputSize = new VESize(576, 1024);
        this.externalSettingsJsonStr = null;
        this.bitrateMode = ENCODE_BITRATE_MODE.ENCODE_BITRATE_ABR;
        this.bps = 4194304;
        this.swCRF = 15;
        this.swQP = 15;
        this.fps = -1;
        this.gopSize = 60;
        this.swPreset = ENCODE_PRESET.ENCODE_LEVEL_ULTRAFAST.ordinal();
        this.encodeStandard = ENCODE_STANDARD.ENCODE_STANDARD_H264.ordinal();
        this.encodeProfile = ENCODE_PROFILE.ENCODE_PROFILE_UNKNOWN.ordinal();
        this.swMaxrate = 15000000;
        this.hasBFrame = false;
        this.outputSize = vESize;
        this.useHWEncoder = z;
        this.compileType = COMPILE_TYPE.COMPILE_TYPE_MP4;
    }

    @Deprecated
    public VEVideoEncodeSettings(VESize vESize, boolean z, int i, int i2) {
        this.rotate = 0;
        this.resizeMode = 2;
        this.resizeX = 0.0f;
        this.resizeY = 0.0f;
        this.speed = 1.0f;
        this.outputSize = new VESize(576, 1024);
        this.externalSettingsJsonStr = null;
        this.bitrateMode = ENCODE_BITRATE_MODE.ENCODE_BITRATE_ABR;
        this.bps = 4194304;
        this.swCRF = 15;
        this.swQP = 15;
        this.fps = -1;
        this.gopSize = 60;
        this.swPreset = ENCODE_PRESET.ENCODE_LEVEL_ULTRAFAST.ordinal();
        this.encodeStandard = ENCODE_STANDARD.ENCODE_STANDARD_H264.ordinal();
        this.encodeProfile = ENCODE_PROFILE.ENCODE_PROFILE_UNKNOWN.ordinal();
        this.swMaxrate = 15000000;
        this.hasBFrame = false;
        this.outputSize = vESize;
        this.useHWEncoder = z;
        this.fps = i;
        this.gopSize = i2;
        this.compileType = COMPILE_TYPE.COMPILE_TYPE_MP4;
    }

    @Deprecated
    public VEVideoEncodeSettings(VESize vESize, boolean z, int i, int i2, int i3, int i4, boolean z2) {
        this.rotate = 0;
        this.resizeMode = 2;
        this.resizeX = 0.0f;
        this.resizeY = 0.0f;
        this.speed = 1.0f;
        this.outputSize = new VESize(576, 1024);
        this.externalSettingsJsonStr = null;
        this.bitrateMode = ENCODE_BITRATE_MODE.ENCODE_BITRATE_ABR;
        this.bps = 4194304;
        this.swCRF = 15;
        this.swQP = 15;
        this.fps = -1;
        this.gopSize = 60;
        this.swPreset = ENCODE_PRESET.ENCODE_LEVEL_ULTRAFAST.ordinal();
        this.encodeStandard = ENCODE_STANDARD.ENCODE_STANDARD_H264.ordinal();
        this.encodeProfile = ENCODE_PROFILE.ENCODE_PROFILE_UNKNOWN.ordinal();
        this.swMaxrate = 15000000;
        this.hasBFrame = false;
        this.outputSize = vESize;
        this.useHWEncoder = z;
        this.fps = i;
        this.gopSize = i2;
        this.bps = i3;
        this.swPreset = i4;
        this.hasBFrame = z2;
        this.compileType = COMPILE_TYPE.COMPILE_TYPE_MP4;
    }

    public int describeContents() {
        return 0;
    }

    public ENCODE_BITRATE_MODE getBitrateMode() {
        return this.bitrateMode;
    }

    public int getBitrateValue() {
        int i = AnonymousClass2.$SwitchMap$com$ss$android$vesdk$VEVideoEncodeSettings$ENCODE_BITRATE_MODE[this.bitrateMode.ordinal()];
        if (i == 1) {
            return getBps();
        }
        if (i == 2) {
            return getSwCRF();
        }
        if (i == 3) {
            return getSwQP();
        }
        if (i == 4) {
            return getBps();
        }
        throw new IllegalStateException("CompileTime BUG by SDK. Unhandled ENCODE_BITRATE_MODE enum value.");
    }

    public int getBps() {
        return this.bps;
    }

    public COMPILE_TYPE getCompileType() {
        return this.compileType;
    }

    public int getEncodeProfile() {
        return this.encodeProfile;
    }

    public int getFps() {
        return this.fps;
    }

    public int getGopSize() {
        return this.gopSize;
    }

    public int getResizeMode() {
        return this.resizeMode;
    }

    public float getResizeX() {
        return this.resizeX;
    }

    public float getResizeY() {
        return this.resizeY;
    }

    public int getRotate() {
        return this.rotate;
    }

    public float getSpeed() {
        return this.speed;
    }

    public int getSwCRF() {
        return this.swCRF;
    }

    public long getSwMaxRate() {
        return this.swMaxrate;
    }

    public int getSwPreset() {
        return this.swPreset;
    }

    public int getSwQP() {
        return this.swQP;
    }

    public VEVideoCompileEncodeSettings getVideoCompileEncodeSetting() {
        return this.mVideoCompileEncodeSetting;
    }

    public VESize getVideoRes() {
        return this.outputSize;
    }

    public VEVideoCompileEncodeSettings getWatermarkCompileEncodeSetting() {
        return this.mVideoWatermarkCompileEncodeSetting;
    }

    public VEWatermarkParam getWatermarkParam() {
        return this.mWatermarkParam;
    }

    public boolean isEnableInterLeave() {
        return this.enableInterLeave;
    }

    public boolean isEnableRemuxVideo() {
        return this.enableRemuxVideo;
    }

    public boolean isHwEnc() {
        return this.useHWEncoder;
    }

    @Deprecated
    public void setBps(int i) {
        this.bitrateMode = ENCODE_BITRATE_MODE.ENCODE_BITRATE_VBR;
        this.bps = i;
    }

    public void setCompileType(COMPILE_TYPE compile_type) {
        this.compileType = compile_type;
    }

    public void setEnableInterLeave(boolean z) {
        this.enableInterLeave = z;
    }

    public void setEnableRemuxVideo(boolean z) {
        this.enableRemuxVideo = z;
    }

    public void setEncodeProfile(ENCODE_PROFILE encode_profile) {
        this.encodeProfile = encode_profile.ordinal();
    }

    public void setFps(int i) {
        this.fps = i;
    }

    public void setGopSize(int i) {
        this.gopSize = i;
    }

    public void setHwEnc(boolean z) {
        this.useHWEncoder = z;
    }

    public void setQP(int i) {
        this.bitrateMode = ENCODE_BITRATE_MODE.ENCODE_BITRATE_QP;
        this.swQP = i;
    }

    public void setResizeMode(int i) {
        this.resizeMode = i;
    }

    public void setResizeX(float f) {
        this.resizeX = f;
    }

    public void setResizeY(float f) {
        this.resizeY = f;
    }

    public void setRotate(int i) {
        this.rotate = i;
    }

    public void setSWCRF(int i) {
        this.bitrateMode = ENCODE_BITRATE_MODE.ENCODE_BITRATE_CRF;
        this.swCRF = i;
    }

    public void setSpeed(float f) {
        this.speed = f;
    }

    public void setSwPreset(ENCODE_PRESET encode_preset) {
        this.swPreset = encode_preset.ordinal();
    }

    public void setVideoBitrate(ENCODE_BITRATE_MODE encode_bitrate_mode, int i) {
        this.bitrateMode = encode_bitrate_mode;
        int i2 = AnonymousClass2.$SwitchMap$com$ss$android$vesdk$VEVideoEncodeSettings$ENCODE_BITRATE_MODE[this.bitrateMode.ordinal()];
        if (i2 != 1) {
            if (i2 == 2) {
                this.swCRF = i;
                return;
            } else if (i2 == 3) {
                this.swQP = i;
                return;
            } else if (i2 != 4) {
                throw new IllegalStateException("CompileTime BUG by sdk. Unhandled bitrateMode");
            }
        }
        this.bps = i;
    }

    public void setVideoRes(int i, int i2) {
        VESize vESize = this.outputSize;
        vESize.width = i;
        vESize.height = i2;
    }

    public void setWatermark(VEWatermarkParam vEWatermarkParam) {
        this.mWatermarkParam = vEWatermarkParam;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("VEVideoEncodeSettings{compileType=");
        sb.append(this.compileType);
        sb.append(", rotate=");
        sb.append(this.rotate);
        sb.append(", resizeMode=");
        sb.append(this.resizeMode);
        sb.append(", resizeX=");
        sb.append(this.resizeX);
        sb.append(", resizeY=");
        sb.append(this.resizeY);
        sb.append(", speed=");
        sb.append(this.speed);
        sb.append(", outputSize=");
        sb.append(this.outputSize);
        sb.append(", bitrateMode=");
        sb.append(this.bitrateMode);
        sb.append(", bps=");
        sb.append(this.bps);
        sb.append(", swCRF=");
        sb.append(this.swCRF);
        sb.append(", swQP=");
        sb.append(this.swQP);
        sb.append(", fps=");
        sb.append(this.fps);
        sb.append(", gopSize=");
        sb.append(this.gopSize);
        sb.append(", swPreset=");
        sb.append(this.swPreset);
        sb.append(", encodeStandard=");
        sb.append(this.encodeStandard);
        sb.append(", encodeProfile=");
        sb.append(this.encodeProfile);
        sb.append(", swMaxrate=");
        sb.append(this.swMaxrate);
        sb.append(", useHWEncoder=");
        sb.append(this.useHWEncoder);
        sb.append(", enableRemuxVideo=");
        sb.append(this.enableRemuxVideo);
        sb.append(", enableInterLeave=");
        sb.append(this.enableInterLeave);
        sb.append(", hasBFrame=");
        sb.append(this.hasBFrame);
        sb.append(", mWatermarkParam=");
        sb.append(this.mWatermarkParam);
        sb.append(", mVideoWatermarkCompileEncodeSetting=");
        sb.append(this.mVideoWatermarkCompileEncodeSetting);
        sb.append(", mVideoCompileEncodeSetting=");
        sb.append(this.mVideoCompileEncodeSetting);
        sb.append('}');
        return sb.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.compileType, i);
        parcel.writeInt(this.rotate);
        parcel.writeInt(this.resizeMode);
        parcel.writeFloat(this.resizeX);
        parcel.writeFloat(this.resizeY);
        parcel.writeFloat(this.speed);
        parcel.writeParcelable(this.outputSize, i);
        parcel.writeParcelable(this.bitrateMode, i);
        parcel.writeInt(this.bps);
        parcel.writeInt(this.fps);
        parcel.writeInt(this.swCRF);
        parcel.writeInt(this.swQP);
        parcel.writeInt(this.gopSize);
        parcel.writeInt(this.swPreset);
        parcel.writeInt(this.encodeStandard);
        parcel.writeInt(this.encodeProfile);
        parcel.writeByte(this.useHWEncoder ? (byte) 1 : 0);
        parcel.writeByte(this.enableRemuxVideo ? (byte) 1 : 0);
        parcel.writeByte(this.enableInterLeave ? (byte) 1 : 0);
        parcel.writeByte(this.hasBFrame ? (byte) 1 : 0);
        parcel.writeLong(this.swMaxrate);
        parcel.writeParcelable(this.mWatermarkParam, i);
        parcel.writeParcelable(this.mVideoWatermarkCompileEncodeSetting, i);
        parcel.writeParcelable(this.mVideoCompileEncodeSetting, i);
        parcel.writeString(this.externalSettingsJsonStr);
    }
}
