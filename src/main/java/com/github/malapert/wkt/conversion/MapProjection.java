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
package com.github.malapert.wkt.conversion;

import com.github.malapert.wkt.utils.Singleton;
import com.github.malapert.wkt.utils.Utils;
import static com.github.malapert.wkt.metadata.WktDescription.LEFT_DELIMITER;
import static com.github.malapert.wkt.metadata.WktDescription.RIGHT_DELIMITER;
import static com.github.malapert.wkt.metadata.WktDescription.WKT_SEPARATOR;
import com.github.malapert.wkt.utils.WktElt;
import com.github.malapert.wkt.utils.WktEltCollection;
import com.github.malapert.wkt.metadata.Identifier;
import com.github.malapert.wkt.method.MethodFactory;
import static com.github.malapert.wkt.parameter.MapProjectionParameter.MAP_PROJECTION_PARAMETER;
import com.github.malapert.wkt.parameter.Operation;
import com.github.malapert.wkt.parameter.OperationFactory;
import java.util.List;

/**
 * Defines the conversion for a projected CRS.
 * 
 * {@code
 * <map projection keyword> <left delimiter> <map projection name>
 * <wkt separator>
 * <map projection method> { <wkt separator> <map projection parameter> }… [ {
 * <wkt separator> <identifier> } ]… <right delimiter>
 * }
 *
 * @author Jean-Christophe Malapert
 */
public class MapProjection extends AbstractConversion {

    public static final String MAP_PROJECTION_KEYWORD = "CONVERSION";

    public MapProjection(final WktElt mapProjectionwktElts) {
        parse(mapProjectionwktElts);
    }

    private void parse(final WktElt mapProjectionwktElts) {
        final WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
        final List<WktElt> attributes = wktEltCollection.getAttributesFor(mapProjectionwktElts, MAP_PROJECTION_KEYWORD);
        setName(attributes.get(0).getKeyword());

        final List<WktElt> nodes = wktEltCollection.getNodesFor(mapProjectionwktElts, MAP_PROJECTION_KEYWORD);
        for (final WktElt node : nodes) {
            switch (node.getKeyword()) {
                case "METHOD":
                case "PROJECTION":
                    setMethod(MethodFactory.createFromWkt(node));
                    break;
                case MAP_PROJECTION_PARAMETER:
                    this.getParameters().add(OperationFactory.createFromWkt(node, OperationFactory.OperationType.MAP_PROJECTION));
                    break;
                case Identifier.IDENTIFIER_KEYWORD:
                    this.getIdentifiers().add(new Identifier(node));
                    break;
                default:
                    throw new RuntimeException();
            }
        }
    }

    @Override
    public StringBuffer toWkt(final String endLine, final String tab, final int deepLevel) {
        StringBuffer wkt = new StringBuffer();
        wkt = wkt.append(MAP_PROJECTION_KEYWORD).append(LEFT_DELIMITER);
        wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(this.name);
        wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(this.method.toWkt(endLine, tab, deepLevel + 1));
        if (!parameters.isEmpty()) {
            for (final Operation param : parameters) {
                wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(param.toWkt(endLine, tab, deepLevel + 1));
            }
        }
        if (!identifiers.isEmpty()) {
            for (final Identifier id : identifiers) {
                wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(id.toWkt(endLine, tab, deepLevel + 1));
            }
        }
        wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel)).append(RIGHT_DELIMITER);
        return wkt;
    }
    
    @Override
    public StringBuffer toWkt() {
        return toWkt("\n", "   ", 0);
    }    
}
