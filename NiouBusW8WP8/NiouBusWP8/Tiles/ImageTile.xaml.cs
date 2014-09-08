using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using System.Windows.Media.Imaging;
using System.Windows.Media;

namespace NiouBusWP8.Tiles
{
    public partial class ImageTile : UserControl
    {
        public ImageTile()
        {
            InitializeComponent();
        }

        public string Image
        {
            set
            {
                this.imgImage.Source = new BitmapImage(new Uri(value, UriKind.RelativeOrAbsolute));
            }
        }
    }
}
