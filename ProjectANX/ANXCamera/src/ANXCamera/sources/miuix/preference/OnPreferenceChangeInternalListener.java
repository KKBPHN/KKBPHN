package miuix.preference;

import androidx.preference.Preference;

interface OnPreferenceChangeInternalListener {
    void notifyPreferenceChangeInternal(Preference preference);

    boolean onPreferenceChangeInternal(Preference preference, Object obj);
}
