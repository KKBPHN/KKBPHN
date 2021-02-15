package com.android.camera.preferences;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.view.View;
import android.widget.VideoView;

/* compiled from: lambda */
public final /* synthetic */ class O00000Oo implements OnPreparedListener {
    private final /* synthetic */ VideoView O0OOoO0;
    private final /* synthetic */ View O0OOoOO;

    public /* synthetic */ O00000Oo(VideoView videoView, View view) {
        this.O0OOoO0 = videoView;
        this.O0OOoOO = view;
    }

    public final void onPrepared(MediaPlayer mediaPlayer) {
        EffectComparisonPreference.O000000o(this.O0OOoO0, this.O0OOoOO, mediaPlayer);
    }
}
