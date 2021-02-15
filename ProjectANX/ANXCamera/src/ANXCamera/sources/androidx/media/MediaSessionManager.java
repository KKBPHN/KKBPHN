package androidx.media;

import android.content.Context;
import android.os.Build.VERSION;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

public final class MediaSessionManager {
    static final boolean DEBUG = Log.isLoggable(TAG, 3);
    static final String TAG = "MediaSessionManager";
    private static final Object sLock = new Object();
    private static volatile MediaSessionManager sSessionManager;
    MediaSessionManagerImpl mImpl;

    interface MediaSessionManagerImpl {
        Context getContext();

        boolean isTrustedForMediaControl(RemoteUserInfoImpl remoteUserInfoImpl);
    }

    public final class RemoteUserInfo {
        public static final String LEGACY_CONTROLLER = "android.media.session.MediaController";
        @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
        public static final int UNKNOWN_PID = -1;
        @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
        public static final int UNKNOWN_UID = -1;
        RemoteUserInfoImpl mImpl;

        @RequiresApi(28)
        @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
        public RemoteUserInfo(android.media.session.MediaSessionManager.RemoteUserInfo remoteUserInfo) {
            this.mImpl = new RemoteUserInfoImplApi28(remoteUserInfo);
        }

        public RemoteUserInfo(@NonNull String str, int i, int i2) {
            this.mImpl = VERSION.SDK_INT >= 28 ? new RemoteUserInfoImplApi28(str, i, i2) : new RemoteUserInfoImplBase(str, i, i2);
        }

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof RemoteUserInfo)) {
                return false;
            }
            return this.mImpl.equals(((RemoteUserInfo) obj).mImpl);
        }

        @NonNull
        public String getPackageName() {
            return this.mImpl.getPackageName();
        }

        public int getPid() {
            return this.mImpl.getPid();
        }

        public int getUid() {
            return this.mImpl.getUid();
        }

        public int hashCode() {
            return this.mImpl.hashCode();
        }
    }

    interface RemoteUserInfoImpl {
        String getPackageName();

        int getPid();

        int getUid();
    }

    private MediaSessionManager(Context context) {
        int i = VERSION.SDK_INT;
        MediaSessionManagerImpl mediaSessionManagerImplBase = i >= 28 ? new MediaSessionManagerImplApi28(context) : i >= 21 ? new MediaSessionManagerImplApi21(context) : new MediaSessionManagerImplBase(context);
        this.mImpl = mediaSessionManagerImplBase;
    }

    @NonNull
    public static MediaSessionManager getSessionManager(@NonNull Context context) {
        MediaSessionManager mediaSessionManager;
        if (context != null) {
            synchronized (sLock) {
                if (sSessionManager == null) {
                    sSessionManager = new MediaSessionManager(context.getApplicationContext());
                }
                mediaSessionManager = sSessionManager;
            }
            return mediaSessionManager;
        }
        throw new IllegalArgumentException("context cannot be null");
    }

    /* access modifiers changed from: 0000 */
    public Context getContext() {
        return this.mImpl.getContext();
    }

    public boolean isTrustedForMediaControl(@NonNull RemoteUserInfo remoteUserInfo) {
        if (remoteUserInfo != null) {
            return this.mImpl.isTrustedForMediaControl(remoteUserInfo.mImpl);
        }
        throw new IllegalArgumentException("userInfo should not be null");
    }
}
