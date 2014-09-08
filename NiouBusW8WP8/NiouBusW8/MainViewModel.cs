using NiouBusEngine;
using System;
using System.ComponentModel;

namespace NiouBusW8
{
    public class MainViewModel : INotifyPropertyChanged
    {
        public LignesViewModel Lignes { get; set; }
        public ArretsViewModel Arrets { get; set; }
        public HorairesViewModel Horaires { get; set; }

        public event PropertyChangedEventHandler PropertyChanged;
        private void NotifyPropertyChanged(String propertyName)
        {
            PropertyChangedEventHandler handler = PropertyChanged;
            if (null != handler)
            {
                handler(this, new PropertyChangedEventArgs(propertyName));
            }
        }
    }
}
