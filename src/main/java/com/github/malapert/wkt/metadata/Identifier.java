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
package com.github.malapert.wkt.metadata;

import com.github.malapert.wkt.utils.Singleton;
import com.github.malapert.wkt.utils.Utils;
import com.github.malapert.wkt.utils.WktElt;
import com.github.malapert.wkt.utils.WktEltCollection;
import static com.github.malapert.wkt.metadata.Identifier.Citation.AUTHORITY_CITATION_KEYWORD;
import static com.github.malapert.wkt.metadata.Identifier.URI.URI_KEYWORD;
import java.util.List;

/**
 * Identifier is an optional attribute which references an external description 
 * of the object and which may be applied to a coordinate reference system, a 
 * coordinate operation or a boundCRS. 
 * 
 * It may also be utilised for components of these objects although this is not 
 * recommended except for coordinate operation methods 
 * (including map projections) and parameters. Multiple identifiers may be given
 * for any object.
 * 
 * <p>
 * When an identifier is given for a coordinate reference system, coordinate 
 * operation or boundCRS, it applies to the whole object including all of its 
 * components.
 * 
 * <p>
 * <pre>
 * {@code
 * <identifier>	::= <identifier keyword> <left delimiter> <authority name> <wkt separator>
 * <authority unique identifier> [ <wkt separator> <version> ] [ <wkt separator>
 * <authority citation> ] [ <wkt separator> <id uri> ]
 * <right delimiter>
 * }
 * </pre>
 *
 * @author Jean-Christophe Malapert
 */
public class Identifier implements WktDescription {

    public final static String IDENTIFIER_KEYWORD = "ID";

    private String authorityName;
    private String authorityUniqueIdentifier;
    private String version;
    private Citation citation;
    private URI uri;

    /**
     * Constructs the Identifier WKT element based on required parameters.
     * @param authorityName
     * @param authorityUniqueIdentifier 
     */
    public Identifier(final String authorityName, final String authorityUniqueIdentifier) {
        this.authorityName = authorityName;
        this.authorityUniqueIdentifier = authorityUniqueIdentifier;
    }
    
    /**
     * Constructs the Identifier WKT element by parsing the WKT element.
     * @param identifierWktElts the Identifier WKT element
     */
    public Identifier(final WktElt identifierWktElts) {
        parse(identifierWktElts);
    }

    /**
     * Parses the Identifier WKT element.
     * @param identifierWktElts the Identifier WKT element
     */
    private void parse(final WktElt identifierWktElts) {
        WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
        List<WktElt> attributes = wktEltCollection.getAttributesFor(identifierWktElts, IDENTIFIER_KEYWORD);
        this.setAuthorityName(attributes.get(0).getKeyword());
        this.setAuthorityUniqueIdentifier(attributes.get(1).getKeyword());
        this.setVersion((attributes.size() == 3) ? attributes.get(2).getKeyword() : null);

        List<WktElt> nodes = wktEltCollection.getNodesFor(identifierWktElts, IDENTIFIER_KEYWORD);
        for (WktElt node : nodes) {
            switch (node.getKeyword()) {
                case AUTHORITY_CITATION_KEYWORD:
                    this.setCitation(new Citation(node));
                    break;
                case URI_KEYWORD:
                    this.setUri(new URI(node));
                    break;
                default:
                    throw new RuntimeException();
            }
        }
    }

    @Override
    public StringBuffer toWkt(int deepLevel) {
        StringBuffer wkt = new StringBuffer();
        wkt = wkt.append(IDENTIFIER_KEYWORD).append(LEFT_DELIMITER);
        wkt = wkt.append("\n").append(Utils.makeSpaces(deepLevel+1)).append(getAuthorityName());
        wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel+1)).append(getAuthorityUniqueIdentifier());
        if (getVersion() != null) {
            wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel+1)).append(getVersion());
        }
        if (getCitation() != null) {
            wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel+1)).append(getCitation().toWkt());
        }
        if (getUri() != null) {
            wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel+1)).append(getUri().toWkt());
        }
        wkt = wkt.append("\n").append(Utils.makeSpaces(deepLevel)).append(RIGHT_DELIMITER);
        return wkt;
    }

    /**
     * Returns the authority name.
     * @return the authorityName
     */
    public String getAuthorityName() {
        return authorityName;
    }

    /**
     * Sets the authority name.
     * @param authorityName the authorityName to set
     */
    public void setAuthorityName(final String authorityName) {
        this.authorityName = authorityName;
    }

    /**
     * Returns the authority unique identifier.
     * @return the authorityUniqueIdentifier
     */
    public String getAuthorityUniqueIdentifier() {
        return authorityUniqueIdentifier;
    }

    /**
     * Sets the authority unique identifier.
     * @param authorityUniqueIdentifier the authorityUniqueIdentifier to set
     */
    public void setAuthorityUniqueIdentifier(final String authorityUniqueIdentifier) {
        this.authorityUniqueIdentifier = authorityUniqueIdentifier;
    }

    /**
     * Returns the version.
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the version.
     * @param version the version to set
     */
    public void setVersion(final String version) {
        this.version = version;
    }

    /**
     * Returns the citation.
     * @return the citation
     */
    public Citation getCitation() {
        return citation;
    }

    /**
     * Sets the citation.
     * @param citation the citation to set
     */
    public void setCitation(final Citation citation) {
        this.citation = citation;
    }

    /**
     * Returns the URI.
     * @return the uri
     */
    public URI getUri() {
        return uri;
    }

    /**
     * Sets the URI.
     * @param uri the uri to set
     */
    public void setUri(final URI uri) {
        this.uri = uri;
    }

    public static class Citation {

        public final static String AUTHORITY_CITATION_KEYWORD = "CITATION";
        
        private String description;

        public Citation(final String description) {
            this.description = description;
        }
        
        public Citation(final WktElt citationWkt) {
            WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
            List<WktElt> attributes = wktEltCollection.getAttributesFor(citationWkt, AUTHORITY_CITATION_KEYWORD);
            this.description = attributes.get(0).getKeyword();
        }

        public StringBuffer toWkt() {
            StringBuffer wkt = new StringBuffer();
            wkt = wkt.append(AUTHORITY_CITATION_KEYWORD).append(LEFT_DELIMITER);
            wkt = wkt.append(this.description);
            wkt = wkt.append(RIGHT_DELIMITER);
            return wkt;
        }
    }

    public static class URI {
        
        public final static String URI_KEYWORD = "URI";       

        private String description;
        
        public URI(final String description) {
            this.description = description;
        }

        public URI(final WktElt uriWkt) {
            WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
            List<WktElt> attributes = wktEltCollection.getAttributesFor(uriWkt, URI_KEYWORD);
            this.description = attributes.get(0).getKeyword();            
        }

        public StringBuffer toWkt() {
            StringBuffer wkt = new StringBuffer();
            wkt = wkt.append(URI_KEYWORD).append(LEFT_DELIMITER);
            wkt = wkt.append(this.description);
            wkt = wkt.append(RIGHT_DELIMITER);
            return wkt;
        }
    }
}
