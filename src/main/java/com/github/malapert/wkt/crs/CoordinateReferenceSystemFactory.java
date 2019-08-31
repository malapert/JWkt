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

import com.github.malapert.wkt.conversion.Conversion;
import com.github.malapert.wkt.conversion.DerivedConversion;
import com.github.malapert.wkt.conversion.MapProjection;
import com.github.malapert.wkt.crs.BaseDerivatedCrsFactory.BaseEngineeringCrs;
import com.github.malapert.wkt.crs.BaseDerivatedCrsFactory.BaseGeodeticCrs;
import com.github.malapert.wkt.crs.CoordinateReferenceSystem.CrsType;
import com.github.malapert.wkt.utils.Singleton;
import com.github.malapert.wkt.utils.WktElt;
import com.github.malapert.wkt.utils.WktEltCollection;
import com.github.malapert.wkt.cs.CoordinateSystem;
import com.github.malapert.wkt.datum.Datum;
import com.github.malapert.wkt.datum.EngineeringDatum;
import com.github.malapert.wkt.datum.GeodeticDatum;
import com.github.malapert.wkt.datum.ImageDatum;
import com.github.malapert.wkt.datum.ParametricDatum;
import com.github.malapert.wkt.datum.TemporalDatum;
import com.github.malapert.wkt.datum.VerticalDatum;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author malapert
 */
public abstract class CoordinateReferenceSystemFactory {

    public static CoordinateReferenceSystem create(final String crsName, final Datum datum, final CoordinateSystem cs) {
        if (datum instanceof EngineeringDatum) {
            return new EngineeringCrs(EngineeringCrs.EngineeringCrsKeyword.ENGCRS, crsName, (EngineeringDatum) datum, cs);
        } else if (datum instanceof GeodeticDatum) {
            return new GeodeticCrs(GeodeticCrs.GeodeticCrsKeyword.GEODCRS, crsName, (GeodeticDatum) datum, cs);
        } else if (datum instanceof ImageDatum) {
            return new ImageCrs(crsName, (ImageDatum) datum, cs);
        } else if (datum instanceof ParametricDatum) {
            return new ParametricCrs(crsName, (ParametricDatum) datum, cs);
        } else if (datum instanceof TemporalDatum) {
            return new TemporalCrs(crsName, (TemporalDatum) datum, cs);
        } else if (datum instanceof VerticalDatum) {
            return new VerticalCrs(VerticalCrs.VerticalKeyword.VERTCRS, crsName, (VerticalDatum) datum, cs);
        } else {
            throw new RuntimeException();
        }
    }
    
    public static DerivedCoordinateReferenceSystem create(final String crsName, final BaseCrs baseCrs, final Conversion conversion, final CoordinateSystem cs) {
        if (baseCrs.getDatum() instanceof GeodeticDatum && conversion instanceof MapProjection) {
            return new ProjectedCrs(ProjectedCrs.ProjectedCrsKeyword.PROJCRS, crsName, (BaseGeodeticCrs) baseCrs, (MapProjection)conversion, cs);
        } else if (baseCrs.getDatum() instanceof EngineeringDatum) {
            return new DerivedEngineeringCrs(EngineeringCrs.EngineeringCrsKeyword.ENGCRS, crsName, (BaseEngineeringCrs) baseCrs, (DerivedConversion) conversion, cs);
        } else if (baseCrs.getDatum() instanceof GeodeticDatum) {
            return new DerivedGeodeticCrs(GeodeticCrs.GeodeticCrsKeyword.GEODCRS, crsName, (BaseGeodeticCrs) baseCrs, (DerivedConversion) conversion, cs);
        } else if (baseCrs.getDatum() instanceof ParametricDatum) {
            return new DerivedParametricCrs(crsName, (DerivedParametricCrs.BaseParametricCrs) baseCrs, (DerivedConversion) conversion, cs);
        } else if (baseCrs.getDatum() instanceof TemporalDatum) {
            return new DerivedTemporalCrs(crsName, (DerivedTemporalCrs.BaseTemporalCrs) baseCrs, (DerivedConversion) conversion, cs);
        } else if (baseCrs.getDatum() instanceof VerticalDatum) {
            return new DerivedVerticalCrs(VerticalCrs.VerticalKeyword.VERTCRS, crsName, (DerivedVerticalCrs.BaseVerticalCrs) baseCrs, (DerivedConversion) conversion, cs);
        } else {
            throw new RuntimeException();
        }
    }    

    public static <T extends CoordinateReferenceSystem> T create(final Class<T> crsClass) {
        CrsType[] crsTypeArray = CoordinateReferenceSystem.CrsType.values();
        for (CrsType crsType : crsTypeArray) {
            try {
                Class<?> clazz = Class.forName(crsType.getCrsClassName());
                if(clazz.isAssignableFrom(crsClass)) {
                    return (T) clazz.newInstance();
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(CoordinateReferenceSystemFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        throw new RuntimeException();
    }

    public static <T extends CoordinateReferenceSystem> T createFromWkt(final WktElt crsWkt, final Class<T> crsClass) {
        return (T) createFromWkt(crsWkt);
    }
    
    public static CoordinateReferenceSystem createFromWkt(final WktElt crsWkt) {
        CoordinateReferenceSystem.CrsType crsType = CoordinateReferenceSystem.CrsType.findCRSFor(crsWkt.getKeyword(),crsWkt.isDerivated());
        try {
            Class<?> clazz = Class.forName(crsType.getCrsClassName());
            Constructor<?> constructor = clazz.getConstructor(WktElt.class);
            return (CoordinateReferenceSystem) constructor.newInstance(crsWkt);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException();
        }

    }

    public static CoordinateReferenceSystem createFromWkt(final String crsWkt) {
        ParserWkt parser = new ParserWkt(crsWkt);
        WktEltCollection wktEltCollection = parser.createsWktIndex();
        Singleton.getInstance().setCollection(wktEltCollection);
        Iterator<WktElt> iter = wktEltCollection.iterator();
        if (!iter.hasNext()) {
            throw new RuntimeException();
        }
        return createFromWkt(iter.next());

    }
    
    public static <T extends CoordinateReferenceSystem> T createFromWkt(final String crsWkt, final Class<T> crsClass) {
        return (T) createFromWkt(crsWkt);
    }
    

    public static class ParserWkt {

        private String wktDescription;

        public ParserWkt(final String wktDescription) {
            this.wktDescription = wktDescription;
        }

        /**
         * Creates the WKT index based on the WKT description.
         *
         * @return the WKT index based on the WKT description
         */
        public WktEltCollection createsWktIndex() {
            boolean isDerived = false;
            WktEltCollection wktCollection = new WktEltCollection();
            /* Creating Stack */
            Stack<Integer> stk = new Stack<>();
            int len = wktDescription.length();
            boolean isDoubleQuoteisOpen = false;
            for (int i = 0; i < len; i++) {
                char ch = wktDescription.charAt(i);
                // we detect the left delimiter - this delimiter must be not in double quote
                if (ch == '[' && !isDoubleQuoteisOpen) {
                    String keyword = scanPreviousCharsForKeyword(i, wktDescription);
                    WktElt wkt = new WktElt(keyword.toUpperCase(), i + 1, WktElt.WktType.NODE);
                    if (DerivedConversion.DERIVED_CONVERSION_KEYWORD.equals(keyword.toUpperCase())
                            || MapProjection.MAP_PROJECTION_KEYWORD.equals(keyword.toUpperCase())) {
                        isDerived = true;
                    }
                    wktCollection.addWktElt(wkt);
                    stk.push(i);
                    // we detect the right delimiter - this delimiter must be not in double quote                
                } else if (ch == ']' && !isDoubleQuoteisOpen) {
                    try {
                        int p = stk.pop();
                        WktElt wkt = wktCollection.wktEltStartingAt(p + 1);
                        wkt.setStop(i);
                        scanPreviousCharsForAttributes(i, wktDescription, isDoubleQuoteisOpen, wktCollection);
                    } catch (Exception e) {
                        //']' at index (i + 1) is unmatched
                        throw new RuntimeException("']' at index " + (i + 1) + " is unmatched");
                    }
                    // we detect the wkt separator - this delimiter must be not in double quote                
                } else if (ch == ',' && !isDoubleQuoteisOpen) {
                    scanPreviousCharsForAttributes(i, wktDescription, isDoubleQuoteisOpen, wktCollection);
                    // we detect the double quote in order to know if we are at the
                    // beginning or the ending of the double quote
                } else if (ch == '"') {
                    isDoubleQuoteisOpen = (isDoubleQuoteisOpen == false);
                }
            }
            wktCollection.getCollection().get(0).setDerivated(isDerived);
            // we set the node of each found element .
            wktCollection.index();
            return wktCollection;
        }

        /**
         * Scans the previous chars to find the keyword.
         *
         * This keyword is the node of the WKT description (e.g CS, DATUM, ...).
         *
         * @param currentIndex current index
         * @param wkt the WKT description
         * @return the keyword
         */
        private String scanPreviousCharsForKeyword(int currentIndex, final String wkt) {
            int j = currentIndex - 1;
            String keyword = null;
            while (j >= 0) {
                char prevChar = wkt.charAt(j);
                if (prevChar == ',') {
                    keyword = wkt.substring(j + 1, currentIndex).trim();
                    break;
                } else if (j == 0) {
                    keyword = wkt.substring(0, currentIndex).trim();
                    break;
                }
                j--;
            }
            return keyword;
        }

        /**
         * Scans the previous chars to get attributes.
         *
         * @param currentIndex the current index on the WKT description
         * @param wkt the WKT description
         * @param isDoubleQuoteisOpen True when the double quote is opened,
         * False when it is closed
         * @param wktCollection the WKT structure where the index is saved.
         */
        private void scanPreviousCharsForAttributes(int currentIndex, String wkt, boolean isDoubleQuoteisOpen, WktEltCollection wktCollection) {
            int j = currentIndex - 1;
            while (j >= 0) {
                char prevChar = wkt.charAt(j);
                if (prevChar == '"') {
                    isDoubleQuoteisOpen = (isDoubleQuoteisOpen == false);
                }
                if (isDoubleQuoteisOpen) {
                    j--;
                    continue;
                } else if (prevChar == ']') {
                    break;
                } else if (prevChar == ',') {
                    String attribute = wkt.substring(j + 1, currentIndex).trim();
                    WktElt wktElt = new WktElt(attribute, j + 1, WktElt.WktType.ATTRIBUTE);
                    wktElt.setStop(currentIndex);
                    wktCollection.addWktElt(wktElt);
                    break;
                } else if (prevChar == '[') {
                    String attribute = wkt.substring(j + 1, currentIndex).trim();
                    WktElt wktElt = new WktElt(attribute, j + 1, WktElt.WktType.ATTRIBUTE);
                    wktElt.setStop(currentIndex);
                    wktCollection.addWktElt(wktElt);
                    break;
                } else if (j == 0) {
                    break;
                }
                j--;
            }
        }

    }

}
