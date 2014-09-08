using Microsoft.Phone.Controls;
using System;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Windows;
using System.Windows.Media;
using System.Windows.Navigation;
using NiouBusEngine;
using NiouBusEngine.Navitia;
using NiouBusEngine.Timeo;
using System.Windows.Media.Imaging;
using System.Linq;

namespace NiouBusWP8
{
    public partial class MainPage : PhoneApplicationPage, INotifyPropertyChanged
    {
        public const string BaseUri = "/Pages/MainPage.xaml";

        public MainPage()
        {
            InitializeComponent();
        }

        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            base.OnNavigatedTo(e);
            this.InitializeApplicationBar();

            LinkList = FavoriteHelper.LoadFavorites();

            if (LinkList == null)
            {
                Color backgroundColor = Colors.Transparent; // (Color)App.Current.Resources["ThemeAccentColor"];
                Color foregroundColor = Colors.White;

                var list = new ObservableCollection<Favorite>()
                {
                    new Favorite() 
                    { 
                        Title = "réseaux",
                        Uri = NetworksPage.BaseUri,
                        Background = backgroundColor,
                        Foreground = foregroundColor
                    },
                    new Favorite() 
                    { 
                        Title = "paramètres",
                        Uri = SettingsPage.BaseUri,
                        Background = backgroundColor,
                        Foreground = foregroundColor
                    },
                };
                FavoriteHelper.CreateAssetBitmap("/NiouBusWP8;component/Assets/appbar/xaml/appbar.transit.connection.xaml", Favorite.GetShellContentString(list[0].Uri), backgroundColor, foregroundColor);
                FavoriteHelper.CreateAssetBitmap("/NiouBusWP8;component/Assets/appbar/xaml/appbar.settings.xaml", Favorite.GetShellContentString(list[1].Uri), backgroundColor, foregroundColor);

                LinkList = list;
                FavoriteHelper.SaveFavorites(LinkList);
            }

            DataContext = this;
        }

        public ObservableCollection<Favorite> LinkList { get; set; }


        private void tile_Tap(object sender, System.Windows.Input.GestureEventArgs e)
        {
            FrameworkElement element = sender as FrameworkElement;
            if (element == null) return;
            Favorite favorite = element.DataContext as Favorite;
            if (favorite == null) return;

            NavigationService.Navigate(new Uri(favorite.Uri, UriKind.Relative));
        }

        private void tile_PinTap(object sender, System.Windows.Input.GestureEventArgs e)
        {
            FrameworkElement element = sender as FrameworkElement;
            if (element == null) return;
            Favorite favorite = element.DataContext as Favorite;
            if (favorite == null) return;

            FavoriteHelper.AddFavoriteToDesktop(favorite);
        }

        private void tile_DeleteTap(object sender, System.Windows.Input.GestureEventArgs e)
        {
            FrameworkElement element = sender as FrameworkElement;
            if (element == null) return;
            Favorite favorite = element.DataContext as Favorite;
            if (favorite == null) return;

            LinkList.Remove(favorite);

            FavoriteHelper.SaveFavorites(LinkList);

            NotifyPropertyChanged("LinkList");
        }

        private void tile_MoveUpTap(object sender, System.Windows.Input.GestureEventArgs e)
        {
            FrameworkElement element = sender as FrameworkElement;
            if (element == null) return;
            Favorite favorite = element.DataContext as Favorite;
            if (favorite == null) return;
            int index = LinkList.IndexOf(favorite);
            if (index <= 0) return;

            LinkList.Remove(favorite);
            LinkList.Insert(index - 1, favorite);

            FavoriteHelper.SaveFavorites(LinkList);

            NotifyPropertyChanged("LinkList");
        }

        private void tile_MoveDownTap(object sender, System.Windows.Input.GestureEventArgs e)
        {
            FrameworkElement element = sender as FrameworkElement;
            if (element == null) return;
            Favorite favorite = element.DataContext as Favorite;
            if (favorite == null) return;
            int index = LinkList.IndexOf(favorite);
            if (index < 0 || index >= LinkList.Count - 1) return;

            LinkList.Remove(favorite);
            LinkList.Insert(index + 1, favorite);

            FavoriteHelper.SaveFavorites(LinkList);

            NotifyPropertyChanged("LinkList");
        }

        public event PropertyChangedEventHandler PropertyChanged;

        protected void NotifyPropertyChanged(String propertyName)
        {
            PropertyChangedEventHandler handler = PropertyChanged;
            if (null != handler)
            {
                handler(this, new PropertyChangedEventArgs(propertyName));
            }
        }

        private void tile_NetworksTap(object sender, EventArgs e)
        {
            NavigationService.Navigate(new Uri(NetworksPage.BaseUri, UriKind.Relative));
        }

        private void tile_SettingsTap(object sender, EventArgs e)
        {
            NavigationService.Navigate(new Uri(SettingsPage.BaseUri, UriKind.Relative));
        }
    }
}