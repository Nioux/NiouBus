package org.nioux.nioubus.navitia;

public class Conversion {


	public static double parseFloat(double val) { return val; }
	
	@SuppressWarnings("unused")
	public static double[] Hayford_Iagrs(double[] pa_longitude, double[] pa_latitude)
	//<!-- De PRIAM (ED50) - Hayford 1909 (3) -- VERS -- GPS (WGS84) Sexa - Iagrs80--RGF93 mais WGS84 tout de meme avec les constantes (2) -->
		{
		//<!-- Constantes Ellipsoide de Hayford PRIAM ED50 -->
		double Hayford_a = 6378388.00;
		double Hayford_f = 297;
		double Hayford_b = Hayford_a - (Hayford_a / Hayford_f);

		// la valeur e est en fait e au carre

		double Hayford_e = ((Math.pow(Hayford_a,2) - Math.pow(Hayford_b,2)) / Math.pow(Hayford_a,2));

		//<!-- Constantes Ellipsoide Iagrs reporte GPS WGS84 -->
		double Iagrs_a = 6378137;
		double Iagrs_f = 298.257223563;
		double Iagrs_b = Iagrs_a - (Iagrs_a / Iagrs_f);

		// la valeur e est en fait e au carre
	               
		double Iagrs_e = (Math.pow(Iagrs_a,2) - Math.pow(Iagrs_b,2)) / Math.pow(Iagrs_a,2);

		//<!-- Constantes Tx, Ty, Tz en metres de Transformation de 3 vers 2 -->
		double Tx = -84;
		double Ty = -97;
		double Tz = -117;

		//<!-- Coordonnees LONGITUDE Priam Saisies (Si W -> -1 ou Si E -> 1) Pri_Deg, Pri_Min, Pri_Sec (virgules autorisees) -->

		//<!-- Mise en Float des variables lues pour eviter les erreurs NaN (Not a Number) -->
		double Pri_Long_Sgn = parseFloat(pa_longitude[0]);
		double Pri_Long_Deg = parseFloat(pa_longitude[1]);
		double Pri_Long_Min = parseFloat(pa_longitude[2]);
		double Pri_Long_Sec = parseFloat(pa_longitude[3]);

		//<!-- Coordonnees LONGITUDE Priam (Lambda) Decimale calculee -->
		double Pri_Long_Dec = Pri_Long_Sgn * ((Pri_Long_Deg) + (Pri_Long_Min / 60) + (Pri_Long_Sec / 3600));

		//<!-- Coordonnees LATITUDE Priam Saisies (Si N -> 1 ou Si S -> -1) Pri_Deg, Pri_Min, Pri_Sec (virgules autorisees) -->

		//<!-- Mise en Float les variables lues pour eviter les erreurs NaN (Not a Number) -->
		double Pri_Lat_Sgn = parseFloat(pa_latitude[0]);
		double Pri_Lat_Deg = parseFloat(pa_latitude[1]);
		double Pri_Lat_Min = parseFloat(pa_latitude[2]);
		double Pri_Lat_Sec = parseFloat(pa_latitude[3]);

		//<!-- Coordonnees LATITUDE Priam (Phi) Decimale calculee -->
		double Pri_Lat_Dec = Pri_Lat_Sgn * ((Pri_Lat_Deg) + (Pri_Lat_Min / 60) + (Pri_Lat_Sec / 3600));

		//<!-- Pas de consideration de hauteur (en metres) mis e 0 -- Peut etre perfectible en ajoutant les tables de hauteur -->
		double Hauteur = 0;

		//<!-- Reference Meridien de Grennwich pas decalage d'un systeme e l'autre -->

	     //<!-- Partie - coordonnees geocentriques sur l'ellipsoede 'H' Origine (Hayford 1909 - ED50 Priam)
		//<!-- Variable v (en metres) calculee par la fct Hayford_a / Racine(1-(Hayford_e*sin(Pri_Lat_Dec*(Pi/180)))^2) -->
		double H_v = Hayford_a / (Math.sqrt(1 - (Hayford_e * Math.pow(Math.sin(Pri_Lat_Dec * (Math.PI / 180)),2))));

		//<!-- Variable X calculee en metres X = (v+h).Cos(Phi).Cos(Lambda) (H_X comme Hayford Z) -->
		double H_X = (H_v + Hauteur) * Math.cos(Pri_Lat_Dec * (Math.PI / 180)) * Math.cos(Pri_Long_Dec * (Math.PI / 180));

		//<!-- Variable Y calculee en metres Y = (v+h).Cos(Phi).Sin(Lambda) (H_Y comme Hayford Z) -->
		double H_Y = (H_v + Hauteur) * Math.cos(Pri_Lat_Dec * (Math.PI / 180)) * Math.sin(Pri_Long_Dec * (Math.PI / 180));

		//<!-- Variable Z calculee en metres Z = (v*(1-e)+h).Sin(Phi) (H_Z comme Hayford Z) -->
		double H_Z = (H_v * (1 - Hayford_e) + Hauteur) * Math.sin(Pri_Lat_Dec * (Math.PI / 180));

	     //<!-- Partie - coordonnees geocentriques sur l'ellipsoede 'I' Destination (Iagrs80 - WGS84 GPS) -->
		//<!-- Decalage longitude Paris Greenwich (2e 20' 14.025"") - Valeur en Degres decimaux -->
		double Paris_Green = 2 + (20 / 60) + (14.025 / 3600);
		//<!-- Dans ce cas aucune difference - meme reference = Greenwich nomme Lambda_0 -->
		double Lambda_0 = 0;

		//<!-- Variable I_X calculee en metres X'= Tx + X * Cos(Lambda_0) + Y * Sin(Lambda_0) (I_X comme Iagrs X) -->
		double I_X = Tx + H_X * Math.cos(Lambda_0 * (Math.PI / 180)) + H_Y * Math.sin(Lambda_0 * (Math.PI / 180));

		//<!-- Variable I_Y calculee en metres Y'= Ty - X * Sin(Lambda_0) + Y * Cos(Lambda_0) (I_Y comme Iagrs Y) -->
		double I_Y = Ty - H_X * Math.sin(Lambda_0 * (Math.PI / 180)) + H_Y * Math.cos(Lambda_0 * (Math.PI / 180));

		//<!-- Variable I_Z calculee en metres Z'= Z + Tz (I_Z comme Iagrs Z) -->
		double I_Z = H_Z + Tz;

		//<!-- Variable Re_1 calculee en metres Re_1 = RACINE (X'^2 + Y'^2) Re_1 Car autre formule de calcul donne meme resultat -->
		double Re_1 = Math.sqrt(Math.pow(I_X,2) + (Math.pow(I_Y,2)));

	     //<!-- Partie Coordonnees geographiques sur l'ellipsoede 'I' de destination - Iagrs80 - Origine Greenwich -->
		//<!-- Longitude Lambda en degres decimaux sur Iagrs - Long_I_Dec = Atan(Y'/X')/(Pi/180) -->
		double Long_I_Dec = Math.atan(I_Y / I_X) / (Math.PI / 180);

		//<!-- Latitude Phi en degres decimaux sur Iagrs - Lat_I_Dec = difficile e enoncer calcul recurcif -->
		double ecart = 1;
		double Phi = Pri_Lat_Dec / (180 * Math.PI);
		while (ecart > 0.0000000001)
			{
			double Phi_1 = Math.atan((I_Z + Iagrs_e * Math.sin(Phi) * Iagrs_a / Math.sqrt(1 - Iagrs_e * Math.pow(Math.sin(Phi),2))) / Re_1);
			ecart = Math.abs(Phi - Phi_1);
			Phi = Phi_1;
			}
		double Lat_I_Dec = Phi * (180 / Math.PI);

		//<!-- Variable v en metre sur Geoede de destination - I_v = Iagrs_a / (racine (1 - Iagrs_e * (sin(Phi * 180/PI)^2) -->
		double I_v = Iagrs_a / (Math.sqrt(1 - (Iagrs_e * Math.pow(Math.sin(Lat_I_Dec * (Math.PI / 180)),2))));

		//<!-- Hauteur ellipsoedale h_I en metres - Resultat e corriger pour passer e des altitudes NGF au dessus de l'ellipsoede -->
		//<!-- Formule = h_I = Re / Cos(Phi) - v -->
		double h_I = Re_1 / Math.cos(Lat_I_Dec*(Math.PI / 180)) - I_v;
	    
	    return new double[] {Long_I_Dec,Lat_I_Dec};
	     
	//<!-- Fin de la fonction De PRIAM (ED50) - Hayford 1909 (3) -- VERS -- GPS (WGS84) Sexa - Iagrs80 (2) -->
		}
		
	@SuppressWarnings("unused")
	public static double[] NTF_ED50(double[] pa_longitude, double[] pa_latitude)
	//<!-- De GeoConcept (NTF) Sexa - Clarke 1880 (1) -- VERS --  PRIAM (ED50) - Hayford 1909 (3) -->
		{
		//<!-- Constantes Ellipsoede Hayford PRIAM ED50 -->	
		double Hayford_a = 6378388.00;
		double Hayford_f = 297;
		double Hayford_b = Hayford_a - (Hayford_a / Hayford_f);
		double Hayford_e = (Math.pow(Hayford_a,2) - Math.pow(Hayford_b,2)) / Math.pow(Hayford_a,2);

		//<!-- Constantes Ellipsoede Clarke GeoConcept NTF -->
		double Clarke_a = 6378249.2;
		double Clarke_b = 6356515;
		double Clarke_f = 1 / ((Clarke_a - Clarke_b) / Clarke_a);
		double Clarke_e = (Math.pow(Clarke_a,2) - Math.pow(Clarke_b,2)) / Math.pow(Clarke_a,2);

		//<!-- Constantes Tx, Ty, Tz en metres de Transformation de 1 vers 3 -->
		double Tx = -84;
		double Ty = 37;
		double Tz = 437;

		//<!-- Coordonnees LONGITUDE NTF Saisies (Si W -> -1 ou Si E -> 1) Pri_Deg, Pri_Min, Pri_Sec (virgules autorisees) -->
		
		//<!-- Mise en Float des variables lues pour eviter les erreurs NaN (Not a Number) -->
		double NTF_Long_Sgn = parseFloat(pa_longitude[0]);
		double NTF_Long_Deg = parseFloat(pa_longitude[1]);
		double NTF_Long_Min = parseFloat(pa_longitude[2]);
		double NTF_Long_Sec = parseFloat(pa_longitude[3]);

		//<!-- Coordonnees LONGITUDE NTF (Lambda) Decimale calculee -->
		double NTF_Long_Dec = NTF_Long_Sgn * ((NTF_Long_Deg) + (NTF_Long_Min / 60) + (NTF_Long_Sec / 3600));

		//<!-- Coordonnees LATITUDE NTF Saisies (Si N -> 1 ou Si S -> -1) Pri_Deg, Pri_Min, Pri_Sec (virgules autorisees) -->
		
		//<!-- Mise en Float les variables lues pour eviter les erreurs NaN (Not a Number) -->
		double NTF_Lat_Sgn = parseFloat(pa_latitude[0]);
		double NTF_Lat_Deg = parseFloat(pa_latitude[1]);
		double NTF_Lat_Min = parseFloat(pa_latitude[2]);
		double NTF_Lat_Sec = parseFloat(pa_latitude[3]);

		//<!-- Coordonnees LATITUDE NTF (Phi) Decimale calculee -->
		double NTF_Lat_Dec = NTF_Lat_Sgn * ((NTF_Lat_Deg) + (NTF_Lat_Min / 60) + (NTF_Lat_Sec / 3600));

		//<!-- Pas de consideration de hauteur (en metres) mis e 0 -- Peut etre perfectible en ajoutant les tables de hauteur -->
		double Hauteur = 0;

		//<!-- Reference Meridien de Grennwich pas decalage d'un systeme e l'autre -->

	     //<!-- Partie - coordonnees geocentriques sur l'ellipsoede 'NTF' Origine (Clarke 1880 - NTF)
		//<!-- Variable v (en metres) calculee par la fct Hayford_a / Racine(1-(Hayford_e*sin(Pri_Lat_Dec*(Pi/180)))^2) -->
		double NTF_v = Clarke_a / (Math.sqrt(1 - (Clarke_e * Math.pow(Math.sin(NTF_Lat_Dec * (Math.PI / 180)),2))));

		//<!-- Variable X calculee en metres X = (v+h).Cos(Phi).Cos(Lambda) -->
		double NTF_X = (NTF_v + Hauteur) * Math.cos(NTF_Lat_Dec * (Math.PI / 180)) * Math.cos(NTF_Long_Dec * (Math.PI / 180));

		//<!-- Variable Y calculee en metres Y = (v+h).Cos(Phi).Sin(Lambda) -->
		double NTF_Y = (NTF_v + Hauteur) * Math.cos(NTF_Lat_Dec * (Math.PI / 180)) * Math.sin(NTF_Long_Dec * (Math.PI / 180));

		//<!-- Variable Z calculee en metres Z = (v*(1-e)+h).Sin(Phi) -->
		double NTF_Z = (NTF_v * (1 - Clarke_e) + Hauteur) * Math.sin(NTF_Lat_Dec * (Math.PI / 180));

	     //<!-- Partie - coordonnees geocentriques sur l'ellipsoede 'ED50' Destination ( Hayford 1909 - ED50 Priam) -->
		//<!-- Decalage longitude Paris Greenwich (2e 20' 14.025"") - Valeur en Degres decimaux -->
		double Paris_Green = 2 + (20 / 60) + (14.025 / 3600);
		//<!-- Dans ce cas aucune difference - meme reference = Greenwich nomme Lambda_0 -->
		double Lambda_0 = 0;

		//<!-- Variable X calculee en metres X'= Tx + X * Cos(Lambda_0) + Y * Sin(Lambda_0) -->
		double ED50_X = Tx + NTF_X * Math.cos(Lambda_0 * (Math.PI / 180)) + NTF_Y * Math.sin(Lambda_0 * (Math.PI / 180));

		//<!-- Variable Y calculee en metres Y'= Ty - X * Sin(Lambda_0) + Y * Cos(Lambda_0) -->
		double ED50_Y = Ty - NTF_X * Math.sin(Lambda_0 * (Math.PI / 180)) + NTF_Y * Math.cos(Lambda_0 * (Math.PI / 180));

		//<!-- Variable Z calculee en metres Z'= Z + Tz -->
		double ED50_Z = NTF_Z + Tz;

		//<!-- Variable Re_1 calculee en metres Re_1 = RACINE (X'^2 + Y'^2) Re_1 Car autre formule de calcul donne meme resultat -->
		double Re_1 = Math.sqrt(Math.pow(ED50_X,2) + (Math.pow(ED50_Y,2)));

	     //<!-- Partie Coordonnees geographiques sur l'ellipsoede de destination - Hayford 1909 - Origine Greenwich -->
		//<!-- Longitude Lambda en degres decimaux sur Hayford -->
		double Long_ED50_Dec = Math.atan(ED50_Y / ED50_X) / (Math.PI / 180);

		//<!-- Latitude Phi en degres decimaux sur Hayford - Lat_ED50_Dec = difficile e enoncer calcul recurcif -->
		double ecart = 1;
		double Phi = NTF_Lat_Dec / (180 * Math.PI);
		while (ecart > 0.0000000001)
			{
			double Phi_1 = Math.atan((ED50_Z + Hayford_e * Math.sin(Phi) * Hayford_a / Math.sqrt(1 - Hayford_e * Math.pow(Math.sin(Phi),2))) / Re_1);
			ecart = Math.abs(Phi - Phi_1);
			Phi = Phi_1;
			}
		double Lat_ED50_Dec = Phi * (180 / Math.PI);

		//<!-- Variable v en metre sur Geoede de destination - ED50_v -->
		double ED50_v = Hayford_a / (Math.sqrt(1 - (Hayford_e * Math.pow(Math.sin(Lat_ED50_Dec * (Math.PI / 180)),2))));

		//<!-- Hauteur ellipsoedale h_ED50 en metres - Resultat e corriger pour passer e des altitudes NGF au dessus de l'ellipsoede -->
		double h_ED50 = Re_1 / Math.cos(Lat_ED50_Dec*(Math.PI / 180)) - ED50_v;

	     //<!-- Tranformation Degres decimaux Longitude arrivee en degre minute seconde avec reconnaissance E W -->
		double E_W_Long_ED50;
		if (Long_ED50_Dec > 0)
			{
			E_W_Long_ED50 = 1;
			}
		else
			{
			E_W_Long_ED50 = -1;
			}
		Long_ED50_Dec = Math.abs(Long_ED50_Dec);
		double Deg_Long_ED50 = Math.abs(Math.floor(Long_ED50_Dec));
		double Min_Long_ED50 = Math.floor((Long_ED50_Dec - Deg_Long_ED50) * 60);
		double Sec_Long_ED50 = Math.round(((Long_ED50_Dec - Deg_Long_ED50 - Min_Long_ED50 / 60) * 3600)*1000)/1000;

	     //<!-- Tranformation Degres decimaux Latitude arrivee en degre minute seconde avec reconnaissance N S -->
		double N_S_Lat_ED50;
		if (Lat_ED50_Dec > 0)
			{
			N_S_Lat_ED50 = 1;
			}
		else
			{
			N_S_Lat_ED50 = -1;
			}
		Lat_ED50_Dec = Math.abs(Lat_ED50_Dec);
		double Deg_Lat_ED50 = Math.floor(Lat_ED50_Dec);
		double Min_Lat_ED50 = Math.floor((Lat_ED50_Dec - Deg_Lat_ED50) * 60);
		double Sec_Lat_ED50 = Math.round(((Lat_ED50_Dec - Deg_Lat_ED50 - Min_Lat_ED50 / 60) * 3600)*1000)/1000;

		double[] la_result = Hayford_Iagrs(new double[] {E_W_Long_ED50, Deg_Long_ED50, Min_Long_ED50, Sec_Long_ED50}, new double[]{N_S_Lat_ED50, Deg_Lat_ED50, Min_Lat_ED50, Sec_Lat_ED50});
	    return la_result;
	//<!-- Fin de la fonction NTF_ED50 - GeoConcept (NTF) Sexa - Clarke 1880 (1) -- VERS -- PRIAM (ED50) - Hayford 1909 (3) -->
		}

	@SuppressWarnings("unused")
	public static double[] Lamb_WGS84(double pi_x, double pi_y)
	//<!-- De Lambert II etendu (NTF) metrique - Clarke 1880 (1) -- VERS -- (NTF) - Clarke 1880 (1) -->
		{
		//<!-- Debut des calculs sur une base de Lambert II ---- Voir si portable en Etendu -->
		//<!-- Mise en Float les variables lues pour eviter les erreurs NaN (Not a Number) -->
		double Lamb_EE = pi_x;
		double Lamb_NN = pi_y;
		
		//<!-- Recherche de la zone Lambert du point - Inutilise pour l'instant - Seul Lambert II-->
		double Lamb_Zone = Math.floor(Lamb_NN / 1000000);
		
		//<!-- Constante pour la Zone II Lambert -->
		//<!-- Calcul des constantes sur le goide Clarke 1880 pour Lambert II -->
		double Lamb_a = 6378249.2;
		double Lamb_f = 293.466021300;
		double Lamb_b = Lamb_a * (1 - 1 / Lamb_f);
		double Lamb_e = Math.sqrt((Math.pow(Lamb_a,2) - Math.pow(Lamb_b,2)) / Math.pow(Lamb_a,2));
		double Lamb_Phi1 = 50.99879884 / 200 * 180;
		double Lamb_Phi2 = 52.99557167 / 200 * 180;
		double Lamb_vo1 = Lamb_a / Math.sqrt(1 - Math.pow(Lamb_e,2) * (Math.pow(Math.sin(Lamb_Phi1 * Math.PI / 180),2)));
		double Lamb_vo2 = Lamb_a / Math.sqrt(1 - Math.pow(Lamb_e,2) * (Math.pow(Math.sin(Lamb_Phi2 * Math.PI / 180),2)));
		//<!-- Phi0 est la Latitude du parallele d origine -->
		double Lamb_Phi0 = 52 * 0.9;
		double Lamb_po1 = Lamb_a * (1 - Math.pow(Lamb_e,2)) / Math.pow((Math.sqrt(1 - Math.pow(Lamb_e,2) * Math.pow((Math.sin(Lamb_Phi1 * Math.PI / 180)),2))),3);
		double Lamb_po2 = Lamb_a * (1 - Math.pow(Lamb_e,2)) / Math.pow((Math.sqrt(1 - Math.pow(Lamb_e,2) * Math.pow((Math.sin(Lamb_Phi2 * Math.PI / 180)),2))),3);
		double Lamb_m1 = 1 + Lamb_po1 / 2 / Lamb_vo1 * Math.pow(((Lamb_Phi1 - Lamb_Phi0) * Math.PI / 180),2); 
		double Lamb_m2 = 1 + Lamb_po2 / 2 / Lamb_vo2 * Math.pow(((Lamb_Phi2 - Lamb_Phi0) * Math.PI / 180),2);
		double Lamb_m = (Lamb_m1 + Lamb_m2) / 2;
		double Lamb_CE = 600;
		double Lamb_CN = 2200;
		double Lamb_mL = 2 - Lamb_m;
		double Lamb_v0 = Lamb_a / Math.sqrt(1 - Math.pow(Lamb_e,2) * (Math.pow(Math.sin(Lamb_Phi0 * Math.PI / 180),2)));
		double Lamb_R0 = Lamb_v0 / Math.tan(Lamb_Phi0 * Math.PI / 180);
		//<!-- mLR0 est le Rayon du parallele d origine apres reduction d echelle -->
		double Lamb_mLR0 = Lamb_mL * Lamb_R0;
		double Lamb_Ls = Math.log(Math.tan(Math.PI / 4 + Lamb_Phi0 / 2 * Math.PI / 180)) - Lamb_e / 2 * Math.log((1 + Lamb_e * Math.sin(Lamb_Phi0 * Math.PI / 180 )) / (1 - Lamb_e * Math.sin(Lamb_Phi0 * Math.PI / 180)));
		
		//<!-- Abscisse en m dans le repere associe aux meridien et parallele d'origine -->
		double Lamb_E1 = Lamb_EE - Lamb_CE * 1000;
		//<!-- Ordonnee en m dans le repere associe aux meridien et parallele d'origine -->
		double Lamb_N1 = Lamb_NN - Lamb_CN * 1000;
		//<!-- Convergence des meridiens en degres -->
		double Lamb_gamma = Math.atan(Lamb_E1 / (Lamb_mLR0 - Lamb_N1)) * 180 / Math.PI;
		//<!-- Lambda0 est la Longitude du meridien de Paris en degre -->
		double Lamb_Lambda0 = 2.596921296 / 200 * 180;
		//<!-- Longitude du point recherche en degre par rapport e Greenwich -->
		double NTF_Lambda = (Lamb_gamma / Math.sin(Lamb_Phi0 * Math.PI / 180) + Lamb_Lambda0);
		
		//<!-- Rayon du parallele passant par le point recherche - en metres -->
		double Lamb_R = (Lamb_mLR0 - Lamb_N1) / Math.cos(Lamb_gamma * Math.PI / 180);
		//<!-- Valeur de L0 pour Phi0 -->
		double Lamb_L0 = Math.log(Math.tan(Math.PI / 4 + Lamb_Phi0 * Math.PI / 360)) - (Lamb_e / 2) * Math.log((1 + Lamb_e * Math.sin(Lamb_Phi0 * Math.PI / 180)) / (1 - Lamb_e * Math.sin(Lamb_Phi0 * Math.PI / 180)));
		//<!-- Latitude isometrique L en fonction  de Phi -->
		double Lamb_L = Lamb_L0 + Math.log(Lamb_mLR0 / Lamb_R) / Math.sin(Lamb_Phi0 * Math.PI / 180);
		//<!-- Latitude du point recherche -->
		//<!-- Latitude Phi en degres decimaux -->
		double ecart = 1;
		double Phi = 2 * Math.atan(Math.exp(Lamb_L)) - Math.PI / 2;
		while (ecart > 0.000000000001)
			{
			double Phi_1 = 2 * (Math.atan(Math.exp(Lamb_L + Lamb_e / 2 * Math.log((1 + Lamb_e * Math.sin(Phi)) / (1 - Lamb_e * Math.sin(Phi)))))) - Math.PI / 2;
			ecart = Math.abs(Phi_1 - Phi);
			Phi = Phi_1;
			}
		double NTF_Phi = Phi * 180 / Math.PI;

		//<!-- Module de correction e la projection du point donne -->
		//<!-- Rayon de courbure de l ellipse normale principale -->
		double Lamb_v = Lamb_a / Math.sqrt(1 - Math.pow(Lamb_e,2) * (Math.pow(Math.sin(NTF_Phi * Math.PI / 180),2)));
		//<!-- Module de reduction e la projection -->
		double Lamb_mr = Lamb_R * Math.sin(Lamb_Phi0 * Math.PI / 180) / Lamb_v / Math.cos(NTF_Phi * Math.PI / 180);
		//<!-- Coefficient d alteration lineaire en centimetres par kilometres -->
		double Lamb_kr = (Lamb_mr - 1) * 100000;
		
		//<!-- Mise en forme des latitude Longitude en DMS -->
		//<!-- Tranformation Degres decimaux Longitude arrivee en degre minute seconde avec reconnaissance E W -->
		double E_W_Long_NTF;
		if (NTF_Lambda > 0)
			{
			E_W_Long_NTF = 1;
			}
		else
			{
			E_W_Long_NTF = -1;
			}
		NTF_Lambda = Math.abs(NTF_Lambda);
		double Deg_Long_NTF = Math.abs(Math.floor(NTF_Lambda));
		double Min_Long_NTF = Math.floor((NTF_Lambda - Deg_Long_NTF) * 60);
		double Sec_Long_NTF = Math.round(((NTF_Lambda - Deg_Long_NTF - Min_Long_NTF / 60) * 3600)*1000)/1000;
		
	     //<!-- Tranformation Degres decimaux Latitude arrivee en degre minute seconde avec reconnaissance N S -->
		double N_S_Lat_NTF;
		if (NTF_Phi > 0)
			{
			N_S_Lat_NTF = 1;
			}
		else
			{
			N_S_Lat_NTF = -1;
			}
		NTF_Phi = Math.abs(NTF_Phi);
		double Deg_Lat_NTF = Math.floor(NTF_Phi);
		double Min_Lat_NTF = Math.floor((NTF_Phi - Deg_Lat_NTF) * 60);
		double Sec_Lat_NTF = Math.round(((NTF_Phi - Deg_Lat_NTF - Min_Lat_NTF / 60) * 3600)*1000)/1000;
	    double[] la_longitude = new double[]{E_W_Long_NTF, Deg_Long_NTF, Min_Long_NTF, Sec_Long_NTF};
		double[] la_latitude = new double[]{N_S_Lat_NTF, Deg_Lat_NTF, Min_Lat_NTF, Sec_Lat_NTF};

		double[] la_result = NTF_ED50(la_longitude, la_latitude);
		return la_result;

	//<!-- Fin de la fonction Lambert II etendu (NTF) metrique - Clarke 1880 (1) -- VERS -- (NTF) - Clarke 1880 (1) -->
	//<!-- Module perfectible en ajoutant des boutons radio designant les zones et en fonction des zones faire les constantes -->
		}

	@SuppressWarnings("unused")
	public static double[] NTF_Lambert(double p_Long_Deg,double p_Long_Min,double p_Long_Sec,double p_Lat_Deg,double p_Lat_Min,double p_Lat_Sec,double p_Long_sgn,double p_Lat_sgn)
	//<!-- Fonction de conversion des valeurs sexagesimales NTF (geoconcept) en coordonnees Lambert II etendu -->
		{
		//<!-- Constantes Ellipsoede Hayford 1909 -->
		//<!-- 1/2 grand axe de l ellipsoide en m -->
		double Lamb_a = 6378249.2;
		//<!-- 1/2 petit axe de l ellipsoide en m -->
		double Lamb_b = 6356515;
		//<!-- Latitude Parallele d origine en degres -->
		double Lamb_Phi0 = 46.800;
		//<!-- Longitude du meridien de Paris en degres -->
		double Lamb_Lambda0 = 2.596921296 / 200 * 180;
		//<!-- Excentricite de l ellipsoide -->
		double Lamb_e = (Math.sqrt(Math.pow(Lamb_a,2) - Math.pow(Lamb_b,2))) / Lamb_a;
		
		//<!-- Recueil des longitude et latitude -->
		//<!-- Coordonnees LONGITUDE Geoconcept NTF (Si W -> -1 ou Si E -> 1) NTF_Deg, NTF_Min, NTF_Sec (virgules autorisees) -->
		
		//<!-- Mise en Float les variables lues pour eviter les erreurs NaN (Not a Number) -->
		double NTF_Long_Sgn = parseFloat(p_Long_sgn);
		double NTF_Long_Deg = parseFloat(p_Long_Deg);
		double NTF_Long_Min = parseFloat(p_Long_Min);
		double NTF_Long_Sec = parseFloat(p_Long_Sec);

		//<!-- Coordonnees LONGITUDE NTF (Lambda) Decimale calculee -->
		double NTF_Long_Dec = NTF_Long_Sgn * ((NTF_Long_Deg) + (NTF_Long_Min / 60) + (NTF_Long_Sec / 3600));

		//<!-- Coordonnees LATITUDE Geoconcept NTF (Si N -> 1 ou Si S -> -1) Pri_Deg, Pri_Min, Pri_Sec (virgules autorisees) -->
		
		//<!-- Mise en Float des variables lues pour eviter les erreurs NaN (Not a Number) -->
		double NTF_Lat_Sgn = parseFloat(p_Lat_sgn);
		double NTF_Lat_Deg = parseFloat(p_Lat_Deg);
		double NTF_Lat_Min = parseFloat(p_Lat_Min);
		double NTF_Lat_Sec = parseFloat(p_Lat_Sec);

		//<!-- Coordonnee LATITUDE NTF (Phi) Decimale calculee -->
		double NTF_Lat_Dec = NTF_Lat_Sgn * ((NTF_Lat_Deg) + (NTF_Lat_Min / 60) + (NTF_Lat_Sec / 3600));
		//<!-- Fin du Recueil des longitude et latitude -->

		//<!-- Variable Lamb_v en metres - Rayon de courbure de l 'ellipse normale principale - Lamb_v = Lamb_a / (racine (1 - Lamb_e^2 * (sin(Phi * PI/180)^2)) -->
		double Lamb_v = Lamb_a / (Math.sqrt(1 - Math.pow (Lamb_e,2) * Math.pow(Math.sin (NTF_Lat_Dec * Math.PI / 180),2)));

		//<!-- Latitude isometrique - Lamb_LatIso = Ln(tan(Pi/4+Phi/2))-e/2*Ln((1+e*sin(Phi))/(1-e*sin(Phi))) -->
		double Lamb_LatIso = (Math.log(Math.tan((Math.PI / 4 + (NTF_Lat_Dec * Math.PI / 360))))) - Lamb_e / 2 * (Math.log((1 + Lamb_e * Math.sin(NTF_Lat_Dec * Math.PI /180))/(1-Lamb_e*Math.sin(NTF_Lat_Dec * Math.PI /180))));

		//<!-- Latitude isometrique pour Lambda0 - Lamb_LatIso0 = Ln(tan(Pi/4+Phi0/2))-e/2*Ln((1+e*sin(Phi0))/(1-e*sin(Phi0))) -->
		double Lamb_LatIso0 = (Math.log(Math.tan((Math.PI / 4 + (Lamb_Phi0 * Math.PI / 360))))) - Lamb_e / 2 * (Math.log((1 + Lamb_e * Math.sin(Lamb_Phi0 * Math.PI /180))/(1-Lamb_e*Math.sin(Lamb_Phi0 * Math.PI /180))));

		//<!-- Convergence des meridiens Lamb_Gamma -->
		double Lamb_Gamma = 0;
		if(NTF_Long_Dec < 180)
			{
			Lamb_Gamma = (NTF_Long_Dec - Lamb_Lambda0) * Math.sin(Lamb_Phi0 * Math.PI / 180);
			}
		if(NTF_Long_Dec > 180)
			{
			Lamb_Gamma = (NTF_Long_Dec - Lamb_Lambda0 - 360) * Math.sin(Lamb_Phi0 * Math.PI / 180);
			}

		//<!-- Constantes de Zone Lambert II en Km -->
		double Lamb_Ce = 600;
		double Lamb_Cn = 2200;
		
		//<!-- Calcul des constantes pour la zone II de Lambert -->
		double Lamb_v0 = Lamb_a / (Math.sqrt(1 - Math.pow (Lamb_e,2) * Math.pow(Math.sin (Lamb_Phi0 * Math.PI / 180),2)));
		double Lamb_R0 = Lamb_v0 / Math.tan(Lamb_Phi0 * Math.PI / 180);
		
		double Lamb_Phi1 = 50.99879884 / 200 * 180;
		double Lamb_Phi2 = 52.99557167 / 200 * 180;

		double Lamb_v01 = Lamb_a / (Math.sqrt(1 - Math.pow (Lamb_e,2) * Math.pow(Math.sin (Lamb_Phi1 * Math.PI / 180),2)));
		double Lamb_v02 = Lamb_a / (Math.sqrt(1 - Math.pow (Lamb_e,2) * Math.pow(Math.sin (Lamb_Phi2 * Math.PI / 180),2)));
		double Lamb_Ro01 = Lamb_a * (1 - Math.pow (Lamb_e,2)) / Math.pow((Math.sqrt(1 - Math.pow (Lamb_e,2) * Math.pow(Math.sin (Lamb_Phi1 * Math.PI / 180),2))),3);
		double Lamb_Ro02 = Lamb_a * (1 - Math.pow (Lamb_e,2)) / Math.pow((Math.sqrt(1 - Math.pow (Lamb_e,2) * Math.pow(Math.sin (Lamb_Phi2 * Math.PI / 180),2))),3);
		double Lamb_m1 = 1 + Lamb_Ro01 / 2 / Lamb_v01 * Math.pow((Lamb_Phi1 - Lamb_Phi0) * Math.PI / 180,2);
		double Lamb_m2 = 1 + Lamb_Ro02 / 2 / Lamb_v02 * Math.pow((Lamb_Phi2 - Lamb_Phi0) * Math.PI / 180,2);
		double Lamb_m = (Lamb_m1 + Lamb_m2) / 2;
		double Lamb_mL = 2 - Lamb_m;

		//<!-- Rayon du parallele d origine apres reduction d echelle en metres -->
		double Lamb_mLR0 = Lamb_mL * Lamb_R0;
		
		//<!-- Rayon du parallele passant par le point recherche en metres -->
		double Lamb_R = Lamb_mLR0 * Math.exp(- Math.sin(Lamb_Phi0 * Math.PI / 180) * (Lamb_LatIso - Lamb_LatIso0));

		//<!-- Abscisse X1 dans le repere associe au meridien d origine et au parallele d origine en metres -->
		double Lamb_E1 = Lamb_R * Math.sin(Lamb_Gamma * Math.PI / 180);

		//<!-- Coordonnees Lambert II du point recherche en metres -->
		double Lamb_EE = Lamb_E1 + Lamb_Ce * 1000;
		double Lamb_NN = Lamb_mLR0 - Lamb_R + Lamb_E1 * Math.tan(Lamb_Gamma * Math.PI / 360) + Lamb_Cn * 1000;

		//<!-- arrondissement des valeurs pour affichage 3 chiffres apres la virgule -->
		double Lamb_EE_Arr = Math.round(Lamb_EE * 1000) / 1000;
		double Lamb_NN_Arr = Math.round(Lamb_NN * 1000) / 1000;
		
		return new double[]{Lamb_EE_Arr,Lamb_NN_Arr};
	//<!-- Fin de la fonction de conversion des valeurs sexagesimales NTF (geoconcept) en coordonnees Lambert II etendu -->
	}


	@SuppressWarnings("unused")
	public static double[] Hayford_Clarke(double p_Long_Deg,double p_Long_Min,double p_Long_Sec,double p_Lat_Deg,double p_Lat_Min,double p_Lat_Sec,double p_Long_sgn,double p_Lat_sgn)
	//<!-- De PRIAM (ED50) - Hayford 1909 (3) -- VERS -- GeoConcept (NTF) Sexa - Clarke 1880 (1) -->
		{
		//<!-- Constantes Ellipsoede Hayford PRIAM ED50 -->	
		double Hayford_a = 6378388.00;
		double Hayford_f = 297;
		double Hayford_b = Hayford_a - (Hayford_a / Hayford_f);

		// la valeur e est en fait e au carre

		double Hayford_e = (Math.pow(Hayford_a,2) - Math.pow(Hayford_b,2)) / Math.pow(Hayford_a,2);

		//<!-- Constantes Ellipsoede Clarke GeoConcept NTF -->
		double Clarke_a = 6378249.2;
		double Clarke_b = 6356515;
		double Clarke_f = 1 / ((Clarke_a - Clarke_b) / Clarke_a);

		// la valeur e est en fait e au carre

		double Clarke_e = (Math.pow(Clarke_a,2) - Math.pow(Clarke_b,2)) / Math.pow(Clarke_a,2);

		//<!-- Constantes Tx, Ty, Tz en metres de Transformation de 3 vers 1 -->
		double Tx = 84;
		double Ty = -37;
		double Tz = -437;

		//<!-- Coordonnees LONGITUDE Priam Saisies (Si W -> -1 ou Si E -> 1) Pri_Deg, Pri_Min, Pri_Sec (virgules autorisees) -->
		
		//<!-- Mise en Float des variables lues pour eviter les erreurs NaN (Not a Number) -->
		double Pri_Long_Sgn = parseFloat(p_Long_sgn);
		double Pri_Long_Deg = parseFloat(p_Long_Deg);
		double Pri_Long_Min = parseFloat(p_Long_Min);
		double Pri_Long_Sec = parseFloat(p_Long_Sec);

		//<!-- Coordonnees LONGITUDE Priam (Lambda) Decimale calculee -->
		double Pri_Long_Dec = Pri_Long_Sgn * ((Pri_Long_Deg) + (Pri_Long_Min / 60) + (Pri_Long_Sec / 3600));

		//<!-- Coordonnees LATITUDE Priam Saisies (Si N -> 1 ou Si S -> -1) Pri_Deg, Pri_Min, Pri_Sec (virgules autorisees) -->
		//<!-- double Pri_Lat_Signe = Test du menu deroulant N ou S generant 1 ou -1 Normalement Nord sinon Pas de Lambert ! -->
		
		//<!-- Mise en Float les variables lues pour eviter les erreurs NaN (Not a Number) -->
		double Pri_Lat_Sgn = parseFloat(p_Lat_sgn);
		double Pri_Lat_Deg = parseFloat(p_Lat_Deg);
		double Pri_Lat_Min = parseFloat(p_Lat_Min);
		double Pri_Lat_Sec = parseFloat(p_Lat_Sec);

		//<!-- Coordonnees LATITUDE Priam (Phi) Decimale calculee -->
		double Pri_Lat_Dec = Pri_Lat_Sgn * ((Pri_Lat_Deg) + (Pri_Lat_Min / 60) + (Pri_Lat_Sec / 3600));

		//<!-- Pas de consideration de hauteur (en metres) mis e 0 -- Peut etre perfectible en ajoutant les tables de hauteur -->
		double Hauteur = 0;

		//<!-- Reference Meridien de Grennwich pas decalage d'un systeme e l'autre -->

	     //<!-- Partie - coordonnees geocentriques sur l'ellipsoede 'H' Origine (Hayford 1909 - ED50 Priam)
		//<!-- Variable v (en metres) calculee par la fct Hayford_a / Racine(1-(Hayford_e*sin(Pri_Lat_Dec*(Pi/180)))^2) -->
		double H_v = Hayford_a / (Math.sqrt(1 - (Hayford_e * Math.pow(Math.sin(Pri_Lat_Dec * (Math.PI / 180)),2))));

		//<!-- Variable X calculee en metres X = (v+h).Cos(Phi).Cos(Lambda) (H_X comme Hayford Z) -->
		double H_X = (H_v + Hauteur) * Math.cos(Pri_Lat_Dec * (Math.PI / 180)) * Math.cos(Pri_Long_Dec * (Math.PI / 180));

		//<!-- Variable Y calculee en metres Y = (v+h).Cos(Phi).Sin(Lambda) (H_Y comme Hayford Z) -->
		double H_Y = (H_v + Hauteur) * Math.cos(Pri_Lat_Dec * (Math.PI / 180)) * Math.sin(Pri_Long_Dec * (Math.PI / 180));

		//<!-- Variable Z calculee en metres Z = (v*(1-e)+h).Sin(Phi) (H_Z comme Hayford Z) -->
		double H_Z = (H_v * (1 - Hayford_e) + Hauteur) * Math.sin(Pri_Lat_Dec * (Math.PI / 180));

	     //<!-- Partie - coordonnees geocentriques sur l'ellipsoede 'C' Destination (Clarke 1880 - NTF Lambet GeoConcept) -->
		//<!-- Decalage longitude Paris Greenwich (2e 20' 14.025"") - Valeur en Degres decimaux -->
		double Paris_Green = 2 + (20 / 60) + (14.025 / 3600);
		//<!-- Dans ce cas aucune difference - meme reference = Greenwich nomme Lambda_0 -->
		double Lambda_0 = 0;

		//<!-- Variable C_X calculee en metres X'= Tx + X * Cos(Lambda_0) + Y * Sin(Lambda_0) (C_X comme Clarke X) -->
		double C_X = Tx + H_X * Math.cos(Lambda_0 * (Math.PI / 180)) + H_Y * Math.sin(Lambda_0 * (Math.PI / 180));

		//<!-- Variable C_Y calculee en metres Y'= Ty - X * Sin(Lambda_0) + Y * Cos(Lambda_0) (C_Y comme Clarke Y) -->
		double C_Y = Ty - H_X * Math.sin(Lambda_0 * (Math.PI / 180)) + H_Y * Math.cos(Lambda_0 * (Math.PI / 180));

		//<!-- Variable C_Z calculee en metres Z'= Z + Tz (C_Z comme Clarke Z) -->
		double C_Z = H_Z + Tz;

		//<!-- Variable Re_1 calculee en metres Re_1 = RACINE (X'^2 + Y'^2) Re_1 Car autre formule de calcul donne meme resultat -->
		double Re_1 = Math.sqrt(Math.pow(C_X,2) + (Math.pow(C_Y,2)));

	     //<!-- Partie Coordonnees geographiques sur l'ellipsoede 'C' de destination - Clarke 1880 - Origine Greenwich -->
		//<!-- Longitude Lambda en degres decimaux sur Clarke - Long_C_Dec = Atan(Y'/X')/(Pi/180) -->
		double Long_C_Dec = Math.atan(C_Y / C_X) / (Math.PI / 180);

		//<!-- Latitude Phi en degres decimaux sur Clarke - Lat_C_Dec = difficile e enoncer calcul recurcif -->
		double ecart = 1;
		double Phi = Pri_Lat_Dec / (180 * Math.PI);
		while (ecart > 0.0000000001)
			{
			double Phi_1 = Math.atan((C_Z + Clarke_e * Math.sin(Phi) * Clarke_a / Math.sqrt(1 - Clarke_e * Math.pow(Math.sin(Phi),2))) / Re_1);
			ecart = Math.abs(Phi - Phi_1);
			Phi = Phi_1;
			}
		double Lat_C_Dec = Phi * (180 / Math.PI);

		//<!-- Variable v en metre sur Geoede de destination - C_v = Clarke_a / (racine (1 - Clarke_e * (sin(Phi * 180/PI)^2) -->
		double C_v = Clarke_a / (Math.sqrt(1 - (Clarke_e * Math.pow(Math.sin(Lat_C_Dec * (Math.PI / 180)),2))));

		//<!-- Hauteur ellipsoedale h_C en metres - Resultat e corriger pour passer e des altitudes NGF au dessus de l'ellipsoede -->
		//<!-- Formule = h_C = Re / Cos(Phi) - v -->
		double h_C = Re_1 / Math.cos(Lat_C_Dec*(Math.PI / 180)) - C_v;

	     //<!-- Tranformation Degres decimaux Longitude arrivee en degre minute seconde avec reconnaissance E W -->
		double E_W_Long_C;
		if (Long_C_Dec > 0)
			{
			E_W_Long_C = 1;
			}
		else
			{
			E_W_Long_C = -1;
			}
		Long_C_Dec = Math.abs(Long_C_Dec);
		double Deg_Long_C = Math.abs(Math.floor(Long_C_Dec));
		double Min_Long_C = Math.floor((Long_C_Dec - Deg_Long_C) * 60);
		double Sec_Long_C = Math.round(((Long_C_Dec - Deg_Long_C - Min_Long_C / 60) * 3600)*1000)/1000;
		
	     //<!-- Tranformation Degres decimaux Latitude arrivee en degre minute seconde avec reconnaissance N S -->
		double N_S_Lat_C;
		if (Lat_C_Dec > 0)
			{
			N_S_Lat_C = 1;
			}
		else
			{
			N_S_Lat_C = -1;
			}
		Lat_C_Dec = Math.abs(Lat_C_Dec);
		double Deg_Lat_C = Math.floor(Lat_C_Dec);
		double Min_Lat_C = Math.floor((Lat_C_Dec - Deg_Lat_C) * 60);
		double Sec_Lat_C = Math.round(((Lat_C_Dec - Deg_Lat_C - Min_Lat_C / 60) * 3600)*1000)/1000;
	  
	  
		double[] la_Result = NTF_Lambert(Deg_Long_C, Min_Long_C, Sec_Long_C, Deg_Lat_C, Min_Lat_C, Sec_Lat_C, E_W_Long_C, N_S_Lat_C);
		return la_Result;
	     
	//<!-- Fin de la fonction De PRIAM (ED50) - Hayford 1909 (3) -- VERS -- GeoConcept (NTF) Sexa - Clarke 1880 (1) -->
		}

	@SuppressWarnings("unused")
	public static double[] WGS_ED50(double p_Longitude,double p_Latitude)
	//<!-- De GPS (WGS84) - Iagrs80 (2) -- VERS -- ED50 Sexa - Hayford 1909 (3)-->
		{
		//<!-- Constantes Ellipsoede Hayford PRIAM ED50 -->
		
		double ED50_a = 6378388.00;
		double ED50_f = 297;
		double ED50_b = ED50_a - (ED50_a / ED50_f);

		// la valeur e est en fait e au carre

		double ED50_e = (Math.pow(ED50_a,2) - Math.pow(ED50_b,2)) / Math.pow(ED50_a,2);

		//<!-- Constantes Ellipsoede Iagrs reporte GPS WGS84 -->
		double WGS_a = 6378137;

		// La valeur f d origine Iagrs est 298.2572221010

		double WGS_f = 298.257223563;

		double WGS_b = WGS_a - (WGS_a / WGS_f);

		// la valeur e est en fait e au carre
	    
		double WGS_e = (Math.pow(WGS_a,2) - Math.pow(WGS_b,2)) / Math.pow(WGS_a,2);

		//<!-- Constantes Tx, Ty, Tz en metres de Transformation de 3 vers 2 -->
		double Tx = 84;
		double Ty = 97;
		double Tz = 117;

		//<!-- Coordonnees LONGITUDE WGS84 Saisies (Si W -> -1 ou Si E -> 1) Pri_Deg, Pri_Min, Pri_Sec (virgules autorisees) -->	
		
	    
		//<!-- Coordonnees LONGITUDE WGS84 (Lambda) Decimale calculee -->
		double WGS_Long_Dec = p_Longitude;	
	    
		//<!-- Coordonnees LATITUDE WGS84 Saisies (Si N -> 1 ou Si S -> -1) Pri_Deg, Pri_Min, Pri_Sec (virgules autorisees) -->
		//<!-- double Pri_Lat_Signe = Test du menu deroulant N ou S generant 1 ou -1 Normalement Nord sinon Pas de Lambert ! -->
		
		//<!-- Mise en Float les variables lues pour eviter les erreurs NaN (Not a Number) -->
		
	    
		//<!-- Coordonnees LATITUDE Priam (Phi) Decimale calculee -->
		double WGS_Lat_Dec = p_Latitude;

		//<!-- Pas de consideration de hauteur (en metres) mis e 0 -- Peut etre perfectible en ajoutant les tables de hauteur -->
		double Hauteur = 0;

		//<!-- Reference Meridien de Grennwich pas decalage d'un systeme e l'autre (n'affecte que le goide de Clarke) -->

	     //<!-- Partie - coordonnees geocentriques sur l'ellipsoede Origine -->
		//<!-- Variable v (en metres) calculee -->
		double WGS_v = WGS_a / (Math.sqrt(1 - (WGS_e * Math.pow(Math.sin(WGS_Lat_Dec * (Math.PI / 180)),2))));
	    
		//<!-- Variable X calculee en metres -->
		double WGS_X = (WGS_v + Hauteur) * Math.cos(WGS_Lat_Dec * (Math.PI / 180)) * Math.cos(WGS_Long_Dec * (Math.PI / 180));
	    
		//<!-- Variable Y calculee en metres Y = (v+h).Cos(Phi).Sin(Lambda) (H_Y comme Hayford Z) -->
		double WGS_Y = (WGS_v + Hauteur) * Math.cos(WGS_Lat_Dec * (Math.PI / 180)) * Math.sin(WGS_Long_Dec * (Math.PI / 180));
	     
		//<!-- Variable Z calculee en metres Z = (v*(1-e)+h).Sin(Phi) (H_Z comme Hayford Z) -->
		double WGS_Z = (WGS_v * (1 - WGS_e) + Hauteur) * Math.sin(WGS_Lat_Dec * (Math.PI / 180));
	    
	     //<!-- Partie - coordonnees geocentriques sur l'ellipsoede Destination -->
		//<!-- Decalage longitude Paris Greenwich (2e 20' 14.025"") - Valeur en Degres decimaux -->
		double Paris_Green = 2 + (20 / 60) + (14.025 / 3600);
		
		//<!-- Dans ce cas aucune difference - meme reference = Greenwich nomme Lambda_0 -->
		double Lambda_0 = 0;

		//<!-- Variable X' calculee en metres -->
		double ED50_X = Tx + WGS_X * Math.cos(Lambda_0 * (Math.PI / 180)) + WGS_Y * Math.sin(Lambda_0 * (Math.PI / 180));

		//<!-- Variable Y' calculee en metres -->
		double ED50_Y = Ty - WGS_X * Math.sin(Lambda_0 * (Math.PI / 180)) + WGS_Y * Math.cos(Lambda_0 * (Math.PI / 180));

		//<!-- Variable Z' calculee en metres -->
		double ED50_Z = WGS_Z + Tz;

		//<!-- Variable Re_1 calculee en metres Re_1 = RACINE (X'^2 + Y'^2) Re_1 Car autre formule de calcul donne meme resultat -->
		double Re_1 = Math.sqrt(Math.pow(ED50_X,2) + (Math.pow(ED50_Y,2)));

	     //<!-- Partie Coordonnees geographiques sur l'ellipsoede de destination Origine Greenwich -->
		//<!-- Longitude Lambda en degres decimaux sur Hayford -->
		double Long_ED50_Dec = Math.atan(ED50_Y / ED50_X) / (Math.PI / 180);
	    
		//<!-- Latitude Phi en degres decimaux sur Iagrs -->
		double ecart = 1;
		double Phi = WGS_Lat_Dec / (180 * Math.PI);
		while (ecart > 0.0000000001)
			{
			double Phi_1 = Math.atan((ED50_Z + ED50_e * Math.sin(Phi) * ED50_a / Math.sqrt(1 - ED50_e * Math.pow(Math.sin(Phi),2))) / Re_1);
			ecart = Math.abs(Phi - Phi_1);
			Phi = Phi_1;
			}
		double Lat_ED50_Dec = Phi * (180 / Math.PI);

		//<!-- Variable v en metre sur Geoede de destination - I_v = Iagrs_a / (racine (1 - Iagrs_e * (sin(Phi * 180/PI)^2) -->
		double ED50_v = ED50_a / (Math.sqrt(1 - (ED50_e * Math.pow(Math.sin(Lat_ED50_Dec * (Math.PI / 180)),2))));

		//<!-- Hauteur ellipsoedale h_I en metres - Resultat e corriger pour passer e des altitudes NGF au dessus de l'ellipsoede -->
		//<!-- Formule = Re / Cos(Phi) - v -->
		double h_ED50 = Re_1 / Math.cos(Lat_ED50_Dec*(Math.PI / 180)) - ED50_v;

	     //<!-- Tranformation Degres decimaux Longitude arrivee en degre minute seconde avec reconnaissance E W -->
		double E_W_Long_ED50;
		if (Long_ED50_Dec > 0)
			{
			E_W_Long_ED50 = 1;
			}
		else
			{
			E_W_Long_ED50 = -1;
			}
		Long_ED50_Dec = Math.abs(Long_ED50_Dec);
		double Deg_Long_ED50 = Math.abs(Math.floor(Long_ED50_Dec));
		double Min_Long_ED50 = Math.floor((Long_ED50_Dec - Deg_Long_ED50) * 60);
		double Sec_Long_ED50 = Math.round(((Long_ED50_Dec - Deg_Long_ED50 - Min_Long_ED50 / 60) * 3600)*1000)/1000;
		
	     //<!-- Tranformation Degres decimaux Latitude arrivee en degre minute seconde avec reconnaissance N S -->
		double N_S_Lat_ED50;
		if (Lat_ED50_Dec > 0)
			{
			N_S_Lat_ED50 = 1;
			}
		else
			{
			N_S_Lat_ED50 = -1;
			}
		Lat_ED50_Dec = Math.abs(Lat_ED50_Dec);
		double Deg_Lat_ED50 = Math.floor(Lat_ED50_Dec);
		double Min_Lat_ED50 = Math.floor((Lat_ED50_Dec - Deg_Lat_ED50) * 60);
		double Sec_Lat_ED50 = Math.round(((Lat_ED50_Dec - Deg_Lat_ED50 - Min_Lat_ED50 / 60) * 3600)*1000)/1000;	
			
		double[] la_Result = Hayford_Clarke(Deg_Long_ED50, Min_Long_ED50, Sec_Long_ED50, Deg_Lat_ED50, Min_Lat_ED50, Sec_Lat_ED50, E_W_Long_ED50, N_S_Lat_ED50);
		return la_Result;
	     
	//<!-- Fin de la fonction De GPS (WGS84) Sexa - Iagrs80 (2) -- VERS --  PRIAM (ED50) - Hayford 1909 (3) -->
		}
	
}
