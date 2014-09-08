using ImageTools;
using Microsoft.Phone.Shell;
using NiouBusWP8.Tiles;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.IO.IsolatedStorage;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;
using Windows.Storage;

namespace NiouBusWP8
{
    public class FavoriteHelper
    {
        public static void SaveFavorites(ObservableCollection<Favorite> favorites)
        {
            IsolatedStorageSettings.ApplicationSettings["favorites"] = favorites;
            IsolatedStorageSettings.ApplicationSettings.Save();
        }

        public static ObservableCollection<Favorite> LoadFavorites()
        {
            ObservableCollection<Favorite> favorites;
            if(IsolatedStorageSettings.ApplicationSettings.TryGetValue("favorites", out favorites))
            {
                return favorites;
            }
            return null;
        }

        public static void AddFavorite(Favorite favorite)
        {
            CreateFavoriteBitmap(favorite);
            AddFavoriteToMainPage(favorite);
            //AddFavoriteToDesktop(favorite);
        }

        public static void AddFavoriteToMainPage(Favorite favorite)
        {
            ObservableCollection<Favorite> favorites = LoadFavorites();
            Favorite old = favorites.Where(f => f.Uri == favorite.Uri).FirstOrDefault();
            if (old != null)
            {
                favorites.Remove(old);
            }
            favorites.Add(favorite);
            SaveFavorites(favorites);
        }

        public static void AddFavoriteToDesktop(Favorite favorite)
        {
            PinTileBitmap(favorite.Uri, favorite.ImageSrc);
        }

        public static string CreateFavoriteBitmap(Favorite favorite)
        {
            var tile = GetSimpleTile(favorite.Title, favorite.Line1, favorite.Line2, favorite.Background, favorite.Foreground);
            var bmp = GetUIElementBitmap(tile);
            SaveBitmap(bmp, favorite.ImageSrc);
            return favorite.ImageSrc;
        }

        public static string CreateAssetBitmap(string asset, string filename, Color backgroundColor, Color foregroundColor)
        {
            Canvas canvas = new Canvas();
            System.Windows.Application.LoadComponent(canvas, new System.Uri(asset, System.UriKind.Relative));
            canvas.Background = new SolidColorBrush(backgroundColor);
            (canvas.Children[0] as Path).Fill = new SolidColorBrush(foregroundColor);
            var bmp = GetUIElementBitmap(canvas);
            SaveBitmap(bmp, filename);
            return filename;
        }

        private static UserControl GetSimpleTile(string title, string line1, string line2, Color backgroundColor, Color foregroundColor)
        {
            var tile = new SimpleTile();
            tile.Title = title;
            tile.Line1 = line1;
            tile.Line2 = line2;
            tile.LayoutRoot.Background = new SolidColorBrush(backgroundColor);
            tile.Foreground = new SolidColorBrush(foregroundColor);
            return tile;
        }

        private static UserControl GetImageTile(string image, Color backgroundColor, Color foregroundColor)
        {
            var tile = new ImageTile();
            tile.Image = image;
            tile.LayoutRoot.Background = new SolidColorBrush(backgroundColor);
            tile.Foreground = new SolidColorBrush(foregroundColor);
            return tile;
        }

        private static WriteableBitmap GetUIElementBitmap(UIElement element, int width = 336, int height = 336)
        {
            // None of this actually does anything, but for some reason the StackPanel
            // won't render properly without it.
            Border image = new Border();
            image.Measure(new Size(width, height));
            image.Arrange(new Rect(0, 0, width, height));
            image.UpdateLayout();
            // End of function-less code.

            element.Measure(new Size(width, height));
            element.Arrange(new Rect(0, 0, width, height));
            element.UpdateLayout();

            var bmp = new WriteableBitmap(width, height);
            if (element.Clip != null)
            {
                bmp.Render(element, new ScaleTransform() { ScaleX = width / element.Clip.Bounds.Width, ScaleY = height / element.Clip.Bounds.Height }); //new TranslateTransform());
            }
            else
            {
                bmp.Render(element, new TranslateTransform());
            }
            bmp.Invalidate();

            return bmp;
        }

        private static void SaveBitmap(WriteableBitmap bmp, string filename)
        {
            using (var isf = IsolatedStorageFile.GetUserStoreForApplication())
            {
                if (!isf.DirectoryExists("/Shared/ShellContent"))
                {
                    isf.CreateDirectory("/Shared/ShellContent");
                }

                using (var stream = isf.OpenFile(filename, System.IO.FileMode.OpenOrCreate))
                {
                    ExtendedImage myImage = bmp.ToImage();
                    myImage.WriteToStream(stream, filename + ".png");
                    //bmp.SaveJpeg(stream, bmp.PixelWidth, bmp.PixelHeight, 0, 100);
                }
            }

        }

        private static void PinTileBitmap(string url, string filename)
        {
            foreach (var tile in ShellTile.ActiveTiles)
            {
                Debug.WriteLine(tile.NavigationUri);
            }
            Deployment.Current.Dispatcher.BeginInvoke(() =>
            {
                var data = new StandardTileData();
                data.BackBackgroundImage = data.BackgroundImage = new Uri("isostore:" + filename, UriKind.Absolute);
                data.BackContent = string.Empty;

                ShellTile TileToFind = ShellTile.ActiveTiles.FirstOrDefault(x => x.NavigationUri.ToString().Contains(url));
                if (TileToFind != null)
                {
                    TileToFind.Update(data);
                }
                else
                {
                    ShellTile.Create(new Uri(url, UriKind.Relative), data, false);
                }
            });
        }

    }
}
