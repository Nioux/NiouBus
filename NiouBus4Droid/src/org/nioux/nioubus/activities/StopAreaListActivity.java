package org.nioux.nioubus.activities;

import org.nioux.nioubus.R;
import org.nioux.nioubus.adapters.StopAreaListAdapter;
import org.nioux.nioubus.navitia.DOM;
import org.nioux.nioubus.navitia.ID;
import org.nioux.nioubus.tasks.StopAreaListTask;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class StopAreaListActivity extends ListActivity implements TextWatcher
{
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	MainPreferencesActivity.setPreferenceTheme(this);

    	super.onCreate(savedInstanceState);
        setContentView(R.layout.stopareas);

        
        EditText etFilter = EditText.class.cast(findViewById(R.id.editTextFilter));
        etFilter.addTextChangedListener(this);
        
        setTitle(getResources().getText(R.string.title_stopareas));
        
        MapView mapView = (MapView) findViewById(R.id.mapview);
        if(mapView != null)
        {
			mapView.setTileSource(TileSourceFactory.MAPNIK);
			mapView.setMultiTouchControls(true);
			mapView.setClickable(true);
			mapView.setBuiltInZoomControls(true);
        }
		
        new StopAreaListTask(this).execute(getIntent());
    }
    
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		
	}
	
	public void beforeTextChanged(CharSequence s, int start, int count, int after)
	{
		
	}
	
	public void afterTextChanged(Editable s)
	{
		StopAreaListAdapter adapter = StopAreaListAdapter.class.cast(getListAdapter());
		if(adapter != null && adapter.getStopAreas() != null && s != null) {
			setListAdapter(new StopAreaListAdapter(this, StopAreaListAdapter.class.cast(getListAdapter()).getStopAreas(), s.toString()));
		}
	}

	public void onAddBookmark(View view) 
    {
		Toast.makeText(this,"Dans une prochaine version...",Toast.LENGTH_SHORT).show();
    }
    
    public void onRemoveBookmark(View view) 
    {
		Toast.makeText(this,"Dans une prochaine version...",Toast.LENGTH_SHORT).show();
    }
    
    public void onMapMode(View view) 
    {
		//Toast.makeText(this,"Dans une prochaine version...",Toast.LENGTH_SHORT).show();
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
        //TextView tvCurrentDate = TextView.class.cast(findViewById(R.id.currentDate));
    }
    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		StopAreaListAdapter adapter = StopAreaListAdapter.class.cast(getListAdapter());
		
		if(adapter != null && adapter.getFilteredStopAreas() != null)
		{
	        DOM.StopArea stoparea = adapter.getFilteredStopAreas().LineStopAreaList.StopArea.get(position);
			Intent intent = new Intent(this, DepartureListActivity.class);
			intent.putExtra(ID.Server, getIntent().getStringExtra(ID.Server));
			intent.putExtra(ID.NetworkExternalCode, getIntent().getStringExtra(ID.NetworkExternalCode));
			intent.putExtra(ID.LineExternalCode, getIntent().getStringExtra(ID.LineExternalCode));
			intent.putExtra(ID.StopAreaExternalCode, stoparea.StopAreaExternalCode);
			intent.putExtra(ID.Direction, getIntent().getStringExtra(ID.Direction));
			startActivity(intent);
		}
	}
    
}
