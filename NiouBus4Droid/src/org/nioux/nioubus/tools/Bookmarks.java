package org.nioux.nioubus.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.nioux.nioubus.BuildConfig;
import org.nioux.nioubus.NiouBusApplication;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Xml;

public class Bookmarks
{
	protected ArrayList<Bookmark> BookmarkList = new ArrayList<Bookmark>();
	
	public String toXML()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<Bookmarks>");
		for(Bookmark bookmark:this.BookmarkList)
		{
			sb.append(bookmark.toXML());
		}
		sb.append("</Bookmarks>");
		return sb.toString();
	}
	
	public static Bookmarks fromXML(InputStream is)
	{
		XmlPullParser parser = Xml.newPullParser();
		try
		{
			parser.setInput(is, null);
			
			/*int eventtype = parser.getEventType();
			while(eventtype != XmlPullParser.END_DOCUMENT)
			{
				Log.e("nioubus", Integer.toString(eventtype));
				parser.next();
				eventtype = parser.getEventType();
			}*/
			
			
			parser.next();
			
			return fromXML(parser);
		}
		catch (Exception e)
		{
			if(BuildConfig.DEBUG)
			{
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static Bookmarks fromXML(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		Bookmarks bookmarks = new Bookmarks();
		if(parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("Bookmarks"))
		{
			parser.next();
			while(parser.getEventType() != XmlPullParser.END_DOCUMENT)
			{
				if(parser.getEventType() != XmlPullParser.END_TAG && parser.getName().equals("Bookmarks"))
				{
					break;
				}
				
				if(parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("Bookmark"))
				{
					Bookmark bookmark = org.nioux.nioubus.tools.Bookmark.fromXML(parser);
					if(bookmark != null)
					{
						bookmarks.BookmarkList.add(bookmark);
					}
				}
				parser.next();
			}
		}
		return bookmarks;
	}

	public static Bookmarks get()
	{
		FileInputStream fis = null;
		try
		{
			fis = NiouBusApplication.getContext().openFileInput("bookmarks.xml");
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
		Bookmarks bookmarks =  fromXML(fis);
		try
		{
			fis.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return bookmarks;
	}
	
	public void save()
	{
		String xml = toXML();
		FileOutputStream fos = null;
		try
		{
			fos = NiouBusApplication.getContext().openFileOutput("bookmarks.xml", Context.MODE_PRIVATE);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return;
		}
		try
		{
			fos.write(xml.getBytes());
			fos.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean exists()
	{
		FileInputStream fis = null;
		try
		{
			fis = NiouBusApplication.getContext().openFileInput("bookmarks.xml");
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return false;
		}
		try
		{
			fis.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return true;
	}

	public Bookmark find(Bookmark bookmark)
	{
		for(Bookmark bookmarkCompare : BookmarkList)
		{
			if(bookmark.equivallent(bookmarkCompare))
			{
				return bookmarkCompare;
			}
		}
		return null;
	}
	
	public boolean contains(Bookmark bookmark)
	{
		return find(bookmark) != null;
	}
	
	public void add(Bookmark bookmark)
	{
		if(!contains(bookmark))
		{
			BookmarkList.add(bookmark);
		}
	}

	public void remove(Bookmark bookmark)
	{
		Bookmark bookmarkFound = find(bookmark);
		if(bookmarkFound != null)
		{
			BookmarkList.remove(bookmarkFound);
		}
	}
	
	public int size()
	{
		return BookmarkList.size();
	}
	
	public Bookmark get(int index)
	{
		return BookmarkList.get(index);
	}
}
