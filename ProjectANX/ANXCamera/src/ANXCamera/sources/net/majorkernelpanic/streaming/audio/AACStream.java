package net.majorkernelpanic.streaming.audio;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OutputFormat;
import android.os.Build.VERSION;
import android.os.Environment;
import android.util.Log;
import com.android.camera.module.loader.FunctionParseBeautyBodySlimCount;
import com.xiaomi.asr.engine.record.AudioType.Frequency;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import net.majorkernelpanic.streaming.rtp.AACADTSPacketizer;
import net.majorkernelpanic.streaming.rtp.AACLATMPacketizer;
import net.majorkernelpanic.streaming.rtp.MediaCodecInputStream;
import tv.danmaku.ijk.media.player.IjkMediaMeta;
import tv.danmaku.ijk.media.player.misc.IMediaFormat;

public class AACStream extends AudioStream {
    private static final String[] AUDIO_OBJECT_TYPES = {"NULL", "AAC Main", "AAC LC (Low Complexity)", "AAC SSR (Scalable Sample Rate)", "AAC LTP (Long Term Prediction)"};
    public static final int[] AUDIO_SAMPLING_RATES = {96000, 88200, 64000, Frequency.FREQ_48KHZ, 44100, 32000, 24000, Frequency.FREQ_22KHZ, 16000, 12000, Frequency.FREQ_11KHZ, 8000, 7350, -1, -1, -1};
    public static final String TAG = "AACStream";
    /* access modifiers changed from: private */
    public AudioRecord mAudioRecord = null;
    private int mChannel;
    private int mConfig;
    private int mProfile;
    private int mSamplingRateIndex;
    private String mSessionDescription = null;
    private SharedPreferences mSettings = null;
    private Thread mThread = null;

    public AACStream() {
        boolean AACStreamingSupported = AACStreamingSupported();
        String str = TAG;
        if (AACStreamingSupported) {
            Log.d(str, "AAC supported on this phone");
        } else {
            Log.e(str, "AAC not supported on this phone");
            throw new RuntimeException("AAC not supported by this phone !");
        }
    }

    private static boolean AACStreamingSupported() {
        if (VERSION.SDK_INT < 14) {
            return false;
        }
        try {
            OutputFormat.class.getField("AAC_ADTS");
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0119, code lost:
        r6.read(r11, 1, 5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        r6.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0124, code lost:
        if (r3.delete() != false) goto L_0x0129;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0126, code lost:
        android.util.Log.e(r1, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0129, code lost:
        r14.mSamplingRateIndex = (r11[1] & 60) >> 2;
        r14.mProfile = ((r11[1] & -64) >> 6) + 1;
        r14.mChannel = ((r11[1] & 1) << 2) | ((r11[2] & -64) >> 6);
        r0 = r14.mQuality;
        r3 = AUDIO_SAMPLING_RATES;
        r4 = r14.mSamplingRateIndex;
        r0.samplingRate = r3[r4];
        r14.mConfig = (((r14.mProfile & 31) << 11) | ((r4 & 15) << 7)) | ((r14.mChannel & 15) << 3);
        r0 = new java.lang.StringBuilder();
        r0.append("MPEG VERSION: ");
        r0.append((r11[0] & 8) >> 3);
        android.util.Log.i(r1, r0.toString());
        r0 = new java.lang.StringBuilder();
        r0.append("PROTECTION: ");
        r0.append(r11[0] & 1);
        android.util.Log.i(r1, r0.toString());
        r0 = new java.lang.StringBuilder();
        r0.append("PROFILE: ");
        r0.append(AUDIO_OBJECT_TYPES[r14.mProfile]);
        android.util.Log.i(r1, r0.toString());
        r0 = new java.lang.StringBuilder();
        r0.append("SAMPLING FREQUENCY: ");
        r0.append(r14.mQuality.samplingRate);
        android.util.Log.i(r1, r0.toString());
        r0 = new java.lang.StringBuilder();
        r0.append("CHANNEL: ");
        r0.append(r14.mChannel);
        android.util.Log.i(r1, r0.toString());
        r0 = r14.mSettings;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x01dc, code lost:
        if (r0 == null) goto L_0x0208;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x01de, code lost:
        r0 = r0.edit();
        r1 = new java.lang.StringBuilder();
        r1.append(r14.mQuality.samplingRate);
        r1.append(r7);
        r1.append(r14.mConfig);
        r1.append(r7);
        r1.append(r14.mChannel);
        r0.putString(r5, r1.toString());
        r0.commit();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0208, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x020b, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:?, code lost:
        r6.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0214, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @SuppressLint({"InlinedApi"})
    private void testADTS() {
        String str = "Temp file could not be erased";
        String str2 = TAG;
        setAudioEncoder(3);
        try {
            setOutputFormat(OutputFormat.class.getField("AAC_ADTS").getInt(null));
        } catch (Exception unused) {
            setOutputFormat(6);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("libstreaming-aac-");
        sb.append(this.mQuality.samplingRate);
        String sb2 = sb.toString();
        SharedPreferences sharedPreferences = this.mSettings;
        String str3 = ",";
        if (sharedPreferences == null || !sharedPreferences.contains(sb2)) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(Environment.getExternalStorageDirectory().getPath());
            sb3.append("/spydroid-test.adts");
            String sb4 = sb3.toString();
            if (Environment.getExternalStorageState().equals("mounted")) {
                byte[] bArr = new byte[9];
                this.mMediaRecorder = new MediaRecorder();
                this.mMediaRecorder.setAudioSource(this.mAudioSource);
                this.mMediaRecorder.setOutputFormat(this.mOutputFormat);
                this.mMediaRecorder.setAudioEncoder(this.mAudioEncoder);
                this.mMediaRecorder.setAudioChannels(1);
                this.mMediaRecorder.setAudioSamplingRate(this.mQuality.samplingRate);
                this.mMediaRecorder.setAudioEncodingBitRate(this.mQuality.bitRate);
                this.mMediaRecorder.setOutputFile(sb4);
                this.mMediaRecorder.setMaxDuration(1000);
                this.mMediaRecorder.prepare();
                this.mMediaRecorder.start();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException unused2) {
                }
                this.mMediaRecorder.stop();
                this.mMediaRecorder.release();
                this.mMediaRecorder = null;
                File file = new File(sb4);
                try {
                    RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
                    while (true) {
                        if ((randomAccessFile.readByte() & -1) == -1) {
                            bArr[0] = randomAccessFile.readByte();
                            if ((bArr[0] & -16) == -16) {
                                break;
                            }
                        }
                    }
                } catch (Throwable th) {
                    if (!file.delete()) {
                        Log.e(str2, str);
                    }
                    throw th;
                }
            } else {
                throw new IllegalStateException("No external storage or external storage not ready !");
            }
        } else {
            String[] split = this.mSettings.getString(sb2, "").split(str3);
            this.mQuality.samplingRate = Integer.valueOf(split[0]).intValue();
            this.mConfig = Integer.valueOf(split[1]).intValue();
            this.mChannel = Integer.valueOf(split[2]).intValue();
        }
    }

    public synchronized void configure() {
        String sb;
        super.configure();
        this.mQuality = AudioQuality.copyOf(this.mRequestedQuality);
        int i = 0;
        while (true) {
            if (i >= AUDIO_SAMPLING_RATES.length) {
                break;
            } else if (AUDIO_SAMPLING_RATES[i] == this.mQuality.samplingRate) {
                this.mSamplingRateIndex = i;
                break;
            } else {
                i++;
            }
        }
        if (i > 12) {
            this.mQuality.samplingRate = 16000;
        }
        if (this.mMode != this.mRequestedMode || this.mPacketizer == null) {
            this.mMode = this.mRequestedMode;
            this.mPacketizer = this.mMode == 1 ? new AACADTSPacketizer() : new AACLATMPacketizer();
            this.mPacketizer.setDestination(this.mDestination, this.mRtpPort, this.mRtcpPort);
            this.mPacketizer.getRtpSocket().setOutputStream(this.mOutputStream, this.mChannelIdentifier);
        }
        if (this.mMode == 1) {
            testADTS();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("m=audio ");
            sb2.append(String.valueOf(getDestinationPorts()[0]));
            sb2.append(" RTP/AVP 96\r\na=rtpmap:96 mpeg4-generic/");
            sb2.append(this.mQuality.samplingRate);
            sb2.append("\r\na=fmtp:96 streamtype=5; profile-level-id=15; mode=AAC-hbr; config=");
            sb2.append(Integer.toHexString(this.mConfig));
            sb2.append("; SizeLength=13; IndexLength=3; IndexDeltaLength=3;\r\n");
            sb = sb2.toString();
        } else {
            this.mProfile = 2;
            this.mChannel = 1;
            this.mConfig = ((this.mProfile & 31) << 11) | ((this.mSamplingRateIndex & 15) << 7) | ((this.mChannel & 15) << 3);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("m=audio ");
            sb3.append(String.valueOf(getDestinationPorts()[0]));
            sb3.append(" RTP/AVP 96\r\na=rtpmap:96 mpeg4-generic/");
            sb3.append(this.mQuality.samplingRate);
            sb3.append("\r\na=fmtp:96 streamtype=5; profile-level-id=15; mode=AAC-hbr; config=");
            sb3.append(Integer.toHexString(this.mConfig));
            sb3.append("; SizeLength=13; IndexLength=3; IndexDeltaLength=3;\r\n");
            sb = sb3.toString();
        }
        this.mSessionDescription = sb;
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"InlinedApi", "NewApi"})
    public void encodeWithMediaCodec() {
        final int minBufferSize = AudioRecord.getMinBufferSize(this.mQuality.samplingRate, 16, 2) * 2;
        ((AACLATMPacketizer) this.mPacketizer).setSamplingRate(this.mQuality.samplingRate);
        AudioRecord audioRecord = new AudioRecord(1, this.mQuality.samplingRate, 16, 2, minBufferSize);
        this.mAudioRecord = audioRecord;
        String str = "audio/mp4a-latm";
        this.mMediaCodec = MediaCodec.createEncoderByType(str);
        MediaFormat mediaFormat = new MediaFormat();
        mediaFormat.setString(IMediaFormat.KEY_MIME, str);
        mediaFormat.setInteger(IjkMediaMeta.IJKM_KEY_BITRATE, this.mQuality.bitRate);
        mediaFormat.setInteger("channel-count", 1);
        mediaFormat.setInteger("sample-rate", this.mQuality.samplingRate);
        mediaFormat.setInteger("aac-profile", 2);
        mediaFormat.setInteger("max-input-size", minBufferSize);
        this.mMediaCodec.configure(mediaFormat, null, null, 1);
        this.mAudioRecord.startRecording();
        this.mMediaCodec.start();
        MediaCodecInputStream mediaCodecInputStream = new MediaCodecInputStream(this.mMediaCodec);
        final ByteBuffer[] inputBuffers = this.mMediaCodec.getInputBuffers();
        this.mThread = new Thread(new Runnable() {
            public void run() {
                while (!Thread.interrupted()) {
                    try {
                        int dequeueInputBuffer = AACStream.this.mMediaCodec.dequeueInputBuffer(FunctionParseBeautyBodySlimCount.TIP_INTERVAL_TIME);
                        if (dequeueInputBuffer >= 0) {
                            inputBuffers[dequeueInputBuffer].clear();
                            int read = AACStream.this.mAudioRecord.read(inputBuffers[dequeueInputBuffer], minBufferSize);
                            if (read != -3) {
                                if (read != -2) {
                                    AACStream.this.mMediaCodec.queueInputBuffer(dequeueInputBuffer, 0, read, System.nanoTime() / 1000, 0);
                                }
                            }
                            Log.e(AACStream.TAG, "An error occured with the AudioRecord API !");
                        }
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        });
        this.mThread.start();
        this.mPacketizer.setInputStream(mediaCodecInputStream);
        this.mPacketizer.start();
        this.mStreaming = true;
    }

    /* access modifiers changed from: protected */
    public void encodeWithMediaRecorder() {
        testADTS();
        ((AACADTSPacketizer) this.mPacketizer).setSamplingRate(this.mQuality.samplingRate);
        super.encodeWithMediaRecorder();
    }

    public String getSessionDescription() {
        String str = this.mSessionDescription;
        if (str != null) {
            return str;
        }
        throw new IllegalStateException("You need to call configure() first !");
    }

    public void setPreferences(SharedPreferences sharedPreferences) {
        this.mSettings = sharedPreferences;
    }

    public synchronized void start() {
        if (!this.mStreaming) {
            configure();
            super.start();
        }
    }

    public synchronized void stop() {
        if (this.mStreaming) {
            if (this.mMode == 2) {
                Log.d(TAG, "Interrupting threads...");
                this.mThread.interrupt();
                this.mAudioRecord.stop();
                this.mAudioRecord.release();
                this.mAudioRecord = null;
            }
            super.stop();
        }
    }
}
