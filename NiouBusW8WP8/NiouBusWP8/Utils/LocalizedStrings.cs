using NiouBusWP8.Resources;
using System.Collections.Generic;
using System.Reflection;
using System.Windows.Media;
using System.Linq;
using System.Collections.ObjectModel;
using System;
using System.Net; 
using System.Windows; 
using System.Windows.Controls; 
using System.Windows.Documents; 
using System.Windows.Input; 
using System.Windows.Media.Animation; 
using System.Windows.Shapes; 
using Microsoft.Phone.Controls; 

namespace NiouBusWP8
{
    /// <summary>
    /// Permet d'accéder aux ressources de chaîne.
    /// </summary>
    public class LocalizedStrings
    {
        private static AppResources _localizedResources = new AppResources();

        public AppResources LocalizedResources { get { return _localizedResources; } }
    }

}