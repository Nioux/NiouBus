using Microsoft.Phone.Controls;
using NiouBusEngine;
using NiouBusEngine.Navitia;
using System;
using System.Windows.Controls;
using System.Windows.Navigation;

namespace NiouBusWP8
{
    public partial class StopAreaListPage : BasePage
    {
        public const string BaseUri = "/Navitia/Pages/StopAreaListPage.xaml";
        public StopAreaListViewModel VM = new StopAreaListViewModel();

        public StopAreaListPage()
        {
            InitializeComponent();
            DataContext = VM;
        }

        protected override async void OnNavigatedTo(NavigationEventArgs e)
        {
            VM.Server = NavigationContext.SafeQueryString(Parameters.Server);
            VM.Network = NavigationContext.SafeQueryString(Parameters.NetworkExternalCode);
            VM.LineExternalCode = NavigationContext.SafeQueryString(Parameters.LineExternalCode);
            VM.StopAreaExternalCode = NavigationContext.SafeQueryString(Parameters.StopAreaExternalCode);

            if (VM.IsDataLoading)
            {
                await VM.LoadDataAsync(false);
            }
        }

        private void llsStopAreaList_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (llsStopAreaList.SelectedItem == null) return;
            StopAreaViewModel savm = llsStopAreaList.SelectedItem as StopAreaViewModel;
            NavigationService.Navigate(new Uri(DepartureBoardListPage.BaseUri + "?" + NavitiaTools.GetParameters(Server: VM.Server, NetworkExternalCode: VM.Network, LineExternalCode: VM.LineExternalCode, Direction: NavigationContext.SafeQueryString("Direction"), StopAreaExternalCode: savm.StopAreaExternalCode).EscapeAndConcat(), UriKind.Relative));
            llsStopAreaList.SelectedItem = null;
        }

        private void appBarButtonRefresh_Click(object sender, EventArgs e)
        {

        }

        private void appBarButtonPin_Click(object sender, EventArgs e)
        {

        }

    }
}