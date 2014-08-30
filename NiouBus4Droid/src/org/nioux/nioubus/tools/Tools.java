package org.nioux.nioubus.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.WeakHashMap;

import org.nioux.nioubus.BuildConfig;
import org.nioux.nioubus.NiouBusApplication;
import org.nioux.nioubus.navitia.DOM;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Xml;

public class Tools
{

	private static final String uriBase = "http://<server>.<placeholder>";

	private static Hashtable<String, Class<?> > getDOMHashtable()
	{
		Hashtable<String, Class<?> > map = new Hashtable<String, Class<?> >();
		Class<?>[] clss = DOM.class.getDeclaredClasses();
		for(int i = 0; i < clss.length; i++)
		{
			map.put(clss[i].getSimpleName(), clss[i]);
		}
		return map;
	}
	
	private static Hashtable<String, Class<?> > MapClasses = getDOMHashtable();
	
	static Object getObjectFromName(String name)
	{
		Object retObj = null;
		Class<?> clazz = MapClasses.get(name);
		if(clazz == null)
		{
			return null;
		}
		try
		{
			retObj = clazz.newInstance();
		}
		catch (InstantiationException e)
		{
			if(BuildConfig.DEBUG)
			{
				e.printStackTrace();
			}
		}
		catch (IllegalAccessException e)
		{
			if(BuildConfig.DEBUG)
			{
				e.printStackTrace();
			}
		}
		return retObj;
	}

	public static Object getObjectFromXml(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		Object retObj = getObjectFromName(parser.getName());

		if(retObj != null)
		{
			loadContent(retObj, parser);
		}

		return retObj;
	}
	
	
	
	public static void unknownTag(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		if(!parser.isEmptyElementTag())
		{
			int eventType = parser.next();
			while(eventType != XmlPullParser.END_TAG)
			{
				if(eventType == XmlPullParser.START_TAG)
				{
					unknownTag(parser);
				}
				eventType = parser.next();
			}
		}
	}
	
	public static Field getSafeField(Object objThis, String name)
	{
		Field field = null;
		try
		{
			field = objThis.getClass().getField(name);//.getDeclaredField(name);
		}
		catch (NoSuchFieldException e)
		{
			// e.printStackTrace();
			if(BuildConfig.DEBUG)
			{
				System.err.println("Class : " + objThis.getClass().getSimpleName() +  " / NoSuchField : " + name);
			}
		}
		return field;
	}
	
	public static void setObjectField(Object objThis, Field field, Object value)
	{
		//Field field = getSafeField(objThis, name);
		if(field != null)
		{
			try
			{
				if(field.getType().equals(int.class))
				{
					field.setInt(objThis, Integer.parseInt(String.class.cast(value)));
				}
				else if(field.getType().equals(double.class))
				{
					field.setDouble(objThis, Double.parseDouble(String.class.cast(value).replace(',', '.')));
				}
				else if(field.getType().equals(boolean.class))
				{
					field.setBoolean(objThis, String.class.cast(value).equals("True"));
				}
				else if(field.getType().equals(String.class))
				{
					String strValue = String.class.cast(value);
					if(strValue != null)
					{
						strValue = strValue.replace("\\", "");
						strValue = strValue.replace("&gt;", ">");
						strValue = strValue.replace("&#39;", "'");
						strValue = strValue.replace("&#039;", "'");
						field.set(objThis, strValue);
					}
				}
				else if(field.getType().equals(ArrayList.class))
				{
					ArrayList.class.cast(field.get(objThis)).add(value);
				}
				else
				{
					field.set(objThis, value);
				}
			}
			catch (NumberFormatException e)
			{
				if(BuildConfig.DEBUG)
				{
					e.printStackTrace();
				}
			}
			catch (IllegalArgumentException e)
			{
				if(BuildConfig.DEBUG)
				{
					e.printStackTrace();
				}
			}
			catch (IllegalAccessException e)
			{
				if(BuildConfig.DEBUG)
				{
					e.printStackTrace();
				}
			}
		}
	}
/*	
	public static void setObjectField(Object objThis, Field field, Object value)
	{
		try
		{
			Class<?> cls = ArrayList.class;
			ArrayList<Object> al = ArrayList<>.cast(field.get(objThis));
			if(al == null)
			{
				field.set(objThis, value);
			}
			else
			{
				al.add(value);
			}
		}
		catch (IllegalArgumentException e)
		{
			//e.printStackTrace();
			System.err.println("Class : " + objThis.getClass().getSimpleName() +  " / IllegalArgument : " + field.getName());
		}
		catch (IllegalAccessException e)
		{
			//e.printStackTrace();
			System.err.println("Class : " + objThis.getClass().getSimpleName() +  " / IllegalAccess : " + field.getName());
		}
	}
*/
	public static void loadContent(Object objThis, XmlPullParser parser) throws XmlPullParserException, IOException
	{
		for(int i = 0; i<parser.getAttributeCount();i++)
		{
			String name = parser.getAttributeName(i);
			String value = parser.getAttributeValue(i);
			Field field = getSafeField(objThis, name);
			if(field != null)
			{
				setObjectField(objThis, field, value);
			}
		}
		if(!parser.isEmptyElementTag())
		{
			StringBuilder __Content = new StringBuilder();
			int eventType = parser.next();
			while(eventType != XmlPullParser.END_TAG)
			{
				if(eventType == XmlPullParser.START_TAG)
				{
					String name = parser.getName();

					// TODO : changer la façon dont sont gérés les fields vides
					Field field = getSafeField(objThis, name);
					if(field != null)
					{
						Object objValue = getObjectFromXml(parser);
						setObjectField(objThis, field, objValue);
					}
					else
					{
						unknownTag(parser);
					}
				}
				else if(eventType == XmlPullParser.TEXT)
				{
					__Content.append(parser.getText().trim());
				}
				eventType = parser.next();
			}
			if(__Content.length() > 0)
			{
				Field field = getSafeField(objThis, "Value");
				if(field != null)
				{
					setObjectField(objThis, field, __Content.toString());
				}
			}
		}
		else
		{
			parser.next();
		}
	}
	

	static Object getObjectFromFile(String filename)
	{
		Object retObj = null;
		/*
		 * FileInputStream fis; try { fis = new FileInputStream(filename);
		 * ObjectInputStream ois = new ObjectInputStream(fis); retObj =
		 * ois.readObject(); fis.close(); } catch (FileNotFoundException e) {
		 * e.printStackTrace(); } catch (StreamCorruptedException e) {
		 * e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); }
		 * catch (ClassNotFoundException e) { e.printStackTrace(); }
		 */
		return retObj;
	}

	// TODO : regarder les SoftReference, WeakHashMap, ...
	//static Hashtable<String, Object> objectCache = new Hashtable<String, Object>();
	static WeakHashMap<String, Object> objectCache = new WeakHashMap<String, Object>();

	static Object getObjectFromMemory(String cachename)
	{
		if (objectCache.containsKey(cachename))
		{
			return objectCache.get(cachename);
		}
		return null;
	}

	static void saveObjectToMemory(String cachename, Object obj)
	{
		objectCache.put(cachename, obj);
	}

	static String lockInputStream = "lock";
	static InputStream globalInputStream = null;
	
	public static void openGlobalConnection(URL url)
	{
		try
		{
			globalInputStream = url.openStream();
		}
		catch (IOException e)
		{
			if(BuildConfig.DEBUG)
			{
				e.printStackTrace();
			}
		}
	}
	public static void closeGlobalConnection()
	{
		synchronized(lockInputStream)
		{
			if(globalInputStream != null)
			{
				try
				{
					globalInputStream.close();
				}
				catch (IOException e)
				{
					if(BuildConfig.DEBUG)
					{
						e.printStackTrace();
					}
				}
				globalInputStream = null;
			}
		}
	}
	
	public static Object getObjectFromUrl(String uri)
	{
		//InputStream is = null;
		URL url = null;
		try
		{
			if(BuildConfig.DEBUG)
			{
				System.err.println(uri);
			}
			url = new URL(uri);
		}
		catch (MalformedURLException e)
		{
			if(BuildConfig.DEBUG)
			{
				e.printStackTrace();
			}
		}
		closeGlobalConnection();
		openGlobalConnection(url);
		Object retObj = getObjectFromStream(globalInputStream);
		closeGlobalConnection();
		return retObj;
	}

	public static Object getObjectFromStream(InputStream is)
	{
		XmlPullParser parser = Xml.newPullParser();
		try
		{
			parser.setInput(is, null);
			parser.next();
			return getObjectFromXml(parser);
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

	public static Object getObjectFromRequest(String server, Hashtable<String, String> params)
	{
		Object retObj = null;

		retObj = getObjectFromMemory(buildCacheName(server, params));
		if (retObj != null)
		{
			return retObj;
		}

		retObj = getObjectFromFile(buildCacheName(server, params));
		if (retObj != null)
		{
			saveObjectToMemory(buildCacheName(server, params), retObj);
			return retObj;
		}

		retObj = getObjectFromUrl(buildRequest(server, params));
		if (retObj != null)
		{
			saveObjectToMemory(buildCacheName(server, params), retObj);
			//saveObjectToFile(buildCacheName(server, params), retObj);
			return retObj;
		}
		return null;
	}

	/*
	 * private Document loadXmlFromFile(String filename) { try { FileInputStream
	 * is = new FileInputStream(filename);
	 * 
	 * DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	 * DocumentBuilder builder = factory.newDocumentBuilder(); Document dom =
	 * builder.parse(is);
	 * 
	 * is.close();
	 * 
	 * return dom; } catch (FileNotFoundException e) { e.printStackTrace(); }
	 * catch (ParserConfigurationException e) { e.printStackTrace(); } catch
	 * (SAXException e) { e.printStackTrace(); } catch (IOException e) {
	 * e.printStackTrace(); } return null; }
	 * 
	 * private static void saveXmlToFile(Document doc, String filename) {
	 * 
	 * // Prepare the DOM document for writing Source source = new
	 * DOMSource(doc);
	 * 
	 * // Prepare the output file File file = new File(filename); Result result
	 * = new StreamResult(file);
	 * 
	 * // Write the DOM document to the file Transformer xformer; try { xformer
	 * = TransformerFactory.newInstance().newTransformer();
	 * xformer.transform(source, result); } catch
	 * (TransformerConfigurationException e) { e.printStackTrace(); } catch
	 * (TransformerFactoryConfigurationError e) { e.printStackTrace(); } catch
	 * (TransformerException e) { e.printStackTrace(); }
	 * 
	 * }
	 */
	
	private static String urlEncode(String input) {
		String output = "";
		if(input != null)
		{
			for(char c:input.toCharArray()) {
				switch(c) {
				case ' ':
					output += "%20";
					break;
				default:
					output += c;
					break;
				}
			}
		}
		return output;
	}
	private static String buildRequest(String server, Hashtable<String, String> params)
	{
		String uri = uriBase.replace("<server>", server);
		for (String key : params.keySet())
		{
			if(params.get(key) != null)
			{
				uri += key + "=" + urlEncode(params.get(key)) + "&";
			}
		}
		return uri;
	}

	private static String buildCacheName(String server, Hashtable<String, String> params)
	{
		String file = "";
		file += server;
		file += "_";
		for (String key : params.keySet())
		{
			if(params.get(key) != null)
			{
				file += key + "-" + params.get(key) + "_";
			}
		}
		return file;
	}

	/*
	 * private Document getXmlRequest(String server, Hashtable<String, String>
	 * params) { Document doc; String filename = buildCacheName(server, params);
	 * Log.i("Transpoid", filename); doc = loadXmlFromFile(filename); if(doc ==
	 * null) { String uri = buildRequest(server, params); Log.i("Transpoid",
	 * uri); doc = loadXmlFromUri(uri); saveXmlToFile(doc, filename); } return
	 * doc; }
	 */

	public static void saveObjectToFile(String filename, Object obj)
	{
		FileOutputStream fos;
		try
		{
			fos = NiouBusApplication.getContext().openFileOutput(filename, Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(obj);
			os.close();
		}
		catch (FileNotFoundException e)
		{
			if(BuildConfig.DEBUG)
			{
				e.printStackTrace();
			}
		}
		catch (IOException e)
		{
			if(BuildConfig.DEBUG)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static Object loadObjectFromFile(String filename)
	{
		FileInputStream fis;
		try
		{
			fis = NiouBusApplication.getContext().openFileInput(filename);
			ObjectInputStream is = new ObjectInputStream(fis);
			Object obj = is.readObject();
			is.close();
			return obj;
		}
		catch (FileNotFoundException e)
		{
			if(BuildConfig.DEBUG)
			{
				e.printStackTrace();
			}
		}
		catch (StreamCorruptedException e)
		{
			if(BuildConfig.DEBUG)
			{
				e.printStackTrace();
			}
		}
		catch (IOException e)
		{
			if(BuildConfig.DEBUG)
			{
				e.printStackTrace();
			}
		}
		catch (ClassNotFoundException e)
		{
			if(BuildConfig.DEBUG)
			{
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	
	
	
	
	private static final String PLAIN_ASCII = "AaEeIiOoUu" // grave
        + "AaEeIiOoUuYy" // acute
        + "AaEeIiOoUuYy" // circumflex
        + "AaOoNn" // tilde
        + "AaEeIiOoUuYy" // umlaut
        + "Aa" // ring
        + "Cc" // cedilla
        + "OoUu" // double acute
        ;

	private static final String UNICODE = "\u00C0\u00E0\u00C8\u00E8\u00CC\u00EC\u00D2\u00F2\u00D9\u00F9"
        + "\u00C1\u00E1\u00C9\u00E9\u00CD\u00ED\u00D3\u00F3\u00DA\u00FA\u00DD\u00FD"
        + "\u00C2\u00E2\u00CA\u00EA\u00CE\u00EE\u00D4\u00F4\u00DB\u00FB\u0176\u0177"
        + "\u00C3\u00E3\u00D5\u00F5\u00D1\u00F1"
        + "\u00C4\u00E4\u00CB\u00EB\u00CF\u00EF\u00D6\u00F6\u00DC\u00FC\u0178\u00FF"
        + "\u00C5\u00E5" + "\u00C7\u00E7" + "\u0150\u0151\u0170\u0171";

	/**
	 * remove accented from a string and replace with ascii equivalent
	 */
	public static String removeAccents(String s) {
	    if (s == null)
	        return null;
	    StringBuilder sb = new StringBuilder(s.length());
	    int n = s.length();
	    int pos = -1;
	    char c;
	    boolean found = false;
	    for (int i = 0; i < n; i++) {
	        pos = -1;
	        c = s.charAt(i);
	        pos = (c <= 126) ? -1 : UNICODE.indexOf(c);
	        if (pos > -1) {
	            found = true;
	            sb.append(PLAIN_ASCII.charAt(pos));
	        } else {
	            sb.append(c);
	        }
	    }
	    if (!found) {
	        return s;
	    } else {
	        return sb.toString();
	    }
	}

	public static boolean filterString(String data, String filter)
	{
		return removeAccents(data.trim()).toLowerCase().contains(removeAccents(filter.trim()).toLowerCase());
	}
	
}
