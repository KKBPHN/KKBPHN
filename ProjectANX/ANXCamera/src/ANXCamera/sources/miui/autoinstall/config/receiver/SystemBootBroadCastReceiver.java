package miui.autoinstall.config.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import miui.autoinstall.config.service.AutoInstallService;

public class SystemBootBroadCastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Intent intent2 = new Intent(context, AutoInstallService.class);
        intent2.setAction(intent.getAction());
        context.startService(intent2);
    }
}
