package com.xiaomi.camera.device;

import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.os.HandlerThread;
import androidx.annotation.NonNull;
import com.android.camera2.Camera2Proxy;
import com.android.camera2.CameraCapabilities;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class CameraHandlerThread extends HandlerThread {
    private final CookieStore mCookieStore = new CookieStore();

    public final class Cookie {
        public Camera2Proxy mCamera2Device;
        public CameraCapabilities mCameraCapabilities;
        public final String mCameraId;
        public boolean mIsClosing;
        public boolean mIsOpening;
        public final Handler mStreamingHandler;

        private Cookie(String str) {
            this.mIsOpening = false;
            this.mIsClosing = false;
            StringBuilder sb = new StringBuilder();
            sb.append("Streaming (");
            sb.append(str);
            sb.append(")");
            HandlerThread handlerThread = new HandlerThread(sb.toString());
            handlerThread.start();
            this.mStreamingHandler = new Handler(handlerThread.getLooper());
            this.mCameraId = str;
        }
    }

    public final class CookieStore {
        public CameraManager mCameraManager;
        private final Map mCookies;

        private CookieStore() {
            this.mCookies = new LinkedHashMap();
        }

        static /* synthetic */ Cookie O000Oo00(String str) {
            return new Cookie(str);
        }

        @NonNull
        public Cookie getCookie(String str) {
            return (Cookie) this.mCookies.computeIfAbsent(str, O00000Oo.INSTANCE);
        }

        @NonNull
        public Set getCookies() {
            return Collections.unmodifiableSet(new HashSet(this.mCookies.values()));
        }
    }

    public CameraHandlerThread() {
        super(CameraHandlerThread.class.getSimpleName(), -2);
    }

    @NonNull
    public CookieStore getCookieStore() {
        return this.mCookieStore;
    }
}
