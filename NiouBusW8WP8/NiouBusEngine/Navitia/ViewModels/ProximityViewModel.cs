using System;

namespace NiouBusEngine
{
    public class ProximityViewModel : BaseViewModel
    {
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

        private Navitia.Coord _Coord;
        public Navitia.Coord Coord
        {
            get
            {

                return _Coord;
            }
            set
            {
                SetProperty(ref _Coord, value);
                //_Coord = value;
                //NotifyPropertyChanged("Coord");
            }
        }
    }
}
