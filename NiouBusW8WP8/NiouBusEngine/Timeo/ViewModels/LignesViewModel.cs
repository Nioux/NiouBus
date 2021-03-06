﻿using System;
using System.Linq;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Collections;
using NiouBusEngine.Timeo;
using System.Threading.Tasks;

namespace NiouBusEngine
{
    public class LignesViewModel : BaseViewModel
    {
        public LignesViewModel()
        {
            this.Items = new ObservableCollection<LigneViewModel>();
        }

        public string Server { get; set; }
        public string Key { get; set; }
        public string Ville { get; set; }

        private ObservableCollection<LigneViewModel> _Items;
        public ObservableCollection<LigneViewModel> Items { get { return _Items; } private set { Set(ref _Items, value); } }

        private bool _IsDataLoading = true;
        public bool IsDataLoading { get { return _IsDataLoading; } private set  { Set(ref _IsDataLoading, value); } }

        private Timeo.xmldata _xmldata = null;
        public Timeo.xmldata xmldata { get { return _xmldata; } set { Set(ref _xmldata, value); } }

        public async Task LoadDataAsync(bool refresh)
        {
            this.IsDataLoading = true;
            var xd = await TimeoTools.GetLignesAsync(Server, Key, Ville, refresh);
            if(xd == null)
            {
                this.IsDataLoading = false;
                return;
            }

            xmldata = xd;

            this.IsDataLoading = false;
        }
    }
}