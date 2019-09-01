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

import com.github.malapert.wkt.cs.CoordinateSystem;
import com.github.malapert.wkt.datum.GeodeticDatum;
import com.github.malapert.wkt.utils.WktElt;

/**
 *
 * @author malapert
 */
public class Geographic2DCrs extends GeodeticCrs {
    
    public Geographic2DCrs(final GeodeticCrsKeyword keyword, final String crsName, final GeodeticDatum datum, final CoordinateSystem cs) {
        super(keyword, crsName, datum, cs);
        CoordinateSystem cs2D = this.getCs();
        cs2D.setCsType(CoordinateSystem.CsType.ellipsoidal);
        cs2D.setDimension(2);        
    }
    
    public Geographic2DCrs(final WktElt geographic2DCrs) {
        super(geographic2DCrs);
        CoordinateSystem cs2D = this.getCs();
        cs2D.setCsType(CoordinateSystem.CsType.ellipsoidal);
        cs2D.setDimension(2);
    }
    
}
