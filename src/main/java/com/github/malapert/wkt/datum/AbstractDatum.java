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
import com.github.malapert.wkt.utils.WktElt;
import com.github.malapert.wkt.utils.WktEltCollection;
import com.github.malapert.wkt.metadata.Identifier;
import com.github.malapert.wkt.metadata.WktDescription;
import static com.github.malapert.wkt.metadata.WktDescription.LEFT_DELIMITER;
import static com.github.malapert.wkt.metadata.WktDescription.RIGHT_DELIMITER;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author malapert
 */
public abstract class AbstractDatum implements Datum {

    protected String keyword = null;
    private String datumName = null;
    private Anchor anchor = null;
    private List<Identifier> identifierList = new ArrayList<>();

    /**
     * @return the datumName
     */
    @Override
    public final String getDatumName() {
        return datumName;
    }

    /**
     * @param datumName the datumName to set
     */
    public final void setDatumName(final String datumName) {
        this.datumName = datumName;
    }

    /**
     * @return the anchor
     */
    @Override
    public final Anchor getAnchor() {
        return anchor;
    }

    /**
     * @param anchor the anchor to set
     */
    public final void setAnchor(final Anchor anchor) {
        this.anchor = anchor;
    }

    /**
     * @return the identifierList
     */
    @Override
    public final List<Identifier> getIdentifierList() {
        return identifierList;
    }

    /**
     * @param identifierList the identifierList to set
     */
    public final void setIdentifierList(final List<Identifier> identifierList) {
        this.identifierList = identifierList;
    }

    /**
     * @return the keyword
     */
    public final String getKeyword() {
        return keyword;
    }

    /**
     * @param keyword the keyword to set
     */
    public final void setKeyword(final String keyword) {
        this.keyword = keyword;
    }

    protected void parse(final WktElt datumWkt) {
        setKeyword(datumWkt.getKeyword());
        final WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
        final List<WktElt> attributes = wktEltCollection.getAttributesFor(datumWkt, this.getKeyword());
        this.setDatumName(Utils.removeQuotes(attributes.get(0).getKeyword()));
        final List<WktElt> nodes = wktEltCollection.getNodesFor(datumWkt, this.getKeyword());
        for (final WktElt node : nodes) {
            switch (node.getKeyword()) {
                case Anchor.KEYWORD_ANCHOR:
                    this.setAnchor(new Anchor(node));
                    break;
                case Identifier.IDENTIFIER_KEYWORD:
                    this.getIdentifierList().add(new Identifier(datumWkt));
                    break;
                default:
                    throw new RuntimeException();
            }
        }
    }

    @Override
    public StringBuffer toWkt(final String endLine, final String tab, int deepLevel) {
        StringBuffer wkt = new StringBuffer();
        wkt = wkt.append(this.getKeyword()).append(LEFT_DELIMITER);
        wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(Utils.addQuotes(this.getDatumName()));
        if (getAnchor() != null) {
            wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(getAnchor().toWkt(endLine, tab, deepLevel + 1));
        }
        for (Identifier id : this.getIdentifierList()) {
            wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel + 1)).append(id.toWkt(endLine, tab, deepLevel + 1));
        }
        wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel)).append(RIGHT_DELIMITER);
        return wkt;
    }

    @Override
    public StringBuffer toWkt() {
        return toWkt("\n", "   ", 0);
    }

    /**
     * Defines an Anchor.
     *
     * <pre>
     * {@code
     * <datum anchor>::=<datum anchor keyword> <left delimiter> <datum anchor description> <right delimiter>
     * }
     * </pre>
     *
     * @author Jean-Christophe Malapert
     */
    public static class Anchor implements WktDescription {

        public static final String KEYWORD_ANCHOR = "ANCHOR";
        private String description;

        /**
         * Creates an anchor based on its description element.
         *
         * @param description
         */
        public Anchor(final String description) {
            this.description = description;
        }

        /**
         * Creates an anchor based on ANCHOR WKT element.
         *
         * @param datumElts
         */
        public Anchor(final WktElt datumElts) {
            parse(datumElts);
        }

        /**
         * Parses the ANCHOR WKT element.
         *
         * @param datumElts the ANCHOR WKT element
         */
        private void parse(final WktElt datumElts) {
            final WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
            final List<WktElt> attributes = wktEltCollection.getAttributesFor(datumElts, KEYWORD_ANCHOR);
            this.setDescription(Utils.removeQuotes(attributes.get(0).getKeyword()));
        }

        /**
         * Returns the description.
         *
         * @return the description
         */
        public String getDescription() {
            return description;
        }

        /**
         * Sets the description.
         *
         * @param description the description to set
         */
        public void setDescription(final String description) {
            this.description = description;
        }

        @Override
        public StringBuffer toWkt(final String endLine, final String tab, int deepLevel) {
            StringBuffer wkt = new StringBuffer();
            wkt = wkt.append("ANCHOR").append(LEFT_DELIMITER);
            wkt = wkt.append(Utils.addQuotes(getDescription()));
            wkt = wkt.append(RIGHT_DELIMITER);
            return wkt;
        }

        @Override
        public StringBuffer toWkt() {
            return toWkt("\n", "   ", 0);
        }

    }

}
