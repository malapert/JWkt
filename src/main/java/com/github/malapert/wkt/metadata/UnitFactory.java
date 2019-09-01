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

import com.github.malapert.wkt.utils.WktElt;

/**
 * {@link com.github.malapert.wkt.metadata.Unit} factory
 *
 * @author Jean-Christophe Malapert
 */
public abstract class UnitFactory {

    /**
     * Returns the right Unit implementation according to the <i>keyword</i>.
     *
     * @param unitWkt
     * @return the right Unit implementation
     */
    public static Unit createFromWkt(final WktElt unitWkt) {
        switch (unitWkt.getKeyword()) {
            case AngleUnit.ANGLEUNIT_KEYWORD:
            case AngleUnit.ANGLEUNIT_UNIT_KEYWORD:
                return new AngleUnit(unitWkt);
            case LengthUnit.LENGTH_KEYWORD:
            case LengthUnit.LENGTH_UNIT_KEYWORD:
                return new LengthUnit(unitWkt);
            case ScaleUnit.SCALEUNIT_KEYWORD:
            case ScaleUnit.SCALEUNIT_UNIT_KEYWORD:
                return new ScaleUnit(unitWkt);
            case ParametricUnit.PARAMETRICUNIT_KEYWORD:
            case ParametricUnit.PARAMETRICUNIT_UNIT_KEYWORD:
                return new ParametricUnit(unitWkt);
            case TimeUnit.TIMEUNIT_KEYWORD:
            case TimeUnit.TIMEUNIT_UNIT_KEYWORD:
                return new TimeUnit(unitWkt);
            default:
                throw new RuntimeException("Unknown unit");
        }
    }

    public static Unit createUnitParameterFromWkt(final WktElt unitWkt) {
        switch (unitWkt.getKeyword()) {
            case AngleUnit.ANGLEUNIT_KEYWORD:
            case AngleUnit.ANGLEUNIT_UNIT_KEYWORD:
                return new AngleUnit(unitWkt);
            case LengthUnit.LENGTH_KEYWORD:
            case LengthUnit.LENGTH_UNIT_KEYWORD:
                return new LengthUnit(unitWkt);
            case ScaleUnit.SCALEUNIT_KEYWORD:
            case ScaleUnit.SCALEUNIT_UNIT_KEYWORD:
                return new ScaleUnit(unitWkt);
            default:
                throw new RuntimeException("Unknown unit");
        }
    }

    /**
     * The unit for an angle.
     *
     * <pre>
     * {@code
     * <angle unit> ::= <angle unit keyword> <left delimiter> <unit name> <wkt separator> <conversion factor> [ { <wkt separator> <identifier> } ]…  <right delimiter>
     * }
     * </pre>
     */
    public static class AngleUnit extends Unit {

        public final static String ANGLEUNIT_KEYWORD = "ANGLEUNIT";
        public final static String ANGLEUNIT_UNIT_KEYWORD = "ANGLEUNIT.UNIT";

        public AngleUnit(final String name, float conversionFactor) {
            super(name, conversionFactor);
        }
                
        /**
         * Empty constructor
         *
         * @param unitWkt
         */
        public AngleUnit(final WktElt unitWkt) {
            super(unitWkt);
        }

        @Override
        public String getUnitKeyword() {
            return ANGLEUNIT_KEYWORD;
        }
    }

    /**
     * The unit for a length.
     * <pre>
     * {@code
     * <length unit>::=	<length unit keyword> <left delimiter> <unit name> <wkt separator> <conversion factor> [ { <wkt separator> <identifier> } ]…  <right delimiter>
     * }
     * </pre>
     */
    public static class LengthUnit extends Unit {

        public final static String LENGTH_KEYWORD = "LENGTHUNIT";
        public final static String LENGTH_UNIT_KEYWORD = "LENGTHUNIT.UNIT";

        public LengthUnit(final String name, float conversionFactor) {
            super(name, conversionFactor);
        }        
        
        /**
         * Empty constructor.
         *
         * @param unitWkt
         */
        public LengthUnit(final WktElt unitWkt) {
            super(unitWkt);
        }

        @Override
        public String getUnitKeyword() {
            return LENGTH_KEYWORD;
        }

    }

    /**
     * The unit for a scale.
     *
     * <pre>
     * {@code
     * <scale unit>::=<scale unit keyword> <left delimiter> <unit name> <wkt separator> <conversion factor> [ { <wkt separator> <identifier> } ]…  <right delimiter>
     * }
     * </pre>
     */
    public static class ScaleUnit extends Unit {

        public final static String SCALEUNIT_KEYWORD = "SCALEUNIT";
        public final static String SCALEUNIT_UNIT_KEYWORD = "SCALEUNIT.UNIT";

        public ScaleUnit(final String name, float conversionFactor) {
            super(name, conversionFactor);
        }
        
        /**
         * Empty constructor.
         *
         * @param unitWkt
         */
        public ScaleUnit(final WktElt unitWkt) {
            super(unitWkt);
        }

        @Override
        public String getUnitKeyword() {
            return SCALEUNIT_KEYWORD;
        }
    }

    /**
     * The unit for parametric.
     *
     * <pre>
     * {@code
     * <parametric unit>::=<parametric unit keyword> <left delimiter> <unit name> <wkt separator> <conversion factor> [ { <wkt separator> <identifier> } ]…  <right delimiter>
     * }
     * </pre>
     */
    public static class ParametricUnit extends Unit {

        public final static String PARAMETRICUNIT_KEYWORD = "PARAMETRICUNIT";
        public final static String PARAMETRICUNIT_UNIT_KEYWORD = "PARAMETRICUNIT.UNIT";

        public ParametricUnit(final String name, float conversionFactor) {
            super(name, conversionFactor);
        }
        
        /**
         * Empty constructor.
         *
         * @param unitWkt
         */
        public ParametricUnit(final WktElt unitWkt) {
            super(unitWkt);
        }

        @Override
        public String getUnitKeyword() {
            return PARAMETRICUNIT_KEYWORD;
        }
    }

    /**
     * The unit for time.
     *
     * <pre>
     * {@code
     * <time unit>::=<time unit keyword> <left delimiter> <unit name> <wkt separator> <conversion factor> [ { <wkt separator> <identifier> } ]… <right delimiter>
     * }
     * </pre>
     */
    public static class TimeUnit extends Unit {

        public final static String TIMEUNIT_KEYWORD = "TIMEUNIT";
        public final static String TIMEUNIT_UNIT_KEYWORD = "TIMEUNIT.UNIT";

        public TimeUnit(final String name, float conversionFactor) {
            super(name, conversionFactor);
        }
        
        /**
         * Empty constructor.
         *
         * @param unitWkt
         */
        public TimeUnit(final WktElt unitWkt) {
            super(unitWkt);
        }

        @Override
        public String getUnitKeyword() {
            return TIMEUNIT_KEYWORD;
        }
    }
}
