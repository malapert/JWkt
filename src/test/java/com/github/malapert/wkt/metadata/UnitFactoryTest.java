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
package com.github.malapert.wkt.metadata;

import com.github.malapert.wkt.crs.CoordinateReferenceSystemFactory;
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
public class UnitFactoryTest {
    
    public UnitFactoryTest() {
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
     * Test of createFromWkt method, of class UnitFactory.
     */
    @Test
    public void testCreateLengthUnitWkt1() {
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt("LENGTHUNIT[\"metre\",1]");
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        Unit unit = UnitFactory.createFromWkt(col.getCollection().get(0));
        assertEquals("LENGTHUNIT[\"metre\",1]", unit.toWkt("", "", 0).toString());
    }
    
    @Test
    public void testCreateLengthUnitWkt2() {
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt("LENGTHUNIT[\"German legal metre\",1.0000135965]");
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        Unit unit = UnitFactory.createFromWkt(col.getCollection().get(0));
        assertEquals("LENGTHUNIT[\"German legal metre\",1.0000135965]", unit.toWkt("", "", 0).toString());
    }

    @Test
    public void testCreateAngleUnitWkt() {
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt("ANGLEUNIT[\"degree\",0.0174532925199433]");
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        Unit unit = UnitFactory.createFromWkt(col.getCollection().get(0));
        assertEquals("ANGLEUNIT[\"degree\",0.0174532925199433]", unit.toWkt("", "", 0).toString());
    } 

    @Test
    public void testCreateScaleUnitWkt() {
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt("SCALEUNIT[\"parts per million\",1E-06]");
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        Unit unit = UnitFactory.createFromWkt(col.getCollection().get(0));
        //assertEquals("SCALEUNIT[\"parts per million\",1E-06]", unit.toWkt("", "", 0).toString());
    } 
    
    @Test
    public void testCreateParametricUnitWkt() {
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt("PARAMETRICUNIT[\"hectopascal\",100]");
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        Unit unit = UnitFactory.createFromWkt(col.getCollection().get(0));
        assertEquals("PARAMETRICUNIT[\"hectopascal\",100]", unit.toWkt("", "", 0).toString());
    }    
    
    @Test
    public void testCreateTimeUnitWkt1() {
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt("TIMEUNIT[\"millisecond\",0.001]");
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        Unit unit = UnitFactory.createFromWkt(col.getCollection().get(0));
        assertEquals("TIMEUNIT[\"millisecond\",0.001]", unit.toWkt("", "", 0).toString());
    }   
    
    @Test
    public void testCreateTimeUnitWkt2() {
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt("TIMEUNIT[\"calendar month\"]");
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        Unit unit = UnitFactory.createFromWkt(col.getCollection().get(0));
        assertEquals("TIMEUNIT[\"calendar month\"]", unit.toWkt("", "", 0).toString());
    }  

    @Test
    public void testCreateTimeUnitWkt3() {
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt("TIMEUNIT[\"calendar second\"]");
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        Unit unit = UnitFactory.createFromWkt(col.getCollection().get(0));
        assertEquals("TIMEUNIT[\"calendar second\"]", unit.toWkt("", "", 0).toString());
    }     
    
    @Test
    public void testCreateTimeUnitWkt4() {
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt("TIMEUNIT[\"day\",86400.0]");
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        Unit unit = UnitFactory.createFromWkt(col.getCollection().get(0));
        assertEquals("TIMEUNIT[\"day\",86400.0]", unit.toWkt("", "", 0).toString());
    }      
}
