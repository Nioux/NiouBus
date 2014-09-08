﻿using System;

namespace NiouBusEngine
{
    public class LigneViewModel : BaseViewModel, IComparable
    {
        private string _Nom;
        public string Nom { get { return _Nom; } set { SetProperty(ref _Nom, value); } }

        private string _Vers;
        public string Vers { get { return _Vers; } set { SetProperty(ref _Vers, value); } }

        private string _Code;
        public string Code { get { return _Code; } set { SetProperty(ref _Code, value); } }

        private string _Sens;
        public string Sens { get { return _Sens; } set { SetProperty(ref _Sens, value); } }

        private string _Couleur;
        public string Couleur { get { return _Couleur; } set { SetProperty(ref _Couleur, value); } }

        public int CompareTo(object obj)
        {
            return Nom.CompareTo((obj as LigneViewModel).Nom);
        }
    }
}