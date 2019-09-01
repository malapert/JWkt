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
package com.github.malapert.wkt.datum;

import com.github.malapert.wkt.utils.WktElt;
import java.util.Arrays;
import java.util.List;

/**
 * <engineering datum keyword> <left delimiter> <datum name> [ <wkt separator> <datum anchor> ] [ { <wkt separator> <identifier> } ]…  <right delimiter>  
 * @author malapert
 */
public class EngineeringDatum extends AbstractDatum {
    
    public enum EngineeringDatumKeyword {
        EDATUM,
        ENGINEERINGDATUM;
        private EngineeringDatumKeyword() {        
        }
        
        public static List<String> getKeywords() {
            return Arrays.asList(EngineeringDatumKeyword.EDATUM.name(), EngineeringDatumKeyword.ENGINEERINGDATUM.name());
        }         
    }
    
    public EngineeringDatum(final EngineeringDatumKeyword keyword, final String datumName) {
        setKeyword(keyword.name());
        setDatumName(datumName);
    }
    
    public EngineeringDatum(WktElt engineeringDatumWkt) {
        parse(engineeringDatumWkt);
    }    
    
}
