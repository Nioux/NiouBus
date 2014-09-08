using Microsoft.Phone.Controls;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Controls.Primitives;
using System.Windows.Media;
using System.Windows.Navigation;

namespace NiouBusWP8
{
    public static class Extensions
    {
        public static String SafeQueryString(this NavigationContext NavigationContext, String id)
        {
            string value;
            if(NavigationContext.QueryString.TryGetValue(id, out value))
            {
                return value;
            }
            return null;
        }

        public static Task ShowAsync(this Popup popup)
        {
            TaskCompletionSource<bool> tcs = new TaskCompletionSource<bool>();

            EventHandler onClosed = null;
            onClosed = (s, e) =>
            {
                popup.Closed -= onClosed;
                tcs.SetResult(true);
            };
            popup.Closed += onClosed;
            popup.IsOpen = true;

            return tcs.Task;
        }

        public static void InitializeApplicationBar(this PhoneApplicationPage page)
        {
            if (page.ApplicationBar != null)
            {
                if (GlobalSettings.Instance.Theme == GlobalSettings.Themes.Dark)
                {
                    page.ApplicationBar.ForegroundColor = Colors.White;
                    page.ApplicationBar.BackgroundColor = Colors.Black;
                }
                else
                {
                    page.ApplicationBar.ForegroundColor = Colors.Black;
                    page.ApplicationBar.BackgroundColor = Colors.White;
                }
            }
        }
    }
}
