package com.android.camera.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.log.Log;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import miui.view.animation.CubicEaseOutInterpolator;

public class VideoTagView implements OnClickListener {
    private static final int DEFAULT_TAG_MARGIN_LEFT = Util.dpToPixel(14.9f);
    private static final int DEFAULT_TAG_MARGIN_RIGHT = Util.dpToPixel(13.1f);
    private static final int SPLIT_LINE_MARGIN = Util.dpToPixel(8.7f);
    private static final String TAG = "VideoTagView";
    private static final int VERTICAL_TAG = 100;
    public static final int VIDEO_TAG_STATE_PAUSE = 2;
    public static final int VIDEO_TAG_STATE_PREPARE = 0;
    public static final int VIDEO_TAG_STATE_RESUME = 3;
    public static final int VIDEO_TAG_STATE_START = 1;
    public static final int VIDEO_TAG_STATE_STOP = 4;
    private boolean isRecordingPause;
    /* access modifiers changed from: private */
    public boolean isShowTagValue;
    private boolean isTagRecording;
    private Context mContext;
    private long mNeedRemoveTime;
    private long mPauseRecordingTime;
    private long mStartRecordingTime;
    private TextView mTagCountValue;
    private FrameLayout mTagFullLayout;
    private View mVideoTag;
    private AnimatorSet mVideoTagExpandAnim;
    private AnimatorSet mVideoTagHideAnim;
    private int mVideoTagNormalSize;
    private int recordingPauseTagCount = 0;
    private StringBuilder srtBuilder = new StringBuilder();
    private int videoTagCount = 0;

    class VideoTagCapsuleEvaluator implements TypeEvaluator {
        private PointF pointA;
        private PointF pointB;

        private VideoTagCapsuleEvaluator() {
            this.pointA = new PointF(0.2f, 1.8f);
            this.pointB = new PointF(0.4f, 1.0f);
        }

        public PointF evaluate(float f, PointF pointF, PointF pointF2) {
            float f2 = 1.0f - f;
            float f3 = pointF.x * f2 * f2 * f2;
            PointF pointF3 = this.pointA;
            float f4 = f3 + (pointF3.x * 3.0f * f * f2 * f2);
            PointF pointF4 = this.pointB;
            return new PointF(f4 + (pointF4.x * 3.0f * f * f * f2) + (pointF2.x * f * f * f), (pointF.y * f2 * f2 * f2) + (pointF3.y * 3.0f * f * f2 * f2) + (pointF4.y * 3.0f * f * f * f2) + (pointF2.y * f * f * f));
        }
    }

    class VideoTagCountEvaluator implements TypeEvaluator {
        private PointF pointA;
        private PointF pointB;

        private VideoTagCountEvaluator() {
            this.pointA = new PointF(0.2f, 1.6f);
            this.pointB = new PointF(0.2f, 1.0f);
        }

        public PointF evaluate(float f, PointF pointF, PointF pointF2) {
            float f2 = 1.0f - f;
            float f3 = pointF.x * f2 * f2 * f2;
            PointF pointF3 = this.pointA;
            float f4 = f3 + (pointF3.x * 3.0f * f * f2 * f2);
            PointF pointF4 = this.pointB;
            return new PointF(f4 + (pointF4.x * 3.0f * f * f * f2) + (pointF2.x * f * f * f), (pointF.y * f2 * f2 * f2) + (pointF3.y * 3.0f * f * f2 * f2) + (pointF4.y * 3.0f * f * f * f2) + (pointF2.y * f * f * f));
        }
    }

    private String getTime(long j) {
        long j2 = (j - this.mStartRecordingTime) - this.mNeedRemoveTime;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss,SSS");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        return simpleDateFormat.format(Long.valueOf(j2));
    }

    private void initVideoTagAnimator() {
        if (this.mVideoTagExpandAnim == null) {
            ValueAnimator ofObject = ValueAnimator.ofObject(new VideoTagCapsuleEvaluator(), new Object[]{new PointF(0.0f, 0.0f), new PointF(1.0f, 1.0f)});
            ofObject.addUpdateListener(new O0000o00(this));
            ValueAnimator ofObject2 = ValueAnimator.ofObject(new VideoTagCountEvaluator(), new Object[]{new PointF(0.0f, 0.0f), new PointF(1.0f, 1.0f)});
            ofObject2.addUpdateListener(new O0000o(this));
            this.mVideoTagExpandAnim = new AnimatorSet();
            this.mVideoTagExpandAnim.playTogether(new Animator[]{ofObject, ofObject2});
            this.mVideoTagExpandAnim.setDuration(600);
        }
        if (this.mVideoTagHideAnim == null) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
            ofFloat.setInterpolator(new CubicEaseOutInterpolator());
            ofFloat.addUpdateListener(new C0419O0000o0O(this));
            ofFloat.addListener(new AnimatorListenerAdapter() {
                public void onAnimationCancel(Animator animator) {
                    VideoTagView.this.isShowTagValue = false;
                    VideoTagView.this.resetTagView();
                }

                public void onAnimationEnd(Animator animator) {
                    VideoTagView.this.isShowTagValue = false;
                    VideoTagView.this.resetTagView();
                }
            });
            ValueAnimator ofFloat2 = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
            ofFloat2.setInterpolator(new CubicEaseOutInterpolator());
            ofFloat2.addUpdateListener(new O0000o0(this));
            this.mVideoTagHideAnim = new AnimatorSet();
            this.mVideoTagHideAnim.setDuration(200);
            this.mVideoTagHideAnim.setStartDelay(1000);
            this.mVideoTagHideAnim.playTogether(new Animator[]{ofFloat, ofFloat2});
        }
    }

    /* access modifiers changed from: private */
    public void resetTagView() {
        int i;
        FrameLayout frameLayout;
        if (this.isTagRecording) {
            frameLayout = this.mTagFullLayout;
            i = 0;
        } else {
            frameLayout = this.mTagFullLayout;
            i = 8;
        }
        frameLayout.setVisibility(i);
        this.mTagFullLayout.setEnabled(true);
    }

    private void setTagLayoutWidth(int i) {
        LayoutParams layoutParams = (LayoutParams) this.mVideoTag.getLayoutParams();
        layoutParams.width = i;
        this.mVideoTag.setLayoutParams(layoutParams);
        if (!this.isShowTagValue) {
            resetTagView();
        } else if (this.mTagCountValue.getVisibility() != 0) {
            this.mTagCountValue.setVisibility(0);
        }
    }

    private void setViewMargin(View view) {
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        marginLayoutParams.height = Display.getTopBarHeight();
        marginLayoutParams.topMargin = Display.getTopMargin();
        marginLayoutParams.setMarginEnd(Display.getEndMargin() + view.getResources().getDimensionPixelSize(R.dimen.video_tag_margin_end));
        view.setLayoutParams(marginLayoutParams);
    }

    private void updateTagValueView(TextView textView) {
        StringBuilder sb = new StringBuilder();
        if (this.videoTagCount < 10) {
            sb.append("0");
        }
        sb.append(this.videoTagCount);
        textView.setText(sb.toString());
        View view = this.mVideoTag;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.mVideoTag.getContext().getString(R.string.video_tag_flag_title));
        sb2.append(sb.toString());
        view.setContentDescription(sb2.toString());
        if (Util.isAccessible()) {
            this.mVideoTag.postDelayed(new C0420O0000o0o(this), 100);
        }
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mTagCountValue.getLayoutParams();
        int measureText = (int) textView.getPaint().measureText(textView.getText().toString());
        marginLayoutParams.width = measureText;
        marginLayoutParams.setMarginEnd(-measureText);
        this.mTagCountValue.setLayoutParams(marginLayoutParams);
        initVideoTagAnimator();
        AnimatorSet animatorSet = this.mVideoTagExpandAnim;
        if (animatorSet != null) {
            animatorSet.start();
        }
        AnimatorSet animatorSet2 = this.mVideoTagHideAnim;
        if (animatorSet2 != null) {
            animatorSet2.start();
        }
    }

    private void updateTagView() {
        resetTagView();
    }

    public /* synthetic */ void O00000oO(ValueAnimator valueAnimator) {
        float f = ((PointF) valueAnimator.getAnimatedValue()).y;
        int i = this.mVideoTagNormalSize;
        setTagLayoutWidth((int) ((f * ((float) i)) + ((float) i)));
    }

    public /* synthetic */ void O00000oo(ValueAnimator valueAnimator) {
        this.mTagCountValue.setTranslationX(((float) (Util.isLayoutRTL(this.mContext) ? 1 : -1)) * ((PointF) valueAnimator.getAnimatedValue()).y * ((float) this.mVideoTagNormalSize));
    }

    public /* synthetic */ void O0000O0o(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        int i = this.mVideoTagNormalSize;
        setTagLayoutWidth((int) ((floatValue * ((float) i)) + ((float) i)));
    }

    public /* synthetic */ void O0000OOo(ValueAnimator valueAnimator) {
        this.mTagCountValue.setTranslationX(((float) (Util.isLayoutRTL(this.mContext) ? 1 : -1)) * ((Float) valueAnimator.getAnimatedValue()).floatValue() * ((float) this.mVideoTagNormalSize));
    }

    public /* synthetic */ void O00oooO() {
        this.mVideoTag.sendAccessibilityEvent(32768);
    }

    public String getVideoTagContent() {
        return this.srtBuilder.toString();
    }

    public void init(View view, Context context) {
        this.mContext = context;
        this.mVideoTagNormalSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.video_tag_normal_size);
        this.mTagFullLayout = (FrameLayout) view.findViewById(R.id.video_tag_layout);
        this.mVideoTag = view.findViewById(R.id.video_tag);
        this.mTagCountValue = (TextView) this.mTagFullLayout.findViewById(R.id.video_tag_value);
        this.mTagFullLayout.setOnClickListener(this);
        setViewMargin(this.mTagFullLayout);
    }

    public void onClick(View view) {
        long j;
        this.isShowTagValue = true;
        this.mTagFullLayout.setEnabled(false);
        if (this.isRecordingPause) {
            int i = this.recordingPauseTagCount;
            if (i == 0) {
                this.recordingPauseTagCount = i + 1;
                j = this.mPauseRecordingTime;
                String time = getTime(j);
                this.videoTagCount++;
                StringBuilder sb = this.srtBuilder;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(this.videoTagCount);
                String str = "\n";
                sb2.append(str);
                sb.append(sb2.toString());
                this.srtBuilder.append(String.format("%s\n", new Object[]{time}));
                this.srtBuilder.append(str);
                StringBuilder sb3 = new StringBuilder();
                sb3.append("video tag number: ");
                sb3.append(this.videoTagCount);
                Log.u(TAG, sb3.toString());
            }
        } else {
            j = System.currentTimeMillis();
            String time2 = getTime(j);
            this.videoTagCount++;
            StringBuilder sb4 = this.srtBuilder;
            StringBuilder sb22 = new StringBuilder();
            sb22.append(this.videoTagCount);
            String str2 = "\n";
            sb22.append(str2);
            sb4.append(sb22.toString());
            this.srtBuilder.append(String.format("%s\n", new Object[]{time2}));
            this.srtBuilder.append(str2);
            StringBuilder sb32 = new StringBuilder();
            sb32.append("video tag number: ");
            sb32.append(this.videoTagCount);
            Log.u(TAG, sb32.toString());
        }
        updateTagValueView(this.mTagCountValue);
    }

    public void pause() {
        Log.d(TAG, "handleTagRecordingPause: ");
        this.isRecordingPause = true;
        this.recordingPauseTagCount = 0;
        this.mPauseRecordingTime = System.currentTimeMillis();
    }

    public void prepare() {
        Log.d(TAG, "handleTagRecordingPrepare: ");
        this.videoTagCount = 0;
        this.mNeedRemoveTime = 0;
        this.isShowTagValue = true;
        this.isRecordingPause = false;
        this.isTagRecording = true;
        updateTagView();
        if (this.srtBuilder.length() != 0) {
            StringBuilder sb = this.srtBuilder;
            sb.delete(0, sb.length());
        }
        View view = this.mVideoTag;
        view.setContentDescription(view.getContext().getString(R.string.video_tag_flag_title));
    }

    public void resume() {
        Log.d(TAG, "handleTagRecordingResume: ");
        this.isRecordingPause = false;
        this.mNeedRemoveTime += System.currentTimeMillis() - this.mPauseRecordingTime;
    }

    public void start() {
        Log.d(TAG, "handleTagRecordingStart: ");
        this.mStartRecordingTime = System.currentTimeMillis();
    }

    public void stop() {
        Log.d(TAG, "handleTagRecordingStop: ");
        this.isRecordingPause = false;
        this.videoTagCount = 0;
        this.isTagRecording = false;
        this.mTagFullLayout.setVisibility(8);
    }
}
