using Microsoft.Phone.Controls;
using NiouBusEngine;
using NiouBusEngine.Navitia;
using System;
using System.Windows.Media;
using System.Windows.Navigation;

namespace NiouBusWP8
{
    public partial class DepartureBoardListPage : BasePage
    {
        public const string BaseUri = "/Navitia/Pages/DepartureBoardListPage.xaml";
        private DepartureBoardListViewModel _VM;
        public DepartureBoardListViewModel VM
        {
            get
            {
                return _VM;
            }
            set
            {
                _VM = value;
            }
        }

        public DepartureBoardListPage()
        {
            VM = new DepartureBoardListViewModel();

            InitializeComponent();

            //DataContext = VM;
        }

        protected override async void OnNavigatedTo(NavigationEventArgs e)
        {
            if (VM.IsDataLoading)
            {
                VM.Server = NavigationContext.SafeQueryString(Parameters.Server);
                VM.Network = NavigationContext.SafeQueryString(Parameters.NetworkExternalCode);
                VM.LineExternalCode = NavigationContext.SafeQueryString(Parameters.LineExternalCode);
                VM.Direction = NavigationContext.SafeQueryString(Parameters.Direction);
                VM.StopAreaExternalCode = NavigationContext.SafeQueryString(Parameters.StopAreaExternalCode);
                await VM.LoadDataAsync(false);
            }
        }

        private void appBarButtonPin_Click(object sender, EventArgs e)
        {
            string url = DepartureBoardListPage.BaseUri + "?" + NavitiaTools.GetParameters(Server: VM.Server, NetworkExternalCode: VM.Network, LineExternalCode: VM.LineExternalCode, Direction: VM.Direction, StopAreaExternalCode: VM.StopAreaExternalCode).EscapeAndConcat();
            NavigationService.Navigate(new Uri(ColorPickerPage.BaseUri + "?" +
                ColorPickerPage.GetParameters(Title: VM.LineName, Line1: VM.StopPointName, Line2: VM.DirectionName, Uri: url).EscapeAndConcat(), UriKind.Relative));
        }

        private async void DatePicker_ValueChanged(object sender, DateTimeValueChangedEventArgs e)
        {
            if (e.OldDateTime.HasValue && e.NewDateTime.HasValue)
            {
                VM.Date = e.NewDateTime.Value;
                await VM.LoadDataAsync(true);
            }
        }

        private async void appBarButtonPrevious_Click(object sender, EventArgs e)
        {
            VM.Date = VM.Date.AddDays(-1);
            await VM.LoadDataAsync(true);
        }

        private async void appBarButtonNext_Click(object sender, EventArgs e)
        {
            VM.Date = VM.Date.AddDays(1);
            await VM.LoadDataAsync(true);
        }
    }
}