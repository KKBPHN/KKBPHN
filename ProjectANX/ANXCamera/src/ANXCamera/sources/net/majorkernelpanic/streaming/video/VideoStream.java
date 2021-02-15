package net.majorkernelpanic.streaming.video;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.ErrorCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaRecorder;
import android.os.Looper;
import android.os.ParcelFileDescriptor.AutoCloseInputStream;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import miui.text.ExtraTextUtils;
import net.majorkernelpanic.streaming.MediaStream;
import net.majorkernelpanic.streaming.exceptions.CameraInUseException;
import net.majorkernelpanic.streaming.exceptions.ConfNotSupportedException;
import net.majorkernelpanic.streaming.exceptions.InvalidSurfaceException;
import net.majorkernelpanic.streaming.gl.SurfaceView;
import net.majorkernelpanic.streaming.hw.EncoderDebugger;
import net.majorkernelpanic.streaming.hw.NV21Convertor;
import net.majorkernelpanic.streaming.rtp.MediaCodecInputStream;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

public abstract class VideoStream extends MediaStream {
    protected static final String TAG = "VideoStream";
    protected Camera mCamera;
    protected int mCameraId;
    protected int mCameraImageFormat;
    protected Looper mCameraLooper;
    protected boolean mCameraOpenedManually;
    protected Thread mCameraThread;
    protected Surface mCodecInputSurface;
    protected int mEncoderColorFormat;
    protected String mEncoderName;
    protected boolean mFlashEnabled;
    protected int mMaxFps;
    protected String mMimeType;
    protected int mOrientation;
    protected boolean mPreviewStarted;
    protected VideoQuality mQuality;
    protected int mRequestedOrientation;
    protected VideoQuality mRequestedQuality;
    protected SharedPreferences mSettings;
    protected Callback mSurfaceHolderCallback;
    protected boolean mSurfaceReady;
    protected SurfaceView mSurfaceView;
    protected boolean mUnlocked;
    protected boolean mUpdated;
    protected int mVideoEncoder;

    public VideoStream() {
        this(0);
    }

    @SuppressLint({"InlinedApi"})
    public VideoStream(int i) {
        this.mRequestedQuality = VideoQuality.copyOf(VideoQuality.DEFAULT_VIDEO_QUALITY);
        this.mQuality = VideoQuality.copyOf(this.mRequestedQuality);
        this.mSurfaceHolderCallback = null;
        this.mSurfaceView = null;
        this.mSettings = null;
        this.mCameraId = 0;
        this.mRequestedOrientation = 0;
        this.mOrientation = 0;
        this.mCameraOpenedManually = true;
        this.mFlashEnabled = false;
        this.mSurfaceReady = false;
        this.mUnlocked = false;
        this.mPreviewStarted = false;
        this.mUpdated = false;
        this.mMaxFps = 0;
        setCamera(i);
    }

    private void measureFramerate() {
        String str = ",";
        final Semaphore semaphore = new Semaphore(0);
        this.mCamera.setPreviewCallback(new PreviewCallback() {
            long count = 0;
            int i = 0;
            long now;
            long oldnow;
            int t = 0;

            public void onPreviewFrame(byte[] bArr, Camera camera) {
                this.i++;
                this.now = System.nanoTime() / 1000;
                if (this.i > 3) {
                    this.t = (int) (((long) this.t) + (this.now - this.oldnow));
                    this.count++;
                }
                if (this.i > 20) {
                    VideoStream.this.mQuality.framerate = (int) ((ExtraTextUtils.MB / (((long) this.t) / this.count)) + 1);
                    semaphore.release();
                }
                this.oldnow = this.now;
            }
        });
        try {
            semaphore.tryAcquire(2, TimeUnit.SECONDS);
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Actual framerate: ");
            sb.append(this.mQuality.framerate);
            Log.d(str2, sb.toString());
            if (this.mSettings != null) {
                Editor edit = this.mSettings.edit();
                StringBuilder sb2 = new StringBuilder();
                sb2.append("libstreaming-fps");
                sb2.append(this.mRequestedQuality.framerate);
                sb2.append(str);
                sb2.append(this.mCameraImageFormat);
                sb2.append(str);
                sb2.append(this.mRequestedQuality.resX);
                sb2.append(this.mRequestedQuality.resY);
                edit.putInt(sb2.toString(), this.mQuality.framerate);
                edit.commit();
            }
        } catch (InterruptedException unused) {
        }
        this.mCamera.setPreviewCallback(null);
    }

    private void openCamera() {
        final Semaphore semaphore = new Semaphore(0);
        final RuntimeException[] runtimeExceptionArr = new RuntimeException[1];
        this.mCameraThread = new Thread(new Runnable() {
            public void run() {
                Looper.prepare();
                VideoStream.this.mCameraLooper = Looper.myLooper();
                try {
                    VideoStream.this.mCamera = Camera.open(VideoStream.this.mCameraId);
                } catch (RuntimeException e) {
                    runtimeExceptionArr[0] = e;
                } catch (Throwable th) {
                    semaphore.release();
                    Looper.loop();
                    throw th;
                }
                semaphore.release();
                Looper.loop();
            }
        });
        this.mCameraThread.start();
        semaphore.acquireUninterruptibly();
        if (runtimeExceptionArr[0] != null) {
            throw new CameraInUseException(runtimeExceptionArr[0].getMessage());
        }
    }

    public synchronized void configure() {
        super.configure();
        this.mOrientation = this.mRequestedOrientation;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't wrap try/catch for region: R(3:25|26|27) */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0078, code lost:
        throw new net.majorkernelpanic.streaming.exceptions.InvalidSurfaceException("Invalid surface !");
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0071 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void createCamera() {
        if (this.mSurfaceView == null) {
            throw new InvalidSurfaceException("Invalid surface !");
        } else if (this.mSurfaceView.getHolder() == null || !this.mSurfaceReady) {
            throw new InvalidSurfaceException("Invalid surface !");
        } else if (this.mCamera == null) {
            openCamera();
            this.mUpdated = false;
            this.mUnlocked = false;
            this.mCamera.setErrorCallback(new ErrorCallback() {
                public void onError(int i, Camera camera) {
                    String str = VideoStream.TAG;
                    if (i == 100) {
                        Log.e(str, "Media server died !");
                        VideoStream videoStream = VideoStream.this;
                        videoStream.mCameraOpenedManually = false;
                        videoStream.stop();
                        return;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("Error unknown with the camera: ");
                    sb.append(i);
                    Log.e(str, sb.toString());
                }
            });
            try {
                Parameters parameters = this.mCamera.getParameters();
                if (parameters.getFlashMode() != null) {
                    parameters.setFlashMode(this.mFlashEnabled ? "torch" : "off");
                }
                parameters.setRecordingHint(true);
                this.mCamera.setParameters(parameters);
                this.mCamera.setDisplayOrientation(this.mOrientation);
                if (this.mMode == 5) {
                    this.mSurfaceView.startGLThread();
                    this.mCamera.setPreviewTexture(this.mSurfaceView.getSurfaceTexture());
                } else {
                    this.mCamera.setPreviewDisplay(this.mSurfaceView.getHolder());
                }
            } catch (RuntimeException e) {
                destroyCamera();
                throw e;
            }
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void destroyCamera() {
        if (this.mCamera != null) {
            if (this.mStreaming) {
                super.stop();
            }
            lockCamera();
            this.mCamera.stopPreview();
            try {
                this.mCamera.release();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage() != null ? e.getMessage() : "unknown error");
            }
            this.mCamera = null;
            this.mCameraLooper.quit();
            this.mUnlocked = false;
            this.mPreviewStarted = false;
        }
    }

    /* access modifiers changed from: protected */
    public void encodeWithMediaCodec() {
        if (this.mMode == 5) {
            encodeWithMediaCodecMethod2();
        } else {
            encodeWithMediaCodecMethod1();
        }
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"NewApi"})
    public void encodeWithMediaCodecMethod1() {
        Log.d(TAG, "Video encoded using the MediaCodec API with a buffer");
        createCamera();
        updateCamera();
        measureFramerate();
        if (!this.mPreviewStarted) {
            try {
                this.mCamera.startPreview();
                this.mPreviewStarted = true;
            } catch (RuntimeException e) {
                destroyCamera();
                throw e;
            }
        }
        SharedPreferences sharedPreferences = this.mSettings;
        VideoQuality videoQuality = this.mQuality;
        EncoderDebugger debug = EncoderDebugger.debug(sharedPreferences, videoQuality.resX, videoQuality.resY);
        final NV21Convertor nV21Convertor = debug.getNV21Convertor();
        this.mMediaCodec = MediaCodec.createByCodecName(debug.getEncoderName());
        VideoQuality videoQuality2 = this.mQuality;
        MediaFormat createVideoFormat = MediaFormat.createVideoFormat("video/avc", videoQuality2.resX, videoQuality2.resY);
        createVideoFormat.setInteger(IjkMediaMeta.IJKM_KEY_BITRATE, this.mQuality.bitrate);
        createVideoFormat.setInteger("frame-rate", this.mQuality.framerate);
        createVideoFormat.setInteger("color-format", debug.getEncoderColorFormat());
        createVideoFormat.setInteger("i-frame-interval", 1);
        this.mMediaCodec.configure(createVideoFormat, null, null, 1);
        this.mMediaCodec.start();
        AnonymousClass2 r0 = new PreviewCallback() {
            long i = 0;
            ByteBuffer[] inputBuffers = VideoStream.this.mMediaCodec.getInputBuffers();
            long now = (System.nanoTime() / 1000);
            long oldnow = this.now;

            public void onPreviewFrame(byte[] bArr, Camera camera) {
                this.oldnow = this.now;
                this.now = System.nanoTime() / 1000;
                long j = this.i;
                this.i = 1 + j;
                if (j > 3) {
                    this.i = 0;
                }
                try {
                    int dequeueInputBuffer = VideoStream.this.mMediaCodec.dequeueInputBuffer(500000);
                    String str = VideoStream.TAG;
                    if (dequeueInputBuffer >= 0) {
                        this.inputBuffers[dequeueInputBuffer].clear();
                        if (bArr == null) {
                            Log.e(str, "Symptom of the \"Callback buffer was to small\" problem...");
                        } else {
                            nV21Convertor.convert(bArr, this.inputBuffers[dequeueInputBuffer]);
                        }
                        VideoStream.this.mMediaCodec.queueInputBuffer(dequeueInputBuffer, 0, this.inputBuffers[dequeueInputBuffer].position(), this.now, 0);
                    } else {
                        Log.e(str, "No buffer available !");
                    }
                } finally {
                    VideoStream.this.mCamera.addCallbackBuffer(bArr);
                }
            }
        };
        for (int i = 0; i < 10; i++) {
            this.mCamera.addCallbackBuffer(new byte[nV21Convertor.getBufferSize()]);
        }
        this.mCamera.setPreviewCallbackWithBuffer(r0);
        this.mPacketizer.setInputStream(new MediaCodecInputStream(this.mMediaCodec));
        this.mPacketizer.start();
        this.mStreaming = true;
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"InlinedApi", "NewApi"})
    public void encodeWithMediaCodecMethod2() {
        Log.d(TAG, "Video encoded using the MediaCodec API with a surface");
        SharedPreferences sharedPreferences = this.mSettings;
        VideoQuality videoQuality = this.mQuality;
        this.mMediaCodec = MediaCodec.createByCodecName(EncoderDebugger.debug(sharedPreferences, videoQuality.resX, videoQuality.resY).getEncoderName());
        VideoQuality videoQuality2 = this.mQuality;
        MediaFormat createVideoFormat = MediaFormat.createVideoFormat("video/avc", videoQuality2.resX, videoQuality2.resY);
        createVideoFormat.setInteger(IjkMediaMeta.IJKM_KEY_BITRATE, this.mQuality.bitrate);
        createVideoFormat.setInteger("frame-rate", this.mQuality.framerate);
        createVideoFormat.setInteger("color-format", 2130708361);
        createVideoFormat.setInteger("i-frame-interval", 1);
        this.mMediaCodec.configure(createVideoFormat, null, null, 1);
        Surface createInputSurface = this.mMediaCodec.createInputSurface();
        this.mMediaCodec.start();
        this.mPacketizer.setInputStream(new MediaCodecInputStream(this.mMediaCodec));
        this.mPacketizer.start();
        this.mStreaming = true;
        this.mCodecInputSurface = createInputSurface;
    }

    /* access modifiers changed from: protected */
    public void encodeWithMediaRecorder() {
        String str = TAG;
        Log.d(str, "Video encoded using the MediaRecorder API");
        createSockets();
        destroyCamera();
        createCamera();
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
            this.mMediaRecorder.setOutputFile(MediaStream.sPipeApi == 2 ? this.mParcelWrite.getFileDescriptor() : this.mSender.getFileDescriptor());
            this.mMediaRecorder.prepare();
            this.mMediaRecorder.start();
            InputStream autoCloseInputStream = MediaStream.sPipeApi == 2 ? new AutoCloseInputStream(this.mParcelRead) : this.mReceiver.getInputStream();
            try {
                byte[] bArr = new byte[4];
                while (!Thread.interrupted()) {
                    while (autoCloseInputStream.read() != 109) {
                    }
                    autoCloseInputStream.read(bArr, 0, 3);
                    if (bArr[0] == 100 && bArr[1] == 97 && bArr[2] == 116) {
                        break;
                    }
                }
                this.mPacketizer.setInputStream(autoCloseInputStream);
                this.mPacketizer.start();
                this.mStreaming = true;
            } catch (IOException e) {
                Log.e(str, "Couldn't skip mp4 header :/");
                stop();
                throw e;
            }
        } catch (Exception e2) {
            throw new ConfNotSupportedException(e2.getMessage());
        }
    }

    public int getCamera() {
        return this.mCameraId;
    }

    public Surface getCodecInputSurface() {
        return this.mCodecInputSurface;
    }

    public boolean getFlashState() {
        return this.mFlashEnabled;
    }

    public abstract String getSessionDescription();

    public VideoQuality getVideoQuality() {
        return this.mRequestedQuality;
    }

    /* access modifiers changed from: protected */
    public void lockCamera() {
        if (this.mUnlocked) {
            String str = TAG;
            Log.d(str, "Locking camera");
            try {
                this.mCamera.reconnect();
            } catch (Exception e) {
                Log.e(str, e.getMessage());
            }
            this.mUnlocked = false;
        }
    }

    public void setCamera(int i) {
        CameraInfo cameraInfo = new CameraInfo();
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i2 = 0; i2 < numberOfCameras; i2++) {
            Camera.getCameraInfo(i2, cameraInfo);
            if (cameraInfo.facing == i) {
                this.mCameraId = i2;
                return;
            }
        }
    }

    public synchronized void setFlashState(boolean z) {
        if (this.mCamera != null) {
            if (this.mStreaming && this.mMode == 1) {
                lockCamera();
            }
            Parameters parameters = this.mCamera.getParameters();
            if (parameters.getFlashMode() != null) {
                parameters.setFlashMode(z ? "torch" : "off");
                try {
                    this.mCamera.setParameters(parameters);
                    this.mFlashEnabled = z;
                    if (this.mStreaming && this.mMode == 1) {
                        unlockCamera();
                    }
                } catch (RuntimeException unused) {
                    this.mFlashEnabled = false;
                    throw new RuntimeException("Can't turn the flash on !");
                } catch (Throwable th) {
                    if (this.mStreaming && this.mMode == 1) {
                        unlockCamera();
                    }
                    throw th;
                }
            } else {
                throw new RuntimeException("Can't turn the flash on !");
            }
        } else {
            this.mFlashEnabled = z;
        }
    }

    public void setPreferences(SharedPreferences sharedPreferences) {
        this.mSettings = sharedPreferences;
    }

    public void setPreviewOrientation(int i) {
        this.mRequestedOrientation = i;
        this.mUpdated = false;
    }

    public synchronized void setSurfaceView(SurfaceView surfaceView) {
        this.mSurfaceView = surfaceView;
        if (!(this.mSurfaceHolderCallback == null || this.mSurfaceView == null || this.mSurfaceView.getHolder() == null)) {
            this.mSurfaceView.getHolder().removeCallback(this.mSurfaceHolderCallback);
        }
        if (!(this.mSurfaceView == null || this.mSurfaceView.getHolder() == null)) {
            this.mSurfaceHolderCallback = new Callback() {
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
                    Log.d(VideoStream.TAG, "Surface Changed !");
                }

                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    VideoStream.this.mSurfaceReady = true;
                }

                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    VideoStream videoStream = VideoStream.this;
                    videoStream.mSurfaceReady = false;
                    videoStream.stopPreview();
                    Log.d(VideoStream.TAG, "Surface destroyed !");
                }
            };
            this.mSurfaceView.getHolder().addCallback(this.mSurfaceHolderCallback);
            this.mSurfaceReady = true;
        }
    }

    public void setVideoQuality(VideoQuality videoQuality) {
        if (!this.mRequestedQuality.equals(videoQuality)) {
            this.mRequestedQuality = VideoQuality.copyOf(videoQuality);
            this.mUpdated = false;
        }
    }

    public synchronized void start() {
        if (!this.mPreviewStarted) {
            this.mCameraOpenedManually = false;
        }
        super.start();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Stream configuration: FPS: ");
        sb.append(this.mQuality.framerate);
        sb.append(" Width: ");
        sb.append(this.mQuality.resX);
        sb.append(" Height: ");
        sb.append(this.mQuality.resY);
        Log.d(str, sb.toString());
    }

    public synchronized void startPreview() {
        this.mCameraOpenedManually = true;
        if (!this.mPreviewStarted) {
            createCamera();
            updateCamera();
        }
    }

    public synchronized void stop() {
        if (this.mCamera != null) {
            if (this.mMode == 2) {
                this.mCamera.setPreviewCallbackWithBuffer(null);
            }
            if (this.mMode == 5) {
                this.mSurfaceView.removeMediaCodecSurface();
            }
            super.stop();
            if (!this.mCameraOpenedManually) {
                destroyCamera();
            } else {
                try {
                    startPreview();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        } else {
            super.stop();
        }
    }

    public synchronized void stopPreview() {
        this.mCameraOpenedManually = false;
        stop();
    }

    public void switchCamera() {
        int i = 1;
        if (Camera.getNumberOfCameras() != 1) {
            boolean z = this.mStreaming;
            boolean z2 = this.mCamera != null && this.mCameraOpenedManually;
            if (this.mCameraId != 0) {
                i = 0;
            }
            this.mCameraId = i;
            setCamera(this.mCameraId);
            stopPreview();
            this.mFlashEnabled = false;
            if (z2) {
                startPreview();
            }
            if (z) {
                start();
                return;
            }
            return;
        }
        throw new IllegalStateException("Phone only has one camera !");
    }

    public synchronized void toggleFlash() {
        setFlashState(!this.mFlashEnabled);
    }

    /* access modifiers changed from: protected */
    public void unlockCamera() {
        if (!this.mUnlocked) {
            String str = TAG;
            Log.d(str, "Unlocking camera");
            try {
                this.mCamera.unlock();
            } catch (Exception e) {
                Log.e(str, e.getMessage());
            }
            this.mUnlocked = true;
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void updateCamera() {
        if (!this.mUpdated) {
            if (this.mPreviewStarted) {
                this.mPreviewStarted = false;
                this.mCamera.stopPreview();
            }
            Parameters parameters = this.mCamera.getParameters();
            this.mQuality = VideoQuality.determineClosestSupportedResolution(parameters, this.mQuality);
            int[] determineMaximumSupportedFramerate = VideoQuality.determineMaximumSupportedFramerate(parameters);
            this.mSurfaceView.requestAspectRatio(((double) this.mQuality.resX) / ((double) this.mQuality.resY));
            parameters.setPreviewFormat(this.mCameraImageFormat);
            parameters.setPreviewSize(this.mQuality.resX, this.mQuality.resY);
            parameters.setPreviewFpsRange(determineMaximumSupportedFramerate[0], determineMaximumSupportedFramerate[1]);
            try {
                this.mCamera.setParameters(parameters);
                this.mCamera.setDisplayOrientation(this.mOrientation);
                this.mCamera.startPreview();
                this.mPreviewStarted = true;
                this.mUpdated = true;
            } catch (RuntimeException e) {
                destroyCamera();
                throw e;
            }
        }
    }
}
