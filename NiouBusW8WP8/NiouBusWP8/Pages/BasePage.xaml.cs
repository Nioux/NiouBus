using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using NiouBusEngine;
using System.ComponentModel;
using System.Runtime.CompilerServices;

namespace NiouBusWP8
{
    public partial class BasePage : PhoneApplicationPage, INotifyPropertyChanged
    {
        public event PropertyChangedEventHandler PropertyChanged;
        protected void NotifyPropertyChanged(String propertyName)
        {
            PropertyChangedEventHandler handler = PropertyChanged;
            if (null != handler)
            {
                handler(this, new PropertyChangedEventArgs(propertyName));
            }
        }

        protected bool Set<T>(ref T storage, T value, [CallerMemberName] String propertyName = null)
        {
            if (Equals(storage, value)) return false;

            storage = value;
            NotifyPropertyChanged(propertyName);

            return true;
        }

        public BasePage()
        {
            InitializeComponent();
        }

        private bool _IsDataLoading = true;
        public bool IsDataLoading { get { return _IsDataLoading; } set { Set(ref _IsDataLoading, value); } }
    }
}