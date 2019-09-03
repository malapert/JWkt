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
import com.github.malapert.wkt.utils.WktElt;
import com.github.malapert.wkt.utils.WktEltCollection;
import com.github.malapert.wkt.metadata.Identifier;
import com.github.malapert.wkt.method.DerivedConversionMethod;
import com.github.malapert.wkt.method.MethodFactory;
import com.github.malapert.wkt.parameter.CoordinateOperationParameter;
import com.github.malapert.wkt.parameter.CoordinateOperationParameterFile;
import com.github.malapert.wkt.parameter.Operation;
import com.github.malapert.wkt.parameter.OperationFactory;
import java.util.List;

/**
 * Defines a conversion for derived CRS.
 * 
 * {@code 
 * <deriving conversion >::=<deriving conversion keyword> <left delimiter>
 * <deriving conversion name> <wkt separator> <operation method> {
 * <wkt separator> <operation parameter> | <operation parameter file> }… [ {
 * <wkt separator> <identifier> } ]… <right delimiter>
 *}
 * 
 * @author Jean-Christophe Malapert
 */
public class DerivedConversion extends AbstractConversion {

    public static final String DERIVED_CONVERSION_KEYWORD = "DERIVINGCONVERSION";

    public DerivedConversion(final WktElt derivedConversionWkt) {
        parse(derivedConversionWkt);
    }

    private void parse(final WktElt derivedConversionWkt) {
        final WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
        final List<WktElt> attributes = wktEltCollection.getAttributesFor(derivedConversionWkt, DERIVED_CONVERSION_KEYWORD);
        setName(Utils.removeQuotes(attributes.get(0).getKeyword()));

        final List<WktElt> nodes = wktEltCollection.getNodesFor(derivedConversionWkt, DERIVED_CONVERSION_KEYWORD);
        for (final WktElt node : nodes) {
            switch (node.getKeyword()) {
                case DerivedConversionMethod.DERIVED_CONVERSION_METHOD:
                    setMethod(MethodFactory.createFromWkt(node));
                    break;
                case CoordinateOperationParameterFile.OPERATION_PARAMETER_FILE:
                case CoordinateOperationParameter.OPERATION_PARAMETER_KEYWORD:
                    this.getParameters().add(OperationFactory.createFromWkt(node,OperationFactory.OperationType.DERIVED_CONVERSION));
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
    public StringBuffer toWkt(final String endLine, final String tab, int deepLevel) {
        StringBuffer wkt = new StringBuffer();
        wkt = wkt.append(DERIVED_CONVERSION_KEYWORD).append(LEFT_DELIMITER);
        wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(Utils.addQuotes(this.name));
        wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(this.getMethod().toWkt(endLine, tab, deepLevel + 1));
        for(final Operation parameter:getParameters()) {
            wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(parameter.toWkt(endLine, tab, deepLevel + 1));
        }       
        for (final Identifier id : this.getIdentifiers()) {
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
