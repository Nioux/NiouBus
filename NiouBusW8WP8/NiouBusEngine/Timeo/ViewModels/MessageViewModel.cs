using System;
using System.ComponentModel;

namespace NiouBusEngine
{
    public class MessageViewModel : BaseViewModel
    {
        private string _Titre;
        public string Titre { get { return _Titre; } set { Set(ref _Titre, value); } }

        private string _Texte;
        public string Texte { get { return _Texte; } set { Set(ref _Texte, value); } }     }
}
