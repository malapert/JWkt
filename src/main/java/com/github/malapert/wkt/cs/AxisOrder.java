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
package com.github.malapert.wkt.cs;

import com.github.malapert.wkt.utils.Singleton;
import com.github.malapert.wkt.metadata.WktDescription;
import com.github.malapert.wkt.utils.WktElt;
import com.github.malapert.wkt.utils.WktEltCollection;
import java.util.List;

/**
 * Defines the axis order when defining a coordinate system.
 * 
 * <pre>
 * {@code
 * <axis order>::=<axis order keyword> <left delimiter> <unsigned integer> <right delimiter>
 * }
 * </pre>
 * @author Jean-Christophe Malapert
 */
public class AxisOrder implements WktDescription {
    
    public static final String ORDER_KEYWORD = "ORDER";
    
    private int text;    
 
    /**
     * Creates an AxisOrder. 
     * @param text 
     */
    public AxisOrder(final int text) {
        this.text = text;
    }
            
    /**
     * Creates an AxisOrder by parsing the ORDER WKT element.
     * @param axisElt 
     */
    public AxisOrder(final WktElt axisElt) {
        parse(axisElt);
    }
    
    private void parse(final WktElt axisWktElts) {
        WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
        List<WktElt> attributes = wktEltCollection.getAttributesFor(axisWktElts, ORDER_KEYWORD);
        this.setText(Integer.parseInt(attributes.get(0).getKeyword())); 
    }     

    /**
     * @return the text
     */
    public int getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(int text) {
        this.text = text;
    }

    @Override
    public StringBuffer toWkt(int deepLevel) {
        StringBuffer wkt = new StringBuffer();
        wkt = wkt.append(ORDER_KEYWORD).append(LEFT_DELIMITER);
        wkt = wkt.append(this.getText());
        wkt = wkt.append(RIGHT_DELIMITER);
        return wkt;
    }
}
