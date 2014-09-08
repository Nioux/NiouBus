using Microsoft.Phone.Controls;
using NiouBusEngine;
using NiouBusEngine.Navitia;
using NiouBusEngine.Timeo;
using System;
using System.IO;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Media;
using System.Windows.Navigation;

namespace NiouBusWP8
{
    public partial class NetworksPage : PhoneApplicationPage//, INotifyPropertyChanged
    {
        public const string BaseUri = "/Pages/NetworksPage.xaml";

        //CollectionViewSource Networks = new CollectionViewSource();

        public NetworksPage()
        {
            InitializeComponent();
        }

        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            base.OnNavigatedTo(e);

            DataContext = GlobalSettings.ServerList;
        }

        private void FirstPivot_Filter(object sender, System.Windows.Data.FilterEventArgs e)
        {
            NetworkListViewModel network = e.Item as NetworkListViewModel;
            if (network.Region.StartsWith("a"))
                e.Accepted = true;
            else
                e.Accepted = false;
        }

        private void LongListSelector_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (LLS_Servers.SelectedItem == null) return;
            NetworkViewModel vm = LLS_Servers.SelectedItem as NetworkViewModel;
            if (vm != null)
            {
                if (vm.NetworkExternalCode != null)
                {
                    NavigationService.Navigate(new Uri(LineListPage.BaseUri + "?" + NavitiaTools.GetParameters(Server: vm.Server, NetworkExternalCode: vm.NetworkExternalCode).EscapeAndConcat(), UriKind.Relative));
                }
                else if (vm.TimeoKey != null)
                {
                    NavigationService.Navigate(new Uri(LignesPage.BaseUri + "?" + TimeoTools.GetParameters(server: vm.Server, key: vm.TimeoKey, ville: vm.TimeoVille).EscapeAndConcat(), UriKind.Relative));
                }
            }
            LLS_Servers.SelectedItem = null;
        }

        private void appBarButtonPin_Click(object sender, EventArgs e)
        {
            Color backgroundColor = (Color)App.Current.Resources["ThemeAccentColor"];
            Color foregroundColor = Colors.White;
            FavoriteHelper.AddFavorite(                    
                new Favorite() 
                    { 
                        Title = "réseaux",
                        Uri = NetworksPage.BaseUri,
                        Background = backgroundColor,
                        Foreground = foregroundColor
                    });
            FavoriteHelper.CreateAssetBitmap("/NiouBusWP8;component/Assets/appbar/xaml/appbar.transit.connection.xaml", Favorite.GetShellContentString(NetworksPage.BaseUri), backgroundColor, foregroundColor);
        }

        private void tbSearch_Changed(object sender, TextChangedEventArgs e)
        {
            ServerListViewModel VM = DataContext as ServerListViewModel;
            VM.Search = tbSearch.Text;
        }

        private void tbSearch_GotFocus(object sender, RoutedEventArgs e)
        {
            ServerListViewModel VM = DataContext as ServerListViewModel;
            VM.HeaderEnabled = false;
            gridNetworks.Margin = new Thickness(0, -100, 0, 0);
        }

        private void tbSearch_LostFocus(object sender, RoutedEventArgs e)
        {
            ServerListViewModel VM = DataContext as ServerListViewModel;
            VM.HeaderEnabled = true;
            gridNetworks.Margin = new Thickness(0, -30, 0, 0);
        }
    }
}