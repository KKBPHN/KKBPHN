package com.android.camera.videoplayer.player_messages;

import com.android.camera.videoplayer.PlayerMessageState;
import com.android.camera.videoplayer.manager.VideoPlayerManagerCallback;
import com.android.camera.videoplayer.ui.MediaPlayerWrapper.State;
import com.android.camera.videoplayer.ui.VideoPlayerView;

public class Prepare extends PlayerMessage {
    private static final boolean SHOW_LOGS = false;
    private static final String TAG = "Prepare";
    private PlayerMessageState mResultPlayerMessageState;

    /* renamed from: com.android.camera.videoplayer.player_messages.Prepare$1 reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State = new int[State.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(20:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|(3:19|20|22)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(22:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|22) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0040 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x004b */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0056 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0062 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x006e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[State.IDLE.ordinal()] = 1;
            $SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[State.INITIALIZED.ordinal()] = 2;
            $SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[State.PREPARING.ordinal()] = 3;
            $SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[State.STARTED.ordinal()] = 4;
            $SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[State.PAUSED.ordinal()] = 5;
            $SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[State.STOPPED.ordinal()] = 6;
            $SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[State.PLAYBACK_COMPLETED.ordinal()] = 7;
            $SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[State.END.ordinal()] = 8;
            $SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[State.PREPARED.ordinal()] = 9;
            try {
                $SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[State.ERROR.ordinal()] = 10;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public Prepare(VideoPlayerView videoPlayerView, VideoPlayerManagerCallback videoPlayerManagerCallback) {
        super(videoPlayerView, videoPlayerManagerCallback);
    }

    /* access modifiers changed from: protected */
    public void performAction(VideoPlayerView videoPlayerView) {
        PlayerMessageState playerMessageState;
        videoPlayerView.prepare();
        State currentState = videoPlayerView.getCurrentState();
        switch (AnonymousClass1.$SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[currentState.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                StringBuilder sb = new StringBuilder();
                sb.append("unhandled state ");
                sb.append(currentState);
                throw new RuntimeException(sb.toString());
            case 9:
                playerMessageState = PlayerMessageState.PREPARED;
                break;
            case 10:
                playerMessageState = PlayerMessageState.ERROR;
                break;
            default:
                return;
        }
        this.mResultPlayerMessageState = playerMessageState;
    }

    /* access modifiers changed from: protected */
    public PlayerMessageState stateAfter() {
        return this.mResultPlayerMessageState;
    }

    /* access modifiers changed from: protected */
    public PlayerMessageState stateBefore() {
        return PlayerMessageState.PREPARING;
    }
}
