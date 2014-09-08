namespace NiouBusEngine
{
    public class HoraireViewModel : BaseViewModel
    {
        private string _Destination;
        public string Destination { get { return _Destination; } set { SetProperty(ref _Destination, value); } }

        private string _Duree;
        public string Duree { get { return _Duree; } set { SetProperty(ref _Duree, value); } }
    }
}