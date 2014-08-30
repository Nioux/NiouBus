package org.nioux.nioubus.tasks;

import java.util.ArrayList;

import org.nioux.nioubus.R;
import org.nioux.nioubus.adapters.EndOfCourseAdapter;
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
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.view.View;

public class EndOfCourseTask extends AsyncTask<Intent, ProgressDialog, DOM.EndOfCourseList>
{
	private final ListActivity activity;
	private ProgressDialog progress = null;
	
	public EndOfCourseTask(ListActivity activity)
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
	protected DOM.EndOfCourseList doInBackground(Intent... arg0)
	{
		Intent intent = arg0[0];
		DOM.Params params = new DOM.Params(intent.getStringExtra(ID.Server));
		params.put(ID.Action, DOM.EndOfCourseList.Action);
		params.put(ID.NetworkExternalCode, intent.getStringExtra(ID.NetworkExternalCode) );
		params.put(ID.StopIdx, Integer.toString(intent.getIntExtra(ID.StopIdx, 0)));
		return DOM.EndOfCourseList.class.cast(DOM.EndOfCourseList.get(params));
	}
	
	@Override
	protected void onPostExecute(DOM.EndOfCourseList result)
	{
		super.onPostExecute(result);
		
		activity.setListAdapter(new EndOfCourseAdapter(activity, result));

        if(result != null && result.StopList != null && result.StopList.Stop != null && result.StopList.Stop.size() > 0)
        {

        	GeoPoint center = null;
        	// création du tableau d'items overlay
        	ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        	
        	
        	ArrayList<DOM.Stop> stops = result.StopList.Stop;
        	for(DOM.Stop stop:stops)
        	{
				DOM.Coord coord = stop.StopArea.Coord;
				if(coord != null && coord.CoordX != null && coord.CoordY != null)
				{
					double x = coord.CoordX.Value;
					double y = coord.CoordY.Value;
				    double[] latlong = Conversion.Lamb_WGS84(x, y);
				    if(latlong != null && latlong.length >= 2)
				    {
					    GeoPoint p = new GeoPoint((int) (latlong[1] * 1E6), (int) (latlong[0] * 1E6));
		            	// ajout d'un item sur le Geopoint
		            	items.add(new OverlayItem(stop.StopArea.StopAreaName, stop.StopArea.StopAreaName, p));
		            	if(center == null)
		            	{
		            		center = p;
		            	}
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
