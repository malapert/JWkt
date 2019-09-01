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
package com.github.malapert.wkt.cs;

import com.github.malapert.wkt.utils.Singleton;
import com.github.malapert.wkt.utils.Utils;
import com.github.malapert.wkt.metadata.WktDescription;
import com.github.malapert.wkt.utils.WktElt;
import com.github.malapert.wkt.utils.WktEltCollection;
import com.github.malapert.wkt.metadata.Unit;
import com.github.malapert.wkt.metadata.UnitFactory;
import com.github.malapert.wkt.metadata.Identifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines axis for a coordinate system.
 * 
 * <pre>
 * {@code
 * <axis>::=<axis keyword> <left delimiter>  <axis nameAbbrev> <wkt separator> <axis direction> [ <wkt separator> <axis order> ] [ <wkt separator> <axis unit> ] [ { <wkt separator> <identifier> } ]…  <right delimiter>
 * }
 * </pre>
 *
 * @author Jean-Christophe Malapert
 */
public class Axis implements WktDescription {

    public final static String AXIS_KEYWORD = "AXIS";
    private String nameAbbrev;
    private String direction;
    private AxisOrder axisOrder;
    private Unit axisUnit;
    private List<Identifier> identifierList = new ArrayList<>();

    /**
     * Creates an axis based on required parameters.
     * @param nameAbbrev axis name abbreviation
     * @param direction direction name
     */
    public Axis(final String nameAbbrev, final String direction) {
        this.nameAbbrev = nameAbbrev;
        this.direction = direction;
    }
    
    /**
     * Creates an axis by parsing the AXIS WKT element.
     * @param geoDcrsElt 
     */
    public Axis(final WktElt geoDcrsElt) {
        parse(geoDcrsElt);
    }

    /**
     * Parses the AXIS WKT element.
     * @param axisWktElts the AXIS WKT element
     */
    private void parse(final WktElt axisWktElts) {
        final WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
        final List<WktElt> attributes = wktEltCollection.getAttributesFor(axisWktElts, AXIS_KEYWORD);
        this.setNameAbbrev(Utils.removeQuotes(attributes.get(0).getKeyword()));
        //TODO : check with the enumatated list.
        this.setDirection(Utils.removeQuotes(attributes.get(1).getKeyword()));

        final List<WktElt> nodes = wktEltCollection.getNodesFor(axisWktElts, AXIS_KEYWORD);
        for (final WktElt node : nodes) {
            switch (node.getKeyword()) {
                case Identifier.IDENTIFIER_KEYWORD:
                    this.getIdentifierList().add(new Identifier(node));
                    break;
                case AxisOrder.ORDER_KEYWORD:
                    this.setAxisOrder(new AxisOrder(node));
                    break;
                case UnitFactory.AngleUnit.ANGLEUNIT_KEYWORD:
                case UnitFactory.AngleUnit.ANGLEUNIT_UNIT_KEYWORD:
                case UnitFactory.LengthUnit.LENGTH_KEYWORD:
                case UnitFactory.LengthUnit.LENGTH_UNIT_KEYWORD:
                case UnitFactory.ScaleUnit.SCALEUNIT_KEYWORD:
                case UnitFactory.ScaleUnit.SCALEUNIT_UNIT_KEYWORD:
                case UnitFactory.ParametricUnit.PARAMETRICUNIT_KEYWORD:
                case UnitFactory.ParametricUnit.PARAMETRICUNIT_UNIT_KEYWORD:
                case UnitFactory.TimeUnit.TIMEUNIT_KEYWORD:
                case UnitFactory.TimeUnit.TIMEUNIT_UNIT_KEYWORD:
                    Unit unit = UnitFactory.createFromWkt(node);
                    this.setAxisUnit(unit);
                    break;
                default:
                    throw new RuntimeException();
            }
        }        
    }

    /**
     * @return the nameAbbrev
     */
    public String getNameAbbrev() {
        return nameAbbrev;
    }

    /**
     * @param nameAbbrev the nameAbbrev to set
     */
    public void setNameAbbrev(String nameAbbrev) {
        this.nameAbbrev = nameAbbrev;
    }

    /**
     * @return the direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * @return the axisOrder
     */
    public AxisOrder getAxisOrder() {
        return axisOrder;
    }

    /**
     * @param axisOrder the axisOrder to set
     */
    public void setAxisOrder(AxisOrder axisOrder) {
        this.axisOrder = axisOrder;
    }

    /**
     * @return the axisUnit
     */
    public Unit getAxisUnit() {
        return axisUnit;
    }

    /**
     * @param axisUnit the axisUnit to set
     */
    public void setAxisUnit(Unit axisUnit) {
        this.axisUnit = axisUnit;
    }

    /**
     * @return the identifierList
     */
    public List<Identifier> getIdentifierList() {
        return identifierList;
    }

    /**
     * @param identifierList the identifierList to set
     */
    public void setIdentifierList(List<Identifier> identifierList) {
        this.identifierList = identifierList;
    }

    @Override
    public StringBuffer toWkt(final String endLine, final String tab, int deepLevel) {
        StringBuffer wkt = new StringBuffer();
        wkt = wkt.append(AXIS_KEYWORD).append(LEFT_DELIMITER);
        wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel+1)).append(Utils.addQuotes(getNameAbbrev()));
        wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel+1)).append(Utils.addQuotes(getDirection()));
        if (getAxisOrder() != null) {
            wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel+1)).append(getAxisOrder().toWkt(endLine, tab, deepLevel+1));
        }
        if (getAxisUnit() != null) {
            wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel+1)).append(getAxisUnit().toWkt(endLine, tab, deepLevel+1));
        }
        for (Identifier id : getIdentifierList()) {
            wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel+1)).append(id.toWkt(endLine, tab, deepLevel+1));
        }
        wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel)).append(RIGHT_DELIMITER);
        return wkt;
    }

    @Override
    public StringBuffer toWkt() {
        return toWkt("\n", "   ", 0);
    }    
}
