/* 
 * Copyright (C) 2016 Jean-Christophe Malapert
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.malapert.wkt.utils;

import com.github.malapert.wkt.crs.CoordinateReferenceSystem;
import com.github.malapert.wkt.crs.CoordinateReferenceSystemFactory;
import java.text.ParseException;

/**
 * Creates a WKT parser.
 * 
 * @author Jean-Christophe Malapert
 */
public class WKTParser {


    public static void main(String[] args) throws ParseException {
        String wkt1 = "GEODCRS[\"ICRS Equatorial\",\n"
                + "  DATUM[\"International Celestial Equatorial\",\n"
                + "    ELLIPSOID[\"(trouver un nom)\", 1, 0, LENGTHUNIT[\"ua\", 149597870691]],ANCHOR[\"fffff\"],ID[\"EuroGeographics\",\"ES_ED50 (BAL99) to ETRS89\",\"2001-04-20\"],ID[\"EPSG\",4326,URI[\"urn:ogc:def:crs:EPSG::4326\"]]],PRIMEM[\"Vernal equinox\", 0],\n"
                + "  CS[spherical, 2],\n"
                + "    AXIS[\"Declination (dec)\", north, ORDER[1]],\n"
                + "    AXIS[\"Right ascension (RA)\", east, ORDER[2]],\n"
                + "    ANGLEUNIT[\"degree\", 0.017453292519943295],\n"
                + "  SCOPE[\"fffff\"],"
                + "  AREA[\"For celestial objects: satellites, planets, stars, galaxies.\"],\n"                
                + "  ID[\"IAU\", 1]]";
        String wkt2 = "PROJCRS[\"ETRS89 Lambert Azimuthal Equal Area CRS\",  BASEGEODCRS[\"ETRS89\",\n" +
"     DATUM[\"ETRS89\",\n" +
"       ELLIPSOID[\"GRS 80\",6378137,298.257222101,LENGTHUNIT[\"metre\",1.0]]]],\n" +
"   CONVERSION[\"LAEA\",\n" +
"     METHOD[\"Lambert Azimuthal Equal Area\",ID[\"EPSG\",9820]],\n" +
"     PARAMETER[\"Latitude of origin\",52.0,\n" +
"       ANGLEUNIT[\"degree\",0.0174532925199433]],\n" +
"     PARAMETER[\"Longitude of origin\",10.0,\n" +
"      ANGLEUNIT[\"degree\",0.0174532925199433]],\n" +
"     PARAMETER[\"False easting\",4321000.0,LENGTHUNIT[\"metre\",1.0]],\n" +
"     PARAMETER[\"False northing\",3210000.0,LENGTHUNIT[\"metre\",1.0]]],\n" +
"   CS[Cartesian,2],\n" +
"     AXIS[\"(Y)\",north,ORDER[1]],\n" +
"     AXIS[\"(X)\",east,ORDER[2]],\n" +
"     LENGTHUNIT[\"metre\",1.0],\n" +
"   SCOPE[\"Description of a purpose\"],\n" +
"   AREA[\"An area description\"],\n" +
"   ID[\"EuroGeographics\",\"ETRS-LAEA\"]]";
        String wkt3 = "TIMECRS[\"GPS Time\",\n" +
"                  TDATUM[\"Time origin\",TIMEORIGIN[1980-01-01T00:00:00.0Z]],\n" +
"                  CS[temporal,1],AXIS[\"time\",future],TIMEUNIT[\"day\",86400.0]]";
        String wkt4 = "VERTCRS[\"NAVD88\",\n" +
"     VDATUM[\"North American Vertical Datum 1988\"],\n" +
"     CS[vertical,1],\n" +
"       AXIS[\"gravity-related height (H)\",up],LENGTHUNIT[\"metre\",1.0]]";
        String wkt5 = " ENGCRS[\"A ship-centred CRS\",\n" +
"     EDATUM[\"Ship reference point\",ANCHOR[\"Centre of buoyancy\"]],\n" +
"     CS[Cartesian,3],\n" +
"       AXIS[\"(x)\",forward],\n" +
"       AXIS[\"(y)\",starboard],\n" +
"       AXIS[\"(z)\",down],\n" +
"       LENGTHUNIT[\"metre\",1.0]]";
        String wkt6 = "PARAMETRICCRS[\"WMO standard atmosphere layer 0\",\n" +
"               PDATUM[\"Mean Sea Level\",ANCHOR[\"1013.25 hPa at 15°C\"]],\n" +
"               CS[parametric,1],\n" +
"               AXIS[\"pressure (hPa)\",up],PARAMETRICUNIT[\"HectoPascal\",100.0]]";
        String wkt7 = "GEODCRS[\"ETRS89 Lambert Azimuthal Equal Area CRS\",\n" +
"     BASEGEODCRS[\"WGS 84\",\n" +
"       DATUM[\"WGS 84\",\n" +
"         ELLIPSOID[\"WGS 84\",6378137,298.2572236,LENGTHUNIT[\"metre\",1.0]]]],\n" +
"     DERIVINGCONVERSION[\"Atlantic pole\",\n" +
"       METHOD[\"Pole rotation\",ID[\"Authority\",1234]],\n" +
"       PARAMETER[\"Latitude of rotated pole\",52.0,\n" +
"         ANGLEUNIT[\"degree\",0.0174532925199433]],\n" +
"       PARAMETER[\"Longitude of rotated pole\",-30.0,\n" +
"        ANGLEUNIT[\"degree\",0.0174532925199433]],\n" +
"       PARAMETER[\"Axis rotation\",-25.0,\n" +
"        ANGLEUNIT[\"degree\",0.0174532925199433]]],\n" +
"     CS[ellipsoidal,2],\n" +
"       AXIS[\"latitude\",north,ORDER[1]],\n" +
"       AXIS[\"longitude\",east,ORDER[2]],\n" +
"       ANGLEUNIT[\"degree\",0.0174532925199433]]";
        
        String wkt8 = "   COMPOUNDCRS[\"ICAO layer 0\",\n" +
"     GEODETICCRS[\"WGS 84\",\n" +
"       DATUM[\"World Geodetic System 1984\",\n" +
"         ELLIPSOID[\"WGS 84\",6378137,298.257223563,\n" +
"           LENGTHUNIT[\"metre\",1.0]]],\n" +
"       CS[ellipsoidal,2],\n" +
"         AXIS[\"latitude\",north,ORDER[1]],\n" +
"         AXIS[\"longitude\",east,ORDER[2]],\n" +
"         ANGLEUNIT[\"degree\",0.0174532925199433]],\n" +
"         PARAMETRICCRS[\"WMO standard atmosphere\",\n" +
"       PARAMETRICDATUM[\"Mean Sea Level\",\n" +
"         ANCHOR[\"Mean Sea Level = 1013.25 hPa\"]],\n" +
"           CS[parametric,1],\n" +
"             AXIS[\"pressure (P)\",unspecified],\n" +
"             PARAMETRICUNIT[\"HectoPascal\",100]]]";
        CoordinateReferenceSystem crs = CoordinateReferenceSystemFactory.createFromWkt(wkt8);
        //DerivedCoordinateReferenceSystem crs = CoordinateReferenceSystemFactory.createFromWkt(wkt8, DerivedCoordinateReferenceSystem.class);
        
        System.out.println(crs.toString());
    }
}
