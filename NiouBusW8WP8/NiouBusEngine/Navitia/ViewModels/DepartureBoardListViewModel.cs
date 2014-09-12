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
                SetProperty(ref _Date, value);
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
                SetProperty(ref _Server, value);
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
                SetProperty(ref _Network, value);
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
                SetProperty(ref _Nota, value);
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
                SetProperty(ref _LineExternalCode, value);
                //_LineExternalCode = value;
                //NotifyPropertyChanged("LineExternalCode");
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
                SetProperty(ref _Direction, value);
                //_Direction = value;
                //NotifyPropertyChanged("Direction");
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
                SetProperty(ref _StopAreaExternalCode, value);
                //_StopAreaExternalCode = value;
                //NotifyPropertyChanged("StopAreaExternalCode");
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
                SetProperty(ref _IsDataLoading, value);
                //_IsDataLoading = value;
                //NotifyPropertyChanged("IsDataLoading");
            }
        }

        public async Task LoadDataAsync(bool refresh)
        {
            // Exemple de données ; remplacer par des données réelles
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
                        Hour = stop.StopTime != null ? stop.StopTime.Hour : stop.DepartureTime.Hour,
                        Minute = stop.StopTime != null ? stop.StopTime.Minute : stop.DepartureTime.Minute,
                        RouteName = stop.Route.RouteName.ToLower(),
                        //LineExternalCode = line.LineExternalCode,
                        //LineCode = line.LineCode.ToLower(),
                        //LineName = line.LineName.ToLower(),
                        //Direction = "1",
                        //DirectionName = line.Forward.ForwardName.ToLower(),
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
            if (xd.StopPointList != null && xd.StopPointList.Count > 0)
            {
                sl = xd.StopPointList[0];
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
                SetProperty(ref _DirectionName, value);
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
                SetProperty(ref _StopPointName, value);
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
                SetProperty(ref _LineName, value);
            }
        }
    }
}
