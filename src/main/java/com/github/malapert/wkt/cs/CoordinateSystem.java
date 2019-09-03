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
import com.github.malapert.wkt.metadata.Identifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Defines a coordinate system based on flavor (coordinate system type and
 * dimension) and the direction of axis.
 *
 * <pre>
 * {@code
 * <coordinate system>::=<cs keyword> <left delimiter> <cs type> <wkt separator> <dimension> [ { <wkt separator> <identifier> } ]…  <right delimiter> { <wkt separator> <axis> }… [ <wkt separator> <cs unit> ]
 * }
 * </pre>
 *
 * @author Jean-Christophe Malapert
 */
public class CoordinateSystem implements WktDescription {

    public final static String COORDINATE_SYSTEM_KEYWORD = "CS";
    public final static String AXIS_KEYWORD = "AXIS";

    /**
     * List of coordinate systems.
     */
    public enum CsType {

        affine(new HashMap<String, Integer[]>() {
            {
                put("EngineeringCrs", new Integer[]{2, 3});
                put("ImageCrs", new Integer[]{2});
            }
        }),
        Cartesian(new HashMap<String, Integer[]>() {
            {
                put("GeodeticCrs", new Integer[]{3});
                put("ProjectedCrs", new Integer[]{2});
                put("EngineeringCrs", new Integer[]{2, 3});
                put("ImageCrs", new Integer[]{2});
            }
        }),
        cylindrical(new HashMap<String, Integer[]>() {
            {
                put("EngineeringCrs", new Integer[]{3});
            }
        }),
        ellipsoidal(new HashMap<String, Integer[]>() {
            {
                put("GeodeticCrs", new Integer[]{2, 3});
            }
        }),
        linear(new HashMap<String, Integer[]>() {
            {
                put("EngineeringCrs", new Integer[]{1});
            }
        }),
        parametric(new HashMap<String, Integer[]>() {
            {
                put("ParametricCrs", new Integer[]{1});
            }
        }),
        polar(new HashMap<String, Integer[]>() {
            {
                put("EngineeringCrs", new Integer[]{2});
            }
        }),
        spherical(new HashMap<String, Integer[]>() {
            {
                put("GeodeticCrs", new Integer[]{3});
                put("EngineeringCrs", new Integer[]{3});
            }
        }),
        temporal(new HashMap<String, Integer[]>() {
            {
                put("TemporalCrs", new Integer[]{1});
            }
        }),
        vertical(new HashMap<String, Integer[]>() {
            {
                put("VerticalCrs", new Integer[]{1});
            }
        });

        private Map<String, Integer[]> crs = new HashMap<>();

        CsType(final Map<String, Integer[]> crs) {
            this.crs = crs;
        }

        public Map<String, Integer[]> getCrs() {
            return this.crs;
        }

        public Integer[] getDimensionForCrs(final String searchedCrsName) {
            Integer[] result = null;
            for (final Entry<String, Integer[]> crsEntry : this.crs.entrySet()) {
                final String crsName = crsEntry.getKey();
                if (crsName.equals(searchedCrsName)) {
                    result = crsEntry.getValue();
                    break;
                }                
            }
            return result;
        }

        public List<CsType> getCsTypeFromCrs(String searchedCrsName) {
            List<CsType> csTypeList = new ArrayList<>();
            CsType[] csArray = CsType.values();
            for (CsType csType : csArray) {
                Map<String, Integer[]> crsMap = csType.getCrs();
                Set<String> crsKeys = crsMap.keySet();
                for (String crsKey : crsKeys) {
                    if (crsKey.equals(searchedCrsName)) {
                        csTypeList.add(csType);
                    }
                }
            }
            return csTypeList;
        }
    }

    private CsType csType;
    private int dimension;
    private List<Identifier> identifierList = new ArrayList<>();
    private List<Axis> axisList = new ArrayList<>();
    private Unit unit;

    /**
     * Creates a coordinate system based on the type of coordinate system and
     * its dimension.
     *
     * @param csType type of the coordinate system
     * @param dimension dimension of the coordinate system
     */
    public CoordinateSystem(final CsType csType, int dimension) {
        this.csType = csType;
        this.dimension = dimension;
    }

    /**
     * Creates a coordinate system by parsing on CS WKT element.
     *
     * @param csWktElts CS WKT element
     */
    public CoordinateSystem(final WktElt csWktElts) {
        parse(csWktElts);
    }

    /**
     * Returns the type of the coordinate system.
     *
     * @return the type of the coordinate system
     */
    public CsType getCsType() {
        return csType;
    }

    /**
     * Sets the type of the coordinate system.
     *
     * @param csType the csType to set
     */
    public void setCsType(final CsType csType) {
        this.csType = csType;
    }

    /**
     * Returns the dimension.
     *
     * @return the dimension
     */
    public int getDimension() {
        return dimension;
    }

    /**
     * Sets the dimension.
     *
     * @param dimension the dimension to set
     */
    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    /**
     * Returns the list of Identifier object.
     *
     * @return the identifierList
     */
    public List<Identifier> getIdentifierList() {
        return identifierList;
    }

    /**
     * Sets the list of Identifier object.
     *
     * @param identifierList the identifierList to set
     */
    public void setIdentifierList(final List<Identifier> identifierList) {
        this.identifierList = identifierList;
    }

    /**
     * Returns the list of axis object.
     *
     * @return the axisList
     */
    public List<Axis> getAxisList() {
        return axisList;
    }

    /**
     * Sets the list of axis.
     *
     * @param axisList the axisList to set
     */
    public void setAxisList(final List<Axis> axisList) {
        this.axisList = axisList;
    }

    /**
     * Returns the unit.
     *
     * @return the unit
     */
    public Unit getUnit() {
        return unit;
    }

    /**
     * Sets the unit.
     *
     * @param unit the unit to set
     */
    public void setUnit(final Unit unit) {
        this.unit = unit;
    }

    /**
     * Parses the CS WKT element.
     *
     * @param csWktElts the CS WKT element
     */
    private void parse(final WktElt csWktElts) {
        final WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
        final List<WktElt> attributes = wktEltCollection.getAttributesFor(csWktElts, COORDINATE_SYSTEM_KEYWORD);
        this.csType = CsType.valueOf(attributes.get(0).getKeyword());
        this.dimension = Integer.parseInt(attributes.get(1).getKeyword());

        final List<WktElt> nodes = wktEltCollection.getNodesFor(csWktElts, COORDINATE_SYSTEM_KEYWORD);
        if (nodes.isEmpty()) {
            return;
        }
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
        wkt = wkt.append(COORDINATE_SYSTEM_KEYWORD).append(LEFT_DELIMITER);
        wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(getCsType());
        wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(getDimension());
        for (Identifier id : getIdentifierList()) {
            wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(id.toWkt(endLine, tab, deepLevel + 1));
        }
        wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel)).append(RIGHT_DELIMITER);
        for (Axis axis : getAxisList()) {
            wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel)).append(axis.toWkt(endLine, tab, deepLevel));
        }        
        if (getUnit() != null) {
            wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel)).append(getUnit().toWkt(endLine, tab, deepLevel));
        }
        return wkt;
    }
    
    @Override
    public StringBuffer toWkt() {
        return toWkt("\n", "   ", 0);
    }    

}
