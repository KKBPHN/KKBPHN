package com.android.zxing;

import android.media.Image;
import com.android.camera2.Camera2Proxy;
import com.android.camera2.Camera2Proxy.PreviewCallback;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class PreviewDecodeManager {
    public static final int DECODE_TYPE_DOCUMENT = 3;
    public static final int DECODE_TYPE_GOOGLE_LENS = 2;
    public static final int DECODE_TYPE_HAND_GESTURE = 1;
    public static final int DECODE_TYPE_QR = 0;
    /* access modifiers changed from: private */
    public ConcurrentHashMap mDecoders;
    private PreviewCallback mPreviewCallback;

    class Singleton {
        public static final PreviewDecodeManager INSTANCE = new PreviewDecodeManager();

        private Singleton() {
        }
    }

    private PreviewDecodeManager() {
        this.mPreviewCallback = new PreviewCallback() {
            public boolean onPreviewFrame(Image image, Camera2Proxy camera2Proxy, int i) {
                for (Entry value : PreviewDecodeManager.this.mDecoders.entrySet()) {
                    Decoder decoder = (Decoder) value.getValue();
                    if (decoder.needPreviewFrame()) {
                        if (decoder.isNeedImage()) {
                            decoder.onPreviewFrame(image);
                        } else {
                            decoder.onPreviewFrame(new PreviewImage(image, i));
                        }
                    }
                }
                return true;
            }
        };
        this.mDecoders = new ConcurrentHashMap();
    }

    public static PreviewDecodeManager getInstance() {
        return Singleton.INSTANCE;
    }

    public Decoder getDecoder(int i) {
        return (Decoder) this.mDecoders.get(Integer.valueOf(i));
    }

    public PreviewCallback getPreviewCallback() {
        return this.mPreviewCallback;
    }

    public String getScanResult() {
        return ((QrDecoder) this.mDecoders.get(Integer.valueOf(0))).getScanResult();
    }

    public void init(int i, int i2) {
        Object obj;
        int i3;
        if (i2 != 0) {
            i3 = 1;
            if (i2 != 1) {
                i3 = 2;
                if (i2 != 2) {
                    i3 = 3;
                    if (i2 == 3 && this.mDecoders.get(Integer.valueOf(3)) == null) {
                        DocumentDecoder documentDecoder = new DocumentDecoder();
                        documentDecoder.init(i);
                        obj = documentDecoder;
                    } else {
                        return;
                    }
                } else if (this.mDecoders.get(Integer.valueOf(2)) == null) {
                    GoogleLensDecoder googleLensDecoder = new GoogleLensDecoder();
                    googleLensDecoder.init(i);
                    obj = googleLensDecoder;
                } else {
                    return;
                }
            } else if (this.mDecoders.get(Integer.valueOf(1)) == null) {
                HandGestureDecoder handGestureDecoder = new HandGestureDecoder();
                handGestureDecoder.init(i);
                obj = handGestureDecoder;
            } else {
                return;
            }
        } else {
            i3 = 0;
            if (this.mDecoders.get(Integer.valueOf(0)) == null) {
                QrDecoder qrDecoder = new QrDecoder();
                qrDecoder.init(i);
                obj = qrDecoder;
            } else {
                return;
            }
        }
        this.mDecoders.put(Integer.valueOf(i3), obj);
    }

    public void quit() {
        for (Decoder decoder : this.mDecoders.values()) {
            decoder.stopDecode();
            decoder.quit();
        }
        this.mDecoders.clear();
    }

    public void reset() {
        for (Decoder reset : this.mDecoders.values()) {
            reset.reset();
        }
    }

    public void resetScanResult() {
        Decoder decoder = (Decoder) this.mDecoders.get(Integer.valueOf(0));
        if (decoder != null) {
            ((QrDecoder) decoder).resetScanResult();
        }
    }

    public void startDecode() {
        for (Decoder startDecode : this.mDecoders.values()) {
            startDecode.startDecode();
        }
    }

    public void stopDecode(int i) {
        Decoder decoder = (Decoder) this.mDecoders.get(Integer.valueOf(i));
        if (decoder != null) {
            decoder.stopDecode();
        }
    }
}
