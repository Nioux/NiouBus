using NiouBusEngine.Timeo;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Threading.Tasks;

namespace NiouBusEngine
{
    public class HorairesViewModel : BaseViewModel
    {
        public HorairesViewModel()
        {
            this.Items = new ObservableCollection<HoraireViewModel>();
            this.Messages = new ObservableCollection<MessageViewModel>();
        }

        public string Server { get; set; }
        public string Key { get; set; }
        public string Ville { get; set; }

        public ObservableCollection<HoraireViewModel> Items { get; private set; }

        private string _Refs = null;
        public string Refs { get { return _Refs; } set { SetProperty(ref _Refs, value); } } 
        
        private string _Arret = null;
        public string Arret { get { return _Arret; } set { SetProperty(ref _Arret, value); } } 
        
        private string _LigneNom = null;
        public string LigneNom { get { return _LigneNom; } set { SetProperty(ref _LigneNom, value); } } 
        
        private string _Vers = null;
        public string Vers { get { return _Vers; } set { SetProperty(ref _Vers, value); } } 
        
        private string _Code = null;
        public string Code { get { return _Code; } set { SetProperty(ref _Code, value); } } 
        
        private string _Ligne = null;
        public string Ligne { get { return _Ligne; } set { SetProperty(ref _Ligne, value); } } 
        
        private string _Sens = null;
        public string Sens { get { return _Sens; } set { SetProperty(ref _Sens, value); } } 

        public ObservableCollection<MessageViewModel> Messages { get; private set; }

        private bool _IsDataLoading = true;
        public bool IsDataLoading { get { return _IsDataLoading; } private set { SetProperty(ref _IsDataLoading, value); NotifyPropertyChanged("IsDataLoaded"); } } 
        public bool IsDataLoaded { get { return !IsDataLoading; } private set { IsDataLoading = !value; } } 

        private string _Heure = null;
        public string Heure { get { return _Heure; } set { SetProperty(ref _Heure, value); } } 

        public async Task<bool> LoadDataAsync(bool refresh)
        {
            this.IsDataLoading = true;
            var xds = await TimeoTools.GetPassagesAsync(Server, Key, Ville, Refs, refresh);
            if(xds == null || xds.Count == 0)
            {
                this.IsDataLoading = false;
                return false;
            }
            var xd = xds[0];

            var description = xd.horaire[0].description;
            this.Arret = description.arret;
            this.LigneNom = description.ligne_nom;
            this.Vers = description.vers;

            this.Code = description.code;
            this.Ligne = description.ligne;
            this.Sens = description.sens;

            this.Heure = xd.heure;

            this.Items.Clear();
            foreach (var passage in xd.horaire[0].passage)
            {
                this.Items.Add(new HoraireViewModel() 
                { 
                    Destination = passage.destination, 
                    Duree = passage.duree 
                });
            }

            this.Messages.Clear();
            foreach (var message in xd.horaire[0].message)
            {
                this.Messages.Add(new MessageViewModel() 
                { 
                    Titre = message.titre, 
                    Texte = message.texte 
                });
            }
            var reseau = xd.reseau;
            this.Messages.Add(new MessageViewModel() 
            { 
                Titre = reseau.titre, 
                Texte = reseau.texte 
            });

            this.IsDataLoading = false;

            return true;
        }
    }
}