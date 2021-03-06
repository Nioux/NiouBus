﻿using System;
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
        public static async Task<T> GetXmlDataAsync<T>(String url, bool refresh) where T : class
        {
            try
            {
                Debug.WriteLine(url);

                using (HttpClient client = new HttpClient())
                {
                    client.Timeout = new TimeSpan(0, 5, 0);
                    client.DefaultRequestHeaders.IfModifiedSince = new DateTimeOffset(DateTime.Parse("1970-01-01 09:00am +00"));

                    using (HttpRequestMessage request = new HttpRequestMessage(HttpMethod.Get, url))
                    {
                        request.Headers.CacheControl = new System.Net.Http.Headers.CacheControlHeaderValue();
                        request.Headers.CacheControl.NoCache = true;
                        request.Headers.CacheControl.NoStore = true;
                        //request.Method = HttpMethod.Post;
                        //request.Headers.Accept.Add(new System.Net.Http.Headers.MediaTypeWithQualityHeaderValue("application/json"));

                        using (HttpResponseMessage response = await client.SendAsync(request))
                        {
                            using (Stream responseStream = await response.Content.ReadAsStreamAsync())
                            {
                                //return responseStream.JsonDeserializeTo<T>();
                                return responseStream.XmlDeserializeTo<T>();
                            }
                        }
                    }
                }
            }
            catch(Exception ex)
            {
                Debug.WriteLine(ex);
                return null;
            }
        }
    }
}
