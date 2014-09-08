using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using System.Windows.Media;
using System.Collections.ObjectModel;
using System.Reflection;
using NiouBusEngine;
using System.ComponentModel;
using System.Runtime.CompilerServices;

namespace NiouBusWP8
{
    public partial class ColorPickerControl : UserControl, INotifyPropertyChanged
    {
        public event PropertyChangedEventHandler PropertyChanged;
        protected void NotifyPropertyChanged(String propertyName)
        {
            PropertyChangedEventHandler handler = PropertyChanged;
            if (null != handler)
            {
                handler(this, new PropertyChangedEventArgs(propertyName));
            }
        }
        protected bool SetProperty<T>(ref T storage, T value, [CallerMemberName] String propertyName = null)
        {
            if (Equals(storage, value)) return false;

            storage = value;
            NotifyPropertyChanged(propertyName);

            return true;
        }

        public delegate void ValueChangedEventHandler(object sender, ColorPickerEventArgs e);

        public event ValueChangedEventHandler ValueChanged;

        //private Color _SelectedColor;
        public Color SelectedColor 
        { 
            get
            {
                ColorSelectModel csm = ColorPicker.SelectedItem as ColorSelectModel;
                return csm.Color;
            }
            set
            {
                var si = Colors.Where(c => c.Color == value).FirstOrDefault();
                if (si != null)
                {
                    ColorPicker.SelectedItem = si;
                }
                //SetProperty(ref _SelectedColor, value);
            }
        }

        public ObservableCollection<ColorSelectModel> Colors { get; private set; }

        public ColorPickerControl()
        {
            var colors = new List<ColorSelectModel>();
            colors.AddRange(
                    typeof(Colors).GetProperties(BindingFlags.Static | BindingFlags.Public)
                        .Where(p => p.PropertyType == typeof(Color))
                        .Where(p => p.Name != "Transparent")
                        .Select(p => new ColorSelectModel(p.Name, (Color)p.GetValue(typeof(Colors), null))));

            Colors = new ObservableCollection<ColorSelectModel>(colors);

            InitializeComponent();

            DataContext = this;
        }

        public class ColorPickerEventArgs : EventArgs
        {
            public ColorSelectModel Color { get; set; }
        }

        public class ColorSelectModel
        {
            public ColorSelectModel(string text, Color color)
            {
                this.Text = text;
                this.Color = color;
                this.ColorBrush = new SolidColorBrush(color);
            }
            public string Text { get; set; }
            public Color Color { get; set; }
            public SolidColorBrush ColorBrush { get; set; }
        }

        private void ColorPicker_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (ColorPicker != null)
            {
                ColorSelectModel csm = ColorPicker.SelectedItem as ColorSelectModel;
                if(csm != null)
                {
                    SelectedColor = csm.Color;
                    if (ValueChanged != null)
                    {
                        ValueChanged(this, new ColorPickerEventArgs() { Color = csm });
                    }
                }
            }
        }
    }
}
