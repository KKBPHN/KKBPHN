package miuix.preference;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.preference.ListPreferenceDialogFragment;
import com.android.camera.aiwatermark.util.WatermarkConstant;
import miui.app.AlertDialog.Builder;

public class ListPreferenceDialogFragmentCompat extends ListPreferenceDialogFragment {
    private PreferenceDialogFragmentCompatDelegate mDelegate = new PreferenceDialogFragmentCompatDelegate(this.mImpl, this);
    private IPreferenceDialogFragment mImpl = new IPreferenceDialogFragment() {
        public boolean needInputMethod() {
            return false;
        }

        public void onBindDialogView(View view) {
            ListPreferenceDialogFragmentCompat.this.onBindDialogView(view);
        }

        public View onCreateDialogView(Context context) {
            return ListPreferenceDialogFragmentCompat.this.onCreateDialogView(context);
        }

        public void onPrepareDialogBuilder(Builder builder) {
            ListPreferenceDialogFragmentCompat.this.onPrepareDialogBuilder(builder);
        }
    };

    public static ListPreferenceDialogFragmentCompat newInstance(String str) {
        ListPreferenceDialogFragmentCompat listPreferenceDialogFragmentCompat = new ListPreferenceDialogFragmentCompat();
        Bundle bundle = new Bundle(1);
        bundle.putString(WatermarkConstant.ITEM_KEY, str);
        listPreferenceDialogFragmentCompat.setArguments(bundle);
        return listPreferenceDialogFragmentCompat;
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
