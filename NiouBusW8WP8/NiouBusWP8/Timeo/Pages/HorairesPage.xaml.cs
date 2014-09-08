using Microsoft.Phone.Controls;
using NiouBusEngine;
using NiouBusEngine.Timeo;
using System;
using System.Windows;
using System.Windows.Media;
using System.Windows.Navigation;
using System.Windows.Threading;


namespace NiouBusWP8
{
    public partial class HorairesPage : PhoneApplicationPage
    {
        public const string BaseUri = "/Timeo/Pages/HorairesPage.xaml";
        public HorairesViewModel VM = new HorairesViewModel();

        // Constructeur
        public HorairesPage()
        {
            InitializeComponent();

            DataContext = VM;
        }

        // Charger les données pour les éléments ViewModel
        protected async override void OnNavigatedTo(NavigationEventArgs e)
        {
            //if (e.NavigationMode == NavigationMode.Back && ColorPickerPage.SelectedColorItem != null)
            //{
            //    AddPin();
            //    ColorPickerPage.SelectedColorItem = null;
            //}
            //else
            //{
            //    ColorPickerPage.SelectedColorItem = null;
                //HorairesViewModel model = DataContext as HorairesViewModel;
                VM.Server = NavigationContext.SafeQueryString(Parameters.server);
                VM.Key = NavigationContext.SafeQueryString(Parameters.key);
                VM.Ville = NavigationContext.SafeQueryString(Parameters.ville);
                String refs = NavigationContext.SafeQueryString(Parameters.refs);
                if (refs == null)
                {
                    String ligne = NavigationContext.SafeQueryString(Parameters.ligne);
                    String sens = NavigationContext.SafeQueryString(Parameters.sens);
                    String code = NavigationContext.SafeQueryString(Parameters.code);

                    var xd = await TimeoTools.GetArretAsync(VM.Server, VM.Key, VM.Ville, sens, ligne, code, true);
                    if (xd != null && xd.als != null && xd.als.Count > 0)
                    {
                        refs = xd.als[0].refs;
                    }
                }
                VM.Refs = refs;
                await VM.LoadDataAsync(true);

                DispatcherTimer dt = new DispatcherTimer();
                dt.Interval = TimeSpan.FromSeconds(10);
                dt.Tick += appBarButtonRefresh_Click;
                dt.Start();
            //}
        }

        async void appBarButtonRefresh_Click(object sender, EventArgs e)
        {
            await VM.LoadDataAsync(true);
        }

        void appBarButtonPin_Click(object sender, EventArgs e)
        {
            string url = BaseUri + "?" + TimeoTools.GetParameters(server: VM.Server, key: VM.Key, ville: VM.Ville, ligne: VM.Ligne, sens: VM.Sens, code: VM.Code).EscapeAndConcat();
            NavigationService.Navigate(new Uri(ColorPickerPage.BaseUri + "?" +
                ColorPickerPage.GetParameters(Title: VM.LigneNom, Line1: VM.Arret, Line2: VM.Vers, Uri: url).EscapeAndConcat(), UriKind.Relative));
            //NavigationService.Navigate(new Uri(ColorPickerPage.BaseUri, UriKind.Relative));
        }

        //private void AddPin()
        //{
        //    string refs = VM.Refs;
        //    string code = VM.Code;
        //    string ligne = VM.Ligne;
        //    string sens = VM.Sens;
        //    string url = BaseUri + "?" + TimeoTools.GetParameters(server: VM.Server, key: VM.Key, ville: VM.Ville, ligne: ligne, sens: sens, code: code).EscapeAndConcat();
        //    //var filename = "/Shared/ShellContent/" + refs.Replace("|", "_") + ".jpg";

        //    if (code.Equals("0"))
        //    {
        //        MessageBox.Show("Les raccourcis ne sont pas supportés pour cet arrêt");
        //        return;
        //    }

        //    Color backgroundColor = (ColorPickerPage.SelectedColorItem.Color as SolidColorBrush).Color;
        //    Color foregroundColor = Colors.White;

        //    //FavoriteHelper.PinTextTile(VM.LigneNom, VM.Arret, VM.Vers, url, filename, backgroundColor, foregroundColor);
        //    FavoriteHelper.AddFavorite(new Favorite()
        //    {
        //        Title = VM.LigneNom,
        //        Line1 = VM.Arret,
        //        Line2 = VM.Vers,
        //        Uri = url,
        //        //ImageSrc = filename,
        //        Background = backgroundColor,
        //        Foreground = foregroundColor
        //    });
        //}

        private void appBarMenuItemReverse_Click(object sender, EventArgs e)
        {
            String refs = VM.Refs;
            String code = VM.Code;
            String ligne = VM.Ligne;
            String sens = VM.Sens;
            if (sens.Equals("R"))
            {
                sens = "A";
            }
            else
            {
                sens = "R";
            }
            String url = BaseUri + "?" + TimeoTools.GetParameters(server: VM.Server, key: VM.Key, ville: VM.Ville, ligne: ligne, sens: sens, code: code).EscapeAndConcat();
            NavigationService.Navigate(new Uri(url, UriKind.Relative));
        }

        private void appBarMenuItemLine_Click(object sender, EventArgs e)
        {
            String refs = VM.Refs;
            String code = VM.Code;
            String ligne = VM.Ligne;
            String sens = VM.Sens;
            String url = ArretsPage.BaseUri + "?" + TimeoTools.GetParameters(server: VM.Server, key: VM.Key, ville: VM.Ville, ligne: ligne, sens: sens).EscapeAndConcat();
            NavigationService.Navigate(new Uri(url, UriKind.Relative));
        }

        private void appBarMenuItemArret_Click(object sender, EventArgs e)
        {
            String refs = VM.Refs;
            String code = VM.Code;
            String ligne = VM.Ligne;
            String sens = VM.Sens;
            String url = ArretsPage.BaseUri + "?" + TimeoTools.GetParameters(server: VM.Server, key: VM.Key, ville: VM.Ville, code: code).EscapeAndConcat();
            NavigationService.Navigate(new Uri(url, UriKind.Relative));
        }
    }
}