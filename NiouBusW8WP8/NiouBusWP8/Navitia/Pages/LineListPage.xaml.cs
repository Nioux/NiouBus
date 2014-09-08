using Microsoft.Phone.Controls;
using Microsoft.Phone.Maps.Toolkit;
using NiouBusEngine;
using NiouBusEngine.Navitia;
using System;
using System.Linq;
using System.Windows.Controls;
using System.Windows.Navigation;

namespace NiouBusWP8
{
    public partial class LineListPage : PhoneApplicationPage
    {
        public const string BaseUri = "/Navitia/Pages/LineListPage.xaml";
        public LineListViewModel VM = new LineListViewModel();

        public LineListPage()
        {
            InitializeComponent();

            DataContext = VM;
        }

        protected async override void OnNavigatedTo(NavigationEventArgs e)
        {
            VM.Server = NavigationContext.SafeQueryString(Parameters.Server);
            VM.Network = NavigationContext.SafeQueryString(Parameters.NetworkExternalCode);
            VM.StopAreaExternalCode = NavigationContext.SafeQueryString(Parameters.StopAreaExternalCode);

            if (VM.IsDataLoading)
            {
                await VM.LoadDataAsync(false);
            }
        }

        private void LongListSelector_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (llsLineList.SelectedItem == null) return;
            LineViewModel lvm = llsLineList.SelectedItem as LineViewModel;
            if (String.IsNullOrWhiteSpace(VM.StopAreaExternalCode))
            {
                NavigationService.Navigate(new Uri(StopAreaListPage.BaseUri + "?" + NavitiaTools.GetParameters(Server: VM.Server, NetworkExternalCode: VM.Network, LineExternalCode: lvm.LineExternalCode, Direction: lvm.Direction).EscapeAndConcat(), UriKind.Relative));
            }
            else
            {
                NavigationService.Navigate(new Uri(DepartureBoardListPage.BaseUri + "?" + NavitiaTools.GetParameters(Server: VM.Server, NetworkExternalCode: VM.Network, LineExternalCode: lvm.LineExternalCode, Direction: lvm.Direction, StopAreaExternalCode: VM.StopAreaExternalCode).EscapeAndConcat(), UriKind.Relative));
            }
            llsLineList.SelectedItem = null;

            
        }

        private void pivot_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (pivot.SelectedIndex == 1)
            {
                //MapExtensions.GetChildren(mapItems).OfType<MapItemsControl>().First().ItemsSource = (DataContext as LineListViewModel).Items;
            }
        }

        private void llsLineList_LayoutUpdated(object sender, EventArgs e)
        {

        }

        private void appBarButtonRefresh_Click(object sender, EventArgs e)
        {

        }

        private void appBarButtonPin_Click(object sender, EventArgs e)
        {
            Func<NetworkViewModel, bool> isNetwork = (NetworkViewModel net) => { return net.Server == VM.Server && net.NetworkExternalCode == VM.Network; };
            var network = GlobalSettings.ServerList.Networks.First(nets => nets.Where(isNetwork).FirstOrDefault() != null).First(isNetwork);

            string url = BaseUri + "?" + NavitiaTools.GetParameters(Server: VM.Server, NetworkExternalCode: VM.Network).EscapeAndConcat();
            NavigationService.Navigate(new Uri(ColorPickerPage.BaseUri + "?" + ColorPickerPage.GetParameters(Title: network.NetworkName, Line1: "liste des lignes", Line2: null, Uri: url).EscapeAndConcat(), UriKind.Relative));
        }

        private void appBarButtonMap_Click(object sender, EventArgs e)
        {
            NavigationService.Navigate(new Uri(ProximityListPage.BaseUri + "?" + NavitiaTools.GetParameters(Server: VM.Server, NetworkExternalCode: VM.Network).EscapeAndConcat(), UriKind.RelativeOrAbsolute));
        }

    }
}