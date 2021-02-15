package com.ss.android.medialib.config;

import android.util.Log;
import androidx.annotation.Keep;
import com.ss.android.ttve.nativePort.TENativeLibsLoader;
import java.util.regex.Pattern;

public class GPUModelDetector {
    private static final String TAG = "GPUModelDetector";
    private static final GPUModelDetector instance = new GPUModelDetector();
    /* access modifiers changed from: private */
    public boolean isSurpportAdvancedEffect;
    /* access modifiers changed from: private */
    public boolean isSurpportHighResolutionRecord;
    private ENvGpuModel mGPUModel;
    private int mGPUModelNumber;
    private ENvGpuSubModel mGPUSubModel;

    @Keep
    public enum ENvGpuModel {
        Unknown,
        Adreno,
        Mali,
        PowerVR,
        Intel,
        NVIDIA,
        Vivante,
        VideoCore
    }

    @Keep
    public enum ENvGpuSubModel {
        Unknown,
        Adreno,
        Mali,
        Mali_MP,
        Mali_T,
        PowerVR_SGX,
        PowerVR_SGX_MP,
        PowerVR_SGX_MP2,
        PowerVR_Rogue_Han,
        PowerVR_Rogue_Hood,
        PowerVR_Rogue_Marlowe,
        PowerVR_Rogue_G,
        Intel_HD_Graphics,
        NVIDIA_Tegra,
        NVIDIA_Tegra_X1,
        NVIDIA_AP,
        Vivante_GC,
        VideoCore_IV_HW
    }

    public class GPUModelInfo {
        private ENvGpuModel mGPUModel;
        private int mGPUModelNumber;
        private ENvGpuSubModel mGPUSubModel;

        public GPUModelInfo(ENvGpuModel eNvGpuModel, ENvGpuSubModel eNvGpuSubModel, int i) {
            this.mGPUModel = eNvGpuModel;
            this.mGPUSubModel = eNvGpuSubModel;
            this.mGPUModelNumber = i;
        }

        public ENvGpuModel getGPUModel() {
            return this.mGPUModel;
        }

        public int getGPUModelNumber() {
            return this.mGPUModelNumber;
        }

        public ENvGpuSubModel getGPUSubModel() {
            return this.mGPUSubModel;
        }
    }

    static {
        TENativeLibsLoader.loadLibrary();
    }

    private GPUModelDetector() {
    }

    private void DetectAdrenoGpu(String[] strArr) {
        String str;
        int length = strArr.length;
        if (length >= 2) {
            if (strArr[1].equalsIgnoreCase("(TM)")) {
                if (length == 3 && isNumeric(strArr[2])) {
                    this.mGPUSubModel = ENvGpuSubModel.Adreno;
                    str = strArr[2];
                }
            }
            if (isNumeric(strArr[1])) {
                this.mGPUSubModel = ENvGpuSubModel.Adreno;
                str = strArr[1];
            }
            this.mGPUModelNumber = parseInt(str);
        }
    }

    private void DetectIntelGpu(String[] strArr) {
        int length = strArr.length;
        if (length >= 3 && strArr[1].equalsIgnoreCase("HD") && strArr[2].equalsIgnoreCase("Graphics")) {
            this.mGPUSubModel = ENvGpuSubModel.Intel_HD_Graphics;
            if (length == 4 && isNumeric(strArr[3])) {
                this.mGPUModelNumber = parseInt(strArr[3]);
            }
        }
    }

    private void DetectMaliGpu(String[] strArr) {
        String substring;
        ENvGpuSubModel eNvGpuSubModel;
        int length = strArr.length;
        if (length >= 2) {
            if (isNumeric(strArr[1])) {
                if (length == 2) {
                    eNvGpuSubModel = ENvGpuSubModel.Mali;
                } else {
                    if (length == 3 && strArr[2].equalsIgnoreCase("MP")) {
                        eNvGpuSubModel = ENvGpuSubModel.Mali_MP;
                    }
                    substring = strArr[1];
                }
                this.mGPUSubModel = eNvGpuSubModel;
                substring = strArr[1];
            } else {
                if (length == 2 && strArr[1] != null && strArr[1].trim().length() > 0 && strArr[1].charAt(0) == 'T') {
                    substring = strArr[1].substring(1);
                    if (isNumeric(substring)) {
                        this.mGPUSubModel = ENvGpuSubModel.Mali_T;
                    }
                }
            }
            this.mGPUModelNumber = parseInt(substring);
        }
    }

    private void DetectNVIDIAGpu(String[] strArr) {
        ENvGpuSubModel eNvGpuSubModel;
        int length = strArr.length;
        if (length >= 2) {
            if (strArr[1].equalsIgnoreCase("Tegra")) {
                eNvGpuSubModel = (length < 3 || !strArr[2].equalsIgnoreCase("X1")) ? ENvGpuSubModel.NVIDIA_Tegra : ENvGpuSubModel.NVIDIA_Tegra_X1;
            } else {
                if (strArr[1].equalsIgnoreCase("AP")) {
                    eNvGpuSubModel = ENvGpuSubModel.NVIDIA_AP;
                }
            }
            this.mGPUSubModel = eNvGpuSubModel;
        }
    }

    private void DetectPowerVRGpu(String[] strArr) {
        String str;
        ENvGpuSubModel eNvGpuSubModel;
        ENvGpuSubModel eNvGpuSubModel2;
        int length = strArr.length;
        if (length >= 2) {
            if (strArr[1].equalsIgnoreCase("SGX")) {
                if (length == 3) {
                    if (strArr[2].substring(strArr[2].length() - 2).equalsIgnoreCase("MP")) {
                        str = strArr[2].substring(0, strArr[2].length() - 2);
                        if (isNumeric(str)) {
                            eNvGpuSubModel = ENvGpuSubModel.PowerVR_SGX_MP;
                            this.mGPUSubModel = eNvGpuSubModel;
                        }
                    }
                    if (strArr[2].substring(strArr[2].length() - 3).equalsIgnoreCase("MP2")) {
                        str = strArr[2].substring(0, strArr[2].length() - 3);
                        if (isNumeric(str)) {
                            eNvGpuSubModel = ENvGpuSubModel.PowerVR_SGX_MP2;
                            this.mGPUSubModel = eNvGpuSubModel;
                        }
                    } else if (isNumeric(strArr[2])) {
                        this.mGPUSubModel = ENvGpuSubModel.PowerVR_SGX;
                        str = strArr[2];
                    }
                }
                return;
            } else if (strArr[1].equalsIgnoreCase("Rogue") && length == 3) {
                if (strArr[2].equalsIgnoreCase("Han")) {
                    eNvGpuSubModel2 = ENvGpuSubModel.PowerVR_Rogue_Han;
                } else if (strArr[2].equalsIgnoreCase("Hood")) {
                    eNvGpuSubModel2 = ENvGpuSubModel.PowerVR_Rogue_Hood;
                } else if (strArr[2].equalsIgnoreCase("Marlowe")) {
                    eNvGpuSubModel2 = ENvGpuSubModel.PowerVR_Rogue_Marlowe;
                } else {
                    if (strArr[2].startsWith("G")) {
                        str = strArr[2].substring(1);
                        if (isNumeric(str)) {
                            eNvGpuSubModel = ENvGpuSubModel.PowerVR_Rogue_G;
                            this.mGPUSubModel = eNvGpuSubModel;
                        }
                    }
                }
                this.mGPUSubModel = eNvGpuSubModel2;
            } else {
                return;
            }
            this.mGPUModelNumber = parseInt(str);
        }
    }

    private void DetectVideoCoreGpu(String[] strArr) {
        if (strArr.length >= 3 && strArr[1].equalsIgnoreCase("IV") && strArr[2].equalsIgnoreCase("HW")) {
            this.mGPUSubModel = ENvGpuSubModel.VideoCore_IV_HW;
        }
    }

    private void DetectVivanteGCGpu(String[] strArr) {
        if (strArr.length >= 2) {
            this.mGPUSubModel = ENvGpuSubModel.Vivante_GC;
            this.mGPUModelNumber = parseInt(stripNonDigitCharFromString(strArr[0]));
        }
    }

    private void DetectVivanteGpu(String[] strArr) {
        if (strArr.length >= 2 && !strArr[1].equalsIgnoreCase("GC")) {
            this.mGPUSubModel = ENvGpuSubModel.Vivante_GC;
            this.mGPUModelNumber = parseInt(stripNonDigitCharFromString(strArr[1]));
        }
    }

    /* access modifiers changed from: private */
    public GPUModelInfo getGPUModel(String str) {
        if (str != null && str.trim().length() > 0) {
            this.mGPUModel = ENvGpuModel.Unknown;
            this.mGPUSubModel = ENvGpuSubModel.Unknown;
            this.mGPUModelNumber = 0;
            String[] split = str.split("[-\\s+]");
            if (split.length >= 1) {
                String trim = split[0].trim();
                if (trim.equalsIgnoreCase("Adreno")) {
                    this.mGPUModel = ENvGpuModel.Adreno;
                    DetectAdrenoGpu(split);
                } else if (trim.equalsIgnoreCase("Mali")) {
                    this.mGPUModel = ENvGpuModel.Mali;
                    DetectMaliGpu(split);
                } else if (trim.equalsIgnoreCase("PowerVR")) {
                    this.mGPUModel = ENvGpuModel.PowerVR;
                    DetectPowerVRGpu(split);
                } else if (trim.equalsIgnoreCase("Intel") || trim.equalsIgnoreCase("Intel(R)")) {
                    this.mGPUModel = ENvGpuModel.Intel;
                    DetectIntelGpu(split);
                } else if (trim.equalsIgnoreCase("NVIDIA")) {
                    this.mGPUModel = ENvGpuModel.Intel;
                    DetectNVIDIAGpu(split);
                } else if (trim.equalsIgnoreCase("Vivante")) {
                    this.mGPUModel = ENvGpuModel.Vivante;
                    DetectVivanteGpu(split);
                } else if (trim.equalsIgnoreCase("GC1000")) {
                    this.mGPUModel = ENvGpuModel.Vivante;
                    DetectVivanteGCGpu(split);
                } else if (trim.equalsIgnoreCase("VideoCore")) {
                    this.mGPUModel = ENvGpuModel.VideoCore;
                    DetectVideoCoreGpu(split);
                }
            }
        }
        return new GPUModelInfo(this.mGPUModel, this.mGPUSubModel, this.mGPUModelNumber);
    }

    public static GPUModelDetector getInstance() {
        return instance;
    }

    private boolean isNumeric(String str) {
        return Pattern.compile("-?[0-9]+.?[0-9]+").matcher(str).matches() || Pattern.compile("[0-9]+").matcher(str).matches();
    }

    public static native String nativeGetGLVersion();

    public static native String nativeGetGPURenderer();

    private int parseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private String stripNonDigitCharFromString(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            String valueOf = String.valueOf(str.charAt(i));
            if (isNumeric(valueOf)) {
                sb.append(valueOf);
            }
        }
        return sb.toString();
    }

    public void detectGPUSurpportAdvancedEffect() {
        new Thread() {
            public void run() {
                String nativeGetGPURenderer = GPUModelDetector.nativeGetGPURenderer();
                StringBuilder sb = new StringBuilder();
                sb.append("gpurender is");
                sb.append(nativeGetGPURenderer);
                String sb2 = sb.toString();
                String str = GPUModelDetector.TAG;
                Log.i(str, sb2);
                boolean z = false;
                if (nativeGetGPURenderer != null) {
                    GPUModelInfo access$000 = GPUModelDetector.this.getGPUModel(nativeGetGPURenderer);
                    ENvGpuModel gPUModel = access$000.getGPUModel();
                    ENvGpuSubModel gPUSubModel = access$000.getGPUSubModel();
                    int gPUModelNumber = access$000.getGPUModelNumber();
                    if (gPUModel != ENvGpuModel.Vivante) {
                    }
                }
                z = true;
                GPUModelDetector.this.isSurpportAdvancedEffect = z;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("isSurpportAdvancedEffect:");
                sb3.append(GPUModelDetector.this.isSurpportAdvancedEffect);
                Log.i(str, sb3.toString());
            }
        }.start();
    }

    public void detectGPUSurpportHighResolutionRecord() {
        new Thread() {
            public void run() {
                String nativeGetGPURenderer = GPUModelDetector.nativeGetGPURenderer();
                StringBuilder sb = new StringBuilder();
                sb.append("gpurender is");
                sb.append(nativeGetGPURenderer);
                String sb2 = sb.toString();
                String str = GPUModelDetector.TAG;
                Log.i(str, sb2);
                boolean z = false;
                if (nativeGetGPURenderer != null) {
                    GPUModelInfo access$000 = GPUModelDetector.this.getGPUModel(nativeGetGPURenderer);
                    ENvGpuModel gPUModel = access$000.getGPUModel();
                    ENvGpuSubModel gPUSubModel = access$000.getGPUSubModel();
                    int gPUModelNumber = access$000.getGPUModelNumber();
                    if (gPUModel != ENvGpuModel.Vivante) {
                    }
                }
                z = true;
                GPUModelDetector.this.isSurpportHighResolutionRecord = z;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("isSurpportHighResolutionSupport:");
                sb3.append(GPUModelDetector.this.isSurpportHighResolutionRecord);
                Log.i(str, sb3.toString());
            }
        }.start();
    }

    public String getGLVersion() {
        return nativeGetGLVersion();
    }

    public GPUModelInfo getGPUModel() {
        return getGPUModel(nativeGetGPURenderer());
    }

    public boolean isSurpportAdvancedEffect() {
        return this.isSurpportAdvancedEffect;
    }

    public boolean isSurpportHighResolutionRecord() {
        return this.isSurpportHighResolutionRecord;
    }
}
