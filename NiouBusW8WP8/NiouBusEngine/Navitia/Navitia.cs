using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Globalization;
using System.Threading.Tasks;
using System.Xml.Serialization;

namespace NiouBusEngine.Navitia
{
    public class Parameters
    {
        public const string Server = "Server";
        public const string Action = "Action";
        public const string NetworkExternalCode = "NetworkExternalCode";
        public const string StopAreaExternalCode = "StopAreaExternalCode";
        public const string LineExternalCode = "LineExternalCode";
        public const string Sens = "Sens";
        public const string Direction = "Direction";
        public const string Date = "Date";
        public const string DateChangeTime = "DateChangeTime";
        public const string Type = "Type";
        public const string X = "X";
        public const string Y = "Y";
    }

    public class Action
    {
        public const string DepartureBoard = "DepartureBoard";
        public const string LineList = "LineList";
        public const string ProximityList = "ProximityList";
        public const string StopAreaList = "StopAreaList";
    }

    public class ProximityListType
    {
        public const string StopArea = "StopArea";
    }

    public class NavitiaTools
    {
        public static Dictionary<string, object> GetParameters(
            string Server,
            string NetworkExternalCode,
            string LineExternalCode = null,
            string Direction = null,
            string StopAreaExternalCode = null
        )
        {
            Dictionary<string, object> parameters = new Dictionary<string, object>()
            {
                { Navitia.Parameters.Server, Server },
                { Navitia.Parameters.NetworkExternalCode, NetworkExternalCode },
                { Navitia.Parameters.LineExternalCode, LineExternalCode },
                { Navitia.Parameters.Direction, Direction },
                { Navitia.Parameters.StopAreaExternalCode, StopAreaExternalCode },
            };
            return parameters;
        }


        public static async Task<T> GetAsync<T>(
            string Server,
            string Action,
            string NetworkExternalCode = null,
            string StopAreaExternalCode = null,
            string LineExternalCode = null,
            string Sens = null,
            DateTime? Date = null,
            TimeSpan? DateChangeTime = null,
            string Type = null,
            NavitiaDouble? X = null,
            NavitiaDouble? Y = null
            ) where T : class
        {
            Dictionary<string, object> parameters = new Dictionary<string, object>() 
                {
                    {Parameters.Action, Action},
                    {Parameters.NetworkExternalCode, NetworkExternalCode},
                    {Parameters.StopAreaExternalCode, StopAreaExternalCode},
                    {Parameters.LineExternalCode, LineExternalCode},
                    {Parameters.Sens, Sens},
                    {Parameters.Date, Date.HasValue ? string.Format("{0:D2}|{1:D2}|{2:D2}", Date.Value.Year, Date.Value.Month, Date.Value.Day) : null},
                    {Parameters.DateChangeTime, DateChangeTime},
                    {Parameters.Type, Type},
                    {Parameters.X, X},
                    {Parameters.Y, Y},
                };
            string url = string.Format(Constants.NavitiaUrlPattern, Server, parameters.EscapeAndConcat());
            return await Tools.GetXmlDataAsync<T>(url, true);
        }
    }




    //public struct NavitiaBoolean : IXmlSerializable
    //{

    //    // we're just wrapping a bool
    //    private bool Value;

    //    // allow implicit casts to/from bool
    //    public static implicit operator bool(NavitiaBoolean b)
    //    {
    //        System.Boolean
    //        return b.Value;
    //    }
    //    public static implicit operator NavitiaBoolean(bool b)
    //    {
    //        return new NavitiaBoolean() { Value = b };
    //    }

    //    // implement IXmlSerializable
    //    public System.Xml.Schema.XmlSchema GetSchema() { return null; }
    //    public void ReadXml(System.Xml.XmlReader reader)
    //    {
    //        Value = (reader.ReadElementContentAsString() == "True");
    //    }
    //    public void WriteXml(System.Xml.XmlWriter writer)
    //    {
    //        writer.WriteString((Value) ? "True" : "False");
    //    }
    //}

    public struct NavitiaDouble : IXmlSerializable
    {
        private static IFormatProvider FormatProvider = new CultureInfo("fr-FR");
        // we're just wrapping a bool
        private double Value;

        // allow implicit casts to/from bool
        public static implicit operator double(NavitiaDouble b)
        {
            return b.Value;
        }
        public static implicit operator NavitiaDouble(double b)
        {
            return new NavitiaDouble() { Value = b };
        }

        // implement IXmlSerializable
        public System.Xml.Schema.XmlSchema GetSchema() { return null; }
        public void ReadXml(System.Xml.XmlReader reader)
        {
            Value = double.Parse(reader.ReadElementContentAsString(), FormatProvider);
        }
        public void WriteXml(System.Xml.XmlWriter writer)
        {
            writer.WriteString(Value.ToString(FormatProvider));
        }

        public override string ToString()
        {
            return Value.ToString(FormatProvider);
        }
    }

    public class ActionJourneyResultList : BaseViewModel
    {
        public static String Action = "PlanJourney";
        // elements
        //[XmlArray("JourneyResultList")]
        //[XmlArrayItem("JourneyResult", typeof(JourneyResult))]
        [XmlElement]
        public JourneyResultList JourneyResultList { get; set; }

        //[XmlArray("CommentList")]
        //[XmlArrayItem("Comment", typeof(Comment))]
        [XmlElement]
        public CommentList CommentList { get; set; }

        //[XmlArray("ODTList")]
        //[XmlArrayItem("ODT", typeof(ODT))]
        [XmlElement]
        public ODTList ODTList { get; set; }

        //[XmlArray("EventList")]
        //[XmlArrayItem("Event", typeof(Event))]
        [XmlElement]
        public EventList EventList { get; set; }
    }

    public class ActionLineStopAreaList : BaseViewModel
    {
        public static String Action = "LineStopAreaList";
        // elements
        //[XmlArray("LineStopAreaList")]
        //[XmlArrayItem("StopArea", typeof(StopArea))]
        [XmlElement]
        public StopAreaList LineStopAreaList { get; set; }
    }

    [XmlRoot]
    public class ActionLineList : BaseViewModel
    {
        public static String Action = "LineList";
        
        // elements
        //[XmlArray("LineList")]
        //[XmlArrayItem("Line", typeof(Line))]
        [XmlElement]
        public LineList LineList { get; set; }
    }

    public class ActionNetworkList : BaseViewModel
    {
        public static String Action = "NetworkList";
        // elements
        //[XmlArray("NetworkList")]
        //[XmlArrayItem("Network", typeof(Network))]
        [XmlElement]
        public NetworkList NetworkList { get; set; }

    }

    public class ActionModeList : BaseViewModel
    {
        public static String Action = "ModeList";
        // elements
        //[XmlArray("ModeList")]
        //[XmlArrayItem("Mode", typeof(Mode))]
        [XmlElement]
        public ModeList ModeList { get; set; }
    }

    public class ActionStopAreaList : BaseViewModel
    {
        public static String Action = "StopAreaList";
        // elements
        //public StopAreaList StopAreaList { get; set; }
        //[XmlArray("StopAreaList")]
        //[XmlArrayItem("StopArea", typeof(StopArea))]
        [XmlElement]
        public StopAreaList StopAreaList { get; set; }
    }

    public class ActionEntryPointList : BaseViewModel
    {
        public static String Action = "EntryPoint";
        // elements
        //[XmlArray("EntryPointList")]
        //[XmlArrayItem("EntryPoint", typeof(EntryPoint))]
        [XmlElement]
        public EntryPointList EntryPointList { get; set; }
        [XmlElement]
        public PagerInfo PagerInfo { get; set; }

    }

    public class BinaryCriteria : BaseViewModel
    {
        public static String Action = "MakeBinaryCriteria";
        // elements
        [XmlElement]
        public Vehicle Vehicle { get; set; }
        [XmlElement]
        public StopPointEquipment StopPointEquipment { get; set; }
        [XmlElement]
        public Mode Mode { get; set; }
        [XmlElement]
        public ModeType ModeType { get; set; }
    }


    public class EndOfCourseList : BaseViewModel
    {
        public static String Action = "EndOfCourse";

        //[XmlArray("StopList")]
        //[XmlArrayItem("Stop", typeof(Stop))]
        [XmlElement]
        public StopList StopList { get; set; }
        [XmlElement]
        public VehicleJourney VehicleJourney { get; set; }
    }


    public class ActionProximityList : BaseViewModel
    {
        public static String Action = "ProximityList";

        //[XmlArray("ProximityList")]
        //[XmlArrayItem("Proximity", typeof(Proximity))]
        [XmlElement]
        public ProximityList ProximityList { get; set; }
    }

    public class ActionStreetNetwork : BaseViewModel
    {
        public static String Action = "StreetNetwork";

        //[XmlArray("SegmentList")]
        //[XmlArrayItem("Segment", typeof(Segment))]
        [XmlElement]
        public SegmentList SegmentList { get; set; }
    }

    public class ActionNextDepartureList : BaseViewModel
    {
        public static String Action = "NextDeparture";

        //[XmlArray("List")]
        //[XmlArrayItem("", typeof())]
        //public NextDeparture[] NextDepartureList { get; set; }

        //[XmlArray("EventList")]
        //[XmlArrayItem("Event", typeof(Event))]
        [XmlElement]
        public EventList EventList { get; set; }
    }

    public class ActionImpactList : BaseViewModel
    {
        public static String Action = "GetImpactList";

        //[XmlArray("ImpactList")]
        //[XmlArrayItem("Impact", typeof(Impact))]
        [XmlElement]
        public ImpactList ImpactList { get; set; }
    }

    public class ActionSiteTypeList : BaseViewModel
    {
        public static String Action = "SiteTypeList";

        //[XmlArray("SiteTypeList")]
        //[XmlArrayItem("SiteType", typeof(SiteType))]
        [XmlElement]
        public SiteTypeList SiteTypeList { get; set; }
    }

    public class ActionRoutePointList : BaseViewModel
    {

    }

    public class ActionModeTypeList : BaseViewModel
    {

    }

    public class DepartureBoardList : BaseViewModel
    {
        public static String Action = "DepartureBoard";

        // attributs
        [XmlIgnore]
        private int _MaxHourNumber;
        [XmlAttribute]
        public int MaxHourNumber { get { return _MaxHourNumber; } set { Set(ref _MaxHourNumber, value); } }

        [XmlIgnore]
        private int _MaxMinuteNumber;
        [XmlAttribute]
        public int MaxMinuteNumber { get { return _MaxMinuteNumber; } set { Set(ref _MaxMinuteNumber, value); } }

        // elements
        //[XmlArray("StopList")]
        //[XmlArrayItem("Stop", typeof(Stop))]
        [XmlElement]
        public StopList StopList { get; set; }

        //[XmlArray("StopPointList")]
        //[XmlArrayItem("StopPoint", typeof(StopPoint))]
        [XmlElement]
        public StopPointList StopPointList { get; set; }

        //[XmlArray("LineList")]
        //[XmlArrayItem("Line", typeof(Line))]
        [XmlElement]
        public LineList LineList { get; set; }

        //[XmlArray("RouteList")]
        //[XmlArrayItem("Route", typeof(Route))]
        [XmlElement]
        public RouteList RouteList { get; set; }

        //[XmlArray("DestinationList")]
        //[XmlArrayItem("Destination", typeof(Destination))]
        [XmlElement]
        public DestinationList DestinationList { get; set; }

        //[XmlArray("CommentList")]
        //[XmlArrayItem("Comment", typeof(Comment))]
        [XmlElement]
        public CommentList CommentList { get; set; }

        //[XmlArray("ODTList")]
        //[XmlArrayItem("ODT", typeof(ODT))]
        [XmlElement]
        public ODTList ODTList { get; set; }

        [XmlElement]
        public ValidityPatternSet ValidityPatternSet { get; set; }

        //[XmlArray("VehicleList")]
        //[XmlArrayItem("Vehicle", typeof(Vehicle))]
        [XmlElement]
        public VehicleList VehicleList { get; set; }

    }


    [XmlRoot]
    public class ServerList : BaseViewModel
    {

        // elements
        //[XmlArray("Server")]
        [XmlElement]
        //[XmlArray("NetworkList")]
        //[XmlArrayItem("Network", typeof(Network))]
        public ObservableCollection<NetworkList> NetworkList { get; set; }

        //public NetworkList GetServerByExternalCode(String _ServerExternalCode)
        //{
        //    foreach (NetworkList networklist in NetworkList)
        //    {
        //        if (networklist.Network.ServerExternalCode.Equals(_ServerExternalCode))
        //        {
        //            return networklist;
        //        }
        //    }
        //    return null;
        //}
    }



    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Types de base
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //public class CoordX
    //{
    //    public double Value = 0.0;
    //}


    //public class CoordY
    //{
    //    public double Value = 0.0;
    //}


    public class Coord : BaseViewModel
    {
        // éléments simples
        //private double _CoordX;
        //[XmlIgnore]
        //public double CoordX
        //{
        //    get
        //    {
        //        return _CoordX;
        //    }
        //    set
        //    {
        //        _CoordX = value;
        //    }
        //}
        //[XmlElement(ElementName = "CoordX")]
        //public String CoordXString
        //{
        //    get
        //    {
        //        return _CoordX.ToString();//.Replace(".", ",");
        //    }
        //    set
        //    {
        //        _CoordX = double.Parse(value/*.Replace(",", ".")*/);
        //    }
        //}

        [XmlElement]
        public NavitiaDouble CoordX { get; set; }
        [XmlElement]
        public NavitiaDouble CoordY { get; set; }

        //private double _CoordY;
        //[XmlIgnore]
        //public double CoordY
        //{
        //    get
        //    {
        //        return _CoordY;
        //    }
        //    set
        //    {
        //        _CoordY = value;
        //    }
        //}
        //[XmlElement(ElementName = "CoordY")]
        //public String CoordYString
        //{
        //    get
        //    {
        //        return _CoordY.ToString();//.Replace(".", ",");
        //    }
        //    set
        //    {
        //        _CoordY = double.Parse(value/*.Replace(",", ".")*/);
        //    }
        //}
        //public double CoordY { get; set; }
    }


    //public class Year
    //{
    //    public int Value { get; set; }
    //}


    //public class Month
    //{
    //    public int Value { get; set; }
    //}


    //public class Day
    //{
    //    public int Value { get; set; }
    //}


    public class Date : BaseViewModel
    {
        // elements simples
        [XmlElement]
        public int Year { get; set; }
        [XmlElement]
        public int Month { get; set; }
        [XmlElement]
        public int Day { get; set; }

        public DateTime DateTime
        {
            get
            {
                return new DateTime(Year, Month, Day);
            }
        }
    }


    //public class TotalSeconds
    //{
    //    public int Value { get; set; }
    //}


    //public class Hour
    //{
    //    public int Value { get; set; }
    //}


    //public class Minute
    //{
    //    public int Value { get; set; }
    //}


    //public class Second
    //{
    //    public int Value { get; set; }
    //}


    public class Time : BaseViewModel
    {
        // elements simples
        [XmlElement]
        public int TotalSeconds { get; set; }
        [XmlElement]
        public int Day { get; set; }
        [XmlElement]
        public int Hour { get; set; }
        [XmlElement]
        public int Minute { get; set; }
        [XmlElement]
        public int Second { get; set; }

        public TimeSpan TimeSpan
        {
            get
            {
                return new TimeSpan(Day, Hour, Minute, Second);
            }
        }

        //public int getMinutes()
        //{
        //    if (Day != null)
        //    {
        //        return Day.Value * 60 * 24 + Hour.Value * 60 + Minute.Value;
        //    }
        //    return -1;
        //}
    }


    public class Duration : Time
    {
        // elements simples
    }


    public class ArrivalDate : Date
    {
        // elements simples
    }


    public class ArrivalTime : Time
    {
        // elements simples
    }


    public class DepartureDate : Date
    {
        // elements simples
    }


    public class DepartureTime : Time
    {
    }


    public class TotalLinkTime : Time
    {
        // elements simples
    }


    public class TotalWaitTime : Time
    {
        // elements simples
    }


    public class StopTime : Time
    {
    }


    public class StopArrivalTime : Time
    {
    }


    public class StartDate : Date
    {
    }


    public class EndDate : Date
    {
    }





    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Elements
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public class Address : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public int AddressIdx { get; set; }
        [XmlAttribute]
        public int AddressId { get; set; }
        [XmlAttribute]
        public String AddressName { get; set; }
        [XmlAttribute]
        public String AddressExternalCode { get; set; }

        // elements
        [XmlElement]
        public City City { get; set; }
        [XmlElement]
        public AddressType AddressType { get; set; }
        [XmlElement]
        public Coord Coord { get; set; }
    }


    public class AddressType : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public int AddressTypeIdx { get; set; }
        [XmlAttribute]
        public int AddressTypeId { get; set; }
        [XmlAttribute]
        public String AddressTypeName { get; set; }
        [XmlAttribute]
        public String AddressTypeExternalCode { get; set; }
    }


    public class After : BaseViewModel
    {
        // elements simples
        [XmlElement]
        public int Sens { get; set; }
        [XmlElement]
        public int Year { get; set; }
        [XmlElement]
        public int Month { get; set; }
        [XmlElement]
        public int Day { get; set; }
        [XmlElement]
        public int Hour { get; set; }
        [XmlElement]
        public int Minute { get; set; }
        [XmlElement]
        public int Criteria { get; set; }
    }


    public class Arrival : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public String Type { get; set; }
        [XmlAttribute]
        public int ODTIdx { get; set; }
        [XmlAttribute]
        public int ODTPos { get; set; }
        [XmlAttribute]
        public int StopIdx { get; set; }

        // elements
        [XmlElement]
        public Date Date { get; set; }
        [XmlElement]
        public Time Time { get; set; }
        [XmlElement]
        public Coord Coord { get; set; }
        [XmlElement]
        public StopPoint StopPoint { get; set; }
    }


    public class Backward : BaseViewModel
    {
        [XmlAttribute]
        public String BackwardName { get; set; }
        [XmlElement]
        public Direction Direction { get; set; }
    }


    public class Before : BaseViewModel
    {
        // elements simples
        [XmlElement]
        public int Sens { get; set; }
        [XmlElement]
        public int Year { get; set; }
        [XmlElement]
        public int Month { get; set; }
        [XmlElement]
        public int Day { get; set; }
        [XmlElement]
        public int Hour { get; set; }
        [XmlElement]
        public int Minute { get; set; }
        [XmlElement]
        public int Criteria { get; set; }
    }


    public class Call : BaseViewModel
    {
        // elements
        [XmlElement]
        public Before Before { get; set; }
        [XmlElement]
        public This This { get; set; }
        [XmlElement]
        public After After { get; set; }
    }


    public class City : BaseViewModel
    {

        [XmlAttribute]
        public int CityIdx { get; set; }
        [XmlAttribute]
        public int CityId { get; set; }
        [XmlAttribute]
        public String CityName { get; set; }
        [XmlAttribute]
        public String CityExternalCode { get; set; }
        [XmlAttribute]
        public String CityCode { get; set; }

        [XmlElement]
        public Country Country { get; set; }
    }


    public class Comment : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public int CommentIdx { get; set; }
        [XmlAttribute]
        public int CommentId { get; set; }
        [XmlAttribute]
        public String CommentName { get; set; }
        [XmlAttribute]
        public String CommentExternalCode { get; set; }

        // elements
        [XmlElement]
        public StartDate StartDate { get; set; }
        [XmlElement]
        public EndDate EndDate { get; set; }
    }


    public class CommentList : BaseViewModel
    {
        [XmlAttribute]
        public int CommentCount { get; set; }

        [XmlElement]
        public ObservableCollectionViewModel<Comment> Comment { get; set; }
    }


    public class Company : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public int CompanyIdx { get; set; }
        [XmlAttribute]
        public int CompanyId { get; set; }
        [XmlAttribute]
        public String CompanyName { get; set; }
        [XmlAttribute]
        public String CompanyExternalCode { get; set; }
    }


    public class Country : BaseViewModel
    {
        [XmlAttribute]
        public int CountryIdx { get; set; }
        [XmlAttribute]
        public int CountryId { get; set; }
        [XmlAttribute]
        public String CountryName { get; set; }
        [XmlAttribute]
        public String CountryExternalCode { get; set; }
    }


    public class Departure : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public String Type { get; set; }
        [XmlAttribute]
        public int ODTIdx { get; set; }
        [XmlAttribute]
        public int ODTPos { get; set; }
        [XmlAttribute]
        public int StopIdx { get; set; }

        // elements
        [XmlElement]
        public Date Date { get; set; }
        [XmlElement]
        public Time Time { get; set; }
        [XmlElement]
        public Coord Coord { get; set; }
        [XmlElement]
        public StopPoint StopPoint { get; set; }
    }


    public class Destination : BaseViewModel
    {
        // elements
        [XmlElement]
        public StopArea StopArea { get; set; }
    }


    public class DestinationList : BaseViewModel
    {
        [XmlAttribute]
        public int DestinationCount { get; set; }

        [XmlElement]
        public ObservableCollectionViewModel<Destination> Destination { get; set; }
    }


    public class Direction : BaseViewModel
    {
        [XmlElement]
        public StopArea StopArea { get; set; }
    }


    public class EndNode : BaseViewModel
    {
        // elements
        [XmlElement]
        public Coord Coord { get; set; }
    }


    public class EntryPoint : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public String EntryPointType { get; set; }
        [XmlAttribute]
        public int EntryPointResponseQuality { get; set; }
        [XmlAttribute]
        public String CityName { get; set; }
        [XmlAttribute]
        public String Number { get; set; }
        [XmlAttribute]
        public String TypeName { get; set; }
        [XmlAttribute]
        public String EntryPointName { get; set; }

        // elements
        [XmlElement]
        public Site Site { get; set; }
        [XmlElement]
        public Coord Coord { get; set; }

        //[XmlArray("HangList")]
        //[XmlArrayItem("Hang", typeof(Hang))]
        [XmlElement]
        public HangList HangList { get; set; }

        [XmlElement]
        public StopArea StopArea { get; set; }
        [XmlElement]
        public City City { get; set; }
    }


    public class EntryPointList : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public int EntryPointCount { get; set; }
        [XmlAttribute]
        public int EntryPointInQualityCount { get; set; }

        [XmlElement]
        public ObservableCollectionViewModel<EntryPoint> EntryPoint { get; set; }
    }


    public class Equipment : BaseViewModel
    {
        //[XmlAttribute]
        //public NavitiaBoolean BikeDepot { get; set; }
        //[XmlAttribute]
        //public NavitiaBoolean BikeAccepted { get; set; }
        //[XmlAttribute]
        //public NavitiaBoolean Escalator { get; set; }
        //[XmlAttribute]
        //public NavitiaBoolean Elevator { get; set; }
        //[XmlAttribute]
        //public NavitiaBoolean MIPAccess { get; set; }
        //[XmlAttribute]
        //public NavitiaBoolean Sheltered { get; set; }
        //[XmlAttribute]
        //public NavitiaBoolean VisualAnnouncement { get; set; }
        //[XmlAttribute]
        //public NavitiaBoolean AudibleAnnouncement { get; set; }
        //[XmlAttribute]
        //public NavitiaBoolean AppropriateEscort { get; set; }
        //[XmlAttribute]
        //public NavitiaBoolean AppropriateSignage { get; set; }
    }


    public class Event : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public int EventID { get; set; }
        [XmlAttribute]
        public int ProviderID { get; set; }

        // éléments
        [XmlElement]
        public EventLevelTitle EventLevelTitle { get; set; }
        [XmlElement]
        public EventTitle EventTitle { get; set; }
        [XmlElement]
        public EventExternalCode EventExternalCode { get; set; }
        [XmlElement]
        public EventPublicationStartDate EventPublicationStartDate { get; set; }
        [XmlElement]
        public EventPublicationEndDate EventPublicationEndDate { get; set; }

        //[XmlArray("ImpactList")]
        //[XmlArrayItem("Impact", typeof(Impact))]
        [XmlElement]
        public ImpactList ImpactList { get; set; }
    }


    public class EventLevelTitle : BaseViewModel
    {
        public String Value { get; set; }
    }


    public class EventTitle : BaseViewModel
    {
        public String Value { get; set; }
    }


    public class EventExternalCode : BaseViewModel
    {
        public String Value { get; set; }
    }


    public class EventPublicationStartDate : BaseViewModel
    {
        public String Value { get; set; }
    }


    public class EventPublicationEndDate : BaseViewModel
    {
        public String Value { get; set; }
    }


    public class EventList : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public int EventCount { get; set; }

        [XmlElement]
        public ObservableCollectionViewModel<Event> Event { get; set; }
    }

    public class EventPos : BaseViewModel
    {

    }


    public class EventPosList : ObservableCollectionViewModel<EventPos>
    {
        // attributs
        [XmlAttribute]
        public int EventPosCount { get; set; }
    }


    public class FareSection : BaseViewModel
    {
    }


    public class FareSectionList : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public int FareSectionCount { get; set; }

        [XmlElement]
        public ObservableCollectionViewModel<FareSection> FareSection { get; set; }
    }


    public class FareZone : BaseViewModel
    {
    }


    public class FareZoneList : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public int FareZoneCount { get; set; }

        [XmlElement]
        public ObservableCollectionViewModel<FareZone> FareZone { get; set; }
    }



    public class Forward : BaseViewModel
    {
        [XmlAttribute]
        public String ForwardName { get; set; }

        [XmlElement]
        public Direction Direction { get; set; }
    }


    public class Hang : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public int StopPointIdx { get; set; }
        [XmlAttribute]
        public int Duration { get; set; }
        [XmlAttribute]
        public int ConnectionKind { get; set; }
    }


    public class HangList : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public String Odd { get; set; }
        [XmlAttribute]
        public int StartNb { get; set; }
        [XmlAttribute]
        public int EndNb { get; set; }

        [XmlElement]
        public ObservableCollectionViewModel<Hang> Hang { get; set; }
    }


    public class Impact : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public int ImpactID { get; set; }

        [XmlElement]
        public StopPoint StopPoint { get; set; }
        [XmlElement]
        public ImpactState ImpactState { get; set; }
        [XmlElement]
        public ImpactStartDate ImpactStartDate { get; set; }
        [XmlElement]
        public ImpactEndDate ImpactEndDate { get; set; }
        [XmlElement]
        public ImpactCloseDate ImpactCloseDate { get; set; }
        [XmlElement]
        public ImpactDuration ImpactDuration { get; set; }
        [XmlElement]
        public ImpactMessage ImpactMessage { get; set; }
        [XmlElement]
        public Event Event { get; set; }
    }


    public class ImpactList : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public int ImpactCount { get; set; }

        [XmlElement]
        public ObservableCollectionViewModel<Impact> Impact { get; set; }
    }

    public class ImpactPos : BaseViewModel
    {

    }

    public class ImpactPosList : ObservableCollectionViewModel<ImpactPos>
    {
        // attributs
        [XmlAttribute]
        public int ImpactPosCount { get; set; }
    }


    public class ImpactState : BaseViewModel
    {
        public String Value { get; set; }
    }


    public class ImpactStartDate : BaseViewModel
    {
        public String Value { get; set; }
    }


    public class ImpactEndDate : BaseViewModel
    {
        public String Value { get; set; }
    }


    public class ImpactCloseDate : BaseViewModel
    {
        public String Value { get; set; }
    }


    public class ImpactDuration : BaseViewModel
    {
        public int Value { get; set; }
    }


    public class ImpactMessage : BaseViewModel
    {
        public String Value { get; set; }
    }



    public class IsTransday : BaseViewModel
    {
        // attributs
        public int Value { get; set; }
    }


    public class JourneyResult : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public int SectionCount { get; set; }
        [XmlAttribute]
        public String Criteria { get; set; }
        [XmlAttribute]
        public int IsCriteriaFound { get; set; }
        [XmlAttribute]
        public int IsBest { get; set; }
        [XmlAttribute]
        public int JourneyResultPosition { get; set; }
        [XmlAttribute]
        public int IsFirstSoluce { get; set; }
        [XmlAttribute]
        public int IsLastSoluce { get; set; }
        [XmlAttribute]
        public int IsDisrupt { get; set; }

        // elements
        [XmlElement]
        public Summary Summary { get; set; }
        [XmlElement]
        public ObservableCollection<Section> Section { get; set; }
    }


    public class JourneyResultList : BaseViewModel
    {
        [XmlAttribute]
        public int JourneyResultCount { get; set; }

        [XmlElement]
        public ObservableCollectionViewModel<JourneyResult> JourneyResult { get; set; }
    }


    public class Line : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public int LineIdx { get; set; }
        [XmlAttribute]
        public int LineId { get; set; }
        [XmlAttribute]
        public String LineName { get; set; }
        [XmlAttribute]
        public String LineCode { get; set; }
        [XmlAttribute]
        public String LineExternalCode { get; set; }
        [XmlAttribute]
        public String LineAdditionalData { get; set; }
        [XmlAttribute]
        public String SortOrder { get; set; }

        // elements
        [XmlElement]
        public Mode Mode { get; set; }
        [XmlElement]
        public Network Network { get; set; }
        [XmlElement]
        public Forward Forward { get; set; }
        [XmlElement]
        public Backward Backward { get; set; }

        //[XmlArray("EventPosList")]
        //[XmlArrayItem("EventPos", typeof(EventPos))]
        //public EventPosList EventPosList { get; set; }
    }


    public class LineList : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public int LineCount { get; set; }

        [XmlElement]
        public ObservableCollectionViewModel<Line> Line { get; set; }
    }


    public class LineStopAreaList : ObservableCollectionViewModel<StopArea>
    {
        // attributs
        [XmlAttribute]
        public int StopAreaCount { get; set; }
    }


    public class Mode : BaseViewModel
    {
        [XmlAttribute]
        public int ModeIdx { get; set; }
        [XmlAttribute]
        public int ModeId { get; set; }
        [XmlAttribute]
        public String ModeName { get; set; }
        [XmlAttribute]
        public String ModeExternalCode { get; set; }

        public String Value { get; set; }
    }


    public class ModeType : BaseViewModel
    {
        public String Value { get; set; }
    }


    public class ModeList : BaseViewModel
    {
        [XmlAttribute]
        public int ModeCount { get; set; }

        [XmlElement]
        public ObservableCollectionViewModel<Mode> Mode { get; set; }
    }


    public class Network : BaseViewModel
    {
        [XmlAttribute]
        public int NetworkIdx { get; set; }
        [XmlAttribute]
        public int NetworkId { get; set; }
        [XmlAttribute]
        public String NetworkName { get; set; }
        [XmlAttribute]
        public String NetworkExternalCode { get; set; }
        [XmlAttribute]
        public String NetworkAdditionalData { get; set; }

        [XmlAttribute]
        public String Server { get; set; }
        [XmlAttribute]
        public String TimeoKey { get; set; }
        [XmlAttribute]
        public String TimeoVille { get; set; }
        //[XmlArray("List")]
        //[XmlArrayItem("", typeof())]
        //public EventPos[] EventPosList { get; set; }

        public override string ToString()
        {
            return NetworkName;
        }
    }


    public class NetworkList : BaseViewModel// ObservableCollectionViewModel<Network>
    {
        // attributs
        [XmlAttribute]
        public int NetworkCount { get; set; }
        [XmlAttribute]
        public string Region { get; set; }

        // listes d'elements
        [XmlElement]
        public ObservableCollection<Network> Network { get; set; }

        public override string ToString()
        {
            return Region;
        }


        //public Network GetNetworkByExternalCode(String _NetworkExternalCode)
        //{
        //    foreach (Network network in Network)
        //    {
        //        if (network.NetworkExternalCode.Equals(_NetworkExternalCode))
        //        {
        //            return network;
        //        }
        //    }
        //    return null;
        //}


    }


    public class NextDepartureList : ObservableCollectionViewModel<Stop>
    {
        // attributs
        [XmlAttribute]
        public int StopCount { get; set; }
    }


    public class Nota : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public String NotaType { get; set; }
    }


    public class ODT : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public int ODTIdx { get; set; }
        [XmlAttribute]
        public int ODTId { get; set; }
        [XmlAttribute]
        public String ODTName { get; set; }
        [XmlAttribute]
        public String ODTExternalCode { get; set; }
        [XmlAttribute]
        public int Position { get; set; }

        // elements
        [XmlElement]
        public Network Netowrk { get; set; }
    }


    public class ODTList : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public int ODTCount { get; set; }

        [XmlElement]
        public ObservableCollectionViewModel<ODT> ODT { get; set; }
    }


    public class PagerInfo : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public int ResponseCount { get; set; }
        [XmlAttribute]
        public int ResponseStartIndex { get; set; }
        [XmlAttribute]
        public int TotalCount { get; set; }
    }


    public class Proximity : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public int Distance { get; set; }

        [XmlElement]
        public StopArea StopArea { get; set; }
        [XmlElement]
        public StopPoint StopPoint { get; set; }
    }


    public class ProximityList : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public int ProximityCount { get; set; }

        [XmlElement]
        public ObservableCollectionViewModel<Proximity> Proximity { get; set; }
    }


    public class Route : BaseViewModel
    {
        [XmlAttribute]
        public int RouteIdx { get; set; }
        [XmlAttribute]
        public int RouteId { get; set; }
        [XmlAttribute]
        public String RouteName { get; set; }
        [XmlAttribute]
        public String RouteExternalCode { get; set; }
        [XmlAttribute]
        public int IsForward { get; set; }
        [XmlAttribute]
        public int RouteLineIdx { get; set; }
        [XmlAttribute]
        public int IsFrequence { get; set; }

        [XmlElement]
        public Line Line { get; set; }
    }


    public class RouteList : BaseViewModel
    {
        [XmlAttribute]
        public int RouteCount { get; set; }

        [XmlElement]
        public ObservableCollectionViewModel<Route> Route { get; set; }
    }


    public class Section : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public String Type { get; set; }

        // elements
        [XmlElement]
        public Duration Duration { get; set; }
        [XmlElement]
        public Departure Departure { get; set; }
        [XmlElement]
        public Arrival Arrival { get; set; }
        [XmlElement]
        public VehicleJourney VehicleJourney { get; set; }
        [XmlElement]
        public Nota Nota { get; set; }
        //[XmlArray("FareSectionList")]
        //[XmlArrayItem("FareSection", typeof(FareSection))]
        [XmlElement]
        public FareSectionList FareSectionList { get; set; }
        //[XmlArray("FareZoneList")]
        //[XmlArrayItem("FareZone", typeof(FareZone))]
        [XmlElement]
        public FareZoneList FareZoneList { get; set; }
    }


    public class Segment : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public int Length { get; set; }
        [XmlAttribute]
        public int FromNbPar { get; set; }
        [XmlAttribute]
        public int ToNbPar { get; set; }
        [XmlAttribute]
        public int FromNbOdd { get; set; }
        [XmlAttribute]
        public int ToNbOdd { get; set; }
        [XmlAttribute]
        public int Duration { get; set; }

        // elements
        [XmlElement]
        public Address Address { get; set; }
        [XmlElement]
        public StartNode StartNode { get; set; }
        [XmlElement]
        public EndNode EndNode { get; set; }
    }


    public class SegmentList : BaseViewModel
    {
        [XmlAttribute]
        public int SegmentCount { get; set; }

        [XmlElement]
        public ObservableCollectionViewModel<Segment> Segment { get; set; }
    }


    public class Site : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public int SiteIdx { get; set; }
        [XmlAttribute]
        public int SiteId { get; set; }
        [XmlAttribute]
        public String SiteName { get; set; }
        [XmlAttribute]
        public String SiteExternalCode { get; set; }

        // elements
        [XmlElement]
        public City City { get; set; }
        [XmlElement]
        public SiteType SiteType { get; set; }
        [XmlElement]
        public Coord Coord { get; set; }
        [XmlElement]
        public SiteAddress SiteAddress { get; set; }
        [XmlElement]
        public Comment Comment { get; set; }
    }


    public class SiteType : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public int SiteTypeIdx { get; set; }
        [XmlAttribute]
        public int SiteTypeId { get; set; }
        [XmlAttribute]
        public String SiteTypeName { get; set; }
        [XmlAttribute]
        public String SiteTypeExternalCode { get; set; }

        // elements
    }


    public class SiteTypeList : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public int SiteTypeCount { get; set; }

        [XmlElement]
        public ObservableCollectionViewModel<SiteType> SiteType { get; set; }
    }


    public class SiteAddress : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public String SiteAddressName { get; set; }
        [XmlAttribute]
        public String SiteAddressNumber { get; set; }
        [XmlAttribute]
        public String SiteAddressTypeName { get; set; }

        // elements
    }


    public class StartNode : BaseViewModel
    {
        // elements
        [XmlElement]
        public Coord Coord { get; set; }
    }


    public class Stop : BaseViewModel
    {
        [XmlAttribute]
        public int StopIdx { get; set; }
        [XmlAttribute]
        public int StopPointIdx { get; set; }
        [XmlAttribute]
        public int VehicleJourneyIdx { get; set; }
        [XmlAttribute]
        public String VehicleJourneyExternalCode { get; set; }
        [XmlAttribute]
        public int HourNumber { get; set; }
        [XmlAttribute]
        public int MinuteNumber { get; set; }
        [XmlAttribute]
        public int DestinationPos { get; set; }
        [XmlAttribute]
        public int ODTIdx { get; set; }
        [XmlAttribute]
        public int ODTPos { get; set; }
        [XmlAttribute]
        public int ValidityPatternSetCommentPos { get; set; }
        [XmlAttribute]
        public int StopOrder { get; set; }
        [XmlAttribute]
        public int VehicleIdx { get; set; }

        [XmlElement]
        public StopTime StopTime { get; set; }
        [XmlElement]
        public StopArrivalTime StopArrivalTime { get; set; }
        [XmlElement]
        public IsTransday IsTransday { get; set; }
        [XmlElement]
        public StopPoint StopPoint { get; set; }
        [XmlElement]
        public DepartureTime DepartureTime { get; set; }
        [XmlElement]
        public Route Route { get; set; }
        [XmlElement]
        public StopArea StopArea { get; set; }

        [XmlIgnore]
        public Time StopOrDepartureTime
        {
            get
            {
                if (StopTime != null)
                {
                    return StopTime;
                }
                if (DepartureTime != null)
                {
                    return DepartureTime;
                }
                return null;
            }
        }
    }


    public class StopArea : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public int StopAreaIdx { get; set; }
        [XmlAttribute]
        public int StopAreaId { get; set; }
        [XmlAttribute]
        public String StopAreaName { get; set; }
        [XmlAttribute]
        public String StopAreaExternalCode { get; set; }
        [XmlAttribute]
        public int MainStopArea { get; set; }
        [XmlAttribute]
        public int MultiModal { get; set; }
        [XmlAttribute]
        public int CarPark { get; set; }
        [XmlAttribute]
        public int MainConnection { get; set; }
        [XmlAttribute]
        public String AdditionalData { get; set; }

        // elements
        [XmlElement]
        public City City { get; set; }
        [XmlElement]
        public Coord Coord { get; set; }
        //[XmlArray("HangList")]
        //[XmlArrayItem("Hang", typeof(Hang))]
        [XmlElement]
        public HangList HangList { get; set; }
        //[XmlArray("ModeList")]
        //[XmlArrayItem("Mode", typeof(Mode))]
        [XmlElement]
        public ModeList ModeList { get; set; }
    }


    public class StopAreaList : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public int StopAreaCount { get; set; }

        [XmlElement]
        public ObservableCollectionViewModel<StopArea> StopArea { get; set; }
    }

    [XmlType(AnonymousType = true)]
    public class StopList : BaseViewModel// ObservableCollectionViewModel<Stop>
    {
        [XmlAttribute]
        public int StopCount { get; set; }
        [XmlAttribute]
        public string Nota { get; set; }

        [XmlElement]
        public StopTime StopTime { get; set; }
        [XmlElement]
        public Route Route { get; set; }

        [XmlElement("Stop")]
        //[XmlArrayItem("Stop", typeof(Stop))]
        public ObservableCollection<Stop> Stop { get; set; }
    }


    public class StopPoint : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public int StopPointIdx { get; set; }
        [XmlAttribute]
        public String StopPointExternalCode { get; set; }
        [XmlAttribute]
        public String StopPointName { get; set; }
        [XmlAttribute]
        public int StopPointId { get; set; }
        [XmlAttribute]
        public int FareZone { get; set; }

        // éléments
        [XmlElement]
        public StopPointAddress StopPointAddress { get; set; }
        [XmlElement]
        public Equipment Equipment { get; set; }
        [XmlElement]
        public Mode Mode { get; set; }
        [XmlElement]
        public City City { get; set; }
        [XmlElement]
        public StopArea StopArea { get; set; }
        [XmlElement]
        public Coord Coord { get; set; }
        //[XmlArray("List")]
        //[XmlArrayItem("", typeof())]
        //public ImpactPos[] ImpactPosList { get; set; }
    }


    public class StopPointAddress : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public String StopPointAddressTypeName { get; set; }
        [XmlAttribute]
        public String StopPointAddressNumber { get; set; }
        [XmlAttribute]
        public String StopPointAddressName { get; set; }
    }


    public class StopPointEquipment : BaseViewModel
    {
        public String Value { get; set; }
    }


    public class StopPointList : BaseViewModel
    {
        [XmlAttribute]
        public int StopPointCount { get; set; }

        [XmlElement]
        public ObservableCollectionViewModel<StopPoint> StopPoint { get; set; }
    }


    public class Summary : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public int Interchange { get; set; }

        // elements
        [XmlElement]
        public DepartureDate DepartureDate { get; set; }
        [XmlElement]
        public DepartureTime DepartureTime { get; set; }
        [XmlElement]
        public ArrivalDate ArrivalDate { get; set; }
        [XmlElement]
        public ArrivalTime ArrivalTime { get; set; }
        [XmlElement]
        public Duration Duration { get; set; }
        [XmlElement]
        public TotalLinkTime TotalLinkTime { get; set; }
        [XmlElement]
        public TotalWaitTime TotalWaitTime { get; set; }
        [XmlElement]
        public Call Call { get; set; }
    }


    public class This : BaseViewModel
    {
        // elements simples
        [XmlElement]
        public int Sens { get; set; }
        [XmlElement]
        public int Year { get; set; }
        [XmlElement]
        public int Month { get; set; }
        [XmlElement]
        public int Day { get; set; }
        [XmlElement]
        public int Hour { get; set; }
        [XmlElement]
        public int Minute { get; set; }
        [XmlElement]
        public int Criteria { get; set; }
    }


    public class ValidityPattern : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public String ValidityPatternBeginningDay { get; set; }
        [XmlAttribute]
        public int IsAdapted { get; set; }
        [XmlAttribute]
        public int ValidityPatternIdx { get; set; }

        public String Value { get; set; }
    }


    public class ValidityPatternSet : BaseViewModel
    {

        [XmlAttribute]
        public String ValidityPatternAdditionalData { get; set; }
        [XmlAttribute]
        public String ValidityPatternExternalCode { get; set; }
        [XmlAttribute]
        public String ValidityPatternSetName { get; set; }
        [XmlAttribute]
        public int ValidityPatternSetId { get; set; }
        [XmlAttribute]
        public int ValidityPatternSetIdx { get; set; }

        //[XmlArray("ValidityPatternSetDetailList")]
        //[XmlArrayItem("ValidityPatternSetDetail", typeof(ValidityPatternSetDetail))]
        [XmlElement]
        public ValidityPatternSetDetailList ValidityPatternSetDetailList { get; set; }
    }

    public class ValidityPatternSetDetail : BaseViewModel
    {

    }

    public class ValidityPatternSetDetailList : BaseViewModel
    {
        [XmlAttribute]
        public int VPatternSetDetailCount { get; set; }

        [XmlElement]
        public ObservableCollectionViewModel<ValidityPatternSetDetail> ValidityPatternSetDetail { get; set; }
    }


    public class Vehicle : BaseViewModel
    {
        [XmlAttribute]
        public int VehicleIdx { get; set; }
        [XmlAttribute]
        public int VehicleId { get; set; }
        [XmlIgnore]
        public bool BikeAccepted { get; set; }
        [XmlAttribute("BikeAccepted")]
        public string BikeAcceptedString { get { return BikeAccepted ? "True" : "False"; } set { BikeAccepted = value == "True" ? true : false; } }
        //[XmlAttribute]
        //public NavitiaBoolean MIPAccess { get; set; }
        //[XmlAttribute]
        //public NavitiaBoolean AirConditioned { get; set; }
        [XmlAttribute]
        public String VehicleExternalCode { get; set; }
        [XmlAttribute]
        public String VehicleName { get; set; }

        public String Value { get; set; }
    }


    public class VehicleJourney : BaseViewModel
    {
        // attributs
        [XmlAttribute]
        public int VehicleJourneyIdx { get; set; }
        [XmlAttribute]
        public int VehicleJourneyId { get; set; }
        [XmlAttribute]
        public String VehicleJourneyName { get; set; }
        [XmlAttribute]
        public int VehicleJourneyRouteIdx { get; set; }
        [XmlAttribute]
        public String VehicleJourneyExternalCode { get; set; }

        // elements
        [XmlElement]
        public Route Route { get; set; }
        [XmlElement]
        public Destination Destination { get; set; }
        [XmlElement]
        public Mode Mode { get; set; }
        [XmlElement]
        public Company Company { get; set; }
        [XmlElement]
        public Vehicle Vehicle { get; set; }
        [XmlElement]
        public ValidityPattern ValidityPattern { get; set; }
        //[XmlArray("List")]
        //[XmlArrayItem("", typeof())]
        //public EventPos[] EventPosList { get; set; }
    }


    public class VehicleList : BaseViewModel
    {
        [XmlAttribute]
        public int VehicleCount { get; set; }

        [XmlElement]
        public ObservableCollectionViewModel<Vehicle> Vehicle { get; set; }
    }

    // extensions

    public class Server : BaseViewModel
    {
        // attributs
        //[XmlAttribute]
        //public String ServerName { get; set; }
        //[XmlAttribute]
        //public String ServerExternalCode { get; set; }
        [XmlAttribute]
        public String Region { get; set; }

        // elements
        //[XmlArray("NetworkList")]
        //[XmlArrayItem("Network", typeof(Network))]
        [XmlElement]
        public NetworkList NetworkList { get; set; }
    }



    public class ActionAddressList : ObservableCollectionViewModel<Address>
    {

    }

    public class ActionAddressTypeList : ObservableCollectionViewModel<AddressType>
    {

    }

    public class ActionBookingTypeList : ObservableCollectionViewModel<BookingType>
    {

    }

    public class ActionCityList : ObservableCollectionViewModel<City>
    {

    }

    public class ActionCommonLetterList : ObservableCollectionViewModel<CommonLetter>
    {

    }

    public class ActionCompanyList : ObservableCollectionViewModel<Company>
    {

    }

    public class ActionDepartmentList : ObservableCollectionViewModel<Department>
    {

    }

    public class ActionDirectStopAreaList : ObservableCollectionViewModel<StopArea>
    {

    }

    public class ActionDistrictList : ObservableCollectionViewModel<District>
    {

    }

    public class FirstLetter : BaseViewModel
    {

    }
    public class ActionFirstLetterList : ObservableCollectionViewModel<FirstLetter>
    {

    }

    public class ActionFreqSettingList : ObservableCollectionViewModel<FreqSetting>
    {

    }

    public class ActionGetEvent : BaseViewModel
    {

    }

    public class ActionGetEventList : ObservableCollectionViewModel<Event>
    {

    }

    public class ActionGetImpact : BaseViewModel
    {

    }

    public class ActionGetMediaList : ObservableCollectionViewModel<Media>
    {

    }

    public class ActionGetMessage : BaseViewModel
    {

    }

    public class ActionIsochronList : ObservableCollectionViewModel<Isochron>
    {

    }

    public class ActionLineRouteDescription : BaseViewModel
    {

    }

    public class ActionNextArrivalList : ObservableCollectionViewModel<Arrival>
    {

    }

    public class ActionOnBoardServiceList : ObservableCollectionViewModel<OnBoardService>
    {

    }

    public class ActionPhoneticList : ObservableCollectionViewModel<Phonetic>
    {

    }

    public class ActionRouteList : ObservableCollectionViewModel<Route>
    {

    }
    public class ActionSiteList : ObservableCollectionViewModel<Site>
    {

    }
    public class ActionStopPointList : ObservableCollectionViewModel<StopPoint>
    {

    }
    public class VehicleJourneyDetail : BaseViewModel
    {

    }
    public class ActionVehicleJourneyDetailList : ObservableCollectionViewModel<VehicleJourneyDetail>
    {

    }
    public class ActionVehicleJourneyList : ObservableCollectionViewModel<VehicleJourney>
    {

    }
    public class AddressList : ObservableCollectionViewModel<Address> { }
    public class AddressTypeList : ObservableCollectionViewModel<AddressType> { }
    public class BookingType : BaseViewModel { }
    public class BookingTypeList : ObservableCollectionViewModel<BookingType> { }
    public class CityList : ObservableCollectionViewModel<City> { }
    public class CommonLetter : BaseViewModel { }
    public class CommonLetterList : ObservableCollectionViewModel<CommonLetter> { }
    public class CompanyList : ObservableCollectionViewModel<Company> { }
    public class DLL : BaseViewModel { }
    public class Database : BaseViewModel { }
    public class Department : BaseViewModel { }
    public class DepartmentList : ObservableCollectionViewModel<Department> { }
    public class DirectStopAreaList : ObservableCollectionViewModel<StopArea> { }
    public class District : BaseViewModel { }
    public class DistrictList : ObservableCollectionViewModel<District> { }
    public class FreqSetting : BaseViewModel { }
    public class FreqSettingList : ObservableCollectionViewModel<FreqSetting> { }
    public class FreqStop : BaseViewModel { }
    public class FreqStopList : ObservableCollectionViewModel<FreqStop> { }
    public class Isochron : BaseViewModel { }
    public class IsochronList : ObservableCollectionViewModel<Isochron> { }
    public class LineSchedule : BaseViewModel { }
    public class LineScheduleList : ObservableCollectionViewModel<LineSchedule> { }
    public class Media : BaseViewModel { }
    public class MediaList : ObservableCollectionViewModel<Media> { }
    public class Message : BaseViewModel { }
    public class ModeTypeList : ObservableCollectionViewModel<ModeType> { }
    public class MsgCategory : BaseViewModel { }
    public class NextArrivalList : ObservableCollectionViewModel<Arrival> { }
    public class OnBoardService : BaseViewModel { }
    public class OnBoardServiceList : ObservableCollectionViewModel<OnBoardService> { }
    public class Origin : BaseViewModel { }
    public class Phonetic : BaseViewModel { }
    public class PhoneticList : ObservableCollectionViewModel<Phonetic> { }
    public class RoutePoint : BaseViewModel { }
    public class RoutePointList : ObservableCollectionViewModel<RoutePoint> { }
    public class SiteList : ObservableCollectionViewModel<Site> { }
    public class Thread : BaseViewModel { }
    public class VPTranslation : BaseViewModel { }
    public class VehicleJourneyList : ObservableCollectionViewModel<VehicleJourney> { }
}
