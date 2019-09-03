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
import com.github.malapert.wkt.metadata.Unit;
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
public class PrimeMeridianTest {
    
    public PrimeMeridianTest() {
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
     * Test of getPrimeMeridianKeyword method, of class PrimeMeridian.
     */
    @Test
    public void testCreatePrimeMeridianWkt() {
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt("PRIMEM[\"Paris\",2.5969213,ANGLEUNIT[\"grad\",0.015707963267949]]");
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        PrimeMeridian prime = new PrimeMeridian(col.getCollection().get(0));
        assertEquals("PRIMEM[\"Paris\",2.5969213,ANGLEUNIT[\"grad\",0.015707963267949]]", prime.toWkt("", "", 0).toString()); 
    }

    @Test
    public void testCreatePrimeMeridianWkt1() {
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt("PRIMEM[\"Ferro\",-17.6666667]");
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        PrimeMeridian prime = new PrimeMeridian(col.getCollection().get(0));
        assertEquals("PRIMEM[\"Ferro\",-17.6666667]", prime.toWkt("", "", 0).toString()); 
    }

    @Test
    public void testCreatePrimeMeridianWkt2() {
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt("PRIMEM[\"Greenwich\",0.0, ANGLEUNIT[\"degree\",0.0174532925199433]]");
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        PrimeMeridian prime = new PrimeMeridian(col.getCollection().get(0));
        assertEquals("PRIMEM[\"Greenwich\",0.0,ANGLEUNIT[\"degree\",0.0174532925199433]]", prime.toWkt("", "", 0).toString()); 
    }    
}
