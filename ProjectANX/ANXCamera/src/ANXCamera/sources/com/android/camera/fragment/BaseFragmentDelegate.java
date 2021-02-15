package com.android.camera.fragment;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.util.SparseArray;
import android.util.SparseIntArray;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.android.camera.Camera;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.R;
import com.android.camera.animation.AnimationComposite;
import com.android.camera.data.DataRepository;
import com.android.camera.features.gif.FragmentGifEdit;
import com.android.camera.features.mimoji2.fragment.FragmentMimojiFullScreen;
import com.android.camera.features.mimoji2.fragment.bottomlist.FragmentMimojiBottomList;
import com.android.camera.features.mimoji2.fragment.edit.FragmentMimojiEmoticon;
import com.android.camera.fragment.aiwatermark.FragmentWatermark;
import com.android.camera.fragment.ambilight.FragmentAmbilight;
import com.android.camera.fragment.beauty.FragmentBlankBeauty;
import com.android.camera.fragment.beauty.FragmentBlankDefault;
import com.android.camera.fragment.beauty.FragmentPopuEyeLightTip;
import com.android.camera.fragment.beauty.FragmentPopupBeauty;
import com.android.camera.fragment.beauty.FragmentPopupBeautyLevel;
import com.android.camera.fragment.bottom.action.FragmentBottomAction;
import com.android.camera.fragment.bottom.action.FragmentLighting;
import com.android.camera.fragment.clone.FragmentCloneGallery;
import com.android.camera.fragment.clone.FragmentCloneProcess;
import com.android.camera.fragment.clone.FragmentCloneUseGuide;
import com.android.camera.fragment.clone.FragmentSlowShutterUseGuide;
import com.android.camera.fragment.clone.FragmentTimeFreezeProcess;
import com.android.camera.fragment.clone.FragmentTimeFreezeUseGuide;
import com.android.camera.fragment.dialog.FragmentLiveReview;
import com.android.camera.fragment.dollyZoom.FragmentDollyZoomProcess;
import com.android.camera.fragment.dollyZoom.FragmentDollyZoomUseGuide;
import com.android.camera.fragment.dual.FragmentDualCameraAdjust;
import com.android.camera.fragment.dual.FragmentDualStereo;
import com.android.camera.fragment.fastmotion.FragmentFastMotion;
import com.android.camera.fragment.fastmotion.FragmentFastmotionPro;
import com.android.camera.fragment.film.FragmentFilmDreamProcess;
import com.android.camera.fragment.film.FragmentFilmGallery;
import com.android.camera.fragment.fullscreen.FragmentFullScreen;
import com.android.camera.fragment.idcard.FragmentIDCard;
import com.android.camera.fragment.lifeCircle.BaseFragmentLifecycle;
import com.android.camera.fragment.lifeCircle.BaseLifeCircleBindFragment;
import com.android.camera.fragment.lifeCircle.BaseLifecycleListener;
import com.android.camera.fragment.live.FragmentLiveSpeed;
import com.android.camera.fragment.live.FragmentLiveSticker;
import com.android.camera.fragment.manually.FragmentManually;
import com.android.camera.fragment.mimoji.FragmentMimoji;
import com.android.camera.fragment.mode.FragmentMoreModeEdit;
import com.android.camera.fragment.mode.FragmentMoreModeNormal;
import com.android.camera.fragment.mode.FragmentMoreModePopup;
import com.android.camera.fragment.sticker.FragmentSticker;
import com.android.camera.fragment.subtitle.FragmentSubtitle;
import com.android.camera.fragment.top.FragmentTopConfig;
import com.android.camera.fragment.vv.FragmentVV;
import com.android.camera.fragment.vv.FragmentVVFeature;
import com.android.camera.fragment.vv.FragmentVVGallery;
import com.android.camera.fragment.vv.FragmentVVProcess;
import com.android.camera.log.Log;
import com.android.camera.module.loader.StartControl;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BaseDelegate;
import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BaseFragmentDelegate implements BaseDelegate {
    public static final int BEAUTY_FRAGMENT_CONTAINER_ID = 2131296378;
    public static final int FRAGMENT_AMBILIGHT = 16777200;
    public static final int FRAGMENT_ASD_WATERMARK = 1048572;
    public static final int FRAGMENT_BASE_WATERMARK = 1048566;
    public static final int FRAGMENT_BEAUTY = 251;
    public static final int FRAGMENT_BLANK_BEAUTY = 4090;
    public static final int FRAGMENT_BLANK_DEFAULT = 14;
    public static final int FRAGMENT_BOTTOM_ACTION = 241;
    public static final int FRAGMENT_BOTTOM_INTENT_DONE = 4083;
    public static final int FRAGMENT_BOTTOM_POPUP_TIPS = 65529;
    public static final int FRAGMENT_CITY_WATERMARK = 1048573;
    public static final int FRAGMENT_CLONE_GALLERY = 1048564;
    public static final int FRAGMENT_CLONE_PROCESS = 1048563;
    public static final int FRAGMENT_CLONE_USE_GUIDE = 1048565;
    public static final int FRAGMENT_CUSTOM_TINT = 65528;
    public static final int FRAGMENT_DISPATCH = 13;
    public static final int FRAGMENT_DOLLY_ZOOM_PROCESS = 16777211;
    public static final int FRAGMENT_DOLLY_ZOOM_USE_GUIDE = 16777212;
    public static final int FRAGMENT_DUAL_CAMERA_ADJUST = 4084;
    public static final int FRAGMENT_DUAL_CAMERA_BOKEH_ADJUST = 4091;
    public static final int FRAGMENT_DUAL_STEREO = 4085;
    public static final int FRAGMENT_EYE_LIGHT_POPU_TIP = 4089;
    public static final int FRAGMENT_FAST_MOTION = 16777201;
    public static final int FRAGMENT_FAST_MOTION_EXTRA = 16777202;
    public static final int FRAGMENT_FAST_MOTION_PRO = 16777206;
    public static final int FRAGMENT_FESTIVAL_WATERMARK = 1048571;
    public static final int FRAGMENT_FILM_DREAM_PROCESS = 16777205;
    public static final int FRAGMENT_FILM_GALLERY = 16777203;
    public static final int FRAGMENT_FILM_PREVIEW = 16777204;
    public static final int FRAGMENT_FILM_TIME_FREEZE = 16777209;
    public static final int FRAGMENT_FILTER = 250;
    public static final int FRAGMENT_GENERAL_WATERMARK = 1048567;
    public static final int FRAGMENT_GIF = 65524;
    public static final int FRAGMENT_ID_CARD = 1048560;
    public static final int FRAGMENT_INVALID = 240;
    public static final int FRAGMENT_LIGHTING = 4087;
    public static final int FRAGMENT_LIVE_REVIEW = 1048561;
    public static final int FRAGMENT_MAIN_CONTENT = 243;
    public static final int FRAGMENT_MANUALLY_EXTRA = 254;
    public static final int FRAGMENT_MASTER_FILTER = 16777207;
    public static final int FRAGMENT_MIMOJI_EDIT = 65521;
    public static final int FRAGMENT_MIMOJI_EMOTICON = 65522;
    public static final int FRAGMENT_MIMOJI_LIST = 65520;
    public static final int FRAGMENT_MIMOJI_SCREEN = 65523;
    public static final int FRAGMENT_MODES_EDIT = 65527;
    public static final int FRAGMENT_MODES_MORE_NORMAL = 65525;
    public static final int FRAGMENT_MODES_MORE_POPUP = 65526;
    public static final int FRAGMENT_MODE_SELECT = 242;
    public static final int FRAGMENT_PANORAMA = 4080;
    public static final int FRAGMENT_POPUP_BEAUTY = 249;
    public static final int FRAGMENT_POPUP_BEAUTYLEVEL = 4082;
    public static final int FRAGMENT_POPUP_LIVE_SPEED = 4093;
    public static final int FRAGMENT_POPUP_LIVE_STICKER = 4092;
    public static final int FRAGMENT_POPUP_MAKEUP = 252;
    public static final int FRAGMENT_POPUP_MANUALLY = 247;
    public static final int FRAGMENT_SCREEN_LIGHT = 4086;
    public static final int FRAGMENT_SILHOUETTE_WATERMARK = 1048569;
    public static final int FRAGMENT_SLOW_SHUTTER_USE_GUIDE = 16777213;
    public static final int FRAGMENT_SPOTS_WATERMARK = 1048568;
    public static final int FRAGMENT_STICKER = 255;
    public static final int FRAGMENT_SUBTITLE = 65534;
    public static final int FRAGMENT_TEXT_WATERMARK = 1048570;
    public static final int FRAGMENT_TIME_FREEZE_USE_GUIDE = 16777210;
    public static final int FRAGMENT_TOP_ALERT = 253;
    public static final int FRAGMENT_TOP_CONFIG = 244;
    public static final int FRAGMENT_VERTICAL = 4088;
    public static final int FRAGMENT_VV = 65530;
    public static final int FRAGMENT_VV_FEATURE = 16777208;
    public static final int FRAGMENT_VV_GALLERY = 65531;
    public static final int FRAGMENT_VV_PREVIEW = 65532;
    public static final int FRAGMENT_VV_PROCESS = 65533;
    public static final int FRAGMENT_WATERMARK = 1048574;
    public static final int FRAGMENT_WIDESELFIE = 4094;
    public static final int MODULE_CONTENT = 1048562;
    private static final String TAG = "BaseFragmentDelegate";
    private AnimationComposite animationComposite;
    private SparseArray currentFragments;
    private Camera mActivity;
    private BaseLifecycleListener mBaseLifecycleListener = null;
    private List mLastFragmentAlias;
    private SparseIntArray mStoreFragments;
    private FragmentManager mSupportFragmentManager = null;
    private SparseIntArray originalFragments;

    @Retention(RetentionPolicy.SOURCE)
    public @interface FragmentInto {
    }

    public BaseFragmentDelegate(Camera camera) {
        this.mActivity = camera;
        this.animationComposite = new AnimationComposite();
        this.originalFragments = new SparseIntArray();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x006c, code lost:
        r4.setLastFragmentInfo(r3);
        r4.pendingShow();
        r1.show(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0124, code lost:
        updateCurrentFragments(r2.containerViewId, r3, r2.operateType);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0163, code lost:
        r11.animationComposite.put(r3.getFragmentInto(), r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x016c, code lost:
        updateCurrentFragments(r2.containerViewId, r10, r2.operateType);
     */
    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.android.camera.fragment.BaseFragmentOperation>, for r12v0, types: [java.util.List, java.util.List<com.android.camera.fragment.BaseFragmentOperation>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void applyUpdateSet(List<BaseFragmentOperation> list, BaseLifecycleListener baseLifecycleListener) {
        BaseFragment baseFragment;
        BaseFragment baseFragment2;
        if (list == null || list.isEmpty()) {
            throw new RuntimeException("need operation info");
        }
        Camera camera = this.mActivity;
        if (camera == null || !camera.isFinishing()) {
            FragmentManager supportFragmentManager = this.mActivity.getSupportFragmentManager();
            FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
            for (BaseFragmentOperation baseFragmentOperation : list) {
                int activeFragment = getActiveFragment(baseFragmentOperation.containerViewId);
                int i = baseFragmentOperation.pendingFragmentInfo;
                switch (baseFragmentOperation.operateType) {
                    case 1:
                        int activeFragment2 = getActiveFragment(baseFragmentOperation.containerViewId);
                        this.animationComposite.remove(activeFragment2);
                        baseFragment = constructFragment(false, i, baseFragmentOperation.pendingFragmentAlias, activeFragment2, baseLifecycleListener);
                        beginTransaction.replace(baseFragmentOperation.containerViewId, baseFragment, baseFragment.getFragmentTag());
                        break;
                    case 2:
                        this.animationComposite.remove(i);
                        BaseFragment baseFragment3 = (BaseFragment) supportFragmentManager.findFragmentByTag(String.valueOf(i));
                        if (baseFragment3 != null) {
                            baseFragment3.pendingGone(false);
                            beginTransaction.remove(baseFragment3);
                            break;
                        }
                        break;
                    case 3:
                        this.animationComposite.remove(activeFragment);
                        BaseFragment baseFragment4 = (BaseFragment) supportFragmentManager.findFragmentByTag(String.valueOf(activeFragment));
                        if (baseFragment4 != null) {
                            baseFragment4.pendingGone(false);
                            beginTransaction.remove(baseFragment4);
                            break;
                        }
                        break;
                    case 4:
                        BaseFragment baseFragment5 = (BaseFragment) supportFragmentManager.findFragmentByTag(String.valueOf(activeFragment));
                        if (baseFragment5 != null) {
                            baseFragment5.pendingGone(false);
                            baseFragment5.setNewFragmentInfo(i);
                            beginTransaction.hide(baseFragment5);
                        }
                        BaseFragment baseFragment6 = (BaseFragment) supportFragmentManager.findFragmentByTag(String.valueOf(i));
                        if (baseFragment6 != null) {
                            beginTransaction.remove(baseFragment6);
                        }
                        baseFragment = constructFragment(false, i, baseFragmentOperation.pendingFragmentAlias, activeFragment, baseLifecycleListener);
                        beginTransaction.add(baseFragmentOperation.containerViewId, baseFragment, baseFragment.getFragmentTag());
                        break;
                    case 5:
                        List list2 = (List) this.currentFragments.get(baseFragmentOperation.containerViewId);
                        for (int i2 = 0; i2 < list2.size(); i2++) {
                            int intValue = ((Integer) list2.get(i2)).intValue();
                            if (intValue != i) {
                                this.animationComposite.remove(intValue);
                                BaseFragment baseFragment7 = (BaseFragment) supportFragmentManager.findFragmentByTag(String.valueOf(intValue));
                                if (baseFragment7 != null) {
                                    if (intValue != activeFragment) {
                                        baseFragment7.pendingGone(true);
                                    } else {
                                        baseFragment7.pendingGone(false);
                                    }
                                    beginTransaction.remove(baseFragment7);
                                }
                            }
                        }
                        baseFragment2 = (BaseFragment) supportFragmentManager.findFragmentByTag(String.valueOf(i));
                        if (baseFragment2 == null) {
                            Log.d(TAG, "popup null, create new");
                            baseFragment2 = constructFragment(false, i, baseFragmentOperation.pendingFragmentAlias, activeFragment, baseLifecycleListener);
                            break;
                        }
                        break;
                    case 6:
                        if (activeFragment != i) {
                            BaseFragment baseFragment8 = (BaseFragment) supportFragmentManager.findFragmentByTag(String.valueOf(activeFragment));
                            if (baseFragment8 != null) {
                                baseFragment8.pendingGone(true);
                                beginTransaction.hide(baseFragment8);
                            }
                        }
                        baseFragment2 = (BaseFragment) supportFragmentManager.findFragmentByTag(String.valueOf(i));
                        break;
                    case 7:
                        BaseFragment baseFragment9 = (BaseFragment) supportFragmentManager.findFragmentByTag(String.valueOf(activeFragment));
                        if (baseFragment9 != null) {
                            beginTransaction.hide(baseFragment9);
                            break;
                        }
                        break;
                }
            }
            beginTransaction.commitAllowingStateLoss();
        }
    }

    @Deprecated
    public static void bindLifeCircle(Fragment fragment) {
        FragmentManager childFragmentManager = fragment.getChildFragmentManager();
        BaseLifeCircleBindFragment baseLifeCircleBindFragment = new BaseLifeCircleBindFragment();
        BaseFragmentLifecycle fragmentLifecycle = baseLifeCircleBindFragment.getFragmentLifecycle();
        AnonymousClass1 r2 = new BaseLifecycleListener() {
            public void onLifeAlive() {
            }

            public void onLifeDestroy(String str) {
            }

            public void onLifeStart(String str) {
            }

            public void onLifeStop(String str) {
            }

            public void setBlockingLifeCycles(List list) {
            }
        };
        String str = BaseLifeCircleBindFragment.FRAGMENT_TAG;
        fragmentLifecycle.addListener(r2, str);
        childFragmentManager.beginTransaction().add((Fragment) baseLifeCircleBindFragment, str).commitAllowingStateLoss();
    }

    private BaseFragment constructFragment(boolean z, int i, String str, int i2, BaseLifecycleListener baseLifecycleListener) {
        BaseFragment baseFragment = null;
        if (i == 13) {
            baseFragment = new DispatchFragment();
        } else if (i == 14) {
            baseFragment = new FragmentBlankDefault();
        } else if (i == 240) {
            return null;
        } else {
            if (i == 241) {
                baseFragment = new FragmentBottomAction();
            } else if (i == 243) {
                baseFragment = new FragmentMainContent();
            } else if (i != 244) {
                switch (i) {
                    case 247:
                        baseFragment = new FragmentManually();
                        break;
                    case 249:
                        baseFragment = new FragmentPopupBeauty();
                        break;
                    case 251:
                        baseFragment = new FragmentBeauty();
                        break;
                    case 255:
                        baseFragment = new FragmentSticker();
                        break;
                    case 4080:
                        baseFragment = new FragmentPanorama();
                        break;
                    case 4082:
                        baseFragment = new FragmentPopupBeautyLevel();
                        break;
                    case 4083:
                        baseFragment = new FragmentBottomIntentDone();
                        break;
                    case 4084:
                        baseFragment = new FragmentDualCameraAdjust();
                        break;
                    case 4085:
                        baseFragment = new FragmentDualStereo();
                        break;
                    case 4086:
                        baseFragment = new FragmentFullScreen();
                        break;
                    case FRAGMENT_LIGHTING /*4087*/:
                        baseFragment = new FragmentLighting();
                        break;
                    case FRAGMENT_VERTICAL /*4088*/:
                        baseFragment = new FragmentVertical();
                        break;
                    case 4089:
                        baseFragment = new FragmentPopuEyeLightTip();
                        break;
                    case 4090:
                        baseFragment = new FragmentBlankBeauty();
                        break;
                    case FRAGMENT_MIMOJI_LIST /*65520*/:
                        baseFragment = this.mActivity.getCurrentModuleIndex() == 184 ? new FragmentMimojiBottomList() : new FragmentMimoji();
                        break;
                    case 1048574:
                        baseFragment = new FragmentWatermark();
                        break;
                    case FRAGMENT_FILM_GALLERY /*16777203*/:
                        baseFragment = new FragmentFilmGallery();
                        break;
                    case FRAGMENT_FILM_DREAM_PROCESS /*16777205*/:
                        baseFragment = new FragmentFilmDreamProcess();
                        break;
                    case FRAGMENT_FAST_MOTION_PRO /*16777206*/:
                        baseFragment = new FragmentFastmotionPro();
                        break;
                    case 16777207:
                        baseFragment = new FragmentMasterFilter();
                        break;
                    case FRAGMENT_VV_FEATURE /*16777208*/:
                        baseFragment = new FragmentVVFeature();
                        break;
                    case FRAGMENT_FILM_TIME_FREEZE /*16777209*/:
                        baseFragment = new FragmentTimeFreezeProcess();
                        break;
                    case FRAGMENT_TIME_FREEZE_USE_GUIDE /*16777210*/:
                        baseFragment = new FragmentTimeFreezeUseGuide();
                        break;
                    case FRAGMENT_DOLLY_ZOOM_PROCESS /*16777211*/:
                        baseFragment = new FragmentDollyZoomProcess();
                        break;
                    case FRAGMENT_DOLLY_ZOOM_USE_GUIDE /*16777212*/:
                        baseFragment = new FragmentDollyZoomUseGuide();
                        break;
                    case FRAGMENT_SLOW_SHUTTER_USE_GUIDE /*16777213*/:
                        baseFragment = new FragmentSlowShutterUseGuide();
                        break;
                    default:
                        switch (i) {
                            case FRAGMENT_POPUP_LIVE_STICKER /*4092*/:
                                baseFragment = new FragmentLiveSticker();
                                break;
                            case FRAGMENT_POPUP_LIVE_SPEED /*4093*/:
                                baseFragment = new FragmentLiveSpeed();
                                break;
                            case 4094:
                                baseFragment = new FragmentWideSelfie();
                                break;
                            default:
                                switch (i) {
                                    case 65522:
                                        baseFragment = new FragmentMimojiEmoticon();
                                        break;
                                    case 65523:
                                        baseFragment = new FragmentMimojiFullScreen();
                                        break;
                                    case 65524:
                                        baseFragment = new FragmentGifEdit();
                                        break;
                                    case FRAGMENT_MODES_MORE_NORMAL /*65525*/:
                                        baseFragment = new FragmentMoreModeNormal();
                                        break;
                                    case FRAGMENT_MODES_MORE_POPUP /*65526*/:
                                        baseFragment = new FragmentMoreModePopup();
                                        break;
                                    case FRAGMENT_MODES_EDIT /*65527*/:
                                        baseFragment = new FragmentMoreModeEdit();
                                        break;
                                    default:
                                        switch (i) {
                                            case 65529:
                                                baseFragment = new FragmentBottomPopupTips();
                                                break;
                                            case FRAGMENT_VV /*65530*/:
                                                baseFragment = new FragmentVV();
                                                break;
                                            case FRAGMENT_VV_GALLERY /*65531*/:
                                                baseFragment = new FragmentVVGallery();
                                                break;
                                            default:
                                                switch (i) {
                                                    case FRAGMENT_VV_PROCESS /*65533*/:
                                                        baseFragment = new FragmentVVProcess();
                                                        break;
                                                    case FRAGMENT_SUBTITLE /*65534*/:
                                                        baseFragment = new FragmentSubtitle();
                                                        break;
                                                    default:
                                                        switch (i) {
                                                            case FRAGMENT_ID_CARD /*1048560*/:
                                                                baseFragment = new FragmentIDCard();
                                                                break;
                                                            case 1048561:
                                                                baseFragment = new FragmentLiveReview();
                                                                break;
                                                            case MODULE_CONTENT /*1048562*/:
                                                                baseFragment = new FragmentModuleContent();
                                                                break;
                                                            case FRAGMENT_CLONE_PROCESS /*1048563*/:
                                                                baseFragment = new FragmentCloneProcess();
                                                                break;
                                                            case FRAGMENT_CLONE_GALLERY /*1048564*/:
                                                                baseFragment = new FragmentCloneGallery();
                                                                break;
                                                            case FRAGMENT_CLONE_USE_GUIDE /*1048565*/:
                                                                baseFragment = new FragmentCloneUseGuide();
                                                                break;
                                                            default:
                                                                switch (i) {
                                                                    case FRAGMENT_AMBILIGHT /*16777200*/:
                                                                        baseFragment = new FragmentAmbilight();
                                                                        break;
                                                                    case 16777201:
                                                                        baseFragment = new FragmentFastMotion();
                                                                        break;
                                                                    default:
                                                                        if (str != null) {
                                                                            baseFragment = (BaseFragment) Fragment.instantiate(this.mActivity, str);
                                                                            break;
                                                                        }
                                                                        break;
                                                                }
                                                        }
                                                }
                                        }
                                }
                        }
                }
                baseFragment.setDegree(this.animationComposite.getTargetDegree());
            } else {
                baseFragment = new FragmentTopConfig();
            }
        }
        inceptFragment(baseFragment, z, i2, baseLifecycleListener);
        return baseFragment;
    }

    private void inceptFragment(BaseFragment baseFragment, boolean z, int i, BaseLifecycleListener baseLifecycleListener) {
        baseFragment.setLastFragmentInfo(i);
        baseFragment.setLifecycleListener(baseLifecycleListener);
        baseFragment.registerProtocol();
        baseFragment.setEnableClickInitValue(!z);
    }

    private void initCurrentFragments(SparseIntArray sparseIntArray) {
        int size = sparseIntArray.size();
        this.currentFragments = new SparseArray(size);
        for (int i = 0; i < size; i++) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(Integer.valueOf(sparseIntArray.valueAt(i)));
            this.currentFragments.put(sparseIntArray.keyAt(i), arrayList);
        }
    }

    private int popStoredFragment(@IdRes int i) {
        SparseIntArray sparseIntArray = this.mStoreFragments;
        if (sparseIntArray == null) {
            return 240;
        }
        int i2 = sparseIntArray.get(i, 240);
        this.mStoreFragments.delete(i);
        return i2;
    }

    private void storeFragmentInfo(@IdRes int i, int i2) {
        if (this.mStoreFragments == null) {
            this.mStoreFragments = new SparseIntArray();
        }
        this.mStoreFragments.put(i, i2);
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.android.camera.fragment.BaseFragmentOperation>, for r7v0, types: [java.util.List, java.util.List<com.android.camera.fragment.BaseFragmentOperation>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void batchFragmentRequest(List<BaseFragmentOperation> list) {
        List resetFeatureFragment = resetFeatureFragment(list);
        if (list == null) {
            if (resetFeatureFragment != null) {
                applyUpdateSet(resetFeatureFragment, null);
            }
            return;
        }
        for (BaseFragmentOperation baseFragmentOperation : list) {
            int i = baseFragmentOperation.operateType;
            if ((i != 1 && i != 6) || getActiveFragment(baseFragmentOperation.containerViewId) != baseFragmentOperation.pendingFragmentInfo) {
                if (resetFeatureFragment == null) {
                    resetFeatureFragment = new ArrayList(list.size());
                }
                resetFeatureFragment.add(baseFragmentOperation);
            }
        }
        if (resetFeatureFragment != null) {
            applyUpdateSet(resetFeatureFragment, null);
        }
        this.mLastFragmentAlias = list;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:113:0x031b, code lost:
        if (getActiveFragment(com.android.camera.R.id.bottom_beauty) != 251) goto L_0x031d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:116:0x0326, code lost:
        r10 = com.android.camera.fragment.BaseFragmentOperation.create(com.android.camera.R.id.bottom_beauty);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00f5, code lost:
        r10 = com.android.camera.fragment.BaseFragmentOperation.create(com.android.camera.R.id.rotation_full_screen_feature);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0137, code lost:
        r10 = com.android.camera.fragment.BaseFragmentOperation.create(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x01c3, code lost:
        if (getActiveFragment(com.android.camera.R.id.bottom_beauty) != 65520) goto L_0x031d;
     */
    /* JADX WARNING: Removed duplicated region for block: B:121:0x0337 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:122:0x0338  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void delegateEvent(int i) {
        BaseFragmentOperation baseFragmentOperation;
        BaseFragmentOperation baseFragmentOperation2;
        int i2;
        int i3;
        ArrayList arrayList = new ArrayList();
        if (i != 2) {
            if (i != 23) {
                if (i != 6) {
                    if (i != 7) {
                        switch (i) {
                            case 10:
                                if (getActiveFragment(R.id.bottom_beauty) != 4090) {
                                    baseFragmentOperation2 = BaseFragmentOperation.create(R.id.bottom_beauty);
                                    i2 = 4090;
                                    break;
                                }
                                break;
                            case 11:
                                if (getActiveFragment(R.id.full_screen_feature) != 1048561) {
                                    baseFragmentOperation2 = BaseFragmentOperation.create(R.id.full_screen_feature);
                                    i2 = 1048561;
                                    break;
                                }
                                break;
                            case 12:
                                if (getActiveFragment(R.id.bottom_beauty) != 4092) {
                                    baseFragmentOperation2 = BaseFragmentOperation.create(R.id.bottom_beauty);
                                    i2 = FRAGMENT_POPUP_LIVE_STICKER;
                                    break;
                                }
                                break;
                            case 13:
                                if (getActiveFragment(R.id.bottom_beauty) != 4093) {
                                    baseFragmentOperation2 = BaseFragmentOperation.create(R.id.bottom_beauty);
                                    i2 = FRAGMENT_POPUP_LIVE_SPEED;
                                    break;
                                }
                                break;
                            case 14:
                                int uiStyle = DataRepository.dataItemRunning().getUiStyle();
                                i2 = FRAGMENT_MIMOJI_LIST;
                                if (uiStyle != 0) {
                                    break;
                                } else {
                                    i3 = R.id.bottom_mimoji;
                                    if (getActiveFragment(R.id.bottom_mimoji) != 65520) {
                                        baseFragmentOperation2 = BaseFragmentOperation.create(R.id.bottom_mimoji);
                                        break;
                                    }
                                }
                                break;
                            case 15:
                                if (getActiveFragment(R.id.bottom_action) == 65530) {
                                    arrayList.add(BaseFragmentOperation.create(R.id.bottom_popup_tips).show(getOriginalFragment(R.id.bottom_popup_tips)));
                                    if (C0124O00000oO.isSupportedOpticalZoom() || C0124O00000oO.Oo00ooo() || HybridZoomingSystem.IS_3_OR_MORE_SAT) {
                                        arrayList.add(BaseFragmentOperation.create(R.id.bottom_popup_dual_camera_adjust).show(getOriginalFragment(R.id.bottom_popup_dual_camera_adjust)));
                                    }
                                    baseFragmentOperation = BaseFragmentOperation.create(R.id.full_screen_feature).popAndClearOthers(4086);
                                    break;
                                } else {
                                    arrayList.add(BaseFragmentOperation.create(R.id.bottom_popup_tips).hideCurrent());
                                    arrayList.add(BaseFragmentOperation.create(R.id.bottom_popup_dual_camera_adjust).hideCurrent());
                                    baseFragmentOperation = BaseFragmentOperation.create(R.id.full_screen_feature).push(FRAGMENT_VV_PROCESS);
                                    break;
                                }
                            default:
                                switch (i) {
                                    case 19:
                                        i3 = R.id.fragment_full_screen_mimoji_emoticon;
                                        if (getActiveFragment(R.id.fragment_full_screen_mimoji_emoticon) != 65522) {
                                            baseFragmentOperation2 = BaseFragmentOperation.create(R.id.fragment_full_screen_mimoji_emoticon);
                                            i2 = 65522;
                                            break;
                                        }
                                        break;
                                    case 20:
                                        i3 = R.id.fragment_full_screen_mimoji_gif;
                                        if (getActiveFragment(R.id.fragment_full_screen_mimoji_gif) != 65524) {
                                            baseFragmentOperation2 = BaseFragmentOperation.create(R.id.fragment_full_screen_mimoji_gif);
                                            i2 = 65524;
                                            break;
                                        }
                                        break;
                                    case 21:
                                        if (getActiveFragment(R.id.bottom_beauty) != 1048574) {
                                            baseFragmentOperation2 = BaseFragmentOperation.create(R.id.bottom_beauty);
                                            i2 = 1048574;
                                            break;
                                        }
                                        break;
                                    default:
                                        switch (i) {
                                            case 30:
                                                if (getActiveFragment(R.id.rotation_full_screen_feature) != 65525) {
                                                    baseFragmentOperation2 = BaseFragmentOperation.create(R.id.rotation_full_screen_feature);
                                                    i2 = FRAGMENT_MODES_MORE_NORMAL;
                                                    break;
                                                }
                                                break;
                                            case 31:
                                                if (getActiveFragment(R.id.rotation_full_screen_feature) != 65526) {
                                                    baseFragmentOperation2 = BaseFragmentOperation.create(R.id.rotation_full_screen_feature);
                                                    i2 = FRAGMENT_MODES_MORE_POPUP;
                                                    break;
                                                }
                                                break;
                                            case 32:
                                                if (getActiveFragment(R.id.rotation_full_screen_feature) != 65527) {
                                                    baseFragmentOperation2 = BaseFragmentOperation.create(R.id.rotation_full_screen_feature);
                                                    i2 = FRAGMENT_MODES_EDIT;
                                                    break;
                                                }
                                                break;
                                            case 33:
                                                if (getActiveFragment(R.id.bottom_beauty) != 4087) {
                                                    baseFragmentOperation2 = BaseFragmentOperation.create(R.id.bottom_beauty);
                                                    i2 = FRAGMENT_LIGHTING;
                                                    break;
                                                }
                                                break;
                                            case 34:
                                                if (getActiveFragment(R.id.bottom_beauty) != 16777201) {
                                                    baseFragmentOperation2 = BaseFragmentOperation.create(R.id.bottom_beauty);
                                                    i2 = 16777201;
                                                    break;
                                                }
                                                break;
                                            case 35:
                                                if (getActiveFragment(R.id.full_screen_feature) != 16777213) {
                                                    baseFragmentOperation2 = BaseFragmentOperation.create(R.id.full_screen_feature);
                                                    i2 = FRAGMENT_SLOW_SHUTTER_USE_GUIDE;
                                                    break;
                                                }
                                                break;
                                            case 36:
                                                if (getActiveFragment(R.id.bottom_beauty) != 16777206) {
                                                    baseFragmentOperation2 = BaseFragmentOperation.create(R.id.bottom_beauty);
                                                    i2 = FRAGMENT_FAST_MOTION_PRO;
                                                    break;
                                                }
                                                break;
                                            case 37:
                                                if (getActiveFragment(R.id.bottom_beauty) != 16777207) {
                                                    baseFragmentOperation2 = BaseFragmentOperation.create(R.id.bottom_beauty);
                                                    i2 = 16777207;
                                                    break;
                                                }
                                                break;
                                            case 38:
                                                if (getActiveFragment(R.id.full_screen_feature) == 16777210) {
                                                    baseFragmentOperation2 = BaseFragmentOperation.create(R.id.full_screen_feature);
                                                    i2 = FRAGMENT_FILM_TIME_FREEZE;
                                                    break;
                                                } else {
                                                    baseFragmentOperation2 = BaseFragmentOperation.create(R.id.full_screen_feature);
                                                    i2 = FRAGMENT_TIME_FREEZE_USE_GUIDE;
                                                    break;
                                                }
                                            case 39:
                                                if (getActiveFragment(R.id.full_screen_feature) == 16777212) {
                                                    baseFragmentOperation2 = BaseFragmentOperation.create(R.id.full_screen_feature);
                                                    i2 = FRAGMENT_DOLLY_ZOOM_PROCESS;
                                                    break;
                                                } else {
                                                    baseFragmentOperation2 = BaseFragmentOperation.create(R.id.full_screen_feature);
                                                    i2 = FRAGMENT_DOLLY_ZOOM_USE_GUIDE;
                                                    break;
                                                }
                                        }
                                }
                        }
                    } else {
                        int activeFragment = getActiveFragment(R.id.bottom_action);
                        if (activeFragment == 65530) {
                            delegateEvent(15);
                            return;
                        }
                        if (activeFragment != 241) {
                            arrayList.add(BaseFragmentOperation.create(R.id.bottom_action).popAndClearOthers(241));
                            arrayList.add(BaseFragmentOperation.create(R.id.bottom_popup_tips).replaceWith(65529));
                        }
                        if (arrayList.isEmpty()) {
                            applyUpdateSet(arrayList, null);
                            return;
                        }
                        return;
                    }
                    BaseFragmentOperation baseFragmentOperation3 = BaseFragmentOperation.create(R.id.full_screen_feature);
                    baseFragmentOperation = baseFragmentOperation3.removeCurrent();
                    arrayList.add(baseFragmentOperation);
                    if (arrayList.isEmpty()) {
                    }
                } else {
                    if (getActiveFragment(R.id.bottom_action) != 4083) {
                        arrayList.add(BaseFragmentOperation.create(R.id.bottom_action).push(4083));
                        arrayList.add(BaseFragmentOperation.create(R.id.main_top).hideCurrent());
                        arrayList.add(BaseFragmentOperation.create(R.id.bottom_popup_manually).hideCurrent());
                        int activeFragment2 = getActiveFragment(R.id.bottom_beauty);
                        if (activeFragment2 != 240) {
                            storeFragmentInfo(R.id.bottom_beauty, activeFragment2);
                            arrayList.add(BaseFragmentOperation.create(R.id.bottom_beauty).hideCurrent());
                        }
                        arrayList.add(BaseFragmentOperation.create(R.id.bottom_popup_tips).hideCurrent());
                        baseFragmentOperation = BaseFragmentOperation.create(R.id.bottom_popup_dual_camera_adjust).hideCurrent();
                    } else {
                        arrayList.add(BaseFragmentOperation.create(R.id.bottom_action).popAndClearOthers(241));
                        arrayList.add(BaseFragmentOperation.create(R.id.main_top).show(getOriginalFragment(R.id.main_top)));
                        arrayList.add(BaseFragmentOperation.create(R.id.bottom_popup_manually).show(getOriginalFragment(R.id.bottom_popup_manually)));
                        int popStoredFragment = popStoredFragment(R.id.bottom_beauty);
                        if (popStoredFragment != 240) {
                            arrayList.add(BaseFragmentOperation.create(R.id.bottom_beauty).show(popStoredFragment));
                        }
                        arrayList.add(BaseFragmentOperation.create(R.id.bottom_popup_tips).show(getOriginalFragment(R.id.bottom_popup_tips)));
                        if (C0124O00000oO.isSupportedOpticalZoom() || C0124O00000oO.Oo00ooo() || HybridZoomingSystem.IS_3_OR_MORE_SAT) {
                            baseFragmentOperation = BaseFragmentOperation.create(R.id.bottom_popup_dual_camera_adjust).show(getOriginalFragment(R.id.bottom_popup_dual_camera_adjust));
                        }
                        if (arrayList.isEmpty()) {
                        }
                    }
                    arrayList.add(baseFragmentOperation);
                    if (arrayList.isEmpty()) {
                    }
                }
            } else if (getActiveFragment(R.id.full_screen_feature) != 1048565) {
                baseFragmentOperation2 = BaseFragmentOperation.create(R.id.full_screen_feature);
                i2 = FRAGMENT_CLONE_USE_GUIDE;
            } else {
                baseFragmentOperation2 = BaseFragmentOperation.create(R.id.full_screen_feature);
                i2 = FRAGMENT_CLONE_PROCESS;
            }
            baseFragmentOperation = baseFragmentOperation2.replaceWith(i2);
            arrayList.add(baseFragmentOperation);
            if (arrayList.isEmpty()) {
            }
        } else {
            i2 = 251;
        }
        baseFragmentOperation2 = BaseFragmentOperation.create(R.id.bottom_beauty);
        baseFragmentOperation = baseFragmentOperation2.replaceWith(i2);
        arrayList.add(baseFragmentOperation);
        if (arrayList.isEmpty()) {
        }
    }

    public Disposable delegateMode(@Nullable Completable completable, StartControl startControl, BaseLifecycleListener baseLifecycleListener) {
        if (this.mActivity == null) {
            return null;
        }
        Action o000000o = baseLifecycleListener != null ? new O000000o(baseLifecycleListener) : null;
        if (completable != null) {
            if (baseLifecycleListener != null) {
                completable.subscribe(o000000o);
            } else {
                completable.subscribe();
            }
        }
        return this.animationComposite.dispose(null, null, startControl);
    }

    public int getActiveFragment(@IdRes int i) {
        List list = (List) this.currentFragments.get(i);
        if (list == null || list.isEmpty()) {
            return 240;
        }
        return ((Integer) list.get(list.size() - 1)).intValue();
    }

    public AnimationComposite getAnimationComposite() {
        return this.animationComposite;
    }

    public int getOriginalFragment(@IdRes int i) {
        return this.originalFragments.get(i, 240);
    }

    /* JADX WARNING: type inference failed for: r9v4, types: [int] */
    /* JADX WARNING: type inference failed for: r9v5 */
    /* JADX WARNING: type inference failed for: r9v6 */
    /* JADX WARNING: type inference failed for: r9v7 */
    /* JADX WARNING: type inference failed for: r9v11 */
    /* JADX WARNING: type inference failed for: r9v12 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r9v5
  assigns: []
  uses: []
  mth insns count: 125
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x00e9  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0103  */
    /* JADX WARNING: Unknown variable types count: 2 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void init(FragmentManager fragmentManager, int i, BaseLifecycleListener baseLifecycleListener) {
        ? r9;
        BaseFragment baseFragment;
        ? r92;
        int i2;
        String str;
        int i3;
        boolean z;
        BaseFragmentDelegate baseFragmentDelegate;
        registerProtocol();
        this.mSupportFragmentManager = fragmentManager;
        this.mBaseLifecycleListener = baseLifecycleListener;
        BaseLifecycleListener baseLifecycleListener2 = baseLifecycleListener;
        BaseFragment constructFragment = constructFragment(true, 13, null, 240, baseLifecycleListener2);
        BaseFragment constructFragment2 = constructFragment(true, 244, null, 240, baseLifecycleListener2);
        BaseFragment constructFragment3 = constructFragment(true, 247, null, 240, baseLifecycleListener2);
        BaseFragment constructFragment4 = constructFragment(true, 65529, null, 240, baseLifecycleListener2);
        BaseFragment constructFragment5 = constructFragment(true, 241, null, 240, baseLifecycleListener2);
        BaseFragment constructFragment6 = constructFragment(true, 243, null, 240, baseLifecycleListener2);
        BaseFragment constructFragment7 = constructFragment(true, 4080, null, 240, baseLifecycleListener2);
        BaseFragment constructFragment8 = constructFragment(true, MODULE_CONTENT, null, 240, baseLifecycleListener2);
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        this.animationComposite.put(constructFragment.getFragmentInto(), constructFragment);
        beginTransaction.add((Fragment) constructFragment, constructFragment.getFragmentTag());
        beginTransaction.replace(R.id.bottom_popup_tips, constructFragment4, constructFragment4.getFragmentTag());
        beginTransaction.replace(R.id.bottom_action, constructFragment5, constructFragment5.getFragmentTag());
        beginTransaction.replace(R.id.main_top, constructFragment2, constructFragment2.getFragmentTag());
        beginTransaction.replace(R.id.bottom_popup_manually, constructFragment3, constructFragment3.getFragmentTag());
        beginTransaction.replace(R.id.main_content, constructFragment6, constructFragment6.getFragmentTag());
        beginTransaction.replace(R.id.panorama_content, constructFragment7, constructFragment7.getFragmentTag());
        beginTransaction.replace(R.id.module_content, constructFragment8, constructFragment8.getFragmentTag());
        if (C0124O00000oO.isSupportedOpticalZoom() || HybridZoomingSystem.IS_3_OR_MORE_SAT || C0122O00000o.instance().isSupportBokehAdjust()) {
            r92 = R.id.bottom_action;
            z = true;
            i3 = 4084;
            str = null;
            i2 = 240;
            baseFragmentDelegate = this;
        } else if (C0124O00000oO.Oo00ooo()) {
            z = true;
            baseFragmentDelegate = this;
            r9 = R.id.main_content;
            i3 = 4085;
            r9 = R.id.bottom_popup_manually;
            str = null;
            r9 = R.id.main_top;
            i2 = 240;
            r92 = R.id.bottom_action;
        } else {
            r9 = R.id.bottom_action;
            baseFragment = null;
            if (baseFragment == null) {
                this.originalFragments.put(R.id.bottom_popup_dual_camera_adjust, baseFragment.getFragmentInto());
                this.animationComposite.put(baseFragment.getFragmentInto(), baseFragment);
                beginTransaction.replace(R.id.bottom_popup_dual_camera_adjust, baseFragment, baseFragment.getFragmentTag());
            } else {
                this.originalFragments.put(R.id.bottom_popup_dual_camera_adjust, 240);
            }
            this.originalFragments.put(R.id.bottom_popup_tips, constructFragment4.getFragmentInto());
            this.originalFragments.put(r9, constructFragment5.getFragmentInto());
            this.originalFragments.put(R.id.main_top, constructFragment2.getFragmentInto());
            this.originalFragments.put(R.id.bottom_popup_manually, constructFragment3.getFragmentInto());
            this.originalFragments.put(R.id.main_content, constructFragment6.getFragmentInto());
            this.originalFragments.put(R.id.panorama_content, constructFragment7.getFragmentInto());
            this.originalFragments.put(R.id.bottom_beauty, 240);
            this.originalFragments.put(R.id.main_subtitle, 240);
            this.originalFragments.put(R.id.mimoji_full_screen, 240);
            this.originalFragments.put(R.id.module_content, 240);
            this.animationComposite.put(constructFragment4.getFragmentInto(), constructFragment4);
            this.animationComposite.put(constructFragment2.getFragmentInto(), constructFragment2);
            this.animationComposite.put(constructFragment3.getFragmentInto(), constructFragment3);
            this.animationComposite.put(constructFragment6.getFragmentInto(), constructFragment6);
            this.animationComposite.put(constructFragment5.getFragmentInto(), constructFragment5);
            this.animationComposite.put(constructFragment7.getFragmentInto(), constructFragment7);
            initCurrentFragments(this.originalFragments);
            beginTransaction.commitAllowingStateLoss();
            baseLifecycleListener.onLifeAlive();
        }
        baseFragment = baseFragmentDelegate.constructFragment(z, i3, str, i2, baseLifecycleListener);
        r9 = r92;
        if (baseFragment == null) {
        }
        this.originalFragments.put(R.id.bottom_popup_tips, constructFragment4.getFragmentInto());
        this.originalFragments.put(r9, constructFragment5.getFragmentInto());
        this.originalFragments.put(R.id.main_top, constructFragment2.getFragmentInto());
        this.originalFragments.put(R.id.bottom_popup_manually, constructFragment3.getFragmentInto());
        this.originalFragments.put(R.id.main_content, constructFragment6.getFragmentInto());
        this.originalFragments.put(R.id.panorama_content, constructFragment7.getFragmentInto());
        this.originalFragments.put(R.id.bottom_beauty, 240);
        this.originalFragments.put(R.id.main_subtitle, 240);
        this.originalFragments.put(R.id.mimoji_full_screen, 240);
        this.originalFragments.put(R.id.module_content, 240);
        this.animationComposite.put(constructFragment4.getFragmentInto(), constructFragment4);
        this.animationComposite.put(constructFragment2.getFragmentInto(), constructFragment2);
        this.animationComposite.put(constructFragment3.getFragmentInto(), constructFragment3);
        this.animationComposite.put(constructFragment6.getFragmentInto(), constructFragment6);
        this.animationComposite.put(constructFragment5.getFragmentInto(), constructFragment5);
        this.animationComposite.put(constructFragment7.getFragmentInto(), constructFragment7);
        initCurrentFragments(this.originalFragments);
        beginTransaction.commitAllowingStateLoss();
        baseLifecycleListener.onLifeAlive();
    }

    public void lazyLoadFragment(@IdRes int i, int i2) {
        if (this.originalFragments.get(i) != i2) {
            FragmentTransaction beginTransaction = this.mSupportFragmentManager.beginTransaction();
            BaseFragment constructFragment = constructFragment(true, i2, null, 240, this.mBaseLifecycleListener);
            beginTransaction.replace(i, constructFragment, constructFragment.getFragmentTag());
            this.originalFragments.put(i, constructFragment.getFragmentInto());
            this.animationComposite.put(constructFragment.getFragmentInto(), constructFragment);
            updateCurrentFragments(i, constructFragment.getFragmentInto(), 1);
            beginTransaction.commitAllowingStateLoss();
        }
    }

    public void registerProtocol() {
        ModeCoordinatorImpl.getInstance().attachProtocol(160, this);
    }

    public List resetFeatureFragment(List list) {
        List<BaseFragmentOperation> list2 = this.mLastFragmentAlias;
        if (list2 == null) {
            return null;
        }
        List list3 = null;
        for (BaseFragmentOperation baseFragmentOperation : list2) {
            int i = baseFragmentOperation.containerViewId;
            int originalFragment = getOriginalFragment(i);
            boolean z = false;
            if (list != null) {
                Iterator it = list.iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (((BaseFragmentOperation) it.next()).saveWith(baseFragmentOperation)) {
                            z = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
            if (!z) {
                if (list3 == null) {
                    list3 = new ArrayList();
                }
                list3.add(originalFragment == 240 ? BaseFragmentOperation.create(i).popAndClearOthers(4090) : BaseFragmentOperation.create(i).popAndClearOthers(originalFragment));
            }
        }
        this.mLastFragmentAlias = null;
        return list3;
    }

    public void unRegisterProtocol() {
        ModeCoordinatorImpl.getInstance().detachProtocol(160, this);
        Camera camera = this.mActivity;
        if (camera != null && !camera.isFinishing()) {
            List resetFeatureFragment = resetFeatureFragment(null);
            if (resetFeatureFragment != null) {
                applyUpdateSet(resetFeatureFragment, null);
            }
        }
        this.animationComposite.destroy();
        this.mActivity = null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0024, code lost:
        if (((java.lang.Integer) r0.get(r1)).intValue() != r4) goto L_0x002a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0026, code lost:
        r0.remove(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002a, code lost:
        r1 = r1 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0031, code lost:
        if (r1 >= r0.size()) goto L_0x0049;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x003d, code lost:
        if (((java.lang.Integer) r0.get(r1)).intValue() != r4) goto L_0x0043;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x003f, code lost:
        r0.remove(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0055, code lost:
        if (r1 >= r0.size()) goto L_0x008a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0061, code lost:
        if (((java.lang.Integer) r0.get(r1)).intValue() != r4) goto L_0x0064;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0064, code lost:
        r1 = r1 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x006b, code lost:
        if (r1 >= r0.size()) goto L_0x008a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0077, code lost:
        if (((java.lang.Integer) r0.get(r1)).intValue() != r4) goto L_0x007a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x007a, code lost:
        r1 = r1 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0018, code lost:
        if (r1 >= r0.size()) goto L_0x008a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateCurrentFragments(@IdRes int i, int i2, int i3) {
        List list = (List) this.currentFragments.get(i);
        if (list != null || i3 == 1) {
            int i4 = 0;
            switch (i3) {
                case 1:
                    if (list == null) {
                        list = new ArrayList();
                        this.currentFragments.put(i, list);
                        break;
                    }
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    list.clear();
                    break;
                case 6:
                    while (true) {
                        int i5 = i5 + 1;
                        break;
                    }
                case 7:
                    break;
            }
            list.add(Integer.valueOf(i2));
        }
    }
}
