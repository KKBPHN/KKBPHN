package net.majorkernelpanic.streaming.audio;

import android.media.MediaRecorder.OutputFormat;

public class AMRNBStream extends AudioStream {
    public AMRNBStream() {
        setAudioSource(5);
        try {
            setOutputFormat(OutputFormat.class.getField("RAW_AMR").getInt(null));
        } catch (Exception unused) {
            setOutputFormat(3);
        }
        setAudioEncoder(1);
    }

    public synchronized void configure() {
        super.configure();
        this.mMode = 1;
        this.mQuality = AudioQuality.copyOf(this.mRequestedQuality);
    }

    /* access modifiers changed from: protected */
    public void encodeWithMediaCodec() {
        super.encodeWithMediaRecorder();
    }

    public String getSessionDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("m=audio ");
        sb.append(String.valueOf(getDestinationPorts()[0]));
        sb.append(" RTP/AVP 96\r\na=rtpmap:96 AMR/8000\r\na=fmtp:96 octet-align=1;\r\n");
        return sb.toString();
    }

    public synchronized void start() {
        if (!this.mStreaming) {
            configure();
            super.start();
        }
    }
}
