using Microsoft.Phone.Controls;
using NiouBusEngine;
using System;
using System.Collections.Generic;
using System.Windows.Controls;
using System.Windows.Navigation;
using Windows.Phone.Speech.Recognition;
using System.Linq;
using System.Diagnostics;
using NiouBusWP8;
using System.Windows.Media;
using System.Threading.Tasks;
using NiouBusEngine.Timeo;

namespace NiouBusWP8
{
    public partial class ArretsPage : PhoneApplicationPage
    {
        public const string BaseUri = "/Timeo/Pages/ArretsPage.xaml";
        public ArretsViewModel VM = new ArretsViewModel();

        // Constructeur
        public ArretsPage()
        {
            InitializeComponent();

            DataContext = VM;
        }


        // Charger les données pour les éléments ViewModel
        protected override async void OnNavigatedTo(NavigationEventArgs e)
        {
            VM.Server = NavigationContext.SafeQueryString(Parameters.server);
            VM.Key = NavigationContext.SafeQueryString(Parameters.key);
            VM.Ville = NavigationContext.SafeQueryString(Parameters.ville);
            VM.Ligne = NavigationContext.SafeQueryString(Parameters.ligne);
            VM.Sens = NavigationContext.SafeQueryString(Parameters.sens);
            VM.Arret = NavigationContext.SafeQueryString(Parameters.code);

            if (VM.IsDataLoading)
            {
                await VM.LoadDataAsync(false);
            }

            //if (e.NavigationMode == NavigationMode.Back && ColorPickerPage.SelectedColorItem != null)
            //{
            //    await AddPin();
            //    ColorPickerPage.SelectedColorItem = null;
            //}
            
        }

        //private async Task AddPin()
        //{
        //    string arret = VM.Arret;
        //    string ligne = VM.Ligne;
        //    string sens = VM.Sens;
        //    string url = BaseUri + "?" + TimeoTools.GetParameters(server: VM.Server, key: VM.Key, ville: VM.Ville, xml: "1", ligne: VM.Ligne, sens: VM.Sens, code: VM.Arret).EscapeAndConcat();
        //    //var filename = "/Shared/ShellContent/" + Guid.NewGuid().ToString() + ".jpg";
        //    var result = await TimeoTools.GetArretsAsync(server: VM.Server, key: VM.Key, ville: VM.Ville, sens: VM.Sens, ligne: VM.Ligne, arret: VM.Arret, refresh: true);
        //    Color backgroundColor = (ColorPickerPage.SelectedColorItem.Color as SolidColorBrush).Color;
        //    Color foregroundColor = Colors.White;

        //    //WP8Tools.PinTextTile(VM.Items[0].Ligne, "vers " + VM.Items[0].Vers, null, url, filename, backgroundBrush, foregroundColor);
        //    FavoriteHelper.AddFavorite(new Favorite()
        //    {
        //        Title = VM.xmldata.als[0].ligne.nom,
        //        Line1 = "vers " + VM.xmldata.als[0].ligne.vers,
        //        Line2 = null,
        //        Uri = url,
        //        //ImageSrc = filename,
        //        Background = backgroundColor,
        //        Foreground = foregroundColor
        //    });
        //    //.PinTextTile(VM.xmldata.als[0].ligne.nom, "vers " + VM.xmldata.als[0].ligne.vers, null, url, filename, backgroundColor, foregroundColor);
        //}

        private void LongListSelector_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (LLS_Arrets.SelectedItem == null) return;
            //ArretViewModel vm = LLS_Arrets.SelectedItem as ArretViewModel;
            als als = LLS_Arrets.SelectedItem as als;
            NavigationService.Navigate(new Uri(HorairesPage.BaseUri + "?" + TimeoTools.GetParameters(server: VM.Server, key: VM.Key, ville: VM.Ville, refs: als.refs).EscapeAndConcat(), UriKind.Relative));
            LLS_Arrets.SelectedItem = null;
        }

        async void appBarButtonRefresh_Click(object sender, EventArgs e)
        {
            await VM.LoadDataAsync(true);
            //SpeechReco();
        }

        private void appBarMenuItemReverse_Click(object sender, EventArgs e)
        {
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
            String url = ArretsPage.BaseUri + "?" + TimeoTools.GetParameters(server: VM.Server, key: VM.Key, ville: VM.Ville, ligne: ligne, sens: sens).EscapeAndConcat();
            NavigationService.Navigate(new Uri(url, UriKind.Relative));
        }

        private void appBarMenuItemLines_Click(object sender, EventArgs e)
        {
            String url = LignesPage.BaseUri + "?" + TimeoTools.GetParameters(server: VM.Server, key: VM.Key, ville: VM.Ville).EscapeAndConcat();
            NavigationService.Navigate(new Uri(url, UriKind.Relative));
        }

        //async void SpeechReco()
        //{
        //    SpeechRecognizerUI recoWithUI = new SpeechRecognizerUI();

        //    string[] lignes = (from arret in VM.Items select arret.Nom).ToArray();

        //    recoWithUI.Recognizer.Grammars.AddGrammarFromList("arrets", lignes);

        //    recoWithUI.Settings.ListenText = "Dites le nom de l'arrêt";
        //    recoWithUI.Settings.ExampleText = " 'rue de l'église', 'gare lille flandres', 'marcel paul' ";

        //    SpeechRecognitionUIResult recoFruit = await recoWithUI.RecognizeWithUIAsync();

        //}

        private void appBarButtonPin_Click(object sender, EventArgs e)
        {
            string url = BaseUri + "?" + TimeoTools.GetParameters(server: VM.Server, key: VM.Key, ville: VM.Ville, xml: "1", ligne: VM.Ligne, sens: VM.Sens, code: VM.Arret).EscapeAndConcat();

            NavigationService.Navigate(new Uri(ColorPickerPage.BaseUri + "?" + ColorPickerPage.GetParameters(Title: VM.xmldata.als[0].ligne.nom, Line1: "vers " + VM.xmldata.als[0].ligne.vers, Line2: null, Uri: url).EscapeAndConcat(), UriKind.Relative));
        }
    }
}