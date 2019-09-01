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
package com.github.malapert.wkt.utils;

/**
 *
 * @author malapert
 */
public class Utils {

    public final static String TEXT_LATIN = "[\\w\\d\\[\\]\\(\\)\\{\\}<=>\\.,:;\\+\\-\\s#%&'\\*\\^\\?\\|°\"/\\\\]+";

    public static String removeQuotes(final String keyword) {
        return keyword.replaceAll("\"", "");
    }
    
    public static String addQuotes(final String keyword) {
        return "\"".concat(keyword).concat("\"");
    }
    
    public static StringBuffer makeSpaces(String tab, int space) {
        StringBuffer str = new StringBuffer();
        for(int i=0;i<space;i++) {
            str = str.append(tab);
        }
        return str;
    }

}
