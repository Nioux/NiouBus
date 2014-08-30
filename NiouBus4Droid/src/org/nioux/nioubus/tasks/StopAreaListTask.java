package org.nioux.nioubus.tasks;

import java.util.ArrayList;

import org.nioux.nioubus.R;
import org.nioux.nioubus.adapters.StopAreaListAdapter;
import org.nioux.nioubus.navitia.Conversion;
import org.nioux.nioubus.navitia.DOM;
import org.nioux.nioubus.navitia.ID;
import org.nioux.nioubus.tools.Tools;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

public class StopAreaListTask extends AsyncTask<Intent, ProgressDialog, DOM.ActionLineStopAreaList>
{
	private ProgressDialog progress = null;
	private final ListActivity activity;
	
	public StopAreaListTask(ListActivity activity)
	{
		this.activity = activity;
	}
	
	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
        progress = ProgressDialog.show(activity, activity.getResources().getText(R.string.title_loading), activity.getResources().getText(R.string.message_loading_stopareas), true, true, new OnCancelListener()
		{
			public void onCancel(DialogInterface dialog)
			{
				Tools.closeGlobalConnection();
				activity.finish();
			}
		});
	}
	
	@Override
    protected DOM.ActionLineStopAreaList doInBackground(Intent... intents)
    {
		Intent intent = intents[0];
		DOM.Params params = new DOM.Params(intent.getStringExtra(ID.Server));
		params.put(ID.Action, DOM.ActionLineStopAreaList.Action);
		params.put(ID.LineExternalCode, intent.getStringExtra(ID.LineExternalCode));
		return DOM.ActionLineStopAreaList.class.cast(DOM.ActionLineStopAreaList.get(params));
		//return DOM.ActionLineStopAreaList.get(intent.getStringExtra(ID.Server), intent.getStringExtra(ID.LineExternalCode));
    }

	@Override
    protected void onPostExecute(DOM.ActionLineStopAreaList stopareas)
    {
		if(stopareas == null)
		{
			Toast.makeText(activity, "Erreur de chargement des arrêts", Toast.LENGTH_SHORT).show();
		}
		activity.setListAdapter(new StopAreaListAdapter(activity, stopareas, null));
        //MapView mapView = (MapView) findViewById(R.id.mapview);
        if(stopareas != null && stopareas.LineStopAreaList != null && stopareas.LineStopAreaList.StopArea != null && stopareas.LineStopAreaList.StopArea.size() > 0)
        {

        	GeoPoint center = null;
        	// création du tableau d'items overlay
        	ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        	
        	
        	ArrayList<DOM.StopArea> sas = stopareas.LineStopAreaList.StopArea;
        	for(DOM.StopArea sa:sas)
        	{
				DOM.Coord coord = sa.Coord;
				if(coord != null && coord.CoordX != null && coord.CoordY != null)
				{
					double x = coord.CoordX.Value;
					double y = coord.CoordY.Value;
				    double[] latlong = Conversion.Lamb_WGS84(x, y);
				    if(latlong != null && latlong.length >= 2)
				    {
					    GeoPoint p = new GeoPoint((int) (latlong[1] * 1E6), (int) (latlong[0] * 1E6));
		            	// ajout d'un item sur le Geopoint
		            	items.add(new OverlayItem(sa.StopAreaName, sa.StopAreaName, p));
		            	if(center == null)
		            	{
		            		center = p;
		            	}
		            	/*
					    Log.i("nioubus", p.toString());
						MapView mapView = (MapView) findViewById(R.id.mapview);

				        SimpleLocationOverlay overlay = new SimpleLocationOverlay(StopAreaListActivity.this);
				        overlay.setLocation(p);
				        mapView.getOverlays().add(overlay);

				        MapController  mc = mapView.getController();
						mc.setCenter(p);
				        //mc.animateTo(p);
				        mc.setZoom(15); 
				        mapView.invalidate();
						mc.setCenter(p);
				        //mc.animateTo(p);
				        mc.setZoom(15); 
				        mapView.invalidate();
				        
				        mapView.setVisibility(View.VISIBLE);
				        View mapButtonView = (View) findViewById(R.id.ivMap);
				        mapButtonView.setClickable(true);
				        break;*/
				    }
				}
        	}
        	
        	
        	// création de l'overlay avec les items créés
        	/* OnTapListener for the Markers, shows a simple Toast. */
        	ResourceProxy mResourceProxy = new DefaultResourceProxyImpl(activity);
        	Overlay mMyLocationOverlay = new ItemizedIconOverlay<OverlayItem>(items, null, mResourceProxy);
        	
        	// ajout de l'overlay à la carte	
			MapView mapView = (MapView) activity.findViewById(R.id.mapview);
			mapView.getOverlays().add(mMyLocationOverlay);            	

			if(center != null)
			{
		        MapController  mc = mapView.getController();
				mc.setCenter(center);
		        //mc.animateTo(p);
		        mc.setZoom(13); 
		        mapView.invalidate();
				mc.setCenter(center);
		        //mc.animateTo(p);
		        mc.setZoom(13); 
		        mapView.invalidate();
		        
		        //mapView.setVisibility(View.VISIBLE);
		        View mapButtonView = (View) activity.findViewById(R.id.ivMap);
		        mapButtonView.setClickable(true);
			}
        	
        	
        	/*
			DOM.Coord coord = departures.StopPointList.StopPoint.get(0).StopArea.Coord;
			if(coord != null && coord.CoordX != null && coord.CoordY != null)
			{
				double x = coord.CoordX.Value;
				double y = coord.CoordY.Value;
			    double[] latlong = Conversion.Lamb_WGS84(x, y);
			    if(latlong != null && latlong.length >= 2)
			    {
				    GeoPoint p = new GeoPoint((int) (latlong[1] * 1E6), (int) (latlong[0] * 1E6));
				    Log.i("nioubus", p.toString());
					MapView mapView = (MapView) findViewById(R.id.mapview);

			        SimpleLocationOverlay overlay = new SimpleLocationOverlay(DepartureListActivity.this);
			        overlay.setLocation(p);
			        mapView.getOverlays().add(overlay);

			        MapController  mc = mapView.getController();
					mc.setCenter(p);
			        //mc.animateTo(p);
			        mc.setZoom(15); 
			        mapView.invalidate();
					mc.setCenter(p);
			        //mc.animateTo(p);
			        mc.setZoom(15); 
			        mapView.invalidate();
			        
			        mapView.setVisibility(View.VISIBLE);
			        View mapButtonView = (View) findViewById(R.id.ivMap);
			        mapButtonView.setClickable(true);
			    }
			}*/
        }
        progress.dismiss();
    }
}

