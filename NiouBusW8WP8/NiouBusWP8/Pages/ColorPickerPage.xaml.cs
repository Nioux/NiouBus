using Microsoft.Phone.Controls;
using System.Collections.Generic;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using NiouBusWP8;
using NiouBusEngine;

namespace NiouBusWP8
{
    public partial class ColorPickerPage : PhoneApplicationPage
    {
        public const string BaseUri = "/Pages/ColorPickerPage.xaml";

        static string[] colorNames =
        {
            "Transparent",
            "Lime",
            "Green",
            "Emerald",
            "Teal",
            "Cyan",
            "Cobalt",
            "Indigo",
            "Violet",
            "Pink",
            "Magenta",
            "Crimson",
            "Red",
            "Orange",
            "Amber",
            "Yellow",
            "Brown",
            "Olive",
            "Steel",
            "Mauve",
            "Taupe"
        };

        static uint[] uintColors =
        { 
            0x00FFFFFF,
            0xCCA4C400,
            0xCC60A917,
            0xCC008A00,
            0xCC00ABA9,
            0xCC1BA1E2,
            0xCC0050EF,
            0xCC6A00FF,
            0xCCAA00FF,
            0xCCF472D0,
            0xCCD80073,
            0xCCA20025,
            0xCCE51400,
            0xCCFA6800, 
            0xCCF0A30A,
            0xCCE3C800,
            0xCC825A2C,
            0xCC6D8764,
            0xCC647687,
            0xCC76608A,
            0xCC87794E
        };

        public ColorItem SelectedColorItem { get; set; }

        public string Title { get; set; }
        public string Line1 { get; set; }
        public string Line2 { get; set; }
        public string Uri { get; set; }

        public static Dictionary<string, object> GetParameters(string Title, string Line1, string Line2, string Uri)
        {
            return new Dictionary<string, object>()
            {
                {"Title", Title},
                {"Line1", Line1},
                {"Line2", Line2},
                {"Uri", Uri},
            };
        }

        public ColorPickerPage()
        {
            InitializeComponent();

            this.Loaded += ColorPickerPage_Loaded;
        }

        protected override void OnNavigatedTo(System.Windows.Navigation.NavigationEventArgs e)
        {
            base.OnNavigatedTo(e);

            Title = NavigationContext.SafeQueryString("Title");
            Line1 = NavigationContext.SafeQueryString("Line1");
            Line2 = NavigationContext.SafeQueryString("Line2");
            Uri = NavigationContext.SafeQueryString("Uri");
        }

        private void ColorPickerPage_Loaded(object sender, RoutedEventArgs e)
        {
            List<ColorItem> item = new List<ColorItem>();
            for (int i = 0; i < uintColors.Length; i++)
            {
                item.Add(new ColorItem() { Text = colorNames[i], Color = ConvertColor(uintColors[i]) });
            };

            listBox.ItemsSource = item; //Fill ItemSource with all colors
        }

        private Brush ConvertColor(uint uintCol)
        {
            byte A = (byte)((uintCol & 0xFF000000) >> 24);
            byte R = (byte)((uintCol & 0x00FF0000) >> 16);
            byte G = (byte)((uintCol & 0x0000FF00) >> 8);
            byte B = (byte)((uintCol & 0x000000FF) >> 0);

            return new SolidColorBrush(Color.FromArgb(A, R, G, B));
        }

        private void lstColor_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (e.AddedItems.Count > 0)
            {
                SelectedColorItem = ((ColorItem)e.AddedItems[0]);
                AddPin();
                this.NavigationService.GoBack();
            }
        }

        private void AddPin()
        {
            Color backgroundColor = (SelectedColorItem.Color as SolidColorBrush).Color;
            Color foregroundColor = Colors.White;

            FavoriteHelper.AddFavorite(new Favorite()
            {
                Title = this.Title,
                Line1 = this.Line1,
                Line2 = this.Line2,
                Uri = this.Uri,
                Background = backgroundColor,
                Foreground = foregroundColor
            });
        }
    }
}