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


import com.github.malapert.wkt.conversion.DerivedConversion;
import com.github.malapert.wkt.utils.WktElt;
import com.github.malapert.wkt.crs.BaseDerivatedCrsFactory.BaseGeodeticCrs;
import com.github.malapert.wkt.crs.GeodeticCrs.GeodeticCrsKeyword;
import com.github.malapert.wkt.cs.CoordinateSystem;

/**
 * <derived geodetic crs>::=<geodetic crs keyword> <left delimiter> <derived crs name> <wkt separator> <base geodetic crs> <wkt separator> <deriving conversion> <wkt separator> <coordinate system> <scope extent identifier remark> <right delimiter>
 * @author malapert
 */
public class DerivedGeodeticCrs extends AbstractDerivatedCoordinateReferenceSystem {   
    
    public DerivedGeodeticCrs(final GeodeticCrsKeyword keyword, final String crsName, final BaseGeodeticCrs baseCrs, final DerivedConversion conversion, final CoordinateSystem cs) {
        setKeyword(keyword.name());
        setCrsName(crsName);
        setBaseDerivatedCrs(baseCrs);
        setConversionFromBaseCrs(conversion);
        setCs(cs);
    }
    
    public DerivedGeodeticCrs(WktElt derivedGeodeticCrsWkt) {
        parseDerivatedCrs(derivedGeodeticCrsWkt);      
    }

    protected DerivedGeodeticCrs() {
    }
    
    @Override
    public void parseSpecificWkt(WktElt crsWkt) {
        if(BaseGeodeticCrs.BASE_GEODETIC_CRS_KEYWORD.equals(crsWkt.getKeyword())) {
            setBaseDerivatedCrs(new BaseGeodeticCrs(crsWkt));
        } else {
            throw new RuntimeException();
        }        
    }

    @Override
    protected boolean hasSpecificParsing() {
        return true;
    }              
    
}
