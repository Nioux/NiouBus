using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using System.Xml.Serialization;

namespace NiouBusEngine.Timeo
{
    public class Parameters
    {
        public const string server = "server";
        public const string key = "key";
        public const string ville = "ville";
        public const string xml = "xml";
        public const string ligne = "ligne";
        public const string sens = "sens";
        public const string code = "code";
        public const string refs = "refs";
    }

    [XmlRoot]
    public class xmldata
    {
        public erreur erreur { get; set; }
        public String heure { get; set; }
        public String date { get; set; }
        public String expire { get; set; }
        public reseau reseau { get; set; }
        [XmlArray("horaires")]
        [XmlArrayItem("horaire", typeof(horaire))]
        public horaire[] horaire { get; set; }
        [XmlArray("alss")]
        [XmlArrayItem("als", typeof(als))]
        public List<als> als { get; set; }
    }

    public class erreur
    {
        [XmlAttribute]
        public String code { get; set; }
    }

    public class reseau
    {
        public String titre { get; set; }
        public String texte { get; set; }
        public bool bloquant { get; set; }
    }

    public class horaire
    {
        [XmlAttribute]
        public String id { get; set; }
        public description description { get; set; }
        [XmlArray("passages")]
        [XmlArrayItem("passage", typeof(passage))]
        public passage[] passage { get; set; }
        [XmlArray("messages")]
        [XmlArrayItem("message", typeof(message))]
        public message[] message { get; set; }
    }

    public class description
    {
        public String code { get; set; }
        public String arret { get; set; }
        public String ligne { get; set; }
        public String ligne_nom { get; set; }
        public String sens { get; set; }
        public String vers { get; set; }
        public String couleur { get; set; }
    }

    public class passage
    {
        [XmlAttribute]
        public String id { get; set; }
        public String duree { get; set; }
        public String destination { get; set; }
    }

    public class message
    {
        [XmlAttribute]
        public String id { get; set; }
        [XmlAttribute]
        public String type { get; set; }
        public String titre { get; set; }
        public String texte { get; set; }
        public String bloquant { get; set; }
    }

    public class als : IComparable
    {
        [XmlAttribute]
        public String id { get; set; }
        public arret arret { get; set; }
        public ligne ligne { get; set; }
        public String refs { get; set; }

        public int CompareTo(object obj)
        {
            return ligne.nom.CompareTo((obj as als).ligne.nom);
        }
    }

    public class arret
    {
        public String code { get; set; }
        public String nom { get; set; }
    }

    public class ligne
    {
        public String code { get; set; }
        public String nom { get; set; }
        public String sens { get; set; }
        public String vers { get; set; }
        public String couleur { get; set; }
    }



    public class TimeoTools
    {
        public static Dictionary<string, object> GetParameters(
        string key,
        string ville,
        string ligne = null,
        string sens = null,
        string code = null,
        string server = null,
        string refs = null,
        string xml = null
        )
        {
            Dictionary<string, object> parameters = new Dictionary<string, object>()
            {
                { "server", server },
                { "key", key },
                { "ville", ville },
                { "ligne", ligne },
                { "sens", sens },
                { "code", code },
                { "refs", refs },
                { "xml", xml}
            };
            return parameters;
        }

        public static async Task<T> GetAsync<T>(
            string server,
            string key,
            string ville,
            string xml,
            string sens = null,
            string ligne = null,
            string code = null,
            string refs = null
            ) where T : class
        {
            Dictionary<string, object> parameters = new Dictionary<string, object>() 
            {
                {"service", "api"},
                {"key", key},
                {"ville", ville},
                {"xml", xml},
                {"sens", sens},
                {"ligne", ligne},
                {"code", code},
                {"refs", refs}
            };
            string url = string.Format(Constants.TimeoUrlPattern, server, parameters.EscapeAndConcat());
            return await Tools.GetXmlDataAsync<T>(url, true);
        }



        public static async Task<xmldata> GetLignesAsync(string server, string key, string ville, bool refresh)
        {
            return await GetAsync<xmldata>(
                server: server,
                key: key,
                ville: ville,
                xml: "1");
        }
        public static async Task<xmldata> GetArretsAsync(string server, string key, string ville, string sens, string ligne, string arret, bool refresh)
        {
            return await GetAsync<xmldata>(
                server: server,
                key: key,
                ville: ville,
                xml: "1",
                sens: sens,
                ligne: ligne,
                code: arret);
        }
        public static async Task<xmldata> GetArretAsync(string server, string key, string ville, String sens, String ligne, String code, bool refresh)
        {
            return await GetAsync<xmldata>(
                server: server,
                key: key,
                ville: ville,
                xml: "1",
                sens: sens,
                ligne: ligne,
                code: code);
        }
        public static async Task<List<xmldata>> GetPassagesAsync(string server, string key, string ville, String refs, bool refresh)
        {
            List<xmldata> xmldatas = new List<xmldata>();
            if (refs != null)
            {
                string[] arefs = refs.Split(new char[] { ',' });
                foreach (string iref in arefs)
                {
                    var passages = await GetAsync<xmldata>(
                        server: server,
                        key: key,
                        ville: ville,
                        xml: "3",
                        refs: refs);
                    if (passages != null)
                    {
                        xmldatas.Add(passages);
                    }
                }
            }
            return xmldatas;
        }
    }
}
