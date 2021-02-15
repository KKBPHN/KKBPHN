package com.android.camera.module.impl.component;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import com.android.camera.ActivityBase;
import com.android.camera.CameraSettings;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.R;
import com.android.camera.aftersales.AftersalesManager;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.config.ComponentConfigSlowMotion;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiAvatarEngine2;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.ActionProcessing;
import com.android.camera.protocol.ModeProtocol.AmbilightProtocol;
import com.android.camera.protocol.ModeProtocol.AmbilightSelector;
import com.android.camera.protocol.ModeProtocol.BackStack;
import com.android.camera.protocol.ModeProtocol.BottomPopupTips;
import com.android.camera.protocol.ModeProtocol.CameraClickObservable;
import com.android.camera.protocol.ModeProtocol.CloneProcess;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.DualController;
import com.android.camera.protocol.ModeProtocol.FilmDreamProcess;
import com.android.camera.protocol.ModeProtocol.LiveVVProcess;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.protocol.ModeProtocol.ManuallyAdjust;
import com.android.camera.protocol.ModeProtocol.MiBeautyProtocol;
import com.android.camera.protocol.ModeProtocol.MimojiAvatarEngine;
import com.android.camera.protocol.ModeProtocol.PanoramaProtocol;
import com.android.camera.protocol.ModeProtocol.RecordState;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.protocol.ModeProtocol.WideSelfieProtocol;
import com.android.camera.protocol.ModeProtocol.ZoomProtocol;

public class RecordingStateChangeImpl implements RecordState {
    private static final String TAG = "RecordingState";
    private ActivityBase mActivity;

    public RecordingStateChangeImpl(ActivityBase activityBase) {
        this.mActivity = activityBase;
    }

    public static RecordingStateChangeImpl create(ActivityBase activityBase) {
        return new RecordingStateChangeImpl(activityBase);
    }

    private int getCurrentModuleIndex() {
        ActivityBase activityBase = this.mActivity;
        if (activityBase == null) {
            return 160;
        }
        return activityBase.getCurrentModuleIndex();
    }

    private void setZoomRatio(float f, float f2) {
        ZoomProtocol zoomProtocol = (ZoomProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(170);
        if (zoomProtocol != null) {
            zoomProtocol.setMaxZoomRatio(f);
            zoomProtocol.setMinZoomRatio(f2);
        }
    }

    private void updateRightTips(TopAlert topAlert, int i) {
        int i2;
        int i3;
        boolean isProVideoLogOpen = CameraSettings.isProVideoLogOpen(getCurrentModuleIndex());
        boolean z = CameraSettings.isVideoQuality8KOpen(getCurrentModuleIndex()) && !CameraSettings.isReal8K();
        boolean isHdr10Alive = CameraSettings.isHdr10Alive(getCurrentModuleIndex());
        boolean isHdr10PlusAlive = CameraSettings.isHdr10PlusAlive(getCurrentModuleIndex());
        if (topAlert != null) {
            if (isHdr10Alive) {
                i3 = R.string.video_hdr10_tip;
            } else if (isHdr10PlusAlive) {
                i3 = R.string.video_hdr10plus_tip;
            } else if (z) {
                i3 = R.string.video_ultra_clear_tip_8k;
            } else {
                if (isProVideoLogOpen) {
                    i3 = R.string.pref_camera_video_log_tips;
                }
                if (getCurrentModuleIndex() != 208 || getCurrentModuleIndex() == 212) {
                    i2 = R.string.film_video_tip;
                } else if (getCurrentModuleIndex() == 207) {
                    i2 = R.string.film_video_b_tip;
                } else {
                    return;
                }
                topAlert.alertAiDetectTipHint(8, i2, -1);
            }
            topAlert.alertVideoUltraClear(i, i3);
            if (getCurrentModuleIndex() != 208) {
            }
            i2 = R.string.film_video_tip;
            topAlert.alertAiDetectTipHint(8, i2, -1);
        }
    }

    public void onFailed() {
        Log.d(TAG, "onFailed");
        onFinish();
        AftersalesManager.getInstance().count(System.currentTimeMillis(), 7);
        ((ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162)).processingFailed();
    }

    public void onFinish() {
        int[] iArr;
        Log.d(TAG, "onFinish");
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
        ConfigChangeImpl configChangeImpl = (ConfigChangeImpl) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
        updateRightTips(topAlert, 0);
        int currentModuleIndex = getCurrentModuleIndex();
        if (currentModuleIndex != 169) {
            if (currentModuleIndex == 174) {
                if (bottomPopupTips != null) {
                    bottomPopupTips.reInitTipImage();
                }
                if (actionProcessing != null) {
                    actionProcessing.processingFinish();
                }
                if (topAlert != null) {
                    topAlert.showConfigMenu();
                    topAlert.setRecordingTimeState(2);
                    iArr = new int[]{225, 245};
                } else {
                    return;
                }
            } else if (currentModuleIndex == 183) {
                if (bottomPopupTips != null) {
                    bottomPopupTips.reInitTipImage();
                }
                if (actionProcessing != null) {
                    actionProcessing.processingFinish();
                }
                if (topAlert != null) {
                    topAlert.showConfigMenu();
                    topAlert.setRecordingTimeState(2);
                    iArr = new int[]{197, 245};
                } else {
                    return;
                }
            } else if (currentModuleIndex != 187) {
                if (currentModuleIndex == 212) {
                    FilmDreamProcess filmDreamProcess = (FilmDreamProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(931);
                    if (filmDreamProcess != null) {
                        filmDreamProcess.processingFinish();
                    }
                    if (topAlert != null) {
                        topAlert.showConfigMenu();
                    } else {
                        return;
                    }
                } else if (currentModuleIndex == 179) {
                    if (topAlert != null) {
                        topAlert.showConfigMenu();
                    }
                    LiveVVProcess liveVVProcess = (LiveVVProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(230);
                    if (liveVVProcess != null) {
                        liveVVProcess.processingFinish();
                        return;
                    }
                    return;
                } else if (currentModuleIndex == 180) {
                    if (bottomPopupTips != null) {
                        bottomPopupTips.reInitTipImage();
                    }
                    if (actionProcessing != null) {
                        actionProcessing.processingFinish();
                    }
                    if (topAlert != null) {
                        topAlert.showConfigMenu();
                        topAlert.setRecordingTimeState(2);
                    }
                    ManuallyAdjust manuallyAdjust = (ManuallyAdjust) ModeCoordinatorImpl.getInstance().getAttachProtocol(181);
                    if (manuallyAdjust != null) {
                        manuallyAdjust.onRecordingStop();
                    }
                    ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
                    if (configChanges != null) {
                        configChanges.reCheckParameterResetTip(true);
                        configChanges.reCheckParameterDescriptionTip();
                        return;
                    }
                    return;
                } else if (currentModuleIndex == 207) {
                    if (bottomPopupTips != null) {
                        bottomPopupTips.reInitTipImage();
                    }
                    if (actionProcessing != null) {
                        actionProcessing.processingFinish();
                    }
                    if (topAlert != null) {
                        topAlert.setConfigMenuResetWhenRestartmode();
                    } else {
                        return;
                    }
                } else if (currentModuleIndex != 208) {
                    if (bottomPopupTips != null) {
                        bottomPopupTips.reInitTipImage();
                    }
                    if (actionProcessing != null) {
                        actionProcessing.processingFinish();
                    }
                    if (topAlert != null) {
                        topAlert.showConfigMenu();
                        if (topAlert.isExtraMenuShowing()) {
                            topAlert.hideExtraMenu();
                        }
                        topAlert.setRecordingTimeState(2);
                    }
                    if (configChangeImpl != null) {
                        configChangeImpl.reCheckMacroMode();
                        configChangeImpl.recheckVideoFps(false);
                        configChangeImpl.reCheckAiEnhancedVideo();
                        return;
                    }
                    return;
                }
                topAlert.setRecordingTimeState(2);
                return;
            } else {
                AmbilightSelector ambilightSelector = (AmbilightSelector) ModeCoordinatorImpl.getInstance().getAttachProtocol(421);
                if (ambilightSelector != null) {
                    ambilightSelector.onRecordingStop();
                    return;
                }
                return;
            }
            topAlert.enableMenuItem(true, iArr);
            topAlert.alertMusicClose(true);
            return;
        }
        if (bottomPopupTips != null) {
            bottomPopupTips.reInitTipImage();
        }
        if (actionProcessing != null) {
            if (getCurrentModuleIndex() == 208) {
                actionProcessing.enableStopButton(true, true);
            }
            actionProcessing.processingFinish();
        }
        if (topAlert != null) {
            topAlert.showConfigMenu();
            topAlert.setRecordingTimeState(2);
        }
        ConfigChanges configChanges2 = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
        if (configChanges2 != null) {
            configChanges2.reCheckParameterDescriptionTip();
        }
        if (configChangeImpl != null) {
            configChangeImpl.reCheckMacroMode();
        }
    }

    public void onLongExposePrepare() {
        ((ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162)).processingLongExposePrepare();
    }

    public void onLongExposeStart() {
        ((ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162)).processingLongExposeStart();
    }

    public void onMimojiCreateBack() {
        ((ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162)).processingMimojiBack();
        if (getCurrentModuleIndex() == 184) {
            MimojiAvatarEngine2 mimojiAvatarEngine2 = (MimojiAvatarEngine2) ModeCoordinatorImpl.getInstance().getAttachProtocol(246);
            if (mimojiAvatarEngine2 != null) {
                mimojiAvatarEngine2.backToPreview(false, false);
            }
        } else {
            MimojiAvatarEngine mimojiAvatarEngine = (MimojiAvatarEngine) ModeCoordinatorImpl.getInstance().getAttachProtocol(217);
            if (mimojiAvatarEngine != null) {
                mimojiAvatarEngine.backToPreview(false, true);
            }
        }
        ((MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166)).mimojiEnd();
    }

    public void onPause() {
        int[] iArr;
        Log.d(TAG, "onPause");
        ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        updateRightTips(topAlert, 0);
        int currentModuleIndex = getCurrentModuleIndex();
        if (currentModuleIndex == 174) {
            actionProcessing.processingPause();
            topAlert.setRecordingTimeState(3);
            bottomPopupTips.reInitTipImage();
            iArr = new int[]{225, 245};
        } else if (currentModuleIndex == 179) {
            topAlert.showConfigMenu();
            ((LiveVVProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(230)).processingPause();
            return;
        } else if (currentModuleIndex == 183) {
            actionProcessing.processingPause();
            topAlert.setRecordingTimeState(3);
            bottomPopupTips.reInitTipImage();
            iArr = new int[]{197, 245};
        } else if (currentModuleIndex == 204) {
            actionProcessing.processingPause();
            topAlert.setRecordingTimeState(3);
            topAlert.showConfigMenu();
        } else if (currentModuleIndex != 212) {
            actionProcessing.processingPause();
            topAlert.setRecordingTimeState(3);
            return;
        } else {
            topAlert.setRecordingTimeState(3);
            FilmDreamProcess filmDreamProcess = (FilmDreamProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(931);
            if (filmDreamProcess != null) {
                filmDreamProcess.processingPause();
                return;
            }
            return;
        }
        topAlert.disableMenuItem(true, iArr);
        topAlert.showConfigMenu();
    }

    public void onPostPreview() {
        Log.d(TAG, "onPostPreview");
        ((BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171)).handleBackStackFromShutter();
        ((TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172)).setRecordingTimeState(2);
        ((ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162)).processingWorkspace(false);
    }

    public void onPostPreviewBack() {
        Log.d(TAG, "onPostPreviewBack");
        ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
        int currentModuleIndex = getCurrentModuleIndex();
        if (currentModuleIndex == 177 || currentModuleIndex == 184) {
            actionProcessing.processingFinish();
        } else {
            actionProcessing.processingWorkspace(true);
        }
        ((BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175)).reInitTipImage();
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        int[] iArr = new int[2];
        int i = (getCurrentModuleIndex() == 177 || getCurrentModuleIndex() == 184 || getCurrentModuleIndex() == 183) ? 197 : 225;
        iArr[0] = i;
        iArr[1] = 245;
        topAlert.enableMenuItem(true, iArr);
        topAlert.showConfigMenu();
    }

    public void onPostSavingFinish() {
        String str = TAG;
        Log.d(str, "onPostSavingFinish");
        ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
        int currentModuleIndex = getCurrentModuleIndex();
        if (currentModuleIndex == 166) {
            PanoramaProtocol panoramaProtocol = (PanoramaProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(176);
            if (panoramaProtocol != null) {
                Log.d(str, "onPostExecute setDisplayPreviewBitmap null");
                panoramaProtocol.setDisplayPreviewBitmap(null);
                panoramaProtocol.showSmallPreview(false);
            }
        } else if (currentModuleIndex == 172) {
            if (actionProcessing != null) {
                actionProcessing.processingFinish();
            }
            ConfigChangeImpl configChangeImpl = (ConfigChangeImpl) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
            if (configChangeImpl != null) {
                configChangeImpl.recheckVideoFps(false);
            }
        } else if (currentModuleIndex == 176) {
            WideSelfieProtocol wideSelfieProtocol = (WideSelfieProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(216);
            if (wideSelfieProtocol != null) {
                wideSelfieProtocol.updatePreviewBitmap(null, null, null);
            }
        } else if (actionProcessing != null) {
            actionProcessing.processingFinish();
        }
    }

    public void onPostSavingStart() {
        Log.d(TAG, "onPostSaving");
        ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (getCurrentModuleIndex() == 208) {
            topAlert.setConfigMenuResetWhenRestartmode();
        } else {
            topAlert.showConfigMenu();
        }
        ((BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175)).reInitTipImage();
        int currentModuleIndex = getCurrentModuleIndex();
        if (currentModuleIndex == 166) {
            actionProcessing.processingFinish();
            actionProcessing.updateLoading(false);
            if (C0122O00000o.instance().OOOO0OO()) {
                DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
                if (dualController != null) {
                    dualController.showZoomButton();
                    if (topAlert != null) {
                        topAlert.clearAlertStatus();
                    }
                }
            }
            ((PanoramaProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(176)).resetShootUI();
        } else if (currentModuleIndex == 173) {
            actionProcessing.processingPostAction();
            DualController dualController2 = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
            if (dualController2 != null) {
                dualController2.showZoomButton();
            }
            if (topAlert != null) {
                topAlert.clearAlertStatus();
                topAlert.alertAiDetectTipHint(8, R.string.super_night_toast, -1);
            }
            BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
            if (bottomPopupTips != null) {
                bottomPopupTips.updateTipImage();
            }
        } else if (currentModuleIndex == 184) {
        } else {
            if (currentModuleIndex == 187) {
                actionProcessing.processingPostAction();
                if (topAlert != null) {
                    topAlert.clearAlertStatus();
                    topAlert.alertAiDetectTipHint(8, R.string.super_night_toast, -1);
                }
            } else if (currentModuleIndex == 176) {
                actionProcessing.processingFinish();
                actionProcessing.updateLoading(false);
                ((WideSelfieProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(216)).resetShootUI();
            } else if (currentModuleIndex != 177) {
                topAlert.setRecordingTimeState(2);
                actionProcessing.processingPostAction();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00f9, code lost:
        if (r0 != null) goto L_0x0110;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x010e, code lost:
        if (r0 != null) goto L_0x0110;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x018e, code lost:
        if (r0 != null) goto L_0x0110;
     */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x01a0  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onPrepare() {
        BottomPopupTips bottomPopupTips;
        Log.d(TAG, "onPrepare: ");
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert != null) {
            if (topAlert.isExtraMenuShowing()) {
                topAlert.hideExtraMenu();
            }
            topAlert.hideConfigMenu();
        }
        int currentModuleIndex = getCurrentModuleIndex();
        if (currentModuleIndex == 179) {
            ((LiveVVProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(230)).processingPrepare();
        } else if (currentModuleIndex == 187) {
            ((ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162)).processingPrepare();
        } else if (currentModuleIndex != 212) {
            ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
            actionProcessing.processingPrepare();
            BackStack backStack = (BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171);
            if (DataRepository.dataItemLive().getTimerBurstController().isInTimerBurstShotting()) {
                backStack.handleBackStackFromTimerBurstShutter();
            } else {
                backStack.handleBackStackFromShutter();
            }
            if (actionProcessing.isShowFilterView()) {
                actionProcessing.showOrHideFilterView();
            }
            DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
            if (dualController != null) {
                dualController.hideZoomButton();
            }
            if (CameraSettings.isAlgoFPS(getCurrentModuleIndex())) {
                CameraClickObservable cameraClickObservable = (CameraClickObservable) ModeCoordinatorImpl.getInstance().getAttachProtocol(227);
                if (cameraClickObservable != null) {
                    cameraClickObservable.subscribe(171);
                }
            }
        } else {
            ((FilmDreamProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(931)).processingPrepare();
        }
        int currentModuleIndex2 = getCurrentModuleIndex();
        if (currentModuleIndex2 != 163) {
            if (currentModuleIndex2 == 166) {
                ((PanoramaProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(176)).setShootUI();
                if (C0122O00000o.instance().OOOO0OO()) {
                    DualController dualController2 = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
                    if (dualController2 != null) {
                        dualController2.hideZoomButton();
                        if (topAlert != null) {
                            topAlert.alertUpdateValue(0, 0, null);
                        }
                    }
                }
            } else if (currentModuleIndex2 == 176) {
                MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
                if (mainContentProtocol != null) {
                    mainContentProtocol.clearIndicator(1);
                }
                WideSelfieProtocol wideSelfieProtocol = (WideSelfieProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(216);
                if (wideSelfieProtocol != null) {
                    wideSelfieProtocol.setShootingUI();
                }
            } else if (currentModuleIndex2 == 187) {
                AmbilightSelector ambilightSelector = (AmbilightSelector) ModeCoordinatorImpl.getInstance().getAttachProtocol(421);
                if (ambilightSelector != null) {
                    ambilightSelector.onRecordingPrepare();
                }
            } else if (currentModuleIndex2 != 212) {
                if (currentModuleIndex2 != 173) {
                    if (currentModuleIndex2 != 174) {
                        if (currentModuleIndex2 != 179) {
                            if (currentModuleIndex2 == 180) {
                                ManuallyAdjust manuallyAdjust = (ManuallyAdjust) ModeCoordinatorImpl.getInstance().getAttachProtocol(181);
                                if (manuallyAdjust != null) {
                                    manuallyAdjust.onRecordingPrepare();
                                }
                            } else if (!(currentModuleIndex2 == 183 || currentModuleIndex2 == 184)) {
                                if (!CameraSettings.isAlgoFPS(getCurrentModuleIndex())) {
                                }
                            }
                        }
                    }
                }
            }
            bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
            if (bottomPopupTips != null) {
                bottomPopupTips.hideTipImage();
                bottomPopupTips.hideLeftTipImage();
                bottomPopupTips.hideRightTipImage();
                bottomPopupTips.hideCenterTipImage();
                bottomPopupTips.directHideLeftImageIntro();
            }
            if (HybridZoomingSystem.IS_3_OR_MORE_SAT && CameraSettings.getRetainZoom(currentModuleIndex) < 1.0f) {
                bottomPopupTips.directlyHideTips();
                return;
            }
        }
        MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
        if (miBeautyProtocol != null && miBeautyProtocol.isBeautyPanelShow()) {
            miBeautyProtocol.dismiss(3);
        }
        topAlert.setRecordingTimeState(1);
        bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        if (bottomPopupTips != null) {
        }
        if (HybridZoomingSystem.IS_3_OR_MORE_SAT) {
        }
    }

    public void onResume() {
        Log.d(TAG, "onResume");
        ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        updateRightTips(topAlert, 8);
        int currentModuleIndex = getCurrentModuleIndex();
        if (currentModuleIndex != 174) {
            if (currentModuleIndex == 179) {
                topAlert.hideConfigMenu();
                ((LiveVVProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(230)).processingResume();
                return;
            } else if (currentModuleIndex != 183) {
                if (currentModuleIndex != 187) {
                    actionProcessing.processingResume();
                    topAlert.setRecordingTimeState(4);
                    if (currentModuleIndex == 204) {
                        topAlert.hideConfigMenu();
                        return;
                    }
                    return;
                }
                actionProcessing.enableStopButton(true, true);
                return;
            }
        }
        actionProcessing.processingResume();
        topAlert.setRecordingTimeState(4);
        topAlert.hideConfigMenu();
        BackStack backStack = (BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171);
        if (backStack != null) {
            backStack.handleBackStackFromShutter();
        }
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        bottomPopupTips.hideTipImage();
        bottomPopupTips.hideLeftTipImage();
        bottomPopupTips.hideRightTipImage();
        bottomPopupTips.hideCenterTipImage();
        bottomPopupTips.directHideLeftImageIntro();
    }

    public void onStart() {
        Log.d(TAG, "onStart");
        ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
        MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        updateRightTips(topAlert, 8);
        int currentModuleIndex = getCurrentModuleIndex();
        if (currentModuleIndex == 163) {
            actionProcessing.processingStart();
            BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
            if (bottomPopupTips != null) {
                bottomPopupTips.hideNearRangeTip();
            }
        } else if (currentModuleIndex != 172) {
            if (currentModuleIndex != 174) {
                if (currentModuleIndex == 176) {
                    actionProcessing.processingStart();
                    WideSelfieProtocol wideSelfieProtocol = (WideSelfieProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(216);
                    if (wideSelfieProtocol != null) {
                        wideSelfieProtocol.updateHintText(R.string.wideselfie_rotate_slowly);
                        return;
                    }
                    return;
                } else if (currentModuleIndex == 179) {
                    ((LiveVVProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(230)).processingStart();
                    return;
                } else if (currentModuleIndex != 183) {
                    if (currentModuleIndex != 185) {
                        if (currentModuleIndex != 187) {
                            if (currentModuleIndex == 208) {
                                actionProcessing.processingStart();
                                if (mainContentProtocol == null) {
                                    return;
                                }
                            } else if (currentModuleIndex == 212) {
                                ((FilmDreamProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(931)).processingStart();
                                if (mainContentProtocol == null) {
                                    return;
                                }
                            }
                            mainContentProtocol.clearFocusView(7);
                            return;
                        } else if (((AmbilightProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(420)) == null) {
                            return;
                        }
                        actionProcessing.processingStart();
                        return;
                    }
                    CloneProcess cloneProcess = (CloneProcess) ModeCoordinatorImpl.getInstance().getAttachProtocol(418);
                    if (cloneProcess != null) {
                        cloneProcess.processingStart();
                        return;
                    }
                    return;
                }
            }
            actionProcessing.processingStart();
            topAlert.alertMusicClose(false);
        } else {
            actionProcessing.processingStart();
            ComponentConfigSlowMotion componentConfigSlowMotion = DataRepository.dataItemConfig().getComponentConfigSlowMotion();
            if (topAlert != null) {
                topAlert.alertVideoUltraClear(8, componentConfigSlowMotion.getValueSelectedStringIdIgnoreClose(172));
            }
        }
    }

    public void prepareCreateMimoji() {
        ((BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171)).handleBackStackFromShutter();
        ((ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162)).processingMimojiPrepare();
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        topAlert.disableMenuItem(true, 197, 193, 162);
        topAlert.alertFlash(8, "1", false);
        BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
        bottomPopupTips.hideTipImage();
        bottomPopupTips.hideLeftTipImage();
        bottomPopupTips.hideRightTipImage();
        bottomPopupTips.directHideCenterTipImage();
        bottomPopupTips.directHideLeftImageIntro();
        bottomPopupTips.directlyHideTips();
    }

    public void registerProtocol() {
        ModeCoordinatorImpl.getInstance().attachProtocol(212, this);
    }

    public void unRegisterProtocol() {
        this.mActivity = null;
        ModeCoordinatorImpl.getInstance().detachProtocol(212, this);
    }

    /* access modifiers changed from: protected */
    public void updateZoomRatioToggleButtonState(boolean z) {
        DualController dualController = (DualController) ModeCoordinatorImpl.getInstance().getAttachProtocol(182);
        if (dualController != null) {
            dualController.setRecordingOrPausing(z);
            if (z) {
                dualController.hideZoomButton();
            } else {
                dualController.showZoomButton();
            }
        }
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert == null) {
            return;
        }
        if (z) {
            topAlert.alertUpdateValue(0, 0, null);
        } else {
            topAlert.clearAlertStatus();
        }
    }
}
