package O00000Oo.O000000o.O000000o;

import android.media.MediaPlayer;
import java.io.IOException;

public class O000000o {
    private MediaPlayer mMediaPlayer = new MediaPlayer();
    private String mPath;

    public O000000o(String str) {
        this.mPath = str;
    }

    public void destroy() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
    }

    public boolean isPlaying() {
        return this.mMediaPlayer.isPlaying();
    }

    public void pause() {
        this.mMediaPlayer.pause();
    }

    public void play() {
        this.mMediaPlayer.start();
    }

    public boolean prepare() {
        try {
            this.mMediaPlayer.setDataSource(this.mPath);
            this.mMediaPlayer.setAudioStreamType(3);
            this.mMediaPlayer.prepare();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void resume() {
        this.mMediaPlayer.start();
    }

    public void setLoop(boolean z) {
        this.mMediaPlayer.setLooping(z);
    }

    public void stop() {
        this.mMediaPlayer.stop();
    }
}
