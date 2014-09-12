using System;
using System.Collections.ObjectModel;
using System.Threading.Tasks;

namespace NiouBusEngine
{
    public class DepartureBoardListViewModel : BaseViewModel
    {
        public DepartureBoardListViewModel()
        {
            this.Items = new ObservableCollection<StopViewModel>();
            this._Date = DateTime.Now;
        }

        public ObservableCollection<StopViewModel> Items { get; private set; }

        private DateTime _Date;
        public DateTime Date
        {
            get
            {
                return _Date;
            }
            set
            {
                Set(ref _Date, value);
            }
        }

        private string _Server;
        public string Server
        {
            get
            {
                return _Server;
            }
            set
            {
                Set(ref _Server, value);
            }
        }

        private string _Network;
        public string Network
        {
            get
            {
                return _Network;
            }
            set
            {
                Set(ref _Network, value);
            }
        }

        private string _Nota;
        public string Nota
        {
            get
            {
                return _Nota;
            }
            set
            {
                Set(ref _Nota, value);
            }
        }

        private String _LineExternalCode;
        public String LineExternalCode
        {
            get
            {
                return _LineExternalCode;
            }
            set
            {
                Set(ref _LineExternalCode, value);
            }
        }

        private String _Direction;
        public String Direction
        {
            get
            {
                return _Direction;
            }
            set
            {
                Set(ref _Direction, value);
            }
        }

        private String _StopAreaExternalCode;
        public String StopAreaExternalCode
        {
            get
            {
                return _StopAreaExternalCode;
            }
            set
            {
                Set(ref _StopAreaExternalCode, value);
            }
        }

        private bool _IsDataLoading = true;
        public bool IsDataLoading
        {
            get
            {
                return _IsDataLoading;
            }
            private set 
            {
                Set(ref _IsDataLoading, value);
            }
        }

        public async Task LoadDataAsync(bool refresh)
        {
            this.IsDataLoading = true;

            var now = Date;

            var xd = await Navitia.NavitiaTools.GetAsync<Navitia.DepartureBoardList>(
                Server: Server,
                Action: Navitia.Action.DepartureBoard,
                NetworkExternalCode: Network,
                LineExternalCode: LineExternalCode,
                Sens: Direction,
                StopAreaExternalCode: StopAreaExternalCode,
                Date: now,
                DateChangeTime: new TimeSpan(4, 0, 0));
            if(xd == null)
            {
                this.IsDataLoading = false;
                return;
            }

            this.Items.Clear();
            if (xd.StopList != null && xd.StopList.Stop != null)
            {
                foreach (var stop in xd.StopList.Stop)
                {
                    this.Items.Add(new StopViewModel()
                    {
                        Hour = stop.StopOrDepartureTime.Hour,
                        Minute = stop.StopOrDepartureTime.Minute,
                        RouteName = stop.Route.RouteName.ToLower(),
                    });
                }
            }

            if(xd.StopList != null && !string.IsNullOrEmpty(xd.StopList.Nota))
            {
                Nota = xd.StopList.Nota;
            }
            else
            {
                Nota = null;
            }

            Navitia.Line domline = null;
            if (xd.LineList != null && xd.LineList.Count > 0)
            {
                domline = xd.LineList[0];
            }

            Navitia.StopPoint sl = null;
            if (xd.StopPointList != null && xd.StopPointList.StopPoint.Count > 0)
            {
                sl = xd.StopPointList.StopPoint[0];
            }
            if (sl != null)
            {
                StopPointName = sl.StopPointName.ToLower();
            }
            if (domline != null)
            {
                if (Direction.Equals("1"))
                {
                    if (domline.Forward != null)
                    {
                        DirectionName = domline.Forward.ForwardName.ToLower();
                    }
                }
                else
                {
                    if (domline.Backward != null)
                    {
                        DirectionName = domline.Backward.BackwardName.ToLower();
                    }
                }
            }

            if (xd.LineList != null && xd.LineList.Count > 0)
            {
                LineName = xd.LineList[0].LineCode.ToLower();
            }
                        
            this.IsDataLoading = false;
        }


        private String _DirectionName;
        public String DirectionName
        {
            get
            {
                return _DirectionName;
            }
            set
            {
                Set(ref _DirectionName, value);
            }
        }

        private String _StopPointName;
        public String StopPointName
        {
            get
            {
                return _StopPointName;
            }
            set
            {
                Set(ref _StopPointName, value);
            }
        }

        private String _LineName;
        public String LineName
        {
            get
            {
                return _LineName;
            }
            set
            {
                Set(ref _LineName, value);
            }
        }
    }
}
