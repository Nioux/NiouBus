using System;
using System.Windows.Data;
using System.Threading;
using System.Windows.Media;

namespace NiouBusWP8
{
    public class ColorToBrushConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            Brush brush = value as Brush;
            if(brush != null)
            {
                return brush;
            }
            Color? color = value as Color?;
            if (color.HasValue)
            {
                if (color.Value == Colors.Transparent)
                {
                    return new SolidColorBrush((Color)App.Current.Resources["ThemeAccentColor"]);
                }
                else
                {
                    return new SolidColorBrush(color.Value);
                }
            }
            return null;
        }

        public object ConvertBack(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}
