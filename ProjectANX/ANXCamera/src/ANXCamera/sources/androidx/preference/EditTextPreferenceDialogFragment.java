package androidx.preference;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.android.camera.aiwatermark.util.WatermarkConstant;

@Deprecated
public class EditTextPreferenceDialogFragment extends PreferenceDialogFragment {
    private static final String SAVE_STATE_TEXT = "EditTextPreferenceDialogFragment.text";
    private EditText mEditText;
    private CharSequence mText;

    private EditTextPreference getEditTextPreference() {
        return (EditTextPreference) getPreference();
    }

    @Deprecated
    public static EditTextPreferenceDialogFragment newInstance(String str) {
        EditTextPreferenceDialogFragment editTextPreferenceDialogFragment = new EditTextPreferenceDialogFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString(WatermarkConstant.ITEM_KEY, str);
        editTextPreferenceDialogFragment.setArguments(bundle);
        return editTextPreferenceDialogFragment;
    }

    /* access modifiers changed from: protected */
    @RestrictTo({Scope.LIBRARY})
    public boolean needInputMethod() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void onBindDialogView(View view) {
        super.onBindDialogView(view);
        this.mEditText = (EditText) view.findViewById(16908291);
        this.mEditText.requestFocus();
        EditText editText = this.mEditText;
        if (editText != null) {
            editText.setText(this.mText);
            EditText editText2 = this.mEditText;
            editText2.setSelection(editText2.getText().length());
            return;
        }
        throw new IllegalStateException("Dialog view must contain an EditText with id @android:id/edit");
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mText = bundle == null ? getEditTextPreference().getText() : bundle.getCharSequence(SAVE_STATE_TEXT);
    }

    @Deprecated
    public void onDialogClosed(boolean z) {
        if (z) {
            String obj = this.mEditText.getText().toString();
            if (getEditTextPreference().callChangeListener(obj)) {
                getEditTextPreference().setText(obj);
            }
        }
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putCharSequence(SAVE_STATE_TEXT, this.mText);
    }
}
