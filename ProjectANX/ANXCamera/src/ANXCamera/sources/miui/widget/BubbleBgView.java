package miui.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import com.miui.internal.util.DeviceHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class BubbleBgView extends View {
    private static final int[] BUBBLE_DATA = (DeviceHelper.IS_TABLET ? new int[]{110, 197, 198, 0, 30, -80, 20, -9703, 160, -32, 230, 0, 30, 0, 100, 872358400, 591, 24, m.cW, 0, 30, -50, 50, -419432704, 805, 273, 173, -30, 10, -80, 20, 1929561904, m.cs, -68, m.dL, 0, 30, 0, 50, -637576704} : new int[]{39, 161, 103, 0, 30, -80, 20, -9703, 103, 39, 114, 0, 30, 0, 100, 872358400, 302, 41, 211, 0, 30, -50, 50, -419432704, m.dF, 166, 119, -30, 10, -80, 20, 1929561904, 171, 226, 181, -15, 15, -20, 30, 1291385080, af.au, 33, 195, 0, 30, 0, 50, -637576704});
    private static final int CNT_DATA_PER = 8;
    private static final int INDEX_COLOR = 7;
    private static final int INDEX_MAXDX = 4;
    private static final int INDEX_MAXDY = 6;
    private static final int INDEX_MINDX = 3;
    private static final int INDEX_MINDY = 5;
    private static final int INDEX_RADIUS = 2;
    private static final int INDEX_X = 0;
    private static final int INDEX_Y = 1;
    private static final int MSG_FLUSH = 1;
    private ArrayList mBubbles;
    /* access modifiers changed from: private */
    public Handler mHandler;
    private Paint mPaint;

    class Bubble {
        private static final int DURATION = 3000;
        private static final int MIN_DURATION = 2000;
        private static final Random RANDOM = new Random(System.currentTimeMillis());
        private final int cX;
        private final int cY;
        /* access modifiers changed from: private */
        public final int color;
        private AnimatorSet mAnimator;
        private final int maxX;
        private final int maxY;
        private final int minX;
        private final int minY;
        /* access modifiers changed from: private */
        public final int radius;

        public Bubble(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            this.cX = i;
            this.cY = i2;
            this.radius = i3;
            this.minX = i4 + i;
            this.maxX = i5 + i;
            this.minY = i6 + i2;
            this.maxY = i7 + i2;
            this.color = i8;
            resetAnimator();
        }

        /* access modifiers changed from: private */
        public float getCurrentX() {
            AnimatorSet animatorSet = this.mAnimator;
            return animatorSet == null ? (float) this.cX : ((Float) ((ValueAnimator) animatorSet.getChildAnimations().get(0)).getAnimatedValue()).floatValue();
        }

        /* access modifiers changed from: private */
        public float getCurrentY() {
            AnimatorSet animatorSet = this.mAnimator;
            return animatorSet == null ? (float) this.cY : ((Float) ((ValueAnimator) animatorSet.getChildAnimations().get(1)).getAnimatedValue()).floatValue();
        }

        /* access modifiers changed from: private */
        public void resetAnimator() {
            int nextInt = RANDOM.nextInt(1000) + 2000;
            this.mAnimator = new AnimatorSet();
            ValueAnimator valueAnimator = new ValueAnimator();
            long j = (long) nextInt;
            valueAnimator.setDuration(j);
            ValueAnimator valueAnimator2 = new ValueAnimator();
            valueAnimator2.setDuration(j);
            this.mAnimator.playTogether(new Animator[]{valueAnimator, valueAnimator2});
            this.mAnimator.addListener(new AnimatorListener() {
                public void onAnimationCancel(Animator animator) {
                }

                public void onAnimationEnd(Animator animator) {
                    Bubble.this.resetAnimator();
                }

                public void onAnimationRepeat(Animator animator) {
                }

                public void onAnimationStart(Animator animator) {
                }
            });
            this.mAnimator.cancel();
            int nextInt2 = RANDOM.nextInt(this.maxX - this.minX) + this.minX;
            int nextInt3 = RANDOM.nextInt(this.maxY - this.minY) + this.minY;
            ValueAnimator valueAnimator3 = (ValueAnimator) this.mAnimator.getChildAnimations().get(0);
            int i = this.cX;
            valueAnimator3.setFloatValues(new float[]{(float) i, (float) nextInt2, (float) i});
            ValueAnimator valueAnimator4 = (ValueAnimator) this.mAnimator.getChildAnimations().get(1);
            int i2 = this.cY;
            valueAnimator4.setFloatValues(new float[]{(float) i2, (float) nextInt3, (float) i2});
            this.mAnimator.start();
        }
    }

    public BubbleBgView(Context context) {
        this(context, null);
    }

    public BubbleBgView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BubbleBgView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mBubbles = new ArrayList();
        this.mPaint = new Paint(1);
        this.mHandler = new Handler() {
            private static final int FLUSH_RATE = 100;

            public void handleMessage(Message message) {
                if (message.what == 1) {
                    BubbleBgView.this.invalidate();
                    BubbleBgView.this.mHandler.sendEmptyMessageDelayed(1, 10);
                }
            }
        };
        initBubbles();
        this.mHandler.sendEmptyMessage(1);
    }

    private void initBubbles() {
        float f = getResources().getDisplayMetrics().density;
        int i = 0;
        while (true) {
            int[] iArr = BUBBLE_DATA;
            if (i < iArr.length) {
                int i2 = (int) (((float) iArr[i + 0]) * f);
                int i3 = (int) (((float) iArr[i + 1]) * f);
                int i4 = (int) (((float) iArr[i + 2]) * f);
                int i5 = iArr[i + 3];
                int i6 = iArr[i + 4];
                int i7 = iArr[i + 5];
                int i8 = iArr[i + 6];
                int i9 = iArr[i + 7];
                ArrayList arrayList = this.mBubbles;
                Bubble bubble = new Bubble(i2, i3, i4, i5, i6, i7, i8, i9);
                arrayList.add(bubble);
                i += 8;
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Iterator it = this.mBubbles.iterator();
        while (it.hasNext()) {
            Bubble bubble = (Bubble) it.next();
            this.mPaint.setColor(bubble.color);
            canvas.drawCircle(bubble.getCurrentX(), bubble.getCurrentY(), (float) bubble.radius, this.mPaint);
        }
    }
}
