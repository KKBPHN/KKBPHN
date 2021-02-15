package com.miui.internal.smooth;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PointF;
import android.graphics.RectF;

public class SmoothPathProvider {
    private static final float DEFAULT_KSI = 0.46f;
    private static final float DEFAULT_SMOOTH = 0.8f;
    private SmoothData mAllData = null;
    private float mKsi = DEFAULT_KSI;
    private float mSmooth = 0.8f;

    public class CornerData {
        public static final int BOTTOM_LEFT = 3;
        public static final int BOTTOM_RIGHT = 2;
        public static final int TOP_LEFT = 0;
        public static final int TOP_RIGHT = 1;
        public PointF[] bezierAnchorHorizontal = new PointF[4];
        public PointF[] bezierAnchorVertical = new PointF[4];
        public float radius;
        public RectF rect;
        public double smoothForHorizontal;
        public double smoothForVertical;
        public float swapAngle;
        public double thetaForHorizontal;
        public double thetaForVertical;

        public void build(float f, RectF rectF, float f2, float f3, double d, float f4, int i) {
            RectF rectF2 = rectF;
            float f5 = f2;
            float f6 = f3;
            double d2 = d;
            float f7 = f4;
            int i2 = i;
            this.radius = f;
            float width = rectF.width();
            float height = rectF.height();
            float f8 = rectF2.left;
            float f9 = rectF2.top;
            float f10 = rectF2.right;
            float f11 = rectF2.bottom;
            this.smoothForHorizontal = SmoothPathProvider.smoothForWidth(width, this.radius, d2, f7);
            this.smoothForVertical = SmoothPathProvider.smoothForHeight(height, this.radius, d2, f7);
            this.thetaForHorizontal = SmoothPathProvider.thetaForWidth(this.smoothForHorizontal);
            this.thetaForVertical = SmoothPathProvider.thetaForHeight(this.smoothForVertical);
            this.swapAngle = (float) SmoothPathProvider.radToAngle((1.5707963267948966d - this.thetaForVertical) - this.thetaForHorizontal);
            double d3 = (double) f7;
            double access$500 = SmoothPathProvider.kForWidth(this.smoothForHorizontal * d3, this.thetaForHorizontal);
            float f12 = f10;
            double access$600 = SmoothPathProvider.mForWidth(this.radius, this.thetaForHorizontal);
            double access$700 = SmoothPathProvider.nForWidth(this.radius, this.thetaForHorizontal);
            double access$800 = SmoothPathProvider.pForWidth(this.radius, this.thetaForHorizontal);
            double access$900 = SmoothPathProvider.xForWidth(this.radius, this.thetaForHorizontal);
            double access$1000 = SmoothPathProvider.yForWidth(access$500, access$900);
            double access$1100 = SmoothPathProvider.kForHeight(this.smoothForVertical * d3, this.thetaForVertical);
            double d4 = access$900;
            double access$1200 = SmoothPathProvider.mForHeight(this.radius, this.thetaForVertical);
            double access$1300 = SmoothPathProvider.nForHeight(this.radius, this.thetaForVertical);
            double access$1400 = SmoothPathProvider.pForHeight(this.radius, this.thetaForVertical);
            double d5 = access$1200;
            double access$1500 = SmoothPathProvider.xForHeight(this.radius, this.thetaForVertical);
            double access$1600 = SmoothPathProvider.yForHeight(access$1100, access$1500);
            if (i2 == 0) {
                float f13 = f8 + f5;
                float f14 = f9 + f6;
                float f15 = this.radius;
                this.rect = new RectF(f13, f14, (f15 * 2.0f) + f13, (f15 * 2.0f) + f14);
                double d6 = access$1600;
                double d7 = (double) f13;
                float f16 = f13;
                double d8 = (double) f14;
                this.bezierAnchorHorizontal[0] = new PointF((float) (access$600 + d7), (float) (access$700 + d8));
                this.bezierAnchorHorizontal[1] = new PointF((float) (access$800 + d7), f14);
                double d9 = access$800 + d4;
                double d10 = access$1500;
                this.bezierAnchorHorizontal[2] = new PointF((float) (d9 + d7), f14);
                this.bezierAnchorHorizontal[3] = new PointF((float) (d9 + access$1000 + d7), f14);
                double d11 = access$1400 + d10;
                float f17 = f16;
                this.bezierAnchorVertical[0] = new PointF(f17, (float) (d11 + d6 + d8));
                this.bezierAnchorVertical[1] = new PointF(f17, (float) (d11 + d8));
                this.bezierAnchorVertical[2] = new PointF(f17, (float) (access$1400 + d8));
                this.bezierAnchorVertical[3] = new PointF((float) (d5 + d7), (float) (access$1300 + d8));
                return;
            }
            double d12 = access$1600;
            double d13 = access$1500;
            if (i2 == 1) {
                float f18 = f9 + f6;
                float f19 = this.radius;
                float f20 = f12 - f5;
                this.rect = new RectF((f12 - (f19 * 2.0f)) - f5, f18, f20, (f19 * 2.0f) + f18);
                double d14 = (double) f12;
                double d15 = d14 - access$800;
                double d16 = d15 - d4;
                float f21 = f20;
                double d17 = (double) f5;
                double d18 = access$1400;
                this.bezierAnchorHorizontal[0] = new PointF((float) ((d16 - access$1000) - d17), f18);
                this.bezierAnchorHorizontal[1] = new PointF((float) (d16 - d17), f18);
                this.bezierAnchorHorizontal[2] = new PointF((float) (d15 - d17), f18);
                double d19 = (double) f18;
                this.bezierAnchorHorizontal[3] = new PointF((float) ((d14 - access$600) - d17), (float) (access$700 + d19));
                this.bezierAnchorVertical[0] = new PointF((float) ((d14 - d5) - d17), (float) (access$1300 + d19));
                float f22 = f21;
                this.bezierAnchorVertical[1] = new PointF(f22, (float) (d18 + d19));
                double d20 = d18 + d13;
                this.bezierAnchorVertical[2] = new PointF(f22, (float) (d20 + d19));
                this.bezierAnchorVertical[3] = new PointF(f22, (float) (d20 + d12 + d19));
                return;
            }
            float f23 = f12;
            double d21 = access$1400;
            if (i2 == 2) {
                float f24 = this.radius;
                float f25 = f23 - f5;
                float f26 = f11 - f6;
                this.rect = new RectF((f23 - (f24 * 2.0f)) - f5, (f11 - (f24 * 2.0f)) - f6, f25, f26);
                double d22 = (double) f23;
                double d23 = (double) f5;
                float f27 = f25;
                float f28 = f26;
                double d24 = (double) f11;
                double d25 = d23;
                double d26 = (double) f6;
                this.bezierAnchorHorizontal[0] = new PointF((float) ((d22 - access$600) - d23), (float) ((d24 - access$700) - d26));
                double d27 = d22 - access$800;
                float f29 = f28;
                this.bezierAnchorHorizontal[1] = new PointF((float) (d27 - d25), f29);
                double d28 = d27 - d4;
                this.bezierAnchorHorizontal[2] = new PointF((float) (d28 - d25), f29);
                this.bezierAnchorHorizontal[3] = new PointF((float) ((d28 - access$1000) - d25), f29);
                double d29 = d24 - d21;
                double d30 = d29 - d13;
                float f30 = f27;
                this.bezierAnchorVertical[0] = new PointF(f30, (float) ((d30 - d12) - d26));
                this.bezierAnchorVertical[1] = new PointF(f30, (float) (d30 - d26));
                this.bezierAnchorVertical[2] = new PointF(f30, (float) (d29 - d26));
                this.bezierAnchorVertical[3] = new PointF((float) ((d22 - d5) - d25), (float) ((d24 - access$1300) - d26));
            } else if (i2 == 3) {
                float f31 = f8 + f5;
                float f32 = this.radius;
                float f33 = f11 - f6;
                this.rect = new RectF(f31, (f11 - (f32 * 2.0f)) - f6, (f32 * 2.0f) + f31, f33);
                double d31 = access$800 + d4;
                double d32 = (double) f31;
                this.bezierAnchorHorizontal[0] = new PointF((float) (d31 + access$1000 + d32), f33);
                this.bezierAnchorHorizontal[1] = new PointF((float) (d31 + d32), f33);
                this.bezierAnchorHorizontal[2] = new PointF((float) (access$800 + d32), f33);
                double d33 = (double) f11;
                double d34 = (double) f6;
                this.bezierAnchorHorizontal[3] = new PointF((float) (access$600 + d32), (float) ((d33 - access$700) - d34));
                this.bezierAnchorVertical[0] = new PointF((float) (d5 + d32), (float) ((d33 - access$1300) - d34));
                double d35 = d33 - d21;
                this.bezierAnchorVertical[1] = new PointF(f31, (float) (d35 - d34));
                double d36 = d35 - d13;
                this.bezierAnchorVertical[2] = new PointF(f31, (float) (d36 - d34));
                this.bezierAnchorVertical[3] = new PointF(f31, (float) ((d36 - d12) - d34));
            }
        }
    }

    public class SmoothData {
        public CornerData bottomLeft = null;
        public CornerData bottomRight = null;
        public float height;
        public float ksi;
        public double smooth;
        public CornerData topLeft = null;
        public CornerData topRight = null;
        public float width;

        public SmoothData(float f, float f2, double d, float f3) {
            this.width = f;
            this.height = f2;
            this.smooth = d;
            this.ksi = f3;
        }
    }

    private void ensureFourCornerData() {
        SmoothData smoothData = this.mAllData;
        if (smoothData.topLeft == null) {
            smoothData.topLeft = new CornerData();
        }
        SmoothData smoothData2 = this.mAllData;
        if (smoothData2.topRight == null) {
            smoothData2.topRight = new CornerData();
        }
        SmoothData smoothData3 = this.mAllData;
        if (smoothData3.bottomRight == null) {
            smoothData3.bottomRight = new CornerData();
        }
        SmoothData smoothData4 = this.mAllData;
        if (smoothData4.bottomLeft == null) {
            smoothData4.bottomLeft = new CornerData();
        }
    }

    private boolean isFourCornerDataValid() {
        SmoothData smoothData = this.mAllData;
        return smoothData.topLeft == null || smoothData.topRight == null || smoothData.bottomRight == null || smoothData.bottomLeft == null;
    }

    private static boolean isHeightCollapsed(float f, float f2, float f3, double d, float f4) {
        return ((double) f) <= ((double) (f2 + f3)) * ((d * ((double) f4)) + 1.0d);
    }

    private static boolean isWidthCollapsed(float f, float f2, float f3, double d, float f4) {
        return ((double) f) <= ((double) (f2 + f3)) * ((d * ((double) f4)) + 1.0d);
    }

    /* access modifiers changed from: private */
    public static double kForHeight(double d, double d2) {
        if (d2 == 0.0d) {
            return 0.0d;
        }
        double d3 = d2 / 2.0d;
        return (((((d * 0.46000000834465027d) + Math.tan(d3)) * 2.0d) * (Math.cos(d2) + 1.0d)) / (Math.tan(d3) * 3.0d)) - 1.0d;
    }

    /* access modifiers changed from: private */
    public static double kForWidth(double d, double d2) {
        if (d2 == 0.0d) {
            return 0.0d;
        }
        double d3 = d2 / 2.0d;
        return (((((d * 0.46000000834465027d) + Math.tan(d3)) * 2.0d) * (Math.cos(d2) + 1.0d)) / (Math.tan(d3) * 3.0d)) - 1.0d;
    }

    /* access modifiers changed from: private */
    public static double mForHeight(float f, double d) {
        return ((double) f) * (1.0d - Math.cos(d));
    }

    /* access modifiers changed from: private */
    public static double mForWidth(float f, double d) {
        return ((double) f) * (1.0d - Math.sin(d));
    }

    /* access modifiers changed from: private */
    public static double nForHeight(float f, double d) {
        return ((double) f) * (1.0d - Math.sin(d));
    }

    /* access modifiers changed from: private */
    public static double nForWidth(float f, double d) {
        return ((double) f) * (1.0d - Math.cos(d));
    }

    /* access modifiers changed from: private */
    public static double pForHeight(float f, double d) {
        return ((double) f) * (1.0d - Math.tan(d / 2.0d));
    }

    /* access modifiers changed from: private */
    public static double pForWidth(float f, double d) {
        return ((double) f) * (1.0d - Math.tan(d / 2.0d));
    }

    /* access modifiers changed from: private */
    public static double radToAngle(double d) {
        return (d * 180.0d) / 3.141592653589793d;
    }

    /* access modifiers changed from: private */
    public static double smoothForHeight(float f, float f2, double d, float f3) {
        return isHeightCollapsed(f, f2, f2, d, f3) ? (double) Math.max(Math.min(((f / (f2 * 2.0f)) - 1.0f) / f3, 1.0f), 0.0f) : d;
    }

    /* access modifiers changed from: private */
    public static double smoothForWidth(float f, float f2, double d, float f3) {
        return isWidthCollapsed(f, f2, f2, d, f3) ? (double) Math.max(Math.min(((f / (f2 * 2.0f)) - 1.0f) / f3, 1.0f), 0.0f) : d;
    }

    /* access modifiers changed from: private */
    public static double thetaForHeight(double d) {
        return (d * 3.141592653589793d) / 4.0d;
    }

    /* access modifiers changed from: private */
    public static double thetaForWidth(double d) {
        return (d * 3.141592653589793d) / 4.0d;
    }

    /* access modifiers changed from: private */
    public static double xForHeight(float f, double d) {
        return ((((double) f) * 1.5d) * Math.tan(d / 2.0d)) / (Math.cos(d) + 1.0d);
    }

    /* access modifiers changed from: private */
    public static double xForWidth(float f, double d) {
        return ((((double) f) * 1.5d) * Math.tan(d / 2.0d)) / (Math.cos(d) + 1.0d);
    }

    /* access modifiers changed from: private */
    public static double yForHeight(double d, double d2) {
        return d * d2;
    }

    /* access modifiers changed from: private */
    public static double yForWidth(double d, double d2) {
        return d * d2;
    }

    public void buildSmoothData(RectF rectF, float f) {
        buildSmoothData(rectF, f, 0.0f, 0.0f);
    }

    public void buildSmoothData(RectF rectF, float f, float f2, float f3) {
        buildSmoothData(rectF, new float[]{f, f, f, f, f, f, f, f}, f2, f3);
    }

    public void buildSmoothData(RectF rectF, float[] fArr) {
        buildSmoothData(rectF, fArr, 0.0f, 0.0f);
    }

    public void buildSmoothData(RectF rectF, float[] fArr, float f, float f2) {
        float[] fArr2 = fArr;
        float ksi = getKsi();
        float smooth = getSmooth();
        float width = rectF.width();
        float height = rectF.height();
        double d = (double) smooth;
        SmoothData smoothData = new SmoothData(width, height, d, ksi);
        this.mAllData = smoothData;
        if (fArr2 != null) {
            float[] fArr3 = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
            for (int i = 0; i < Math.min(fArr3.length, fArr2.length); i++) {
                fArr3[i] = fArr2[i];
            }
            float f3 = fArr3[0];
            float f4 = fArr3[1];
            float f5 = fArr3[2];
            float f6 = fArr3[3];
            float f7 = fArr3[4];
            float f8 = fArr3[5];
            float f9 = fArr3[6];
            float f10 = fArr3[7];
            if (f3 + f5 > width) {
                f3 = (fArr3[0] * width) / (fArr3[0] + fArr3[2]);
                f5 = (fArr3[2] * width) / (fArr3[0] + fArr3[2]);
            }
            float f11 = f5;
            if (f6 + f8 > height) {
                f6 = (fArr3[3] * height) / (fArr3[3] + fArr3[5]);
                f8 = (fArr3[5] * height) / (fArr3[3] + fArr3[5]);
            }
            float f12 = f8;
            if (f7 + f9 > width) {
                f7 = (fArr3[4] * width) / (fArr3[4] + fArr3[6]);
                f9 = (width * fArr3[6]) / (fArr3[4] + fArr3[6]);
            }
            float f13 = f7;
            float f14 = f9;
            if (f10 + f4 > height) {
                f10 = (fArr3[7] * height) / (fArr3[7] + fArr3[1]);
                f4 = (height * fArr3[1]) / (fArr3[7] + fArr3[1]);
            }
            float f15 = f10;
            ensureFourCornerData();
            CornerData cornerData = this.mAllData.topLeft;
            float min = Math.min(f3, f4);
            CornerData cornerData2 = cornerData;
            float f16 = min;
            RectF rectF2 = rectF;
            float f17 = f;
            float f18 = f2;
            float f19 = f11;
            float f20 = f14;
            float f21 = ksi;
            float f22 = f15;
            cornerData2.build(f16, rectF2, f17, f18, d, f21, 0);
            double d2 = d;
            this.mAllData.topRight.build(Math.min(f19, f6), rectF2, f17, f18, d2, f21, 1);
            this.mAllData.bottomRight.build(Math.min(f13, f12), rectF2, f17, f18, d2, f21, 2);
            this.mAllData.bottomLeft.build(Math.min(f20, f22), rectF, f, f2, d, ksi, 3);
        }
    }

    public void drawPath(Canvas canvas, Paint paint, int i, int i2, int i3) {
        Canvas canvas2 = canvas;
        Paint paint2 = paint;
        int i4 = i2;
        int i5 = i3;
        if (isFourCornerDataValid()) {
            paint.setColor(i);
            SmoothData smoothData = this.mAllData;
            canvas2.drawRect(new RectF(0.0f, 0.0f, smoothData.width, smoothData.height), paint2);
            return;
        }
        PointF pointF = new PointF();
        paint2.setColor(i4);
        CornerData cornerData = this.mAllData.topLeft;
        canvas.drawArc(cornerData.rect, (float) radToAngle(cornerData.thetaForVertical + 3.141592653589793d), this.mAllData.topLeft.swapAngle, false, paint);
        CornerData cornerData2 = this.mAllData.topLeft;
        PointF[] pointFArr = cornerData2.bezierAnchorHorizontal;
        pointF.x = pointFArr[0].x;
        pointF.y = pointFArr[0].y;
        if (cornerData2.smoothForHorizontal != 0.0d) {
            Path path = new Path();
            path.moveTo(pointF.x, pointF.y);
            PointF[] pointFArr2 = this.mAllData.topLeft.bezierAnchorHorizontal;
            path.cubicTo(pointFArr2[1].x, pointFArr2[1].y, pointFArr2[2].x, pointFArr2[2].y, pointFArr2[3].x, pointFArr2[3].y);
            paint2.setColor(i5);
            canvas2.drawPath(path, paint2);
            PointF[] pointFArr3 = this.mAllData.topLeft.bezierAnchorHorizontal;
            pointF.x = pointFArr3[3].x;
            pointF.y = pointFArr3[3].y;
        }
        SmoothData smoothData2 = this.mAllData;
        if (!isWidthCollapsed(smoothData2.width, smoothData2.topLeft.radius, smoothData2.topRight.radius, smoothData2.smooth, smoothData2.ksi)) {
            paint.setColor(i);
            float f = pointF.x;
            float f2 = pointF.y;
            PointF[] pointFArr4 = this.mAllData.topRight.bezierAnchorHorizontal;
            canvas.drawLine(f, f2, pointFArr4[0].x, pointFArr4[0].y, paint);
            PointF[] pointFArr5 = this.mAllData.topRight.bezierAnchorHorizontal;
            pointF.x = pointFArr5[0].x;
            pointF.y = pointFArr5[0].y;
        }
        if (this.mAllData.topRight.smoothForHorizontal != 0.0d) {
            Path path2 = new Path();
            path2.moveTo(pointF.x, pointF.y);
            PointF[] pointFArr6 = this.mAllData.topRight.bezierAnchorHorizontal;
            path2.cubicTo(pointFArr6[1].x, pointFArr6[1].y, pointFArr6[2].x, pointFArr6[2].y, pointFArr6[3].x, pointFArr6[3].y);
            paint2.setColor(i5);
            canvas2.drawPath(path2, paint2);
            PointF[] pointFArr7 = this.mAllData.topRight.bezierAnchorHorizontal;
            pointF.x = pointFArr7[3].x;
            pointF.y = pointFArr7[3].y;
        }
        paint2.setColor(i4);
        CornerData cornerData3 = this.mAllData.topRight;
        canvas.drawArc(cornerData3.rect, (float) radToAngle(cornerData3.thetaForHorizontal + 4.71238898038469d), this.mAllData.topRight.swapAngle, false, paint);
        CornerData cornerData4 = this.mAllData.topRight;
        PointF[] pointFArr8 = cornerData4.bezierAnchorVertical;
        pointF.x = pointFArr8[0].x;
        pointF.y = pointFArr8[0].y;
        if (cornerData4.smoothForVertical != 0.0d) {
            Path path3 = new Path();
            path3.moveTo(pointF.x, pointF.y);
            PointF[] pointFArr9 = this.mAllData.topRight.bezierAnchorVertical;
            path3.cubicTo(pointFArr9[1].x, pointFArr9[1].y, pointFArr9[2].x, pointFArr9[2].y, pointFArr9[3].x, pointFArr9[3].y);
            paint2.setColor(i5);
            canvas2.drawPath(path3, paint2);
            PointF[] pointFArr10 = this.mAllData.topRight.bezierAnchorVertical;
            pointF.x = pointFArr10[3].x;
            pointF.y = pointFArr10[3].y;
        }
        SmoothData smoothData3 = this.mAllData;
        if (!isHeightCollapsed(smoothData3.height, smoothData3.topRight.radius, smoothData3.bottomRight.radius, smoothData3.smooth, smoothData3.ksi)) {
            paint.setColor(i);
            float f3 = pointF.x;
            float f4 = pointF.y;
            PointF[] pointFArr11 = this.mAllData.bottomRight.bezierAnchorVertical;
            canvas.drawLine(f3, f4, pointFArr11[0].x, pointFArr11[0].y, paint);
            PointF[] pointFArr12 = this.mAllData.bottomRight.bezierAnchorVertical;
            pointF.x = pointFArr12[0].x;
            pointF.y = pointFArr12[0].y;
        }
        if (this.mAllData.bottomRight.smoothForVertical != 0.0d) {
            Path path4 = new Path();
            path4.moveTo(pointF.x, pointF.y);
            PointF[] pointFArr13 = this.mAllData.bottomRight.bezierAnchorVertical;
            path4.cubicTo(pointFArr13[1].x, pointFArr13[1].y, pointFArr13[2].x, pointFArr13[2].y, pointFArr13[3].x, pointFArr13[3].y);
            paint2.setColor(i5);
            canvas2.drawPath(path4, paint2);
            PointF[] pointFArr14 = this.mAllData.bottomRight.bezierAnchorVertical;
            pointF.x = pointFArr14[3].x;
            pointF.y = pointFArr14[3].y;
        }
        paint2.setColor(i4);
        CornerData cornerData5 = this.mAllData.bottomRight;
        canvas.drawArc(cornerData5.rect, (float) radToAngle(cornerData5.thetaForVertical), this.mAllData.bottomRight.swapAngle, false, paint);
        CornerData cornerData6 = this.mAllData.bottomRight;
        PointF[] pointFArr15 = cornerData6.bezierAnchorHorizontal;
        pointF.x = pointFArr15[0].x;
        pointF.y = pointFArr15[0].y;
        if (cornerData6.smoothForHorizontal != 0.0d) {
            Path path5 = new Path();
            path5.moveTo(pointF.x, pointF.y);
            PointF[] pointFArr16 = this.mAllData.bottomRight.bezierAnchorHorizontal;
            path5.cubicTo(pointFArr16[1].x, pointFArr16[1].y, pointFArr16[2].x, pointFArr16[2].y, pointFArr16[3].x, pointFArr16[3].y);
            paint2.setColor(i5);
            canvas2.drawPath(path5, paint2);
            PointF[] pointFArr17 = this.mAllData.bottomRight.bezierAnchorHorizontal;
            pointF.x = pointFArr17[3].x;
            pointF.y = pointFArr17[3].y;
        }
        SmoothData smoothData4 = this.mAllData;
        if (!isWidthCollapsed(smoothData4.width, smoothData4.bottomRight.radius, smoothData4.bottomLeft.radius, smoothData4.smooth, smoothData4.ksi)) {
            paint.setColor(i);
            float f5 = pointF.x;
            float f6 = pointF.y;
            PointF[] pointFArr18 = this.mAllData.bottomLeft.bezierAnchorHorizontal;
            canvas.drawLine(f5, f6, pointFArr18[0].x, pointFArr18[0].y, paint);
            PointF[] pointFArr19 = this.mAllData.bottomLeft.bezierAnchorHorizontal;
            pointF.x = pointFArr19[0].x;
            pointF.y = pointFArr19[0].y;
        }
        if (this.mAllData.bottomLeft.smoothForHorizontal != 0.0d) {
            Path path6 = new Path();
            path6.moveTo(pointF.x, pointF.y);
            PointF[] pointFArr20 = this.mAllData.bottomLeft.bezierAnchorHorizontal;
            path6.cubicTo(pointFArr20[1].x, pointFArr20[1].y, pointFArr20[2].x, pointFArr20[2].y, pointFArr20[3].x, pointFArr20[3].y);
            paint2.setColor(i5);
            canvas2.drawPath(path6, paint2);
            PointF[] pointFArr21 = this.mAllData.bottomLeft.bezierAnchorHorizontal;
            pointF.x = pointFArr21[3].x;
            pointF.y = pointFArr21[3].y;
        }
        paint2.setColor(i4);
        CornerData cornerData7 = this.mAllData.bottomLeft;
        canvas.drawArc(cornerData7.rect, (float) radToAngle(cornerData7.thetaForHorizontal + 1.5707963267948966d), this.mAllData.bottomLeft.swapAngle, false, paint);
        CornerData cornerData8 = this.mAllData.bottomLeft;
        PointF[] pointFArr22 = cornerData8.bezierAnchorVertical;
        pointF.x = pointFArr22[0].x;
        pointF.y = pointFArr22[0].y;
        if (cornerData8.smoothForVertical != 0.0d) {
            Path path7 = new Path();
            path7.moveTo(pointF.x, pointF.y);
            PointF[] pointFArr23 = this.mAllData.bottomLeft.bezierAnchorVertical;
            path7.cubicTo(pointFArr23[1].x, pointFArr23[1].y, pointFArr23[2].x, pointFArr23[2].y, pointFArr23[3].x, pointFArr23[3].y);
            paint2.setColor(i5);
            canvas2.drawPath(path7, paint2);
            PointF[] pointFArr24 = this.mAllData.bottomLeft.bezierAnchorVertical;
            pointF.x = pointFArr24[3].x;
            pointF.y = pointFArr24[3].y;
        }
        SmoothData smoothData5 = this.mAllData;
        if (!isHeightCollapsed(smoothData5.height, smoothData5.bottomLeft.radius, smoothData5.topLeft.radius, smoothData5.smooth, smoothData5.ksi)) {
            paint.setColor(i);
            float f7 = pointF.x;
            float f8 = pointF.y;
            PointF[] pointFArr25 = this.mAllData.topLeft.bezierAnchorVertical;
            canvas.drawLine(f7, f8, pointFArr25[0].x, pointFArr25[0].y, paint);
            PointF[] pointFArr26 = this.mAllData.topLeft.bezierAnchorVertical;
            pointF.x = pointFArr26[0].x;
            pointF.y = pointFArr26[0].y;
        }
        if (this.mAllData.topLeft.smoothForVertical != 0.0d) {
            Path path8 = new Path();
            path8.moveTo(pointF.x, pointF.y);
            PointF[] pointFArr27 = this.mAllData.topLeft.bezierAnchorVertical;
            path8.cubicTo(pointFArr27[1].x, pointFArr27[1].y, pointFArr27[2].x, pointFArr27[2].y, pointFArr27[3].x, pointFArr27[3].y);
            paint2.setColor(i5);
            canvas2.drawPath(path8, paint2);
            PointF[] pointFArr28 = this.mAllData.topLeft.bezierAnchorVertical;
            pointF.x = pointFArr28[3].x;
            pointF.y = pointFArr28[3].y;
        }
    }

    /* access modifiers changed from: 0000 */
    public float getKsi() {
        return this.mKsi;
    }

    /* access modifiers changed from: 0000 */
    public float getSmooth() {
        return this.mSmooth;
    }

    public Path getSmoothPath(Path path) {
        if (path == null) {
            path = new Path();
        }
        path.reset();
        if (isFourCornerDataValid()) {
            SmoothData smoothData = this.mAllData;
            path.addRect(new RectF(0.0f, 0.0f, smoothData.width, smoothData.height), Direction.CCW);
            return path;
        }
        CornerData cornerData = this.mAllData.topLeft;
        if (cornerData.swapAngle != 0.0f) {
            path.arcTo(cornerData.rect, (float) radToAngle(cornerData.thetaForVertical + 3.141592653589793d), this.mAllData.topLeft.swapAngle);
        } else {
            PointF[] pointFArr = cornerData.bezierAnchorHorizontal;
            path.moveTo(pointFArr[0].x, pointFArr[0].y);
        }
        CornerData cornerData2 = this.mAllData.topLeft;
        if (cornerData2.smoothForHorizontal != 0.0d) {
            PointF[] pointFArr2 = cornerData2.bezierAnchorHorizontal;
            path.cubicTo(pointFArr2[1].x, pointFArr2[1].y, pointFArr2[2].x, pointFArr2[2].y, pointFArr2[3].x, pointFArr2[3].y);
        }
        SmoothData smoothData2 = this.mAllData;
        if (!isWidthCollapsed(smoothData2.width, smoothData2.topLeft.radius, smoothData2.topRight.radius, smoothData2.smooth, smoothData2.ksi)) {
            PointF[] pointFArr3 = this.mAllData.topRight.bezierAnchorHorizontal;
            path.lineTo(pointFArr3[0].x, pointFArr3[0].y);
        }
        CornerData cornerData3 = this.mAllData.topRight;
        if (cornerData3.smoothForHorizontal != 0.0d) {
            PointF[] pointFArr4 = cornerData3.bezierAnchorHorizontal;
            path.cubicTo(pointFArr4[1].x, pointFArr4[1].y, pointFArr4[2].x, pointFArr4[2].y, pointFArr4[3].x, pointFArr4[3].y);
        }
        CornerData cornerData4 = this.mAllData.topRight;
        if (cornerData4.swapAngle != 0.0f) {
            path.arcTo(cornerData4.rect, (float) radToAngle(cornerData4.thetaForHorizontal + 4.71238898038469d), this.mAllData.topRight.swapAngle);
        }
        CornerData cornerData5 = this.mAllData.topRight;
        if (cornerData5.smoothForVertical != 0.0d) {
            PointF[] pointFArr5 = cornerData5.bezierAnchorVertical;
            path.cubicTo(pointFArr5[1].x, pointFArr5[1].y, pointFArr5[2].x, pointFArr5[2].y, pointFArr5[3].x, pointFArr5[3].y);
        }
        SmoothData smoothData3 = this.mAllData;
        if (!isHeightCollapsed(smoothData3.height, smoothData3.topRight.radius, smoothData3.bottomRight.radius, smoothData3.smooth, smoothData3.ksi)) {
            PointF[] pointFArr6 = this.mAllData.bottomRight.bezierAnchorVertical;
            path.lineTo(pointFArr6[0].x, pointFArr6[0].y);
        }
        CornerData cornerData6 = this.mAllData.bottomRight;
        if (cornerData6.smoothForVertical != 0.0d) {
            PointF[] pointFArr7 = cornerData6.bezierAnchorVertical;
            path.cubicTo(pointFArr7[1].x, pointFArr7[1].y, pointFArr7[2].x, pointFArr7[2].y, pointFArr7[3].x, pointFArr7[3].y);
        }
        CornerData cornerData7 = this.mAllData.bottomRight;
        if (cornerData7.swapAngle != 0.0f) {
            path.arcTo(cornerData7.rect, (float) radToAngle(cornerData7.thetaForVertical), this.mAllData.bottomRight.swapAngle);
        }
        CornerData cornerData8 = this.mAllData.bottomRight;
        if (cornerData8.smoothForHorizontal != 0.0d) {
            PointF[] pointFArr8 = cornerData8.bezierAnchorHorizontal;
            path.cubicTo(pointFArr8[1].x, pointFArr8[1].y, pointFArr8[2].x, pointFArr8[2].y, pointFArr8[3].x, pointFArr8[3].y);
        }
        SmoothData smoothData4 = this.mAllData;
        if (!isWidthCollapsed(smoothData4.width, smoothData4.bottomRight.radius, smoothData4.bottomLeft.radius, smoothData4.smooth, smoothData4.ksi)) {
            PointF[] pointFArr9 = this.mAllData.bottomLeft.bezierAnchorHorizontal;
            path.lineTo(pointFArr9[0].x, pointFArr9[0].y);
        }
        CornerData cornerData9 = this.mAllData.bottomLeft;
        if (cornerData9.smoothForHorizontal != 0.0d) {
            PointF[] pointFArr10 = cornerData9.bezierAnchorHorizontal;
            path.cubicTo(pointFArr10[1].x, pointFArr10[1].y, pointFArr10[2].x, pointFArr10[2].y, pointFArr10[3].x, pointFArr10[3].y);
        }
        CornerData cornerData10 = this.mAllData.bottomLeft;
        if (cornerData10.swapAngle != 0.0f) {
            path.arcTo(cornerData10.rect, (float) radToAngle(cornerData10.thetaForHorizontal + 1.5707963267948966d), this.mAllData.bottomLeft.swapAngle);
        }
        CornerData cornerData11 = this.mAllData.bottomLeft;
        if (cornerData11.smoothForVertical != 0.0d) {
            PointF[] pointFArr11 = cornerData11.bezierAnchorVertical;
            path.cubicTo(pointFArr11[1].x, pointFArr11[1].y, pointFArr11[2].x, pointFArr11[2].y, pointFArr11[3].x, pointFArr11[3].y);
        }
        SmoothData smoothData5 = this.mAllData;
        if (!isHeightCollapsed(smoothData5.height, smoothData5.bottomLeft.radius, smoothData5.topLeft.radius, smoothData5.smooth, smoothData5.ksi)) {
            PointF[] pointFArr12 = this.mAllData.topLeft.bezierAnchorVertical;
            path.lineTo(pointFArr12[0].x, pointFArr12[0].y);
        }
        CornerData cornerData12 = this.mAllData.topLeft;
        if (cornerData12.smoothForVertical != 0.0d) {
            PointF[] pointFArr13 = cornerData12.bezierAnchorVertical;
            path.cubicTo(pointFArr13[1].x, pointFArr13[1].y, pointFArr13[2].x, pointFArr13[2].y, pointFArr13[3].x, pointFArr13[3].y);
        }
        path.close();
        return path;
    }

    /* access modifiers changed from: 0000 */
    public void setKsi(float f) {
        this.mKsi = f;
    }

    /* access modifiers changed from: 0000 */
    public void setSmooth(float f) {
        this.mSmooth = f;
    }
}
