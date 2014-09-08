using System;

namespace NiouBusEngine
{
    public class StopAreaViewModel : BaseViewModel
    {
        private String _StopAreaExternalCode;
        public String StopAreaExternalCode
        {
            get
            {
                return _StopAreaExternalCode;
            }
            set
            {
                SetProperty(ref _StopAreaExternalCode, value);
                //_StopAreaExternalCode = value;
                //NotifyPropertyChanged("StopAreaExternalCode");
            }
        }

        private String _StopAreaName;
        public String StopAreaName
        {
            get
            {
                return _StopAreaName;
            }
            set
            {
                SetProperty(ref _StopAreaName, value);
                //_StopAreaName = value;
                //NotifyPropertyChanged("StopAreaName");
            }
        }

        private String _CityName;
        public String CityName
        {
            get
            {
                return _CityName;
            }
            set
            {
                SetProperty(ref _CityName, value);
                //_CityName = value;
                //NotifyPropertyChanged("CityName");
            }
        }

    }
}
