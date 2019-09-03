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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author malapert
 */
public class Utils {

    public final static String TEXT_LATIN = "[\\w\\d\\[\\]\\(\\)\\{\\}<=>\\.,:;\\+\\-\\s#%&'\\*\\^\\?\\|°\"/\\\\]+";
    private static final String ISO_8601_PATTERN_1 = "yyyy-MM-dd'T'HH:mm:ssZ";
    private static final String ISO_8601_PATTERN_2 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final String ISO_8601_PATTERN_3 = "yyyy-MM-dd";
    private static final String ISO_8601_PATTERN_4 = "yyyy-MM";
    private static final String ISO_8601_PATTERN_5 = "yyyy";
    private static final String[] SUPPORTED_ISO_8601_PATTERNS = new String[]{ISO_8601_PATTERN_1,
        ISO_8601_PATTERN_2, ISO_8601_PATTERN_3, ISO_8601_PATTERN_4, ISO_8601_PATTERN_5};
    private static final int TICK_MARK_COUNT = 2;
    private static final int COLON_PREFIX_COUNT = "+00".length();

    public static String removeQuotes(final String keyword) {
        return keyword.replaceAll("\"", "");
    }

    public static String addQuotes(final String keyword) {
        return "\"".concat(keyword).concat("\"");
    }

    public static StringBuffer makeSpaces(String tab, int space) {
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < space; i++) {
            str = str.append(tab);
        }
        return str;
    }

    public static boolean isValidISO8601(final String dateStr) {
        String s = dateStr.replace("Z", "+00:00");
        for (String pattern : SUPPORTED_ISO_8601_PATTERNS) {
            String str = s;
            int colonPosition = pattern.lastIndexOf('Z') - TICK_MARK_COUNT + COLON_PREFIX_COUNT;
            if (str.length() > colonPosition) {
                str = str.substring(0, colonPosition) + str.substring(colonPosition + 1);
            }
            try {
                new SimpleDateFormat(pattern).parse(str);
                return true;
            } catch (final ParseException e) {
            }
        }
        return false;
    }

    public static int precision(final String number) {
        final int result;
        if(isNumeric(number)) {
            final int posComma = number.indexOf(".");
            final int e = number.indexOf(("e"));
            final int E = number.indexOf(("E"));
            if (e > 0) {
                result = e - posComma - 1;
            } else if (E > 0) {
                result = E - posComma - 1;
            } else {
                result = number.length() - posComma - 1;   
            }            
        } else {
            result = -1;
        }
        return result;
    }

    public static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

}
