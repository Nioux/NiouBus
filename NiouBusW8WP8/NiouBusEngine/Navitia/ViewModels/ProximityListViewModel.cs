using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NiouBusEngine
{
    public class ProximityListViewModel : BaseViewModel
    {
        public ProximityListViewModel()
        {
            this.Items = new ObservableCollection<ProximityViewModel>();
        }

        public ObservableCollection<ProximityViewModel> Items { get; private set; }

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

        private Navitia.Coord _Coord;
        public Navitia.Coord Coord
        {
            get
            {
                return _Coord;
            }
            set
            {
                SetProperty(ref _Coord, value);
                //_Coord = value;
                //NotifyPropertyChanged("Coord");
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

            var xd = await Navitia.NavitiaTools.GetAsync<Navitia.ActionProximityList>(
                Server: Server,
                Action: "ProximityList",
                NetworkExternalCode: Network,
                Type: "StopArea",
                X: Coord.CoordXString,
                Y: Coord.CoordYString);
            if (xd == null)
            {
                this.IsDataLoading = false;
                return false;
            }

            this.Items.Clear();
            foreach (var proximity in xd.ProximityList)
            {
                this.Items.Add(new ProximityViewModel()
                {
                    Coord = proximity.StopArea.Coord,
                    StopAreaExternalCode = proximity.StopArea.StopAreaExternalCode,
                    StopAreaName = proximity.StopArea.StopAreaName.ToLower(),

                    //LineExternalCode = line.LineExternalCode,
                    //LineCode = line.LineCode.ToLower(),
                    //LineName = line.LineName.ToLower(),
                    //Direction = "1",
                    //DirectionName = line.Forward.ForwardName.ToLower(),
                    //Coord = line.Forward.Direction.StopArea.Coord,
                });
            }
                        
            this.IsDataLoading = false;

            return true;
        }
    }
}
