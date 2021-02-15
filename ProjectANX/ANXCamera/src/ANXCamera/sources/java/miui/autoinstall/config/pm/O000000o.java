package java.miui.autoinstall.config.pm;

import android.os.ResultReceiver;

public class O000000o implements O00000Oo {
    private final ResultReceiver mRemote;

    public O000000o(ResultReceiver resultReceiver) {
        this.mRemote = resultReceiver;
    }

    public void onRefuseInstall(String str, int i) {
        this.mRemote.send(1, MarketInstallObserver.O00000oo(str, i));
    }

    public void onServiceDead() {
        this.mRemote.send(2, null);
    }

    public void packageInstalled(String str, int i) {
        this.mRemote.send(0, MarketInstallObserver.O00000oo(str, i));
    }
}
