package miuix.preference;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.preference.MultiSelectListPreferenceDialogFragment;
import com.android.camera.aiwatermark.util.WatermarkConstant;
import miui.app.AlertDialog.Builder;

public class MultiSelectListPreferenceDialogFragmentCompat extends MultiSelectListPreferenceDialogFragment {
    private PreferenceDialogFragmentCompatDelegate mDelegate = new PreferenceDialogFragmentCompatDelegate(this.mImpl, this);
    private IPreferenceDialogFragment mImpl = new IPreferenceDialogFragment() {
        public boolean needInputMethod() {
            return false;
        }

        public void onBindDialogView(View view) {
            MultiSelectListPreferenceDialogFragmentCompat.this.onBindDialogView(view);
        }

        public View onCreateDialogView(Context context) {
            return MultiSelectListPreferenceDialogFragmentCompat.this.onCreateDialogView(context);
        }

        public void onPrepareDialogBuilder(Builder builder) {
            MultiSelectListPreferenceDialogFragmentCompat.this.onPrepareDialogBuilder(builder);
        }
    };

    public static MultiSelectListPreferenceDialogFragmentCompat newInstance(String str) {
        MultiSelectListPreferenceDialogFragmentCompat multiSelectListPreferenceDialogFragmentCompat = new MultiSelectListPreferenceDialogFragmentCompat();
        Bundle bundle = new Bundle(1);
        bundle.putString(WatermarkConstant.ITEM_KEY, str);
        multiSelectListPreferenceDialogFragmentCompat.setArguments(bundle);
        return multiSelectListPreferenceDialogFragmentCompat;
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
