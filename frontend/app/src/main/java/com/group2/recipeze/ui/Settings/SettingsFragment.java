package com.group2.recipeze.ui.Settings;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.group2.recipeze.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        //log out button set to button
        Preference logOutBut = getPreferenceManager().findPreference("logOut");
        if (logOutBut != null) {
            logOutBut.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    Toast.makeText(getActivity(), "log out button is clicked", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }
        //delete account but set to button
        Preference delAccBut = getPreferenceManager().findPreference("delAcc");
        if (delAccBut != null) {
            delAccBut.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    Toast.makeText(getActivity(), "delete account button is clicked", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }
    }


}