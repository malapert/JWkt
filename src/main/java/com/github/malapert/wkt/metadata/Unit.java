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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Some attributes of coordinate reference systems and coordinate operations are
 * numbers which require the unit to be specified.
 *
 * <p>
 * <
 * pre>
 * {@code
 * <unit> ::= <angle unit> | <length unit> | <scale unit> | <parametric unit> | <time unit>
 * }
 * </pre> each type of unit has the same syntax:<br>
 * <pre>
 * {@code
 * <unit type keyword> <left delimiter> <unit name> <wkt separator> <conversion factor> [ { <wkt separator> <identifier> } ]…  <right delimiter>
 * }
 * </pre>
 *
 * @author Jean-Christophe Malapert
 */
public abstract class Unit implements WktDescription {

    private String unitName;
    private BigDecimal conversionFactor;
    private List<Identifier> identifierList = new ArrayList<>();

    protected Unit(final String name, float conversionFactor) {
        setUnitName(name);
        setConversionFactor(conversionFactor);
    }

    protected Unit(final WktElt unitWkt) {
        parse(unitWkt);
    }

    /**
     * Sets the Unit WKT element in order to parse it.
     *
     * @param unitWkt the Unit WKT element
     */
    public final void parse(WktElt unitWkt) {
        WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();

        List<WktElt> attributes = wktEltCollection.getAttributesFor(unitWkt, getUnitKeyword());
        this.setUnitName(Utils.removeQuotes(attributes.get(0).getKeyword()));
        if (attributes.size() > 1) {
            this.conversionFactor = new BigDecimal(attributes.get(1).getKeyword());

            List<WktElt> nodes = wktEltCollection.getNodesFor(unitWkt, getUnitKeyword());
            for (WktElt node : nodes) {
                switch (node.getKeyword()) {
                    case Identifier.IDENTIFIER_KEYWORD:
                        this.getIdentifierList().add(new Identifier(unitWkt));
                        break;
                    default:
                        throw new RuntimeException();
                }
            }
        }
    }

    /**
     * Returns the keyword of the Unit node.
     *
     * @return the keyword of the Unit node
     */
    public abstract String getUnitKeyword();

    /**
     * Converts to WKT.
     *
     * @return Returns the conversion into WKT
     */
    @Override
    public final StringBuffer toWkt(final String endLine, final String tab, int deepLevel) {
        StringBuffer wkt = new StringBuffer();
        wkt = wkt.append(this.getUnitKeyword()).append(LEFT_DELIMITER);
        wkt = wkt.append(Utils.addQuotes(this.getUnitName()));
        if(this.conversionFactor != null) {
            wkt = wkt.append(WKT_SEPARATOR).append(this.conversionFactor);
        }
        for (final Identifier id : this.getIdentifierList()) {
            wkt = wkt.append(WKT_SEPARATOR).append(id.toWkt(endLine, tab, deepLevel + 1));
        }
        wkt = wkt.append(RIGHT_DELIMITER);
        return wkt;
    }

    @Override
    public StringBuffer toWkt() {
        return toWkt("\n", "   ", 0);
    }

    /**
     * Returns the unit name.
     *
     * @return the unitName
     */
    public String getUnitName() {
        return unitName;
    }

    /**
     * Sets the unit name.
     *
     * @param unitName the unitName to set
     */
    public final void setUnitName(final String unitName) {
        this.unitName = unitName;
    }

    /**
     * Returns the conversion factor.
     *
     * @return the conversionFactor
     */
    public float getConversionFactor() {
        return conversionFactor.floatValue();
    }

    /**
     * Sets the conversion factor.
     *
     * @param conversionFactor the conversionFactor to set
     */
    public final void setConversionFactor(float conversionFactor) {
        this.conversionFactor = new BigDecimal(conversionFactor);
    }

    /**
     * Returns the list of Identifier WKT elements.
     *
     * @return the identifierList
     */
    public List<Identifier> getIdentifierList() {
        return identifierList;
    }

    /**
     * Sets the list of Identifier WKT elements.
     *
     * @param identifierList the identifierList to set
     */
    public void setIdentifierList(final List<Identifier> identifierList) {
        this.identifierList = identifierList;
    }
}
