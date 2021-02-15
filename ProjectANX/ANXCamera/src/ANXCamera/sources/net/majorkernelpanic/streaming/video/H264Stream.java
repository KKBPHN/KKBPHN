package net.majorkernelpanic.streaming.video;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import net.majorkernelpanic.streaming.exceptions.ConfNotSupportedException;
import net.majorkernelpanic.streaming.exceptions.StorageUnavailableException;
import net.majorkernelpanic.streaming.hw.EncoderDebugger;
import net.majorkernelpanic.streaming.mp4.MP4Config;
import net.majorkernelpanic.streaming.rtp.H264Packetizer;

public class H264Stream extends VideoStream {
    public static final String TAG = "H264Stream";
    private MP4Config mConfig;
    /* access modifiers changed from: private */
    public Semaphore mLock;

    public H264Stream() {
        this(0);
    }

    public H264Stream(int i) {
        super(i);
        this.mLock = new Semaphore(0);
        this.mMimeType = "video/avc";
        this.mCameraImageFormat = 17;
        this.mVideoEncoder = 2;
        this.mPacketizer = new H264Packetizer();
    }

    private MP4Config testH264() {
        return this.mMode != 1 ? testMediaCodecAPI() : testMediaRecorderAPI();
    }

    @SuppressLint({"NewApi"})
    private MP4Config testMediaCodecAPI() {
        try {
            EncoderDebugger debug = EncoderDebugger.debug(this.mSettings, this.mQuality.resX, this.mQuality.resY);
            return new MP4Config(debug.getB64SPS(), debug.getB64PPS());
        } catch (Exception unused) {
            Log.e(TAG, "Resolution not supported with the MediaCodec API, we fallback on the old streamign method.");
            throw new UnsupportedOperationException("Resolution not supported with the MediaCodec API");
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0166, code lost:
        if (r7 != false) goto L_0x0168;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:?, code lost:
        startPreview();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0189, code lost:
        if (r7 == false) goto L_0x018c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private MP4Config testMediaRecorderAPI() {
        String sb;
        String str;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("libstreaming-h264-mr-");
        sb2.append(this.mRequestedQuality.framerate);
        String str2 = ",";
        sb2.append(str2);
        sb2.append(this.mRequestedQuality.resX);
        sb2.append(str2);
        sb2.append(this.mRequestedQuality.resY);
        String sb3 = sb2.toString();
        SharedPreferences sharedPreferences = this.mSettings;
        if (sharedPreferences != null && sharedPreferences.contains(sb3)) {
            String[] split = this.mSettings.getString(sb3, "").split(str2);
            return new MP4Config(split[0], split[1], split[2]);
        } else if (Environment.getExternalStorageState().equals("mounted")) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(Environment.getExternalStorageDirectory().getPath());
            sb4.append("/spydroid-test.mp4");
            sb = sb4.toString();
            StringBuilder sb5 = new StringBuilder();
            sb5.append("Testing H264 support... Test file saved at: ");
            sb5.append(sb);
            String sb6 = sb5.toString();
            str = TAG;
            Log.i(str, sb6);
            try {
                new File(sb).createNewFile();
                boolean z = this.mFlashEnabled;
                this.mFlashEnabled = false;
                boolean z2 = this.mPreviewStarted;
                boolean z3 = this.mCamera != null;
                createCamera();
                if (this.mPreviewStarted) {
                    lockCamera();
                    try {
                        this.mCamera.stopPreview();
                    } catch (Exception unused) {
                    }
                    this.mPreviewStarted = false;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                unlockCamera();
                try {
                    this.mMediaRecorder = new MediaRecorder();
                    this.mMediaRecorder.setCamera(this.mCamera);
                    this.mMediaRecorder.setVideoSource(1);
                    this.mMediaRecorder.setOutputFormat(1);
                    this.mMediaRecorder.setVideoEncoder(this.mVideoEncoder);
                    this.mMediaRecorder.setPreviewDisplay(this.mSurfaceView.getHolder().getSurface());
                    this.mMediaRecorder.setVideoSize(this.mRequestedQuality.resX, this.mRequestedQuality.resY);
                    this.mMediaRecorder.setVideoFrameRate(this.mRequestedQuality.framerate);
                    this.mMediaRecorder.setVideoEncodingBitRate((int) (((double) this.mRequestedQuality.bitrate) * 0.8d));
                    this.mMediaRecorder.setOutputFile(sb);
                    this.mMediaRecorder.setMaxDuration(3000);
                    this.mMediaRecorder.setOnInfoListener(new OnInfoListener() {
                        public void onInfo(MediaRecorder mediaRecorder, int i, int i2) {
                            String str = H264Stream.TAG;
                            Log.d(str, "MediaRecorder callback called !");
                            String str2 = i == 800 ? "MediaRecorder: MAX_DURATION_REACHED" : i == 801 ? "MediaRecorder: MAX_FILESIZE_REACHED" : i == 1 ? "MediaRecorder: INFO_UNKNOWN" : "WTF ?";
                            Log.d(str, str2);
                            H264Stream.this.mLock.release();
                        }
                    });
                    this.mMediaRecorder.prepare();
                    this.mMediaRecorder.start();
                    if (this.mLock.tryAcquire(6, TimeUnit.SECONDS)) {
                        Log.d(str, "MediaRecorder callback was called :)");
                        Thread.sleep(400);
                    } else {
                        Log.d(str, "MediaRecorder callback was not called after 6 seconds... :(");
                    }
                    try {
                        this.mMediaRecorder.stop();
                    } catch (Exception unused2) {
                    }
                    this.mMediaRecorder.release();
                    this.mMediaRecorder = null;
                    lockCamera();
                    if (!z3) {
                        destroyCamera();
                    }
                    this.mFlashEnabled = z;
                } catch (IOException e2) {
                    throw new ConfNotSupportedException(e2.getMessage());
                } catch (RuntimeException e3) {
                    throw new ConfNotSupportedException(e3.getMessage());
                } catch (InterruptedException e4) {
                    e4.printStackTrace();
                    try {
                        this.mMediaRecorder.stop();
                    } catch (Exception unused3) {
                    }
                    this.mMediaRecorder.release();
                    this.mMediaRecorder = null;
                    lockCamera();
                    if (!z3) {
                        destroyCamera();
                    }
                    this.mFlashEnabled = z;
                } catch (Throwable th) {
                    try {
                        this.mMediaRecorder.stop();
                    } catch (Exception unused4) {
                    }
                    this.mMediaRecorder.release();
                    this.mMediaRecorder = null;
                    lockCamera();
                    if (!z3) {
                        destroyCamera();
                    }
                    this.mFlashEnabled = z;
                    if (z2) {
                        try {
                            startPreview();
                        } catch (Exception unused5) {
                        }
                    }
                    throw th;
                }
            } catch (IOException e5) {
                throw new StorageUnavailableException(e5.getMessage());
            }
        } else {
            throw new StorageUnavailableException("No external storage or external storage not ready !");
        }
        MP4Config mP4Config = new MP4Config(sb);
        if (!new File(sb).delete()) {
            Log.e(str, "Temp file could not be erased");
        }
        Log.i(str, "H264 Test succeded...");
        SharedPreferences sharedPreferences2 = this.mSettings;
        if (sharedPreferences2 != null) {
            Editor edit = sharedPreferences2.edit();
            StringBuilder sb7 = new StringBuilder();
            sb7.append(mP4Config.getProfileLevel());
            sb7.append(str2);
            sb7.append(mP4Config.getB64SPS());
            sb7.append(str2);
            sb7.append(mP4Config.getB64PPS());
            edit.putString(sb3, sb7.toString());
            edit.commit();
        }
        return mP4Config;
    }

    public synchronized void configure() {
        super.configure();
        this.mMode = this.mRequestedMode;
        this.mQuality = VideoQuality.copyOf(this.mRequestedQuality);
        this.mConfig = testH264();
    }

    public synchronized String getSessionDescription() {
        StringBuilder sb;
        if (this.mConfig != null) {
            sb = new StringBuilder();
            sb.append("m=video ");
            sb.append(String.valueOf(getDestinationPorts()[0]));
            sb.append(" RTP/AVP 96\r\na=rtpmap:96 H264/90000\r\na=fmtp:96 packetization-mode=1;profile-level-id=");
            sb.append(this.mConfig.getProfileLevel());
            sb.append(";sprop-parameter-sets=");
            sb.append(this.mConfig.getB64SPS());
            sb.append(",");
            sb.append(this.mConfig.getB64PPS());
            sb.append(";\r\n");
        } else {
            throw new IllegalStateException("You need to call configure() first !");
        }
        return sb.toString();
    }

    public synchronized void start() {
        if (!this.mStreaming) {
            configure();
            ((H264Packetizer) this.mPacketizer).setStreamParameters(Base64.decode(this.mConfig.getB64PPS(), 2), Base64.decode(this.mConfig.getB64SPS(), 2));
            super.start();
        }
    }
}
