package miui.autoinstall.config.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import com.miui.internal.R;
import miui.app.AlertDialog.Builder;
import miui.autoinstall.config.download.AutoInstallNotification;
import miui.autoinstall.config.pm.PackageManagerCompat;
import miui.autoinstall.config.service.AutoInstallService;

public class RestoreFailedDialogActivity extends Activity {
    /* access modifiers changed from: private */
    public boolean mNotNow;
    private PackageManagerCompat mPmCompat;
    /* access modifiers changed from: private */
    public boolean mRightNow;

    private void configDialog() {
        new Builder(this).setTitle(R.string.system_app_restore_failed).setMessage(R.string.system_app_restore_failed_detail).setCancelable(false).setPositiveButton((CharSequence) getResources().getString(R.string.retry), (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                RestoreFailedDialogActivity.this.mRightNow = true;
                RestoreFailedDialogActivity.this.restore();
            }
        }).setNegativeButton((CharSequence) getResources().getString(R.string.do_not_restore_temporarily), (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                RestoreFailedDialogActivity.this.mNotNow = true;
                RestoreFailedDialogActivity.this.donotRestore();
            }
        }).setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                RestoreFailedDialogActivity.this.finish();
            }
        }).create().show();
    }

    /* access modifiers changed from: private */
    public void donotRestore() {
        new AutoInstallNotification(this).showNotRestoreNotification();
        this.mPmCompat.recordRestoreNotNow(true);
    }

    /* access modifiers changed from: private */
    public void restore() {
        Intent intent = new Intent(this, AutoInstallService.class);
        intent.setAction(AutoInstallService.ACTION_START_PROCEDURE);
        startService(intent);
        this.mPmCompat.recordRestoreNotNow(false);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mPmCompat = new PackageManagerCompat(this);
        configDialog();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        if (!this.mNotNow && !this.mRightNow) {
            this.mNotNow = false;
            this.mRightNow = false;
            donotRestore();
        }
    }
}
