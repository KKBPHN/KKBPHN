package com.android.camera.jcodec;

import java.nio.ByteBuffer;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.containers.mp4.boxes.Box;
import org.jcodec.containers.mp4.boxes.Header;

public class MCoverBox extends Box {
    private static final String FOURCC = "mcvr";
    private byte[] data;

    public MCoverBox(Header header) {
        super(header);
    }

    public static MCoverBox createCoverBox(byte[] bArr) {
        MCoverBox mCoverBox = new MCoverBox(Header.createHeader(FOURCC, 0));
        mCoverBox.data = bArr;
        return mCoverBox;
    }

    public static String fourcc() {
        return FOURCC;
    }

    /* access modifiers changed from: protected */
    public void doWrite(ByteBuffer byteBuffer) {
        byteBuffer.put(this.data);
    }

    public int estimateSize() {
        return this.data.length + 8;
    }

    public byte[] getData() {
        return this.data;
    }

    public void parse(ByteBuffer byteBuffer) {
        this.data = NIOUtils.toArray(NIOUtils.readBuf(byteBuffer));
    }
}
