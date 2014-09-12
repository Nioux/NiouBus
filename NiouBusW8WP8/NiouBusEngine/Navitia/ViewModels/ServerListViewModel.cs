using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NiouBusEngine
{
    public class ServerListViewModel : BaseViewModel
    {
        private ObservableCollection<NetworkListViewModel> _Networks = new ObservableCollection<NetworkListViewModel>();
        public ObservableCollection<NetworkListViewModel> Networks { get { return _Networks; } set { SetProperty(ref _Networks, value); } }

        public string _Search = string.Empty;
        public string Search 
        { 
            get { return _Search; } 
            set 
            { 
                SetProperty(ref _Search, value); 
                NotifyPropertyChanged("FilteredNetworks"); 
            } 
        }

        private bool _HeaderEnabled = true;
        public bool HeaderEnabled
        {
            get { return _HeaderEnabled; }
            set
            {
                SetProperty(ref _HeaderEnabled, value);
            }
        }
        public List<NetworkListViewModel> FilteredNetworks 
        { 
            get 
            {
                Func<NetworkViewModel, bool> isSearched = (NetworkViewModel net) => { return net.Server.Contains(Search) || net.NetworkExternalCode.Contains(Search); };
                List<NetworkListViewModel> fnetworklists = new List<NetworkListViewModel>();
                foreach(var networks in Networks)
                {
                    NetworkListViewModel fnetworklist = new NetworkListViewModel();
                    fnetworklist.Region = networks.Region;
                    foreach (var network in networks)
                    {
                        if(network.NetworkName.Contains(Search))
                        {
                            fnetworklist.Add(network);
                        }
                    }
                    if (fnetworklist.Count > 0)
                    {
                        fnetworklists.Add(fnetworklist);
                    }
                }
                //foreach()
                //var networks = Networks.Where(nets => nets.Contains(isSearched));
                return fnetworklists; 
            } 
        }

        public void Load(Stream stream)
        {
            Networks.Clear();
            var servers = stream.XmlDeserializeTo<Navitia.ServerList>();
            if (servers != null && servers.NetworkList != null)
            {
                foreach (var networklist in servers.NetworkList)
                {
                    var networklistVM = new NetworkListViewModel();
                    networklistVM.Region = networklist.Region.ToLower();
                    foreach (var network in networklist.Network)
                    {
                        var networkVM = new NetworkViewModel();
                        networkVM.NetworkName = network.NetworkName.ToLower();
                        networkVM.NetworkAdditionalData = network.NetworkAdditionalData != null ? network.NetworkAdditionalData.ToLower() : null;
                        networkVM.NetworkExternalCode = network.NetworkExternalCode;
                        networkVM.Server = network.Server;
                        networkVM.TimeoKey = network.TimeoKey;
                        networkVM.TimeoVille = network.TimeoVille;
                        networklistVM.Add(networkVM);
                    }
                    Networks.Add(networklistVM);
                }
            }
        }
    }
}
