using System;
using System.Collections.ObjectModel;
using System.Threading.Tasks;

namespace NiouBusEngine
{
    public class LineListViewModel : BaseViewModel
    {
        public LineListViewModel()
        {
            this.Items = new ObservableCollection<LineViewModel>();
        }

        public ObservableCollection<LineViewModel> Items { get; private set; }

        private string _Server;
        public string Server
        {
            get
            {
                return _Server;
            }
            set
            {
                Set(ref _Server, value);
            }
        }

        private string _Network;
        public string Network
        {
            get
            {
                return _Network;
            }
            set
            {
                Set(ref _Network, value);
            }
        }

        private String _StopAreaExternalCode;
        public String StopAreaExternalCode
        {
            get
            {
                return _StopAreaExternalCode;
            }
            set
            {
                Set(ref _StopAreaExternalCode, value);
            }
        }

        private bool _IsDataLoading = true;
        public bool IsDataLoading
        {
            get
            {
                return _IsDataLoading;
            }
            private set 
            {
                Set(ref _IsDataLoading, value);
            }
        }

        public async Task<bool> LoadDataAsync(bool refresh)
        {
            this.IsDataLoading = true;

            var xd = await Navitia.NavitiaTools.GetAsync<Navitia.ActionLineList>(
                Server: Server,
                Action: Navitia.Action.LineList,
                NetworkExternalCode: Network,
                StopAreaExternalCode: StopAreaExternalCode);
            if (xd == null)
            {
                this.IsDataLoading = false;
                return false;
            }

            this.Items.Clear();
            foreach (var line in xd.LineList.Line)
            {
                if (!String.IsNullOrWhiteSpace(line.Forward.ForwardName))
                {
                    this.Items.Add(new LineViewModel()
                    {
                        LineExternalCode = line.LineExternalCode,
                        LineCode = line.LineCode,
                        LineName = line.LineName,
                        Direction = "1",
                        DirectionName = line.Forward.ForwardName,
                        Coord = line.Forward.Direction.StopArea.Coord,
                    });
                }
                if (!String.IsNullOrWhiteSpace(line.Backward.BackwardName))
                {
                    this.Items.Add(new LineViewModel()
                    {
                        LineExternalCode = line.LineExternalCode,
                        LineCode = line.LineCode,
                        LineName = line.LineName,
                        Direction = "-1",
                        DirectionName = line.Backward.BackwardName,
                        Coord = line.Backward.Direction.StopArea.Coord,
                    });
                }
            }
            this.Items.BubbleSort();
                        
            this.IsDataLoading = false;

            return true;
        }
    }
}
