package tv.danmaku.ijk.media.player;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Rect;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.adobe.xmp.options.PropertyOptions;
import com.ss.android.ugc.effectmanager.effect.model.ComposerHelper;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import tv.danmaku.ijk.media.player.IjkMediaMeta.IjkStreamMeta;
import tv.danmaku.ijk.media.player.annotations.AccessedByNative;
import tv.danmaku.ijk.media.player.annotations.CalledByNative;
import tv.danmaku.ijk.media.player.misc.IAndroidIO;
import tv.danmaku.ijk.media.player.misc.IMediaDataSource;
import tv.danmaku.ijk.media.player.misc.IjkTrackInfo;
import tv.danmaku.ijk.media.player.pragma.DebugLog;

public final class IjkMediaPlayer extends AbstractMediaPlayer {
    public static final int FFP_PROPV_DECODER_AVCODEC = 1;
    public static final int FFP_PROPV_DECODER_MEDIACODEC = 2;
    public static final int FFP_PROPV_DECODER_UNKNOWN = 0;
    public static final int FFP_PROPV_DECODER_VIDEOTOOLBOX = 3;
    public static final int FFP_PROP_FLOAT_DROP_FRAME_RATE = 10007;
    public static final int FFP_PROP_FLOAT_PLAYBACK_RATE = 10003;
    public static final int FFP_PROP_INT64_ASYNC_STATISTIC_BUF_BACKWARDS = 20201;
    public static final int FFP_PROP_INT64_ASYNC_STATISTIC_BUF_CAPACITY = 20203;
    public static final int FFP_PROP_INT64_ASYNC_STATISTIC_BUF_FORWARDS = 20202;
    public static final int FFP_PROP_INT64_AUDIO_CACHED_BYTES = 20008;
    public static final int FFP_PROP_INT64_AUDIO_CACHED_DURATION = 20006;
    public static final int FFP_PROP_INT64_AUDIO_CACHED_PACKETS = 20010;
    public static final int FFP_PROP_INT64_AUDIO_DECODER = 20004;
    public static final int FFP_PROP_INT64_BIT_RATE = 20100;
    public static final int FFP_PROP_INT64_CACHE_STATISTIC_COUNT_BYTES = 20208;
    public static final int FFP_PROP_INT64_CACHE_STATISTIC_FILE_FORWARDS = 20206;
    public static final int FFP_PROP_INT64_CACHE_STATISTIC_FILE_POS = 20207;
    public static final int FFP_PROP_INT64_CACHE_STATISTIC_PHYSICAL_POS = 20205;
    public static final int FFP_PROP_INT64_IMMEDIATE_RECONNECT = 20211;
    public static final int FFP_PROP_INT64_LATEST_SEEK_LOAD_DURATION = 20300;
    public static final int FFP_PROP_INT64_LOGICAL_FILE_SIZE = 20209;
    public static final int FFP_PROP_INT64_SELECTED_AUDIO_STREAM = 20002;
    public static final int FFP_PROP_INT64_SELECTED_TIMEDTEXT_STREAM = 20011;
    public static final int FFP_PROP_INT64_SELECTED_VIDEO_STREAM = 20001;
    public static final int FFP_PROP_INT64_SHARE_CACHE_DATA = 20210;
    public static final int FFP_PROP_INT64_TCP_SPEED = 20200;
    public static final int FFP_PROP_INT64_TRAFFIC_STATISTIC_BYTE_COUNT = 20204;
    public static final int FFP_PROP_INT64_VIDEO_CACHED_BYTES = 20007;
    public static final int FFP_PROP_INT64_VIDEO_CACHED_DURATION = 20005;
    public static final int FFP_PROP_INT64_VIDEO_CACHED_PACKETS = 20009;
    public static final int FFP_PROP_INT64_VIDEO_DECODER = 20003;
    public static final int IJK_LOG_DEBUG = 3;
    public static final int IJK_LOG_DEFAULT = 1;
    public static final int IJK_LOG_ERROR = 6;
    public static final int IJK_LOG_FATAL = 7;
    public static final int IJK_LOG_INFO = 4;
    public static final int IJK_LOG_SILENT = 8;
    public static final int IJK_LOG_UNKNOWN = 0;
    public static final int IJK_LOG_VERBOSE = 2;
    public static final int IJK_LOG_WARN = 5;
    private static final int MEDIA_BUFFERING_UPDATE = 3;
    private static final int MEDIA_ERROR = 100;
    private static final int MEDIA_INFO = 200;
    private static final int MEDIA_NOP = 0;
    private static final int MEDIA_PLAYBACK_COMPLETE = 2;
    private static final int MEDIA_PREPARED = 1;
    private static final int MEDIA_SEEK_COMPLETE = 4;
    protected static final int MEDIA_SET_VIDEO_SAR = 10001;
    private static final int MEDIA_SET_VIDEO_SIZE = 5;
    private static final int MEDIA_TIMED_TEXT = 99;
    public static final int OPT_CATEGORY_CODEC = 2;
    public static final int OPT_CATEGORY_FORMAT = 1;
    public static final int OPT_CATEGORY_PLAYER = 4;
    public static final int OPT_CATEGORY_SWS = 3;
    public static final int PROP_FLOAT_VIDEO_DECODE_FRAMES_PER_SECOND = 10001;
    public static final int PROP_FLOAT_VIDEO_OUTPUT_FRAMES_PER_SECOND = 10002;
    public static final int SDL_FCC_RV16 = 909203026;
    public static final int SDL_FCC_RV32 = 842225234;
    public static final int SDL_FCC_YV12 = 842094169;
    /* access modifiers changed from: private */
    public static final String TAG = "tv.danmaku.ijk.media.player.IjkMediaPlayer";
    private static volatile boolean mIsLibLoaded = false;
    private static volatile boolean mIsNativeInitialized = false;
    private static final IjkLibLoader sLocalLibLoader = new IjkLibLoader() {
        public void loadLibrary(String str) {
            System.loadLibrary(str);
        }
    };
    private String mDataSource;
    private EventHandler mEventHandler;
    @AccessedByNative
    private int mListenerContext;
    @AccessedByNative
    private long mNativeAndroidIO;
    @AccessedByNative
    private long mNativeMediaDataSource;
    /* access modifiers changed from: private */
    @AccessedByNative
    public long mNativeMediaPlayer;
    @AccessedByNative
    private int mNativeSurfaceTexture;
    private OnControlMessageListener mOnControlMessageListener;
    private OnMediaCodecSelectListener mOnMediaCodecSelectListener;
    private OnNativeInvokeListener mOnNativeInvokeListener;
    private boolean mScreenOnWhilePlaying;
    private boolean mStayAwake;
    private SurfaceHolder mSurfaceHolder;
    /* access modifiers changed from: private */
    public int mVideoHeight;
    /* access modifiers changed from: private */
    public int mVideoSarDen;
    /* access modifiers changed from: private */
    public int mVideoSarNum;
    /* access modifiers changed from: private */
    public int mVideoWidth;
    private WakeLock mWakeLock;

    public class DefaultMediaCodecSelector implements OnMediaCodecSelectListener {
        public static final DefaultMediaCodecSelector sInstance = new DefaultMediaCodecSelector();

        @TargetApi(16)
        public String onMediaCodecSelect(IMediaPlayer iMediaPlayer, String str, int i, int i2) {
            String str2 = str;
            if (VERSION.SDK_INT < 16 || TextUtils.isEmpty(str)) {
                return null;
            }
            Log.i(IjkMediaPlayer.TAG, String.format(Locale.US, "onSelectCodec: mime=%s, profile=%d, level=%d", new Object[]{str2, Integer.valueOf(i), Integer.valueOf(i2)}));
            ArrayList arrayList = new ArrayList();
            int codecCount = MediaCodecList.getCodecCount();
            for (int i3 = 0; i3 < codecCount; i3++) {
                MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i3);
                Log.d(IjkMediaPlayer.TAG, String.format(Locale.US, "  found codec: %s", new Object[]{codecInfoAt.getName()}));
                if (!codecInfoAt.isEncoder()) {
                    String[] supportedTypes = codecInfoAt.getSupportedTypes();
                    if (supportedTypes != null) {
                        for (String str3 : supportedTypes) {
                            if (!TextUtils.isEmpty(str3)) {
                                Log.d(IjkMediaPlayer.TAG, String.format(Locale.US, "    mime: %s", new Object[]{str3}));
                                if (str3.equalsIgnoreCase(str2)) {
                                    IjkMediaCodecInfo ijkMediaCodecInfo = IjkMediaCodecInfo.setupCandidate(codecInfoAt, str2);
                                    if (ijkMediaCodecInfo != null) {
                                        arrayList.add(ijkMediaCodecInfo);
                                        Log.i(IjkMediaPlayer.TAG, String.format(Locale.US, "candidate codec: %s rank=%d", new Object[]{codecInfoAt.getName(), Integer.valueOf(ijkMediaCodecInfo.mRank)}));
                                        ijkMediaCodecInfo.dumpProfileLevels(str2);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (arrayList.isEmpty()) {
                return null;
            }
            IjkMediaCodecInfo ijkMediaCodecInfo2 = (IjkMediaCodecInfo) arrayList.get(0);
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                IjkMediaCodecInfo ijkMediaCodecInfo3 = (IjkMediaCodecInfo) it.next();
                if (ijkMediaCodecInfo3.mRank > ijkMediaCodecInfo2.mRank) {
                    ijkMediaCodecInfo2 = ijkMediaCodecInfo3;
                }
            }
            if (ijkMediaCodecInfo2.mRank < 600) {
                Log.w(IjkMediaPlayer.TAG, String.format(Locale.US, "unaccetable codec: %s", new Object[]{ijkMediaCodecInfo2.mCodecInfo.getName()}));
                return null;
            }
            Log.i(IjkMediaPlayer.TAG, String.format(Locale.US, "selected codec: %s rank=%d", new Object[]{ijkMediaCodecInfo2.mCodecInfo.getName(), Integer.valueOf(ijkMediaCodecInfo2.mRank)}));
            return ijkMediaCodecInfo2.mCodecInfo.getName();
        }
    }

    class EventHandler extends Handler {
        private final WeakReference mWeakPlayer;

        public EventHandler(IjkMediaPlayer ijkMediaPlayer, Looper looper) {
            super(looper);
            this.mWeakPlayer = new WeakReference(ijkMediaPlayer);
        }

        public void handleMessage(Message message) {
            IjkMediaPlayer ijkMediaPlayer = (IjkMediaPlayer) this.mWeakPlayer.get();
            if (ijkMediaPlayer != null) {
                long j = 0;
                if (ijkMediaPlayer.mNativeMediaPlayer != 0) {
                    int i = message.what;
                    if (i != 0) {
                        if (i == 1) {
                            ijkMediaPlayer.notifyOnPrepared();
                        } else if (i == 2) {
                            ijkMediaPlayer.stayAwake(false);
                            ijkMediaPlayer.notifyOnCompletion();
                            return;
                        } else if (i == 3) {
                            long j2 = (long) message.arg1;
                            if (j2 < 0) {
                                j2 = 0;
                            }
                            long duration = ijkMediaPlayer.getDuration();
                            if (duration > 0) {
                                j = (j2 * 100) / duration;
                            }
                            if (j >= 100) {
                                j = 100;
                            }
                            ijkMediaPlayer.notifyOnBufferingUpdate((int) j);
                            return;
                        } else if (i == 4) {
                            ijkMediaPlayer.notifyOnSeekComplete();
                            return;
                        } else if (i == 5) {
                            ijkMediaPlayer.mVideoWidth = message.arg1;
                            ijkMediaPlayer.mVideoHeight = message.arg2;
                            ijkMediaPlayer.notifyOnVideoSizeChanged(ijkMediaPlayer.mVideoWidth, ijkMediaPlayer.mVideoHeight, ijkMediaPlayer.mVideoSarNum, ijkMediaPlayer.mVideoSarDen);
                            return;
                        } else if (i == 99) {
                            if (message.obj == null) {
                                ijkMediaPlayer.notifyOnTimedText(null);
                            } else {
                                ijkMediaPlayer.notifyOnTimedText(new IjkTimedText(new Rect(0, 0, 1, 1), (String) message.obj));
                            }
                            return;
                        } else if (i == 100) {
                            String access$100 = IjkMediaPlayer.TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("Error (");
                            sb.append(message.arg1);
                            sb.append(",");
                            sb.append(message.arg2);
                            sb.append(")");
                            DebugLog.e(access$100, sb.toString());
                            if (!ijkMediaPlayer.notifyOnError(message.arg1, message.arg2)) {
                                ijkMediaPlayer.notifyOnCompletion();
                            }
                            ijkMediaPlayer.stayAwake(false);
                            return;
                        } else if (i == 200) {
                            if (message.arg1 == 3) {
                                DebugLog.i(IjkMediaPlayer.TAG, "Info: MEDIA_INFO_VIDEO_RENDERING_START\n");
                            }
                            ijkMediaPlayer.notifyOnInfo(message.arg1, message.arg2);
                            return;
                        } else if (i != 10001) {
                            String access$1002 = IjkMediaPlayer.TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("Unknown message type ");
                            sb2.append(message.what);
                            DebugLog.e(access$1002, sb2.toString());
                        } else {
                            ijkMediaPlayer.mVideoSarNum = message.arg1;
                            ijkMediaPlayer.mVideoSarDen = message.arg2;
                            ijkMediaPlayer.notifyOnVideoSizeChanged(ijkMediaPlayer.mVideoWidth, ijkMediaPlayer.mVideoHeight, ijkMediaPlayer.mVideoSarNum, ijkMediaPlayer.mVideoSarDen);
                        }
                    }
                    return;
                }
            }
            DebugLog.w(IjkMediaPlayer.TAG, "IjkMediaPlayer went away with unhandled events");
        }
    }

    public interface OnControlMessageListener {
        String onControlResolveSegmentUrl(int i);
    }

    public interface OnMediaCodecSelectListener {
        String onMediaCodecSelect(IMediaPlayer iMediaPlayer, String str, int i, int i2);
    }

    public interface OnNativeInvokeListener {
        public static final String ARG_ERROR = "error";
        public static final String ARG_FAMILIY = "family";
        public static final String ARG_FD = "fd";
        public static final String ARG_FILE_SIZE = "file_size";
        public static final String ARG_HTTP_CODE = "http_code";
        public static final String ARG_IP = "ip";
        public static final String ARG_OFFSET = "offset";
        public static final String ARG_PORT = "port";
        public static final String ARG_RETRY_COUNTER = "retry_counter";
        public static final String ARG_SEGMENT_INDEX = "segment_index";
        public static final String ARG_URL = "url";
        public static final int CTRL_DID_TCP_OPEN = 131074;
        public static final int CTRL_WILL_CONCAT_RESOLVE_SEGMENT = 131079;
        public static final int CTRL_WILL_HTTP_OPEN = 131075;
        public static final int CTRL_WILL_LIVE_OPEN = 131077;
        public static final int CTRL_WILL_TCP_OPEN = 131073;
        public static final int EVENT_DID_HTTP_OPEN = 2;
        public static final int EVENT_DID_HTTP_SEEK = 4;
        public static final int EVENT_WILL_HTTP_OPEN = 1;
        public static final int EVENT_WILL_HTTP_SEEK = 3;

        boolean onNativeInvoke(int i, Bundle bundle);
    }

    public IjkMediaPlayer() {
        this(sLocalLibLoader);
    }

    public IjkMediaPlayer(IjkLibLoader ijkLibLoader) {
        this.mWakeLock = null;
        initPlayer(ijkLibLoader);
    }

    private native String _getAudioCodecInfo();

    private static native String _getColorFormatName(int i);

    private native int _getLoopCount();

    private native Bundle _getMediaMeta();

    private native float _getPropertyFloat(int i, float f);

    private native long _getPropertyLong(int i, long j);

    private native String _getVideoCodecInfo();

    private native void _pause();

    private native void _release();

    private native void _reset();

    private native void _setAndroidIOCallback(IAndroidIO iAndroidIO);

    private native void _setDataSource(String str, String[] strArr, String[] strArr2);

    private native void _setDataSource(IMediaDataSource iMediaDataSource);

    private native void _setDataSourceFd(int i);

    private native void _setFrameAtTime(String str, long j, long j2, int i, int i2);

    private native void _setLoopCount(int i);

    private native void _setOption(int i, String str, long j);

    private native void _setOption(int i, String str, String str2);

    private native void _setPropertyFloat(int i, float f);

    private native void _setPropertyLong(int i, long j);

    private native void _setStreamSelected(int i, boolean z);

    private native void _setVideoSurface(Surface surface);

    private native void _start();

    private native void _stop();

    public static String getColorFormatName(int i) {
        return _getColorFormatName(i);
    }

    private static void initNativeOnce() {
        synchronized (IjkMediaPlayer.class) {
            if (!mIsNativeInitialized) {
                native_init();
                mIsNativeInitialized = true;
            }
        }
    }

    private void initPlayer(IjkLibLoader ijkLibLoader) {
        EventHandler eventHandler;
        loadLibrariesOnce(ijkLibLoader);
        initNativeOnce();
        Looper myLooper = Looper.myLooper();
        if (myLooper != null) {
            eventHandler = new EventHandler(this, myLooper);
        } else {
            Looper mainLooper = Looper.getMainLooper();
            if (mainLooper != null) {
                eventHandler = new EventHandler(this, mainLooper);
            } else {
                this.mEventHandler = null;
                native_setup(new WeakReference(this));
            }
        }
        this.mEventHandler = eventHandler;
        native_setup(new WeakReference(this));
    }

    public static void loadLibrariesOnce(IjkLibLoader ijkLibLoader) {
        synchronized (IjkMediaPlayer.class) {
            if (!mIsLibLoaded) {
                if (ijkLibLoader == null) {
                    ijkLibLoader = sLocalLibLoader;
                }
                ijkLibLoader.loadLibrary("ijkffmpeg");
                ijkLibLoader.loadLibrary("ijksdl");
                ijkLibLoader.loadLibrary("ijkplayer");
                mIsLibLoaded = true;
            }
        }
    }

    private native void native_finalize();

    private static native void native_init();

    private native void native_message_loop(Object obj);

    public static native void native_profileBegin(String str);

    public static native void native_profileEnd();

    public static native void native_setLogLevel(int i);

    private native void native_setup(Object obj);

    @CalledByNative
    private static boolean onNativeInvoke(Object obj, int i, Bundle bundle) {
        DebugLog.ifmt(TAG, "onNativeInvoke %d", Integer.valueOf(i));
        if (obj == null || !(obj instanceof WeakReference)) {
            throw new IllegalStateException("<null weakThiz>.onNativeInvoke()");
        }
        IjkMediaPlayer ijkMediaPlayer = (IjkMediaPlayer) ((WeakReference) obj).get();
        if (ijkMediaPlayer != null) {
            OnNativeInvokeListener onNativeInvokeListener = ijkMediaPlayer.mOnNativeInvokeListener;
            if (onNativeInvokeListener != null && onNativeInvokeListener.onNativeInvoke(i, bundle)) {
                return true;
            }
            if (i != 131079) {
                return false;
            }
            OnControlMessageListener onControlMessageListener = ijkMediaPlayer.mOnControlMessageListener;
            if (onControlMessageListener == null) {
                return false;
            }
            int i2 = bundle.getInt(OnNativeInvokeListener.ARG_SEGMENT_INDEX, -1);
            if (i2 >= 0) {
                String onControlResolveSegmentUrl = onControlMessageListener.onControlResolveSegmentUrl(i2);
                if (onControlResolveSegmentUrl != null) {
                    bundle.putString("url", onControlResolveSegmentUrl);
                    return true;
                }
                throw new RuntimeException(new IOException("onNativeInvoke() = <NULL newUrl>"));
            }
            throw new InvalidParameterException("onNativeInvoke(invalid segment index)");
        }
        throw new IllegalStateException("<null weakPlayer>.onNativeInvoke()");
    }

    @CalledByNative
    private static String onSelectCodec(Object obj, String str, int i, int i2) {
        if (obj == null || !(obj instanceof WeakReference)) {
            return null;
        }
        IjkMediaPlayer ijkMediaPlayer = (IjkMediaPlayer) ((WeakReference) obj).get();
        if (ijkMediaPlayer == null) {
            return null;
        }
        OnMediaCodecSelectListener onMediaCodecSelectListener = ijkMediaPlayer.mOnMediaCodecSelectListener;
        if (onMediaCodecSelectListener == null) {
            onMediaCodecSelectListener = DefaultMediaCodecSelector.sInstance;
        }
        return onMediaCodecSelectListener.onMediaCodecSelect(ijkMediaPlayer, str, i, i2);
    }

    @CalledByNative
    private static void postEventFromNative(Object obj, int i, int i2, int i3, Object obj2) {
        if (obj != null) {
            IjkMediaPlayer ijkMediaPlayer = (IjkMediaPlayer) ((WeakReference) obj).get();
            if (ijkMediaPlayer != null) {
                if (i == 200 && i2 == 2) {
                    ijkMediaPlayer.start();
                }
                EventHandler eventHandler = ijkMediaPlayer.mEventHandler;
                if (eventHandler != null) {
                    ijkMediaPlayer.mEventHandler.sendMessage(eventHandler.obtainMessage(i, i2, i3, obj2));
                }
            }
        }
    }

    private void setDataSource(FileDescriptor fileDescriptor, long j, long j2) {
        setDataSource(fileDescriptor);
    }

    /* access modifiers changed from: private */
    @SuppressLint({"Wakelock"})
    public void stayAwake(boolean z) {
        WakeLock wakeLock = this.mWakeLock;
        if (wakeLock != null) {
            if (z && !wakeLock.isHeld()) {
                this.mWakeLock.acquire();
            } else if (!z && this.mWakeLock.isHeld()) {
                this.mWakeLock.release();
            }
        }
        this.mStayAwake = z;
        updateSurfaceScreenOn();
    }

    private void updateSurfaceScreenOn() {
        SurfaceHolder surfaceHolder = this.mSurfaceHolder;
        if (surfaceHolder != null) {
            boolean z = this.mScreenOnWhilePlaying && this.mStayAwake;
            surfaceHolder.setKeepScreenOn(z);
        }
    }

    public native void _prepareAsync();

    public void deselectTrack(int i) {
        _setStreamSelected(i, false);
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        super.finalize();
        native_finalize();
    }

    public long getAsyncStatisticBufBackwards() {
        return _getPropertyLong(FFP_PROP_INT64_ASYNC_STATISTIC_BUF_BACKWARDS, 0);
    }

    public long getAsyncStatisticBufCapacity() {
        return _getPropertyLong(FFP_PROP_INT64_ASYNC_STATISTIC_BUF_CAPACITY, 0);
    }

    public long getAsyncStatisticBufForwards() {
        return _getPropertyLong(FFP_PROP_INT64_ASYNC_STATISTIC_BUF_FORWARDS, 0);
    }

    public long getAudioCachedBytes() {
        return _getPropertyLong(FFP_PROP_INT64_AUDIO_CACHED_BYTES, 0);
    }

    public long getAudioCachedDuration() {
        return _getPropertyLong(FFP_PROP_INT64_AUDIO_CACHED_DURATION, 0);
    }

    public long getAudioCachedPackets() {
        return _getPropertyLong(FFP_PROP_INT64_AUDIO_CACHED_PACKETS, 0);
    }

    public native int getAudioSessionId();

    public long getBitRate() {
        return _getPropertyLong(FFP_PROP_INT64_BIT_RATE, 0);
    }

    public long getCacheStatisticCountBytes() {
        return _getPropertyLong(FFP_PROP_INT64_CACHE_STATISTIC_COUNT_BYTES, 0);
    }

    public long getCacheStatisticFileForwards() {
        return _getPropertyLong(FFP_PROP_INT64_CACHE_STATISTIC_FILE_FORWARDS, 0);
    }

    public long getCacheStatisticFilePos() {
        return _getPropertyLong(FFP_PROP_INT64_CACHE_STATISTIC_FILE_POS, 0);
    }

    public long getCacheStatisticPhysicalPos() {
        return _getPropertyLong(FFP_PROP_INT64_CACHE_STATISTIC_PHYSICAL_POS, 0);
    }

    public native long getCurrentPosition();

    public String getDataSource() {
        return this.mDataSource;
    }

    public float getDropFrameRate() {
        return _getPropertyFloat(10007, 0.0f);
    }

    public native long getDuration();

    public long getFileSize() {
        return _getPropertyLong(FFP_PROP_INT64_LOGICAL_FILE_SIZE, 0);
    }

    public MediaInfo getMediaInfo() {
        MediaInfo mediaInfo = new MediaInfo();
        mediaInfo.mMediaPlayerName = "ijkplayer";
        String _getVideoCodecInfo = _getVideoCodecInfo();
        String str = "";
        String str2 = ",";
        if (!TextUtils.isEmpty(_getVideoCodecInfo)) {
            String[] split = _getVideoCodecInfo.split(str2);
            if (split.length >= 2) {
                mediaInfo.mVideoDecoder = split[0];
                mediaInfo.mVideoDecoderImpl = split[1];
            } else if (split.length >= 1) {
                mediaInfo.mVideoDecoder = split[0];
                mediaInfo.mVideoDecoderImpl = str;
            }
        }
        String _getAudioCodecInfo = _getAudioCodecInfo();
        if (!TextUtils.isEmpty(_getAudioCodecInfo)) {
            String[] split2 = _getAudioCodecInfo.split(str2);
            if (split2.length >= 2) {
                mediaInfo.mAudioDecoder = split2[0];
                mediaInfo.mAudioDecoderImpl = split2[1];
            } else if (split2.length >= 1) {
                mediaInfo.mAudioDecoder = split2[0];
                mediaInfo.mAudioDecoderImpl = str;
            }
        }
        try {
            mediaInfo.mMeta = IjkMediaMeta.parse(_getMediaMeta());
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return mediaInfo;
    }

    public Bundle getMediaMeta() {
        return _getMediaMeta();
    }

    public long getSeekLoadDuration() {
        return _getPropertyLong(FFP_PROP_INT64_LATEST_SEEK_LOAD_DURATION, 0);
    }

    public int getSelectedTrack(int i) {
        int i2;
        if (i == 1) {
            i2 = FFP_PROP_INT64_SELECTED_VIDEO_STREAM;
        } else if (i == 2) {
            i2 = FFP_PROP_INT64_SELECTED_AUDIO_STREAM;
        } else if (i != 3) {
            return -1;
        } else {
            i2 = FFP_PROP_INT64_SELECTED_TIMEDTEXT_STREAM;
        }
        return (int) _getPropertyLong(i2, -1);
    }

    public float getSpeed(float f) {
        return _getPropertyFloat(10003, 0.0f);
    }

    public long getTcpSpeed() {
        return _getPropertyLong(FFP_PROP_INT64_TCP_SPEED, 0);
    }

    public IjkTrackInfo[] getTrackInfo() {
        int i;
        Bundle mediaMeta = getMediaMeta();
        if (mediaMeta == null) {
            return null;
        }
        IjkMediaMeta parse = IjkMediaMeta.parse(mediaMeta);
        if (parse == null || parse.mStreams == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        Iterator it = parse.mStreams.iterator();
        while (it.hasNext()) {
            IjkStreamMeta ijkStreamMeta = (IjkStreamMeta) it.next();
            IjkTrackInfo ijkTrackInfo = new IjkTrackInfo(ijkStreamMeta);
            if (ijkStreamMeta.mType.equalsIgnoreCase("video")) {
                i = 1;
            } else if (ijkStreamMeta.mType.equalsIgnoreCase("audio")) {
                i = 2;
            } else if (ijkStreamMeta.mType.equalsIgnoreCase("timedtext")) {
                i = 3;
            } else {
                arrayList.add(ijkTrackInfo);
            }
            ijkTrackInfo.setTrackType(i);
            arrayList.add(ijkTrackInfo);
        }
        return (IjkTrackInfo[]) arrayList.toArray(new IjkTrackInfo[arrayList.size()]);
    }

    public long getTrafficStatisticByteCount() {
        return _getPropertyLong(FFP_PROP_INT64_TRAFFIC_STATISTIC_BYTE_COUNT, 0);
    }

    public long getVideoCachedBytes() {
        return _getPropertyLong(FFP_PROP_INT64_VIDEO_CACHED_BYTES, 0);
    }

    public long getVideoCachedDuration() {
        return _getPropertyLong(FFP_PROP_INT64_VIDEO_CACHED_DURATION, 0);
    }

    public long getVideoCachedPackets() {
        return _getPropertyLong(FFP_PROP_INT64_VIDEO_CACHED_PACKETS, 0);
    }

    public float getVideoDecodeFramesPerSecond() {
        return _getPropertyFloat(10001, 0.0f);
    }

    public int getVideoDecoder() {
        return (int) _getPropertyLong(FFP_PROP_INT64_VIDEO_DECODER, 0);
    }

    public int getVideoHeight() {
        return this.mVideoHeight;
    }

    public float getVideoOutputFramesPerSecond() {
        return _getPropertyFloat(10002, 0.0f);
    }

    public int getVideoSarDen() {
        return this.mVideoSarDen;
    }

    public int getVideoSarNum() {
        return this.mVideoSarNum;
    }

    public int getVideoWidth() {
        return this.mVideoWidth;
    }

    public void httphookReconnect() {
        _setPropertyLong(FFP_PROP_INT64_IMMEDIATE_RECONNECT, 1);
    }

    public boolean isLooping() {
        return _getLoopCount() != 1;
    }

    public boolean isPlayable() {
        return true;
    }

    public native boolean isPlaying();

    public void pause() {
        stayAwake(false);
        _pause();
    }

    public void prepareAsync() {
        _prepareAsync();
    }

    public void release() {
        stayAwake(false);
        updateSurfaceScreenOn();
        resetListeners();
        _release();
    }

    public void reset() {
        stayAwake(false);
        _reset();
        this.mEventHandler.removeCallbacksAndMessages(null);
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
    }

    public void resetListeners() {
        super.resetListeners();
        this.mOnMediaCodecSelectListener = null;
    }

    public native void seekTo(long j);

    public void selectTrack(int i) {
        _setStreamSelected(i, true);
    }

    public void setAndroidIOCallback(IAndroidIO iAndroidIO) {
        _setAndroidIOCallback(iAndroidIO);
    }

    public void setAudioStreamType(int i) {
    }

    public void setCacheShare(int i) {
        _setPropertyLong(FFP_PROP_INT64_SHARE_CACHE_DATA, (long) i);
    }

    public void setDataSource(Context context, Uri uri) {
        setDataSource(context, uri, (Map) null);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:31:0x007d, code lost:
        if (r0 == null) goto L_0x0085;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0080, code lost:
        if (r0 == null) goto L_0x0085;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0082, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0085, code lost:
        android.util.Log.d(TAG, "Couldn't open file on client side, trying server side");
        setDataSource(r9.toString(), r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0093, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @TargetApi(14)
    public void setDataSource(Context context, Uri uri, Map map) {
        String scheme = uri.getScheme();
        if (ComposerHelper.COMPOSER_PATH.equals(scheme)) {
            setDataSource(uri.getPath());
            return;
        }
        if ("content".equals(scheme)) {
            if ("settings".equals(uri.getAuthority())) {
                uri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.getDefaultType(uri));
                if (uri == null) {
                    throw new FileNotFoundException("Failed to resolve default ringtone");
                }
            }
        }
        AssetFileDescriptor assetFileDescriptor = null;
        try {
            assetFileDescriptor = context.getContentResolver().openAssetFileDescriptor(uri, "r");
            if (assetFileDescriptor == null) {
                if (assetFileDescriptor != null) {
                    assetFileDescriptor.close();
                }
                return;
            }
            if (assetFileDescriptor.getDeclaredLength() < 0) {
                setDataSource(assetFileDescriptor.getFileDescriptor());
            } else {
                setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getDeclaredLength());
            }
            if (assetFileDescriptor != null) {
                assetFileDescriptor.close();
            }
        } catch (SecurityException unused) {
        } catch (IOException unused2) {
        } catch (Throwable th) {
            if (assetFileDescriptor != null) {
                assetFileDescriptor.close();
            }
            throw th;
        }
    }

    @TargetApi(13)
    public void setDataSource(FileDescriptor fileDescriptor) {
        if (VERSION.SDK_INT < 12) {
            try {
                Field declaredField = fileDescriptor.getClass().getDeclaredField("descriptor");
                declaredField.setAccessible(true);
                _setDataSourceFd(declaredField.getInt(fileDescriptor));
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e2) {
                throw new RuntimeException(e2);
            }
        } else {
            ParcelFileDescriptor dup = ParcelFileDescriptor.dup(fileDescriptor);
            try {
                _setDataSourceFd(dup.getFd());
            } finally {
                dup.close();
            }
        }
    }

    public void setDataSource(String str) {
        this.mDataSource = str;
        _setDataSource(str, null, null);
    }

    public void setDataSource(String str, Map map) {
        if (map != null && !map.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Entry entry : map.entrySet()) {
                sb.append((String) entry.getKey());
                sb.append(":");
                if (!TextUtils.isEmpty((String) entry.getValue())) {
                    sb.append((String) entry.getValue());
                }
                sb.append("\r\n");
                setOption(1, "headers", sb.toString());
                setOption(1, "protocol_whitelist", "async,cache,crypto,file,http,https,ijkhttphook,ijkinject,ijklivehook,ijklongurl,ijksegment,ijktcphook,pipe,rtp,tcp,tls,udp,ijkurlhook,data");
            }
        }
        setDataSource(str);
    }

    public void setDataSource(IMediaDataSource iMediaDataSource) {
        _setDataSource(iMediaDataSource);
    }

    public void setDisplay(SurfaceHolder surfaceHolder) {
        this.mSurfaceHolder = surfaceHolder;
        _setVideoSurface(surfaceHolder != null ? surfaceHolder.getSurface() : null);
        updateSurfaceScreenOn();
    }

    public void setKeepInBackground(boolean z) {
    }

    public void setLogEnabled(boolean z) {
    }

    public void setLooping(boolean z) {
        boolean z2 = !z;
        setOption(4, "loop", z2 ? 1 : 0);
        _setLoopCount(z2);
    }

    public void setOnControlMessageListener(OnControlMessageListener onControlMessageListener) {
        this.mOnControlMessageListener = onControlMessageListener;
    }

    public void setOnMediaCodecSelectListener(OnMediaCodecSelectListener onMediaCodecSelectListener) {
        this.mOnMediaCodecSelectListener = onMediaCodecSelectListener;
    }

    public void setOnNativeInvokeListener(OnNativeInvokeListener onNativeInvokeListener) {
        this.mOnNativeInvokeListener = onNativeInvokeListener;
    }

    public void setOption(int i, String str, long j) {
        _setOption(i, str, j);
    }

    public void setOption(int i, String str, String str2) {
        _setOption(i, str, str2);
    }

    public void setScreenOnWhilePlaying(boolean z) {
        if (this.mScreenOnWhilePlaying != z) {
            if (z && this.mSurfaceHolder == null) {
                DebugLog.w(TAG, "setScreenOnWhilePlaying(true) is ineffective without a SurfaceHolder");
            }
            this.mScreenOnWhilePlaying = z;
            updateSurfaceScreenOn();
        }
    }

    public void setSpeed(float f) {
        _setPropertyFloat(10003, f);
    }

    public void setSurface(Surface surface) {
        if (this.mScreenOnWhilePlaying && surface != null) {
            DebugLog.w(TAG, "setScreenOnWhilePlaying(true) is ineffective for Surface");
        }
        this.mSurfaceHolder = null;
        _setVideoSurface(surface);
        updateSurfaceScreenOn();
    }

    public native void setVolume(float f, float f2);

    @SuppressLint({"Wakelock"})
    public void setWakeMode(Context context, int i) {
        boolean z;
        WakeLock wakeLock = this.mWakeLock;
        if (wakeLock != null) {
            if (wakeLock.isHeld()) {
                z = true;
                this.mWakeLock.release();
            } else {
                z = false;
            }
            this.mWakeLock = null;
        } else {
            z = false;
        }
        this.mWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(i | PropertyOptions.DELETE_EXISTING, IjkMediaPlayer.class.getName());
        this.mWakeLock.setReferenceCounted(false);
        if (z) {
            this.mWakeLock.acquire();
        }
    }

    public void start() {
        stayAwake(true);
        _start();
    }

    public void stop() {
        stayAwake(false);
        _stop();
    }
}
