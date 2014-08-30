package org.nioux.nioubus.activities;

import org.nioux.nioubus.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

public class MainPreferencesActivity extends PreferenceActivity implements OnPreferenceChangeListener
{
	public final static String DEFAULT_THEME = "nioux.nioubus.org:style/ThemeLight";
	public final static String KEY_SELECT_THEME = "keySelectTheme";
	
	public static void setPreferenceTheme(Activity activity)
	{
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity.getBaseContext());
        activity.setTheme(activity.getResources().getIdentifier(preferences.getString(KEY_SELECT_THEME, DEFAULT_THEME), null, null));
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	MainPreferencesActivity.setPreferenceTheme(this);

        super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		Preference customPref = (Preference) findPreference(KEY_SELECT_THEME);
        customPref.setOnPreferenceChangeListener(this);
    }

	public boolean onPreferenceChange(Preference preference, Object newValue)
	{
		Intent intent = new Intent(this, MainListActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		return true;
	}
}
