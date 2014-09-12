using NiouBusEngine.Timeo;
using System;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Threading.Tasks;

namespace NiouBusEngine
{
    public class ArretsViewModel : BaseViewModel
    {
        public ArretsViewModel()
        {
            //this.Items = new ObservableCollection<ArretViewModel>();
        }

        public string Server { get; set; }
        public string Key { get; set; }
        public string Ville { get; set; }

        //public ObservableCollection<ArretViewModel> Items { get; private set; }

        private string _Ligne = null;
        public string Ligne { get { return _Ligne; } set { Set(ref _Ligne, value); } }

        private string _Sens = null;
        public string Sens { get { return _Sens; } set { Set(ref _Sens, value); } }

        private string _Arret = null;
        public string Arret { get { return _Arret; } set { Set(ref _Arret, value); } } 
        
        private bool _IsDataLoading = true;
        public bool IsDataLoading { get { return _IsDataLoading; } private set { Set(ref _IsDataLoading, value); } }

        private Timeo.xmldata _xmldata = null;
        public Timeo.xmldata xmldata { get { return _xmldata; } set { Set(ref _xmldata, value); } }
        
        public async Task LoadDataAsync(bool refresh)
        {
            this.IsDataLoading = true;
            var xd = await TimeoTools.GetArretsAsync(Server, Key, Ville, Sens, Ligne, Arret, refresh);
            if (xd == null)
            {
                this.IsDataLoading = false;
                return;
            }

            xmldata = xd;

            this.IsDataLoading = false;
        }

    }
}