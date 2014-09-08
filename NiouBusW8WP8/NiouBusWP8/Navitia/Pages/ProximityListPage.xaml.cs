using Microsoft.Phone.Controls;
using Microsoft.Phone.Maps.Toolkit;
using NiouBusEngine;
using NiouBusEngine.Navitia;
using System;
using System.Device.Location;
using System.Linq;
using System.Windows;
using System.Windows.Navigation;
using System.Windows.Threading;
using Windows.Devices.Geolocation;

namespace NiouBusWP8
{
    public partial class ProximityListPage : PhoneApplicationPage
    {
        public const string BaseUri = "/Navitia/Pages/ProximityListPage.xaml";
        public ProximityListViewModel VM = new ProximityListViewModel();

        private DispatcherTimer _TimerChange;
        public ProximityListPage()
        {
            InitializeComponent();

            _TimerChange = new DispatcherTimer();
            _TimerChange.Interval = TimeSpan.FromSeconds(2);
            _TimerChange.Tick += new EventHandler(timerChange_Tick);
            
            DataContext = VM;
            MapExtensions.GetChildren(mapItems).OfType<MapItemsControl>().First().ItemsSource = VM.Items;
        }

        protected async override void OnNavigatedTo(NavigationEventArgs e)
        {
            VM.Server = NavigationContext.SafeQueryString(Parameters.Server);
            VM.Network = NavigationContext.SafeQueryString(Parameters.NetworkExternalCode);

            if (VM.IsDataLoading)
            {
                Geolocator geolocator = new Geolocator();
                Geoposition geoposition = await geolocator.GetGeopositionAsync();
                GeoCoordinate geocoord = geoposition.Coordinate.ToGeoCoordinate();
                mapItems.Center = geocoord;
                MapExtensions.GetChildren(mapItems).OfType<UserLocationMarker>().First().GeoCoordinate = geocoord;

                VM.Coord = CoordConverter2.Convert(mapItems.Center);

                await VM.LoadDataAsync(false);
            }
        }

        private void mapItems_CenterChanged(object sender, Microsoft.Phone.Maps.Controls.MapCenterChangedEventArgs e)
        {
            _TimerChange.Stop();
            _TimerChange.Start();
        }

        private void plushpin_Tap(object sender, System.Windows.Input.GestureEventArgs e)
        {
            ProximityViewModel proximity = (sender as Pushpin).DataContext as ProximityViewModel;
            if (MessageBox.Show(String.Format("{0}", proximity.StopAreaName), "arrêt", MessageBoxButton.OKCancel) == MessageBoxResult.OK)
            {
                NavigationService.Navigate(new Uri(LineListPage.BaseUri + "?" + NavitiaTools.GetParameters(Server: VM.Server, NetworkExternalCode: "", StopAreaExternalCode: proximity.StopAreaExternalCode).EscapeAndConcat(), UriKind.Relative));
            }
            e.Handled = true;
        }

        private void appBarButtonRefresh_Click(object sender, EventArgs e)
        {

        }

        private async void appBarButtonMyPosition_Click(object sender, EventArgs e)
        {
            Geolocator geolocator = new Geolocator();
            geolocator.DesiredAccuracy = Windows.Devices.Geolocation.PositionAccuracy.High;
            Geoposition geoposition = await geolocator.GetGeopositionAsync(maximumAge: TimeSpan.FromMinutes(1), timeout: TimeSpan.FromSeconds(30));
            GeoCoordinate geocoord = geoposition.Coordinate.ToGeoCoordinate();
            mapItems.Center = geocoord;
            MapExtensions.GetChildren(mapItems).OfType<UserLocationMarker>().First().GeoCoordinate = geocoord;
            mapItems.ZoomLevel = 15;
        }

        async void timerChange_Tick(object sender, EventArgs e)
        {
            _TimerChange.Stop();
            VM.Coord = CoordConverter2.Convert(mapItems.Center);
            await VM.LoadDataAsync(false);
        }

        private void appBarButtonContacts_Click(object sender, EventArgs e)
        {
            WP8Tools.AddressSelector selector = new WP8Tools.AddressSelector(mapItems);
            selector.SelectAddress();
        }

        private void appBarButtonAddress_Click(object sender, EventArgs e)
        {
            WP8Tools.AddressSelector selector = new WP8Tools.AddressSelector(mapItems);
            selector.EnterAddress();
        }

        private void appBarButtonPin_Click(object sender, EventArgs e)
        {
            Func<NetworkViewModel, bool> isNetwork = (NetworkViewModel net) => { return net.Server == VM.Server && net.NetworkExternalCode == VM.Network; };
            var network = GlobalSettings.ServerList.Networks.First(nets => nets.Where(isNetwork).FirstOrDefault() != null).First(isNetwork);

            string url = BaseUri + "?" + NavitiaTools.GetParameters(Server: VM.Server, NetworkExternalCode: VM.Network).EscapeAndConcat();
            NavigationService.Navigate(new Uri(ColorPickerPage.BaseUri + "?" + ColorPickerPage.GetParameters(Title: network.NetworkName, Line1: "plan", Line2: null, Uri: url).EscapeAndConcat(), UriKind.Relative));
        }
    }
}