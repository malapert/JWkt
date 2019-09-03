/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.malapert.wkt.utils;

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
public class UtilsTest {
    
    public UtilsTest() {
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
     * Test of removeQuotes method, of class Utils.
     */
    @Test
    public void testRemoveQuotes() {
        String keyword = "\"test\"";
        String expResult = "test";
        String result = Utils.removeQuotes(keyword);
        assertEquals("removeQuotes", expResult, result);
    }

    /**
     * Test of addQuotes method, of class Utils.
     */
    @Test
    public void testAddQuotes() {
        String keyword = "test";
        String expResult = "\"test\"";
        String result = Utils.addQuotes(keyword);
        assertEquals("addQuotes", expResult, result);
    }

    /**
     * Test of makeSpaces method, of class Utils.
     */
    @Test
    public void testMakeSpaces() {
        String tab = "";
        int space = 0;
        StringBuffer expResult = new StringBuffer("");
        StringBuffer result = Utils.makeSpaces(tab, space);
        assertEquals("makeSpaces", expResult.toString(), result.toString());
    }

    /**
     * Test of isValidISO8601 method, of class Utils.
     */
    @Test
    public void testIsValidISO8601() {
        String dateStr = "2019";
        boolean expResult = true;
        boolean result = Utils.isValidISO8601(dateStr);
        assertEquals("isValidISO8601", expResult, result);
    }

    /**
     * Test of precision method, of class Utils.
     */
    @Test
    public void testPrecision() {
        String number = "10.45";
        int expResult = 2;
        int result = Utils.precision(number);
        assertEquals("precision", expResult, result);
    }
    
    /**
     * Test of precision method, of class Utils.
     */
    @Test
    public void testPrecisionScientific() {
        String number = "10.45e-10";
        int expResult = 2;
        int result = Utils.precision(number);
        assertEquals("precision", expResult, result);
    }    

    /**
     * Test of isNumeric method, of class Utils.
     */
    @Test
    public void testIsNumeric() {
        String strNum = "3.5";
        boolean expResult = true;
        boolean result = Utils.isNumeric(strNum);
        assertEquals("isNumeric",expResult, result);
    }
    
    
    /**
     * Test of isNumeric method, of class Utils.
     */
    @Test
    public void testIsNumericSienctific() {
        String strNum = "3.5e-10";
        boolean expResult = true;
        boolean result = Utils.isNumeric(strNum);
        assertEquals("isNumeric",expResult, result);
    }    
}
