package org.nioux.nioubus.activities;

import org.nioux.nioubus.R;
import org.nioux.nioubus.adapters.ServerListAdapter;
import org.nioux.nioubus.navitia.DOM;
import org.nioux.nioubus.tasks.ServerListAsyncTask;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;

public class ServerListActivity extends ExpandableListActivity implements TextWatcher
{
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	MainPreferencesActivity.setPreferenceTheme(this);

    	super.onCreate(savedInstanceState);
        setContentView(R.layout.servers);
        
        EditText etFilter = EditText.class.cast(findViewById(R.id.editTextFilter));
        etFilter.addTextChangedListener(this);
        setTitle(getResources().getText(R.string.title_networks));
        
        new ServerListAsyncTask(this).execute(getIntent());
    }
    
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		
	}
	
	public void beforeTextChanged(CharSequence s, int start, int count, int after)
	{
		
	}
	
	public void afterTextChanged(Editable s)
	{
		setListAdapter(new ServerListAdapter(this, ServerListAdapter.class.cast(getExpandableListAdapter()).getServers(), s.toString()));
		if(s.toString().length() > 0)
		{
			for(int i = 0; i < getExpandableListAdapter().getGroupCount(); i++)
			{
				getExpandableListView().expandGroup(i);
			}
		}
	}

	@Override
    protected void onDestroy()
    {
    	super.onDestroy();
    }
    
    @Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
    {
    	
    	if(ServerListAdapter.class.cast(getExpandableListAdapter()).getServers() != null)
    	{
    		DOM.Server server = ServerListAdapter.class.cast(getExpandableListAdapter()).getFilteredServers().Server.get(groupPosition);
    		if(server != null)
    		{
    			DOM.Network network = server.NetworkList.Network.get(childPosition);
    			if(network != null)
    			{
					//Intent intent = new Intent(this, NetworkListActivity.class);
					//intent.putExtra(Helpers.id.Server, server.getServerExternalCode());
    				
					SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
					SharedPreferences.Editor editor = settings.edit();
					editor.putString("keyServer", server.ServerExternalCode);
					editor.putString("keyServerName", server.ServerName);
					editor.putString("keyNetwork", network.NetworkExternalCode);
					editor.putString("keyNetworkName", network.NetworkName);
					editor.commit();
					
					//Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage( getBaseContext().getPackageName() );
					Intent intent = new Intent(ServerListActivity.this, MainListActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
    				/*
					Intent intent = new Intent(this, LineListActivity.class);
					intent.putExtra(Helpers.id.Server, server.ServerExternalCode);
					intent.putExtra(Helpers.id.NetworkExternalCode, network.NetworkExternalCode);
					startActivity(intent);
					*/
    			}
    		}
    	}
    	return true;
    }
}