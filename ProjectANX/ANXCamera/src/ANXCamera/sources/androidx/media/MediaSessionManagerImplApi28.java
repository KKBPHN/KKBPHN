package androidx.media;

import android.content.Context;
import android.media.session.MediaSessionManager;
import android.media.session.MediaSessionManager.RemoteUserInfo;
import androidx.annotation.RequiresApi;

@RequiresApi(28)
class MediaSessionManagerImplApi28 extends MediaSessionManagerImplApi21 {
    MediaSessionManager mObject;

    final class RemoteUserInfoImplApi28 extends RemoteUserInfoImplBase {
        final RemoteUserInfo mObject;

        RemoteUserInfoImplApi28(RemoteUserInfo remoteUserInfo) {
            super(remoteUserInfo.getPackageName(), remoteUserInfo.getPid(), remoteUserInfo.getUid());
            this.mObject = remoteUserInfo;
        }

        RemoteUserInfoImplApi28(String str, int i, int i2) {
            super(str, i, i2);
            this.mObject = new RemoteUserInfo(str, i, i2);
        }
    }

    MediaSessionManagerImplApi28(Context context) {
        super(context);
        this.mObject = (MediaSessionManager) context.getSystemService("media_session");
    }

    public boolean isTrustedForMediaControl(RemoteUserInfoImpl remoteUserInfoImpl) {
        return super.isTrustedForMediaControl(remoteUserInfoImpl);
    }
}
