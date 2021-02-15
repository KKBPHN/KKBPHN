package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EdgeEffect;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.recyclerview.R;
import androidx.recyclerview.widget.RecyclerView.EdgeEffectFactory;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import java.lang.reflect.Field;
import miuix.spring.view.SpringHelper;
import miuix.view.HapticCompat;
import miuix.view.HapticFeedbackConstants;

public abstract class SpringRecyclerView extends RemixRecyclerView {
    private static final Field NESTED_SCROLL_HELPER;
    private static final EdgeEffectFactory NON_EFFECT_FACTORY = new NonEdgeEffectFactory();
    private static final String TAG = "SpringRecyclerView";
    private static final Field VIEW_FLINGER;
    /* access modifiers changed from: private */
    public boolean mHorizontalOverScrolling;
    /* access modifiers changed from: private */
    public int mManagedScrollState;
    /* access modifiers changed from: private */
    public SpringFlinger mSpringFlinger;
    /* access modifiers changed from: private */
    public SpringHelper mSpringHelper;
    /* access modifiers changed from: private */
    public SpringNestedScrollingHelper mSpringNestedScrollingHelper;
    /* access modifiers changed from: private */
    public boolean mVerticalOverScrolling;

    class NonEdgeEffect extends EdgeEffect {
        NonEdgeEffect(Context context) {
            super(context);
        }

        public boolean draw(Canvas canvas) {
            return false;
        }

        public void finish() {
        }

        @Nullable
        public BlendMode getBlendMode() {
            return null;
        }

        public int getColor() {
            return 0;
        }

        public int getMaxHeight() {
            return 0;
        }

        public boolean isFinished() {
            return true;
        }

        public void onAbsorb(int i) {
        }

        public void onPull(float f) {
        }

        public void onPull(float f, float f2) {
        }

        public void onRelease() {
        }

        public void setBlendMode(@Nullable BlendMode blendMode) {
        }

        public void setColor(int i) {
        }

        public void setSize(int i, int i2) {
        }
    }

    class NonEdgeEffectFactory extends EdgeEffectFactory {
        private NonEdgeEffectFactory() {
        }

        /* access modifiers changed from: protected */
        @NonNull
        public EdgeEffect createEdgeEffect(@NonNull RecyclerView recyclerView, int i) {
            return new NonEdgeEffect(recyclerView.getContext());
        }
    }

    class SpringFlinger extends ViewFlinger {
        private SpringFlinger() {
            super();
        }

        public void fling(int i, int i2) {
            int horizontalDistance = SpringRecyclerView.this.mSpringHelper.getHorizontalDistance();
            int verticalDistance = SpringRecyclerView.this.mSpringHelper.getVerticalDistance();
            if (!SpringRecyclerView.this.springAvailable() || (horizontalDistance == 0 && verticalDistance == 0)) {
                super.fling(i, i2);
            } else {
                overFling(i, i2, horizontalDistance, verticalDistance);
            }
        }

        /* access modifiers changed from: 0000 */
        public void notifyHorizontalEdgeReached(int i) {
            SpringRecyclerView.this.mHorizontalOverScrolling = true;
            SpringRecyclerView.this.setScrollState(2);
            resetFlingPosition();
            this.mOverScroller.notifyHorizontalEdgeReached(0, -i, SpringRecyclerView.this.getWidth());
        }

        /* access modifiers changed from: 0000 */
        public void notifyVerticalEdgeReached(int i) {
            SpringRecyclerView.this.mVerticalOverScrolling = true;
            SpringRecyclerView.this.setScrollState(2);
            resetFlingPosition();
            this.mOverScroller.notifyVerticalEdgeReached(0, -i, SpringRecyclerView.this.getHeight());
        }

        /* access modifiers changed from: 0000 */
        public void overFling(int i, int i2, int i3, int i4) {
            int i5;
            int i6;
            int i7;
            int i8;
            int i9 = i3;
            int i10 = i4;
            boolean z = true;
            SpringRecyclerView.this.mHorizontalOverScrolling = i9 != 0;
            SpringRecyclerView springRecyclerView = SpringRecyclerView.this;
            if (i10 == 0) {
                z = false;
            }
            springRecyclerView.mVerticalOverScrolling = z;
            SpringRecyclerView.this.setScrollState(2);
            resetFlingPosition();
            int signum = Integer.signum(i) * i9;
            int i11 = Integer.MIN_VALUE;
            int i12 = Integer.MAX_VALUE;
            int i13 = -i9;
            if (signum > 0) {
                i6 = i13;
                i5 = i6;
            } else if (i < 0) {
                i5 = i13;
                i6 = Integer.MIN_VALUE;
            } else {
                i6 = i13;
                i5 = Integer.MAX_VALUE;
            }
            if (Integer.signum(i2) * i10 > 0) {
                i8 = -i10;
                i7 = i8;
            } else {
                if (i2 < 0) {
                    i12 = -i10;
                } else {
                    i11 = -i10;
                }
                i8 = i11;
                i7 = i12;
            }
            this.mOverScroller.fling(0, 0, i, i2, i6, i5, i8, i7, SpringRecyclerView.this.getWidth(), SpringRecyclerView.this.getHeight());
            postOnAnimation();
        }

        /* access modifiers changed from: 0000 */
        public void springBack(int i, int i2) {
            if (i != 0) {
                SpringRecyclerView.this.mHorizontalOverScrolling = true;
            }
            if (i2 != 0) {
                SpringRecyclerView.this.mVerticalOverScrolling = true;
            }
            SpringRecyclerView.this.setScrollState(2);
            resetFlingPosition();
            int i3 = -i;
            int i4 = -i2;
            this.mOverScroller.springBack(0, 0, i3, i3, i4, i4);
            postOnAnimation();
        }
    }

    class SpringNestedScrollingHelper extends NestedScrollingChildHelper {
        SpringNestedScrollingHelper(@NonNull View view) {
            super(view);
        }

        public boolean dispatchNestedPreScroll(int i, int i2, @Nullable int[] iArr, @Nullable int[] iArr2, int i3) {
            return SpringRecyclerView.this.mSpringHelper.handleNestedPreScroll(i, i2, iArr, iArr2, i3);
        }

        public void dispatchNestedScroll(int i, int i2, int i3, int i4, @Nullable int[] iArr, int i5, @Nullable int[] iArr2) {
            SpringRecyclerView.this.mSpringHelper.handleNestedScroll(i, i2, i3, i4, iArr, i5, iArr2);
        }

        /* access modifiers changed from: 0000 */
        public boolean super_dispatchNestedPreScroll(int i, int i2, @Nullable int[] iArr, @Nullable int[] iArr2, int i3) {
            if (SpringRecyclerView.this.mHorizontalOverScrolling || SpringRecyclerView.this.mVerticalOverScrolling || (i == 0 && i2 == 0)) {
                return false;
            }
            return super.dispatchNestedPreScroll(i, i2, iArr, iArr2, i3);
        }

        /* access modifiers changed from: 0000 */
        public void super_dispatchNestedScroll(int i, int i2, int i3, int i4, @Nullable int[] iArr, int i5, @Nullable int[] iArr2) {
            if (!SpringRecyclerView.this.mHorizontalOverScrolling && !SpringRecyclerView.this.mVerticalOverScrolling) {
                super.dispatchNestedScroll(i, i2, i3, i4, iArr, i5, iArr2);
            }
        }
    }

    static {
        try {
            VIEW_FLINGER = RecyclerView.class.getDeclaredField("mViewFlinger");
            VIEW_FLINGER.setAccessible(true);
            try {
                NESTED_SCROLL_HELPER = RecyclerView.class.getDeclaredField("mScrollingChildHelper");
                NESTED_SCROLL_HELPER.setAccessible(true);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        } catch (NoSuchFieldException e2) {
            throw new RuntimeException(e2);
        }
    }

    public SpringRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public SpringRecyclerView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.recyclerViewStyle);
    }

    public SpringRecyclerView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mManagedScrollState = 0;
        this.mSpringHelper = new SpringHelper() {
            /* access modifiers changed from: protected */
            public boolean canScrollHorizontally() {
                LayoutManager layoutManager = SpringRecyclerView.this.mLayout;
                return layoutManager != null && layoutManager.canScrollHorizontally();
            }

            /* access modifiers changed from: protected */
            public boolean canScrollVertically() {
                LayoutManager layoutManager = SpringRecyclerView.this.mLayout;
                return layoutManager != null && layoutManager.canScrollVertically();
            }

            /* access modifiers changed from: protected */
            public boolean dispatchNestedPreScroll(int i, int i2, @Nullable int[] iArr, @Nullable int[] iArr2, int i3) {
                if (SpringRecyclerView.this.mHorizontalOverScrolling && getHorizontalDistance() == 0) {
                    SpringRecyclerView.this.mHorizontalOverScrolling = false;
                }
                if (SpringRecyclerView.this.mVerticalOverScrolling && getVerticalDistance() == 0) {
                    SpringRecyclerView.this.mVerticalOverScrolling = false;
                }
                return SpringRecyclerView.this.mSpringNestedScrollingHelper.super_dispatchNestedPreScroll(i, i2, iArr, iArr2, i3);
            }

            /* access modifiers changed from: protected */
            public void dispatchNestedScroll(int i, int i2, int i3, int i4, @Nullable int[] iArr, int i5, @Nullable int[] iArr2) {
                int i6 = i3;
                int i7 = i4;
                SpringRecyclerView.this.mSpringNestedScrollingHelper.super_dispatchNestedScroll(i, i2, i3, i4, iArr, i5, iArr2);
                if (springAvailable() && SpringRecyclerView.this.mManagedScrollState == 2) {
                    if (!SpringRecyclerView.this.mHorizontalOverScrolling && canScrollHorizontally() && i6 != 0) {
                        SpringRecyclerView.this.mSpringFlinger.notifyHorizontalEdgeReached(i3);
                    }
                    if (!SpringRecyclerView.this.mVerticalOverScrolling && canScrollVertically() && i7 != 0) {
                        SpringRecyclerView.this.mSpringFlinger.notifyVerticalEdgeReached(i4);
                    }
                }
            }

            /* access modifiers changed from: protected */
            public int getHeight() {
                return SpringRecyclerView.this.getHeight();
            }

            /* access modifiers changed from: protected */
            public int getWidth() {
                return SpringRecyclerView.this.getWidth();
            }

            /* access modifiers changed from: protected */
            public boolean springAvailable() {
                return SpringRecyclerView.this.springAvailable();
            }

            /* access modifiers changed from: protected */
            @Keep
            public void vibrate() {
                HapticCompat.performHapticFeedback(SpringRecyclerView.this, HapticFeedbackConstants.MIUI_SCROLL_EDGE);
            }
        };
        this.mSpringFlinger = new SpringFlinger();
        this.mSpringNestedScrollingHelper = new SpringNestedScrollingHelper(this);
        replaceViewFlinger(this.mSpringFlinger);
        replaceNestedScrollingHelper(this.mSpringNestedScrollingHelper);
        super.setEdgeEffectFactory(NON_EFFECT_FACTORY);
    }

    private void replaceNestedScrollingHelper(NestedScrollingChildHelper nestedScrollingChildHelper) {
        try {
            NESTED_SCROLL_HELPER.set(this, nestedScrollingChildHelper);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void replaceViewFlinger(ViewFlinger viewFlinger) {
        try {
            VIEW_FLINGER.set(this, viewFlinger);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /* access modifiers changed from: private */
    public boolean springAvailable() {
        return getOverScrollMode() != 2 && getSpringEnabled();
    }

    public void draw(Canvas canvas) {
        int horizontalDistance = this.mSpringHelper.getHorizontalDistance();
        int verticalDistance = this.mSpringHelper.getVerticalDistance();
        if (horizontalDistance == 0 && verticalDistance == 0) {
            super.draw(canvas);
            return;
        }
        int save = canvas.save();
        canvas.translate((float) (-horizontalDistance), (float) (-verticalDistance));
        super.draw(canvas);
        canvas.restoreToCount(save);
    }

    public /* bridge */ /* synthetic */ boolean getSpringEnabled() {
        return super.getSpringEnabled();
    }

    public /* bridge */ /* synthetic */ boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return super.onInterceptTouchEvent(motionEvent);
    }

    public void onScrollStateChanged(int i) {
        super.onScrollStateChanged(i);
        this.mManagedScrollState = i;
        if (springAvailable() && i != 2 && (this.mHorizontalOverScrolling || this.mVerticalOverScrolling)) {
            this.mSpringFlinger.stop();
            this.mHorizontalOverScrolling = false;
            this.mVerticalOverScrolling = false;
        }
    }

    public /* bridge */ /* synthetic */ boolean onTouchEvent(MotionEvent motionEvent) {
        return super.onTouchEvent(motionEvent);
    }

    public /* bridge */ /* synthetic */ void setOverScrollMode(int i) {
        super.setOverScrollMode(i);
    }

    /* access modifiers changed from: 0000 */
    public void setScrollState(int i) {
        if (this.mManagedScrollState == 1 && i == 0) {
            int horizontalDistance = this.mSpringHelper.getHorizontalDistance();
            int verticalDistance = this.mSpringHelper.getVerticalDistance();
            if (!(horizontalDistance == 0 && verticalDistance == 0)) {
                this.mSpringFlinger.springBack(horizontalDistance, verticalDistance);
                return;
            }
        }
        super.setScrollState(i);
    }

    public /* bridge */ /* synthetic */ void setSpringEnabled(boolean z) {
        super.setSpringEnabled(z);
    }
}
