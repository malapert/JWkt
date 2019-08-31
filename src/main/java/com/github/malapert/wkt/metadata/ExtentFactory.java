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
package com.github.malapert.wkt.metadata;

import com.github.malapert.wkt.utils.Singleton;
import com.github.malapert.wkt.utils.WktElt;
import com.github.malapert.wkt.utils.WktEltCollection;
import static com.github.malapert.wkt.metadata.ExtentFactory.AreaDescription.AREA_DESCRIPTION_KEYWORD;
import static com.github.malapert.wkt.metadata.ExtentFactory.GeographicBoundingBox.GEOGRAPHIC_BOUDING_BOX_KEYWORD;
import static com.github.malapert.wkt.metadata.ExtentFactory.TemporalExtent.TEMPORAL_EXTENT_KEYWORD;
import static com.github.malapert.wkt.metadata.ExtentFactory.VerticalExtent.VERTICAL_EXTENT_KEYWORD;
import com.github.malapert.wkt.metadata.UnitFactory.LengthUnit;
import java.util.List;

/**
 * {@link com.github.malapert.wkt.metadata.Extent} Factory. 
 * 
 * @author Jean-Christophe Malapert
 */
public abstract class ExtentFactory  {

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
     * @param extentWkt extent wkt
     * @return the {@link com.github.malapert.wkt.metadata.Extent}
     */
    public static Extent createFromWkt(final WktElt extentWkt) {
        switch (extentWkt.getKeyword()) {
            case AREA_DESCRIPTION_KEYWORD:
                return new AreaDescription(extentWkt);
            case GEOGRAPHIC_BOUDING_BOX_KEYWORD:
                return new GeographicBoundingBox(extentWkt);
            case VERTICAL_EXTENT_KEYWORD:
                return new VerticalExtent(extentWkt);
            case TEMPORAL_EXTENT_KEYWORD:
                return new TemporalExtent(extentWkt);
            default:
                throw new RuntimeException();
        }
    }

    /**
     * Area description is an optional attribute which describes a geographic area over which a CRS or coordinate operation is applicable.
     * <pre>
     * {@code
     * <area description> ::= <area description keyword> <left delimiter> <area text description> <right delimiter>  
     * }
     * </pre>
     */
    public static final class AreaDescription implements Extent  {

        /**
         * Keyword.
         */
        public final static String AREA_DESCRIPTION_KEYWORD = "AREA";
        
        /**
         * description.
         */
        private String description;
        
        public AreaDescription(final String description) {
            this.description = description;
        }

        /**
         * Constructor.
         * @param extentWktElts
         */
        public AreaDescription(final WktElt extentWktElts) {
            final WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
            final List<WktElt> attributes = wktEltCollection.getAttributesFor(extentWktElts, AREA_DESCRIPTION_KEYWORD);
            this.setDescription(attributes.get(0).getKeyword());
        }

        @Override
        public StringBuffer toWkt(int deepLevel) {
            StringBuffer wkt = new StringBuffer();
            wkt = wkt.append(AREA_DESCRIPTION_KEYWORD).append(LEFT_DELIMITER);
            wkt = wkt.append(this.getDescription());
            wkt = wkt.append(RIGHT_DELIMITER);
            return wkt;
        }

        /**
         * Return the area text description.
         * @return the area text description
         */
        @Override
        public String getDescription() {
            return description;
        }

        /**
         * Sets the area text description.
         * @param description the area text description to set
         */
        public void setDescription(final String description) {
            this.description = description;
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
     * The geographic bounding box is an approximate description of location. 
     * For most purposes a coordinate precision of two decimal places of a 
     * degree is sufficient. At this resolution the identification of the 
     * geodetic CRS to which the bounding box coordinates are referenced is not 
     * required.
     * <p>
     * <pre>
     * {@code
     * <geographic bounding box> ::= <geographic bounding box keyword> <left delimiter> <lower left latitude> <wkt separator> <lower left longitude> <wkt separator> <upper right latitude> <wkt separator> <upper right longitude> <right delimiter>  
     * }
     * </pre>
     */
    public static class GeographicBoundingBox implements Extent {

        public final static String GEOGRAPHIC_BOUDING_BOX_KEYWORD = "BBOX";
        private float lowerLeftLatitude;
        private float lowerLeftLongitude;
        private float upperRightLatitude;
        private float upperRightLongitude;
        
        public GeographicBoundingBox(float lowerLeftLatitude, 
                float lowerLeftLongitude,
                float upperRightLatitude,
                float upperRightLongitude) {
            setLowerLeftLatitude(lowerLeftLatitude);
            setLowerLeftLongitude(lowerLeftLongitude);
            setUpperRightLatitude(upperRightLatitude);
            setUpperRightLongitude(upperRightLongitude);
        }

        /**
         * Empty constructor.
         * @param extentWkt
         */
        public GeographicBoundingBox(final WktElt extentWkt) {
            WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
            List<WktElt> attributes = wktEltCollection.getAttributesFor(extentWkt, GEOGRAPHIC_BOUDING_BOX_KEYWORD);
            this.setLowerLeftLatitude(Float.parseFloat(attributes.get(0).getKeyword()));
            this.setLowerLeftLongitude(Float.parseFloat(attributes.get(1).getKeyword()));
            this.setUpperRightLatitude(Float.parseFloat(attributes.get(2).getKeyword()));
            this.setUpperRightLongitude(Float.parseFloat(attributes.get(3).getKeyword()));  
        }

        @Override
        public StringBuffer toWkt(int deepLevel) {
            StringBuffer wkt = new StringBuffer();
            wkt = wkt.append(GEOGRAPHIC_BOUDING_BOX_KEYWORD).append(LEFT_DELIMITER);
            wkt = wkt.append(this.getLowerLeftLatitude());
            wkt = wkt.append(WKT_SEPARATOR).append(this.getLowerLeftLongitude());
            wkt = wkt.append(WKT_SEPARATOR).append(this.getUpperRightLatitude());
            wkt = wkt.append(WKT_SEPARATOR).append(this.getUpperRightLongitude());
            wkt = wkt.append(RIGHT_DELIMITER);
            return wkt;
        }

        /**
         * Returns the lower left latitude.
         * @return the lowerLeftLatitude
         */
        public float getLowerLeftLatitude() {
            return lowerLeftLatitude;
        }

        /**
         * Sets the lower left latitude.
         * @param lowerLeftLatitude the lowerLeftLatitude to set
         */
        public final void setLowerLeftLatitude(float lowerLeftLatitude) {
            this.lowerLeftLatitude = lowerLeftLatitude;
        }

        /**
         * Returns the lower left longitude.
         * @return the lowerLeftLongitude
         */
        public float getLowerLeftLongitude() {
            return lowerLeftLongitude;
        }

        /**
         * Sets the lower left longitude.
         * @param lowerLeftLongitude the lowerLeftLongitude to set
         */
        public final void setLowerLeftLongitude(float lowerLeftLongitude) {
            this.lowerLeftLongitude = lowerLeftLongitude;
        }

        /**
         * Returns the upper right latitude.
         * @return the upperRightLatitude
         */
        public float getUpperRightLatitude() {
            return upperRightLatitude;
        }

        /**
         * Sets the upper right latitude.
         * @param upperRightLatitude the upperRightLatitude to set
         */
        public final void setUpperRightLatitude(float upperRightLatitude) {
            this.upperRightLatitude = upperRightLatitude;
        }

        /**
         * Returns the upper right longitude.
         * @return the upperRightLongitude
         */
        public float getUpperRightLongitude() {
            return upperRightLongitude;
        }

        /**
         * Sets the upper right longitude.
         * @param upperRightLongitude the upperRightLongitude to set
         */
        public final void setUpperRightLongitude(float upperRightLongitude) {
            this.upperRightLongitude = upperRightLongitude;
        }        

        @Override
        public String getDescription() {
            return null;
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
         * @param extentWktElts
         */
        public TemporalExtent(final WktElt extentWktElts) {
            WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
            List<WktElt> attributes = wktEltCollection.getAttributesFor(extentWktElts, GEOGRAPHIC_BOUDING_BOX_KEYWORD);
            this.setStart(attributes.get(0).getKeyword());
            this.setStop(attributes.get(1).getKeyword());             
        }

        @Override
        public StringBuffer toWkt(int deepLevel) {
            StringBuffer wkt = new StringBuffer();
            wkt = wkt.append(TEMPORAL_EXTENT_KEYWORD).append(LEFT_DELIMITER);
            wkt = wkt.append(this.getStart()).append(WKT_SEPARATOR).append(this.getStop());            
            wkt = wkt.append(RIGHT_DELIMITER);
            return wkt;
        }

        /**
         * Returns the start date.
         * @return the start
         */
        public String getStart() {
            return start;
        }

        /**
         * Sets the start date.
         * @param start the start to set
         */
        public final void setStart(final String start) {
            this.start = start;
        }

        /**
         * Returns the stop date.
         * @return the stop
         */
        public String getStop() {
            return stop;
        }

        /**
         * Sets the stop date.
         * @param stop the stop to set
         */
        public final void setStop(final String stop) {
            this.stop = stop;
        }                

        @Override
        public String getDescription() {
            return null;
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
    }

    /**
     * Vertical extent is an optional attribute which describes a height range 
     * over which a CRS or coordinate operation is applicable. 
     * 
     * Depths have negative height values. Vertical extent is an approximate 
     * description of location; heights are relative to an unspecified mean sea 
     * level.
     * 
     * <p>
     * <pre>
     * {@code 
     * <vertical extent> ::= <vertical extent keyword> <left delimiter> <vertical extent minimum height> <wkt separator> <vertical extent maximum height> [ <wkt separator> <length unit> ] <right delimiter>  
     * }
     * </pre>
     */
    public static class VerticalExtent implements Extent {

        public final static String VERTICAL_EXTENT_KEYWORD = "VERTICALEXTENT";
        private float minimumHeight;
        private float maximumHeight;
        private Unit lengthUnit;

        public VerticalExtent(float minimumHeight, float maximumHeight) {
            setMinimumHeight(minimumHeight);
            setMaximumHeight(maximumHeight);
        }
        
        /**
         * Empty constructor.
         * @param extentWktElts
         */
        public VerticalExtent(final WktElt extentWktElts) {
            WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
            List<WktElt> attributes = wktEltCollection.getAttributesFor(extentWktElts, GEOGRAPHIC_BOUDING_BOX_KEYWORD);
            this.setMinimumHeight(Float.parseFloat(attributes.get(0).getKeyword()));
            this.setMaximumHeight(Float.parseFloat(attributes.get(1).getKeyword()));
            List<WktElt> nodes = wktEltCollection.getNodesFor(extentWktElts, GEOGRAPHIC_BOUDING_BOX_KEYWORD);
            for(WktElt node:nodes) {
                if(UnitFactory.LengthUnit.LENGTH_KEYWORD.equals(node.getKeyword())
                   || UnitFactory.LengthUnit.LENGTH_UNIT_KEYWORD.equals(node.getKeyword())) {
                    LengthUnit unit = new UnitFactory.LengthUnit(extentWktElts);
                    this.setLengthUnit(unit);                    
                } else {
                    throw new RuntimeException();
                }
            }  
        }

        @Override
        public StringBuffer toWkt(int deepLevel) {
            StringBuffer wkt = new StringBuffer();
            wkt = wkt.append(VERTICAL_EXTENT_KEYWORD).append(LEFT_DELIMITER);
            wkt = wkt.append(getMinimumHeight()).append(WKT_SEPARATOR).append(getMaximumHeight());
            if(getLengthUnit()!=null) {
                wkt = wkt.append(WKT_SEPARATOR).append(getLengthUnit().toWkt(deepLevel+1));
            }
            wkt = wkt.append(RIGHT_DELIMITER);
            return wkt;
        }

        /**
         * Returns the vertical minimum height.
         * @return the minimumHeight
         */
        public float getMinimumHeight() {
            return minimumHeight;
        }

        /**
         * Sets the vertical minimum height.
         * @param minimumHeight the minimumHeight to set
         */
        public final void setMinimumHeight(float minimumHeight) {
            this.minimumHeight = minimumHeight;
        }

        /**
         * Returns the vertical maximum height.
         * @return the maximumHeight
         */
        public float getMaximumHeight() {
            return maximumHeight;
        }

        /**
         * Sets the vertical maximum height.
         * @param maximumHeight the maximumHeight to set
         */
        public final void setMaximumHeight(float maximumHeight) {
            this.maximumHeight = maximumHeight;
        }

        /**
         * Returns the length unit.
         * @return the lengthUnit
         */
        public Unit getLengthUnit() {
            return lengthUnit;
        }

        /**
         * Sets the length unit.
         * @param lengthUnit the lengthUnit to set
         */
        public final void setLengthUnit(UnitFactory.LengthUnit lengthUnit) {
            this.lengthUnit = lengthUnit;
        }        

        @Override
        public String getDescription() {
            return null;
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
    }
}
