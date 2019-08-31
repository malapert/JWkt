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
import java.util.Arrays;
import java.util.List;

/**
 * <parametric datum>::=<parametric datum keyword> <left delimiter> <datum name>
 * [ <wkt separator> <datum anchor> ] [ { <wkt separator> <identifier> } ]…
 *  <right delimiter>
 *
 * @author malapert
 */
public class ParametricDatum extends AbstractDatum {

    public enum ParametricDatumKeywords {
        PDATUM,
        PARAMETRICDATUM;

        private ParametricDatumKeywords() {
        }

        public static List<String> getKeywords() {
            return Arrays.asList(ParametricDatumKeywords.PARAMETRICDATUM.name(), ParametricDatumKeywords.PDATUM.name());
        }
    }

    public ParametricDatum(final ParametricDatumKeywords keyword, final String datumName) {
        setKeyword(keyword.name());
        setDatumName(datumName);
    }
    
    public ParametricDatum(WktElt parametricDatumWkt) {
        parse(parametricDatumWkt);
    }
    
}
