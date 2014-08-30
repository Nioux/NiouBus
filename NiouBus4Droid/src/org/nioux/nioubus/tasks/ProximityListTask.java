package org.nioux.nioubus.tasks;

import java.util.ArrayList;

import org.nioux.nioubus.R;
import org.nioux.nioubus.activities.LineListActivity;
import org.nioux.nioubus.navitia.Conversion;
import org.nioux.nioubus.navitia.Conversion2;
import org.nioux.nioubus.navitia.DOM;
import org.nioux.nioubus.navitia.ID;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.bonuspack.overlays.DefaultInfoWindow;
import org.osmdroid.bonuspack.overlays.ExtendedOverlayItem;
import org.osmdroid.bonuspack.overlays.ItemizedOverlayWithBubble;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ProximityListTask extends AsyncTask<Intent, ProgressDialog, DOM.ActionProximityList>
{
	protected final Activity activity;
	
	public ProximityListTask(Activity activity)
	{
		this.activity = activity;
	}
	
	@Override
	protected void onPreExecute()
	{
		// TODO Auto-generated method stub
		super.onPreExecute();
		
		View progress = activity.findViewById(R.id.progress);
		progress.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected DOM.ActionProximityList doInBackground(Intent... intents)
	{
		
		MapView mapView = (MapView) activity.findViewById(R.id.mapview);
		if(mapView != null)
		{
			MapController  mc = mapView.getController();
			if(mc != null)
			{
				IGeoPoint gp = mapView.getMapCenter();

				Conversion2.CoordinateConverter cc = new Conversion2.CoordinateConverter();
				Conversion2.CoordinateConverter.XY xy = cc.convertFromWGS84(((double)gp.getLatitudeE6())/1E6, ((double)gp.getLongitudeE6())/1E6);
				Log.d("nioubus", String.format("%f %f", xy.X, xy.Y));
				
				Intent intent = intents[0];
		    	DOM.Params params = new DOM.Params(intent.getStringExtra(ID.Server));
				params.put(ID.Action, DOM.ActionProximityList.Action);
				params.put(ID.Type, "StopArea");
				params.put(ID.X, Double.toString(xy.X));
				params.put(ID.Y, Double.toString(xy.Y));
				params.put(ID.Distance, Integer.toString(1000));
				params.put(ID.MinCount, Integer.toString(0));
				params.put(ID.NbMax, Integer.toString(100));
				params.put(ID.CircleFilter, Integer.toString(1));
				
				return DOM.ActionProximityList.class.cast(DOM.ActionProximityList.get(params));
			}
		}
		//double lat = 0;//coord.CoordX.Value;
		//double lon = 0;//coord.CoordY.Value;
		return null;
	}
	
	
	
	
	public class IntentInfoWindow extends DefaultInfoWindow {
		
		protected Intent intent = null;
		
		public IntentInfoWindow(MapView mapView) {
			super(R.layout.bonuspack_bubble, mapView);
			
			Button btn = (Button)(mView.findViewById(R.id.bubble_moreinfo));
			btn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					if(intent != null)
					{
						view.getContext().startActivity(intent);
					}

				
				}
			});
		}

		@Override public void onOpen(ExtendedOverlayItem item){

			intent = Intent.class.cast(item.getRelatedObject());
			
			super.onOpen(item);
			
			mView.findViewById(R.id.bubble_moreinfo).setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void onPostExecute(DOM.ActionProximityList proximity)
	{
		// TODO Auto-generated method stub
		super.onPostExecute(proximity);

		View progress = activity.findViewById(R.id.progress);
		progress.setVisibility(View.GONE);
		
        if(proximity != null && proximity.ProximityList != null && proximity.ProximityList.Proximity != null && proximity.ProximityList.Proximity.size() > 0 && proximity.ProximityList.Proximity.get(0).StopArea != null)
        {
    		//Toast.makeText(activity, "onPostExecute", Toast.LENGTH_SHORT).show();
    		
        	GeoPoint center = null;
        	// création du tableau d'items overlay
        	ArrayList<ExtendedOverlayItem> items = new ArrayList<ExtendedOverlayItem>();
        	//ArrayList<ExtendedOverlayItem> items = new ArrayList<OverlayItem>();
        	ArrayList<DOM.Proximity> proximities = proximity.ProximityList.Proximity;
        	for(DOM.Proximity prox:proximities)
        	{
				DOM.Coord coord = prox.StopArea.Coord;
				if(coord != null && coord.CoordX != null && coord.CoordY != null)
				{
					double x = coord.CoordX.Value;
					double y = coord.CoordY.Value;
				    double[] latlong = Conversion.Lamb_WGS84(x, y);
				    if(latlong != null && latlong.length >= 2)
				    {
					    GeoPoint p = new GeoPoint((int) (latlong[1] * 1E6), (int) (latlong[0] * 1E6));
		            	// ajout d'un item sur le Geopoint
					    //Intent intent = new Intent();
					    
				        DOM.StopArea stoparea = prox.StopArea;
						Intent intent = new Intent(activity, LineListActivity.class);
						intent.putExtra(ID.Server, activity.getIntent().getStringExtra(ID.Server));
						//intent.putExtra(ID.NetworkExternalCode, activity.getIntent().getStringExtra(ID.NetworkExternalCode));
						//intent.putExtra(ID.LineExternalCode, activity.getIntent().getStringExtra(ID.LineExternalCode));
						intent.putExtra(ID.StopAreaExternalCode, stoparea.StopAreaExternalCode);
						//intent.putExtra(ID.Direction, activity.getIntent().getStringExtra(ID.Direction));
						//startActivity(intent);
					    
					    
					    
					    
					    
					    ExtendedOverlayItem eoi = new ExtendedOverlayItem(prox.StopArea.StopAreaName, prox.StopArea.City.CityName, p, activity);
					    eoi.setRelatedObject(intent);
		            	items.add(eoi);
		            	//items.add(new OverlayItem(prox.StopArea.StopAreaName, prox.StopArea.StopAreaName, p));
		            	
		            	
		            	if(center == null)
		            	{
		            		center = p;
		            	}
				    }
				}
        	}

        	
        	
        	ResourceProxy mResourceProxy = new DefaultResourceProxyImpl(activity);
        	//Overlay mMyLocationOverlay = new ItemizedIconOverlay<OverlayItem>(items, null, mResourceProxy);
        	
        	// ajout de l'overlay à la carte	
			MapView mapView = (MapView) activity.findViewById(R.id.mapview);

        	ItemizedOverlayWithBubble<ExtendedOverlayItem> mMyLocationOverlay2 = new ItemizedOverlayWithBubble<ExtendedOverlayItem>(activity, items, mapView, new IntentInfoWindow(mapView)); 
        	
        	mapView.getOverlays().clear();
			mapView.getOverlays().add(mMyLocationOverlay2);            	
			//mapView.getOverlays().add(mMyLocationOverlay);            	

	        mapView.invalidate();
			/*
			if(center != null)
			{
		        MapController  mc = mapView.getController();
				mc.setCenter(center);
		        //mc.animateTo(p);
		        mc.setZoom(13); 
		        mapView.invalidate();
				mc.setCenter(center);
		        //mc.animateTo(p);
		        mc.setZoom(15); 
		        mapView.invalidate();
		        
		        //mapView.setVisibility(View.VISIBLE);
		        //View mapButtonView = (View) activity.findViewById(R.id.ivMap);
		        //mapButtonView.setClickable(true);
			}
			*/
			
			/*
        	DOM.Coord coord = proximity.ProximityList.Proximity.get(0).StopArea.Coord;
			if(coord != null && coord.CoordX != null && coord.CoordY != null)
			{
				double x = coord.CoordX.Value;
				double y = coord.CoordY.Value;
				Log.d("nioubus", String.format("%f %f", x, y));
				
				Conversion2.CoordinateConverter cc = new Conversion2.CoordinateConverter();
				Conversion2.CoordinateConverter.LatLong ll = cc.convertToWGS84(x, y);
				Log.d("nioubus", String.format("%f %f", ll.Lat, ll.Long));
				
				Conversion2.CoordinateConverter.XY xy = cc.convertFromWGS84(ll.Lat, ll.Long);
				Log.d("nioubus", String.format("%f %f", xy.X, xy.Y));
//			    double[] latlong = Conversion.Lamb_WGS84(x, y);
//			    if(latlong != null && latlong.length >= 2)
			    {
				    GeoPoint p = new GeoPoint((int) (ll.Lat * 1E6), (int) (ll.Long * 1E6));
//				    GeoPoint p = new GeoPoint((int) (latlong[1] * 1E6), (int) (latlong[0] * 1E6));
				    Log.d("nioubus", p.toString());
					MapView mapView = (MapView) activity.findViewById(R.id.mapview);
					Toast.makeText(activity, String.format("%f / %f", ((double)mapView.getMapCenter().getLatitudeE6())/1E6, ((double)mapView.getMapCenter().getLongitudeE6())/1E6), Toast.LENGTH_SHORT).show();

			        SimpleLocationOverlay overlay = new SimpleLocationOverlay(activity);
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
			        
			        //mapView.setVisibility(View.VISIBLE);
			        //View mapButtonView = (View) activity.findViewById(R.id.ivMap);
			        //mapButtonView.setClickable(true);
			    }
			}
			*/
        }
        try
        {
        	//progress.dismiss();
        }
        catch (Exception e)
        {
		}
	}

}
