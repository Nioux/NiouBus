package org.nioux.nioubus.adapters;

import org.nioux.nioubus.R;
import org.nioux.nioubus.navitia.DOM;
import org.nioux.nioubus.tools.Tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ServerListAdapter extends BaseExpandableListAdapter
{
	private final LayoutInflater _inflater;
	
	private DOM.ServerList filterServers(DOM.ServerList servers, String filter)
	{
		if(filter == null || filter.length() == 0)
		{
			return servers;
		}
		else
		{
			DOM.ServerList retServers = new DOM.ServerList();
			for(DOM.Server server:servers.Server)
			{
				if(Tools.filterString(server.ServerName, filter))
				{
					retServers.Server.add(server);
				}
				else
				{
					DOM.Server retServer = new DOM.Server();
					retServer.NetworkList = new DOM.NetworkList();
					retServer.ServerName = server.ServerName;
					retServer.ServerExternalCode = server.ServerExternalCode;
					for(DOM.Network network:server.NetworkList.Network)
					{
						if(Tools.filterString(network.NetworkName, filter) || Tools.filterString(network.NetworkAdditionalData, filter))
						{
							retServer.NetworkList.Network.add(network);
						}
					}
					if(retServer.NetworkList.Network.size() > 0)
					{
						retServers.Server.add(retServer);
					}
				}
			}
			return retServers;
		}
	}
	
	private String _filter = null;
	
	public ServerListAdapter(Context context, DOM.ServerList servers, String filter)
	{
		_inflater = LayoutInflater.from(context);
		_servers = servers;
		_filter = filter;
		_filteredservers = filterServers(servers, filter);
	}

	public synchronized DOM.ServerList getServers()
	{
		return _servers;
	}
	
	public synchronized DOM.ServerList getFilteredServers()
	{
		return _filteredservers;
	}
	
	public String getFilter()
	{
		return _filter;
	}
	
	private DOM.ServerList _servers = null;
	private DOM.ServerList _filteredservers = null;
	
	/*protected synchronized void setServers(DOM.ServerList servers)
	{
		_servers = servers;
		runOnUiThread(new Runnable() { public void run() { toastErrors(); } } );
	}*/
	
	/*
	@Override
	public int getCount() 
	{
		return getServers().getServers().length;
	}

	@Override
	public DOM.Server getItem(int index) 
	{
		return getServers().getServers()[index];
	}

	@Override
	public long getItemId(int arg0) 
	{
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		DOM.Server server = getItem(position);

		View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
		//View v = inflater.inflate(android.R.layout.two_line_list_item, parent, false);
		TextView tvLine1 = (TextView)v.findViewById(android.R.id.text1);
		//TextView tvLine2 = (TextView)v.findViewById(android.R.id.text2);
		tvLine1.setText(server.getServerName().toLowerCase());
		//tvLine2.setText(server.getName());
		return v;
	}
*/
	//@Override
	public DOM.Network getChild(int groupPosition, int childPosition)
	{
		DOM.Server server = getGroup(groupPosition);
		if(server != null)
		{
			DOM.Network network = server.NetworkList.Network.get(childPosition);
			return network;
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
		DOM.Network network = getChild(groupPosition, childPosition);
		View v = _inflater.inflate(R.layout.simple_expandable_list_item_2, parent, false);
		//View v = inflater.inflate(android.R.layout.two_line_list_item, parent, false);
		TextView tvLine1 = (TextView)v.findViewById(android.R.id.text1);
		TextView tvLine2 = (TextView)v.findViewById(android.R.id.text2);
		if(network != null)
		{
			tvLine1.setText(network.NetworkName.toLowerCase());
			tvLine2.setText(network.NetworkAdditionalData.toLowerCase());
		}
		return v;
	}

	//@Override
	public int getChildrenCount(int groupPosition)
	{
		DOM.Server server = getGroup(groupPosition);
		if(server != null)
		{
			return server.NetworkList.Network.size();
		}
		return 0;
	}

	//@Override
	public DOM.Server getGroup(int groupPosition)
	{
		if(getServers() != null)
		{
			return getFilteredServers().Server.get(groupPosition);
		}
		return null;
	}

	//@Override
	public int getGroupCount()
	{
		if(getServers() != null)
		{
			return getFilteredServers().Server.size();
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
		DOM.Server server = getGroup(groupPosition);

		View v = _inflater.inflate(R.layout.simple_expandable_list_item_1, parent, false);
		//View v = inflater.inflate(android.R.layout.two_line_list_item, parent, false);
		TextView tvLine1 = (TextView)v.findViewById(android.R.id.text1);
		//TextView tvLine2 = (TextView)v.findViewById(android.R.id.text2);
		tvLine1.setText(server.ServerName.toLowerCase());
		//tvLine2.setText(server.getName());
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
