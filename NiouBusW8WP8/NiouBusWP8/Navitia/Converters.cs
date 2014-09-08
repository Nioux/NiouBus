using System;
using System.Collections.Generic;
using System.Device.Location;
using System.Diagnostics;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Data;
using NiouBusEngine;

namespace NiouBusWP8
{
    public class CoordConverter2 : IValueConverter
    {
        static Conversion.CoordinateConverter cc = new Conversion.CoordinateConverter();

        public virtual object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            NiouBusEngine.Navitia.Coord coord = value as NiouBusEngine.Navitia.Coord;
            if (coord == null || coord.CoordX == 0.0 || coord.CoordY == 0.0)
            {
                return DependencyProperty.UnsetValue;
            }
            Conversion.CoordinateConverter.LatLong ll = cc.convertToWGS84(coord.CoordX, coord.CoordY);
            //Debug.WriteLine(String.Format("{0} {1}", ll.Lat, ll.Long));
            return new GeoCoordinate(ll.Lat, ll.Long);
        }

        public virtual object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            //GeoCoordinate geocoord = value as GeoCoordinate;
            //return new NiouBusEngine.Navitia.Navitia.Coord() { CoordX = geocoord.Latitude, CoordY = geocoord.Longitude};
            //return null;
            throw new NotImplementedException();
        }

        public static NiouBusEngine.Navitia.Coord Convert(GeoCoordinate geocoord)
        {
            Conversion.CoordinateConverter.XY xy = cc.convertFromWGS84(geocoord.Latitude, geocoord.Longitude);
            return new NiouBusEngine.Navitia.Coord() { CoordX = xy.X, CoordY = xy.Y };
        }
    }

    public sealed class CoordConverter : CoordConverter2
    {
    }
}
