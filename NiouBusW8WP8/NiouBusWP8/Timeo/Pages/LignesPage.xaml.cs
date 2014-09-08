using Microsoft.Phone.Controls;
using NiouBusEngine;
using System;
using System.Windows.Controls;
using System.Windows.Navigation;
using Windows.Phone.Speech.Recognition;
using System.Linq;
using NiouBusEngine.Timeo;

namespace NiouBusWP8
{
    public partial class LignesPage : PhoneApplicationPage
    {
        public const string BaseUri = "/Timeo/Pages/LignesPage.xaml";
        public LignesViewModel VM = new LignesViewModel();

        // Constructeur
        public LignesPage()
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
            if (VM.IsDataLoading)
            {
                await VM.LoadDataAsync(false);
            }
        }

        private void LongListSelector_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (LLS_Lignes.SelectedItem == null) return;
            //LigneViewModel vm = LLS_Lignes.SelectedItem as LigneViewModel;
            als als = LLS_Lignes.SelectedItem as als;
            NavigationService.Navigate(new Uri(ArretsPage.BaseUri + "?" + TimeoTools.GetParameters(server: VM.Server, key: VM.Key, ville: VM.Ville, ligne: als.ligne.code, sens: als.ligne.sens).EscapeAndConcat(), UriKind.Relative));
            LLS_Lignes.SelectedItem = null;
        }

        async void appBarButtonRefresh_Click(object sender, EventArgs e)
        {
            await VM.LoadDataAsync(true);
            //SpeechReco();
        }

        //async void SpeechReco()
        //{
        //    SpeechRecognizerUI recoWithUI = new SpeechRecognizerUI();

        //    //LignesViewModel vm = DataContext as LignesViewModel;
        //    string[] lignes = (from ligne in VM.Items select ligne.Nom).ToArray();

        //    recoWithUI.Recognizer.Grammars.AddGrammarFromList("lignes", lignes);

        //    recoWithUI.Settings.ListenText = "Dites le nom de la ligne";
        //    recoWithUI.Settings.ExampleText = " 'ligne 12', 'liane 1', 'corolle 2' ";

        //    SpeechRecognitionUIResult recoFruit = await recoWithUI.RecognizeWithUIAsync();

        //}

        private void appBarButtonPin_Click(object sender, EventArgs e)
        {
            Func<NetworkViewModel, bool> isNetwork = (NetworkViewModel net) => { return net.Server == VM.Server && net.TimeoVille == VM.Ville && net.TimeoKey == VM.Key; };
            var network = GlobalSettings.ServerList.Networks.First(nets => nets.Where(isNetwork).FirstOrDefault() != null).First(isNetwork);

            string url = BaseUri + "?" + TimeoTools.GetParameters(server: VM.Server, key: VM.Key, ville: VM.Ville).EscapeAndConcat();
            NavigationService.Navigate(new Uri(ColorPickerPage.BaseUri + "?" + ColorPickerPage.GetParameters(Title: network.NetworkName, Line1: "liste des lignes", Line2: null, Uri: url).EscapeAndConcat(), UriKind.Relative));
        }

        private void appBarButtonMap_Click(object sender, EventArgs e)
        {

        }
    }
}