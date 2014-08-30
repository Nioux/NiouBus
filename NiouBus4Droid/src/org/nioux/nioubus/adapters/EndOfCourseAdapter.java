package org.nioux.nioubus.adapters;

import org.nioux.nioubus.navitia.DOM;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EndOfCourseAdapter extends BaseAdapter
{
	final private LayoutInflater _inflater;
	final private DOM.EndOfCourseList endofcourse;

	public EndOfCourseAdapter(Context context, DOM.EndOfCourseList endofcourse)
	{
		this._inflater = LayoutInflater.from(context);
		this.endofcourse = endofcourse;
	}
	
	public DOM.EndOfCourseList getEndOfCourseList()
	{
		return this.endofcourse;
	}
	
	public int getCount()
	{
		if(getEndOfCourseList() != null && getEndOfCourseList().StopList != null && getEndOfCourseList().StopList.Stop != null)
		{
			return getEndOfCourseList().StopList.Stop.size();
		}
		return 0;
	}

	public DOM.Stop getItem(int index)
	{
		if(getEndOfCourseList() != null && getEndOfCourseList().StopList != null && getEndOfCourseList().StopList.Stop != null)
		{
			return getEndOfCourseList().StopList.Stop.get(index);
		}
		return null;
	}

	public long getItemId(int index)
	{
		return index;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		View v = null;
		if(convertView != null)
		{
			v = convertView;
		}
		else
		{
			v = _inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
		}
		TextView tvLine1 = (TextView)v.findViewById(android.R.id.text1);
		TextView tvLine2 = (TextView)v.findViewById(android.R.id.text2);

		DOM.Stop stop = getItem(position);
		if(stop != null)
		{
			if(tvLine1 != null)
			{
				DOM.StopTime time = stop.StopTime;
				if(time != null && time.Hour != null && time.Minute != null)
				{
					tvLine1.setText(String.format("%02d:%02d", time.Hour.Value, time.Minute.Value));
				}
			}
			if(tvLine2 != null)
			{
				DOM.StopPoint stoppoint = stop.StopPoint;
				if(stoppoint != null && stoppoint.City != null && stoppoint.City.CityName != null && stoppoint.StopPointName != null)
				{
					tvLine2.setText(String.format("%s - %s", stop.StopPoint.City.CityName.toLowerCase(), stop.StopPoint.StopPointName.toLowerCase()));
				}
			}
		}
		return v;
	}
}
