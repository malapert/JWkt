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
