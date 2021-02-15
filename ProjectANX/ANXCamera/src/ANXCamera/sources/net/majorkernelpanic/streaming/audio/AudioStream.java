package net.majorkernelpanic.streaming.audio;

import android.media.MediaRecorder;
import android.os.ParcelFileDescriptor.AutoCloseInputStream;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import net.majorkernelpanic.streaming.MediaStream;

public abstract class AudioStream extends MediaStream {
    protected int mAudioEncoder;
    protected int mAudioSource;
    protected int mOutputFormat;
    protected AudioQuality mQuality = AudioQuality.copyOf(this.mRequestedQuality);
    protected AudioQuality mRequestedQuality = AudioQuality.copyOf(AudioQuality.DEFAULT_AUDIO_QUALITY);

    public AudioStream() {
        setAudioSource(5);
    }

    /* access modifiers changed from: protected */
    public void encodeWithMediaRecorder() {
        InputStream inputStream;
        createSockets();
        StringBuilder sb = new StringBuilder();
        sb.append("Requested audio with ");
        sb.append(this.mQuality.bitRate / 1000);
        sb.append("kbps at ");
        sb.append(this.mQuality.samplingRate / 1000);
        sb.append("kHz");
        Log.v("MediaStream", sb.toString());
        this.mMediaRecorder = new MediaRecorder();
        this.mMediaRecorder.setAudioSource(this.mAudioSource);
        this.mMediaRecorder.setOutputFormat(this.mOutputFormat);
        this.mMediaRecorder.setAudioEncoder(this.mAudioEncoder);
        this.mMediaRecorder.setAudioChannels(1);
        this.mMediaRecorder.setAudioSamplingRate(this.mQuality.samplingRate);
        this.mMediaRecorder.setAudioEncodingBitRate(this.mQuality.bitRate);
        FileDescriptor fileDescriptor = MediaStream.sPipeApi == 2 ? this.mParcelWrite.getFileDescriptor() : this.mSender.getFileDescriptor();
        this.mMediaRecorder.setOutputFile(fileDescriptor);
        this.mMediaRecorder.setOutputFile(fileDescriptor);
        this.mMediaRecorder.prepare();
        this.mMediaRecorder.start();
        if (MediaStream.sPipeApi == 2) {
            inputStream = new AutoCloseInputStream(this.mParcelRead);
        } else {
            try {
                inputStream = this.mReceiver.getInputStream();
            } catch (IOException unused) {
                stop();
                throw new IOException("Something happened with the local sockets :/ Start failed !");
            }
        }
        this.mPacketizer.setInputStream(inputStream);
        this.mPacketizer.start();
        this.mStreaming = true;
    }

    public AudioQuality getAudioQuality() {
        return this.mQuality;
    }

    /* access modifiers changed from: protected */
    public void setAudioEncoder(int i) {
        this.mAudioEncoder = i;
    }

    public void setAudioQuality(AudioQuality audioQuality) {
        this.mRequestedQuality = audioQuality;
    }

    public void setAudioSource(int i) {
        this.mAudioSource = i;
    }

    /* access modifiers changed from: protected */
    public void setOutputFormat(int i) {
        this.mOutputFormat = i;
    }
}
