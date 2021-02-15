package com.miui.internal.webkit;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import com.miui.internal.R;
import com.miui.internal.hybrid.provider.AbsWebView;
import miui.app.ProgressDialog;

public class DefaultDeviceAccountLogin extends DeviceAccountLogin {
    private static final String DIALOG_FRAGMENT_TAG = "dialog";
    private static final int MSG_LOGIN_FINISH = 1;
    private static final int MSG_SHOW_DIALOG = 0;
    private static final int SHOW_DIALOG_DELAY = 500;
    private ProgressDialogFragment mDialogFragment;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                DefaultDeviceAccountLogin.this.showLoginProgress();
            } else if (i == 1) {
                DefaultDeviceAccountLogin.this.dismissDialog();
                DefaultDeviceAccountLogin.this.mWebView.setVisibility(0);
            }
        }
    };
    /* access modifiers changed from: private */
    public AbsWebView mWebView;

    public class ProgressDialogFragment extends DialogFragment {
        public Dialog onCreateDialog(Bundle bundle) {
            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getString(R.string.web_sso_login_message));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            return progressDialog;
        }
    }

    public DefaultDeviceAccountLogin(Activity activity, AbsWebView absWebView) {
        super(activity);
        this.mWebView = absWebView;
    }

    /* access modifiers changed from: private */
    public void dismissDialog() {
        this.mHandler.removeMessages(0);
        ProgressDialogFragment progressDialogFragment = this.mDialogFragment;
        if (progressDialogFragment != null && progressDialogFragment.isAdded()) {
            this.mDialogFragment.dismissAllowingStateLoss();
        }
        this.mDialogFragment = null;
    }

    /* access modifiers changed from: private */
    public void showLoginProgress() {
        dismissDialog();
        this.mDialogFragment = new ProgressDialogFragment();
        try {
            this.mDialogFragment.show(this.mActivity.getFragmentManager(), DIALOG_FRAGMENT_TAG);
        } catch (IllegalStateException unused) {
        }
    }

    public void onLoginCancel() {
        dismissDialog();
        this.mWebView.setVisibility(0);
    }

    public void onLoginFail() {
        dismissDialog();
        this.mWebView.setVisibility(0);
        Toast.makeText(this.mActivity, R.string.web_sso_login_fail, 0).show();
    }

    public void onLoginPageFinished() {
        this.mHandler.sendEmptyMessageDelayed(1, 500);
    }

    public void onLoginStart() {
        this.mHandler.sendEmptyMessageDelayed(0, 500);
    }

    public void onLoginSuccess(String str) {
        this.mWebView.loadUrl(str);
    }
}
