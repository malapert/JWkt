/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testGetScope() {
        
        
 //assertTrue("Testing https request:", response != null && response.getStatusLine().
//                getStatusCode() == 200);        
    }
    
    @Test
    public void testSetScope() {
        
    }
    
    @Test
    public void testSetExtent() {
        
    }
    
    @Test
    public void testGetExtent() {
        
    }  
    
    @Test
    public void testToWkt() {
        ExtentFactory.AreaDescription area = new ExtentFactory.AreaDescription("Netherlands offshore.");
        ExtentFactory.TemporalExtent tmp = new ExtentFactory.TemporalExtent("1976-01", "2001-04");
        List<Extent> extents = Arrays.asList(area, tmp);
        Usage usage1 = new Usage(new Scope("Spatial referencing"),extents);
        System.out.println("u1:"+usage1.toWkt(1));
        
        area = new ExtentFactory.AreaDescription("Finland - onshore between 26°30’E and 27°30’E.");
        ExtentFactory.GeographicBoundingBox geo = new ExtentFactory.GeographicBoundingBox(60.36f,26.5f,70.05f,27.5f);
        extents = Arrays.asList(area, geo);
        Usage usage2 = new Usage(new Scope("Cadastre."),extents);
        System.out.println("u2:"+usage2.toWkt(1)); 
        
        ScopeExtent scope = new ScopeExtent();
        scope.getUsageList().add(usage1);
        scope.getUsageList().add(usage2);
        System.out.println("u12:"+scope.toWkt(1));
    }
    
}
