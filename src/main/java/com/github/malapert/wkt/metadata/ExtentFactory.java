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
import com.github.malapert.wkt.utils.WktElt;
import com.github.malapert.wkt.utils.WktEltCollection;
import static com.github.malapert.wkt.metadata.ExtentFactory.AreaDescription.AREA_DESCRIPTION_KEYWORD;
import static com.github.malapert.wkt.metadata.ExtentFactory.GeographicBoundingBox.GEOGRAPHIC_BOUDING_BOX_KEYWORD;
import static com.github.malapert.wkt.metadata.ExtentFactory.TemporalExtent.TEMPORAL_EXTENT_KEYWORD;
import static com.github.malapert.wkt.metadata.ExtentFactory.VerticalExtent.VERTICAL_EXTENT_KEYWORD;
import com.github.malapert.wkt.metadata.UnitFactory.LengthUnit;
import com.github.malapert.wkt.utils.Utils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;

/**
 * {@link com.github.malapert.wkt.metadata.Extent} Factory.
 *
 * @author Jean-Christophe Malapert
 */
public abstract class ExtentFactory {

    /**
     * Returns the correct Extent according to the <i>keyword</i>.
     *
     * The different value can be:
     * <ul>
     * <li>AREA_DESCRIPTION_KEYWORD</li>
     * <li>GEOGRAPHIC_BOUDING_BOX_KEYWORD</li>
     * <li>VERTICAL_EXTENT_KEYWORD</li>
     * <li>TEMPORAL_EXTENT_KEYWORD</li>
     * </ul>
     *
     * @param extentWkt extent wkt
     * @return the {@link com.github.malapert.wkt.metadata.Extent}
     */
    public static Extent createFromWkt(final WktElt extentWkt) {
        final Extent extent;
        switch (extentWkt.getKeyword()) {
            case AREA_DESCRIPTION_KEYWORD:
                extent = new AreaDescription(extentWkt);
                break;
            case GEOGRAPHIC_BOUDING_BOX_KEYWORD:
                extent = new GeographicBoundingBox(extentWkt);
                break;
            case VERTICAL_EXTENT_KEYWORD:
                extent = new VerticalExtent(extentWkt);
                break;
            case TEMPORAL_EXTENT_KEYWORD:
                extent = new TemporalExtent(extentWkt);
                break;
            default:
                throw new RuntimeException();
        }
        return extent;
    }

    /**
     * Area areaDescription is an optional attribute which describes a
     * geographic area over which a CRS or coordinate operation is applicable.
     * <pre>
     * {@code
     * <area areaDescription> ::= <area areaDescription keyword> <left delimiter> <area text areaDescription> <right delimiter>
     * }
     * </pre>
     */
    public static final class AreaDescription implements Extent {

        /**
         * Keyword.
         */
        public final static String AREA_DESCRIPTION_KEYWORD = "AREA";

        /**
         * areaDescription.
         */
        private String areaDescription;

        public AreaDescription(final String description) {
            this.areaDescription = description;
        }

        /**
         * Constructor.
         *
         * @param extentWktElts
         */
        public AreaDescription(final WktElt extentWktElts) {
            final WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
            final List<WktElt> attributes = wktEltCollection.getAttributesFor(extentWktElts, AREA_DESCRIPTION_KEYWORD);
            this.setAreaDescription(Utils.removeQuotes(attributes.get(0).getKeyword()));
        }

        @Override
        public StringBuffer toWkt(final String endLine, final String tab, int deepLevel) {
            StringBuffer wkt = new StringBuffer();
            wkt = wkt.append(AREA_DESCRIPTION_KEYWORD).append(LEFT_DELIMITER);
            wkt = wkt.append(Utils.addQuotes(this.getDescription()));
            wkt = wkt.append(RIGHT_DELIMITER);
            return wkt;
        }

        @Override
        public StringBuffer toWkt() {
            return toWkt("\n", "   ", 0);
        }

        /**
         * Return the area description.
         *
         * @return the area description
         */
        public String getDescription() {
            return this.areaDescription;
        }

        /**
         * Sets the area text areaDescription.
         *
         * @param description the area text areaDescription to set
         */
        public void setAreaDescription(final String description) {
            this.areaDescription = description;
        }

        @Override
        public GeographicBoundingBox getGeographicElement() {
            return null;
        }

        @Override
        public TemporalExtent getTemporalElement() {
            return null;
        }

        @Override
        public VerticalExtent getVerticalElement() {
            return null;
        }

        @Override
        public AreaDescription getAreaDescription() {
            return this;
        }

    }

    /**
     * The geographic bounding box is an optional attribute which describes a
     * "north up" area.
     *
     * Upper right latitude will be greater than the lower left latitude.
     * Generally the upper right longitude will be greater than the lower left
     * longitude. However when the area crosses the 180° meridian, the value of
     * the lower left longitude will be greater than the value of the upper
     * right longitude.
     *
     * <p>
     * The geographic bounding box is an approximate areaDescription of
     * location. For most purposes a coordinate precision of two decimal places
     * of a degree is sufficient. At this resolution the identification of the
     * geodetic CRS to which the bounding box coordinates are referenced is not
     * required.
     * <p>
     * <
     * pre> {@code
     * <geographic bounding box> ::= <geographic bounding box keyword> <left delimiter> <lower left latitude> <wkt separator> <lower left longitude> <wkt separator> <upper right latitude> <wkt separator> <upper right longitude> <right delimiter>
     * }
     * </pre>
     */
    public static class GeographicBoundingBox implements Extent {

        public final static String GEOGRAPHIC_BOUDING_BOX_KEYWORD = "BBOX";
        private BigDecimal lowerLeftLatitude;
        private BigDecimal lowerLeftLongitude;
        private BigDecimal upperRightLatitude;
        private BigDecimal upperRightLongitude;

        public GeographicBoundingBox(float lowerLeftLatitude,
                float lowerLeftLongitude,
                float upperRightLatitude,
                float upperRightLongitude) {
            setLowerLeftLatitude(lowerLeftLatitude);
            setLowerLeftLongitude(lowerLeftLongitude);
            setUpperRightLatitude(upperRightLatitude);
            setUpperRightLongitude(upperRightLongitude);
        }

        public GeographicBoundingBox(float lowerLeftLatitude,
                float lowerLeftLongitude,
                float upperRightLatitude,
                float upperRightLongitude, int precision) {
            setLowerLeftLatitude(lowerLeftLatitude, precision);
            setLowerLeftLongitude(lowerLeftLongitude, precision);
            setUpperRightLatitude(upperRightLatitude, precision);
            setUpperRightLongitude(upperRightLongitude, precision);
        }

        /**
         * Empty constructor.
         *
         * @param extentWkt
         */
        public GeographicBoundingBox(final WktElt extentWkt) {
            final WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
            final List<WktElt> attributes = wktEltCollection.getAttributesFor(extentWkt, GEOGRAPHIC_BOUDING_BOX_KEYWORD);
            this.lowerLeftLatitude = new BigDecimal(attributes.get(0).getKeyword());
            this.lowerLeftLongitude = new BigDecimal(attributes.get(1).getKeyword());
            this.upperRightLatitude = new BigDecimal(attributes.get(2).getKeyword());
            this.upperRightLongitude = new BigDecimal(attributes.get(3).getKeyword());
        }

        @Override
        public StringBuffer toWkt(final String endLine, final String tab, int deepLevel) {
            StringBuffer wkt = new StringBuffer();
            wkt = wkt.append(GEOGRAPHIC_BOUDING_BOX_KEYWORD).append(LEFT_DELIMITER);
            wkt = wkt.append(this.lowerLeftLatitude);
            wkt = wkt.append(WKT_SEPARATOR).append(this.lowerLeftLongitude);
            wkt = wkt.append(WKT_SEPARATOR).append(this.upperRightLatitude);
            wkt = wkt.append(WKT_SEPARATOR).append(this.upperRightLongitude);
            wkt = wkt.append(RIGHT_DELIMITER);
            return wkt;
        }

        @Override
        public StringBuffer toWkt() {
            return toWkt("\n", "   ", 0);
        }

        /**
         * Returns the lower left latitude.
         *
         * @return the lowerLeftLatitude
         */
        public float getLowerLeftLatitude() {
            return this.lowerLeftLatitude.floatValue();
        }

        /**
         * Sets the lower left latitude.
         *
         * @param lowerLeftLatitude the lowerLeftLatitude to set
         */
        public final void setLowerLeftLatitude(float lowerLeftLatitude) {
            this.lowerLeftLatitude = new BigDecimal(lowerLeftLatitude);
        }

        public final void setLowerLeftLatitude(float lowerLeftLatitude, int precision) {
            this.lowerLeftLatitude = new BigDecimal(lowerLeftLatitude).setScale(precision, RoundingMode.HALF_EVEN);
        }

        /**
         * Returns the lower left longitude.
         *
         * @return the lowerLeftLongitude
         */
        public float getLowerLeftLongitude() {
            return this.lowerLeftLongitude.floatValue();
        }

        /**
         * Sets the lower left longitude.
         *
         * @param lowerLeftLongitude the lowerLeftLongitude to set
         */
        public final void setLowerLeftLongitude(float lowerLeftLongitude) {
            this.lowerLeftLongitude = new BigDecimal(lowerLeftLongitude);
        }

        public final void setLowerLeftLongitude(float lowerLeftLongitude, int precision) {
            this.lowerLeftLongitude = new BigDecimal(lowerLeftLongitude).setScale(precision, RoundingMode.HALF_EVEN);
        }

        /**
         * Returns the upper right latitude.
         *
         * @return the upperRightLatitude
         */
        public float getUpperRightLatitude() {
            return this.upperRightLatitude.floatValue();
        }

        /**
         * Sets the upper right latitude.
         *
         * @param upperRightLatitude the upperRightLatitude to set
         */
        public final void setUpperRightLatitude(float upperRightLatitude) {
            this.upperRightLatitude = new BigDecimal(upperRightLatitude);
        }

        public final void setUpperRightLatitude(float upperRightLatitude, int precision) {
            this.upperRightLatitude = new BigDecimal(upperRightLatitude).setScale(precision, RoundingMode.HALF_EVEN);
        }

        /**
         * Returns the upper right longitude.
         *
         * @return the upperRightLongitude
         */
        public float getUpperRightLongitude() {
            return this.upperRightLongitude.floatValue();
        }

        /**
         * Sets the upper right longitude.
         *
         * @param upperRightLongitude the upperRightLongitude to set
         */
        public final void setUpperRightLongitude(float upperRightLongitude) {
            this.upperRightLongitude = new BigDecimal(upperRightLongitude);
        }

        public final void setUpperRightLongitude(float upperRightLongitude, int precision) {
            this.upperRightLongitude = new BigDecimal(upperRightLongitude).setScale(precision, RoundingMode.HALF_EVEN);
        }

        @Override
        public GeographicBoundingBox getGeographicElement() {
            return this;
        }

        @Override
        public TemporalExtent getTemporalElement() {
            return null;
        }

        @Override
        public VerticalExtent getVerticalElement() {
            return null;
        }

        @Override
        public AreaDescription getAreaDescription() {
            return null;
        }
    }

    /**
     * Temporal extent is an optional attribute which describes a date or time
     * range over which a CRS or coordinate operation is applicable.
     *
     * The format for date and time values is defined in ISO 9075-2. Start time
     * is earlier than end time.
     *
     * <p>
     * {@code
     * <temporal extent> ::= <temporal extent keyword> <left delimiter> <temporal extent start> <wkt separator> <temporal extent end> <right delimiter>
     * }
     */
    public static class TemporalExtent implements Extent {

        public final static String TEMPORAL_EXTENT_KEYWORD = "TIMEEXTENT";
        private String start;
        private String stop;

        public TemporalExtent(final String start, final String stop) {
            setStart(start);
            setStop(stop);
        }

        /**
         * Empty constructor.
         *
         * @param extentWktElts
         */
        public TemporalExtent(final WktElt extentWktElts) {
            final WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
            final List<WktElt> attributes = wktEltCollection.getAttributesFor(extentWktElts, TEMPORAL_EXTENT_KEYWORD);
            this.setStart(Utils.removeQuotes(attributes.get(0).getKeyword()));
            this.setStop(Utils.removeQuotes(attributes.get(1).getKeyword()));
        }

        @Override
        public StringBuffer toWkt(final String endLine, final String tab, int deepLevel) {
            StringBuffer wkt = new StringBuffer();
            wkt = wkt.append(TEMPORAL_EXTENT_KEYWORD).append(LEFT_DELIMITER);
            if (Utils.isValidISO8601(this.getStart())) {
                wkt = wkt.append(this.getStart());
            } else {
                wkt = wkt.append(Utils.addQuotes(this.getStart()));
            }
            wkt = wkt.append(WKT_SEPARATOR);
            if (Utils.isValidISO8601(this.getStop())) {
                wkt = wkt.append(this.getStop());
            } else {
                wkt = wkt.append(Utils.addQuotes(this.getStop()));
            }
            wkt = wkt.append(RIGHT_DELIMITER);
            return wkt;
        }

        @Override
        public StringBuffer toWkt() {
            return toWkt("\n", "   ", 0);
        }

        /**
         * Returns the start date.
         *
         * @return the start
         */
        public String getStart() {
            return start;
        }

        /**
         * Sets the start date.
         *
         * @param start the start to set
         */
        public final void setStart(final String start) {
            this.start = start;
        }

        /**
         * Returns the stop date.
         *
         * @return the stop
         */
        public String getStop() {
            return stop;
        }

        /**
         * Sets the stop date.
         *
         * @param stop the stop to set
         */
        public final void setStop(final String stop) {
            this.stop = stop;
        }

        @Override
        public GeographicBoundingBox getGeographicElement() {
            return null;
        }

        @Override
        public TemporalExtent getTemporalElement() {
            return this;
        }

        @Override
        public VerticalExtent getVerticalElement() {
            return null;
        }

        @Override
        public AreaDescription getAreaDescription() {
            return null;
        }
    }

    /**
     * Vertical extent is an optional attribute which describes a height range
     * over which a CRS or coordinate operation is applicable.
     *
     * Depths have negative height values. Vertical extent is an approximate
     * areaDescription of location; heights are relative to an unspecified mean
     * sea level.
     *
     * <p>
     * <
     * pre> null null null null     {@code
     * <vertical extent> ::= <vertical extent keyword> <left delimiter> <vertical extent minimum height> <wkt separator> <vertical extent maximum height> [ <wkt separator> <length unit> ] <right delimiter>
     * }
     * </pre>
     */
    public static class VerticalExtent implements Extent {

        public final static String VERTICAL_EXTENT_KEYWORD = "VERTICALEXTENT";
        private BigDecimal minimumHeight;
        private BigDecimal maximumHeight;
        private Unit lengthUnit;

        public VerticalExtent(float minimumHeight, float maximumHeight) {
            setMinimumHeight(minimumHeight);
            setMaximumHeight(maximumHeight);
        }

        public VerticalExtent(float minimumHeight, float maximumHeight, int precision) {
            setMinimumHeight(minimumHeight, precision);
            setMaximumHeight(maximumHeight, precision);
        }

        /**
         * Empty constructor.
         *
         * @param extentWktElts
         */
        public VerticalExtent(final WktElt extentWktElts) {
            final WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
            final List<WktElt> attributes = wktEltCollection.getAttributesFor(extentWktElts, VERTICAL_EXTENT_KEYWORD);
            this.minimumHeight = new BigDecimal(attributes.get(0).getKeyword());
            this.maximumHeight = new BigDecimal(attributes.get(1).getKeyword());
            final List<WktElt> nodes = wktEltCollection.getNodesFor(extentWktElts, VERTICAL_EXTENT_KEYWORD);
            for (final WktElt node : nodes) {
                if (UnitFactory.LengthUnit.LENGTH_KEYWORD.equals(node.getKeyword())
                        || UnitFactory.LengthUnit.LENGTH_UNIT_KEYWORD.equals(node.getKeyword())) {
                    LengthUnit unit = new UnitFactory.LengthUnit(extentWktElts);
                    this.setLengthUnit(unit);
                } else {
                    throw new RuntimeException();
                }
            }
        }

        @Override
        public StringBuffer toWkt(final String endLine, final String tab, int deepLevel) {
            StringBuffer wkt = new StringBuffer();
            wkt = wkt.append(VERTICAL_EXTENT_KEYWORD).append(LEFT_DELIMITER);
            wkt = wkt.append(this.minimumHeight).append(WKT_SEPARATOR).append(this.maximumHeight);
            if (getLengthUnit() != null) {
                wkt = wkt.append(WKT_SEPARATOR).append(getLengthUnit().toWkt(endLine, tab, deepLevel + 1));
            }
            wkt = wkt.append(RIGHT_DELIMITER);
            return wkt;
        }

        @Override
        public StringBuffer toWkt() {
            return toWkt("\n", "   ", 0);
        }

        /**
         * Returns the vertical minimum height.
         *
         * @return the minimumHeight
         */
        public float getMinimumHeight() {
            return this.minimumHeight.floatValue();
        }

        /**
         * Sets the vertical minimum height.
         *
         * @param minimumHeight the minimumHeight to set
         */
        public final void setMinimumHeight(float minimumHeight) {
            this.minimumHeight = new BigDecimal(minimumHeight);
        }

        public final void setMinimumHeight(float minimumHeight, int precision) {
            this.minimumHeight = new BigDecimal(minimumHeight).setScale(precision, RoundingMode.HALF_EVEN);
        }

        /**
         * Returns the vertical maximum height.
         *
         * @return the maximumHeight
         */
        public float getMaximumHeight() {
            return this.maximumHeight.floatValue();
        }

        /**
         * Sets the vertical maximum height.
         *
         * @param maximumHeight the maximumHeight to set
         */
        public final void setMaximumHeight(float maximumHeight) {
            this.maximumHeight = new BigDecimal(maximumHeight);
        }

        public final void setMaximumHeight(float maximumHeight, int precision) {
            this.maximumHeight = new BigDecimal(maximumHeight).setScale(precision, RoundingMode.HALF_EVEN);
        }

        /**
         * Returns the length unit.
         *
         * @return the lengthUnit
         */
        public Unit getLengthUnit() {
            return lengthUnit;
        }

        /**
         * Sets the length unit.
         *
         * @param lengthUnit the lengthUnit to set
         */
        public final void setLengthUnit(UnitFactory.LengthUnit lengthUnit) {
            this.lengthUnit = lengthUnit;
        }

        @Override
        public GeographicBoundingBox getGeographicElement() {
            return null;
        }

        @Override
        public TemporalExtent getTemporalElement() {
            return null;
        }

        @Override
        public VerticalExtent getVerticalElement() {
            return this;
        }

        @Override
        public AreaDescription getAreaDescription() {
            return null;
        }
    }
}
