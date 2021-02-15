package com.xiaomi.camera.imagecodec;

import android.hardware.camera2.CaptureRequest.Key;
import android.util.Log;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public class CaptureRequestVendorTags {
    private static final String TAG = "CaptureRequestVendorTags";
    public static final VendorTag XIAOMI_JPEG_ORIENTATION = create(O000000o.INSTANCE, Integer.class);
    private static Constructor requestConstructor;

    static /* synthetic */ String O00oooo0() {
        return "xiaomi.jpeg.orientation";
    }

    private static VendorTag create(final Supplier supplier, final Class cls) {
        return new VendorTag() {
            /* access modifiers changed from: protected */
            public Key create() {
                return CaptureRequestVendorTags.requestKey(getName(), cls);
            }

            public String getName() {
                return (String) supplier.get();
            }
        };
    }

    static Key requestKey(String str, Class cls) {
        try {
            if (requestConstructor == null) {
                requestConstructor = Key.class.getConstructor(new Class[]{String.class, cls.getClass()});
                requestConstructor.setAccessible(true);
            }
            return (Key) requestConstructor.newInstance(new Object[]{str, cls});
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Cannot find/call Key constructor: ");
            sb.append(e.getMessage());
            Log.d(TAG, sb.toString());
            return null;
        }
    }
}
