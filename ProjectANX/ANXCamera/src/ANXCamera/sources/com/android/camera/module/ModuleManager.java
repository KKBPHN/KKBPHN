package com.android.camera.module;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.android.camera.CameraSettings;
import com.android.camera.constant.ModeConstant;
import com.android.camera.data.observeable.VMFeature;
import com.android.camera.dualvideo.DualVideoRecordModule;
import com.android.camera.dualvideo.DualVideoSelectModule;
import com.android.camera.features.mimoji2.module.MimojiModule;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.MultiFeatureManager;

public class ModuleManager {
    public static final int MODULE_INDEX_NONE = -1;
    private static int sCurrentModuleIndex = -1;
    private static ModuleManager sInstance = new ModuleManager();

    private static Module createModuleByAlias(String str) {
        try {
            return (Module) Class.forName(str).newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (InstantiationException e3) {
            e3.printStackTrace();
        }
        return null;
    }

    public static int getActiveModuleIndex() {
        return sCurrentModuleIndex;
    }

    private static Module getCameraByDevice() {
        return new Camera2Module();
    }

    @Nullable
    public static Module getModuleByIndex(int i) {
        String featureNameByLocalMode = VMFeature.getFeatureNameByLocalMode(i);
        if (!TextUtils.isEmpty(featureNameByLocalMode)) {
            MultiFeatureManager multiFeatureManager = (MultiFeatureManager) ModeCoordinatorImpl.getInstance().getAttachProtocol(929);
            if (multiFeatureManager != null && !multiFeatureManager.hasFeatureInstalled(featureNameByLocalMode)) {
                FakerModule fakerModule = new FakerModule();
                fakerModule.setTargetFeatureName(featureNameByLocalMode);
                return fakerModule;
            }
        }
        if (i != 169) {
            if (i == 254) {
                return new FakerModule();
            }
            if (i == 204) {
                return CameraSettings.getDualVideoConfig().ismDrawSelectWindow() ? new DualVideoSelectModule() : new DualVideoRecordModule();
            }
            if (i != 205) {
                switch (i) {
                    case 161:
                        return new FunModule();
                    case 162:
                        break;
                    case 163:
                        break;
                    default:
                        switch (i) {
                            case 165:
                            case 167:
                                break;
                            case 166:
                                return new Panorama3Module();
                            default:
                                switch (i) {
                                    case 171:
                                    case 173:
                                    case 175:
                                        break;
                                    case 172:
                                        break;
                                    case 174:
                                    case 177:
                                        return new LiveModule();
                                    case 176:
                                        return new WideSelfieModule();
                                    default:
                                        switch (i) {
                                            case 179:
                                                return new LiveModuleSubVV();
                                            case 180:
                                                break;
                                            case 181:
                                                return createModuleByAlias(ModeConstant.MODE_FUN2_ALIAS);
                                            case 182:
                                            case 186:
                                                break;
                                            case 183:
                                                return new MiLiveModule();
                                            case 184:
                                                return new MimojiModule();
                                            case 185:
                                                return new CloneModule();
                                            case 187:
                                                return new AmbilightModule();
                                            case 188:
                                                return new SuperMoonModule();
                                            case 189:
                                                return new DollyZoomModule();
                                            default:
                                                switch (i) {
                                                    case 207:
                                                    case 208:
                                                        return new VideoModule();
                                                    case 209:
                                                    case 210:
                                                    case 211:
                                                        return new FunModule();
                                                    case 212:
                                                        return new FilmDreamModule();
                                                    case 213:
                                                        return new TimeFreezeModule();
                                                    case 214:
                                                        break;
                                                    default:
                                                        return null;
                                                }
                                        }
                                }
                        }
                }
            }
            return getCameraByDevice();
        }
        return getVideoByDevice();
    }

    private static Module getVideoByDevice() {
        return new VideoModule();
    }

    public static ModuleManager instance() {
        return sInstance;
    }

    public static boolean isCameraModule() {
        return sCurrentModuleIndex == 163;
    }

    public static boolean isCapture() {
        return isCameraModule() || isSquareModule() || isProPhotoModule() || isSuperNightScene() || isUltraPixel();
    }

    public static boolean isDocumentMode() {
        return CameraSettings.isDocumentModeOn(sCurrentModuleIndex);
    }

    public static boolean isFastMotionModule() {
        return sCurrentModuleIndex == 169;
    }

    public static boolean isFastmotionModulePro() {
        return isFastMotionModule() && C0122O00000o.instance().OOO00Oo();
    }

    public static boolean isFunARModule() {
        int i = sCurrentModuleIndex;
        return i == 177 || i == 184;
    }

    public static boolean isFunModule() {
        return sCurrentModuleIndex == 161;
    }

    public static boolean isIDCardMode() {
        return sCurrentModuleIndex == 182;
    }

    public static boolean isInVideoCategory() {
        return isVideoCategory(sCurrentModuleIndex);
    }

    public static boolean isLiveModule() {
        return sCurrentModuleIndex == 174;
    }

    public static boolean isMiLiveModule() {
        return sCurrentModuleIndex == 183;
    }

    public static boolean isPanoramaModule() {
        return sCurrentModuleIndex == 166;
    }

    public static boolean isPortraitModule() {
        return sCurrentModuleIndex == 171;
    }

    public static boolean isProModule() {
        return isProPhotoModule() || isProVideoModule();
    }

    public static boolean isProPhotoModule() {
        return sCurrentModuleIndex == 167;
    }

    public static boolean isProVideoModule() {
        return sCurrentModuleIndex == 180;
    }

    public static boolean isSquareModule() {
        return sCurrentModuleIndex == 165;
    }

    public static boolean isSuperMoonMode() {
        return sCurrentModuleIndex == 188;
    }

    public static boolean isSuperNightScene() {
        return sCurrentModuleIndex == 173;
    }

    public static boolean isSuperNightVideoMode() {
        return sCurrentModuleIndex == 214;
    }

    public static boolean isSupportCropFrontMode() {
        return isCameraModule() || isSquareModule() || isPortraitModule() || isSuperNightScene();
    }

    public static boolean isUltraPixel() {
        return sCurrentModuleIndex == 175;
    }

    public static boolean isVideoCategory(int i) {
        if (!(i == 161 || i == 162 || i == 169 || i == 172 || i == 174 || i == 180 || i == 183 || i == 189 || i == 204 || i == 207 || i == 208)) {
            switch (i) {
                case 211:
                case 212:
                case 213:
                case 214:
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    public static boolean isVideoModule() {
        return sCurrentModuleIndex == 162;
    }

    public static boolean isVideoNewSlowMotion() {
        return sCurrentModuleIndex == 172;
    }

    public static boolean isWideSelfieModule() {
        return sCurrentModuleIndex == 176;
    }

    public static void setActiveModuleIndex(int i) {
        sCurrentModuleIndex = i;
    }
}
