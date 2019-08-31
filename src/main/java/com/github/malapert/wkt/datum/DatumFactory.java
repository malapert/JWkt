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
package com.github.malapert.wkt.datum;

import com.github.malapert.wkt.utils.WktElt;

/**
 *
 * @author malapert
 */
public abstract class DatumFactory {
    
    public static Datum createFromWkt(final WktElt datumWkt) {        
        if(EngineeringDatum.EngineeringDatumKeyword.getKeywords().contains(datumWkt.getKeyword())) {
            return new EngineeringDatum(datumWkt);
        } else if (GeodeticDatum.GeodeticDatumKeyword.getKeywords().contains(datumWkt.getKeyword())) {
            return new GeodeticDatum(datumWkt);
        } else if (ImageDatum.ImageDatumKeyword.getKeywords().contains(datumWkt.getKeyword())) {
            return new ImageDatum(datumWkt);
        } else if (ParametricDatum.ParametricDatumKeywords.getKeywords().contains(datumWkt.getKeyword())) {
            return new ParametricDatum(datumWkt);
        } else if (TemporalDatum.TemporalDatumKeyword.getKeywords().contains(datumWkt.getKeyword())) {
            return new TemporalDatum(datumWkt);
        } else if (VerticalDatum.VerticalDatumKeyword.getKeywords().contains(datumWkt.getKeyword())) {
            return new VerticalDatum(datumWkt);
        } else {
            throw new RuntimeException();
        }
    }    
}
