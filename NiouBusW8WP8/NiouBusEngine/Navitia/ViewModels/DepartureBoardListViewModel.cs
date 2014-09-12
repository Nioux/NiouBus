using System;
using System.Threading.Tasks;

namespace NiouBusEngine
{
    public class DepartureBoardListViewModel : BaseViewModel
    {
        public DepartureBoardListViewModel()
        {
            this._Date = DateTime.Now;
        }

        private DateTime _Date;
        public DateTime Date { get { return _Date; } set { Set(ref _Date, value); } }

        private string _Server;
        public string Server { get { return _Server; } set { Set(ref _Server, value); } }

        private string _Network;
        public string Network { get { return _Network; } set { Set(ref _Network, value); } }

        private String _LineExternalCode;
        public String LineExternalCode { get { return _LineExternalCode; } set { Set(ref _LineExternalCode, value); } }

        private String _Direction;
        public String Direction { get { return _Direction; } set { Set(ref _Direction, value); } }

        private String _StopAreaExternalCode;
        public String StopAreaExternalCode { get { return _StopAreaExternalCode; } set { Set(ref _StopAreaExternalCode, value); } }

        private Navitia.DepartureBoardList _DepartureBoardList;
        public Navitia.DepartureBoardList DepartureBoardList { get { return _DepartureBoardList; } set { Set(ref _DepartureBoardList, value); } }

        public async Task LoadDataAsync()
        {
            DepartureBoardList = await Navitia.NavitiaTools.GetAsync<Navitia.DepartureBoardList>(
                Server: Server,
                Action: Navitia.Action.DepartureBoard,
                NetworkExternalCode: Network,
                LineExternalCode: LineExternalCode,
                Sens: Direction,
                StopAreaExternalCode: StopAreaExternalCode,
                Date: Date,
                DateChangeTime: new TimeSpan(4, 0, 0));
        }


        public String DirectionName
        {
            get
            {
                if (DepartureBoardList != null && DepartureBoardList.RouteList != null && DepartureBoardList.RouteList.Count > 0 && DepartureBoardList.RouteList[0].RouteName != null)
                {
                    return DepartureBoardList.RouteList[0].RouteName.ToLower();
                }
                return null;
            }
        }

        public String StopPointName
        {
            get
            {
                if (DepartureBoardList != null && DepartureBoardList.StopPointList != null && DepartureBoardList.StopPointList.Count > 0 && DepartureBoardList.StopPointList[0].StopPointName != null)
                {
                    return DepartureBoardList.StopPointList[0].StopPointName.ToLower();
                }
                return null;
            }
        }

        public String LineName
        {
            get
            {
                if (DepartureBoardList != null && DepartureBoardList.LineList != null && DepartureBoardList.LineList.Count > 0 && DepartureBoardList.LineList[0].LineCode != null)
                {
                    return DepartureBoardList.LineList[0].LineCode.ToLower();
                }
                return null;
            }
        }
    }
}
