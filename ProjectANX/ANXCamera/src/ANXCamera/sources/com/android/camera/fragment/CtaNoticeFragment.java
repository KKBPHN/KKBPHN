package com.android.camera.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.global.DataItemGlobal;
import com.android.camera.log.Log;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.ui.ScreenHint;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;
import miui.app.AlertDialog.Builder;

public class CtaNoticeFragment extends DialogFragment {
    public static final String TAG = "CtaNoticeFragment";
    public static final int TYPE_FEATURE = 5;
    public static final int TYPE_LIVE_VIDEO = 1;
    public static final int TYPE_MI_LIVE_VIDEO = 4;
    public static final int TYPE_STICKER = 3;
    public static final int TYPE_VOICE_CAPTION = 2;
    private OnCtaNoticeClickListener mClickListener;
    private int mType;

    public class CTA {
        private static boolean sCanConnectToNetworkTemp;

        public static boolean canConnectNetwork() {
            if (sCanConnectToNetworkTemp) {
                return true;
            }
            return ((DataItemGlobal) DataRepository.provider().dataGlobal()).getCTACanCollect();
        }

        public static void setCanConnectNetwork(boolean z) {
            ((DataItemGlobal) DataRepository.provider().dataGlobal()).setCTACanCollect(z);
        }
    }

    public interface OnCtaNoticeClickListener {
        void onNegativeClick(DialogInterface dialogInterface, int i);

        void onPositiveClick(DialogInterface dialogInterface, int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

    public CtaNoticeFragment(OnCtaNoticeClickListener onCtaNoticeClickListener, int i) {
        this.mClickListener = onCtaNoticeClickListener;
        this.mType = i;
    }

    public static boolean checkCta(FragmentManager fragmentManager, int i) {
        return checkCta(fragmentManager, null, i);
    }

    public static boolean checkCta(FragmentManager fragmentManager, OnCtaNoticeClickListener onCtaNoticeClickListener, int i) {
        return showCta(fragmentManager, onCtaNoticeClickListener, i) == null;
    }

    public static CtaNoticeFragment showCta(FragmentManager fragmentManager, OnCtaNoticeClickListener onCtaNoticeClickListener, int i) {
        if (CameraSettings.isAllowCTA()) {
            return null;
        }
        String str = TAG;
        Fragment findFragmentByTag = fragmentManager.findFragmentByTag(str);
        if (findFragmentByTag != null) {
            return (CtaNoticeFragment) findFragmentByTag;
        }
        CtaNoticeFragment ctaNoticeFragment = new CtaNoticeFragment(onCtaNoticeClickListener, i);
        ctaNoticeFragment.show(fragmentManager, str);
        return ctaNoticeFragment;
    }

    public /* synthetic */ void O000000o(DialogInterface dialogInterface, int i) {
        Log.u(TAG, "onClick LocationAccess PositiveButton");
        CameraStatUtils.trackCTADialogAgree();
        CameraSettings.updateCTAPreference(true);
        CTA.setCanConnectNetwork(true);
        OnCtaNoticeClickListener onCtaNoticeClickListener = this.mClickListener;
        if (onCtaNoticeClickListener != null) {
            onCtaNoticeClickListener.onPositiveClick(dialogInterface, i);
        }
    }

    public /* synthetic */ void O00000Oo(DialogInterface dialogInterface, int i) {
        Log.u(TAG, "onClick LocationAccess NegativeButton");
        CameraStatUtils.trackCTADialogDisagree();
        CameraSettings.updateCTAPreference(false);
        CTA.setCanConnectNetwork(false);
        OnCtaNoticeClickListener onCtaNoticeClickListener = this.mClickListener;
        if (onCtaNoticeClickListener != null) {
            onCtaNoticeClickListener.onNegativeClick(dialogInterface, i);
        }
    }

    public void dismiss() {
        if (getFragmentManager() != null) {
            super.dismiss();
        }
    }

    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        OnCtaNoticeClickListener onCtaNoticeClickListener = this.mClickListener;
        if (onCtaNoticeClickListener != null) {
            onCtaNoticeClickListener.onNegativeClick(dialogInterface, -2);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setCancelable(false);
    }

    public Dialog onCreateDialog(Bundle bundle) {
        String format = String.format("%s_%s", new Object[]{Locale.getDefault().getLanguage(), Locale.getDefault().getCountry()});
        String string = getString(R.string.link_user_agreement, new Object[]{format});
        String string2 = getString(R.string.link_privacy_policy, new Object[]{format});
        StringBuilder sb = new StringBuilder();
        sb.append("onCreateDialog: lang = ");
        sb.append(format);
        sb.append(", linkUserAgreement = ");
        sb.append(string);
        sb.append(", linkPrivacyPolicy = ");
        sb.append(string2);
        Log.d(TAG, sb.toString());
        Spanned fromHtml = Html.fromHtml(getString(R.string.cta_user_tips, new Object[]{string, string2}), 63);
        View inflate = getActivity().getLayoutInflater().inflate(R.layout.dialog_cta, null);
        TextView textView = (TextView) inflate.findViewById(R.id.tv_cta_user_tips);
        textView.setText(fromHtml);
        ScreenHint.setLinkClickEvent(textView, getActivity());
        return new Builder(getActivity()).setView(inflate).setPositiveButton((int) R.string.network_access_user_notice_agree, (OnClickListener) new C0281O00000oO(this)).setNegativeButton((int) R.string.user_disagree, (OnClickListener) new O00000o(this)).create();
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCancelable(true);
        }
    }
}
