using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NiouBusEngine
{
    public class NetworkViewModel
    {
        public String NetworkName { get; set; }
        public String NetworkExternalCode { get; set; }
        public String NetworkAdditionalData { get; set; }

        public String Server { get; set; }
        public String TimeoKey { get; set; }
        public String TimeoVille { get; set; }

        public override string ToString()
        {
            if (string.IsNullOrEmpty(NetworkAdditionalData))
            {
                return NetworkName;
            }
            else
            {
                return NetworkName + " - " + NetworkAdditionalData;
            }
        }
    }
}
