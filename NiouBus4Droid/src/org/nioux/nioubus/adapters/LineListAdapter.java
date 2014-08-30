package org.nioux.nioubus.adapters;

import org.nioux.nioubus.navitia.DOM;
import org.nioux.nioubus.tools.Tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class LineListAdapter extends BaseExpandableListAdapter
{
	protected final LayoutInflater _inflater;

	protected final DOM.ActionLineList _lines;
	protected DOM.ActionLineList _filteredlines;
	protected String _filter;
	
	DOM.ActionLineList filterLines(DOM.ActionLineList lines, String filter)
	{
		DOM.ActionLineList retLines = new DOM.ActionLineList();
		retLines.LineList = new DOM.LineList();
		for(DOM.Line line:lines.LineList.Line)
		{
			if(Tools.filterString(line.LineCode, filter) || Tools.filterString(line.LineName, filter))
			{
				retLines.LineList.Line.add(line);
			}
			else
			{
				DOM.Line retLine = new DOM.Line();
				retLine.LineCode = line.LineCode;
				retLine.LineExternalCode = line.LineExternalCode;
				retLine.LineId = line.LineId;
				retLine.LineIdx = line.LineIdx;
				retLine.LineName = line.LineName;
				if(line.Backward != null && Tools.filterString(line.Backward.BackwardName, filter))
				{
					retLine.Backward = line.Backward;
				}
				if(line.Forward != null && Tools.filterString(line.Forward.ForwardName, filter))
				{
					retLine.Forward = line.Forward;
				}
			
				if(retLine.Backward != null || retLine.Forward != null)
				{
					retLines.LineList.Line.add(retLine);
				}
			}
		}
		return retLines;
	}
	
	public LineListAdapter(Context context, DOM.ActionLineList lines, String filter)
	{
		_inflater = LayoutInflater.from(context);
		_lines = lines;
		_filter = filter;
		if(filter == null)
		{
			_filteredlines = _lines;
		}
		else
		{
			_filteredlines = filterLines(_lines, _filter);
		}
	}
	
	public DOM.ActionLineList getLines()
	{
		return _lines;
	}
	
	public DOM.ActionLineList getFilteredLines()
	{
		return _filteredlines;
	}
	
	public String getFilter()
	{
		return _filter;
	}
	
	
	
	//@Override
	public DOM.Direction getChild(int groupPosition, int childPosition)
	{
		int children = 0;
		DOM.Line line = getGroup(groupPosition);
		if(line != null)
		{
			if(line.Forward != null && line.Forward.ForwardName != null && line.Forward.ForwardName.length() > 0)
			{
				if(childPosition == children)
				{
					return line.Forward.Direction;
				}
				children++;
			}
			if(line.Backward != null && line.Backward.BackwardName != null && line.Backward.BackwardName.length() > 0)
			{
				if(childPosition == children)
				{
					return line.Backward.Direction;
				}
				children++;
			}
		}
		return null;
	}

	public String getChildName(int groupPosition, int childPosition)
	{
		int children = 0;
		DOM.Line line = getGroup(groupPosition);
		if(line != null)
		{
			if(line.Forward != null && line.Forward.ForwardName != null && line.Forward.ForwardName.length() > 0)
			{
				if(childPosition == children)
				{
					return line.Forward.ForwardName;
				}
				children++;
			}
			if(line.Backward != null && line.Backward.BackwardName != null && line.Backward.BackwardName.length() > 0)
			{
				if(childPosition == children)
				{
					return line.Backward.BackwardName;
				}
				children++;
			}
		}
		return null;
	}
	
	//@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		return 0;
	}

	//@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
	{
		String direction = getChildName(groupPosition, childPosition);
		
		View v = _inflater.inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
		//View v = inflater.inflate(android.R.layout.two_line_list_item, parent, false);
		TextView tvLine1 = (TextView)v.findViewById(android.R.id.text1);
		//TextView tvLine2 = (TextView)v.findViewById(android.R.id.text2);
		tvLine1.setText(direction.toLowerCase());
		//tvLine2.setText(direction.getStopArea().getStopAreaExternalCode().toLowerCase());
		//v.setFocusable(false);
		//System.err.println(v.isClickable());
		//v.setClickable(true);
		return v;
	}

	//@Override
	public int getChildrenCount(int groupPosition)
	{
		int children = 0;
		DOM.Line line = getGroup(groupPosition);
		if(line != null)
		{
			if(line.Forward != null && line.Forward.ForwardName != null && line.Forward.ForwardName.length() > 0)
			{
				children++;
			}
			if(line.Backward != null && line.Backward.BackwardName != null && line.Backward.BackwardName.length() > 0)
			{
				children++;
			}
		}
		return children;
	}

	//@Override
	public DOM.Line getGroup(int groupPosition)
	{
		if(getFilteredLines() != null)
		{
			return getFilteredLines().LineList.Line.get(groupPosition);
		}
		return null;
	}

	//@Override
	public int getGroupCount()
	{
		if(getFilteredLines() != null)
		{
			return getFilteredLines().LineList.Line.size();
		}
		return 0;
	}

	//@Override
	public long getGroupId(int groupPosition)
	{
		return 0;
	}

	//@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{
		DOM.Line line = getGroup(groupPosition);

		View v = _inflater.inflate(android.R.layout.simple_expandable_list_item_2, parent, false);
		//View v = inflater.inflate(android.R.layout.two_line_list_item, parent, false);
		TextView tvLine1 = (TextView)v.findViewById(android.R.id.text1);
		TextView tvLine2 = (TextView)v.findViewById(android.R.id.text2);
		if(line != null)
		{
			tvLine1.setText(line.LineCode.toLowerCase());
			tvLine2.setText(line.LineName.toLowerCase());
		}
		return v;
	}

	//@Override
	public boolean hasStableIds()
	{
		return false;
	}

	//@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return true;
	}
	
}
