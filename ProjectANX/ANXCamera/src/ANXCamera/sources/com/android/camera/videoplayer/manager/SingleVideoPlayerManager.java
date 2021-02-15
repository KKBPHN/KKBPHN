package com.android.camera.videoplayer.manager;

import android.content.res.AssetFileDescriptor;
import com.android.camera.videoplayer.MessagesHandlerThread;
import com.android.camera.videoplayer.PlayerMessageState;
import com.android.camera.videoplayer.SetNewViewForPlayback;
import com.android.camera.videoplayer.meta.MetaData;
import com.android.camera.videoplayer.player_messages.ClearPlayerInstance;
import com.android.camera.videoplayer.player_messages.CreateNewPlayerInstance;
import com.android.camera.videoplayer.player_messages.PlayerMessage;
import com.android.camera.videoplayer.player_messages.Prepare;
import com.android.camera.videoplayer.player_messages.Release;
import com.android.camera.videoplayer.player_messages.Reset;
import com.android.camera.videoplayer.player_messages.SetAssetsDataSourceMessage;
import com.android.camera.videoplayer.player_messages.SetUrlDataSourceMessage;
import com.android.camera.videoplayer.player_messages.Start;
import com.android.camera.videoplayer.player_messages.Stop;
import com.android.camera.videoplayer.ui.MediaPlayerWrapper.MainThreadMediaPlayerListener;
import com.android.camera.videoplayer.ui.VideoPlayerView;
import java.util.Arrays;

public class SingleVideoPlayerManager implements VideoPlayerManager, VideoPlayerManagerCallback, MainThreadMediaPlayerListener {
    private static final boolean SHOW_LOGS = false;
    private static final String TAG = "SingleVideoPlayerManager";
    private MetaData mCurrentItemMetaData;
    private VideoPlayerView mCurrentPlayer = null;
    private PlayerMessageState mCurrentPlayerState = PlayerMessageState.IDLE;
    private final MessagesHandlerThread mPlayerHandler = new MessagesHandlerThread();
    private final PlayerItemChangeListener mPlayerItemChangeListener;

    /* renamed from: com.android.camera.videoplayer.manager.SingleVideoPlayerManager$1 reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$camera$videoplayer$PlayerMessageState = new int[PlayerMessageState.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(48:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|35|36|37|38|39|40|41|42|43|44|45|46|(3:47|48|50)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(50:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|35|36|37|38|39|40|41|42|43|44|45|46|47|48|50) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0040 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x004b */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0056 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0062 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x006e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x007a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0086 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0092 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x009e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x00aa */
        /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x00b6 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x00c2 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:35:0x00ce */
        /* JADX WARNING: Missing exception handler attribute for start block: B:37:0x00da */
        /* JADX WARNING: Missing exception handler attribute for start block: B:39:0x00e6 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:41:0x00f2 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:43:0x00fe */
        /* JADX WARNING: Missing exception handler attribute for start block: B:45:0x010a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:47:0x0116 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.SETTING_NEW_PLAYER.ordinal()] = 1;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.IDLE.ordinal()] = 2;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.CREATING_PLAYER_INSTANCE.ordinal()] = 3;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.PLAYER_INSTANCE_CREATED.ordinal()] = 4;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.CLEARING_PLAYER_INSTANCE.ordinal()] = 5;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.PLAYER_INSTANCE_CLEARED.ordinal()] = 6;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.INITIALIZED.ordinal()] = 7;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.PREPARING.ordinal()] = 8;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.PREPARED.ordinal()] = 9;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.STARTING.ordinal()] = 10;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.STARTED.ordinal()] = 11;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.PAUSING.ordinal()] = 12;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.PAUSED.ordinal()] = 13;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.SETTING_DATA_SOURCE.ordinal()] = 14;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.DATA_SOURCE_SET.ordinal()] = 15;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.STOPPING.ordinal()] = 16;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.STOPPED.ordinal()] = 17;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.ERROR.ordinal()] = 18;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.PLAYBACK_COMPLETED.ordinal()] = 19;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.RESETTING.ordinal()] = 20;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.RESET.ordinal()] = 21;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.RELEASING.ordinal()] = 22;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.RELEASED.ordinal()] = 23;
            try {
                $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.END.ordinal()] = 24;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public SingleVideoPlayerManager(PlayerItemChangeListener playerItemChangeListener) {
        this.mPlayerItemChangeListener = playerItemChangeListener;
    }

    private boolean isInPlaybackState() {
        PlayerMessageState playerMessageState = this.mCurrentPlayerState;
        return playerMessageState == PlayerMessageState.STARTED || playerMessageState == PlayerMessageState.STARTING;
    }

    private void resetReleaseClearCurrentPlayer() {
        switch (AnonymousClass1.$SwitchMap$com$android$camera$videoplayer$PlayerMessageState[this.mCurrentPlayerState.ordinal()]) {
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 16:
            case 17:
            case 18:
            case 19:
                this.mPlayerHandler.addMessage(new Reset(this.mCurrentPlayer, this));
                break;
            case 20:
            case 21:
                break;
            case 22:
            case 23:
                break;
            case 24:
                StringBuilder sb = new StringBuilder();
                sb.append("unhandled ");
                sb.append(this.mCurrentPlayerState);
                throw new RuntimeException(sb.toString());
            default:
                return;
        }
        this.mPlayerHandler.addMessage(new Release(this.mCurrentPlayer, this));
        this.mPlayerHandler.addMessage(new ClearPlayerInstance(this.mCurrentPlayer, this));
    }

    private void setNewViewForPlayback(MetaData metaData, VideoPlayerView videoPlayerView) {
        this.mPlayerHandler.addMessage(new SetNewViewForPlayback(metaData, videoPlayerView, this));
    }

    private void startNewPlayback(MetaData metaData, VideoPlayerView videoPlayerView, AssetFileDescriptor assetFileDescriptor) {
        videoPlayerView.addMediaPlayerListener(this);
        this.mPlayerHandler.clearAllPendingMessages(TAG);
        stopResetReleaseClearCurrentPlayer();
        setNewViewForPlayback(metaData, videoPlayerView);
        startPlayback(videoPlayerView, assetFileDescriptor);
    }

    private void startNewPlayback(MetaData metaData, VideoPlayerView videoPlayerView, String str) {
        videoPlayerView.addMediaPlayerListener(this);
        this.mPlayerHandler.clearAllPendingMessages(TAG);
        stopResetReleaseClearCurrentPlayer();
        setNewViewForPlayback(metaData, videoPlayerView);
        startPlayback(videoPlayerView, str);
    }

    private void startPlayback(VideoPlayerView videoPlayerView, AssetFileDescriptor assetFileDescriptor) {
        this.mPlayerHandler.addMessages(Arrays.asList(new PlayerMessage[]{new CreateNewPlayerInstance(videoPlayerView, this), new SetAssetsDataSourceMessage(videoPlayerView, assetFileDescriptor, this), new Prepare(videoPlayerView, this), new Start(videoPlayerView, this)}));
    }

    private void startPlayback(VideoPlayerView videoPlayerView, String str) {
        this.mPlayerHandler.addMessages(Arrays.asList(new PlayerMessage[]{new CreateNewPlayerInstance(videoPlayerView, this), new SetUrlDataSourceMessage(videoPlayerView, str, this), new Prepare(videoPlayerView, this), new Start(videoPlayerView, this)}));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0033, code lost:
        r3.mPlayerHandler.addMessage(new com.android.camera.videoplayer.player_messages.Reset(r3.mCurrentPlayer, r3));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x003f, code lost:
        r3.mPlayerHandler.addMessage(new com.android.camera.videoplayer.player_messages.Release(r3.mCurrentPlayer, r3));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x004b, code lost:
        r3.mPlayerHandler.addMessage(new com.android.camera.videoplayer.player_messages.ClearPlayerInstance(r3.mCurrentPlayer, r3));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void stopResetReleaseClearCurrentPlayer() {
        switch (AnonymousClass1.$SwitchMap$com$android$camera$videoplayer$PlayerMessageState[this.mCurrentPlayerState.ordinal()]) {
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
                this.mPlayerHandler.addMessage(new Stop(this.mCurrentPlayer, this));
                break;
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
                break;
            case 20:
            case 21:
                break;
            case 22:
            case 23:
                break;
            case 24:
                StringBuilder sb = new StringBuilder();
                sb.append("unhandled ");
                sb.append(this.mCurrentPlayerState);
                throw new RuntimeException(sb.toString());
            default:
                return;
        }
    }

    public PlayerMessageState getCurrentPlayerState() {
        return this.mCurrentPlayerState;
    }

    public void onBufferingUpdateMainThread(int i) {
    }

    public void onErrorMainThread(int i, int i2) {
        this.mCurrentPlayerState = PlayerMessageState.ERROR;
    }

    public void onVideoCompletionMainThread() {
        this.mCurrentPlayerState = PlayerMessageState.PLAYBACK_COMPLETED;
    }

    public void onVideoPreparedMainThread() {
    }

    public void onVideoSizeChangedMainThread(int i, int i2) {
    }

    public void onVideoStoppedMainThread() {
    }

    public void playNewVideo(MetaData metaData, VideoPlayerView videoPlayerView, AssetFileDescriptor assetFileDescriptor) {
        this.mPlayerHandler.pauseQueueProcessing(TAG);
        boolean z = true;
        boolean z2 = this.mCurrentPlayer == videoPlayerView;
        VideoPlayerView videoPlayerView2 = this.mCurrentPlayer;
        if (videoPlayerView2 == null || videoPlayerView2.getAssetFileDescriptorDataSource() != assetFileDescriptor) {
            z = false;
        }
        if (!z2 || !isInPlaybackState() || !z) {
            startNewPlayback(metaData, videoPlayerView, assetFileDescriptor);
        }
        this.mPlayerHandler.resumeQueueProcessing(TAG);
    }

    public void playNewVideo(MetaData metaData, VideoPlayerView videoPlayerView, String str) {
        this.mPlayerHandler.pauseQueueProcessing(TAG);
        boolean z = true;
        boolean z2 = this.mCurrentPlayer == videoPlayerView;
        VideoPlayerView videoPlayerView2 = this.mCurrentPlayer;
        if (videoPlayerView2 == null || !str.equals(videoPlayerView2.getVideoUrlDataSource())) {
            z = false;
        }
        if (!z2 || !isInPlaybackState() || !z) {
            startNewPlayback(metaData, videoPlayerView, str);
        }
        this.mPlayerHandler.resumeQueueProcessing(TAG);
    }

    public void resetMediaPlayer() {
        this.mPlayerHandler.pauseQueueProcessing(TAG);
        this.mPlayerHandler.clearAllPendingMessages(TAG);
        resetReleaseClearCurrentPlayer();
        this.mPlayerHandler.resumeQueueProcessing(TAG);
    }

    public void resumeMediaPlayer() {
        VideoPlayerView videoPlayerView = this.mCurrentPlayer;
        if (videoPlayerView != null && this.mCurrentItemMetaData != null && videoPlayerView.getAssetFileDescriptorDataSource() != null) {
            MetaData metaData = this.mCurrentItemMetaData;
            VideoPlayerView videoPlayerView2 = this.mCurrentPlayer;
            playNewVideo(metaData, videoPlayerView2, videoPlayerView2.getAssetFileDescriptorDataSource());
        }
    }

    public void setCurrentItem(MetaData metaData, VideoPlayerView videoPlayerView) {
        this.mCurrentPlayer = videoPlayerView;
        this.mCurrentItemMetaData = metaData;
        this.mPlayerItemChangeListener.onPlayerItemChanged(metaData);
    }

    public void setVideoPlayerState(VideoPlayerView videoPlayerView, PlayerMessageState playerMessageState) {
        this.mCurrentPlayerState = playerMessageState;
    }

    public void stopAnyPlayback() {
        this.mPlayerHandler.pauseQueueProcessing(TAG);
        this.mPlayerHandler.clearAllPendingMessages(TAG);
        stopResetReleaseClearCurrentPlayer();
        this.mPlayerHandler.resumeQueueProcessing(TAG);
    }

    public void terminate() {
        this.mPlayerHandler.terminate();
    }
}
