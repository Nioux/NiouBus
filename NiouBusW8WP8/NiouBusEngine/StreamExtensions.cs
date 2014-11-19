using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.Serialization.Json;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Serialization;

namespace NiouBusEngine
{
    public static class StreamExtensions
    {
        public static T XmlDeserializeTo<T>(this Stream stream) where T : class
        {
            return stream.XmlDeserializeTo<T>(Encoding.UTF8);
        }

        public static T XmlDeserializeTo<T>(this Stream stream, Encoding encoding) where T : class
        {
            XmlSerializer serializer = new XmlSerializer(typeof(T));
            using (StreamReader reader = new StreamReader(stream, encoding))
            {
                return serializer.Deserialize(reader) as T;
            }
        }

        public static T JsonDeserializeTo<T>(this Stream stream) where T : class
        {
            return stream.JsonDeserializeTo<T>(Encoding.UTF8);
        }

        public static T JsonDeserializeTo<T>(this Stream stream, Encoding encoding) where T : class
        {
            DataContractJsonSerializer serializer = new DataContractJsonSerializer(typeof(T));
            return serializer.ReadObject(stream) as T;
        }
    }
}
