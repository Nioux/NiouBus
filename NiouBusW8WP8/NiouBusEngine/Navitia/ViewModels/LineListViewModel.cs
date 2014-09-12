using System;
using System.Collections.ObjectModel;
using System.Threading.Tasks;

namespace NiouBusEngine
{
    public class LineListViewModel : BaseViewModel
    {
        public LineListViewModel()
        {
            this.Items = new ObservableCollection<LineViewModel>();
        }

        public ObservableCollection<LineViewModel> Items { get; private set; }

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

        public async Task<bool> LoadDataAsync(bool refresh)
        {
            // Exemple de données ; remplacer par des données réelles
            this.IsDataLoading = true;

            var xd = await Navitia.NavitiaTools.GetAsync<Navitia.ActionLineList>(
                Server: Server,
                Action: Navitia.Action.LineList,
                NetworkExternalCode: Network,
                StopAreaExternalCode: StopAreaExternalCode);
            if (xd == null)
            {
                this.IsDataLoading = false;
                return false;
            }

            this.Items.Clear();
            foreach (var line in xd.Line)
            {
                if (!String.IsNullOrWhiteSpace(line.Forward.ForwardName))
                {
                    this.Items.Add(new LineViewModel()
                    {
                        LineExternalCode = line.LineExternalCode,
                        LineCode = line.LineCode,
                        LineName = line.LineName,
                        Direction = "1",
                        DirectionName = line.Forward.ForwardName,
                        Coord = line.Forward.Direction.StopArea.Coord,
                    });
                }
                if (!String.IsNullOrWhiteSpace(line.Backward.BackwardName))
                {
                    this.Items.Add(new LineViewModel()
                    {
                        LineExternalCode = line.LineExternalCode,
                        LineCode = line.LineCode,
                        LineName = line.LineName,
                        Direction = "-1",
                        DirectionName = line.Backward.BackwardName,
                        Coord = line.Backward.Direction.StopArea.Coord,
                    });
                }
            }
            this.Items.BubbleSort();
                        
            this.IsDataLoading = false;

            return true;
        }
    }
}
