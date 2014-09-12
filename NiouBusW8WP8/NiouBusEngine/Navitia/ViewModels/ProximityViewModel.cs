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
                Set(ref _StopAreaName, value);
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
                Set(ref _StopAreaExternalCode, value);
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
                Set(ref _Coord, value);
            }
        }
    }
}
