using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NiouBusEngine
{
    public class StopViewModel : BaseViewModel
    {
        private int _Hour;
        public int Hour
        {
            get
            {
                return _Hour;
            }
            set
            {
                Set(ref _Hour, value);
                NotifyPropertyChanged("Time");
            }
        }

        private int _Minute;
        public int Minute
        {
            get
            {
                return _Minute;
            }
            set
            {
                Set(ref _Minute, value);
                NotifyPropertyChanged("Time");
            }
        }

        public String Time
        {
            get
            {
                return String.Format("{0:D2}:{1:D2}", Hour, Minute);
            }
        }

        private String _RouteName;
        public String RouteName
        {
            get
            {
                return _RouteName;
            }
            set
            {
                Set(ref _RouteName, value);
            }
        }
    }
}
