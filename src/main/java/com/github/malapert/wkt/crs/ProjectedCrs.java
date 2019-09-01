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

import com.github.malapert.wkt.utils.Utils;
import static com.github.malapert.wkt.metadata.WktDescription.LEFT_DELIMITER;
import static com.github.malapert.wkt.metadata.WktDescription.RIGHT_DELIMITER;
import static com.github.malapert.wkt.metadata.WktDescription.WKT_SEPARATOR;
import com.github.malapert.wkt.utils.WktElt;
import com.github.malapert.wkt.conversion.MapProjection;
import com.github.malapert.wkt.crs.BaseDerivatedCrsFactory.BaseGeodeticCrs;
import com.github.malapert.wkt.cs.CoordinateSystem;
import com.github.malapert.wkt.parameter.MapProjectionParameter;
import java.util.Arrays;
import java.util.List;

/**
 * <projected crs keyword> <left delimiter> <crs name> <wkt separator> 
 * <base geodetic crs> <wkt separator> <map projection> <wkt separator> 
 * <coordinate system> <scope extent identifier remark> <right delimiter>  
 * @author malapert
 */
public class ProjectedCrs extends AbstractDerivatedCoordinateReferenceSystem {  

      
    public enum ProjectedCrsKeyword {
        PROJCRS,
        PROJECTEDCRS;
        
        ProjectedCrsKeyword() {
            
        }
        
        public static List<String> getKeywords() {
            return Arrays.asList(ProjectedCrsKeyword.PROJCRS.name(), ProjectedCrsKeyword.PROJECTEDCRS.name());
        }        
    }
    
    public ProjectedCrs(final ProjectedCrsKeyword keyword, final String crsName, final BaseGeodeticCrs baseCrs, final MapProjection mapProjection, final CoordinateSystem cs) {
        setKeyword(keyword.name());
        setCrsName(crsName);
        setBaseDerivatedCrs(baseCrs);
        setConversionFromBaseCrs(mapProjection);
        setCs(cs);
    }      
   
    
    public ProjectedCrs(final WktElt projectedCrsElts) {
        parseDerivatedCrs(projectedCrsElts);
    }

    protected ProjectedCrs() {
    }
    
    @Override
    protected void parseSpecificWkt(WktElt crsWkt) {
        if(null != crsWkt.getKeyword()) switch (crsWkt.getKeyword()) {
            case BaseDerivatedCrsFactory.BaseGeodeticCrs.BASE_GEODETIC_CRS_KEYWORD:
                this.setBaseDerivatedCrs(BaseDerivatedCrsFactory.createFromWkt(crsWkt));
                break;
            case MapProjection.MAP_PROJECTION_KEYWORD:
            case MapProjectionParameter.MAP_PROJECTION_PARAMETER:
                this.setConversionFromBaseCrs(new MapProjection(crsWkt));
                break;
            default:
                throw new RuntimeException();
        }
    }   

    @Override
    protected boolean hasSpecificParsing() {
        return true;
    }          

    @Override
    public StringBuffer toWkt(final String endLine, final String tab, int deepLevel) {
        StringBuffer wkt = new StringBuffer();
        wkt = wkt.append(this.getKeyword()).append(LEFT_DELIMITER);
        wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel+1)).append(this.getCrsName());
        wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel+1)).append(this.getBaseDerivatedCrs().toWkt(endLine, tab, deepLevel+1));
        wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel+1)).append(this.getConversionFromBaseCrs().toWkt(endLine, tab, deepLevel+1));        
        wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel+1)).append(this.getCs().toWkt(endLine, tab, deepLevel+1));
        wkt = wkt.append(this.getScopeExtent().toWkt(endLine, tab, deepLevel+1));
        wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel)).append(RIGHT_DELIMITER);
        return wkt;    
    }  
    
}
