package com.android.camera.module.impl.component;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.text.TextUtils;
import android.view.Surface;
import com.android.camera.log.Log;
import com.android.camera.module.MiLiveModule;
import com.android.camera.module.impl.component.ILive.ILivePlayer;
import com.android.camera.module.impl.component.ILive.ILivePlayerStateListener;
import com.android.camera.module.impl.component.ILive.ILiveSegmentData;
import com.xiaomi.recordmediaprocess.EffectMediaPlayer;
import com.xiaomi.recordmediaprocess.EffectMediaPlayer.SurfaceGravity;
import com.xiaomi.recordmediaprocess.EffectNotifier;
import com.xiaomi.recordmediaprocess.MediaComposeFile;
import com.xiaomi.recordmediaprocess.MediaEffectGraph;
import java.util.Arrays;
import java.util.List;

public class MiLivePlayer implements ILivePlayer {
    private static final int DEFAULT_FPS = 30;
    private static final int DEFAULT_TARGET_BITRATE = 31457280;
    /* access modifiers changed from: private */
    public final String TAG;
    private String mAudioPath;
    private EffectNotifier mComposeNotifier = new EffectNotifier() {
        public void OnReceiveFailed() {
            Log.d(MiLivePlayer.this.TAG, "Compose notifier OnReceiveFailed");
            MiLivePlayer.this.setComposeState(-1);
            MiLivePlayer.this.setComposeState(1);
        }

        public void OnReceiveFinish() {
            Log.d(MiLivePlayer.this.TAG, "Compose notifier OnReceiveFinish");
            MiLivePlayer.this.setComposeState(3);
        }
    };
    private int mComposeState = 0;
    private EffectMediaPlayer mEffectMediaPlayer;
    private MediaComposeFile mMediaComposeFile;
    private MediaEffectGraph mMediaEffectGraph;
    private EffectNotifier mPlayerNotifier = new EffectNotifier() {
        public void OnReceiveFailed() {
            Log.d(MiLivePlayer.this.TAG, "player notifier OnReceiveFailed");
            MiLivePlayer.this.setPlayerState(-1);
        }

        public void OnReceiveFinish() {
            Log.d(MiLivePlayer.this.TAG, "player notifier OnReceiveFinish");
        }
    };
    private int mPlayerState = 0;
    private int mPreviewHeight;
    private int mPreviewWidth;
    private List mSegmentData;
    private ILivePlayerStateListener mStateListener;
    private int mVideoHeight;
    private int mVideoWidth;

    public MiLivePlayer(Activity activity) {
        StringBuilder sb = new StringBuilder();
        sb.append(MiLivePlayer.class.getSimpleName());
        sb.append("@");
        sb.append(hashCode());
        this.TAG = sb.toString();
        MiLiveModule.loadLibs(activity.getApplicationContext(), MiLiveModule.LIB_LOAD_CALLER_PLAYER);
    }

    private String getComposeStateString(int i) {
        return i != -1 ? i != 0 ? i != 1 ? i != 2 ? i != 3 ? "UNKNOWN" : "COMPOSED" : "COMPOSING" : "PREPARE" : "IDLE" : "ERROR";
    }

    private String getPlayerStateString(int i) {
        return i != -1 ? i != 0 ? i != 1 ? i != 2 ? i != 3 ? i != 4 ? "UNKNOWN" : "PAUSE" : "PREVIEWING" : "PENDING_START" : "PREPARE" : "IDLE" : "ERROR";
    }

    private void initEffectGraph() {
        List list = this.mSegmentData;
        if (list != null && list.size() > 0) {
            releaseAllLibs();
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("initEffectGraph mSegmentData = ");
            sb.append(Arrays.toString(this.mSegmentData.toArray()));
            Log.d(str, sb.toString());
            String[] strArr = new String[this.mSegmentData.size()];
            float[] fArr = new float[this.mSegmentData.size()];
            for (int i = 0; i < this.mSegmentData.size(); i++) {
                strArr[i] = ((ILiveSegmentData) this.mSegmentData.get(i)).getPath();
                fArr[i] = ((ILiveSegmentData) this.mSegmentData.get(i)).getSpeed();
            }
            this.mMediaEffectGraph = new MediaEffectGraph();
            this.mMediaEffectGraph.ConstructMediaEffectGraph();
            this.mMediaEffectGraph.AddSourcesAndEffectBySourcesPath(strArr, fArr);
            if (!TextUtils.isEmpty(this.mAudioPath)) {
                this.mMediaEffectGraph.SetAudioMute(true);
                this.mMediaEffectGraph.AddAudioTrack(this.mAudioPath, false);
            }
        }
    }

    private void initMediaCompose() {
        if (this.mVideoHeight <= 0 || this.mVideoWidth <= 0) {
            Log.e(this.TAG, "invalid video size.");
            return;
        }
        initEffectGraph();
        this.mMediaComposeFile = new MediaComposeFile(this.mMediaEffectGraph);
        this.mMediaComposeFile.ConstructMediaComposeFile(this.mVideoWidth, this.mVideoHeight, DEFAULT_TARGET_BITRATE, 30, this.mComposeNotifier);
    }

    private void initMediaPlayer(int i, int i2) {
        if (this.mVideoHeight <= 0 || this.mVideoWidth <= 0 || i <= 0 || i2 <= 0) {
            Log.e(this.TAG, "invalid video size.");
            return;
        }
        initEffectGraph();
        this.mEffectMediaPlayer = new EffectMediaPlayer(this.mMediaEffectGraph);
        this.mEffectMediaPlayer.ConstructMediaPlayer();
        this.mEffectMediaPlayer.SetPlayerNotify(this.mPlayerNotifier);
        this.mEffectMediaPlayer.SetPlayLoop(true);
        this.mEffectMediaPlayer.setGravity(SurfaceGravity.SurfaceGravityResizeAspectFit, i, i2);
        this.mEffectMediaPlayer.SetGraphLoop(true);
        this.mEffectMediaPlayer.EnableUserAdjustRotatePlay(true);
        Log.d(this.TAG, "initMediaPlayer");
    }

    private void releaseAllLibs() {
        EffectMediaPlayer effectMediaPlayer = this.mEffectMediaPlayer;
        if (effectMediaPlayer != null) {
            effectMediaPlayer.DestructMediaPlayer();
            this.mEffectMediaPlayer = null;
        }
        MediaComposeFile mediaComposeFile = this.mMediaComposeFile;
        if (mediaComposeFile != null) {
            mediaComposeFile.DestructMediaComposeFile();
            this.mMediaComposeFile = null;
        }
        MediaEffectGraph mediaEffectGraph = this.mMediaEffectGraph;
        if (mediaEffectGraph != null) {
            mediaEffectGraph.DestructMediaEffectGraph();
            this.mMediaEffectGraph = null;
        }
    }

    /* access modifiers changed from: private */
    public void setComposeState(int i) {
        if (this.mComposeState != i) {
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("ComposeState state change from ");
            sb.append(getComposeStateString(this.mComposeState));
            sb.append(" to ");
            sb.append(getComposeStateString(i));
            Log.d(str, sb.toString());
            this.mComposeState = i;
            ILivePlayerStateListener iLivePlayerStateListener = this.mStateListener;
            if (iLivePlayerStateListener != null) {
                iLivePlayerStateListener.onComposeStateChange(this.mComposeState);
            }
        }
    }

    /* access modifiers changed from: private */
    public void setPlayerState(int i) {
        if (this.mPlayerState != i) {
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Player state change from ");
            sb.append(getPlayerStateString(this.mPlayerState));
            sb.append(" to ");
            sb.append(getPlayerStateString(i));
            Log.d(str, sb.toString());
            this.mPlayerState = i;
            ILivePlayerStateListener iLivePlayerStateListener = this.mStateListener;
            if (iLivePlayerStateListener != null) {
                iLivePlayerStateListener.onPlayStateChange(this.mPlayerState);
            }
        }
    }

    public void cancelCompose() {
        if (this.mComposeState == 2) {
            this.mMediaComposeFile.CancelCompose();
        }
    }

    public void init(int i, int i2, int i3, int i4, List list, String str) {
        String str2 = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("init video size = ");
        sb.append(i);
        String str3 = "x";
        sb.append(str3);
        sb.append(i2);
        sb.append(", preview size = ");
        sb.append(i3);
        sb.append(str3);
        sb.append(i4);
        sb.append(", data = ");
        sb.append(Arrays.toString(list.toArray()));
        sb.append(", audioPath = ");
        sb.append(str);
        Log.d(str2, sb.toString());
        this.mVideoWidth = Math.max(i, i2);
        this.mVideoHeight = Math.min(i, i2);
        this.mPreviewWidth = i3;
        this.mPreviewHeight = i4;
        this.mSegmentData = list;
        this.mAudioPath = str;
        setPlayerState(1);
        setComposeState(1);
    }

    public void pausePlayer() {
        if (this.mPlayerState == 3) {
            this.mEffectMediaPlayer.PausePreView();
            setPlayerState(4);
        }
    }

    public void release() {
        Log.d(this.TAG, "release");
        releaseAllLibs();
        reset();
        MiLiveModule.unloadLibs(MiLiveModule.LIB_LOAD_CALLER_PLAYER);
    }

    public void reset() {
        if (this.mPlayerState != 0) {
            setPlayerState(1);
        }
        if (this.mComposeState != 0) {
            setComposeState(1);
        }
    }

    public void resumePlayer() {
        if (this.mPlayerState == 4) {
            this.mEffectMediaPlayer.ResumePreView();
            setPlayerState(3);
        }
    }

    public void setStateListener(ILivePlayerStateListener iLivePlayerStateListener) {
        this.mStateListener = iLivePlayerStateListener;
    }

    public void startCompose(String str) {
        String str2 = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("startCompose path = ");
        sb.append(str);
        sb.append(", state = ");
        sb.append(getComposeStateString(this.mComposeState));
        Log.d(str2, sb.toString());
        if (this.mComposeState == 1 || this.mPlayerState == 4) {
            initMediaCompose();
            setComposeState(2);
            Log.d(this.TAG, "startCompose +");
            this.mMediaComposeFile.SetComposeFileName(str);
            this.mMediaComposeFile.BeginCompose();
            Log.d(this.TAG, "startCompose -");
        }
    }

    public void startPlayer(SurfaceTexture surfaceTexture) {
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("startPlayer state = ");
        sb.append(getPlayerStateString(this.mPlayerState));
        sb.append(",texture = ");
        sb.append(surfaceTexture);
        Log.d(str, sb.toString());
        if (this.mPlayerState == 1) {
            initMediaPlayer(this.mPreviewWidth, this.mPreviewHeight);
            this.mEffectMediaPlayer.SetViewSurface(new Surface(surfaceTexture));
            setPlayerState(2);
            this.mEffectMediaPlayer.StartPreView();
            setPlayerState(3);
        }
    }

    public void stopPlayer() {
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("stopPlayer state = ");
        sb.append(getPlayerStateString(this.mPlayerState));
        Log.d(str, sb.toString());
        int i = this.mPlayerState;
        if (i == 3 || i == 4) {
            this.mEffectMediaPlayer.StopPreView();
            setPlayerState(1);
        }
    }
}
