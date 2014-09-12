using System;
using System.Collections.ObjectModel;
using System.Threading.Tasks;

namespace NiouBusEngine
{
    public class StopAreaListViewModel : BaseViewModel
    {
        public StopAreaListViewModel()
        {
            this.Items = new ObservableCollection<StopAreaViewModel>();
        }

        public ObservableCollection<StopAreaViewModel> Items { get; private set; }

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

            var xd = await Navitia.NavitiaTools.GetAsync<Navitia.ActionStopAreaList>(
                Server: Server,
                Action: Navitia.Action.StopAreaList,
                NetworkExternalCode: Network,
                LineExternalCode: LineExternalCode);
            if (xd == null)
            {
                this.IsDataLoading = false;
                return;
            }

            this.Items.Clear();
            foreach (var stoparea in xd.StopArea)
            {
                this.Items.Add(new StopAreaViewModel()
                {
                    StopAreaExternalCode = stoparea.StopAreaExternalCode,
                    StopAreaName = stoparea.StopAreaName.ToLower(),
                    CityName = stoparea.City.CityName.ToLower(),
                });
            }
                        
            this.IsDataLoading = false;
        }
    }
}
