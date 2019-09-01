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
package com.github.malapert.wkt.parameter;

import com.github.malapert.wkt.utils.Singleton;
import com.github.malapert.wkt.utils.Utils;
import static com.github.malapert.wkt.metadata.WktDescription.LEFT_DELIMITER;
import static com.github.malapert.wkt.metadata.WktDescription.RIGHT_DELIMITER;
import static com.github.malapert.wkt.metadata.WktDescription.WKT_SEPARATOR;
import com.github.malapert.wkt.utils.WktElt;
import com.github.malapert.wkt.utils.WktEltCollection;
import com.github.malapert.wkt.metadata.Identifier;
import com.github.malapert.wkt.metadata.UnitFactory;
import java.util.List;

/**
 * <parameter keyword>  <left delimiter> <parameter name> <wkt separator>
 * <parameter value> [ <wkt separator> <map projection parameter unit> ] [ {
 * <wkt separator> <identifier> } ]… <right delimiter>
 *
 * @author malapert
 */
public class MapProjectionParameter extends AbstractOperation {

    public static final String MAP_PROJECTION_PARAMETER = "PARAMETER";
    
    public MapProjectionParameter(final String name, final String value) {
        setParameterName(name);
        setParameterValueOrFile(value);
    }

    public MapProjectionParameter(final WktElt mapProjectionParameterElts) {
        parse(mapProjectionParameterElts);
    }

    private void parse(final WktElt mapProjectionParameterElts) {
        final WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
        final List<WktElt> attributes = wktEltCollection.getAttributesFor(mapProjectionParameterElts, MAP_PROJECTION_PARAMETER);
        setParameterName(Utils.removeQuotes(attributes.get(0).getKeyword()));
        setParameterValueOrFile(attributes.get(1).getKeyword());
        List<WktElt> nodes = wktEltCollection.getNodesFor(mapProjectionParameterElts, MAP_PROJECTION_PARAMETER);
        for (WktElt node : nodes) {
            switch (node.getKeyword()) {
                case UnitFactory.AngleUnit.ANGLEUNIT_KEYWORD:
                case UnitFactory.AngleUnit.ANGLEUNIT_UNIT_KEYWORD:
                case UnitFactory.LengthUnit.LENGTH_KEYWORD:
                case UnitFactory.LengthUnit.LENGTH_UNIT_KEYWORD:
                case UnitFactory.ScaleUnit.SCALEUNIT_KEYWORD:
                case UnitFactory.ScaleUnit.SCALEUNIT_UNIT_KEYWORD:
                    setParameterUnit(UnitFactory.createFromWkt(node));                   
                    break;
                case Identifier.IDENTIFIER_KEYWORD:
                    this.getIdentifierList().add(new Identifier(node));
                    break;
                default:
                    throw new RuntimeException();
            }
        }
    }

    @Override
    public StringBuffer toWkt(final String endLine, final String tab, int deepLevel) {
        StringBuffer wkt = new StringBuffer();
        wkt = wkt.append(MAP_PROJECTION_PARAMETER).append(LEFT_DELIMITER);
        wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(Utils.addQuotes(this.parameterName));
        wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(this.getParameterValueOrFile());
        if (getParameterUnit() != null) {
            wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(this.getParameterUnit().toWkt(endLine, tab, deepLevel + 1));
        }
        for (Identifier id : identifierList) {
            wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(id.toWkt(endLine, tab, deepLevel + 1));
        }
        wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel)).append(RIGHT_DELIMITER);
        return wkt;
    }
    
    @Override
    public StringBuffer toWkt() {
        return toWkt("\n", "   ", 0);
    }    

}
