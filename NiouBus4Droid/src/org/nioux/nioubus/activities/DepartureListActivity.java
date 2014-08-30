package org.nioux.nioubus.activities;

import java.util.Calendar;

import org.nioux.nioubus.R;
import org.nioux.nioubus.adapters.DepartureListAdapter;
import org.nioux.nioubus.navitia.DOM;
import org.nioux.nioubus.navitia.ID;
import org.nioux.nioubus.tasks.DepartureListTask;
import org.nioux.nioubus.tools.Bookmark;
import org.nioux.nioubus.tools.Bookmarks;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;

import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DepartureListActivity extends ExpandableListActivity
{
    //@Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	MainPreferencesActivity.setPreferenceTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.departures);
		
		int deltadate = getIntent().getIntExtra(ID.DeltaDate, 0);
		Calendar date = Calendar.getInstance();
		date.add(Calendar.DATE, deltadate);

        setTitle(getResources().getText(R.string.title_departures));
        TextView tvCurrentDate = TextView.class.cast(findViewById(R.id.currentDate));
        if(tvCurrentDate != null)
        {
        	java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.FULL);
        	tvCurrentDate.setText(df.format(date.getTime()));
        }
        
        MapView mapView = (MapView) findViewById(R.id.mapview);
        if(mapView != null)
        {
			mapView.setTileSource(TileSourceFactory.MAPNIK);
			mapView.setMultiTouchControls(true);
			mapView.setClickable(true);
			mapView.setBuiltInZoomControls(true);
        }
		
        new DepartureListTask(this).execute(getIntent());
    }
    
    
    

    @Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
    {
    	DepartureListAdapter adapter = DepartureListAdapter.class.cast(getExpandableListAdapter());
    	DOM.DepartureBoardList departures = adapter.getDepartures();
    	if(departures != null)
    	{    		
    		DOM.Stop stop = adapter.getChild(groupPosition, childPosition);
			Intent intent = new Intent(this, EndOfCourseActivity.class);
			intent.putExtra(ID.Server, getIntent().getStringExtra(ID.Server));
			if(departures.LineList != null && departures.LineList.Line != null && departures.LineList.Line.size() > 0 && departures.LineList.Line.get(0).Network != null)
			{
				intent.putExtra(ID.NetworkExternalCode, departures.LineList.Line.get(0).Network.NetworkExternalCode);
			}
			else
			{
				intent.putExtra(ID.NetworkExternalCode, getIntent().getStringExtra(ID.NetworkExternalCode));
			}
			intent.putExtra(ID.StopIdx, stop.StopIdx);
			startActivity(intent);
    	}
    	return true;
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.departures, menu);

		DOM.Favorite favorite = new DOM.Favorite();
		favorite.Activity = DepartureListActivity.class.getName();
		favorite.Parameters.put("Direction", direction); 
		favorite.Parameters.put("LineExternalCode", line); 
		favorite.Parameters.put("Server", server); 
		favorite.Parameters.put("StopAreaExternalCode", stoparea);
		
		DOM.FavoriteList favorites = Tools.getFavorites(getBaseContext());
		if(favorites != null)
		{
			for(DOM.Favorite fav : favorites.Favorites)
			{
				if(fav.isEquivalent(favorite))
				{
					menu.getItem(0).setVisible(false);
					menu.getItem(1).setVisible(true);
				}
			}
		}
		
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	switch(item.getItemId())
    	{
    	case R.id.mi_mapmode:
    		onMapMode(item);
    		break;
    	//case R.id.mi_addfavorite:
    	//	onAddFavorite(item);
    	//	break;
    	case R.id.mi_removefavorite:
    		onRemoveFavorite(item);
    		break;
    	case R.id.mi_yesterday:
    		onYesterday(item);
    		break;
    	case R.id.mi_tomorrow:
    		onTomorrow(item);
    		break;
    	}
    	return true;
    }*/
    
    @Override
    protected void onResume() {

    	super.onResume();
    	
    	refreshBookmark();
    }
    
    protected void refreshBookmark()
    {
		Bookmark bookmark = new Bookmark();
		bookmark.Activity = DepartureListActivity.class.getName();
		bookmark.Bundle = getIntent().getExtras();
		/*favorite.Parameters.put(ID.Direction, getIntent().getStringExtra(ID.Direction)); 
		favorite.Parameters.put(ID.LineExternalCode, getIntent().getStringExtra(ID.LineExternalCode)); 
		favorite.Parameters.put(ID.Server, getIntent().getStringExtra(ID.Server)); 
		favorite.Parameters.put(ID.StopAreaExternalCode, getIntent().getStringExtra(ID.StopAreaExternalCode));*/
		
		ImageView ibAddBookmark = (ImageView)findViewById(R.id.ibAddBookmark);
		ImageView ibRemoveBookmark = (ImageView)findViewById(R.id.ibRemoveBookmark);
		ibAddBookmark.setVisibility(View.VISIBLE);
		ibRemoveBookmark.setVisibility(View.GONE);

		if(Bookmarks.get().contains(bookmark))
		{
			ibAddBookmark.setVisibility(View.GONE);
			ibRemoveBookmark.setVisibility(View.VISIBLE);
		}
    }
    
    public void onAddBookmark(View view) 
    {
    	DepartureListAdapter adapter = DepartureListAdapter.class.cast(getExpandableListAdapter());
    	if(adapter != null)
    	{
	    	DOM.DepartureBoardList departures = adapter.departures;
			if(departures != null)
			{
				String name = "";
				DOM.Line domline = null;
				if(departures.LineList.Line.size() > 0)
				{
					domline = departures.LineList.Line.get(0);
				}
				//name = name + line.getLineCode();
				//name = name + " - ";
				DOM.StopPoint sl = null;
				if(departures.StopPointList != null)
				{
					if(departures.StopPointList.StopPoint != null)
					{
						if(departures.StopPointList.StopPoint.size() > 0)
						{
							sl = departures.StopPointList.StopPoint.get(0);
						}
					}
				}
				if(sl != null)
				{
					name = name + sl.StopPointName;
					name = name + " - ";
				}
				if(domline != null)
				{
					if(getIntent().getStringExtra(ID.Direction) != null && getIntent().getStringExtra(ID.Direction).equals("1"))
					{
						if(domline.Forward != null)
						{
							name = name + domline.Forward.ForwardName;
						}
					}
					else
					{
						if(domline.Backward != null)
						{
							name = name + domline.Backward.BackwardName;
						}
					}
				}
				
				final DOM.Line fdomline = domline; 

				
				final Bookmark favorite = new Bookmark();
				favorite.Activity = DepartureListActivity.class.getName();
				if(fdomline != null)
				{
					favorite.Title = domline.LineCode;
				}
				favorite.SubTitle = name;
				favorite.Bundle = getIntent().getExtras();
				/*
				favorite.Parameters.put(ID.Direction, getIntent().getStringExtra(ID.Direction)); 
				favorite.Parameters.put(ID.LineExternalCode, getIntent().getStringExtra(ID.LineExternalCode)); 
				favorite.Parameters.put(ID.Server, getIntent().getStringExtra(ID.Server)); 
				favorite.Parameters.put(ID.StopAreaExternalCode, getIntent().getStringExtra(ID.StopAreaExternalCode)); 
				*/
				
				
		        //On instancie notre layout en tant que View
		        LayoutInflater factory = LayoutInflater.from(this);
		        View alertDialogView = factory.inflate(R.layout.alertdialog_addbookmark, null);
		 
		        //Création de l'AlertDialog
		        AlertDialog.Builder adb = new AlertDialog.Builder(this);
		 
		        //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
		        adb.setView(alertDialogView);
		 
		        //On donne un titre à l'AlertDialog
		        adb.setTitle("Ajout d'un favori");
		 
		        //On modifie l'icône de l'AlertDialog pour le fun ;)
		        adb.setIcon(android.R.drawable.ic_dialog_info);
		 
            	//Lorsque l'on cliquera sur le bouton "OK", on récupère l'EditText correspondant à notre vue personnalisée (cad à alertDialogView)
            	final EditText etTitle = (EditText)alertDialogView.findViewById(R.id.EditTextTitle);
            	final EditText etSubTitle = (EditText)alertDialogView.findViewById(R.id.EditTextSubTitle);
            	
            	etTitle.setText(favorite.Title );
            	etSubTitle.setText(favorite.SubTitle );
 
		        //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
		        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		 
		            	favorite.Title = etTitle.getText().toString();
		            	favorite.SubTitle = etSubTitle.getText().toString();

		            	Bookmarks bookmarks = Bookmarks.get();
		            	bookmarks.add(favorite);
		            	bookmarks.save();
		            	//Tools.addFavorite(favorite);
				    	refreshBookmark();
						Toast.makeText(DepartureListActivity.this,"Horaire ajouté aux favoris",Toast.LENGTH_SHORT).show();
		          } });
		 
		        //On crée un bouton "Annuler" à notre AlertDialog et on lui affecte un évènement
		        adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            	//Lorsque l'on cliquera sur annuler on quittera l'application
		            	dialog.dismiss();
		          } });
		        adb.show();				
				
				
				/*
				DOM.Favorite favorite = new DOM.Favorite();
				favorite.Activity = DepartureListActivity.class.getName();
				if(domline != null)
				{
					favorite.Title = domline.LineCode;
				}
				favorite.SubTitle = name;
				favorite.Parameters.put(ID.Direction, getIntent().getStringExtra(ID.Direction)); 
				favorite.Parameters.put(ID.LineExternalCode, getIntent().getStringExtra(ID.LineExternalCode)); 
				favorite.Parameters.put(ID.Server, getIntent().getStringExtra(ID.Server)); 
				favorite.Parameters.put(ID.StopAreaExternalCode, getIntent().getStringExtra(ID.StopAreaExternalCode)); 
				Tools.addFavorite(getBaseContext(), favorite);
				*/
			}
    	}
    	//refreshBookmark();
		//Toast.makeText(this,"Horaire ajouté aux favoris",Toast.LENGTH_SHORT).show();
    }
    
    public void onRemoveBookmark(View view) 
    {
		Bookmark bookmark = new Bookmark();
		bookmark.Activity = DepartureListActivity.class.getName();
		bookmark.Bundle = getIntent().getExtras();
		/*
		bookmark.Bundle.putString(ID.Direction, getIntent().getStringExtra(ID.Direction));
		bookmark.Bundle.putString(ID.LineExternalCode, getIntent().getStringExtra(ID.LineExternalCode)); 
		bookmark.Bundle.putString(ID.Server, getIntent().getStringExtra(ID.Server)); 
		bookmark.Bundle.putString(ID.StopAreaExternalCode, getIntent().getStringExtra(ID.StopAreaExternalCode));*/
		
		Bookmarks bookmarks = Bookmarks.get();
		bookmarks.remove(bookmark);
		bookmarks.save();
		//Tools.removeFavorite(bookmark);
    	refreshBookmark();

		Toast.makeText(this,"Horaire retiré des favoris",Toast.LENGTH_SHORT).show();
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
    
    public void onYesterday(View view) 
    {
		Intent intent = new Intent(this, DepartureListActivity.class);
		intent.putExtra(ID.Server, getIntent().getStringExtra(ID.Server));
		intent.putExtra(ID.NetworkExternalCode, getIntent().getStringExtra(ID.NetworkExternalCode));
		intent.putExtra(ID.LineExternalCode, getIntent().getStringExtra(ID.LineExternalCode));
		intent.putExtra(ID.Direction, getIntent().getStringExtra(ID.Direction));
		intent.putExtra(ID.StopAreaExternalCode, getIntent().getStringExtra(ID.StopAreaExternalCode));
		intent.putExtra(ID.DeltaDate, getIntent().getIntExtra("DeltaDate", 0)-1);
		startActivity(intent);
    }
    
    public void onTomorrow(View view) 
    {
		Intent intent = new Intent(this, DepartureListActivity.class);
		intent.putExtra(ID.Server, getIntent().getStringExtra(ID.Server));
		intent.putExtra(ID.NetworkExternalCode, getIntent().getStringExtra(ID.NetworkExternalCode));
		intent.putExtra(ID.LineExternalCode, getIntent().getStringExtra(ID.LineExternalCode));
		intent.putExtra(ID.Direction, getIntent().getStringExtra(ID.Direction));
		intent.putExtra(ID.StopAreaExternalCode, getIntent().getStringExtra(ID.StopAreaExternalCode));
		intent.putExtra(ID.DeltaDate, getIntent().getIntExtra("DeltaDate", 0)+1);
		startActivity(intent);
    }
    

}
