using Microsoft.Phone.Controls;
using NiouBusEngine;
using NiouBusEngine.Navitia;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.IO.IsolatedStorage;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Media;

namespace NiouBusWP8
{
    public class GlobalSettings : BaseViewModel
    {
        private static string GetSettingsValue([CallerMemberName] string name = null)
        {
            lock (IsolatedStorageSettings.ApplicationSettings)
            {
                string value;
                if (IsolatedStorageSettings.ApplicationSettings.TryGetValue(name, out value))
                {
                    return value;
                }
                return null;
            }
        }

        private static void SetSettingsValue(string value, [CallerMemberName] string name = null)
        {
            lock (IsolatedStorageSettings.ApplicationSettings)
            {
                if (IsolatedStorageSettings.ApplicationSettings.Contains(name))
                {
                    if (value == null)
                    {
                        IsolatedStorageSettings.ApplicationSettings.Remove(name);
                    }
                    else
                    {
                        IsolatedStorageSettings.ApplicationSettings[name] = value;
                    }
                }
                else
                {
                    if (value != null)
                    {
                        IsolatedStorageSettings.ApplicationSettings.Add(name, value);
                    }
                }
            }
        }

        private static ServerListViewModel _ServerList = null;
        public static ServerListViewModel ServerList
        {
            get
            {
                if (_ServerList == null)
                {
                    _ServerList = new NiouBusEngine.ServerListViewModel();
                    using (Stream stream = Application.GetResourceStream(new Uri("ServerList.xml", UriKind.RelativeOrAbsolute)).Stream)
                    {
                        _ServerList.Load(stream);
                    }
                }
                return _ServerList;
            }
        }

        private static GlobalSettings _Instance = new GlobalSettings();
        public static GlobalSettings Instance
        {
            get
            {
                return _Instance;
            }
        }
        private Color _BackgroundColor = Colors.Blue;
        public Color BackgroundColor { get { return _BackgroundColor; } set { SetProperty(ref _BackgroundColor, value); } }

        public class Themes
        {
            public static string Dark = "Dark";
            public static string Light = "Light";
        }

        private Color ConvertColor(uint uintCol)
        {
            byte A = (byte)((uintCol & 0xFF000000) >> 24);
            byte R = (byte)((uintCol & 0x00FF0000) >> 16);
            byte G = (byte)((uintCol & 0x0000FF00) >> 8);
            byte B = (byte)((uintCol & 0x000000FF) >> 0);

            return Color.FromArgb(A, R, G, B);
        }

        private Color ConvertColor(string sCol)
        {
            uint icolor = uint.Parse(sCol.Replace("#", ""), NumberStyles.HexNumber);
            return ConvertColor(icolor);
        }

        public Color AccentColor
        {
            get
            {
                string color = GetSettingsValue();
                if (string.IsNullOrEmpty(color)) return (Color)App.Current.Resources["PhoneAccentColor"];
                uint icolor = uint.Parse(color.Replace("#",""), NumberStyles.HexNumber);
                return ConvertColor(icolor);
            }
            set
            {
                string color = value.ToString();
                SetSettingsValue(color);
                InitializeAccentColor();
            }
        }
        public string Theme
        {
            get
            {
                return GetSettingsValue();
            }
            set
            {
                SetSettingsValue(value);
                InitializeTheme();
            }
        }
        public void InitializeAccentColor()
        {
            App.Current.Resources.Remove("ThemeAccentColor");
            App.Current.Resources.Add("ThemeAccentColor", AccentColor);
            App.Current.Resources.Remove("ThemeAccentBrush");
            App.Current.Resources.Add("ThemeAccentBrush", new SolidColorBrush(AccentColor));
        }
        public void InitializeTheme()
        {
            if(Theme == Themes.Dark)
            {
                ThemeManager.OverrideOptions = ThemeManagerOverrideOptions.None;
                ThemeManager.OverrideOptions = ThemeManagerOverrideOptions.SystemTrayColors;
                ThemeManager.ToDarkTheme();
                //ThemeManager.SetBackground(new SolidColorBrush(Colors.Blue));
                //ThemeManager.SetAccentColor(Colors.Green);
                App.Current.Resources.Remove("ThemeAccentColor");
                App.Current.Resources.Add("ThemeAccentColor", App.Current.Resources["PhoneAccentColor"]);
                App.Current.Resources.Remove("ThemeBackgroundColor");
                App.Current.Resources.Add("ThemeBackgroundColor", Colors.Black);
                App.Current.Resources.Remove("ThemeAccentBrush");
                App.Current.Resources.Add("ThemeAccentBrush", App.Current.Resources["PhoneAccentBrush"]);
                App.Current.Resources.Remove("ThemeBackgroundBrush");
                App.Current.Resources.Add("ThemeBackgroundBrush", new SolidColorBrush(Colors.Black));
            }
            else
            {
                ThemeManager.OverrideOptions = ThemeManagerOverrideOptions.None;
                ThemeManager.OverrideOptions = ThemeManagerOverrideOptions.SystemTrayColors;
                ThemeManager.ToLightTheme();
                //ThemeManager.SetBackground(new SolidColorBrush(Colors.Blue));
                //ThemeManager.SetAccentColor(Colors.Green);
                App.Current.Resources.Remove("ThemeAccentColor");
                App.Current.Resources.Add("ThemeAccentColor", App.Current.Resources["PhoneAccentColor"]);
                App.Current.Resources.Remove("ThemeBackgroundColor");
                App.Current.Resources.Add("ThemeBackgroundColor", Colors.White);
                App.Current.Resources.Remove("ThemeAccentBrush");
                App.Current.Resources.Add("ThemeAccentBrush", App.Current.Resources["PhoneAccentBrush"]);
                App.Current.Resources.Remove("ThemeBackgroundBrush");
                App.Current.Resources.Add("ThemeBackgroundBrush", new SolidColorBrush(Colors.White));
            }
        }
    }
}
