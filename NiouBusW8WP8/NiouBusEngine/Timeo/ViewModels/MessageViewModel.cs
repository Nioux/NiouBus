using System;
using System.ComponentModel;

namespace NiouBusEngine
{
    public class MessageViewModel : BaseViewModel
    {
        private string _Titre;
        public string Titre { get { return _Titre; } set { SetProperty(ref _Titre, value); } }

        private string _Texte;
        public string Texte { get { return _Texte; } set { SetProperty(ref _Texte, value); } }     }
}
