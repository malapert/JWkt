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
package com.github.malapert.wkt.method;

import com.github.malapert.wkt.utils.Singleton;
import com.github.malapert.wkt.utils.Utils;
import static com.github.malapert.wkt.metadata.WktDescription.LEFT_DELIMITER;
import static com.github.malapert.wkt.metadata.WktDescription.RIGHT_DELIMITER;
import static com.github.malapert.wkt.metadata.WktDescription.WKT_SEPARATOR;
import com.github.malapert.wkt.utils.WktElt;
import com.github.malapert.wkt.utils.WktEltCollection;
import com.github.malapert.wkt.metadata.Identifier;
import static com.github.malapert.wkt.method.DerivedConversionMethod.DERIVED_CONVERSION_METHOD;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author malapert
 */
public abstract class AbstractMethod implements Method {
    
    public String keyword;
    public String methodName;
    public List<Identifier> identifierList = new ArrayList<>();
    
    protected final void parse(final WktElt conversionMethodWkt) {
        this.setKeyword(conversionMethodWkt.getKeyword());
        final WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
        final List<WktElt> attributes = wktEltCollection.getAttributesFor(conversionMethodWkt, DERIVED_CONVERSION_METHOD);
        this.setMethodName(Utils.removeQuotes(attributes.get(0).getKeyword()));
        final List<WktElt> nodes = wktEltCollection.getNodesFor(conversionMethodWkt, DERIVED_CONVERSION_METHOD);
        for (final WktElt node : nodes) {
            switch (node.getKeyword()) {
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
        wkt = wkt.append(DERIVED_CONVERSION_METHOD).append(LEFT_DELIMITER);
        wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(Utils.addQuotes(this.getMethodName()));
        for (Identifier id : getIdentifierList()) {
            wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(id.toWkt(endLine, tab, deepLevel + 1));
        }        
        wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel)).append(RIGHT_DELIMITER);
        return wkt;
    }
    
    @Override
    public StringBuffer toWkt() {
        return toWkt("\n", "   ", 0);
    }    

    /**
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * @param keyword the keyword to set
     */
    public final void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * @return the methodName
     */
    @Override
    public String getMethodName() {
        return methodName;
    }

    /**
     * @param methodName the methodName to set
     */
    public final void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * @return the identifierList
     */
    @Override
    public List<Identifier> getIdentifierList() {
        return identifierList;
    }

    /**
     * @param identifierList the identifierList to set
     */
    public void setIdentifierList(List<Identifier> identifierList) {
        this.identifierList = identifierList;
    }
        
}
