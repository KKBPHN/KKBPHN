package miui.autoinstall.config.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import com.miui.internal.R;
import com.xiaomi.stat.b;
import java.text.DecimalFormat;
import miui.app.AlertDialog.Builder;
import miui.autoinstall.config.download.AutoInstallNotification;
import miui.autoinstall.config.pm.PackageManagerCompat;
import miui.autoinstall.config.service.AutoInstallService;

public class PromptRestoreDialogActivity extends Activity {
    /* access modifiers changed from: private */
    public boolean mNotNow;
    private PackageManagerCompat mPmCompat;
    /* access modifiers changed from: private */
    public boolean mRightNow;

    private void configDialog(final boolean z, long j) {
        new Builder(this).setTitle((CharSequence) getResources().getString(!z ? R.string.system_restore : R.string.mobile_data_remind)).setMessage((CharSequence) !z ? getResources().getString(R.string.after_reset_need_restore_system_app) : getResources().getString(R.string.mobile_data_consume, new Object[]{getConsume(j)})).setCancelable(false).setPositiveButton((CharSequence) getResources().getString(!z ? R.string.restore_right_now : R.string.download_right_now), (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                PromptRestoreDialogActivity.this.mRightNow = true;
                PromptRestoreDialogActivity.this.restore(z);
            }
        }).setNegativeButton((CharSequence) getResources().getString(R.string.do_not_restore_temporarily), (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                PromptRestoreDialogActivity.this.mNotNow = true;
                PromptRestoreDialogActivity.this.donotRestore();
            }
        }).setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                PromptRestoreDialogActivity.this.finish();
            }
        }).create().show();
    }

    /* access modifiers changed from: private */
    public void donotRestore() {
        new AutoInstallNotification(this).showNotRestoreNotification();
        this.mPmCompat.recordRestoreNotNow(true);
    }

    private String getConsume(long j) {
        return new DecimalFormat(b.m).format((double) ((((float) j) / 1024.0f) / 1024.0f));
    }

    /* access modifiers changed from: private */
    public void restore(boolean z) {
        Intent intent = new Intent(this, AutoInstallService.class);
        intent.setAction(!z ? AutoInstallService.ACTION_START_PROCEDURE : AutoInstallService.ACTION_DOWNLOAD_BY_PASS);
        startService(intent);
        this.mPmCompat.recordRestoreNotNow(false);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mPmCompat = new PackageManagerCompat(this);
        configDialog(getIntent().getBooleanExtra(AutoInstallService.EXTRA_IS_MOBILE_DATA_REMIND, false), getIntent().getLongExtra(AutoInstallService.EXTRA_MOBILE_DATA_CONSUME, 0));
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
