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
 * Any information contained in a remark is informative. 
 * 
 * It does not modify the defining parameters of an object. A remark may be 
 * applied to a coordinate reference system, coordinate operation or boundCRS as
 * a whole. A remark should not be included in the WKT for components of a 
 * coordinate reference system or coordinate operation, but a remark in the 
 * coordinate reference system or coordinate operation object may include 
 * information about these components.
 * 
 * <p>
 * <pre>
 * {@code 
 * <remark> ::=	<remark keyword> <left delimiter> <quoted Unicode text> <right delimiter>  
 * }
 * </pre> 
 * 
 * @author Jean-Christophe Malapert
 */
public class Remark implements WktDescription {
    public final static String REMARK_KEYWORD = "REMARK";
    private String text;
    
    /**
     * Creates a Remark based on the text.
     * @param text 
     */
    public Remark(final String text) {
        this.text = text;
    }
    
    /**
     * Creates a Remark by parsing the REMARK WKT element.
     * @param geoDcrsElt the REMARK WKT element
     */
    public Remark(WktElt geoDcrsElt) {
        parse(geoDcrsElt);
    }
    
    /**
     * Parses the REMARK WKT element.
     * @param remarkWktElts the REMARK WKT element
     */
    private void parse(final WktElt remarkWktElts) {
        final WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
        final List<WktElt> attributes = wktEltCollection.getAttributesFor(remarkWktElts, REMARK_KEYWORD);
        this.setText(Utils.removeQuotes(attributes.get(0).getKeyword()));   
    }    

    /**
     * Returns the text.
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text.
     * @param text the text to set
     */
    public void setText(final String text) {
        this.text = text;
    }

    @Override
    public StringBuffer toWkt(final String endLine, final String tab, int deepLevel) {
        StringBuffer wkt = new StringBuffer();
        wkt = wkt.append(REMARK_KEYWORD).append(LEFT_DELIMITER);
        wkt = wkt.append(Utils.addQuotes(getText()));
        wkt = wkt.append(RIGHT_DELIMITER);
        return wkt;
    }   
    
    @Override
    public StringBuffer toWkt() {
        return toWkt("\n", "   ", 0);
    }    
}
