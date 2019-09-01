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

/**
 * Interface that provides the export as WKT description.
 * @author Jean-Christophe Malapert
 */
public interface WktDescription {
    
    public static final String LEFT_DELIMITER = "[";
    public static final String RIGHT_DELIMITER = "]";
    public static final String WKT_SEPARATOR = ",";      
    
    /**
     * Returns the WKT description of the coordinate reference system.
     * @param endLine
     * @param tab
     * @param deepLevel deep level of the tree for displaying
     * @return the WKT description of the coordinate reference system
     */    
    public StringBuffer toWkt(final String endLine, final String tab, int deepLevel);
    
    public StringBuffer toWkt();
    
}
