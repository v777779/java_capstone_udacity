package ru.vpcb.footballassistant;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.widget.Toast;

import ru.vpcb.footballassistant.utils.FDUtils;


public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {

    private boolean mIsSnackbarStyle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_settings);
//        setHasOptionsMenu(true);                            // inflates menu if necessary

        setupPreferences();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);

        setupPreferences();  // setup summary and listeners
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (preference == null) return;

        // Updates the summary for the preference
        if (preference instanceof ListPreference) {
            String value = sharedPreferences.getString(preference.getKey(), "");
            setPreferenceSummary(preference, value);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {  // checks input
        if (preference == null) return false;  // input data ignored

        if (preference instanceof android.support.v7.preference.ListPreference) {
            String s = (String) newValue;
            if (s.equals(getString(R.string.pref_delay_time_high))) {
                if(preference.getTitle().toString().equals(getString(R.string.pref_date_span_title))) {
                    FDUtils.showMessage(getActivity(),getString(R.string.pref_date_span_high_message));
                }else {
                    FDUtils.showMessage(getActivity(), getString(R.string.pref_delay_time_high_message));
                }
            }

        }
        return true;
    }

    /**
     * Update the summary for the preference
     *
     * @param preference Preference  the preference to be updated
     * @param value      String the value that the preference was updated to
     */
    private void setPreferenceSummary(Preference preference, String value) {
        ListPreference listPreference = (ListPreference) preference;
        int prefIndex = listPreference.findIndexOfValue(value);
        if (prefIndex >= 0) {
            listPreference.setSummary(listPreference.getEntries()[prefIndex]);
        }
    }

    private void setupPreferences() {
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen prefScreen = getPreferenceScreen();
        int count = prefScreen.getPreferenceCount();

        for (int i = 0; i < count; i++) {
            Preference p = prefScreen.getPreference(i);
            if (p instanceof ListPreference) {
                String value = sharedPreferences.getString(p.getKey(), "");
                setPreferenceSummary(p, value);
                p.setOnPreferenceChangeListener(this);
            }
        }
    }




}