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

/**
 *
 * @author malapert
 */
public class HorizontalCrsFactory {
    
    public static CoordinateReferenceSystem createFromWkt(WktElt crsWkt) {
        if(CoordinateReferenceSystem.CrsType.GEODETIC_CRS.getKeywords().contains(crsWkt.getKeyword())) {
            return new Geographic2DCrs(crsWkt);
        } else if(CoordinateReferenceSystem.CrsType.PROJECTED_CRS.getKeywords().contains(crsWkt.getKeyword())) {
            return new ProjectedCrs(crsWkt);
        } else if(CoordinateReferenceSystem.CrsType.ENGINEERING_CRS.getKeywords().contains(crsWkt.getKeyword())) {
            return new EngineeringCrs(crsWkt);
        } else {
            throw new RuntimeException();
        }
    }
    
}
