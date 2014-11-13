using Microsoft.Phone.Controls;
using Microsoft.Phone.Maps.Controls;
using Microsoft.Phone.Maps.Toolkit;
using NiouBusEngine;
using NiouBusEngine.Navitia;
using System;
using System.Collections;
using System.Device.Location;
using System.Linq;
using System.Windows;
using System.Windows.Navigation;
using System.Windows.Threading;
using Windows.Devices.Geolocation;

namespace NiouBusWP8
{
    public partial class JourneySelectionPage : BasePage
    {
        private DispatcherTimer _TimerChange;
        public JourneySelectionPage()
        {
            InitializeComponent();

            _TimerChange = new DispatcherTimer();
            _TimerChange.Interval = TimeSpan.FromSeconds(2);
            _TimerChange.Tick += new EventHandler(timerChange_Tick);
            
            DataContext = new ProximityListViewModel();
            MapExtensions.GetChildren(CurrentMap).OfType<MapItemsControl>().First().ItemsSource = (DataContext as ProximityListViewModel).Items;
        }

        private IEnumerable SourceDataContextItems
        {
            get
            {
                return MapExtensions.GetChildren(mapItemsSource).OfType<MapItemsControl>().First().ItemsSource;
            }
            set
            {
                MapExtensions.GetChildren(mapItemsSource).OfType<MapItemsControl>().First().ItemsSource = value;
            }
        }

        protected async override void OnNavigatedTo(NavigationEventArgs e)
        {

            ProximityListViewModel vm = DataContext as ProximityListViewModel;
            if (vm.IsDataLoading)
            {
                vm.Server = NavigationContext.SafeQueryString(Parameters.Server);
                vm.Network = NavigationContext.SafeQueryString(Parameters.NetworkExternalCode);

                Geolocator geolocator = new Geolocator();
                Geoposition geoposition = await geolocator.GetGeopositionAsync();
                GeoCoordinate geocoord = geoposition.Coordinate.ToGeoCoordinate();
                CurrentMap.Center = geocoord;
                MapExtensions.GetChildren(CurrentMap).OfType<UserLocationMarker>().First().GeoCoordinate = geocoord;

                vm.Coord = CoordConverter2.Convert(CurrentMap.Center);
                await vm.LoadDataAsync(false);
            }
        }

        async void timerChange_Tick(object sender, EventArgs e)
        {
            _TimerChange.Stop();
            ProximityListViewModel vm = DataContext as ProximityListViewModel;
            vm.Coord = CoordConverter2.Convert(CurrentMap.Center);
            await vm.LoadDataAsync(false);
        }

        private Map CurrentMap
        {
            get
            {
                if (((mapItemsSource.Parent as PivotItem).Parent as Pivot).SelectedIndex == 0)
                {
                    return mapItemsSource;
                }
                else
                {
                    return mapItemsDestination;
                }
            }
        }

        private async void appBarButtonMyPosition_Click(object sender, EventArgs e)
        {
            Geolocator geolocator = new Geolocator();
            geolocator.DesiredAccuracy = Windows.Devices.Geolocation.PositionAccuracy.High;
            Geoposition geoposition = await geolocator.GetGeopositionAsync(maximumAge: TimeSpan.FromMinutes(1), timeout: TimeSpan.FromSeconds(30));
            GeoCoordinate geocoord = geoposition.Coordinate.ToGeoCoordinate();
            CurrentMap.Center = geocoord;
            MapExtensions.GetChildren(CurrentMap).OfType<UserLocationMarker>().First().GeoCoordinate = geocoord;
            CurrentMap.ZoomLevel = 15;
        }

        private void appBarButtonAddress_Click(object sender, EventArgs e)
        {
            WP8Tools.AddressSelector selector = new WP8Tools.AddressSelector(CurrentMap);
            selector.EnterAddress();
        }

        private void appBarButtonContacts_Click(object sender, EventArgs e)
        {
            WP8Tools.AddressSelector selector = new WP8Tools.AddressSelector(CurrentMap);
            selector.SelectAddress();
        }

        private void appBarButtonRefresh_Click(object sender, EventArgs e)
        {

        }

        private void mapItemsSource_CenterChanged(object sender, Microsoft.Phone.Maps.Controls.MapCenterChangedEventArgs e)
        {
            _TimerChange.Stop();
            _TimerChange.Start();
        }

        private void mapItemsDestination_CenterChanged(object sender, Microsoft.Phone.Maps.Controls.MapCenterChangedEventArgs e)
        {
            _TimerChange.Stop();
            _TimerChange.Start();
        }

        private void plushpinSource_Tap(object sender, System.Windows.Input.GestureEventArgs e)
        {
            ProximityListViewModel vm = DataContext as ProximityListViewModel;
            ProximityViewModel proximity = (sender as Pushpin).DataContext as ProximityViewModel;
            if (MessageBox.Show(String.Format("{0}", proximity.StopAreaName), "arrêt", MessageBoxButton.OKCancel) == MessageBoxResult.OK)
            {
                //NavigationService.Navigate(new Uri("/Navitia/Pages/LineListPage.xaml?Server=" + vm.Server + "&NetworkExternalCode=" + vm.Network + "&StopAreaExternalCode=" + proximity.StopAreaExternalCode, UriKind.Relative));
                NavigationService.Navigate(new Uri("/Navitia/Pages/LineListPage.xaml?Server=" + vm.Server + "&NetworkExternalCode=" + "" + "&StopAreaExternalCode=" + proximity.StopAreaExternalCode, UriKind.Relative));
            }
            e.Handled = true;
        }

        private void plushpinDestination_Tap(object sender, System.Windows.Input.GestureEventArgs e)
        {
            ProximityListViewModel vm = DataContext as ProximityListViewModel;
            ProximityViewModel proximity = (sender as Pushpin).DataContext as ProximityViewModel;
            if (MessageBox.Show(String.Format("{0}", proximity.StopAreaName), "arrêt", MessageBoxButton.OKCancel) == MessageBoxResult.OK)
            {
                //NavigationService.Navigate(new Uri("/Navitia/Pages/LineListPage.xaml?Server=" + vm.Server + "&NetworkExternalCode=" + vm.Network + "&StopAreaExternalCode=" + proximity.StopAreaExternalCode, UriKind.Relative));
                NavigationService.Navigate(new Uri("/Navitia/Pages/LineListPage.xaml?Server=" + vm.Server + "&NetworkExternalCode=" + "" + "&StopAreaExternalCode=" + proximity.StopAreaExternalCode, UriKind.Relative));
            }
            e.Handled = true;
        }
    }
}