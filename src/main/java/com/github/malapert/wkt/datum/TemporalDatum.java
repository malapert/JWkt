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
package com.github.malapert.wkt.datum;

import com.github.malapert.wkt.utils.Singleton;
import com.github.malapert.wkt.utils.Utils;
import com.github.malapert.wkt.metadata.WktDescription;
import com.github.malapert.wkt.utils.WktElt;
import com.github.malapert.wkt.utils.WktEltCollection;
import com.github.malapert.wkt.metadata.Identifier;
import java.util.Arrays;
import java.util.List;

/**
 * <temporal datum>::=<temporal datum keyword> <left delimiter> <datum name> [
 * <wkt separator> <temporal origin> ] [ { <wkt separator> <identifier> } ]…
 *  <right delimiter>
 *
 * @author malapert
 */
public final class TemporalDatum extends AbstractDatum {

    public enum TemporalDatumKeyword {
        TDATUM, TIMEDATUM;

        private TemporalDatumKeyword() {
        }

        public static List<String> getKeywords() {
            return Arrays.asList(TemporalDatumKeyword.TDATUM.name(), TemporalDatumKeyword.TIMEDATUM.name());
        }
    }

    private TemporalOrigin temporalOrigin;

    public TemporalDatum(final TemporalDatumKeyword keyword, final String datumName) {
        setKeyword(keyword.name());
        setDatumName(datumName);
    }

    public TemporalDatum(final WktElt temporalDatumWkt) {
        parse(temporalDatumWkt);
    }

    @Override
    protected void parse(final WktElt temporalDatumWkt) {
        setKeyword(temporalDatumWkt.getKeyword());
        final WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();        
        final List<WktElt> attributes = wktEltCollection.getAttributesFor(temporalDatumWkt, temporalDatumWkt.getKeyword());
        this.setDatumName(Utils.removeQuotes(attributes.get(0).getKeyword()));
        final List<WktElt> nodes = wktEltCollection.getNodesFor(temporalDatumWkt, temporalDatumWkt.getKeyword());
        for (final WktElt node : nodes) {
            switch (node.getKeyword()) {
                case TemporalOrigin.TEMPORAL_DATUM_KEYWORD:
                    this.setTemporalOrigin(new TemporalOrigin(node));
                    break;
                case Identifier.IDENTIFIER_KEYWORD:
                    this.getIdentifierList().add(new Identifier(node));
                    break;
                default:
                    throw new RuntimeException();
            }
        }
    }

    /**
     * @return the temporalOrigin
     */
    public TemporalOrigin getTemporalOrigin() {
        return temporalOrigin;
    }

    /**
     * @param temporalOrigin the temporalOrigin to set
     */
    public void setTemporalOrigin(TemporalOrigin temporalOrigin) {
        this.temporalOrigin = temporalOrigin;
    }

    @Override
    public StringBuffer toWkt(final String endLine, final String tab, int deepLevel) {
        StringBuffer wkt = new StringBuffer();
        wkt = wkt.append(this.keyword).append(LEFT_DELIMITER);
        wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(Utils.addQuotes(this.getDatumName()));
        wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(this.getTemporalOrigin().toWkt(endLine, tab, deepLevel + 1));
        for (Identifier id : this.getIdentifierList()) {
            wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(id.toWkt(endLine, tab, deepLevel + 1));
        }
        wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel)).append(RIGHT_DELIMITER);
        return wkt;
    }
        
   /**
     * <temporal origin >::=<temporal origin keyword> <left delimiter>
     * <temporal origin description> <right delimiter>
     *
     * @author malapert
     */
    public static class TemporalOrigin implements WktDescription {

        public static final String TEMPORAL_DATUM_KEYWORD = "TIMEORIGIN";
        private String description;

        public TemporalOrigin(final String description) {
            this.description = description;
        }

        public TemporalOrigin(final WktElt temporalOriginWkt) {
            parse(temporalOriginWkt);
        }

        private void parse(final WktElt temporalOriginWkt) {
            WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
            List<WktElt> attributes = wktEltCollection.getAttributesFor(temporalOriginWkt, temporalOriginWkt.getKeyword());
            this.description = Utils.removeQuotes(attributes.get(0).getKeyword());
        }

        @Override
        public StringBuffer toWkt(final String endLine, final String tab, int deepLevel) {
            StringBuffer wkt = new StringBuffer();
            wkt = wkt.append(TEMPORAL_DATUM_KEYWORD).append(LEFT_DELIMITER);
            wkt = wkt.append(Utils.addQuotes(this.description));
            wkt = wkt.append(RIGHT_DELIMITER);
            return wkt;
        }        

        @Override
        public StringBuffer toWkt() {
            return toWkt("\n", "   ", 0);
        }
    }
}
