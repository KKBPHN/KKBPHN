package com.android.camera.ui;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import com.android.camera.ActivityBase;
import com.android.camera.CameraScreenNail;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.Display;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.log.Log;
import com.android.camera.module.ModuleManager;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.watermark.WaterMarkData;
import com.android.camera2.CameraHardwareFace;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import miui.view.animation.CubicEaseOutInterpolator;
import tv.danmaku.ijk.media.player.IjkMediaCodecInfo;

public class FaceView extends FrameView {
    private static final int AUTO_HIDE_TIME_DELAY = 2000;
    private static final int GENDER_FEMALE_RECT_COLOR = -1152383;
    private static final int GENDER_MALE_RECT_COLOR = -9455628;
    private static final int LATEST_FACE_NUM = 6;
    private static final int MAGIC_MIRROR_RECT_COLOR = -18377;
    private static final int MAX_FACE_MOVE_DISTANCE = 120;
    private static final int MAX_FACE_WIDTH_DISTANCE = 90;
    public static final float MAX_GENDER_FEMALE = 0.4f;
    public static final float MIN_ANALYZE_PROB = 0.5f;
    private static final int MIN_FACE_WIDTH = 670;
    public static final float MIN_GENDER_MALE = 0.6f;
    private static final int MSG_ANNOUNCE_FACE_TIP = 4;
    private static final int MSG_AUTOMATIC_HIDE_FACE = 1;
    private static final int MSG_CANCEL_FACE_HIDE = 2;
    private static final int MSG_SET_FACE_VISIBLE = 3;
    private static final int SHOW_TYPE_GENDER_AGE = 1;
    private static final int SHOW_TYPE_NORMAL = 0;
    private static final int SHOW_TYPE_NULL = -1;
    private static final int SHOW_TYPE_SCORE = 4;
    private static final int SHOW_TYPE_SCORE_WINNER = 2;
    private static final int STATE_FORCE_HIDE = 4;
    private static final int STATE_HIDE = 2;
    private static final int STATE_NORMAL = 1;
    private static final int STATE_WAIT_FOR_HIDE = 3;
    private static final String TAG = "FaceView";
    private Configuration configuration;
    private Rect mActiveArraySize;
    private int mAgeFemaleHonPadding;
    private int mAgeMaleHonPadding;
    private int mAgeVerPadding;
    private Drawable mBeautyScoreIc;
    private Matrix mCamera2TranslateMatrix = new Matrix();
    private CameraScreenNail mCameraScreenNail;
    private Matrix mCanvasMatrix = new Matrix();
    private int mCorrection;
    private List mCurrentWaterMarkDataInfos;
    private CameraHardwareFace[] mDrawingFaces;
    private Paint mEffectRectPaint;
    private String mFaceAgeFormat;
    private Paint mFaceInfoNumberPaint;
    private Paint mFaceInfoTextPaint;
    private int mFacePopupBottom;
    private String mFacePosition;
    private Animator mFaceRectHideAnimator;
    private String mFaceScoreInfoFormat;
    /* access modifiers changed from: private */
    public String mFaceTipContentDescription;
    private CameraHardwareFace[] mFaces;
    private Drawable mFemaleAgeInfoPop;
    private int mGap;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            int i = message.what;
            if (i != 1) {
                if (i == 2) {
                    FaceView.this.cancelHideAnimator();
                } else if (i == 3) {
                    FaceView.this.setFaceRectVisible(0, 0);
                } else if (i == 4) {
                    FaceView faceView = FaceView.this;
                    faceView.announceForAccessibility(faceView.mFaceTipContentDescription);
                }
            } else if (FaceView.this.mRectState == 3) {
                FaceView.this.mRectState = 2;
                FaceView.this.setFaceRectVisible(8, IjkMediaCodecInfo.RANK_LAST_CHANCE);
            }
        }
    };
    private boolean mIsCameraFaceDetectionAutoHidden;
    private boolean mIsUpdateFaceInfos = true;
    private int mLatestFaceIndex = -1;
    private CameraHardwareFace[] mLatestFaces = new CameraHardwareFace[6];
    private boolean mLightingOn;
    private Drawable mMagicMirrorInfoPop;
    private Drawable mMaleAgeInfoPop;
    private Matrix mMatrix = new Matrix();
    private boolean mMirror;
    /* access modifiers changed from: private */
    public Paint mNormalRectPaint;
    private int mOrientation;
    private int mPopBottomMargin;
    private RectF mRect = new RectF();
    /* access modifiers changed from: private */
    public int mRectState = 1;
    private int mScoreHonPadding;
    private int mScoreVerPadding;
    private Drawable mSexFemaleIc;
    private Drawable mSexMaleIc;
    private boolean mShowGenderAndAge;
    private boolean mShowMagicMirror;
    private boolean mSkipDraw;
    private Pattern mSplitFaceInfoPattern;
    private List mWaterInfos;
    private int mWinnerIndex = -1;
    private float mZoomValue;

    @Retention(RetentionPolicy.SOURCE)
    @interface FaceViewRectState {
    }

    @Retention(RetentionPolicy.SOURCE)
    @interface PopType {
    }

    public FaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (C0124O00000oO.isMTKPlatform()) {
            setLayerType(2, null);
        }
        this.mEffectRectPaint = new Paint();
        this.mEffectRectPaint.setAntiAlias(true);
        this.mEffectRectPaint.setStrokeWidth((float) getResources().getDimensionPixelSize(R.dimen.face_rect_width));
        this.mEffectRectPaint.setStyle(Style.STROKE);
        this.mNormalRectPaint = new Paint();
        this.mNormalRectPaint.setAntiAlias(true);
        this.mNormalRectPaint.setColor(-1);
        this.mNormalRectPaint.setStyle(Style.STROKE);
        this.mNormalRectPaint.setStrokeWidth((float) Util.dpToPixel(1.0f));
        this.mCameraScreenNail = ((ActivityBase) context).getCameraScreenNail();
    }

    private String calcFacePos(Rect rect, float f, float f2) {
        int i = rect.left;
        int i2 = 2;
        int i3 = f < ((float) i) ? 1 : (f < ((float) i) || f > ((float) rect.right)) ? f > ((float) rect.right) ? 3 : 0 : 2;
        int i4 = rect.top;
        if (f2 < ((float) i4)) {
            i2 = 1;
        } else if (f2 <= ((float) i4) || f2 > ((float) rect.bottom)) {
            i2 = f2 > ((float) rect.bottom) ? 3 : 0;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(i3);
        sb.append("_");
        sb.append(i2);
        return sb.toString();
    }

    /* access modifiers changed from: private */
    public void cancelHideAnimator() {
        Animator animator = this.mFaceRectHideAnimator;
        if (animator != null && animator.isRunning()) {
            this.mFaceRectHideAnimator.cancel();
        }
    }

    private void clearAttemptHideFaceRect() {
        this.mRectState = 1;
        this.mHandler.removeMessages(1);
        this.mHandler.sendEmptyMessage(2);
    }

    private int determineWatermarkType() {
        if (CameraSettings.isMagicMirrorOn()) {
            return 1;
        }
        return CameraSettings.showGenderAge() ? 2 : 0;
    }

    private void drawFaceInfoText(Canvas canvas, String str, int i, int i2) {
        Paint paint;
        float f;
        float f2;
        float f3;
        Matcher matcher = this.mSplitFaceInfoPattern.matcher(str);
        while (matcher.find()) {
            String group = matcher.group();
            if (group.matches("\\d+\\.?\\d*")) {
                f3 = this.mFaceInfoNumberPaint.measureText(group);
                f = ((float) i) + (f3 / 2.0f);
                f2 = (float) i2;
                paint = this.mFaceInfoNumberPaint;
            } else {
                f3 = this.mFaceInfoTextPaint.measureText(group);
                f = ((float) i) + (f3 / 2.0f);
                f2 = (float) i2;
                paint = this.mFaceInfoTextPaint;
            }
            canvas.drawText(group, f, f2, paint);
            i = (int) (((float) i) + f3);
        }
    }

    private void drawFacePopInfo(Canvas canvas, RectF rectF, Drawable drawable, Drawable drawable2, String str, int i, int i2, int i3, int i4) {
        Canvas canvas2 = canvas;
        RectF rectF2 = rectF;
        Drawable drawable3 = drawable;
        Drawable drawable4 = drawable2;
        String str2 = str;
        int i5 = i;
        Matcher matcher = this.mSplitFaceInfoPattern.matcher(str2);
        float f = 0.0f;
        while (matcher.find()) {
            String group = matcher.group();
            f += (group.matches("\\d+\\.?\\d*") ? this.mFaceInfoNumberPaint : this.mFaceInfoTextPaint).measureText(group);
        }
        float f2 = (float) i2;
        int intrinsicWidth = ((int) ((((float) ((drawable.getIntrinsicWidth() + i5) + this.mGap)) + f) + ((float) i5))) / 2;
        Rect rect = new Rect(((int) rectF.centerX()) - intrinsicWidth, ((((int) rectF2.top) - ((int) ((3.6f * f2) + ((float) drawable.getIntrinsicHeight())))) - i4) - i3, ((int) rectF.centerX()) + intrinsicWidth, ((int) rectF2.top) - i4);
        if (drawable4 != null) {
            drawable4.setBounds(rect);
            drawable4.draw(canvas2);
        }
        Rect rect2 = new Rect();
        int i6 = rect.left;
        float f3 = f2 * 1.8f;
        rect2.set(i6 + i5, (int) ((((float) rect.top) + f3) - ((float) this.mCorrection)), i6 + i5 + drawable.getIntrinsicWidth(), (int) (((((float) rect.top) + f3) - ((float) this.mCorrection)) + ((float) drawable.getIntrinsicHeight())));
        drawable3.setBounds(rect2);
        drawable3.draw(canvas2);
        if (f != 0.0f) {
            FontMetricsInt fontMetricsInt = this.mFaceInfoTextPaint.getFontMetricsInt();
            drawFaceInfoText(canvas2, str2, rect2.right + this.mGap, (((rect2.bottom + rect2.top) - fontMetricsInt.bottom) - fontMetricsInt.top) / 2);
        }
    }

    private void drawFaceRect(Canvas canvas, RectF rectF, int i, CameraHardwareFace cameraHardwareFace) {
        float f;
        float f2;
        Paint paint;
        Paint paint2;
        int i2;
        if (i != -1) {
            if (i != 0) {
                boolean z = true;
                if (i != 1) {
                    if ((i == 2 || i == 4) && cameraHardwareFace.beautyscore > 0.0f) {
                        paint2 = this.mEffectRectPaint;
                        i2 = MAGIC_MIRROR_RECT_COLOR;
                    } else {
                        return;
                    }
                } else if (isValidAGInfo(cameraHardwareFace)) {
                    if (cameraHardwareFace.gender >= 0.4f) {
                        z = false;
                    }
                    paint2 = this.mEffectRectPaint;
                    i2 = z ? GENDER_FEMALE_RECT_COLOR : GENDER_MALE_RECT_COLOR;
                } else {
                    return;
                }
                paint2.setColor(i2);
                f2 = rectF.width() * 0.015f;
                f = rectF.height() * 0.015f;
                paint = this.mEffectRectPaint;
            } else {
                f2 = rectF.width() * 0.015f;
                f = rectF.height() * 0.015f;
                paint = this.mNormalRectPaint;
            }
            canvas.drawRoundRect(rectF, f2, f, paint);
        }
    }

    private String getAgeInfo(CameraHardwareFace cameraHardwareFace) {
        int i = (int) cameraHardwareFace.ageMale;
        if (cameraHardwareFace.gender < 0.4f) {
            i = (int) cameraHardwareFace.ageFemale;
        }
        return String.format(this.configuration.locale, this.mFaceAgeFormat, new Object[]{Integer.valueOf(i)});
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0106  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void getFacePos(CameraHardwareFace cameraHardwareFace, int i) {
        String str;
        float f;
        float f2;
        MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
        if (mainContentProtocol != null) {
            View referenceLine = mainContentProtocol.getReferenceLine();
            if (referenceLine != null) {
                Rect displayRect = Util.getDisplayRect();
                int width = referenceLine.getWidth() / 3;
                int height = referenceLine.getHeight() / 3;
                transToViewRect(cameraHardwareFace.rect, this.mRect);
                int i2 = 0;
                if (ModuleManager.isSquareModule()) {
                    i2 = Display.getWindowWidth() / 6;
                }
                Rect rect = new Rect();
                int i3 = this.mOrientation;
                if (i3 == 0) {
                    RectF rectF = this.mRect;
                    f = (rectF.left + rectF.right) / 2.0f;
                    f2 = (rectF.top + rectF.bottom) / 2.0f;
                    rect.set(width, i2 + height, width * 2, i2 + (height * 2));
                } else {
                    if (i3 == 90) {
                        float windowHeight = (float) Display.getWindowHeight();
                        RectF rectF2 = this.mRect;
                        float f3 = windowHeight + ((rectF2.left + rectF2.right) / 2.0f);
                        float f4 = (rectF2.top + rectF2.bottom) / 2.0f;
                        rect.set((Display.getWindowHeight() + i2) - (displayRect.top + (height * 2)), width, (i2 + Display.getWindowHeight()) - (displayRect.top + height), width * 2);
                        str = calcFacePos(rect, f3, f4);
                    } else if (i3 == 180) {
                        float windowWidth = (float) Display.getWindowWidth();
                        RectF rectF3 = this.mRect;
                        f = windowWidth + ((rectF3.left + rectF3.right) / 2.0f);
                        float windowHeight2 = (float) Display.getWindowHeight();
                        RectF rectF4 = this.mRect;
                        f2 = windowHeight2 + ((rectF4.top + rectF4.bottom) / 2.0f);
                        rect.set(width, Display.getWindowHeight() - ((displayRect.top + i2) + (height * 2)), width * 2, Display.getWindowHeight() - ((i2 + displayRect.top) + height));
                    } else if (i3 == 270) {
                        RectF rectF5 = this.mRect;
                        float f5 = (rectF5.left + rectF5.right) / 2.0f;
                        float windowWidth2 = (float) Display.getWindowWidth();
                        RectF rectF6 = this.mRect;
                        float f6 = windowWidth2 + ((rectF6.top + rectF6.bottom) / 2.0f);
                        rect.set(i2 + height, width, i2 + (height * 2), width * 2);
                        str = calcFacePos(rect, f5, f6);
                    } else {
                        str = "";
                    }
                    if (!str.equals(this.mFacePosition)) {
                        setContentDescription(i, str);
                    }
                }
                str = calcFacePos(rect, f, f2);
                if (!str.equals(this.mFacePosition)) {
                }
            }
        }
    }

    private int getPopShowType(CameraHardwareFace[] cameraHardwareFaceArr) {
        int i;
        if (cameraHardwareFaceArr == null || cameraHardwareFaceArr.length <= 0) {
            return 0;
        }
        if (!this.mShowMagicMirror) {
            return this.mShowGenderAndAge ? 1 : 0;
        }
        this.mWinnerIndex = -1;
        int i2 = 0;
        for (int i3 = 0; i3 < this.mFaces.length; i3++) {
            float f = cameraHardwareFaceArr[i3].beautyscore;
            if (f > 0.0f) {
                i2++;
                int i4 = this.mWinnerIndex;
                if (i4 == -1 || f > cameraHardwareFaceArr[i4].beautyscore) {
                    this.mWinnerIndex = i3;
                }
            }
        }
        if (i2 > 1) {
            i = 2;
        } else if (i2 <= 0) {
            return -1;
        } else {
            i = 4;
        }
        return i;
    }

    private String getScoreInfo(CameraHardwareFace cameraHardwareFace) {
        return String.format(this.configuration.locale, this.mFaceScoreInfoFormat, new Object[]{Float.valueOf(cameraHardwareFace.beautyscore / 10.0f)});
    }

    private void initFaceInfoStyle() {
        if (this.mFaceInfoTextPaint == null) {
            this.mSplitFaceInfoPattern = Pattern.compile("(\\D+)|(\\d+\\.?\\d*)");
            Resources resources = getResources();
            this.configuration = resources.getConfiguration();
            this.mFaceAgeFormat = resources.getString(R.string.face_age_info);
            this.mFaceScoreInfoFormat = resources.getString(R.string.face_score_info);
            this.mMaleAgeInfoPop = resources.getDrawable(R.drawable.male_age_info_pop);
            this.mFemaleAgeInfoPop = resources.getDrawable(R.drawable.female_age_info_pop);
            this.mSexMaleIc = resources.getDrawable(R.drawable.ic_sex_male);
            this.mSexFemaleIc = resources.getDrawable(R.drawable.ic_sex_female);
            this.mFaceInfoTextPaint = new Paint();
            this.mFaceInfoTextPaint.setAntiAlias(true);
            this.mFaceInfoTextPaint.setColor(-1);
            float dimension = resources.getDimension(R.dimen.face_info_magic_textSize);
            this.mFaceInfoTextPaint.setTextSize(dimension);
            this.mFaceInfoTextPaint.setTextAlign(Align.CENTER);
            this.mFaceInfoTextPaint.setFakeBoldText(true);
            this.mFaceInfoNumberPaint = new Paint(this.mFaceInfoTextPaint);
            if (this.configuration.locale.equals(Locale.SIMPLIFIED_CHINESE) || this.configuration.locale.equals(Locale.TRADITIONAL_CHINESE)) {
                this.mFaceInfoNumberPaint.setFakeBoldText(false);
                this.mFaceInfoNumberPaint.setTextSize(dimension * 1.16f);
            }
            this.mMagicMirrorInfoPop = resources.getDrawable(R.drawable.magic_mirror_info_pop);
            this.mBeautyScoreIc = resources.getDrawable(R.drawable.ic_beauty_score);
            this.mAgeVerPadding = resources.getDimensionPixelSize(R.dimen.face_info_ver_padding);
            this.mGap = resources.getDimensionPixelSize(R.dimen.face_info_text_left_dis);
            this.mCorrection = resources.getDimensionPixelOffset(R.dimen.face_info_correction);
            this.mPopBottomMargin = resources.getDimensionPixelSize(R.dimen.face_pop_bottom_margin);
            this.mScoreHonPadding = resources.getDimensionPixelSize(R.dimen.face_info_score_hon_padding);
            this.mScoreVerPadding = resources.getDimensionPixelSize(R.dimen.face_info_score_ver_padding);
            this.mAgeMaleHonPadding = resources.getDimensionPixelSize(R.dimen.face_info_male_hon_padding);
            this.mAgeFemaleHonPadding = resources.getDimensionPixelSize(R.dimen.face_info_female_hon_padding);
            this.mFacePopupBottom = (int) (((double) this.mMagicMirrorInfoPop.getIntrinsicHeight()) * 0.12d);
        }
    }

    private boolean isValidAGInfo(CameraHardwareFace cameraHardwareFace) {
        if (0.5f <= cameraHardwareFace.prob) {
            float f = cameraHardwareFace.gender;
            if (f != 0.0f && (f <= 0.4f || 0.6f <= f)) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002f, code lost:
        if (r7 == 270) goto L_0x0054;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0043, code lost:
        if (r7 == 180) goto L_0x0045;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0052, code lost:
        if (r7 == 270) goto L_0x0054;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0054, code lost:
        r2 = (float) r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0055, code lost:
        r5.postTranslate(r4, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0023, code lost:
        if (r7 == 180) goto L_0x0045;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void performMatrix(Matrix matrix, boolean z, int i, float f, float f2, int i2, int i3) {
        float f3;
        float f4;
        float f5 = 0.0f;
        if (z) {
            matrix.setScale(-1.0f, 1.0f);
            matrix.postRotate((float) (-i));
            if (i == 90 || i == 270) {
                matrix.postScale(f2, f);
                f3 = i == 90 ? (float) (-i3) : 0.0f;
            } else {
                matrix.postScale(f, f2);
                f4 = i == 180 ? (float) (-i2) : 0.0f;
            }
        } else {
            matrix.setRotate((float) i);
            if (i == 90 || i == 270) {
                matrix.postScale(f2, f);
                f3 = i == 90 ? (float) i3 : 0.0f;
            } else {
                matrix.postScale(f, f2);
                f4 = i == 180 ? (float) i2 : 0.0f;
            }
        }
        f5 = (float) i3;
        matrix.postTranslate(f4, f5);
    }

    private void prepareMatrix() {
        this.mCamera2TranslateMatrix.reset();
        this.mMatrix.reset();
        this.mCanvasMatrix.reset();
        Util.scaleCamera2Matrix(this.mCamera2TranslateMatrix, this.mActiveArraySize, this.mZoomValue);
        Util.prepareMatrix(this.mMatrix, this.mMirror, this.mCameraDisplayOrientation, this.mCameraScreenNail.getRenderWidth(), this.mCameraScreenNail.getRenderHeight(), getWidth() / 2, getHeight() / 2, this.mActiveArraySize.width(), this.mActiveArraySize.height());
        if (!this.mLightingOn) {
            this.mMatrix.postRotate((float) this.mOrientation);
            this.mCanvasMatrix.postRotate((float) this.mOrientation);
        }
    }

    private void setContentDescription(int i, String str) {
        this.mFacePosition = str;
        if (i > 0) {
            this.mHandler.removeMessages(4);
            int i2 = 0;
            this.mFaceTipContentDescription = getResources().getQuantityString(R.plurals.accessibility_focus_face_detect, i, new Object[]{Integer.valueOf(i)});
            if (str != null && i == 1) {
                char c = 65535;
                int hashCode = str.hashCode();
                switch (hashCode) {
                    case 50083:
                        if (str.equals("1_1")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 50084:
                        if (str.equals("1_2")) {
                            c = 3;
                            break;
                        }
                        break;
                    case 50085:
                        if (str.equals("1_3")) {
                            c = 6;
                            break;
                        }
                        break;
                    default:
                        switch (hashCode) {
                            case 51044:
                                if (str.equals("2_1")) {
                                    c = 1;
                                    break;
                                }
                                break;
                            case 51045:
                                if (str.equals("2_2")) {
                                    c = 4;
                                    break;
                                }
                                break;
                            case 51046:
                                if (str.equals("2_3")) {
                                    c = 7;
                                    break;
                                }
                                break;
                            default:
                                switch (hashCode) {
                                    case 52005:
                                        if (str.equals("3_1")) {
                                            c = 2;
                                            break;
                                        }
                                        break;
                                    case 52006:
                                        if (str.equals("3_2")) {
                                            c = 5;
                                            break;
                                        }
                                        break;
                                    case 52007:
                                        if (str.equals("3_3")) {
                                            c = 8;
                                            break;
                                        }
                                        break;
                                }
                        }
                }
                switch (c) {
                    case 0:
                        i2 = R.string.accessibility_focus_face_pos_left_top;
                        break;
                    case 1:
                        i2 = R.string.accessibility_focus_face_pos_top;
                        break;
                    case 2:
                        i2 = R.string.accessibility_focus_face_pos_right_top;
                        break;
                    case 3:
                        i2 = R.string.accessibility_focus_face_pos_left;
                        break;
                    case 4:
                        i2 = R.string.accessibility_focus_face_pos_mid;
                        break;
                    case 5:
                        i2 = R.string.accessibility_focus_face_pos_right;
                        break;
                    case 6:
                        i2 = R.string.accessibility_focus_face_pos_left_bottom;
                        break;
                    case 7:
                        i2 = R.string.accessibility_focus_face_pos_bottom;
                        break;
                    case 8:
                        i2 = R.string.accessibility_focus_face_pos_right_bottom;
                        break;
                }
                if (i2 > 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(this.mFaceTipContentDescription);
                    sb.append(",");
                    sb.append(getResources().getString(i2));
                    this.mFaceTipContentDescription = sb.toString();
                }
            }
            this.mHandler.sendEmptyMessageDelayed(4, 500);
        }
    }

    private void setCurrentFaceInfos(RectF rectF, String str, int i, int i2, int i3) {
        if (!this.mIsUpdateFaceInfos) {
            Log.d(TAG, "setCurrentFaceInfos@2: updateInfo=false");
            return;
        }
        if (this.mCurrentWaterMarkDataInfos == null) {
            this.mCurrentWaterMarkDataInfos = new ArrayList();
        }
        WaterMarkData waterMarkData = new WaterMarkData();
        waterMarkData.setFaceRectF(rectF);
        waterMarkData.setInfo(str);
        waterMarkData.setFaceViewWidth(i);
        waterMarkData.setFaceViewHeight(i2);
        waterMarkData.setOrientation(i3);
        waterMarkData.setWatermarkType(determineWatermarkType());
        List list = this.mCurrentWaterMarkDataInfos;
        if (list != null) {
            list.add(waterMarkData);
        }
    }

    private void setCurrentFaceInfos(RectF rectF, boolean z, String str, int i, int i2, int i3) {
        if (!this.mIsUpdateFaceInfos) {
            Log.d(TAG, "setCurrentFaceInfos@1: updateInfo=false");
            return;
        }
        if (this.mCurrentWaterMarkDataInfos == null) {
            this.mCurrentWaterMarkDataInfos = new ArrayList();
        }
        WaterMarkData waterMarkData = new WaterMarkData();
        waterMarkData.setFaceRectF(rectF);
        waterMarkData.setFemale(z);
        waterMarkData.setInfo(str);
        waterMarkData.setFaceViewWidth(i);
        waterMarkData.setFaceViewHeight(i2);
        waterMarkData.setOrientation(i3);
        waterMarkData.setWatermarkType(determineWatermarkType());
        this.mCurrentWaterMarkDataInfos.add(waterMarkData);
    }

    private void setToVisible() {
        if (getVisibility() != 0) {
            setVisibility(0);
        }
    }

    private void showNormalFaceRectImmediately() {
        this.mRectState = 1;
        this.mHandler.removeMessages(1);
        this.mHandler.sendEmptyMessage(3);
    }

    private void updateFaceInfos() {
        if (!this.mIsUpdateFaceInfos) {
            Log.d(TAG, "updateFaceInfos: false");
            return;
        }
        if (this.mWaterInfos == null) {
            this.mWaterInfos = new ArrayList();
        }
        List list = this.mCurrentWaterMarkDataInfos;
        if (list != null && !list.isEmpty()) {
            if (!this.mWaterInfos.isEmpty()) {
                this.mWaterInfos.clear();
            }
            this.mWaterInfos.addAll(this.mCurrentWaterMarkDataInfos);
        }
        List list2 = this.mCurrentWaterMarkDataInfos;
        if (list2 != null) {
            list2.clear();
        }
    }

    private void updateLatestFaces() {
        int i = this.mLatestFaceIndex;
        int i2 = i >= 5 ? 0 : i + 1;
        CameraHardwareFace[] cameraHardwareFaceArr = this.mFaces;
        if (cameraHardwareFaceArr == null || cameraHardwareFaceArr.length < 1) {
            this.mLatestFaces[i2] = null;
        } else {
            CameraHardwareFace cameraHardwareFace = cameraHardwareFaceArr[0];
            for (int i3 = 1; i3 < cameraHardwareFaceArr.length; i3++) {
                int i4 = cameraHardwareFaceArr[i3].rect.right - cameraHardwareFaceArr[i3].rect.left;
                Rect rect = cameraHardwareFace.rect;
                if (i4 > rect.right - rect.left) {
                    cameraHardwareFace = cameraHardwareFaceArr[i3];
                }
            }
            this.mLatestFaces[i2] = cameraHardwareFace;
        }
        this.mLatestFaceIndex = i2;
    }

    public void attemptHideFaceRect(long j) {
        this.mRectState = 3;
        this.mHandler.removeMessages(1);
        this.mHandler.sendEmptyMessageDelayed(1, j);
    }

    public void clear() {
        this.mFaces = null;
        this.mRectState = 1;
        clearPreviousFaces();
        postInvalidate();
    }

    public void clearFaceFlags() {
        this.mShowGenderAndAge = false;
        this.mShowMagicMirror = false;
    }

    public void clearPreviousFaces() {
        this.mLatestFaceIndex = -1;
        int i = 0;
        while (true) {
            CameraHardwareFace[] cameraHardwareFaceArr = this.mLatestFaces;
            if (i < cameraHardwareFaceArr.length) {
                cameraHardwareFaceArr[i] = null;
                i++;
            } else {
                return;
            }
        }
    }

    public boolean faceExisted() {
        int i = this.mLatestFaceIndex;
        if (i < 0) {
            return false;
        }
        int i2 = i;
        for (int i3 = 0; i3 < 3; i3++) {
            if (i2 < 0) {
                i2 += this.mLatestFaces.length;
            }
            if (this.mLatestFaces[i2] != null) {
                return true;
            }
            i2--;
        }
        return false;
    }

    public boolean faceExists() {
        CameraHardwareFace[] cameraHardwareFaceArr = this.mFaces;
        return cameraHardwareFaceArr != null && cameraHardwareFaceArr.length > 0;
    }

    public void forceHideRect() {
        if (this.mRectState != 4) {
            this.mRectState = 4;
            this.mHandler.removeMessages(1);
            if (this.mNormalRectPaint.getAlpha() > 0) {
                setFaceRectVisible(8, 200);
            }
        }
    }

    public List getFaceWaterMarkInfos() {
        this.mDrawingFaces = getFaces();
        CameraHardwareFace[] cameraHardwareFaceArr = this.mDrawingFaces;
        if (cameraHardwareFaceArr == null || cameraHardwareFaceArr.length == 0 || this.mCameraScreenNail == null || this.mPause) {
            return null;
        }
        int popShowType = getPopShowType(cameraHardwareFaceArr);
        int i = 0;
        while (true) {
            CameraHardwareFace[] cameraHardwareFaceArr2 = this.mDrawingFaces;
            if (i < cameraHardwareFaceArr2.length) {
                this.mRect.set(cameraHardwareFaceArr2[i].rect);
                transToViewRect(this.mDrawingFaces[i].rect, this.mRect);
                if (popShowType == 1 && isValidAGInfo(this.mDrawingFaces[i])) {
                    initFaceInfoStyle();
                    boolean z = this.mDrawingFaces[i].gender < 0.4f;
                    String ageInfo = getAgeInfo(this.mDrawingFaces[i]);
                    RectF rectF = this.mRect;
                    setCurrentFaceInfos(new RectF(rectF.left, rectF.top, rectF.right, rectF.bottom), z, ageInfo, getWidth(), getHeight(), this.mOrientation);
                }
                if ((popShowType == 2 || popShowType == 4) && this.mDrawingFaces[i].beautyscore > 0.0f) {
                    RectF rectF2 = this.mRect;
                    setCurrentFaceInfos(new RectF(rectF2.left, rectF2.top, rectF2.right, rectF2.bottom), getScoreInfo(this.mDrawingFaces[i]), getWidth(), getHeight(), this.mOrientation);
                }
                i++;
            } else {
                updateFaceInfos();
                return this.mWaterInfos;
            }
        }
    }

    public CameraHardwareFace[] getFaces() {
        return this.mFaces;
    }

    public RectF getFocusRect() {
        RectF rectF = new RectF();
        CameraScreenNail cameraScreenNail = ((ActivityBase) getContext()).getCameraScreenNail();
        if (cameraScreenNail != null) {
            int i = this.mLatestFaceIndex;
            if (i >= 0 && i < 6) {
                this.mCamera2TranslateMatrix.reset();
                this.mMatrix.reset();
                Util.scaleCamera2Matrix(this.mCamera2TranslateMatrix, this.mActiveArraySize, this.mZoomValue);
                Util.prepareMatrix(this.mMatrix, this.mMirror, this.mCameraDisplayOrientation, cameraScreenNail.getRenderWidth(), cameraScreenNail.getRenderHeight(), getWidth() / 2, getHeight() / 2, this.mActiveArraySize.width(), this.mActiveArraySize.height());
                rectF.set(this.mLatestFaces[this.mLatestFaceIndex].rect);
                this.mMatrix.postRotate((float) this.mOrientation);
                this.mCamera2TranslateMatrix.mapRect(rectF);
                this.mMatrix.mapRect(rectF);
                return rectF;
            }
        }
        return null;
    }

    public RectF[] getViewRects(CameraSize cameraSize) {
        CameraHardwareFace[] cameraHardwareFaceArr = this.mFaces;
        if (cameraHardwareFaceArr == null || cameraHardwareFaceArr.length <= 0) {
            return null;
        }
        Matrix matrix = new Matrix();
        RectF[] rectFArr = new RectF[cameraHardwareFaceArr.length];
        int renderWidth = this.mCameraScreenNail.getRenderWidth();
        int renderHeight = this.mCameraScreenNail.getRenderHeight();
        int height = (DataRepository.dataItemRunning().getUiStyle() == 0 && DataRepository.dataItemGlobal().getCurrentMode() == 165) ? cameraSize.getHeight() : cameraSize.getWidth();
        int height2 = cameraSize.getHeight();
        if (this.mLightingOn && this.mMirror) {
            height *= 2;
            height2 *= 2;
        }
        int i = height2;
        int i2 = height;
        performMatrix(matrix, this.mMirror, this.mOrientation, ((float) i) / ((float) renderWidth), ((float) i2) / ((float) renderHeight), i, i2);
        for (int i3 = 0; i3 < cameraHardwareFaceArr.length; i3++) {
            rectFArr[i3] = new RectF();
            rectFArr[i3].set(cameraHardwareFaceArr[i3].rect);
            this.mCamera2TranslateMatrix.mapRect(rectFArr[i3]);
            this.mMatrix.mapRect(rectFArr[i3]);
            matrix.mapRect(rectFArr[i3]);
        }
        return rectFArr;
    }

    public boolean isFaceStable() {
        CameraHardwareFace[] cameraHardwareFaceArr;
        boolean z = false;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        for (CameraHardwareFace cameraHardwareFace : this.mLatestFaces) {
            if (cameraHardwareFace == null) {
                i++;
                if (i >= 3) {
                    return false;
                }
            } else {
                Rect rect = cameraHardwareFace.rect;
                int i6 = rect.right;
                int i7 = rect.left;
                i2 += i6 - i7;
                int i8 = rect.bottom;
                int i9 = rect.top;
                i3 += i8 - i9;
                i4 += i7;
                i5 += i9;
            }
        }
        int length = this.mLatestFaces.length - i;
        int i10 = i2 / length;
        int i11 = i3 / length;
        int i12 = i4 / length;
        int i13 = i5 / length;
        int i14 = i10 / 3;
        if (i14 <= 90) {
            i14 = 90;
        }
        CameraHardwareFace[] cameraHardwareFaceArr2 = this.mLatestFaces;
        int length2 = cameraHardwareFaceArr2.length;
        for (int i15 = 0; i15 < length2; i15++) {
            CameraHardwareFace cameraHardwareFace2 = cameraHardwareFaceArr2[i15];
            if (cameraHardwareFace2 != null) {
                Rect rect2 = cameraHardwareFace2.rect;
                if (Math.abs((rect2.right - rect2.left) - i10) > i14 || Math.abs(cameraHardwareFace2.rect.left - i12) > 120 || Math.abs(cameraHardwareFace2.rect.top - i13) > 120) {
                    return false;
                }
            }
        }
        if (i10 > MIN_FACE_WIDTH || i11 > MIN_FACE_WIDTH) {
            z = true;
        }
        this.mIsBigEnoughRect = z;
        return true;
    }

    public boolean isGroupCapture() {
        CameraHardwareFace[] cameraHardwareFaceArr = this.mFaces;
        return cameraHardwareFaceArr != null && cameraHardwareFaceArr.length > 1;
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mHandler.removeCallbacksAndMessages(null);
        cancelHideAnimator();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        String ageInfo;
        RectF rectF;
        Drawable drawable;
        Drawable drawable2;
        int i;
        CameraHardwareFace cameraHardwareFace;
        Canvas canvas2 = canvas;
        if (!this.mSkipDraw) {
            this.mDrawingFaces = getFaces();
            CameraHardwareFace[] cameraHardwareFaceArr = this.mDrawingFaces;
            if (!(cameraHardwareFaceArr == null || cameraHardwareFaceArr.length == 0 || this.mCameraScreenNail == null || this.mPause)) {
                canvas.save();
                if (!this.mLightingOn) {
                    canvas2.rotate((float) (-this.mOrientation));
                }
                int popShowType = getPopShowType(this.mDrawingFaces);
                int i2 = 0;
                while (true) {
                    CameraHardwareFace[] cameraHardwareFaceArr2 = this.mDrawingFaces;
                    if (i2 >= cameraHardwareFaceArr2.length) {
                        break;
                    }
                    this.mRect.set(cameraHardwareFaceArr2[i2].rect);
                    transToViewRect(this.mDrawingFaces[i2].rect, this.mRect);
                    drawFaceRect(canvas2, this.mRect, popShowType, this.mDrawingFaces[i2]);
                    boolean z = true;
                    if (popShowType != 1) {
                        if (popShowType != 2) {
                            if (popShowType == 4 && this.mDrawingFaces[i2].beautyscore > 0.0f) {
                                initFaceInfoStyle();
                                rectF = this.mRect;
                                drawable = this.mBeautyScoreIc;
                                drawable2 = this.mMagicMirrorInfoPop;
                                cameraHardwareFace = this.mDrawingFaces[i2];
                            }
                            i2++;
                        } else if (this.mDrawingFaces[i2].beautyscore <= 0.0f) {
                            i2++;
                        } else {
                            initFaceInfoStyle();
                            rectF = this.mRect;
                            drawable = this.mBeautyScoreIc;
                            drawable2 = this.mMagicMirrorInfoPop;
                            cameraHardwareFace = this.mDrawingFaces[i2];
                        }
                        ageInfo = getScoreInfo(cameraHardwareFace);
                        i = this.mAgeFemaleHonPadding;
                    } else if (isValidAGInfo(this.mDrawingFaces[i2])) {
                        initFaceInfoStyle();
                        if (this.mDrawingFaces[i2].gender >= 0.4f) {
                            z = false;
                        }
                        ageInfo = getAgeInfo(this.mDrawingFaces[i2]);
                        rectF = this.mRect;
                        drawable = z ? this.mSexFemaleIc : this.mSexMaleIc;
                        drawable2 = z ? this.mFemaleAgeInfoPop : this.mMaleAgeInfoPop;
                        i = z ? this.mAgeFemaleHonPadding : this.mAgeMaleHonPadding;
                    } else {
                        i2++;
                    }
                    drawFacePopInfo(canvas, rectF, drawable, drawable2, ageInfo, i, this.mAgeVerPadding, this.mFacePopupBottom, this.mPopBottomMargin);
                    i2++;
                }
                canvas.restore();
            }
        }
    }

    public void pause() {
        super.pause();
        clearPreviousFaces();
    }

    public void reShowFaceRect() {
        if (this.mRectState != 1) {
            showNormalFaceRectImmediately();
        }
    }

    public void resume() {
        super.resume();
        this.mIsCameraFaceDetectionAutoHidden = CameraSettings.isCameraFaceDetectionAutoHidden();
    }

    public void setCameraDisplayOrientation(int i) {
        this.mCameraDisplayOrientation = i;
        StringBuilder sb = new StringBuilder();
        sb.append("mCameraDisplayOrientation=");
        sb.append(i);
        Log.v(TAG, sb.toString());
    }

    public void setFaceRectVisible(int i, int i2) {
        cancelHideAnimator();
        if (i2 == 0) {
            this.mNormalRectPaint.setAlpha(i == 0 ? 255 : 0);
            return;
        }
        this.mFaceRectHideAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mFaceRectHideAnimator.setInterpolator(new CubicEaseOutInterpolator() {
            public float getInterpolation(float f) {
                float interpolation = super.getInterpolation(f);
                FaceView.this.mNormalRectPaint.setAlpha((int) ((1.0f - interpolation) * 255.0f));
                FaceView.this.invalidate();
                return interpolation;
            }
        });
        this.mFaceRectHideAnimator.setDuration((long) i2);
        this.mFaceRectHideAnimator.start();
    }

    public boolean setFaces(CameraHardwareFace[] cameraHardwareFaceArr, Rect rect, Rect rect2) {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append("Num of faces = ");
        sb.append(cameraHardwareFaceArr == null ? 0 : cameraHardwareFaceArr.length);
        String sb2 = sb.toString();
        String str2 = TAG;
        Log.c(str2, sb2);
        if (this.mPause) {
            return false;
        }
        int length = cameraHardwareFaceArr != null ? cameraHardwareFaceArr.length : 0;
        CameraHardwareFace[] cameraHardwareFaceArr2 = this.mFaces;
        int length2 = cameraHardwareFaceArr2 != null ? cameraHardwareFaceArr2.length : 0;
        boolean z = length != length2;
        if (Util.isAccessible()) {
            if (length == 1) {
                getFacePos(cameraHardwareFaceArr[0], length);
            } else if (length == 0) {
                this.mHandler.removeMessages(4);
            } else if (z) {
                setContentDescription(length, "");
            }
        }
        if (length == 0 && length2 == 0) {
            return true;
        }
        this.mFaces = cameraHardwareFaceArr;
        this.mActiveArraySize = rect;
        this.mZoomValue = HybridZoomingSystem.toZoomRatio(rect, rect2);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("setFaces: activeArraySize=");
        sb3.append(rect);
        sb3.append(" cropRegion=");
        sb3.append(rect2);
        sb3.append(" mZoomValue=");
        sb3.append(this.mZoomValue);
        sb3.append(" face=");
        CameraHardwareFace[] cameraHardwareFaceArr3 = this.mFaces;
        if (cameraHardwareFaceArr3 == null || cameraHardwareFaceArr3.length == 0) {
            str = "null";
        } else {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(this.mFaces[0].rect.width());
            sb4.append("x");
            sb4.append(this.mFaces[0].rect.height());
            str = sb4.toString();
        }
        sb3.append(str);
        Log.d(str2, sb3.toString());
        updateLatestFaces();
        prepareMatrix();
        postInvalidate();
        if (this.mRectState != 4 && !this.mShowGenderAndAge && !this.mShowMagicMirror) {
            if (z && length > 0) {
                showNormalFaceRectImmediately();
            }
            if (this.mIsCameraFaceDetectionAutoHidden) {
                if (isFaceStable()) {
                    int i = this.mRectState;
                    if (!(i == 2 || i == 3)) {
                        attemptHideFaceRect(2000);
                    }
                } else if (this.mRectState != 1) {
                    clearAttemptHideFaceRect();
                }
            }
        }
        return true;
    }

    public void setLightingOn(boolean z) {
        this.mLightingOn = z;
        StringBuilder sb = new StringBuilder();
        sb.append("mLightingOn=");
        sb.append(this.mLightingOn);
        Log.v(TAG, sb.toString());
    }

    public void setMirror(boolean z) {
        this.mMirror = z;
        StringBuilder sb = new StringBuilder();
        sb.append("mMirror=");
        sb.append(z);
        Log.v(TAG, sb.toString());
    }

    public void setOrientation(int i, boolean z) {
        this.mOrientation = i;
        if (!this.mPause && faceExists() && !this.mSkipDraw) {
            invalidate();
        }
    }

    public void setShowGenderAndAge(boolean z) {
        boolean z2 = this.mShowGenderAndAge && !z;
        this.mShowGenderAndAge = z;
        if (z2) {
            showNormalFaceRectImmediately();
        }
        if (z) {
            setShowMagicMirror(false);
        }
    }

    public void setShowMagicMirror(boolean z) {
        boolean z2 = this.mShowMagicMirror && !z;
        this.mShowMagicMirror = z;
        if (z2) {
            showNormalFaceRectImmediately();
        }
        if (z) {
            setShowGenderAndAge(false);
        }
    }

    public void setShutterStatus(boolean z) {
        if (this.mIsUpdateFaceInfos != z) {
            this.mIsUpdateFaceInfos = z;
            if (!z) {
                List list = this.mCurrentWaterMarkDataInfos;
                if (list != null && !list.isEmpty()) {
                    this.mCurrentWaterMarkDataInfos.clear();
                }
                List list2 = this.mWaterInfos;
                if (list2 != null && !list2.isEmpty()) {
                    this.mWaterInfos.clear();
                }
                StringBuilder sb = new StringBuilder();
                sb.append("setShutterStatus: updateInfo=");
                sb.append(this.mIsUpdateFaceInfos);
                Log.d(TAG, sb.toString());
            }
        }
    }

    public void setSkipDraw(boolean z) {
        this.mSkipDraw = z;
    }

    public void showFail() {
        setToVisible();
        invalidate();
    }

    public void showStart() {
        setToVisible();
        invalidate();
    }

    public void showSuccess() {
        setToVisible();
        invalidate();
    }

    public void transToViewRect(Rect rect, RectF rectF) {
        rectF.set(rect);
        this.mCamera2TranslateMatrix.mapRect(rectF);
        this.mMatrix.mapRect(rectF);
    }
}
