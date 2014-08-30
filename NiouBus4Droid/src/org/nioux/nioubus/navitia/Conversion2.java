package org.nioux.nioubus.navitia;

public class Conversion2
{
	// http://professionnels.ign.fr/DISPLAY/000/526/701/5267019/NTG_71.pdf param�tres.
	// http://professionnels.ign.fr/DISPLAY/000/526/702/5267024/NTG_80.pdf conversions de coordonn�es.
	// http://geodesie.ign.fr/contenu/fichiers/documentation/pedagogiques/transfo.pdf p12 param�tres.
	// http://www.forumsig.org/showthread.php?p=64050#post64050
	public static class CoordinateConverter {
		public CoordinateConverter() {
			this.init1();
		}
		// Param�tres de l'ellipso�de de Clarke de 1880 utilis� pour le syst�me g�od�sique NTF.
		public CoordinateConverterParameters clarkeEllipsoid;
		
		public CoordinateConverterParameters wgs84Ellipsoid;
		
		// Param�tres de la projection Lambert II �tendue dans le syst�me g�od�sique NTF.
		public CoordinateConverterParameters lambertIIE;
		
		
		public static double deg2rad(double deg) {
			return Math.toRadians(deg);
		}
		public static double rad2deg(double rad) {
			return Math.toDegrees(rad);
		}
		
		// Initialisation des constantes.
		public void init1() {
			this.clarkeEllipsoid = new CoordinateConverterParameters(); // Valeurs provenant du fichier Data.txt ligne 6 de Circ� France 4.
			this.clarkeEllipsoid.a = 6378249.2;
			this.clarkeEllipsoid.b = 6356515;
			this.clarkeEllipsoid.f = 1 / 293.4660213;
			this.clarkeEllipsoid.e2 = 0.006803487646;
			this.clarkeEllipsoid.e = Math.sqrt(0.006803487646);
			this.clarkeEllipsoid.T = new CoordinateConverterParameters(); // Valeurs provenant du fichier Data.txt ligne 44 de Circ� France 4.
			this.clarkeEllipsoid.T.x = -168;
			this.clarkeEllipsoid.T.y = -60;
			this.clarkeEllipsoid.T.z = 320;
			
			this.wgs84Ellipsoid = new CoordinateConverterParameters(); // Valeurs provenant du fichier Data.txt ligne 7 de Circ� France 4.
			this.wgs84Ellipsoid.a = 6378137;
			this.wgs84Ellipsoid.b = 6356752.3141;
			this.wgs84Ellipsoid.f = 1 / 298.257223563;
			this.wgs84Ellipsoid.e2 = 0.006694380025;
			this.wgs84Ellipsoid.e = Math.sqrt(0.006694380025);
			
			this.lambertIIE = new CoordinateConverterParameters(); // Valeurs provenant du fichier Data.txt ligne 57 de Circ� France 4.
			this.lambertIIE.X0 = 600000;
			this.lambertIIE.Y0 = 2200000;
			this.lambertIIE.phi0 = deg2rad(52 * 0.9);
			this.lambertIIE.phi1 = deg2rad(50.99879884 * 0.9);
			this.lambertIIE.phi2 = deg2rad(52.99557167 * 0.9);
			this.lambertIIE.lambda0 = deg2rad((14.025 / 60 + 20) / 60 + 2);
			this.lambertIIE.scale = 0.99987742;

			// Valeurs provenant de la derni�re page de NTG_71.pdf
			this.lambertIIE.n = 0.7289686274; // sin(self::$lambertIIE->phi0);
			this.lambertIIE.c = 11745793.39; // $tmp * exp(self::$lambertIIE->n * self::L(self::$lambertIIE->phi0, self::$clarkeEllipsoid->e));
			this.lambertIIE.Xs = 600000; // self::$lambertIIE->X0;
			this.lambertIIE.Ys = 8199695.768; // self::$lambertIIE->Y0 + $tmp;
			double Lamb_vo1 = this.clarkeEllipsoid.a / Math.sqrt(1 - this.clarkeEllipsoid.e2 * (Math.pow(Math.sin(this.lambertIIE.phi1),2)));
			double Lamb_vo2 = this.clarkeEllipsoid.a / Math.sqrt(1 - this.clarkeEllipsoid.e2 * (Math.pow(Math.sin(this.lambertIIE.phi2),2)));
			double Lamb_po1 = this.clarkeEllipsoid.a * (1 - this.clarkeEllipsoid.e2) / Math.pow(Math.sqrt(1 - this.clarkeEllipsoid.e2 * Math.pow(Math.sin(this.lambertIIE.phi1),2)),3);
			double Lamb_po2 = this.clarkeEllipsoid.a * (1 - this.clarkeEllipsoid.e2) / Math.pow(Math.sqrt(1 - this.clarkeEllipsoid.e2 * Math.pow(Math.sin(this.lambertIIE.phi2),2)),3);
			double Lamb_m1 = 1 + Lamb_po1 / 2 / Lamb_vo1 * Math.pow(((this.lambertIIE.phi1 - this.lambertIIE.phi0)),2); 
			double Lamb_m2 = 1 + Lamb_po2 / 2 / Lamb_vo2 * Math.pow(((this.lambertIIE.phi2 - this.lambertIIE.phi0)),2);
			double Lamb_m = (Lamb_m1 + Lamb_m2) / 2;
			double Lamb_mL = 2 - Lamb_m;
			double Lamb_v0 = this.clarkeEllipsoid.a / Math.sqrt(1 - this.clarkeEllipsoid.e2 * (Math.pow(Math.sin(this.lambertIIE.phi0),2)));
			double Lamb_R0 = Lamb_v0 / Math.tan(this.lambertIIE.phi0);
			// mLR0 est le Rayon du parall�le d origine apr�s r�duction d echelle
			this.lambertIIE.Lamb_mLR0 = Lamb_mL * Lamb_R0;
		}
		
		public static class XY {
			public final double X;
			public final double Y;
			public XY(double x, double y) {
				this.X = x;
				this.Y = y;
			}
		}

		public static class XYZ {
			public final double X;
			public final double Y;
			public final double Z;
			public XYZ(double x, double y, double z) {
				this.X = x;
				this.Y = y;
				this.Z = z;
			}
		}
		
		public static class PhiLambda {
			public final double Phi;
			public final double Lambda;
			public PhiLambda (double phi, double lambda) {
				this.Phi = phi;
				this.Lambda = lambda;
			}
		}

		// Effectue les �tapes de la transformation des coordonn�es WGS84 g�ographiques vers la projection Lambert II �tendue.
		public XY convertFromWGS84(double lat, double lng) {
			double phi = deg2rad(lat);
			double lambda = deg2rad(lng);
			
			XYZ xyz = this.convertGeographicToCartesian(phi, lambda, this.wgs84Ellipsoid);
			xyz = this.translateForNTF(xyz.X, xyz.Y, xyz.Z);
			PhiLambda pl = this.convertCartesianToGeographic(xyz.X, xyz.Y, xyz.Z, this.clarkeEllipsoid);
			XY xy = this.convertToLambertIIExtended(pl.Phi, pl.Lambda);
			return new XY(xy.X, xy.Y);
		}
		
		public static class LatLong {
			public final double Lat;
			public final double Long;
			public LatLong(double lat, double _long) {
				this.Lat = lat;
				this.Long = _long;
			}
		}
		
		// Effectue les �tapes de la transformation des coordonn�es de la projection Lambert II �tendue vers WGS84 g�ographiques.
		public LatLong convertToWGS84( double X, double Y) {
			PhiLambda pl = this.convertFromLambertIIExtended(X, Y);
			XYZ xyz = this.convertGeographicToCartesian(pl.Phi, pl.Lambda, this.clarkeEllipsoid);
			xyz = this.translateForWGS84(xyz.X, xyz.Y, xyz.Z);
			pl = this.convertCartesianToGeographic(xyz.X, xyz.Y, xyz.Z, this.wgs84Ellipsoid);
			return new LatLong(rad2deg(pl.Phi), rad2deg(pl.Lambda));
		}
		
		// Translate les coordonn�es cart�siennes de l'ellipso�de du WGS84 vers l'ellipso�de Clarke.
		public XYZ translateForNTF(double X, double Y, double Z) {
			X -= this.clarkeEllipsoid.T.x;
			Y -= this.clarkeEllipsoid.T.y;
			Z -= this.clarkeEllipsoid.T.z;
			return new XYZ(X, Y, Z);
		}
		
		// Translate les coordonn�es cart�siennes de l'ellipso�de Clarke vers l'ellipso�de du WGS84.
		public XYZ translateForWGS84(double X, double Y, double Z) {
			X += this.clarkeEllipsoid.T.x;
			Y += this.clarkeEllipsoid.T.y;
			Z += this.clarkeEllipsoid.T.z;
			return new XYZ(X, Y, Z);
		}
		
		// Transformation de coordonn�es g�ographiques en cart�siennes. ALG0009 de NTG_80.pdf
		public XYZ convertGeographicToCartesian(double phi, double lambda, CoordinateConverterParameters ellipsoid) {
			double N = this.N(phi, ellipsoid.a, ellipsoid.e2);
			double X = N * Math.cos(phi) * Math.cos(lambda);
			double Y = N * Math.cos(phi) * Math.sin(lambda);
			double Z = N * (1 - ellipsoid.e2) * Math.sin(phi);
			return new XYZ(X, Y, Z);
		}
		
		// Transformation de coordonn�es cart�siennes en g�ographiques. Algorithme page 4 de transfo.pdf. Donne les m�mes r�sultats que ALG0012 de NTG_80.pdf
		public PhiLambda convertCartesianToGeographic(double X, double Y, double Z, CoordinateConverterParameters ellipsoid) {
			double R = Math.sqrt(Math.pow(X, 2) + Math.pow(Y, 2) + Math.pow(Z, 2)); // Algo non it�ratif.
			double r = Math.sqrt(Math.pow(X, 2) + Math.pow(Y, 2));
			double ae2 = ellipsoid.a * ellipsoid.e2;
			double lambda = Math.atan(Y / X);
			double mu = Math.atan(Z * ((1 - ellipsoid.f) + ae2 / R) / r);
			double phi = Math.atan((Z * (1 - ellipsoid.f) + ae2 * Math.pow(Math.sin(mu), 3)) / ((1 - ellipsoid.f) * (r - ae2 * Math.pow(Math.cos(mu), 3))));
			return new PhiLambda(phi, lambda);
		}
		
		// Transformation de coordonn�es g�ographiques NTF en projection de Lambert. ALG0003 de NTG_71.pdf
		public XY convertToLambertIIExtended(double phi, double lambda) {
			double Lamb_LatIso = Math.log(Math.tan(Math.PI / 4 + phi / 2)) - this.clarkeEllipsoid.e / 2 * Math.log((1 + this.clarkeEllipsoid.e * Math.sin(phi))/(1 - this.clarkeEllipsoid.e * Math.sin(phi)));
			double Lamb_LatIso0 = Math.log(Math.tan(Math.PI / 4 + this.lambertIIE.phi0 / 2)) - this.clarkeEllipsoid.e / 2 * Math.log((1 + this.clarkeEllipsoid.e * Math.sin(this.lambertIIE.phi0))/(1 - this.clarkeEllipsoid.e * Math.sin(this.lambertIIE.phi0)));
			double Lamb_Gamma = 0;
			if (lambda < Math.PI) {
				Lamb_Gamma = (lambda - this.lambertIIE.lambda0) * Math.sin(this.lambertIIE.phi0);
			}
			if (lambda > Math.PI) {
				Lamb_Gamma = (lambda - this.lambertIIE.lambda0 - 2 * Math.PI) * Math.sin(this.lambertIIE.phi0);
			}
			double Lamb_R = this.lambertIIE.Lamb_mLR0 * Math.exp(- Math.sin(this.lambertIIE.phi0) * (Lamb_LatIso - Lamb_LatIso0));
			double Lamb_E1 = Lamb_R * Math.sin(Lamb_Gamma);
			double X = Lamb_E1 + this.lambertIIE.X0;
			double Y = this.lambertIIE.Lamb_mLR0 - Lamb_R + Lamb_E1 * Math.tan(Lamb_Gamma / 2) + this.lambertIIE.Y0;
			return new XY(X, Y);
		}
		
		// Transformation de coordonn�es en projection de Lambert en g�ographiques. ALG0004 de NTG_71.pdf
		public PhiLambda convertFromLambertIIExtended(double X, double Y) {
			double Lamb_Ls = Math.log(Math.tan(Math.PI / 4 + this.lambertIIE.phi0 / 2)) - this.clarkeEllipsoid.e / 2 * Math.log((1 + this.clarkeEllipsoid.e * Math.sin(this.lambertIIE.phi0)) / (1 - this.clarkeEllipsoid.e * Math.sin(this.lambertIIE.phi0)));
			double dX = X - this.lambertIIE.X0;
			double dY = Y - this.lambertIIE.Y0;
			double gamma = Math.atan(dX / (this.lambertIIE.Lamb_mLR0 - dY));
			double lambda = (gamma / Math.sin(this.lambertIIE.phi0) + this.lambertIIE.lambda0);
			double Lamb_R = (this.lambertIIE.Lamb_mLR0 - dY) / Math.cos(gamma);
			double Lamb_L0 = Math.log(Math.tan(Math.PI / 4 + this.lambertIIE.phi0 / 2)) - (this.clarkeEllipsoid.e / 2) * Math.log((1 + this.clarkeEllipsoid.e * Math.sin(this.lambertIIE.phi0)) / (1 - this.clarkeEllipsoid.e * Math.sin(this.lambertIIE.phi0)));
			double Lamb_L = Lamb_L0 + Math.log(this.lambertIIE.Lamb_mLR0 / Lamb_R) / Math.sin(this.lambertIIE.phi0);
			double epsilon = 1;
			double phi = 2 * Math.atan(Math.exp(Lamb_L)) - Math.PI / 2;
			while (epsilon > 1e-10) {
				double phi1 = 2 * (Math.atan(Math.exp(Lamb_L + this.clarkeEllipsoid.e / 2 * Math.log((1 + this.clarkeEllipsoid.e * Math.sin(phi)) / (1 - this.clarkeEllipsoid.e * Math.sin(phi)))))) - Math.PI / 2;
				epsilon = Math.abs(phi1 - phi);
				phi = phi1;
			}
			return new PhiLambda(phi, lambda);
		}
		
		// Calcul de la r�ciproque de la latitude isom�trique. ALG0002 de NTG_71.pdf 
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
		private static double N(double phi, double a, double e2) {
			return a / Math.sqrt(1 - e2 * Math.pow(Math.sin(phi), 2));
		}

		// Calcul de la latitude isom�trique. ALG0001 de NTG_71.pdf
		private static double L(double phi, double e) {
			return Math.log(Math.tan((Math.PI / 4) + (phi / 2))) * Math.pow((1 - e * Math.sin(phi)) / (1 + e * Math.sin(phi)), e / 2);
		}
	}

	public static class CoordinateConverterParameters {
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
