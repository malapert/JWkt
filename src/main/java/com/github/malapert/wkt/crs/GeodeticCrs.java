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

import com.github.malapert.wkt.utils.WktElt;
import com.github.malapert.wkt.cs.CoordinateSystem;
import com.github.malapert.wkt.datum.GeodeticDatum;
import com.github.malapert.wkt.cs.PrimeMeridian;
import java.util.Arrays;
import java.util.List;

/**
 * Coordinate Reference System for Geodetic.
 * <pre>
 * {@code
 * <geodetic crs>::=<geodetic crs keyword> <left delimiter> <crs name> <wkt separator> <geodetic datum> <wkt separator> <coordinate system> <scope extent identifier remark> <right delimiter> 
 * }
 * </pre>
 * @author malapert
 */
public class GeodeticCrs extends AbstractCoordinateReferenceSystem {

    /**
     * List of geodetic CRS keywords.
     */
    public enum GeodeticCrsKeyword {
        GEODCRS,
        GEODETICCRS;
        
        GeodeticCrsKeyword() {
            
        }
        
        public static List<String> getKeywords() {
            return Arrays.asList(GeodeticCrsKeyword.GEODCRS.name(), GeodeticCrsKeyword.GEODETICCRS.name());
        }
    }
    
    public GeodeticCrs(final GeodeticCrsKeyword keyword, final String crsName, final GeodeticDatum datum, final CoordinateSystem cs) {
        setKeyword(keyword.name());
        setCrsName(crsName);
        setCrsDatum(datum);
        setCs(cs);
    }    
    
    /**
     * Creates the Geodetic CRS by parsing the Geodetic WKT element.
     * @param geodeticCrsElts 
     */
    public GeodeticCrs(final WktElt geodeticCrsElts) {
        parseCrs(geodeticCrsElts);
    }
    
    protected GeodeticCrs() {
    }    

    @Override
    protected void parseSpecificWkt(WktElt crsWkt) {        
        if(GeodeticDatum.GeodeticDatumKeyword.getKeywords().contains(crsWkt.getKeyword())) {
            setCrsDatum(new GeodeticDatum(crsWkt));
        } else if(PrimeMeridian.PrimeMeridianKeyword.getKeywords().contains(crsWkt.getKeyword())) {
            ((GeodeticDatum)getCrsDatum()).setPrimeMeridian(new PrimeMeridian(crsWkt));
        } else {
            throw new RuntimeException();
        }
    }  

    @Override
    protected boolean hasSpecificParsing() {
        return true;
    }
    
    
}
