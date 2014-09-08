﻿using System;
using System.ComponentModel;

namespace NiouBusEngine
{
    public class ArretViewModel : BaseViewModel
    {
        private string _Arret;
        public string Arret { get { return _Arret; } set { SetProperty(ref _Arret, value); } } 
        
        private string _Ligne;
        public string Ligne { get { return _Ligne; } set { SetProperty(ref _Ligne, value); } } 
        
        private string _Vers;
        public string Vers { get { return _Vers; } set { SetProperty(ref _Vers, value); } } 
        
        private string _Refs;
        public string Refs { get { return _Refs; } set { SetProperty(ref _Refs, value); } }     
    }
}