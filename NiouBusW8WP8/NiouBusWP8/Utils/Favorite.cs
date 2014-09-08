using System;
using System.Collections.Generic;
using System.IO;
using System.IO.IsolatedStorage;
using System.Linq;
using System.Runtime.Serialization;
using System.Security.Cryptography;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Windows.Media;
using System.Windows.Media.Imaging;

namespace NiouBusWP8
{
    [DataContract]
    public class Favorite
    {
        [DataMember]
        public string Title { get; set; }
        [DataMember]
        public string Line1 { get; set; }
        [DataMember]
        public string Line2 { get; set; }
        [DataMember]
        public string Uri { get; set; }
        //[DataMember]
        //public string ImageSrc { get; set; }
        [IgnoreDataMember]
        public string ImageSrc 
        { 
            get 
            {
                return GetShellContentString(Uri);
            } 
        }
        [IgnoreDataMember]
        public BitmapImage Image
        {
            get
            {
                if (ImageSrc.StartsWith("/Shared/"))
                {
                    BitmapImage bi = new BitmapImage();

                    using (IsolatedStorageFile myIsolatedStorage = IsolatedStorageFile.GetUserStoreForApplication())
                    {
                        using (IsolatedStorageFileStream fileStream = myIsolatedStorage.OpenFile(ImageSrc, FileMode.Open, FileAccess.Read))
                        {
                            bi.SetSource(fileStream);
                        }
                    }
                    return bi;
                }
                return new BitmapImage(new Uri(ImageSrc, UriKind.RelativeOrAbsolute));
            }
        }
        [IgnoreDataMember]
        public Color Background { get; set; }
        [IgnoreDataMember]
        public Color Foreground { get; set; }

        [DataMember]
        public string BackgroundName 
        { 
            get
            {
                return Background.ToString();
            }
            set
            {
                Background = ColorFromHtml(value);
            }
        }
        [DataMember]
        public string ForegroundName
        { 
            get
            {
                return Foreground.ToString();
            }
            set
            {
                Foreground = ColorFromHtml(value);
            }
        }

        public static string GetHashString(string source)
        {
            SHA256 mySHA256 = new SHA256Managed();
            byte[] bytesHash = mySHA256.ComputeHash(System.Text.Encoding.UTF8.GetBytes(source));
            return BitConverter.ToString(bytesHash).Replace("-", string.Empty);
        }

        public static string GetShellContentString(string uri)
        {
            string filename = GetHashString(uri);
            return "/Shared/ShellContent/" + filename;
        }

        public static Color ColorFromHtml( string s)
        {
            // Check fore a valid code (RGB = 6 chars, ARGB = 8 chars)
            var regex = new Regex(@"^[A-Fa-f0-9]*$");
            if (s == null || ((s = s.Trim().TrimStart('#')).Length != 6 && s.Length != 8) || !regex.IsMatch(s))
                return Colors.White;
 
            int index = 0;
 
            // If the color length is 8, the first 2 chars contain the alpha part
            byte a = 255;
            if (s.Length == 8)
            {
                a = Convert.ToByte(s.Substring(0, 2), 16);
                index += 2;
            }
 
            // Get R value
            byte r = Convert.ToByte(s.Substring(index, 2), 16);
            index += 2;
            // Get G value
            byte g = Convert.ToByte(s.Substring(index, 2), 16);
            index += 2;
            // Get B value
            byte b = Convert.ToByte(s.Substring(index, 2), 16);
 
            return Color.FromArgb(a, r, g, b);
        }
        public override bool Equals(object obj)
        {
            if (obj == null) return false;
            return this.Uri.Equals((obj as Favorite).Uri);
        }

        public override int GetHashCode()
        {
            return this.Uri.GetHashCode();
        }
    }
}
