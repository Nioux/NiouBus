using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NiouBusEngine
{
    public class NetworkListViewModel : List<NetworkViewModel>
    {
        public string Region { get; set; }

        public override string ToString()
        {
            return Region;
        }
    }
}
