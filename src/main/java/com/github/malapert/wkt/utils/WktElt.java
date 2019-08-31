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
package com.github.malapert.wkt.utils;

/**
 * Stores the WKT elements of the WKT description into a data structure.
 * @author Jean-Christophe Malapert
 */
public class WktElt {
    /**
     * keyword as node or attribute value.
     */
    private String keyword;
    /**
     * start position in the WKT description.
     */
    private int start;
    /**
     * stop position in the WKT description.
     */
    private int stop;
    /**
     * Node value.
     */
    private String node;
    /**
     * Type of a WKT element : either attribute or node.
     */
    private WktType wktType;      
   
    public boolean derivated = false;
    
    /**
     * The type of a WKT element.
     */
    public enum WktType {
        NODE,ATTRIBUTE
    }
    
    /**
     * Construct a WKT index.
     * @param keyword keyword or attribute to store
     * @param start start position of the WKT element
     * @param type type of the WKT element : either node or attribute
     */
    public WktElt(final String keyword, int start, final WktType type) {
        this.keyword = keyword;
        this.start = start;
        this.wktType = type;
    }
    
    /**
     * Returns the type of the WKT element.
     * @return the wktType
     */
    public WktType getWktType() {
        return wktType;
    }

    /**
     * Sets the type of the WKT element.
     * @param wktType the wktType to set
     */
    public void setWktType(final WktType wktType) {
        this.wktType = wktType;
    }    

    /**
     * Returns the keyword or attribute value of the WKT description.
     * @return the keyword or attribute value
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Sets the keyword or the attribute of the WKT description.
     * @param keyword the keyword or attribute to set
     */
    public void setKeyword(final String keyword) {
        this.keyword = keyword;
    }

    /**
     * Start position of the WKT element in the WKT description.
     * @return the start position
     */
    public int getStart() {
        return start;
    }

    /**
     * Sets the start position of the WKT element in the WKT description.
     * @param start the start to set
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * Stop position of the WKT element in the WKT description.
     * @return the stop
     */
    public int getStop() {
        return stop;
    }

    /**
     * Sets the stop position of the WKT element in the WKT description.     
     * @param stop the stop to set
     */
    public void setStop(int stop) {
        this.stop = stop;
    }

    /**
     * Return the node of the WKT element.
     * @return the node
     */
    public String getNode() {
        return node;
    }

    /**
     * Sets the node of the WKT element.
     * @param node the node to set
     */
    public void setNode(final String node) {
        this.node = node;
    }           
    
    /**
     * @return the derivated
     */
    public boolean isDerivated() {
        return derivated;
    }

    /**
     * @param derivated the derivated to set
     */
    public void setDerivated(boolean derivated) {
        this.derivated = derivated;
    }    
}
