package androidx.preference;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog.Builder;
import com.android.camera.aiwatermark.util.WatermarkConstant;

public class ListPreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat {
    private static final String SAVE_STATE_ENTRIES = "ListPreferenceDialogFragment.entries";
    private static final String SAVE_STATE_ENTRY_VALUES = "ListPreferenceDialogFragment.entryValues";
    private static final String SAVE_STATE_INDEX = "ListPreferenceDialogFragment.index";
    int mClickedDialogEntryIndex;
    private CharSequence[] mEntries;
    private CharSequence[] mEntryValues;

    private ListPreference getListPreference() {
        return (ListPreference) getPreference();
    }

    public static ListPreferenceDialogFragmentCompat newInstance(String str) {
        ListPreferenceDialogFragmentCompat listPreferenceDialogFragmentCompat = new ListPreferenceDialogFragmentCompat();
        Bundle bundle = new Bundle(1);
        bundle.putString(WatermarkConstant.ITEM_KEY, str);
        listPreferenceDialogFragmentCompat.setArguments(bundle);
        return listPreferenceDialogFragmentCompat;
    }

    public void onCreate(Bundle bundle) {
        CharSequence[] charSequenceArr;
        super.onCreate(bundle);
        if (bundle == null) {
            ListPreference listPreference = getListPreference();
            if (listPreference.getEntries() == null || listPreference.getEntryValues() == null) {
                throw new IllegalStateException("ListPreference requires an entries array and an entryValues array.");
            }
            this.mClickedDialogEntryIndex = listPreference.findIndexOfValue(listPreference.getValue());
            this.mEntries = listPreference.getEntries();
            charSequenceArr = listPreference.getEntryValues();
        } else {
            this.mClickedDialogEntryIndex = bundle.getInt(SAVE_STATE_INDEX, 0);
            this.mEntries = bundle.getCharSequenceArray(SAVE_STATE_ENTRIES);
            charSequenceArr = bundle.getCharSequenceArray(SAVE_STATE_ENTRY_VALUES);
        }
        this.mEntryValues = charSequenceArr;
    }

    public void onDialogClosed(boolean z) {
        if (z) {
            int i = this.mClickedDialogEntryIndex;
            if (i >= 0) {
                String charSequence = this.mEntryValues[i].toString();
                ListPreference listPreference = getListPreference();
                if (listPreference.callChangeListener(charSequence)) {
                    listPreference.setValue(charSequence);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onPrepareDialogBuilder(Builder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.setSingleChoiceItems(this.mEntries, this.mClickedDialogEntryIndex, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ListPreferenceDialogFragmentCompat listPreferenceDialogFragmentCompat = ListPreferenceDialogFragmentCompat.this;
                listPreferenceDialogFragmentCompat.mClickedDialogEntryIndex = i;
                listPreferenceDialogFragmentCompat.onClick(dialogInterface, -1);
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton((CharSequence) null, (OnClickListener) null);
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(SAVE_STATE_INDEX, this.mClickedDialogEntryIndex);
        bundle.putCharSequenceArray(SAVE_STATE_ENTRIES, this.mEntries);
        bundle.putCharSequenceArray(SAVE_STATE_ENTRY_VALUES, this.mEntryValues);
    }
}
