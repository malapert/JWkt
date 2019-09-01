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
package com.github.malapert.wkt.crs;

import com.github.malapert.wkt.datum.Datum;
import com.github.malapert.wkt.utils.Singleton;
import com.github.malapert.wkt.utils.Utils;
import static com.github.malapert.wkt.metadata.WktDescription.LEFT_DELIMITER;
import static com.github.malapert.wkt.metadata.WktDescription.RIGHT_DELIMITER;
import static com.github.malapert.wkt.metadata.WktDescription.WKT_SEPARATOR;
import com.github.malapert.wkt.utils.WktElt;
import com.github.malapert.wkt.utils.WktEltCollection;
import com.github.malapert.wkt.metadata.Unit;
import com.github.malapert.wkt.metadata.UnitFactory;
import com.github.malapert.wkt.datum.EngineeringDatum;
import com.github.malapert.wkt.datum.GeodeticDatum;
import com.github.malapert.wkt.conversion.MapProjection;
import java.util.List;

/**
 *
 * @author malapert
 */
public abstract class BaseDerivatedCrsFactory {

    public static BaseCrs createFromWkt(final WktElt datumWkt) {
        final BaseCrs crs;
        switch (datumWkt.getKeyword()) {
            case BaseGeodeticCrs.BASE_GEODETIC_CRS_KEYWORD:
                crs = new BaseGeodeticCrs(datumWkt);
                break;
            case BaseEngineeringCrs.BASE_ENGINEERING_CRS_KEYWORD:
                crs = new BaseEngineeringCrs(datumWkt);
                break;
            case BaseProjectedCrs.BASE_PROJECTED_CRS:
                crs = new BaseProjectedCrs(datumWkt);
                break;
            default:
                throw new RuntimeException();

        }
        return crs;
    }

    /**
     * <base projected crs keyword> <left delimiter> <base crs name>
     * <wkt separator> <base geodetic crs> <wkt separator> <map projection>
     * <right delimiter> @author malapert
     */
    public static class BaseProjectedCrs implements BaseCrs {

        public static final String BASE_PROJECTED_CRS = "BASEPROJCRS";

        private String baseCrsName;
        private BaseGeodeticCrs baseGeodetic;
        private MapProjection mapProjection;

        public BaseProjectedCrs(WktElt datumWkt) {
            parse(datumWkt);
        }

        private void parse(WktElt datumWkt) {
            final WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
            final List<WktElt> attributes = wktEltCollection.getAttributesFor(datumWkt, BASE_PROJECTED_CRS);
            this.setBaseCrsName(attributes.get(0).getKeyword());
            final List<WktElt> nodes = wktEltCollection.getNodesFor(datumWkt, BASE_PROJECTED_CRS);
            for (final WktElt node : nodes) {
                switch (node.getKeyword()) {
                    case BaseGeodeticCrs.BASE_GEODETIC_CRS_KEYWORD:
                        this.setBaseGeodetic(new BaseGeodeticCrs(node));
                        break;
                    case MapProjection.MAP_PROJECTION_KEYWORD:
                        this.setMapProjection(new MapProjection(node));
                        break;
                    default:
                        throw new RuntimeException();
                }
            }
        }

        /**
         * @return the crsName
         */
        @Override
        public String getBaseCrsName() {
            return baseCrsName;
        }

        /**
         * @param baseCrsName the crsName to set
         */
        public void setBaseCrsName(String baseCrsName) {
            this.baseCrsName = baseCrsName;
        }

        /**
         * @return the baseGeodetic
         */
        public BaseGeodeticCrs getBaseGeodetic() {
            return baseGeodetic;
        }

        /**
         * @param baseGeodetic the baseGeodetic to set
         */
        public void setBaseGeodetic(BaseGeodeticCrs baseGeodetic) {
            this.baseGeodetic = baseGeodetic;
        }

        /**
         * @return the mapProjection
         */
        public MapProjection getMapProjection() {
            return mapProjection;
        }

        /**
         * @param mapProjection the mapProjection to set
         */
        public void setMapProjection(MapProjection mapProjection) {
            this.mapProjection = mapProjection;
        }

        @Override
        public StringBuffer toWkt(final String endLine, final String tab, int deepLevel) {
            StringBuffer wkt = new StringBuffer();
            wkt = wkt.append(BASE_PROJECTED_CRS).append(LEFT_DELIMITER);
            wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(this.getBaseCrsName());
            wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(this.getBaseGeodetic().toWkt(endLine, tab, deepLevel + 1));
            wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(this.getMapProjection().toWkt(endLine, tab, deepLevel + 1));
            wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel)).append(RIGHT_DELIMITER);
            return wkt;
        }

        @Override
        public Datum getDatum() {
            return getBaseGeodetic().getDatum();
        }

        @Override
        public StringBuffer toWkt() {
            return toWkt("\n", "   ", 0);
        }
    }

    /**
     * <base engineering crs keyword> <left delimiter> <base crs name>
     * <wkt separator> <engineering datum> <right delimiter> @author malapert
     */
    public static class BaseEngineeringCrs implements BaseCrs {

        public static final String BASE_ENGINEERING_CRS_KEYWORD = "BASEENGCRS";

        private String baseCrsName;
        private EngineeringDatum datum;

        public BaseEngineeringCrs(WktElt datumWkt) {
            parse(datumWkt);
        }

        private void parse(final WktElt baseEngineeringCrsWkt) {
            final WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
            final List<WktElt> attributes = wktEltCollection.getAttributesFor(baseEngineeringCrsWkt, BASE_ENGINEERING_CRS_KEYWORD);
            this.setBaseCrsName(attributes.get(0).getKeyword());
            final List<WktElt> nodes = wktEltCollection.getNodesFor(baseEngineeringCrsWkt, BASE_ENGINEERING_CRS_KEYWORD);
            for (final WktElt node : nodes) {
                if (EngineeringDatum.EngineeringDatumKeyword.getKeywords().contains(node.getKeyword())) {
                    this.setDatum(new EngineeringDatum(baseEngineeringCrsWkt));
                } else {
                    throw new RuntimeException();
                }
            }
        }

        /**
         * @return the baseCrsName
         */
        @Override
        public String getBaseCrsName() {
            return baseCrsName;
        }

        /**
         * @param baseCrsName the baseCrsName to set
         */
        public void setBaseCrsName(String baseCrsName) {
            this.baseCrsName = baseCrsName;
        }

        /**
         * @return the datum
         */
        @Override
        public EngineeringDatum getDatum() {
            return datum;
        }

        /**
         * @param datum the datum to set
         */
        public void setDatum(EngineeringDatum datum) {
            this.datum = datum;
        }

        @Override
        public StringBuffer toWkt(final String endLine, final String tab, int deepLevel) {
            StringBuffer wkt = new StringBuffer();
            wkt = wkt.append(BASE_ENGINEERING_CRS_KEYWORD).append(LEFT_DELIMITER);
            wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(this.getBaseCrsName());
            wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(this.getDatum().toWkt(endLine, tab, deepLevel + 1));
            wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel)).append(RIGHT_DELIMITER);
            return wkt;
        }

        @Override
        public StringBuffer toWkt() {
            return toWkt("\n", "   ", 0);
        }

    }

    /**
     * <base geodetic crs keyword> <left delimiter> <base crs name>
     * <wkt separator>
     * <geodetic datum> [ <wkt separator> <ellipsoidal cs unit> ]
     * <right delimiter>
     *
     * @author malapert
     */
    public static class BaseGeodeticCrs implements BaseCrs {

        private String baseCrsName;
        private GeodeticDatum datum;
        private Unit angleUnit;

        public static final String BASE_GEODETIC_CRS_KEYWORD = "BASEGEODCRS";

        public BaseGeodeticCrs(final String baseCrsName, final GeodeticDatum geodeticDatum) {
            this.baseCrsName = baseCrsName;
            this.datum = geodeticDatum;
        }

        public BaseGeodeticCrs(final WktElt baseGeodeticCrsElts) {
            parse(baseGeodeticCrsElts);
        }

        private void parse(final WktElt baseGeodeticCrsElts) {
            final WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
            final List<WktElt> attributes = wktEltCollection.getAttributesFor(baseGeodeticCrsElts, BASE_GEODETIC_CRS_KEYWORD);
            this.setBaseCrsName(attributes.get(0).getKeyword());
            final List<WktElt> nodes = wktEltCollection.getNodesFor(baseGeodeticCrsElts, BASE_GEODETIC_CRS_KEYWORD);
            for (final WktElt node : nodes) {
                switch (node.getKeyword()) {
                    case "DATUM":
                    case "GEODETICDATUM":
                        this.setDatum(new GeodeticDatum(node));
                        break;
                    case UnitFactory.AngleUnit.ANGLEUNIT_KEYWORD:
                    case UnitFactory.AngleUnit.ANGLEUNIT_UNIT_KEYWORD:
                        final Unit unit = UnitFactory.createFromWkt(node);
                        this.setAngleUnit(unit);
                        break;
                    default:
                        throw new RuntimeException();
                }
            }
        }

        /**
         * @return the baseCrsName
         */
        @Override
        public String getBaseCrsName() {
            return baseCrsName;
        }

        /**
         * @param baseCrsName the baseCrsName to set
         */
        public void setBaseCrsName(String baseCrsName) {
            this.baseCrsName = baseCrsName;
        }

        /**
         * @return the datum
         */
        @Override
        public GeodeticDatum getDatum() {
            return datum;
        }

        /**
         * @param datum the datum to set
         */
        public void setDatum(GeodeticDatum datum) {
            this.datum = datum;
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

        @Override
        public StringBuffer toWkt(final String endLine, final String tab, int deepLevel) {
            StringBuffer wkt = new StringBuffer();
            wkt = wkt.append(BASE_GEODETIC_CRS_KEYWORD).append(LEFT_DELIMITER);
            wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(this.getBaseCrsName());
            wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(this.getDatum().toWkt(endLine, tab, deepLevel + 1));
            if (this.getAngleUnit() != null) {
                wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(this.getAngleUnit().toWkt(endLine, tab, deepLevel + 1));
            }
            wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel)).append(RIGHT_DELIMITER);
            return wkt;
        }

        @Override
        public StringBuffer toWkt() {
            return toWkt("\n", "   ", 0);
        }

    }

}
