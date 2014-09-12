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
                Set(ref _StopAreaExternalCode, value);
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
                Set(ref _StopAreaName, value);
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
                Set(ref _CityName, value);
            }
        }

    }
}
