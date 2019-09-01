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

import com.github.malapert.wkt.utils.WktElt;
import com.github.malapert.wkt.cs.CoordinateSystem;
import com.github.malapert.wkt.datum.ParametricDatum;

/**
 * <parametric crs>::=<parametric crs keyword> <left delimiter> <crs name> <wkt separator> <parametric datum> <wkt separator> <coordinate system> <scope extent identifier remark> <right delimiter>  
 * @author malapert
 */
public class ParametricCrs extends AbstractCoordinateReferenceSystem {
    
    public static final String PARAMETRIC_KEYWORD = "PARAMETRICCRS";       
    
    public ParametricCrs(final String crsName, final ParametricDatum datum, final CoordinateSystem cs) {
        setKeyword(PARAMETRIC_KEYWORD);
        setCrsName(crsName);
        setCrsDatum(datum);
        setCs(cs);
    }    
    
    public ParametricCrs(WktElt parametricCrsWkt) {
        parseCrs(parametricCrsWkt);
    }

    protected ParametricCrs() {
    }

    @Override
    protected void parseSpecificWkt(WktElt crsWkt) {
        if(ParametricDatum.ParametricDatumKeywords.getKeywords().contains(crsWkt.getKeyword())) {
            setCrsDatum(new ParametricDatum(crsWkt));
        } else {
            throw new RuntimeException();
        }
    }   

    @Override
    protected boolean hasSpecificParsing() {
        return true;
    }
    
    
    
}
