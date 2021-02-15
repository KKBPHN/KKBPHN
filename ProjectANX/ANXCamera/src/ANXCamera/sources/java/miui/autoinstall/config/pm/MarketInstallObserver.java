package java.miui.autoinstall.config.pm;

import android.os.Bundle;
import android.os.ResultReceiver;

public class MarketInstallObserver extends ResultReceiver {
    private static final String KEY_PACKAGE_NAME = "packageName";
    private static final int O000OO = 1;
    private static final int O000OO0o = 0;
    private static final int O000OOOo = 2;
    private static final String O000OOo0 = "returnCode";
    private final O00000Oo mListener;

    public MarketInstallObserver(O00000Oo o00000Oo) {
        super(null);
        this.mListener = o00000Oo;
    }

    private static int O00000Oo(Bundle bundle) {
        return bundle.getInt(O000OOo0);
    }

    private static String O00000o0(Bundle bundle) {
        return bundle.getString(KEY_PACKAGE_NAME);
    }

    /* access modifiers changed from: private */
    public static Bundle O00000oo(String str, int i) {
        Bundle bundle = new Bundle(2);
        bundle.putString(KEY_PACKAGE_NAME, str);
        bundle.putInt(O000OOo0, i);
        return bundle;
    }

    /* access modifiers changed from: protected */
    public void onReceiveResult(int i, Bundle bundle) {
        super.onReceiveResult(i, bundle);
        O00000Oo o00000Oo = this.mListener;
        if (o00000Oo == null) {
            return;
        }
        if (i == 0) {
            o00000Oo.packageInstalled(O00000o0(bundle), O00000Oo(bundle));
        } else if (i == 1) {
            o00000Oo.onRefuseInstall(O00000o0(bundle), O00000Oo(bundle));
        } else if (i == 2) {
            o00000Oo.onServiceDead();
        }
    }
}
