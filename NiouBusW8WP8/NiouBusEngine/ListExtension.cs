using System;
using System.Collections;
using System.Collections.Generic;

namespace NiouBusEngine
{
    public static class ListExtension
    {
        public static void BubbleSort(this IList o)
        {
            for (int i = o.Count - 1; i >= 0; i--)
            {
                for (int j = 1; j <= i; j++)
                {
                    object o1 = o[j - 1];
                    object o2 = o[j];
                    if (((IComparable)o1).CompareTo(o2) > 0)
                    {
                        o.Remove(o1);
                        o.Insert(j, o1);
                    }
                }
            }
        }

        public static string EscapeAndConcat(this Dictionary<string, object> parameters)
        {
            string url = string.Empty;
            foreach (var param in parameters)
            {
                if (param.Value != null)
                {
                    url += param.Key + "=" + Uri.EscapeDataString(param.Value.ToString()) + "&";
                }
            }
            return url;
        }

    }

}