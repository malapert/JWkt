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
import com.github.malapert.wkt.crs.VerticalCrs.VerticalKeyword;
import com.github.malapert.wkt.cs.CoordinateSystem;
import com.github.malapert.wkt.utils.Singleton;
import com.github.malapert.wkt.utils.Utils;
import static com.github.malapert.wkt.metadata.WktDescription.LEFT_DELIMITER;
import static com.github.malapert.wkt.metadata.WktDescription.RIGHT_DELIMITER;
import static com.github.malapert.wkt.metadata.WktDescription.WKT_SEPARATOR;
import com.github.malapert.wkt.utils.WktElt;
import com.github.malapert.wkt.utils.WktEltCollection;
import com.github.malapert.wkt.datum.VerticalDatum;
import java.util.List;

/**
 *
 * <derived vertical crs>::=<vertical crs keyword> <left delimiter>
 * <derived crs name> <wkt separator> <base vertical crs> <wkt separator>
 * <deriving conversion> <wkt separator> <coordinate system>
 * <scope extent identifier remark> <right delimiter>
 *
 * @author malapert
 */
public class DerivedVerticalCrs extends AbstractDerivatedCoordinateReferenceSystem {
    
    public DerivedVerticalCrs(final VerticalKeyword keyword, final String crsName, final BaseVerticalCrs baseCrs, final DerivedConversion conversion, final CoordinateSystem cs) {
        setKeyword(keyword.name());
        setCrsName(crsName);
        setBaseDerivatedCrs(baseCrs);
        setConversionFromBaseCrs(conversion);
        setCs(cs);
    }

    public DerivedVerticalCrs(final WktElt derivedVerticalCrs) {
        parseDerivatedCrs(derivedVerticalCrs);
    }

    protected DerivedVerticalCrs() {
    }

    @Override
    protected void parseDerivatedCrs(WktElt crsWkt) {
        if(BaseVerticalCrs.BASE_VERTICAL_CRS_KEYWORD.equals(crsWkt.getKeyword())) {
            setBaseDerivatedCrs(new BaseVerticalCrs(crsWkt));
        } else {
            super.parseDerivatedCrs(crsWkt); 
        }
    }
    

    /**
     * <base vertical crs>::=<base vertical crs keyword> <left delimiter>
     * <base crs name> <wkt separator> <vertical datum> <right delimiter>
     *
     * @author malapert
     */
    public class BaseVerticalCrs implements BaseCrs {

        public static final String BASE_VERTICAL_CRS_KEYWORD = "BASEVERTCRS";
        private String baseCrsName;
        private VerticalDatum datum;

        public BaseVerticalCrs(final WktElt baseVerticalCrs) {
            parse(baseVerticalCrs);
        }

        private void parse(final WktElt baseVerticalCrs) {
            WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
            List<WktElt> attributes = wktEltCollection.getAttributesFor(baseVerticalCrs, baseVerticalCrs.getKeyword());
            this.setBaseCrsName(attributes.get(0).getKeyword());
            List<WktElt> nodes = wktEltCollection.getNodesFor(baseVerticalCrs, baseVerticalCrs.getKeyword());
            for (WktElt node : nodes) {
                if (VerticalDatum.VerticalDatumKeyword.getKeywords().contains(node.getKeyword())) {
                    this.setDatum(new VerticalDatum(node));
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
        public VerticalDatum getDatum() {
            return datum;
        }

        /**
         * @param datum the datum to set
         */
        public void setDatum(VerticalDatum datum) {
            this.datum = datum;
        }

        @Override
        public StringBuffer toWkt(int deepLevel) {
            StringBuffer wkt = new StringBuffer();
            wkt = wkt.append(BASE_VERTICAL_CRS_KEYWORD).append(LEFT_DELIMITER);
            wkt = wkt.append("\n").append(Utils.makeSpaces(deepLevel + 1)).append(this.getBaseCrsName());
            wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel + 1)).append(this.getDatum().toWkt(deepLevel + 1));
            wkt = wkt.append("\n").append(Utils.makeSpaces(deepLevel)).append(RIGHT_DELIMITER);
            return wkt;
        }
    }
}
