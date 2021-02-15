package com.android.camera.preferences;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.VideoView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.camera.CameraSettings;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.log.Log;

public class MoreModeStylePreference extends Preference implements OnClickListener, OnCheckedChangeListener {
    private static final String TAG = "MoreModeStylePreference";
    private View mPopupCover;
    private RadioButton mPopupRadioBtn;
    private VideoView mPopupVideoView;
    private View mTabCover;
    private RadioButton mTabRadioBtn;
    private VideoView mTabVideoView;
    private int mValue;

    public MoreModeStylePreference(Context context) {
        super(context);
    }

    public MoreModeStylePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MoreModeStylePreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    static /* synthetic */ void O000000o(VideoView videoView, View view, MediaPlayer mediaPlayer) {
        mediaPlayer.setLooping(true);
        mediaPlayer.setOnInfoListener(new O00000o0(videoView, view));
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
        videoView.setOnPreparedListener(new O00000o(videoView, view));
    }

    private void preparedVideos() {
        int[] moreModePrefVideo = Display.getMoreModePrefVideo(Util.isNightUiMode(getContext()));
        preparedVideoView(this.mTabVideoView, this.mTabCover, moreModePrefVideo[0]);
        preparedVideoView(this.mPopupVideoView, this.mPopupCover, moreModePrefVideo[1]);
    }

    public int getValue() {
        return this.mValue;
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        VideoView videoView;
        super.onBindViewHolder(preferenceViewHolder);
        this.mTabVideoView = (VideoView) preferenceViewHolder.findViewById(R.id.open_type_tab_video);
        this.mPopupVideoView = (VideoView) preferenceViewHolder.findViewById(R.id.open_type_popup_video);
        this.mTabRadioBtn = (RadioButton) preferenceViewHolder.findViewById(R.id.open_type_tab_radio);
        this.mPopupRadioBtn = (RadioButton) preferenceViewHolder.findViewById(R.id.open_type_popup_radio);
        this.mTabCover = preferenceViewHolder.findViewById(R.id.tab_cover);
        this.mPopupCover = preferenceViewHolder.findViewById(R.id.popup_cover);
        this.mTabRadioBtn.setOnCheckedChangeListener(this);
        this.mPopupRadioBtn.setOnCheckedChangeListener(this);
        preferenceViewHolder.findViewById(R.id.open_type_popup).setOnClickListener(this);
        preferenceViewHolder.findViewById(R.id.open_type_tab).setOnClickListener(this);
        preparedVideos();
        this.mValue = CameraSettings.getMoreModeStyle();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onBindViewHolder ");
        sb.append(this.mValue);
        Log.d(str, sb.toString());
        int i = this.mValue;
        if (i == 0) {
            this.mTabRadioBtn.setChecked(true);
            this.mPopupRadioBtn.setChecked(false);
            this.mTabVideoView.start();
            videoView = this.mPopupVideoView;
        } else if (1 == i) {
            this.mTabRadioBtn.setChecked(false);
            this.mPopupRadioBtn.setChecked(true);
            this.mPopupVideoView.start();
            videoView = this.mTabVideoView;
        } else {
            return;
        }
        videoView.pause();
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        int id = compoundButton.getId();
        if (id == R.id.open_type_popup_radio) {
            if (z) {
                Log.u(TAG, "onCheckedChanged open_type_popup_radio");
            }
            this.mTabRadioBtn.setChecked(!z);
            this.mPopupVideoView.start();
            this.mTabVideoView.pause();
            this.mTabVideoView.seekTo(0);
            this.mValue = 1;
        } else if (id == R.id.open_type_tab_radio) {
            if (z) {
                Log.u(TAG, "onCheckedChanged open_type_tab_radio");
            }
            this.mPopupRadioBtn.setChecked(!z);
            this.mPopupVideoView.pause();
            this.mPopupVideoView.seekTo(0);
            this.mTabVideoView.start();
            this.mValue = 0;
        }
        persistString(String.valueOf(this.mValue));
        CameraSettings.setMoreModeStyle(this.mValue);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.open_type_popup) {
            this.mTabRadioBtn.setChecked(false);
            this.mPopupRadioBtn.setChecked(true);
            this.mPopupVideoView.start();
            this.mTabVideoView.pause();
            this.mTabVideoView.seekTo(0);
            this.mValue = 1;
        } else if (id == R.id.open_type_tab) {
            this.mTabRadioBtn.setChecked(true);
            this.mPopupRadioBtn.setChecked(false);
            this.mPopupVideoView.pause();
            this.mPopupVideoView.seekTo(0);
            this.mTabVideoView.start();
            this.mValue = 0;
        }
        persistString(String.valueOf(this.mValue));
        CameraSettings.setMoreModeStyle(this.mValue);
    }

    public void onPause() {
        Log.d(TAG, "onPause");
        VideoView videoView = this.mTabVideoView;
        if (videoView != null) {
            videoView.stopPlayback();
        }
        View view = this.mTabCover;
        if (view != null) {
            view.setVisibility(0);
        }
        VideoView videoView2 = this.mPopupVideoView;
        if (videoView2 != null) {
            videoView2.stopPlayback();
        }
        View view2 = this.mPopupCover;
        if (view2 != null) {
            view2.setVisibility(0);
        }
    }

    public void onResume() {
        VideoView videoView;
        Log.d(TAG, "onResume");
        if (this.mTabVideoView != null && this.mPopupVideoView != null) {
            preparedVideos();
            int i = this.mValue;
            if (i == 0) {
                videoView = this.mTabVideoView;
            } else if (1 == i) {
                videoView = this.mPopupVideoView;
            } else {
                return;
            }
            videoView.start();
        }
    }
}
