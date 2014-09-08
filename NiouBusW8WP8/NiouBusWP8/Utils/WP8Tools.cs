using Coding4Fun.Toolkit.Controls;
using Microsoft.Phone.Maps.Controls;
using Microsoft.Phone.Maps.Services;
using Microsoft.Phone.Maps.Toolkit;
using Microsoft.Phone.Shell;
using Microsoft.Phone.Tasks;
using NiouBusEngine;
using NiouBusWP8.Tiles;
using System;
using System.Collections.Generic;
using System.Device.Location;
using System.Diagnostics;
using System.IO.IsolatedStorage;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;

namespace NiouBusWP8
{
    public class WP8Tools
    {
        public class AddressSelector
        {
            private Map _Map;

            public AddressSelector(Map map)
            {
                this._Map = map;
            }

            public void SelectAddress()
            {
                AddressChooserTask addressChooserTask;
                addressChooserTask = new AddressChooserTask();
                addressChooserTask.Completed += new EventHandler<AddressResult>(addressChooserTask_Completed);
                addressChooserTask.Show();
            }

            private void addressChooserTask_Completed(object sender, AddressResult e)
            {
                if (e.TaskResult == TaskResult.OK)
                {
                    if (MessageBox.Show(e.Address, "Adresse", MessageBoxButton.OKCancel) == MessageBoxResult.OK)
                    {
                        GeocodeQuery query = new GeocodeQuery()
                        {
                            GeoCoordinate = new GeoCoordinate(0, 0),
                            SearchTerm = e.Address
                        };
                        query.QueryCompleted += query_QueryCompleted;
                        query.QueryAsync();
                    }
                }
            }

            private void query_QueryCompleted(object sender, QueryCompletedEventArgs<IList<MapLocation>> e)
            {
                if (e.Result.Count > 0)
                {
                    var coord = e.Result[0].GeoCoordinate;
                    _Map.Center = coord;
                    MapExtensions.GetChildren(_Map).OfType<UserLocationMarker>().First().GeoCoordinate = coord;
                    _Map.ZoomLevel = 15;
                }
                else
                {
                    MessageBox.Show("Adresse introuvable");
                }
            }

            public void EnterAddress()
            {
                InputPrompt prompt = new InputPrompt();
                prompt.Title = "Adresse";
                prompt.Message = "Entrez l'adresse recherchée :";
                //prompt.InputScope = new InputScope { Names = { new InputScopeName() { NameValue = InputScopeNameValue.AddressStreet } } };
                prompt.InputScope = new InputScope { Names = { new InputScopeName() { NameValue = InputScopeNameValue.Maps } } };
                prompt.Show();

                prompt.Completed += (pResult, sResult) =>
                {
                    if (!string.IsNullOrWhiteSpace(sResult.Result))
                    {
                        GeocodeQuery query = new GeocodeQuery()
                        {
                            GeoCoordinate = new GeoCoordinate(0, 0),
                            SearchTerm = sResult.Result
                        };
                        query.QueryCompleted += query_QueryCompleted;
                        query.QueryAsync();
                    }
                };
            }
        }
    }
}
