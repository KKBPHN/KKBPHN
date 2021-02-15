package com.android.camera.fragment.settings;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ReplacementTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.ThermalHelper;
import com.android.camera.ToastUtils;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.log.Log;
import com.android.camera.sensitive.SensitiveFilter;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.ui.ValuePreference;
import java.util.Locale;

public class WatermarkFragment extends BasePreferenceFragment implements TextWatcher {
    private static final int MSG_BG_FILTER_WORDS = 1;
    private static final int MSG_MT_UI = 2;
    private static final int PROP_NAME_MAX = 14;
    public static final String TAG = "WatermarkFragment";
    private AlertDialog mAlertDialog;
    protected BackgroundHandler mBackgroundHandler;
    /* access modifiers changed from: private */
    public String mDefindWatermark;
    /* access modifiers changed from: private */
    public EditText mEtUserDefineWords;
    private boolean mLocked = false;
    protected PreferenceScreen mPreferenceGroup;
    /* access modifiers changed from: private */
    public HandlerThread mThreadBg;
    protected UiHandler mUiHandler;
    private Preference mUserDefineWatermark;

    final class AllCapTransformationMethod extends ReplacementTransformationMethod {
        private AllCapTransformationMethod() {
        }

        /* access modifiers changed from: protected */
        public char[] getOriginal() {
            return new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        }

        /* access modifiers changed from: protected */
        public char[] getReplacement() {
            return new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        }
    }

    final class BackgroundHandler extends Handler {
        BackgroundHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            WatermarkFragment.this.doInBackground(message);
        }
    }

    class TextJudge {
        boolean isOverLimit;
        String legalString;

        TextJudge() {
        }
    }

    final class UiHandler extends Handler {
        UiHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            WatermarkFragment.this.doInMainThread(message);
        }
    }

    private String checkContentlegal(CharSequence charSequence) {
        return SensitiveFilter.getInstance().getSensitiveWord((String) charSequence);
    }

    private boolean checkContentlength(CharSequence charSequence) {
        StringBuilder sb = new StringBuilder(32);
        sb.append(charSequence);
        TextJudge textLength = getTextLength(sb);
        if (!textLength.isOverLimit) {
            return true;
        }
        this.mEtUserDefineWords.setText(textLength.legalString);
        EditText editText = this.mEtUserDefineWords;
        editText.setSelection(editText.getText().length());
        ToastUtils.showToast((Context) getActivity(), getResources().getString(R.string.custom_watermark_too_many_words));
        return false;
    }

    /* access modifiers changed from: private */
    public void doInBackground(Message message) {
        if (message.what == 1) {
            String checkContentlegal = checkContentlegal(this.mEtUserDefineWords.getText().toString());
            Message obtain = Message.obtain();
            obtain.what = 2;
            Bundle bundle = new Bundle();
            bundle.putString("LEGAL_WORD", checkContentlegal);
            obtain.setData(bundle);
            this.mUiHandler.sendMessage(obtain);
        }
    }

    /* access modifiers changed from: private */
    public void doInMainThread(Message message) {
        if (message.what == 2) {
            String string = message.getData().getString("LEGAL_WORD");
            if (TextUtils.isEmpty(string)) {
                String customWords = getCustomWords();
                if (!customWords.equals(CameraSettings.getCustomWatermark())) {
                    CameraSettings.setCustomWatermark(customWords);
                    this.mDefindWatermark = customWords;
                    Util.generateWatermark2File();
                    CameraStatUtils.trackUserDefineWatermark();
                }
                ValuePreference valuePreference = (ValuePreference) this.mUserDefineWatermark;
                if (customWords.equals(CameraSettings.getDefaultWatermarkStr())) {
                    customWords = "";
                }
                valuePreference.setValue(customWords);
                Toast.makeText(getActivity(), R.string.custom_watermark_words_save_success, 0).show();
                return;
            }
            ToastUtils.showToast((Context) getActivity(), getString(R.string.custom_watermark_contains_senstive_words, new Object[]{string}));
        }
    }

    private void filterPreference() {
        if (!C0124O00000oO.Oo0O000()) {
            removePreference(this.mPreferenceGroup, "pref_time_watermark_key");
        }
        String str = "user_define_watermark_key";
        if (!CameraSettings.isSupportedDualCameraWaterMark()) {
            removePreference(this.mPreferenceGroup, "pref_dualcamera_watermark_key");
            removePreference(this.mPreferenceGroup, str);
        }
        if (!CameraSettings.isSupportedDualCameraWaterMark()) {
            return;
        }
        if (!C0122O00000o.instance().OO0oo0o() || DataRepository.dataItemGlobal().getCurrentCameraId() == 1) {
            removePreference(this.mPreferenceGroup, str);
        }
    }

    private String getCustomWords() {
        EditText editText = this.mEtUserDefineWords;
        if (editText == null) {
            return CameraSettings.getDefaultWatermarkStr();
        }
        String trim = editText.getText().toString().trim();
        return TextUtils.isEmpty(trim) ? CameraSettings.getDefaultWatermarkStr() : trim.toUpperCase(Locale.ENGLISH);
    }

    private void initializeActivity() {
        this.mPreferenceGroup = getPreferenceScreen();
        PreferenceScreen preferenceScreen = this.mPreferenceGroup;
        if (preferenceScreen != null) {
            preferenceScreen.removeAll();
        }
        addPreferencesFromResource(getPreferenceXml());
        this.mPreferenceGroup = getPreferenceScreen();
        if (this.mPreferenceGroup == null) {
            Log.e(TAG, "fail to init PreferenceGroup");
            getActivity().finish();
        }
        filterPreference();
        registerListener();
        updateEntries();
        updatePreferences(this.mPreferenceGroup, this.mPreferences);
    }

    private void onSave() {
        this.mBackgroundHandler.sendEmptyMessage(1);
    }

    private void registerListener() {
        registerListener(this.mPreferenceGroup, this);
        Preference findPreference = this.mPreferenceGroup.findPreference("pref_time_watermark_key");
        if (findPreference != null) {
            findPreference.setOnPreferenceClickListener(this);
        }
        Preference findPreference2 = this.mPreferenceGroup.findPreference("pref_dualcamera_watermark_key");
        if (findPreference2 != null) {
            findPreference2.setOnPreferenceClickListener(this);
        }
        this.mUserDefineWatermark = this.mPreferenceGroup.findPreference("user_define_watermark_key");
        Preference preference = this.mUserDefineWatermark;
        if (preference != null) {
            preference.setOnPreferenceClickListener(this);
        }
    }

    /* access modifiers changed from: private */
    public void release() {
        BackgroundHandler backgroundHandler = this.mBackgroundHandler;
        if (backgroundHandler != null) {
            backgroundHandler.removeCallbacksAndMessages(null);
        }
        UiHandler uiHandler = this.mUiHandler;
        if (uiHandler != null) {
            uiHandler.removeCallbacksAndMessages(null);
        }
        HandlerThread handlerThread = this.mThreadBg;
        if (handlerThread != null) {
            handlerThread.quit();
        }
    }

    private void showDialog() {
        AlertDialog alertDialog = this.mAlertDialog;
        if (alertDialog == null) {
            Builder builder = new Builder(getActivity(), R.style.EditAlertDialog);
            View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_userdefine_watermark, null);
            ((TextView) inflate.findViewById(R.id.tv_water_mark_tips)).setText(getString(R.string.custom_watermark_tips_gen2, new Object[]{Integer.valueOf(14), Integer.valueOf(18)}));
            this.mEtUserDefineWords = (EditText) inflate.findViewById(R.id.et_user_define_words);
            this.mEtUserDefineWords.addTextChangedListener(this);
            this.mEtUserDefineWords.setTransformationMethod(new AllCapTransformationMethod());
            builder.setTitle(getString(R.string.pref_userdefine_watermark_title)).setView(inflate);
            builder.setNegativeButton(getString(R.string.user_define_watermark_cancel), O0000O0o.INSTANCE);
            builder.setPositiveButton(getString(R.string.user_define_watermark_save), new O0000OOo(this));
            this.mAlertDialog = builder.create();
            this.mAlertDialog.setOnShowListener(new OnShowListener() {
                public void onShow(DialogInterface dialogInterface) {
                    EditText editText;
                    if (WatermarkFragment.this.getActivity().getIntent().getBooleanExtra("StartActivityWhenLocked", false)) {
                        WatermarkFragment.this.getActivity().setShowWhenLocked(true);
                    }
                    WatermarkFragment.this.mEtUserDefineWords.setHint(CameraSettings.getDefaultWatermarkStr());
                    String str = "";
                    if (TextUtils.isEmpty(WatermarkFragment.this.mDefindWatermark) || CameraSettings.getDefaultWatermarkStr().equals(WatermarkFragment.this.mDefindWatermark)) {
                        editText = WatermarkFragment.this.mEtUserDefineWords;
                    } else {
                        editText = WatermarkFragment.this.mEtUserDefineWords;
                        str = WatermarkFragment.this.mDefindWatermark;
                    }
                    editText.setText(str);
                    WatermarkFragment.this.mEtUserDefineWords.setSelection(WatermarkFragment.this.mEtUserDefineWords.getText().length());
                    WatermarkFragment.this.mThreadBg = new HandlerThread(WatermarkFragment.TAG, 10);
                    WatermarkFragment.this.mThreadBg.start();
                    WatermarkFragment watermarkFragment = WatermarkFragment.this;
                    watermarkFragment.mBackgroundHandler = new BackgroundHandler(watermarkFragment.mThreadBg.getLooper());
                    WatermarkFragment watermarkFragment2 = WatermarkFragment.this;
                    watermarkFragment2.mUiHandler = new UiHandler(Looper.getMainLooper());
                    WatermarkFragment.this.mEtUserDefineWords.setFocusable(true);
                    WatermarkFragment.this.mEtUserDefineWords.setFocusableInTouchMode(true);
                    WatermarkFragment.this.mEtUserDefineWords.requestFocus();
                }
            });
            this.mAlertDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    WatermarkFragment.this.release();
                }
            });
        } else if (alertDialog.isShowing()) {
            return;
        }
        this.mAlertDialog.show();
    }

    private void updateEntries() {
        String str = "pref_dualcamera_watermark_key";
        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) this.mPreferenceGroup.findPreference(str);
        if (checkBoxPreference != null) {
            checkBoxPreference.setDefaultValue(Boolean.valueOf(C0124O00000oO.O000O0o(C0122O00000o.instance().O0oo000())));
            checkBoxPreference.setChecked(C0124O00000oO.O000O0o(C0122O00000o.instance().O0oo000()));
        }
        if (this.mUserDefineWatermark != null) {
            this.mDefindWatermark = CameraSettings.getCustomWatermark();
            ((ValuePreference) this.mUserDefineWatermark).setValue(this.mDefindWatermark.equals(CameraSettings.getDefaultWatermarkStr()) ? "" : this.mDefindWatermark);
            ValuePreference valuePreference = (ValuePreference) this.mUserDefineWatermark;
            boolean z = CameraSettings.isSupportedDualCameraWaterMark() && this.mPreferences.getBoolean(str, C0124O00000oO.O000O0o(C0122O00000o.instance().O0oo000()));
            valuePreference.setEnabled(z);
        }
    }

    public /* synthetic */ void O00000oO(DialogInterface dialogInterface, int i) {
        Log.u(TAG, "onClick PositiveButton");
        onSave();
    }

    public void afterTextChanged(Editable editable) {
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    /* access modifiers changed from: protected */
    public int getPreferenceXml() {
        return R.xml.camera_preferences_watermark;
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0057  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0076  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0079 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public TextJudge getTextLength(CharSequence charSequence) {
        TextJudge textJudge = new TextJudge();
        StringBuilder sb = new StringBuilder(32);
        StringBuilder sb2 = new StringBuilder(32);
        int length = charSequence.length();
        char c = 65535;
        double d = 0.0d;
        int i = 0;
        while (i < length) {
            String valueOf = String.valueOf(charSequence.charAt(i));
            boolean matches = true ^ valueOf.matches("[^\\x00-\\xff]");
            if (c >= 0 && matches != c) {
                if (matches != c) {
                    double length2 = (double) sb.length();
                    if (c != 0) {
                        length2 /= 1.29d;
                    }
                    d += length2;
                    sb.delete(0, sb.length());
                }
                if (i == length - 1) {
                    d += c == 0 ? (double) sb.length() : ((double) sb.length()) / 1.29d;
                    sb.delete(0, sb.length());
                    sb.append(valueOf);
                }
                if (d > 14.0d) {
                    sb2.append(valueOf);
                }
                i++;
                c = matches;
            }
            sb.append(valueOf);
            if (i == length - 1) {
            }
            if (d > 14.0d) {
            }
            i++;
            c = matches;
        }
        if (d > 14.0d) {
            textJudge.isOverLimit = true;
        } else {
            textJudge.isOverLimit = false;
        }
        textJudge.legalString = String.valueOf(sb2);
        return textJudge;
    }

    public void onBackPressed() {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mLocked = getActivity().getIntent().getBooleanExtra("StartActivityWhenLocked", false);
        if (this.mLocked) {
            getActivity().setShowWhenLocked(true);
        }
        this.mFromWhere = getActivity().getIntent().getIntExtra("from_where", 0);
        if (getActivity().getIntent().getCharSequenceExtra(":miui:starting_window_label") != null) {
            ActionBar actionBar = getActivity().getActionBar();
            if (actionBar != null) {
                actionBar.setTitle(R.string.pref_watermark_title);
            }
        }
        initializeActivity();
    }

    public void onDestroy() {
        EditText editText = this.mEtUserDefineWords;
        if (editText != null) {
            editText.removeTextChangedListener(this);
            this.mEtUserDefineWords.setTransformationMethod(null);
        }
        super.onDestroy();
        release();
    }

    public void onPause() {
        super.onPause();
        if (C0124O00000oO.Oo0O0oo()) {
            ThermalHelper.updateDisplayFrameRate(false, getActivity().getApplication());
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x002c  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0030  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onPreferenceChange(Preference preference, Object obj) {
        char c;
        String key = preference.getKey();
        int hashCode = key.hashCode();
        if (hashCode != 1739638146) {
            if (hashCode == 1752299636 && key.equals("user_define_watermark_key")) {
                c = 1;
                if (c != 0) {
                    if (c == 1) {
                        return false;
                    }
                } else if (C0122O00000o.instance().OO0oo0o()) {
                    int i = this.mFromWhere;
                    if (!(i == 177 || i == 184 || DataRepository.dataItemGlobal().getCurrentCameraId() == 1)) {
                        if (!((Boolean) obj).booleanValue() || isFromSecureKeyguard()) {
                            this.mUserDefineWatermark.setEnabled(false);
                        } else {
                            this.mUserDefineWatermark.setEnabled(true);
                        }
                    }
                }
                super.onPreferenceChange(preference, obj);
                return true;
            }
        } else if (key.equals("pref_dualcamera_watermark_key")) {
            c = 0;
            if (c != 0) {
            }
            super.onPreferenceChange(preference, obj);
            return true;
        }
        c = 65535;
        if (c != 0) {
        }
        super.onPreferenceChange(preference, obj);
        return true;
    }

    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        if (TextUtils.isEmpty(key)) {
            return true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("onPreferenceClick: key=");
        sb.append(key);
        Log.u(TAG, sb.toString());
        if (!key.equals("user_define_watermark_key")) {
            return false;
        }
        showDialog();
        return true;
    }

    public void onRestart() {
        updateEntries();
        updatePreferences(this.mPreferenceGroup, this.mPreferences);
    }

    public void onResume() {
        super.onResume();
        if (C0124O00000oO.Oo0O0oo()) {
            ThermalHelper.updateDisplayFrameRate(true, getActivity().getApplication());
        }
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        checkContentlength(charSequence);
    }

    /* access modifiers changed from: protected */
    public void updateConflictPreference(Preference preference) {
    }
}
