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
import com.github.malapert.wkt.metadata.UnitFactory.AngleUnit;
import com.github.malapert.wkt.metadata.Identifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <prime meridian keyword> <left delimiter> <prime meridian name>
 * <wkt separator> <irm longitude> [ <wkt separator> <angle unit> ] [ {
 * <wkt separator> <identifier> } ]…  <right delimiter>
 *
 * @author malapert
 */
public class PrimeMeridian implements WktDescription {

    private PrimeMeridianKeyword primeMeridianKeyword;
    private String meridianName;
    private float longitude;
    private Unit angleUnit;
    private List<Identifier> identifierList = new ArrayList<>();

    public static enum PrimeMeridianKeyword {
        PRIMEM,
        PRIMEMERIDIAN;
        private PrimeMeridianKeyword() {        
        }
        
        public static List<String> getKeywords() {
            return Arrays.asList(PrimeMeridianKeyword.PRIMEM.name(), PrimeMeridianKeyword.PRIMEMERIDIAN.name());
        }          
    }
    
    public PrimeMeridian(final PrimeMeridianKeyword primeMeridianKeyword,
                         final String meridianName,
                         float longitude) {
        this.primeMeridianKeyword = primeMeridianKeyword;
        this.meridianName = meridianName;
        this.longitude = longitude;
    }
                         

    public PrimeMeridian(WktElt datumElt) {
        parse(datumElt);
    }

    private void parse(final WktElt primeMeridianElts) {
        final WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();

        setPrimeMeridianKeyword(PrimeMeridianKeyword.valueOf(primeMeridianElts.getKeyword()));

        final List<WktElt> attributes = wktEltCollection.getAttributesFor(primeMeridianElts, getPrimeMeridianKeyword().name());
        this.setMeridianName(Utils.removeQuotes(attributes.get(0).getKeyword()));
        this.setLongitude(Float.parseFloat(attributes.get(1).getKeyword()));

        final List<WktElt> nodes = wktEltCollection.getNodesFor(primeMeridianElts, getPrimeMeridianKeyword().name());
        for (final WktElt node : nodes) {
            switch (node.getKeyword()) {
                case Identifier.IDENTIFIER_KEYWORD:
                    getIdentifierList().add(new Identifier(node));
                    break;
                case AngleUnit.ANGLEUNIT_KEYWORD:
                case AngleUnit.ANGLEUNIT_UNIT_KEYWORD:
                    Unit unit = UnitFactory.createFromWkt(node);
                    this.setAngleUnit(unit);
                    break;
                default:
                    throw new RuntimeException();
            }
        }
    }

    /**
     * @return the primeMeridianKeyword
     */
    public PrimeMeridianKeyword getPrimeMeridianKeyword() {
        return primeMeridianKeyword;
    }

    /**
     * @param primeMeridianKeyword the primeMeridianKeyword to set
     */
    public void setPrimeMeridianKeyword(PrimeMeridianKeyword primeMeridianKeyword) {
        this.primeMeridianKeyword = primeMeridianKeyword;
    }

    /**
     * @return the meridianName
     */
    public String getMeridianName() {
        return meridianName;
    }

    /**
     * @param meridianName the meridianName to set
     */
    public void setMeridianName(String meridianName) {
        this.meridianName = meridianName;
    }

    /**
     * @return the longitude
     */
    public float getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the angleUnit
     */
    public Unit getAngleUnit() {
        return angleUnit;
    }

    /**
     * @param angleUnit the angleUnit to set
     */
    public void setAngleUnit(Unit angleUnit) {
        this.angleUnit = angleUnit;
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
        wkt = wkt.append(getPrimeMeridianKeyword()).append(LEFT_DELIMITER);
        wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel+1)).append(Utils.removeQuotes(getMeridianName()));
        wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel+1)).append(getLongitude());
        if (getAngleUnit() != null) {
            wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel+1)).append(getAngleUnit());
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
