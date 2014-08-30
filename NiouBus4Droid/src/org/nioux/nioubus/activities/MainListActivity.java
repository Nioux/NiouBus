package org.nioux.nioubus.activities;

import org.nioux.nioubus.R;
import org.nioux.nioubus.navitia.DOM;
import org.nioux.nioubus.navitia.ID;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

public class MainListActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	MainPreferencesActivity.setPreferenceTheme(this);
        
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        setTitle(getResources().getText(R.string.title_main));
        
		//Properties systemProperties = System.getProperties(); 
		//systemProperties.setProperty("http.proxyHost", "odprox02s.priv.atos.fr"); 
		//systemProperties.setProperty("http.proxyPort", "3128"); 

        DOM.convertFavoritesToBookmarks();
        
		doInitializeData();
    }
    
    @Override
    protected void onResume()
    {
    	super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());
        String servername = preferences.getString("keyServerName", null);
        String networkname = preferences.getString("keyNetworkName", null);

        //TextView tvServerNetwork = TextView.class.cast(findViewById(R.id.server_network));
        if(servername != null && networkname != null)
        {
        	//String servername = Helpers.getServerList(this).getServerByExternalCode(server).ServerName;
        	//String networkname = Helpers.getNetworkList(server).NetworkList.getNetworkByExternalCode(network).NetworkName;
        	
        	setTitle("Réseau : " + servername.toLowerCase() + " / " + networkname.toLowerCase());
        }
        else
        {
        	setTitle("Aucun réseau sélectionné");
        }
    }
    /*
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		
		Intent intent = null;
		switch(position)
		{
		case 0:
			intent = new Intent(this, FavoriteListActivity.class);
			break;
		case 1:
			intent = new Intent(this, ServerListActivity.class);
			break;
		}
		startActivity(intent);
	}
	*/
	public void onClickImageServers(View view)
	{
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());
        String server = preferences.getString("keyServer", null);
        String servername = preferences.getString("keyServerName", null);
        String network = preferences.getString("keyNetwork", null);
        String networkname = preferences.getString("keyNetworkName", null);

		if(server != null && network != null)
		{
			Intent intent = new Intent(this, LineListActivity.class);
			intent.putExtra(ID.Server, server);
			intent.putExtra(ID.ServerName, servername);
			intent.putExtra(ID.NetworkExternalCode, network);
			intent.putExtra(ID.NetworkName, networkname);
			startActivity(intent);
		}
		else
		{
			startActivity(new Intent(this, ServerListActivity.class));
		}
	}
    
	public void onClickImageFavorites(View view)
	{
		startActivity(new Intent(this, FavoriteListActivity.class));
	}
    
	public void onClickImageSearch(View view)
	{
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());
        String server = preferences.getString("keyServer", null);
        String servername = preferences.getString("keyServerName", null);
        String network = preferences.getString("keyNetwork", null);
        String networkname = preferences.getString("keyNetworkName", null);

		if(server != null && network != null)
		{
			Intent intent = new Intent(this, MapActivity.class);
			intent.putExtra(ID.Server, server);
			intent.putExtra(ID.ServerName, servername);
			intent.putExtra(ID.NetworkExternalCode, network);
			intent.putExtra(ID.NetworkName, networkname);
			startActivity(intent);
		}
		else
		{
			startActivity(new Intent(this, ServerListActivity.class));
		}
		//Toast.makeText(this,"Dans une prochaine version...",Toast.LENGTH_SHORT).show();
	}
    
	public void onClickImageAbout(View view)
	{
		//Toast.makeText(this,"Dans une prochaine version...",Toast.LENGTH_SHORT).show();
		startActivity(new Intent(this, AboutActivity.class));
	}
    
	public void onClickImageParameters(View view)
	{
		startActivity(new Intent(this, MainPreferencesActivity.class));
	}
    
    private void doInitializeData() 
    {
		//setListAdapter(new MainListAdapter());
    }
/*
	public void onClickImageMap(View view)
	{
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());
        String server = preferences.getString("keyServer", null);
        String network = preferences.getString("keyNetwork", null);

		if(server != null && network != null)
		{
			Intent intent = new Intent(this, MapActivity.class);
			intent.putExtra(Helpers.id.Server, server);
			intent.putExtra(Helpers.id.NetworkExternalCode, network);
			startActivity(intent);
		}
		else
		{
			startActivity(new Intent(this, ServerListActivity.class));
		}
		//Toast.makeText(this,"Dans une prochaine version...",Toast.LENGTH_SHORT).show();
	}*/
    
	public void onClickImageNetwork(View view)
	{
		//startActivity(new Intent(this, NetworkPreferencesActivity.class));
		startActivity(new Intent(this, ServerListActivity.class));
		//Toast.makeText(this,"Dans une prochaine version...",Toast.LENGTH_SHORT).show();
	}
    
    /*    
	public class MainListAdapter extends BaseAdapter
	{
		LayoutInflater inflater = LayoutInflater.from(MainListActivity.this);
		
		@Override
		public int getCount() 
		{
			return 2;
		}

		@Override
		public DOM.Server getItem(int index) 
		{
			return null;
		}

		@Override
		public long getItemId(int arg0) 
		{
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
			//View v = inflater.inflate(android.R.layout.two_line_list_item, parent, false);
			TextView tvLine1 = (TextView)v.findViewById(android.R.id.text1);
			//TextView tvLine2 = (TextView)v.findViewById(android.R.id.text2);
			switch(position)
			{
			case 0:
				tvLine1.setText("favoris");
				break;
			case 1:
				tvLine1.setText("tous les horaires");
				break;
			}
			//tvLine2.setText(server.getName());
			return v;
		}
		
	}
*/
}
