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
package com.github.malapert.wkt.crs;

import com.github.malapert.wkt.cs.CoordinateSystem;
import com.github.malapert.wkt.datum.Datum;
import com.github.malapert.wkt.datum.EngineeringDatum;
import com.github.malapert.wkt.datum.GeodeticDatum;
import com.github.malapert.wkt.datum.ImageDatum;
import com.github.malapert.wkt.datum.ParametricDatum;
import com.github.malapert.wkt.datum.TemporalDatum;
import com.github.malapert.wkt.datum.VerticalDatum;
import com.github.malapert.wkt.metadata.Metadata;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author malapert
 */
public interface CoordinateReferenceSystem extends Metadata {
    
    public enum CrsType {
        GEODETIC_CRS (GeodeticCrs.GeodeticCrsKeyword.getKeywords(),"The WKT representation of a geodetic coordinate reference system","<geodetic crs keyword> <left delimiter> <crs name> <wkt separator> <geodetic datum> <wkt separator> <coordinate system> <scope extent identifier remark> <right delimiter>", Arrays.asList("DATUM"), false, GeodeticCrs.class.getName(), GeodeticDatum.class.getName()),
        PROJECTED_CRS (ProjectedCrs.ProjectedCrsKeyword.getKeywords(),"The WKT representation of a projected coordinate reference system","<projected crs keyword> <left delimiter> <crs name> <wkt separator> <base geodetic crs> <wkt separator> <map projection> <wkt separator> <coordinate system> <scope extent identifier remark> <right delimiter>",Arrays.asList("BASEGEODCRS"), false, ProjectedCrs.class.getName(), null),
        VERTICAL_CRS (VerticalCrs.VerticalKeyword.getKeywords(),"The WKT representation of a vertical coordinate reference system","<vertical crs keyword> <left delimiter> <crs name> <wkt separator> <vertical datum> <wkt separator> <coordinate system> <scope extent identifier remark> <right delimiter>",Arrays.asList("VDATUM","VERTICALDATUM"), false, VerticalCrs.class.getName(), VerticalDatum.class.getName()),
        ENGINEERING_CRS (EngineeringCrs.EngineeringCrsKeyword.getKeywords(),"The WKT representation of an engineering coordinate reference system","<engineering crs keyword> <left delimiter> <crs name> <wkt separator> <engineering datum> <wkt separator> <coordinate system> <scope extent identifier remark> <right delimiter>", Arrays.asList("EDATUM","ENGINEERINGDATUM"), false, EngineeringCrs.class.getName(), EngineeringDatum.class.getName()),
        IMAGE_CRS (Arrays.asList(ImageCrs.IMAGE_CRS_KEYWORD),"The WKT representation of an image coordinate reference system","<image crs keyword> <left delimiter> <crs name> <wkt separator> <image datum> <wkt separator> <coordinate system> <scope extent identifier remark> <right delimiter>", Arrays.asList("IDATUM","IMAGEDATUM"), false, ImageCrs.class.getName(), ImageDatum.class.getName()),
        PARAMETRIC_CRS (Arrays.asList(ParametricCrs.PARAMETRIC_KEYWORD),"The WKT representation of a parametric coordinate reference system","<parametric crs keyword> <left delimiter> <crs name> <wkt separator> <parametric datum> <wkt separator> <coordinate system> <scope extent identifier remark> <right delimiter>",Arrays.asList("PDATUM","PARAMETRICDATUM"), false, ParametricCrs.class.getName(), ParametricDatum.class.getName()),
        TEMPORAL_CRS (Arrays.asList(TemporalCrs.TEMPORAL_CRS_KEYWORD),"The WKT representation of a temporal coordinate reference system","<temporal crs keyword> <left delimiter> <crs name> <wkt separator> <temporal datum> <wkt separator> <coordinate system> <scope extent identifier remark> <right delimiter>", Arrays.asList("TDATUM","TIMEDATUM"), false, TemporalCrs.class.getName(), TemporalDatum.class.getName()),
        DERIVED_GEODETIC_CRS (GeodeticCrs.GeodeticCrsKeyword.getKeywords(),"A Derived CRS is a CRS which cannot exist in its own right but is defined through a coordinate conversion from another coordinate reference system","<geodetic crs keyword> <left delimiter> <derived crs name> <wkt separator> <base geodetic crs> <wkt separator> <deriving conversion> <wkt separator> <coordinate system> <scope extent identifier remark> <right delimiter>", Arrays.asList("BASEGEODCRS"), true, DerivedGeodeticCrs.class.getName(),null),
        DERIVED_VERTICAL_CRS (VerticalCrs.VerticalKeyword.getKeywords(),"A Derived CRS is a CRS which cannot exist in its own right but is defined through a coordinate conversion from another coordinate reference system","<vertical crs keyword> <left delimiter> <derived crs name> <wkt separator> <base vertical crs> <wkt separator> <deriving conversion> <wkt separator> <coordinate system> <scope extent identifier remark> <right delimiter>", Arrays.asList("BASEVERTCRS"), true, DerivedVerticalCrs.class.getName(),null),
        DERIVED_ENGINEERING_CRS (EngineeringCrs.EngineeringCrsKeyword.getKeywords(),"A Derived CRS is a CRS which cannot exist in its own right but is defined through a coordinate conversion from another coordinate reference system","	<engineering crs keyword> <left delimiter> <derived crs name> <wkt separator> { <base projected crs> | <base geodetic crs>  | <base engineering crs> } <wkt separator> <deriving conversion> <wkt separator> <coordinate system> <scope extent identifier remark> <right delimiter>", Arrays.asList("BASEPROJCRS","BASEGEODCRS","BASEENGCRS"), true, DerivedEngineeringCrs.class.getName(),null),
        DERIVED_PARAMETRIC_CRS (Arrays.asList(ParametricCrs.PARAMETRIC_KEYWORD),"A Derived CRS is a CRS which cannot exist in its own right but is defined through a coordinate conversion from another coordinate reference system","<parametric crs keyword> <left delimiter> <derived crs name> <wkt separator> <base parametric crs> <wkt separator> <deriving conversion> <wkt separator> <coordinate system> <scope extent identifier remark> <right delimiter>",Arrays.asList("BASEPARAMCRS"), true, DerivedParametricCrs.class.getName(),null),
        DERIVED_TEMPORAL_CRS (Arrays.asList(TemporalCrs.TEMPORAL_CRS_KEYWORD),"A Derived CRS is a CRS which cannot exist in its own right but is defined through a coordinate conversion from another coordinate reference system","<temporal crs keyword> <left delimiter> <derived crs name> <wkt separator> <base temporal crs> <wkt separator> <deriving conversion> <wkt separator> <coordinate system> <scope extent identifier remark> <right delimiter>",Arrays.asList("TIMECRS"), true, DerivedTemporalCrs.class.getName(), null),
        COMPOUND_CRS (Arrays.asList(CompoundCrs.COMPOUND_CRS),"A compound CRS is a non-repeating sequence of two or more independent coordinate reference systems none of which can itself be compound","<compound crs keyword> <left delimiter> <compound crs name> <wkt separator> <horizontal crs> <wkt separator> <vertical crs> | <parametric crs> | <temporal crs> | { <vertical crs> <wkt separator> <temporal crs> } | { <parametric crs> <wkt separator> <temporal crs> } [ <scope extent identifier remark> ] <right delimiter>",null, false, CompoundCrs.class.getName(), null);
        
        /**
         * Keywords that are used in the WKT description to define the 
         * coordinate reference system.
         */
        private final List<String> keywords;
        /**
         * Textual description of the coordinate system.
         */
        private final String description;
        /**
         * WKT syntax of the coordinate system.
         */
        private final String syntax;
        
        /**
         * Keywords that are used in the WKT description to define the 
         * datumOrOriginCrs of the coordinate reference system.
         */
        private final List<String> datumKeywords;        
        
        private final boolean isDerivated;
        
        private final String crsClassName;
        
        private final String datumClassName;
        
        CrsType(final List<String> keywords, final String description, final String syntax, final List<String> datumKeywords, final boolean isDerivated, final String crsClassName, final String datumClassName) {
            this.keywords = keywords;
            this.description = description;
            this.syntax = syntax;
            this.datumKeywords = datumKeywords;
            this.isDerivated = isDerivated;
            this.crsClassName = crsClassName;
            this.datumClassName = datumClassName;
        }

        /**
         * Returns the list of keywords that define the coordinate reference system
         * @return the keywords
         */
        public List<String> getKeywords() {
            return keywords;
        }

        /**
         * Returns the textual description of the coordinate reference system.
         * @return the description
         */
        public String getDescription() {
            return description;
        }

        /**
         * Returns the WKT syntax of the coordinate reference system.
         * @return the syntax
         */
        public String getSyntax() {
            return syntax;
        }
        
        /**
         * Returns the keywords for datumOrOriginCrs.
         * @return the keywords for datumOrOriginCrs
         */
        public List<String> getDatumKeywords() {
            return this.datumKeywords;
        }
        
        /**
         * Finds a coordinate reference system for a keyword value.
         * 
         * Returns a coordinate reference system for which the <i>keyword</i>
         * value is contained in the keywords of the CRS {@link #keywords}
         * 
         * @param keyword keyword to search in the keywords of each CRS
         * @return the coordinate reference system
         */
        public boolean isValidCRSFor(final String keyword) {
            return getKeywords().contains(keyword);
        }                
        
        public boolean isDerivated() {
            return this.isDerivated;
        }
        
        public String getCrsClassName() {
            return this.crsClassName;
        }
        
        public String getDatumClassName() {
            return this.datumClassName;
        }
        
        public static CrsType findCRSFor(final String keyword) {
            return findCRSFor(keyword, false);
        }
        
        public static CrsType findCRSForDatum(final String keyword) {
            CrsType result = null;
            CrsType[] crsTypes = CrsType.values();
            for (CrsType crsType : crsTypes) {
                if(crsType.getDatumKeywords().contains(keyword)) {
                    result = crsType;
                    break;
                }
            }
            return result;            
        }
        
        public static CrsType findCRSFor(final String keyword, boolean isDerivated) {
            CrsType result = null;
            CrsType[] crsTypes = CrsType.values();
            for (CrsType crsType : crsTypes) {
                if(crsType.getKeywords().contains(keyword) && crsType.isDerivated == isDerivated) {
                    result = crsType;
                    break;
                }
            }
            return result;
        }        
    }
    
    public String getCrsName();
    public Datum getCrsDatum();
    public CoordinateSystem getCs();
    public StringBuffer toWkt(int deepLevel);
}
