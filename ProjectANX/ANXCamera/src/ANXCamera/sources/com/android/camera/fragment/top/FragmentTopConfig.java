package com.android.camera.fragment.top;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RotateDrawable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.StringRes;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import com.android.camera.ActivityBase;
import com.android.camera.Camera;
import com.android.camera.CameraSettings;
import com.android.camera.Display;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.R;
import com.android.camera.ToastUtils;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.animation.type.AlphaInOnSubscribe;
import com.android.camera.animation.type.AlphaOutOnSubscribe;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.config.ComponentConfigFlash;
import com.android.camera.data.data.config.ComponentConfigHdr;
import com.android.camera.data.data.config.ComponentConfigMeter;
import com.android.camera.data.data.config.ComponentConfigUltraWide;
import com.android.camera.data.data.config.ComponentConfigVideoQuality;
import com.android.camera.data.data.config.ComponentRunningMacroMode;
import com.android.camera.data.data.config.ComponentRunningUltraPixel;
import com.android.camera.data.data.config.DataItemConfig;
import com.android.camera.data.data.config.SupportedConfigFactory;
import com.android.camera.data.data.config.SupportedConfigs;
import com.android.camera.data.data.config.TopConfigItem;
import com.android.camera.data.data.runing.ComponentRunningAiAudio;
import com.android.camera.data.data.runing.ComponentRunningColorEnhance;
import com.android.camera.data.data.runing.ComponentRunningEisPro;
import com.android.camera.data.data.runing.ComponentRunningMoon;
import com.android.camera.data.data.runing.ComponentRunningShine;
import com.android.camera.data.observeable.RxData.DataWrap;
import com.android.camera.data.observeable.VMFeature;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.fragment.FragmentUtils;
import com.android.camera.fragment.music.LiveMusicActivity;
import com.android.camera.fragment.top.TopExpandAdapter.ExpandListener;
import com.android.camera.log.Log;
import com.android.camera.module.ModuleManager;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BottomPopupTips;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.CameraClickObservable;
import com.android.camera.protocol.ModeProtocol.CameraModuleSpecial;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.DollyZoomAction;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.HandleBeautyRecording;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.protocol.ModeProtocol.MoreModePopupController;
import com.android.camera.protocol.ModeProtocol.MultiFeatureManager;
import com.android.camera.protocol.ModeProtocol.SnapShotIndicator;
import com.android.camera.protocol.ModeProtocol.StandaloneRecorderProtocol;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.protocol.ModeProtocol.TopConfigProtocol;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.AlgoAttr;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.android.camera.statistic.MistatsConstants.BeautyAttr;
import com.android.camera.statistic.MistatsConstants.FeatureName;
import com.android.camera.statistic.MistatsConstants.FlashAttr;
import com.android.camera.statistic.MistatsConstants.GoogleLens;
import com.android.camera.statistic.MistatsConstants.Live;
import com.android.camera.statistic.MistatsConstants.LiveShotAttr;
import com.android.camera.statistic.MistatsConstants.Manual;
import com.android.camera.statistic.MistatsConstants.MiLive;
import com.android.camera.statistic.MistatsConstants.VLogAttr;
import com.android.camera.statistic.MistatsWrapper;
import com.android.camera.timerburst.CustomSeekBar;
import com.android.camera.timerburst.TimerBurstController;
import com.android.camera.tts.TTSHelper;
import com.android.camera.ui.ColorImageView;
import com.android.camera.ui.ShapeBackGroundView;
import com.android.camera.ui.SlideSwitchButton.SlideSwitchListener;
import com.android.camera.ui.VideoTagView;
import com.android.camera.ui.drawable.snap.PaintConditionReferred;
import com.xiaomi.stat.MiStat.Param;
import io.reactivex.Completable;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import miui.view.animation.CubicEaseInOutInterpolator;
import miui.view.animation.CubicEaseOutInterpolator;
import miui.view.animation.QuadraticEaseInOutInterpolator;

public class FragmentTopConfig extends BaseFragment implements OnClickListener, ExpandListener, TopAlert, SnapShotIndicator, HandleBackTrace, HandleBeautyRecording, SlideSwitchListener {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int EXPAND_STATE_CENTER = 2;
    private static final int EXPAND_STATE_LEFT = 0;
    private static final int EXPAND_STATE_LEFT_FROM_SIBLING = 1;
    private static final int EXPAND_STATE_RIGHT = 4;
    private static final int EXPAND_STATE_RIGHT_FROM_SIBLING = 3;
    private static final String TAG = "FragmentTopConfig";
    public static final int TIP_HINT_DURATION_1S = 1000;
    public static final int TIP_HINT_DURATION_2S = 2000;
    public static final int TIP_HINT_DURATION_3S = 3000;
    private int[] mAiSceneResources;
    private int[] mAutoZoomResources;
    private TextView mCaptureDelayNumber;
    private boolean mCaptureNumberAutoHibernationOffset = false;
    private int[] mCinematicRatioResources;
    private List mConfigViews;
    private int mCurrentAiSceneLevel;
    private CustomSeekBar mCustomSeekBarCount;
    private CustomSeekBar mCustomSeekBarInterval;
    private SparseBooleanArray mDisabledFunctionMenu;
    private int mDisplayRectTopMargin;
    private int[] mDocumentResources;
    /* access modifiers changed from: private */
    public ExtraAdapter mExtraAdapter;
    /* access modifiers changed from: private */
    public ValueAnimator mExtraMenuHideAnimator;
    private FragmentTopAlert mFragmentTopAlert;
    private int mGifResource;
    private ImageView mImageViewBack;
    private boolean mIsRTL;
    private boolean mIsShowExtraMenu;
    private boolean mIsShowExtraTimerMenu = false;
    private boolean mIsShowTopLyingDirectHint;
    private View mLayoutCount;
    private View mLayoutInterval;
    private int[] mLightingResource;
    private ObjectAnimator mLiveShotAnimator;
    private int[] mLiveShotResource;
    private LinearLayout mLlTimerMenu;
    private int[] mMoreResources;
    private TextView mMultiSnapNum;
    private TextAppearanceSpan mSnapStyle;
    private int mSpacesItemWidth;
    private SpannableStringBuilder mStringBuilder;
    private int[] mSuperEISResources;
    private int[] mSuperMacroResources;
    private SupportedConfigs mSupportedConfigs;
    private Map mTipsState = new HashMap();
    /* access modifiers changed from: private */
    public int mTopBackgroundHeight;
    /* access modifiers changed from: private */
    public ShapeBackGroundView mTopBackgroundView;
    private TopBarAnimationComponent mTopBarAnimationComponent;
    private View mTopConfigMenu;
    /* access modifiers changed from: private */
    public int mTopConfigMenuHeight;
    private int mTopConfigTotalWidth;
    /* access modifiers changed from: private */
    public ViewGroup mTopConfigViewGroup;
    private TopExpendView mTopExpandView;
    private RecyclerView mTopExtraMenu;
    /* access modifiers changed from: private */
    public int mTopExtraMenuHeight;
    private boolean mTopFloatingMode;
    private TextView mTvShotInterval;
    private String[] mUltraPixel108PhotographyTipString;
    private int[] mUltraPixelPhotographyIconResources;
    private String[] mUltraPixelPhotographyTipString;
    private int[] mUltraPixelPortraitResources;
    private int[] mUltraWideBokehResources;
    private VMFeature mVMFeature;
    private int[] mVideo8KResource;
    private int[] mVideoBokehResource;
    private AnimatorSet mZoomInAnimator;
    private AnimatorSet mZoomOutAnimator;

    static /* synthetic */ void O000000o(int i, List list, ImageView imageView) {
        Object tag = imageView.getTag();
        if (!(tag instanceof TopConfigItem) || ((TopConfigItem) tag).configItem != 217) {
            if ((i == 90 || i == 270) && imageView.getRotation() != ((float) i)) {
                list.add(imageView);
            }
        }
    }

    static /* synthetic */ void O000000o(List list, ImageView imageView) {
        Object tag = imageView.getTag();
        if (!(tag instanceof TopConfigItem) || ((TopConfigItem) tag).configItem != 169) {
            list.add(imageView);
        }
    }

    private void alertHDR(int i, boolean z, boolean z2, boolean z3) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert) && !topAlert.isShowMoonSelector()) {
            if (z3) {
                if (i != 0) {
                    reverseExpandTopBar(true);
                } else if (z2) {
                    ImageView topImage = getTopImage(194);
                    if (topImage != null) {
                        topImage.performClick();
                    }
                }
            }
            alertSwitchTip(FragmentTopAlert.TIP_HDR, i, (int) R.string.hdr_tip);
        }
    }

    private void alertTopMusicHint(int i, String str) {
        FragmentTopAlert topAlert = getTopAlert();
        if (topAlert != null) {
            topAlert.alertMusicTip(i, str);
        }
    }

    private void animatorExtraMenu(boolean z) {
        final int i;
        final int i2;
        final boolean z2 = z;
        if (this.mTopExtraMenu.getVisibility() == 0 || z2 || this.mIsShowExtraTimerMenu) {
            if (!z2) {
                ValueAnimator valueAnimator = this.mExtraMenuHideAnimator;
                if (valueAnimator != null && valueAnimator.isRunning()) {
                    return;
                }
            }
            int uiStyle = DataRepository.dataItemRunning().getUiStyle();
            int i3 = 0;
            if (this.mCurrentMode != 165 && (uiStyle == 3 || Display.fitDisplayFull(1.3333333f))) {
                i = 153;
                i2 = 0;
            } else {
                i2 = 255;
                i = 255;
            }
            AnonymousClass2 r2 = new AnimatorListener() {
                public void onAnimationCancel(Animator animator) {
                    FragmentTopConfig.this.setClickEnable(true);
                    if (!z2) {
                        FragmentTopConfig.this.directHiddenExtraMenu();
                    }
                    FragmentTopConfig.this.mExtraMenuHideAnimator = null;
                }

                public void onAnimationEnd(Animator animator) {
                    FragmentTopConfig.this.setClickEnable(true);
                    if (!z2) {
                        FragmentTopConfig.this.directHiddenExtraMenu();
                    }
                    FragmentTopConfig.this.mExtraMenuHideAnimator = null;
                }

                public void onAnimationRepeat(Animator animator) {
                }

                public void onAnimationStart(Animator animator) {
                    FragmentTopConfig.this.setClickEnable(false);
                }
            };
            final int blackOriginHeight = this.mTopBackgroundView.getBlackOriginHeight();
            if (z2) {
                initExtraTimerBurstMenu();
                this.mTopConfigMenu.setVisibility(8);
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mTopConfigViewGroup.getLayoutParams();
                marginLayoutParams.height = this.mTopExtraMenuHeight;
                this.mTopConfigViewGroup.setLayoutParams(marginLayoutParams);
                ShapeBackGroundView shapeBackGroundView = this.mTopBackgroundView;
                if (blackOriginHeight != 0) {
                    i3 = this.mTopExtraMenuHeight;
                }
                shapeBackGroundView.setTag(Integer.valueOf(i3));
                MarginLayoutParams marginLayoutParams2 = (MarginLayoutParams) this.mTopBackgroundView.getLayoutParams();
                marginLayoutParams2.height = this.mTopExtraMenuHeight;
                this.mTopBackgroundView.setLayoutParams(marginLayoutParams2);
                this.mTopBackgroundView.startBackGroundAnimator(i2, i, blackOriginHeight, this.mTopExtraMenuHeight, 0, 66, 200, r2);
            } else {
                this.mTopBackgroundView.setTag(Integer.valueOf(blackOriginHeight));
                this.mTopConfigMenu.setVisibility(0);
                this.mTopExtraMenu.setVisibility(8);
                ViewCompat.setAlpha(this.mTopConfigMenu, 0.0f);
                ViewCompat.animate(this.mTopConfigMenu).alpha(1.0f).setInterpolator(new CubicEaseOutInterpolator()).setDuration(200).setStartDelay(100).start();
                if (this.mExtraMenuHideAnimator == null) {
                    this.mExtraMenuHideAnimator = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
                    this.mExtraMenuHideAnimator.addUpdateListener(new AnimatorUpdateListener() {
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) FragmentTopConfig.this.mTopConfigViewGroup.getLayoutParams();
                            marginLayoutParams.height = (int) (((float) FragmentTopConfig.this.mTopConfigMenuHeight) + (((float) (FragmentTopConfig.this.mTopExtraMenuHeight - FragmentTopConfig.this.mTopConfigMenuHeight)) * floatValue));
                            FragmentTopConfig.this.mTopConfigViewGroup.setLayoutParams(marginLayoutParams);
                            MarginLayoutParams marginLayoutParams2 = (MarginLayoutParams) FragmentTopConfig.this.mTopBackgroundView.getLayoutParams();
                            marginLayoutParams2.height = (int) (((float) FragmentTopConfig.this.mTopBackgroundHeight) + (((float) (FragmentTopConfig.this.mTopExtraMenuHeight - FragmentTopConfig.this.mTopBackgroundHeight)) * floatValue));
                            FragmentTopConfig.this.mTopBackgroundView.setLayoutParams(marginLayoutParams2);
                            float f = (float) blackOriginHeight;
                            int access$500 = FragmentTopConfig.this.mTopExtraMenuHeight;
                            int i = blackOriginHeight;
                            int i2 = (int) (f + (((float) (access$500 - i)) * floatValue));
                            int i3 = i == 0 ? 0 : i2;
                            ShapeBackGroundView access$600 = FragmentTopConfig.this.mTopBackgroundView;
                            int i4 = i2;
                            access$600.setBackgroundAlphaAndRadius((int) (((float) i4) + (((float) (i - i4)) * floatValue)), (int) (floatValue * 66.0f), i2, i3);
                        }
                    });
                    this.mExtraMenuHideAnimator.addListener(r2);
                    this.mExtraMenuHideAnimator.setInterpolator(new CubicEaseOutInterpolator());
                    this.mExtraMenuHideAnimator.setDuration(200);
                }
                this.mExtraMenuHideAnimator.start();
            }
        }
    }

    private void checkFeatureState() {
        String featureNameByLocalMode = VMFeature.getFeatureNameByLocalMode(this.mCurrentMode);
        if (!TextUtils.isEmpty(featureNameByLocalMode) && !((MultiFeatureManager) ModeCoordinatorImpl.getInstance().getAttachProtocol(929)).hasFeatureInstalled(featureNameByLocalMode)) {
            AlphaOutOnSubscribe.directSetResult(this.mTopConfigMenu);
            this.mTopConfigMenu.setTag(Integer.valueOf(-1));
            if (this.mVMFeature == null) {
                this.mVMFeature = (VMFeature) DataRepository.dataItemObservable().get(VMFeature.class);
                this.mVMFeature.startObservable(this, new C0323O0000oO(this));
            }
        }
    }

    private void configBottomPopupTips(boolean z) {
        if (C0122O00000o.instance().OOO0OoO()) {
            BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
            if (bottomPopupTips != null) {
                bottomPopupTips.showIDCardTip(z);
            }
        }
    }

    /* access modifiers changed from: private */
    public void directHiddenExtraMenu() {
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mTopConfigViewGroup.getLayoutParams();
        int i = marginLayoutParams.height;
        int i2 = this.mTopConfigMenuHeight;
        if (i != i2) {
            marginLayoutParams.height = i2;
            this.mTopConfigViewGroup.setLayoutParams(marginLayoutParams);
            int blackOriginHeight = this.mTopBackgroundView.getBlackOriginHeight();
            this.mTopBackgroundView.setTag(Integer.valueOf(blackOriginHeight));
            this.mTopBackgroundView.setCurrentHeight(blackOriginHeight);
            this.mTopBackgroundView.setMaskSpecificHeight(blackOriginHeight, false);
            this.mTopBackgroundView.setCurrentRadius(0);
            MarginLayoutParams marginLayoutParams2 = (MarginLayoutParams) this.mTopBackgroundView.getLayoutParams();
            marginLayoutParams2.height = this.mTopBackgroundHeight;
            this.mTopBackgroundView.setLayoutParams(marginLayoutParams2);
            this.mTopConfigMenu.setVisibility(0);
            this.mTopExtraMenu.setVisibility(8);
            this.mLlTimerMenu.setVisibility(8);
            this.mIsShowExtraTimerMenu = false;
        }
    }

    private void enableAllDisabledMenuItem() {
        SparseBooleanArray sparseBooleanArray = this.mDisabledFunctionMenu;
        if (sparseBooleanArray != null && sparseBooleanArray.size() != 0) {
            int size = this.mDisabledFunctionMenu.size();
            for (int i = 0; i < size; i++) {
                ImageView topImage = getTopImage(this.mDisabledFunctionMenu.keyAt(i));
                if (topImage != null) {
                    AlphaInOnSubscribe.directSetResult(topImage);
                }
            }
            this.mDisabledFunctionMenu.clear();
        }
    }

    private void expandExtraView(ComponentData componentData, View view, int i) {
        TopExpendView topExpendView;
        FragmentTopAlert topAlert = getTopAlert();
        if (topAlert != null) {
            topAlert.hideRecordingTime();
        }
        if (!reverseExpandTopBar(true)) {
            TopExpandAdapter topExpandAdapter = new TopExpandAdapter(componentData, this);
            topExpandAdapter.setAnchorViewX(this.mIsRTL ? view.getRight() : view.getLeft());
            int i2 = 90;
            if (isBothLandscapeMode()) {
                topExpendView = this.mTopExpandView;
                if (this.mDegree >= 180) {
                    i2 = 270;
                }
            } else if (isLeftLandscapeMode()) {
                topExpendView = this.mTopExpandView;
            } else {
                topExpendView = this.mTopExpandView;
                i2 = this.mDegree;
            }
            topExpendView.setRotation(i2);
            this.mTopExpandView.setAdapter(topExpandAdapter);
            this.mTopExpandView.setOnClickListener(new C0328O0000oo0(this));
            TopBarAnimationComponent topBarAnimationComponent = this.mTopBarAnimationComponent;
            topBarAnimationComponent.mTopExpendView = this.mTopExpandView;
            topBarAnimationComponent.mReverseLeft = view.getLeft();
            this.mTopBarAnimationComponent.hideOtherViews(i, this.mConfigViews);
            TopBarAnimationComponent topBarAnimationComponent2 = this.mTopBarAnimationComponent;
            topBarAnimationComponent2.mAnchorView = view;
            topBarAnimationComponent2.spacesItemWidth = this.mSpacesItemWidth;
            topBarAnimationComponent2.showExpendView();
        }
    }

    private Drawable getAiSceneDrawable(int i) {
        TypedArray obtainTypedArray = getResources().obtainTypedArray(R.array.ai_scene_drawables);
        Drawable drawable = null;
        if (i >= 0 && i < obtainTypedArray.length()) {
            drawable = obtainTypedArray.getDrawable(i);
        }
        obtainTypedArray.recycle();
        return drawable;
    }

    private int[] getAiSceneResources() {
        return new int[]{R.drawable.ic_new_ai_scene_off, R.drawable.ic_new_ai_scene_off_shadow};
    }

    private Drawable getAiSceneShadowDrawable(int i) {
        TypedArray obtainTypedArray = getResources().obtainTypedArray(R.array.ai_scene_shadow_drawables);
        Drawable drawable = null;
        if (i >= 0 && i < obtainTypedArray.length()) {
            drawable = obtainTypedArray.getDrawable(i);
        }
        obtainTypedArray.recycle();
        return drawable;
    }

    private int[] getAutoZoomResources() {
        return new int[]{R.drawable.ic_autozoom_off, R.drawable.ic_autozoom_on};
    }

    private int[] getCinematicRatioResources() {
        return new int[]{R.drawable.ic_cinematic_aspect_ratio_off, R.drawable.ic_cinematic_aspect_ratio_shadow};
    }

    private int[] getDocumentResources() {
        return new int[]{R.drawable.document_mode_normal, R.drawable.document_mode_selected};
    }

    private int[] getExposureFeedbackResources() {
        return new int[]{R.drawable.ic_config_exposure_feedback_off, R.drawable.ic_config_exposure_feedback_on};
    }

    private int getGifRecource() {
        return R.drawable.ic_vector_new_config_gif;
    }

    private int getInitialMargin(TopConfigItem topConfigItem, ImageView imageView, int i) {
        int i2;
        SupportedConfigs supportedConfigs = this.mSupportedConfigs;
        int configsSize = supportedConfigs == null ? 0 : supportedConfigs.getConfigsSize();
        if (configsSize <= 0) {
            return 0;
        }
        int i3 = topConfigItem.index;
        LayoutParams layoutParams = (LayoutParams) imageView.getLayoutParams();
        layoutParams.gravity = 0;
        if (configsSize == 1) {
            layoutParams.leftMargin = 0;
            int i4 = topConfigItem.gravity;
            if (i4 == 0) {
                i4 = 8388613;
            }
            layoutParams.gravity = i4;
        } else if (configsSize == 2) {
            if (i3 == 0) {
                layoutParams.leftMargin = 0;
                i2 = topConfigItem.gravity;
                if (i2 == 0) {
                    i2 = 8388611;
                }
            } else {
                if (i3 == 1) {
                    layoutParams.leftMargin = 0;
                    i2 = topConfigItem.gravity;
                    if (i2 == 0) {
                        i2 = 8388613;
                    }
                }
                this.mSpacesItemWidth = (this.mTopConfigTotalWidth - (this.mSupportedConfigs.getConfigsSize() * i)) / (configsSize - 1);
            }
            layoutParams.gravity = i2;
            this.mSpacesItemWidth = (this.mTopConfigTotalWidth - (this.mSupportedConfigs.getConfigsSize() * i)) / (configsSize - 1);
        } else if (i3 == 0) {
            layoutParams.leftMargin = 0;
            layoutParams.gravity = GravityCompat.START;
        } else {
            int i5 = configsSize - 1;
            if (i3 == i5) {
                layoutParams.leftMargin = 0;
                layoutParams.gravity = GravityCompat.END;
            } else {
                this.mSpacesItemWidth = (this.mTopConfigTotalWidth - (this.mSupportedConfigs.getConfigsSize() * i)) / i5;
                return (this.mSpacesItemWidth * i3) + (i3 * i);
            }
        }
        imageView.setLayoutParams(layoutParams);
        return 0;
    }

    private int[] getLightingResources() {
        return new int[]{R.drawable.ic_new_lighting_off, R.drawable.ic_new_lighting_on};
    }

    private int[] getLiveShotResources() {
        return new int[]{R.drawable.ic_motionphoto, R.drawable.ic_motionphoto_shadow};
    }

    private int[] getMoreResources() {
        return new int[]{R.drawable.ic_new_more, R.drawable.ic_new_more_shadow};
    }

    private String[] getPixel108SwitchTipsString() {
        return new String[]{getString(R.string.accessibility_ultra_pixel_photography_off, getString(R.string.ultra_pixel_108mp)), getString(R.string.accessibility_ultra_pixel_photography_on, getString(R.string.ultra_pixel_108mp))};
    }

    private int getPortraitResources() {
        return R.drawable.ic_new_portrait_button_normal;
    }

    private int getSettingResources() {
        return R.drawable.ic_new_config_setting;
    }

    private int[] getSuperEISResources() {
        return new int[]{R.drawable.ic_config_super_eis_off, R.drawable.ic_config_super_eis_on};
    }

    private int[] getSuperMacroResources() {
        return new int[]{R.drawable.ic_config_super_macro_mode, R.drawable.ic_config_super_macro_mode_shadow};
    }

    private FragmentTopAlert getTopAlert() {
        String str;
        FragmentTopAlert fragmentTopAlert = this.mFragmentTopAlert;
        String str2 = TAG;
        if (fragmentTopAlert == null) {
            str = "getTopAlert(): fragment is null";
        } else if (fragmentTopAlert.isAdded()) {
            return this.mFragmentTopAlert;
        } else {
            str = "getTopAlert(): fragment is not added yet";
        }
        Log.d(str2, str);
        return null;
    }

    private int[] getUltraPixelPortraitResources() {
        return new int[]{R.drawable.ic_config_ultrapixelportrait_off, R.drawable.ic_config_ultrapixelportrait_off};
    }

    private int[] getUltraWideBokehResources() {
        return new int[]{R.drawable.ic_ultra_wide_bokeh, R.drawable.ic_ultra_wide_bokeh_shadow};
    }

    private int[] getVideo8KRecource() {
        return new int[]{R.drawable.ic_config_video_8k_normal, R.drawable.ic_config_video_8k_highlight};
    }

    private int[] getVideoBokehResources() {
        return new int[]{R.drawable.ic_new_portrait_button_normal, R.drawable.ic_new_portrait_button_on};
    }

    private void initExtraMenu() {
        int currentCameraId = DataRepository.dataItemGlobal().getCurrentCameraId();
        SupportedConfigs supportedExtraConfigs = SupportedConfigFactory.getSupportedExtraConfigs(this.mCurrentMode, currentCameraId, Camera2DataContainer.getInstance().getCapabilitiesByBogusCameraId(currentCameraId, this.mCurrentMode), DataRepository.dataItemGlobal().isNormalIntent());
        final int integer = getResources().getInteger(R.integer.back_top_extra_column_count);
        int integer2 = getResources().getInteger(R.integer.front_top_extra_column_count);
        if (DataRepository.dataItemGlobal().getDisplayMode() == 2) {
            integer = integer2;
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), integer);
        this.mExtraAdapter = new ExtraAdapter(getContext(), supportedExtraConfigs, this, this);
        if (this.mTopFloatingMode) {
            ((MarginLayoutParams) this.mTopExtraMenu.getLayoutParams()).width = 1080;
        }
        this.mTopExtraMenu.getRecycledViewPool().setMaxRecycledViews(2, this.mExtraAdapter.getItemCount(2));
        this.mTopExtraMenu.getRecycledViewPool().setMaxRecycledViews(1, this.mExtraAdapter.getItemCount(1));
        ((SimpleItemAnimator) this.mTopExtraMenu.getItemAnimator()).setSupportsChangeAnimations(false);
        this.mTopExtraMenu.setAdapter(this.mExtraAdapter);
        gridLayoutManager.setSpanSizeLookup(new SpanSizeLookup() {
            public int getSpanSize(int i) {
                if (FragmentTopConfig.this.mExtraAdapter.getItemViewType(i) == 1) {
                    return integer;
                }
                return 1;
            }
        });
        this.mTopExtraMenu.setLayoutManager(gridLayoutManager);
        this.mTopExtraMenu.setFocusable(false);
        this.mTopExtraMenuHeight = getResources().getDimensionPixelSize(R.dimen.top_config_extra_recyclerview_marginBottom) + getResources().getDimensionPixelSize(R.dimen.top_config_extra_recyclerview_margintop) + (this.mExtraAdapter.getTotalRow(1) * getResources().getDimensionPixelSize(R.dimen.top_config_extra_mulit_item_height)) + (this.mExtraAdapter.getTotalRow(2) * getResources().getDimensionPixelSize(R.dimen.top_config_extra_toggle_item_height));
        this.mExtraAdapter.setAnimateHeight(this.mTopExtraMenuHeight - this.mTopConfigMenuHeight);
    }

    private void initExtraTimerBurstMenu() {
        if (!this.mIsShowExtraMenu) {
            this.mTopExtraMenu.setVisibility(8);
        } else {
            boolean z = this.mIsShowExtraTimerMenu;
            if (z) {
                this.mTopExtraMenu.setVisibility(8);
                this.mLlTimerMenu.setVisibility(0);
                return;
            } else if (!z) {
                this.mTopExtraMenu.setVisibility(0);
            } else {
                return;
            }
        }
        this.mLlTimerMenu.setVisibility(8);
    }

    private void initSnapNumAnimator() {
        this.mZoomInAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.zoom_button_zoom_in);
        this.mZoomInAnimator.setTarget(this.mMultiSnapNum);
        this.mZoomInAnimator.setInterpolator(new QuadraticEaseInOutInterpolator());
        this.mZoomOutAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.zoom_button_zoom_out);
        this.mZoomOutAnimator.setTarget(this.mMultiSnapNum);
        this.mZoomOutAnimator.setInterpolator(new QuadraticEaseInOutInterpolator());
    }

    private void initTopView() {
        ColorImageView colorImageView = (ColorImageView) this.mTopConfigMenu.findViewById(R.id.top_config_00);
        ColorImageView colorImageView2 = (ColorImageView) this.mTopConfigMenu.findViewById(R.id.top_config_01);
        ColorImageView colorImageView3 = (ColorImageView) this.mTopConfigMenu.findViewById(R.id.top_config_02);
        ColorImageView colorImageView4 = (ColorImageView) this.mTopConfigMenu.findViewById(R.id.top_config_03);
        ColorImageView colorImageView5 = (ColorImageView) this.mTopConfigMenu.findViewById(R.id.top_config_04);
        ColorImageView colorImageView6 = (ColorImageView) this.mTopConfigMenu.findViewById(R.id.top_config_05);
        ColorImageView colorImageView7 = (ColorImageView) this.mTopConfigMenu.findViewById(R.id.top_config_06);
        ColorImageView colorImageView8 = (ColorImageView) this.mTopConfigMenu.findViewById(R.id.top_config_07);
        ColorImageView colorImageView9 = (ColorImageView) this.mTopConfigMenu.findViewById(R.id.top_config_08);
        ColorImageView colorImageView10 = (ColorImageView) this.mTopConfigMenu.findViewById(R.id.top_config_09);
        ColorImageView colorImageView11 = (ColorImageView) this.mTopConfigMenu.findViewById(R.id.top_config_10);
        colorImageView.setOnClickListener(this);
        colorImageView2.setOnClickListener(this);
        colorImageView3.setOnClickListener(this);
        colorImageView4.setOnClickListener(this);
        colorImageView5.setOnClickListener(this);
        colorImageView6.setOnClickListener(this);
        colorImageView7.setOnClickListener(this);
        colorImageView8.setOnClickListener(this);
        colorImageView9.setOnClickListener(this);
        colorImageView10.setOnClickListener(this);
        colorImageView11.setOnClickListener(this);
        FolmeUtils.touchTint(colorImageView, colorImageView2, colorImageView3, colorImageView4, colorImageView5, colorImageView6, colorImageView7, colorImageView8, colorImageView9, colorImageView10, colorImageView11);
        this.mConfigViews = new ArrayList();
        this.mConfigViews.add(colorImageView);
        this.mConfigViews.add(colorImageView2);
        this.mConfigViews.add(colorImageView3);
        this.mConfigViews.add(colorImageView4);
        this.mConfigViews.add(colorImageView5);
        this.mConfigViews.add(colorImageView6);
        this.mConfigViews.add(colorImageView7);
        this.mConfigViews.add(colorImageView8);
        this.mConfigViews.add(colorImageView9);
        this.mConfigViews.add(colorImageView10);
        this.mConfigViews.add(colorImageView11);
    }

    private void notifyExtraMenuVisibilityChange(boolean z) {
        TopConfigProtocol topConfigProtocol = (TopConfigProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(193);
        if (topConfigProtocol != null) {
            topConfigProtocol.onExtraMenuVisibilityChange(z);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x006a A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x007c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void onClickByExtraMenu(View view, ConfigChanges configChanges) {
        int i;
        boolean z;
        boolean z2;
        int i2;
        if (Util.isAccessible()) {
            boolean isSwitchOn = DataRepository.dataItemRunning().isSwitchOn("pref_speech_shutter");
            int intValue = ((Integer) view.getTag()).intValue();
            if (intValue == 237) {
                i2 = R.string.pref_camera_picture_format_entry_raw;
                z = DataRepository.dataItemConfig().getComponentConfigRaw().isSwitchOn(this.mCurrentMode);
            } else if (intValue == 251) {
                i2 = R.string.moive_frame;
                z = CameraSettings.isCinematicAspectRatioEnabled(this.mCurrentMode);
            } else if (intValue == 255) {
                ComponentRunningMacroMode componentRunningMacroMode = DataRepository.dataItemRunning().getComponentRunningMacroMode();
                i = componentRunningMacroMode.getResText();
                z = componentRunningMacroMode.isSwitchOn(this.mCurrentMode);
                z2 = true;
                if (intValue == 262) {
                }
                if (i != -1) {
                }
            } else if (intValue != 262) {
                z2 = false;
                z = false;
                i = -1;
                if (intValue == 262 && (!isSwitchOn || !z2)) {
                    view.postDelayed(new O0000o(this, view), 500);
                } else if (i != -1) {
                    TTSHelper tTSHelper = this.mTTSHelper;
                    StringBuilder sb = new StringBuilder();
                    sb.append(getString(i));
                    sb.append(",");
                    sb.append(getString(z ? R.string.accessibility_closed : R.string.accessibility_open));
                    TTSHelper.speakingTextInTalkbackMode(tTSHelper, sb.toString());
                }
            } else {
                i2 = R.string.speech_shutter_tip;
                z = isSwitchOn;
            }
            i = i2;
            z2 = true;
            if (intValue == 262) {
            }
            if (i != -1) {
            }
        }
        int intValue2 = ((Integer) view.getTag()).intValue();
        Log.u(TAG, String.format(Locale.ENGLISH, "top config onClickByExtraMenu, ConfigItem=0x%x", new Object[]{Integer.valueOf(intValue2)}));
        if (intValue2 == 170 && view.getId() == R.id.rl_item_top_config_description) {
            showExtraTimerBurstMenu();
            CameraSettings.setTimerBurstEnable(false);
            configChanges.onConfigChanged(170);
            return;
        }
        configChanges.onConfigChanged(intValue2);
        if (intValue2 == 225) {
            removeExtraMenu(5);
        } else if (intValue2 != 242) {
            this.mExtraAdapter.notifyDataSetChanged();
        } else {
            hideExtraMenu();
            ((TopConfigProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(193)).startAiLens();
            HashMap hashMap = new HashMap();
            hashMap.put(BaseEvent.OPERATE_STATE, AlgoAttr.VAULE_START_AI_DETECT_BUTTON);
            MistatsWrapper.mistatEvent(GoogleLens.KEY_GOOGLE_LENS, hashMap);
        }
    }

    private void onInstallStateChanged(HashMap hashMap) {
        if (isAdded()) {
            for (Entry entry : hashMap.entrySet()) {
                if (((String) entry.getKey()).equals(VMFeature.getFeatureNameByLocalMode(this.mCurrentMode))) {
                    int intValue = ((Integer) entry.getValue()).intValue();
                    if (VMFeature.getScope(intValue) == 16) {
                        if (intValue == 21) {
                            AlphaInOnSubscribe.directSetResult(this.mTopConfigMenu);
                            this.mTopConfigMenu.setTag(Integer.valueOf(1));
                        }
                    }
                }
            }
        }
    }

    private void reConfigTipOfAiEnhancedVideo() {
        alertAiEnhancedVideoHint(DataRepository.dataItemRunning().getComponentRunningAiEnhancedVideo().isSwitchOn(this.mCurrentMode) ? 0 : 8, R.string.pref_camera_video_ai_scene_title);
    }

    private void reConfigTipOfFlash(boolean z) {
        if (!isExtraMenuShowing() && this.mCurrentMode != 182) {
            ComponentConfigFlash componentFlash = DataRepository.dataItemConfig().getComponentFlash();
            FragmentTopAlert topAlert = getTopAlert();
            if (!componentFlash.isEmpty()) {
                String componentValue = componentFlash.getComponentValue(this.mCurrentMode);
                String str = "1";
                if (str.equals(componentValue) || ComponentConfigFlash.FLASH_VALUE_SCREEN_LIGHT_ON.equals(componentValue)) {
                    alertFlash(0, str, false, z);
                } else {
                    String str2 = "2";
                    if (!str2.equals(componentValue)) {
                        str2 = "5";
                        if (!str2.equals(componentValue)) {
                            if (topAlert != null && !topAlert.isFlashShowing()) {
                                alertFlash(8, str, false, z);
                            }
                        }
                    }
                    alertFlash(0, str2, false, z);
                }
            }
        }
    }

    private void reConfigTipOfHdr(boolean z) {
        ComponentConfigHdr componentHdr = DataRepository.dataItemConfig().getComponentHdr();
        if (!componentHdr.isEmpty()) {
            String componentValue = componentHdr.getComponentValue(this.mCurrentMode);
            if ("on".equals(componentValue) || "normal".equals(componentValue)) {
                alertHDR(0, false, false, z);
            } else if ("live".equals(componentValue)) {
                alertHDR(0, true, false, z);
            } else if (isHDRShowing()) {
                alertHDR(8, false, false, z);
            }
        }
    }

    private void reConfigTipOfMusicHint(boolean z) {
        int i = this.mCurrentMode;
        if (i != 174 && i != 183) {
            alertTopMusicHint(8, null);
        } else if (!isExtraMenuShowing()) {
            String[] currentLiveMusic = CameraSettings.getCurrentLiveMusic();
            if (!currentLiveMusic[1].isEmpty()) {
                alertTopMusicHint(0, currentLiveMusic[1]);
            }
        }
    }

    private void reConfigTipOfSubtitle() {
        alertSubtitleHint(DataRepository.dataItemRunning().getComponentRunningSubtitle().isSwitchOn(this.mCurrentMode) ? 0 : 8, R.string.pref_video_subtitle);
    }

    private void reConfigTipOfSuperNightSe() {
        if (!isExtraMenuShowing()) {
            alertSuperNightSeTip(CameraSettings.isSuperNightOn() ? 0 : 8);
        }
    }

    /* JADX WARNING: type inference failed for: r10v0 */
    /* JADX WARNING: type inference failed for: r13v0 */
    /* JADX WARNING: type inference failed for: r14v0 */
    /* JADX WARNING: type inference failed for: r14v1, types: [int] */
    /* JADX WARNING: type inference failed for: r13v1, types: [int] */
    /* JADX WARNING: type inference failed for: r4v1, types: [int] */
    /* JADX WARNING: type inference failed for: r14v6 */
    /* JADX WARNING: type inference failed for: r13v2 */
    /* JADX WARNING: type inference failed for: r4v2 */
    /* JADX WARNING: type inference failed for: r4v3 */
    /* JADX WARNING: type inference failed for: r13v3 */
    /* JADX WARNING: type inference failed for: r14v7 */
    /* JADX WARNING: type inference failed for: r14v8 */
    /* JADX WARNING: type inference failed for: r13v4 */
    /* JADX WARNING: type inference failed for: r4v4 */
    /* JADX WARNING: type inference failed for: r4v5 */
    /* JADX WARNING: type inference failed for: r14v9, types: [int] */
    /* JADX WARNING: type inference failed for: r13v5 */
    /* JADX WARNING: type inference failed for: r4v6 */
    /* JADX WARNING: type inference failed for: r4v7 */
    /* JADX WARNING: type inference failed for: r3v21 */
    /* JADX WARNING: type inference failed for: r3v22 */
    /* JADX WARNING: type inference failed for: r14v10 */
    /* JADX WARNING: type inference failed for: r7v5 */
    /* JADX WARNING: type inference failed for: r13v6 */
    /* JADX WARNING: type inference failed for: r4v9 */
    /* JADX WARNING: type inference failed for: r14v11, types: [int] */
    /* JADX WARNING: type inference failed for: r7v7, types: [int] */
    /* JADX WARNING: type inference failed for: r14v12 */
    /* JADX WARNING: type inference failed for: r13v7 */
    /* JADX WARNING: type inference failed for: r4v13 */
    /* JADX WARNING: type inference failed for: r14v13 */
    /* JADX WARNING: type inference failed for: r13v8 */
    /* JADX WARNING: type inference failed for: r3v28 */
    /* JADX WARNING: type inference failed for: r4v14 */
    /* JADX WARNING: type inference failed for: r14v14 */
    /* JADX WARNING: type inference failed for: r13v9 */
    /* JADX WARNING: type inference failed for: r3v29 */
    /* JADX WARNING: type inference failed for: r14v15, types: [int] */
    /* JADX WARNING: type inference failed for: r7v11, types: [int] */
    /* JADX WARNING: type inference failed for: r14v16 */
    /* JADX WARNING: type inference failed for: r13v10 */
    /* JADX WARNING: type inference failed for: r4v17 */
    /* JADX WARNING: type inference failed for: r4v18 */
    /* JADX WARNING: type inference failed for: r14v17 */
    /* JADX WARNING: type inference failed for: r13v11 */
    /* JADX WARNING: type inference failed for: r4v20 */
    /* JADX WARNING: type inference failed for: r14v18 */
    /* JADX WARNING: type inference failed for: r13v12 */
    /* JADX WARNING: type inference failed for: r14v19 */
    /* JADX WARNING: type inference failed for: r13v13 */
    /* JADX WARNING: type inference failed for: r4v24 */
    /* JADX WARNING: type inference failed for: r14v20 */
    /* JADX WARNING: type inference failed for: r13v14 */
    /* JADX WARNING: type inference failed for: r14v21 */
    /* JADX WARNING: type inference failed for: r13v15 */
    /* JADX WARNING: type inference failed for: r10v3 */
    /* JADX WARNING: type inference failed for: r4v26 */
    /* JADX WARNING: type inference failed for: r7v15, types: [int[]] */
    /* JADX WARNING: type inference failed for: r14v22 */
    /* JADX WARNING: type inference failed for: r13v16 */
    /* JADX WARNING: type inference failed for: r10v4 */
    /* JADX WARNING: type inference failed for: r10v5 */
    /* JADX WARNING: type inference failed for: r14v23 */
    /* JADX WARNING: type inference failed for: r4v28 */
    /* JADX WARNING: type inference failed for: r13v17 */
    /* JADX WARNING: type inference failed for: r14v24 */
    /* JADX WARNING: type inference failed for: r4v29 */
    /* JADX WARNING: type inference failed for: r14v25 */
    /* JADX WARNING: type inference failed for: r3v42 */
    /* JADX WARNING: type inference failed for: r14v26 */
    /* JADX WARNING: type inference failed for: r13v18 */
    /* JADX WARNING: type inference failed for: r3v43 */
    /* JADX WARNING: type inference failed for: r3v44 */
    /* JADX WARNING: type inference failed for: r13v19 */
    /* JADX WARNING: type inference failed for: r14v27 */
    /* JADX WARNING: type inference failed for: r10v8, types: [int] */
    /* JADX WARNING: type inference failed for: r11v3, types: [int] */
    /* JADX WARNING: type inference failed for: r3v45, types: [int] */
    /* JADX WARNING: type inference failed for: r14v28 */
    /* JADX WARNING: type inference failed for: r13v20 */
    /* JADX WARNING: type inference failed for: r14v29 */
    /* JADX WARNING: type inference failed for: r13v22 */
    /* JADX WARNING: type inference failed for: r4v33 */
    /* JADX WARNING: type inference failed for: r14v30 */
    /* JADX WARNING: type inference failed for: r13v23 */
    /* JADX WARNING: type inference failed for: r4v34 */
    /* JADX WARNING: type inference failed for: r14v31 */
    /* JADX WARNING: type inference failed for: r13v24 */
    /* JADX WARNING: type inference failed for: r3v48 */
    /* JADX WARNING: type inference failed for: r4v35 */
    /* JADX WARNING: type inference failed for: r14v32 */
    /* JADX WARNING: type inference failed for: r13v25 */
    /* JADX WARNING: type inference failed for: r3v50 */
    /* JADX WARNING: type inference failed for: r3v51 */
    /* JADX WARNING: type inference failed for: r13v26 */
    /* JADX WARNING: type inference failed for: r14v33 */
    /* JADX WARNING: type inference failed for: r10v16, types: [int] */
    /* JADX WARNING: type inference failed for: r11v4, types: [int] */
    /* JADX WARNING: type inference failed for: r3v52, types: [int] */
    /* JADX WARNING: type inference failed for: r14v34 */
    /* JADX WARNING: type inference failed for: r13v27 */
    /* JADX WARNING: type inference failed for: r14v35 */
    /* JADX WARNING: type inference failed for: r3v53 */
    /* JADX WARNING: type inference failed for: r4v38 */
    /* JADX WARNING: type inference failed for: r13v28 */
    /* JADX WARNING: type inference failed for: r14v36, types: [int] */
    /* JADX WARNING: type inference failed for: r3v55 */
    /* JADX WARNING: type inference failed for: r3v56, types: [int[]] */
    /* JADX WARNING: type inference failed for: r14v37 */
    /* JADX WARNING: type inference failed for: r13v29 */
    /* JADX WARNING: type inference failed for: r3v57 */
    /* JADX WARNING: type inference failed for: r4v41 */
    /* JADX WARNING: type inference failed for: r14v38 */
    /* JADX WARNING: type inference failed for: r4v43 */
    /* JADX WARNING: type inference failed for: r4v44 */
    /* JADX WARNING: type inference failed for: r4v45 */
    /* JADX WARNING: type inference failed for: r4v46 */
    /* JADX WARNING: type inference failed for: r4v47 */
    /* JADX WARNING: type inference failed for: r7v45, types: [int[]] */
    /* JADX WARNING: type inference failed for: r14v39 */
    /* JADX WARNING: type inference failed for: r13v30 */
    /* JADX WARNING: type inference failed for: r10v21 */
    /* JADX WARNING: type inference failed for: r10v22 */
    /* JADX WARNING: type inference failed for: r14v40 */
    /* JADX WARNING: type inference failed for: r13v31 */
    /* JADX WARNING: type inference failed for: r4v49 */
    /* JADX WARNING: type inference failed for: r14v41, types: [int] */
    /* JADX WARNING: type inference failed for: r3v62, types: [int] */
    /* JADX WARNING: type inference failed for: r4v51 */
    /* JADX WARNING: type inference failed for: r13v32 */
    /* JADX WARNING: type inference failed for: r4v52, types: [int[]] */
    /* JADX WARNING: type inference failed for: r14v42 */
    /* JADX WARNING: type inference failed for: r13v33 */
    /* JADX WARNING: type inference failed for: r4v53 */
    /* JADX WARNING: type inference failed for: r4v54 */
    /* JADX WARNING: type inference failed for: r4v56, types: [int[]] */
    /* JADX WARNING: type inference failed for: r14v43 */
    /* JADX WARNING: type inference failed for: r13v34 */
    /* JADX WARNING: type inference failed for: r4v57 */
    /* JADX WARNING: type inference failed for: r4v58 */
    /* JADX WARNING: type inference failed for: r4v59, types: [int[]] */
    /* JADX WARNING: type inference failed for: r14v44 */
    /* JADX WARNING: type inference failed for: r13v35 */
    /* JADX WARNING: type inference failed for: r14v45, types: [int] */
    /* JADX WARNING: type inference failed for: r13v36, types: [int] */
    /* JADX WARNING: type inference failed for: r4v65, types: [int] */
    /* JADX WARNING: type inference failed for: r4v68 */
    /* JADX WARNING: type inference failed for: r14v46, types: [int] */
    /* JADX WARNING: type inference failed for: r13v37, types: [int] */
    /* JADX WARNING: type inference failed for: r3v68, types: [int] */
    /* JADX WARNING: type inference failed for: r4v71 */
    /* JADX WARNING: type inference failed for: r14v47 */
    /* JADX WARNING: type inference failed for: r4v72 */
    /* JADX WARNING: type inference failed for: r4v73, types: [int[]] */
    /* JADX WARNING: type inference failed for: r4v74 */
    /* JADX WARNING: type inference failed for: r4v75, types: [int[]] */
    /* JADX WARNING: type inference failed for: r4v76 */
    /* JADX WARNING: type inference failed for: r14v48 */
    /* JADX WARNING: type inference failed for: r3v70 */
    /* JADX WARNING: type inference failed for: r4v78 */
    /* JADX WARNING: type inference failed for: r14v49 */
    /* JADX WARNING: type inference failed for: r4v80 */
    /* JADX WARNING: type inference failed for: r4v81 */
    /* JADX WARNING: type inference failed for: r4v82 */
    /* JADX WARNING: type inference failed for: r4v83, types: [int[]] */
    /* JADX WARNING: type inference failed for: r4v84 */
    /* JADX WARNING: type inference failed for: r4v85, types: [int[]] */
    /* JADX WARNING: type inference failed for: r4v86 */
    /* JADX WARNING: type inference failed for: r14v50 */
    /* JADX WARNING: type inference failed for: r13v38 */
    /* JADX WARNING: type inference failed for: r3v72 */
    /* JADX WARNING: type inference failed for: r4v88 */
    /* JADX WARNING: type inference failed for: r14v51 */
    /* JADX WARNING: type inference failed for: r3v74 */
    /* JADX WARNING: type inference failed for: r13v39 */
    /* JADX WARNING: type inference failed for: r3v76 */
    /* JADX WARNING: type inference failed for: r14v52 */
    /* JADX WARNING: type inference failed for: r3v77 */
    /* JADX WARNING: type inference failed for: r3v78, types: [int[]] */
    /* JADX WARNING: type inference failed for: r3v79 */
    /* JADX WARNING: type inference failed for: r3v80, types: [int[]] */
    /* JADX WARNING: type inference failed for: r3v81 */
    /* JADX WARNING: type inference failed for: r3v83 */
    /* JADX WARNING: type inference failed for: r14v53 */
    /* JADX WARNING: type inference failed for: r3v84 */
    /* JADX WARNING: type inference failed for: r3v85, types: [int[]] */
    /* JADX WARNING: type inference failed for: r3v86 */
    /* JADX WARNING: type inference failed for: r3v87, types: [int[]] */
    /* JADX WARNING: type inference failed for: r3v88 */
    /* JADX WARNING: type inference failed for: r14v54, types: [int] */
    /* JADX WARNING: type inference failed for: r3v89 */
    /* JADX WARNING: type inference failed for: r14v55 */
    /* JADX WARNING: type inference failed for: r4v92 */
    /* JADX WARNING: type inference failed for: r13v40 */
    /* JADX WARNING: type inference failed for: r14v56, types: [int] */
    /* JADX WARNING: type inference failed for: r4v96 */
    /* JADX WARNING: type inference failed for: r14v57 */
    /* JADX WARNING: type inference failed for: r4v98 */
    /* JADX WARNING: type inference failed for: r14v58 */
    /* JADX WARNING: type inference failed for: r3v99 */
    /* JADX WARNING: type inference failed for: r14v59 */
    /* JADX WARNING: type inference failed for: r13v41 */
    /* JADX WARNING: type inference failed for: r3v100 */
    /* JADX WARNING: type inference failed for: r4v101, types: [int[]] */
    /* JADX WARNING: type inference failed for: r4v102 */
    /* JADX WARNING: type inference failed for: r14v60 */
    /* JADX WARNING: type inference failed for: r4v105 */
    /* JADX WARNING: type inference failed for: r4v106 */
    /* JADX WARNING: type inference failed for: r4v107 */
    /* JADX WARNING: type inference failed for: r14v61 */
    /* JADX WARNING: type inference failed for: r13v42 */
    /* JADX WARNING: type inference failed for: r4v108 */
    /* JADX WARNING: type inference failed for: r7v64, types: [int[]] */
    /* JADX WARNING: type inference failed for: r14v62 */
    /* JADX WARNING: type inference failed for: r13v43 */
    /* JADX WARNING: type inference failed for: r3v106 */
    /* JADX WARNING: type inference failed for: r3v107 */
    /* JADX WARNING: type inference failed for: r3v108 */
    /* JADX WARNING: type inference failed for: r3v110 */
    /* JADX WARNING: type inference failed for: r14v63 */
    /* JADX WARNING: type inference failed for: r3v111 */
    /* JADX WARNING: type inference failed for: r3v112, types: [int[]] */
    /* JADX WARNING: type inference failed for: r3v113 */
    /* JADX WARNING: type inference failed for: r3v114, types: [int[]] */
    /* JADX WARNING: type inference failed for: r3v115 */
    /* JADX WARNING: type inference failed for: r4v111, types: [int[]] */
    /* JADX WARNING: type inference failed for: r14v64 */
    /* JADX WARNING: type inference failed for: r13v44 */
    /* JADX WARNING: type inference failed for: r4v112 */
    /* JADX WARNING: type inference failed for: r4v113 */
    /* JADX WARNING: type inference failed for: r4v117 */
    /* JADX WARNING: type inference failed for: r7v66, types: [int[]] */
    /* JADX WARNING: type inference failed for: r14v65 */
    /* JADX WARNING: type inference failed for: r4v118 */
    /* JADX WARNING: type inference failed for: r4v119 */
    /* JADX WARNING: type inference failed for: r4v123 */
    /* JADX WARNING: type inference failed for: r14v66 */
    /* JADX WARNING: type inference failed for: r13v45 */
    /* JADX WARNING: type inference failed for: r4v124 */
    /* JADX WARNING: type inference failed for: r4v125 */
    /* JADX WARNING: type inference failed for: r4v126 */
    /* JADX WARNING: type inference failed for: r14v67 */
    /* JADX WARNING: type inference failed for: r13v46 */
    /* JADX WARNING: type inference failed for: r13v47 */
    /* JADX WARNING: type inference failed for: r14v68 */
    /* JADX WARNING: type inference failed for: r14v69 */
    /* JADX WARNING: type inference failed for: r13v48 */
    /* JADX WARNING: type inference failed for: r4v131 */
    /* JADX WARNING: type inference failed for: r14v70 */
    /* JADX WARNING: type inference failed for: r13v49 */
    /* JADX WARNING: type inference failed for: r4v132 */
    /* JADX WARNING: type inference failed for: r4v133 */
    /* JADX WARNING: type inference failed for: r14v71 */
    /* JADX WARNING: type inference failed for: r13v50 */
    /* JADX WARNING: type inference failed for: r4v134 */
    /* JADX WARNING: type inference failed for: r4v135 */
    /* JADX WARNING: type inference failed for: r3v131 */
    /* JADX WARNING: type inference failed for: r3v132 */
    /* JADX WARNING: type inference failed for: r14v72 */
    /* JADX WARNING: type inference failed for: r14v73 */
    /* JADX WARNING: type inference failed for: r7v68 */
    /* JADX WARNING: type inference failed for: r14v74 */
    /* JADX WARNING: type inference failed for: r13v51 */
    /* JADX WARNING: type inference failed for: r4v136 */
    /* JADX WARNING: type inference failed for: r14v75 */
    /* JADX WARNING: type inference failed for: r13v52 */
    /* JADX WARNING: type inference failed for: r14v76 */
    /* JADX WARNING: type inference failed for: r13v53 */
    /* JADX WARNING: type inference failed for: r3v133 */
    /* JADX WARNING: type inference failed for: r14v77 */
    /* JADX WARNING: type inference failed for: r7v69 */
    /* JADX WARNING: type inference failed for: r14v78 */
    /* JADX WARNING: type inference failed for: r13v54 */
    /* JADX WARNING: type inference failed for: r4v137 */
    /* JADX WARNING: type inference failed for: r4v138 */
    /* JADX WARNING: type inference failed for: r14v79 */
    /* JADX WARNING: type inference failed for: r13v55 */
    /* JADX WARNING: type inference failed for: r14v80 */
    /* JADX WARNING: type inference failed for: r13v56 */
    /* JADX WARNING: type inference failed for: r14v81 */
    /* JADX WARNING: type inference failed for: r13v57 */
    /* JADX WARNING: type inference failed for: r14v82 */
    /* JADX WARNING: type inference failed for: r13v58 */
    /* JADX WARNING: type inference failed for: r14v83 */
    /* JADX WARNING: type inference failed for: r13v59 */
    /* JADX WARNING: type inference failed for: r10v31 */
    /* JADX WARNING: type inference failed for: r14v84 */
    /* JADX WARNING: type inference failed for: r4v139 */
    /* JADX WARNING: type inference failed for: r14v85 */
    /* JADX WARNING: type inference failed for: r14v86 */
    /* JADX WARNING: type inference failed for: r3v134 */
    /* JADX WARNING: type inference failed for: r14v87 */
    /* JADX WARNING: type inference failed for: r13v60 */
    /* JADX WARNING: type inference failed for: r3v135 */
    /* JADX WARNING: type inference failed for: r3v136 */
    /* JADX WARNING: type inference failed for: r14v88 */
    /* JADX WARNING: type inference failed for: r13v61 */
    /* JADX WARNING: type inference failed for: r4v140 */
    /* JADX WARNING: type inference failed for: r14v89 */
    /* JADX WARNING: type inference failed for: r13v62 */
    /* JADX WARNING: type inference failed for: r4v141 */
    /* JADX WARNING: type inference failed for: r14v90 */
    /* JADX WARNING: type inference failed for: r13v63 */
    /* JADX WARNING: type inference failed for: r14v91 */
    /* JADX WARNING: type inference failed for: r13v64 */
    /* JADX WARNING: type inference failed for: r3v137 */
    /* JADX WARNING: type inference failed for: r3v138 */
    /* JADX WARNING: type inference failed for: r14v92 */
    /* JADX WARNING: type inference failed for: r14v93 */
    /* JADX WARNING: type inference failed for: r3v139 */
    /* JADX WARNING: type inference failed for: r14v94 */
    /* JADX WARNING: type inference failed for: r13v65 */
    /* JADX WARNING: type inference failed for: r3v140 */
    /* JADX WARNING: type inference failed for: r4v142 */
    /* JADX WARNING: type inference failed for: r4v143 */
    /* JADX WARNING: type inference failed for: r4v144 */
    /* JADX WARNING: type inference failed for: r4v145 */
    /* JADX WARNING: type inference failed for: r4v146 */
    /* JADX WARNING: type inference failed for: r14v95 */
    /* JADX WARNING: type inference failed for: r13v66 */
    /* JADX WARNING: type inference failed for: r10v32 */
    /* JADX WARNING: type inference failed for: r10v33 */
    /* JADX WARNING: type inference failed for: r14v96 */
    /* JADX WARNING: type inference failed for: r13v67 */
    /* JADX WARNING: type inference failed for: r4v147 */
    /* JADX WARNING: type inference failed for: r14v97 */
    /* JADX WARNING: type inference failed for: r14v98 */
    /* JADX WARNING: type inference failed for: r13v68 */
    /* JADX WARNING: type inference failed for: r4v148 */
    /* JADX WARNING: type inference failed for: r4v149 */
    /* JADX WARNING: type inference failed for: r14v99 */
    /* JADX WARNING: type inference failed for: r13v69 */
    /* JADX WARNING: type inference failed for: r4v150 */
    /* JADX WARNING: type inference failed for: r4v151 */
    /* JADX WARNING: type inference failed for: r14v100 */
    /* JADX WARNING: type inference failed for: r13v70 */
    /* JADX WARNING: type inference failed for: r14v101 */
    /* JADX WARNING: type inference failed for: r13v71 */
    /* JADX WARNING: type inference failed for: r4v152 */
    /* JADX WARNING: type inference failed for: r14v102 */
    /* JADX WARNING: type inference failed for: r13v72 */
    /* JADX WARNING: type inference failed for: r3v141 */
    /* JADX WARNING: type inference failed for: r4v153 */
    /* JADX WARNING: type inference failed for: r4v154 */
    /* JADX WARNING: type inference failed for: r4v155 */
    /* JADX WARNING: type inference failed for: r14v103 */
    /* JADX WARNING: type inference failed for: r3v142 */
    /* JADX WARNING: type inference failed for: r4v156 */
    /* JADX WARNING: type inference failed for: r4v157 */
    /* JADX WARNING: type inference failed for: r4v158 */
    /* JADX WARNING: type inference failed for: r4v159 */
    /* JADX WARNING: type inference failed for: r4v160 */
    /* JADX WARNING: type inference failed for: r14v104 */
    /* JADX WARNING: type inference failed for: r13v73 */
    /* JADX WARNING: type inference failed for: r14v105 */
    /* JADX WARNING: type inference failed for: r3v143 */
    /* JADX WARNING: type inference failed for: r3v144 */
    /* JADX WARNING: type inference failed for: r3v145 */
    /* JADX WARNING: type inference failed for: r3v146 */
    /* JADX WARNING: type inference failed for: r3v147 */
    /* JADX WARNING: type inference failed for: r3v148 */
    /* JADX WARNING: type inference failed for: r14v106 */
    /* JADX WARNING: type inference failed for: r3v149 */
    /* JADX WARNING: type inference failed for: r14v107 */
    /* JADX WARNING: type inference failed for: r4v161 */
    /* JADX WARNING: type inference failed for: r14v108 */
    /* JADX WARNING: type inference failed for: r4v162 */
    /* JADX WARNING: type inference failed for: r14v109 */
    /* JADX WARNING: type inference failed for: r4v163 */
    /* JADX WARNING: type inference failed for: r14v110 */
    /* JADX WARNING: type inference failed for: r3v150 */
    /* JADX WARNING: type inference failed for: r14v111 */
    /* JADX WARNING: type inference failed for: r13v74 */
    /* JADX WARNING: type inference failed for: r3v151 */
    /* JADX WARNING: type inference failed for: r4v164 */
    /* JADX WARNING: type inference failed for: r4v165 */
    /* JADX WARNING: type inference failed for: r14v112 */
    /* JADX WARNING: type inference failed for: r13v75 */
    /* JADX WARNING: type inference failed for: r3v152 */
    /* JADX WARNING: type inference failed for: r3v153 */
    /* JADX WARNING: type inference failed for: r3v154 */
    /* JADX WARNING: type inference failed for: r3v155 */
    /* JADX WARNING: type inference failed for: r3v156 */
    /* JADX WARNING: type inference failed for: r3v157 */
    /* JADX WARNING: type inference failed for: r14v113 */
    /* JADX WARNING: type inference failed for: r13v76 */
    /* JADX WARNING: type inference failed for: r4v166 */
    /* JADX WARNING: type inference failed for: r4v167 */
    /* JADX WARNING: type inference failed for: r4v168 */
    /* JADX WARNING: type inference failed for: r14v114 */
    /* JADX WARNING: type inference failed for: r4v169 */
    /* JADX WARNING: type inference failed for: r4v170 */
    /* JADX WARNING: type inference failed for: r13v77 */
    /* JADX WARNING: type inference failed for: r4v171 */
    /* JADX WARNING: type inference failed for: r4v172 */
    /* JADX WARNING: type inference failed for: r4v173 */
    /* JADX WARNING: type inference failed for: r14v115 */
    /* JADX WARNING: type inference failed for: r13v78 */
    /* JADX WARNING: Code restructure failed: missing block: B:121:0x02f9, code lost:
        r3 = true;
        r10 = true;
        r14 = r14;
        r13 = r13;
        r4 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:137:0x0350, code lost:
        r4 = r3;
        r7 = null;
        r3 = false;
        r13 = 0;
        r14 = r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:154:0x03ae, code lost:
        r4 = r3;
        r3 = r7;
        r14 = r14;
        r13 = r13;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:155:0x03b0, code lost:
        r10 = true;
        r14 = r14;
        r13 = r13;
        r4 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:156:0x03b1, code lost:
        r7 = null;
        r14 = r14;
        r13 = r13;
        r4 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:180:0x0426, code lost:
        r7 = null;
        r3 = false;
        r4 = 0;
        r14 = r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:181:0x0429, code lost:
        r13 = r4;
        r14 = r14;
        r4 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:188:0x0445, code lost:
        r3 = r4;
        r7 = null;
        r4 = r10;
        r14 = r14;
        r13 = r13;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:190:0x0450, code lost:
        r7 = null;
        r3 = false;
        r4 = 0;
        r14 = r14;
        r13 = r13;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:195:0x046c, code lost:
        r7 = r4;
        r4 = 0;
        r14 = r14;
        r13 = r13;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:204:0x04b2, code lost:
        r4 = r3;
        r7 = null;
        r14 = r14;
        r13 = r13;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:205:0x04b4, code lost:
        r3 = false;
        r14 = r14;
        r13 = r13;
        r4 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:209:0x04d6, code lost:
        r3 = !r3;
        r13 = 0;
        r4 = r7;
        r14 = r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:219:0x04f9, code lost:
        r7 = null;
        r14 = r14;
        r13 = r13;
        r4 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:220:0x04fb, code lost:
        r7 = null;
        r3 = false;
        r4 = 0;
        r13 = 0;
        r14 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:221:0x0500, code lost:
        r10 = true;
        r14 = r14;
        r13 = r13;
        r4 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:222:0x0501, code lost:
        if (r14 <= 0) goto L_0x05c6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:223:0x0503, code lost:
        r11 = getResources().getDrawable(r14);
        r1.margin = getInitialMargin(r1, r2, r11.getIntrinsicWidth());
        r12 = (android.widget.FrameLayout.LayoutParams) r20.getLayoutParams();
        r14 = r1.margin;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:224:0x051d, code lost:
        if (r14 <= 0) goto L_0x0532;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:225:0x051f, code lost:
        r12.gravity |= androidx.core.view.GravityCompat.START;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:226:0x052a, code lost:
        if (r0.mIsRTL == false) goto L_0x052f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:227:0x052c, code lost:
        r12.rightMargin = r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:228:0x052f, code lost:
        r12.leftMargin = r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:229:0x0532, code lost:
        r12.setMarginStart(0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:230:0x0535, code lost:
        r12.gravity |= 16;
        r2.setLayoutParams(r12);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:231:0x0542, code lost:
        if (r1.configItem != 177) goto L_0x0548;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:232:0x0544, code lost:
        r2.setImageDrawable(null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:233:0x0548, code lost:
        r2.setImageDrawable(r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:234:0x054b, code lost:
        if (r13 <= 0) goto L_0x0551;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:235:0x054d, code lost:
        r2.setBackgroundResource(r13);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:236:0x0551, code lost:
        r2.setBackground(null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:237:0x0554, code lost:
        if (r10 == false) goto L_0x0562;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:238:0x0556, code lost:
        if (r3 == false) goto L_0x055d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:239:0x0558, code lost:
        r3 = com.android.camera.customization.TintColor.tintColor();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:240:0x055d, code lost:
        r3 = -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:241:0x055e, code lost:
        r2.setColorFilter(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:242:0x0562, code lost:
        r2.setColorFilter(0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:244:0x0567, code lost:
        if (r1.configItem != 193) goto L_0x0582;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:246:0x056b, code lost:
        if (r1.enable != false) goto L_0x0574;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:247:0x056d, code lost:
        r3 = 0.4f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:248:0x0570, code lost:
        r2.setAlpha(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:250:0x057d, code lost:
        if (r20.getAlpha() != 0.4f) goto L_0x0582;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:251:0x057f, code lost:
        r3 = 1.0f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:252:0x0582, code lost:
        r5 = 90.0f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:253:0x0588, code lost:
        if (isBothLandscapeMode() == false) goto L_0x059b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:255:0x058e, code lost:
        if (r1.configItem != 217) goto L_0x0591;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:257:0x0595, code lost:
        if (r0.mDegree >= 180) goto L_0x0598;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:258:0x0598, code lost:
        r5 = 270.0f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:260:0x059f, code lost:
        if (isLeftLandscapeMode() == false) goto L_0x05a5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:261:0x05a1, code lost:
        r2.setRotation(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:263:0x05a9, code lost:
        if (r1.configItem != 169) goto L_0x05ad;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:264:0x05ab, code lost:
        r1 = 0.0f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:265:0x05ad, code lost:
        r1 = (float) r0.mDegree;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:266:0x05b0, code lost:
        r2.setRotation(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:267:0x05b3, code lost:
        if (r4 <= 0) goto L_0x05bd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:268:0x05b5, code lost:
        r2.setContentDescription(getString(r4));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:270:0x05c1, code lost:
        if (android.text.TextUtils.isEmpty(r7) != false) goto L_0x05c6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:271:0x05c3, code lost:
        r2.setContentDescription(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:272:0x05c6, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x010d, code lost:
        if (com.android.camera.CameraSettings.getCurrentLiveMusic()[1].isEmpty() == false) goto L_0x02f9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x01a7, code lost:
        r7 = null;
        r13 = 0;
        r14 = r14;
        r4 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x01e8, code lost:
        r7 = null;
        r13 = 0;
        r14 = r14;
        r3 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x01ea, code lost:
        r10 = true;
        r17 = r4;
        r4 = r3;
        r3 = r17;
        r14 = r14;
        r13 = r13;
     */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r14v6
  assigns: []
  uses: []
  mth insns count: 665
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
    /* JADX WARNING: Unknown variable types count: 184 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @SuppressLint({"ResourceType"})
    private boolean setTopImageResource(TopConfigItem topConfigItem, ImageView imageView, int i, DataItemConfig dataItemConfig, boolean z) {
        ? r14;
        ? r13;
        boolean z2;
        CharSequence charSequence;
        ? r4;
        boolean z3;
        ? r142;
        ? r132;
        ? r42;
        boolean isGifOn;
        ? r143;
        ? r7;
        boolean z4;
        ? r133;
        ? r3;
        ? r43;
        CharSequence charSequence2;
        ? r144;
        ? r134;
        String str;
        boolean ai108Running;
        ? r145;
        ? r135;
        ? r146;
        ? r136;
        boolean z5;
        ? r147;
        ? r137;
        boolean z6;
        ? r32;
        boolean z7;
        ? r138;
        ? r148;
        ? r149;
        ? r139;
        ? r33;
        ? r1410;
        ? r1310;
        ? r34;
        ? r1411;
        ? r35;
        ? r1412;
        ? r1413;
        boolean z8;
        ? r1414;
        ? r36;
        ? r44;
        TopConfigItem topConfigItem2 = topConfigItem;
        ImageView imageView2 = imageView;
        int i2 = i;
        int i3 = topConfigItem2.configItem;
        String str2 = ", ";
        ? r10 = 2131755040;
        String str3 = "normal";
        String str4 = TAG;
        ? r1311 = 2131231211;
        ? r1415 = 2131231210;
        String str5 = "on";
        switch (i3) {
            case 162:
                isGifOn = CameraSettings.isGifOn();
                r42 = isGifOn ? 2131755140 : 2131755139;
                r142 = this.mGifResource;
                r132 = 2131231636;
                break;
            case 164:
                if (i2 != 204) {
                    r1311 = r1311;
                    r1415 = r1415;
                    r3 = 2131755080;
                    break;
                } else {
                    r3 = 2131755669;
                    break;
                }
            case 165:
                ComponentRunningEisPro componentRunningEisPro = DataRepository.dataItemRunning().getComponentRunningEisPro();
                if (!componentRunningEisPro.isEmpty()) {
                    ? valueSelectedDrawableIgnoreClose = componentRunningEisPro.getValueSelectedDrawableIgnoreClose(i2);
                    ? valueSelectedStringIdIgnoreClose = componentRunningEisPro.getValueSelectedStringIdIgnoreClose(i2);
                    z4 = TextUtils.equals("off", componentRunningEisPro.getComponentValue(i2));
                    r143 = valueSelectedDrawableIgnoreClose;
                    r7 = valueSelectedStringIdIgnoreClose;
                    break;
                }
                break;
            case 166:
                r1415 = 2131231100;
                r1311 = 2131231101;
                r3 = 2131755264;
                break;
            case 168:
                ComponentRunningAiAudio componentRunningAiAudio = DataRepository.dataItemRunning().getComponentRunningAiAudio();
                if (!componentRunningAiAudio.isEmpty()) {
                    ? valueSelectedDrawableIgnoreClose2 = componentRunningAiAudio.getValueSelectedDrawableIgnoreClose(i2);
                    ? valueSelectedStringIdIgnoreClose2 = componentRunningAiAudio.getValueSelectedStringIdIgnoreClose(i2);
                    z4 = TextUtils.equals(str3, componentRunningAiAudio.getComponentValue(i2));
                    r143 = valueSelectedDrawableIgnoreClose2;
                    r7 = valueSelectedStringIdIgnoreClose2;
                    break;
                }
                break;
            case 169:
                r142 = 2131231497;
                r132 = 2131231498;
                isGifOn = CameraSettings.isPanoramaVertical(getContext());
                if (!isGifOn) {
                    r142 = r142;
                    r132 = r132;
                    r42 = 2131755123;
                    break;
                } else {
                    r42 = 2131755124;
                    break;
                }
            case 171:
                ai108Running = DataRepository.dataItemRunning().getAi108Running();
                r144 = 2131231557;
                r134 = 2131231558;
                String[] strArr = this.mUltraPixel108PhotographyTipString;
                if (!ai108Running) {
                    str = strArr[0];
                    r144 = r144;
                    r134 = r134;
                    break;
                } else {
                    str = strArr[1];
                    break;
                }
            case 172:
                r145 = 2131231657;
                r135 = 2131231661;
                break;
            case 175:
                z5 = CameraSettings.isAiEnhancedVideoEnabled(i);
                ? r72 = this.mAiSceneResources;
                r146 = r72[0];
                r136 = r72[1];
                if (CameraSettings.getAiSceneOpen(i)) {
                    ? r102 = 2131755041;
                }
                if (!z) {
                    reConfigTipOfAiEnhancedVideo();
                    break;
                }
                break;
            case 176:
                return false;
            case 177:
                ? r1416 = 2131231449;
                break;
            case 179:
                r3 = 2131755081;
                break;
            case 193:
                ComponentConfigFlash componentFlash = dataItemConfig.getComponentFlash();
                if (!componentFlash.isEmpty()) {
                    if (!"1".equals(componentFlash.getComponentValue(i2))) {
                        if (!"2".equals(componentFlash.getComponentValue(i2))) {
                            if (!ComponentConfigFlash.FLASH_VALUE_SCREEN_LIGHT_ON.equals(componentFlash.getComponentValue(i2))) {
                                if (!"5".equals(componentFlash.getComponentValue(i2))) {
                                    z7 = false;
                                    ? valueSelectedDrawableIgnoreClose3 = componentFlash.getValueSelectedDrawableIgnoreClose(i2);
                                    ? valueSelectedShadowDrawable = componentFlash.getValueSelectedShadowDrawable(i2);
                                    ? valueSelectedStringIdIgnoreClose3 = componentFlash.getValueSelectedStringIdIgnoreClose(i2);
                                    if (!z || (z && this.mCurrentMode == 167)) {
                                        reConfigTipOfFlash(true);
                                    }
                                    r148 = valueSelectedDrawableIgnoreClose3;
                                    r138 = valueSelectedShadowDrawable;
                                    r32 = valueSelectedStringIdIgnoreClose3;
                                }
                            }
                        }
                    }
                    z7 = true;
                    ? valueSelectedDrawableIgnoreClose32 = componentFlash.getValueSelectedDrawableIgnoreClose(i2);
                    ? valueSelectedShadowDrawable2 = componentFlash.getValueSelectedShadowDrawable(i2);
                    ? valueSelectedStringIdIgnoreClose32 = componentFlash.getValueSelectedStringIdIgnoreClose(i2);
                    reConfigTipOfFlash(true);
                    r148 = valueSelectedDrawableIgnoreClose32;
                    r138 = valueSelectedShadowDrawable2;
                    r32 = valueSelectedStringIdIgnoreClose32;
                } else {
                    r32 = 0;
                    z7 = false;
                    r138 = 0;
                    r148 = 0;
                }
                topConfigItem2.enable = !componentFlash.isDisabled(this.mCurrentMode);
                r149 = r147;
                r139 = r137;
                r33 = r32;
                break;
            case 194:
                ComponentConfigHdr componentHdr = dataItemConfig.getComponentHdr();
                if (!componentHdr.isEmpty()) {
                    TopConfigProtocol topConfigProtocol = (TopConfigProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(193);
                    if (!str5.equals(componentHdr.getComponentValue(i2)) && !str3.equals(componentHdr.getComponentValue(i2))) {
                        if (!"auto".equals(componentHdr.getComponentValue(i2)) || topConfigProtocol == null || !topConfigProtocol.getAutoHDRTargetState()) {
                            z6 = false;
                            r147 = componentHdr.getValueSelectedDrawableIgnoreClose(i2);
                            r137 = componentHdr.getValueSelectedShadowDrawable(i2);
                            r34 = componentHdr.getValueSelectedStringIdIgnoreClose(i2);
                        }
                    }
                    z6 = true;
                    r147 = componentHdr.getValueSelectedDrawableIgnoreClose(i2);
                    r137 = componentHdr.getValueSelectedShadowDrawable(i2);
                    r34 = componentHdr.getValueSelectedStringIdIgnoreClose(i2);
                } else {
                    r34 = 0;
                    z6 = false;
                    r137 = 0;
                    r147 = 0;
                }
                Log.c(str4, "setTopImageResource：refresh HDR ui");
                r149 = r1410;
                r139 = r1310;
                r33 = r34;
                break;
            case 195:
                r1411 = getPortraitResources();
                r35 = 2131755167;
                break;
            case 197:
                ? r37 = this.mMoreResources;
                r1415 = r37[0];
                r1311 = r37[1];
                r3 = 2131755146;
                break;
            case 200:
                String componentValue = dataItemConfig.getComponentBokeh().getComponentValue(i2);
                r1412 = str5.equals(componentValue) ? 2131231523 : 2131231522;
                ? r45 = str5.equals(componentValue) ? 2131755054 : 2131755053;
                z3 = str5.equals(componentValue);
                r44 = r45;
                break;
            case 201:
                z5 = CameraSettings.getAiSceneOpen(i);
                ? r73 = this.mAiSceneResources;
                ? r1417 = r73[0];
                ? r1312 = r73[1];
                if (CameraSettings.getAiSceneOpen(i)) {
                    r10 = 2131755041;
                }
                configBottomPopupTips(false);
                r146 = r1417;
                r136 = r1312;
                ? r103 = r10;
                break;
            case 205:
                ComponentConfigUltraWide componentConfigUltraWide = dataItemConfig.getComponentConfigUltraWide();
                if (!componentConfigUltraWide.isEmpty()) {
                    ? r1418 = componentConfigUltraWide.getValueSelectedDrawableIgnoreClose(i2);
                    r43 = componentConfigUltraWide.getValueSelectedStringIdIgnoreClose(i2);
                    charSequence2 = null;
                    r133 = 0;
                    r1413 = r1418;
                    break;
                }
                break;
            case 206:
                isGifOn = CameraSettings.isLiveShotOn();
                ? r46 = this.mLiveShotResource;
                r142 = r46[0];
                r132 = r46[1];
                if (!isGifOn) {
                    r142 = r142;
                    r132 = r132;
                    r42 = 2131755057;
                    break;
                } else {
                    r42 = 2131755058;
                    break;
                }
            case 207:
                isGifOn = DataRepository.dataItemRunning().isSwitchOn("pref_ultra_wide_bokeh_enabled");
                ? r47 = this.mUltraWideBokehResources;
                r142 = r47[0];
                r132 = r47[1];
                if (!isGifOn) {
                    r142 = r142;
                    r132 = r132;
                    r42 = 2131755065;
                    break;
                } else {
                    r42 = 2131755066;
                    break;
                }
            case 209:
                ai108Running = CameraSettings.isUltraPixelOn();
                ? r48 = this.mUltraPixelPhotographyIconResources;
                r144 = r48[0];
                r134 = r48[1];
                String[] strArr2 = this.mUltraPixelPhotographyTipString;
                if (!ai108Running) {
                    str = strArr2[0];
                    r144 = r144;
                    r134 = r134;
                    break;
                } else {
                    str = strArr2[1];
                    break;
                }
            case 212:
                ComponentRunningShine componentRunningShine = DataRepository.dataItemRunning().getComponentRunningShine();
                ? topConfigEntryRes = componentRunningShine.getTopConfigEntryRes(i2);
                ? topConfigEntryShadowRes = componentRunningShine.getTopConfigEntryShadowRes(topConfigEntryRes);
                z2 = !componentRunningShine.isTopFilterEntry();
                z3 = componentRunningShine.determineStatus(i2);
                if (componentRunningShine.isTopBeautyEntry()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(getString(R.string.video_beauty_tip));
                    sb.append(str2);
                    sb.append(getString(z3 ? R.string.accessibility_open : R.string.accessibility_closed));
                    charSequence = sb.toString();
                    r4 = 0;
                    r14 = topConfigEntryRes;
                    r13 = topConfigEntryShadowRes;
                    break;
                } else {
                    ? r49 = componentRunningShine.getTopConfigEntryDesRes();
                }
            case 214:
                ComponentConfigMeter componentConfigMeter = dataItemConfig.getComponentConfigMeter();
                if (!componentConfigMeter.isEmpty()) {
                    r1415 = componentConfigMeter.getValueSelectedDrawableIgnoreClose(i2);
                    r1311 = componentConfigMeter.getValueSelectedShadowDrawableId(i2);
                    r3 = componentConfigMeter.getValueSelectedStringIdIgnoreClose(i2);
                    break;
                }
                break;
            case 215:
                z3 = CameraSettings.isUltraPixelPortraitFrontOn();
                r1412 = CameraSettings.isUltraPixelPortraitFrontOn() ? this.mUltraPixelPortraitResources[1] : this.mUltraPixelPortraitResources[0];
                r44 = 2131756834;
                break;
            case 217:
                r1411 = 2131231145;
                r35 = 2131755868;
                break;
            case 218:
                r1412 = CameraSettings.isSuperEISEnabled(i) ? this.mSuperEISResources[1] : this.mSuperEISResources[0];
                ? r410 = CameraSettings.isSuperEISEnabled(i) ? 2131755187 : 2131755186;
                z3 = CameraSettings.isSuperEISEnabled(i);
                r44 = r410;
                break;
            case 220:
                z8 = CameraSettings.isSubtitleEnabled(i);
                r1414 = CameraSettings.isSubtitleEnabled(i) ? this.mAutoZoomResources[1] : this.mAutoZoomResources[0];
                r36 = 2131756693;
                if (!z) {
                    reConfigTipOfSubtitle();
                    break;
                }
                break;
            case 221:
                z8 = CameraSettings.isDocumentModeOn(i);
                r1414 = CameraSettings.isDocumentModeOn(i) ? this.mDocumentResources[1] : this.mDocumentResources[0];
                r36 = 2131756565;
                break;
            case 225:
                r1411 = getSettingResources();
                r35 = 2131755175;
                break;
            case 227:
                ComponentRunningColorEnhance componentRunningColorEnhance = DataRepository.dataItemRunning().getComponentRunningColorEnhance();
                ? resIcon = componentRunningColorEnhance.getResIcon(componentRunningColorEnhance.isEnabled(this.mCurrentMode));
                z3 = componentRunningColorEnhance.isEnabled(this.mCurrentMode);
                r1412 = resIcon;
                r44 = 2131755579;
                break;
            case 239:
                z3 = i2 != 174 ? CameraSettings.isFaceBeautyOn(this.mCurrentMode, null) : CameraSettings.isLiveBeautyOpen();
                r1412 = 2131231149;
                r44 = 2131755048;
                break;
            case 242:
                if (!Util.isGlobalVersion()) {
                    r1411 = 2131231200;
                    r35 = 2131756130;
                    break;
                } else {
                    r1415 = 2131231202;
                    r1311 = 2131231203;
                    r3 = 2131756583;
                    break;
                }
            case 243:
                boolean z9 = CameraSettings.isVideoBokehOn();
                StringBuilder sb2 = new StringBuilder();
                sb2.append("setTopImageResource: VIDEO_BOKEH isSwitchOn = ");
                sb2.append(z9);
                Log.d(str4, sb2.toString());
                ? r411 = this.mVideoBokehResource;
                ? r1419 = z9 ? r411[1] : r411[0];
                StringBuilder sb3 = new StringBuilder();
                sb3.append(getString(R.string.pref_camera_video_bokeh_on));
                sb3.append(str2);
                sb3.append(getString(z9 ? R.string.accessibility_open : R.string.accessibility_closed));
                CharSequence charSequence3 = sb3.toString();
                ? r412 = 0;
                break;
            case 245:
                r1413 = 2131231617;
                r133 = 2131231618;
                r43 = 2131755820;
                charSequence2 = null;
                break;
            case 251:
                z8 = CameraSettings.isCinematicAspectRatioEnabled(i);
                ? r74 = this.mCinematicRatioResources;
                ? r1420 = r74[0];
                ? r1313 = r74[1];
                charSequence = null;
                ? r1421 = r1420;
                ? r1314 = r1313;
                ? r38 = CameraSettings.getAiSceneOpen(i) ? 2131755144 : 2131755143;
                break;
            case 253:
                z8 = CameraSettings.isAutoZoomEnabled(i);
                r1414 = CameraSettings.isAutoZoomEnabled(i) ? this.mAutoZoomResources[1] : this.mAutoZoomResources[0];
                r36 = 2131755336;
                break;
            case 255:
                isGifOn = CameraSettings.isMacroModeEnabled(i);
                ? r413 = this.mSuperMacroResources;
                r142 = r413[0];
                r132 = r413[1];
                if (!isGifOn) {
                    r142 = r142;
                    r132 = r132;
                    r42 = 2131755189;
                    break;
                } else {
                    r42 = 2131755190;
                    break;
                }
            case 256:
                String componentValue2 = DataRepository.dataItemConfig().getComponentConfigVideoQuality().getComponentValue(this.mCurrentMode);
                z3 = CameraSettings.isVideoQuality8KOpen(this.mCurrentMode) || "3001".equalsIgnoreCase(componentValue2) || ComponentConfigVideoQuality.QUALITY_8K_24FPS.equalsIgnoreCase(componentValue2);
                r44 = z3 ? 2131755037 : 2131755036;
                r1412 = this.mVideo8KResource[0];
                break;
            case 263:
                z3 = CameraSettings.isMasterFilterOn(this.mCurrentMode);
                r14 = z3 ? 2131231482 : 2131231480;
                charSequence = null;
                z2 = false;
                r13 = 2131231481;
                r4 = 2131755098;
                break;
            case 512:
                break;
            case 513:
                StandaloneRecorderProtocol standaloneRecorderProtocol = (StandaloneRecorderProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(429);
                if (!CameraSettings.getDualVideoConfig().ismDrawSelectWindow() && standaloneRecorderProtocol != null && standaloneRecorderProtocol.getRecorderManager(null) != null && standaloneRecorderProtocol.getRecorderManager(null).isRecordingPaused()) {
                    r145 = 2131231273;
                    r135 = 2131231274;
                    break;
                } else {
                    imageView2.setImageDrawable(null);
                    imageView2.setBackground(null);
                    return true;
                }
                break;
        }
    }

    private void setTopTipMarin(View view, boolean z) {
        long j;
        double windowWidth;
        double d;
        int topHeight = Display.getTopHeight();
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.capture_delay_number_text_size);
        int round = Math.round(((float) dimensionPixelSize) * 1.327f);
        int i = round - dimensionPixelSize;
        if (this.mCurrentMode == 165) {
            j = (long) topHeight;
            windowWidth = (double) Display.getWindowWidth();
            d = 0.0148d;
        } else if (this.mDisplayRectTopMargin == 0) {
            j = (long) topHeight;
            windowWidth = (double) Display.getWindowWidth();
            d = 0.0574d;
        } else {
            j = (long) topHeight;
            windowWidth = (double) Display.getWindowWidth();
            d = 0.0889d;
        }
        int round2 = ((int) (j + Math.round(windowWidth * d))) - i;
        StringBuilder sb = new StringBuilder();
        sb.append("showDelayNumber: topMargin = ");
        sb.append(round2);
        sb.append(", topHeight = ");
        sb.append(Display.getTopHeight());
        sb.append(", fontHeight = ");
        sb.append(dimensionPixelSize);
        sb.append(", viewHeight = ");
        sb.append(round);
        sb.append(", offset = ");
        sb.append(i);
        Log.d(TAG, sb.toString());
        ((MarginLayoutParams) view.getLayoutParams()).topMargin = round2 + (z ? getResources().getDimensionPixelSize(R.dimen.auto_hibernation_capture_delay_number_offset) : 0);
        int i2 = this.mDegree;
        if (i2 > 0) {
            ViewCompat.setRotation(view, (float) i2);
        }
    }

    private void showExtraMenu() {
        Log.u(TAG, "config showExtraMenu");
        hideSwitchTip();
        hideAlert();
        this.mIsShowExtraMenu = true;
        initExtraMenu();
        animatorExtraMenu(true);
        notifyExtraMenuVisibilityChange(true);
    }

    private void showExtraTimerBurstMenu() {
        Log.u(TAG, "showExtraTimerBurstMenu");
        this.mIsShowExtraMenu = true;
        switchExtraTimerBurstMenu();
    }

    private void showTips(ConfigChanges configChanges, boolean z) {
        String str = FragmentTopAlert.TIP_ULTRA_PIXEL;
        if (getTipsState(str)) {
            setTipsState(str, false);
            alertSwitchTip(str, 0, DataRepository.dataItemRunning().getComponentUltraPixel().getUltraPixelOpenTip());
        }
        configChanges.reCheckModuleUltraPixel();
        String str2 = FragmentTopAlert.TIP_VIDEO_BEAUTIFY;
        if (getTipsState(str2)) {
            setTipsState(str2, false);
            configChanges.reCheckVideoBeautify();
        }
        String str3 = "ai_watermark";
        boolean z2 = true;
        if (getTipsState(str3)) {
            setTipsState(str3, false);
            configChanges.reCheckAIWatermark(true);
        }
        String str4 = FragmentTopAlert.TIP_HDR;
        if (getTipsState(str4)) {
            setTipsState(str4, false);
            configChanges.reCheckVideoHdr();
        }
        String str5 = FragmentTopAlert.TIP_SUPER_EIS;
        if (getTipsState(str5)) {
            setTipsState(str5, false);
            configChanges.reCheckSuperEIS();
        }
        String str6 = "super_eis_pro";
        if (getTipsState(str6)) {
            setTipsState(str6, false);
            configChanges.reCheckSuperEISPro();
        }
        String str7 = FragmentTopAlert.TIP_LIVE_SHOT;
        if (getTipsState(str7)) {
            setTipsState(str7, false);
            configChanges.reCheckLiveShot();
        }
        if (!isExtraMenuShowing()) {
            configChanges.reCheckColorEnhance();
        }
        String str8 = FragmentTopAlert.TIP_ULTRA_WIDE_BOKEH;
        if (getTipsState(str8)) {
            setTipsState(str8, false);
            configChanges.reCheckUltraWideBokeh();
        }
        String str9 = FragmentTopAlert.TIP_AI_AUDIO;
        if (getTipsState(str9)) {
            setTipsState(str9, false);
            configChanges.reCheckAiAudio();
        }
        if (!isExtraMenuShowing()) {
            configChanges.reCheckEyeLight();
        }
        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
        int i = this.mCurrentMode;
        if (!(i == 162 || i == 169) || cameraAction == null || !cameraAction.isRecording()) {
            z2 = false;
        }
        if (!isExtraMenuShowing() && !z2) {
            setTipsState("macro", false);
            configChanges.reCheckMacroMode();
        }
        if (!isExtraMenuShowing()) {
            setTipsState(FragmentTopAlert.TIP_TIMER_BURST, false);
            configChanges.reCheckTimerBurst();
        }
        if (!isExtraMenuShowing()) {
            configChanges.reCheckFrontBokenTip();
        }
        if (!isExtraMenuShowing()) {
            configChanges.reCheckHandGesture();
        }
        if (!isExtraMenuShowing()) {
            configChanges.reCheckSubtitleMode();
        }
        if (!isExtraMenuShowing()) {
            configChanges.reCheckAiEnhancedVideo();
        }
        CameraModuleSpecial cameraModuleSpecial = (CameraModuleSpecial) ModeCoordinatorImpl.getInstance().getAttachProtocol(195);
        if (isExtraMenuShowing()) {
            return;
        }
        if (cameraModuleSpecial == null || !cameraModuleSpecial.isStartCountCapture()) {
            configChanges.reCheckTilt();
        }
    }

    private void switchExtraTimerBurstMenu() {
        int i;
        TextView textView;
        this.mIsShowExtraTimerMenu = !this.mIsShowExtraTimerMenu;
        if (this.mIsShowExtraTimerMenu) {
            if (this.mTopFloatingMode) {
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mLlTimerMenu.getLayoutParams();
                marginLayoutParams.topMargin = 33;
                marginLayoutParams.width = 1080;
            }
            MistatsWrapper.commonKeyTriggerEvent(FeatureName.VALUE_GOTO_TIMER_BURST_MENU, null, null);
            this.mTopExtraMenu.setVisibility(8);
            this.mLlTimerMenu.setVisibility(0);
            ScaleAnimation scaleAnimation = new ScaleAnimation(0.9f, 1.0f, 0.9f, 1.0f, 1, 0.5f, 1, 1.0f);
            scaleAnimation.setDuration(300);
            scaleAnimation.setInterpolator(new CubicEaseOutInterpolator());
            this.mLlTimerMenu.startAnimation(scaleAnimation);
            int timerBurstTotalCount = CameraSettings.getTimerBurstTotalCount();
            int timerBurstInterval = CameraSettings.getTimerBurstInterval();
            if (this.mCurrentMode == 167) {
                textView = this.mTvShotInterval;
                i = R.string.timer_burst_manu_param_interval;
            } else {
                textView = this.mTvShotInterval;
                i = R.string.timer_burst_param_interval;
            }
            textView.setText(getString(i));
            int i2 = timerBurstInterval;
            this.mCustomSeekBarInterval.initSeekBarConfig(TimerBurstController.timeInterval, i2, 1, "S", DataRepository.dataItemLive().getTimerBurstController());
            this.mCustomSeekBarCount.initSeekBarConfig(TimerBurstController.shotCount, timerBurstTotalCount / 10, 10, "", DataRepository.dataItemLive().getTimerBurstController());
            this.mLayoutCount.setContentDescription(getResources().getQuantityString(R.plurals.accessibility_timer_burst_count, timerBurstTotalCount, new Object[]{Integer.valueOf(timerBurstTotalCount)}));
            this.mLayoutInterval.setContentDescription(getResources().getQuantityString(R.plurals.accessibility_timer_burst_interval, timerBurstInterval, new Object[]{Integer.valueOf(timerBurstInterval)}));
            return;
        }
        this.mTopExtraMenu.setVisibility(0);
        this.mLlTimerMenu.setVisibility(8);
        ScaleAnimation scaleAnimation2 = new ScaleAnimation(1.065f, 1.0f, 1.065f, 1.0f, 1, 0.5f, 1, 1.0f);
        scaleAnimation2.setDuration(300);
        scaleAnimation2.setInterpolator(new CubicEaseOutInterpolator());
        this.mTopExtraMenu.startAnimation(scaleAnimation2);
    }

    public /* synthetic */ void O0000O0o(DataWrap dataWrap) {
        onInstallStateChanged((HashMap) dataWrap.get());
    }

    public /* synthetic */ void O0000o0o(boolean z) {
        FragmentTopAlert topAlert = getTopAlert();
        if (topAlert != null) {
            topAlert.setShow(true);
            reConfigTipOfFlash(z);
            reConfigTipOfMusicHint(z);
            alertUpdateValue(0, 0, null);
            updateLyingDirectHint(false, true);
            if (CameraSettings.isProVideoHistogramOpen(this.mCurrentMode) || CameraSettings.isProPhotoHistogramOpen(this.mCurrentMode)) {
                topAlert.alertHistogram(0);
            }
            if (CameraSettings.isProVideoAudioMapOpen(this.mCurrentMode)) {
                topAlert.alertAudioMap(0);
            }
            TopConfigProtocol topConfigProtocol = (TopConfigProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(193);
            if (topConfigProtocol != null) {
                int i = this.mCurrentMode;
                if (i == 162 || i == 163) {
                    topConfigProtocol.reShowMoon();
                }
            }
            ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
            if (configChanges != null) {
                showTips(configChanges, true);
                configChanges.reCheckVideoUltraClearTip();
                configChanges.reCheckVideoHDR10Tip();
                configChanges.reCheckParameterResetTip(true);
                configChanges.reCheckParameterDescriptionTip();
                configChanges.reCheckVideoLog();
                configChanges.reCheckRaw();
                configChanges.reCheckFastMotion(true);
                configChanges.recheckVideoFps(false);
                configChanges.reCheckDocumentMode();
                configChanges.reCheckDualVideoMode();
                configChanges.reCheckHanGestureDescTip();
                configChanges.reCheckSpeechShutterDescTip();
                configChanges.reCheckAmbilight();
            }
        }
    }

    public /* synthetic */ void O0000oOo(View view) {
        onBackEvent(6);
    }

    public /* synthetic */ void O0000oo(View view) {
        if (isAdded()) {
            view.sendAccessibilityEvent(128);
        }
    }

    public /* synthetic */ void O0000oo0(View view) {
        if (isAdded()) {
            view.sendAccessibilityEvent(128);
        }
    }

    public void addView(View view, ViewGroup.LayoutParams layoutParams) {
        getTopAlert().addView(view, layoutParams);
    }

    public void alertAiAudio(int i, @StringRes int i2) {
        if (Util.isWiredHeadsetOn()) {
            i = 8;
        }
        FragmentTopAlert topAlert = getTopAlert();
        if (topAlert != null) {
            boolean z = true;
            if (!this.mIsShowExtraMenu) {
                z = false;
            }
            topAlert.alertAiAudio(i, i2, z);
        }
    }

    public void alertAiAudioBGHint(int i, int i2) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.alertAiAudioBGHint(i, i2);
        }
    }

    public void alertAiAudioMutexToastIfNeed(Context context, int i) {
        FragmentTopAlert topAlert = getTopAlert();
        if (topAlert != null) {
            topAlert.alertAiAudioMutexToastIfNeed(context, i);
        }
    }

    public void alertAiDetectTipHint(int i, int i2, long j) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.alertRecommendTipHint(i, i2, j);
        }
    }

    public void alertAiEnhancedVideoHint(int i, int i2) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.alertAiEnhancedVideoHint(i, i2);
        }
    }

    public void alertAiSceneSelector(int i) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.alertAiSceneSelector(i);
            BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
            if (bottomPopupTips != null) {
                bottomPopupTips.reConfigQrCodeTip();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x001f, code lost:
        if (r6.mCaptureDelayNumber.getVisibility() == 0) goto L_0x002b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0029, code lost:
        if (r6.mCaptureDelayNumber.getVisibility() == 0) goto L_0x002b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x002b, code lost:
        setTopTipMarin(r6.mCaptureDelayNumber, r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0030, code lost:
        r6.mCaptureNumberAutoHibernationOffset = r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0032, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void alertAutoHibernationDescTip(String str, int i, int i2, long j) {
        boolean z;
        FragmentTopAlert topAlert = getTopAlert();
        if (topAlert != null) {
            topAlert.alertRecommendDescTip(str, i, i2, j);
            if (i == 0) {
                int i3 = this.mDegree;
                if (i3 == 0 || i3 == 180) {
                    z = true;
                }
            }
            z = false;
        }
    }

    public void alertDocumentTip(@StringRes int i) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.alertDocumentTip(i);
        }
    }

    public void alertDualVideoHint(int i, int i2) {
        FragmentTopAlert topAlert = getTopAlert();
        if (topAlert != null && isTopAlertShowing(topAlert)) {
            topAlert.alertDualVideoHint(i, i2, true);
        }
    }

    public void alertFastmotionIndicator(int i, String str, String str2, String str3, boolean z) {
        FragmentTopAlert topAlert = getTopAlert();
        if (topAlert != null) {
            topAlert.alertFastmotionIndicator(i, str, str2, str3, z);
        }
    }

    public void alertFastmotionProValue(String str, String str2, String str3, boolean z, boolean z2) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.alertFastmotionProTip(str, str2, str3, z, z2);
        }
    }

    public void alertFastmotionValue(String str, String str2) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.alertFastmotionTip(str, str2);
        }
    }

    public void alertFlash(int i, String str, boolean z) {
        alertFlash(i, str, z, true);
    }

    public void alertFlash(int i, String str, boolean z, boolean z2) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            if (z2) {
                if (i != 0) {
                    reverseExpandTopBar(true);
                } else if (z) {
                    ImageView topImage = getTopImage(193);
                    if (topImage != null) {
                        topImage.performClick();
                    }
                }
            }
            topAlert.alertFlash(i, str);
        }
    }

    public void alertHDR(int i, boolean z, boolean z2) {
        alertHDR(i, z, z2, true);
    }

    public void alertHandGestureHint(int i) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.alertHandGestureHint(i);
        }
    }

    public void alertLightingTip(int i) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.alertLightingTip(i);
        }
    }

    public void alertLiveShotHint(int i, int i2) {
        alertSwitchTip(FragmentTopAlert.TIP_LIVE_SHOT, i, i2);
    }

    public void alertMacroModeHint(int i, int i2) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.alertMacroModeHint(i, i2, isTopConfig(255));
        }
    }

    public void alertMimojiFaceDetect(boolean z, int i) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.alertMimojiFaceDetect(z, i);
        }
    }

    public void alertMoonModeSelector(int i, boolean z) {
        if (isTopAlertShowing(getTopAlert())) {
            DataRepository.dataItemRunning().getComponentRunningMoon().setComponentValue(this.mCurrentMode, z ? ComponentRunningMoon.MOON : ComponentRunningMoon.NIGHT);
            boolean z2 = true;
            alertSlideSwitchLayout(i == 0, 246);
            if (this.mCurrentMode == 163) {
                CameraModuleSpecial cameraModuleSpecial = (CameraModuleSpecial) ModeCoordinatorImpl.getInstance().getAttachProtocol(195);
                if (cameraModuleSpecial != null) {
                    if (i == 0) {
                        z2 = false;
                    }
                    cameraModuleSpecial.showOrHideChip(z2);
                }
            }
        }
    }

    public void alertMusicClose(boolean z) {
        FragmentTopAlert topAlert = getTopAlert();
        if (topAlert != null) {
            topAlert.alertMusicClose(z);
        }
    }

    public void alertParameterDescriptionTip(int i, int i2) {
        FragmentTopAlert topAlert = getTopAlert();
        if (topAlert != null) {
            topAlert.alertParameterDescriptionTip(i, i2, true);
        }
    }

    public void alertParameterResetTip(boolean z, int i, @StringRes int i2) {
        FragmentTopAlert topAlert = getTopAlert();
        if (topAlert != null) {
            topAlert.alertParameterResetTip(z, i, i2, Util.getDisplayRect(0).top + getResources().getDimensionPixelSize(R.dimen.reset_manually_parameter_tip_margin_top), !(this.mIsShowExtraMenu ^ true));
        }
    }

    public void alertProColourHint(int i, int i2) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.alertProColourHint(i, i2);
        }
    }

    public void alertRecommendDescTip(String str, int i, int i2) {
        alertRecommendDescTip(str, i, i2, 3000);
    }

    public void alertRecommendDescTip(String str, int i, int i2, long j) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.alertRecommendDescTip(str, i, i2, j);
        }
    }

    public void alertSlideSwitchLayout(boolean z, int i) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.alertSlideSwitchLayout(z, i);
        }
    }

    public void alertSubtitleHint(int i, int i2) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.alertSubtitleHint(i, i2);
        }
    }

    public void alertSuperNightSeTip(int i) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.alertSuperNightSeTip(i);
        }
    }

    public void alertSwitchTip(String str, int i, int i2) {
        alertSwitchTip(str, i, getString(i2));
    }

    public void alertSwitchTip(String str, int i, int i2, int i3, long j) {
        alertSwitchTip(str, i, i2, getString(i3), j);
    }

    public void alertSwitchTip(String str, int i, int i2, String str2, long j) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.alertSwitchTip(str, i, i2, str2, j);
        }
    }

    public void alertSwitchTip(String str, int i, String str2) {
        alertSwitchTip(str, i, 1, str2, 3000);
    }

    public void alertTimerBurstHint(int i, int i2) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.alertTimerBurstHint(i, i2, isTopConfig(170));
        }
    }

    public void alertTopHint(int i, @StringRes int i2) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.alertTopHint(i, i2);
        }
    }

    public void alertTopHint(int i, int i2, long j) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.alertTopHint(i, i2, j);
        }
    }

    public void alertTopHint(int i, String str) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.alertTopHint(i, str);
        }
    }

    public void alertTopHint(int i, String str, long j) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.alertTopHint(i, str, j);
        }
    }

    public void alertUpdateValue(int i, @StringRes int i2, String str) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.alertUpdateValue(i, i2, str);
        }
    }

    public void alertVideoUltraClear(int i, @StringRes int i2) {
        FragmentTopAlert topAlert = getTopAlert();
        if (topAlert != null) {
            boolean z = true;
            if (!this.mIsShowExtraMenu) {
                z = false;
            }
            topAlert.alertVideoUltraClear(i, i2, z);
        }
    }

    public void alertVideoUltraClear(int i, String str) {
        FragmentTopAlert topAlert = getTopAlert();
        if (topAlert != null) {
            boolean z = true;
            if (!this.mIsShowExtraMenu) {
                z = false;
            }
            topAlert.alertVideoUltraClear(i, str, z);
        }
    }

    public void animTopBlackCover() {
        ExtraAdapter extraAdapter = this.mExtraAdapter;
        if ((extraAdapter == null || !extraAdapter.animationRunning()) && ((Integer) this.mTopBackgroundView.getTag()).intValue() != this.mTopBackgroundView.getCurrentMaskHeight()) {
            ShapeBackGroundView shapeBackGroundView = this.mTopBackgroundView;
            shapeBackGroundView.setMaskSpecificHeight(((Integer) shapeBackGroundView.getTag()).intValue(), true);
        }
    }

    public void clearAlertStatus() {
        FragmentTopAlert topAlert = getTopAlert();
        if (topAlert != null) {
            topAlert.clearAlertStatus();
        }
    }

    public void clearAllTipsState() {
        this.mTipsState.clear();
    }

    public void clearFastmotionValue() {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.clearFastmotionTip();
        }
    }

    public void clearVideoUltraClear() {
        FragmentTopAlert topAlert = getTopAlert();
        if (topAlert != null) {
            topAlert.clearVideoUltraClear();
        }
    }

    public boolean containShortDurationDescriptionTips(String str) {
        FragmentTopAlert topAlert = getTopAlert();
        if (!isTopAlertShowing(topAlert)) {
            return false;
        }
        return topAlert.containShortDurationDescriptionTips(str);
    }

    public void directHideLyingDirectHint() {
    }

    public void disableMenuItem(boolean z, int... iArr) {
        if (iArr != null && this.mDisabledFunctionMenu != null) {
            for (int i : iArr) {
                this.mDisabledFunctionMenu.put(i, z);
                if (z) {
                    ImageView topImage = getTopImage(i);
                    if (topImage != null) {
                        AlphaOutOnSubscribe.directSetResult(topImage);
                    }
                }
            }
        }
    }

    public void enableMenuItem(boolean z, int... iArr) {
        SparseBooleanArray sparseBooleanArray = this.mDisabledFunctionMenu;
        if (sparseBooleanArray != null && sparseBooleanArray.size() != 0) {
            for (int i : iArr) {
                this.mDisabledFunctionMenu.delete(i);
                if (z) {
                    ImageView topImage = getTopImage(i);
                    if (topImage != null) {
                        AlphaInOnSubscribe.directSetResult(topImage);
                    }
                }
            }
        }
    }

    public boolean enableSwitch() {
        if (!isEnableClick()) {
            return false;
        }
        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
        return cameraAction == null || !cameraAction.isDoingAction();
    }

    public boolean getAlertIsShow() {
        FragmentTopAlert topAlert = getTopAlert();
        if (topAlert == null) {
            return false;
        }
        return topAlert.isShow();
    }

    public int getCurrentAiSceneLevel() {
        return this.mCurrentAiSceneLevel;
    }

    public int getFragmentInto() {
        return 244;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_top_config;
    }

    public boolean getTipsState(String str) {
        if (isExtraMenuShowing()) {
            return false;
        }
        Boolean bool = (Boolean) this.mTipsState.get(str);
        return bool != null && bool.booleanValue();
    }

    public ImageView getTopImage(int i) {
        for (ImageView imageView : this.mConfigViews) {
            TopConfigItem topConfigItem = (TopConfigItem) imageView.getTag();
            if (topConfigItem != null && topConfigItem.configItem == i) {
                return imageView;
            }
        }
        return null;
    }

    public VideoTagView getVideoTag() {
        FragmentTopAlert topAlert = getTopAlert();
        if (topAlert == null) {
            return null;
        }
        return topAlert.getVideoTag();
    }

    public void hideAlert() {
        FragmentTopAlert topAlert = getTopAlert();
        if (topAlert != null && !this.mCaptureNumberAutoHibernationOffset) {
            topAlert.clear(true);
            topAlert.setShow(false);
        }
    }

    public void hideConfigMenu() {
        Completable.create(new AlphaOutOnSubscribe(this.mTopConfigMenu)).subscribe();
    }

    public void hideDelayNumber() {
        if (this.mCaptureDelayNumber.getVisibility() != 8) {
            this.mCaptureDelayNumber.setVisibility(8);
        }
    }

    public void hideExtraMenu() {
        onBackEvent(6);
    }

    public void hideRecommendDescTip(String str) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.hideRecommendDescTip(str);
        }
    }

    public void hideSwitchTip() {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.hideSwitchTip();
        }
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        this.mMoreResources = getMoreResources();
        this.mAiSceneResources = getAiSceneResources();
        this.mCinematicRatioResources = getCinematicRatioResources();
        this.mAutoZoomResources = getAutoZoomResources();
        this.mUltraWideBokehResources = getUltraWideBokehResources();
        this.mUltraPixelPhotographyIconResources = ComponentRunningUltraPixel.getUltraPixelTopMenuResources();
        this.mUltraPixelPhotographyTipString = ComponentRunningUltraPixel.getUltraPixelSwitchTipsString();
        this.mUltraPixel108PhotographyTipString = getPixel108SwitchTipsString();
        this.mLiveShotResource = getLiveShotResources();
        this.mLightingResource = getLightingResources();
        this.mVideoBokehResource = getVideoBokehResources();
        this.mSuperMacroResources = getSuperMacroResources();
        this.mUltraPixelPortraitResources = getUltraPixelPortraitResources();
        this.mSuperEISResources = getSuperEISResources();
        this.mVideo8KResource = getVideo8KRecource();
        this.mGifResource = getGifRecource();
        this.mDocumentResources = getDocumentResources();
        this.mIsRTL = Util.isLayoutRTL(getContext());
        this.mTopBarAnimationComponent = new TopBarAnimationComponent();
        this.mMultiSnapNum = (TextView) view.findViewById(R.id.v6_multi_snap_number);
        this.mCaptureDelayNumber = (TextView) view.findViewById(R.id.v6_capture_delay_number);
        this.mDisabledFunctionMenu = new SparseBooleanArray(1);
        this.mTopBackgroundView = (ShapeBackGroundView) view.findViewById(R.id.top_config_background);
        this.mTopConfigViewGroup = (ViewGroup) view.findViewById(R.id.top_config);
        this.mTopExtraMenu = (RecyclerView) view.findViewById(R.id.top_config_extra_recyclerview);
        this.mTopConfigMenu = view.findViewById(R.id.top_config_menu);
        ((LayoutParams) this.mTopConfigMenu.getLayoutParams()).width = Display.getTopBarWidth();
        this.mLlTimerMenu = (LinearLayout) view.findViewById(R.id.layout_burst_menu);
        this.mImageViewBack = (ImageView) view.findViewById(R.id.iv_timer_burst_back);
        FolmeUtils.touchTint((View) this.mImageViewBack);
        this.mImageViewBack.setOnClickListener(this);
        this.mTvShotInterval = (TextView) view.findViewById(R.id.tv_shot_interval);
        this.mCustomSeekBarInterval = (CustomSeekBar) view.findViewById(R.id.csb_interval);
        this.mCustomSeekBarCount = (CustomSeekBar) view.findViewById(R.id.csb_count);
        this.mLayoutCount = view.findViewById(R.id.ll_shot_count);
        this.mLayoutInterval = view.findViewById(R.id.ll_shot_interval);
        boolean z = C0122O00000o.instance().OOOOoO() && Display.getDisplayRatio() == Display.DISPLAY_RATIO_123;
        this.mTopFloatingMode = z;
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mTopConfigMenu.getLayoutParams();
        marginLayoutParams.topMargin = Display.getTopMargin();
        marginLayoutParams.height = Display.getTopBarHeight();
        MarginLayoutParams marginLayoutParams2 = (MarginLayoutParams) this.mTopConfigViewGroup.getLayoutParams();
        marginLayoutParams2.height = marginLayoutParams.topMargin + marginLayoutParams.height;
        this.mTopConfigMenuHeight = marginLayoutParams2.height;
        MarginLayoutParams marginLayoutParams3 = (MarginLayoutParams) this.mTopBackgroundView.getLayoutParams();
        this.mTopBackgroundHeight = Display.getTopCoverHeight();
        marginLayoutParams3.height = this.mTopBackgroundHeight;
        this.mTopBackgroundView.initHeight(marginLayoutParams3.height);
        if (this.mTopFloatingMode) {
            this.mTopBackgroundView.setTopFloating(true, Display.getBottomBarHeight());
        }
        this.mTopExpandView = (TopExpendView) view.findViewById(R.id.top_config_expand_view);
        this.mTopConfigTotalWidth = Display.getTopBarWidth();
        initTopView();
        if (((ActivityBase) getContext()).getCameraIntentManager().isFromScreenSlide().booleanValue()) {
            Util.startScreenSlideAlphaInAnimation(this.mTopConfigMenu);
        }
        provideAnimateElement(this.mCurrentMode, null, 2);
    }

    public boolean isContainAlertLightingTip(int i) {
        FragmentTopAlert topAlert = getTopAlert();
        if (!isTopAlertShowing(topAlert)) {
            return false;
        }
        return topAlert.isContainAlertRecommendTip(topAlert.parseLightingRes(i));
    }

    public boolean isContainAlertRecommendTip(int... iArr) {
        FragmentTopAlert topAlert = getTopAlert();
        if (topAlert == null) {
            return false;
        }
        return topAlert.isContainAlertRecommendTip(iArr);
    }

    public boolean isCurrentRecommendTipText(int i) {
        FragmentTopAlert topAlert = getTopAlert();
        if (!isTopAlertShowing(topAlert)) {
            return false;
        }
        return topAlert.isCurrentRecommendTipText(i);
    }

    public boolean isExtraMenuShowing() {
        return this.mIsShowExtraMenu;
    }

    public boolean isHDRShowing() {
        FragmentTopAlert topAlert = getTopAlert();
        if (topAlert == null) {
            return false;
        }
        return topAlert.isHDRShowing();
    }

    public boolean isShowBacklightSelector() {
        FragmentTopAlert topAlert = getTopAlert();
        if (topAlert == null) {
            return false;
        }
        return topAlert.isShowBacklightSelector();
    }

    public boolean isTopAlertShowing(FragmentTopAlert fragmentTopAlert) {
        return fragmentTopAlert != null && fragmentTopAlert.isShow() && !isExtraMenuShowing();
    }

    public boolean isTopConfig(int i) {
        SupportedConfigs supportedConfigs = this.mSupportedConfigs;
        return supportedConfigs != null && supportedConfigs.getLength() > 0 && this.mSupportedConfigs.contains(i);
    }

    public boolean needViewClear() {
        return true;
    }

    public void notifyAfterFrameAvailable(int i) {
        super.notifyAfterFrameAvailable(i);
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.notifyAfterFrameAvailable(i);
        }
        int i2 = this.mCurrentMode;
        if (!(i2 == 254 || i2 == 209 || i2 == 210 || getTopAlert() == null || ((this.mCurrentMode == 177 && DataRepository.dataItemLive().getMimojiStatusManager().IsInMimojiCreate()) || (this.mCurrentMode == 184 && DataRepository.dataItemLive().getMimojiStatusManager2().isInMimojiCreate())))) {
            clearVideoUltraClear();
            reConfigTipOfFlash(false);
            reConfigTipOfMusicHint(false);
            alertUpdateValue(0, 0, null);
        }
        if (this.mCurrentMode != 204) {
            animTopBlackCover();
        }
        if (this.mIsShowExtraMenu) {
            RecyclerView recyclerView = this.mTopExtraMenu;
            if (!(recyclerView == null || recyclerView.getAdapter() == null)) {
                ExtraAdapter extraAdapter = this.mExtraAdapter;
                if (extraAdapter != null && !extraAdapter.animationRunning()) {
                    int totalRow = this.mExtraAdapter.getTotalRow(1);
                    if (totalRow != 0) {
                        this.mExtraAdapter.notifyItemRangeChanged(0, totalRow);
                    }
                } else {
                    return;
                }
            }
            return;
        }
        ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
        if (!(this.mCurrentMode == 254 || configChanges == null)) {
            showTips(configChanges, false);
        }
        if (!(this.mTopConfigMenu.getVisibility() == 0 || this.mTopConfigMenu.getTag() == null || ((Integer) this.mTopConfigMenu.getTag()).intValue() != -1)) {
            AlphaInOnSubscribe.directSetResult(this.mTopConfigMenu);
            this.mTopConfigMenu.setTag(Integer.valueOf(1));
        }
        checkFeatureState();
    }

    public void notifyDataChanged(int i, int i2) {
        super.notifyDataChanged(i, i2);
        int i3 = this.mCurrentMode;
        int i4 = 7;
        if (this.mResetType != 7) {
            i4 = 2;
        }
        provideAnimateElement(i3, null, i4);
        if (this.mFragmentTopAlert == null) {
            this.mFragmentTopAlert = new FragmentTopAlert();
            this.mFragmentTopAlert.setShow(!isExtraMenuShowing());
            this.mFragmentTopAlert.setDegree(this.mDegree);
            FragmentManager childFragmentManager = getChildFragmentManager();
            FragmentTopAlert fragmentTopAlert = this.mFragmentTopAlert;
            FragmentUtils.addFragmentWithTag(childFragmentManager, (int) R.id.top_alert, (Fragment) fragmentTopAlert, fragmentTopAlert.getFragmentTag());
        }
    }

    public void onAngleChanged(float f) {
    }

    public boolean onBackEvent(int i) {
        if (this.mCurrentMode == 188 && i == 3) {
            alertAiDetectTipHint(8, R.string.super_moon_improve_effect_tips, -1);
        }
        FragmentTopAlert topAlert = getTopAlert();
        if (this.mCurrentMode != 180 || !DataRepository.dataItemGlobal().getBoolean(CameraSettings.KEY_CAMERA_AUDIO_MAP, true) || topAlert == null || topAlert.getAudioMapVisibilityState() != 8) {
            if (!reverseExpandTopBar(i != 4) || topAlert == null) {
                boolean z = this.mIsShowExtraMenu || this.mIsShowExtraTimerMenu;
                RecyclerView recyclerView = this.mTopExtraMenu;
                if (recyclerView == null || recyclerView.getVisibility() != 0) {
                    LinearLayout linearLayout = this.mLlTimerMenu;
                    if (linearLayout == null || linearLayout.getVisibility() != 0) {
                        return false;
                    }
                }
                if (i == 1 || i == 2) {
                    if (!this.mIsShowExtraMenu) {
                        return false;
                    }
                    animatorExtraMenu(false);
                } else {
                    if (!(i == 4 || i == 6)) {
                        if (i != 7) {
                            directHiddenExtraMenu();
                        }
                        if (!(i == 4 || i == 7)) {
                            reInitAlert(true);
                        }
                        this.mIsShowExtraMenu = z;
                        if (!this.mIsShowExtraMenu && i != 3) {
                            notifyExtraMenuVisibilityChange(false);
                            this.mIsShowExtraTimerMenu = false;
                            initExtraTimerBurstMenu();
                        }
                        return true;
                    }
                    animatorExtraMenu(false);
                }
                z = false;
                reInitAlert(true);
                this.mIsShowExtraMenu = z;
                notifyExtraMenuVisibilityChange(false);
                this.mIsShowExtraTimerMenu = false;
                initExtraTimerBurstMenu();
                return true;
            }
            topAlert.showRecordingTime();
            return true;
        }
        topAlert.removeHandlerCallBack();
        topAlert.setAudioMapVisibility(0);
        topAlert.setVolumeControlAnimationViewVisibility(8);
        topAlert.setVolumeControlPanelVisibility(8);
        return true;
    }

    public void onBeautyRecordingStart() {
        onBackEvent(5);
        ViewCompat.animate(this.mTopConfigMenu).alpha(0.0f).start();
    }

    public void onBeautyRecordingStop() {
        ViewCompat.animate(this.mTopConfigMenu).alpha(1.0f).start();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:130:0x02ff, code lost:
        r3.onConfigChanged(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:141:0x0335, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x01dd, code lost:
        r8.subscribe(r0);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onClick(View view) {
        ComponentData componentRunningEisPro;
        int i;
        int i2;
        int i3;
        View view2 = view;
        String str = TAG;
        Log.u(str, "top config onclick");
        if (isEnableClick()) {
            if (view.getId() != R.id.iv_timer_burst_back) {
                MoreModePopupController moreModePopupController = (MoreModePopupController) ModeCoordinatorImpl.getInstance().getAttachProtocol(2561);
                if (moreModePopupController == null || !moreModePopupController.isExpanded()) {
                    ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
                    if (configChanges != null) {
                        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
                        if (cameraAction != null && cameraAction.isDoingAction()) {
                            return;
                        }
                        if (CameraSettings.isFrontCamera() && ((Camera) getContext()).isScreenSlideOff()) {
                            return;
                        }
                        if (this.mIsShowExtraMenu) {
                            onClickByExtraMenu(view2, configChanges);
                            return;
                        }
                        Object tag = view.getTag();
                        StringBuilder sb = new StringBuilder();
                        sb.append("top config item:");
                        sb.append(tag);
                        Log.d(str, sb.toString());
                        if (tag instanceof TopConfigItem) {
                            TopConfigItem topConfigItem = (TopConfigItem) tag;
                            Log.u(str, String.format(Locale.ENGLISH, "top config onclick, ConfigItem=0x%x", new Object[]{Integer.valueOf(topConfigItem.configItem)}));
                            if (this.mDisabledFunctionMenu.size() <= 0 || this.mDisabledFunctionMenu.indexOfKey(topConfigItem.configItem) < 0) {
                                if (Util.isAccessible()) {
                                    int i4 = topConfigItem.configItem;
                                    if (!(i4 == 164 || i4 == 225 || i4 == 166 || i4 == 179)) {
                                        view2.postDelayed(new C0325O0000oOO(this, view2), 100);
                                    }
                                }
                                CameraClickObservable cameraClickObservable = (CameraClickObservable) ModeCoordinatorImpl.getInstance().getAttachProtocol(227);
                                int i5 = topConfigItem.configItem;
                                String str2 = BaseEvent.FEATURE_NAME;
                                switch (i5) {
                                    case 162:
                                        configChanges.onConfigChanged(162);
                                        break;
                                    case 164:
                                        configChanges.onConfigChanged(164);
                                        break;
                                    case 165:
                                        componentRunningEisPro = DataRepository.dataItemRunning().getComponentRunningEisPro();
                                        if (componentRunningEisPro != null) {
                                            i = 165;
                                        }
                                        break;
                                    case 166:
                                        configChanges.onConfigChanged(166);
                                        break;
                                    case 168:
                                        componentRunningEisPro = DataRepository.dataItemRunning().getComponentRunningAiAudio();
                                        if (componentRunningEisPro != null) {
                                            i = 168;
                                        }
                                        break;
                                    case 169:
                                        configChanges.onConfigChanged(169);
                                        updateConfigItem(169);
                                        break;
                                    case 171:
                                        i2 = 171;
                                        break;
                                    case 172:
                                        CameraStatUtils.trackVVWorkspaceClick(VLogAttr.VALUE_VV_CLICK_WORKSPACE_ICON);
                                        i2 = 172;
                                        break;
                                    case 175:
                                        i2 = 175;
                                        break;
                                    case 179:
                                        DollyZoomAction dollyZoomAction = (DollyZoomAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(677);
                                        if (dollyZoomAction != null) {
                                            dollyZoomAction.onGuideClicked();
                                        }
                                        i2 = 179;
                                        break;
                                    case 193:
                                        ComponentConfigFlash componentFlash = ((DataItemConfig) DataRepository.provider().dataConfig()).getComponentFlash();
                                        if (!componentFlash.isDisabled(this.mCurrentMode)) {
                                            MistatsWrapper.commonKeyTriggerEvent(str2, FlashAttr.VALUE_FLASH_OUT_BUTTON, null);
                                            if (componentFlash.disableUpdate()) {
                                                int disableReasonString = componentFlash.getDisableReasonString();
                                                if (disableReasonString != 0) {
                                                    ToastUtils.showToast((Context) getActivity(), disableReasonString);
                                                }
                                                Log.w(str, "ignore click flash for disable update");
                                            } else if (this.mCurrentMode != 171 || !C0122O00000o.instance().OOO00oo() || !CameraSettings.isBackCamera()) {
                                                expandExtraView(componentFlash, view2, topConfigItem.configItem);
                                            } else {
                                                String componentValue = componentFlash.getComponentValue(this.mCurrentMode);
                                                String str3 = "0";
                                                if (componentValue == str3) {
                                                    str3 = "5";
                                                }
                                                componentFlash.setComponentValue(this.mCurrentMode, str3);
                                                onExpandValueChange(componentFlash, componentValue, str3);
                                            }
                                            if (cameraClickObservable != null) {
                                                i3 = 161;
                                            }
                                        }
                                        break;
                                    case 194:
                                        DataItemConfig dataItemConfig = (DataItemConfig) DataRepository.provider().dataConfig();
                                        MistatsWrapper.commonKeyTriggerEvent(str2, AlgoAttr.VALUE_HDR_OUT_BUTTON, null);
                                        expandExtraView(dataItemConfig.getComponentHdr(), view2, topConfigItem.configItem);
                                        if (cameraClickObservable != null) {
                                            cameraClickObservable.subscribe(162);
                                            break;
                                        }
                                        break;
                                    case 195:
                                        i2 = 195;
                                        break;
                                    case 196:
                                        i2 = 196;
                                        break;
                                    case 197:
                                        MistatsWrapper.commonKeyTriggerEvent(FeatureName.VALUE_MENU_MORE, Integer.valueOf(1), null);
                                        showExtraMenu();
                                        if (cameraClickObservable != null) {
                                            cameraClickObservable.subscribe(164);
                                            break;
                                        }
                                        break;
                                    case 200:
                                        DataItemConfig dataItemConfig2 = (DataItemConfig) DataRepository.provider().dataConfig();
                                        dataItemConfig2.getComponentBokeh().toggle(this.mCurrentMode);
                                        String componentValue2 = dataItemConfig2.getComponentBokeh().getComponentValue(this.mCurrentMode);
                                        CameraStatUtils.tarckBokenChanged(this.mCurrentMode, componentValue2);
                                        updateConfigItem(200);
                                        if (dataItemConfig2.reConfigHdrIfBokehChanged(this.mCurrentMode, componentValue2)) {
                                            updateConfigItem(194);
                                        }
                                        configChanges.configBokeh(componentValue2);
                                        break;
                                    case 201:
                                        configChanges.onConfigChanged(201);
                                        if (cameraClickObservable != null) {
                                            cameraClickObservable.subscribe(166);
                                            break;
                                        }
                                        break;
                                    case 203:
                                        i2 = 203;
                                        break;
                                    case 205:
                                        i2 = 205;
                                        break;
                                    case 206:
                                        MistatsWrapper.commonKeyTriggerEvent(LiveShotAttr.VALUE_TOPMENU_LIVESHOT_CLICK, null, null);
                                        configChanges.onConfigChanged(206);
                                        if (cameraClickObservable != null) {
                                            i3 = 163;
                                        }
                                        break;
                                    case 207:
                                        i2 = 207;
                                        break;
                                    case 209:
                                        i2 = 209;
                                        break;
                                    case 212:
                                        MistatsWrapper.commonKeyTriggerEvent(BeautyAttr.VALUE_BEAUTY_TOP_BUTTON, null, null);
                                        i2 = 212;
                                        break;
                                    case 214:
                                        CameraStatUtils.trackMeterClick();
                                        componentRunningEisPro = ((DataItemConfig) DataRepository.provider().dataConfig()).getComponentConfigMeter();
                                        i = topConfigItem.configItem;
                                        expandExtraView(componentRunningEisPro, view2, i);
                                        break;
                                    case 215:
                                        i2 = 215;
                                        break;
                                    case 216:
                                        CameraStatUtils.trackVVClick(VLogAttr.VALUE_VV_ICON_CLICK);
                                        i2 = 216;
                                        break;
                                    case 217:
                                        i2 = 217;
                                        break;
                                    case 218:
                                        i2 = 218;
                                        break;
                                    case 220:
                                        i2 = 220;
                                        break;
                                    case 225:
                                        configChanges.showSetting();
                                        break;
                                    case 227:
                                        configChanges.onConfigChanged(227);
                                        break;
                                    case 239:
                                        i2 = 239;
                                        break;
                                    case 242:
                                        TopConfigProtocol topConfigProtocol = (TopConfigProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(193);
                                        if (topConfigProtocol != null) {
                                            topConfigProtocol.startAiLens();
                                        }
                                        MistatsWrapper.commonKeyTriggerEvent(FeatureName.VALUE_AI_DETECT_CHANGED, null, null);
                                        break;
                                    case 243:
                                        i2 = 243;
                                        break;
                                    case 245:
                                        int i6 = this.mCurrentMode;
                                        if (i6 == 174) {
                                            CameraStatUtils.trackLiveClick(Live.VALUE_LIVE_MUSIC_ICON_CLICK);
                                        } else if (i6 == 183) {
                                            CameraStatUtils.trackMiLiveClick(MiLive.VALUE_MI_LIVE_CLICK_MUSIC);
                                        }
                                        Camera camera = (Camera) getActivity();
                                        Intent intent = new Intent(camera, LiveMusicActivity.class);
                                        intent.putExtra("StartActivityWhenLocked", camera.startFromKeyguard());
                                        camera.startActivity(intent);
                                        camera.setJumpFlag(8);
                                        break;
                                    case 251:
                                        configChanges.onConfigChanged(251);
                                        if (cameraClickObservable != null) {
                                            cameraClickObservable.subscribe(169);
                                            break;
                                        }
                                        break;
                                    case 253:
                                        i2 = 253;
                                        break;
                                    case 255:
                                        i2 = 255;
                                        break;
                                    case 256:
                                        i2 = 256;
                                        break;
                                    case 263:
                                        i2 = 263;
                                        break;
                                    case 512:
                                        i2 = 512;
                                        break;
                                    case 513:
                                        i2 = 513;
                                        break;
                                }
                            } else {
                                Log.w(str, String.format(Locale.ENGLISH, "top config onclick is disabled, ConfigItem=0x%x", new Object[]{Integer.valueOf(topConfigItem.configItem)}));
                            }
                        } else {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("invalid tag: ");
                            sb2.append(getResources().getResourceEntryName(view.getId()));
                            throw new RuntimeException(sb2.toString());
                        }
                    }
                } else {
                    moreModePopupController.shrink(true);
                }
            } else {
                Log.u(str, "onClick timer burst back");
                switchExtraTimerBurstMenu();
                this.mExtraAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onExpandValueChange(ComponentData componentData, String str, String str2) {
        if (isEnableClick()) {
            DataItemConfig dataItemConfig = DataRepository.dataItemConfig();
            ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
            if (configChanges != null) {
                FragmentTopAlert topAlert = getTopAlert();
                if (topAlert != null) {
                    topAlert.showRecordingTime();
                }
                if (str == str2) {
                    reverseExpandTopBar(true);
                    return;
                }
                CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
                if (cameraAction == null || !cameraAction.isDoingAction()) {
                    int displayTitleString = componentData.getDisplayTitleString();
                    String str3 = TAG;
                    switch (displayTitleString) {
                        case R.string.pref_camera_ai_audio /*2131756139*/:
                            updateConfigItem(168);
                            configChanges.onConfigChanged(168);
                            CameraStatUtils.trackAIAudio(str2);
                            break;
                        case R.string.pref_camera_autoexposure_title /*2131756165*/:
                            MistatsWrapper.moduleUIClickEvent(componentData.getKey(this.mCurrentMode), Manual.AUTOEXPOSURE, (Object) str2);
                            updateConfigItem(214);
                            configChanges.configMeter(str2);
                            break;
                        case R.string.pref_camera_eis_title /*2131756218*/:
                            DataRepository.dataItemRunning().getComponentRunningEisPro().setComponentPreValue(str);
                            updateConfigItem(165);
                            configChanges.onConfigChanged(165);
                            break;
                        case R.string.pref_camera_flashmode_title /*2131756299*/:
                            if (componentData.getDisplayTitleString() == R.string.pref_camera_flashmode_title) {
                                String str4 = "5";
                                if (str == str4 || str2 == str4) {
                                    String str5 = "0";
                                    if (!(str2 == str5 || str2 == "200")) {
                                        configChanges.configBackSoftLightSwitch(str5);
                                        CameraStatUtils.trackFlashChanged(this.mCurrentMode, str5);
                                    }
                                }
                            }
                            CameraStatUtils.trackFlashChanged(this.mCurrentMode, str2);
                            updateConfigItem(193);
                            boolean reConfigHhrIfFlashChanged = dataItemConfig.reConfigHhrIfFlashChanged(this.mCurrentMode, str, str2);
                            if (reConfigHhrIfFlashChanged) {
                                updateConfigItem(194);
                            }
                            Log.d(str3, "flash change");
                            configChanges.configFlash(str2, reConfigHhrIfFlashChanged);
                            break;
                        case R.string.pref_camera_hdr_title /*2131756321*/:
                            CameraStatUtils.trackHdrChanged(this.mCurrentMode, str2);
                            updateConfigItem(194);
                            configChanges.restoreMutexFlash("e");
                            if (dataItemConfig.reConfigFlashIfHdrChanged(this.mCurrentMode, str2)) {
                                updateConfigItem(193);
                            }
                            if (dataItemConfig.reConfigBokehIfHdrChanged(this.mCurrentMode, str2)) {
                                updateConfigItem(200);
                            }
                            configChanges.configHdr(str2);
                            Log.d(str3, "hdr change");
                            break;
                    }
                    reverseExpandTopBar(true);
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00b0, code lost:
        if (r8 != null) goto L_0x00b2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00b4, code lost:
        r4 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00bd, code lost:
        if (r8 != null) goto L_0x00b2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x00c8, code lost:
        if (r8 != null) goto L_0x00b2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void provideAnimateElement(int i, List list, int i2) {
        CompletableOnSubscribe completableOnSubscribe;
        ShapeBackGroundView shapeBackGroundView;
        int i3 = i;
        List list2 = list;
        int i4 = i2;
        int i5 = this.mCurrentMode;
        boolean z = true;
        boolean z2 = i4 == 3;
        super.provideAnimateElement(i, list, i2);
        if (isInModeChanging() || i4 == 3) {
            this.mIsShowTopLyingDirectHint = false;
        }
        setSnapNumVisible(false, true);
        hideDelayNumber();
        boolean z3 = i5 == 161 ? i3 != 161 : i5 == 162 ? i3 != 162 : i5 == 169 ? i3 != 169 : i5 == 174 || (i5 == 180 ? i3 != 180 : !(i5 == 183 && i3 == 183));
        if (z3) {
            int i6 = 7;
            if (i4 != 7) {
                i6 = 4;
            }
            onBackEvent(i6);
        }
        int topMaskTargetHeight = PaintConditionReferred.createModeChange(this.mCurrentMode, false, false).getTopMaskTargetHeight(i3);
        this.mTopBackgroundView.setBlackOriginHeight(topMaskTargetHeight);
        if (isExtraMenuShowing()) {
            topMaskTargetHeight = topMaskTargetHeight == 0 ? 0 : this.mTopExtraMenuHeight;
            this.mTopBackgroundView.setBackgroundAlpha(153);
        } else {
            this.mTopBackgroundView.setCurrentRadius(0);
            this.mTopBackgroundView.setBackgroundAlpha(0);
        }
        Integer num = (Integer) this.mTopBackgroundView.getTag();
        if (num == null || num.intValue() != topMaskTargetHeight) {
            this.mTopBackgroundView.setTag(Integer.valueOf(topMaskTargetHeight));
            if (topMaskTargetHeight > this.mTopBackgroundView.getCurrentMaskHeight()) {
                shapeBackGroundView = this.mTopBackgroundView;
            } else if (list2 == null) {
                shapeBackGroundView = this.mTopBackgroundView;
            } else if (i5 == 165 && i3 == 254) {
                shapeBackGroundView = this.mTopBackgroundView;
            }
            boolean z4 = true;
            shapeBackGroundView.setMaskSpecificHeight(topMaskTargetHeight, z4);
        }
        if (z2) {
            enableAllDisabledMenuItem();
        }
        FragmentTopAlert topAlert = getTopAlert();
        if (topAlert != null) {
            topAlert.provideAnimateElement(i3, list2, i4);
        }
        DataItemConfig dataItemConfig = DataRepository.dataItemConfig();
        if (this.mTopConfigMenu.getVisibility() != 0 && z2) {
            AlphaInOnSubscribe.directSetResult(this.mTopConfigMenu);
            this.mTopConfigMenu.setTag(Integer.valueOf(1));
        }
        this.mSupportedConfigs = SupportedConfigFactory.getSupportedTopConfigs(this.mCurrentMode, DataRepository.dataItemGlobal().getCurrentCameraId(), DataRepository.dataItemGlobal().isNormalIntent());
        if (this.mSupportedConfigs != null) {
            int i7 = 0;
            while (i7 < this.mConfigViews.size()) {
                ImageView imageView = (ImageView) this.mConfigViews.get(i7);
                imageView.setEnabled(z);
                TopConfigItem configItem = this.mSupportedConfigs.getConfigItem(i7);
                boolean z5 = list2 != null ? z : false;
                TopConfigItem topConfigItem = configItem;
                boolean topImageResource = setTopImageResource(configItem, imageView, i, dataItemConfig, z5);
                if (!topImageResource || this.mDisabledFunctionMenu.indexOfKey(topConfigItem.configItem) < 0 || !this.mDisabledFunctionMenu.get(topConfigItem.configItem)) {
                    TopConfigItem topConfigItem2 = (TopConfigItem) imageView.getTag();
                    if (topConfigItem2 == null || topConfigItem2.configItem != topConfigItem.configItem) {
                        imageView.setTag(topConfigItem);
                        if (list2 != null) {
                            if (topImageResource) {
                                AlphaInOnSubscribe alphaInOnSubscribe = new AlphaInOnSubscribe(imageView);
                                if (this.mCurrentMode == 167 && 193 == topConfigItem.configItem) {
                                    alphaInOnSubscribe.setTargetAlpha(topConfigItem.enable ? 1.0f : 0.4f);
                                }
                                alphaInOnSubscribe.setStartDelayTime(150).setDurationTime(150);
                                completableOnSubscribe = alphaInOnSubscribe;
                            } else if (!(i5 == 165 || this.mCurrentMode == 165)) {
                                completableOnSubscribe = new AlphaOutOnSubscribe(imageView).setDurationTime(150);
                            }
                            list2.add(Completable.create(completableOnSubscribe));
                        } else if (topImageResource) {
                            AlphaInOnSubscribe.directSetResult(imageView);
                        }
                        AlphaOutOnSubscribe.directSetResult(imageView);
                    } else {
                        imageView.setTag(topConfigItem);
                    }
                }
                i7++;
                z = true;
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x003d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void provideRotateItem(List list, int i) {
        FragmentTopAlert topAlert;
        List list2;
        Consumer o0000oO0;
        super.provideRotateItem(list, i);
        if (ModuleManager.isPanoramaModule()) {
            list2 = this.mConfigViews;
            o0000oO0 = new C0327O0000oo(list);
        } else if (isBothLandscapeMode()) {
            list2 = this.mConfigViews;
            o0000oO0 = new C0324O0000oO0(i, list);
        } else {
            if (!isLeftLandscapeMode()) {
                list.addAll(this.mConfigViews);
            }
            list.add(this.mMultiSnapNum);
            list.add(this.mCaptureDelayNumber);
            topAlert = getTopAlert();
            if (topAlert != null) {
                topAlert.provideRotateItem(list, i);
            }
            if (this.mTopExpandView != null && !isLeftLandscapeMode()) {
                this.mTopExpandView.provideRotateItem(list, i);
                return;
            }
        }
        list2.forEach(o0000oO0);
        list.add(this.mMultiSnapNum);
        list.add(this.mCaptureDelayNumber);
        topAlert = getTopAlert();
        if (topAlert != null) {
        }
        if (this.mTopExpandView != null) {
        }
    }

    public void reInitAlert(boolean z) {
        if (!CameraSettings.isHandGestureOpen() || DataRepository.dataItemRunning().getHandGestureRunning()) {
            AndroidSchedulers.mainThread().scheduleDirect(new C0326O0000oOo(this, z), this.mIsShowExtraMenu ? 120 : 0, TimeUnit.MILLISECONDS);
        }
    }

    public void refreshExtraMenu() {
        if (this.mIsShowExtraMenu) {
            RecyclerView recyclerView = this.mTopExtraMenu;
            if (recyclerView != null && recyclerView.getAdapter() != null) {
                this.mTopExtraMenu.getAdapter().notifyDataSetChanged();
            }
        }
    }

    public void refreshHistogramStatsView() {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.refreshHistogramStatsView();
        }
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        registerBackStack(modeCoordinator, this);
        modeCoordinator.attachProtocol(172, this);
        if (!C0124O00000oO.isSupportedOpticalZoom() && !HybridZoomingSystem.IS_3_OR_MORE_SAT) {
            modeCoordinator.attachProtocol(184, this);
        }
    }

    public void removeExtraMenu(int i) {
        onBackEvent(i);
    }

    public boolean reverseExpandTopBar(boolean z) {
        return this.mTopBarAnimationComponent.reverse(z);
    }

    public void rotate() {
    }

    public void setAiSceneImageLevel(int i) {
        if (i == 25) {
            i = 23;
        }
        this.mCurrentAiSceneLevel = i;
        Drawable aiSceneDrawable = getAiSceneDrawable(i);
        Drawable aiSceneShadowDrawable = getAiSceneShadowDrawable(i);
        boolean z = true;
        if (aiSceneDrawable == null || aiSceneShadowDrawable == null) {
            aiSceneDrawable = getResources().getDrawable(this.mAiSceneResources[0]);
            aiSceneShadowDrawable = getResources().getDrawable(this.mAiSceneResources[1]);
        }
        ImageView topImage = getTopImage(201);
        if (aiSceneDrawable != null && topImage != null && aiSceneShadowDrawable != null) {
            topImage.setImageDrawable(aiSceneDrawable);
            topImage.setBackground(aiSceneShadowDrawable);
            if (i != 38) {
                z = false;
            }
            configBottomPopupTips(z);
            BottomPopupTips bottomPopupTips = (BottomPopupTips) ModeCoordinatorImpl.getInstance().getAttachProtocol(175);
            if (bottomPopupTips != null) {
                bottomPopupTips.reConfigQrCodeTip();
            }
            topImage.setContentDescription(getString(CameraSettings.getAiSceneOpen(this.mCurrentMode) ? R.string.accessibility_ai_scene_on : R.string.accessibility_ai_scene_off));
            if (Util.isAccessible()) {
                String[] stringArray = topImage.getContext().getResources().getStringArray(R.array.ai_scene_res);
                if (isAdded() && i > 0 && i < stringArray.length) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(getString(R.string.accessibility_ai_identify));
                    sb.append(stringArray[i]);
                    topImage.announceForAccessibility(sb.toString());
                }
            }
        }
    }

    public void setAlertAnim(boolean z) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.setAlertAnim(z);
        }
    }

    public void setClickEnable(boolean z) {
        super.setClickEnable(z);
        FragmentTopAlert topAlert = getTopAlert();
        if (topAlert != null) {
            topAlert.setClickEnable(z);
        }
    }

    public void setConfigMenuResetWhenRestartmode() {
        this.mTopConfigMenu.setTag(Integer.valueOf(-1));
    }

    public void setRecordingTimeState(int i) {
        setRecordingTimeState(i, false);
    }

    public void setRecordingTimeState(int i, boolean z) {
        FragmentTopAlert topAlert = getTopAlert();
        if (topAlert != null) {
            topAlert.setRecordingTimeState(i, z);
        } else {
            FragmentTopAlert.setPendingRecordingState(i);
        }
    }

    public void setShow(boolean z) {
        if (getTopAlert() != null) {
            getTopAlert().setShow(z);
        }
    }

    @TargetApi(21)
    public void setSnapNumValue(int i) {
        if (this.mSnapStyle == null) {
            this.mSnapStyle = new TextAppearanceSpan(getContext(), R.style.SnapTipTextStyle);
        }
        SpannableStringBuilder spannableStringBuilder = this.mStringBuilder;
        if (spannableStringBuilder == null) {
            this.mStringBuilder = new SpannableStringBuilder();
        } else {
            spannableStringBuilder.clear();
        }
        this.mStringBuilder.append(String.format("%02d", new Object[]{Integer.valueOf(i)}), this.mSnapStyle, 33);
        this.mMultiSnapNum.setText(this.mStringBuilder);
    }

    public void setSnapNumVisible(boolean z, boolean z2) {
        if (z != (this.mMultiSnapNum.getVisibility() == 0)) {
            if (this.mZoomInAnimator == null) {
                initSnapNumAnimator();
            }
            if (z) {
                setTopTipMarin(this.mMultiSnapNum, false);
                AlphaInOnSubscribe.directSetResult(this.mMultiSnapNum);
                setSnapNumValue(0);
                this.mZoomInAnimator.start();
            } else {
                this.mZoomOutAnimator.start();
                Completable.create(new AlphaOutOnSubscribe(this.mMultiSnapNum).setStartDelayTime(500)).subscribe();
            }
        }
    }

    public void setTipsState(String str, boolean z) {
        this.mTipsState.put(str, Boolean.valueOf(z));
    }

    public void setVolumeValue(float[] fArr) {
        FragmentTopAlert topAlert = getTopAlert();
        if (topAlert != null) {
            topAlert.setAudioMapMoveVolumeValue(fArr);
        }
    }

    public void showConfigMenu() {
        Completable.create(new AlphaInOnSubscribe(this.mTopConfigMenu)).subscribe();
    }

    public void showDelayNumber(int i) {
        if (this.mCaptureDelayNumber.getVisibility() != 0) {
            setTopTipMarin(this.mCaptureDelayNumber, this.mCaptureNumberAutoHibernationOffset);
            Completable.create(new AlphaInOnSubscribe(this.mCaptureDelayNumber)).subscribe();
        }
        this.mCaptureDelayNumber.setText(String.valueOf(i));
    }

    public void showDocumentStateButton(int i) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.showDocumentStateButton(i);
        }
    }

    public void startLiveShotAnimation() {
        ImageView topImage = getTopImage(206);
        if (topImage != null) {
            Drawable drawable = topImage.getDrawable();
            if (drawable instanceof LayerDrawable) {
                RotateDrawable rotateDrawable = (RotateDrawable) ((LayerDrawable) drawable).getDrawable(0);
                ObjectAnimator objectAnimator = this.mLiveShotAnimator;
                if (objectAnimator == null || objectAnimator.getTarget() != rotateDrawable) {
                    this.mLiveShotAnimator = ObjectAnimator.ofInt(rotateDrawable, Param.LEVEL, new int[]{0, 10000});
                    this.mLiveShotAnimator.setDuration(1000);
                    this.mLiveShotAnimator.setInterpolator(new CubicEaseInOutInterpolator());
                }
                if (this.mLiveShotAnimator.isRunning()) {
                    this.mLiveShotAnimator.cancel();
                }
                this.mLiveShotAnimator.start();
            }
        }
    }

    public void toSlideSwitch(int i, String str, String str2) {
        if (isEnableClick()) {
            ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
            if (configChanges != null) {
                configChanges.onConfigValueChanged(i, str);
                ExtraAdapter extraAdapter = this.mExtraAdapter;
                if (extraAdapter != null) {
                    extraAdapter.notifyDataSetChanged();
                }
                if (Util.isAccessible() && DataRepository.dataItemRunning().isSwitchOn("pref_speech_shutter")) {
                    TTSHelper.speakingTextInTalkbackMode(this.mTTSHelper, str2);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        unRegisterBackStack(modeCoordinator, this);
        modeCoordinator.detachProtocol(172, this);
        if (!C0124O00000oO.isSupportedOpticalZoom() && !HybridZoomingSystem.IS_3_OR_MORE_SAT) {
            modeCoordinator.detachProtocol(184, this);
        }
    }

    public void updateConfigItem(int... iArr) {
        DataItemConfig dataItemConfig = DataRepository.dataItemConfig();
        for (int topImage : iArr) {
            ImageView topImage2 = getTopImage(topImage);
            if (topImage2 != null) {
                setTopImageResource((TopConfigItem) topImage2.getTag(), topImage2, this.mCurrentMode, dataItemConfig, false);
            }
        }
    }

    public void updateContentDescription() {
        ImageView topImage = getTopImage(196);
        if (topImage != null) {
            topImage.setContentDescription(getString(R.string.accessibility_filter_open_panel));
        }
    }

    public void updateFastmotionProRecordingTime(String str, String str2) {
        FragmentTopAlert topAlert = getTopAlert();
        if (topAlert != null) {
            topAlert.updateFastmotionProRecordingTime(str, str2);
        }
    }

    public void updateHistogramStatsData(int[] iArr) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.updateHistogramStatsData(iArr);
        }
    }

    public void updateHistogramStatsData(int[] iArr, int[] iArr2, int[] iArr3) {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.updateHistogramStatsData(iArr, iArr2, iArr3);
        }
    }

    public void updateLyingDirectHint(boolean z, boolean z2) {
        if (!z2) {
            this.mIsShowTopLyingDirectHint = z;
        }
        if (!isExtraMenuShowing()) {
            FragmentTopAlert topAlert = getTopAlert();
            if (isTopAlertShowing(topAlert)) {
                topAlert.updateLyingDirectHint(this.mIsShowTopLyingDirectHint);
            }
        }
    }

    public void updateProVideoRecordingSimpleView(boolean z) {
        FragmentTopAlert topAlert = getTopAlert();
        if (topAlert != null) {
            topAlert.updateProVideoRecordingSimpleView(z);
        }
    }

    public void updateRGBHistogramSwitched(boolean z) {
        boolean isTopAlertShowing = isTopAlertShowing(getTopAlert());
    }

    public void updateRecordingTime(String str) {
        FragmentTopAlert topAlert = getTopAlert();
        if (topAlert != null) {
            topAlert.updateRecordingTime(str);
        }
    }

    public void updateRecordingTimeStyle(boolean z) {
        FragmentTopAlert topAlert = getTopAlert();
        if (topAlert != null) {
            topAlert.updateRecordingTimeStyle(z);
        }
    }

    public void updateTopAlertLayout() {
        FragmentTopAlert topAlert = getTopAlert();
        if (isTopAlertShowing(topAlert)) {
            topAlert.updateTopAlertLayout();
        }
    }
}
