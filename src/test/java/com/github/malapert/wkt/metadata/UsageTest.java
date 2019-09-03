/* 
 * Copyright (C) 2016-2019 Jean-Christophe Malapert
 *
  * This JWkt is free software; you can redistribute it and/or
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

import java.util.Arrays;
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
public class UsageTest {
        
    Usage usage;    
    
    public UsageTest() {        
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        ExtentFactory.AreaDescription area = new ExtentFactory.AreaDescription("Netherlands offshore.");
        ExtentFactory.TemporalExtent tmp = new ExtentFactory.TemporalExtent("1976-01", "2001-04");
        List<Extent> extents = Arrays.asList(area, tmp);
        usage = new Usage(new Scope("Spatial referencing"),extents);
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testGetScope() {
        final Scope scope = usage.getScope();
        assertEquals("testGetScope", "Spatial referencing", scope.getDescription());                
    }
    
    @Test
    public void testSetScope() {
        usage.setScope(new Scope("test"));
        final Scope scope = usage.getScope();
        assertEquals("testSetScope", "test", scope.getDescription());        
    }
    
    @Test
    public void testSetExtent() {
        Extent tmp = new ExtentFactory.TemporalExtent("1976-01", "2009-04");        
        usage.setExtentList(Arrays.asList(tmp));
        Extent ext = usage.getExtentList().get(0);
        assertEquals("testSetExtent", "2009-04", ext.getTemporalElement().getStop());                        
    }
    
    @Test
    public void testGetExtent() {
        Extent ext = usage.getExtentList().get(1);
        assertEquals("testGetExtent", "2001-04", ext.getTemporalElement().getStop());         
    }  
    
    @Test
    public void testToWkt1() {              
        ExtentFactory.AreaDescription area = new ExtentFactory.AreaDescription("Netherlands offshore.");
        ExtentFactory.TemporalExtent tmp = new ExtentFactory.TemporalExtent("1976-01", "2001-04");
        List<Extent> extents = Arrays.asList(area, tmp);
        Usage usage1 = new Usage(new Scope("Spatial referencing"),extents);
        assertEquals("USAGE[SCOPE[\"Spatial referencing\"],AREA[\"Netherlands offshore.\"],TIMEEXTENT[1976-01,2001-04]]", usage1.toWkt("","",0).toString());
    }

    @Test
    public void testToWkt2() {
        ExtentFactory.AreaDescription area = new ExtentFactory.AreaDescription("Finland - onshore between 26°30’E and 27°30’E.");
        ExtentFactory.GeographicBoundingBox geo = new ExtentFactory.GeographicBoundingBox(60.36f,26.5f,70.05f,27.5f, 2);
        List<Extent> extents = Arrays.asList(area, geo);
        Usage usage2 = new Usage(new Scope("Cadastre."),extents);
        System.out.println(usage2.toWkt("","",0).toString());
        assertEquals("USAGE[SCOPE[\"Cadastre.\"],AREA[\"Finland - onshore between 26°30’E and 27°30’E.\"],BBOX[60.36,26.50,70.05,27.50]]", usage2.toWkt("","",0).toString());
    }    
}
