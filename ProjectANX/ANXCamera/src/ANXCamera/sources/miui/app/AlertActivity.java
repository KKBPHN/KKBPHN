package miui.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import com.android.internal.app.AlertController;
import com.android.internal.app.AlertController.AlertParams;
import com.miui.internal.variable.AlertControllerWrapper;

public abstract class AlertActivity extends Activity implements DialogInterface {
    protected AlertController mAlert;
    protected AlertParams mAlertParams;

    public void cancel() {
        finish();
    }

    public void dismiss() {
        if (!isFinishing()) {
            finish();
        }
    }

    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mAlert = new AlertControllerWrapper(this, this, getWindow());
        this.mAlertParams = new AlertControllerWrapper.AlertParams(this);
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (this.mAlert.onKeyDown(i, keyEvent)) {
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (this.mAlert.onKeyUp(i, keyEvent)) {
            return true;
        }
        return super.onKeyUp(i, keyEvent);
    }

    /* access modifiers changed from: protected */
    public void setupAlert() {
        this.mAlertParams.apply(this.mAlert);
        this.mAlert.installContent();
    }
}
