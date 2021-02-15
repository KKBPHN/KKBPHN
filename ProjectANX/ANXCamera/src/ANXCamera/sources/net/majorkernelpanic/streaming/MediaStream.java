package net.majorkernelpanic.streaming;

import android.annotation.SuppressLint;
import android.media.MediaCodec;
import android.media.MediaRecorder;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Build.VERSION;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.Random;
import net.majorkernelpanic.streaming.rtp.AbstractPacketizer;

public abstract class MediaStream implements Stream {
    public static final byte MODE_MEDIACODEC_API = 2;
    public static final byte MODE_MEDIACODEC_API_2 = 5;
    public static final byte MODE_MEDIARECORDER_API = 1;
    public static final byte PIPE_API_LS = 1;
    public static final byte PIPE_API_PFD = 2;
    protected static final String PREF_PREFIX = "libstreaming-";
    protected static final String TAG = "MediaStream";
    protected static final byte sPipeApi = (VERSION.SDK_INT > 20 ? (byte) 2 : 1);
    protected static byte sSuggestedMode = 5;
    protected byte mChannelIdentifier = 0;
    protected boolean mConfigured = false;
    protected InetAddress mDestination;
    private LocalServerSocket mLss = null;
    protected MediaCodec mMediaCodec;
    protected MediaRecorder mMediaRecorder;
    protected byte mMode;
    protected OutputStream mOutputStream = null;
    protected AbstractPacketizer mPacketizer = null;
    protected ParcelFileDescriptor[] mParcelFileDescriptors;
    protected ParcelFileDescriptor mParcelRead;
    protected ParcelFileDescriptor mParcelWrite;
    protected LocalSocket mReceiver;
    protected byte mRequestedMode;
    protected int mRtcpPort = 0;
    protected int mRtpPort = 0;
    protected LocalSocket mSender = null;
    private int mSocketId;
    protected boolean mStreaming = false;
    private int mTTL = 64;

    static {
        try {
            Class.forName("android.media.MediaCodec");
            Log.i(TAG, "Phone supports the MediaCoded API");
        } catch (ClassNotFoundException unused) {
            throw new UnsupportedOperationException("does not support the MediaCodec API");
        }
    }

    public MediaStream() {
        byte b = sSuggestedMode;
        this.mRequestedMode = b;
        this.mMode = b;
    }

    /* access modifiers changed from: protected */
    public void closeSockets() {
        if (sPipeApi == 1) {
            try {
                this.mReceiver.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                this.mSender.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                this.mLss.close();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            this.mLss = null;
            this.mSender = null;
            this.mReceiver = null;
            return;
        }
        try {
            if (this.mParcelRead != null) {
                this.mParcelRead.close();
            }
        } catch (Exception e4) {
            e4.printStackTrace();
        }
        try {
            if (this.mParcelWrite != null) {
                this.mParcelWrite.close();
            }
        } catch (Exception e5) {
            e5.printStackTrace();
        }
    }

    public synchronized void configure() {
        if (!this.mStreaming) {
            if (this.mPacketizer != null) {
                this.mPacketizer.setDestination(this.mDestination, this.mRtpPort, this.mRtcpPort);
                this.mPacketizer.getRtpSocket().setOutputStream(this.mOutputStream, this.mChannelIdentifier);
            }
            this.mMode = this.mRequestedMode;
            this.mConfigured = true;
        } else {
            throw new IllegalStateException("Can't be called while streaming.");
        }
    }

    /* access modifiers changed from: protected */
    public void createSockets() {
        String str;
        int i = 0;
        if (sPipeApi == 1) {
            while (true) {
                str = "net.majorkernelpanic.streaming-";
                if (i >= 10) {
                    break;
                }
                try {
                    this.mSocketId = new Random().nextInt();
                    StringBuilder sb = new StringBuilder();
                    sb.append(str);
                    sb.append(this.mSocketId);
                    this.mLss = new LocalServerSocket(sb.toString());
                    break;
                } catch (IOException unused) {
                    i++;
                }
            }
            this.mReceiver = new LocalSocket();
            LocalSocket localSocket = this.mReceiver;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(this.mSocketId);
            localSocket.connect(new LocalSocketAddress(sb2.toString()));
            this.mReceiver.setReceiveBufferSize(500000);
            this.mReceiver.setSoTimeout(3000);
            this.mSender = this.mLss.accept();
            this.mSender.setSendBufferSize(500000);
            return;
        }
        Log.e(TAG, "parcelFileDescriptors createPipe version = Lollipop");
        this.mParcelFileDescriptors = ParcelFileDescriptor.createPipe();
        this.mParcelRead = new ParcelFileDescriptor(this.mParcelFileDescriptors[0]);
        this.mParcelWrite = new ParcelFileDescriptor(this.mParcelFileDescriptors[1]);
    }

    public abstract void encodeWithMediaCodec();

    public abstract void encodeWithMediaRecorder();

    public long getBitrate() {
        if (!this.mStreaming) {
            return 0;
        }
        return this.mPacketizer.getRtpSocket().getBitrate();
    }

    public int[] getDestinationPorts() {
        return new int[]{this.mRtpPort, this.mRtcpPort};
    }

    public int[] getLocalPorts() {
        return this.mPacketizer.getRtpSocket().getLocalPorts();
    }

    public AbstractPacketizer getPacketizer() {
        return this.mPacketizer;
    }

    public int getSSRC() {
        return getPacketizer().getSSRC();
    }

    public abstract String getSessionDescription();

    public byte getStreamingMethod() {
        return this.mMode;
    }

    public boolean isStreaming() {
        return this.mStreaming;
    }

    public void setDestinationAddress(InetAddress inetAddress) {
        this.mDestination = inetAddress;
    }

    public void setDestinationPorts(int i) {
        if (i % 2 == 1) {
            this.mRtpPort = i - 1;
        } else {
            this.mRtpPort = i;
            i++;
        }
        this.mRtcpPort = i;
    }

    public void setDestinationPorts(int i, int i2) {
        this.mRtpPort = i;
        this.mRtcpPort = i2;
        this.mOutputStream = null;
    }

    public void setOutputStream(OutputStream outputStream, byte b) {
        this.mOutputStream = outputStream;
        this.mChannelIdentifier = b;
    }

    public void setStreamingMethod(byte b) {
        this.mRequestedMode = b;
    }

    public void setTimeToLive(int i) {
        this.mTTL = i;
    }

    public synchronized void start() {
        if (this.mDestination == null) {
            throw new IllegalStateException("No destination ip address set for the stream !");
        } else if (this.mRtpPort <= 0 || this.mRtcpPort <= 0) {
            throw new IllegalStateException("No destination ports set for the stream !");
        } else {
            this.mPacketizer.setTimeToLive(this.mTTL);
            if (this.mMode != 1) {
                encodeWithMediaCodec();
            } else {
                encodeWithMediaRecorder();
            }
        }
    }

    @SuppressLint({"NewApi"})
    public synchronized void stop() {
        if (this.mStreaming) {
            try {
                if (this.mMode == 1) {
                    this.mMediaRecorder.stop();
                    this.mMediaRecorder.release();
                    this.mMediaRecorder = null;
                    closeSockets();
                    this.mPacketizer.stop();
                } else {
                    this.mPacketizer.stop();
                    this.mMediaCodec.stop();
                    this.mMediaCodec.release();
                    this.mMediaCodec = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.mStreaming = false;
        }
    }
}
