using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NiouBusEngine
{
    public class LineViewModel : BaseViewModel, IComparable
    {
        private String _LineExternalCode;
        public String LineExternalCode
        {
            get
            {
                return _LineExternalCode;
            }
            set
            {
                SetProperty(ref _LineExternalCode, value);
                //_LineExternalCode = value;
                //NotifyPropertyChanged("LineExternalCode");
            }
        }

        private String _LineCode;
        public String LineCode
        {
            get
            {
                return _LineCode;
            }
            set
            {
                SetProperty(ref _LineCode, value);
                //_LineCode = value;
                //NotifyPropertyChanged("LineCode");
            }
        }

        private String _LineName;
        public String LineName
        {
            get
            {
                return _LineName;
            }
            set
            {
                SetProperty(ref _LineName, value);
                //_LineName = value;
                //NotifyPropertyChanged("LineName");
            }
        }

        private String _Direction;
        public String Direction
        {
            get
            {
                return _Direction;
            }
            set
            {
                SetProperty(ref _Direction, value);
                //_Direction = value;
                //NotifyPropertyChanged("Direction");
            }
        }

        private String _DirectionName;
        public String DirectionName
        {
            get
            {
                return _DirectionName;
            }
            set
            {
                SetProperty(ref _DirectionName, value);
                //_DirectionName = value;
                //NotifyPropertyChanged("DirectionName");
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

        public int CompareTo(object obj)
        {
            return LineCode.CompareTo((obj as LineViewModel).LineCode);
            //throw new NotImplementedException();
        }

        public override string ToString()
        {
            if (string.IsNullOrEmpty(LineCode))
            {
                return LineName;
            }
            else
            {
                return LineCode;
            }
        }
    }
}
