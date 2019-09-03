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
public class IdentifierTest {
    
    public IdentifierTest() {
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
     * Test of getAuthorityName method, of class Identifier.
     */
    @Test
    public void testGetAuthorityName() {
        System.out.println("getAuthorityName");
        Identifier instance = new Identifier("name", "id");
        String expResult = "name";
        String result = instance.getAuthorityName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setAuthorityName method, of class Identifier.
     */
    @Test
    public void testSetAuthorityName() {
        System.out.println("setAuthorityName");
        String authorityName = "test";
        Identifier instance = new Identifier("name", "id");
        instance.setAuthorityName(authorityName);
        assertEquals(authorityName, instance.getAuthorityName());
    }

    /**
     * Test of getAuthorityUniqueIdentifier method, of class Identifier.
     */
    @Test
    public void testGetAuthorityUniqueIdentifier() {
        System.out.println("getAuthorityUniqueIdentifier");
        Identifier instance = new Identifier("name", "id");
        String expResult = "id";
        String result = instance.getAuthorityUniqueIdentifier();
        assertEquals(expResult, result);
    }

    /**
     * Test of setAuthorityUniqueIdentifier method, of class Identifier.
     */
    @Test
    public void testCreateIdentifierWkt1() {
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt("ID[\"Authority name\",\"Abcd_Ef\",7.1]");
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        Identifier id = new Identifier(col.getCollection().get(0));
        assertEquals("ID[\"Authority name\",\"Abcd_Ef\",7.1]", id.toWkt("", "", 0).toString());
    }

    @Test
    public void testCreateIdentifierWkt2() {
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt("ID[\"EPSG\",4326]");
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        Identifier id = new Identifier(col.getCollection().get(0));
        assertEquals("ID[\"EPSG\",4326]", id.toWkt("", "", 0).toString());
    }

    @Test
    public void testCreateIdentifierWkt3() {
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt("ID[\"EPSG\",4326,URI[\"urn:ogc:def:crs:EPSG::4326\"]]");
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        Identifier id = new Identifier(col.getCollection().get(0));
        assertEquals("ID[\"EPSG\",4326,URI[\"urn:ogc:def:crs:EPSG::4326\"]]", id.toWkt("", "", 0).toString());
    }  
    
    @Test
    public void testCreateIdentifierWkt4() {
        CoordinateReferenceSystemFactory.ParserWkt parser = new CoordinateReferenceSystemFactory.ParserWkt("ID[\"EuroGeographics\",\"ES_ED50 (BAL99) to ETRS89\",\"2001-04-20\"]");
        WktEltCollection col = parser.createsWktIndex();
        Singleton.getInstance().setCollection(col);
        Identifier id = new Identifier(col.getCollection().get(0));
        assertEquals("ID[\"EuroGeographics\",\"ES_ED50 (BAL99) to ETRS89\",\"2001-04-20\"]", id.toWkt("", "", 0).toString());
    }     
}
