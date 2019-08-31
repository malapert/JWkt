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

import com.github.malapert.wkt.conversion.DerivedConversion;
import com.github.malapert.wkt.cs.CoordinateSystem;
import com.github.malapert.wkt.utils.Singleton;
import com.github.malapert.wkt.utils.Utils;
import static com.github.malapert.wkt.metadata.WktDescription.LEFT_DELIMITER;
import static com.github.malapert.wkt.metadata.WktDescription.RIGHT_DELIMITER;
import static com.github.malapert.wkt.metadata.WktDescription.WKT_SEPARATOR;
import com.github.malapert.wkt.utils.WktElt;
import com.github.malapert.wkt.utils.WktEltCollection;
import com.github.malapert.wkt.datum.ParametricDatum;
import java.util.List;

/**
 *
 * @author malapert
 */
public class DerivedParametricCrs extends AbstractDerivatedCoordinateReferenceSystem {

    public DerivedParametricCrs(final String crsName, final BaseParametricCrs baseCrs, final DerivedConversion conversion, final CoordinateSystem cs) {
        setKeyword(ParametricCrs.PARAMETRIC_KEYWORD);
        setCrsName(crsName);
        setBaseDerivatedCrs(baseCrs);
        setConversionFromBaseCrs(conversion);
        setCs(cs);
    }
    
    public DerivedParametricCrs(WktElt derivedParametricCrs) {
        parseDerivatedCrs(derivedParametricCrs);
    }

    protected DerivedParametricCrs() {
    }

    @Override
    protected void parseSpecificWkt(WktElt crsWkt) {
        if(ParametricCrs.PARAMETRIC_KEYWORD.equals(crsWkt.getKeyword())) {
            setBaseDerivatedCrs(new BaseParametricCrs(crsWkt));
        } else {
            throw new RuntimeException();
       }
    }

    @Override
    protected boolean hasSpecificParsing() {
        return true;
    } 

    /**
     *
     * @author malapert
     */
    public static class BaseParametricCrs implements BaseCrs {

        private String baseCrsName;
        private ParametricDatum datum;

        public static final String BASE_PARAMETRIC_CRS_KEYWORD = "BASEPARAMCRS";

        public BaseParametricCrs(WktElt datumWkt) {
            parse(datumWkt);
        }

        private void parse(final WktElt baseVerticalCrs) {
            WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
            List<WktElt> attributes = wktEltCollection.getAttributesFor(baseVerticalCrs, BASE_PARAMETRIC_CRS_KEYWORD);
            this.setBaseCrsName(attributes.get(0).getKeyword());
            List<WktElt> nodes = wktEltCollection.getNodesFor(baseVerticalCrs, BASE_PARAMETRIC_CRS_KEYWORD);
            for (WktElt node : nodes) {
                if (ParametricDatum.ParametricDatumKeywords.getKeywords().contains(node.getKeyword())) {
                    this.setDatum(new ParametricDatum(baseVerticalCrs));
                } else {
                    throw new RuntimeException();
                }
            }
        }

        /**
         * @return the baseCrsName
         */
        @Override
        public String getBaseCrsName() {
            return baseCrsName;
        }

        /**
         * @param baseCrsName the baseCrsName to set
         */
        public void setBaseCrsName(String baseCrsName) {
            this.baseCrsName = baseCrsName;
        }

        /**
         * @return the datum
         */
        @Override
        public ParametricDatum getDatum() {
            return datum;
        }

        /**
         * @param datum the datum to set
         */
        public void setDatum(ParametricDatum datum) {
            this.datum = datum;
        }

        @Override
        public StringBuffer toWkt(int deepLevel) {
            StringBuffer wkt = new StringBuffer();
            wkt = wkt.append(BASE_PARAMETRIC_CRS_KEYWORD).append(LEFT_DELIMITER);
            wkt = wkt.append("\n").append(Utils.makeSpaces(deepLevel + 1)).append(this.getBaseCrsName());
            wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel + 1)).append(this.getDatum().toWkt(deepLevel + 1));
            wkt = wkt.append("\n").append(Utils.makeSpaces(deepLevel)).append(RIGHT_DELIMITER);
            return wkt;
        }
    }

}
