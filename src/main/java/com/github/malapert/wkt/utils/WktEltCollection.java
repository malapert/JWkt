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

import com.github.malapert.wkt.utils.WktElt.WktType;
import com.github.malapert.wkt.conversion.DerivedConversion;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Creates a collection of the WKT elements.
 * @author Jean-Christophe Malapert
 */
public class WktEltCollection {

    private final List<WktElt> collection = new ArrayList<>();

    /**
     * Empty constructor.
     */
    public WktEltCollection() {

    }

    /**
     * Adds an WKT element in the collection.
     * @param elt WKT element
     */
    public void addWktElt(WktElt elt) {
        this.getCollection().add(elt);
    }
    
    /**
     * Finds the WKT element at the position <i>start</i>.
     * @param start
     * @return WKT element
     */
    public WktElt wktEltStartingAt(int start) {
        WktElt wktResult = null;
        for (WktElt wkt : getCollection()) {
            if (wkt.getStart() == start) {
                wktResult = wkt;
                break;
            }
        }
        return wktResult;
    }          
      
    /**
     * Tests if the <i>wkt1</i> element is included in the <i>wkt2</i>.     
     * 
     * @param wkt1 WKT element
     * @param wkt2 WKT element
     * @return True when the <i>wkt1</i> element is included in the <i>wkt2</i>
     * element otherwise False
     */
    public static boolean isWkt1inWkt2(WktElt wkt1, WktElt wkt2) {
        return (wkt2.getStart() <= wkt1.getStart() && wkt1.getStop()<=wkt2.getStop());
    }

    /**
     * Returns the collection of WKT elements.
     * @return the collection
     */
    public List<WktElt> getCollection() {
        return collection;
    }
    
    /**
     * Iterates on each WKT element from the collection.
     * @return the iterator on the collection
     */
    public Iterator<WktElt> iterator() {
        return collection.iterator();
    }
    
    /**
     * Iterates on each sub WKT element from a WKT element of the collection.
     * @param wktIn WKT element
     * @return the iterator sub elements from the the WKT element
     */    
    public Iterator<WktElt> iterator(final WktElt wktIn) {
        List<WktElt> findWktElt = new ArrayList<>();
        for (WktElt wkt : getCollection()) {
            if(wkt.getStart() >= wktIn.getStart() 
               && wkt.getStop() <= wktIn.getStop()) {
                findWktElt.add(wkt);
            }
        }
        return findWktElt.iterator();
    }

    /**
     * Returns the attributes of a WKT node from a WKT element.
     * @param wktIn the WKT element
     * @param nodeName the node to extract
     * @return the list of attributes of the node
     */
    public List<WktElt> getAttributesFor(final WktElt wktIn, final String nodeName) {
        List<WktElt> attributes = new ArrayList<>();
        Iterator<WktElt> iter = iterator(wktIn);
        while(iter.hasNext()) {
            WktElt wktElt = iter.next();
            if(WktType.ATTRIBUTE == wktElt.getWktType() && nodeName.equals(wktElt.getNode())) {
                attributes.add(wktElt);
            }
        }
        return attributes;
    }
    
    /**
     * Returns the sub nodes of a WKT node from a WKT element.
     * @param wktIn the WKT element
     * @param nodeName the node to extract
     * @return the list of sub nodes from the node
     */    
    public List<WktElt> getNodesFor(final WktElt wktIn, final String nodeName) {
        List<WktElt> nodes = new ArrayList<>();
        Iterator<WktElt> iter = iterator(wktIn);
        while(iter.hasNext()) {
            WktElt wktElt = iter.next();
            if(WktType.NODE == wktElt.getWktType() && nodeName.equals(wktElt.getNode())) {
                nodes.add(wktElt);
            }
        }
        return nodes;        
    }    
    
    /**
     * Indexes the WKT element by setting the node for each WKT element.
     * 
     * The first parent is "root". The others are the keyword of each node 
     * (e.g DATUM, CS, ...)
     */
    public void index() {
        List<WktElt> wktElts = this.getCollection();
        if(wktElts.isEmpty()) {
            return;
        }
        WktElt previousWkt = wktElts.get(0);
        previousWkt.setNode("root");   
        for(int i=1;i<wktElts.size();i++) {
            WktElt current = wktElts.get(i);
            if(isWkt1inWkt2(current, previousWkt)) {
                current.setNode(previousWkt.getKeyword());                
            } else {
                for(int j=i-1;j>=0;j--) {
                    previousWkt = wktElts.get(j);
                    if(isWkt1inWkt2(current, previousWkt)) {
                        current.setNode(previousWkt.getKeyword()); 
                        break;
                    }
                }
            }          
            previousWkt = current;
        }      
    }
    
    public boolean isDerivatedCrs() {
        boolean result = false;
        for(WktElt wkt:getCollection()) {
            if(DerivedConversion.DERIVED_CONVERSION_KEYWORD.equals(wkt.getKeyword())) {
                result = true;
                break;
            }
        }
        return result;
    }

}
