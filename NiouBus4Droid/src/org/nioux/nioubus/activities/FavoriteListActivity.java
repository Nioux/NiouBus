package org.nioux.nioubus.activities;

import org.nioux.nioubus.R;
import org.nioux.nioubus.adapters.FavoriteListAdapter;
import org.nioux.nioubus.tools.Bookmark;
import org.nioux.nioubus.tools.Bookmarks;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;

public class FavoriteListActivity extends ListActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	MainPreferencesActivity.setPreferenceTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites);
        
        setTitle(getResources().getText(R.string.title_favorites));
        registerForContextMenu(getListView());
    }
    
    @Override
    protected void onResume() 
    {
    	super.onResume();
    	refreshFavorites();
    }
    
    void refreshFavorites()
    {
    	Bookmarks favs = Bookmarks.get();
    	if(favs != null)
    	{
    		setListAdapter(new FavoriteListAdapter(this, favs));
    	}
    }

    @Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);

		Bookmark favorite = Bookmark.class.cast(v.getTag());

		if(favorite != null)
		{
			Class<?> cls = null;
			/*try
			{
				cls = Class.forName(favorite.Activity);
			}
			catch (ClassNotFoundException e)
			{
				if(BuildConfig.DEBUG)
				{
					e.printStackTrace();
				}
			}*/
			cls = DepartureListActivity.class;
			if(cls != null)
			{
				Intent intent = new Intent(this, cls);
				intent.putExtras(favorite.Bundle);
				/*for(Entry<String, String> entry : favorite.Parameters.entrySet()) 
				{
				    String key = entry.getKey();
				    String value = entry.getValue();
					intent.putExtra(key, value);
				}*/
				startActivity(intent);
			}
		}
	}
    
    int clickedFavorite = 0;

    static class FavortiesMenuOptions
    {
    	final static int Rename = 1;
    	final static int Delete = 2;
    	final static int Up = 3;
    	final static int Down = 4;
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
    {
    	super.onCreateContextMenu(menu, v, menuInfo);
    	
    	
    	AdapterView.AdapterContextMenuInfo info;
    	try 
    	{
    	    info = (AdapterView.AdapterContextMenuInfo) menuInfo;
    	} 
    	catch (ClassCastException e) 
    	{
    	    //Log.e(TAG, "bad menuInfo", e);
    	    return;
    	}
    	clickedFavorite = info.position;
        menu.setHeaderTitle("Menu");   
        //menu.add(0, FavortiesMenuOptions.Rename, 0, "Renommer");
        //menu.add(0, FavortiesMenuOptions.Up, 0, "Monter");
        //menu.add(0, FavortiesMenuOptions.Down, 0, "Descendre");
        menu.add(0, FavortiesMenuOptions.Delete, 0, "Supprimer");
		
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
    	Bookmarks favorites = FavoriteListAdapter.class.cast(getListAdapter()).getFavorites();
    	
    	switch(item.getItemId())
    	{
    	case FavortiesMenuOptions.Rename:
    		renameFavorite(clickedFavorite);
    		break;
    	case FavortiesMenuOptions.Up:
    		break;
    	case FavortiesMenuOptions.Down:
    		break;
    	case FavortiesMenuOptions.Delete:
    		favorites.remove(favorites.get(clickedFavorite));
    		favorites.save();
    		refreshFavorites();
    		break;
    	}

    	return super.onContextItemSelected(item);
    }
    
    void renameFavorite(int indexFavorite)
    {
    	/*
    	final DOM.FavoriteList favorites = FavoriteListAdapter.class.cast(getListAdapter()).getFavorites();
    	final DOM.Favorite favorite = favorites.Favorites[indexFavorite];
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

            	Tools.saveFavorites(favorites);
            	//Tools.addFavorite(getBaseContext(), favorite);
            	refreshFavorites();
				Toast.makeText(FavoriteListActivity.this,"Favori renommé",Toast.LENGTH_SHORT).show();
          } });
 
        //On crée un bouton "Annuler" à notre AlertDialog et on lui affecte un évènement
        adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	//Lorsque l'on cliquera sur annuler on quittera l'application
            	dialog.dismiss();
          } });
        adb.show();				
    	*/
    }
}

