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
        public string Ligne { get { return _Ligne; } set { SetProperty(ref _Ligne, value); } }

        private string _Sens = null;
        public string Sens { get { return _Sens; } set { SetProperty(ref _Sens, value); } }

        private string _Arret = null;
        public string Arret { get { return _Arret; } set { SetProperty(ref _Arret, value); } } 
        
        private bool _IsDataLoading = true;
        public bool IsDataLoading { get { return _IsDataLoading; } private set { SetProperty(ref _IsDataLoading, value); } }

        private Timeo.xmldata _xmldata = null;
        public Timeo.xmldata xmldata { get { return _xmldata; } set { SetProperty(ref _xmldata, value); } }
        
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

            //Ligne = xd.als[0].ligne.nom;
            //Sens = xd.als[0].ligne.vers;

            //this.Items.Clear();
            //foreach (var als in xd.als)
            //{
            //    this.Items.Add(new ArretViewModel() 
            //    { 
            //        Arret = als.arret.nom, 
            //        Ligne = als.ligne.nom,
            //        Vers = als.ligne.vers, 
            //        Refs = als.refs 
            //    });
            //}
                        
            this.IsDataLoading = false;
        }

    }
}