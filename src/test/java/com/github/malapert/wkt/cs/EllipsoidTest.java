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
package com.github.malapert.wkt.cs;

import com.github.malapert.wkt.crs.CoordinateReferenceSystemFactory;
import com.github.malapert.wkt.metadata.Identifier;
import com.github.malapert.wkt.metadata.Remark;
import com.github.malapert.wkt.metadata.UnitFactory;
import com.github.malapert.wkt.utils.Singleton;
import com.github.malapert.wkt.utils.WktEltCollection;
import java.util.List;
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
public class EllipsoidTest {
    
    public EllipsoidTest() {
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
     * Test of getEllipsoid method, of class Ellipsoid.
     */
    @Test
    public void testCreateEllipsoidWkt() {
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt("ELLIPSOID[\"GRS 1980\",6378137,298.257222101,LENGTHUNIT[\"metre\",1.0]]");
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        Ellipsoid ellipsoid = new Ellipsoid(col.getCollection().get(0));
        assertEquals("ELLIPSOID[\"GRS 1980\",6378137,298.257222101,LENGTHUNIT[\"metre\",1.0]]", ellipsoid.toWkt("", "", 0).toString());
    }
    
    @Test
    public void testCreateSpheroidWkt() {
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt("SPHEROID[\"GRS 1980\",6378137.0,298.257222101]");
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        Ellipsoid ellipsoid = new Ellipsoid(col.getCollection().get(0));
        assertEquals("SPHEROID[\"GRS 1980\",6378137.0,298.257222101]", ellipsoid.toWkt("", "", 0).toString());
    }    

    @Test
    public void testCreateEllipsoidWkt1() {
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt("ELLIPSOID[\"Clark 1866\",20925832.164,294.97869821,LENGTHUNIT[\"US survey foot\",0.304800609601219]]");
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        Ellipsoid ellipsoid = new Ellipsoid(col.getCollection().get(0));
        assertEquals("ELLIPSOID[\"Clark 1866\",20925832.164,294.97869821,LENGTHUNIT[\"US survey foot\",0.304800609601219]]", ellipsoid.toWkt("", "", 0).toString());
    } 

    @Test
    public void testCreateSpheroidWkt1() {
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt("ELLIPSOID[\"Sphere\",6371000,0,LENGTHUNIT[\"metre\",1.0]]");
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        Ellipsoid ellipsoid = new Ellipsoid(col.getCollection().get(0));
        assertEquals("ELLIPSOID[\"Sphere\",6371000,0,LENGTHUNIT[\"metre\",1.0]]", ellipsoid.toWkt("", "", 0).toString());
    }     
}
