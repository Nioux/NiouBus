package org.nioux.nioubus.tasks;

import org.nioux.nioubus.R;
import org.nioux.nioubus.adapters.ServerListAdapter;
import org.nioux.nioubus.navitia.DOM;
import org.nioux.nioubus.tools.Tools;

import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

public class ServerListAsyncTask extends AsyncTask<Intent, ProgressDialog, DOM.ServerList>
{
	private final ExpandableListActivity activity;
	private ProgressDialog progress = null;
	
	public ServerListAsyncTask(ExpandableListActivity activity)
	{
		this.activity = activity;
	}
	

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
        progress = ProgressDialog.show(activity, activity.getResources().getText(R.string.title_loading), activity.getResources().getText(R.string.message_loading_departures), true, true, new OnCancelListener()
		{
			public void onCancel(DialogInterface dialog)
			{
				Tools.closeGlobalConnection();
				activity.finish();
			}
		});
	}
	
	@Override
	protected DOM.ServerList doInBackground(Intent... intents)
	{
		return DOM.ServerList.get();
	}

	@Override
	protected void onPostExecute(DOM.ServerList result)
	{
		super.onPostExecute(result);
		
		if(result == null)
		{
			Toast.makeText(activity, "Erreur de chargement des réseaux", Toast.LENGTH_SHORT).show();
		}
		else
		{
			activity.setListAdapter(new ServerListAdapter(activity, result, null));
		}

        try
        {
        	progress.dismiss();
        }
        catch (Exception e)
        {
		}
	}
}
