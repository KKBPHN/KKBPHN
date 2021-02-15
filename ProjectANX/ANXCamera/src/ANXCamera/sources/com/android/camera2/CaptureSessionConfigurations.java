package com.android.camera2;

import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.CaptureRequest.Key;
import com.android.camera.lib.compatibility.related.v30.V30Utils;
import com.android.camera2.vendortag.VendorTag;
import com.android.camera2.vendortag.VendorTagHelper;
import java.util.HashMap;
import java.util.Map;

public class CaptureSessionConfigurations {
    private final CameraCapabilities mCapabilities;
    private final Map mSessionParameters = new HashMap();

    public CaptureSessionConfigurations(CameraCapabilities cameraCapabilities) {
        this.mCapabilities = cameraCapabilities;
    }

    static /* synthetic */ void O000000o(Builder builder, Object obj, Object obj2) {
        if (obj instanceof VendorTag) {
            VendorTagHelper.setValueSafely(builder, (VendorTag) obj, obj2);
        } else if (obj instanceof Key) {
            builder.set((Key) obj, obj2);
        } else if (obj instanceof String) {
            V30Utils.applySessionKey(builder, (String) obj, obj2);
            throw null;
        }
    }

    public synchronized void apply(Builder builder) {
        this.mSessionParameters.forEach(new O00000Oo(builder));
    }

    public synchronized Object get(Key key) {
        return this.mSessionParameters.get(key);
    }

    public synchronized Object get(VendorTag vendorTag) {
        return this.mSessionParameters.get(vendorTag);
    }

    public synchronized Object get(String str) {
        return this.mSessionParameters.get(str);
    }

    public synchronized void reset() {
        this.mSessionParameters.clear();
    }

    public synchronized void set(Key key, Object obj) {
        if (key == null || obj == null) {
            throw new IllegalArgumentException("Both key and value are must not be null");
        } else if (this.mCapabilities != null && this.mCapabilities.isTagDefined(key.getName())) {
            this.mSessionParameters.put(key, obj);
        }
    }

    public synchronized void set(VendorTag vendorTag, Object obj) {
        if (vendorTag == null || obj == null) {
            throw new IllegalArgumentException("Both key and value are must not be null");
        } else if (this.mCapabilities != null && this.mCapabilities.isTagDefined(vendorTag.getName())) {
            this.mSessionParameters.put(vendorTag, obj);
        }
    }

    public synchronized void set(String str, Object obj) {
        if (str == null || obj == null) {
            throw new IllegalArgumentException("Both key and value are must not be null");
        } else if (this.mCapabilities != null) {
            this.mSessionParameters.put(str, obj);
        }
    }
}
