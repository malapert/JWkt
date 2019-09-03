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
        System.out.println(geo.toWkt("", "", 0).toString());
        assertEquals("GEOGCRS[\"S-95\",DATUM[\"Pulkovo 1995\",ELLIPSOID[\"Krassowsky 1940\",6378245,298.3,LENGTHUNIT[\"metre\",1.0]]],CS[ellipsoidal,2],AXIS[\"latitude\",north,ORDER[1]],AXIS[\"longitude\",east,ORDER[2]],ANGLEUNIT[\"degree\",0.0174532925199433],REMARK[\"Система Геодеэических Координвт года 1995(СК-95)\"]]", geo.toWkt("", "", 0).toString());
    }
    
}
