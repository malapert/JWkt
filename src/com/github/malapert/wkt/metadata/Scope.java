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

import com.github.malapert.wkt.utils.Singleton;
import com.github.malapert.wkt.utils.WktElt;
import com.github.malapert.wkt.utils.WktEltCollection;
import java.util.List;

/**
 * The BNF element "scope extent identifier remark" is a collection of four 
 * optional attributes which may be applied to a coordinate reference system, 
 * a coordinate operation or a boundCRS.
 * 
 * The "scope extent identifier remark" collection is to simplify the BNF 
 * through grouping; each of the four attributes may appear separately in a WKT 
 * string
 * 
 * <pre>
 * {@code
 * <scope extent identifier remark>::=[ <wkt separator> <scope> ]  [ { <wkt separator> <extent> } ]...  [ { <wkt separator> <identifier> } ]…  [ <wkt separator> <remark>]
 * }
 * </pre>
 *
 * @author Jean-Christophe Malapert
 */
public class Scope implements WktDescription {
    
    public final static String SCOPE_KEYWORD = "SCOPE";
    protected String description; 
    protected WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();

    public Scope(final String description) {
        this.description = description;
    }
    
    public Scope(WktElt scopeWktElts) {
        parse(scopeWktElts);
    }
    
    /**
     * Returns the description.
     * @return the description
     */
    public String getDescription() {
        return description;
    }       

    /**
     * Sets the description.
     * @param description the description to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }    

    /**
     * Parses the SCOPE WKT element.
     * @param scopeWktElts the SCOPE WKT element
     */
    protected void parse(final WktElt scopeWktElts) {        
        List<WktElt> attributes = wktEltCollection.getAttributesFor(scopeWktElts, Scope.SCOPE_KEYWORD);
        this.setDescription(attributes.get(0).getKeyword());
    }

    @Override
    public StringBuffer toWkt(int deepLEvel) {
        StringBuffer wkt = new StringBuffer();
        wkt = wkt.append(Scope.SCOPE_KEYWORD).append(LEFT_DELIMITER);
        wkt = wkt.append(this.getDescription());
        wkt = wkt.append(RIGHT_DELIMITER);
        return wkt;
    }
}
