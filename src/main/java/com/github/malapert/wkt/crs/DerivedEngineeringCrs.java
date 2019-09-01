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
import com.github.malapert.wkt.crs.BaseDerivatedCrsFactory.BaseEngineeringCrs;
import com.github.malapert.wkt.crs.EngineeringCrs.EngineeringCrsKeyword;
import com.github.malapert.wkt.cs.CoordinateSystem;
import com.github.malapert.wkt.utils.WktElt;

/**
 * <derived engineering crs>::=	<engineering crs keyword> <left delimiter>
 * <derived crs name> <wkt separator> { <base projected crs> |
 * <base geodetic crs> | <base engineering crs> } <wkt separator>
 * <deriving conversion> <wkt separator> <coordinate system>
 * <scope extent identifier remark> <right delimiter>
 *
 * @author malapert
 */
public class DerivedEngineeringCrs extends AbstractDerivatedCoordinateReferenceSystem {

    public DerivedEngineeringCrs(final EngineeringCrsKeyword keyword, final String crsName, final BaseEngineeringCrs basecrs, final DerivedConversion conversion, final CoordinateSystem cs) {
        setKeyword(keyword.name());
        setCrsName(crsName);
        setBaseDerivatedCrs(basecrs);
        setConversionFromBaseCrs(conversion);
    }
    
    public DerivedEngineeringCrs(WktElt derivedEngineeringCrsWkt) {
        parseDerivatedCrs(derivedEngineeringCrsWkt);
    }

    protected DerivedEngineeringCrs() {
    }


    @Override
    protected void parseSpecificWkt(WktElt crsWkt) {
        if (BaseDerivatedCrsFactory.BaseProjectedCrs.BASE_PROJECTED_CRS.equals(crsWkt.getKeyword())
                || BaseDerivatedCrsFactory.BaseGeodeticCrs.BASE_GEODETIC_CRS_KEYWORD.equals(crsWkt.getKeyword())
                || BaseDerivatedCrsFactory.BaseEngineeringCrs.BASE_ENGINEERING_CRS_KEYWORD.equals(crsWkt.getKeyword())) {                   
            setBaseDerivatedCrs(BaseDerivatedCrsFactory.createFromWkt(crsWkt));
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    protected boolean hasSpecificParsing() {
        return true;
    }          

}
