package com.xiaomi.camera.core;

import android.media.Image;
import android.util.SparseIntArray;
import com.android.camera.log.Log;
import java.util.HashMap;
import java.util.Map;

public class ImageMemoryManager {
    private static final String TAG = "ImageMemoryManager";
    private static final int TOTAL_MAX_MEMORY_USAGE = 1073741824;
    private static int mUsedMemory;
    private SparseIntArray mHoldImageNumArray = new SparseIntArray();
    private Map mImagesMap = new HashMap();
    private int mMaxHoldImageNumber;
    private final Object mObjLock = new Object();

    class ImageInfo {
        int owner;
        int size;

        ImageInfo(int i, int i2) {
            this.owner = i;
            this.size = i2;
        }
    }

    public ImageMemoryManager(int i) {
        this.mMaxHoldImageNumber = i;
    }

    private int getHoldImageNumber(int i) {
        return this.mHoldImageNumArray.get(i);
    }

    private int getImageUsedMemory(Image image) {
        int remaining = image.getPlanes()[0].getBuffer().remaining();
        int format = image.getFormat();
        return format != 32 ? (format == 35 || (format != 256 && format == 842094169)) ? (int) (((double) remaining) * 1.5d) : remaining : remaining * 2;
    }

    private int getMaxHoldImageNumber() {
        int i = 0;
        for (int i2 = 0; i2 < this.mHoldImageNumArray.size(); i2++) {
            if (this.mHoldImageNumArray.valueAt(i2) > i) {
                i = this.mHoldImageNumArray.valueAt(i2);
            }
        }
        return i;
    }

    public static boolean isMemoryFull() {
        return mUsedMemory > TOTAL_MAX_MEMORY_USAGE;
    }

    public void holdAnImage(int i, Image image) {
        synchronized (this.mObjLock) {
            this.mHoldImageNumArray.put(i, this.mHoldImageNumArray.get(i) + 1);
            int imageUsedMemory = getImageUsedMemory(image);
            this.mImagesMap.put(image, new ImageInfo(i, imageUsedMemory));
            mUsedMemory += imageUsedMemory;
            Log.d(TAG, "holdAnImage: %s, queue_%d.size=%d", image, Integer.valueOf(i), Integer.valueOf(getHoldImageNumber(i)));
        }
    }

    public boolean needWaitImageClose() {
        boolean z;
        synchronized (this.mObjLock) {
            z = true;
            if (getMaxHoldImageNumber() < this.mMaxHoldImageNumber - 1) {
                z = false;
            }
        }
        return z;
    }

    public void releaseAnImage(Image image) {
        synchronized (this.mObjLock) {
            if (this.mImagesMap.containsKey(image)) {
                ImageInfo imageInfo = (ImageInfo) this.mImagesMap.get(image);
                int i = this.mHoldImageNumArray.get(imageInfo.owner);
                if (i > 0) {
                    this.mHoldImageNumArray.put(imageInfo.owner, i - 1);
                }
                mUsedMemory -= imageInfo.size;
                this.mImagesMap.remove(image);
                this.mObjLock.notifyAll();
                Log.d(TAG, "releaseAnImage: %s, queue_%d.size=%d", image, Integer.valueOf(imageInfo.owner), Integer.valueOf(getHoldImageNumber(imageInfo.owner)));
            } else {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("releaseAnImage: not hold image ");
                sb.append(image);
                Log.d(str, sb.toString());
            }
        }
    }

    public void waitImageCloseIfNeeded(int i) {
        synchronized (this.mObjLock) {
            while (getHoldImageNumber(i) >= this.mMaxHoldImageNumber - 1) {
                try {
                    Log.d(TAG, "waitImageCloseIfNeeded: wait E");
                    this.mObjLock.wait();
                    Log.d(TAG, "waitImageCloseIfNeeded: wait X");
                } catch (InterruptedException e) {
                    Log.w(TAG, "waitImageCloseIfNeeded: failed!", (Throwable) e);
                }
            }
        }
    }
}
