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
package com.github.malapert.wkt.method;

import com.github.malapert.wkt.utils.WktElt;
import java.util.Arrays;
import java.util.List;

/**
 *
 * <map projection method keyword> <left delimiter>
 * <map projection method name>
 * [ { <wkt separator> <identifier> } ]… <right delimiter>
 *
 * @author malapert
 */
public class MapProjectionMethod extends AbstractMethod {

    public enum MapMethod {
        METHOD,
        PROJECTION;

        private MapMethod() {
        }

        public static List<String> getKeywords() {
            return Arrays.asList(MapMethod.METHOD.name(), MapMethod.PROJECTION.name());
        }
    }
    
    public MapProjectionMethod(final MapMethod mapMethod, final String name) {
        setKeyword(mapMethod.name());
        setMethodName(name);
    }

    public MapProjectionMethod(final WktElt mapProjectionMethodwktElts) {
        parse(mapProjectionMethodwktElts);
    }

}
