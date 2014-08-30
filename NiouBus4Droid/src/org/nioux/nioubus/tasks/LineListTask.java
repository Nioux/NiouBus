package org.nioux.nioubus.tasks;

import org.nioux.nioubus.R;
import org.nioux.nioubus.adapters.LineListAdapter;
import org.nioux.nioubus.navitia.DOM;
import org.nioux.nioubus.navitia.ID;
import org.nioux.nioubus.tools.Tools;

import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.widget.Toast;

public class LineListTask extends AsyncTask<Intent, ProgressDialog, DOM.ActionLineList>
{
	private ProgressDialog progress = null;
	private final ExpandableListActivity activity;
	
	public LineListTask(ExpandableListActivity activity)
	{
		this.activity = activity;
	}

	protected void startProgressDialog()
	{
		stopProgressDialog();
        progress = ProgressDialog.show(activity, activity.getResources().getText(R.string.title_loading), activity.getResources().getText(R.string.message_loading_lines), true, true, new OnCancelListener()
		{
			public void onCancel(DialogInterface dialog)
			{
				Tools.closeGlobalConnection();
				activity.finish();
			}
		});
	}
	
	protected void stopProgressDialog()
	{
		if(progress != null)
		{
				progress.dismiss();
		}
		progress = null;
	}
	
	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
        startProgressDialog();
	}
	@Override
	protected DOM.ActionLineList doInBackground(Intent... intents)
	{
		Intent intent = intents[0];
		DOM.Params params = new DOM.Params(intent.getStringExtra(ID.Server));
		params.put(ID.Action, DOM.ActionLineList.Action);
		params.put(ID.NetworkExternalCode, intent.getStringExtra(ID.NetworkExternalCode) );
		params.put(ID.StopAreaExternalCode, intent.getStringExtra(ID.StopAreaExternalCode) );
		return DOM.ActionLineList.class.cast(DOM.ActionLineList.get(params));
        /*String server = intent.getStringExtra(Helpers.id.Server);
        String network = intent.getStringExtra(Helpers.id.NetworkExternalCode);
        String stoparea = intent.getStringExtra(Helpers.id.StopAreaExternalCode);
        return DOM.ActionLineList.get(server, network, null, null);
		//return Helpers.getLineListByNetworkExternalCode(server, network);*/
	}

	@Override
	protected void onPostExecute(DOM.ActionLineList result)
	{
		super.onPostExecute(result);
		if(result == null)
		{
			Toast.makeText(activity, "Erreur de chargement des lignes", Toast.LENGTH_SHORT).show();
		}
		else
		{
			activity.setListAdapter(new LineListAdapter(activity, result, null));
		}
		stopProgressDialog();
	}
	
	@Override
	protected void onCancelled()
	{
		super.onCancelled();
		stopProgressDialog();
	}
	
}
