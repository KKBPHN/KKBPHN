package com.android.camera.fragment;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.hardware.camera2.params.MeteringRectangle;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Pair;
import android.util.Size;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.camera.ActivityBase;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.RotateDialogController;
import com.android.camera.Util;
import com.android.camera.aiwatermark.DragListener;
import com.android.camera.aiwatermark.data.WatermarkItem;
import com.android.camera.aiwatermark.util.WatermarkConstant;
import com.android.camera.animation.type.AlphaInOnSubscribe;
import com.android.camera.animation.type.SlideInOnSubscribe;
import com.android.camera.animation.type.SlideOutOnSubscribe;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.runing.ComponentRunningAIWatermark;
import com.android.camera.data.provider.DataProvider.ProviderEditor;
import com.android.camera.effect.renders.CustomTextWaterMark;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiAvatarEngine2;
import com.android.camera.features.mimoji2.widget.helper.MimojiHelper2;
import com.android.camera.features.mimoji2.widget.helper.MimojiStatusManager2;
import com.android.camera.fragment.aiwatermark.adapter.WatermarkLocationAdapter;
import com.android.camera.fragment.mimoji.MimojiHelper;
import com.android.camera.log.Log;
import com.android.camera.module.ModuleManager;
import com.android.camera.module.impl.component.MimojiStatusManager;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.AutoZoomModuleProtocol;
import com.android.camera.protocol.ModeProtocol.AutoZoomViewProtocol;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.KeyEvent;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.protocol.ModeProtocol.ManuallyValueChanged;
import com.android.camera.protocol.ModeProtocol.MiBeautyProtocol;
import com.android.camera.protocol.ModeProtocol.MimojiAvatarEngine;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.protocol.ModeProtocol.WatermarkProtocol;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.ui.AfRegionsView;
import com.android.camera.ui.DocumentView;
import com.android.camera.ui.FaceView;
import com.android.camera.ui.FocusIndicator;
import com.android.camera.ui.FocusView;
import com.android.camera.ui.FocusView.ExposureViewListener;
import com.android.camera.ui.FrameView;
import com.android.camera.ui.LightingView;
import com.android.camera.ui.ObjectView;
import com.android.camera.ui.ObjectView.ObjectViewListener;
import com.android.camera.ui.V6EffectCropView;
import com.android.camera.ui.V6GestureRecognizer;
import com.android.camera.ui.V6PreviewFrame;
import com.android.camera.ui.V6PreviewPanel;
import com.android.camera.ui.ZoomView;
import com.android.camera.ui.ZoomView.zoomValueChangeListener;
import com.android.camera2.CameraCapabilities;
import com.android.camera2.CameraHardwareFace;
import com.android.camera2.autozoom.AutoZoomCaptureResult;
import com.android.camera2.autozoom.AutoZoomView;
import com.bumptech.glide.Glide;
import io.reactivex.Completable;
import java.util.List;
import java.util.Locale;
import miuix.view.animation.CubicEaseOutInterpolator;

public class FragmentMainContent extends BaseFragment implements MainContentProtocol, AutoZoomViewProtocol, HandleBackTrace, zoomValueChangeListener {
    public static final int FRAGMENT_INFO = 243;
    public static final int FRONT_CAMERA_ID = 1;
    private static final String TAG = "FragmentMainContent";
    private long lastConfirmTime;
    /* access modifiers changed from: private */
    public int lastFaceResult;
    private int mActiveIndicator = 2;
    private AfRegionsView mAfRegionsView;
    private AutoZoomView mAutoZoomOverlay;
    private ImageView mCenterHintIcon;
    private TextView mCenterHintText;
    private ViewGroup mCoverParent;
    /* access modifiers changed from: private */
    public int mCurrentMimojiFaceResult;
    private int mDisplayRectLeftMargin;
    private int mDisplayRectTopMargin;
    private DocumentView mDocumentView;
    private V6EffectCropView mEffectCropView;
    private FaceView mFaceView;
    private FocusView mFocusView;
    private Handler mHandler = new Handler();
    private ValueAnimator mHistogramAnimator;
    private boolean mIsHorizontal;
    private boolean mIsIntentAction;
    private boolean mIsMimojiCreateLowLight;
    private boolean mIsMimojiFaceDetectTip;
    private boolean mIsRecording;
    private boolean mIsShowMainLyingDirectHint;
    private ImageView mIvIdPhotoBox;
    private int mLastCameraId = -1;
    /* access modifiers changed from: private */
    public boolean mLastFaceSuccess;
    private int mLastTranslateY;
    private View mLeftCover;
    private LightingView mLightingView;
    private DragListener mListener = null;
    /* access modifiers changed from: private */
    public AlertDialog mLocationDialog = null;
    private TextView mLyingDirectHint;
    private int mMimojiDetectTipType;
    private int mMimojiFaceDetect;
    private boolean mMimojiLastFaceSuccess;
    private LightingView mMimojiLightingView;
    private View mMoreModeMaskView;
    private int mNormalCoverHeight;
    private ObjectView mObjectView;
    private OnClickListener mOnClickListener = new OnClickListener() {
        public void onClick(View view) {
            if (FragmentMainContent.this.mLocationDialog == null) {
                final WatermarkItem majorWatermarkItem = DataRepository.dataItemRunning().getComponentRunningAIWatermark().getMajorWatermarkItem();
                if (majorWatermarkItem != null) {
                    TextView access$100 = FragmentMainContent.this.findTextView(view, majorWatermarkItem);
                    if (access$100 != null) {
                        View inflate = FragmentMainContent.this.getActivity().getLayoutInflater().inflate(R.layout.watermark_location_list, null);
                        final WatermarkLocationAdapter watermarkLocationAdapter = new WatermarkLocationAdapter(FragmentMainContent.this.getContext(), majorWatermarkItem);
                        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.aiwatermark_location_rv);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FragmentMainContent.this.getContext());
                        linearLayoutManager.setOrientation(1);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(watermarkLocationAdapter);
                        watermarkLocationAdapter.setDefaultSelect(access$100.getText().toString());
                        FragmentMainContent fragmentMainContent = FragmentMainContent.this;
                        fragmentMainContent.mLocationDialog = RotateDialogController.showLocationDialog(fragmentMainContent.getActivity(), FragmentMainContent.this.getActivity().getString(R.string.dialog_ok), new Runnable() {
                            public void run() {
                                String selectLocation = watermarkLocationAdapter.getSelectLocation();
                                if (selectLocation != null && !selectLocation.isEmpty()) {
                                    ProviderEditor editor = DataRepository.dataItemGlobal().editor();
                                    editor.putString(majorWatermarkItem.getKey(), selectLocation);
                                    editor.commit();
                                    FragmentMainContent fragmentMainContent = FragmentMainContent.this;
                                    fragmentMainContent.updateWatermarkRotation(fragmentMainContent.mWatermarkDegree);
                                }
                            }
                        }, FragmentMainContent.this.getActivity().getString(R.string.snap_cancel), new Runnable() {
                            public void run() {
                                Log.d(FragmentMainContent.TAG, "on cancel click.");
                            }
                        }, inflate, FragmentMainContent.this.getContext().getString(R.string.aiwatermark_location_list_title));
                        FragmentMainContent.this.mLocationDialog.setOnDismissListener(new OnDismissListener() {
                            public void onDismiss(DialogInterface dialogInterface) {
                                FragmentMainContent.this.mLocationDialog.setOnDismissListener(null);
                                FragmentMainContent.this.mLocationDialog = null;
                            }
                        });
                    }
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public ViewGroup mPreviewCenterHint;
    private V6PreviewFrame mPreviewFrame;
    private ViewGroup mPreviewPage;
    private V6PreviewPanel mPreviewPanel;
    private View mRightCover;
    private float mSuperMoonScaleSize;
    private FrameLayout mWatermarkBackgroundLayout;
    private ViewStub mWatermarkBackgroundViewStub;
    /* access modifiers changed from: private */
    public int mWatermarkDegree = 0;
    private FrameLayout mWatermarkExtendLayout;
    private ViewStub mWatermarkExtendViewStub;
    private FrameLayout mWatermarkLayout;
    private float mWatermarkScaleSize;
    private ViewStub mWatermarkViewStub;
    private ZoomView mZoomView;
    private ZoomView mZoomViewHorizontal;
    private RectF mergeRectF = new RectF();

    /* renamed from: com.android.camera.fragment.FragmentMainContent$6 reason: invalid class name */
    /* synthetic */ class AnonymousClass6 {
        static final /* synthetic */ int[] $SwitchMap$com$android$camera$fragment$FragmentMainContent$CoverState = new int[CoverState.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$com$android$camera$fragment$FragmentMainContent$CoverState[CoverState.NONE.ordinal()] = 1;
            $SwitchMap$com$android$camera$fragment$FragmentMainContent$CoverState[CoverState.TB.ordinal()] = 2;
            try {
                $SwitchMap$com$android$camera$fragment$FragmentMainContent$CoverState[CoverState.LR.ordinal()] = 3;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    enum CoverState {
        NONE,
        TB,
        LR,
        TOP,
        BOTTOM,
        LEFT,
        RIGHT
    }

    static /* synthetic */ void O000000o(TextView textView) {
        LayoutParams layoutParams = textView.getLayoutParams();
        layoutParams.width = -2;
        layoutParams.height = -2;
        textView.setLayoutParams(layoutParams);
    }

    static /* synthetic */ void O000000o(WatermarkItem watermarkItem, boolean z, View view) {
        if (!z) {
            view = null;
        }
        watermarkItem.updateTextBitmap(view);
    }

    private void adjustViewHeight() {
        if (getContext() != null && this.mPreviewPanel != null) {
            ViewGroup viewGroup = this.mPreviewPage;
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) viewGroup.getLayoutParams();
            MarginLayoutParams marginLayoutParams2 = (MarginLayoutParams) this.mPreviewPanel.getLayoutParams();
            MarginLayoutParams marginLayoutParams3 = (MarginLayoutParams) this.mPreviewCenterHint.getLayoutParams();
            Rect previewRect = Util.getPreviewRect(getContext());
            if (marginLayoutParams2.height != previewRect.height() || previewRect.top != this.mDisplayRectTopMargin || previewRect.left != this.mDisplayRectLeftMargin) {
                this.mDisplayRectTopMargin = previewRect.top;
                this.mDisplayRectLeftMargin = previewRect.left;
                marginLayoutParams2.height = previewRect.height();
                marginLayoutParams2.topMargin = previewRect.top;
                int i = previewRect.left;
                marginLayoutParams2.leftMargin = i;
                marginLayoutParams2.rightMargin = i;
                this.mPreviewPanel.setLayoutParams(marginLayoutParams2);
                marginLayoutParams3.height = (previewRect.width() * 4) / 3;
                this.mPreviewCenterHint.setLayoutParams(marginLayoutParams3);
                marginLayoutParams.height = previewRect.height() + this.mDisplayRectTopMargin;
                viewGroup.setLayoutParams(marginLayoutParams);
                setDisplaySize(previewRect.width(), previewRect.height());
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0132, code lost:
        if ((r12.leftMargin + r12.width) > com.android.camera.Util.getDisplayRect().width()) goto L_0x0134;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0468, code lost:
        if ((r12.leftMargin + r12.width) > com.android.camera.Util.getDisplayRect().width()) goto L_0x0134;
     */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x046e  */
    /* JADX WARNING: Removed duplicated region for block: B:51:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void autoRelayoutUI(String str, LinearLayout linearLayout, boolean z) {
        char c;
        TextView textView;
        FrameLayout.LayoutParams layoutParams;
        FrameLayout.LayoutParams layoutParams2;
        int i;
        Resources resources;
        Resources resources2;
        int i2;
        int i3;
        Resources resources3;
        switch (str.hashCode()) {
            case 37820169:
                if (str.equals(WatermarkConstant.LOCATION_TIME_1)) {
                    c = 2;
                    break;
                }
            case 37820170:
                if (str.equals(WatermarkConstant.LOCATION_TIME_2)) {
                    c = 3;
                    break;
                }
            case 1888438524:
                if (str.equals(WatermarkConstant.LONGITUDE_LATITUDE)) {
                    c = 4;
                    break;
                }
            case 1901043637:
                if (str.equals("location")) {
                    c = 1;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        if (c == 2) {
            textView = (TextView) this.mWatermarkLayout.findViewById(R.id.location_time_1_location);
            View findViewById = this.mWatermarkLayout.findViewById(R.id.divide_1);
            LayoutParams layoutParams3 = textView.getLayoutParams();
            int length = Util.getLength(textView.getText().toString());
            int dimensionPixelSize = this.mWatermarkLayout.getContext().getResources().getDimensionPixelSize(R.dimen.aiwatermark_location_1_text_max_width);
            LinearLayout.LayoutParams layoutParams4 = (LinearLayout.LayoutParams) findViewById.getLayoutParams();
            if (length <= 24) {
                layoutParams3.width = (int) (((float) (length * dimensionPixelSize)) / 24.0f);
                layoutParams4.height = this.mWatermarkLayout.getResources().getDimensionPixelSize(R.dimen.aiwatermark_divide_1_short);
            } else {
                layoutParams4.height = this.mWatermarkLayout.getResources().getDimensionPixelSize(R.dimen.aiwatermark_divide_1_long);
                layoutParams3.width = dimensionPixelSize;
            }
            findViewById.setLayoutParams(layoutParams4);
            findViewById.layout(layoutParams4.leftMargin, layoutParams4.topMargin, layoutParams4.leftMargin + layoutParams4.width, layoutParams4.topMargin + layoutParams4.height);
            findViewById.requestLayout();
            int max = Math.max(layoutParams3.width, ((TextView) this.mWatermarkLayout.findViewById(R.id.location_time_1_time)).getWidth());
            LinearLayout linearLayout2 = (LinearLayout) this.mWatermarkLayout.findViewById(R.id.location_time_1_time_lcation);
            LinearLayout.LayoutParams layoutParams5 = (LinearLayout.LayoutParams) linearLayout2.getLayoutParams();
            layoutParams5.width = max;
            layoutParams5.height = -2;
            linearLayout2.setLayoutParams(layoutParams5);
            linearLayout2.layout(layoutParams5.leftMargin, layoutParams5.topMargin, layoutParams5.leftMargin + layoutParams5.width, layoutParams5.topMargin + layoutParams5.height);
            linearLayout2.requestLayout();
            layoutParams = (FrameLayout.LayoutParams) linearLayout.getLayoutParams();
            int dimensionPixelSize2 = this.mWatermarkLayout.getResources().getDimensionPixelSize(R.dimen.aiwatermark_location_2_rl_padding);
            int dimensionPixelSize3 = this.mWatermarkLayout.getResources().getDimensionPixelSize(R.dimen.aiwatermark_location_time_1_day_width);
            int dimensionPixelSize4 = this.mWatermarkLayout.getResources().getDimensionPixelSize(R.dimen.aiwatermark_divide_1_width);
            layoutParams.width = (dimensionPixelSize2 * 2) + dimensionPixelSize3 + dimensionPixelSize4 + this.mWatermarkLayout.getResources().getDimensionPixelSize(R.dimen.aiwatermark_divide_1_margin_left) + max + this.mWatermarkLayout.getResources().getDimensionPixelSize(R.dimen.aiwatermark_location_1_inner_margin);
            layoutParams.height = this.mWatermarkLayout.getResources().getDimensionPixelSize(R.dimen.aiwatermark_location_1_height);
            layoutParams2 = (FrameLayout.LayoutParams) this.mWatermarkLayout.getLayoutParams();
            layoutParams2.width = layoutParams.width;
            layoutParams2.height = layoutParams.height;
        } else if (c == 3) {
            textView = (TextView) this.mWatermarkLayout.findViewById(R.id.location_time_2_location);
            LinearLayout.LayoutParams layoutParams6 = (LinearLayout.LayoutParams) textView.getLayoutParams();
            int length2 = Util.getLength(textView.getText().toString());
            int dimensionPixelSize5 = this.mWatermarkLayout.getContext().getResources().getDimensionPixelSize(R.dimen.aiwatermark_location_2_text_max_width);
            if (length2 <= 24) {
                layoutParams6.width = (int) (((float) (length2 * dimensionPixelSize5)) / 24.0f);
                resources = this.mWatermarkLayout.getResources();
                i = R.dimen.aiwatermark_location_3_text_1_height;
            } else {
                layoutParams6.width = dimensionPixelSize5;
                resources = this.mWatermarkLayout.getResources();
                i = R.dimen.aiwatermark_location_3_text_2_height;
            }
            layoutParams6.height = resources.getDimensionPixelSize(i);
            FrameLayout.LayoutParams layoutParams7 = (FrameLayout.LayoutParams) linearLayout.getLayoutParams();
            layoutParams7.width = this.mWatermarkLayout.getResources().getDimensionPixelSize(R.dimen.aiwatermark_location_2_margin_left) + this.mWatermarkLayout.getResources().getDimensionPixelSize(R.dimen.aiwatermark_location_rl_padding) + Math.max(layoutParams6.width, ((TextView) this.mWatermarkLayout.findViewById(R.id.location_time_2_time)).getWidth());
            int dimensionPixelSize6 = this.mWatermarkLayout.getResources().getDimensionPixelSize(R.dimen.aiwatermark_location_tb_padding);
            int dimensionPixelSize7 = this.mWatermarkLayout.getResources().getDimensionPixelSize(R.dimen.aiwatermark_location_3_line_height);
            int dimensionPixelSize8 = this.mWatermarkLayout.getResources().getDimensionPixelSize(R.dimen.aiwatermark_location_3_text_margin_top);
            int dimensionPixelSize9 = this.mWatermarkLayout.getResources().getDimensionPixelSize(R.dimen.aiwatermark_location_3_time_margin_top);
            layoutParams7.height = (dimensionPixelSize6 * 2) + dimensionPixelSize7 + dimensionPixelSize8 + layoutParams6.height + dimensionPixelSize9 + this.mWatermarkLayout.getResources().getDimensionPixelSize(R.dimen.aiwatermark_location_3_time_height);
            FrameLayout.LayoutParams layoutParams8 = (FrameLayout.LayoutParams) this.mWatermarkLayout.getLayoutParams();
            layoutParams8.width = layoutParams7.width;
            layoutParams8.height = layoutParams7.height;
            if (layoutParams8.leftMargin + layoutParams8.width > Util.getDisplayRect().width()) {
                layoutParams8.leftMargin = Util.getDisplayRect().width() - layoutParams8.width;
            }
            this.mWatermarkLayout.setLayoutParams(layoutParams8);
            this.mWatermarkLayout.layout(layoutParams8.leftMargin, layoutParams8.topMargin, layoutParams8.leftMargin + layoutParams8.width, layoutParams8.topMargin + layoutParams8.height);
            this.mWatermarkLayout.requestLayout();
            linearLayout.setLayoutParams(layoutParams7);
            linearLayout.layout(layoutParams7.leftMargin, layoutParams7.topMargin, layoutParams7.leftMargin + layoutParams7.width, layoutParams7.topMargin + layoutParams7.height);
            linearLayout.requestLayout();
            if (textView != null) {
            }
        } else if (c != 4) {
            textView = (TextView) this.mWatermarkLayout.findViewById(R.id.location_text);
            LayoutParams layoutParams9 = textView.getLayoutParams();
            int length3 = Util.getLength(textView.getText().toString());
            int dimensionPixelSize10 = this.mWatermarkLayout.getContext().getResources().getDimensionPixelSize(R.dimen.aiwatermark_location_text_max_width);
            View findViewById2 = this.mWatermarkLayout.findViewById(R.id.location_icon);
            LinearLayout.LayoutParams layoutParams10 = (LinearLayout.LayoutParams) findViewById2.getLayoutParams();
            if (length3 <= 24) {
                layoutParams9.width = (int) (((float) (length3 * dimensionPixelSize10)) / 24.0f);
                layoutParams9.height = this.mWatermarkLayout.getContext().getResources().getDimensionPixelSize(R.dimen.aiwatermark_location_1_textview_1_height);
                resources3 = this.mWatermarkLayout.getContext().getResources();
                i3 = R.dimen.aiwatermark_location_1_icon_mergin_1;
            } else {
                layoutParams9.width = dimensionPixelSize10;
                layoutParams9.height = this.mWatermarkLayout.getContext().getResources().getDimensionPixelSize(R.dimen.aiwatermark_location_1_textview_2_height);
                resources3 = this.mWatermarkLayout.getContext().getResources();
                i3 = R.dimen.aiwatermark_location_1_icon_mergin_2;
            }
            layoutParams10.topMargin = resources3.getDimensionPixelSize(i3);
            findViewById2.setLayoutParams(layoutParams10);
            layoutParams = (FrameLayout.LayoutParams) linearLayout.getLayoutParams();
            int dimensionPixelSize11 = this.mWatermarkLayout.getResources().getDimensionPixelSize(R.dimen.aiwatermark_location_rl_padding);
            int dimensionPixelSize12 = this.mWatermarkLayout.getResources().getDimensionPixelSize(R.dimen.aiwatermark_location_icon_width);
            int dimensionPixelSize13 = this.mWatermarkLayout.getResources().getDimensionPixelSize(R.dimen.aiwatermark_location_text_margin_start);
            int dimensionPixelSize14 = layoutParams9.height + (this.mWatermarkLayout.getResources().getDimensionPixelSize(R.dimen.aiwatermark_location_tb_padding) * 2);
            layoutParams.width = layoutParams9.width + (dimensionPixelSize11 * 2) + dimensionPixelSize12 + dimensionPixelSize13;
            layoutParams.height = dimensionPixelSize14;
            layoutParams2 = (FrameLayout.LayoutParams) this.mWatermarkLayout.getLayoutParams();
            layoutParams2.width = layoutParams.width;
            layoutParams2.height = layoutParams.height;
        } else {
            FrameLayout.LayoutParams layoutParams11 = (FrameLayout.LayoutParams) linearLayout.getLayoutParams();
            ViewGroup viewGroup = (ViewGroup) linearLayout.findViewById(R.id.altitude_group);
            StringBuilder sb = new StringBuilder();
            sb.append("hasAltitude = ");
            sb.append(z);
            Log.d(TAG, sb.toString());
            if (z) {
                viewGroup.setVisibility(0);
                resources2 = this.mWatermarkLayout.getResources();
                i2 = R.dimen.aiwatermark_log_lat_width;
            } else {
                viewGroup.setVisibility(8);
                resources2 = this.mWatermarkLayout.getResources();
                i2 = R.dimen.aiwatermark_log_lat_width_no_altitude;
            }
            layoutParams11.width = resources2.getDimensionPixelSize(i2);
            layoutParams11.height = this.mWatermarkLayout.getResources().getDimensionPixelSize(R.dimen.aiwatermark_log_lat_height);
            FrameLayout.LayoutParams layoutParams12 = (FrameLayout.LayoutParams) this.mWatermarkLayout.getLayoutParams();
            layoutParams12.width = layoutParams11.width;
            layoutParams12.height = layoutParams11.height;
            this.mWatermarkLayout.setLayoutParams(layoutParams12);
            this.mWatermarkLayout.layout(0, 0, layoutParams12.width, layoutParams12.height);
            this.mWatermarkLayout.requestLayout();
            linearLayout.setLayoutParams(layoutParams11);
            linearLayout.layout(0, 0, layoutParams11.width, layoutParams11.height);
            linearLayout.requestLayout();
            textView = null;
            if (textView != null) {
                reSizeTextView(textView);
                return;
            }
            return;
        }
        layoutParams2.leftMargin = Util.getDisplayRect().width() - layoutParams2.width;
        this.mWatermarkLayout.setLayoutParams(layoutParams2);
        this.mWatermarkLayout.layout(layoutParams2.leftMargin, layoutParams2.topMargin, layoutParams2.leftMargin + layoutParams2.width, layoutParams2.topMargin + layoutParams2.height);
        this.mWatermarkLayout.requestLayout();
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.layout(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.leftMargin + layoutParams.width, layoutParams.topMargin + layoutParams.height);
        linearLayout.requestLayout();
        if (textView != null) {
        }
    }

    private FrameLayout.LayoutParams calculateLP(WatermarkItem watermarkItem, int i, FrameLayout.LayoutParams layoutParams) {
        int i2;
        Resources resources;
        if (needMoveWatermark(watermarkItem, i)) {
            WatermarkProtocol watermarkProtocol = (WatermarkProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(253);
            MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
            if (watermarkProtocol != null && watermarkProtocol.isWatermarkPanelShow()) {
                layoutParams.topMargin = (layoutParams.topMargin - getContext().getResources().getDimensionPixelSize(R.dimen.wm_item_width)) - getContext().getResources().getDimensionPixelSize(R.dimen.beautycamera_makeup_item_margin);
            } else if (miBeautyProtocol != null && miBeautyProtocol.isBeautyPanelShow()) {
                String currentType = DataRepository.dataItemRunning().getComponentRunningShine().getCurrentType();
                char c = 65535;
                int hashCode = currentType.hashCode();
                if (hashCode != 49) {
                    if (hashCode != 50) {
                        if (hashCode != 55) {
                            if (hashCode == 1573 && currentType.equals("16")) {
                                c = 3;
                            }
                        } else if (currentType.equals("7")) {
                            c = 0;
                        }
                    } else if (currentType.equals("2")) {
                        c = 2;
                    }
                } else if (currentType.equals("1")) {
                    c = 1;
                }
                int i3 = R.dimen.beauty_fragment_height;
                if (c == 0 || c == 1 || c == 2 || c == 3) {
                    i2 = layoutParams.topMargin;
                    resources = getContext().getResources();
                } else {
                    i2 = layoutParams.topMargin - getContext().getResources().getDimensionPixelSize(R.dimen.beauty_fragment_height);
                    resources = getContext().getResources();
                    i3 = R.dimen.beautycamera_popup_fragment_height;
                }
                layoutParams.topMargin = i2 - resources.getDimensionPixelSize(i3);
            }
        }
        return layoutParams;
    }

    private void consumeResult(int i, boolean z) {
        if (System.currentTimeMillis() - this.lastConfirmTime >= ((long) (z ? 700 : 1000))) {
            this.lastConfirmTime = System.currentTimeMillis();
            StringBuilder sb = new StringBuilder();
            sb.append(i);
            sb.append("");
            Log.d("faceResult:", sb.toString());
            if (z) {
                mimojiFaceDetectSync(161, i);
                return;
            }
            final TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (this.lastFaceResult != i || topAlert == null || !topAlert.isContainAlertLightingTip(i)) {
                this.lastFaceResult = i;
                final LightingView lightingView = this.mLightingView;
                if (lightingView != null) {
                    lightingView.post(new Runnable() {
                        public void run() {
                            TopAlert topAlert = topAlert;
                            if (topAlert != null) {
                                topAlert.alertLightingTip(FragmentMainContent.this.lastFaceResult);
                            }
                        }
                    });
                }
                boolean z2 = i == 6;
                if (this.mLastFaceSuccess != z2) {
                    this.mLastFaceSuccess = z2;
                    if (lightingView != null) {
                        lightingView.post(new Runnable() {
                            public void run() {
                                boolean access$500 = FragmentMainContent.this.mLastFaceSuccess;
                                LightingView lightingView = lightingView;
                                if (access$500) {
                                    lightingView.triggerAnimateSuccess();
                                } else {
                                    lightingView.triggerAnimateFocusing();
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    private void doAccessible(View view) {
        if (Util.isAccessible()) {
            view.setOnClickListener(new C0285O0000o0O(this, view));
        }
    }

    /* access modifiers changed from: private */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public TextView findTextView(View view, WatermarkItem watermarkItem) {
        char c;
        int i;
        String key = watermarkItem.getKey();
        switch (key.hashCode()) {
            case 37820169:
                if (key.equals(WatermarkConstant.LOCATION_TIME_1)) {
                    c = 1;
                    break;
                }
            case 37820170:
                if (key.equals(WatermarkConstant.LOCATION_TIME_2)) {
                    c = 2;
                    break;
                }
            case 1901043637:
                if (key.equals("location")) {
                    c = 0;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        if (c == 0) {
            i = R.id.location_text;
        } else if (c == 1) {
            i = R.id.location_time_1_location;
        } else if (c != 2) {
            return null;
        } else {
            i = R.id.location_time_2_location;
        }
        return (TextView) view.findViewById(i);
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    private LinearLayout getLayoutByKey(FrameLayout frameLayout, String str) {
        char c;
        int i;
        switch (str.hashCode()) {
            case 3560141:
                if (str.equals("time")) {
                    c = 1;
                    break;
                }
            case 37820169:
                if (str.equals(WatermarkConstant.LOCATION_TIME_1)) {
                    c = 5;
                    break;
                }
            case 37820170:
                if (str.equals(WatermarkConstant.LOCATION_TIME_2)) {
                    c = 6;
                    break;
                }
            case 1888438524:
                if (str.equals(WatermarkConstant.LONGITUDE_LATITUDE)) {
                    c = 7;
                    break;
                }
            case 1901043637:
                if (str.equals("location")) {
                    c = 4;
                    break;
                }
            case 2089596377:
                if (str.equals(WatermarkConstant.EXTEND_TIME_1)) {
                    c = 2;
                    break;
                }
            case 2089596380:
                if (str.equals(WatermarkConstant.EXTEND_TIME_2)) {
                    c = 3;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 2:
                i = R.id.extend_time_watermark_1;
                break;
            case 3:
                i = R.id.extend_time_watermark_2;
                break;
            case 4:
                i = R.id.location;
                break;
            case 5:
                i = R.id.location_time_1;
                break;
            case 6:
                i = R.id.location_time_2;
                break;
            case 7:
                i = R.id.longitude_latitude;
                break;
            default:
                i = R.id.time_watermark;
                break;
        }
        return (LinearLayout) frameLayout.findViewById(i);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0067, code lost:
        if ((r8 & 32) != 0) goto L_0x0101;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00b3, code lost:
        if ((r8 & 32) != 0) goto L_0x016b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x00ff, code lost:
        if ((r8 & 32) != 0) goto L_0x0101;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x0169, code lost:
        if ((r8 & 32) != 0) goto L_0x016b;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private FrameLayout.LayoutParams getLayoutParams(FrameLayout.LayoutParams layoutParams, int i, Rect rect, Size size, int i2) {
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        if (i < 0) {
            getLayoutParamsForExtent(layoutParams, rect, size, i, i2);
            return layoutParams;
        }
        if (i2 == -1 || i2 == 0) {
            int i8 = rect.left;
            int i9 = rect.top;
            size.getWidth();
            size.getHeight();
            if ((i & 1) != 0) {
                i8 = rect.left;
                size.getWidth();
            }
            if ((i & 2) != 0) {
                i9 = rect.top;
                size.getHeight();
            }
            if ((i & 4) != 0) {
                i8 = rect.right - size.getWidth();
            }
            if ((i & 8) != 0) {
                i9 = rect.bottom - size.getHeight();
            }
            if ((i & 16) != 0) {
                int i10 = rect.top;
                i9 = i10 + (((rect.bottom - i10) - size.getHeight()) / 2);
                size.getHeight();
            }
        } else {
            if (i2 == 90) {
                int i11 = rect.left;
                int i12 = rect.top;
                size.getHeight();
                size.getWidth();
                if ((i & 1) != 0) {
                    i12 = rect.top;
                    size.getWidth();
                }
                if ((i & 2) != 0) {
                    i11 = rect.right - size.getHeight();
                }
                if ((i & 4) != 0) {
                    i12 = rect.bottom - size.getWidth();
                }
                if ((i & 8) != 0) {
                    i11 = rect.left;
                    size.getHeight();
                }
                if ((i & 16) != 0) {
                    int i13 = rect.left;
                    i11 = i13 + (((rect.right - i13) - size.getHeight()) / 2);
                    size.getHeight();
                }
            } else if (i2 != 180) {
                if (i2 == 270) {
                    i7 = rect.left;
                    i6 = rect.top;
                    size.getHeight();
                    size.getWidth();
                    if ((i & 1) != 0) {
                        i6 = rect.bottom - size.getWidth();
                    }
                    if ((i & 2) != 0) {
                        i7 = rect.left;
                        size.getHeight();
                    }
                    if ((i & 4) != 0) {
                        i6 = rect.top;
                        size.getWidth();
                    }
                    if ((i & 8) != 0) {
                        i7 = rect.right - size.getHeight();
                    }
                    if ((i & 16) != 0) {
                        int i14 = rect.left;
                        i7 = i14 + (((rect.right - i14) - size.getHeight()) / 2);
                        size.getHeight();
                    }
                }
                return layoutParams;
            } else {
                i5 = rect.left;
                i4 = rect.top;
                size.getWidth();
                size.getHeight();
                if ((i & 1) != 0) {
                    i5 = rect.right - size.getWidth();
                }
                if ((i & 2) != 0) {
                    i4 = rect.bottom - size.getHeight();
                }
                if ((i & 4) != 0) {
                    i5 = rect.left;
                    size.getWidth();
                }
                if ((i & 8) != 0) {
                    i4 = rect.top;
                    size.getHeight();
                }
                if ((i & 16) != 0) {
                    int i15 = rect.top;
                    i4 = i15 + (((rect.bottom - i15) - size.getHeight()) / 2);
                    size.getHeight();
                }
            }
            int i16 = rect.top;
            i6 = i16 + (((rect.bottom - i16) - size.getWidth()) / 2);
            size.getWidth();
            layoutParams.leftMargin = i7;
            layoutParams.topMargin = i6;
            layoutParams.width = size.getHeight();
            i3 = size.getWidth();
            layoutParams.height = i3;
            return layoutParams;
        }
        int i17 = rect.left;
        i5 = i17 + (((rect.right - i17) - size.getWidth()) / 2);
        size.getWidth();
        layoutParams.leftMargin = i5;
        layoutParams.topMargin = i4;
        layoutParams.width = size.getWidth();
        i3 = size.getHeight();
        layoutParams.height = i3;
        return layoutParams;
    }

    private FrameLayout.LayoutParams getLayoutParamsForExtent(FrameLayout.LayoutParams layoutParams, Rect rect, Size size, int i, int i2) {
        int i3 = rect.left;
        int i4 = rect.top;
        int dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.extend_watermark_text_margin_bottom);
        if (i2 == -1 || i2 == 0) {
            if (i == -1) {
                int i5 = rect.top;
                i4 = i5 + (((rect.bottom - i5) - size.getHeight()) / 2);
            } else {
                i4 = rect.bottom - (dimensionPixelOffset + size.getHeight());
            }
            int i6 = rect.left;
            i3 = i6 + (((rect.right - i6) - size.getWidth()) / 2);
            layoutParams.width = size.getWidth();
            layoutParams.height = size.getHeight();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("getLayoutParamsForExtent failed, unsupported degree:");
            sb.append(i2);
            Log.w(TAG, sb.toString());
        }
        layoutParams.leftMargin = i3;
        layoutParams.topMargin = i4;
        return layoutParams;
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0044  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x004d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private String getLocationText(String str, View view) {
        char c;
        TextView textView;
        int i;
        switch (str.hashCode()) {
            case 37820169:
                if (str.equals(WatermarkConstant.LOCATION_TIME_1)) {
                    c = 1;
                    break;
                }
            case 37820170:
                if (str.equals(WatermarkConstant.LOCATION_TIME_2)) {
                    c = 2;
                    break;
                }
            case 1901043637:
                if (str.equals("location")) {
                    c = 0;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        if (c == 0) {
            i = R.id.location_text;
        } else if (c == 1) {
            i = R.id.location_time_1_location;
        } else if (c != 2) {
            textView = null;
            return textView == null ? textView.getText().toString() : "";
        } else {
            i = R.id.location_time_2_location;
        }
        textView = (TextView) view.findViewById(i);
        if (textView == null) {
        }
    }

    private RectF getMergeRect(RectF rectF, RectF rectF2) {
        float max = Math.max(rectF.left, rectF2.left);
        float min = Math.min(rectF.right, rectF2.right);
        this.mergeRectF.set(max, Math.max(rectF.top, rectF2.top), min, Math.min(rectF.bottom, rectF2.bottom));
        return this.mergeRectF;
    }

    private FrameLayout initWatermarkLayout(ViewStub viewStub) {
        Log.d(TAG, "initWatermarkLayout");
        if (viewStub != null) {
            return (FrameLayout) viewStub.inflate();
        }
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x001e, code lost:
        if (r8 != 270) goto L_0x0046;
     */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x004a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int[] initWatermarkLocation(FrameLayout.LayoutParams layoutParams, Size size, Rect rect, int i) {
        DragListener dragListener;
        int[] iArr = new int[4];
        iArr[0] = layoutParams.leftMargin;
        iArr[1] = layoutParams.topMargin;
        if (!(i == -1 || i == 0)) {
            if (i != 90) {
                if (i != 180) {
                }
            }
            iArr[2] = layoutParams.leftMargin + size.getHeight();
            iArr[3] = layoutParams.topMargin + size.getWidth();
            dragListener = this.mListener;
            if (dragListener != null) {
                dragListener.reInit(rect, iArr);
            }
            return iArr;
        }
        iArr[2] = layoutParams.leftMargin + size.getWidth();
        iArr[3] = layoutParams.topMargin + size.getHeight();
        dragListener = this.mListener;
        if (dragListener != null) {
        }
        return iArr;
    }

    private boolean isMimojiFaceDetectTip() {
        boolean z = this.mIsMimojiFaceDetectTip;
        this.mIsMimojiFaceDetectTip = false;
        return z;
    }

    private boolean isRectIntersect(RectF rectF, RectF rectF2) {
        return rectF2.right >= rectF.left && rectF2.left <= rectF.right && rectF2.bottom >= rectF.top && rectF2.top <= rectF.bottom;
    }

    private boolean isReferenceLineEnabled() {
        return DataRepository.dataItemGlobal().getBoolean(CameraSettings.KEY_REFERENCE_LINE, false);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00aa, code lost:
        if (com.android.camera.features.mimoji2.widget.helper.MimojiHelper2.getTipsResId(r6.mCurrentMimojiFaceResult) == -1) goto L_0x00ac;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void mimojiFaceDetectSync(int i, int i2) {
        int i3;
        int i4;
        if (this.mCurrentMode == 184) {
            i4 = MimojiHelper2.getTipsResIdFace(i2);
            i3 = MimojiHelper2.getTipsResId(i2);
        } else {
            i4 = MimojiHelper.getTipsResIdFace(i2);
            i3 = MimojiHelper.getTipsResId(i2);
        }
        if (160 == i && i3 == -1 && i2 != 6) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("mimojiFaceDetectSync 0, faceResult = ");
            sb.append(i2);
            sb.append(", mimoji tips resId = ");
            sb.append(i3);
            Log.c(str, sb.toString());
        } else if (161 == i && i4 == -1 && i2 != 6) {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("mimojiFaceDetectSync 1, faceResult = ");
            sb2.append(i2);
            sb2.append(", miface tips resId = ");
            sb2.append(i4);
            Log.c(str2, sb2.toString());
        } else if (i2 == this.mMimojiFaceDetect && i == this.mMimojiDetectTipType) {
            setMimojiFaceDetectTip();
            String str3 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("mimojiFaceDetectSync 2, faceResult = ");
            sb3.append(i2);
            sb3.append("type:");
            sb3.append(i);
            Log.c(str3, sb3.toString());
        } else {
            this.mMimojiDetectTipType = i;
            this.mMimojiFaceDetect = i2;
            setMimojiFaceDetectTip();
            if (this.mCurrentMode == 184) {
                if (i2 == 6) {
                }
                this.mLastFaceSuccess = false;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("face_detect_type:");
                sb4.append(i);
                sb4.append(",result:");
                sb4.append(i2);
                sb4.append(",is_face_location_ok:");
                sb4.append(this.mLastFaceSuccess);
                Log.d("mimojiFaceDetectSync", sb4.toString());
            }
            if (i2 == 6 && MimojiHelper.getTipsResId(this.mCurrentMimojiFaceResult) == -1) {
            }
            this.mLastFaceSuccess = false;
            StringBuilder sb42 = new StringBuilder();
            sb42.append("face_detect_type:");
            sb42.append(i);
            sb42.append(",result:");
            sb42.append(i2);
            sb42.append(",is_face_location_ok:");
            sb42.append(this.mLastFaceSuccess);
            Log.d("mimojiFaceDetectSync", sb42.toString());
            this.mLastFaceSuccess = true;
            StringBuilder sb422 = new StringBuilder();
            sb422.append("face_detect_type:");
            sb422.append(i);
            sb422.append(",result:");
            sb422.append(i2);
            sb422.append(",is_face_location_ok:");
            sb422.append(this.mLastFaceSuccess);
            Log.d("mimojiFaceDetectSync", sb422.toString());
        }
    }

    private boolean needMoveWatermark(WatermarkItem watermarkItem, int i) {
        return i == 0 && watermarkItem != null && (watermarkItem.getLocation() & 8) != 0 && !watermarkItem.hasMove();
    }

    private boolean needReferenceLineMode() {
        int i = this.mCurrentMode;
        return (i == 254 || i == 204) ? false : true;
    }

    private boolean needShowZoomView(int i) {
        if (i == 180 || i == 214) {
            return true;
        }
        return i == 162 && this.mIsRecording && C0122O00000o.instance().O000O00o(i) && !isAutoZoomEnabled() && !CameraSettings.isSuperEISEnabled(162) && DataRepository.dataItemGlobal().getCurrentCameraId() != 1;
    }

    private void onZoomViewOrientationChanged(int i) {
        ZoomView zoomView;
        ZoomView zoomView2;
        boolean z = (i + 180) % 180 != 0;
        if (z == this.mIsHorizontal || !needShowZoomView(this.mCurrentMode)) {
            this.mIsHorizontal = z;
            return;
        }
        if (this.mIsHorizontal) {
            this.mZoomView.show(this.mDegree);
            this.mZoomViewHorizontal.hide();
            zoomView2 = this.mZoomView;
            zoomView = this.mZoomViewHorizontal;
        } else {
            this.mZoomView.hide();
            this.mZoomViewHorizontal.show(this.mDegree);
            zoomView2 = this.mZoomViewHorizontal;
            zoomView = this.mZoomView;
        }
        zoomView2.setCurrentZoomRatio(zoomView.getCurrentZoomRatio());
        this.mIsHorizontal = z;
    }

    private void reSizeTextView(TextView textView) {
        this.mHandler.post(new O0000o00(textView));
    }

    private void resetSmoothZoom() {
        ZoomView zoomView = this.mZoomViewHorizontal;
        if (zoomView != null) {
            zoomView.hide();
        }
        ZoomView zoomView2 = this.mZoomView;
        if (zoomView2 != null) {
            zoomView2.hide();
        }
        KeyEvent keyEvent = (KeyEvent) ModeCoordinatorImpl.getInstance().getAttachProtocol(239);
        if (keyEvent != null) {
            keyEvent.resetZoomKeyEvent();
        }
    }

    private void setMimojiFaceDetectTip() {
        this.mIsMimojiFaceDetectTip = true;
    }

    private void setText(TextView textView, String str) {
        if (str != null) {
            textView.setText(str);
        } else {
            Log.w(TAG, "warning text is null please check");
        }
    }

    private void setViewMargin(View view, int i) {
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        marginLayoutParams.topMargin = i;
        view.setLayoutParams(marginLayoutParams);
        ViewCompat.setTranslationY(view, 0.0f);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0089, code lost:
        r7.setText(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00fa, code lost:
        setText(r7, r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0186, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void setWatermarkText(ViewGroup viewGroup, String[] strArr, String str) {
        TextView textView;
        String str2;
        String str3;
        TextView textView2;
        if (strArr != null && strArr.length != 0) {
            char c = 65535;
            switch (str.hashCode()) {
                case 3560141:
                    if (str.equals("time")) {
                        c = 1;
                        break;
                    }
                    break;
                case 37820169:
                    if (str.equals(WatermarkConstant.LOCATION_TIME_1)) {
                        c = 5;
                        break;
                    }
                    break;
                case 37820170:
                    if (str.equals(WatermarkConstant.LOCATION_TIME_2)) {
                        c = 6;
                        break;
                    }
                    break;
                case 1888438524:
                    if (str.equals(WatermarkConstant.LONGITUDE_LATITUDE)) {
                        c = 7;
                        break;
                    }
                    break;
                case 1901043637:
                    if (str.equals("location")) {
                        c = 4;
                        break;
                    }
                    break;
                case 2089596377:
                    if (str.equals(WatermarkConstant.EXTEND_TIME_1)) {
                        c = 2;
                        break;
                    }
                    break;
                case 2089596380:
                    if (str.equals(WatermarkConstant.EXTEND_TIME_2)) {
                        c = 3;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 2:
                    TextView textView3 = (TextView) viewGroup.findViewById(R.id.extend_hour);
                    textView = (TextView) viewGroup.findViewById(R.id.extend_month);
                    if (Util.isGlobalVersion()) {
                        textView3.setTextSize(0, getResources().getDimension(R.dimen.extend_time_watermark_1_hour_text_size) * 0.96f);
                    }
                    textView3.setText(strArr[0]);
                    str2 = strArr[1];
                    break;
                case 3:
                    TextView textView4 = (TextView) viewGroup.findViewById(R.id.extend_day);
                    textView = (TextView) viewGroup.findViewById(R.id.extend_week);
                    if (Util.isGlobalVersion()) {
                        Typeface create = Typeface.create(CustomTextWaterMark.FONT_SANS_SERIF_LIGHT, 1);
                        textView4.setTextSize(0, getResources().getDimension(R.dimen.extend_time_watermark_2_day_text_size) * 0.85f);
                        textView4.setTypeface(create);
                    }
                    textView4.setText(strArr[0]);
                    str2 = strArr[1].toUpperCase(Locale.ENGLISH);
                    break;
                case 4:
                    TextView textView5 = (TextView) viewGroup.findViewById(R.id.location_text);
                    setText(textView5, strArr[0]);
                    textView5.invalidate();
                    break;
                case 5:
                    TextView textView6 = (TextView) viewGroup.findViewById(R.id.location_time_1_day);
                    TextView textView7 = (TextView) viewGroup.findViewById(R.id.location_time_1_time);
                    textView2 = (TextView) viewGroup.findViewById(R.id.location_time_1_location);
                    setText(textView6, strArr[0]);
                    setText(textView7, strArr[1]);
                    str3 = strArr[2];
                case 6:
                    TextView textView8 = (TextView) viewGroup.findViewById(R.id.location_time_2_location);
                    textView2 = (TextView) viewGroup.findViewById(R.id.location_time_2_time);
                    setText(textView8, strArr[0]);
                    str3 = strArr[1];
                case 7:
                    TextView textView9 = (TextView) viewGroup.findViewById(R.id.longitude);
                    TextView textView10 = (TextView) viewGroup.findViewById(R.id.latitude);
                    textView2 = (TextView) viewGroup.findViewById(R.id.altitude);
                    setText(textView9, strArr[0]);
                    setText(textView10, strArr[1]);
                    if (strArr.length >= 3) {
                        str3 = strArr[2];
                    }
                    break;
                default:
                    TextView textView11 = (TextView) viewGroup.findViewById(R.id.month);
                    TextView textView12 = (TextView) viewGroup.findViewById(R.id.date);
                    textView = (TextView) viewGroup.findViewById(R.id.hour);
                    textView11.setText(strArr[0].toUpperCase(Locale.ENGLISH));
                    textView12.setText(strArr[1]);
                    str2 = strArr[2];
                    break;
            }
        } else {
            Log.w(TAG, "There is no data to display");
        }
    }

    private void showIndicator(FocusIndicator focusIndicator, int i) {
        if (i == 1) {
            focusIndicator.showStart();
        } else if (i == 2) {
            focusIndicator.showSuccess();
        } else if (i == 3) {
            focusIndicator.showFail();
        }
    }

    private void showWatermarkItem(FrameLayout frameLayout, boolean z) {
        boolean isDualWatermark = DataRepository.dataItemRunning().getComponentRunningAIWatermark().isDualWatermark();
        int i = 8;
        int i2 = (!isDualWatermark || !z) ? 8 : 0;
        StringBuilder sb = new StringBuilder();
        sb.append("showWatermarkItem visible:");
        sb.append(z);
        sb.append(" isDual:");
        sb.append(isDualWatermark);
        sb.append(" visibility:");
        sb.append(i2);
        Log.d(TAG, sb.toString());
        if (frameLayout != null) {
            View findViewById = frameLayout.findViewById(R.id.watermark_sample_image_f);
            if (z) {
                i = 0;
            }
            findViewById.setVisibility(i);
            frameLayout.findViewById(R.id.extend_time_watermark_1).setVisibility(i2);
            frameLayout.findViewById(R.id.extend_time_watermark_2).setVisibility(i2);
            showWatermarkItem(isDualWatermark, z, frameLayout.findViewById(R.id.time_watermark), frameLayout.findViewById(R.id.location), frameLayout.findViewById(R.id.location_time_1), frameLayout.findViewById(R.id.location_time_2), frameLayout.findViewById(R.id.longitude_latitude));
        }
    }

    private void showWatermarkItem(boolean z, boolean z2, View... viewArr) {
        for (View view : viewArr) {
            int i = 8;
            int i2 = z2 ? 0 : 8;
            if (!z) {
                i = i2;
            }
            view.setVisibility(i);
        }
    }

    private void showWatermarkTipsIfNeeded(WatermarkItem watermarkItem, WatermarkItem watermarkItem2) {
        if (watermarkItem != null && watermarkItem.getType() == 11) {
            String key = watermarkItem.getKey();
            String str = WatermarkConstant.SUPER_MOON_RESET;
            if (TextUtils.equals(key, str)) {
                return;
            }
            if (watermarkItem2 == null || TextUtils.equals(watermarkItem2.getKey(), str)) {
                TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
                if (topAlert != null) {
                    topAlert.alertAiDetectTipHint(0, R.string.super_moon_improve_effect_tips, 3000);
                }
            }
        }
    }

    private void updateImageWatermarkItemRotation(ViewStub viewStub, FrameLayout frameLayout, int i, ComponentRunningAIWatermark componentRunningAIWatermark, WatermarkItem watermarkItem) {
        Size size;
        String str = TAG;
        Log.d(str, "updateWatermarkItemRotation E");
        if (viewStub != null && frameLayout != null && watermarkItem != null) {
            int i2 = 0;
            watermarkItem.setHasMove(false);
            StringBuilder sb = new StringBuilder();
            sb.append("updateWatermarkItemRotation item:");
            sb.append(watermarkItem.getType());
            sb.append(" ,key:");
            sb.append(watermarkItem.getKey());
            Log.d(str, sb.toString());
            int location = watermarkItem.getLocation();
            Rect displayRect = Util.getDisplayRect();
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) frameLayout.getLayoutParams();
            Size bitmapSize = Util.getBitmapSize(watermarkItem.getResId());
            if (!componentRunningAIWatermark.isDualWatermark()) {
                size = new Size((int) (((float) bitmapSize.getWidth()) * this.mWatermarkScaleSize), (int) (((float) bitmapSize.getHeight()) * this.mWatermarkScaleSize));
            } else {
                if (watermarkItem.getType() == 11) {
                    bitmapSize = new Size(displayRect.right - displayRect.left, displayRect.bottom - displayRect.top);
                }
                size = bitmapSize;
            }
            getLayoutParams(layoutParams, location, displayRect, size, this.mWatermarkDegree);
            int[] initWatermarkLocation = initWatermarkLocation(layoutParams, size, displayRect, i);
            if (componentRunningAIWatermark.isFixedLocation()) {
                componentRunningAIWatermark.updateLocation(initWatermarkLocation, displayRect, watermarkItem.getType());
                watermarkItem.setCaptureCoordinate(initWatermarkLocation);
            } else {
                calculateLP(watermarkItem, i, layoutParams);
            }
            frameLayout.setLayoutParams(layoutParams);
            frameLayout.invalidate();
            showWatermarkItem(frameLayout, false);
            ImageView imageView = (ImageView) frameLayout.findViewById(R.id.watermark_sample_image_f);
            imageView.setImageBitmap(Util.rotate(Util.convertResToBitmap(watermarkItem.getResId()), i));
            imageView.setVisibility(0);
            updateTextBitmap(watermarkItem, null);
            if (watermarkItem.getText() != null) {
                i2 = getResources().getIdentifier(watermarkItem.getText(), "string", CameraAppImpl.getAndroidContext().getPackageName());
            }
            if (i2 <= 0) {
                i2 = R.string.lighting_pattern_null;
            }
            imageView.setContentDescription(getString(i2));
            doAccessible(frameLayout);
            Log.d(str, "updateWatermarkItemRotation X");
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x006b, code lost:
        r3 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x006d, code lost:
        r0.alertTopHint(4, (int) com.android.camera.R.string.aiwatermark_location_absent_hint);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0079, code lost:
        if (com.android.camera.LocationManager.instance().getCurrentLocationDirectly() == null) goto L_0x0097;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0095, code lost:
        if (android.text.TextUtils.equals(com.android.camera.aiwatermark.util.WatermarkConstant.DEFAULT_LOCATION, r1) == false) goto L_0x006d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0097, code lost:
        r0.alertTopHint(0, (int) com.android.camera.R.string.aiwatermark_location_absent_hint);
        r9.setVisibility(4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x009e, code lost:
        r7.mHandler.post(new com.android.camera.fragment.C0286O0000o0o(r8, r3, r9));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00a8, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateTextBitmap(WatermarkItem watermarkItem, View view) {
        char c;
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        String key = watermarkItem.getKey();
        boolean z = true;
        switch (key.hashCode()) {
            case 3560141:
                if (key.equals("time")) {
                    c = 4;
                    break;
                }
            case 37820169:
                if (key.equals(WatermarkConstant.LOCATION_TIME_1)) {
                    c = 1;
                    break;
                }
            case 37820170:
                if (key.equals(WatermarkConstant.LOCATION_TIME_2)) {
                    c = 2;
                    break;
                }
            case 1888438524:
                if (key.equals(WatermarkConstant.LONGITUDE_LATITUDE)) {
                    c = 3;
                    break;
                }
            case 1901043637:
                if (key.equals("location")) {
                    c = 0;
                    break;
                }
            case 2089596377:
                if (key.equals(WatermarkConstant.EXTEND_TIME_1)) {
                    c = 5;
                    break;
                }
            case 2089596380:
                if (key.equals(WatermarkConstant.EXTEND_TIME_2)) {
                    c = 6;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
                String locationText = getLocationText(watermarkItem.getKey(), view);
                if (!watermarkItem.getLocationList().isEmpty()) {
                    break;
                }
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
            case 6:
                break;
            default:
                topAlert.alertTopHint(4, (int) R.string.aiwatermark_location_absent_hint);
                break;
        }
    }

    private void updateTextWatermarkItemRotation(ViewStub viewStub, FrameLayout frameLayout, int i, ComponentRunningAIWatermark componentRunningAIWatermark, WatermarkItem watermarkItem) {
        String str = TAG;
        Log.d(str, "updateTextWatermarkItemRotation E");
        if (viewStub != null && frameLayout != null && watermarkItem != null) {
            watermarkItem.setHasMove(false);
            StringBuilder sb = new StringBuilder();
            sb.append("updateTextWatermarkItemRotation item:");
            sb.append(watermarkItem.getType());
            sb.append(" ,key:");
            sb.append(watermarkItem.getKey());
            Log.d(str, sb.toString());
            String key = watermarkItem.getKey();
            LinearLayout layoutByKey = getLayoutByKey(frameLayout, key);
            setWatermarkText(layoutByKey, watermarkItem.getWatermarkText(watermarkItem.getKey(), Util.get24HourMode(frameLayout.getContext())), key);
            showWatermarkItem(frameLayout, false);
            layoutByKey.setVisibility(0);
            frameLayout.setVisibility(0);
            if (TextUtils.equals(key, "location") || TextUtils.equals(key, WatermarkConstant.LOCATION_TIME_1) || TextUtils.equals(key, WatermarkConstant.LOCATION_TIME_2) || TextUtils.equals(key, WatermarkConstant.LONGITUDE_LATITUDE)) {
                autoRelayoutUI(key, layoutByKey, watermarkItem.hasAltitude());
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("in layout.getWidth=");
            sb2.append(layoutByKey.getWidth());
            Log.d(str, sb2.toString());
            StringBuilder sb3 = new StringBuilder();
            sb3.append("out watermarkLayout.getWidth=");
            sb3.append(this.mWatermarkLayout.getWidth());
            Log.d(str, sb3.toString());
            Size size = new Size(layoutByKey.getWidth(), layoutByKey.getHeight());
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) frameLayout.getLayoutParams();
            Rect displayRect = Util.getDisplayRect();
            getLayoutParams(layoutParams, watermarkItem.getLocation(), displayRect, size, this.mWatermarkDegree);
            int[] initWatermarkLocation = initWatermarkLocation(layoutParams, size, displayRect, i);
            if (componentRunningAIWatermark.isFixedLocation()) {
                componentRunningAIWatermark.updateLocation(initWatermarkLocation, displayRect, watermarkItem.getType());
                watermarkItem.setCaptureCoordinate(initWatermarkLocation);
            } else {
                calculateLP(watermarkItem, i, layoutParams);
            }
            frameLayout.setLayoutParams(layoutParams);
            frameLayout.invalidate();
            layoutByKey.setContentDescription(watermarkItem.getTimeWatermarkString());
            layoutByKey.setRotation((float) i);
            updateTextBitmap(watermarkItem, layoutByKey);
            doAccessible(frameLayout);
            Log.d(str, "updateTextWatermarkItemRotation X");
        }
    }

    private void updateWatermarkItemBackground(ComponentRunningAIWatermark componentRunningAIWatermark) {
        int i;
        Resources resources;
        int i2;
        String str = TAG;
        Log.d(str, "updateWatermarkItemBackground E");
        if (this.mWatermarkBackgroundViewStub != null && this.mWatermarkBackgroundLayout != null) {
            WatermarkItem majorWatermarkItem = componentRunningAIWatermark.getMajorWatermarkItem();
            WatermarkItem minorWatermarkItem = componentRunningAIWatermark.getMinorWatermarkItem();
            String str2 = WatermarkConstant.SUPER_MOON_RESET;
            boolean z = (majorWatermarkItem != null && !TextUtils.equals(str2, majorWatermarkItem.getKey())) || (minorWatermarkItem != null && !TextUtils.equals(str2, minorWatermarkItem.getKey()));
            if (!componentRunningAIWatermark.isDualWatermark() || !z) {
                this.mWatermarkBackgroundLayout.setVisibility(8);
                this.mWatermarkBackgroundLayout.findViewById(R.id.watermark_sample_background_image_f).setVisibility(8);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("updateWatermarkItemBackground type:");
                sb.append(majorWatermarkItem.getType());
                Log.d(str, sb.toString());
                Rect displayRect = Util.getDisplayRect();
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mWatermarkBackgroundLayout.getLayoutParams();
                if (TextUtils.equals(majorWatermarkItem.getKey(), WatermarkConstant.SUPER_MOON_WINDOW)) {
                    i2 = getResources().getDimensionPixelOffset(R.dimen.extend_watermark_special_background_margin_left);
                    resources = getResources();
                    i = R.dimen.extend_watermark_special_background_margin_top;
                } else {
                    i2 = getResources().getDimensionPixelOffset(R.dimen.extend_watermark_background_margin_left);
                    resources = getResources();
                    i = R.dimen.extend_watermark_background_margin_top;
                }
                int dimensionPixelOffset = resources.getDimensionPixelOffset(i);
                Size bitmapSize = Util.getBitmapSize(R.drawable.bg_super_moon_effect);
                int i3 = displayRect.left;
                float f = (float) i2;
                float f2 = this.mSuperMoonScaleSize;
                layoutParams.leftMargin = i3 + ((int) (f * f2));
                layoutParams.topMargin = displayRect.top + ((int) (((float) dimensionPixelOffset) * f2));
                layoutParams.width = (int) (((float) bitmapSize.getWidth()) * this.mSuperMoonScaleSize);
                layoutParams.height = (int) (((float) bitmapSize.getHeight()) * this.mSuperMoonScaleSize);
                this.mWatermarkBackgroundLayout.setLayoutParams(layoutParams);
                this.mWatermarkBackgroundLayout.invalidate();
                this.mWatermarkBackgroundLayout.setVisibility(0);
                this.mWatermarkBackgroundLayout.findViewById(R.id.watermark_sample_background_image_f).setVisibility(0);
            }
            Log.d(str, "updateWatermarkItemBackground X");
        }
    }

    /* access modifiers changed from: private */
    public void updateWatermarkRotation(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("updateWatermarkRotation degree = ");
        sb.append(i);
        Log.d(TAG, sb.toString());
        ComponentRunningAIWatermark componentRunningAIWatermark = DataRepository.dataItemRunning().getComponentRunningAIWatermark();
        if (componentRunningAIWatermark != null) {
            WatermarkItem majorWatermarkItem = componentRunningAIWatermark.getMajorWatermarkItem();
            if (majorWatermarkItem != null) {
                boolean isTextWatermark = majorWatermarkItem.isTextWatermark();
                ViewStub viewStub = this.mWatermarkViewStub;
                FrameLayout frameLayout = this.mWatermarkLayout;
                int i2 = this.mWatermarkDegree;
                if (isTextWatermark) {
                    updateTextWatermarkItemRotation(viewStub, frameLayout, i2, componentRunningAIWatermark, majorWatermarkItem);
                } else {
                    updateImageWatermarkItemRotation(viewStub, frameLayout, i2, componentRunningAIWatermark, majorWatermarkItem);
                }
            }
            WatermarkItem minorWatermarkItem = componentRunningAIWatermark.getMinorWatermarkItem();
            if (minorWatermarkItem != null) {
                boolean isTextWatermark2 = minorWatermarkItem.isTextWatermark();
                ViewStub viewStub2 = this.mWatermarkExtendViewStub;
                FrameLayout frameLayout2 = this.mWatermarkExtendLayout;
                int i3 = this.mWatermarkDegree;
                if (isTextWatermark2) {
                    updateTextWatermarkItemRotation(viewStub2, frameLayout2, i3, componentRunningAIWatermark, minorWatermarkItem);
                } else {
                    updateImageWatermarkItemRotation(viewStub2, frameLayout2, i3, componentRunningAIWatermark, minorWatermarkItem);
                }
            }
        }
    }

    public /* synthetic */ boolean O00000o(View view, MotionEvent motionEvent) {
        if (Util.isAccessible()) {
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (isAdded() && topAlert != null && topAlert.isExtraMenuShowing()) {
                topAlert.hideExtraMenu();
                return true;
            }
        }
        return false;
    }

    public /* synthetic */ void O00000o0(View view, View view2) {
        WatermarkProtocol watermarkProtocol = (WatermarkProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(253);
        if (watermarkProtocol != null && watermarkProtocol.isWatermarkPanelShow()) {
            watermarkProtocol.dismiss(2, 6);
        }
        view.postDelayed(new O0000o0(this, view), 100);
    }

    public /* synthetic */ void O0000Oo0(View view) {
        if (isAdded()) {
            view.sendAccessibilityEvent(128);
        }
    }

    public /* synthetic */ void O0000o0(boolean z) {
        this.mIvIdPhotoBox.setVisibility(z ? 0 : 8);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0045, code lost:
        if (r1 > 0) goto L_0x0064;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0062, code lost:
        if (r1 > 0) goto L_0x0064;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0064, code lost:
        r0.alertMimojiFaceDetect(true, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x009c, code lost:
        if (r1.isInMimojiCreate() == false) goto L_0x00cf;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00cc, code lost:
        if (r1.IsInMimojiCreate() == false) goto L_0x00cf;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0028, code lost:
        if (r7 != false) goto L_0x0064;
     */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00e0  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00e6  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ void O0000o0O(boolean z) {
        int i;
        TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        boolean z2 = true;
        switch (this.mMimojiDetectTipType) {
            case 160:
                if (isMimojiFaceDetectTip()) {
                    i = this.mCurrentMode == 184 ? MimojiHelper2.getTipsResId(this.mMimojiFaceDetect) : MimojiHelper.getTipsResId(this.mMimojiFaceDetect);
                    if (topAlert != null) {
                    }
                } else {
                    return;
                }
                break;
            case 161:
                if (isMimojiFaceDetectTip()) {
                    i = this.mCurrentMode == 184 ? MimojiHelper2.getTipsResIdFace(this.mMimojiFaceDetect) : MimojiHelper.getTipsResIdFace(this.mMimojiFaceDetect);
                    if (topAlert != null) {
                    }
                } else {
                    return;
                }
                break;
            case 162:
                i = this.mCurrentMode == 184 ? MimojiHelper2.getTipsResIdFace(7) : MimojiHelper.getTipsResIdFace(7);
                if (topAlert != null) {
                    if (i != -1) {
                    }
                }
                break;
        }
        if (this.mCurrentMode == 184) {
            MimojiAvatarEngine2 mimojiAvatarEngine2 = (MimojiAvatarEngine2) ModeCoordinatorImpl.getInstance().getAttachProtocol(246);
            if (mimojiAvatarEngine2 != null) {
                mimojiAvatarEngine2.setDetectSuccess(this.mLastFaceSuccess);
                if (this.mLastFaceSuccess && !z) {
                    MimojiStatusManager2 mimojiStatusManager2 = DataRepository.dataItemLive().getMimojiStatusManager2();
                    if (topAlert != null) {
                        if (mimojiStatusManager2.getMimojiPanelState() != 0) {
                        }
                        topAlert.alertMimojiFaceDetect(true, R.string.mimoji_check_normal);
                    }
                }
            }
            if (!this.mLastFaceSuccess || z) {
                z2 = false;
            }
            if (z2) {
                this.mMimojiLightingView.triggerAnimateSuccess();
            } else if (!(this.mMimojiLastFaceSuccess == this.mLastFaceSuccess && this.mIsMimojiCreateLowLight == z)) {
                this.mMimojiLightingView.triggerAnimateStart();
            }
            this.mIsMimojiCreateLowLight = z;
            this.mMimojiLastFaceSuccess = this.mLastFaceSuccess;
        }
        MimojiAvatarEngine mimojiAvatarEngine = (MimojiAvatarEngine) ModeCoordinatorImpl.getInstance().getAttachProtocol(217);
        if (mimojiAvatarEngine != null) {
            mimojiAvatarEngine.setDetectSuccess(this.mLastFaceSuccess);
            if (this.mLastFaceSuccess && !z) {
                MimojiStatusManager mimojiStatusManager = DataRepository.dataItemLive().getMimojiStatusManager();
                if (topAlert != null) {
                    if (!mimojiStatusManager.getMimojiPannelState()) {
                    }
                    topAlert.alertMimojiFaceDetect(true, R.string.mimoji_check_normal);
                }
            }
        }
        z2 = false;
        if (z2) {
        }
        this.mIsMimojiCreateLowLight = z;
        this.mMimojiLastFaceSuccess = this.mLastFaceSuccess;
        topAlert.alertMimojiFaceDetect(false, R.string.mimoji_check_normal);
        z2 = false;
        if (z2) {
        }
        this.mIsMimojiCreateLowLight = z;
        this.mMimojiLastFaceSuccess = this.mLastFaceSuccess;
    }

    public /* synthetic */ void O000oO0O() {
        this.mPreviewFrame.hidePreviewReferenceLine();
        this.mPreviewFrame.hidePreviewGradienter();
    }

    public /* synthetic */ void O000oO0o() {
        this.mCoverParent.getLayoutParams().height = this.mNormalCoverHeight;
        this.mCoverParent.requestLayout();
    }

    public void checkTouchRegionContainSplitFocusExposure(MotionEvent motionEvent) {
        FocusView focusView = this.mFocusView;
        if (focusView != null) {
            focusView.checkTouchRegionContainSplitFocusExposure(motionEvent);
        }
    }

    public void clearFocusView(int i) {
        this.mFocusView.clear(i);
    }

    public void clearIndicator(int i) {
        if (i == 1) {
            this.mFaceView.clear();
        } else if (i == 2) {
            throw new RuntimeException("not allowed call in this method");
        } else if (i == 3) {
            this.mObjectView.clear();
        }
    }

    public void destroyEffectCropView() {
        this.mEffectCropView.onDestroy();
    }

    public void feedData(AutoZoomCaptureResult autoZoomCaptureResult) {
        this.mAutoZoomOverlay.feedData(autoZoomCaptureResult);
    }

    public int getActiveIndicator() {
        return this.mActiveIndicator;
    }

    public List getFaceWaterMarkInfos() {
        return this.mFaceView.getFaceWaterMarkInfos();
    }

    public CameraHardwareFace[] getFaces() {
        return this.mFaceView.getFaces();
    }

    public RectF getFocusRect(int i) {
        if (i == 1) {
            return this.mFaceView.getFocusRect();
        }
        if (i == 3) {
            return this.mObjectView.getFocusRect();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getFragmentTag());
        sb.append(": unexpected type ");
        sb.append(i);
        Log.w(TAG, sb.toString());
        return new RectF();
    }

    public RectF getFocusRectInPreviewFrame() {
        return this.mObjectView.getFocusRectInPreviewFrame();
    }

    public int getFragmentInto() {
        return 243;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_main_content;
    }

    public View getReferenceLine() {
        V6PreviewFrame v6PreviewFrame = this.mPreviewFrame;
        if (v6PreviewFrame != null) {
            return v6PreviewFrame.getReferenceLine();
        }
        return null;
    }

    public RectF[] getViewRects(CameraSize cameraSize) {
        return this.mFaceView.getViewRects(cameraSize);
    }

    public void hideFaceAnimator() {
        FaceView faceView = this.mFaceView;
        if (faceView != null) {
            faceView.attemptHideFaceRect(300);
        }
    }

    public void hideOrShowDocument(boolean z) {
        DocumentView documentView = this.mDocumentView;
        if (documentView != null) {
            documentView.hideOrShowPath(z);
        }
    }

    public void hideReferenceGradienter() {
        if (this.mPreviewFrame != null) {
            this.mHandler.post(new O0000o(this));
        }
    }

    public void hideReviewViews() {
        if (this.mPreviewPanel.mVideoReviewImage.getVisibility() == 0) {
            Util.fadeOut(this.mPreviewPanel.mVideoReviewImage);
        }
        Util.fadeOut(this.mPreviewPanel.mVideoReviewPlay);
    }

    public void initEffectCropView() {
        this.mEffectCropView.onCreate();
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        this.mWatermarkScaleSize = CameraSettings.getResourceFloat(R.dimen.ai_watermark_scale_size, 1.0f);
        this.mSuperMoonScaleSize = WatermarkConstant.calcSuperMoonScale();
        this.mCoverParent = (ViewGroup) view.findViewById(R.id.cover_parent);
        this.mLeftCover = this.mCoverParent.findViewById(R.id.left_cover_layout);
        this.mRightCover = this.mCoverParent.findViewById(R.id.right_cover_layout);
        this.mMoreModeMaskView = view.findViewById(R.id.more_mode_mask);
        this.mPreviewPage = (ViewGroup) view.findViewById(R.id.v6_preview_page);
        this.mPreviewPanel = (V6PreviewPanel) this.mPreviewPage.findViewById(R.id.v6_preview_panel);
        this.mPreviewFrame = (V6PreviewFrame) this.mPreviewPanel.findViewById(R.id.v6_frame_layout);
        this.mPreviewCenterHint = (ViewGroup) this.mPreviewPanel.findViewById(R.id.center_hint_placeholder);
        this.mCenterHintIcon = (ImageView) this.mPreviewCenterHint.findViewById(R.id.center_hint_icon);
        this.mCenterHintText = (TextView) this.mPreviewCenterHint.findViewById(R.id.center_hint_text);
        this.mEffectCropView = (V6EffectCropView) this.mPreviewPanel.findViewById(R.id.v6_effect_crop_view);
        this.mFaceView = (FaceView) this.mPreviewPanel.findViewById(R.id.v6_face_view);
        this.mDocumentView = (DocumentView) this.mPreviewPanel.findViewById(R.id.document_view);
        this.mFocusView = (FocusView) this.mPreviewPanel.findViewById(R.id.v6_focus_view);
        this.mZoomView = (ZoomView) view.findViewById(R.id.v6_zoom_view);
        this.mZoomViewHorizontal = (ZoomView) view.findViewById(R.id.v6_zoom_view_horizontal);
        this.mZoomViewHorizontal.setIsHorizonal(true);
        this.mIvIdPhotoBox = (ImageView) view.findViewById(R.id.v6_id_photo_box);
        setViewMargin(this.mZoomViewHorizontal, ((Util.getDisplayRect(0).bottom + getResources().getDimensionPixelSize(R.dimen.mode_select_margin_top)) + (Math.round(((float) Display.getBottomBarHeight()) * 0.3f) / 2)) - (getResources().getDimensionPixelSize(R.dimen.pro_view_zoom_view_width) / 2));
        this.mZoomView.setZoomValueChangeListener(this);
        this.mZoomViewHorizontal.setZoomValueChangeListener(this);
        this.mAutoZoomOverlay = (AutoZoomView) this.mPreviewPanel.findViewById(R.id.autozoom_overlay);
        if (DataRepository.dataItemRunning().getComponentRunningLighting().getPortraitLightVersion() == 1) {
            this.mLightingView = (LightingView) this.mPreviewPanel.findChildrenById(R.id.lighting_view);
        }
        this.mObjectView = (ObjectView) this.mPreviewPanel.findViewById(R.id.object_view);
        this.mAfRegionsView = (AfRegionsView) this.mPreviewPanel.findViewById(R.id.afregions_view);
        this.mMimojiLightingView = (LightingView) this.mPreviewPanel.findChildrenById(R.id.mimoji_lighting_view);
        this.mMimojiLightingView.setCircleRatio(1.18f);
        this.mMimojiLightingView.setCircleHeightRatio(1.12f);
        boolean OOOoooo = C0122O00000o.instance().OOOoooo();
        boolean OOOOo0o = C0122O00000o.instance().OOOOo0o();
        if (OOOoooo || OOOOo0o) {
            this.mWatermarkViewStub = (ViewStub) view.findViewById(R.id.watermark_viewstub);
            this.mWatermarkLayout = initWatermarkLayout(this.mWatermarkViewStub);
            if (OOOOo0o) {
                this.mWatermarkExtendViewStub = (ViewStub) view.findViewById(R.id.watermark_extend_viewstub);
                this.mWatermarkExtendLayout = initWatermarkLayout(this.mWatermarkExtendViewStub);
                this.mWatermarkBackgroundViewStub = (ViewStub) view.findViewById(R.id.watermark_background_viewstub);
                ViewStub viewStub = this.mWatermarkBackgroundViewStub;
                if (viewStub != null) {
                    this.mWatermarkBackgroundLayout = (FrameLayout) viewStub.inflate();
                }
            }
        }
        LightingView lightingView = this.mLightingView;
        if (lightingView != null) {
            lightingView.setRotation(this.mDegree);
        }
        adjustViewHeight();
        this.mNormalCoverHeight = Display.getWindowHeight() - Display.getBottomHeight();
        this.mCoverParent.getLayoutParams().height = this.mNormalCoverHeight;
        this.mLeftCover.getLayoutParams().width = Util.getCinematicAspectRatioMargin();
        this.mRightCover.getLayoutParams().width = Util.getCinematicAspectRatioMargin();
        this.mIsIntentAction = DataRepository.dataItemGlobal().isIntentAction();
        provideAnimateElement(this.mCurrentMode, null, 2);
    }

    public void initializeFocusView(ExposureViewListener exposureViewListener) {
        this.mFocusView.initialize(exposureViewListener);
    }

    public boolean initializeObjectTrack(RectF rectF, boolean z) {
        this.mFocusView.clear();
        this.mObjectView.clear();
        this.mObjectView.setVisibility(0);
        return this.mObjectView.initializeTrackView(rectF, z);
    }

    public boolean initializeObjectView(RectF rectF, boolean z) {
        return this.mObjectView.initializeTrackView(rectF, z);
    }

    public boolean isAdjustingObjectView() {
        return this.mObjectView.isAdjusting();
    }

    public boolean isAutoZoomActive() {
        return this.mAutoZoomOverlay.isViewActive();
    }

    public boolean isAutoZoomEnabled() {
        return this.mAutoZoomOverlay.isViewEnabled();
    }

    public boolean isAutoZoomViewEnabled() {
        return this.mAutoZoomOverlay.isViewEnabled();
    }

    public boolean isEffectViewMoved() {
        return this.mEffectCropView.isMoved();
    }

    public boolean isEffectViewVisible() {
        return this.mEffectCropView.isVisible();
    }

    public boolean isEvAdjusted(boolean z) {
        FocusView focusView = this.mFocusView;
        return z ? focusView.isEvAdjustedTime() : focusView.isEvAdjusted();
    }

    public boolean isFaceExists(int i) {
        if (i == 1) {
            return this.mFaceView.faceExists();
        }
        if (i != 3) {
            return false;
        }
        return this.mObjectView.faceExists();
    }

    public boolean isFaceLocationOK() {
        return this.mLastFaceSuccess;
    }

    public boolean isFaceStable(int i) {
        if (i == 1) {
            return this.mFaceView.isFaceStable();
        }
        if (i != 3) {
            return false;
        }
        return this.mObjectView.isFaceStable();
    }

    public boolean isFocusViewMoving() {
        return this.mFocusView.isFocusViewMoving();
    }

    public boolean isFocusViewVisible() {
        return this.mFocusView.isVisible();
    }

    public boolean isIndicatorVisible(int i) {
        boolean z;
        boolean z2 = false;
        if (i == 1) {
            if (this.mFaceView.getVisibility() == 0) {
                z = true;
            }
            return z;
        } else if (i == 2) {
            if (this.mFocusView.getVisibility() == 0) {
                z2 = true;
            }
            return z2;
        } else if (i != 3) {
            return false;
        } else {
            if (this.mObjectView.getVisibility() == 0) {
                z2 = true;
            }
            return z2;
        }
    }

    public boolean isNeedExposure(int i) {
        FrameView frameView;
        if (i == 1) {
            frameView = this.mFaceView;
        } else if (i != 3) {
            return false;
        } else {
            frameView = this.mObjectView;
        }
        return frameView.isNeedExposure();
    }

    public boolean isObjectTrackFailed() {
        return this.mObjectView.isTrackFailed();
    }

    public boolean isShowReviewViews() {
        return this.mPreviewPanel.mVideoReviewImage.getVisibility() == 0;
    }

    public boolean isSplitFocusExposureDown() {
        FocusView focusView = this.mFocusView;
        if (focusView != null) {
            return focusView.isSplitFocusExposureDown();
        }
        return false;
    }

    public boolean isZoomAdjustVisible() {
        return this.mZoomView.isVisible() || this.mZoomViewHorizontal.isVisible();
    }

    public boolean isZoomViewMoving() {
        return this.mZoomView.isZoomMoving() || this.mZoomViewHorizontal.isZoomMoving();
    }

    public void lightingCancel() {
        LightingView lightingView = this.mLightingView;
        if (lightingView != null) {
            lightingView.triggerAnimateExit();
        }
        this.lastConfirmTime = -1;
        this.mFaceView.setLightingOn(false);
        this.mAfRegionsView.setLightingOn(false);
    }

    public void lightingDetectFace(CameraHardwareFace[] cameraHardwareFaceArr, boolean z) {
        LightingView lightingView = z ? this.mMimojiLightingView : this.mLightingView;
        if (lightingView != null) {
            int i = 5;
            if (cameraHardwareFaceArr == null || cameraHardwareFaceArr.length == 0 || cameraHardwareFaceArr.length > 1) {
                consumeResult(5, z);
            } else if (this.lastConfirmTime != -1) {
                this.mFaceView.transToViewRect(cameraHardwareFaceArr[0].rect, lightingView.getFaceViewRectF());
                RectF faceViewRectF = lightingView.getFaceViewRectF();
                RectF focusRectF = lightingView.getFocusRectF();
                if (isRectIntersect(faceViewRectF, focusRectF)) {
                    getMergeRect(faceViewRectF, focusRectF);
                    float width = faceViewRectF.width() * faceViewRectF.height();
                    float width2 = this.mergeRectF.width() * this.mergeRectF.height();
                    float width3 = focusRectF.width() * focusRectF.height();
                    float f = 1.0f;
                    float f2 = z ? 0.5f : 1.0f;
                    if (z) {
                        f = 1.5f;
                    }
                    float f3 = 0.2f * width3 * f2;
                    float f4 = width3 * 0.5f * f;
                    if (width2 >= 0.5f * width) {
                        int i2 = width2 < f3 ? 4 : (width2 >= f4 || width >= f4) ? 3 : 6;
                        i = i2;
                    }
                }
                consumeResult(i, z);
            }
        }
    }

    public void lightingFocused() {
        LightingView lightingView = this.mLightingView;
        if (lightingView != null) {
            lightingView.triggerAnimateSuccess();
        }
    }

    public void lightingStart() {
        LightingView lightingView = this.mLightingView;
        if (lightingView != null) {
            lightingView.setCinematicAspectRatio(CameraSettings.isCinematicAspectRatioEnabled(this.mCurrentMode));
            this.mLightingView.triggerAnimateStart();
        }
        this.lastFaceResult = -1;
        this.mLastFaceSuccess = false;
        this.lastConfirmTime = System.currentTimeMillis();
        this.mFaceView.setLightingOn(true);
        this.mAfRegionsView.setLightingOn(true);
    }

    public void mimojiEnd() {
        this.mMimojiLightingView.triggerAnimateExit();
    }

    public void mimojiFaceDetect(final int i) {
        this.mMimojiLightingView.post(new Runnable() {
            public void run() {
                FragmentMainContent.this.mCurrentMimojiFaceResult = i;
                FragmentMainContent.this.mimojiFaceDetectSync(160, i);
            }
        });
    }

    public void mimojiStart() {
        this.lastFaceResult = -1;
        this.mLastFaceSuccess = false;
        this.lastConfirmTime = System.currentTimeMillis();
        this.mFaceView.setLightingOn(true);
        this.mAfRegionsView.setLightingOn(true);
        this.mMimojiLightingView.triggerAnimateStart();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0042, code lost:
        if (r3 >= 0) goto L_0x0044;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0064, code lost:
        if (r3 <= (com.android.camera.Util.getDisplayRect().bottom - r2.mWatermarkLayout.getHeight())) goto L_0x0044;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void moveWatermarkLayout(int i, int i2) {
        int height;
        ComponentRunningAIWatermark componentRunningAIWatermark = DataRepository.dataItemRunning().getComponentRunningAIWatermark();
        if (this.mWatermarkLayout != null && !componentRunningAIWatermark.isDualWatermark() && needMoveWatermark(componentRunningAIWatermark.getMajorWatermarkItem(), this.mWatermarkDegree)) {
            this.mWatermarkLayout.setAlpha(0.0f);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mWatermarkLayout.getLayoutParams();
            if (i != 1) {
                if (i == 2) {
                    height = Util.getDisplayRect().bottom - this.mWatermarkLayout.getHeight();
                }
                this.mWatermarkLayout.setLayoutParams(layoutParams);
                this.mWatermarkLayout.invalidate();
                Completable.create(new AlphaInOnSubscribe(this.mWatermarkLayout).setDurationTime(200).setInterpolator(new CubicEaseOutInterpolator())).subscribe();
            }
            height = (Util.getDisplayRect().bottom - this.mWatermarkLayout.getHeight()) - i2;
            if (height >= 0) {
            }
            this.mWatermarkLayout.setLayoutParams(layoutParams);
            this.mWatermarkLayout.invalidate();
            Completable.create(new AlphaInOnSubscribe(this.mWatermarkLayout).setDurationTime(200).setInterpolator(new CubicEaseOutInterpolator())).subscribe();
            layoutParams.topMargin = height;
            this.mWatermarkLayout.setLayoutParams(layoutParams);
            this.mWatermarkLayout.invalidate();
            Completable.create(new AlphaInOnSubscribe(this.mWatermarkLayout).setDurationTime(200).setInterpolator(new CubicEaseOutInterpolator())).subscribe();
        }
    }

    public boolean needViewClear() {
        return true;
    }

    public void notifyAfterFrameAvailable(int i) {
        super.notifyAfterFrameAvailable(i);
        if (this.mCurrentMode != 254) {
            View view = this.mMoreModeMaskView;
            if (view != null && view.getVisibility() == 0) {
                this.mMoreModeMaskView.setVisibility(8);
            }
        }
        this.mPreviewFrame.updateReferenceLineAccordSquare();
        CameraCapabilities capabilitiesByBogusCameraId = Camera2DataContainer.getInstance().getCapabilitiesByBogusCameraId(DataRepository.dataItemGlobal().getCurrentCameraId(), this.mCurrentMode);
        if (DataRepository.dataItemGlobal().isNormalIntent() || !capabilitiesByBogusCameraId.isSupportLightTripartite()) {
            updateReferenceGradienterSwitched();
        }
        updateCinematicAspectRatioSwitched(CameraSettings.isCinematicAspectRatioEnabled(this.mCurrentMode));
        this.mFocusView.reInit();
        int i2 = this.mCurrentMode;
        if (i2 == 185 || i2 == 210 || i2 == 213) {
            this.mFocusView.setEvAdjustable(false);
        }
        this.mZoomView.reInit();
        this.mZoomViewHorizontal.reInit();
        this.mEffectCropView.updateVisible();
        updateFocusMode(CameraSettings.getFocusMode());
    }

    public void notifyDataChanged(int i, int i2) {
        V6PreviewFrame v6PreviewFrame;
        int i3;
        super.notifyDataChanged(i, i2);
        boolean isIntentAction = DataRepository.dataItemGlobal().isIntentAction();
        if (isIntentAction != this.mIsIntentAction) {
            this.mIsIntentAction = isIntentAction;
            hideReviewViews();
        }
        if (DataRepository.dataItemGlobal().getCurrentCameraId() != this.mLastCameraId) {
            this.mLastCameraId = DataRepository.dataItemGlobal().getCurrentCameraId();
            if (Util.isAccessible()) {
                if (this.mLastCameraId != 1) {
                    v6PreviewFrame = this.mPreviewFrame;
                    i3 = R.string.accessibility_back_preview_status;
                } else if (Util.isScreenSlideOff(getActivity())) {
                    v6PreviewFrame = this.mPreviewFrame;
                    i3 = R.string.accessibility_pull_down_to_open_camera;
                } else {
                    v6PreviewFrame = this.mPreviewFrame;
                    i3 = R.string.accessibility_front_preview_status;
                }
                v6PreviewFrame.setContentDescription(getString(i3));
                this.mPreviewFrame.announceForAccessibility(getString(i3));
            }
        }
        if (this.mCurrentMode == 204) {
            ViewCompat.setImportantForAccessibility(this.mPreviewFrame, 2);
        } else {
            ViewCompat.setImportantForAccessibility(this.mPreviewFrame, 1);
        }
        if (i == 2 || i == 3) {
            adjustViewHeight();
        }
    }

    public void onAutoZoomStarted() {
        if (!this.mAutoZoomOverlay.isViewEnabled()) {
            this.mAutoZoomOverlay.setViewEnable(true);
            this.mAutoZoomOverlay.setViewActive(false);
            this.mAutoZoomOverlay.clear(0);
        }
    }

    public void onAutoZoomStopped() {
        if (this.mAutoZoomOverlay.isViewEnabled()) {
            this.mAutoZoomOverlay.setViewEnable(false);
            this.mAutoZoomOverlay.setViewActive(false);
            this.mAutoZoomOverlay.clear(4);
        }
    }

    public boolean onBackEvent(int i) {
        return false;
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
    }

    public void onDestroy() {
        super.onDestroy();
        destroyEffectCropView();
    }

    public boolean onEffectViewTouchEvent(MotionEvent motionEvent) {
        return this.mEffectCropView.onTouchEvent(motionEvent);
    }

    public void onKeyEventSmoothZoom(int i, android.view.KeyEvent keyEvent) {
        ZoomView zoomView = this.mZoomView;
        if (zoomView != null) {
            zoomView.onKeyEventSmoothZoom(i, keyEvent);
        }
        ZoomView zoomView2 = this.mZoomViewHorizontal;
        if (zoomView2 != null) {
            zoomView2.onKeyEventSmoothZoom(i, keyEvent);
        }
    }

    public void onPause() {
        super.onPause();
        this.mLastFaceSuccess = false;
        FaceView faceView = this.mFaceView;
        if (faceView != null) {
            faceView.setVisibility(8);
        }
        this.mHandler.removeCallbacksAndMessages(null);
        resetSmoothZoom();
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public void onResume() {
        super.onResume();
        FaceView faceView = this.mFaceView;
        if (faceView != null) {
            faceView.setVisibility(0);
        }
        this.mPreviewFrame.setOnTouchListener(new C0289O0000oOO(this));
    }

    public void onStop() {
        super.onStop();
        LightingView lightingView = this.mLightingView;
        if (lightingView != null) {
            lightingView.clear();
        }
    }

    public void onStopObjectTrack() {
        this.mObjectView.clear();
        this.mObjectView.setVisibility(8);
    }

    public void onTrackingStarted(RectF rectF) {
        AutoZoomModuleProtocol autoZoomModuleProtocol = (AutoZoomModuleProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(215);
        if (autoZoomModuleProtocol != null) {
            autoZoomModuleProtocol.startTracking(rectF);
        }
    }

    public void onTrackingStopped(int i) {
        if (this.mAutoZoomOverlay.isViewActive()) {
            this.mAutoZoomOverlay.setViewActive(false);
            this.mAutoZoomOverlay.clear(0);
        }
    }

    public boolean onViewTouchEvent(int i, MotionEvent motionEvent) {
        if (i == this.mFocusView.getId()) {
            return this.mFocusView.onViewTouchEvent(motionEvent);
        }
        if (i == this.mEffectCropView.getId()) {
            return this.mEffectCropView.onViewTouchEvent(motionEvent);
        }
        if (i == this.mAutoZoomOverlay.getId()) {
            return this.mAutoZoomOverlay.onViewTouchEvent(motionEvent);
        }
        return false;
    }

    public void onZoomTouchDown() {
        CameraStatUtils.trackVideoSmoothZoom(CameraStatUtils.modeIdToName(this.mCurrentMode));
        ManuallyValueChanged manuallyValueChanged = (ManuallyValueChanged) ModeCoordinatorImpl.getInstance().getAttachProtocol(174);
        if (manuallyValueChanged != null) {
            manuallyValueChanged.updateSATIsZooming(true);
        }
    }

    public void onZoomTouchUp() {
        ManuallyValueChanged manuallyValueChanged = (ManuallyValueChanged) ModeCoordinatorImpl.getInstance().getAttachProtocol(174);
        if (manuallyValueChanged != null) {
            manuallyValueChanged.updateSATIsZooming(false);
        }
    }

    public void onZoomValueChanged(float f) {
        ManuallyValueChanged manuallyValueChanged = (ManuallyValueChanged) ModeCoordinatorImpl.getInstance().getAttachProtocol(174);
        if (manuallyValueChanged != null) {
            manuallyValueChanged.onDualZoomValueChanged(f, 1);
        }
    }

    public void performHapticFeedback(int i) {
        this.mPreviewFrame.performHapticFeedback(i);
    }

    public void processingFinish() {
        setZoomViewVisible(false);
        this.mFocusView.processingFinish();
        this.mIsRecording = false;
    }

    public void processingStart(String str) {
        this.mIsRecording = true;
        if (needShowZoomView(this.mCurrentMode)) {
            setZoomViewVisible(true);
        }
        this.mFocusView.processingStart();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0067, code lost:
        if (r8.getVisibility() == 0) goto L_0x00cf;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0095, code lost:
        if (r8 == false) goto L_0x00cf;
     */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00d2  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00da  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x0114  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x012e  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x0148  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x01a4  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void provideAnimateElement(int i, List list, int i2) {
        LightingView lightingView;
        int i3;
        super.provideAnimateElement(i, list, i2);
        boolean z = true;
        animateViews(i == 254 ? 1 : -1, list, this.mMoreModeMaskView);
        setIdPhotoBoxVisible(true);
        CoverState coverState = CoverState.NONE;
        boolean isFixedLocation = DataRepository.dataItemRunning().getComponentRunningAIWatermark().isFixedLocation();
        int i4 = 0;
        if (i != 162) {
            if (i != 163) {
                if (i != 165) {
                    if (!(i == 169 || i == 171 || i == 173)) {
                        if (i != 180) {
                            if (i != 188) {
                                if (i != 189) {
                                    if (i != 204) {
                                        if (i != 205) {
                                            if (!(i == 207 || i == 208)) {
                                                switch (i) {
                                                    case 212:
                                                    case 213:
                                                        break;
                                                    case 214:
                                                        break;
                                                }
                                            }
                                        } else {
                                            FrameLayout frameLayout = this.mWatermarkLayout;
                                            if (frameLayout != null) {
                                            }
                                        }
                                    }
                                }
                            }
                            coverState = CoverState.NONE;
                        }
                    }
                    if (CameraSettings.isCinematicAspectRatioEnabled(this.mCurrentMode) && i2 != 3) {
                        coverState = CoverState.LR;
                    }
                    coverState = CoverState.NONE;
                } else {
                    coverState = CoverState.TB;
                }
                z = false;
                if (!z) {
                    i4 = 4;
                }
                setWatermarkVisible(i4);
                if (this.mWatermarkLayout != null) {
                    this.mListener = isFixedLocation ? null : new DragListener(Util.getDisplayRect(), this.mOnClickListener);
                    this.mWatermarkLayout.setOnTouchListener(this.mListener);
                }
                this.mPreviewFrame.hidePreviewReferenceLine();
                this.mPreviewFrame.hidePreviewGradienter();
                this.mFaceView.clear();
                this.mDocumentView.clear();
                this.mFaceView.clearFaceFlags();
                this.mFocusView.clear();
                lightingView = this.mLightingView;
                if (lightingView != null) {
                    lightingView.clear();
                }
                this.mAfRegionsView.clear();
                this.mMimojiLightingView.clear();
                this.mFocusView.releaseListener();
                if (this.mLeftCover.getTag() != null || this.mLeftCover.getTag() != coverState) {
                    this.mLeftCover.setTag(coverState);
                    i3 = AnonymousClass6.$SwitchMap$com$android$camera$fragment$FragmentMainContent$CoverState[coverState.ordinal()];
                    if (i3 == 2) {
                        if (this.mCoverParent.getLayoutParams().height != this.mNormalCoverHeight) {
                            this.mCoverParent.getLayoutParams().height = this.mNormalCoverHeight;
                            this.mCoverParent.requestLayout();
                        }
                        SlideOutOnSubscribe.directSetResult(this.mLeftCover, 3);
                        SlideOutOnSubscribe.directSetResult(this.mRightCover, 5);
                    } else if (i3 != 3) {
                        View view = this.mLeftCover;
                        if (list == null) {
                            SlideOutOnSubscribe.directSetResult(view, 3);
                            SlideOutOnSubscribe.directSetResult(this.mRightCover, 5);
                        } else {
                            if (view.getVisibility() == 0) {
                                list.add(Completable.create(new SlideOutOnSubscribe(this.mLeftCover, 3).setDurationTime(200)));
                            }
                            if (this.mRightCover.getVisibility() == 0) {
                                list.add(Completable.create(new SlideOutOnSubscribe(this.mRightCover, 5).setDurationTime(200)));
                            }
                        }
                        if (this.mCoverParent.getLayoutParams().height != this.mNormalCoverHeight) {
                            this.mHandler.postDelayed(new C0284O0000Ooo(this), 200);
                        }
                    }
                }
                return;
            }
            CoverState coverState2 = (!CameraSettings.isCinematicAspectRatioEnabled(this.mCurrentMode) || i2 == 3) ? CoverState.NONE : CoverState.LR;
            coverState = coverState2;
            boolean isMacroModeEnabled = CameraSettings.isMacroModeEnabled(i);
            if (CameraSettings.isAIWatermarkOn()) {
            }
            z = false;
            if (!z) {
            }
            setWatermarkVisible(i4);
            if (this.mWatermarkLayout != null) {
            }
            this.mPreviewFrame.hidePreviewReferenceLine();
            this.mPreviewFrame.hidePreviewGradienter();
            this.mFaceView.clear();
            this.mDocumentView.clear();
            this.mFaceView.clearFaceFlags();
            this.mFocusView.clear();
            lightingView = this.mLightingView;
            if (lightingView != null) {
            }
            this.mAfRegionsView.clear();
            this.mMimojiLightingView.clear();
            this.mFocusView.releaseListener();
            if (this.mLeftCover.getTag() != null) {
            }
            this.mLeftCover.setTag(coverState);
            i3 = AnonymousClass6.$SwitchMap$com$android$camera$fragment$FragmentMainContent$CoverState[coverState.ordinal()];
            if (i3 == 2) {
            }
        }
        CoverState coverState3 = (!CameraSettings.isCinematicAspectRatioEnabled(this.mCurrentMode) || i2 == 3) ? CoverState.NONE : CoverState.LR;
        coverState = coverState3;
        if (C0122O00000o.instance().O000O00o(i) || this.mCurrentMode == 180) {
            boolean supportVideoSATForVideoQuality = CameraSettings.supportVideoSATForVideoQuality(i);
            this.mZoomViewHorizontal.init();
            this.mZoomViewHorizontal.setSupportVideoSat(supportVideoSATForVideoQuality);
            this.mZoomView.init();
            this.mZoomView.setSupportVideoSat(supportVideoSATForVideoQuality);
        }
        z = false;
        if (!z) {
        }
        setWatermarkVisible(i4);
        if (this.mWatermarkLayout != null) {
        }
        this.mPreviewFrame.hidePreviewReferenceLine();
        this.mPreviewFrame.hidePreviewGradienter();
        this.mFaceView.clear();
        this.mDocumentView.clear();
        this.mFaceView.clearFaceFlags();
        this.mFocusView.clear();
        lightingView = this.mLightingView;
        if (lightingView != null) {
        }
        this.mAfRegionsView.clear();
        this.mMimojiLightingView.clear();
        this.mFocusView.releaseListener();
        if (this.mLeftCover.getTag() != null) {
        }
        this.mLeftCover.setTag(coverState);
        i3 = AnonymousClass6.$SwitchMap$com$android$camera$fragment$FragmentMainContent$CoverState[coverState.ordinal()];
        if (i3 == 2) {
        }
    }

    public void provideRotateItem(List list, int i) {
        super.provideRotateItem(list, i);
        if (this.mWatermarkDegree != i) {
            if (DataRepository.dataItemRunning().getComponentRunningAIWatermark().isFixedOrientation()) {
                this.mWatermarkDegree = 0;
            } else {
                this.mWatermarkDegree = i;
            }
            Log.d(TAG, String.format("provideRotateItem newDegree: %d mWatermarkDegree: %d", new Object[]{Integer.valueOf(i), Integer.valueOf(this.mWatermarkDegree)}));
            updateWatermarkRotation(this.mWatermarkDegree);
        }
        this.mFaceView.setOrientation((360 - i) % m.cQ, false);
        this.mAfRegionsView.setOrientation(i, false);
        LightingView lightingView = this.mLightingView;
        if (lightingView != null) {
            lightingView.setOrientation(i, false);
        }
        this.mFocusView.setOrientation(i, false);
        this.mZoomViewHorizontal.setOrientation(i);
        list.add(this.mFocusView);
    }

    public void reShowFaceRect() {
        this.mFaceView.reShowFaceRect();
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        modeCoordinator.attachProtocol(166, this);
        modeCoordinator.attachProtocol(214, this);
        registerBackStack(modeCoordinator, this);
    }

    public void removeTiltShiftMask() {
        this.mEffectCropView.removeTiltShiftMask();
    }

    public void setActiveIndicator(int i) {
        this.mActiveIndicator = i;
    }

    public void setAfRegionView(MeteringRectangle[] meteringRectangleArr, Rect rect, float f) {
        this.mAfRegionsView.setAfRegionRect(meteringRectangleArr, rect, f);
    }

    public void setCameraDisplayOrientation(int i) {
        FaceView faceView = this.mFaceView;
        if (faceView != null && this.mAfRegionsView != null) {
            faceView.setCameraDisplayOrientation(i);
            this.mAfRegionsView.setCameraDisplayOrientation(i);
        }
    }

    public void setCenterHint(int i, String str, String str2, int i2) {
        this.mHandler.removeCallbacksAndMessages(this.mPreviewCenterHint);
        if (i == 0) {
            this.mCenterHintText.setText(str);
            String str3 = "";
            if (str == null || str.equals(str3)) {
                this.mCenterHintText.setVisibility(8);
            } else {
                this.mCenterHintText.setVisibility(0);
            }
            if (str2 == null || str2.equals(str3)) {
                this.mCenterHintIcon.setVisibility(8);
            } else {
                Glide.with(getActivity()).load(str2).into(this.mCenterHintIcon);
                this.mCenterHintIcon.setVisibility(0);
            }
            if (i2 > 0) {
                this.mHandler.postAtTime(new Runnable() {
                    public void run() {
                        FragmentMainContent.this.mPreviewCenterHint.setVisibility(8);
                    }
                }, this.mPreviewCenterHint, SystemClock.uptimeMillis() + ((long) i2));
            }
        }
        this.mPreviewCenterHint.setVisibility(i);
    }

    public void setDisplaySize(int i, int i2) {
        this.mObjectView.setDisplaySize(i, i2);
    }

    public void setEffectViewVisible(boolean z) {
        V6EffectCropView v6EffectCropView = this.mEffectCropView;
        if (z) {
            v6EffectCropView.show();
        } else {
            v6EffectCropView.hide();
        }
    }

    public void setEvAdjustVisible(boolean z) {
        FocusView focusView = this.mFocusView;
        if (focusView != null) {
            focusView.setEVVisible(z);
        }
    }

    public void setEvAdjustable(boolean z) {
        this.mFocusView.setEvAdjustable(z);
    }

    public boolean setFaces(int i, CameraHardwareFace[] cameraHardwareFaceArr, Rect rect, Rect rect2) {
        if (i != 1) {
            if (i != 3) {
                return false;
            }
            if (cameraHardwareFaceArr != null && cameraHardwareFaceArr.length > 0) {
                this.mObjectView.setObject(cameraHardwareFaceArr[0]);
            }
            return true;
        } else if (this.mCurrentMode != 166 && !DataRepository.dataItemGlobal().isIntentIDPhoto()) {
            return this.mFaceView.setFaces(cameraHardwareFaceArr, rect, rect2);
        } else {
            Log.d(TAG, "current mode is panorama return false");
            return false;
        }
    }

    public void setFocusViewPosition(int i, int i2, int i3) {
        this.mFocusView.setPosition(i, i2, i3);
        this.mFaceView.forceHideRect();
    }

    public void setFocusViewType(boolean z) {
        this.mFocusView.setFocusType(z);
    }

    public void setGestureDetectorEnable(boolean z) {
        V6GestureRecognizer.getInstance((ActivityBase) getContext()).setGestureDetectorEnable(false);
    }

    public void setIdPhotoBoxVisible(boolean z) {
        boolean z2 = DataRepository.dataItemGlobal().isIntentIDPhoto() && z;
        this.mHandler.post(new C0288O0000oO0(this, z2));
    }

    public void setMimojiDetectTipType(int i) {
        if (i != this.mMimojiDetectTipType) {
            this.mMimojiDetectTipType = i;
        }
    }

    public void setObjectViewListener(ObjectViewListener objectViewListener) {
        ObjectView objectView = this.mObjectView;
        if (objectView != null) {
            objectView.setObjectViewListener(objectViewListener);
        }
    }

    public void setPreviewAspectRatio(float f) {
        adjustViewHeight();
        if (this.mDocumentView != null) {
            Rect previewRect = Util.getPreviewRect(getContext());
            this.mDocumentView.setDisplaySize(previewRect.width(), previewRect.height());
        }
    }

    public void setPreviewSize(int i, int i2) {
        AutoZoomView autoZoomView = this.mAutoZoomOverlay;
        if (autoZoomView != null) {
            autoZoomView.setPreviewSize(new Size(i, i2));
        }
        DocumentView documentView = this.mDocumentView;
        if (documentView != null) {
            documentView.setPreviewSize(i, i2);
        }
    }

    public void setShowGenderAndAge(boolean z) {
        this.mFaceView.setShowGenderAndAge(z);
    }

    public void setShowMagicMirror(boolean z) {
        this.mFaceView.setShowMagicMirror(z);
    }

    public void setSkipDrawFace(boolean z) {
        this.mFaceView.setSkipDraw(z);
    }

    public void setWatermarkVisible(int i) {
        boolean isDualWatermark = DataRepository.dataItemRunning().getComponentRunningAIWatermark().isDualWatermark();
        FrameLayout frameLayout = this.mWatermarkLayout;
        if (!(frameLayout == null || frameLayout.getVisibility() == i)) {
            this.mWatermarkLayout.setVisibility(i);
        }
        int i2 = isDualWatermark ? i : 8;
        FrameLayout frameLayout2 = this.mWatermarkExtendLayout;
        if (!(frameLayout2 == null || frameLayout2.getVisibility() == i2)) {
            this.mWatermarkExtendLayout.setVisibility(i2);
        }
        if (!isDualWatermark) {
            i = 8;
        }
        FrameLayout frameLayout3 = this.mWatermarkBackgroundLayout;
        if (frameLayout3 != null && frameLayout3.getVisibility() != i) {
            this.mWatermarkBackgroundLayout.setVisibility(i);
        }
    }

    public void setZoomViewVisible(boolean z) {
        if (z) {
            this.mZoomViewHorizontal.show(this.mDegree);
            this.mZoomViewHorizontal.setCurrentZoomRatio(CameraSettings.getRetainZoom(this.mCurrentMode));
            return;
        }
        this.mZoomViewHorizontal.hide();
    }

    public void showIndicator(int i, int i2) {
        FocusIndicator focusIndicator;
        if (i == 1) {
            focusIndicator = this.mFaceView;
        } else if (i == 2) {
            focusIndicator = this.mFocusView;
        } else if (i == 3) {
            focusIndicator = this.mObjectView;
        } else {
            return;
        }
        showIndicator(focusIndicator, i2);
    }

    public void showReviewViews(Bitmap bitmap) {
        if (bitmap != null) {
            this.mPreviewPanel.mVideoReviewImage.setImageBitmap(bitmap);
            this.mPreviewPanel.mVideoReviewImage.setVisibility(0);
        }
        Util.fadeIn(this.mPreviewPanel.mVideoReviewPlay);
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        modeCoordinator.detachProtocol(166, this);
        unRegisterBackStack(modeCoordinator, this);
        modeCoordinator.detachProtocol(214, this);
    }

    public void updateCinematicAspectRatioSwitched(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("updateCinematicPhotoSwitched isSwitchOn : ");
        sb.append(z);
        Log.i(TAG, sb.toString());
        if (z) {
            if (this.mCoverParent.getLayoutParams().height == this.mNormalCoverHeight) {
                this.mCoverParent.getLayoutParams().height = -1;
                this.mCoverParent.requestLayout();
            }
            Completable.create(new SlideInOnSubscribe(this.mLeftCover, 3)).subscribe();
            Completable.create(new SlideInOnSubscribe(this.mRightCover, 5)).subscribe();
        }
    }

    public void updateContentDescription() {
        if (isAdded()) {
            this.mPreviewFrame.setContentDescription(getString(R.string.accessibility_front_preview_status));
            if (Util.isAccessible()) {
                this.mPreviewFrame.announceForAccessibility(getString(R.string.accessibility_front_preview_status));
            }
        }
    }

    public void updateCurrentZoomRatio(float f) {
        ZoomView zoomView = this.mZoomView;
        if (zoomView != null) {
            zoomView.setCurrentZoomRatio(f);
        }
        ZoomView zoomView2 = this.mZoomViewHorizontal;
        if (zoomView2 != null) {
            zoomView2.setCurrentZoomRatio(f);
        }
    }

    public void updateDocument(Pair pair) {
        if (pair == null || ((float[]) pair.second).length != 8) {
            this.mDocumentView.clear();
            this.mDocumentView.setVisibility(8);
            return;
        }
        if (this.mDocumentView.getVisibility() != 0) {
            this.mDocumentView.setVisibility(0);
        }
        this.mDocumentView.updateDocument(pair);
    }

    public void updateEffectViewVisible() {
        this.mEffectCropView.updateVisible();
    }

    public void updateEffectViewVisible(int i) {
        this.mEffectCropView.updateVisible(i);
    }

    public void updateFaceView(boolean z, boolean z2, boolean z3, boolean z4, int i) {
        if (z2) {
            this.mFaceView.clear();
        }
        this.mFaceView.setVisibility(z ? 0 : 8);
        if (i > 0) {
            this.mFaceView.setCameraDisplayOrientation(i);
        }
        this.mFaceView.setMirror(z3);
        if (z4) {
            this.mFaceView.resume();
        }
    }

    public void updateFocusMode(String str) {
        this.mFocusView.updateFocusMode(str);
    }

    public void updateMimojiFaceDetectResultTip(boolean z) {
        this.mMimojiLightingView.post(new C0287O0000oO(this, z));
    }

    public void updateReferenceGradienterSwitched() {
        if (this.mPreviewFrame != null) {
            boolean z = isReferenceLineEnabled() && !((ActivityBase) getContext()).getCameraIntentManager().isScanQRCodeIntent() && !DataRepository.dataItemGlobal().isIntentIDPhoto() && needReferenceLineMode();
            this.mPreviewFrame.updateReferenceGradienterSwitched(z, CameraSettings.isGradienterOn(), ModuleManager.isSquareModule());
        }
    }

    public void updateWatermarkSample(WatermarkItem watermarkItem) {
        boolean z = true;
        if (watermarkItem != null) {
            Log.k(3, TAG, String.format("updateWatermarkSample Item Key = %s", new Object[]{watermarkItem.getKey()}));
        }
        ComponentRunningAIWatermark componentRunningAIWatermark = DataRepository.dataItemRunning().getComponentRunningAIWatermark();
        showWatermarkTipsIfNeeded(watermarkItem, componentRunningAIWatermark.getMajorWatermarkItem());
        componentRunningAIWatermark.updateWatermarkItem(watermarkItem);
        if (componentRunningAIWatermark.isDualWatermark()) {
            showWatermarkItem(this.mWatermarkLayout, componentRunningAIWatermark.getMajorWatermarkItem() != null);
            FrameLayout frameLayout = this.mWatermarkExtendLayout;
            if (componentRunningAIWatermark.getMinorWatermarkItem() == null) {
                z = false;
            }
            showWatermarkItem(frameLayout, z);
            updateWatermarkItemBackground(componentRunningAIWatermark);
        }
        if (componentRunningAIWatermark.isFixedOrientation()) {
            this.mWatermarkDegree = 0;
        }
        if (watermarkItem != null) {
            watermarkItem.clearCaptureCoordinate();
            if (this.mWatermarkLayout != null) {
                updateWatermarkRotation(this.mWatermarkDegree);
                setWatermarkVisible(0);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0053  */
    /* JADX WARNING: Removed duplicated region for block: B:26:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateWatermarkSample(WatermarkItem watermarkItem, boolean z) {
        int i;
        FrameLayout frameLayout;
        updateWatermarkSample(watermarkItem);
        if (z) {
            TextView textView = null;
            String key = watermarkItem.getKey();
            char c = 65535;
            switch (key.hashCode()) {
                case 37820169:
                    if (key.equals(WatermarkConstant.LOCATION_TIME_1)) {
                        c = 1;
                        break;
                    }
                    break;
                case 37820170:
                    if (key.equals(WatermarkConstant.LOCATION_TIME_2)) {
                        c = 2;
                        break;
                    }
                    break;
                case 1901043637:
                    if (key.equals("location")) {
                        c = 0;
                        break;
                    }
                    break;
            }
            if (c == 0) {
                frameLayout = this.mWatermarkLayout;
                i = R.id.location_text;
            } else if (c != 1) {
                if (c == 2) {
                    frameLayout = this.mWatermarkLayout;
                    i = R.id.location_time_2_location;
                }
                if (textView == null) {
                    WatermarkProtocol watermarkProtocol = (WatermarkProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(253);
                    if (watermarkProtocol != null) {
                        watermarkProtocol.startDottedLineAnimation(textView);
                        return;
                    }
                    return;
                }
                return;
            } else {
                frameLayout = this.mWatermarkLayout;
                i = R.id.location_time_1_location;
            }
            textView = (TextView) frameLayout.findViewById(i);
            if (textView == null) {
            }
        }
    }

    public void updateZoomRatio(float f, float f2) {
        ZoomView zoomView = this.mZoomView;
        if (zoomView != null) {
            zoomView.updateZoomRatio(f, f2);
        }
        ZoomView zoomView2 = this.mZoomViewHorizontal;
        if (zoomView2 != null) {
            zoomView2.updateZoomRatio(f, f2);
        }
    }
}
