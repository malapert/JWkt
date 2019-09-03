/* 
 * Copyright (C) 2016-2019 Jean-Christophe Malapert
 *
 * JWkt is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * JWkt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA 
 */
package com.github.malapert.wkt.crs;

import com.github.malapert.wkt.metadata.Identifier;
import com.github.malapert.wkt.utils.Singleton;
import com.github.malapert.wkt.utils.WktEltCollection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author malapert
 */
public class GeodeticCrsTest {
    
    public GeodeticCrsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testCreateGeodeticWkt() {
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt("GEOGCRS[\"S-95\",DATUM[\"Pulkovo 1995\",ELLIPSOID[\"Krassowsky 1940\",6378245,298.3,LENGTHUNIT[\"metre\",1.0]]],CS[ellipsoidal,2],AXIS[\"latitude\",north,ORDER[1]],AXIS[\"longitude\",east,ORDER[2]],ANGLEUNIT[\"degree\",0.0174532925199433],REMARK[\"Система Геодеэических Координвт года 1995(СК-95)\"]]");
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        GeodeticCrs geo = new GeodeticCrs(col.getCollection().get(0));
        assertEquals("GEOGCRS[\"S-95\",DATUM[\"Pulkovo 1995\",ELLIPSOID[\"Krassowsky 1940\",6378245,298.3,LENGTHUNIT[\"metre\",1.0]]],CS[ellipsoidal,2],AXIS[\"latitude\",north,ORDER[1]],AXIS[\"longitude\",east,ORDER[2]],ANGLEUNIT[\"degree\",0.0174532925199433],REMARK[\"Система Геодеэических Координвт года 1995(СК-95)\"]]", geo.toWkt("", "", 0).toString());
    }

    @Test
    public void testCreateGeodeticWkt1() {
        final String CRS = "GEODCRS[\"JGD2000\",DATUM[\"Japanese Geodetic Datum 2000\",ELLIPSOID[\"GRS 1980\",6378137,298.257222101]],CS[Cartesian,3],AXIS[\"(X)\",geocentricX],AXIS[\"(Y)\",geocentricY],AXIS[\"(Z)\",geocentricZ],LENGTHUNIT[\"metre\",1.0],USAGE[SCOPE[\"Geodesy, topographic mapping and cadastre\"],AREA[\"Japan\"],BBOX[17.09,122.38,46.05,157.64],TIMEEXTENT[2002-04-01,2011-10-21]],ID[\"EPSG\",4946,URI[\"urn:ogc:def:crs:EPSG::4946\"]],REMARK[\"注：JGD2000ジオセントリックは現在JGD2011に代わりました。\"]]";
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt(CRS);
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        GeodeticCrs geo = new GeodeticCrs(col.getCollection().get(0));
        assertEquals(CRS, geo.toWkt("", "", 0).toString());
    }    
    
    @Test
    public void testCreateGeodeticWkt2() {
        final String CRS = "GEOGCRS[\"WGS 84 (G1762)\",DYNAMIC[FRAMEEPOCH[2005.0]],TRF[\"World Geodetic System 1984 (G1762)\",ELLIPSOID[\"WGS 84\",6378137,298.257223563,LENGTHUNIT[\"metre\",1.0]]],CS[ellipsoidal,3],AXIS[\"(lat)\",north,ANGLEUNIT[\"degree\",0.0174532925199433]],AXIS[\"(lon)\",east,ANGLEUNIT[\"degree\",0.0174532925199433]],AXIS[\"ellipsoidal height (h)\",up,LENGTHUNIT[\"metre\",1.0]]]"; 
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt(CRS);
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        GeodeticCrs geo = new GeodeticCrs(col.getCollection().get(0));
        assertEquals(CRS, geo.toWkt("", "", 0).toString());
    }  
    
    @Test
    public void testCreateGeodeticWkt3() {
        final String CRS = "GEOGRAPHICCRS[\"NAD83\",DATUM[\"North American Datum 1983\",ELLIPSOID[\"GRS 1980\",6378137,298.257222101,LENGTHUNIT[\"metre\",1.0]]],CS[ellipsoidal,2],AXIS[\"latitude\",north],AXIS[\"longitude\",east],ANGLEUNIT[\"degree\",0.017453292519943],ID[\"EPSG\",4269],REMARK[\"1986 realisation\"]]";
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt(CRS);
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        GeodeticCrs geo = new GeodeticCrs(col.getCollection().get(0));
        assertEquals(CRS, geo.toWkt("", "", 0).toString());
    } 
    
    @Test
    public void testCreateGeodeticWkt4() {
        final String CRS = "GEOGCRS[\"NTF (Paris)\",DATUM[\"Nouvelle Triangulation Francaise\",ELLIPSOID[\"Clarke 1880 (IGN)\",6378249.2,293.4660213]],PRIMEM[\"Paris\",2.5969213],CS[ellipsoidal,2],AXIS[\"latitude\",north,ORDER[1]],AXIS[\"longitude\",east,ORDER[2]],ANGLEUNIT[\"grad\",0.015707963267949],REMARK[\"Nouvelle Triangulation Française\"]]";
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt(CRS);
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        GeodeticCrs geo = new GeodeticCrs(col.getCollection().get(0));
        assertEquals(CRS, geo.toWkt("", "", 0).toString());
    }     
}
