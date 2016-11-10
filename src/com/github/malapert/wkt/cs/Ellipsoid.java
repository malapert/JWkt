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
import com.github.malapert.wkt.metadata.Identifier;
import com.github.malapert.wkt.metadata.UnitFactory.LengthUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * An ellipsoid is defined through semi-major axis (a) and either semi-minor 
 * axis (b) or inverse flattening (1/f). 
 * 
 * If semi-minor axis is used as the second defining parameter the value for 
 * inverse flattening to be shown in the WKT string should be calculated 
 * from 1/f  =  a / (a – b).
 * 
 * <p>
 * <pre>
 * {@code
 * <ellipsoid>::=<ellipsoid keyword> <left delimiter> <ellipsoid name> <wkt separator> <semi-major axis> <wkt separator> <inverse flattening> [ <wkt separator> <length unit> ]  [ { <wkt separator> <identifier> } ]…  <right delimiter>  
 * }
 * </pre>
 *
 * @author Jean-Christophe Malapert
 */
public class Ellipsoid implements WktDescription {

    private EllipsoidKeyword ellipsoid;
    private String ellipsoidName;
    private float semiMajorAxis;
    private float inverFlattening;
    private LengthUnit lengthUnit;
    private List<Identifier> identifierList = new ArrayList<>();

    /**
     * List of ellipsoid keywords.
     */
    public enum EllipsoidKeyword {
        ELLIPSOID,
        SPHEROID
    }
    
    /**
     * Creates an ellipsoid based on required parameters.
     * @param ellipsoid ellipsoid type
     * @param ellipsoidName ellipsoid name
     * @param semiMajorAxis semi major axis
     * @param inverFlattening invert flattening
     */
    public Ellipsoid(final EllipsoidKeyword ellipsoid, final String ellipsoidName,
                     float semiMajorAxis, float inverFlattening) {
        this.ellipsoid = ellipsoid;
        this.ellipsoidName = ellipsoidName;
        this.semiMajorAxis = semiMajorAxis;
        this.inverFlattening = inverFlattening;
    }

    /**
     * Creates an ellipsoid by parsing on a ELLIPSOID WKT element.
     * @param ellipsoidElts 
     */
    public Ellipsoid(final WktElt ellipsoidElts) {
        parse(ellipsoidElts);
    }

    /**
     * Parses a ELLIPSOID WKT element.
     * @param ellipsoidElts ELLIPSOID WKT element
     */
    private void parse(final WktElt ellipsoidElts) {
        WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();

        this.ellipsoid = EllipsoidKeyword.valueOf(ellipsoidElts.getKeyword());

        List<WktElt> attributes = wktEltCollection.getAttributesFor(ellipsoidElts, this.ellipsoid.name());
        this.setEllipsoidName(attributes.get(0).getKeyword());
        this.setSemiMajorAxis(Float.parseFloat(attributes.get(1).getKeyword()));
        this.setInverFlattening(Float.parseFloat(attributes.get(2).getKeyword()));

        List<WktElt> nodes = wktEltCollection.getNodesFor(ellipsoidElts, ellipsoid.name());
        for (WktElt node : nodes) {
            switch (node.getKeyword()) {
                case LengthUnit.LENGTH_KEYWORD:
                case LengthUnit.LENGTH_UNIT_KEYWORD:
                    LengthUnit unit = new LengthUnit(node);
                    this.setLengthUnit(unit);
                    break;
                case Identifier.IDENTIFIER_KEYWORD:
                    this.getIdentifierList().add(new Identifier(node));
                    break;
                default:
                    throw new RuntimeException();
            }
        }       
    }

    /**
     * Returns the ellipsoid keyword.
     * @return the ellipsoid
     */
    public EllipsoidKeyword getEllipsoid() {
        return ellipsoid;
    }

    /**
     * Sets the ellipsoid keyword.
     * @param ellipsoid the ellipsoid to set
     */
    public void setEllipsoid(final EllipsoidKeyword ellipsoid) {
        this.ellipsoid = ellipsoid;
    }

    /**
     * Returns the ellipsoid name.
     * @return the ellipsoidName
     */
    public String getEllipsoidName() {
        return ellipsoidName;
    }

    /**
     * Sets the ellipsoid name.
     * @param ellipsoidName the ellipsoidName to set
     */
    public void setEllipsoidName(final String ellipsoidName) {
        this.ellipsoidName = ellipsoidName;
    }

    /**
     * Returns the semi-major axis.
     * @return the semiMajorAxis
     */
    public float getSemiMajorAxis() {
        return semiMajorAxis;
    }

    /**
     * Sets the semi-major axis.
     * @param semiMajorAxis the semiMajorAxis to set
     */
    public void setSemiMajorAxis(float semiMajorAxis) {
        this.semiMajorAxis = semiMajorAxis;
    }

    /**
     * Returns the inverse flattening.
     * @return the inverFlattening
     */
    public float getInverFlattening() {
        return inverFlattening;
    }

    /**
     * Sets the inverse flattening.
     * @param inverFlattening the inverFlattening to set
     */
    public void setInverFlattening(float inverFlattening) {
        this.inverFlattening = inverFlattening;
    }

    /**
     * Returns the length unit.
     * @return the lengthUnit
     */
    public LengthUnit getLengthUnit() {
        return lengthUnit;
    }

    /**
     * Sets the length unit.
     * @param lengthUnit the lengthUnit to set
     */
    public void setLengthUnit(final LengthUnit lengthUnit) {
        this.lengthUnit = lengthUnit;
    }

    /**
     * Returns the list of Identifier.
     * @return the identifierList
     */
    public List<Identifier> getIdentifierList() {
        return identifierList;
    }

    /**
     * Sets the list of Identifier.
     * @param identifierList the identifierList to set
     */
    public void setIdentifierList(final List<Identifier> identifierList) {
        this.identifierList = identifierList;
    }

    @Override
    public StringBuffer toWkt(int deepLevel) {
        StringBuffer wkt = new StringBuffer();
        wkt = wkt.append(getEllipsoid()).append(LEFT_DELIMITER);
        wkt = wkt.append("\n").append(Utils.makeSpaces(deepLevel+1)).append(getEllipsoidName());
        wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel+1)).append(getSemiMajorAxis());
        wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel+1)).append(getInverFlattening());
        if (this.getLengthUnit() != null) {
            wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel+1)).append(getLengthUnit().toWkt(deepLevel+1));
        }
        if (!this.getIdentifierList().isEmpty()) {
            for (Identifier id : getIdentifierList()) {
                wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel+1)).append(id.toWkt(deepLevel+1));
            }
        }
        wkt = wkt.append("\n").append(Utils.makeSpaces(deepLevel)).append(RIGHT_DELIMITER);
        return wkt;
    }
}
