package com.example.oyan.ui.settings;



import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;


import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;


import com.example.oyan.R;

import java.util.List;


public class SettingsFragment extends PreferenceFragmentCompat
implements SharedPreferences.OnSharedPreferenceChangeListener{



    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.timer_preferences);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();

        PreferenceScreen preferenceScreen = getPreferenceScreen();

        int count = preferenceScreen.getPreferenceCount();

        for(int i = 0; i < count; i++){
            Preference preference = getPreferenceScreen().getPreference(i);

            if(!(preference instanceof CheckBoxPreference)){
                String value = sharedPreferences.getString(preference.getKey(), "");

                setPreferenceLabel(preference, value);
            }

        }

    }

    private void setPreferenceLabel(Preference preference, String value){
        if(preference instanceof ListPreference){
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(value);
            if(index >= 0){
                listPreference.setSummary(listPreference.getEntries()[index]);
            }
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);

        if(!(preference instanceof CheckBoxPreference)){
            String value = sharedPreferences.getString(preference.getKey(),"");

            setPreferenceLabel(preference, value);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}