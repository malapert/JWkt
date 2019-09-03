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
import com.github.malapert.wkt.utils.WKTParser;
import com.github.malapert.wkt.utils.WktElt;
import com.github.malapert.wkt.utils.WktEltCollection;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
public class ExtentFactoryTest {
    
    public ExtentFactoryTest() {
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
     * Test of createFromWkt method, of class ExtentFactory.
     */
    @Test
    public void testCreateAreaDescription() {
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt("AREA[\"Netherlands offshore.\"]");
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        String expResult = "Netherlands offshore.";          
        Extent extent = ExtentFactory.createFromWkt(col.getCollection().get(0));
        assertEquals(expResult, extent.getAreaDescription().getDescription());
        assertEquals("wkt", "AREA[\"Netherlands offshore.\"]", extent.toWkt("","",0).toString());                                
    }
    
    @Test
    public void testCreateBoundingBox() {
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt("BBOX[-55.95,160.60,-25.88,-171.20]");
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        float expResult = 160.60f;          
        Extent extent = ExtentFactory.createFromWkt(col.getCollection().get(0));
        assertEquals(expResult, extent.getGeographicElement().getLowerLeftLongitude(), 0.01);
        assertEquals("wkt", "BBOX[-55.95,160.60,-25.88,-171.20]", extent.toWkt("","",0).toString());                        
    }   
    
    @Test
    public void testVerticalExtend() {
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt("VERTICALEXTENT[-1000,0,LENGTHUNIT[\"metre\",1.0]]");
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        String expResultUnit = "metre"; 
        float expResultValue = 1.0f;
        float expResultVerticalExt = -1000f;
        Extent extent = ExtentFactory.createFromWkt(col.getCollection().get(0));
        assertEquals("length value",expResultValue, extent.getVerticalElement().getLengthUnit().getConversionFactor(), 0.1);        
        assertEquals("length unit",expResultUnit, extent.getVerticalElement().getLengthUnit().getUnitName());
        assertEquals("min Height",expResultVerticalExt, extent.getVerticalElement().getMinimumHeight(), 0.1);  
        assertEquals("wkt", "VERTICALEXTENT[-1000,0,LENGTHUNIT[\"metre\",1.0]]", extent.toWkt("","",0).toString());                
    }  

    @Test
    public void testCreateTimeExtent1() {
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt("TIMEEXTENT[2013-01-01,2013-12-31]");
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        String expResult = "2013-01-01";          
        Extent extent = ExtentFactory.createFromWkt(col.getCollection().get(0));
        assertEquals("date range as float", expResult, extent.getTemporalElement().getStart());
        assertEquals("wkt", "TIMEEXTENT[2013-01-01,2013-12-31]", extent.toWkt("","",0).toString());        
    }   
    
    @Test
    public void testCreateTimeExtent2() {
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt("TIMEEXTENT[\"Jurassic\",\"Quaternary\"]");
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        String expResult = "Jurassic";          
        Extent extent = ExtentFactory.createFromWkt(col.getCollection().get(0));
        assertEquals("date range as string", expResult, extent.getTemporalElement().getStart());
        assertEquals("wkt", "TIMEEXTENT[\"Jurassic\",\"Quaternary\"]", extent.toWkt("","",0).toString());
    }   
}
