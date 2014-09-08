using NiouBusEngine;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Navigation;


// Pour en savoir plus sur le modèle d'élément Page vierge, consultez la page http://go.microsoft.com/fwlink/?LinkId=234238

namespace NiouBusW8
{
    /// <summary>
    /// Une page vide peut être utilisée seule ou constituer une page de destination au sein d'un frame.
    /// </summary>
    public sealed partial class MainPage : Page
    {
        public MainPage()
        {
            this.InitializeComponent();

            MainViewModel vm = new MainViewModel();
            vm.Lignes = new LignesViewModel();
            vm.Arrets = new ArretsViewModel();
            vm.Horaires = new HorairesViewModel();
            DataContext = vm;
        }

        /// <summary>
        /// Invoqué lorsque cette page est sur le point d'être affichée dans un frame.
        /// </summary>
        /// <param name="e">Données d'événement décrivant la manière dont l'utilisateur a accédé à cette page. La propriété Parameter
        /// est généralement utilisée pour configurer la page.</param>
        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            //MainViewModel mvm = DataContext as MainViewModel;
            //await mvm.Lignes.LoadDataAsync(false);
        }

        private async void lvLignes_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (lvLignes.SelectedItem == null) return;
            LigneViewModel vm = lvLignes.SelectedItem as LigneViewModel;
            MainViewModel mvm = DataContext as MainViewModel;
            mvm.Arrets.Ligne = vm.Code;
            mvm.Arrets.Sens = vm.Sens;
            await mvm.Arrets.LoadDataAsync(false);
        }

        private async void lvArrets_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (lvArrets.SelectedItem == null) return;
            ArretViewModel vm = lvArrets.SelectedItem as ArretViewModel;
            MainViewModel mvm = DataContext as MainViewModel;
            mvm.Horaires.Refs = vm.Refs;
            await mvm.Horaires.LoadDataAsync(false);
        }

        private void lvHoraires_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {

        }

    }
}
