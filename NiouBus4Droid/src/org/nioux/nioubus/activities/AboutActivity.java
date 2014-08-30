package org.nioux.nioubus.activities;

import org.nioux.nioubus.R;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
    	MainPreferencesActivity.setPreferenceTheme(this);

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.about);
		
		PackageInfo pInfo;
		try
		{
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			String version = pInfo.versionName;
			TextView tvVersion = TextView.class.cast(findViewById(R.id.version));
			tvVersion.setText(String.format("NiouBus %s", version));
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}

		findViewById(android.R.id.content).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    finish();
            }
		});

	}
}
