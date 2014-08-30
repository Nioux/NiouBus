package org.nioux.nioubus.navitia;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map.Entry;

import org.nioux.nioubus.NiouBusApplication;
import org.nioux.nioubus.R;
import org.nioux.nioubus.activities.DepartureListActivity;
import org.nioux.nioubus.tools.Bookmark;
import org.nioux.nioubus.tools.Bookmarks;
import org.nioux.nioubus.tools.Tools;

public class DOM
{

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Actions
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public interface IResult<T>
	{
		void onFinished(T t);
	}
	
	@SuppressWarnings("serial")
	public static class Params extends Hashtable<String, String>
	{
		final protected String server;
		public Params(String server)
		{
			this.server = server;
		}
		
		public String getServer()
		{
			return this.server;
		}
		
		@Override
		public synchronized String put(String key, String value)
		{
			if(key == null || value == null)
			{
				return null;
			}
			return super.put(key, value);
		}
	}
	
	@SuppressWarnings("serial")
	public static class Request implements Serializable
	{
		public static Object get(Params params)
		{
			return Tools.getObjectFromRequest(params.getServer(), params);
		}
	}
	
	@SuppressWarnings("serial")
	public static class ActionJourneyResultList extends Request
	{
		public final static String Action = "PlanJourney";
		// elements
		public JourneyResultList JourneyResultList = null;
		public CommentList CommentList = null;
		public ODTList ODTList = null;
		public EventList EventList = null;
		
		public static ActionJourneyResultList get(String Server, String Departure, String Arrival, String Sens, String Time, String Date, String DateDemand, String HangDistance, String Criteria, String Mode, String Extension, String WalkSpeed, String NbBefore, String NbAfter, String User)
		{
			Params params = new Params(Server);
			params.put(ID.Action, Action);
			params.put(ID.Departure, Departure);
			params.put(ID.Arrival, Arrival);
			params.put(ID.Sens, Sens);
			params.put(ID.Time, Time);
			params.put(ID.Date, Date);
			params.put(ID.DateDemand, DateDemand);
			params.put(ID.HangDistance, HangDistance);
			params.put(ID.Criteria, Criteria);
			params.put(ID.Mode, Mode);
			params.put(ID.Extension, Extension);
			params.put(ID.WalkSpeed, WalkSpeed);
			params.put(ID.NbBefore, NbBefore);
			params.put(ID.NbAfter, NbAfter);
			params.put(ID.User, User);
			return ActionJourneyResultList.class.cast(get(params));
		}
	}

	@SuppressWarnings("serial")
	public static class ActionLineStopAreaList extends Request
	{
		public final static String Action = "LineStopAreaList";
		// elements
		public LineStopAreaList LineStopAreaList = null;
		
		//public static ActionLineStopAreaList get(String Server, String LineExternalCode)
		//{
		//	Params params = new Params(Server);
		//	params.put(ID.Action, Action);
		//	params.put(ID.LineExternalCode, LineExternalCode);
		//	return ActionLineStopAreaList.class.cast(get(params));
		//}
	}

	@SuppressWarnings("serial")
	public static class ActionLineList extends Request
	{
		public final static String Action = "LineList";
		// elements
		public LineList LineList = null;

		public static ActionLineList get(String Server, String NetworkExternalCode, String LineExternalCode, String ModeExternalCode)
		{
			Params params = new Params(Server);
			params.put(ID.Action, Action);
			params.put(ID.NetworkExternalCode, NetworkExternalCode);
			params.put(ID.LineExternalCode, LineExternalCode);
			params.put(ID.ModeExternalCode, ModeExternalCode);
			return ActionLineList.class.cast(get(params));
		}
	}

	@SuppressWarnings("serial")
	public static class ActionNetworkList extends Request
	{
		public final static String Action = "NetworkList";
		// elements
		public NetworkList NetworkList = null;

		public static ActionNetworkList get(String Server, String NetworkExternalCode, String LineExternalCode, String ModeExternalCode)
		{
			Params params = new Params(Server);
			params.put(ID.Action, Action);
			params.put(ID.NetworkExternalCode, NetworkExternalCode);
			params.put(ID.LineExternalCode, LineExternalCode);
			params.put(ID.ModeExternalCode, ModeExternalCode);
			return ActionNetworkList.class.cast(get(params));
		}
	}

	@SuppressWarnings("serial")
	public static class ActionModeList extends Request
	{
		public final static String Action = "ModeList";
		// elements
		public ModeList ModeList = null;
	}

	@SuppressWarnings("serial")
	public static class ActionStopAreaList extends Request
	{
		public final static String Action = "StopAreaList";
		// elements
		public StopAreaList StopAreaList = null;
	}

	@SuppressWarnings("serial")
	public static class ActionEntryPointList extends Request
	{
		public final static String Action = "EntryPoint";
		// elements
		public EntryPointList EntryPointList = null;
		public PagerInfo PagerInfo = null;
		
		public static ActionEntryPointList get(String Server, String Name, String NbMax)
		{
			Params params = new Params(Server);
			params.put(ID.Action, Action);
			params.put(ID.Name, Name);
			params.put(ID.NbMax, NbMax);
			return ActionEntryPointList.class.cast(get(params));
		}

		public static ActionEntryPointList get(String Server, String Name, int NbMax)
		{
			return get(Server, Name, Integer.toString(NbMax));
		}
	}

	@SuppressWarnings("serial")
	public static class BinaryCriteria extends Request
	{
		public final static String Action = "MakeBinaryCriteria";
		// elements
		public Vehicle Vehicle = null;
		public StopPointEquipment StopPointEquipment = null;
		public Mode Mode = null;
		public ModeType ModeType = null;
	}
	
	
	@SuppressWarnings("serial")
	public static class EndOfCourseList extends Request
	{
		public final static String Action = "EndOfCourse";
		
		public StopList StopList = null;
		public VehicleJourney VehicleJourney = null;
	}
		
	@SuppressWarnings("serial")
	public static class ActionProximityList extends Request
	{
		public final static String Action = "ProximityList";
		
		public ProximityList ProximityList = null;
	}

	@SuppressWarnings("serial")
	public static class ActionStreetNetwork extends Request
	{
		public final static String Action = "StreetNetwork";
		
		public SegmentList SegmentList = null;
	}

	@SuppressWarnings("serial")
	public static class ActionNextDepartureList extends Request
	{
		public final static String Action = "NextDeparture";
		
		public NextDepartureList NextDepartureList = null;
		public EventList EventList = null;
	}

	@SuppressWarnings("serial")
	public static class ActionImpactList extends Request
	{
		public final static String Action = "GetImpactList";
		
		public ImpactList ImpactList = null;
	}

	@SuppressWarnings("serial")
	public static class ActionSiteTypeList extends Request
	{
		public final static String Action = "SiteTypeList";
		
		public SiteTypeList SiteTypeList = null;
	}
	
	@SuppressWarnings("serial")
	public static class ActionRoutePointList extends Request
	{
		
	}
	
	@SuppressWarnings("serial")
	public static class ActionModeTypeList extends Request
	{
		
	}
	
	
	
	@SuppressWarnings("serial")
	public static class DepartureBoardList extends Request
	{
		public final static String Action = "DepartureBoard";

		// attributs
		public int MaxHourNumber = 0;
		public int MaxMinuteNumber = 0;
		
		// elements
		public StopList StopList = null;
		public StopPointList StopPointList = null;
		public LineList LineList = null;
		public RouteList RouteList = null;
		public DestinationList DestinationList = null;
		public CommentList CommentList = null;
		public ODTList ODTList = null;
		public ValidityPatternSet ValidityPatternSet = null;
		public VehicleList VehicleList = null;
		
		public static DepartureBoardList get(String Server, String LineExternalCode, String StopAreaExternalCode, String Sens, String Date, String DateChangeTime)
		{
			Params params = new Params(Server);
			params.put(ID.Action, Action);
			params.put(ID.LineExternalCode, LineExternalCode);
			params.put(ID.Sens, Sens);
			params.put(ID.StopAreaExternalCode, StopAreaExternalCode);
			params.put(ID.Date, Date);
			params.put(ID.DateChangeTime, DateChangeTime);
			return DepartureBoardList.class.cast(get(params));
		}

		public static DepartureBoardList get(String Server, String LineExternalCode, String StopAreaExternalCode, int Sens, Calendar Calendar)
		{
			DateFormat df = new SimpleDateFormat("yyyy'%7C'MM'%7C'dd");
			String encodedDate = df.format(Calendar.getTime());
			return get(Server, LineExternalCode, StopAreaExternalCode, Integer.toString(Sens), encodedDate, "04%7C00");
		}
	}
	

	
	@SuppressWarnings("serial")
	public static class ServerList implements Serializable
	{
		// elements
		public ArrayList<Server> Server = new ArrayList<Server>();
		
		public Server getServerByExternalCode(String _ServerExternalCode)
		{
			for(Server server:Server)
			{
				if(server.ServerExternalCode.equals(_ServerExternalCode))
				{
					return server;
				}
			}
			return null;
		}

		public static ServerList get()
		{
			return ServerList.class.cast(Tools.getObjectFromStream(NiouBusApplication.getContext().getResources().openRawResource(R.raw.servers)));
		}
		
	}
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Types de base
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("serial")
	public static class CoordX implements Serializable
	{
		public double Value = 0.0;
	}
	
	@SuppressWarnings("serial")
	public static class CoordY implements Serializable
	{
		public double Value = 0.0;
	}
	
	@SuppressWarnings("serial")
	public static class Coord implements Serializable
	{
		// éléments simples
		public CoordX CoordX = null;
		public CoordY CoordY = null;
	}

	@SuppressWarnings("serial")
	public static class Year implements Serializable
	{
		public int Value = 0;
	}
	
	@SuppressWarnings("serial")
	public static class Month implements Serializable
	{
		public int Value = 0;
	}
	
	@SuppressWarnings("serial")
	public static class Day implements Serializable
	{
		public int Value = 0;
	}
	
	@SuppressWarnings("serial")
	public static class Date implements Serializable
	{
		// elements simples
		public Year Year = null;
		public Month Month = null;
		public Day Day = null;
	}

	@SuppressWarnings("serial")
	public static class TotalSeconds implements Serializable
	{
		public int Value = 0;
	}
	
	@SuppressWarnings("serial")
	public static class Hour implements Serializable
	{
		public int Value = 0;
	}
	
	@SuppressWarnings("serial")
	public static class Minute implements Serializable
	{
		public int Value = 0;
	}
	
	@SuppressWarnings("serial")
	public static class Second implements Serializable
	{
		public int Value = 0;
	}
	
	@SuppressWarnings("serial")
	public static class Time implements Serializable
	{
		// elements simples
		public TotalSeconds TotalSeconds = null;
		public Day Day = null;
		public Hour Hour = null;
		public Minute Minute = null;
		public Second Second = null;

	    public int getMinutes()
	    {
	    	if(Day != null)
	    	{
	    		return Day.Value * 60* 24 + Hour.Value * 60 + Minute.Value;
	    	}
	    	return -1;
	    }
	}
	
	@SuppressWarnings("serial")
	public static class Duration extends Time
	{
		// elements simples
	}
	
	@SuppressWarnings("serial")
	public static class ArrivalDate extends Date
	{
		// elements simples
	}

	@SuppressWarnings("serial")
	public static class ArrivalTime extends Time
	{
		// elements simples
	}

	@SuppressWarnings("serial")
	public static class DepartureDate extends Date
	{
		// elements simples
	}
	
	@SuppressWarnings("serial")
	public static class DepartureTime extends Time
	{
	}

	@SuppressWarnings("serial")
	public static class TotalLinkTime extends Time
	{
		// elements simples
	}
	
	@SuppressWarnings("serial")
	public static class TotalWaitTime extends Time
	{
		// elements simples
	}
	
	@SuppressWarnings("serial")
	public static class StopTime extends Time
	{
	}

	@SuppressWarnings("serial")
	public static class StopArrivalTime extends Time
	{
	}

	@SuppressWarnings("serial")
	public static class StartDate extends Date
	{
	}

	@SuppressWarnings("serial")
	public static class EndDate extends Date
	{
	}

	
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Elements
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("serial")
	public static class Address implements Serializable
	{
		// attributs
		public int AddressIdx = 0;
		public int AddressId = 0;
		public String AddressName = "";
		public String AddressExternalCode = "";
		
		// elements
		public City City = null;
		public AddressType AddressType = null;
		public Coord Coord = null;
	}

	@SuppressWarnings("serial")
	public static class AddressType implements Serializable
	{
		// attributs
		public int AddressTypeIdx = 0;
		public int AddressTypeId = 0;
		public String AddressTypeName = "";
		public String AddressTypeExternalCode = "";
	}

	@SuppressWarnings("serial")
	public static class After implements Serializable
	{
		// elements simples
		public int Sens = 0;
		public int Year = 0;
		public int Month = 0;
		public int Day = 0;
		public int Hour = 0;
		public int Minute = 0;
		public int Criteria = 0;
	}

	@SuppressWarnings("serial")
	public static class Arrival implements Serializable
	{
		// attributs
		public String Type = "";
		public int ODTIdx = 0;
		public int ODTPos = 0;
		public int StopIdx = 0;
		
		// elements
		public Date Date = null;
		public Time Time = null;
		public Coord Coord = null;
		public StopPoint StopPoint = null;
	}

	@SuppressWarnings("serial")
	public static class Backward implements Serializable
	{
	
		public String BackwardName = "";
		public Direction Direction = null;
	}

	@SuppressWarnings("serial")
	public static class Before implements Serializable
	{
		// elements simples
		public int Sens = 0;
		public int Year = 0;
		public int Month = 0;
		public int Day = 0;
		public int Hour = 0;
		public int Minute = 0;
		public int Criteria = 0;
	}

	@SuppressWarnings("serial")
	public static class Call implements Serializable
	{
		// elements
		public Before Before = null;
		public This This = null;
		public After After = null;
	}

	@SuppressWarnings("serial")
	public static class City implements Serializable
	{
	
		public int CityIdx = 0;
		public int CityId = 0;
		public String CityName = "";
		public String CityExternalCode = "";
		public String CityCode = "";
	
		public Country Country = null;
	}

	@SuppressWarnings("serial")
	public static class Comment implements Serializable
	{
		// attributs
		public int CommentIdx = 0;
		public int CommentId = 0;
		public String CommentName = "";
		public String CommentExternalCode = "";
		
		// elements
		public StartDate StartDate = null;
		public EndDate EndDate = null;
	}
	
	@SuppressWarnings("serial")
	public static class CommentList implements Serializable
	{	
		public int CommentCount = 0;
		
		public ArrayList<Comment> Comment = new ArrayList<Comment>();
	
	}

	@SuppressWarnings("serial")
	public static class Company implements Serializable
	{
		// attributs
		public int CompanyIdx = 0;
		public int CompanyId = 0;
		public String CompanyName = "";
		public String CompanyExternalCode = "";
	}

	@SuppressWarnings("serial")
	public static class Country implements Serializable
	{
		public int CountryIdx = 0;
		public int CountryId = 0;
		public String CountryName = "";
		public String CountryExternalCode = "";
	}

	@SuppressWarnings("serial")
	public static class Departure implements Serializable
	{
		// attributs
		public String Type = "";
		public int ODTIdx = 0;
		public int ODTPos = 0;
		public int StopIdx = 0;
		
		// elements
		public Date Date = null;
		public Time Time = null;
		public Coord Coord = null;
		public StopPoint StopPoint = null;
	}

	@SuppressWarnings("serial")
	public static class Destination implements Serializable
	{
		// elements
		public StopArea StopArea = null;
	}

	@SuppressWarnings("serial")
	public static class DestinationList implements Serializable
	{
		public int DestinationCount = 0;
		public ArrayList<StopArea> StopArea = new ArrayList<StopArea>();
	}

	@SuppressWarnings("serial")
	public static class Direction implements Serializable
	{
		public StopArea StopArea = null;
	}

	@SuppressWarnings("serial")
	public static class EndNode implements Serializable
	{
		// elements
		public Coord Coord = null;
	}

	@SuppressWarnings("serial")
	public static class EntryPoint implements Serializable
	{
		// attributs
		public String EntryPointType = "";
		public int EntryPointResponseQuality = 0;
		public String CityName = "";
		public String Number = "";
		public String TypeName = "";
		public String EntryPointName = "";
		
		// elements
		public Site Site = null;
		public Coord Coord = null;
		public HangList HangList = null;
		public StopArea StopArea = null;
		public City City = null;
	}

	@SuppressWarnings("serial")
	public static class EntryPointList implements Serializable
	{
		// attributs
		public int EntryPointCount = 0;
		public int EntryPointInQualityCount = 0;
		
		// elements
		public ArrayList<EntryPoint> EntryPoint  = new ArrayList<EntryPoint>();
	}

	@SuppressWarnings("serial")
	public static class Equipment implements Serializable
	{
	
		public boolean BikeDepot = false;
		public boolean BikeAccepted = false;
		public boolean Escalator = false;
		public boolean Elevator = false;
		public boolean MIPAccess = false;
		public boolean Sheltered = false;
		public boolean VisualAnnouncement = false;
		public boolean AudibleAnnouncement = false;
		public boolean AppropriateEscort = false;
		public boolean AppropriateSignage = false;
	}

	@SuppressWarnings("serial")
	public static class Event implements Serializable
	{
		// attributs
		public int EventID = 0;
		public int ProviderID = 0;
		
		// éléments
		public EventLevelTitle EventLevelTitle = null;
		public EventTitle EventTitle = null;
		public EventExternalCode EventExternalCode = null;
		public EventPublicationStartDate EventPublicationStartDate = null;
		public EventPublicationEndDate EventPublicationEndDate = null;
		public ImpactList ImpactList = null;
	}

	@SuppressWarnings("serial")
	public static class EventLevelTitle implements Serializable
	{
		public String Value = "";
	}

	@SuppressWarnings("serial")
	public static class EventTitle implements Serializable
	{
		public String Value = "";
	}

	@SuppressWarnings("serial")
	public static class EventExternalCode implements Serializable
	{
		public String Value = "";
	}

	@SuppressWarnings("serial")
	public static class EventPublicationStartDate implements Serializable
	{
		public String Value = "";
	}

	@SuppressWarnings("serial")
	public static class EventPublicationEndDate implements Serializable
	{
		public String Value = "";
	}

	@SuppressWarnings("serial")
	public static class EventList implements Serializable
	{
		// attributs
		public int EventCount = 0;
	}

	@SuppressWarnings("serial")
	public static class EventPosList implements Serializable
	{
		// attributs
		public int EventPosCount = 0;
	}

	@SuppressWarnings("serial")
	public static class FareSection implements Serializable
	{
	}

	@SuppressWarnings("serial")
	public static class FareSectionList implements Serializable
	{
		// attributs
		public int FareSectionCount = 0;
		
		// elements
		public ArrayList<FareSection> FareSection = new ArrayList<FareSection>();
	}

	@SuppressWarnings("serial")
	public static class FareZone implements Serializable
	{
	}

	@SuppressWarnings("serial")
	public static class FareZoneList implements Serializable
	{
		// attributs
		public int FareZoneCount = 0;
		
		// elements
		public ArrayList<FareZone> FareZone = new ArrayList<FareZone>();
	}
	

	@SuppressWarnings("serial")
	public static class Forward implements Serializable
	{	
		public String ForwardName = "";
		public Direction Direction = null;
	}

	@SuppressWarnings("serial")
	public static class Hang implements Serializable
	{
		// attributs
		public int StopPointIdx = 0;
		public int Duration = 0;
		public int ConnectionKind = 0;
	}

	@SuppressWarnings("serial")
	public static class HangList implements Serializable
	{
		// attributs
		public String Odd = "";
		public int StartNb = 0;
		public int EndNb = 0;
		
		// elements
		public ArrayList<Hang> Hang = new ArrayList<Hang>();
	}
	
	@SuppressWarnings("serial")
	public static class Impact implements Serializable
	{
		// attributs
		public int ImpactID = 0;
		
		public StopPoint StopPoint = null;
		public ImpactState ImpactState = null;
		public ImpactStartDate ImpactStartDate = null;
		public ImpactEndDate ImpactEndDate = null;
		public ImpactCloseDate ImpactCloseDate = null;
		public ImpactDuration ImpactDuration = null;
		public ImpactMessage ImpactMessage = null;
		public Event Event = null;
	}
	
	@SuppressWarnings("serial")
	public static class ImpactList implements Serializable
	{
		// attributs
		public int ImpactCount = 0;

		// elements
		public ArrayList<Impact> Impact = new ArrayList<Impact>();
	}

	
	@SuppressWarnings("serial")
	public static class ImpactPosList implements Serializable
	{
		// attributs
		public int ImpactPosCount = 0;
	}

	@SuppressWarnings("serial")
	public static class ImpactState implements Serializable
	{
		public String Value = "";
	}

	@SuppressWarnings("serial")
	public static class ImpactStartDate implements Serializable
	{
		public String Value = "";
	}

	@SuppressWarnings("serial")
	public static class ImpactEndDate implements Serializable
	{
		public String Value = "";
	}

	@SuppressWarnings("serial")
	public static class ImpactCloseDate implements Serializable
	{
		public String Value = "";
	}

	@SuppressWarnings("serial")
	public static class ImpactDuration implements Serializable
	{
		public int Value = 0;
	}

	@SuppressWarnings("serial")
	public static class ImpactMessage implements Serializable
	{
		public String Value = "";
	}

	
	@SuppressWarnings("serial")
	public static class IsTransday implements Serializable
	{
		// attributs
		public int Value = 0;
	}
	
	@SuppressWarnings("serial")
	public static class JourneyResult implements Serializable
	{
		// attributs
		public int SectionCount = 0;
		public String Criteria = "";
		public int IsCriteriaFound = 0;
		public int IsBest = 0;
		public int JourneyResultPosition = 0;
		public int IsFirstSoluce = 0;
		public int IsLastSoluce = 0;
		public int IsDisrupt = 0;
	
		// elements
		public Summary Summary = null;
		public ArrayList<Section> Section = new ArrayList<Section>();
	}

	@SuppressWarnings("serial")
	public static class JourneyResultList implements Serializable
	{
	
		public int JourneyResultCount = 0;
		public ArrayList<JourneyResult> JourneyResult = new ArrayList<JourneyResult>();
	}

	@SuppressWarnings("serial")
	public static class Line implements Serializable
	{
		// attributs
		public int LineIdx = 0;
		public int LineId = 0;
		public String LineName = "";
		public String LineCode = "";
		public String LineExternalCode = "";
		public String LineAdditionalData = "";
		public String SortOrder = "";
	
		// elements
		public Mode Mode = null;
		public Network Network = null;
		public Forward Forward = null;
		public Backward Backward = null;
		public EventPosList EventPosList = null;
	}

	@SuppressWarnings("serial")
	public static class LineList implements Serializable
	{
		// attributs
		public int LineCount = 0;
		
		// elements
		public ArrayList<Line> Line = new ArrayList<Line>();
	}

	@SuppressWarnings("serial")
	public static class LineStopAreaList implements Serializable
	{
		// attributs
		public int StopAreaCount = 0;
		
		// elements
		public ArrayList<StopArea> StopArea = new ArrayList<StopArea>();
	}

	@SuppressWarnings("serial")
	public static class Mode implements Serializable
	{
		public int ModeIdx = 0;
		public int ModeId = 0;
		public String ModeName = "";
		public String ModeExternalCode = "";
		
		public String Value = "";
	}

	@SuppressWarnings("serial")
	public static class ModeType implements Serializable
	{
		public String Value = "";
	}

	@SuppressWarnings("serial")
	public static class ModeList implements Serializable
	{	
		public int ModeCount = 0;
		public ArrayList<Mode> Mode = new ArrayList<Mode>();
	}

	@SuppressWarnings("serial")
	public static class Network implements Serializable
	{
		public int NetworkIdx = 0;
		public int NetworkId = 0;
		public String NetworkName = "";
		public String NetworkExternalCode = "";
		public String NetworkAdditionalData = "";
		public EventPosList EventPosList = null;
	}

	@SuppressWarnings("serial")
	public static class NetworkList implements Serializable
	{
		// attributs
		public int NetworkCount = 0;
		
		// listes d'elements
		public ArrayList<Network> Network = new ArrayList<Network>();
		
		public Network getNetworkByExternalCode(String _NetworkExternalCode)
		{
			for(Network network:Network)
			{
				if(network.NetworkExternalCode.equals(_NetworkExternalCode))
				{
					return network;
				}
			}
			return null;
		}
	}

	@SuppressWarnings("serial")
	public static class NextDepartureList implements Serializable
	{
		// attributs
		public int StopCount = 0;
	}
	
	@SuppressWarnings("serial")
	public static class Nota implements Serializable
	{
		// attributs
		public String NotaType = "";
	}

	@SuppressWarnings("serial")
	public static class ODT implements Serializable
	{
		// attributs
		public int ODTIdx = 0;
		public int ODTId = 0;
		public String ODTName = "";
		public String ODTExternalCode = "";
		public int Position = 0;
		
		// elements
		public Network Netowrk = null;
	}

	@SuppressWarnings("serial")
	public static class ODTList implements Serializable
	{
		// attributs
		public int ODTCount = 0;
		
		// elements
		public ArrayList<ODT> ODT = new ArrayList<ODT>();
	}

	@SuppressWarnings("serial")
	public static class PagerInfo implements Serializable
	{
		// attributs
		public int ResponseCount = 0;
		public int ResponseStartIndex = 0;
		public int TotalCount = 0;
	}

	@SuppressWarnings("serial")
	public static class Proximity implements Serializable
	{
		// attributs
		public int Distance = 0;
		
		public StopArea StopArea = null;
		public StopPoint StopPoint = null;
	}
	
	@SuppressWarnings("serial")
	public static class ProximityList implements Serializable
	{
		// attributs
		public int ProximityCount = 0;
		
		public ArrayList<Proximity> Proximity = new ArrayList<Proximity>();
	}
	
	@SuppressWarnings("serial")
	public static class Route implements Serializable
	{
		public int RouteIdx = 0;
		public int RouteId = 0;
		public String RouteName = "";
		public String RouteExternalCode = "";
		public int IsForward = 0;
		public int RouteLineIdx = 0;
		public int IsFrequence = 0;
		
		public Line Line = null;
	}

	@SuppressWarnings("serial")
	public static class RouteList implements Serializable
	{	
		public int RouteCount = 0;
		public ArrayList<Route> Route = new ArrayList<Route>();
	}

	@SuppressWarnings("serial")
	public static class Section implements Serializable
	{
		// attributs
		public String Type = "";
		
		// elements
		public Duration Duration = null;
		public Departure Departure = null;
		public Arrival Arrival = null;
		public VehicleJourney VehicleJourney = null;
		public Nota Nota = null;
		public FareSectionList FareSectionList = null;
		public FareZoneList FareZoneList = null;
	}
	
	@SuppressWarnings("serial")
	public static class Segment implements Serializable
	{
		// attributs
		public int Length = 0;
		public int FromNbPar = 0;
		public int ToNbPar = 0;
		public int FromNbOdd = 0;
		public int ToNbOdd = 0;
		public int Duration = 0;
		
		// elements
		public Address Address = null;
		public StartNode StartNode = null;
		public EndNode EndNode = null;
	}

	@SuppressWarnings("serial")
	public static class SegmentList implements Serializable
	{
		public int SegmentCount = 0;
		public ArrayList<Segment> Segment = new ArrayList<Segment>();
	}
	
	@SuppressWarnings("serial")
	public static class Site implements Serializable
	{
		// attributs
		public int SiteIdx = 0;
		public int SiteId = 0;
		public String SiteName = "";
		public String SiteExternalCode = "";
		
		// elements
		public City City = null;
		public SiteType  SiteType = null;
		public Coord Coord = null;
		public SiteAddress SiteAddress = null;
		public Comment Comment = null;
	}

	@SuppressWarnings("serial")
	public static class SiteType implements Serializable
	{
		// attributs
		public int SiteTypeIdx = 0;
		public int SiteTypeId = 0;
		public String SiteTypeName = "";
		public String SiteTypeExternalCode = "";
		
		// elements
	}
	
	@SuppressWarnings("serial")
	public static class SiteTypeList implements Serializable
	{
		// attributs
		public int SiteTypeCount = 0;
		
		// elements
		public ArrayList<SiteType> SiteType = new ArrayList<SiteType>();
	}

	@SuppressWarnings("serial")
	public static class SiteAddress implements Serializable
	{
		// attributs
		public String SiteAddressName = "";
		public String SiteAddressNumber = "";
		public String SiteAddressTypeName = "";
		
		// elements
	}

	@SuppressWarnings("serial")
	public static class StartNode implements Serializable
	{
		// elements
		public Coord Coord = null;
	}

	@SuppressWarnings("serial")
	public static class Stop implements Serializable
	{
		public int StopIdx = 0;
		public int StopPointIdx = 0;
		public int VehicleJourneyIdx = 0;
		public String VehicleJourneyExternalCode = "";
		public int HourNumber = 0;
		public int MinuteNumber = 0;
		public int DestinationPos = 0;
		public int ODTIdx = 0;
		public int ODTPos = 0;
		public int ValidityPatternSetCommentPos = 0;
		public int StopOrder = 0;
		public int VehicleIdx = 0;
		
		public StopTime StopTime = null;
		public StopArrivalTime StopArrivalTime = null;
		public IsTransday IsTransday = null;
		public StopPoint StopPoint = null;
		public DepartureTime DepartureTime = null;
		public Route Route = null;
		public StopArea StopArea = null;

	    public Time getStopTime()
	    {
			if(StopTime != null)
			{
				return StopTime;
			}
			if(DepartureTime != null)
			{
				return DepartureTime;
			}
			return null;
	    }
	}

	@SuppressWarnings("serial")
	public static class StopArea implements Serializable
	{
		// attributs
		public int StopAreaIdx = 0;
		public int StopAreaId = 0;
		public String StopAreaName = "";
		public String StopAreaExternalCode = "";
		public int MainStopArea = 0;
		public int MultiModal = 0;
		public int CarPark = 0;
		public int MainConnection = 0;
		public String AdditionalData = "";
	
		// elements
		public City City = null;
		public Coord Coord = null;
		public HangList HangList = null;
		public ModeList ModeList = null;
	}

	@SuppressWarnings("serial")
	public static class StopAreaList implements Serializable
	{
		// attributs
		public int StopAreaCount = 0;
		
		// elements
		public ArrayList<StopArea> StopArea = new ArrayList<StopArea>();
	}

	@SuppressWarnings("serial")
	public static class StopList implements Serializable
	{
		public int StopCount = 0;
		public String Nota = "";
		
		public StopTime StopTime = null;
		public Route Route = null;
		public ArrayList<Stop> Stop = new ArrayList<Stop>();
	}

	@SuppressWarnings("serial")
	public static class StopPoint implements Serializable
	{
		// attributs
		public int StopPointIdx = 0;
		public String StopPointExternalCode = "";
		public String StopPointName = "";
		public int StopPointId = 0;
		public int FareZone = 0;
	
		// éléments
		public StopPointAddress StopPointAddress = null;
		public Equipment Equipment = null;
		public Mode Mode = null;
		public City City = null;
		public StopArea StopArea = null;
		public Coord Coord = null;
		public ImpactPosList  ImpactPosList = null;
	}

	@SuppressWarnings("serial")
	public static class StopPointAddress implements Serializable
	{
		// attributs
		public String StopPointAddressTypeName = "";
		public String StopPointAddressNumber = "";
		public String StopPointAddressName = "";
	}

	@SuppressWarnings("serial")
	public static class StopPointEquipment implements Serializable
	{
		public String Value = "";
	}

	@SuppressWarnings("serial")
	public static class StopPointList implements Serializable
	{
	
		public int StopPointCount = 0;

		public ArrayList<StopPoint> StopPoint = new ArrayList<StopPoint>();
	}

	@SuppressWarnings("serial")
	public static class Summary implements Serializable
	{
		// attributs
		public int Interchange = 0;
		
		// elements
		public DepartureDate DepartureDate = null;
		public DepartureTime DepartureTime = null;
		public ArrivalDate ArrivalDate = null;
		public ArrivalTime ArrivalTime = null;
		public Duration Duration = null;
		public TotalLinkTime TotalLinkTime = null;
		public TotalWaitTime TotalWaitTime = null;
		public Call Call = null;
	}
	
	@SuppressWarnings("serial")
	public static class This implements Serializable
	{
		// elements simples
		public int Sens = 0;
		public int Year = 0;
		public int Month = 0;
		public int Day = 0;
		public int Hour = 0;
		public int Minute = 0;
		public int Criteria = 0;
	}

	@SuppressWarnings("serial")
	public static class ValidityPattern implements Serializable
	{
		// attributs
		public String ValidityPatternBeginningDay = "";
		public int IsAdapted = 0;
		public int ValidityPatternIdx = 0;
		
		public String Value = "";
	}
	
	@SuppressWarnings("serial")
	public static class ValidityPatternSet implements Serializable
	{
	
		public String ValidityPatternAdditionalData = "";
		public String ValidityPatternExternalCode = "";
		public String ValidityPatternSetName = "";
		public int ValidityPatternSetId = 0;
		public int ValidityPatternSetIdx = 0;
		
		public ValidityPatternSetDetailList ValidityPatternSetDetailList = null;
	}

	@SuppressWarnings("serial")
	public static class ValidityPatternSetDetailList implements Serializable
	{
		public int VPatternSetDetailCount = 0;
	}

	@SuppressWarnings("serial")
	public static class Vehicle implements Serializable
	{
	
		public int VehicleIdx = 0;
		public int VehicleId = 0;
		public boolean BikeAccepted = false;
		public boolean MIPAccess = false;
		public boolean AirConditioned = false;
		public String VehicleExternalCode = "";
		public String VehicleName = "";
		
		public String Value = "";
	}

	@SuppressWarnings("serial")
	public static class VehicleJourney implements Serializable
	{
		// attributs
		public int VehicleJourneyIdx = 0;
		public int VehicleJourneyId = 0;
		public String VehicleJourneyName = "";
		public int VehicleJourneyRouteIdx = 0;
		public String VehicleJourneyExternalCode = "";
		 
		// elements
		public Route Route = null;
		public Destination Destination = null;
		public Mode Mode = null;
		public Company Company = null;
		public Vehicle Vehicle = null;
		public ValidityPattern ValidityPattern = null;
		public EventPosList EventPosList = null;
	}
	
	@SuppressWarnings("serial")
	public static class VehicleList implements Serializable
	{
	
		public int VehicleCount = 0;
		public ArrayList<Vehicle> Vehicle = new ArrayList<Vehicle>();
	}

	// extensions
	@SuppressWarnings("serial")
	public static class Server implements Serializable
	{
		// attributs
		public String ServerName = "";
		public String ServerExternalCode = "";
		
		// elements
		public NetworkList NetworkList = null;
	}


	
	
	
	

	@SuppressWarnings("serial")
    public static class Favorite implements Serializable
    {
		public String Activity = "";
		public String Title = "";
		public String SubTitle = "";
		public HashMap<String, String> Parameters = new HashMap<String, String>();
		
		public Favorite(String activity, String title, String subtitle, HashMap<String, String> hashMap)
		{
			Activity = activity;
			Title = title;
			SubTitle = subtitle;
			Parameters = hashMap;
		}
		public Favorite()
		{
			
		}
		
		public boolean isEquivalent(Favorite favorite)
		{
			if(!this.Activity.equals(favorite.Activity)) return false;
			for(Entry<String, String> entry : this.Parameters.entrySet())
			{
				if(!favorite.Parameters.containsKey(entry.getKey())) return false;
				if(!entry.getValue().equals(favorite.Parameters.get(entry.getKey()))) return false;
			}
			return true;
		}
    }
	
	@SuppressWarnings("serial")
	public static class FavoriteList implements Serializable
	{
		public Favorite[] Favorites = new Favorite[0];
	}



	
	
	
	public static void convertFavoritesToBookmarks()
	{
		Bookmarks bl = new Bookmarks();
		if(!bl.exists())
		{
			DOM.FavoriteList fl = getFavorites();
			if(fl != null)
			{
				for(int i = 0; i<fl.Favorites.length; i++)
				{
					Favorite favorite = fl.Favorites[i];
					Bookmark bookmark = new Bookmark();
					bookmark.Activity = DepartureListActivity.class.getSimpleName();
					bookmark.Title = favorite.Title;
					bookmark.SubTitle = favorite.SubTitle;
					bookmark.Bundle.putString(ID.Server, favorite.Parameters.get(ID.Server));
					bookmark.Bundle.putString(ID.LineExternalCode, favorite.Parameters.get(ID.LineExternalCode));
					bookmark.Bundle.putString(ID.Direction, favorite.Parameters.get(ID.Direction));
					bookmark.Bundle.putString(ID.StopAreaExternalCode, favorite.Parameters.get(ID.StopAreaExternalCode));
					bl.add(bookmark);
				}
			}
			bl.save();
		}
	}
	
	
	
	
	
	
	private static DOM.FavoriteList getFavorites()
	{
    	return DOM.FavoriteList.class.cast(Tools.loadObjectFromFile("favorites.serial"));
	}
	/*
	private static void saveFavorites(DOM.FavoriteList favorites)
	{
    	Tools.saveObjectToFile("favorites.serial", favorites);
	}
	
	private static void addFavorite(DOM.Favorite favorite)
	{
		DOM.FavoriteList favoritelist = getFavorites();
		if(favoritelist == null)
		{
			favoritelist = new DOM.FavoriteList();
		}
		DOM.Favorite[] favorites = favoritelist.Favorites;
		DOM.Favorite[] savefavorites = new DOM.Favorite[favorites.length+1];
		for(int i = 0; i<favorites.length; i++)
		{
			savefavorites[i] = favorites[i];
		}
		savefavorites[favorites.length] = favorite;
		favoritelist.Favorites = savefavorites;
		saveFavorites(favoritelist);
	}

	private static void removeFavorite(DOM.Favorite favorite)
	{
		DOM.FavoriteList favoritelist = getFavorites();
		if(favoritelist == null)
		{
			favoritelist = new DOM.FavoriteList();
		}
		DOM.Favorite[] favorites = favoritelist.Favorites;
		ArrayList<DOM.Favorite> savefavorites = new ArrayList<DOM.Favorite>();
		for(int i = 0; i<favorites.length; i++)
		{
			if(!favorite.isEquivalent(favorites[i]))
			{
				savefavorites.add(favorites[i]);
			}
		}
		favoritelist.Favorites  = new DOM.Favorite[savefavorites.size()];
		for(int i = 0; i < savefavorites.size(); i++)
		{
			favoritelist.Favorites[i] = savefavorites.get(i);
		}
		saveFavorites(favoritelist);
	}
	*/
	
	
	
	
	
	
	

	@SuppressWarnings("serial")
	public static class ActionAddressList extends Request
	{
		
	}
	@SuppressWarnings("serial")
	public static class ActionAddressTypeList extends Request
	{
		
	}
	@SuppressWarnings("serial")
	public static class ActionBookingTypeList extends Request
	{
		
	}
	@SuppressWarnings("serial")
	public static class ActionCityList extends Request
	{
		
	}
	@SuppressWarnings("serial")
	public static class ActionCommonLetterList extends Request
	{
		
	}
	@SuppressWarnings("serial")
	public static class ActionCompanyList extends Request
	{
		
	}
	@SuppressWarnings("serial")
	public static class ActionDepartmentList extends Request
	{
		
	}
	@SuppressWarnings("serial")
	public static class ActionDirectStopAreaList extends Request
	{
		
	}
	@SuppressWarnings("serial")
	public static class ActionDistrictList extends Request
	{
		
	}
	@SuppressWarnings("serial")
	public static class ActionFirstLetterList extends Request
	{
		
	}
	@SuppressWarnings("serial")
	public static class ActionFreqSettingList extends Request
	{
		
	}
	@SuppressWarnings("serial")
	public static class ActionGetEvent extends Request
	{
		
	}
	@SuppressWarnings("serial")
	public static class ActionGetEventList extends Request
	{
		
	}
	@SuppressWarnings("serial")
	public static class ActionGetImpact extends Request
	{
		
	}
	@SuppressWarnings("serial")
	public static class ActionGetMediaList extends Request
	{
		
	}
	@SuppressWarnings("serial")
	public static class ActionGetMessage extends Request
	{
		
	}
	@SuppressWarnings("serial")
	public static class ActionIsochronList extends Request
	{
		
	}
	@SuppressWarnings("serial")
	public static class ActionLineRouteDescription extends Request
	{
		
	}
	@SuppressWarnings("serial")
	public static class ActionNextArrivalList extends Request
	{
		
	}
	@SuppressWarnings("serial")
	public static class ActionOnBoardServiceList extends Request
	{
		
	}
	@SuppressWarnings("serial")
	public static class ActionPhoneticList extends Request
	{
		
	}
	@SuppressWarnings("serial")
	public static class ActionRouteList extends Request
	{
		
	}
	@SuppressWarnings("serial")
	public static class ActionSiteList extends Request
	{
		
	}
	@SuppressWarnings("serial")
	public static class ActionStopPointList extends Request
	{
		
	}
	@SuppressWarnings("serial")
	public static class ActionVehicleJourneyDetailList extends Request
	{
		
	}
	@SuppressWarnings("serial")
	public static class ActionVehicleJourneyList extends Request
	{
		
	}
	@SuppressWarnings("serial")
	public static class AddressList implements Serializable{}
	@SuppressWarnings("serial")
	public static class AddressTypeList implements Serializable{}
	@SuppressWarnings("serial")
	public static class BookingType implements Serializable{}
	@SuppressWarnings("serial")
	public static class BookingTypeList implements Serializable{}
	@SuppressWarnings("serial")
	public static class CityList implements Serializable{}
	@SuppressWarnings("serial")
	public static class CommonLetterList implements Serializable{}
	@SuppressWarnings("serial")
	public static class CompanyList implements Serializable{}
	@SuppressWarnings("serial")
	public static class DLL implements Serializable{}
	@SuppressWarnings("serial")
	public static class Database implements Serializable{}
	@SuppressWarnings("serial")
	public static class Department implements Serializable{}
	@SuppressWarnings("serial")
	public static class DepartmentList implements Serializable{}
	@SuppressWarnings("serial")
	public static class DirectStopAreaList implements Serializable{}
	@SuppressWarnings("serial")
	public static class District implements Serializable{}
	@SuppressWarnings("serial")
	public static class DistrictList implements Serializable{}
	@SuppressWarnings("serial")
	public static class FreqSetting implements Serializable{}
	@SuppressWarnings("serial")
	public static class FreqSettingList implements Serializable{}
	@SuppressWarnings("serial")
	public static class FreqStop implements Serializable{}
	@SuppressWarnings("serial")
	public static class FreqStopList implements Serializable{}
	@SuppressWarnings("serial")
	public static class IsochronList implements Serializable{}
	@SuppressWarnings("serial")
	public static class LineScheduleList implements Serializable{}
	@SuppressWarnings("serial")
	public static class Media implements Serializable{}
	@SuppressWarnings("serial")
	public static class MediaList implements Serializable{}
	@SuppressWarnings("serial")
	public static class Message implements Serializable{}
	@SuppressWarnings("serial")
	public static class ModeTypeList implements Serializable{}
	@SuppressWarnings("serial")
	public static class MsgCategory implements Serializable{}
	@SuppressWarnings("serial")
	public static class NextArrivalList implements Serializable{}
	@SuppressWarnings("serial")
	public static class OnBoardService implements Serializable{}
	@SuppressWarnings("serial")
	public static class OnBoardServiceList implements Serializable{}
	@SuppressWarnings("serial")
	public static class Origin implements Serializable{}
	@SuppressWarnings("serial")
	public static class PhoneticList implements Serializable{}
	@SuppressWarnings("serial")
	public static class RoutePoint implements Serializable{}
	@SuppressWarnings("serial")
	public static class RoutePointList implements Serializable{}
	@SuppressWarnings("serial")
	public static class SiteList implements Serializable{}
	@SuppressWarnings("serial")
	public static class Thread implements Serializable{}
	@SuppressWarnings("serial")
	public static class VPTranslation implements Serializable{}
	@SuppressWarnings("serial")
	public static class VehicleJourneyList implements Serializable{}

}
