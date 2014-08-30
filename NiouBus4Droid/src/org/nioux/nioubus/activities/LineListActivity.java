package org.nioux.nioubus.activities;

import org.nioux.nioubus.R;
import org.nioux.nioubus.adapters.LineListAdapter;
import org.nioux.nioubus.navitia.DOM;
import org.nioux.nioubus.navitia.ID;
import org.nioux.nioubus.tasks.LineListTask;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class LineListActivity extends ExpandableListActivity implements TextWatcher
{

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	MainPreferencesActivity.setPreferenceTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lines);
        
        EditText etFilter = EditText.class.cast(findViewById(R.id.editTextFilter));
        etFilter.addTextChangedListener(this);
        
        String networkname = getIntent().getStringExtra(ID.NetworkName);
        if(networkname != null)
        {
        	setTitle(String.format("%s : %s", getResources().getText(R.string.title_lines), networkname.toLowerCase()));
        }
        else
        {
        	setTitle(String.format("%s : %s", getResources().getText(R.string.title_lines), ""));
        }
        
        new LineListTask(this).execute(getIntent());
    }
    
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		
	}
	
	public void beforeTextChanged(CharSequence s, int start, int count, int after)
	{
		
	}
	
	public void afterTextChanged(Editable s)
	{
		LineListAdapter adapter = LineListAdapter.class.cast(getExpandableListAdapter());
		if(adapter != null)
		{
			setListAdapter(new LineListAdapter(LineListActivity.this, adapter.getLines(), s.toString()));
			if(s.toString().length() > 0)
			{
				for(int i = 0; i < getExpandableListAdapter().getGroupCount(); i++)
				{
					getExpandableListView().expandGroup(i);
				}
			}
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
    
    @Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
    {
    	LineListAdapter adapter = LineListAdapter.class.cast(getExpandableListAdapter());
    	
    	if(adapter != null && adapter.getFilteredLines() != null)
    	{
	    	DOM.Line line = adapter.getFilteredLines().LineList.Line.get(groupPosition);
	    	Intent intent = null;
	    	if(getIntent().getStringExtra(ID.StopAreaExternalCode) == null)
	    	{
				intent = new Intent(this, StopAreaListActivity.class);
	    	}
	    	else
	    	{
				intent = new Intent(this, DepartureListActivity.class);
				intent.putExtra(ID.StopAreaExternalCode, getIntent().getStringExtra(ID.StopAreaExternalCode));
	    	}
			intent.putExtra(ID.Server, getIntent().getStringExtra(ID.Server));
			intent.putExtra(ID.NetworkExternalCode, getIntent().getStringExtra(ID.NetworkExternalCode));
			intent.putExtra(ID.LineExternalCode, line.LineExternalCode);
			intent.putExtra(ID.Direction, Integer.toString(1-(childPosition*2)));
			startActivity(intent);
    	}
    	return true;
    }



}
