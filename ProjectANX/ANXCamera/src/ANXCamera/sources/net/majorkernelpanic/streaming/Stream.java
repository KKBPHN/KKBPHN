package net.majorkernelpanic.streaming;

import java.io.OutputStream;
import java.net.InetAddress;

public interface Stream {
    void configure();

    long getBitrate();

    int[] getDestinationPorts();

    int[] getLocalPorts();

    int getSSRC();

    String getSessionDescription();

    boolean isStreaming();

    void setDestinationAddress(InetAddress inetAddress);

    void setDestinationPorts(int i);

    void setDestinationPorts(int i, int i2);

    void setOutputStream(OutputStream outputStream, byte b);

    void setTimeToLive(int i);

    void start();

    void stop();
}
