package com.android.camera.fragment.vv;

import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.view.Surface;
import android.view.View;
import android.widget.ImageView;
import com.android.camera.log.Log;
import com.android.camera.ui.TextureVideoView;
import com.android.camera.ui.TextureVideoView.MediaPlayerCallback;
import com.android.camera.visibilityutils.items.ListItem;
import com.google.android.play.core.splitinstall.SplitInstallHelper;
import com.xiaomi.mediaprocess.EffectMediaPlayer;
import com.xiaomi.mediaprocess.EffectMediaPlayer.SurfaceGravity;
import com.xiaomi.mediaprocess.EffectNotifier;
import com.xiaomi.mediaprocess.MediaEffectGraph;
import com.xiaomi.vlog.SystemUtil;
import java.util.ArrayList;

public class VVWorkspacePlayerItem implements ListItem, MediaPlayerCallback {
    private static final String TAG = "VVWPI";
    private ImageView mCoverImageView;
    private final Rect mCurrentViewRect = new Rect();
    private EffectMediaPlayer mEffectMediaPlayer;
    private MediaEffectGraph mMediaEffectGraph;
    private ImageView mPlayView;
    private VVItem mVVItem;
    private VVWorkspaceItem mVVWorkspaceItem;
    private TextureVideoView mVideoView;
    private boolean mWaitingResultSurfaceTexture;

    public VVWorkspacePlayerItem(VVWorkspaceItem vVWorkspaceItem, VVItem vVItem) {
        this.mVVWorkspaceItem = vVWorkspaceItem;
        this.mVVItem = vVItem;
        if (this.mVVWorkspaceItem != null && this.mVVItem == null) {
            String str = TAG;
            Log.d(str, "createFromRawInfo");
            this.mVVItem = VVItem.createFromRawInfo(this.mVVWorkspaceItem.getRawInfoPath());
            if (this.mVVItem == null) {
                Log.d(str, "create failed");
            }
        }
    }

    private boolean isValid() {
        if (this.mVideoView != null) {
            VVWorkspaceItem vVWorkspaceItem = this.mVVWorkspaceItem;
            if (vVWorkspaceItem != null && vVWorkspaceItem.getFragments().size() > 0) {
                return true;
            }
        }
        return false;
    }

    private void prepare() {
        SplitInstallHelper.loadLibrary(this.mVideoView.getContext().getApplicationContext(), "vvc++_shared");
        SplitInstallHelper.loadLibrary(this.mVideoView.getContext().getApplicationContext(), "vvffmpeg");
        SplitInstallHelper.loadLibrary(this.mVideoView.getContext().getApplicationContext(), "vvmediaeditor");
        SystemUtil.Init(this.mVideoView.getContext(), 123);
    }

    private void prepareEffectGraph(VVItem vVItem, VVWorkspaceItem vVWorkspaceItem) {
        String str = vVItem.musicPath;
        String[] strArr = (String[]) new ArrayList(vVWorkspaceItem.getFragments()).toArray(new String[0]);
        this.mMediaEffectGraph = new MediaEffectGraph();
        this.mMediaEffectGraph.ConstructMediaEffectGraph();
        this.mMediaEffectGraph.SetAudioMute(true);
        this.mMediaEffectGraph.AddSourceAndEffectByTemplate(strArr, vVItem.baseArchivesFolder);
        this.mMediaEffectGraph.AddAudioTrack(str, false);
    }

    private void startPlay(Surface surface) {
        if (this.mVVItem != null) {
            prepare();
            prepareEffectGraph(this.mVVItem, this.mVVWorkspaceItem);
            this.mEffectMediaPlayer = new EffectMediaPlayer(this.mMediaEffectGraph);
            this.mEffectMediaPlayer.ConstructMediaPlayer();
            this.mEffectMediaPlayer.SetPlayerNotify(new EffectNotifier() {
                public void OnReceiveFailed() {
                    Log.d("OnReceiveFailed:", "yes");
                }

                public void OnReceiveFinish() {
                    Log.d("OnReceiveFinish:", "yes");
                }
            });
            this.mEffectMediaPlayer.SetViewSurface(surface);
            this.mEffectMediaPlayer.setGravity(SurfaceGravity.SurfaceGravityResizeAspectFit, 1920, 1080);
            this.mEffectMediaPlayer.SetPlayLoop(true);
            this.mEffectMediaPlayer.SetGraphLoop(true);
            this.mEffectMediaPlayer.StartPreView();
        }
    }

    private boolean viewIsPartiallyHiddenBottom(int i) {
        int i2 = this.mCurrentViewRect.bottom;
        return i2 > 0 && i2 < i;
    }

    private boolean viewIsPartiallyHiddenTop() {
        return this.mCurrentViewRect.top > 0;
    }

    public void deactivate(View view, int i) {
    }

    public VVItem getVVItem() {
        return this.mVVItem;
    }

    public VVWorkspaceItem getVVWorkspaceItem() {
        return this.mVVWorkspaceItem;
    }

    public int getVisibilityPercents(View view) {
        int i;
        view.getLocalVisibleRect(this.mCurrentViewRect);
        int height = view.getHeight();
        if (viewIsPartiallyHiddenTop()) {
            i = height - this.mCurrentViewRect.top;
        } else if (!viewIsPartiallyHiddenBottom(height)) {
            return 100;
        } else {
            i = this.mCurrentViewRect.bottom;
        }
        return (i * 100) / height;
    }

    public boolean isPlaying() {
        boolean z = false;
        if (!isValid()) {
            return false;
        }
        if (this.mPlayView.getVisibility() != 0) {
            z = true;
        }
        return z;
    }

    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
    }

    public void onCompletion(MediaPlayer mediaPlayer) {
    }

    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        return false;
    }

    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i2) {
        return false;
    }

    public void onPrepared(MediaPlayer mediaPlayer) {
    }

    public void onSurfaceReady(Surface surface) {
        if (this.mWaitingResultSurfaceTexture) {
            this.mWaitingResultSurfaceTexture = false;
            this.mVideoView.setVideoSpecifiedSize(1920, 1080);
            startPlay(surface);
        }
    }

    public void onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        stopPlay();
    }

    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2) {
    }

    public void setActive(View view, int i) {
    }

    public void startPlay() {
        if (isValid()) {
            this.mVideoView.setMediaPlayerCallback(this);
            if (this.mVideoView.getPreviewSurface() != null) {
                startPlay(this.mVideoView.getPreviewSurface());
            } else {
                this.mWaitingResultSurfaceTexture = true;
            }
            this.mCoverImageView.setVisibility(8);
            this.mPlayView.setVisibility(8);
        }
    }

    public void stopPlay() {
        if (isValid()) {
            if (this.mPlayView.getVisibility() != 0) {
                this.mPlayView.setVisibility(0);
            }
            if (this.mCoverImageView.getVisibility() != 0) {
                this.mCoverImageView.setVisibility(0);
            }
            EffectMediaPlayer effectMediaPlayer = this.mEffectMediaPlayer;
            if (effectMediaPlayer != null) {
                effectMediaPlayer.StopPreView();
                this.mEffectMediaPlayer.DestructMediaPlayer();
                this.mEffectMediaPlayer = null;
            }
            MediaEffectGraph mediaEffectGraph = this.mMediaEffectGraph;
            if (mediaEffectGraph != null) {
                mediaEffectGraph.DestructMediaEffectGraph();
                this.mMediaEffectGraph = null;
            }
        }
    }

    public void updateTargetVideoView(ImageView imageView, TextureVideoView textureVideoView, ImageView imageView2) {
        stopPlay();
        this.mCoverImageView = imageView;
        this.mVideoView = textureVideoView;
        this.mPlayView = imageView2;
    }
}
