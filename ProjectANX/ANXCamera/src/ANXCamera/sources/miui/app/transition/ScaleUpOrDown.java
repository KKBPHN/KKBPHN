package miui.app.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.ImageView;
import miui.R;
import miui.view.ClipRoundedBounds;
import miui.view.RoundedFrameLayout;

class ScaleUpOrDown extends Transition {
    private static final float DEFAULT_BACK_TO_SCREEN_CENTER_SCALE = 0.6f;
    private static final float DEFAULT_ZOOMLESS_SCALE = 1.0f;
    private static final String TAG = "ScaleUpOrDown";
    private FragmentTransitionHelper helper;
    private final int mColor;
    private boolean mIsScaleUp;
    private final Rect mPositionRect;
    /* access modifiers changed from: private */
    public final int mRadius;
    private final Bitmap mThumb;

    public ScaleUpOrDown(Activity activity, FragmentOptions fragmentOptions, boolean z) {
        this.mPositionRect = fragmentOptions.getPositionRect();
        this.mColor = fragmentOptions.getColor();
        this.mRadius = fragmentOptions.getRadius();
        this.mThumb = fragmentOptions.getThumb();
        this.mIsScaleUp = z;
        this.helper = new FragmentTransitionHelper(activity);
        setDuration(350);
        setInterpolator(FragmentTransitionHelper.PHYSIC_BASED_INTERPOLATOR);
    }

    /* access modifiers changed from: private */
    public void animOverlayViewProgress(RoundedFrameLayout roundedFrameLayout, float f, float f2, int i, int i2, float f3, float f4) {
        roundedFrameLayout.setTranslationX(f);
        roundedFrameLayout.setTranslationY(f2);
        roundedFrameLayout.setClipRoundedBounds(new RectF(0.0f, 0.0f, (float) i, (float) i2), new float[]{f3, f3, f3, f3, f4, f4, f4, f4});
    }

    /* access modifiers changed from: private */
    public void animViewProgress(View view, float f, float f2, float f3, float f4) {
        view.setScaleX(f);
        view.setScaleY(f2);
        if (view instanceof ClipRoundedBounds) {
            float f5 = f3 / f;
            float f6 = f3 / f2;
            float f7 = f4 / f;
            float f8 = f4 / f2;
            ((ClipRoundedBounds) view).setClipRoundedBounds(new RectF(0.0f, 0.0f, (float) view.getWidth(), (float) view.getHeight()), new float[]{f5, f6, f5, f6, f7, f8, f7, f8});
        }
    }

    private ValueAnimator createAnimator() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat.setInterpolator(getInterpolator());
        ofFloat.setDuration(getDuration());
        return ofFloat;
    }

    private RoundedFrameLayout createOverlayView(Context context, View view) {
        RoundedFrameLayout roundedFrameLayout = new RoundedFrameLayout(context);
        roundedFrameLayout.setBackgroundColor(this.mColor);
        roundedFrameLayout.measure(MeasureSpec.makeMeasureSpec(view.getWidth(), 1073741824), MeasureSpec.makeMeasureSpec(view.getHeight(), 1073741824));
        roundedFrameLayout.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        Bitmap bitmap = this.mThumb;
        if (bitmap != null && !bitmap.isRecycled()) {
            ImageView imageView = new ImageView(context);
            imageView.setImageBitmap(this.mThumb);
            imageView.measure(MeasureSpec.makeMeasureSpec(this.mPositionRect.width(), 1073741824), MeasureSpec.makeMeasureSpec(this.mPositionRect.height(), 1073741824));
            imageView.layout(0, 0, this.mPositionRect.width(), this.mPositionRect.height());
            roundedFrameLayout.addView(imageView);
        }
        return roundedFrameLayout;
    }

    private boolean isScaleDownToCenter(Context context) {
        return context.getResources().getConfiguration().orientation == 2;
    }

    private Animator onAppear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        ValueAnimator createAnimator = createAnimator();
        Context context = viewGroup.getContext();
        View findViewById = view.getRootView().findViewById(R.id.fragment_bottom);
        final ViewGroupOverlay viewGroupOverlay = (ViewGroupOverlay) findViewById.getOverlay();
        final RoundedFrameLayout createOverlayView = createOverlayView(context, findViewById);
        final float width = ((float) this.mPositionRect.width()) / ((float) view.getWidth());
        final float height = ((float) this.mPositionRect.height()) / ((float) view.getHeight());
        Rect rect = this.mPositionRect;
        float f = ((float) rect.left) / (1.0f - width);
        float f2 = ((float) rect.top) / (1.0f - height);
        view.setPivotX(f);
        view.setPivotY(f2);
        final int topRoundedCorner = this.helper.getTopRoundedCorner();
        final int bottomRoundedCorner = this.helper.getBottomRoundedCorner();
        final View view2 = view;
        final RoundedFrameLayout roundedFrameLayout = createOverlayView;
        AnonymousClass1 r1 = new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedFraction = valueAnimator.getAnimatedFraction();
                float access$000 = ((float) ScaleUpOrDown.this.mRadius) + (((float) (topRoundedCorner - ScaleUpOrDown.this.mRadius)) * animatedFraction);
                float access$0002 = ((float) ScaleUpOrDown.this.mRadius) + (((float) (bottomRoundedCorner - ScaleUpOrDown.this.mRadius)) * animatedFraction);
                float f = width;
                float f2 = f + ((1.0f - f) * animatedFraction);
                float f3 = height;
                float f4 = f3 + ((1.0f - f3) * animatedFraction);
                ScaleUpOrDown.this.animViewProgress(view2, f2, f4, access$000, access$0002);
                float[] fArr = new float[9];
                view2.getMatrix().getValues(fArr);
                int width = (int) (((float) view2.getWidth()) * f2);
                int height = (int) (((float) view2.getHeight()) * f4);
                ScaleUpOrDown.this.animOverlayViewProgress(roundedFrameLayout, fArr[2], fArr[5], width, height, access$000, access$0002);
            }
        };
        createAnimator.addUpdateListener(r1);
        createAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                viewGroupOverlay.clear();
            }

            public void onAnimationStart(Animator animator) {
                super.onAnimationStart(animator);
                viewGroupOverlay.add(createOverlayView);
            }
        });
        return createAnimator;
    }

    private Animator onDisappear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        final View view2 = transitionValues.view;
        ValueAnimator createAnimator = createAnimator();
        Context context = viewGroup.getContext();
        final boolean isScaleDownToCenter = isScaleDownToCenter(context);
        final ViewGroupOverlay overlay = viewGroup.getOverlay();
        overlay.clear();
        final RoundedFrameLayout createOverlayView = createOverlayView(context, viewGroup);
        final float width = ((float) this.mPositionRect.width()) / ((float) view2.getWidth());
        final float height = ((float) this.mPositionRect.height()) / ((float) view2.getHeight());
        Rect rect = this.mPositionRect;
        float f = ((float) rect.left) / (1.0f - width);
        float f2 = ((float) rect.top) / (1.0f - height);
        if (isScaleDownToCenter) {
            view2.setPivotX((float) (view2.getWidth() / 2));
            view2.setPivotY((float) (view2.getHeight() / 2));
        } else {
            view2.setPivotX(f);
            view2.setPivotY(f2);
            overlay.add(createOverlayView);
        }
        overlay.add(view2);
        int topRoundedCorner = this.helper.getTopRoundedCorner();
        int bottomRoundedCorner = this.helper.getBottomRoundedCorner();
        if (view2.getScaleX() < 1.0f || view2.getScaleY() < 1.0f) {
            view2.setLeft(0);
            view2.setTop(0);
            view2.setRight(viewGroup.getRight());
            view2.setBottom(viewGroup.getBottom());
        }
        final int i = topRoundedCorner;
        final int i2 = bottomRoundedCorner;
        final RoundedFrameLayout roundedFrameLayout = createOverlayView;
        AnonymousClass3 r0 = new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedFraction = valueAnimator.getAnimatedFraction();
                if (isScaleDownToCenter) {
                    float f = (animatedFraction * -0.39999998f) + 1.0f;
                    ScaleUpOrDown.this.animViewProgress(view2, f, f, (float) i, (float) i2);
                    return;
                }
                float access$000 = ((float) i) + (((float) (ScaleUpOrDown.this.mRadius - i)) * animatedFraction);
                float access$0002 = ((float) i2) + (((float) (ScaleUpOrDown.this.mRadius - i2)) * animatedFraction);
                float f2 = ((width - 1.0f) * animatedFraction) + 1.0f;
                float f3 = ((height - 1.0f) * animatedFraction) + 1.0f;
                ScaleUpOrDown.this.animViewProgress(view2, f2, f3, access$000, access$0002);
                float[] fArr = new float[9];
                view2.getMatrix().getValues(fArr);
                ScaleUpOrDown.this.animOverlayViewProgress(roundedFrameLayout, fArr[2], fArr[5], (int) (((float) view2.getWidth()) * f2), (int) (((float) view2.getHeight()) * f3), access$000, access$0002);
            }
        };
        createAnimator.addUpdateListener(r0);
        createAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator animator) {
                super.onAnimationCancel(animator);
                overlay.remove(createOverlayView);
            }

            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                overlay.remove(createOverlayView);
            }
        });
        return createAnimator;
    }

    public void captureEndValues(TransitionValues transitionValues) {
        Log.d(TAG, "not used method:captureEndValues");
    }

    public void captureStartValues(TransitionValues transitionValues) {
        Log.d(TAG, "not used method:captureStartValues");
    }

    public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        return this.mIsScaleUp ? onAppear(viewGroup, transitionValues2.view, transitionValues, transitionValues2) : onDisappear(viewGroup, transitionValues.view, transitionValues, transitionValues2);
    }

    public boolean isTransitionRequired(TransitionValues transitionValues, TransitionValues transitionValues2) {
        return true;
    }
}
