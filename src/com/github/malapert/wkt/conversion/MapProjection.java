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
        WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
        List<WktElt> attributes = wktEltCollection.getAttributesFor(mapProjectionwktElts, MAP_PROJECTION_KEYWORD);
        setName(attributes.get(0).getKeyword());

        List<WktElt> nodes = wktEltCollection.getNodesFor(mapProjectionwktElts, MAP_PROJECTION_KEYWORD);
        for (WktElt node : nodes) {
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
    public StringBuffer toWkt(int deepLevel) {
        StringBuffer wkt = new StringBuffer();
        wkt = wkt.append(MAP_PROJECTION_KEYWORD).append(LEFT_DELIMITER);
        wkt = wkt.append("\n").append(Utils.makeSpaces(deepLevel + 1)).append(this.name);
        wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel + 1)).append(this.method.toWkt(deepLevel + 1));
        if (!parameters.isEmpty()) {
            for (Operation param : parameters) {
                wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel + 1)).append(param.toWkt(deepLevel + 1));
            }
        }
        if (!identifiers.isEmpty()) {
            for (Identifier id : identifiers) {
                wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel + 1)).append(id.toWkt(deepLevel + 1));
            }
        }
        wkt = wkt.append("\n").append(Utils.makeSpaces(deepLevel)).append(RIGHT_DELIMITER);
        return wkt;
    }
}
