package com.group2.recipeze.ui.Settings;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.group2.recipeze.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    private CharSequence languageChoice;
    private boolean darkOn;
    private boolean notifiOn;
    private boolean nutriantOn;
    private boolean caloryOn;
    private CharSequence deletePost;
    private String name;
    private String password;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        //gets the choice to receive notifications from the application
        SwitchPreferenceCompat notiSwitch = getPreferenceManager().findPreference("notifications");
        if(notiSwitch != null){
            notiSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Toast.makeText(getActivity(), "notifications are turned on/off", Toast.LENGTH_SHORT).show();
                    if(notiSwitch.isChecked()){
                        notifiOn = true;
                    } else{
                        notifiOn = false;
                    }
                    return true;
                }
            });
        }

        //enables dark/light mode to be turned on/off
        SwitchPreferenceCompat colourMode = getPreferenceManager().findPreference("colourMode");
        if(colourMode != null){
            colourMode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Toast.makeText(getActivity(), "darkmode is turned on/off", Toast.LENGTH_SHORT).show();
                    if(colourMode.isChecked()){
                        darkOn = true;
                    }else{
                        darkOn = false;
                    }

                    return true;
                }
            });
        }

        //allows to determine the language selected
        ListPreference langSelect = getPreferenceManager().findPreference("language");
        if(langSelect != null){
            langSelect.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Toast.makeText(getActivity(), "language is chosen", Toast.LENGTH_SHORT).show();
                    languageChoice = langSelect.getEntry();
                    return true;
                }
            });
        }

        //enables nutriant tracking
        SwitchPreferenceCompat nutriantSwitch = getPreferenceManager().findPreference("nut_track");
        if(nutriantSwitch != null){
            nutriantSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Toast.makeText(getActivity(), "nutriant tracking is turned on/off", Toast.LENGTH_SHORT).show();
                    if(nutriantSwitch.isChecked()){
                        nutriantOn = true;
                    }else{
                        nutriantOn = false;
                    }
                    return true;
                }
            });
        }

        //enables calory tracking
        SwitchPreferenceCompat calorySwitch = getPreferenceManager().findPreference("cal_track");
        if(calorySwitch != null){
            calorySwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Toast.makeText(getActivity(), "calory tracking is turned on/off", Toast.LENGTH_SHORT).show();
                    if(calorySwitch.isChecked()){
                        caloryOn = true;
                    }else{
                        caloryOn = false;
                    }
                    return true;
                }
            });
        }

        //select a recipe which the user wants to delete
        ListPreference toDeleteSelected = getPreferenceManager().findPreference("recipeDel");
        if(toDeleteSelected != null){
            toDeleteSelected.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Toast.makeText(getActivity(), "recipe to delete is selected", Toast.LENGTH_SHORT).show();
                    deletePost = toDeleteSelected.getEntry();
                    return true;
                }
            });
        }

        //changes username
        EditTextPreference newUserName = getPreferenceManager().findPreference("change_name");
        if(newUserName != null){
            newUserName.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Toast.makeText(getActivity(), "username is changed", Toast.LENGTH_SHORT).show();
                    name = newUserName.getText();
                    return true;
                }
            });
        }

        //changes password
        EditTextPreference userPass = getPreferenceManager().findPreference("change_pass");
        if(userPass != null){
            userPass.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Toast.makeText(getActivity(), "Password is changed", Toast.LENGTH_SHORT).show();
                    password = userPass.getText();
                    return true;
                }
            });
        }

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