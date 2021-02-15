package net.majorkernelpanic.streaming.video;

import net.majorkernelpanic.streaming.rtp.H263Packetizer;

public class H263Stream extends VideoStream {
    public H263Stream() {
        this(0);
    }

    public H263Stream(int i) {
        super(i);
        this.mCameraImageFormat = 17;
        this.mVideoEncoder = 1;
        this.mPacketizer = new H263Packetizer();
    }

    public synchronized void configure() {
        super.configure();
        this.mMode = 1;
        this.mQuality = VideoQuality.copyOf(this.mRequestedQuality);
    }

    public String getSessionDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("m=video ");
        sb.append(String.valueOf(getDestinationPorts()[0]));
        sb.append(" RTP/AVP 96\r\na=rtpmap:96 H263-1998/90000\r\n");
        return sb.toString();
    }

    public synchronized void start() {
        if (!this.mStreaming) {
            configure();
            super.start();
        }
    }
}
