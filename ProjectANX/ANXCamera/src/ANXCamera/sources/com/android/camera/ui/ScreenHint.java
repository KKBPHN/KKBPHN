package com.android.camera.ui;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;
import com.android.camera.CameraIntentManager;
import com.android.camera.CameraSettings;
import com.android.camera.LocationManager;
import com.android.camera.OnScreenHint;
import com.android.camera.R;
import com.android.camera.RotateDialogController;
import com.android.camera.WebViewActivity;
import com.android.camera.data.DataRepository;
import com.android.camera.data.provider.DataProvider.ProviderEditor;
import com.android.camera.fragment.CtaNoticeFragment.CTA;
import com.android.camera.log.Log;
import com.android.camera.permission.PermissionManager;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.storage.Storage;
import java.util.Locale;
import miui.app.AlertDialog;
import miui.app.AlertDialog.Builder;
import miui.os.Build;

public class ScreenHint {
    public static final String TAG = "ScreenHint";
    /* access modifiers changed from: private */
    public final Activity mActivity;
    /* access modifiers changed from: private */
    public boolean mAllowLocationAccess;
    private OnScreenHint mStorageHint;
    private long mStorageSpace;
    /* access modifiers changed from: private */
    public AlertDialog mSystemChoiceDialog;

    public ScreenHint(Activity activity) {
        this.mActivity = activity;
    }

    public static void setLinkClickEvent(TextView textView, final Activity activity) {
        URLSpan[] uRLSpanArr;
        CharSequence text = textView.getText();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        for (final URLSpan uRLSpan : (URLSpan[]) spannableStringBuilder.getSpans(0, text.length(), URLSpan.class)) {
            spannableStringBuilder.setSpan(new ClickableSpan() {
                public void onClick(View view) {
                    String url = uRLSpan.getURL();
                    try {
                        Intent intent = new Intent("android.intent.action.VIEW");
                        intent.setData(Uri.parse(url));
                        activity.startActivity(intent);
                    } catch (ActivityNotFoundException unused) {
                        Intent intent2 = new Intent(activity, WebViewActivity.class);
                        intent2.putExtra(CameraIntentManager.EXTRA_CTA_WEBVIEW_LINK, url);
                        activity.startActivity(intent2);
                    }
                }
            }, spannableStringBuilder.getSpanStart(uRLSpan), spannableStringBuilder.getSpanEnd(uRLSpan), spannableStringBuilder.getSpanFlags(uRLSpan));
        }
        textView.setText(spannableStringBuilder);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public /* synthetic */ void O0000OoO(DialogInterface dialogInterface, int i) {
        Log.u(TAG, "onClick LocationAccess PositiveButton");
        CameraStatUtils.trackCTADialogAgree();
        recordFirstUse(false);
        this.mAllowLocationAccess = true;
        CameraSettings.updateCTAPreference(true);
        CTA.setCanConnectNetwork(true);
    }

    public /* synthetic */ void O0000Ooo(DialogInterface dialogInterface, int i) {
        Log.u(TAG, "onClick LocationAccess NegativeButton");
        CameraStatUtils.trackCTADialogDisagree();
        recordFirstUse(false);
        this.mAllowLocationAccess = false;
        CameraSettings.updateCTAPreference(false);
    }

    public void cancelHint() {
        OnScreenHint onScreenHint = this.mStorageHint;
        if (onScreenHint != null) {
            onScreenHint.cancel();
            this.mStorageHint = null;
        }
    }

    public void dismissSystemChoiceDialog() {
        AlertDialog alertDialog = this.mSystemChoiceDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.mSystemChoiceDialog = null;
        }
    }

    public void hideToast() {
        RotateTextToast instance = RotateTextToast.getInstance();
        if (instance != null) {
            instance.show(0, 0);
        }
    }

    public boolean isScreenHintVisible() {
        OnScreenHint onScreenHint = this.mStorageHint;
        return onScreenHint != null && onScreenHint.getHintViewVisibility() == 0;
    }

    public void recordFirstUse(boolean z) {
        ProviderEditor editor = DataRepository.dataItemGlobal().editor();
        editor.putBoolean("pref_camera_first_use_hint_shown_key", z);
        editor.putBoolean(CameraSettings.KEY_CAMERA_CONFIRM_LOCATION_SHOWN, z);
        editor.apply();
    }

    public void showConfirmMessage(int i, int i2) {
        Activity activity = this.mActivity;
        RotateDialogController.showSystemAlertDialog(activity, activity.getString(i), this.mActivity.getString(i2), this.mActivity.getString(17039370), null, null, null, null, null);
    }

    public void showFirstUseHint(final Runnable runnable) {
        AlertDialog alertDialog = this.mSystemChoiceDialog;
        if (alertDialog != null) {
            if (!alertDialog.isShowing()) {
                dismissSystemChoiceDialog();
            } else {
                return;
            }
        }
        if (CameraSettings.isShowFirstUseHint()) {
            boolean contains = DataRepository.dataItemGlobal().contains(CameraSettings.KEY_RECORD_LOCATION);
            final boolean z = Build.IS_INTERNATIONAL_BUILD || C0122O00000o.instance().OOOOoo();
            if (C0124O00000oO.Oo00OoO() && !contains) {
                this.mAllowLocationAccess = false;
                String format = String.format("%s_%s", new Object[]{Locale.getDefault().getLanguage(), Locale.getDefault().getCountry()});
                Spanned fromHtml = Html.fromHtml(this.mActivity.getString(R.string.cta_user_tips, new Object[]{this.mActivity.getString(R.string.link_user_agreement, new Object[]{format}), this.mActivity.getString(R.string.link_privacy_policy, new Object[]{format})}), 63);
                Builder builder = new Builder(this.mActivity);
                View inflate = this.mActivity.getLayoutInflater().inflate(R.layout.dialog_cta, null);
                TextView textView = (TextView) inflate.findViewById(R.id.tv_cta_user_tips);
                textView.setText(fromHtml);
                setLinkClickEvent(textView, this.mActivity);
                builder.setView(inflate);
                builder.setPositiveButton((int) R.string.location_access_allow, (OnClickListener) new O0000Oo0(this));
                builder.setNegativeButton((int) R.string.user_disagree, (OnClickListener) new O0000OOo(this));
                this.mSystemChoiceDialog = builder.create();
                this.mSystemChoiceDialog.show();
                this.mSystemChoiceDialog.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialogInterface) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("onDismiss LocationAccess ");
                        sb.append(ScreenHint.this.mAllowLocationAccess);
                        Log.u(ScreenHint.TAG, sb.toString());
                        int i = ScreenHint.this.mAllowLocationAccess ? (!z || PermissionManager.checkCameraLaunchPermissions()) ? 101 : 100 : z ? 102 : 0;
                        if (PermissionManager.requestCameraPermissions(ScreenHint.this.mActivity, i)) {
                            if (ScreenHint.this.mAllowLocationAccess && PermissionManager.checkCameraLocationPermissions()) {
                                CameraSettings.updateRecordLocationPreference(true);
                                LocationManager.instance().recordLocation(true);
                            }
                            runnable.run();
                        }
                        ScreenHint.this.mSystemChoiceDialog = null;
                    }
                });
            }
        }
    }

    public void showObjectTrackHint() {
        ProviderEditor editor = DataRepository.dataItemGlobal().editor();
        editor.putBoolean(CameraSettings.KEY_CAMERA_OBJECT_TRACK_HINT_SHOWN, false);
        editor.apply();
        RotateTextToast.getInstance(this.mActivity).show(R.string.object_track_enable_toast, 0);
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x005f  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0075  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateHint() {
        CharSequence charSequence;
        Activity activity;
        int i;
        Storage.switchStoragePathIfNeeded();
        this.mStorageSpace = Storage.getAvailableSpace();
        if (C0122O00000o.instance().O0oo0O()) {
            Storage.getAvailableSpace(Storage.RAW_DIRECTORY);
        }
        long j = this.mStorageSpace;
        if (j == -1) {
            activity = this.mActivity;
            i = R.string.no_storage;
        } else if (j == -2) {
            activity = this.mActivity;
            i = R.string.preparing_sd;
        } else if (j == -3) {
            activity = this.mActivity;
            i = R.string.access_sd_fail;
        } else if (j >= Storage.LOW_STORAGE_THRESHOLD) {
            charSequence = null;
            if (charSequence == null) {
                OnScreenHint onScreenHint = this.mStorageHint;
                if (onScreenHint == null) {
                    this.mStorageHint = OnScreenHint.makeText(this.mActivity, charSequence);
                } else {
                    onScreenHint.setText(charSequence);
                }
                this.mStorageHint.show();
                return;
            }
            OnScreenHint onScreenHint2 = this.mStorageHint;
            if (onScreenHint2 != null) {
                onScreenHint2.cancel();
                this.mStorageHint = null;
                return;
            }
            return;
        } else if (Storage.isPhoneStoragePriority()) {
            activity = this.mActivity;
            i = R.string.spaceIsLow_content_primary_storage_priority;
        } else {
            activity = this.mActivity;
            i = R.string.spaceIsLow_content_external_storage_priority;
        }
        charSequence = activity.getString(i);
        if (charSequence == null) {
        }
    }
}
