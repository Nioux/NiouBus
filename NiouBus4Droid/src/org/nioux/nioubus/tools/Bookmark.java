package org.nioux.nioubus.tools;

import java.io.IOException;
import org.nioux.nioubus.navitia.ID;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Bundle;

public class Bookmark
{
	protected static String xmlEncode(String str)
	{
		StringBuilder sb = new StringBuilder();
		if(str != null)
		{
			for(char c:str.toCharArray())
			{
				if(c == '\'')
				{
					sb.append("&apos;");
				}
				else if(c == '\"')
				{
					sb.append("&quot;");
				}
				else if(c == '<')
				{
					sb.append("&lt;");
				}
				else if(c == '>')
				{
					sb.append("&gt;");
				}
				else if(c == '&')
				{
					sb.append("&amp;");
				}
				else if(c >= 32 && c < 128)
				{
					sb.append(c);
				}
				else
				{
					sb.append("&#");
					sb.append(Integer.toString(c));
					sb.append(";");
				}
			}
		}
		return sb.toString();
	}
	protected static String[] StoredAttributes = {
		ID.Server,
		ID.NetworkExternalCode,
		ID.LineExternalCode,
		ID.StopAreaExternalCode,
		ID.Direction
	};
	
	// attributs
	public String getServer()
	{
		return this.Bundle.getString(ID.Server);
	}
	public void putServer(String value)
	{
		this.Bundle.putString(ID.Server, value);
	}
	public String getNetworkExternalCode()
	{
		return this.Bundle.getString(ID.NetworkExternalCode);
	}
	public void putNetworkExternalCode(String value)
	{
		this.Bundle.putString(ID.NetworkExternalCode, value);
	}
	public String getLineExternalCode()
	{
		return this.Bundle.getString(ID.LineExternalCode);
	}
	public void putLineExternalCode(String value)
	{
		this.Bundle.putString(ID.LineExternalCode, value);
	}
	public String getStopAreaExternalCode()
	{
		return this.Bundle.getString(ID.StopAreaExternalCode);
	}
	public void putStopAreaExternalCode(String value)
	{
		this.Bundle.putString(ID.StopAreaExternalCode, value);
	}
	public String getDirection()
	{
		return this.Bundle.getString(ID.Direction);
	}
	public void putDirection(String value)
	{
		this.Bundle.putString(ID.Direction, value);
	}
/*		public String BookmarkName = "";
	public String Activity = "";
	public String ServerExternalCode = "";
	public String NetworkExternalCode = "";
	public String LineExternalCode = "";
	public String StopAreaExternalCode = "";
	public String Direction = "";
*/		
	public String Activity = "";
	public String Title = "";
	public String SubTitle = "";
	public Bundle Bundle = new Bundle();
	
	public String toXML()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<Bookmark ");
		
		sb.append("Activity=\"");
		sb.append(xmlEncode(this.Activity));
		sb.append("\" ");
		
		sb.append("Title=\"");
		sb.append(xmlEncode(this.Title));
		sb.append("\" ");
		
		sb.append("SubTitle=\"");
		sb.append(xmlEncode(this.SubTitle));
		sb.append("\" ");
		
		for(String key:this.Bundle.keySet())
		{
			sb.append(key);
			sb.append("=\"");
			String value = this.Bundle.getString(key);
			sb.append(xmlEncode(value));
			sb.append("\" ");
		}
		sb.append("/>");
		return sb.toString();
	}
	
	public static Bookmark fromXML(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		Bookmark bookmark = null;
		if(parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("Bookmark"))
		{
			bookmark = new Bookmark();
			for(int i=0;i<parser.getAttributeCount();i++)
			{
				if(parser.getAttributeName(i).equals("Activity"))
				{
					bookmark.Activity = parser.getAttributeValue(i);
				}
				else if(parser.getAttributeName(i).equals("Title"))
				{
					bookmark.Title = parser.getAttributeValue(i);
				}
				else if(parser.getAttributeName(i).equals("SubTitle"))
				{
					bookmark.SubTitle = parser.getAttributeValue(i);
				}
				else
				{
					bookmark.Bundle.putString(parser.getAttributeName(i), parser.getAttributeValue(i));
				}
			}
			//parser.next();
			//if(parser.getEventType() == XmlPullParser.END_TAG && parser.getName().equals("Bookmark"))
			//{
			//}
			parser.next();
		}
		return bookmark;
	}

	public boolean equivallent(Bookmark bookmarkCompare)
	{
		if(!bookmarkCompare.Activity.equals(Activity))
		{
			return false;
		}
		for(String storedAttribute:StoredAttributes)
		{
			Object obj1 = bookmarkCompare.Bundle.get(storedAttribute);
			Object obj2 = Bundle.get(storedAttribute);
			if(obj1 == null && obj2 == null)
			{
			}
			else if(obj1 == null || obj2 == null) 
			{	
				return false;
			}
			else if(!obj1.equals(obj2))
			{
				return false;
			}
		}
		return true;
	}
}
