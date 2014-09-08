using Microsoft.Phone.Controls;
using System.Collections.Generic;
using System.Windows.Media;

namespace NiouBusWP8.Pages
{
    public partial class TileDesignerPage : PhoneApplicationPage
    {
        public const string BaseUri = "/Pages/TileDesignerPage.xaml";

        public TileDesignerPage()
        {
            InitializeComponent();

            List<ColorItem> item = new List<ColorItem>();
            for (int i = 0; i < uintColors.Length; i++)
            {
                item.Add(new ColorItem() { Text = colorNames[i], Color = ConvertColor(uintColors[i]) });
            };

            listPickerColor.ItemsSource = item; //Fill ItemSource with all colors
        }

        private Brush ConvertColor(uint uintCol)
        {
            byte A = (byte)((uintCol & 0xFF000000) >> 24);
            byte R = (byte)((uintCol & 0x00FF0000) >> 16);
            byte G = (byte)((uintCol & 0x0000FF00) >> 8);
            byte B = (byte)((uintCol & 0x000000FF) >> 0);

            return new SolidColorBrush(Color.FromArgb(A, R, G, B));
        }

        static string[] colorNames =
        {
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
            0xFFA4C400,
            0xFF60A917,
            0xFF008A00,
            0xFF00ABA9,
            0xFF1BA1E2,
            0xFF0050EF,
            0xFF6A00FF,
            0xFFAA00FF,
            0xFFF472D0,
            0xFFD80073,
            0xFFA20025,
            0xFFE51400,
            0xFFFA6800,
            0xFFF0A30A,
            0xFFE3C800,
            0xFF825A2C,
            0xFF6D8764,
            0xFF647687,
            0xFF76608A,
            0xFF87794E
        };

        public static ColorItem SelectedColorItem { get; set; }

    }
}