package org.nioux.nioubus.activities;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

public class NetworkPreferencesActivity extends PreferenceActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {

    	MainPreferencesActivity.setPreferenceTheme(this);
        super.onCreate(savedInstanceState);

        setPreferenceScreen(createPreferenceHierarchy());
    }

    private PreferenceScreen createPreferenceHierarchy() {
        // Root
        PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);

        // Inline preferences
        PreferenceCategory inlinePrefCat = new PreferenceCategory(this);
        inlinePrefCat.setTitle("test");
        root.addPreference(inlinePrefCat);

        
        // Checkbox preference
        CheckBoxPreference checkboxPref = new CheckBoxPreference(this);
        checkboxPref.setKey("checkbox_preference");
        checkboxPref.setTitle("titre");
        checkboxPref.setSummary("summary");
        inlinePrefCat.addPreference(checkboxPref);
/*
        // Switch preference
        SwitchPreference switchPref = new SwitchPreference(this);
        switchPref.setKey("switch_preference");
        switchPref.setTitle(R.string.title_switch_preference);
        switchPref.setSummary(R.string.summary_switch_preference);
        inlinePrefCat.addPreference(switchPref);

        // Dialog based preferences
        PreferenceCategory dialogBasedPrefCat = new PreferenceCategory(this);
        dialogBasedPrefCat.setTitle(R.string.dialog_based_preferences);
        root.addPreference(dialogBasedPrefCat);

        // Edit text preference
        EditTextPreference editTextPref = new EditTextPreference(this);
        editTextPref.setDialogTitle(R.string.dialog_title_edittext_preference);
        editTextPref.setKey("edittext_preference");
        editTextPref.setTitle(R.string.title_edittext_preference);
        editTextPref.setSummary(R.string.summary_edittext_preference);
        dialogBasedPrefCat.addPreference(editTextPref);

        // List preference
        ListPreference listPref = new ListPreference(this);
        listPref.setEntries(R.array.entries_list_preference);
        listPref.setEntryValues(R.array.entryvalues_list_preference);
        listPref.setDialogTitle(R.string.dialog_title_list_preference);
        listPref.setKey("list_preference");
        listPref.setTitle(R.string.title_list_preference);
        listPref.setSummary(R.string.summary_list_preference);
        dialogBasedPrefCat.addPreference(listPref);

        // Launch preferences
        PreferenceCategory launchPrefCat = new PreferenceCategory(this);
        launchPrefCat.setTitle(R.string.launch_preferences);
        root.addPreference(launchPrefCat);

        // Screen preference
        PreferenceScreen screenPref = getPreferenceManager().createPreferenceScreen(this);
        screenPref.setKey("screen_preference");
        screenPref.setTitle(R.string.title_screen_preference);
        screenPref.setSummary(R.string.summary_screen_preference);
        launchPrefCat.addPreference(screenPref);

        // Example of next screen toggle preference
        CheckBoxPreference nextScreenCheckBoxPref = new CheckBoxPreference(this);
        nextScreenCheckBoxPref.setKey("next_screen_toggle_preference");
        nextScreenCheckBoxPref.setTitle(R.string.title_next_screen_toggle_preference);
        nextScreenCheckBoxPref.setSummary(R.string.summary_next_screen_toggle_preference);
        screenPref.addPreference(nextScreenCheckBoxPref);

        // Intent preference
        PreferenceScreen intentPref = getPreferenceManager().createPreferenceScreen(this);
        intentPref.setIntent(new Intent().setAction(Intent.ACTION_VIEW)
                .setData(Uri.parse("http://www.android.com")));
        intentPref.setTitle(R.string.title_intent_preference);
        intentPref.setSummary(R.string.summary_intent_preference);
        launchPrefCat.addPreference(intentPref);

        // Preference attributes
        PreferenceCategory prefAttrsCat = new PreferenceCategory(this);
        prefAttrsCat.setTitle(R.string.preference_attributes);
        root.addPreference(prefAttrsCat);

        // Visual parent toggle preference
        CheckBoxPreference parentCheckBoxPref = new CheckBoxPreference(this);
        parentCheckBoxPref.setTitle(R.string.title_parent_preference);
        parentCheckBoxPref.setSummary(R.string.summary_parent_preference);
        prefAttrsCat.addPreference(parentCheckBoxPref);

        // Visual child toggle preference
        // See res/values/attrs.xml for the <declare-styleable> that defines
        // TogglePrefAttrs.
        TypedArray a = obtainStyledAttributes(R.styleable.TogglePrefAttrs);
        CheckBoxPreference childCheckBoxPref = new CheckBoxPreference(this);
        childCheckBoxPref.setTitle(R.string.title_child_preference);
        childCheckBoxPref.setSummary(R.string.summary_child_preference);
        childCheckBoxPref.setLayoutResource(
                a.getResourceId(R.styleable.TogglePrefAttrs_android_preferenceLayoutChild,
                        0));
        prefAttrsCat.addPreference(childCheckBoxPref);
        a.recycle();
		*/
        return root;
    }
}
