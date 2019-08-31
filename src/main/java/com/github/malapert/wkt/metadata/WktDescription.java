/* 
 * Copyright (C) 2016 Jean-Christophe Malapert
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
     * @param deepLevel deep level of the tree for displaying
     * @return the WKT description of the coordinate reference system
     */    
    public StringBuffer toWkt(int deepLevel);    
    
}
