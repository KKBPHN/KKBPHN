package com.android.camera.fragment;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.android.camera.ActivityBase;
import com.android.camera.Camera;
import com.android.camera.CameraScreenNail;
import com.android.camera.CameraScreenNail.RequestRenderListener;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.customization.TintColor;
import com.android.camera.effect.draw_mode.DrawExtTexAttribute;
import com.android.camera.log.Log;
import com.android.camera.panorama.CaptureDirection;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.protocol.ModeProtocol.PanoramaProtocol;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.ui.GLTextureView;
import com.android.camera.ui.GLTextureView.EGLShareContextGetter;
import com.android.camera.ui.PanoMovingIndicatorView;
import com.android.camera.ui.drawable.PanoramaArrowAnimateDrawable;
import com.android.gallery3d.ui.GLCanvasImpl;
import java.util.List;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;
import miui.view.animation.CubicEaseInOutInterpolator;

public class FragmentPanorama extends BaseFragment implements PanoramaProtocol, RequestRenderListener, OnClickListener {
    public static final int FRAGMENT_INFO = 4080;
    public static final float PREVIEW_CROP_RATIO = 0.8f;
    public static final String TAG = "FragmentPanorama";
    public static final float VERTICAL_PREVIEW_CROP_RATIO = 0.9f;
    private int mArrowMargin;
    private int mArrowTranslation;
    private CaptureDirection mCaptureDirection = CaptureDirection.HORIZONTAL;
    private Interpolator mCubicEaseInOutInterpolator = new CubicEaseInOutInterpolator();
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler();
    private ViewGroup mHintFrame;
    /* access modifiers changed from: private */
    public ImageView mIndicator;
    /* access modifiers changed from: private */
    public int mMoveDirection = -1;
    /* access modifiers changed from: private */
    public View mMoveReferenceLine;
    private PanoMovingIndicatorView mMovingDirectionView;
    private PanoramaArrowAnimateDrawable mPanoramaArrowAnimateDrawable = new PanoramaArrowAnimateDrawable();
    private ImageView mPanoramaPreview;
    private View mPanoramaViewRoot;
    protected CameraSize mPreviewSize;
    private Object mRenderLock = new Object();
    /* access modifiers changed from: private */
    public GLTextureView mStillPreview;
    private View mStillPreviewHintArea;
    /* access modifiers changed from: private */
    public int mStillPreviewTextureHeight;
    /* access modifiers changed from: private */
    public int mStillPreviewTextureOffsetX;
    /* access modifiers changed from: private */
    public int mStillPreviewTextureOffsetY;
    /* access modifiers changed from: private */
    public int mStillPreviewTextureWidth;
    private TextView mUseHintTextView;
    /* access modifiers changed from: private */
    public volatile boolean mWaitingFirstFrame = false;

    class StillPreviewRender implements Renderer {
        private DrawExtTexAttribute mExtTexture;
        float[] mTransform;

        private StillPreviewRender() {
            this.mExtTexture = new DrawExtTexAttribute(true);
            this.mTransform = new float[16];
        }

        public void onDrawFrame(GL10 gl10) {
            CameraScreenNail cameraScreenNail = ((ActivityBase) FragmentPanorama.this.getContext()).getCameraScreenNail();
            GLCanvasImpl gLCanvas = ((ActivityBase) FragmentPanorama.this.getContext()).getGLView().getGLCanvas();
            if (cameraScreenNail != null && gLCanvas != null && cameraScreenNail.getSurfaceTexture() != null) {
                synchronized (gLCanvas) {
                    gLCanvas.clearBuffer();
                    int width = gLCanvas.getWidth();
                    int height = gLCanvas.getHeight();
                    gLCanvas.getState().pushState();
                    gLCanvas.setSize(FragmentPanorama.this.mStillPreview.getWidth(), FragmentPanorama.this.mStillPreview.getHeight());
                    cameraScreenNail.getSurfaceTexture().getTransformMatrix(this.mTransform);
                    gLCanvas.draw(this.mExtTexture.init(cameraScreenNail.getExtTexture(), this.mTransform, FragmentPanorama.this.mStillPreviewTextureOffsetX, FragmentPanorama.this.mStillPreviewTextureOffsetY, FragmentPanorama.this.mStillPreviewTextureWidth, FragmentPanorama.this.mStillPreviewTextureHeight));
                    gLCanvas.setSize(width, height);
                    gLCanvas.getState().popState();
                    gLCanvas.recycledResources();
                }
                if (FragmentPanorama.this.mWaitingFirstFrame) {
                    FragmentPanorama.this.mWaitingFirstFrame = false;
                    FragmentPanorama.this.mHandler.post(new Runnable() {
                        public void run() {
                            Log.d(FragmentPanorama.TAG, "onDrawFrame first frame");
                            FragmentPanorama.this.mMoveReferenceLine.setVisibility(0);
                            FragmentPanorama.this.mIndicator.setVisibility(0);
                        }
                    });
                }
            }
        }

        public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        }

        public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        }
    }

    static /* synthetic */ float O0000OOo(float f) {
        if (f == 1.0f) {
            return 1.0f;
        }
        return (float) ((-Math.pow(2.0d, (double) (f * -10.0f))) + 1.0d);
    }

    private void initStillPreviewRender(GLTextureView gLTextureView) {
        if (gLTextureView.getRenderer() == null) {
            StillPreviewRender stillPreviewRender = new StillPreviewRender();
            gLTextureView.setEGLContextClientVersion(2);
            gLTextureView.setEGLShareContextGetter(new EGLShareContextGetter() {
                public EGLContext getShareContext() {
                    return ((ActivityBase) FragmentPanorama.this.getContext()).getGLView().getEGLContext();
                }
            });
            gLTextureView.setRenderer(stillPreviewRender);
            gLTextureView.setRenderMode(0);
        }
    }

    private void initViewByCaptureDirection(CaptureDirection captureDirection) {
        StringBuilder sb = new StringBuilder();
        sb.append("initViewByCaptureDirection isVertical ");
        sb.append(captureDirection);
        Log.d(TAG, sb.toString());
        boolean z = captureDirection == CaptureDirection.VERTICAL;
        if (C0122O00000o.instance().OOOO0Oo()) {
            this.mUseHintTextView = (TextView) findViewById(R.id.pano_use_hint_vertical);
            LayoutParams layoutParams = (LayoutParams) this.mUseHintTextView.getLayoutParams();
            int dimensionPixelOffset = getContext().getResources().getDimensionPixelOffset(R.dimen.pano_use_hint_top_margin_vertical);
            if (Util.getDisplayRect().top == 0) {
                layoutParams.topMargin = dimensionPixelOffset + Display.getTopBarHeight();
            } else {
                layoutParams.topMargin = (Util.getDisplayRect().top + dimensionPixelOffset) - Display.getTopMargin();
            }
            findViewById(R.id.pano_use_hint).setVisibility(8);
        } else {
            this.mUseHintTextView = (TextView) findViewById(R.id.pano_use_hint);
        }
        ImageView imageView = (ImageView) findViewById(R.id.pano_arrow);
        ImageView imageView2 = (ImageView) findViewById(R.id.pano_arrow_vertical);
        if (imageView.getDrawable() == null) {
            imageView.setImageDrawable(new PanoramaArrowAnimateDrawable());
        }
        if (imageView2.getDrawable() == null) {
            imageView2.setImageDrawable(new PanoramaArrowAnimateDrawable());
        }
        if (z) {
            imageView = imageView2;
        }
        this.mIndicator = imageView;
        this.mPanoramaArrowAnimateDrawable = (PanoramaArrowAnimateDrawable) this.mIndicator.getDrawable();
        this.mArrowMargin = getResources().getDimensionPixelSize(R.dimen.pano_arrow_margin);
        setViewMargin(this.mPanoramaViewRoot, Display.getTopMargin());
        this.mPanoramaPreview = (ImageView) findViewById(z ? R.id.panorama_image_preview_vertical : R.id.panorama_image_preview);
        this.mStillPreview = (GLTextureView) findViewById(z ? R.id.panorama_still_preview_vertical : R.id.panorama_still_preview);
        this.mMovingDirectionView = (PanoMovingIndicatorView) findViewById(z ? R.id.pano_move_direction_view_vertical : R.id.pano_move_direction_view);
        this.mMoveReferenceLine = findViewById(z ? R.id.pano_move_reference_line_vertical : R.id.pano_move_reference_line);
        this.mStillPreviewHintArea = findViewById(z ? R.id.pano_still_preview_hint_area_vertical : R.id.pano_still_preview_hint_area);
        this.mHintFrame = (ViewGroup) findViewById(z ? R.id.pano_preview_hint_frame_vertical : R.id.pano_preview_hint_frame);
        this.mHintFrame.setContentDescription(getString(z ? R.string.accessibility_pano_moving_left : R.string.accessibility_pano_moving_right));
        this.mHintFrame.setVisibility(0);
        int round = z ? Display.getTopBarHeight() + getContext().getResources().getDimensionPixelOffset(R.dimen.pano_thumbnail_top_margin_vertical) : CameraSettings.isAspectRatio123(Display.getWindowWidth(), Display.getWindowHeight()) ? (Display.getCenterDisplayHeight() - getContext().getResources().getDimensionPixelSize(R.dimen.pano_preview_hint_frame_height)) / 2 : Math.round(((float) Display.getCenterDisplayHeight()) * 0.35f) + Display.getTopBarHeight();
        setViewMargin(this.mHintFrame, round);
        this.mHintFrame.setOnClickListener(this);
    }

    private void setViewMargin(View view, int i) {
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        marginLayoutParams.topMargin = i;
        view.setLayoutParams(marginLayoutParams);
    }

    private void updateIndicatorLayoutParams() {
        StringBuilder sb = new StringBuilder();
        sb.append("updateIndicatorLayoutParams ");
        sb.append(this.mMoveDirection);
        Log.d(TAG, sb.toString());
        LayoutParams layoutParams = (LayoutParams) this.mStillPreview.getLayoutParams();
        int i = this.mMoveDirection;
        if (i == 4) {
            layoutParams.removeRule(9);
            layoutParams.addRule(11);
            this.mIndicator.setTranslationX((float) (((((Display.getWindowWidth() - Display.getStartMargin()) - Display.getEndMargin()) - this.mStillPreviewTextureWidth) - this.mArrowMargin) - this.mIndicator.getWidth()));
            this.mIndicator.setTranslationY(0.0f);
        } else {
            if (i == 3) {
                layoutParams.removeRule(11);
                layoutParams.addRule(9);
                this.mIndicator.setTranslationX((float) (this.mStillPreviewTextureWidth + this.mArrowMargin));
                this.mIndicator.setTranslationY(0.0f);
            } else if (i == 5) {
                layoutParams.removeRule(10);
                layoutParams.addRule(12);
                this.mIndicator.setTranslationY((float) (-((this.mStillPreviewTextureHeight + this.mArrowMargin) - this.mArrowTranslation)));
                this.mIndicator.setTranslationX(0.0f);
            } else if (i == 6) {
                layoutParams.removeRule(12);
                layoutParams.addRule(10);
                this.mIndicator.setTranslationX(0.0f);
            } else {
                return;
            }
            this.mPanoramaArrowAnimateDrawable.setTransformationRatio(2.0f);
            return;
        }
        this.mPanoramaArrowAnimateDrawable.setTransformationRatio(0.0f);
    }

    public final View findViewById(int i) {
        if (i == -1) {
            return null;
        }
        return this.mPanoramaViewRoot.findViewById(i);
    }

    public int getCurrentDirection() {
        return this.mMoveDirection;
    }

    public int getFragmentInto() {
        return 4080;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.pano_view;
    }

    public ViewGroup getPreivewContainer() {
        return this.mHintFrame;
    }

    public void initPreviewLayout(CameraSize cameraSize) {
        int i;
        int i2;
        this.mPreviewSize = cameraSize;
        int i3 = cameraSize.width;
        int i4 = cameraSize.height;
        CameraScreenNail cameraScreenNail = ((Camera) getContext()).getCameraScreenNail();
        int width = cameraScreenNail.getWidth();
        int height = cameraScreenNail.getHeight();
        if (this.mCaptureDirection == CaptureDirection.VERTICAL) {
            int dimensionPixelSize = getContext().getResources().getDimensionPixelSize(R.dimen.pano_texture_view_width_vertical);
            int i5 = dimensionPixelSize;
            i = (int) (((((float) dimensionPixelSize) / 0.9f) * ((float) height)) / ((float) width));
            i2 = i5;
        } else {
            int i6 = (int) (((float) height) * 0.8f);
            i = getContext().getResources().getDimensionPixelSize(R.dimen.pano_preview_hint_frame_height);
            i2 = (width * i) / i6;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("initPreviewLayout ");
        sb.append(i2);
        String str = "x";
        sb.append(str);
        sb.append(i);
        sb.append(", ");
        sb.append(i3);
        sb.append(str);
        sb.append(i4);
        Log.d(TAG, sb.toString());
        ViewGroup.LayoutParams layoutParams = this.mStillPreview.getLayoutParams();
        layoutParams.width = i2;
        layoutParams.height = i;
        if (this.mCaptureDirection == CaptureDirection.VERTICAL) {
            int i7 = layoutParams.width;
            this.mStillPreviewTextureWidth = (int) (((float) i7) / 0.9f);
            this.mStillPreviewTextureHeight = layoutParams.height;
            this.mStillPreviewTextureOffsetX = (-(this.mStillPreviewTextureWidth - i7)) / 2;
            this.mStillPreviewTextureOffsetY = 0;
        } else {
            int i8 = layoutParams.width;
            this.mStillPreviewTextureWidth = i8;
            this.mStillPreviewTextureHeight = (i8 * i3) / i4;
            this.mStillPreviewTextureOffsetX = 0;
            this.mStillPreviewTextureOffsetY = (-(this.mStillPreviewTextureHeight - layoutParams.height)) / 2;
        }
        this.mStillPreview.requestLayout();
        this.mUseHintTextView.setText(R.string.pano_click_to_toggle_direction);
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        this.mPanoramaViewRoot = view;
        animateViews(-1, (List) null, this.mPanoramaViewRoot);
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        marginLayoutParams.bottomMargin = Display.getBottomHeight();
        marginLayoutParams.setMarginStart(Display.getStartMargin());
        marginLayoutParams.setMarginEnd(Display.getEndMargin());
        initViewByCaptureDirection(this.mCaptureDirection);
        initStillPreviewRender((GLTextureView) findViewById(R.id.panorama_still_preview));
        initStillPreviewRender((GLTextureView) findViewById(R.id.panorama_still_preview_vertical));
    }

    public boolean isShown() {
        return this.mPanoramaViewRoot.isShown();
    }

    public void moveToDirection(int i) {
        ObjectAnimator objectAnimator;
        this.mMoveDirection = i;
        int dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.pano_thumbnail_height_vertical);
        float translationX = this.mIndicator.getTranslationX();
        float translationY = this.mIndicator.getTranslationY();
        float transformationRatio = this.mPanoramaArrowAnimateDrawable.getTransformationRatio();
        int i2 = this.mMoveDirection;
        float f = 2.0f;
        if (i2 == 3) {
            translationX = (float) (this.mStillPreviewTextureWidth + this.mArrowMargin);
        } else {
            if (i2 == 4) {
                translationX = (float) (((((Display.getWindowWidth() - Display.getStartMargin()) - Display.getEndMargin()) - this.mStillPreviewTextureWidth) - this.mArrowMargin) - this.mIndicator.getWidth());
            } else if (i2 == 5) {
                translationY = (float) (-((this.mStillPreviewTextureHeight + this.mArrowMargin) - this.mArrowTranslation));
            } else if (i2 == 6) {
                translationY = (float) (-((((dimensionPixelOffset - this.mStillPreviewTextureHeight) - this.mArrowMargin) - this.mIndicator.getWidth()) - this.mArrowTranslation));
            } else {
                f = transformationRatio;
            }
            f = 0.0f;
        }
        int i3 = this.mMoveDirection;
        if (i3 == 3 || i3 == 4) {
            ImageView imageView = this.mIndicator;
            objectAnimator = ObjectAnimator.ofFloat(imageView, "translationX", new float[]{imageView.getTranslationX(), translationX});
        } else {
            ImageView imageView2 = this.mIndicator;
            objectAnimator = ObjectAnimator.ofFloat(imageView2, "translationY", new float[]{imageView2.getTranslationY(), translationY});
        }
        objectAnimator.setDuration(500);
        PanoramaArrowAnimateDrawable panoramaArrowAnimateDrawable = this.mPanoramaArrowAnimateDrawable;
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(panoramaArrowAnimateDrawable, "transformationRatio", new float[]{panoramaArrowAnimateDrawable.getTransformationRatio(), f});
        ofFloat.setDuration(200);
        GLTextureView gLTextureView = this.mStillPreview;
        String str = "alpha";
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(gLTextureView, str, new float[]{gLTextureView.getAlpha(), 0.0f});
        ofFloat2.setDuration(250);
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this.mStillPreview, str, new float[]{0.0f, 1.0f});
        ofFloat3.setDuration(250);
        ofFloat3.addListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animator) {
                LayoutParams layoutParams = (LayoutParams) FragmentPanorama.this.mStillPreview.getLayoutParams();
                int i = 9;
                if (FragmentPanorama.this.mMoveDirection == 4) {
                    layoutParams.removeRule(9);
                    layoutParams.addRule(11);
                } else {
                    if (FragmentPanorama.this.mMoveDirection == 3) {
                        layoutParams.removeRule(11);
                    } else {
                        i = 12;
                        if (FragmentPanorama.this.mMoveDirection == 5) {
                            layoutParams.removeRule(10);
                        } else if (FragmentPanorama.this.mMoveDirection == 6) {
                            layoutParams.removeRule(12);
                            layoutParams.addRule(10);
                        }
                    }
                    layoutParams.addRule(i);
                }
                FragmentPanorama.this.mStillPreview.requestLayout();
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(new Animator[]{ofFloat2, ofFloat3});
        AnimatorSet animatorSet2 = new AnimatorSet();
        objectAnimator.setInterpolator(this.mCubicEaseInOutInterpolator);
        animatorSet.setInterpolator(this.mCubicEaseInOutInterpolator);
        ofFloat.setInterpolator(C0291O0000oo.INSTANCE);
        animatorSet2.playTogether(new Animator[]{objectAnimator, ofFloat, animatorSet});
        animatorSet2.start();
    }

    public void notifyAfterFrameAvailable(int i) {
        super.notifyAfterFrameAvailable(i);
        if (this.mCurrentMode == 166) {
            showSmallPreview(true);
        }
    }

    public void onCaptureDirectionDecided(int i, int i2, int i3) {
        this.mStillPreviewHintArea.setVisibility(4);
        this.mUseHintTextView.setText(R.string.pano_go_on_moving);
        this.mMovingDirectionView.setVisibility(0);
        this.mMovingDirectionView.setDisplayCenterY(Util.getRelativeLocation(this.mMovingDirectionView, this.mMoveReferenceLine)[1] + (this.mMoveReferenceLine.getHeight() / 2));
        this.mMovingDirectionView.setMovingAttribute(i, i2, i3);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pano_preview_hint_frame /*2131296910*/:
            case R.id.pano_preview_hint_frame_vertical /*2131296911*/:
                if (isEnableClick()) {
                    int i = 0;
                    int i2 = this.mMoveDirection;
                    if (i2 == 4) {
                        this.mHintFrame.setContentDescription(getString(R.string.accessibility_pano_moving_right));
                        i = 3;
                    } else if (i2 == 3) {
                        this.mHintFrame.setContentDescription(getString(R.string.accessibility_pano_moving_left));
                        i = 4;
                    } else if (i2 == 5) {
                        this.mHintFrame.setContentDescription(getString(R.string.accessibility_pano_moving_right));
                        i = 6;
                    } else if (i2 == 6) {
                        this.mHintFrame.setContentDescription(getString(R.string.accessibility_pano_moving_left));
                        i = 5;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("pano_preview_hint_frame direction from ");
                    sb.append(this.mMoveDirection);
                    sb.append(" to ");
                    sb.append(i);
                    Log.u(TAG, sb.toString());
                    CameraStatUtils.trackDirectionChanged(this.mMoveDirection);
                    moveToDirection(i);
                    CameraSettings.setPanoramaMoveDirection(i);
                    if (Util.isAccessible() && isAdded()) {
                        this.mHintFrame.sendAccessibilityEvent(8);
                        break;
                    }
                } else {
                    return;
                }
        }
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Resources resources = getResources();
        this.mArrowTranslation = (resources.getDimensionPixelOffset(R.dimen.pano_arrow_height) - resources.getDimensionPixelOffset(R.dimen.pano_arrow_width)) / 2;
        this.mMoveDirection = CameraSettings.getPanoramaMoveDirection(getContext());
        int i = this.mMoveDirection;
        CaptureDirection captureDirection = (i == 3 || i == 4) ? CaptureDirection.HORIZONTAL : CaptureDirection.VERTICAL;
        this.mCaptureDirection = captureDirection;
    }

    public void onPreviewMoving() {
        TextView textView;
        int i;
        if (this.mStillPreviewHintArea.getVisibility() != 0) {
            if (this.mMovingDirectionView.isTooFast()) {
                textView = this.mUseHintTextView;
                i = R.string.pano_how_to_use_prompt_slow_down;
            } else {
                boolean isFar = this.mMovingDirectionView.isFar();
                textView = this.mUseHintTextView;
                i = isFar ? R.string.pano_how_to_use_prompt_align_reference_line : R.string.pano_go_on_moving;
            }
            textView.setText(i);
        }
    }

    public void onResume() {
        super.onResume();
        findViewById(R.id.pano_move_reference_line_vertical).setBackgroundColor(TintColor.tintColor());
        findViewById(R.id.pano_move_reference_line).setBackgroundColor(TintColor.tintColor());
    }

    public void onStart() {
        super.onStart();
        this.mStillPreview.onResume();
    }

    public void onStop() {
        super.onStop();
        this.mStillPreview.onPause();
        this.mPanoramaPreview.setImageDrawable(null);
    }

    public void provideAnimateElement(int i, List list, int i2) {
        super.provideAnimateElement(i, list, i2);
        if (i != 166) {
            animateViews(-1, list, this.mPanoramaViewRoot);
        }
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        modeCoordinator.attachProtocol(176, this);
    }

    public void requestRender() {
        synchronized (this.mRenderLock) {
            if (this.mStillPreviewHintArea != null && this.mStillPreviewHintArea.isShown()) {
                this.mStillPreview.requestRender();
            }
        }
    }

    public void resetShootUI() {
        Log.d(TAG, "resetShootUI");
        setClickEnable(true);
        this.mStillPreviewHintArea.setVisibility(4);
        this.mMovingDirectionView.setVisibility(8);
        this.mUseHintTextView.setText(R.string.pano_click_to_toggle_direction);
    }

    public void setDirectionPosition(Point point, int i) {
        this.mMovingDirectionView.setPosition(point, i);
    }

    public void setDirectionTooFast(boolean z, int i) {
        this.mMovingDirectionView.setTooFast(z, i);
    }

    public void setDisplayPreviewBitmap(Bitmap bitmap) {
        ImageView imageView = this.mPanoramaPreview;
        if (bitmap == null) {
            imageView.setImageDrawable(null);
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    public void setShootUI() {
        Log.d(TAG, "setShootUI");
        setClickEnable(false);
        this.mMovingDirectionView.setVisibility(8);
        this.mStillPreviewHintArea.setVisibility(0);
    }

    public void showSmallPreview(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("showSmallPreview mMoveDirection ");
        sb.append(this.mMoveDirection);
        Log.d(TAG, sb.toString());
        this.mMovingDirectionView.setVisibility(8);
        updateIndicatorLayoutParams();
        this.mStillPreview.requestLayout();
        this.mStillPreviewHintArea.setVisibility(0);
        this.mStillPreview.onResume();
        this.mStillPreview.requestRender();
        this.mWaitingFirstFrame = true;
        animateViews(1, z, this.mPanoramaViewRoot);
    }

    public void toggleCaptureDirection() {
        synchronized (this.mRenderLock) {
            Log.d(TAG, "toggleCaptureDirection E");
            this.mCaptureDirection = this.mCaptureDirection == CaptureDirection.HORIZONTAL ? CaptureDirection.VERTICAL : CaptureDirection.HORIZONTAL;
            this.mUseHintTextView.setVisibility(8);
            this.mIndicator.setVisibility(8);
            this.mHintFrame.setVisibility(8);
            int i = 4;
            this.mStillPreviewHintArea.setVisibility(4);
            this.mStillPreview.onPause();
            initViewByCaptureDirection(this.mCaptureDirection);
            initPreviewLayout(this.mPreviewSize);
            boolean z = false;
            this.mUseHintTextView.setVisibility(0);
            this.mIndicator.setVisibility(0);
            this.mHintFrame.setVisibility(0);
            this.mStillPreviewHintArea.setVisibility(0);
            this.mStillPreview.onResume();
            if (this.mCaptureDirection == CaptureDirection.HORIZONTAL) {
                if (!Util.isLayoutRTL(getContext())) {
                    i = 3;
                }
                this.mMoveDirection = i;
            } else {
                this.mMoveDirection = 5;
            }
            CameraSettings.setPanoramaMoveDirection(this.mMoveDirection);
            updateIndicatorLayoutParams();
            if (this.mCaptureDirection == CaptureDirection.VERTICAL) {
                z = true;
            }
            CameraStatUtils.trackDirectionToggle(z);
            Log.d(TAG, "toggleCaptureDirection X");
        }
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        modeCoordinator.detachProtocol(176, this);
    }
}
