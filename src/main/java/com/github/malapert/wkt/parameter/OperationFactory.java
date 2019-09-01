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

import com.github.malapert.wkt.metadata.WktDescription;
import com.github.malapert.wkt.utils.WktElt;

/**
 *
 * @author malapert
 */
public abstract class OperationFactory implements WktDescription {

    public enum OperationType {
        MAP_PROJECTION,
        DERIVED_CONVERSION
    }

    public static Operation createFromWkt(WktElt node, OperationType type) {
        Operation result = null;
        if (null != node.getKeyword() && null != type) {
            switch (type) {
                case MAP_PROJECTION:
                    result = new MapProjectionParameter(node);
                    break;
                case DERIVED_CONVERSION:
                    switch (node.getKeyword()) {
                        case CoordinateOperationParameter.OPERATION_PARAMETER_KEYWORD:
                            result = new CoordinateOperationParameter(node);
                            break;
                        case CoordinateOperationParameterFile.OPERATION_PARAMETER_FILE:
                            result = new CoordinateOperationParameterFile(node);
                            break;
                        default:
                            throw new RuntimeException();
                    }
                    break;
                default:
                    throw new RuntimeException();

            }
        }
        return result;
    }

}
