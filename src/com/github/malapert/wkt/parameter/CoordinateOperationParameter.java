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
package com.github.malapert.wkt.parameter;

import com.github.malapert.wkt.utils.Singleton;
import com.github.malapert.wkt.utils.Utils;
import static com.github.malapert.wkt.metadata.WktDescription.RIGHT_DELIMITER;
import static com.github.malapert.wkt.metadata.WktDescription.WKT_SEPARATOR;
import com.github.malapert.wkt.utils.WktElt;
import com.github.malapert.wkt.utils.WktEltCollection;
import com.github.malapert.wkt.metadata.Identifier;
import com.github.malapert.wkt.metadata.UnitFactory;
import java.util.List;

/**
 * <operation parameter>::=<parameter keyword>  <left delimiter>
 * <parameter name> <wkt separator><parameter value> <wkt separator>
 * <parameter unit> [ { <wkt separator> <identifier> } ]…
 * <right delimiter>
 *
 * @author malapert
 */
public class CoordinateOperationParameter extends AbstractOperation {

    public static final String OPERATION_PARAMETER_KEYWORD = "PARAMETER";

    public CoordinateOperationParameter(final WktElt operationParameter) {
        parse(operationParameter);
    }

    private void parse(final WktElt operationParameter) {
        WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
        List<WktElt> attributes = wktEltCollection.getAttributesFor(operationParameter, OPERATION_PARAMETER_KEYWORD);
        setParameterName(attributes.get(0).getKeyword());
        setParameterValueOrFile(attributes.get(1).getKeyword());

        List<WktElt> nodes = wktEltCollection.getNodesFor(operationParameter, OPERATION_PARAMETER_KEYWORD);
        for (WktElt node : nodes) {
            switch (node.getKeyword()) {
                case Identifier.IDENTIFIER_KEYWORD:
                    this.getIdentifierList().add(new Identifier(node));
                    break;
                case UnitFactory.LengthUnit.LENGTH_KEYWORD:
                case UnitFactory.LengthUnit.LENGTH_UNIT_KEYWORD:
                case UnitFactory.AngleUnit.ANGLEUNIT_KEYWORD:
                case UnitFactory.AngleUnit.ANGLEUNIT_UNIT_KEYWORD:
                case UnitFactory.ScaleUnit.SCALEUNIT_KEYWORD:
                case UnitFactory.ScaleUnit.SCALEUNIT_UNIT_KEYWORD:
                case UnitFactory.TimeUnit.TIMEUNIT_KEYWORD:
                case UnitFactory.TimeUnit.TIMEUNIT_UNIT_KEYWORD:                    
                case UnitFactory.ParametricUnit.PARAMETRICUNIT_KEYWORD:
                case UnitFactory.ParametricUnit.PARAMETRICUNIT_UNIT_KEYWORD:                    
                    setParameterUnit(UnitFactory.createFromWkt(node));
                    break;
                default:
                    throw new RuntimeException();
            }
        }
    }

    @Override
    public StringBuffer toWkt(int deepLevel) {
        StringBuffer wkt = new StringBuffer();
        wkt = wkt.append(OPERATION_PARAMETER_KEYWORD).append(LEFT_DELIMITER);
        wkt = wkt.append("\n").append(Utils.makeSpaces(deepLevel + 1)).append(this.getParameterName());
        wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel + 1)).append(this.getParameterValueOrFile());
        wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel + 1)).append(this.parameterUnit.toWkt(deepLevel + 1));
        if (!identifierList.isEmpty()) {
            for (Identifier id : identifierList) {
                wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel + 1)).append(id.toWkt(deepLevel + 1));
            }
        }
        wkt = wkt.append("\n").append(Utils.makeSpaces(deepLevel)).append(RIGHT_DELIMITER);
        return wkt;
    }

}
