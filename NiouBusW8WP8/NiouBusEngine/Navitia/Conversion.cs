using System;

namespace NiouBusEngine
{
    public class Conversion
    {
        // http://professionnels.ign.fr/DISPLAY/000/526/701/5267019/NTG_71.pdf paramètres.
        // http://professionnels.ign.fr/DISPLAY/000/526/702/5267024/NTG_80.pdf conversions de coordonnées.
        // http://geodesie.ign.fr/contenu/fichiers/documentation/pedagogiques/transfo.pdf p12 paramètres.
        // http://www.forumsig.org/showthread.php?p=64050#post64050
        public class CoordinateConverter
        {
            public CoordinateConverter()
            {
                this.init1();
            }
            // Paramètres de l'ellipsoïde de Clarke de 1880 utilisé pour le système géodésique NTF.
            public CoordinateConverterParameters clarkeEllipsoid;

            public CoordinateConverterParameters wgs84Ellipsoid;

            // Paramètres de la projection Lambert II étendue dans le système géodésique NTF.
            public CoordinateConverterParameters lambertIIE;


            public double deg2rad(double deg)
            {
                //return Math.ToRadians(deg);
                return (Math.PI / 180) * deg;
            }
            public double rad2deg(double rad)
            {
                //return Math.ToDegrees(rad);
                return rad * (180.0 / Math.PI);
            }

            // Initialisation des constantes.
            public void init1()
            {
                this.clarkeEllipsoid = new CoordinateConverterParameters(); // Valeurs provenant du fichier Data.txt ligne 6 de Circé France 4.
                this.clarkeEllipsoid.a = 6378249.2;
                this.clarkeEllipsoid.b = 6356515;
                this.clarkeEllipsoid.f = 1 / 293.4660213;
                this.clarkeEllipsoid.e2 = 0.006803487646;
                this.clarkeEllipsoid.e = Math.Sqrt(0.006803487646);
                this.clarkeEllipsoid.T = new CoordinateConverterParameters(); // Valeurs provenant du fichier Data.txt ligne 44 de Circé France 4.
                this.clarkeEllipsoid.T.x = -168;
                this.clarkeEllipsoid.T.y = -60;
                this.clarkeEllipsoid.T.z = 320;

                this.wgs84Ellipsoid = new CoordinateConverterParameters(); // Valeurs provenant du fichier Data.txt ligne 7 de Circé France 4.
                this.wgs84Ellipsoid.a = 6378137;
                this.wgs84Ellipsoid.b = 6356752.3141;
                this.wgs84Ellipsoid.f = 1 / 298.257223563;
                this.wgs84Ellipsoid.e2 = 0.006694380025;
                this.wgs84Ellipsoid.e = Math.Sqrt(0.006694380025);

                this.lambertIIE = new CoordinateConverterParameters(); // Valeurs provenant du fichier Data.txt ligne 57 de Circé France 4.
                this.lambertIIE.X0 = 600000;
                this.lambertIIE.Y0 = 2200000;
                this.lambertIIE.phi0 = deg2rad(52 * 0.9);
                this.lambertIIE.phi1 = deg2rad(50.99879884 * 0.9);
                this.lambertIIE.phi2 = deg2rad(52.99557167 * 0.9);
                this.lambertIIE.lambda0 = deg2rad((14.025 / 60 + 20) / 60 + 2);
                this.lambertIIE.scale = 0.99987742;

                // Valeurs provenant de la dernière page de NTG_71.pdf
                this.lambertIIE.n = 0.7289686274; // sin(self::$lambertIIE->phi0);
                this.lambertIIE.c = 11745793.39; // $tmp * exp(self::$lambertIIE->n * self::L(self::$lambertIIE->phi0, self::$clarkeEllipsoid->e));
                this.lambertIIE.Xs = 600000; // self::$lambertIIE->X0;
                this.lambertIIE.Ys = 8199695.768; // self::$lambertIIE->Y0 + $tmp;
                double Lamb_vo1 = this.clarkeEllipsoid.a / Math.Sqrt(1 - this.clarkeEllipsoid.e2 * (Math.Pow(Math.Sin(this.lambertIIE.phi1), 2)));
                double Lamb_vo2 = this.clarkeEllipsoid.a / Math.Sqrt(1 - this.clarkeEllipsoid.e2 * (Math.Pow(Math.Sin(this.lambertIIE.phi2), 2)));
                double Lamb_po1 = this.clarkeEllipsoid.a * (1 - this.clarkeEllipsoid.e2) / Math.Pow(Math.Sqrt(1 - this.clarkeEllipsoid.e2 * Math.Pow(Math.Sin(this.lambertIIE.phi1), 2)), 3);
                double Lamb_po2 = this.clarkeEllipsoid.a * (1 - this.clarkeEllipsoid.e2) / Math.Pow(Math.Sqrt(1 - this.clarkeEllipsoid.e2 * Math.Pow(Math.Sin(this.lambertIIE.phi2), 2)), 3);
                double Lamb_m1 = 1 + Lamb_po1 / 2 / Lamb_vo1 * Math.Pow(((this.lambertIIE.phi1 - this.lambertIIE.phi0)), 2);
                double Lamb_m2 = 1 + Lamb_po2 / 2 / Lamb_vo2 * Math.Pow(((this.lambertIIE.phi2 - this.lambertIIE.phi0)), 2);
                double Lamb_m = (Lamb_m1 + Lamb_m2) / 2;
                double Lamb_mL = 2 - Lamb_m;
                double Lamb_v0 = this.clarkeEllipsoid.a / Math.Sqrt(1 - this.clarkeEllipsoid.e2 * (Math.Pow(Math.Sin(this.lambertIIE.phi0), 2)));
                double Lamb_R0 = Lamb_v0 / Math.Tan(this.lambertIIE.phi0);
                // mLR0 est le Rayon du parallèle d origine après réduction d echelle
                this.lambertIIE.Lamb_mLR0 = Lamb_mL * Lamb_R0;
            }

            public class XY
            {
                public double X;
                public double Y;
                public XY(double x, double y)
                {
                    this.X = x;
                    this.Y = y;
                }
            }

            public class XYZ
            {
                public double X;
                public double Y;
                public double Z;
                public XYZ(double x, double y, double z)
                {
                    this.X = x;
                    this.Y = y;
                    this.Z = z;
                }
            }

            public class PhiLambda
            {
                public double Phi;
                public double Lambda;
                public PhiLambda(double phi, double lambda)
                {
                    this.Phi = phi;
                    this.Lambda = lambda;
                }
            }

            // Effectue les étapes de la transformation des coordonnées WGS84 géographiques vers la projection Lambert II étendue.
            public XY convertFromWGS84(double lat, double lng)
            {
                double phi = deg2rad(lat);
                double lambda = deg2rad(lng);

                XYZ xyz = this.convertGeographicToCartesian(phi, lambda, this.wgs84Ellipsoid);
                xyz = this.translateForNTF(xyz.X, xyz.Y, xyz.Z);
                PhiLambda pl = this.convertCartesianToGeographic(xyz.X, xyz.Y, xyz.Z, this.clarkeEllipsoid);
                XY xy = this.convertToLambertIIExtended(pl.Phi, pl.Lambda);
                return new XY(xy.X, xy.Y);
            }

            public class LatLong
            {
                public double Lat;
                public double Long;
                public LatLong(double lat, double _long)
                {
                    this.Lat = lat;
                    this.Long = _long;
                }
            }

            // Effectue les étapes de la transformation des coordonnées de la projection Lambert II étendue vers WGS84 géographiques.
            public LatLong convertToWGS84(double X, double Y)
            {
                PhiLambda pl = this.convertFromLambertIIExtended(X, Y);
                XYZ xyz = this.convertGeographicToCartesian(pl.Phi, pl.Lambda, this.clarkeEllipsoid);
                xyz = this.translateForWGS84(xyz.X, xyz.Y, xyz.Z);
                pl = this.convertCartesianToGeographic(xyz.X, xyz.Y, xyz.Z, this.wgs84Ellipsoid);
                return new LatLong(rad2deg(pl.Phi), rad2deg(pl.Lambda));
            }

            // Translate les coordonnées cartésiennes de l'ellipsoïde du WGS84 vers l'ellipsoïde Clarke.
            public XYZ translateForNTF(double X, double Y, double Z)
            {
                X -= this.clarkeEllipsoid.T.x;
                Y -= this.clarkeEllipsoid.T.y;
                Z -= this.clarkeEllipsoid.T.z;
                return new XYZ(X, Y, Z);
            }

            // Translate les coordonnées cartésiennes de l'ellipsoïde Clarke vers l'ellipsoïde du WGS84.
            public XYZ translateForWGS84(double X, double Y, double Z)
            {
                X += this.clarkeEllipsoid.T.x;
                Y += this.clarkeEllipsoid.T.y;
                Z += this.clarkeEllipsoid.T.z;
                return new XYZ(X, Y, Z);
            }

            // Transformation de coordonnées géographiques en cartésiennes. ALG0009 de NTG_80.pdf
            public XYZ convertGeographicToCartesian(double phi, double lambda, CoordinateConverterParameters ellipsoid)
            {
                double N = this.N(phi, ellipsoid.a, ellipsoid.e2);
                double X = N * Math.Cos(phi) * Math.Cos(lambda);
                double Y = N * Math.Cos(phi) * Math.Sin(lambda);
                double Z = N * (1 - ellipsoid.e2) * Math.Sin(phi);
                return new XYZ(X, Y, Z);
            }

            // Transformation de coordonnées cartésiennes en géographiques. Algorithme page 4 de transfo.pdf. Donne les mêmes résultats que ALG0012 de NTG_80.pdf
            public PhiLambda convertCartesianToGeographic(double X, double Y, double Z, CoordinateConverterParameters ellipsoid)
            {
                double R = Math.Sqrt(Math.Pow(X, 2) + Math.Pow(Y, 2) + Math.Pow(Z, 2)); // Algo non itératif.
                double r = Math.Sqrt(Math.Pow(X, 2) + Math.Pow(Y, 2));
                double ae2 = ellipsoid.a * ellipsoid.e2;
                double lambda = Math.Atan(Y / X);
                double mu = Math.Atan(Z * ((1 - ellipsoid.f) + ae2 / R) / r);
                double phi = Math.Atan((Z * (1 - ellipsoid.f) + ae2 * Math.Pow(Math.Sin(mu), 3)) / ((1 - ellipsoid.f) * (r - ae2 * Math.Pow(Math.Cos(mu), 3))));
                return new PhiLambda(phi, lambda);
            }

            // Transformation de coordonnées géographiques NTF en projection de Lambert. ALG0003 de NTG_71.pdf
            public XY convertToLambertIIExtended(double phi, double lambda)
            {
                double Lamb_LatIso = Math.Log(Math.Tan(Math.PI / 4 + phi / 2)) - this.clarkeEllipsoid.e / 2 * Math.Log((1 + this.clarkeEllipsoid.e * Math.Sin(phi)) / (1 - this.clarkeEllipsoid.e * Math.Sin(phi)));
                double Lamb_LatIso0 = Math.Log(Math.Tan(Math.PI / 4 + this.lambertIIE.phi0 / 2)) - this.clarkeEllipsoid.e / 2 * Math.Log((1 + this.clarkeEllipsoid.e * Math.Sin(this.lambertIIE.phi0)) / (1 - this.clarkeEllipsoid.e * Math.Sin(this.lambertIIE.phi0)));
                double Lamb_Gamma = 0;
                if (lambda < Math.PI)
                {
                    Lamb_Gamma = (lambda - this.lambertIIE.lambda0) * Math.Sin(this.lambertIIE.phi0);
                }
                if (lambda > Math.PI)
                {
                    Lamb_Gamma = (lambda - this.lambertIIE.lambda0 - 2 * Math.PI) * Math.Sin(this.lambertIIE.phi0);
                }
                double Lamb_R = this.lambertIIE.Lamb_mLR0 * Math.Exp(-Math.Sin(this.lambertIIE.phi0) * (Lamb_LatIso - Lamb_LatIso0));
                double Lamb_E1 = Lamb_R * Math.Sin(Lamb_Gamma);
                double X = Lamb_E1 + this.lambertIIE.X0;
                double Y = this.lambertIIE.Lamb_mLR0 - Lamb_R + Lamb_E1 * Math.Tan(Lamb_Gamma / 2) + this.lambertIIE.Y0;
                return new XY(X, Y);
            }

            // Transformation de coordonnées en projection de Lambert en géographiques. ALG0004 de NTG_71.pdf
            public PhiLambda convertFromLambertIIExtended(double X, double Y)
            {
                double Lamb_Ls = Math.Log(Math.Tan(Math.PI / 4 + this.lambertIIE.phi0 / 2)) - this.clarkeEllipsoid.e / 2 * Math.Log((1 + this.clarkeEllipsoid.e * Math.Sin(this.lambertIIE.phi0)) / (1 - this.clarkeEllipsoid.e * Math.Sin(this.lambertIIE.phi0)));
                double dX = X - this.lambertIIE.X0;
                double dY = Y - this.lambertIIE.Y0;
                double gamma = Math.Atan(dX / (this.lambertIIE.Lamb_mLR0 - dY));
                double lambda = (gamma / Math.Sin(this.lambertIIE.phi0) + this.lambertIIE.lambda0);
                double Lamb_R = (this.lambertIIE.Lamb_mLR0 - dY) / Math.Cos(gamma);
                double Lamb_L0 = Math.Log(Math.Tan(Math.PI / 4 + this.lambertIIE.phi0 / 2)) - (this.clarkeEllipsoid.e / 2) * Math.Log((1 + this.clarkeEllipsoid.e * Math.Sin(this.lambertIIE.phi0)) / (1 - this.clarkeEllipsoid.e * Math.Sin(this.lambertIIE.phi0)));
                double Lamb_L = Lamb_L0 + Math.Log(this.lambertIIE.Lamb_mLR0 / Lamb_R) / Math.Sin(this.lambertIIE.phi0);
                double epsilon = 1;
                double phi = 2 * Math.Atan(Math.Exp(Lamb_L)) - Math.PI / 2;
                while (epsilon > 1e-10)
                {
                    double phi1 = 2 * (Math.Atan(Math.Exp(Lamb_L + this.clarkeEllipsoid.e / 2 * Math.Log((1 + this.clarkeEllipsoid.e * Math.Sin(phi)) / (1 - this.clarkeEllipsoid.e * Math.Sin(phi)))))) - Math.PI / 2;
                    epsilon = Math.Abs(phi1 - phi);
                    phi = phi1;
                }
                return new PhiLambda(phi, lambda);
            }

            // Calcul de la réciproque de la latitude isométrique. ALG0002 de NTG_71.pdf 
            /*		private static function Linv($L, $e) {
                        $phi0 = 2 * atan(exp($L)) - M_PI / 2;
                        $phiI = 2 * atan(pow((1 + $e * sin($phi0)) / (1 - $e * sin($phi0)), $e / 2) * exp($L)) - M_PI / 2;
                        while (abs($phiI - $phi0) > 1e-10) {
                            $phi0 = $phiI;
                            $phiI = 2 * atan(pow((1 + $e * sin($phi0)) / (1 - $e * sin($phi0)), $e / 2) * exp($L)) - M_PI / 2;
                        }
                        return $result;
                    }
            */
            // Calcul de la grande normale. ALG0021 de NTG_71.pdf
            private double N(double phi, double a, double e2)
            {
                return a / Math.Sqrt(1 - e2 * Math.Pow(Math.Sin(phi), 2));
            }

            // Calcul de la latitude isométrique. ALG0001 de NTG_71.pdf
            private static double L(double phi, double e)
            {
                return Math.Log(Math.Tan((Math.PI / 4) + (phi / 2))) * Math.Pow((1 - e * Math.Sin(phi)) / (1 + e * Math.Sin(phi)), e / 2);
            }
        }
        //tx_icslibnavitia_CoordinateConverter::init();

        public class CoordinateConverterParameters
        {
            public double a;
            public double b;
            public double f;
            public double e2;
            public double e;
            public CoordinateConverterParameters T;
            public double x;
            public double y;
            public double z;
            public double X0;
            public double Y0;
            public double phi0;
            public double phi1;
            public double phi2;
            public double lambda0;
            public double scale;
            public double n;
            public double c;
            public double Xs;
            public double Ys;
            public double Lamb_mLR0;
        }
    }
}
