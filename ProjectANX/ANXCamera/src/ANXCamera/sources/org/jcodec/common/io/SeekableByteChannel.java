package org.jcodec.common.io;

import java.io.Closeable;
import java.nio.channels.ByteChannel;
import java.nio.channels.Channel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public interface SeekableByteChannel extends ByteChannel, Channel, Closeable, ReadableByteChannel, WritableByteChannel {
    long position();

    SeekableByteChannel setPosition(long j);

    long size();

    SeekableByteChannel truncate(long j);
}
