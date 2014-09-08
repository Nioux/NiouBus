using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using System.Windows.Media;
using System.Reflection;
using System.Collections.ObjectModel;
using System.Diagnostics;
using Windows.System;

namespace NiouBusWP8
{
    public partial class SettingsPage : PhoneApplicationPage
    {
        public const string BaseUri = "/Pages/SettingsPage.xaml";

        public SettingsPage()
        {
            InitializeComponent();
            this.InitializeApplicationBar();

            if (GlobalSettings.Instance.Theme == GlobalSettings.Themes.Dark)
            {
                lpTheme.SelectedIndex = 1;
            }
            else
            {
                lpTheme.SelectedIndex = 0;
            }
        }

        private void lpTheme_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (lpTheme != null)
            {
                if (lpTheme.SelectedIndex == 0)
                {
                    GlobalSettings.Instance.Theme = GlobalSettings.Themes.Light;
                }
                else
                {
                    GlobalSettings.Instance.Theme = GlobalSettings.Themes.Dark;
                }
                this.InitializeApplicationBar();
            }
        }

        private void cpForeground_ValueChanged(object sender, ColorPickerControl.ColorPickerEventArgs e)
        {
            Debug.WriteLine(e.Color.Text);
        }

        private void appBarButtonPin_Click(object sender, EventArgs e)
        {

        }

        //private void cpChrome_ValueChanged(object sender, ColorPickerControl.ColorPickerEventArgs e)
        //{
        //    if (cpChrome != null && cpChrome.SelectedColor != null)
        //    {
        //        App.Current.Resources.Remove("ThemeChromeColor");
        //        App.Current.Resources.Add("ThemeChromeColor", cpChrome.SelectedColor);
        //        App.Current.Resources.Remove("ThemeChromeBrush");
        //        App.Current.Resources.Add("ThemeChromeBrush", new SolidColorBrush(cpChrome.SelectedColor));
        //    }
        //}

        private void cpAccent_ValueChanged(object sender, ColorPickerControl.ColorPickerEventArgs e)
        {
            if (cpAccent != null && cpAccent.SelectedColor != null)
            {
                GlobalSettings.Instance.AccentColor = cpAccent.SelectedColor;
                //App.Current.Resources.Remove("ThemeAccentColor");
                //App.Current.Resources.Add("ThemeAccentColor", cpAccent.SelectedColor);
                //App.Current.Resources.Remove("ThemeAccentBrush");
                //App.Current.Resources.Add("ThemeAccentBrush", new SolidColorBrush(cpAccent.SelectedColor));
            }
        }

    }
}