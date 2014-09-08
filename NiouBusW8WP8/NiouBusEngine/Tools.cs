using System;
using System.Diagnostics;
using System.IO;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Serialization;

namespace NiouBusEngine
{
    public class Tools
    {
        public static async Task<T> GetXmlDataAsync<T>(Stream responseStream) where T : class
        {
            using (StreamReader sr = new StreamReader(responseStream))
            {
                string received = await sr.ReadToEndAsync();

                XmlSerializer serializer = new XmlSerializer(typeof(T));
                byte[] byteArray = Encoding.UTF8.GetBytes(received);
                using (MemoryStream stream = new MemoryStream(byteArray))
                {
                    using (StreamReader reader = new StreamReader(stream))
                    {
                        return serializer.Deserialize(reader) as T;
                    }
                }
            }
        }

        public static T GetXmlData<T>(Stream responseStream) where T : class
        {
            using (StreamReader sr = new StreamReader(responseStream))
            {
                Task<string> task = sr.ReadToEndAsync();
                Task.WaitAll(task);
                string received = task.Result;

                XmlSerializer serializer = new XmlSerializer(typeof(T));
                byte[] byteArray = Encoding.UTF8.GetBytes(received);
                using (MemoryStream stream = new MemoryStream(byteArray))
                {
                    using (StreamReader reader = new StreamReader(stream))
                    {
                        return serializer.Deserialize(reader) as T;
                    }
                }
            }
        }

        public static async Task<T> GetXmlDataAsync<T>(String url, bool refresh) where T : class
        {
            try
            {
                Debug.WriteLine(url);

                using (HttpClient client = new HttpClient())
                {
                    client.Timeout = new TimeSpan(0, 5, 0);
                    using (HttpRequestMessage request = new HttpRequestMessage(HttpMethod.Get, url))
                    {
                        request.Headers.CacheControl = new System.Net.Http.Headers.CacheControlHeaderValue();
                        request.Headers.CacheControl.NoCache = true;
                        request.Headers.CacheControl.NoStore = true;
                        using (HttpResponseMessage response = await client.SendAsync(request))
                        {
                            using (Stream responseStream = await response.Content.ReadAsStreamAsync())
                            {
                                return await GetXmlDataAsync<T>(responseStream);
                            }
                        }
                    }
                }

                //HttpWebRequest request = WebRequest.CreateHttp(url);
                //if (refresh)
                //{
                    
                //    if (request.Headers == null)
                //    {
                //        request.Headers = new WebHeaderCollection();
                //    }
                //    request.Headers[HttpRequestHeader.IfModifiedSince] = DateTime.UtcNow.ToString();
                //}
                //using (WebResponse responseObject = await Task<WebResponse>.Factory.FromAsync(request.BeginGetResponse, request.EndGetResponse, request))
                //{
                //    using (Stream responseStream = responseObject.GetResponseStream())
                //    {
                //        return await GetXmlDataAsync<T>(responseStream);
                //    }
                //}
            }
            catch(Exception ex)
            {
                Debug.WriteLine(ex);
                return null;
            }
        }
        //public static Stream GetServerListStream(string countryIsoCode2ch)
        //{
        //    var flagname = "Full.DLL.Name.CountryFlags.{0}.png";
        //    var rs = Assembly.GetExecutingAssembly().GetManifestResourceStream(
        //               string.Format(flagname, countryIsoCode2ch));

        //    return rs;
        //}

    }
}
