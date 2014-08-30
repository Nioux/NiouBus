package org.nioux.nioubus.adapters;

import org.nioux.nioubus.navitia.DOM;
import org.nioux.nioubus.tools.Tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StopAreaListAdapter extends BaseAdapter
{
	private final LayoutInflater _inflater;
	private final DOM.ActionLineStopAreaList _stopareas;
	private DOM.ActionLineStopAreaList _filteredstopareas = null;
	protected String _filter = null;

	protected DOM.ActionLineStopAreaList filterStopAreas(DOM.ActionLineStopAreaList stopareas, String filter)
	{
		if(filter == null || filter.length() == 0)
		{
			return stopareas;
		}
		DOM.ActionLineStopAreaList retStopAreas = new DOM.ActionLineStopAreaList();
		retStopAreas.LineStopAreaList = new DOM.LineStopAreaList();
		for(DOM.StopArea stoparea:stopareas.LineStopAreaList.StopArea)
		{
			if(Tools.filterString(stoparea.StopAreaName, filter) || Tools.filterString(stoparea.City.CityName, filter))
			{
				retStopAreas.LineStopAreaList.StopArea.add(stoparea);
			}
		}
		return retStopAreas;
	}
	
	public StopAreaListAdapter(Context context, DOM.ActionLineStopAreaList stopareas, String filter)
	{
		_inflater = LayoutInflater.from(context);
		_stopareas = stopareas;
		_filter = filter;
		_filteredstopareas = filterStopAreas(_stopareas, _filter);
	}
	
	public DOM.ActionLineStopAreaList getStopAreas()
	{
		return _stopareas;
	}
	
	public DOM.ActionLineStopAreaList getFilteredStopAreas()
	{
		return _filteredstopareas;
	}
	
	//@Override
	public int getCount() 
	{
		if(getFilteredStopAreas() != null)
		{
			return getFilteredStopAreas().LineStopAreaList.StopArea.size();
		}
		return 0;
	}

	//@Override
	public DOM.StopArea getItem(int index) 
	{
		if(getFilteredStopAreas() != null)
		{
			return getFilteredStopAreas().LineStopAreaList.StopArea.get(index);
		}
		return null;
	}

	//@Override
	public long getItemId(int arg0) 
	{
		return 0;
	}

	//@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		DOM.StopArea stoparea = getItem(position);

		//View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
		View v = _inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
		TextView tvLine1 = (TextView)v.findViewById(android.R.id.text1);
		TextView tvLine2 = (TextView)v.findViewById(android.R.id.text2);
		if(stoparea != null)
		{
			tvLine1.setText(stoparea.StopAreaName.toLowerCase());
			tvLine2.setText(stoparea.City.CityName.toLowerCase());
		}
		return v;
	}
	
}

