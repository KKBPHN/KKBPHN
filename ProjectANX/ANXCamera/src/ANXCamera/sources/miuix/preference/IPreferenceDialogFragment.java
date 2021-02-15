package miuix.preference;

import android.content.Context;
import android.view.View;
import miui.app.AlertDialog.Builder;

interface IPreferenceDialogFragment {
    boolean needInputMethod();

    void onBindDialogView(View view);

    View onCreateDialogView(Context context);

    void onPrepareDialogBuilder(Builder builder);
}
