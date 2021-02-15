package android.support.v4.media.session;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioAttributes.Builder;
import android.media.AudioManager;
import android.media.MediaDescription;
import android.media.MediaMetadata;
import android.media.Rating;
import android.media.RemoteControlClient;
import android.media.RemoteControlClient.MetadataEditor;
import android.media.RemoteControlClient.OnMetadataUpdateListener;
import android.media.RemoteControlClient.OnPlaybackPositionUpdateListener;
import android.media.VolumeProvider;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.BadParcelableException;
import android.os.Binder;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.session.IMediaSession.Stub;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import androidx.annotation.GuardedBy;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.core.app.BundleCompat;
import androidx.media.MediaSessionManager.RemoteUserInfo;
import androidx.media.VolumeProviderCompat;
import androidx.media.session.MediaButtonReceiver;
import androidx.versionedparcelable.ParcelUtils;
import androidx.versionedparcelable.VersionedParcelable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import miui.view.MiuiHapticFeedbackConstants;

public class MediaSessionCompat {
    @RestrictTo({Scope.LIBRARY})
    public static final String ACTION_ARGUMENT_CAPTIONING_ENABLED = "android.support.v4.media.session.action.ARGUMENT_CAPTIONING_ENABLED";
    @RestrictTo({Scope.LIBRARY})
    public static final String ACTION_ARGUMENT_EXTRAS = "android.support.v4.media.session.action.ARGUMENT_EXTRAS";
    @RestrictTo({Scope.LIBRARY})
    public static final String ACTION_ARGUMENT_MEDIA_ID = "android.support.v4.media.session.action.ARGUMENT_MEDIA_ID";
    @RestrictTo({Scope.LIBRARY})
    public static final String ACTION_ARGUMENT_PLAYBACK_SPEED = "android.support.v4.media.session.action.ARGUMENT_PLAYBACK_SPEED";
    @RestrictTo({Scope.LIBRARY})
    public static final String ACTION_ARGUMENT_QUERY = "android.support.v4.media.session.action.ARGUMENT_QUERY";
    @RestrictTo({Scope.LIBRARY})
    public static final String ACTION_ARGUMENT_RATING = "android.support.v4.media.session.action.ARGUMENT_RATING";
    @RestrictTo({Scope.LIBRARY})
    public static final String ACTION_ARGUMENT_REPEAT_MODE = "android.support.v4.media.session.action.ARGUMENT_REPEAT_MODE";
    @RestrictTo({Scope.LIBRARY})
    public static final String ACTION_ARGUMENT_SHUFFLE_MODE = "android.support.v4.media.session.action.ARGUMENT_SHUFFLE_MODE";
    @RestrictTo({Scope.LIBRARY})
    public static final String ACTION_ARGUMENT_URI = "android.support.v4.media.session.action.ARGUMENT_URI";
    public static final String ACTION_FLAG_AS_INAPPROPRIATE = "android.support.v4.media.session.action.FLAG_AS_INAPPROPRIATE";
    public static final String ACTION_FOLLOW = "android.support.v4.media.session.action.FOLLOW";
    @RestrictTo({Scope.LIBRARY})
    public static final String ACTION_PLAY_FROM_URI = "android.support.v4.media.session.action.PLAY_FROM_URI";
    @RestrictTo({Scope.LIBRARY})
    public static final String ACTION_PREPARE = "android.support.v4.media.session.action.PREPARE";
    @RestrictTo({Scope.LIBRARY})
    public static final String ACTION_PREPARE_FROM_MEDIA_ID = "android.support.v4.media.session.action.PREPARE_FROM_MEDIA_ID";
    @RestrictTo({Scope.LIBRARY})
    public static final String ACTION_PREPARE_FROM_SEARCH = "android.support.v4.media.session.action.PREPARE_FROM_SEARCH";
    @RestrictTo({Scope.LIBRARY})
    public static final String ACTION_PREPARE_FROM_URI = "android.support.v4.media.session.action.PREPARE_FROM_URI";
    @RestrictTo({Scope.LIBRARY})
    public static final String ACTION_SET_CAPTIONING_ENABLED = "android.support.v4.media.session.action.SET_CAPTIONING_ENABLED";
    @RestrictTo({Scope.LIBRARY})
    public static final String ACTION_SET_PLAYBACK_SPEED = "android.support.v4.media.session.action.SET_PLAYBACK_SPEED";
    @RestrictTo({Scope.LIBRARY})
    public static final String ACTION_SET_RATING = "android.support.v4.media.session.action.SET_RATING";
    @RestrictTo({Scope.LIBRARY})
    public static final String ACTION_SET_REPEAT_MODE = "android.support.v4.media.session.action.SET_REPEAT_MODE";
    @RestrictTo({Scope.LIBRARY})
    public static final String ACTION_SET_SHUFFLE_MODE = "android.support.v4.media.session.action.SET_SHUFFLE_MODE";
    public static final String ACTION_SKIP_AD = "android.support.v4.media.session.action.SKIP_AD";
    public static final String ACTION_UNFOLLOW = "android.support.v4.media.session.action.UNFOLLOW";
    public static final String ARGUMENT_MEDIA_ATTRIBUTE = "android.support.v4.media.session.ARGUMENT_MEDIA_ATTRIBUTE";
    public static final String ARGUMENT_MEDIA_ATTRIBUTE_VALUE = "android.support.v4.media.session.ARGUMENT_MEDIA_ATTRIBUTE_VALUE";
    private static final String DATA_CALLING_PACKAGE = "data_calling_pkg";
    private static final String DATA_CALLING_PID = "data_calling_pid";
    private static final String DATA_CALLING_UID = "data_calling_uid";
    private static final String DATA_EXTRAS = "data_extras";
    @Deprecated
    public static final int FLAG_HANDLES_MEDIA_BUTTONS = 1;
    public static final int FLAG_HANDLES_QUEUE_COMMANDS = 4;
    @Deprecated
    public static final int FLAG_HANDLES_TRANSPORT_CONTROLS = 2;
    @RestrictTo({Scope.LIBRARY})
    public static final String KEY_EXTRA_BINDER = "android.support.v4.media.session.EXTRA_BINDER";
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public static final String KEY_SESSION2_TOKEN = "android.support.v4.media.session.SESSION_TOKEN2";
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public static final String KEY_TOKEN = "android.support.v4.media.session.TOKEN";
    private static final int MAX_BITMAP_SIZE_IN_DP = 320;
    public static final int MEDIA_ATTRIBUTE_ALBUM = 1;
    public static final int MEDIA_ATTRIBUTE_ARTIST = 0;
    public static final int MEDIA_ATTRIBUTE_PLAYLIST = 2;
    static final String TAG = "MediaSessionCompat";
    static int sMaxBitmapSize;
    private final ArrayList mActiveListeners;
    private final MediaControllerCompat mController;
    private final MediaSessionImpl mImpl;

    public abstract class Callback {
        final android.media.session.MediaSession.Callback mCallbackFwk;
        private CallbackHandler mCallbackHandler = null;
        private boolean mMediaPlayPauseKeyPending;
        WeakReference mSessionImpl;

        class CallbackHandler extends Handler {
            private static final int MSG_MEDIA_PLAY_PAUSE_KEY_DOUBLE_TAP_TIMEOUT = 1;

            CallbackHandler(Looper looper) {
                super(looper);
            }

            public void handleMessage(Message message) {
                if (message.what == 1) {
                    MediaSessionImpl mediaSessionImpl = (MediaSessionImpl) Callback.this.mSessionImpl.get();
                    if (mediaSessionImpl != null) {
                        mediaSessionImpl.setCurrentControllerInfo((RemoteUserInfo) message.obj);
                        Callback.this.handleMediaPlayPauseKeySingleTapIfPending();
                        mediaSessionImpl.setCurrentControllerInfo(null);
                    }
                }
            }
        }

        @RequiresApi(21)
        class MediaSessionCallbackApi21 extends android.media.session.MediaSession.Callback {
            MediaSessionCallbackApi21() {
            }

            /* access modifiers changed from: 0000 */
            public void clearCurrentControllerInfo() {
                WeakReference weakReference = Callback.this.mSessionImpl;
                MediaSessionImpl mediaSessionImpl = weakReference != null ? (MediaSessionImpl) weakReference.get() : null;
                if (mediaSessionImpl != null) {
                    mediaSessionImpl.setCurrentControllerInfo(null);
                }
            }

            /* JADX WARNING: type inference failed for: r1v4, types: [android.os.IBinder] */
            /* JADX WARNING: type inference failed for: r1v5, types: [android.os.IBinder] */
            /* JADX WARNING: type inference failed for: r1v6 */
            /* JADX WARNING: type inference failed for: r1v8 */
            /* JADX WARNING: Multi-variable type inference failed */
            /* JADX WARNING: Unknown variable types count: 2 */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onCommand(String str, Bundle bundle, ResultReceiver resultReceiver) {
                Callback callback;
                MediaDescriptionCompat description;
                MediaSessionCompat.ensureClassLoader(bundle);
                setCurrentControllerInfo();
                try {
                    QueueItem queueItem = 0;
                    if (str.equals(MediaControllerCompat.COMMAND_GET_EXTRA_BINDER)) {
                        MediaSessionImplApi21 mediaSessionImplApi21 = (MediaSessionImplApi21) Callback.this.mSessionImpl.get();
                        if (mediaSessionImplApi21 != null) {
                            Bundle bundle2 = new Bundle();
                            Token sessionToken = mediaSessionImplApi21.getSessionToken();
                            IMediaSession extraBinder = sessionToken.getExtraBinder();
                            BundleCompat.putBinder(bundle2, MediaSessionCompat.KEY_EXTRA_BINDER, extraBinder == null ? queueItem : extraBinder.asBinder());
                            ParcelUtils.putVersionedParcelable(bundle2, MediaSessionCompat.KEY_SESSION2_TOKEN, sessionToken.getSession2Token());
                            resultReceiver.send(0, bundle2);
                        }
                    } else {
                        boolean equals = str.equals(MediaControllerCompat.COMMAND_ADD_QUEUE_ITEM);
                        String str2 = MediaControllerCompat.COMMAND_ARGUMENT_MEDIA_DESCRIPTION;
                        if (equals) {
                            Callback.this.onAddQueueItem((MediaDescriptionCompat) bundle.getParcelable(str2));
                        } else {
                            boolean equals2 = str.equals(MediaControllerCompat.COMMAND_ADD_QUEUE_ITEM_AT);
                            String str3 = MediaControllerCompat.COMMAND_ARGUMENT_INDEX;
                            if (equals2) {
                                Callback.this.onAddQueueItem((MediaDescriptionCompat) bundle.getParcelable(str2), bundle.getInt(str3));
                            } else {
                                if (str.equals(MediaControllerCompat.COMMAND_REMOVE_QUEUE_ITEM)) {
                                    callback = Callback.this;
                                    description = (MediaDescriptionCompat) bundle.getParcelable(str2);
                                } else if (str.equals(MediaControllerCompat.COMMAND_REMOVE_QUEUE_ITEM_AT)) {
                                    MediaSessionImplApi21 mediaSessionImplApi212 = (MediaSessionImplApi21) Callback.this.mSessionImpl.get();
                                    if (!(mediaSessionImplApi212 == null || mediaSessionImplApi212.mQueue == null)) {
                                        int i = bundle.getInt(str3, -1);
                                        if (i >= 0 && i < mediaSessionImplApi212.mQueue.size()) {
                                            queueItem = (QueueItem) mediaSessionImplApi212.mQueue.get(i);
                                        }
                                        if (queueItem != 0) {
                                            callback = Callback.this;
                                            description = queueItem.getDescription();
                                        }
                                    }
                                } else {
                                    Callback.this.onCommand(str, bundle, resultReceiver);
                                }
                                callback.onRemoveQueueItem(description);
                            }
                        }
                    }
                } catch (BadParcelableException unused) {
                    Log.e(MediaSessionCompat.TAG, "Could not unparcel the extra data.");
                }
                clearCurrentControllerInfo();
            }

            public void onCustomAction(String str, Bundle bundle) {
                MediaSessionCompat.ensureClassLoader(bundle);
                setCurrentControllerInfo();
                Bundle bundle2 = bundle.getBundle(MediaSessionCompat.ACTION_ARGUMENT_EXTRAS);
                MediaSessionCompat.ensureClassLoader(bundle2);
                boolean equals = str.equals(MediaSessionCompat.ACTION_PLAY_FROM_URI);
                String str2 = MediaSessionCompat.ACTION_ARGUMENT_URI;
                if (equals) {
                    Callback.this.onPlayFromUri((Uri) bundle.getParcelable(str2), bundle2);
                } else if (str.equals(MediaSessionCompat.ACTION_PREPARE)) {
                    Callback.this.onPrepare();
                } else if (str.equals(MediaSessionCompat.ACTION_PREPARE_FROM_MEDIA_ID)) {
                    Callback.this.onPrepareFromMediaId(bundle.getString(MediaSessionCompat.ACTION_ARGUMENT_MEDIA_ID), bundle2);
                } else if (str.equals(MediaSessionCompat.ACTION_PREPARE_FROM_SEARCH)) {
                    Callback.this.onPrepareFromSearch(bundle.getString(MediaSessionCompat.ACTION_ARGUMENT_QUERY), bundle2);
                } else if (str.equals(MediaSessionCompat.ACTION_PREPARE_FROM_URI)) {
                    Callback.this.onPrepareFromUri((Uri) bundle.getParcelable(str2), bundle2);
                } else if (str.equals(MediaSessionCompat.ACTION_SET_CAPTIONING_ENABLED)) {
                    Callback.this.onSetCaptioningEnabled(bundle.getBoolean(MediaSessionCompat.ACTION_ARGUMENT_CAPTIONING_ENABLED));
                } else if (str.equals(MediaSessionCompat.ACTION_SET_REPEAT_MODE)) {
                    Callback.this.onSetRepeatMode(bundle.getInt(MediaSessionCompat.ACTION_ARGUMENT_REPEAT_MODE));
                } else if (str.equals(MediaSessionCompat.ACTION_SET_SHUFFLE_MODE)) {
                    Callback.this.onSetShuffleMode(bundle.getInt(MediaSessionCompat.ACTION_ARGUMENT_SHUFFLE_MODE));
                } else if (str.equals(MediaSessionCompat.ACTION_SET_RATING)) {
                    Callback.this.onSetRating((RatingCompat) bundle.getParcelable(MediaSessionCompat.ACTION_ARGUMENT_RATING), bundle2);
                } else if (str.equals(MediaSessionCompat.ACTION_SET_PLAYBACK_SPEED)) {
                    Callback.this.onSetPlaybackSpeed(bundle.getFloat(MediaSessionCompat.ACTION_ARGUMENT_PLAYBACK_SPEED, 1.0f));
                } else {
                    Callback.this.onCustomAction(str, bundle);
                }
                clearCurrentControllerInfo();
            }

            public void onFastForward() {
                setCurrentControllerInfo();
                Callback.this.onFastForward();
                clearCurrentControllerInfo();
            }

            public boolean onMediaButtonEvent(Intent intent) {
                setCurrentControllerInfo();
                boolean onMediaButtonEvent = Callback.this.onMediaButtonEvent(intent);
                clearCurrentControllerInfo();
                return onMediaButtonEvent || super.onMediaButtonEvent(intent);
            }

            public void onPause() {
                setCurrentControllerInfo();
                Callback.this.onPause();
                clearCurrentControllerInfo();
            }

            public void onPlay() {
                setCurrentControllerInfo();
                Callback.this.onPlay();
                clearCurrentControllerInfo();
            }

            public void onPlayFromMediaId(String str, Bundle bundle) {
                MediaSessionCompat.ensureClassLoader(bundle);
                setCurrentControllerInfo();
                Callback.this.onPlayFromMediaId(str, bundle);
                clearCurrentControllerInfo();
            }

            public void onPlayFromSearch(String str, Bundle bundle) {
                MediaSessionCompat.ensureClassLoader(bundle);
                setCurrentControllerInfo();
                Callback.this.onPlayFromSearch(str, bundle);
                clearCurrentControllerInfo();
            }

            @RequiresApi(23)
            public void onPlayFromUri(Uri uri, Bundle bundle) {
                MediaSessionCompat.ensureClassLoader(bundle);
                setCurrentControllerInfo();
                Callback.this.onPlayFromUri(uri, bundle);
                clearCurrentControllerInfo();
            }

            @RequiresApi(24)
            public void onPrepare() {
                setCurrentControllerInfo();
                Callback.this.onPrepare();
                clearCurrentControllerInfo();
            }

            @RequiresApi(24)
            public void onPrepareFromMediaId(String str, Bundle bundle) {
                MediaSessionCompat.ensureClassLoader(bundle);
                setCurrentControllerInfo();
                Callback.this.onPrepareFromMediaId(str, bundle);
                clearCurrentControllerInfo();
            }

            @RequiresApi(24)
            public void onPrepareFromSearch(String str, Bundle bundle) {
                MediaSessionCompat.ensureClassLoader(bundle);
                setCurrentControllerInfo();
                Callback.this.onPrepareFromSearch(str, bundle);
                clearCurrentControllerInfo();
            }

            @RequiresApi(24)
            public void onPrepareFromUri(Uri uri, Bundle bundle) {
                MediaSessionCompat.ensureClassLoader(bundle);
                setCurrentControllerInfo();
                Callback.this.onPrepareFromUri(uri, bundle);
                clearCurrentControllerInfo();
            }

            public void onRewind() {
                setCurrentControllerInfo();
                Callback.this.onRewind();
                clearCurrentControllerInfo();
            }

            public void onSeekTo(long j) {
                setCurrentControllerInfo();
                Callback.this.onSeekTo(j);
                clearCurrentControllerInfo();
            }

            public void onSetRating(Rating rating) {
                setCurrentControllerInfo();
                Callback.this.onSetRating(RatingCompat.fromRating(rating));
                clearCurrentControllerInfo();
            }

            public void onSetRating(Rating rating, Bundle bundle) {
            }

            public void onSkipToNext() {
                setCurrentControllerInfo();
                Callback.this.onSkipToNext();
                clearCurrentControllerInfo();
            }

            public void onSkipToPrevious() {
                setCurrentControllerInfo();
                Callback.this.onSkipToPrevious();
                clearCurrentControllerInfo();
            }

            public void onSkipToQueueItem(long j) {
                setCurrentControllerInfo();
                Callback.this.onSkipToQueueItem(j);
                clearCurrentControllerInfo();
            }

            public void onStop() {
                setCurrentControllerInfo();
                Callback.this.onStop();
                clearCurrentControllerInfo();
            }

            /* access modifiers changed from: 0000 */
            public void setCurrentControllerInfo() {
                if (VERSION.SDK_INT < 28) {
                    WeakReference weakReference = Callback.this.mSessionImpl;
                    MediaSessionImpl mediaSessionImpl = weakReference != null ? (MediaSessionImpl) weakReference.get() : null;
                    if (mediaSessionImpl != null) {
                        String callingPackage = mediaSessionImpl.getCallingPackage();
                        if (TextUtils.isEmpty(callingPackage)) {
                            callingPackage = RemoteUserInfo.LEGACY_CONTROLLER;
                        }
                        mediaSessionImpl.setCurrentControllerInfo(new RemoteUserInfo(callingPackage, -1, -1));
                    }
                }
            }
        }

        public Callback() {
            MediaSessionCallbackApi21 mediaSessionCallbackApi21 = null;
            if (VERSION.SDK_INT >= 21) {
                mediaSessionCallbackApi21 = new MediaSessionCallbackApi21();
            }
            this.mCallbackFwk = mediaSessionCallbackApi21;
        }

        /* access modifiers changed from: 0000 */
        public void handleMediaPlayPauseKeySingleTapIfPending() {
            if (this.mMediaPlayPauseKeyPending) {
                boolean z = false;
                this.mMediaPlayPauseKeyPending = false;
                this.mCallbackHandler.removeMessages(1);
                MediaSessionImpl mediaSessionImpl = (MediaSessionImpl) this.mSessionImpl.get();
                if (mediaSessionImpl != null) {
                    PlaybackStateCompat playbackState = mediaSessionImpl.getPlaybackState();
                    long actions = playbackState == null ? 0 : playbackState.getActions();
                    boolean z2 = playbackState != null && playbackState.getState() == 3;
                    boolean z3 = (516 & actions) != 0;
                    if ((actions & 514) != 0) {
                        z = true;
                    }
                    if (z2 && z) {
                        onPause();
                    } else if (!z2 && z3) {
                        onPlay();
                    }
                }
            }
        }

        public void onAddQueueItem(MediaDescriptionCompat mediaDescriptionCompat) {
        }

        public void onAddQueueItem(MediaDescriptionCompat mediaDescriptionCompat, int i) {
        }

        public void onCommand(String str, Bundle bundle, ResultReceiver resultReceiver) {
        }

        public void onCustomAction(String str, Bundle bundle) {
        }

        public void onFastForward() {
        }

        public boolean onMediaButtonEvent(Intent intent) {
            if (VERSION.SDK_INT >= 27) {
                return false;
            }
            MediaSessionImpl mediaSessionImpl = (MediaSessionImpl) this.mSessionImpl.get();
            if (!(mediaSessionImpl == null || this.mCallbackHandler == null)) {
                KeyEvent keyEvent = (KeyEvent) intent.getParcelableExtra("android.intent.extra.KEY_EVENT");
                if (keyEvent != null && keyEvent.getAction() == 0) {
                    RemoteUserInfo currentControllerInfo = mediaSessionImpl.getCurrentControllerInfo();
                    int keyCode = keyEvent.getKeyCode();
                    if (keyCode == 79 || keyCode == 85) {
                        if (keyEvent.getRepeatCount() > 0) {
                            handleMediaPlayPauseKeySingleTapIfPending();
                        } else if (this.mMediaPlayPauseKeyPending) {
                            this.mCallbackHandler.removeMessages(1);
                            this.mMediaPlayPauseKeyPending = false;
                            PlaybackStateCompat playbackState = mediaSessionImpl.getPlaybackState();
                            if (((playbackState == null ? 0 : playbackState.getActions()) & 32) != 0) {
                                onSkipToNext();
                            }
                        } else {
                            this.mMediaPlayPauseKeyPending = true;
                            CallbackHandler callbackHandler = this.mCallbackHandler;
                            callbackHandler.sendMessageDelayed(callbackHandler.obtainMessage(1, currentControllerInfo), (long) ViewConfiguration.getDoubleTapTimeout());
                        }
                        return true;
                    }
                    handleMediaPlayPauseKeySingleTapIfPending();
                    return false;
                }
            }
            return false;
        }

        public void onPause() {
        }

        public void onPlay() {
        }

        public void onPlayFromMediaId(String str, Bundle bundle) {
        }

        public void onPlayFromSearch(String str, Bundle bundle) {
        }

        public void onPlayFromUri(Uri uri, Bundle bundle) {
        }

        public void onPrepare() {
        }

        public void onPrepareFromMediaId(String str, Bundle bundle) {
        }

        public void onPrepareFromSearch(String str, Bundle bundle) {
        }

        public void onPrepareFromUri(Uri uri, Bundle bundle) {
        }

        public void onRemoveQueueItem(MediaDescriptionCompat mediaDescriptionCompat) {
        }

        @Deprecated
        public void onRemoveQueueItemAt(int i) {
        }

        public void onRewind() {
        }

        public void onSeekTo(long j) {
        }

        public void onSetCaptioningEnabled(boolean z) {
        }

        @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
        public void onSetPlaybackSpeed(float f) {
        }

        public void onSetRating(RatingCompat ratingCompat) {
        }

        public void onSetRating(RatingCompat ratingCompat, Bundle bundle) {
        }

        public void onSetRepeatMode(int i) {
        }

        public void onSetShuffleMode(int i) {
        }

        public void onSkipToNext() {
        }

        public void onSkipToPrevious() {
        }

        public void onSkipToQueueItem(long j) {
        }

        public void onStop() {
        }

        /* access modifiers changed from: 0000 */
        public void setSessionImpl(MediaSessionImpl mediaSessionImpl, Handler handler) {
            this.mSessionImpl = new WeakReference(mediaSessionImpl);
            CallbackHandler callbackHandler = this.mCallbackHandler;
            if (callbackHandler != null) {
                callbackHandler.removeCallbacksAndMessages(null);
            }
            this.mCallbackHandler = new CallbackHandler(handler.getLooper());
        }
    }

    interface MediaSessionImpl {
        String getCallingPackage();

        RemoteUserInfo getCurrentControllerInfo();

        Object getMediaSession();

        PlaybackStateCompat getPlaybackState();

        Object getRemoteControlClient();

        Token getSessionToken();

        boolean isActive();

        void release();

        void sendSessionEvent(String str, Bundle bundle);

        void setActive(boolean z);

        void setCallback(Callback callback, Handler handler);

        void setCaptioningEnabled(boolean z);

        void setCurrentControllerInfo(RemoteUserInfo remoteUserInfo);

        void setExtras(Bundle bundle);

        void setFlags(int i);

        void setMediaButtonReceiver(PendingIntent pendingIntent);

        void setMetadata(MediaMetadataCompat mediaMetadataCompat);

        void setPlaybackState(PlaybackStateCompat playbackStateCompat);

        void setPlaybackToLocal(int i);

        void setPlaybackToRemote(VolumeProviderCompat volumeProviderCompat);

        void setQueue(List list);

        void setQueueTitle(CharSequence charSequence);

        void setRatingType(int i);

        void setRepeatMode(int i);

        void setSessionActivity(PendingIntent pendingIntent);

        void setShuffleMode(int i);
    }

    @RequiresApi(18)
    class MediaSessionImplApi18 extends MediaSessionImplBase {
        private static boolean sIsMbrPendingIntentSupported = true;

        MediaSessionImplApi18(Context context, String str, ComponentName componentName, PendingIntent pendingIntent) {
            super(context, str, componentName, pendingIntent);
        }

        /* access modifiers changed from: 0000 */
        public int getRccTransportControlFlagsFromActions(long j) {
            int rccTransportControlFlagsFromActions = super.getRccTransportControlFlagsFromActions(j);
            return (j & 256) != 0 ? rccTransportControlFlagsFromActions | 256 : rccTransportControlFlagsFromActions;
        }

        /* access modifiers changed from: 0000 */
        public void registerMediaButtonEventReceiver(PendingIntent pendingIntent, ComponentName componentName) {
            if (sIsMbrPendingIntentSupported) {
                try {
                    this.mAudioManager.registerMediaButtonEventReceiver(pendingIntent);
                } catch (NullPointerException unused) {
                    Log.w(MediaSessionCompat.TAG, "Unable to register media button event receiver with PendingIntent, falling back to ComponentName.");
                    sIsMbrPendingIntentSupported = false;
                }
            }
            if (!sIsMbrPendingIntentSupported) {
                super.registerMediaButtonEventReceiver(pendingIntent, componentName);
            }
        }

        public void setCallback(Callback callback, Handler handler) {
            AnonymousClass1 r1;
            RemoteControlClient remoteControlClient;
            super.setCallback(callback, handler);
            if (callback == null) {
                remoteControlClient = this.mRcc;
                r1 = null;
            } else {
                r1 = new OnPlaybackPositionUpdateListener() {
                    public void onPlaybackPositionUpdate(long j) {
                        MediaSessionImplApi18.this.postToHandler(18, -1, -1, Long.valueOf(j), null);
                    }
                };
                remoteControlClient = this.mRcc;
            }
            remoteControlClient.setPlaybackPositionUpdateListener(r1);
        }

        /* access modifiers changed from: 0000 */
        public void setRccState(PlaybackStateCompat playbackStateCompat) {
            long position = playbackStateCompat.getPosition();
            float playbackSpeed = playbackStateCompat.getPlaybackSpeed();
            long lastPositionUpdateTime = playbackStateCompat.getLastPositionUpdateTime();
            long elapsedRealtime = SystemClock.elapsedRealtime();
            if (playbackStateCompat.getState() == 3) {
                long j = 0;
                if (position > 0) {
                    if (lastPositionUpdateTime > 0) {
                        j = elapsedRealtime - lastPositionUpdateTime;
                        if (playbackSpeed > 0.0f && playbackSpeed != 1.0f) {
                            j = (long) (((float) j) * playbackSpeed);
                        }
                    }
                    position += j;
                }
            }
            this.mRcc.setPlaybackState(getRccStateFromState(playbackStateCompat.getState()), position, playbackSpeed);
        }

        /* access modifiers changed from: 0000 */
        public void unregisterMediaButtonEventReceiver(PendingIntent pendingIntent, ComponentName componentName) {
            if (sIsMbrPendingIntentSupported) {
                this.mAudioManager.unregisterMediaButtonEventReceiver(pendingIntent);
            } else {
                super.unregisterMediaButtonEventReceiver(pendingIntent, componentName);
            }
        }
    }

    @RequiresApi(19)
    class MediaSessionImplApi19 extends MediaSessionImplApi18 {
        MediaSessionImplApi19(Context context, String str, ComponentName componentName, PendingIntent pendingIntent) {
            super(context, str, componentName, pendingIntent);
        }

        /* access modifiers changed from: 0000 */
        public MetadataEditor buildRccMetadata(Bundle bundle) {
            MetadataEditor buildRccMetadata = super.buildRccMetadata(bundle);
            PlaybackStateCompat playbackStateCompat = this.mState;
            if (((playbackStateCompat == null ? 0 : playbackStateCompat.getActions()) & 128) != 0) {
                buildRccMetadata.addEditableKey(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_TAP_LIGHT);
            }
            if (bundle == null) {
                return buildRccMetadata;
            }
            String str = MediaMetadataCompat.METADATA_KEY_YEAR;
            if (bundle.containsKey(str)) {
                buildRccMetadata.putLong(8, bundle.getLong(str));
            }
            String str2 = MediaMetadataCompat.METADATA_KEY_RATING;
            if (bundle.containsKey(str2)) {
                buildRccMetadata.putObject(101, bundle.getParcelable(str2));
            }
            String str3 = MediaMetadataCompat.METADATA_KEY_USER_RATING;
            if (bundle.containsKey(str3)) {
                buildRccMetadata.putObject(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_TAP_LIGHT, bundle.getParcelable(str3));
            }
            return buildRccMetadata;
        }

        /* access modifiers changed from: 0000 */
        public int getRccTransportControlFlagsFromActions(long j) {
            int rccTransportControlFlagsFromActions = super.getRccTransportControlFlagsFromActions(j);
            return (j & 128) != 0 ? rccTransportControlFlagsFromActions | 512 : rccTransportControlFlagsFromActions;
        }

        public void setCallback(Callback callback, Handler handler) {
            AnonymousClass1 r1;
            RemoteControlClient remoteControlClient;
            super.setCallback(callback, handler);
            if (callback == null) {
                remoteControlClient = this.mRcc;
                r1 = null;
            } else {
                r1 = new OnMetadataUpdateListener() {
                    public void onMetadataUpdate(int i, Object obj) {
                        if (i == 268435457 && (obj instanceof Rating)) {
                            MediaSessionImplApi19.this.postToHandler(19, -1, -1, RatingCompat.fromRating(obj), null);
                        }
                    }
                };
                remoteControlClient = this.mRcc;
            }
            remoteControlClient.setMetadataUpdateListener(r1);
        }
    }

    @RequiresApi(21)
    class MediaSessionImplApi21 implements MediaSessionImpl {
        boolean mCaptioningEnabled;
        boolean mDestroyed = false;
        final RemoteCallbackList mExtraControllerCallbacks = new RemoteCallbackList();
        final Object mLock = new Object();
        MediaMetadataCompat mMetadata;
        PlaybackStateCompat mPlaybackState;
        List mQueue;
        int mRatingType;
        @GuardedBy("mLock")
        RemoteUserInfo mRemoteUserInfo;
        int mRepeatMode;
        final MediaSession mSessionFwk;
        int mShuffleMode;
        final Token mToken;

        class ExtraSession extends Stub {
            ExtraSession() {
            }

            public void addQueueItem(MediaDescriptionCompat mediaDescriptionCompat) {
                throw new AssertionError();
            }

            public void addQueueItemAt(MediaDescriptionCompat mediaDescriptionCompat, int i) {
                throw new AssertionError();
            }

            public void adjustVolume(int i, int i2, String str) {
                throw new AssertionError();
            }

            public void fastForward() {
                throw new AssertionError();
            }

            public Bundle getExtras() {
                throw new AssertionError();
            }

            public long getFlags() {
                throw new AssertionError();
            }

            public PendingIntent getLaunchPendingIntent() {
                throw new AssertionError();
            }

            public MediaMetadataCompat getMetadata() {
                throw new AssertionError();
            }

            public String getPackageName() {
                throw new AssertionError();
            }

            public PlaybackStateCompat getPlaybackState() {
                MediaSessionImplApi21 mediaSessionImplApi21 = MediaSessionImplApi21.this;
                return MediaSessionCompat.getStateWithUpdatedPosition(mediaSessionImplApi21.mPlaybackState, mediaSessionImplApi21.mMetadata);
            }

            public List getQueue() {
                return null;
            }

            public CharSequence getQueueTitle() {
                throw new AssertionError();
            }

            public int getRatingType() {
                return MediaSessionImplApi21.this.mRatingType;
            }

            public int getRepeatMode() {
                return MediaSessionImplApi21.this.mRepeatMode;
            }

            public int getShuffleMode() {
                return MediaSessionImplApi21.this.mShuffleMode;
            }

            public String getTag() {
                throw new AssertionError();
            }

            public ParcelableVolumeInfo getVolumeAttributes() {
                throw new AssertionError();
            }

            public boolean isCaptioningEnabled() {
                return MediaSessionImplApi21.this.mCaptioningEnabled;
            }

            public boolean isShuffleModeEnabledRemoved() {
                return false;
            }

            public boolean isTransportControlEnabled() {
                throw new AssertionError();
            }

            public void next() {
                throw new AssertionError();
            }

            public void pause() {
                throw new AssertionError();
            }

            public void play() {
                throw new AssertionError();
            }

            public void playFromMediaId(String str, Bundle bundle) {
                throw new AssertionError();
            }

            public void playFromSearch(String str, Bundle bundle) {
                throw new AssertionError();
            }

            public void playFromUri(Uri uri, Bundle bundle) {
                throw new AssertionError();
            }

            public void prepare() {
                throw new AssertionError();
            }

            public void prepareFromMediaId(String str, Bundle bundle) {
                throw new AssertionError();
            }

            public void prepareFromSearch(String str, Bundle bundle) {
                throw new AssertionError();
            }

            public void prepareFromUri(Uri uri, Bundle bundle) {
                throw new AssertionError();
            }

            public void previous() {
                throw new AssertionError();
            }

            public void rate(RatingCompat ratingCompat) {
                throw new AssertionError();
            }

            public void rateWithExtras(RatingCompat ratingCompat, Bundle bundle) {
                throw new AssertionError();
            }

            public void registerCallbackListener(IMediaControllerCallback iMediaControllerCallback) {
                if (!MediaSessionImplApi21.this.mDestroyed) {
                    MediaSessionImplApi21.this.mExtraControllerCallbacks.register(iMediaControllerCallback, new RemoteUserInfo(RemoteUserInfo.LEGACY_CONTROLLER, Binder.getCallingPid(), Binder.getCallingUid()));
                }
            }

            public void removeQueueItem(MediaDescriptionCompat mediaDescriptionCompat) {
                throw new AssertionError();
            }

            public void removeQueueItemAt(int i) {
                throw new AssertionError();
            }

            public void rewind() {
                throw new AssertionError();
            }

            public void seekTo(long j) {
                throw new AssertionError();
            }

            public void sendCommand(String str, Bundle bundle, ResultReceiverWrapper resultReceiverWrapper) {
                throw new AssertionError();
            }

            public void sendCustomAction(String str, Bundle bundle) {
                throw new AssertionError();
            }

            public boolean sendMediaButton(KeyEvent keyEvent) {
                throw new AssertionError();
            }

            public void setCaptioningEnabled(boolean z) {
                throw new AssertionError();
            }

            public void setPlaybackSpeed(float f) {
                throw new AssertionError();
            }

            public void setRepeatMode(int i) {
                throw new AssertionError();
            }

            public void setShuffleMode(int i) {
                throw new AssertionError();
            }

            public void setShuffleModeEnabledRemoved(boolean z) {
            }

            public void setVolumeTo(int i, int i2, String str) {
                throw new AssertionError();
            }

            public void skipToQueueItem(long j) {
                throw new AssertionError();
            }

            public void stop() {
                throw new AssertionError();
            }

            public void unregisterCallbackListener(IMediaControllerCallback iMediaControllerCallback) {
                MediaSessionImplApi21.this.mExtraControllerCallbacks.unregister(iMediaControllerCallback);
            }
        }

        MediaSessionImplApi21(Context context, String str, VersionedParcelable versionedParcelable) {
            this.mSessionFwk = new MediaSession(context, str);
            this.mToken = new Token(this.mSessionFwk.getSessionToken(), new ExtraSession(), versionedParcelable);
            setFlags(3);
        }

        MediaSessionImplApi21(Object obj) {
            if (obj instanceof MediaSession) {
                this.mSessionFwk = (MediaSession) obj;
                this.mToken = new Token(this.mSessionFwk.getSessionToken(), new ExtraSession());
                setFlags(3);
                return;
            }
            throw new IllegalArgumentException("mediaSession is not a valid MediaSession object");
        }

        public String getCallingPackage() {
            if (VERSION.SDK_INT < 24) {
                return null;
            }
            try {
                return (String) this.mSessionFwk.getClass().getMethod("getCallingPackage", new Class[0]).invoke(this.mSessionFwk, new Object[0]);
            } catch (Exception e) {
                Log.e(MediaSessionCompat.TAG, "Cannot execute MediaSession.getCallingPackage()", e);
                return null;
            }
        }

        public RemoteUserInfo getCurrentControllerInfo() {
            RemoteUserInfo remoteUserInfo;
            synchronized (this.mLock) {
                remoteUserInfo = this.mRemoteUserInfo;
            }
            return remoteUserInfo;
        }

        public Object getMediaSession() {
            return this.mSessionFwk;
        }

        public PlaybackStateCompat getPlaybackState() {
            return this.mPlaybackState;
        }

        public Object getRemoteControlClient() {
            return null;
        }

        public Token getSessionToken() {
            return this.mToken;
        }

        public boolean isActive() {
            return this.mSessionFwk.isActive();
        }

        public void release() {
            this.mDestroyed = true;
            this.mSessionFwk.release();
        }

        public void sendSessionEvent(String str, Bundle bundle) {
            if (VERSION.SDK_INT < 23) {
                for (int beginBroadcast = this.mExtraControllerCallbacks.beginBroadcast() - 1; beginBroadcast >= 0; beginBroadcast--) {
                    try {
                        ((IMediaControllerCallback) this.mExtraControllerCallbacks.getBroadcastItem(beginBroadcast)).onEvent(str, bundle);
                    } catch (RemoteException unused) {
                    }
                }
                this.mExtraControllerCallbacks.finishBroadcast();
            }
            this.mSessionFwk.sendSessionEvent(str, bundle);
        }

        public void setActive(boolean z) {
            this.mSessionFwk.setActive(z);
        }

        public void setCallback(Callback callback, Handler handler) {
            this.mSessionFwk.setCallback(callback == null ? null : callback.mCallbackFwk, handler);
            if (callback != null) {
                callback.setSessionImpl(this, handler);
            }
        }

        public void setCaptioningEnabled(boolean z) {
            if (this.mCaptioningEnabled != z) {
                this.mCaptioningEnabled = z;
                for (int beginBroadcast = this.mExtraControllerCallbacks.beginBroadcast() - 1; beginBroadcast >= 0; beginBroadcast--) {
                    try {
                        ((IMediaControllerCallback) this.mExtraControllerCallbacks.getBroadcastItem(beginBroadcast)).onCaptioningEnabledChanged(z);
                    } catch (RemoteException unused) {
                    }
                }
                this.mExtraControllerCallbacks.finishBroadcast();
            }
        }

        public void setCurrentControllerInfo(RemoteUserInfo remoteUserInfo) {
            synchronized (this.mLock) {
                this.mRemoteUserInfo = remoteUserInfo;
            }
        }

        public void setExtras(Bundle bundle) {
            this.mSessionFwk.setExtras(bundle);
        }

        @SuppressLint({"WrongConstant"})
        public void setFlags(int i) {
            this.mSessionFwk.setFlags(i | 1 | 2);
        }

        public void setMediaButtonReceiver(PendingIntent pendingIntent) {
            this.mSessionFwk.setMediaButtonReceiver(pendingIntent);
        }

        public void setMetadata(MediaMetadataCompat mediaMetadataCompat) {
            this.mMetadata = mediaMetadataCompat;
            this.mSessionFwk.setMetadata(mediaMetadataCompat == null ? null : (MediaMetadata) mediaMetadataCompat.getMediaMetadata());
        }

        public void setPlaybackState(PlaybackStateCompat playbackStateCompat) {
            this.mPlaybackState = playbackStateCompat;
            for (int beginBroadcast = this.mExtraControllerCallbacks.beginBroadcast() - 1; beginBroadcast >= 0; beginBroadcast--) {
                try {
                    ((IMediaControllerCallback) this.mExtraControllerCallbacks.getBroadcastItem(beginBroadcast)).onPlaybackStateChanged(playbackStateCompat);
                } catch (RemoteException unused) {
                }
            }
            this.mExtraControllerCallbacks.finishBroadcast();
            this.mSessionFwk.setPlaybackState(playbackStateCompat == null ? null : (PlaybackState) playbackStateCompat.getPlaybackState());
        }

        public void setPlaybackToLocal(int i) {
            Builder builder = new Builder();
            builder.setLegacyStreamType(i);
            this.mSessionFwk.setPlaybackToLocal(builder.build());
        }

        public void setPlaybackToRemote(VolumeProviderCompat volumeProviderCompat) {
            this.mSessionFwk.setPlaybackToRemote((VolumeProvider) volumeProviderCompat.getVolumeProvider());
        }

        /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<android.support.v4.media.session.MediaSessionCompat$QueueItem>, for r3v0, types: [java.util.List, java.util.List<android.support.v4.media.session.MediaSessionCompat$QueueItem>] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void setQueue(List<QueueItem> list) {
            this.mQueue = list;
            if (list == null) {
                this.mSessionFwk.setQueue(null);
                return;
            }
            ArrayList arrayList = new ArrayList();
            for (QueueItem queueItem : list) {
                arrayList.add((android.media.session.MediaSession.QueueItem) queueItem.getQueueItem());
            }
            this.mSessionFwk.setQueue(arrayList);
        }

        public void setQueueTitle(CharSequence charSequence) {
            this.mSessionFwk.setQueueTitle(charSequence);
        }

        public void setRatingType(int i) {
            if (VERSION.SDK_INT < 22) {
                this.mRatingType = i;
            } else {
                this.mSessionFwk.setRatingType(i);
            }
        }

        public void setRepeatMode(int i) {
            if (this.mRepeatMode != i) {
                this.mRepeatMode = i;
                for (int beginBroadcast = this.mExtraControllerCallbacks.beginBroadcast() - 1; beginBroadcast >= 0; beginBroadcast--) {
                    try {
                        ((IMediaControllerCallback) this.mExtraControllerCallbacks.getBroadcastItem(beginBroadcast)).onRepeatModeChanged(i);
                    } catch (RemoteException unused) {
                    }
                }
                this.mExtraControllerCallbacks.finishBroadcast();
            }
        }

        public void setSessionActivity(PendingIntent pendingIntent) {
            this.mSessionFwk.setSessionActivity(pendingIntent);
        }

        public void setShuffleMode(int i) {
            if (this.mShuffleMode != i) {
                this.mShuffleMode = i;
                for (int beginBroadcast = this.mExtraControllerCallbacks.beginBroadcast() - 1; beginBroadcast >= 0; beginBroadcast--) {
                    try {
                        ((IMediaControllerCallback) this.mExtraControllerCallbacks.getBroadcastItem(beginBroadcast)).onShuffleModeChanged(i);
                    } catch (RemoteException unused) {
                    }
                }
                this.mExtraControllerCallbacks.finishBroadcast();
            }
        }
    }

    @RequiresApi(28)
    class MediaSessionImplApi28 extends MediaSessionImplApi21 {
        MediaSessionImplApi28(Context context, String str, VersionedParcelable versionedParcelable) {
            super(context, str, versionedParcelable);
        }

        MediaSessionImplApi28(Object obj) {
            super(obj);
        }

        @NonNull
        public final RemoteUserInfo getCurrentControllerInfo() {
            return new RemoteUserInfo(this.mSessionFwk.getCurrentControllerInfo());
        }

        public void setCurrentControllerInfo(RemoteUserInfo remoteUserInfo) {
        }
    }

    class MediaSessionImplBase implements MediaSessionImpl {
        static final int RCC_PLAYSTATE_NONE = 0;
        final AudioManager mAudioManager;
        volatile Callback mCallback;
        boolean mCaptioningEnabled;
        private final Context mContext;
        final RemoteCallbackList mControllerCallbacks = new RemoteCallbackList();
        boolean mDestroyed = false;
        Bundle mExtras;
        int mFlags = 3;
        private MessageHandler mHandler;
        boolean mIsActive = false;
        int mLocalStream;
        final Object mLock = new Object();
        private final ComponentName mMediaButtonReceiverComponentName;
        private final PendingIntent mMediaButtonReceiverIntent;
        MediaMetadataCompat mMetadata;
        final String mPackageName;
        List mQueue;
        CharSequence mQueueTitle;
        int mRatingType;
        final RemoteControlClient mRcc;
        private RemoteUserInfo mRemoteUserInfo;
        int mRepeatMode;
        PendingIntent mSessionActivity;
        int mShuffleMode;
        PlaybackStateCompat mState;
        private final MediaSessionStub mStub;
        final String mTag;
        private final Token mToken;
        private androidx.media.VolumeProviderCompat.Callback mVolumeCallback = new androidx.media.VolumeProviderCompat.Callback() {
            public void onVolumeChanged(VolumeProviderCompat volumeProviderCompat) {
                MediaSessionImplBase mediaSessionImplBase = MediaSessionImplBase.this;
                if (mediaSessionImplBase.mVolumeProvider == volumeProviderCompat) {
                    ParcelableVolumeInfo parcelableVolumeInfo = new ParcelableVolumeInfo(mediaSessionImplBase.mVolumeType, mediaSessionImplBase.mLocalStream, volumeProviderCompat.getVolumeControl(), volumeProviderCompat.getMaxVolume(), volumeProviderCompat.getCurrentVolume());
                    MediaSessionImplBase.this.sendVolumeInfoChanged(parcelableVolumeInfo);
                }
            }
        };
        VolumeProviderCompat mVolumeProvider;
        int mVolumeType;

        final class Command {
            public final String command;
            public final Bundle extras;
            public final ResultReceiver stub;

            public Command(String str, Bundle bundle, ResultReceiver resultReceiver) {
                this.command = str;
                this.extras = bundle;
                this.stub = resultReceiver;
            }
        }

        class MediaSessionStub extends Stub {
            MediaSessionStub() {
            }

            public void addQueueItem(MediaDescriptionCompat mediaDescriptionCompat) {
                postToHandler(25, (Object) mediaDescriptionCompat);
            }

            public void addQueueItemAt(MediaDescriptionCompat mediaDescriptionCompat, int i) {
                postToHandler(26, (Object) mediaDescriptionCompat, i);
            }

            public void adjustVolume(int i, int i2, String str) {
                MediaSessionImplBase.this.adjustVolume(i, i2);
            }

            public void fastForward() {
                postToHandler(16);
            }

            public Bundle getExtras() {
                Bundle bundle;
                synchronized (MediaSessionImplBase.this.mLock) {
                    bundle = MediaSessionImplBase.this.mExtras;
                }
                return bundle;
            }

            public long getFlags() {
                long j;
                synchronized (MediaSessionImplBase.this.mLock) {
                    j = (long) MediaSessionImplBase.this.mFlags;
                }
                return j;
            }

            public PendingIntent getLaunchPendingIntent() {
                PendingIntent pendingIntent;
                synchronized (MediaSessionImplBase.this.mLock) {
                    pendingIntent = MediaSessionImplBase.this.mSessionActivity;
                }
                return pendingIntent;
            }

            public MediaMetadataCompat getMetadata() {
                return MediaSessionImplBase.this.mMetadata;
            }

            public String getPackageName() {
                return MediaSessionImplBase.this.mPackageName;
            }

            public PlaybackStateCompat getPlaybackState() {
                PlaybackStateCompat playbackStateCompat;
                MediaMetadataCompat mediaMetadataCompat;
                synchronized (MediaSessionImplBase.this.mLock) {
                    playbackStateCompat = MediaSessionImplBase.this.mState;
                    mediaMetadataCompat = MediaSessionImplBase.this.mMetadata;
                }
                return MediaSessionCompat.getStateWithUpdatedPosition(playbackStateCompat, mediaMetadataCompat);
            }

            public List getQueue() {
                List list;
                synchronized (MediaSessionImplBase.this.mLock) {
                    list = MediaSessionImplBase.this.mQueue;
                }
                return list;
            }

            public CharSequence getQueueTitle() {
                return MediaSessionImplBase.this.mQueueTitle;
            }

            public int getRatingType() {
                return MediaSessionImplBase.this.mRatingType;
            }

            public int getRepeatMode() {
                return MediaSessionImplBase.this.mRepeatMode;
            }

            public int getShuffleMode() {
                return MediaSessionImplBase.this.mShuffleMode;
            }

            public String getTag() {
                return MediaSessionImplBase.this.mTag;
            }

            public ParcelableVolumeInfo getVolumeAttributes() {
                int i;
                int i2;
                int i3;
                int i4;
                int i5;
                synchronized (MediaSessionImplBase.this.mLock) {
                    i = MediaSessionImplBase.this.mVolumeType;
                    i2 = MediaSessionImplBase.this.mLocalStream;
                    VolumeProviderCompat volumeProviderCompat = MediaSessionImplBase.this.mVolumeProvider;
                    if (i == 2) {
                        int volumeControl = volumeProviderCompat.getVolumeControl();
                        int maxVolume = volumeProviderCompat.getMaxVolume();
                        i5 = volumeControl;
                        i3 = volumeProviderCompat.getCurrentVolume();
                        i4 = maxVolume;
                    } else {
                        int streamMaxVolume = MediaSessionImplBase.this.mAudioManager.getStreamMaxVolume(i2);
                        i3 = MediaSessionImplBase.this.mAudioManager.getStreamVolume(i2);
                        i4 = streamMaxVolume;
                        i5 = 2;
                    }
                }
                ParcelableVolumeInfo parcelableVolumeInfo = new ParcelableVolumeInfo(i, i2, i5, i4, i3);
                return parcelableVolumeInfo;
            }

            public boolean isCaptioningEnabled() {
                return MediaSessionImplBase.this.mCaptioningEnabled;
            }

            public boolean isShuffleModeEnabledRemoved() {
                return false;
            }

            public boolean isTransportControlEnabled() {
                return true;
            }

            public void next() {
                postToHandler(14);
            }

            public void pause() {
                postToHandler(12);
            }

            public void play() {
                postToHandler(7);
            }

            public void playFromMediaId(String str, Bundle bundle) {
                postToHandler(8, (Object) str, bundle);
            }

            public void playFromSearch(String str, Bundle bundle) {
                postToHandler(9, (Object) str, bundle);
            }

            public void playFromUri(Uri uri, Bundle bundle) {
                postToHandler(10, (Object) uri, bundle);
            }

            /* access modifiers changed from: 0000 */
            public void postToHandler(int i) {
                MediaSessionImplBase.this.postToHandler(i, 0, 0, null, null);
            }

            /* access modifiers changed from: 0000 */
            public void postToHandler(int i, int i2) {
                MediaSessionImplBase.this.postToHandler(i, i2, 0, null, null);
            }

            /* access modifiers changed from: 0000 */
            public void postToHandler(int i, Object obj) {
                MediaSessionImplBase.this.postToHandler(i, 0, 0, obj, null);
            }

            /* access modifiers changed from: 0000 */
            public void postToHandler(int i, Object obj, int i2) {
                MediaSessionImplBase.this.postToHandler(i, i2, 0, obj, null);
            }

            /* access modifiers changed from: 0000 */
            public void postToHandler(int i, Object obj, Bundle bundle) {
                MediaSessionImplBase.this.postToHandler(i, 0, 0, obj, bundle);
            }

            public void prepare() {
                postToHandler(3);
            }

            public void prepareFromMediaId(String str, Bundle bundle) {
                postToHandler(4, (Object) str, bundle);
            }

            public void prepareFromSearch(String str, Bundle bundle) {
                postToHandler(5, (Object) str, bundle);
            }

            public void prepareFromUri(Uri uri, Bundle bundle) {
                postToHandler(6, (Object) uri, bundle);
            }

            public void previous() {
                postToHandler(15);
            }

            public void rate(RatingCompat ratingCompat) {
                postToHandler(19, (Object) ratingCompat);
            }

            public void rateWithExtras(RatingCompat ratingCompat, Bundle bundle) {
                postToHandler(31, (Object) ratingCompat, bundle);
            }

            public void registerCallbackListener(IMediaControllerCallback iMediaControllerCallback) {
                if (MediaSessionImplBase.this.mDestroyed) {
                    try {
                        iMediaControllerCallback.onSessionDestroyed();
                    } catch (Exception unused) {
                    }
                    return;
                }
                MediaSessionImplBase.this.mControllerCallbacks.register(iMediaControllerCallback, new RemoteUserInfo(MediaSessionImplBase.this.getPackageNameForUid(Binder.getCallingUid()), Binder.getCallingPid(), Binder.getCallingUid()));
            }

            public void removeQueueItem(MediaDescriptionCompat mediaDescriptionCompat) {
                postToHandler(27, (Object) mediaDescriptionCompat);
            }

            public void removeQueueItemAt(int i) {
                postToHandler(28, i);
            }

            public void rewind() {
                postToHandler(17);
            }

            public void seekTo(long j) {
                postToHandler(18, (Object) Long.valueOf(j));
            }

            public void sendCommand(String str, Bundle bundle, ResultReceiverWrapper resultReceiverWrapper) {
                postToHandler(1, (Object) new Command(str, bundle, resultReceiverWrapper == null ? null : resultReceiverWrapper.mResultReceiver));
            }

            public void sendCustomAction(String str, Bundle bundle) {
                postToHandler(20, (Object) str, bundle);
            }

            public boolean sendMediaButton(KeyEvent keyEvent) {
                postToHandler(21, (Object) keyEvent);
                return true;
            }

            public void setCaptioningEnabled(boolean z) {
                postToHandler(29, (Object) Boolean.valueOf(z));
            }

            public void setPlaybackSpeed(float f) {
                postToHandler(32, (Object) Float.valueOf(f));
            }

            public void setRepeatMode(int i) {
                postToHandler(23, i);
            }

            public void setShuffleMode(int i) {
                postToHandler(30, i);
            }

            public void setShuffleModeEnabledRemoved(boolean z) {
            }

            public void setVolumeTo(int i, int i2, String str) {
                MediaSessionImplBase.this.setVolumeTo(i, i2);
            }

            public void skipToQueueItem(long j) {
                postToHandler(11, (Object) Long.valueOf(j));
            }

            public void stop() {
                postToHandler(13);
            }

            public void unregisterCallbackListener(IMediaControllerCallback iMediaControllerCallback) {
                MediaSessionImplBase.this.mControllerCallbacks.unregister(iMediaControllerCallback);
            }
        }

        class MessageHandler extends Handler {
            private static final int KEYCODE_MEDIA_PAUSE = 127;
            private static final int KEYCODE_MEDIA_PLAY = 126;
            private static final int MSG_ADD_QUEUE_ITEM = 25;
            private static final int MSG_ADD_QUEUE_ITEM_AT = 26;
            private static final int MSG_ADJUST_VOLUME = 2;
            private static final int MSG_COMMAND = 1;
            private static final int MSG_CUSTOM_ACTION = 20;
            private static final int MSG_FAST_FORWARD = 16;
            private static final int MSG_MEDIA_BUTTON = 21;
            private static final int MSG_NEXT = 14;
            private static final int MSG_PAUSE = 12;
            private static final int MSG_PLAY = 7;
            private static final int MSG_PLAY_MEDIA_ID = 8;
            private static final int MSG_PLAY_SEARCH = 9;
            private static final int MSG_PLAY_URI = 10;
            private static final int MSG_PREPARE = 3;
            private static final int MSG_PREPARE_MEDIA_ID = 4;
            private static final int MSG_PREPARE_SEARCH = 5;
            private static final int MSG_PREPARE_URI = 6;
            private static final int MSG_PREVIOUS = 15;
            private static final int MSG_RATE = 19;
            private static final int MSG_RATE_EXTRA = 31;
            private static final int MSG_REMOVE_QUEUE_ITEM = 27;
            private static final int MSG_REMOVE_QUEUE_ITEM_AT = 28;
            private static final int MSG_REWIND = 17;
            private static final int MSG_SEEK_TO = 18;
            private static final int MSG_SET_CAPTIONING_ENABLED = 29;
            private static final int MSG_SET_PLAYBACK_SPEED = 32;
            private static final int MSG_SET_REPEAT_MODE = 23;
            private static final int MSG_SET_SHUFFLE_MODE = 30;
            private static final int MSG_SET_VOLUME = 22;
            private static final int MSG_SKIP_TO_ITEM = 11;
            private static final int MSG_STOP = 13;

            public MessageHandler(Looper looper) {
                super(looper);
            }

            private void onMediaButtonEvent(KeyEvent keyEvent, Callback callback) {
                if (keyEvent != null && keyEvent.getAction() == 0) {
                    PlaybackStateCompat playbackStateCompat = MediaSessionImplBase.this.mState;
                    long actions = playbackStateCompat == null ? 0 : playbackStateCompat.getActions();
                    int keyCode = keyEvent.getKeyCode();
                    if (keyCode != 79) {
                        if (keyCode != 126) {
                            if (keyCode != 127) {
                                switch (keyCode) {
                                    case 85:
                                        break;
                                    case 86:
                                        if ((1 & actions) != 0) {
                                            callback.onStop();
                                            return;
                                        }
                                        return;
                                    case 87:
                                        if ((32 & actions) != 0) {
                                            callback.onSkipToNext();
                                            return;
                                        }
                                        return;
                                    case 88:
                                        if ((16 & actions) != 0) {
                                            callback.onSkipToPrevious();
                                            return;
                                        }
                                        return;
                                    case 89:
                                        if ((8 & actions) != 0) {
                                            callback.onRewind();
                                            return;
                                        }
                                        return;
                                    case 90:
                                        if ((64 & actions) != 0) {
                                            callback.onFastForward();
                                            return;
                                        }
                                        return;
                                    default:
                                        return;
                                }
                            } else if ((2 & actions) != 0) {
                                callback.onPause();
                                return;
                            } else {
                                return;
                            }
                        } else if ((4 & actions) != 0) {
                            callback.onPlay();
                            return;
                        } else {
                            return;
                        }
                    }
                    Log.w(MediaSessionCompat.TAG, "KEYCODE_MEDIA_PLAY_PAUSE and KEYCODE_HEADSETHOOK are handled already");
                }
            }

            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void handleMessage(Message message) {
                MediaDescriptionCompat mediaDescriptionCompat;
                Callback callback = MediaSessionImplBase.this.mCallback;
                if (callback != null) {
                    Bundle data = message.getData();
                    MediaSessionCompat.ensureClassLoader(data);
                    MediaSessionImplBase.this.setCurrentControllerInfo(new RemoteUserInfo(data.getString(MediaSessionCompat.DATA_CALLING_PACKAGE), data.getInt("data_calling_pid"), data.getInt("data_calling_uid")));
                    Bundle bundle = data.getBundle(MediaSessionCompat.DATA_EXTRAS);
                    MediaSessionCompat.ensureClassLoader(bundle);
                    try {
                        switch (message.what) {
                            case 1:
                                Command command = (Command) message.obj;
                                callback.onCommand(command.command, command.extras, command.stub);
                                break;
                            case 2:
                                MediaSessionImplBase.this.adjustVolume(message.arg1, 0);
                                break;
                            case 3:
                                callback.onPrepare();
                                break;
                            case 4:
                                callback.onPrepareFromMediaId((String) message.obj, bundle);
                                break;
                            case 5:
                                callback.onPrepareFromSearch((String) message.obj, bundle);
                                break;
                            case 6:
                                callback.onPrepareFromUri((Uri) message.obj, bundle);
                                break;
                            case 7:
                                callback.onPlay();
                                break;
                            case 8:
                                callback.onPlayFromMediaId((String) message.obj, bundle);
                                break;
                            case 9:
                                callback.onPlayFromSearch((String) message.obj, bundle);
                                break;
                            case 10:
                                callback.onPlayFromUri((Uri) message.obj, bundle);
                                break;
                            case 11:
                                callback.onSkipToQueueItem(((Long) message.obj).longValue());
                                break;
                            case 12:
                                callback.onPause();
                                break;
                            case 13:
                                callback.onStop();
                                break;
                            case 14:
                                callback.onSkipToNext();
                                break;
                            case 15:
                                callback.onSkipToPrevious();
                                break;
                            case 16:
                                callback.onFastForward();
                                break;
                            case 17:
                                callback.onRewind();
                                break;
                            case 18:
                                callback.onSeekTo(((Long) message.obj).longValue());
                                break;
                            case 19:
                                callback.onSetRating((RatingCompat) message.obj);
                                break;
                            case 20:
                                callback.onCustomAction((String) message.obj, bundle);
                                break;
                            case 21:
                                KeyEvent keyEvent = (KeyEvent) message.obj;
                                Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
                                intent.putExtra("android.intent.extra.KEY_EVENT", keyEvent);
                                if (!callback.onMediaButtonEvent(intent)) {
                                    onMediaButtonEvent(keyEvent, callback);
                                    break;
                                }
                                break;
                            case 22:
                                MediaSessionImplBase.this.setVolumeTo(message.arg1, 0);
                                break;
                            case 23:
                                callback.onSetRepeatMode(message.arg1);
                                break;
                            case 25:
                                callback.onAddQueueItem((MediaDescriptionCompat) message.obj);
                                break;
                            case 26:
                                callback.onAddQueueItem((MediaDescriptionCompat) message.obj, message.arg1);
                                break;
                            case 27:
                                mediaDescriptionCompat = (MediaDescriptionCompat) message.obj;
                            case 28:
                                if (MediaSessionImplBase.this.mQueue != null) {
                                    QueueItem queueItem = (message.arg1 < 0 || message.arg1 >= MediaSessionImplBase.this.mQueue.size()) ? null : (QueueItem) MediaSessionImplBase.this.mQueue.get(message.arg1);
                                    if (queueItem != null) {
                                        mediaDescriptionCompat = queueItem.getDescription();
                                    }
                                }
                                break;
                            case 29:
                                callback.onSetCaptioningEnabled(((Boolean) message.obj).booleanValue());
                                break;
                            case 30:
                                callback.onSetShuffleMode(message.arg1);
                                break;
                            case 31:
                                callback.onSetRating((RatingCompat) message.obj, bundle);
                                break;
                            case 32:
                                callback.onSetPlaybackSpeed(((Float) message.obj).floatValue());
                                break;
                        }
                        callback.onRemoveQueueItem(mediaDescriptionCompat);
                    } finally {
                        MediaSessionImplBase.this.setCurrentControllerInfo(null);
                    }
                }
            }
        }

        public MediaSessionImplBase(Context context, String str, ComponentName componentName, PendingIntent pendingIntent) {
            if (componentName != null) {
                this.mContext = context;
                this.mPackageName = context.getPackageName();
                this.mAudioManager = (AudioManager) context.getSystemService("audio");
                this.mTag = str;
                this.mMediaButtonReceiverComponentName = componentName;
                this.mMediaButtonReceiverIntent = pendingIntent;
                this.mStub = new MediaSessionStub();
                this.mToken = new Token(this.mStub);
                this.mRatingType = 0;
                this.mVolumeType = 1;
                this.mLocalStream = 3;
                this.mRcc = new RemoteControlClient(pendingIntent);
                return;
            }
            throw new IllegalArgumentException("MediaButtonReceiver component may not be null.");
        }

        private void sendCaptioningEnabled(boolean z) {
            for (int beginBroadcast = this.mControllerCallbacks.beginBroadcast() - 1; beginBroadcast >= 0; beginBroadcast--) {
                try {
                    ((IMediaControllerCallback) this.mControllerCallbacks.getBroadcastItem(beginBroadcast)).onCaptioningEnabledChanged(z);
                } catch (RemoteException unused) {
                }
            }
            this.mControllerCallbacks.finishBroadcast();
        }

        private void sendEvent(String str, Bundle bundle) {
            for (int beginBroadcast = this.mControllerCallbacks.beginBroadcast() - 1; beginBroadcast >= 0; beginBroadcast--) {
                try {
                    ((IMediaControllerCallback) this.mControllerCallbacks.getBroadcastItem(beginBroadcast)).onEvent(str, bundle);
                } catch (RemoteException unused) {
                }
            }
            this.mControllerCallbacks.finishBroadcast();
        }

        private void sendExtras(Bundle bundle) {
            for (int beginBroadcast = this.mControllerCallbacks.beginBroadcast() - 1; beginBroadcast >= 0; beginBroadcast--) {
                try {
                    ((IMediaControllerCallback) this.mControllerCallbacks.getBroadcastItem(beginBroadcast)).onExtrasChanged(bundle);
                } catch (RemoteException unused) {
                }
            }
            this.mControllerCallbacks.finishBroadcast();
        }

        private void sendMetadata(MediaMetadataCompat mediaMetadataCompat) {
            for (int beginBroadcast = this.mControllerCallbacks.beginBroadcast() - 1; beginBroadcast >= 0; beginBroadcast--) {
                try {
                    ((IMediaControllerCallback) this.mControllerCallbacks.getBroadcastItem(beginBroadcast)).onMetadataChanged(mediaMetadataCompat);
                } catch (RemoteException unused) {
                }
            }
            this.mControllerCallbacks.finishBroadcast();
        }

        private void sendQueue(List list) {
            for (int beginBroadcast = this.mControllerCallbacks.beginBroadcast() - 1; beginBroadcast >= 0; beginBroadcast--) {
                try {
                    ((IMediaControllerCallback) this.mControllerCallbacks.getBroadcastItem(beginBroadcast)).onQueueChanged(list);
                } catch (RemoteException unused) {
                }
            }
            this.mControllerCallbacks.finishBroadcast();
        }

        private void sendQueueTitle(CharSequence charSequence) {
            for (int beginBroadcast = this.mControllerCallbacks.beginBroadcast() - 1; beginBroadcast >= 0; beginBroadcast--) {
                try {
                    ((IMediaControllerCallback) this.mControllerCallbacks.getBroadcastItem(beginBroadcast)).onQueueTitleChanged(charSequence);
                } catch (RemoteException unused) {
                }
            }
            this.mControllerCallbacks.finishBroadcast();
        }

        private void sendRepeatMode(int i) {
            for (int beginBroadcast = this.mControllerCallbacks.beginBroadcast() - 1; beginBroadcast >= 0; beginBroadcast--) {
                try {
                    ((IMediaControllerCallback) this.mControllerCallbacks.getBroadcastItem(beginBroadcast)).onRepeatModeChanged(i);
                } catch (RemoteException unused) {
                }
            }
            this.mControllerCallbacks.finishBroadcast();
        }

        private void sendSessionDestroyed() {
            for (int beginBroadcast = this.mControllerCallbacks.beginBroadcast() - 1; beginBroadcast >= 0; beginBroadcast--) {
                try {
                    ((IMediaControllerCallback) this.mControllerCallbacks.getBroadcastItem(beginBroadcast)).onSessionDestroyed();
                } catch (RemoteException unused) {
                }
            }
            this.mControllerCallbacks.finishBroadcast();
            this.mControllerCallbacks.kill();
        }

        private void sendShuffleMode(int i) {
            for (int beginBroadcast = this.mControllerCallbacks.beginBroadcast() - 1; beginBroadcast >= 0; beginBroadcast--) {
                try {
                    ((IMediaControllerCallback) this.mControllerCallbacks.getBroadcastItem(beginBroadcast)).onShuffleModeChanged(i);
                } catch (RemoteException unused) {
                }
            }
            this.mControllerCallbacks.finishBroadcast();
        }

        private void sendState(PlaybackStateCompat playbackStateCompat) {
            for (int beginBroadcast = this.mControllerCallbacks.beginBroadcast() - 1; beginBroadcast >= 0; beginBroadcast--) {
                try {
                    ((IMediaControllerCallback) this.mControllerCallbacks.getBroadcastItem(beginBroadcast)).onPlaybackStateChanged(playbackStateCompat);
                } catch (RemoteException unused) {
                }
            }
            this.mControllerCallbacks.finishBroadcast();
        }

        /* access modifiers changed from: 0000 */
        public void adjustVolume(int i, int i2) {
            if (this.mVolumeType == 2) {
                VolumeProviderCompat volumeProviderCompat = this.mVolumeProvider;
                if (volumeProviderCompat != null) {
                    volumeProviderCompat.onAdjustVolume(i);
                    return;
                }
                return;
            }
            this.mAudioManager.adjustStreamVolume(this.mLocalStream, i, i2);
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0037, code lost:
            if (r1 != null) goto L_0x001d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:6:0x001b, code lost:
            if (r1 != null) goto L_0x001d;
         */
        /* JADX WARNING: Removed duplicated region for block: B:15:0x0042  */
        /* JADX WARNING: Removed duplicated region for block: B:18:0x0051  */
        /* JADX WARNING: Removed duplicated region for block: B:21:0x0062  */
        /* JADX WARNING: Removed duplicated region for block: B:24:0x0072  */
        /* JADX WARNING: Removed duplicated region for block: B:27:0x0082  */
        /* JADX WARNING: Removed duplicated region for block: B:30:0x0093  */
        /* JADX WARNING: Removed duplicated region for block: B:33:0x00a3  */
        /* JADX WARNING: Removed duplicated region for block: B:36:0x00b3  */
        /* JADX WARNING: Removed duplicated region for block: B:39:0x00c4  */
        /* JADX WARNING: Removed duplicated region for block: B:42:0x00d5  */
        /* JADX WARNING: Removed duplicated region for block: B:45:0x00e5  */
        /* JADX WARNING: Removed duplicated region for block: B:48:0x00f7  */
        /* JADX WARNING: Removed duplicated region for block: B:51:0x0108  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public MetadataEditor buildRccMetadata(Bundle bundle) {
            String str;
            String str2;
            String str3;
            String str4;
            String str5;
            String str6;
            String str7;
            String str8;
            String str9;
            String str10;
            Bitmap bitmap;
            MetadataEditor editMetadata = this.mRcc.editMetadata(true);
            if (bundle == null) {
                return editMetadata;
            }
            String str11 = MediaMetadataCompat.METADATA_KEY_ART;
            if (bundle.containsKey(str11)) {
                bitmap = (Bitmap) bundle.getParcelable(str11);
            } else {
                String str12 = MediaMetadataCompat.METADATA_KEY_ALBUM_ART;
                if (bundle.containsKey(str12)) {
                    bitmap = (Bitmap) bundle.getParcelable(str12);
                }
                str = MediaMetadataCompat.METADATA_KEY_ALBUM;
                if (bundle.containsKey(str)) {
                    editMetadata.putString(1, bundle.getString(str));
                }
                str2 = MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST;
                if (bundle.containsKey(str2)) {
                    editMetadata.putString(13, bundle.getString(str2));
                }
                str3 = MediaMetadataCompat.METADATA_KEY_ARTIST;
                if (bundle.containsKey(str3)) {
                    editMetadata.putString(2, bundle.getString(str3));
                }
                str4 = MediaMetadataCompat.METADATA_KEY_AUTHOR;
                if (bundle.containsKey(str4)) {
                    editMetadata.putString(3, bundle.getString(str4));
                }
                str5 = MediaMetadataCompat.METADATA_KEY_COMPILATION;
                if (bundle.containsKey(str5)) {
                    editMetadata.putString(15, bundle.getString(str5));
                }
                str6 = MediaMetadataCompat.METADATA_KEY_COMPOSER;
                if (bundle.containsKey(str6)) {
                    editMetadata.putString(4, bundle.getString(str6));
                }
                str7 = MediaMetadataCompat.METADATA_KEY_DATE;
                if (bundle.containsKey(str7)) {
                    editMetadata.putString(5, bundle.getString(str7));
                }
                str8 = MediaMetadataCompat.METADATA_KEY_DISC_NUMBER;
                if (bundle.containsKey(str8)) {
                    editMetadata.putLong(14, bundle.getLong(str8));
                }
                str9 = MediaMetadataCompat.METADATA_KEY_DURATION;
                if (bundle.containsKey(str9)) {
                    editMetadata.putLong(9, bundle.getLong(str9));
                }
                str10 = MediaMetadataCompat.METADATA_KEY_GENRE;
                if (bundle.containsKey(str10)) {
                    editMetadata.putString(6, bundle.getString(str10));
                }
                if (bundle.containsKey(MediaMetadataCompat.METADATA_KEY_TITLE)) {
                    editMetadata.putString(7, bundle.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
                }
                if (bundle.containsKey(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER)) {
                    editMetadata.putLong(0, bundle.getLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER));
                }
                if (bundle.containsKey(MediaMetadataCompat.METADATA_KEY_WRITER)) {
                    editMetadata.putString(11, bundle.getString(MediaMetadataCompat.METADATA_KEY_WRITER));
                }
                return editMetadata;
            }
            bitmap = bitmap.copy(bitmap.getConfig(), false);
            editMetadata.putBitmap(100, bitmap);
            str = MediaMetadataCompat.METADATA_KEY_ALBUM;
            if (bundle.containsKey(str)) {
            }
            str2 = MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST;
            if (bundle.containsKey(str2)) {
            }
            str3 = MediaMetadataCompat.METADATA_KEY_ARTIST;
            if (bundle.containsKey(str3)) {
            }
            str4 = MediaMetadataCompat.METADATA_KEY_AUTHOR;
            if (bundle.containsKey(str4)) {
            }
            str5 = MediaMetadataCompat.METADATA_KEY_COMPILATION;
            if (bundle.containsKey(str5)) {
            }
            str6 = MediaMetadataCompat.METADATA_KEY_COMPOSER;
            if (bundle.containsKey(str6)) {
            }
            str7 = MediaMetadataCompat.METADATA_KEY_DATE;
            if (bundle.containsKey(str7)) {
            }
            str8 = MediaMetadataCompat.METADATA_KEY_DISC_NUMBER;
            if (bundle.containsKey(str8)) {
            }
            str9 = MediaMetadataCompat.METADATA_KEY_DURATION;
            if (bundle.containsKey(str9)) {
            }
            str10 = MediaMetadataCompat.METADATA_KEY_GENRE;
            if (bundle.containsKey(str10)) {
            }
            if (bundle.containsKey(MediaMetadataCompat.METADATA_KEY_TITLE)) {
            }
            if (bundle.containsKey(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER)) {
            }
            if (bundle.containsKey(MediaMetadataCompat.METADATA_KEY_WRITER)) {
            }
            return editMetadata;
        }

        public String getCallingPackage() {
            return null;
        }

        public RemoteUserInfo getCurrentControllerInfo() {
            RemoteUserInfo remoteUserInfo;
            synchronized (this.mLock) {
                remoteUserInfo = this.mRemoteUserInfo;
            }
            return remoteUserInfo;
        }

        public Object getMediaSession() {
            return null;
        }

        /* access modifiers changed from: 0000 */
        public String getPackageNameForUid(int i) {
            String nameForUid = this.mContext.getPackageManager().getNameForUid(i);
            return TextUtils.isEmpty(nameForUid) ? RemoteUserInfo.LEGACY_CONTROLLER : nameForUid;
        }

        public PlaybackStateCompat getPlaybackState() {
            PlaybackStateCompat playbackStateCompat;
            synchronized (this.mLock) {
                playbackStateCompat = this.mState;
            }
            return playbackStateCompat;
        }

        /* access modifiers changed from: 0000 */
        public int getRccStateFromState(int i) {
            switch (i) {
                case 0:
                    return 0;
                case 1:
                    return 1;
                case 2:
                    return 2;
                case 3:
                    return 3;
                case 4:
                    return 4;
                case 5:
                    return 5;
                case 6:
                case 8:
                    return 8;
                case 7:
                    return 9;
                case 9:
                    return 7;
                case 10:
                case 11:
                    return 6;
                default:
                    return -1;
            }
        }

        /* access modifiers changed from: 0000 */
        public int getRccTransportControlFlagsFromActions(long j) {
            int i = (1 & j) != 0 ? 32 : 0;
            if ((2 & j) != 0) {
                i |= 16;
            }
            if ((4 & j) != 0) {
                i |= 4;
            }
            if ((8 & j) != 0) {
                i |= 2;
            }
            if ((16 & j) != 0) {
                i |= 1;
            }
            if ((32 & j) != 0) {
                i |= 128;
            }
            if ((64 & j) != 0) {
                i |= 64;
            }
            return (j & 512) != 0 ? i | 8 : i;
        }

        public Object getRemoteControlClient() {
            return null;
        }

        public Token getSessionToken() {
            return this.mToken;
        }

        public boolean isActive() {
            return this.mIsActive;
        }

        /* access modifiers changed from: 0000 */
        public void postToHandler(int i, int i2, int i3, Object obj, Bundle bundle) {
            synchronized (this.mLock) {
                if (this.mHandler != null) {
                    Message obtainMessage = this.mHandler.obtainMessage(i, i2, i3, obj);
                    Bundle bundle2 = new Bundle();
                    int callingUid = Binder.getCallingUid();
                    bundle2.putInt("data_calling_uid", callingUid);
                    bundle2.putString(MediaSessionCompat.DATA_CALLING_PACKAGE, getPackageNameForUid(callingUid));
                    int callingPid = Binder.getCallingPid();
                    if (callingPid > 0) {
                        bundle2.putInt("data_calling_pid", callingPid);
                    } else {
                        bundle2.putInt("data_calling_pid", -1);
                    }
                    if (bundle != null) {
                        bundle2.putBundle(MediaSessionCompat.DATA_EXTRAS, bundle);
                    }
                    obtainMessage.setData(bundle2);
                    obtainMessage.sendToTarget();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void registerMediaButtonEventReceiver(PendingIntent pendingIntent, ComponentName componentName) {
            this.mAudioManager.registerMediaButtonEventReceiver(componentName);
        }

        public void release() {
            this.mIsActive = false;
            this.mDestroyed = true;
            updateMbrAndRcc();
            sendSessionDestroyed();
        }

        public void sendSessionEvent(String str, Bundle bundle) {
            sendEvent(str, bundle);
        }

        /* access modifiers changed from: 0000 */
        public void sendVolumeInfoChanged(ParcelableVolumeInfo parcelableVolumeInfo) {
            for (int beginBroadcast = this.mControllerCallbacks.beginBroadcast() - 1; beginBroadcast >= 0; beginBroadcast--) {
                try {
                    ((IMediaControllerCallback) this.mControllerCallbacks.getBroadcastItem(beginBroadcast)).onVolumeInfoChanged(parcelableVolumeInfo);
                } catch (RemoteException unused) {
                }
            }
            this.mControllerCallbacks.finishBroadcast();
        }

        public void setActive(boolean z) {
            if (z != this.mIsActive) {
                this.mIsActive = z;
                updateMbrAndRcc();
            }
        }

        public void setCallback(Callback callback, Handler handler) {
            this.mCallback = callback;
            if (callback != null) {
                if (handler == null) {
                    handler = new Handler();
                }
                synchronized (this.mLock) {
                    if (this.mHandler != null) {
                        this.mHandler.removeCallbacksAndMessages(null);
                    }
                    this.mHandler = new MessageHandler(handler.getLooper());
                    this.mCallback.setSessionImpl(this, handler);
                }
            }
        }

        public void setCaptioningEnabled(boolean z) {
            if (this.mCaptioningEnabled != z) {
                this.mCaptioningEnabled = z;
                sendCaptioningEnabled(z);
            }
        }

        public void setCurrentControllerInfo(RemoteUserInfo remoteUserInfo) {
            synchronized (this.mLock) {
                this.mRemoteUserInfo = remoteUserInfo;
            }
        }

        public void setExtras(Bundle bundle) {
            this.mExtras = bundle;
            sendExtras(bundle);
        }

        public void setFlags(int i) {
            synchronized (this.mLock) {
                this.mFlags = i | 1 | 2;
            }
        }

        public void setMediaButtonReceiver(PendingIntent pendingIntent) {
        }

        public void setMetadata(MediaMetadataCompat mediaMetadataCompat) {
            if (mediaMetadataCompat != null) {
                mediaMetadataCompat = new MediaMetadataCompat.Builder(mediaMetadataCompat, MediaSessionCompat.sMaxBitmapSize).build();
            }
            synchronized (this.mLock) {
                this.mMetadata = mediaMetadataCompat;
            }
            sendMetadata(mediaMetadataCompat);
            if (this.mIsActive) {
                buildRccMetadata(mediaMetadataCompat == null ? null : mediaMetadataCompat.getBundle()).apply();
            }
        }

        public void setPlaybackState(PlaybackStateCompat playbackStateCompat) {
            synchronized (this.mLock) {
                this.mState = playbackStateCompat;
            }
            sendState(playbackStateCompat);
            if (this.mIsActive) {
                if (playbackStateCompat == null) {
                    this.mRcc.setPlaybackState(0);
                    this.mRcc.setTransportControlFlags(0);
                } else {
                    setRccState(playbackStateCompat);
                    this.mRcc.setTransportControlFlags(getRccTransportControlFlagsFromActions(playbackStateCompat.getActions()));
                }
            }
        }

        public void setPlaybackToLocal(int i) {
            VolumeProviderCompat volumeProviderCompat = this.mVolumeProvider;
            if (volumeProviderCompat != null) {
                volumeProviderCompat.setCallback(null);
            }
            this.mLocalStream = i;
            this.mVolumeType = 1;
            int i2 = this.mVolumeType;
            int i3 = this.mLocalStream;
            ParcelableVolumeInfo parcelableVolumeInfo = new ParcelableVolumeInfo(i2, i3, 2, this.mAudioManager.getStreamMaxVolume(i3), this.mAudioManager.getStreamVolume(this.mLocalStream));
            sendVolumeInfoChanged(parcelableVolumeInfo);
        }

        public void setPlaybackToRemote(VolumeProviderCompat volumeProviderCompat) {
            if (volumeProviderCompat != null) {
                VolumeProviderCompat volumeProviderCompat2 = this.mVolumeProvider;
                if (volumeProviderCompat2 != null) {
                    volumeProviderCompat2.setCallback(null);
                }
                this.mVolumeType = 2;
                this.mVolumeProvider = volumeProviderCompat;
                ParcelableVolumeInfo parcelableVolumeInfo = new ParcelableVolumeInfo(this.mVolumeType, this.mLocalStream, this.mVolumeProvider.getVolumeControl(), this.mVolumeProvider.getMaxVolume(), this.mVolumeProvider.getCurrentVolume());
                sendVolumeInfoChanged(parcelableVolumeInfo);
                volumeProviderCompat.setCallback(this.mVolumeCallback);
                return;
            }
            throw new IllegalArgumentException("volumeProvider may not be null");
        }

        public void setQueue(List list) {
            this.mQueue = list;
            sendQueue(list);
        }

        public void setQueueTitle(CharSequence charSequence) {
            this.mQueueTitle = charSequence;
            sendQueueTitle(charSequence);
        }

        public void setRatingType(int i) {
            this.mRatingType = i;
        }

        /* access modifiers changed from: 0000 */
        public void setRccState(PlaybackStateCompat playbackStateCompat) {
            this.mRcc.setPlaybackState(getRccStateFromState(playbackStateCompat.getState()));
        }

        public void setRepeatMode(int i) {
            if (this.mRepeatMode != i) {
                this.mRepeatMode = i;
                sendRepeatMode(i);
            }
        }

        public void setSessionActivity(PendingIntent pendingIntent) {
            synchronized (this.mLock) {
                this.mSessionActivity = pendingIntent;
            }
        }

        public void setShuffleMode(int i) {
            if (this.mShuffleMode != i) {
                this.mShuffleMode = i;
                sendShuffleMode(i);
            }
        }

        /* access modifiers changed from: 0000 */
        public void setVolumeTo(int i, int i2) {
            if (this.mVolumeType == 2) {
                VolumeProviderCompat volumeProviderCompat = this.mVolumeProvider;
                if (volumeProviderCompat != null) {
                    volumeProviderCompat.onSetVolumeTo(i);
                    return;
                }
                return;
            }
            this.mAudioManager.setStreamVolume(this.mLocalStream, i, i2);
        }

        /* access modifiers changed from: 0000 */
        public void unregisterMediaButtonEventReceiver(PendingIntent pendingIntent, ComponentName componentName) {
            this.mAudioManager.unregisterMediaButtonEventReceiver(componentName);
        }

        /* access modifiers changed from: 0000 */
        public void updateMbrAndRcc() {
            if (this.mIsActive) {
                registerMediaButtonEventReceiver(this.mMediaButtonReceiverIntent, this.mMediaButtonReceiverComponentName);
                this.mAudioManager.registerRemoteControlClient(this.mRcc);
                setMetadata(this.mMetadata);
                setPlaybackState(this.mState);
                return;
            }
            unregisterMediaButtonEventReceiver(this.mMediaButtonReceiverIntent, this.mMediaButtonReceiverComponentName);
            this.mRcc.setPlaybackState(0);
            this.mAudioManager.unregisterRemoteControlClient(this.mRcc);
        }
    }

    public interface OnActiveChangeListener {
        void onActiveChanged();
    }

    @SuppressLint({"BanParcelableUsage"})
    public final class QueueItem implements Parcelable {
        public static final Creator CREATOR = new Creator() {
            public QueueItem createFromParcel(Parcel parcel) {
                return new QueueItem(parcel);
            }

            public QueueItem[] newArray(int i) {
                return new QueueItem[i];
            }
        };
        public static final int UNKNOWN_ID = -1;
        private final MediaDescriptionCompat mDescription;
        private final long mId;
        private android.media.session.MediaSession.QueueItem mItemFwk;

        private QueueItem(android.media.session.MediaSession.QueueItem queueItem, MediaDescriptionCompat mediaDescriptionCompat, long j) {
            if (mediaDescriptionCompat == null) {
                throw new IllegalArgumentException("Description cannot be null.");
            } else if (j != -1) {
                this.mDescription = mediaDescriptionCompat;
                this.mId = j;
                this.mItemFwk = queueItem;
            } else {
                throw new IllegalArgumentException("Id cannot be QueueItem.UNKNOWN_ID");
            }
        }

        QueueItem(Parcel parcel) {
            this.mDescription = (MediaDescriptionCompat) MediaDescriptionCompat.CREATOR.createFromParcel(parcel);
            this.mId = parcel.readLong();
        }

        public QueueItem(MediaDescriptionCompat mediaDescriptionCompat, long j) {
            this(null, mediaDescriptionCompat, j);
        }

        public static QueueItem fromQueueItem(Object obj) {
            if (obj == null || VERSION.SDK_INT < 21) {
                return null;
            }
            android.media.session.MediaSession.QueueItem queueItem = (android.media.session.MediaSession.QueueItem) obj;
            return new QueueItem(queueItem, MediaDescriptionCompat.fromMediaDescription(queueItem.getDescription()), queueItem.getQueueId());
        }

        /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<java.lang.Object>, for r2v0, types: [java.util.List, java.util.List<java.lang.Object>] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static List fromQueueItemList(List<Object> list) {
            if (list == null || VERSION.SDK_INT < 21) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            for (Object fromQueueItem : list) {
                arrayList.add(fromQueueItem(fromQueueItem));
            }
            return arrayList;
        }

        public int describeContents() {
            return 0;
        }

        public MediaDescriptionCompat getDescription() {
            return this.mDescription;
        }

        public long getQueueId() {
            return this.mId;
        }

        public Object getQueueItem() {
            if (this.mItemFwk != null || VERSION.SDK_INT < 21) {
                return this.mItemFwk;
            }
            this.mItemFwk = new android.media.session.MediaSession.QueueItem((MediaDescription) this.mDescription.getMediaDescription(), this.mId);
            return this.mItemFwk;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("MediaSession.QueueItem {Description=");
            sb.append(this.mDescription);
            sb.append(", Id=");
            sb.append(this.mId);
            sb.append(" }");
            return sb.toString();
        }

        public void writeToParcel(Parcel parcel, int i) {
            this.mDescription.writeToParcel(parcel, i);
            parcel.writeLong(this.mId);
        }
    }

    @SuppressLint({"BanParcelableUsage"})
    @RestrictTo({Scope.LIBRARY})
    public final class ResultReceiverWrapper implements Parcelable {
        public static final Creator CREATOR = new Creator() {
            public ResultReceiverWrapper createFromParcel(Parcel parcel) {
                return new ResultReceiverWrapper(parcel);
            }

            public ResultReceiverWrapper[] newArray(int i) {
                return new ResultReceiverWrapper[i];
            }
        };
        ResultReceiver mResultReceiver;

        ResultReceiverWrapper(Parcel parcel) {
            this.mResultReceiver = (ResultReceiver) ResultReceiver.CREATOR.createFromParcel(parcel);
        }

        public ResultReceiverWrapper(@NonNull ResultReceiver resultReceiver) {
            this.mResultReceiver = resultReceiver;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            this.mResultReceiver.writeToParcel(parcel, i);
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SessionFlags {
    }

    @SuppressLint({"BanParcelableUsage"})
    public final class Token implements Parcelable {
        public static final Creator CREATOR = new Creator() {
            public Token createFromParcel(Parcel parcel) {
                return new Token(VERSION.SDK_INT >= 21 ? parcel.readParcelable(null) : parcel.readStrongBinder());
            }

            public Token[] newArray(int i) {
                return new Token[i];
            }
        };
        private IMediaSession mExtraBinder;
        private final Object mInner;
        private VersionedParcelable mSession2Token;

        Token(Object obj) {
            this(obj, null, null);
        }

        Token(Object obj, IMediaSession iMediaSession) {
            this(obj, iMediaSession, null);
        }

        Token(Object obj, IMediaSession iMediaSession, VersionedParcelable versionedParcelable) {
            this.mInner = obj;
            this.mExtraBinder = iMediaSession;
            this.mSession2Token = versionedParcelable;
        }

        @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
        public static Token fromBundle(Bundle bundle) {
            Token token = null;
            if (bundle == null) {
                return null;
            }
            IMediaSession asInterface = Stub.asInterface(BundleCompat.getBinder(bundle, MediaSessionCompat.KEY_EXTRA_BINDER));
            VersionedParcelable versionedParcelable = ParcelUtils.getVersionedParcelable(bundle, MediaSessionCompat.KEY_SESSION2_TOKEN);
            Token token2 = (Token) bundle.getParcelable(MediaSessionCompat.KEY_TOKEN);
            if (token2 != null) {
                token = new Token(token2.mInner, asInterface, versionedParcelable);
            }
            return token;
        }

        public static Token fromToken(Object obj) {
            return fromToken(obj, null);
        }

        @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
        public static Token fromToken(Object obj, IMediaSession iMediaSession) {
            if (obj == null || VERSION.SDK_INT < 21) {
                return null;
            }
            if (obj instanceof android.media.session.MediaSession.Token) {
                return new Token(obj, iMediaSession);
            }
            throw new IllegalArgumentException("token is not a valid MediaSession.Token object");
        }

        public int describeContents() {
            return 0;
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Token)) {
                return false;
            }
            Token token = (Token) obj;
            Object obj2 = this.mInner;
            if (obj2 == null) {
                if (token.mInner != null) {
                    z = false;
                }
                return z;
            }
            Object obj3 = token.mInner;
            if (obj3 == null) {
                return false;
            }
            return obj2.equals(obj3);
        }

        @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
        public IMediaSession getExtraBinder() {
            return this.mExtraBinder;
        }

        @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
        public VersionedParcelable getSession2Token() {
            return this.mSession2Token;
        }

        public Object getToken() {
            return this.mInner;
        }

        public int hashCode() {
            Object obj = this.mInner;
            if (obj == null) {
                return 0;
            }
            return obj.hashCode();
        }

        @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
        public void setExtraBinder(IMediaSession iMediaSession) {
            this.mExtraBinder = iMediaSession;
        }

        @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
        public void setSession2Token(VersionedParcelable versionedParcelable) {
            this.mSession2Token = versionedParcelable;
        }

        @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
        public Bundle toBundle() {
            Bundle bundle = new Bundle();
            bundle.putParcelable(MediaSessionCompat.KEY_TOKEN, this);
            IMediaSession iMediaSession = this.mExtraBinder;
            if (iMediaSession != null) {
                BundleCompat.putBinder(bundle, MediaSessionCompat.KEY_EXTRA_BINDER, iMediaSession.asBinder());
            }
            VersionedParcelable versionedParcelable = this.mSession2Token;
            if (versionedParcelable != null) {
                ParcelUtils.putVersionedParcelable(bundle, MediaSessionCompat.KEY_SESSION2_TOKEN, versionedParcelable);
            }
            return bundle;
        }

        public void writeToParcel(Parcel parcel, int i) {
            int i2 = VERSION.SDK_INT;
            Object obj = this.mInner;
            if (i2 >= 21) {
                parcel.writeParcelable((Parcelable) obj, i);
            } else {
                parcel.writeStrongBinder((IBinder) obj);
            }
        }
    }

    private MediaSessionCompat(Context context, MediaSessionImpl mediaSessionImpl) {
        this.mActiveListeners = new ArrayList();
        this.mImpl = mediaSessionImpl;
        this.mController = new MediaControllerCompat(context, this);
    }

    public MediaSessionCompat(Context context, String str) {
        this(context, str, null, null);
    }

    public MediaSessionCompat(Context context, String str, ComponentName componentName, PendingIntent pendingIntent) {
        this(context, str, componentName, pendingIntent, null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x0087  */
    /* JADX WARNING: Removed duplicated region for block: B:34:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public MediaSessionCompat(Context context, String str, ComponentName componentName, PendingIntent pendingIntent, VersionedParcelable versionedParcelable) {
        Callback r4;
        this.mActiveListeners = new ArrayList();
        if (context == null) {
            throw new IllegalArgumentException("context must not be null");
        } else if (!TextUtils.isEmpty(str)) {
            if (componentName == null) {
                componentName = MediaButtonReceiver.getMediaButtonReceiverComponent(context);
                if (componentName == null) {
                    Log.w(TAG, "Couldn't find a unique registered media button receiver in the given context.");
                }
            }
            if (componentName != null && pendingIntent == null) {
                Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
                intent.setComponent(componentName);
                pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            }
            int i = VERSION.SDK_INT;
            if (i >= 28) {
                this.mImpl = new MediaSessionImplApi28(context, str, versionedParcelable);
                r4 = new Callback() {
                };
            } else if (i >= 21) {
                this.mImpl = new MediaSessionImplApi21(context, str, versionedParcelable);
                r4 = new Callback() {
                };
            } else {
                MediaSessionImpl mediaSessionImplBase = i >= 19 ? new MediaSessionImplApi19(context, str, componentName, pendingIntent) : i >= 18 ? new MediaSessionImplApi18(context, str, componentName, pendingIntent) : new MediaSessionImplBase(context, str, componentName, pendingIntent);
                this.mImpl = mediaSessionImplBase;
                this.mController = new MediaControllerCompat(context, this);
                if (sMaxBitmapSize != 0) {
                    sMaxBitmapSize = (int) (TypedValue.applyDimension(1, 320.0f, context.getResources().getDisplayMetrics()) + 0.5f);
                    return;
                }
                return;
            }
            setCallback(r4);
            this.mImpl.setMediaButtonReceiver(pendingIntent);
            this.mController = new MediaControllerCompat(context, this);
            if (sMaxBitmapSize != 0) {
            }
        } else {
            throw new IllegalArgumentException("tag must not be null or empty");
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public static void ensureClassLoader(@Nullable Bundle bundle) {
        if (bundle != null) {
            bundle.setClassLoader(MediaSessionCompat.class.getClassLoader());
        }
    }

    public static MediaSessionCompat fromMediaSession(Context context, Object obj) {
        int i = VERSION.SDK_INT;
        if (i < 21 || context == null || obj == null) {
            return null;
        }
        return new MediaSessionCompat(context, i >= 28 ? new MediaSessionImplApi28(obj) : new MediaSessionImplApi21(obj));
    }

    static PlaybackStateCompat getStateWithUpdatedPosition(PlaybackStateCompat playbackStateCompat, MediaMetadataCompat mediaMetadataCompat) {
        if (playbackStateCompat == null) {
            return playbackStateCompat;
        }
        long j = -1;
        if (playbackStateCompat.getPosition() == -1) {
            return playbackStateCompat;
        }
        if (playbackStateCompat.getState() != 3 && playbackStateCompat.getState() != 4 && playbackStateCompat.getState() != 5) {
            return playbackStateCompat;
        }
        long lastPositionUpdateTime = playbackStateCompat.getLastPositionUpdateTime();
        if (lastPositionUpdateTime <= 0) {
            return playbackStateCompat;
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long playbackSpeed = ((long) (playbackStateCompat.getPlaybackSpeed() * ((float) (elapsedRealtime - lastPositionUpdateTime)))) + playbackStateCompat.getPosition();
        if (mediaMetadataCompat != null) {
            String str = MediaMetadataCompat.METADATA_KEY_DURATION;
            if (mediaMetadataCompat.containsKey(str)) {
                j = mediaMetadataCompat.getLong(str);
            }
        }
        long j2 = (j < 0 || playbackSpeed <= j) ? playbackSpeed < 0 ? 0 : playbackSpeed : j;
        return new PlaybackStateCompat.Builder(playbackStateCompat).setState(playbackStateCompat.getState(), j2, playbackStateCompat.getPlaybackSpeed(), elapsedRealtime).build();
    }

    public void addOnActiveChangeListener(OnActiveChangeListener onActiveChangeListener) {
        if (onActiveChangeListener != null) {
            this.mActiveListeners.add(onActiveChangeListener);
            return;
        }
        throw new IllegalArgumentException("Listener may not be null");
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public String getCallingPackage() {
        return this.mImpl.getCallingPackage();
    }

    public MediaControllerCompat getController() {
        return this.mController;
    }

    @NonNull
    public final RemoteUserInfo getCurrentControllerInfo() {
        return this.mImpl.getCurrentControllerInfo();
    }

    public Object getMediaSession() {
        return this.mImpl.getMediaSession();
    }

    public Object getRemoteControlClient() {
        return this.mImpl.getRemoteControlClient();
    }

    public Token getSessionToken() {
        return this.mImpl.getSessionToken();
    }

    public boolean isActive() {
        return this.mImpl.isActive();
    }

    public void release() {
        this.mImpl.release();
    }

    public void removeOnActiveChangeListener(OnActiveChangeListener onActiveChangeListener) {
        if (onActiveChangeListener != null) {
            this.mActiveListeners.remove(onActiveChangeListener);
            return;
        }
        throw new IllegalArgumentException("Listener may not be null");
    }

    public void sendSessionEvent(String str, Bundle bundle) {
        if (!TextUtils.isEmpty(str)) {
            this.mImpl.sendSessionEvent(str, bundle);
            return;
        }
        throw new IllegalArgumentException("event cannot be null or empty");
    }

    public void setActive(boolean z) {
        this.mImpl.setActive(z);
        Iterator it = this.mActiveListeners.iterator();
        while (it.hasNext()) {
            ((OnActiveChangeListener) it.next()).onActiveChanged();
        }
    }

    public void setCallback(Callback callback) {
        setCallback(callback, null);
    }

    public void setCallback(Callback callback, Handler handler) {
        MediaSessionImpl mediaSessionImpl = this.mImpl;
        if (callback == null) {
            mediaSessionImpl.setCallback(null, null);
            return;
        }
        if (handler == null) {
            handler = new Handler();
        }
        mediaSessionImpl.setCallback(callback, handler);
    }

    public void setCaptioningEnabled(boolean z) {
        this.mImpl.setCaptioningEnabled(z);
    }

    public void setExtras(Bundle bundle) {
        this.mImpl.setExtras(bundle);
    }

    public void setFlags(int i) {
        this.mImpl.setFlags(i);
    }

    public void setMediaButtonReceiver(PendingIntent pendingIntent) {
        this.mImpl.setMediaButtonReceiver(pendingIntent);
    }

    public void setMetadata(MediaMetadataCompat mediaMetadataCompat) {
        this.mImpl.setMetadata(mediaMetadataCompat);
    }

    public void setPlaybackState(PlaybackStateCompat playbackStateCompat) {
        this.mImpl.setPlaybackState(playbackStateCompat);
    }

    public void setPlaybackToLocal(int i) {
        this.mImpl.setPlaybackToLocal(i);
    }

    public void setPlaybackToRemote(VolumeProviderCompat volumeProviderCompat) {
        if (volumeProviderCompat != null) {
            this.mImpl.setPlaybackToRemote(volumeProviderCompat);
            return;
        }
        throw new IllegalArgumentException("volumeProvider may not be null!");
    }

    public void setQueue(List list) {
        this.mImpl.setQueue(list);
    }

    public void setQueueTitle(CharSequence charSequence) {
        this.mImpl.setQueueTitle(charSequence);
    }

    public void setRatingType(int i) {
        this.mImpl.setRatingType(i);
    }

    public void setRepeatMode(int i) {
        this.mImpl.setRepeatMode(i);
    }

    public void setSessionActivity(PendingIntent pendingIntent) {
        this.mImpl.setSessionActivity(pendingIntent);
    }

    public void setShuffleMode(int i) {
        this.mImpl.setShuffleMode(i);
    }
}
