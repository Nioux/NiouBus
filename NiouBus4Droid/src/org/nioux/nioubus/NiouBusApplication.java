package org.nioux.nioubus;

import android.app.Application;
import android.content.Context;

public class NiouBusApplication extends Application
{
	protected static Context _Context = null;
	public static Context getContext()
	{
		return _Context;
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		_Context = getApplicationContext();
	}
}
