package com.android.camera.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import com.android.camera.CameraAppImpl;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.log.Log;
import com.android.camera.panorama.MorphoPanoramaGP3;
import com.android.camera.ui.drawable.TriangleIndicatorDrawable;
import java.util.HashMap;
import java.util.Objects;

public class PanoMovingIndicatorView extends View {
    private static final int DRAW_TAIL = 1;
    private static final int MAX_GAP = Util.dpToPixel(6.0f);
    private static final int MAX_SPEED_THRESHOLD = 7000;
    private static final int MOVING_BOTTOM_TO_TOP = 2;
    private static final int MOVING_LEFT_TO_RIGHT = 0;
    private static final int MOVING_RIGHT_TO_LEFT = 1;
    private static final int MOVING_TOP_TO_BOTTOM = 3;
    private static final float SHOW_ALIGN_THRESHOLD = 0.25f;
    private static final int SPEED_DEVIATION = (2500 / MAX_GAP);
    private static final float SPEED_FILTER_THRESHOLD = 0.1f;
    private static final int STONE_WIDTH = 22;
    public static final String TAG = "PanoMovingIndicatorView";
    private static int[] sBlockWidth = {Util.dpToPixel(0.67f), Util.dpToPixel(2.0f), Util.dpToPixel(3.34f)};
    private static int[] sGapWidth = {Util.dpToPixel(2.67f), Util.dpToPixel(2.0f), Util.dpToPixel(1.34f)};
    private static HashMap sTimesMap = new HashMap(2);
    private int mArrowMargin = CameraAppImpl.getAndroidContext().getResources().getDimensionPixelOffset(R.dimen.pano_arrow_margin);
    private Point mCurrentFramePos = new Point();
    private int mDirection;
    private int mDisplayCenterY;
    private boolean mFast;
    private int mFilterMoveSpeed;
    private int mHalfStoneHeight;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.what == 1) {
                PanoMovingIndicatorView panoMovingIndicatorView = PanoMovingIndicatorView.this;
                if (((float) panoMovingIndicatorView.getPointGap(panoMovingIndicatorView.mLatestSpeed)) != PanoMovingIndicatorView.this.mPointGap) {
                    PanoMovingIndicatorView panoMovingIndicatorView2 = PanoMovingIndicatorView.this;
                    panoMovingIndicatorView2.filterSpeed(panoMovingIndicatorView2.mLatestSpeed);
                    PanoMovingIndicatorView.this.applyNewGap();
                    sendEmptyMessageDelayed(1, 10);
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public int mLatestSpeed;
    private TriangleIndicatorDrawable mMovingDirectionIc = new TriangleIndicatorDrawable();
    private int mOffsetX;
    private int mOffsetY;
    protected int mOrientation;
    /* access modifiers changed from: private */
    public float mPointGap = -1.0f;
    private int mPreviewCenterY;
    private StateChangeTrigger mStateChangeTrigger = new StateChangeTrigger(Boolean.valueOf(false), sTimesMap);
    private Paint mTailPaint;

    class StateChangeTrigger {
        private Object mCurrentState;
        private Object mLatestState;
        private int mLatestTimes = 0;
        private int mMaxTimes;
        private HashMap mMaxTimesMap;

        public StateChangeTrigger(Object obj, HashMap hashMap) {
            this.mLatestState = obj;
            this.mCurrentState = obj;
            this.mMaxTimesMap = hashMap;
        }

        public Object filter(Object obj) {
            if (!Objects.equals(this.mLatestState, obj)) {
                this.mLatestState = obj;
                this.mLatestTimes = 1;
                Integer num = (Integer) this.mMaxTimesMap.get(this.mLatestState);
                this.mMaxTimes = num == null ? 3 : num.intValue();
            } else {
                int i = this.mLatestTimes;
                if (i < this.mMaxTimes) {
                    this.mLatestTimes = i + 1;
                    String str = PanoMovingIndicatorView.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("mLatestState=");
                    sb.append(this.mLatestState);
                    sb.append("  ");
                    sb.append(this.mLatestTimes);
                    Log.v(str, sb.toString());
                }
            }
            if (this.mMaxTimes == this.mLatestTimes && !Objects.equals(this.mCurrentState, this.mLatestState)) {
                this.mCurrentState = this.mLatestState;
            }
            return this.mCurrentState;
        }

        public void setCurrentState(Object obj) {
            this.mCurrentState = obj;
        }
    }

    static {
        sTimesMap.put(Boolean.valueOf(true), Integer.valueOf(1));
        sTimesMap.put(Boolean.valueOf(false), Integer.valueOf(4));
    }

    public PanoMovingIndicatorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mMovingDirectionIc.setWidth(context.getResources().getDimensionPixelSize(R.dimen.pano_arrow_width));
        this.mMovingDirectionIc.setHeight(context.getResources().getDimensionPixelSize(R.dimen.pano_arrow_height));
        this.mTailPaint = new Paint();
        this.mTailPaint.setColor(-1);
        this.mHalfStoneHeight = context.getResources().getDimensionPixelSize(R.dimen.pano_tail_height);
    }

    /* access modifiers changed from: private */
    public void applyNewGap() {
        this.mPointGap = (float) getPointGap(this.mFilterMoveSpeed);
        invalidate();
    }

    /* access modifiers changed from: private */
    public void filterSpeed(int i) {
        this.mFilterMoveSpeed = (int) ((((float) this.mFilterMoveSpeed) * 0.9f) + (((float) i) * 0.1f));
    }

    /* access modifiers changed from: private */
    public int getPointGap(int i) {
        if (i > 4500) {
            return (MAX_GAP * ((i - MorphoPanoramaGP3.FAST_SPEED_THRESHOLD) + SPEED_DEVIATION)) / 2500;
        }
        return -1;
    }

    public boolean isFar() {
        Point point = this.mCurrentFramePos;
        if (!(point.y == Integer.MIN_VALUE || this.mPreviewCenterY == 0)) {
            int i = this.mDirection;
            int i2 = (i == 0 || i == 1) ? this.mCurrentFramePos.y : point.x;
            if (((float) Math.abs(i2 - this.mPreviewCenterY)) >= ((float) this.mPreviewCenterY) * SHOW_ALIGN_THRESHOLD) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("too far current is ");
                sb.append(i2);
                sb.append(" refy is ");
                sb.append(this.mPreviewCenterY);
                Log.e(str, sb.toString());
                return true;
            }
        }
        return false;
    }

    public boolean isTooFast() {
        return this.mPointGap > 0.0f;
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0042  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0048  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0082 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00aa  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00b0  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00d2  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00d8  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onDraw(Canvas canvas) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        float f;
        int i8;
        float f2;
        Canvas canvas2 = canvas;
        Point point = this.mCurrentFramePos;
        int i9 = point.x;
        if (i9 != Integer.MIN_VALUE) {
            int i10 = point.y;
            if (i10 != Integer.MIN_VALUE) {
                int i11 = this.mArrowMargin;
                TriangleIndicatorDrawable triangleIndicatorDrawable = this.mMovingDirectionIc;
                int i12 = this.mDirection;
                if (i12 == 0) {
                    i9 += this.mOffsetX + i11;
                } else if (1 == i12) {
                    i9 -= this.mOffsetX + i11;
                } else if (2 == i12) {
                    i10 -= i11;
                } else if (3 == i12) {
                    i10 += i11;
                } else {
                    i2 = i9;
                    i = 0;
                    canvas.save();
                    canvas2.translate((float) i2, (float) i);
                    i3 = this.mDirection;
                    if (1 != i3) {
                        f2 = 180.0f;
                    } else if (i3 == 2) {
                        f2 = 270.0f;
                    } else {
                        if (i3 == 3) {
                            f2 = 90.0f;
                        }
                        int i13 = -triangleIndicatorDrawable.getIntrinsicWidth();
                        triangleIndicatorDrawable.setBounds(i13, (-triangleIndicatorDrawable.getIntrinsicHeight()) / 2, 0, triangleIndicatorDrawable.getIntrinsicHeight() / 2);
                        triangleIndicatorDrawable.draw(canvas2);
                        canvas2.translate((float) triangleIndicatorDrawable.getIntrinsicWidth(), 0.0f);
                        float f3 = (float) i13;
                        float f4 = this.mPointGap;
                        i4 = (int) (f3 - (22.0f + f4));
                        i5 = (int) f4;
                        for (i6 = 0; i6 < sGapWidth.length && i5 > 0; i6++) {
                            float f5 = (float) i4;
                            int i14 = this.mHalfStoneHeight;
                            float f6 = (float) i14;
                            int i15 = i4;
                            i8 = i5;
                            canvas.drawRect(f5, (float) (-i14), (float) (sBlockWidth[i6] + i4), f6, this.mTailPaint);
                            int i16 = i15 + sBlockWidth[i6];
                            if (i8 < sGapWidth[i6]) {
                                i4 = i16 + 8;
                                i5 = i8 - 8;
                            } else {
                                i4 = i16 + i8;
                                i5 = 0;
                            }
                        }
                        canvas.drawRect((float) i4, (float) (-this.mHalfStoneHeight), (float) (-triangleIndicatorDrawable.getIntrinsicWidth()), (float) this.mHalfStoneHeight, this.mTailPaint);
                        i7 = this.mDirection;
                        if (1 == i7) {
                            f = -180.0f;
                        } else if (i7 == 2) {
                            f = -270.0f;
                        } else {
                            if (i7 == 3) {
                                f = -90.0f;
                            }
                            canvas2.translate((float) (-i2), (float) (-i));
                            canvas.restore();
                        }
                        canvas2.rotate(f);
                        canvas2.translate((float) (-i2), (float) (-i));
                        canvas.restore();
                    }
                    canvas2.rotate(f2);
                    int i132 = -triangleIndicatorDrawable.getIntrinsicWidth();
                    triangleIndicatorDrawable.setBounds(i132, (-triangleIndicatorDrawable.getIntrinsicHeight()) / 2, 0, triangleIndicatorDrawable.getIntrinsicHeight() / 2);
                    triangleIndicatorDrawable.draw(canvas2);
                    canvas2.translate((float) triangleIndicatorDrawable.getIntrinsicWidth(), 0.0f);
                    float f32 = (float) i132;
                    float f42 = this.mPointGap;
                    i4 = (int) (f32 - (22.0f + f42));
                    i5 = (int) f42;
                    while (i6 < sGapWidth.length) {
                        float f52 = (float) i4;
                        int i142 = this.mHalfStoneHeight;
                        float f62 = (float) i142;
                        int i152 = i4;
                        i8 = i5;
                        canvas.drawRect(f52, (float) (-i142), (float) (sBlockWidth[i6] + i4), f62, this.mTailPaint);
                        int i162 = i152 + sBlockWidth[i6];
                        if (i8 < sGapWidth[i6]) {
                        }
                    }
                    canvas.drawRect((float) i4, (float) (-this.mHalfStoneHeight), (float) (-triangleIndicatorDrawable.getIntrinsicWidth()), (float) this.mHalfStoneHeight, this.mTailPaint);
                    i7 = this.mDirection;
                    if (1 == i7) {
                    }
                    canvas2.rotate(f);
                    canvas2.translate((float) (-i2), (float) (-i));
                    canvas.restore();
                }
                i = i10;
                i2 = i9;
                canvas.save();
                canvas2.translate((float) i2, (float) i);
                i3 = this.mDirection;
                if (1 != i3) {
                }
                canvas2.rotate(f2);
                int i1322 = -triangleIndicatorDrawable.getIntrinsicWidth();
                triangleIndicatorDrawable.setBounds(i1322, (-triangleIndicatorDrawable.getIntrinsicHeight()) / 2, 0, triangleIndicatorDrawable.getIntrinsicHeight() / 2);
                triangleIndicatorDrawable.draw(canvas2);
                canvas2.translate((float) triangleIndicatorDrawable.getIntrinsicWidth(), 0.0f);
                float f322 = (float) i1322;
                float f422 = this.mPointGap;
                i4 = (int) (f322 - (22.0f + f422));
                i5 = (int) f422;
                while (i6 < sGapWidth.length) {
                }
                canvas.drawRect((float) i4, (float) (-this.mHalfStoneHeight), (float) (-triangleIndicatorDrawable.getIntrinsicWidth()), (float) this.mHalfStoneHeight, this.mTailPaint);
                i7 = this.mDirection;
                if (1 == i7) {
                }
                canvas2.rotate(f);
                canvas2.translate((float) (-i2), (float) (-i));
                canvas.restore();
            }
        }
    }

    public void setDisplayCenterY(int i) {
        this.mDisplayCenterY = i;
    }

    public void setMovingAttribute(int i, int i2, int i3) {
        int i4;
        if (i != 3) {
            if (i == 4) {
                i4 = 1;
            } else if (i == 5) {
                i4 = 2;
            } else if (i == 6) {
                this.mDirection = 3;
            }
            this.mDirection = i4;
        } else {
            this.mDirection = 0;
        }
        this.mOffsetX = i2;
        this.mOffsetY = i3;
        this.mFast = false;
        this.mFilterMoveSpeed = MorphoPanoramaGP3.FAST_SPEED_THRESHOLD;
        this.mStateChangeTrigger.setCurrentState(Boolean.valueOf(this.mFast));
        this.mCurrentFramePos.set(Integer.MIN_VALUE, Integer.MIN_VALUE);
        this.mPointGap = -1.0f;
    }

    public void setPosition(Point point, int i) {
        this.mCurrentFramePos.set(point.x, point.y);
        this.mPreviewCenterY = i;
        invalidate();
    }

    public void setTooFast(boolean z, int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setTooFast moveSpeed=");
        sb.append(i);
        sb.append(" fastFlag:");
        sb.append(z);
        Log.i(str, sb.toString());
        int i2 = MAX_SPEED_THRESHOLD;
        if (i <= MAX_SPEED_THRESHOLD) {
            i2 = i;
        }
        this.mLatestSpeed = i2;
        if (((float) getPointGap(this.mLatestSpeed)) != this.mPointGap && !this.mHandler.hasMessages(1)) {
            this.mHandler.sendEmptyMessage(1);
        }
    }
}
