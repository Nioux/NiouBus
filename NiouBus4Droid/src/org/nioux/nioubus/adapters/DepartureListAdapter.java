package org.nioux.nioubus.adapters;

import java.util.ArrayList;

import org.nioux.nioubus.navitia.DOM;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class DepartureListAdapter extends BaseExpandableListAdapter 
{
	private final LayoutInflater inflater;
	public DOM.DepartureBoardList departures = null;
	
	public DepartureListAdapter(Context context, DOM.DepartureBoardList _departures)
	{
		this.inflater = LayoutInflater.from(context);
		this.departures = _departures;
	}
	
	public DOM.DepartureBoardList getDepartures()
	{
		return departures;
	}
	
	//@Override
	public DOM.Stop getChild(int groupPosition, int childPosition) 
	{
		ArrayList<DOM.Stop> stops = getGroup(groupPosition);
		if(stops != null)
		{
			if(stops.size() > childPosition)
			{
				return stops.get(childPosition);
			}
		}
		return null;
	}

	//@Override
	public long getChildId(int groupPosition, int childPosition) 
	{
		return childPosition;
	}

	//@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) 
	{
		View v = null;
		if(convertView == null)
		{
			v = inflater.inflate(android.R.layout.simple_expandable_list_item_2, parent, false);
		}
		else
		{
			v = convertView;
		}
		TextView tvText1 = (TextView)v.findViewById(android.R.id.text1);
		TextView tvText2 = (TextView)v.findViewById(android.R.id.text2);
		
		DOM.Stop stop = getChild(groupPosition, childPosition);
		if(stop != null)
		{
			v.setTag(stop);
			DOM.StopTime time = stop.StopTime;
			if(time != null)
			{
				tvText1.setText(String.format("%02d:%02d", time.Hour.Value, time.Minute.Value));
			}
			DOM.DepartureTime dtime = stop.DepartureTime;
			if(dtime != null)
			{
				tvText1.setText(String.format("%02d:%02d", dtime.Hour.Value, dtime.Minute.Value));
			}
			if(stop.DestinationPos >= 0)
			{
				DOM.DestinationList destinations = departures.DestinationList;
				if(destinations != null)
				{
					ArrayList<DOM.StopArea> stopareas = destinations.StopArea;
					if(stopareas != null)
					{
						if(stopareas.size() > stop.DestinationPos)
						{
							DOM.StopArea stoparea = stopareas.get(stop.DestinationPos);
							if(stoparea != null)
							{
								StringBuilder line2 = new StringBuilder();
								DOM.City city = stoparea.City;
								if(city != null)
								{
									line2.append(city.CityName);
									line2.append(" - ");
								}
								line2.append(stoparea.StopAreaName);
								tvText2.setText(line2.toString().toLowerCase());
							}
						}
					}
				}
			}
			else if(stop.Route != null && stop.Route.RouteName != null)
			{
				tvText2.setText(stop.Route.RouteName.toLowerCase());
			}
		}
		return v;
	}

	//@Override
	public int getChildrenCount(int groupPosition) 
	{
		ArrayList<DOM.Stop> stops = getGroup(groupPosition);
		if(stops != null)
		{
			return stops.size();
		}
		return 0;
	}

	public ArrayList<DOM.Stop> getRelativeGroup(int groupPosition) 
	{
		ArrayList<DOM.Stop> stops = new ArrayList<DOM.Stop>();
		if(departures != null)
		{
			for(int i = 0; i < departures.StopList.Stop.size(); i++)
			{
				DOM.Stop stop = departures.StopList.Stop.get(i);
				DOM.StopTime time = stop.StopTime;
				if(time != null && time.Hour != null)
				{
					if(groupPosition == time.Hour.Value)
					{
						stops.add(stop);
					}
				}
				DOM.DepartureTime dtime = stop.DepartureTime;
				if(dtime != null && dtime.Hour != null)
				{
					if(groupPosition == dtime.Hour.Value)
					{
						stops.add(stop);
					}
				}
			}
		}
		return stops;
	}
	
	protected ArrayList<ArrayList<DOM.Stop> > _AllGroups = null;
	
	public ArrayList<ArrayList<DOM.Stop> > getAllGroups() 
	{
		if(_AllGroups == null)
		{
			_AllGroups = new ArrayList<ArrayList<DOM.Stop> >();
			for(int i = 0; i < 24;i++)
			{
				ArrayList<DOM.Stop> stops = getRelativeGroup((i+4)%24);
				if(stops != null)
				{
					if(stops.size() > 0)
					{
						_AllGroups.add(stops);
					}
				}
			}
		}
		return _AllGroups;
	}
	

	//@Override
	public ArrayList<DOM.Stop> getGroup(int groupPosition) 
	{
		ArrayList<ArrayList<DOM.Stop> > allgroups = getAllGroups();
		return allgroups.get(groupPosition);
	}
	
	//@Override
	public int getGroupCount() 
	{
		ArrayList<ArrayList<DOM.Stop> > allgroups = getAllGroups();
		return allgroups.size();
	}

	//@Override
	public long getGroupId(int groupPosition) 
	{
		return groupPosition;
	}

	//@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) 
	{
		View v = null;
		if(convertView == null)
		{
			v = inflater.inflate(android.R.layout.simple_expandable_list_item_2, parent, false);
		}
		else
		{
			v = convertView;
		}
		TextView tvText1 = (TextView)v.findViewById(android.R.id.text1);
		TextView tvText2 = (TextView)v.findViewById(android.R.id.text2);
		
		ArrayList<DOM.Stop> stops = getGroup(groupPosition);
		String line1 = "";
		StringBuffer line2 = new StringBuffer();
		for(int i = 0; i < stops.size(); i++)
		{
			if(i != 0)
			{
				line2.append(" / ");
			}
			DOM.Stop stop = stops.get(i);
			DOM.Time time = stop.getStopTime();
			if(time != null)
			{
				line1 = String.format("%02d", time.Hour.Value);
				line2.append(String.format("%02d:%02d", time.Hour.Value, time.Minute.Value));
			}
		}
		tvText1.setText(line1);
		tvText2.setText(line2.toString());

		return v;
	}

	//@Override
	public boolean hasStableIds() 
	{
		return true;
	}

	//@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) 
	{
		return true;
	}
	
}
