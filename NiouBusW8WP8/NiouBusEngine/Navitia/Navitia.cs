using System;
using System.Collections.Generic;
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
            string X = null,
            string Y = null
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

    public class ActionJourneyResultList
    {
        public static String Action = "PlanJourney";
        // elements
        [XmlArray("JourneyResultList")]
        [XmlArrayItem("JourneyResult", typeof(JourneyResult))]
        public JourneyResult[] JourneyResultList { get; set; }

        [XmlArray("CommentList")]
        [XmlArrayItem("Comment", typeof(Comment))]
        public Comment[] CommentList { get; set; }

        [XmlArray("ODTList")]
        [XmlArrayItem("ODT", typeof(ODT))]
        public ODT[] ODTList { get; set; }

        [XmlArray("EventList")]
        [XmlArrayItem("Event", typeof(Event))]
        public Event[] EventList { get; set; }
    }

    public class ActionLineStopAreaList
    {
        public static String Action = "LineStopAreaList";
        // elements
        [XmlArray("LineStopAreaList")]
        [XmlArrayItem("StopArea", typeof(StopArea))]
        public StopArea[] LineStopAreaList { get; set; }
    }

    [XmlRoot]
    public class ActionLineList
    {
        public static String Action = "LineList";
        // elements
        //public LineList LineList { get; set; }
        [XmlArray("LineList")]
        [XmlArrayItem("Line", typeof(Line))]
        public Line[] Line { get; set; }
    }

    public class ActionNetworkList
    {
        public static String Action = "NetworkList";
        // elements
        [XmlArray("NetworkList")]
        [XmlArrayItem("Network", typeof(Network))]
        public Network[] NetworkList { get; set; }

    }

    public class ActionModeList
    {
        public static String Action = "ModeList";
        // elements
        [XmlArray("ModeList")]
        [XmlArrayItem("Mode", typeof(Mode))]
        public Mode[] ModeList { get; set; }
    }

    public class ActionStopAreaList
    {
        public static String Action = "StopAreaList";
        // elements
        //public StopAreaList StopAreaList { get; set; }
        [XmlArray("StopAreaList")]
        [XmlArrayItem("StopArea", typeof(StopArea))]
        public StopArea[] StopArea { get; set; }
    }

    public class ActionEntryPointList
    {
        public static String Action = "EntryPoint";
        // elements
        [XmlArray("EntryPointList")]
        [XmlArrayItem("EntryPoint", typeof(EntryPoint))]
        public EntryPoint[] EntryPointList { get; set; }
        public PagerInfo PagerInfo { get; set; }

    }

    public class BinaryCriteria
    {
        public static String Action = "MakeBinaryCriteria";
        // elements
        public Vehicle Vehicle { get; set; }
        public StopPointEquipment StopPointEquipment { get; set; }
        public Mode Mode { get; set; }
        public ModeType ModeType { get; set; }
    }


    public class EndOfCourseList
    {
        public static String Action = "EndOfCourse";

        [XmlArray("StopList")]
        [XmlArrayItem("Stop", typeof(Stop))]
        public Stop[] StopList { get; set; }
        public VehicleJourney VehicleJourney { get; set; }
    }


    public class ActionProximityList
    {
        public static String Action = "ProximityList";

        [XmlArray("ProximityList")]
        [XmlArrayItem("Proximity", typeof(Proximity))]
        public Proximity[] ProximityList { get; set; }
    }

    public class ActionStreetNetwork
    {
        public static String Action = "StreetNetwork";

        [XmlArray("SegmentList")]
        [XmlArrayItem("Segment", typeof(Segment))]
        public Segment[] SegmentList { get; set; }
    }

    public class ActionNextDepartureList
    {
        public static String Action = "NextDeparture";

        //[XmlArray("List")]
        //[XmlArrayItem("", typeof())]
        //public NextDeparture[] NextDepartureList { get; set; }

        [XmlArray("EventList")]
        [XmlArrayItem("Event", typeof(Event))]
        public Event[] EventList { get; set; }
    }

    public class ActionImpactList
    {
        public static String Action = "GetImpactList";

        [XmlArray("ImpactList")]
        [XmlArrayItem("Impact", typeof(Impact))]
        public Impact[] ImpactList { get; set; }
    }

    public class ActionSiteTypeList
    {
        public static String Action = "SiteTypeList";

        [XmlArray("SiteTypeList")]
        [XmlArrayItem("SiteType", typeof(SiteType))]
        public SiteType[] SiteTypeList { get; set; }
    }

    public class ActionRoutePointList
    {

    }

    public class ActionModeTypeList
    {

    }

    public class StopList //: List<Stop>
    {
        [XmlAttribute]
        public string Nota { get; set; }

        [XmlElement]
        public Stop[] Stop { get; set; }
    }

    public class DepartureBoardList
    {
        public static String Action = "DepartureBoard";

        // attributs
        [XmlAttribute]
        public int MaxHourNumber { get; set; }
        [XmlAttribute]
        public int MaxMinuteNumber { get; set; }

        // elements
        //[XmlArray("StopList")]
        //[XmlArrayItem("Stop", typeof(Stop))]
        //public Stop[] StopList { get; set; }
        [XmlElement("StopList")]
        public StopList StopList { get; set; }

        [XmlArray("StopPointList")]
        [XmlArrayItem("StopPoint", typeof(StopPoint))]
        public StopPoint[] StopPointList { get; set; }

        [XmlArray("LineList")]
        [XmlArrayItem("Line", typeof(Line))]
        public Line[] LineList { get; set; }

        [XmlArray("RouteList")]
        [XmlArrayItem("Route", typeof(Route))]
        public Route[] RouteList { get; set; }

        [XmlArray("DestinationList")]
        [XmlArrayItem("Destination", typeof(Destination))]
        public Destination[] DestinationList { get; set; }

        [XmlArray("CommentList")]
        [XmlArrayItem("Comment", typeof(Comment))]
        public Comment[] CommentList { get; set; }

        [XmlArray("ODTList")]
        [XmlArrayItem("ODT", typeof(ODT))]
        public ODT[] ODTList { get; set; }

        public ValidityPatternSet ValidityPatternSet { get; set; }

        [XmlArray("VehicleList")]
        [XmlArrayItem("Vehicle", typeof(Vehicle))]
        public Vehicle[] VehicleList { get; set; }

    }


    [XmlRoot]
    public class ServerList
    {

        // elements
        //[XmlArray("Server")]
        [XmlElement("NetworkList")]
        public NetworkList[] NetworkList { get; set; }

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


    public class Coord
    {
        // éléments simples
        private double _CoordX;
        [XmlIgnore]
        public double CoordX
        {
            get
            {
                return _CoordX;
            }
            set
            {
                _CoordX = value;
            }
        }
        [XmlElement(ElementName = "CoordX")]
        public String CoordXString
        {
            get
            {
                return _CoordX.ToString();//.Replace(".", ",");
            }
            set
            {
                _CoordX = double.Parse(value/*.Replace(",", ".")*/);
            }
        }
        private double _CoordY;
        [XmlIgnore]
        public double CoordY
        {
            get
            {
                return _CoordY;
            }
            set
            {
                _CoordY = value;
            }
        }
        [XmlElement(ElementName = "CoordY")]
        public String CoordYString
        {
            get
            {
                return _CoordY.ToString();//.Replace(".", ",");
            }
            set
            {
                _CoordY = double.Parse(value/*.Replace(",", ".")*/);
            }
        }
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


    public class Date
    {
        // elements simples
        public int Year { get; set; }
        public int Month { get; set; }
        public int Day { get; set; }
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


    public class Time
    {
        // elements simples
        public int TotalSeconds { get; set; }
        public int Day { get; set; }
        public int Hour { get; set; }
        public int Minute { get; set; }
        public int Second { get; set; }

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


    public class Address
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
        public City City { get; set; }
        public AddressType AddressType { get; set; }
        public Coord Coord { get; set; }
    }


    public class AddressType
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


    public class After
    {
        // elements simples
        public int Sens { get; set; }
        public int Year { get; set; }
        public int Month { get; set; }
        public int Day { get; set; }
        public int Hour { get; set; }
        public int Minute { get; set; }
        public int Criteria { get; set; }
    }


    public class Arrival
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
        public Date Date { get; set; }
        public Time Time { get; set; }
        public Coord Coord { get; set; }
        public StopPoint StopPoint { get; set; }
    }


    public class Backward
    {
        [XmlAttribute]
        public String BackwardName { get; set; }
        public Direction Direction { get; set; }
    }


    public class Before
    {
        // elements simples
        public int Sens { get; set; }
        public int Year { get; set; }
        public int Month { get; set; }
        public int Day { get; set; }
        public int Hour { get; set; }
        public int Minute { get; set; }
        public int Criteria { get; set; }
    }


    public class Call
    {
        // elements
        public Before Before { get; set; }
        public This This { get; set; }
        public After After { get; set; }
    }


    public class City
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

        public Country Country { get; set; }
    }


    public class Comment
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
        public StartDate StartDate { get; set; }
        public EndDate EndDate { get; set; }
    }


    //public class CommentList
    //{
    //    [XmlAttribute]
    //    public int CommentCount { get; set; }

    //    public List<Comment> Comment { get; set; }

    //}


    public class Company
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


    public class Country
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


    public class Departure
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
        public Date Date { get; set; }
        public Time Time { get; set; }
        public Coord Coord { get; set; }
        public StopPoint StopPoint { get; set; }
    }


    public class Destination
    {
        // elements
        public StopArea StopArea { get; set; }
    }


    //public class DestinationList
    //{
    //    [XmlAttribute]
    //    public int DestinationCount { get; set; }

    //    public List<StopArea> StopArea { get; set; }
    //}


    public class Direction
    {
        public StopArea StopArea { get; set; }
    }


    public class EndNode
    {
        // elements
        public Coord Coord { get; set; }
    }


    public class EntryPoint
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
        public Site Site { get; set; }
        public Coord Coord { get; set; }

        [XmlArray("HangList")]
        [XmlArrayItem("Hang", typeof(Hang))]
        public Hang[] HangList { get; set; }

        public StopArea StopArea { get; set; }
        public City City { get; set; }
    }


    //public class EntryPointList
    //{
    //    // attributs
    //    [XmlAttribute]
    //    public int EntryPointCount { get; set; }
    //    [XmlAttribute]
    //    public int EntryPointInQualityCount { get; set; }

    //    // elements
    //    public List<EntryPoint> EntryPoint { get; set; }
    //}


    public class Equipment
    {
        //[XmlAttribute]
        //public bool BikeDepot { get; set; }
        //[XmlAttribute]
        //public bool BikeAccepted { get; set; }
        //[XmlAttribute]
        //public bool Escalator { get; set; }
        //[XmlAttribute]
        //public bool Elevator { get; set; }
        //[XmlAttribute]
        //public bool MIPAccess { get; set; }
        //[XmlAttribute]
        //public bool Sheltered { get; set; }
        //[XmlAttribute]
        //public bool VisualAnnouncement { get; set; }
        //[XmlAttribute]
        //public bool AudibleAnnouncement { get; set; }
        //[XmlAttribute]
        //public bool AppropriateEscort { get; set; }
        //[XmlAttribute]
        //public bool AppropriateSignage { get; set; }
    }


    public class Event
    {
        // attributs
        [XmlAttribute]
        public int EventID { get; set; }
        [XmlAttribute]
        public int ProviderID { get; set; }

        // éléments
        public EventLevelTitle EventLevelTitle { get; set; }
        public EventTitle EventTitle { get; set; }
        public EventExternalCode EventExternalCode { get; set; }
        public EventPublicationStartDate EventPublicationStartDate { get; set; }
        public EventPublicationEndDate EventPublicationEndDate { get; set; }

        [XmlArray("ImpactList")]
        [XmlArrayItem("Impact", typeof(Impact))]
        public Impact[] ImpactList { get; set; }
    }


    public class EventLevelTitle
    {
        public String Value { get; set; }
    }


    public class EventTitle
    {
        public String Value { get; set; }
    }


    public class EventExternalCode
    {
        public String Value { get; set; }
    }


    public class EventPublicationStartDate
    {
        public String Value { get; set; }
    }


    public class EventPublicationEndDate
    {
        public String Value { get; set; }
    }


    //public class EventList
    //{
    //    // attributs
    //    [XmlAttribute]
    //    public int EventCount { get; set; }
    //}


    //public class EventPosList
    //{
    //    // attributs
    //    [XmlAttribute]
    //    public int EventPosCount { get; set; }
    //}


    public class FareSection
    {
    }


    //public class FareSectionList
    //{
    //    // attributs
    //    [XmlAttribute]
    //    public int FareSectionCount { get; set; }

    //    // elements
    //    public List<FareSection> FareSection { get; set; }
    //}


    public class FareZone
    {
    }


    //public class FareZoneList
    //{
    //    // attributs
    //    [XmlAttribute]
    //    public int FareZoneCount { get; set; }

    //    // elements
    //    public List<FareZone> FareZone { get; set; }
    //}



    public class Forward
    {
        [XmlAttribute]
        public String ForwardName { get; set; }

        public Direction Direction { get; set; }
    }


    public class Hang
    {
        // attributs
        [XmlAttribute]
        public int StopPointIdx { get; set; }
        [XmlAttribute]
        public int Duration { get; set; }
        [XmlAttribute]
        public int ConnectionKind { get; set; }
    }


    //public class HangList
    //{
    //    // attributs
    //    [XmlAttribute]
    //    public String Odd { get; set; }
    //    [XmlAttribute]
    //    public int StartNb { get; set; }
    //    [XmlAttribute]
    //    public int EndNb { get; set; }

    //    // elements
    //    public List<Hang> Hang { get; set; }
    //}


    public class Impact
    {
        // attributs
        [XmlAttribute]
        public int ImpactID { get; set; }

        public StopPoint StopPoint { get; set; }
        public ImpactState ImpactState { get; set; }
        public ImpactStartDate ImpactStartDate { get; set; }
        public ImpactEndDate ImpactEndDate { get; set; }
        public ImpactCloseDate ImpactCloseDate { get; set; }
        public ImpactDuration ImpactDuration { get; set; }
        public ImpactMessage ImpactMessage { get; set; }
        public Event Event { get; set; }
    }


    //public class ImpactList
    //{
    //    // attributs
    //    [XmlAttribute]
    //    public int ImpactCount { get; set; }

    //    // elements
    //    public List<Impact> Impact { get; set; }
    //}



    //public class ImpactPosList
    //{
    //    // attributs
    //    [XmlAttribute]
    //    public int ImpactPosCount { get; set; }
    //}


    public class ImpactState
    {
        public String Value { get; set; }
    }


    public class ImpactStartDate
    {
        public String Value { get; set; }
    }


    public class ImpactEndDate
    {
        public String Value { get; set; }
    }


    public class ImpactCloseDate
    {
        public String Value { get; set; }
    }


    public class ImpactDuration
    {
        public int Value { get; set; }
    }


    public class ImpactMessage
    {
        public String Value { get; set; }
    }



    public class IsTransday
    {
        // attributs
        public int Value { get; set; }
    }


    public class JourneyResult
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
        public Summary Summary { get; set; }
        public List<Section> Section { get; set; }
    }


    //public class JourneyResultList
    //{
    //    [XmlAttribute]
    //    public int JourneyResultCount { get; set; }

    //    public List<JourneyResult> JourneyResult { get; set; }
    //}


    public class Line
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
        public Mode Mode { get; set; }
        public Network Network { get; set; }
        public Forward Forward { get; set; }
        public Backward Backward { get; set; }

        //[XmlArray("List")]
        //[XmlArrayItem("", typeof())]
        //public EventPos[] EventPosList { get; set; }
    }


    //public class LineList
    //{
    //    // attributs
    //    [XmlAttribute]
    //    public int LineCount { get; set; }

    //    // elements
    //    [XmlArray("LineList")]
    //    [XmlArrayItem("Line", typeof(Line))]
    //    public Line[] Line { get; set; }
    //}


    //public class LineStopAreaList
    //{
    //    // attributs
    //    [XmlAttribute]
    //    public int StopAreaCount { get; set; }

    //    // elements
    //    public List<StopArea> StopArea { get; set; }
    //}


    public class Mode
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


    public class ModeType
    {
        public String Value { get; set; }
    }


    //public class ModeList
    //{
    //    [XmlAttribute]
    //    public int ModeCount { get; set; }

    //    public List<Mode> Mode { get; set; }
    //}


    public class Network
    {
        //[XmlAttribute]
        //public int NetworkIdx { get; set; }
        //[XmlAttribute]
        //public int NetworkId { get; set; }
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


    public class NetworkList
    {
        // attributs
        //[XmlAttribute]
        //public int NetworkCount { get; set; }
        [XmlAttribute]
        public string Region { get; set; }

        // listes d'elements
        [XmlElement("Network")]
        public Network[] Network { get; set; }

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


    //public class NextDepartureList
    //{
    //    // attributs
    //    [XmlAttribute]
    //    public int StopCount { get; set; }
    //}


    public class Nota
    {
        // attributs
        [XmlAttribute]
        public String NotaType { get; set; }
    }


    public class ODT
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
        public Network Netowrk { get; set; }
    }


    //public class ODTList
    //{
    //    // attributs
    //    [XmlAttribute]
    //    public int ODTCount { get; set; }

    //    // elements
    //    public List<ODT> ODT { get; set; }
    //}


    public class PagerInfo
    {
        // attributs
        [XmlAttribute]
        public int ResponseCount { get; set; }
        [XmlAttribute]
        public int ResponseStartIndex { get; set; }
        [XmlAttribute]
        public int TotalCount { get; set; }
    }


    public class Proximity
    {
        // attributs
        [XmlAttribute]
        public int Distance { get; set; }

        public StopArea StopArea { get; set; }
        public StopPoint StopPoint { get; set; }
    }


    //public class ProximityList
    //{
    //    // attributs
    //    [XmlAttribute]
    //    public int ProximityCount { get; set; }

    //    public List<Proximity> Proximity { get; set; }
    //}


    public class Route
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

        public Line Line { get; set; }
    }


    //public class RouteList
    //{
    //    [XmlAttribute]
    //    public int RouteCount { get; set; }

    //    public List<Route> Route { get; set; }
    //}


    public class Section
    {
        // attributs
        [XmlAttribute]
        public String Type { get; set; }

        // elements
        public Duration Duration { get; set; }
        public Departure Departure { get; set; }
        public Arrival Arrival { get; set; }
        public VehicleJourney VehicleJourney { get; set; }
        public Nota Nota { get; set; }
        [XmlArray("FareSectionList")]
        [XmlArrayItem("FareSection", typeof(FareSection))]
        public FareSection[] FareSectionList { get; set; }
        [XmlArray("FareZoneList")]
        [XmlArrayItem("FareZone", typeof(FareZone))]
        public FareZone[] FareZoneList { get; set; }
    }


    public class Segment
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
        public Address Address { get; set; }
        public StartNode StartNode { get; set; }
        public EndNode EndNode { get; set; }
    }


    //public class SegmentList
    //{
    //    [XmlAttribute]
    //    public int SegmentCount { get; set; }

    //    public List<Segment> Segment { get; set; }
    //}


    public class Site
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
        public City City { get; set; }
        public SiteType SiteType { get; set; }
        public Coord Coord { get; set; }
        public SiteAddress SiteAddress { get; set; }
        public Comment Comment { get; set; }
    }


    public class SiteType
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


    //public class SiteTypeList
    //{
    //    // attributs
    //    [XmlAttribute]
    //    public int SiteTypeCount { get; set; }

    //    // elements
    //    public List<SiteType> SiteType { get; set; }
    //}


    public class SiteAddress
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


    public class StartNode
    {
        // elements
        public Coord Coord { get; set; }
    }


    public class Stop
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

        public StopTime StopTime { get; set; }
        public StopArrivalTime StopArrivalTime { get; set; }
        public IsTransday IsTransday { get; set; }
        public StopPoint StopPoint { get; set; }
        public DepartureTime DepartureTime { get; set; }
        public Route Route { get; set; }
        public StopArea StopArea { get; set; }

        public Time getStopTime()
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


    public class StopArea
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
        public City City { get; set; }
        public Coord Coord { get; set; }
        [XmlArray("HangList")]
        [XmlArrayItem("Hang", typeof(Hang))]
        public Hang[] HangList { get; set; }
        [XmlArray("ModeList")]
        [XmlArrayItem("Mode", typeof(Mode))]
        public Mode[] ModeList { get; set; }
    }


    //public class StopAreaList
    //{
    //    // attributs
    //    [XmlAttribute]
    //    public int StopAreaCount { get; set; }

    //    // elements
    //    public List<StopArea> StopArea { get; set; }
    //}


    //public class StopList
    //{
    //    [XmlAttribute]
    //    public int StopCount { get; set; }
    //    [XmlAttribute]
    //    public String Nota { get; set; }

    //    public StopTime StopTime { get; set; }
    //    public Route Route { get; set; }
    //    public List<Stop> Stop { get; set; }
    //}


    public class StopPoint
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
        public StopPointAddress StopPointAddress { get; set; }
        public Equipment Equipment { get; set; }
        public Mode Mode { get; set; }
        public City City { get; set; }
        public StopArea StopArea { get; set; }
        public Coord Coord { get; set; }
        //[XmlArray("List")]
        //[XmlArrayItem("", typeof())]
        //public ImpactPos[] ImpactPosList { get; set; }
    }


    public class StopPointAddress
    {
        // attributs
        [XmlAttribute]
        public String StopPointAddressTypeName { get; set; }
        [XmlAttribute]
        public String StopPointAddressNumber { get; set; }
        [XmlAttribute]
        public String StopPointAddressName { get; set; }
    }


    public class StopPointEquipment
    {
        public String Value { get; set; }
    }


    //public class StopPointList
    //{

    //    [XmlAttribute]
    //    public int StopPointCount { get; set; }

    //    public List<StopPoint> StopPoint { get; set; }
    //}


    public class Summary
    {
        // attributs
        [XmlAttribute]
        public int Interchange { get; set; }

        // elements
        public DepartureDate DepartureDate { get; set; }
        public DepartureTime DepartureTime { get; set; }
        public ArrivalDate ArrivalDate { get; set; }
        public ArrivalTime ArrivalTime { get; set; }
        public Duration Duration { get; set; }
        public TotalLinkTime TotalLinkTime { get; set; }
        public TotalWaitTime TotalWaitTime { get; set; }
        public Call Call { get; set; }
    }


    public class This
    {
        // elements simples
        public int Sens { get; set; }
        public int Year { get; set; }
        public int Month { get; set; }
        public int Day { get; set; }
        public int Hour { get; set; }
        public int Minute { get; set; }
        public int Criteria { get; set; }
    }


    public class ValidityPattern
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


    public class ValidityPatternSet
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

        //[XmlArray("List")]
        //[XmlArrayItem("", typeof())]
        //public ValidityPatternSetDetail[] ValidityPatternSetDetailList { get; set; }
    }


    //public class ValidityPatternSetDetailList
    //{
    //    [XmlAttribute]
    //    public int VPatternSetDetailCount { get; set; }
    //}


    public class Vehicle
    {

        [XmlAttribute]
        public int VehicleIdx { get; set; }
        [XmlAttribute]
        public int VehicleId { get; set; }
        //[XmlAttribute]
        //public bool BikeAccepted { get; set; }
        //[XmlAttribute]
        //public bool MIPAccess { get; set; }
        //[XmlAttribute]
        //public bool AirConditioned { get; set; }
        [XmlAttribute]
        public String VehicleExternalCode { get; set; }
        [XmlAttribute]
        public String VehicleName { get; set; }

        public String Value { get; set; }
    }


    public class VehicleJourney
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
        public Route Route { get; set; }
        public Destination Destination { get; set; }
        public Mode Mode { get; set; }
        public Company Company { get; set; }
        public Vehicle Vehicle { get; set; }
        public ValidityPattern ValidityPattern { get; set; }
        //[XmlArray("List")]
        //[XmlArrayItem("", typeof())]
        //public EventPos[] EventPosList { get; set; }
    }


    //public class VehicleList
    //{

    //    [XmlAttribute]
    //    public int VehicleCount { get; set; }

    //    public List<Vehicle> Vehicle { get; set; }
    //}

    // extensions

    public class Server
    {
        // attributs
        //[XmlAttribute]
        //public String ServerName { get; set; }
        //[XmlAttribute]
        //public String ServerExternalCode { get; set; }
        [XmlAttribute]
        public String Region { get; set; }

        // elements
        public NetworkList NetworkList { get; set; }
    }



    //public class ActionAddressList
    //{

    //}

    //public class ActionAddressTypeList
    //{

    //}

    //public class ActionBookingTypeList
    //{

    //}

    //public class ActionCityList
    //{

    //}

    //public class ActionCommonLetterList
    //{

    //}

    //public class ActionCompanyList
    //{

    //}

    //public class ActionDepartmentList
    //{

    //}

    //public class ActionDirectStopAreaList
    //{

    //}

    //public class ActionDistrictList
    //{

    //}

    //public class ActionFirstLetterList
    //{

    //}

    //public class ActionFreqSettingList
    //{

    //}

    public class ActionGetEvent
    {

    }

    //public class ActionGetEventList
    //{

    //}

    public class ActionGetImpact
    {

    }

    //public class ActionGetMediaList
    //{

    //}

    public class ActionGetMessage
    {

    }

    //public class ActionIsochronList
    //{

    //}

    public class ActionLineRouteDescription
    {

    }

    //public class ActionNextArrivalList
    //{

    //}

    //public class ActionOnBoardServiceList
    //{

    //}

    //public class ActionPhoneticList
    //{

    //}

    //public class ActionRouteList
    //{

    //}
    //public class ActionSiteList
    //{

    //}
    //public class ActionStopPointList
    //{

    //}
    //public class ActionVehicleJourneyDetailList
    //{

    //}
    //public class ActionVehicleJourneyList
    //{

    //}
    //public class AddressList { }
    //public class AddressTypeList { }
    public class BookingType { }
    //public class BookingTypeList { }
    //public class CityList { }
    //public class CommonLetterList { }
    //public class CompanyList { }
    public class DLL { }
    public class Database { }
    public class Department { }
    //public class DepartmentList { }
    //public class DirectStopAreaList { }
    public class District { }
    //public class DistrictList { }
    public class FreqSetting { }
    //public class FreqSettingList { }
    public class FreqStop { }
    //public class FreqStopList { }
    //public class IsochronList { }
    //public class LineScheduleList { }
    public class Media { }
    //public class MediaList { }
    public class Message { }
    //public class ModeTypeList { }
    public class MsgCategory { }
    //public class NextArrivalList { }
    public class OnBoardService { }
    //public class OnBoardServiceList { }
    public class Origin { }
    //public class PhoneticList { }
    public class RoutePoint { }
    //public class RoutePointList { }
    //public class SiteList { }
    public class Thread { }
    public class VPTranslation { }
    //public class VehicleJourneyList { }
}
