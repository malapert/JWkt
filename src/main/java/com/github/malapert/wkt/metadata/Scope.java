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

import com.github.malapert.wkt.utils.Singleton;
import com.github.malapert.wkt.utils.Utils;
import com.github.malapert.wkt.utils.WktElt;
import com.github.malapert.wkt.utils.WktEltCollection;
import java.util.List;

/**
 * Scope describes the purpose or purposes for which a CRS, datum, datum 
 * ensemble, coordinate operation or bound CRS is applied.
 * 
 * <pre>
 * {@code
 * <scope extent identifier remark>::=[ <wkt separator> <scope> ]  [ { <wkt separator> <extent> } ]...  [ { <wkt separator> <identifier> } ]…  [ <wkt separator> <remark>]
 * }
 * </pre>
 *
 * @author Jean-Christophe Malapert
 */
public final class Scope implements WktDescription {
    /**
     * SCOPE keyword.
     */
    public final static String SCOPE_KEYWORD = "SCOPE";
    /**
     * scope description.
     */
    protected String description; 

    /**
     * Constructs a scope based on the description.
     * @param description description
     */
    public Scope(final String description) {
        this.description = description;
    }
    
    /**
     * Constructs a scope by parsing the WKT string.
     * @param scopeWktElts 
     */
    public Scope(final WktElt scopeWktElts) {
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
    private void parse(final WktElt scopeWktElts) { 
        final WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();        
        final List<WktElt> attributes = wktEltCollection.getAttributesFor(scopeWktElts, Scope.SCOPE_KEYWORD);
        this.setDescription(Utils.removeQuotes(attributes.get(0).getKeyword()));
    }

    @Override
    public StringBuffer toWkt(final String endLine, final String tab, final int deepLevel) {
        StringBuffer wkt = new StringBuffer();
        wkt = wkt.append(Scope.SCOPE_KEYWORD).append(LEFT_DELIMITER);
        wkt = wkt.append(Utils.addQuotes(this.getDescription()));
        wkt = wkt.append(RIGHT_DELIMITER);
        return wkt;
    }
    
    @Override
    public StringBuffer toWkt() {
        return toWkt("\n", "   ", 0);
    }
}
