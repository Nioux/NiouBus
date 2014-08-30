package org.nioux.nioubus.activities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.nioux.nioubus.BuildConfig;
import org.nioux.nioubus.R;
import org.nioux.nioubus.navitia.Conversion;
import org.nioux.nioubus.navitia.DOM;
import org.nioux.nioubus.navitia.ID;
import org.nioux.nioubus.tasks.ProximityListTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;


public class MapActivity extends Activity
{
	//protected String server = null;
	//protected String network = null;
	//protected MapView mapView = null;
	
	Runnable runable; 

	@Override
	protected void onCreate(Bundle icicle)
	{
    	MainPreferencesActivity.setPreferenceTheme(this);

    	super.onCreate(icicle);
        /*MapView mapView = new MapView(this);
        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(true);
        //mapView.setMapFile("/sdcard/berlin.map");
        setContentView(mapView);*/
		/*
		MapView mapView = new MapView(this);
        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(true);
        mapView.setMapFile("/sdcard/testmapfile.map");
        setContentView(mapView);*/
		setContentView(R.layout.map);
		
		Intent intent = getIntent();
		if(intent != null && intent.getAction() != null && intent.getAction().equals(Intent.ACTION_VIEW))
		{
			String data = intent.getDataString();
			/*Uri contactUri = intent.getData();
			ContentResolver contentResolver = this.getContentResolver();
			Cursor cursor = contentResolver.query(contactUri,
                    new String[]{ContactsContract..Contacts. Contacts.KIND_POSTAL}, null, null, null);

			/*Cursor c = managedQuery(data, null, null, null, null);
            if (c.moveToFirst()) 
            {
                String name = c.getString(c.getColumnIndexOrThrow(People...NAME));
                txtContacts.setText(name);
            }*/
			AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.editTextAddress);
			textView.setText(data);
		}
		
        //String server = getIntent().getStringExtra(Helpers.id.Server);
        //String network = getIntent().getStringExtra(Helpers.id.NetworkExternalCode);
        
        final MapView mapView = (MapView) findViewById(R.id.mapview);
        if(mapView != null)
        {
			mapView.setTileSource(TileSourceFactory.MAPNIK);
			mapView.setMultiTouchControls(true);
			mapView.setClickable(true);
			mapView.setBuiltInZoomControls(true);

			final Handler handler = new Handler();
			mapView.setMapListener(new MapListener()
			{
				
				public boolean onZoom(ZoomEvent arg0)
				{
					return false;
				}
				
				public boolean onScroll(ScrollEvent evt)
				{
					
					//Toast.makeText(MapActivity.this, String.format("%f / %f", ((double)mapView.getMapCenter().getLatitudeE6())/1E6, ((double)mapView.getMapCenter().getLongitudeE6())/1E6), Toast.LENGTH_SHORT).show();
					
					//DOM.Params params = new DOM.Params(getIntent().getStringExtra(ID.Server));
					
					
					if(runable != null)
					{
						handler.removeCallbacks(runable);
					}
					runable = new Runnable() {
						
					  public void run() {
					    //Do something after 100ms
						  //Toast.makeText(MapActivity.this, String.format("%f / %f", ((double)mapView.getMapCenter().getLatitudeE6())/1E6, ((double)mapView.getMapCenter().getLongitudeE6())/1E6), Toast.LENGTH_SHORT).show();
						    new ProximityListTask(MapActivity.this).execute(getIntent());
						  runable = null;
					  }
					};
					handler.postDelayed(runable, 1000);

					
					return false;
				}
			});
        }
		
		
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() 
		{
		    public void onLocationChanged(Location location) 
		    {
		      // Called when a new location is found by the network location provider.
		      //makeUseOfNewLocation(location);
		    }

			public void onStatusChanged(String provider, int status, Bundle extras) 
			{
				
			}
			
			public void onProviderEnabled(String provider) 
			{
				
			}
			
			public void onProviderDisabled(String provider) 
			{
				
			}
		};

		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		gotoMyLocation();
		
		
		/*MapController  mc;
		mc = mapView.getController();
 		GeoPoint p = new GeoPoint(19240000,-99120000); //mexico
		mc.setCenter(p);
        //mc.animateTo(p);
        mc.setZoom(15); 
        mapView.invalidate();*/
		/*
		final AutoCompleteTextView editTextAddress = (AutoCompleteTextView) findViewById(R.id.editTextAddress);
		editTextAddress.setOnKeyListener(new OnKeyListener() 
		{
		    public boolean onKey(View v, int keyCode, KeyEvent event) 
		    {
		       if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) 
		       {
		    	   MapActivity.this.setAddress(editTextAddress.getText().toString());
		    	   Toast.makeText(MapActivity.this, editTextAddress.getText(), Toast.LENGTH_SHORT).show();
		    	   return true;
		       }
		       return false;
		    }
		});
		*/
		
		
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);//this,R.layout.list_item);
		
		AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.editTextAddress);
		adapter.setNotifyOnChange(true);
		textView.setAdapter(adapter);
		/*textView.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			public void onNothingSelected(AdapterView<?> arg0) 
			{
				
			}
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) 
			{
				onSearch(null);
			}
		});*/
		textView.addTextChangedListener(new TextWatcher() 
		{
		
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				DOM.ActionEntryPointList entrypoints = null;
				try 
				{
			        String server = getIntent().getStringExtra(ID.Server);
			        String network = getIntent().getStringExtra(ID.NetworkExternalCode);
					//entrypoints = Helpers.getEntryPointList(server, network, URLEncoder.encode(s.toString(), "UTF-8"), 10);
					entrypoints = DOM.ActionEntryPointList.get(server, URLEncoder.encode(s.toString(), "UTF-8"), 10);
				} 
				catch (UnsupportedEncodingException e) 
				{
					if(BuildConfig.DEBUG)
					{
						e.printStackTrace();
					}
				}
				catch(NullPointerException e)
				{
					if(BuildConfig.DEBUG)
					{
						e.printStackTrace();
					}
				}
				if(entrypoints != null && entrypoints.EntryPointList != null && entrypoints.EntryPointList.EntryPoint != null)
				{
					adapter.clear();
					for(DOM.EntryPoint entrypoint:entrypoints.EntryPointList.EntryPoint)
					{
						if(entrypoint.EntryPointName != null)
						{
							adapter.add(entrypoint.EntryPointName);
						}
					}
				}
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) 
			{
			
			}
			
			public void afterTextChanged(Editable s) 
			{
			
			}
		});
		
		
		
	}
	
	public void onSearch(View view)
	{
		AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.editTextAddress);
		String s = textView.getText().toString();
		//s = "rue de l'église marcq en baroeul";
		DOM.ActionEntryPointList entrypoints = null;
		try 
		{
	        String server = getIntent().getStringExtra(ID.Server);
	        String network = getIntent().getStringExtra(ID.NetworkExternalCode);
			//entrypoints = Helpers.getEntryPointList(server, network, URLEncoder.encode(s, "UTF-8"), 1);
			entrypoints = DOM.ActionEntryPointList.get(server, URLEncoder.encode(s, "UTF-8"), 1);
		} 
		catch (UnsupportedEncodingException e) 
		{
			if(BuildConfig.DEBUG)
			{
				e.printStackTrace();
			}
		}
		if(entrypoints != null && entrypoints.EntryPointList != null && entrypoints.EntryPointList.EntryPoint != null)
		{
			if(entrypoints.EntryPointList.EntryPoint.size() > 0)
			{
				DOM.EntryPoint entrypoint = entrypoints.EntryPointList.EntryPoint.get(0);
				DOM.Coord coord = entrypoint.Coord;
				if(coord != null && coord.CoordX != null && coord.CoordY != null)
				{
					double x = coord.CoordX.Value;
					double y = coord.CoordY.Value;
				    double[] latlong = Conversion.Lamb_WGS84(x, y);
				    if(latlong != null && latlong.length >= 2)
				    {
					    GeoPoint p = new GeoPoint((int) (latlong[1] * 1E6), (int) (latlong[0] * 1E6));
					    Log.i("nioubus", p.toString());
						MapController  mc;
						MapView mapView = (MapView) findViewById(R.id.mapview);
						mc = mapView.getController();
						mc.setCenter(p);
				        //mc.animateTo(p);
				        mc.setZoom(15); 
				        mapView.invalidate();
						mc.setCenter(p);
				        //mc.animateTo(p);
				        mc.setZoom(15); 
				        mapView.invalidate();
				    }
				}
			}
		}
	}

	public void gotoMyLocation()
	{
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		if(locationManager != null)
		{
			Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if(lastKnownLocation != null)
			{
				double longitude = lastKnownLocation.getLongitude();
				double latitude = lastKnownLocation.getLatitude();
				

			    GeoPoint p = new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
			    if(p != null)
			    {
				    Log.i("nioubus", p.toString());
					MapView mapView = (MapView) findViewById(R.id.mapview);
					if(mapView != null)
					{
						MapController  mc = mapView.getController();
						if(mc != null)
						{
							mc.setCenter(p);
					        //mc.animateTo(p);
					        mc.setZoom(15); 
					        mapView.invalidate();
							mc.setCenter(p);
					        //mc.animateTo(p);
					        mc.setZoom(15);
						}
				        mapView.invalidate();
					}
			    }
			    
			    //new ProximityListTask(this).execute(getIntent());
			}
		}
	}
	public void onMyLocation(View view)
	{
		gotoMyLocation();
	}
	
	public void displayStops()
	{
		
	}
	
	/*
	public static JSONObject getLocationInfo(String address) {

		HttpGet httpGet = new HttpGet("http://maps.google."
				+ "com/maps/api/geocode/json?address=" + address
				+ "ka&sensor=false");
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		StringBuilder stringBuilder = new StringBuilder();

		try {
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while ((b = stream.read()) != -1) {
				stringBuilder.append((char) b);
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(stringBuilder.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonObject;
	}*/
/*
public static GeoPoint getGeoPoint(JSONObject jsonObject) {

		Double lon = new Double(0);
		Double lat = new Double(0);

		try {

			lon = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
				.getJSONObject("geometry").getJSONObject("location")
				.getDouble("lng");

			lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
				.getJSONObject("geometry").getJSONObject("location")
				.getDouble("lat");

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));

	}

	protected void setAddress(String address)
	{
		Geocoder geoCoder = new Geocoder(this);
		List<Address> addresses;
		//try
		//{
			//addresses = geoCoder.getFromLocationName(address,5);
			//if(addresses != null && addresses.size() > 0)
			//{
	        //GeoPoint p = new GeoPoint( (int) (addresses.get(0).getLatitude() * 1E6), (int) (addresses.get(0).getLongitude() * 1E6));
			
			GeoPoint p = getGeoPoint(getLocationInfo(address));
				MapController  mc;
				mc = mapView.getController();
		 

		 
		        mc.animateTo(p);
		        mc.setZoom(15); 
		        mapView.invalidate();
		    //}
		//}
	}*/
	/*
	@Override
	protected boolean isRouteDisplayed()
	{
		return false;
	}
	*/
}
