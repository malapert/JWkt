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
import com.github.malapert.wkt.datum.TemporalDatum;
import java.util.List;

/**
 *
 * @author malapert
 */
public class DerivedTemporalCrs extends AbstractDerivatedCoordinateReferenceSystem {

    public DerivedTemporalCrs(final String crsName, final BaseTemporalCrs baseCrs, final DerivedConversion conversion, final CoordinateSystem cs) {
        setKeyword(TemporalCrs.TEMPORAL_CRS_KEYWORD);
        setCrsName(crsName);
        setBaseDerivatedCrs(baseCrs);
        setConversionFromBaseCrs(conversion);
        setCs(cs);
    }

    
    public DerivedTemporalCrs(final WktElt derivedTemporalCrsWkt) {
        parseDerivatedCrs(derivedTemporalCrsWkt);
    }

    protected DerivedTemporalCrs() {
    }
        
    @Override
    protected void parseSpecificWkt(WktElt crsWkt) {
        if(BaseTemporalCrs.BASE_TEMPORAL_CRS.equals(crsWkt.getKeyword())) {
            setBaseDerivatedCrs(new BaseTemporalCrs(crsWkt));
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
    public static class BaseTemporalCrs implements BaseCrs {

        private String baseCrsName;
        private TemporalDatum datum;

        public static final String BASE_TEMPORAL_CRS = "BASETIMECRS";

        public BaseTemporalCrs(WktElt datumWkt) {
            parse(datumWkt);
        }

        private void parse(final WktElt baseVerticalCrs) {
            WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
            List<WktElt> attributes = wktEltCollection.getAttributesFor(baseVerticalCrs, baseVerticalCrs.getKeyword());
            this.setBaseCrsName(attributes.get(0).getKeyword());
            List<WktElt> nodes = wktEltCollection.getNodesFor(baseVerticalCrs, baseVerticalCrs.getKeyword());
            for (WktElt node : nodes) {
                if (TemporalDatum.TemporalDatumKeyword.getKeywords().contains(node.getKeyword())) {
                    this.setDatum(new TemporalDatum(node));
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
        public TemporalDatum getDatum() {
            return datum;
        }

        /**
         * @param datum the datum to set
         */
        public void setDatum(TemporalDatum datum) {
            this.datum = datum;
        }

        @Override
        public StringBuffer toWkt(int deepLevel) {
            StringBuffer wkt = new StringBuffer();
            wkt = wkt.append(BASE_TEMPORAL_CRS).append(LEFT_DELIMITER);
            wkt = wkt.append("\n").append(Utils.makeSpaces(deepLevel + 1)).append(this.getBaseCrsName());
            wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel + 1)).append(this.getDatum().toWkt(deepLevel + 1));
            wkt = wkt.append("\n").append(Utils.makeSpaces(deepLevel)).append(RIGHT_DELIMITER);
            return wkt;
        }

    }

}
