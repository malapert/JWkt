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

/**
 *
 * @author malapert
 */
public class Utils {

    public final static String TEXT_LATIN = "[\\w\\d\\[\\]\\(\\)\\{\\}<=>\\.,:;\\+\\-\\s#%&'\\*\\^\\?\\|°\"/\\\\]+";

    public static String removeQuotes(String keyword) {
        return keyword.replaceAll("\"", "");
    }
    
    public static StringBuffer makeSpaces(int space) {
        StringBuffer str = new StringBuffer();
        for(int i=0;i<space;i++) {
            str = str.append("   ");
        }
        return str;
    }

}
