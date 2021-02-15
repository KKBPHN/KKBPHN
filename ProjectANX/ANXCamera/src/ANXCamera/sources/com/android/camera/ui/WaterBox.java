package com.android.camera.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import com.android.camera.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;
import miuix.animation.utils.EaseManager;

public final class WaterBox extends RelativeLayout implements SensorEventListener {
    private static final int INVALID_VALUE = -1;
    public static final int MAX_ANGLE = 35;
    public static final float MAX_VALUE = 1.0f;
    public static final float MinValue = 0.0f;
    public static final int POINT_NUM_DEFAULT = 5;
    private static final int SENSOR_ACC_VALUE_DIMENS = 3;
    private static final int SENSOR_BUFFER_COUNT = 2;
    private static final String TAG = "WaterBox";
    private float[][] mAccValuesForAverage;
    private int mAccValuesForAverageIndex;
    private Sensor mAccelerometer;
    private int mColor;
    private Path mCornerPath;
    private float mCornerRadius;
    private AnimConfig mEdgeRotAnimConfig;
    private PointF mEndPoint;
    private int mGradientColorEnd;
    private int mGradientColorStart;
    private boolean mIsValueSet;
    private boolean mIsVisible;
    private ArrayList mPointAnimConfigs;
    private int mPointNum;
    private ArrayList mPoints;
    private float mPreAngle;
    private PointF mRealEndPoint;
    private PointF mRealStartPoint;
    private RectF mRectF;
    private AnimConfig mRotAnimConfig;
    private float mSensorLastAngle;
    private long mSensorLastChangedTime;
    private SensorManager mSensorManager;
    private PointF mStartPoint;
    private float mValue;
    private AnimConfig mValueAnimConfig;
    private AnimConfig mWaterAlphaAnimConfig;
    private WaterData mWaterData;
    private Paint mWaterPaint;
    private Path mWaterPath;
    private PointF mWaterPointBL;
    private PointF mWaterPointBR;
    private PointF mWaterPointTL;
    private PointF mWaterPointTR;

    class LineEndPoints {
        private final PointF endPoint;
        private final PointF startPoint;

        LineEndPoints(PointF pointF, PointF pointF2) {
            this.startPoint = pointF;
            this.endPoint = pointF2;
        }

        public final PointF component1() {
            return this.startPoint;
        }

        public final PointF component2() {
            return this.endPoint;
        }

        /* access modifiers changed from: 0000 */
        public final PointF getEndPoint() {
            return this.endPoint;
        }

        /* access modifiers changed from: 0000 */
        public final PointF getStartPoint() {
            return this.startPoint;
        }

        public int hashCode() {
            PointF pointF = this.startPoint;
            int i = 0;
            int hashCode = (pointF != null ? pointF.hashCode() : 0) * 31;
            PointF pointF2 = this.endPoint;
            if (pointF2 != null) {
                i = pointF2.hashCode();
            }
            return hashCode + i;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("LineEndPoints(startPoint=");
            sb.append(this.startPoint);
            sb.append(", endPoint=");
            sb.append(this.endPoint);
            sb.append(")");
            return sb.toString();
        }
    }

    class WaterData {
        private float edgeRot = 90.0f;
        private float effectPer;
        private float rot = 90.0f;
        private float value = 0.0f;
        private float waterAlpha = 1.0f;

        WaterData() {
        }

        /* access modifiers changed from: 0000 */
        public float getEdgeRot() {
            return this.edgeRot;
        }

        /* access modifiers changed from: 0000 */
        public float getEffectPer() {
            return this.effectPer;
        }

        /* access modifiers changed from: 0000 */
        public float getRot() {
            return this.rot;
        }

        /* access modifiers changed from: 0000 */
        public float getValue() {
            return this.value;
        }

        /* access modifiers changed from: 0000 */
        public float getWaterAlpha() {
            return this.waterAlpha;
        }

        /* access modifiers changed from: 0000 */
        public void setEdgeRot(float f) {
            this.edgeRot = f;
        }

        /* access modifiers changed from: 0000 */
        public void setEffectPer(float f) {
            this.effectPer = f;
        }

        /* access modifiers changed from: 0000 */
        public void setRot(float f) {
            this.rot = f;
        }

        /* access modifiers changed from: 0000 */
        public void setValue(float f) {
            this.value = f;
        }

        /* access modifiers changed from: 0000 */
        public void setWaterAlpha(float f) {
            if (f < 0.0f) {
                f = 0.0f;
            }
            this.waterAlpha = f;
        }
    }

    public WaterBox(Context context) {
        this(context, null);
    }

    public WaterBox(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public WaterBox(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mGradientColorStart = -1;
        this.mGradientColorEnd = -1;
        this.mPointNum = 5;
        this.mValue = 0.0f;
        this.mAccValuesForAverageIndex = 0;
        this.mSensorLastChangedTime = Long.MAX_VALUE;
        initInConstruct();
    }

    private PointF avgPoints(PointF pointF, PointF pointF2) {
        return new PointF((pointF.x + pointF2.x) / 2.0f, (pointF.y + pointF2.y) / 2.0f);
    }

    private void beginEnterAnim() {
        if (this.mIsValueSet) {
            setValue(this.mValue, false);
        }
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<android.graphics.PointF>, for r8v0, types: [java.util.List, java.util.List<android.graphics.PointF>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void constructWaterPath(PointF pointF, PointF pointF2, List<PointF> list) {
        this.mWaterPath.reset();
        this.mWaterPath.moveTo(pointF2.x, pointF2.y);
        for (PointF pointF3 : list) {
            this.mWaterPath.lineTo(pointF3.x, pointF3.y);
        }
        this.mWaterPath.lineTo(pointF.x, pointF.y);
        ArrayList arrayList = new ArrayList();
        arrayList.add(pointF);
        arrayList.addAll(this.mPoints);
        arrayList.add(pointF2);
        PointF avgPoints = avgPoints((PointF) arrayList.get(0), (PointF) arrayList.get(1));
        this.mWaterPath.lineTo(avgPoints.x, avgPoints.y);
        for (int i = 2; i < arrayList.size(); i++) {
            int i2 = i - 1;
            PointF avgPoints2 = avgPoints((PointF) arrayList.get(i2), (PointF) arrayList.get(i));
            this.mWaterPath.quadTo(((PointF) arrayList.get(i2)).x, ((PointF) arrayList.get(i2)).y, avgPoints2.x, avgPoints2.y);
        }
        this.mWaterPath.lineTo(pointF2.x, pointF2.y);
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x011b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private List constructWaterPoints(PointF pointF, PointF pointF2) {
        PointF[] pointFArr;
        int edge = getEdge(pointF);
        int edge2 = getEdge(pointF2);
        List list = null;
        if (edge == 1) {
            if (edge2 == 1) {
                pointFArr = new PointF[]{this.mWaterPointTL, this.mWaterPointBL, this.mWaterPointBR, this.mWaterPointTR};
            } else if (edge2 == 2) {
                pointFArr = new PointF[]{this.mWaterPointTL};
            } else if (edge2 != 3) {
                if (edge2 == 4) {
                    pointFArr = new PointF[]{this.mWaterPointTL, this.mWaterPointBL, this.mWaterPointBR};
                }
                if (list != null || list.isEmpty()) {
                    throw new IllegalStateException();
                }
                Collections.reverse(list);
                return list;
            } else {
                pointFArr = new PointF[]{this.mWaterPointTL, this.mWaterPointBL};
            }
        } else if (edge == 2) {
            if (edge2 == 1) {
                pointFArr = new PointF[]{this.mWaterPointBL, this.mWaterPointBR, this.mWaterPointTR};
            } else if (edge2 == 2) {
                pointFArr = new PointF[]{this.mWaterPointBL, this.mWaterPointBR, this.mWaterPointTR, this.mWaterPointTL};
            } else if (edge2 != 3) {
                if (edge2 == 4) {
                    pointFArr = new PointF[]{this.mWaterPointBL, this.mWaterPointBR};
                }
                if (list != null) {
                }
                throw new IllegalStateException();
            } else {
                pointFArr = new PointF[]{this.mWaterPointBL};
            }
        } else if (edge != 3) {
            if (edge == 4) {
                if (edge2 == 1) {
                    pointFArr = new PointF[]{this.mWaterPointTR};
                } else if (edge2 == 2) {
                    pointFArr = new PointF[]{this.mWaterPointTR, this.mWaterPointTL};
                } else if (edge2 == 3) {
                    pointFArr = new PointF[]{this.mWaterPointTR, this.mWaterPointTL, this.mWaterPointBL};
                } else if (edge2 == 4) {
                    pointFArr = new PointF[]{this.mWaterPointTR, this.mWaterPointTL, this.mWaterPointBL, this.mWaterPointBR};
                }
            }
            if (list != null) {
            }
            throw new IllegalStateException();
        } else if (edge2 == 1) {
            pointFArr = new PointF[]{this.mWaterPointBR, this.mWaterPointTR};
        } else if (edge2 == 2) {
            pointFArr = new PointF[]{this.mWaterPointBR, this.mWaterPointTR, this.mWaterPointTL};
        } else if (edge2 != 3) {
            if (edge2 == 4) {
                pointFArr = new PointF[]{this.mWaterPointBR};
            }
            if (list != null) {
            }
            throw new IllegalStateException();
        } else {
            pointFArr = new PointF[]{this.mWaterPointBR, this.mWaterPointTR, this.mWaterPointTL, this.mWaterPointBL};
        }
        list = Arrays.asList(pointFArr);
        if (list != null) {
        }
        throw new IllegalStateException();
    }

    private void drawWater(Canvas canvas) {
        PointF pointF = this.mStartPoint;
        PointF pointF2 = this.mEndPoint;
        boolean isNaN = Float.isNaN(pointF2.x);
        String str = TAG;
        if (isNaN || Float.isInfinite(pointF2.x)) {
            Log.w(str, "endP.x error");
            pointF2.x = (float) getWidth();
        }
        if (Float.isNaN(pointF.x) || Float.isInfinite(pointF.x)) {
            Log.w(str, "startP.x error");
            pointF.x = 0.0f;
        }
        constructWaterPath(pointF, pointF2, constructWaterPoints(pointF, pointF2));
        if (isGradientColorSet()) {
            Paint paint = this.mWaterPaint;
            LinearGradient linearGradient = new LinearGradient(0.0f, Math.max(pointF.y, pointF2.y) * this.mValue, 0.0f, (float) getHeight(), this.mGradientColorStart, this.mGradientColorEnd, TileMode.CLAMP);
            paint.setShader(linearGradient);
        }
        canvas.drawPath(this.mWaterPath, this.mWaterPaint);
    }

    private void followRot() {
        LineEndPoints lineEnd = getLineEnd(this.mWaterData.getValue(), this.mWaterData.getRot());
        this.mRealStartPoint.x = lineEnd.getStartPoint().x;
        this.mRealStartPoint.y = lineEnd.getStartPoint().y;
        this.mRealEndPoint.x = lineEnd.getEndPoint().x;
        this.mRealEndPoint.y = lineEnd.getEndPoint().y;
        Folme.useValue(this.mWaterData).to("edgeRot", Float.valueOf(this.mWaterData.getRot()), this.mEdgeRotAnimConfig);
        for (int i = 0; i < this.mPoints.size(); i++) {
            PointF pointF = (PointF) this.mPoints.get(i);
            float pointPer = getPointPer(i);
            AnimConfig animConfig = (AnimConfig) this.mPointAnimConfigs.get(i);
            Folme.useValue(pointF).to("x", Float.valueOf(valFromPer(pointPer, this.mRealStartPoint.x, this.mRealEndPoint.x)), animConfig);
            Folme.useValue(pointF).to("y", Float.valueOf(valFromPer(pointPer, this.mRealStartPoint.y, this.mRealEndPoint.y)), animConfig);
        }
        LineEndPoints lineEnd2 = getLineEnd(this.mWaterData.getValue(), this.mWaterData.getEdgeRot());
        this.mStartPoint.x = lineEnd2.getStartPoint().x;
        this.mStartPoint.y = lineEnd2.getStartPoint().y;
        this.mEndPoint.x = lineEnd2.getEndPoint().x;
        this.mEndPoint.y = lineEnd2.getEndPoint().y;
    }

    private int getEdge(PointF pointF) {
        if (near(pointF.x, 0.0f)) {
            return 2;
        }
        if (near(pointF.x, (float) getWidth())) {
            return 4;
        }
        int i = 1;
        if (near(pointF.y, 0.0f)) {
            return 1;
        }
        if (near(pointF.y, (float) getHeight())) {
            i = 3;
        }
        return i;
    }

    private LineEndPoints getLineEnd(float f, float f2) {
        PointF linePoint = getLinePoint(f, f2, -1);
        PointF linePoint2 = getLinePoint(f, f2, 1);
        float f3 = f2 % 360.0f;
        if (f3 < 0.0f) {
            f3 += 360.0f;
        }
        if (f3 > 180.0f) {
            linePoint = linePoint2;
        }
        return new LineEndPoints(linePoint, linePoint2);
    }

    private PointF getLineEndFunc(PointF pointF, float f, float f2, float f3) {
        PointF pointF2 = new PointF((float) (((double) pointF.x) + (Math.cos(toRad(f)) * 35.0d)), (float) (((double) pointF.y) + (Math.sin(toRad(f)) * 35.0d)));
        float f4 = pointF2.y;
        float f5 = pointF.y;
        float f6 = f4 - f5;
        float f7 = pointF.x;
        float f8 = pointF2.x;
        float f9 = f7 - f8;
        float f10 = -((f8 * f5) - (f7 * f4));
        return new PointF((f10 - (f3 * f9)) / f6, (f10 - (f6 * f2)) / f9);
    }

    private PointF getLinePoint(float f, float f2, int i) {
        PointF pointF = new PointF();
        double height = (double) ((0.5f - f) * ((float) getHeight()));
        pointF.x = (float) (((double) (((float) getWidth()) / 2.0f)) + (Math.cos(toRad(f2)) * height));
        pointF.y = (float) (((double) (((float) getHeight()) / 2.0f)) + (Math.sin(toRad(f2)) * height));
        PointF pointF2 = new PointF();
        pointF2.x = (((float) getWidth()) / 2.0f) + (((float) (getWidth() * i)) / 2.0f);
        float f3 = f2 - ((float) (i * 90));
        pointF2.y = getLineEndFunc(pointF, f3, pointF2.x, Float.NaN).y;
        if (Math.abs(this.mWaterData.getRot()) != 90.0f) {
            pointF2.y = Math.min(Math.max(pointF2.y, 0.0f), (float) getHeight());
            pointF2.x = getLineEndFunc(pointF, f3, Float.NaN, pointF2.y).x;
        }
        pointF2.x = (float) Math.round(pointF2.x);
        pointF2.y = (float) Math.round(pointF2.y);
        return pointF2;
    }

    private float getPointPer(int i) {
        return (((float) i) * 1.0f) / ((float) (this.mPointNum - 1));
    }

    private float getWaterAlphaByValue(float f) {
        return f == 0.0f ? 0.0f : 1.0f;
    }

    private void handleNewSensorAverageValue(float[] fArr) {
        float f = fArr[0] / 10.0f;
        float f2 = fArr[1] / 10.0f;
        float f3 = fArr[2] / 10.0f;
        float angle = (float) toAngle(-Math.atan2((double) (-f2), (double) (-f)));
        if (angle < 0.0f) {
            angle += 360.0f;
        }
        boolean z = !isSensorNotChangedForAWhile();
        boolean isSensorAngleChanged = isSensorAngleChanged(angle);
        if (z || isSensorAngleChanged) {
            if (isSensorAngleChanged) {
                this.mSensorLastChangedTime = System.currentTimeMillis();
                this.mSensorLastAngle = angle;
            }
            rotToAngle(angle);
            float abs = 1.0f - Math.abs(f3);
            Folme.useValue(this.mWaterData).to("effectPer", Float.valueOf(abs * 2.0f));
            invalidate();
        }
    }

    private void initAfterSizeConfirm() {
        this.mPoints.clear();
        this.mPointAnimConfigs.clear();
        this.mRectF.bottom = (float) getHeight();
        this.mRectF.right = (float) getWidth();
        this.mWaterData.setWaterAlpha(getWaterAlphaByValue(this.mValue));
        Paint paint = this.mWaterPaint;
        paint.setAlpha((int) (((float) paint.getAlpha()) * this.mWaterData.getWaterAlpha()));
        float height = ((float) getHeight()) - (((float) getHeight()) * this.mWaterData.getValue());
        PointF pointF = this.mRealStartPoint;
        pointF.x = 0.0f;
        pointF.y = height;
        this.mRealEndPoint.x = (float) getWidth();
        this.mRealEndPoint.y = height;
        for (int i = 0; i < this.mPointNum; i++) {
            float pointPer = getPointPer(i);
            this.mPoints.add(new PointF(((float) getWidth()) * pointPer, height));
            this.mPointAnimConfigs.add(new AnimConfig().setEase(EaseManager.getStyle(-2, (float) (0.800000011920929d - (Math.sin(((double) pointPer) * 3.141592653589793d) * 0.5d)), 1.0f)));
        }
        PointF pointF2 = this.mStartPoint;
        pointF2.x = 0.0f;
        pointF2.y = height;
        PointF pointF3 = this.mEndPoint;
        pointF3.x = 0.0f;
        pointF3.y = height;
        this.mWaterPointBL.y = (float) getHeight();
        this.mWaterPointBR.x = (float) getWidth();
        this.mWaterPointBR.y = (float) getHeight();
        this.mWaterPointTR.x = (float) getWidth();
    }

    private void initAnimConfig() {
        this.mEdgeRotAnimConfig = new AnimConfig();
        this.mEdgeRotAnimConfig.setEase(EaseManager.getStyle(-2, 1.0f, 0.9f));
        this.mRotAnimConfig = new AnimConfig();
        this.mRotAnimConfig.setEase(EaseManager.getStyle(-2, 0.4f, 0.8f));
        this.mWaterAlphaAnimConfig = new AnimConfig();
        this.mWaterAlphaAnimConfig.setEase(EaseManager.getStyle(-2, 1.0f, 1.0f));
        this.mValueAnimConfig = new AnimConfig();
        this.mValueAnimConfig.setEase(EaseManager.getStyle(-2, 0.9f, 1.0f));
    }

    private void initInConstruct() {
        if (getBackground() == null) {
            setBackgroundResource(R.color.water_box_bg_color);
        }
        this.mWaterData = new WaterData();
        this.mAccValuesForAverage = new float[2][];
        this.mCornerPath = new Path();
        this.mWaterPath = new Path();
        this.mRectF = new RectF();
        this.mPointAnimConfigs = new ArrayList();
        this.mPoints = new ArrayList();
        this.mPreAngle = this.mWaterData.getRot();
        this.mColor = getResources().getColor(R.color.water_box_water_color);
        this.mCornerRadius = getResources().getDimension(R.dimen.waterbox_round_corner_radius);
        this.mWaterPaint = new Paint();
        this.mWaterPaint.setAntiAlias(true);
        this.mWaterPaint.setColor(this.mColor);
        this.mRealStartPoint = new PointF();
        this.mRealEndPoint = new PointF();
        this.mStartPoint = new PointF();
        this.mEndPoint = new PointF();
        this.mWaterPointTL = new PointF(0.0f, 0.0f);
        this.mWaterPointBL = new PointF(0.0f, (float) getHeight());
        this.mWaterPointBR = new PointF((float) getWidth(), (float) getHeight());
        this.mWaterPointTR = new PointF((float) getWidth(), 0.0f);
        this.mRectF = new RectF(0.0f, 0.0f, 0.0f, 0.0f);
        initAnimConfig();
    }

    private boolean isGradientColorSet() {
        return (this.mGradientColorEnd == -1 || this.mGradientColorStart == -1) ? false : true;
    }

    private boolean isSensorAngleChanged(float f) {
        return Math.abs(this.mSensorLastAngle - f) > 9.0f;
    }

    private boolean isSensorNotChangedForAWhile() {
        return System.currentTimeMillis() - this.mSensorLastChangedTime > 2000;
    }

    private boolean near(float f, float f2) {
        return Math.abs(f - f2) < 5.0f;
    }

    private float normalizeValue(float f) {
        float f2 = 0.0f;
        if (f < 0.0f) {
            f = 0.0f;
        }
        if (f > 1.0f) {
            f = 1.0f;
        }
        double d = (double) f;
        if (d < 0.0d || d > 0.01d) {
            f2 = f;
        }
        double d2 = (double) f2;
        if (d2 > 0.01d && d2 < 0.03d) {
            f2 = 0.03f;
        }
        double d3 = (double) f2;
        if (d3 > 0.97d && d3 < 0.99d) {
            f2 = 0.97f;
        }
        double d4 = (double) f2;
        if (d4 < 0.99d || d4 > 1.0d) {
            return f2;
        }
        return 1.0f;
    }

    private void onUnvisible() {
        unregisterSensorListener();
    }

    private void onVisible() {
        this.mSensorLastChangedTime = Long.MAX_VALUE;
        registerSensorListener();
        beginEnterAnim();
    }

    private void registerSensorListener() {
        Sensor sensor = this.mAccelerometer;
        if (sensor != null) {
            this.mSensorManager.registerListener(this, sensor, 2);
            Log.d(TAG, "registerListener");
        }
    }

    private void resetPath() {
        this.mCornerPath.reset();
        Path path = this.mCornerPath;
        RectF rectF = this.mRectF;
        float f = this.mCornerRadius;
        path.addRoundRect(rectF, f, f, Direction.CW);
        this.mCornerPath.close();
    }

    private void rotToAngle(float f) {
        float f2 = f - (f > 270.0f ? 450.0f : 90.0f);
        if (this.mWaterData.getValue() == 1.0f) {
            this.mWaterData.setEffectPer(0.0f);
        }
        float min = Math.min(this.mWaterData.getEffectPer() * 35.0f, Math.max(this.mWaterData.getEffectPer() * -35.0f, f2)) + 90.0f;
        while (min > this.mPreAngle + 180.0f) {
            min -= 360.0f;
        }
        while (min < this.mPreAngle - 180.0f) {
            min += 360.0f;
        }
        Folme.useValue(this.mWaterData).to("rot", Float.valueOf(min), this.mRotAnimConfig);
    }

    private double toAngle(double d) {
        return (d * 180.0d) / 3.141592653589793d;
    }

    private double toRad(float f) {
        return (((double) f) * 3.141592653589793d) / 180.0d;
    }

    private void unregisterSensorListener() {
        if (this.mAccelerometer != null) {
            this.mSensorManager.unregisterListener(this);
            Log.d(TAG, "unregisterListener");
        }
    }

    private float valFromPer(float f, float f2, float f3) {
        return ((1.0f - f) * f2) + (f * f3);
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        int save = canvas.save();
        canvas.clipPath(this.mCornerPath);
        super.dispatchDraw(canvas);
        canvas.restoreToCount(save);
    }

    public void draw(Canvas canvas) {
        int save = canvas.save();
        canvas.clipPath(this.mCornerPath);
        super.draw(canvas);
        canvas.restoreToCount(save);
    }

    public final float getValue() {
        return this.mValue;
    }

    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Object systemService = getContext().getSystemService("sensor");
        if (systemService != null) {
            this.mSensorManager = (SensorManager) systemService;
            this.mAccelerometer = this.mSensorManager.getDefaultSensor(1);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unregisterSensorListener();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        followRot();
        drawWater(canvas);
        if (!isSensorNotChangedForAWhile()) {
            invalidate();
        }
        super.onDraw(canvas);
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent != null) {
            float[] fArr = sensorEvent.values;
            float[][] fArr2 = this.mAccValuesForAverage;
            int i = this.mAccValuesForAverageIndex;
            fArr2[i] = fArr;
            this.mAccValuesForAverageIndex = i + 1;
            if (this.mAccValuesForAverageIndex == 2) {
                this.mAccValuesForAverageIndex = 0;
                float[] fArr3 = new float[3];
                for (int i2 = 0; i2 < 3; i2++) {
                    for (int i3 = 0; i3 < 2; i3++) {
                        fArr3[i2] = fArr3[i2] + fArr2[i3][i2];
                    }
                }
                for (int i4 = 0; i4 < 3; i4++) {
                    fArr3[i4] = fArr3[i4] / 2.0f;
                }
                handleNewSensorAverageValue(fArr3);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        initAfterSizeConfirm();
        resetPath();
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        boolean z = this.mIsVisible;
        if (i == 0) {
            if (!z) {
                this.mIsVisible = true;
                onVisible();
            }
        } else if (z) {
            this.mIsVisible = false;
            onUnvisible();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("onVisibilityChanged:");
        sb.append(i);
        Log.d(TAG, sb.toString());
    }

    public final void setColor(int i) {
        this.mColor = i;
        this.mWaterPaint.setColor(this.mColor);
    }

    public final void setCornerRadius(float f) {
        this.mCornerRadius = f;
        resetPath();
    }

    public void setGradientColor(int i, int i2) {
        this.mGradientColorStart = i;
        this.mGradientColorEnd = i2;
        this.mWaterPaint.setAlpha(255);
    }

    public void setPointNum(int i) {
        if (i < 0) {
            i = 5;
        }
        this.mPointNum = i;
    }

    public final void setValue(float f, boolean z) {
        this.mIsValueSet = true;
        initAnimConfig();
        float normalizeValue = normalizeValue(f);
        this.mValue = normalizeValue;
        if (this.mValue == 0.0f) {
            this.mWaterData.setValue(normalizeValue);
            this.mWaterData.setWaterAlpha(getWaterAlphaByValue(normalizeValue));
            this.mWaterPaint.setAlpha(0);
        } else {
            if (z) {
                Folme.useValue(this.mWaterData).to("value", Float.valueOf(normalizeValue), this.mValueAnimConfig);
                Folme.useValue(this.mWaterData).to("waterAlpha", Float.valueOf(getWaterAlphaByValue(normalizeValue)), this.mWaterAlphaAnimConfig);
            } else {
                this.mWaterData.setValue(normalizeValue);
                this.mWaterData.setWaterAlpha(getWaterAlphaByValue(normalizeValue));
            }
            this.mWaterPaint.setColor(this.mColor);
        }
        invalidate();
    }
}
