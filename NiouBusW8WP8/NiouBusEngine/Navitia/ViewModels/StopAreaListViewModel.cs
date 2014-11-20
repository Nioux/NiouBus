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

            var xd = await Navitia.NavitiaTools.GetAsync<Navitia.ActionStopAreaList>(
                Server: Server,
                Action: Navitia.Action.StopAreaList,
                NetworkExternalCode: Network,
                LineExternalCode: LineExternalCode,
                StopAreaExternalCode: StopAreaExternalCode);
            if (xd == null)
            {
                this.IsDataLoading = false;
                return;
            }

            this.Items.Clear();
            foreach (var stoparea in xd.StopAreaList.StopArea)
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
