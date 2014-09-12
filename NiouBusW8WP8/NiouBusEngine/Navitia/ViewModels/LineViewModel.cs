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
                Set(ref _LineExternalCode, value);
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
                Set(ref _LineCode, value);
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
                Set(ref _LineName, value);
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
                Set(ref _Direction, value);
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
                Set(ref _DirectionName, value);
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

        public int CompareTo(object obj)
        {
            return LineCode.CompareTo((obj as LineViewModel).LineCode);
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
