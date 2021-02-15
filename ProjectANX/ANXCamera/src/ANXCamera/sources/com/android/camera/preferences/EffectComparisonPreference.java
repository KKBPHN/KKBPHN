package com.android.camera.preferences;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.cardview.widget.CardView;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceViewHolder;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.log.Log;
import com.android.camera.statistic.MistatsConstants.VideoAttr;
import com.android.camera.statistic.MistatsWrapper;
import java.util.HashMap;

public class EffectComparisonPreference extends PreferenceCategory implements OnClickListener {
    private static final String TAG = "EffectComparisonPreference";
    private boolean isNsPauseOrStart = false;
    private boolean isOsPauseOrStart = false;
    private CardView mCardViewNs;
    private CardView mCardViewOs;
    private View mEffectComparisonNsCover;
    private View mEffectComparisonNsPlay;
    private View mEffectComparisonOsCover;
    private View mEffectComparisonOsPlay;
    private VideoView mNsVideoView;
    private VideoView mOsVideoView;
    private TextView mTextViewNs;
    private TextView mTextViewOs;

    public EffectComparisonPreference(Context context) {
        super(context);
    }

    public EffectComparisonPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public EffectComparisonPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    static /* synthetic */ void O000000o(VideoView videoView, View view, MediaPlayer mediaPlayer) {
        mediaPlayer.setLooping(true);
        mediaPlayer.setOnInfoListener(new O000000o(videoView, view));
    }

    static /* synthetic */ boolean O000000o(VideoView videoView, View view, MediaPlayer mediaPlayer, int i, int i2) {
        if (i == 3) {
            videoView.setAlpha(1.0f);
            view.setVisibility(8);
        }
        return true;
    }

    private void preparedVideoView(VideoView videoView, View view, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("android.resource://");
        sb.append(getContext().getPackageName());
        sb.append("/");
        sb.append(i);
        videoView.setVideoURI(Uri.parse(sb.toString()));
        videoView.setOnPreparedListener(new O00000Oo(videoView, view));
    }

    private void preparedVideos() {
        boolean isLocaleChinese = Util.isLocaleChinese();
        preparedVideoView(this.mOsVideoView, this.mEffectComparisonOsPlay, R.raw.effect_comparison_noise_en);
        preparedVideoView(this.mNsVideoView, this.mEffectComparisonNsPlay, R.raw.effect_comparison_silence_en);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.mTextViewOs = (TextView) preferenceViewHolder.findViewById(R.id.effect_comparison_text_os);
        this.mTextViewOs.setSelected(true);
        this.mTextViewNs = (TextView) preferenceViewHolder.findViewById(R.id.effect_comparison_text_ns);
        this.mTextViewNs.setSelected(true);
        this.mOsVideoView = (VideoView) preferenceViewHolder.findViewById(R.id.open_os_video);
        this.mNsVideoView = (VideoView) preferenceViewHolder.findViewById(R.id.open_ns_video);
        this.mEffectComparisonOsCover = preferenceViewHolder.findViewById(R.id.effect_comparison_os_cover);
        this.mEffectComparisonNsCover = preferenceViewHolder.findViewById(R.id.effect_comparison_ns_cover);
        this.mEffectComparisonOsPlay = preferenceViewHolder.findViewById(R.id.effect_comparison_os_play);
        this.mEffectComparisonNsPlay = preferenceViewHolder.findViewById(R.id.effect_comparison_ns_play);
        this.mCardViewOs = (CardView) preferenceViewHolder.findViewById(R.id.card_os);
        this.mCardViewNs = (CardView) preferenceViewHolder.findViewById(R.id.card_ns);
        this.mCardViewOs.setOnClickListener(this);
        this.mCardViewNs.setOnClickListener(this);
        FolmeUtils.touchItemScale(this.mCardViewOs);
        FolmeUtils.touchItemScale(this.mCardViewNs);
        preparedVideos();
    }

    public void onClick(View view) {
        String str;
        HashMap hashMap;
        int id = view.getId();
        String str2 = VideoAttr.KEY_VIDEO_COMMON_CLICK;
        Integer valueOf = Integer.valueOf(1);
        switch (id) {
            case R.id.card_ns /*2131296403*/:
                if (!this.isNsPauseOrStart) {
                    this.mOsVideoView.pause();
                    this.mOsVideoView.seekTo(0);
                    this.mNsVideoView.start();
                    this.mEffectComparisonNsCover.setVisibility(8);
                    this.mEffectComparisonNsPlay.setVisibility(8);
                    this.mEffectComparisonOsCover.setVisibility(0);
                    this.mEffectComparisonOsPlay.setVisibility(0);
                    this.isNsPauseOrStart = true;
                    this.isOsPauseOrStart = false;
                    hashMap = new HashMap();
                    str = VideoAttr.PARAM_AI_NOISE_REDUCTION_NR;
                    break;
                } else {
                    this.mNsVideoView.pause();
                    this.mNsVideoView.seekTo(0);
                    this.mEffectComparisonOsCover.setVisibility(0);
                    this.mEffectComparisonNsCover.setVisibility(0);
                    this.mEffectComparisonNsPlay.setVisibility(0);
                    this.isNsPauseOrStart = false;
                    return;
                }
            case R.id.card_os /*2131296404*/:
                if (!this.isOsPauseOrStart) {
                    this.mNsVideoView.pause();
                    this.mNsVideoView.seekTo(0);
                    this.mOsVideoView.start();
                    this.mEffectComparisonOsCover.setVisibility(8);
                    this.mEffectComparisonOsPlay.setVisibility(8);
                    this.mEffectComparisonNsCover.setVisibility(0);
                    this.mEffectComparisonNsPlay.setVisibility(0);
                    this.isOsPauseOrStart = true;
                    this.isNsPauseOrStart = false;
                    hashMap = new HashMap();
                    str = VideoAttr.PARAM_AI_NOISE_REDUCTION_OS;
                    break;
                } else {
                    this.mOsVideoView.pause();
                    this.mOsVideoView.seekTo(0);
                    this.mEffectComparisonOsCover.setVisibility(0);
                    this.mEffectComparisonNsCover.setVisibility(0);
                    this.mEffectComparisonOsPlay.setVisibility(0);
                    this.isOsPauseOrStart = false;
                    return;
                }
            default:
                return;
        }
        hashMap.put(str, valueOf);
        MistatsWrapper.mistatEvent(str2, hashMap);
    }

    public void onPause() {
        Log.d(TAG, "onPause");
        VideoView videoView = this.mOsVideoView;
        if (videoView != null) {
            videoView.stopPlayback();
        }
        View view = this.mEffectComparisonOsPlay;
        if (view != null) {
            view.setVisibility(0);
        }
        VideoView videoView2 = this.mNsVideoView;
        if (videoView2 != null) {
            videoView2.stopPlayback();
        }
        View view2 = this.mEffectComparisonNsPlay;
        if (view2 != null) {
            view2.setVisibility(0);
        }
    }

    public void onResume() {
        Log.d(TAG, "onResume");
        if (this.mOsVideoView != null && this.mNsVideoView != null) {
            preparedVideos();
            this.mEffectComparisonOsCover.setVisibility(0);
            this.mEffectComparisonNsCover.setVisibility(0);
        }
    }
}
