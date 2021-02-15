package com.android.camera.dualvideo.remote;

import android.view.Surface;
import com.android.camera.dualvideo.render.RenderUtil;
import com.android.camera.log.Log;
import java.io.IOException;
import java.util.Locale;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class RemoteService {
    private static final String TAG = "RemoteService";
    private IjkMediaPlayer ijkMediaPlayer;
    private boolean mIsStreaming = false;

    public boolean isStreaming() {
        return this.mIsStreaming;
    }

    public void startStreaming(Surface surface) {
        if (!this.mIsStreaming) {
            Log.d(TAG, "startStreaming");
            String format = String.format(Locale.US, "rtsp://192.168.43.1:8086?h264=50000-30-%d-%d", new Object[]{Integer.valueOf(RenderUtil.REMOTE_SIZE.getWidth()), Integer.valueOf(RenderUtil.REMOTE_SIZE.getHeight())});
            try {
                this.ijkMediaPlayer = new IjkMediaPlayer();
                this.ijkMediaPlayer.setSurface(surface);
                this.ijkMediaPlayer.setDataSource(format);
                this.ijkMediaPlayer.prepareAsync();
                this.ijkMediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.mIsStreaming = true;
        }
    }

    public void stopStreaming() {
        Log.d(TAG, "stopStreaming");
        this.mIsStreaming = false;
        IjkMediaPlayer ijkMediaPlayer2 = this.ijkMediaPlayer;
        if (ijkMediaPlayer2 != null) {
            ijkMediaPlayer2.stop();
            this.ijkMediaPlayer.release();
        }
    }
}
