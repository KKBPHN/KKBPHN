package com.miui.internal.graphics.gif;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import miui.io.ResettableInputStream;

public class DecodeGifImageHelper {
    public static final int MESSAGE_WHAT_DECODE_FRAMES = 1;
    public Handler mDecodeFrameHandler;
    /* access modifiers changed from: private */
    public DecodeGifFrames mDecodeGifFrames;
    public boolean mDecodedAllFrames;
    public List mFrames = new ArrayList();
    public ResettableInputStream mGifSource;
    public long mMaxDecodeSize = PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED;
    private int mMaxFrames;
    public int mRealFrameCount;

    public class GifDecodeResult {
        public GifDecoder mGifDecoder;
        public boolean mIsDecodeOk;
    }

    public class GifFrame {
        public int mDuration;
        public Bitmap mImage;
        public int mIndex;

        public GifFrame(Bitmap bitmap, int i, int i2) {
            this.mImage = bitmap;
            this.mDuration = i;
            this.mIndex = i2;
        }
    }

    private int calcFrameIndex(int i) {
        int i2 = this.mRealFrameCount;
        return i2 == 0 ? i : i % i2;
    }

    public static GifDecodeResult decode(ResettableInputStream resettableInputStream, long j, int i) {
        GifDecodeResult gifDecodeResult = new GifDecodeResult();
        gifDecodeResult.mGifDecoder = null;
        boolean z = false;
        gifDecodeResult.mIsDecodeOk = false;
        try {
            resettableInputStream.reset();
            gifDecodeResult.mGifDecoder = new GifDecoder();
            GifDecoder gifDecoder = gifDecodeResult.mGifDecoder;
            gifDecoder.setStartFrame(i);
            gifDecoder.setMaxDecodeSize(j);
            if (gifDecoder.read(resettableInputStream) == 0) {
                z = true;
            }
            gifDecodeResult.mIsDecodeOk = z;
            resettableInputStream.close();
        } catch (IOException unused) {
        }
        return gifDecodeResult;
    }

    private int getLastFrameIndex() {
        List list = this.mFrames;
        return ((GifFrame) list.get(list.size() - 1)).mIndex;
    }

    public GifDecodeResult decodeFrom(int i) {
        return decode(this.mGifSource, this.mMaxDecodeSize, i);
    }

    public void decodeNextFrames() {
        int size = this.mFrames.size();
        int i = this.mMaxFrames;
        boolean z = false;
        if (i > 3 ? size <= i / 2 : size <= 2) {
            z = true;
        }
        if (z) {
            this.mDecodeGifFrames.decode(calcFrameIndex(getLastFrameIndex() + 1));
        }
    }

    public void destroy() {
        DecodeGifFrames decodeGifFrames = this.mDecodeGifFrames;
        if (decodeGifFrames != null) {
            decodeGifFrames.destroy();
        }
    }

    public void firstDecodeNextFrames() {
        this.mDecodeFrameHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message message) {
                if (message.what == 1) {
                    DecodeGifImageHelper decodeGifImageHelper = DecodeGifImageHelper.this;
                    if (decodeGifImageHelper.handleDecodeFramesResult(decodeGifImageHelper.mDecodeGifFrames.getAndClearDecodeResult())) {
                        DecodeGifImageHelper.this.decodeNextFrames();
                    }
                }
            }
        };
        this.mDecodeGifFrames = DecodeGifFrames.createInstance(this.mGifSource, this.mMaxDecodeSize, this.mDecodeFrameHandler);
        this.mMaxFrames = this.mFrames.size();
        decodeNextFrames();
    }

    public boolean handleDecodeFramesResult(GifDecodeResult gifDecodeResult) {
        if (gifDecodeResult.mIsDecodeOk) {
            GifDecoder gifDecoder = gifDecodeResult.mGifDecoder;
            if (gifDecoder != null) {
                Log.d("dumpFrameIndex", String.format("Thread#%d: decoded %d frames [%s] [%d]", new Object[]{Long.valueOf(Thread.currentThread().getId()), Integer.valueOf(gifDecodeResult.mGifDecoder.getFrameCount()), Boolean.valueOf(gifDecodeResult.mIsDecodeOk), Integer.valueOf(this.mRealFrameCount)}));
                if (gifDecoder.isDecodeToTheEnd()) {
                    this.mRealFrameCount = gifDecoder.getRealFrameCount();
                }
                int frameCount = gifDecoder.getFrameCount();
                if (frameCount > 0) {
                    int lastFrameIndex = getLastFrameIndex();
                    for (int i = 0; i < frameCount; i++) {
                        this.mFrames.add(new GifFrame(gifDecoder.getFrame(i), gifDecoder.getDelay(i), calcFrameIndex(lastFrameIndex + 1 + i)));
                    }
                }
                return true;
            }
        }
        return false;
    }
}
