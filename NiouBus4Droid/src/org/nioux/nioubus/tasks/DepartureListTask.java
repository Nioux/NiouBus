package org.nioux.nioubus.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.nioux.nioubus.BuildConfig;
import org.nioux.nioubus.R;
import org.nioux.nioubus.adapters.DepartureListAdapter;
import org.nioux.nioubus.navitia.Conversion2;
import org.nioux.nioubus.navitia.DOM;
import org.nioux.nioubus.navitia.ID;
import org.nioux.nioubus.tools.Tools;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.SimpleLocationOverlay;

import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class DepartureListTask extends AsyncTask<Intent, ProgressDialog, DOM.DepartureBoardList>
{
	private final ExpandableListActivity activity;
	private ProgressDialog progress = null;
	public DepartureListTask(ExpandableListActivity activity)
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
    protected DOM.DepartureBoardList doInBackground(Intent... intents)
    {
		Intent intent = intents[0];
		int deltadate = intent.getIntExtra(ID.DeltaDate, 0);
    	java.util.Calendar calendar = java.util.Calendar.getInstance();
    	calendar.add(Calendar.DATE, deltadate);

		DateFormat df = new SimpleDateFormat("yyyy'%7C'MM'%7C'dd");
    	String encodedDate = df.format(calendar.getTime());
    	
    	DOM.Params params = new DOM.Params(intent.getStringExtra(ID.Server));
		params.put(ID.Action, DOM.DepartureBoardList.Action);
		params.put(ID.LineExternalCode, intent.getStringExtra(ID.LineExternalCode));
		params.put(ID.Sens, intent.getStringExtra(ID.Direction));
		params.put(ID.StopAreaExternalCode, intent.getStringExtra(ID.StopAreaExternalCode));
		params.put(ID.Date, encodedDate);
		params.put(ID.DateChangeTime, "04%7C00");
		return DOM.DepartureBoardList.class.cast(DOM.DepartureBoardList.get(params));
    }

	@Override
    protected void onPostExecute(DOM.DepartureBoardList departures)
    {
		toastErrors(departures);
		activity.setListAdapter(new DepartureListAdapter(activity, departures));
        if(departures != null && departures.StopPointList != null && departures.StopPointList.StopPoint != null && departures.StopPointList.StopPoint.size() > 0 && departures.StopPointList.StopPoint.get(0).StopArea != null)
        {
			DOM.Coord coord = departures.StopPointList.StopPoint.get(0).StopArea.Coord;
			if(coord != null && coord.CoordX != null && coord.CoordY != null)
			{
				double x = coord.CoordX.Value;
				double y = coord.CoordY.Value;
				Log.i("nioubus", String.format("%f %f", x, y));
				
				Conversion2.CoordinateConverter cc = new Conversion2.CoordinateConverter();
				Conversion2.CoordinateConverter.LatLong ll = cc.convertToWGS84(x, y);
				Log.i("nioubus", String.format("%f %f", ll.Lat, ll.Long));
				
				//Conversion2.CoordinateConverter.XY xy = cc.convertFromWGS84(ll.Lat, ll.Long);
				//Log.i("nioubus", String.format("%f %f", xy.X, xy.Y));
//			    double[] latlong = Conversion.Lamb_WGS84(x, y);
//			    if(latlong != null && latlong.length >= 2)
			    {
				    GeoPoint p = new GeoPoint((int) (ll.Lat * 1E6), (int) (ll.Long * 1E6));
//				    GeoPoint p = new GeoPoint((int) (latlong[1] * 1E6), (int) (latlong[0] * 1E6));
				    Log.i("nioubus", p.toString());
					MapView mapView = (MapView) activity.findViewById(R.id.mapview);

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
			        View mapButtonView = (View) activity.findViewById(R.id.ivMap);
			        mapButtonView.setClickable(true);
			    }
			}
        }
        if(departures != null)
        {
    		int deltadate = activity.getIntent().getIntExtra("DeltaDate", 0);
    		if(deltadate == 0)
    		{
    			selectNextStop();
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

	protected void toastErrors(DOM.DepartureBoardList departures)
	{
		if(departures != null)
		{
			if(departures.StopList != null)
			{
				String nota = departures.StopList.Nota;
				if(nota != null)
				{
					if(nota.length() > 0)
					{
						if(BuildConfig.DEBUG)
						{
							System.err.println(nota);
						}
						StringBuffer sb = new StringBuffer();
						//sb.append("nioux.nioubus.org:strings/");
						sb.append(nota);
						int idnota = activity.getResources().getIdentifier(sb.toString(), "string", activity.getPackageName());
						if(idnota != 0)
						{
							nota = activity.getString(idnota);
						}
						Toast.makeText(activity, nota, Toast.LENGTH_SHORT).show();
					}
				}
			}
			else
			{
				Toast.makeText(activity, "Erreur de chargement des horaires : aucun arrêt", Toast.LENGTH_SHORT).show();
			}
		}
		else
		{
			Toast.makeText(activity, "Erreur de chargement des horaires", Toast.LENGTH_SHORT).show();
		}
	}

    protected void selectNextStop()
    {
    	Date now = new Date();
    	int nowMinutes = (now.getHours() * 60 + now.getMinutes());
    	DepartureListAdapter adapter = DepartureListAdapter.class.cast(activity.getExpandableListAdapter());
    	for(int iGroup = 0; iGroup < adapter.getGroupCount(); iGroup++)
    	{
    		for(int iChild = 0; iChild < adapter.getChildrenCount(iGroup); iChild++)
    		{
    			DOM.Stop stop = adapter.getChild(iGroup, iChild);
    			if(stop != null)
    			{
    				DOM.Time time = stop.getStopTime();
    				if(time != null && (time.Hour.Value * 60 + time.Minute.Value) >= nowMinutes)
    				{
    					activity.getExpandableListView().expandGroup(iGroup);
    					activity.getExpandableListView().setSelectedChild(iGroup, iChild, true);
    					return;
    				}
    			}
    		}
    	}
    }
}
