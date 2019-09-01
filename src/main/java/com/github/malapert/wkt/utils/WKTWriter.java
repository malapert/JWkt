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
package com.github.malapert.wkt.utils;

import com.github.malapert.wkt.crs.CoordinateReferenceSystem;
import com.github.malapert.wkt.crs.CoordinateReferenceSystemFactory;
import com.github.malapert.wkt.crs.DerivedGeodeticCrs;
import com.github.malapert.wkt.cs.CoordinateSystem;
import com.github.malapert.wkt.metadata.ScopeExtent;
import com.github.malapert.wkt.cs.Ellipsoid;
import com.github.malapert.wkt.datum.GeodeticDatum;
import com.github.malapert.wkt.cs.PrimeMeridian;
import com.github.malapert.wkt.metadata.Extent;
import com.github.malapert.wkt.metadata.ExtentFactory;
import com.github.malapert.wkt.metadata.Scope;
import com.github.malapert.wkt.metadata.Usage;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author malapert
 */
public class WKTWriter {
    
    public static void main(String[] args) {
        Ellipsoid name  = new Ellipsoid(Ellipsoid.EllipsoidKeyword.ELLIPSOID, "(trouver un nom)", 0, 0);
        PrimeMeridian pm = new PrimeMeridian(PrimeMeridian.PrimeMeridianKeyword.PRIMEM, "Greenwich", 0);
        GeodeticDatum datum = new GeodeticDatum("International Celestial Equatorial",name, pm);
        CoordinateSystem cs = new CoordinateSystem(CoordinateSystem.CsType.spherical, 0);        
        ScopeExtent scopeExent = new ScopeExtent();
        Extent area = new ExtentFactory.AreaDescription("Celestial");
        List<Extent> areaList = Arrays.asList(area);
        scopeExent.getUsageList().add(new Usage(new Scope("For celestial objects: satellites, planets, stars, galaxies."), areaList));
        CoordinateReferenceSystem crs = CoordinateReferenceSystemFactory.create("myCrs", datum, cs);
        //GeodeticCrs crs = new GeodeticCrs(GeodeticCrs.GeodeticCrsKeyword.GEODCRS, "MyCRS", datum, cs);
        System.out.println(crs);
        DerivedGeodeticCrs crs2 = CoordinateReferenceSystemFactory.create(DerivedGeodeticCrs.class);
    }
    
}
