using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;

namespace NiouBusWP8.Tiles
{
    public partial class SimpleTile : UserControl
    {
        public SimpleTile()
        {
            InitializeComponent();
        }

        public string Title 
        { 
            get
            {
                return tbTitle.Text;
            }
            set
            {
                tbTitle.Text = value != null ? value.ToLower() : null;
            }
        }

        public string Line1 
        {
            get
            {
                return tbLine1.Text;
            }
            set
            {
                tbLine1.Text = value != null ? value.ToLower() : null;
            }
        }

        public string Line2
        {
            get
            {
                return tbLine2.Text;
            }
            set
            {
                tbLine2.Text = value != null ? value.ToLower() : null;
            }
        }
    }
}
