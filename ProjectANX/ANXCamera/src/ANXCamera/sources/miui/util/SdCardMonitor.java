package miui.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.ss.android.ugc.effectmanager.effect.model.ComposerHelper;
import java.util.HashSet;
import java.util.Iterator;
import miui.os.Environment;

public class SdCardMonitor {
    private HashSet mListeners;
    /* access modifiers changed from: private */
    public Boolean mMounted;
    private BroadcastReceiver mReceiver;

    class Holder {
        /* access modifiers changed from: private */
        public static final SdCardMonitor INSTANCE = new SdCardMonitor();

        private Holder() {
        }
    }

    public interface SdCardStatusListener {
        void onStatusChanged(boolean z);
    }

    private SdCardMonitor() {
        this.mListeners = new HashSet();
        this.mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                boolean isExternalStorageMounted = Environment.isExternalStorageMounted();
                if (SdCardMonitor.this.mMounted == null || SdCardMonitor.this.mMounted.booleanValue() != isExternalStorageMounted) {
                    SdCardMonitor.this.mMounted = Boolean.valueOf(isExternalStorageMounted);
                    SdCardMonitor.this.notifyMountStatus(isExternalStorageMounted);
                }
            }
        };
    }

    public static SdCardMonitor getInstance() {
        return Holder.INSTANCE;
    }

    public static boolean isSdCardAvailable() {
        return Environment.isExternalStorageMounted();
    }

    /* access modifiers changed from: private */
    public void notifyMountStatus(boolean z) {
        Iterator it = this.mListeners.iterator();
        while (it.hasNext()) {
            ((SdCardStatusListener) it.next()).onStatusChanged(z);
        }
    }

    public void addListener(SdCardStatusListener sdCardStatusListener) {
        if (this.mListeners.isEmpty()) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.MEDIA_SHARED");
            intentFilter.addAction("android.intent.action.MEDIA_MOUNTED");
            intentFilter.addAction("android.intent.action.MEDIA_UNMOUNTED");
            intentFilter.addAction("android.intent.action.MEDIA_BAD_REMOVAL");
            intentFilter.addDataScheme(ComposerHelper.COMPOSER_PATH);
            AppConstants.getCurrentApplication().registerReceiver(this.mReceiver, intentFilter);
        }
        this.mListeners.add(sdCardStatusListener);
    }

    public void removeListener(SdCardStatusListener sdCardStatusListener) {
        this.mListeners.remove(sdCardStatusListener);
        if (this.mListeners.isEmpty()) {
            AppConstants.getCurrentApplication().unregisterReceiver(this.mReceiver);
        }
    }
}
