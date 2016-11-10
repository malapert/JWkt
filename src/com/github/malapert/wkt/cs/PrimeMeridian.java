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
        WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();

        setPrimeMeridianKeyword(PrimeMeridianKeyword.valueOf(primeMeridianElts.getKeyword()));

        List<WktElt> attributes = wktEltCollection.getAttributesFor(primeMeridianElts, getPrimeMeridianKeyword().name());
        this.setMeridianName(attributes.get(0).getKeyword());
        this.setLongitude(Float.parseFloat(attributes.get(1).getKeyword()));

        List<WktElt> nodes = wktEltCollection.getNodesFor(primeMeridianElts, getPrimeMeridianKeyword().name());
        for (WktElt node : nodes) {
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
    public StringBuffer toWkt(int deepLevel) {
        StringBuffer wkt = new StringBuffer();
        wkt = wkt.append(getPrimeMeridianKeyword()).append(LEFT_DELIMITER);
        wkt = wkt.append("\n").append(Utils.makeSpaces(deepLevel+1)).append(getMeridianName());
        wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel+1)).append(getLongitude());
        if (getAngleUnit() != null) {
            wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel+1)).append(getAngleUnit());
        }
        if (!getIdentifierList().isEmpty()) {
            for (Identifier id : getIdentifierList()) {
                wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel+1)).append(id.toWkt(deepLevel+1));
            }
        }
        wkt = wkt.append("\n").append(Utils.makeSpaces(deepLevel)).append(RIGHT_DELIMITER);
        return wkt;
    }
}
