package miuix.preference;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.preference.EditTextPreferenceDialogFragment;
import com.android.camera.aiwatermark.util.WatermarkConstant;
import miui.app.AlertDialog.Builder;

public class EditTextPreferenceDialogFragmentCompat extends EditTextPreferenceDialogFragment {
    private PreferenceDialogFragmentCompatDelegate mDelegate = new PreferenceDialogFragmentCompatDelegate(this.mImpl, this);
    private IPreferenceDialogFragment mImpl = new IPreferenceDialogFragment() {
        public boolean needInputMethod() {
            return true;
        }

        public void onBindDialogView(View view) {
            EditTextPreferenceDialogFragmentCompat.this.onBindDialogView(view);
        }

        public View onCreateDialogView(Context context) {
            return EditTextPreferenceDialogFragmentCompat.this.onCreateDialogView(context);
        }

        public void onPrepareDialogBuilder(Builder builder) {
            EditTextPreferenceDialogFragmentCompat.this.onPrepareDialogBuilder(builder);
        }
    };

    public static EditTextPreferenceDialogFragmentCompat newInstance(String str) {
        EditTextPreferenceDialogFragmentCompat editTextPreferenceDialogFragmentCompat = new EditTextPreferenceDialogFragmentCompat();
        Bundle bundle = new Bundle(1);
        bundle.putString(WatermarkConstant.ITEM_KEY, str);
        editTextPreferenceDialogFragmentCompat.setArguments(bundle);
        return editTextPreferenceDialogFragmentCompat;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        return this.mDelegate.onCreateDialog(bundle);
    }

    /* access modifiers changed from: protected */
    public final void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        throw new UnsupportedOperationException("using miuix builder instead");
    }

    /* access modifiers changed from: protected */
    public void onPrepareDialogBuilder(Builder builder) {
        super.onPrepareDialogBuilder(new BuilderDelegate(getActivity(), builder));
    }
}
