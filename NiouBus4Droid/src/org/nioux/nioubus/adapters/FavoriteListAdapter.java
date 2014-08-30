package org.nioux.nioubus.adapters;

import org.nioux.nioubus.tools.Bookmark;
import org.nioux.nioubus.tools.Bookmarks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FavoriteListAdapter extends BaseAdapter
{
	private final LayoutInflater inflater;
	private final Bookmarks bookmarks;
	
	public Bookmarks getFavorites()
	{
		return bookmarks;
	}
	
	public FavoriteListAdapter(Context context, Bookmarks bookmarks)
	{
		this.inflater = LayoutInflater.from(context);
		this.bookmarks = bookmarks;
	}
	
	//@Override
	public int getCount() 
	{
		if(bookmarks != null)
		{
			return bookmarks.size();
		}
		return 0;
	}

	//@Override
	public Bookmark getItem(int index) 
	{
		if(bookmarks != null)
		{
			return bookmarks.get(index);
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
		Bookmark bookmark = getItem(position);

		View v = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
		//View v = inflater.inflate(android.R.layout.two_line_list_item, parent, false);
		TextView tvLine1 = (TextView)v.findViewById(android.R.id.text1);
		TextView tvLine2 = (TextView)v.findViewById(android.R.id.text2);
		if(bookmark != null)
		{
			v.setTag(bookmark);
			tvLine1.setText(bookmark.Title.toLowerCase());
			tvLine2.setText(bookmark.SubTitle.toLowerCase());
		}
		return v;
	}
	
}
