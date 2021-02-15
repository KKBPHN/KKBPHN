package com.android.camera.preferences;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnInfoListener;
import android.view.View;
import android.widget.VideoView;

/* compiled from: lambda */
public final /* synthetic */ class O000000o implements OnInfoListener {
    private final /* synthetic */ VideoView O0OOoO0;
    private final /* synthetic */ View O0OOoOO;

    public /* synthetic */ O000000o(VideoView videoView, View view) {
        this.O0OOoO0 = videoView;
        this.O0OOoOO = view;
    }

    public final boolean onInfo(MediaPlayer mediaPlayer, int i, int i2) {
        return EffectComparisonPreference.O000000o(this.O0OOoO0, this.O0OOoOO, mediaPlayer, i, i2);
    }
}
