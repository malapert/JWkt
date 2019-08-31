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
        WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
        List<WktElt> attributes = wktEltCollection.getAttributesFor(remarkWktElts, REMARK_KEYWORD);
        this.setText(attributes.get(0).getKeyword());   
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
    public StringBuffer toWkt(int deepLevel) {
        StringBuffer wkt = new StringBuffer();
        wkt = wkt.append(REMARK_KEYWORD).append(LEFT_DELIMITER);
        wkt = wkt.append(getText());
        wkt = wkt.append(RIGHT_DELIMITER);
        return wkt;
    }    
}
