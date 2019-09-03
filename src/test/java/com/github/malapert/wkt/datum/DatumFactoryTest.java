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
package com.github.malapert.wkt.datum;

import com.github.malapert.wkt.crs.CoordinateReferenceSystemFactory;
import com.github.malapert.wkt.cs.PrimeMeridian;
import com.github.malapert.wkt.utils.Singleton;
import com.github.malapert.wkt.utils.WktElt;
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
public class DatumFactoryTest {
    
    public DatumFactoryTest() {
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

    /**
     * Test of createFromWkt method, of class DatumFactory.
     */
    @Test
    public void testCreateDatumWkt() {
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt("DATUM[\"North American Datum 1983\",ELLIPSOID[\"GRS 1980\",6378137,298.257222101,LENGTHUNIT[\"metre\",1.0]]]");
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        Datum datum = DatumFactory.createFromWkt(col.getCollection().get(0));
        assertEquals("DATUM[\"North American Datum 1983\",ELLIPSOID[\"GRS 1980\",6378137,298.257222101,LENGTHUNIT[\"metre\",1.0]]]", datum.toWkt("", "", 0).toString()); 
    }
    
    @Test
    public void testCreateDatumWkt1() {
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt("TRF[\"World Geodetic System 1984\",ELLIPSOID[“WGS 84”,6378388.0,298.257223563,LENGTHUNIT[\"metre\",1.0]]],PRIMEM[\"Greenwich\",0.0]");
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        Datum datum = DatumFactory.createFromWkt(col.getCollection().get(0));
        assertEquals("TRF[\"World Geodetic System 1984\",ELLIPSOID[“WGS 84”,6378388.0,298.257223563,LENGTHUNIT[\"metre\",1.0]]],PRIMEM[\"Greenwich\",0.0]", datum.toWkt("", "", 0).toString()); 
    }   
    
    @Test
    public void testCreateDatumWkt2() {
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt("GEODETICDATUM[\"Tananarive 1925\",ELLIPSOID[\"International 1924\",6378388.0,297.0,LENGTHUNIT[\"metre\",1.0]],ANCHOR[\"Tananarive observatory:21.0191667gS, 50.23849537gE of Paris\"]],PRIMEM[\"Paris\",2.5969213,ANGLEUNIT[\"grad\",0.015707963267949]]");
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        Datum datum = DatumFactory.createFromWkt(col.getCollection().get(0));
        System.out.println(datum.toWkt("", "", 0).toString());
        assertEquals("GEODETICDATUM[\"Tananarive 1925\",ELLIPSOID[\"International 1924\",6378388.0,297.0,LENGTHUNIT[\"metre\",1.0]],ANCHOR[\"Tananarive observatory:21.0191667gS, 50.23849537gE of Paris\"]],PRIMEM[\"Paris\",2.5969213,ANGLEUNIT[\"grad\",0.015707963267949]]", datum.toWkt("", "", 0).toString()); 
    }    
    
}
//TODO 8.4
//TODO Check 7.6, 7.7