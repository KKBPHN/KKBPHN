package com.android.camera.videoplayer.player_messages;

import com.android.camera.videoplayer.PlayerMessageState;
import com.android.camera.videoplayer.manager.VideoPlayerManagerCallback;
import com.android.camera.videoplayer.ui.VideoPlayerView;

public class Start extends PlayerMessage {
    private static final boolean SHOW_LOGS = false;
    private static final String TAG = "Start";
    private PlayerMessageState mResultPlayerMessageState;

    /* renamed from: com.android.camera.videoplayer.player_messages.Start$1 reason: invalid class name */
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
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.INITIALIZED.ordinal()] = 3;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.PREPARING.ordinal()] = 4;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.PREPARED.ordinal()] = 5;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.RELEASING.ordinal()] = 6;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.RELEASED.ordinal()] = 7;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.RESETTING.ordinal()] = 8;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.RESET.ordinal()] = 9;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.CLEARING_PLAYER_INSTANCE.ordinal()] = 10;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.PLAYER_INSTANCE_CLEARED.ordinal()] = 11;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.CREATING_PLAYER_INSTANCE.ordinal()] = 12;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.PLAYER_INSTANCE_CREATED.ordinal()] = 13;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.SETTING_DATA_SOURCE.ordinal()] = 14;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.DATA_SOURCE_SET.ordinal()] = 15;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.PLAYBACK_COMPLETED.ordinal()] = 16;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.END.ordinal()] = 17;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.STARTING.ordinal()] = 18;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.ERROR.ordinal()] = 19;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.STARTED.ordinal()] = 20;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.PAUSING.ordinal()] = 21;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.PAUSED.ordinal()] = 22;
            $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.STOPPING.ordinal()] = 23;
            try {
                $SwitchMap$com$android$camera$videoplayer$PlayerMessageState[PlayerMessageState.STOPPED.ordinal()] = 24;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public Start(VideoPlayerView videoPlayerView, VideoPlayerManagerCallback videoPlayerManagerCallback) {
        super(videoPlayerView, videoPlayerManagerCallback);
    }

    /* access modifiers changed from: protected */
    public void performAction(VideoPlayerView videoPlayerView) {
        PlayerMessageState playerMessageState;
        PlayerMessageState currentState = getCurrentState();
        String str = "unhandled current state ";
        switch (AnonymousClass1.$SwitchMap$com$android$camera$videoplayer$PlayerMessageState[currentState.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(currentState);
                throw new RuntimeException(sb.toString());
            case 18:
                videoPlayerView.start();
                playerMessageState = PlayerMessageState.STARTED;
                break;
            case 19:
                playerMessageState = PlayerMessageState.ERROR;
                break;
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(currentState);
                throw new RuntimeException(sb2.toString());
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
        PlayerMessageState currentState = getCurrentState();
        String str = "unhandled current state ";
        switch (AnonymousClass1.$SwitchMap$com$android$camera$videoplayer$PlayerMessageState[currentState.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(currentState);
                throw new RuntimeException(sb.toString());
            case 5:
                return PlayerMessageState.STARTING;
            case 19:
                return PlayerMessageState.ERROR;
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(currentState);
                throw new RuntimeException(sb2.toString());
            default:
                return null;
        }
    }
}
