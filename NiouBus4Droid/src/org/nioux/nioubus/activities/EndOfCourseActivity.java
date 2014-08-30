package org.nioux.nioubus.activities;

import org.nioux.nioubus.R;
import org.nioux.nioubus.adapters.EndOfCourseAdapter;
import org.nioux.nioubus.navitia.DOM;
import org.nioux.nioubus.navitia.ID;
import org.nioux.nioubus.tasks.EndOfCourseTask;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class EndOfCourseActivity extends ListActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
    	MainPreferencesActivity.setPreferenceTheme(this);
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.endofcourse);
		
        MapView mapView = (MapView) findViewById(R.id.mapview);
        if(mapView != null)
        {
			mapView.setTileSource(TileSourceFactory.MAPNIK);
			mapView.setMultiTouchControls(true);
			mapView.setClickable(true);
			mapView.setBuiltInZoomControls(true);
        }
		
        new EndOfCourseTask(this).execute(getIntent());
    }
	
    public void onAddBookmark(View view) 
    {
    	
    }
    
    public void onRemoveBookmark(View view) 
    {
    	
    }
    
    public void onMapMode(View view) 
    {
        View viewMap = View.class.cast(findViewById(R.id.mapview));
        View viewList = View.class.cast(findViewById(android.R.id.list));
        if(viewMap.getVisibility() == View.GONE)
        {
        	viewMap.setVisibility(View.VISIBLE);
        	viewList.setVisibility(View.GONE);
        }
        else if(viewList.getVisibility() == View.GONE)
        {
        	viewMap.setVisibility(View.VISIBLE);
        	viewList.setVisibility(View.VISIBLE);
        }
        else
        {
        	viewMap.setVisibility(View.GONE);
        	viewList.setVisibility(View.VISIBLE);
        }
    }
    
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
    	super.onListItemClick(l, v, position, id);
    	
    	EndOfCourseAdapter adapter = EndOfCourseAdapter.class.cast(getListAdapter());
    	if(adapter != null && adapter.getEndOfCourseList() != null)
    	{
    		DOM.Stop stop = adapter.getEndOfCourseList().StopList.Stop.get(position);
			Intent intent = new Intent(this, LineListActivity.class);
			intent.putExtra(ID.Server, getIntent().getStringExtra(ID.Server));
			intent.putExtra(ID.NetworkExternalCode, getIntent().getStringExtra(ID.NetworkExternalCode) );
			intent.putExtra(ID.StopAreaExternalCode, stop.StopArea.StopAreaExternalCode);
			//intent.putExtra(Helpers.id.ServerName, servername);
			//intent.putExtra(Helpers.id.NetworkExternalCode, network);
			//intent.putExtra(Helpers.id.NetworkName, networkname);
			startActivity(intent);
    	}
    }

}
