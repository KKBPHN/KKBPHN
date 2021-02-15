package com.android.camera.fragment.clone;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.AssetFileDescriptor;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.android.camera.videoplayer.manager.VideoPlayerManager;
import miui.view.animation.CubicEaseOutInterpolator;

public class GuideAssetVideoItem extends AssetVideoItem {
    private String mContentDescription;
    private boolean showFooter;
    private String tip1;
    private String title1;
    private String title2;

    public GuideAssetVideoItem(AssetFileDescriptor assetFileDescriptor, VideoPlayerManager videoPlayerManager, int i, String str, String str2, String str3, boolean z) {
        super(assetFileDescriptor, videoPlayerManager, i);
        this.title1 = str;
        this.title2 = str2;
        this.tip1 = str3;
        this.showFooter = z;
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        String str4 = ", ";
        sb.append(str4);
        sb.append(str2);
        sb.append(str4);
        sb.append(str3);
        this.mContentDescription = sb.toString();
    }

    private void setText(TextView textView, String str) {
        if (!TextUtils.isEmpty(str)) {
            textView.setVisibility(0);
            textView.setText(str);
            return;
        }
        textView.setVisibility(8);
    }

    private void showEnterAnimator(VideoViewHolder videoViewHolder) {
        String str = "translationY";
        ObjectAnimator duration = ObjectAnimator.ofFloat(videoViewHolder.mTextureViewContainer, str, new float[]{200.0f, 0.0f}).setDuration(500);
        duration.setInterpolator(new CubicEaseOutInterpolator());
        String str2 = "alpha";
        ObjectAnimator duration2 = ObjectAnimator.ofFloat(videoViewHolder.mTextureViewContainer, str2, new float[]{0.0f, 1.0f}).setDuration(500);
        duration2.setInterpolator(new CubicEaseOutInterpolator());
        ObjectAnimator duration3 = ObjectAnimator.ofFloat(videoViewHolder.mTipsContainer, str, new float[]{200.0f, 0.0f}).setDuration(500);
        duration3.setInterpolator(new CubicEaseOutInterpolator());
        ObjectAnimator duration4 = ObjectAnimator.ofFloat(videoViewHolder.mTipsContainer, str2, new float[]{0.0f, 1.0f}).setDuration(500);
        duration4.setInterpolator(new CubicEaseOutInterpolator());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(new Animator[]{duration, duration2, duration3, duration4});
        animatorSet.play(duration).with(duration2).after(100).with(duration3).with(duration4);
        animatorSet.start();
    }

    public String getContentDescription() {
        return this.mContentDescription;
    }

    public void setActive(View view, int i) {
        super.setActive(view, i);
        ((VideoViewHolder) view.getTag()).mTextViewTitle2.setSelected(true);
    }

    public void update(int i, VideoViewHolder videoViewHolder, VideoPlayerManager videoPlayerManager) {
        int i2;
        View view;
        super.update(i, videoViewHolder, videoPlayerManager);
        videoViewHolder.mCover.setImageResource(this.mCoverResource);
        setText(videoViewHolder.mTextViewTitle1, this.title1);
        setText(videoViewHolder.mTextViewTitle2, this.title2);
        ((LayoutParams) videoViewHolder.mTextViewTip1.getLayoutParams()).width = -2;
        setText(videoViewHolder.mTextViewTip1, this.tip1);
        showEnterAnimator(videoViewHolder);
        if (this.showFooter) {
            view = videoViewHolder.mFooter;
            i2 = 0;
        } else {
            view = videoViewHolder.mFooter;
            i2 = 8;
        }
        view.setVisibility(i2);
    }
}
