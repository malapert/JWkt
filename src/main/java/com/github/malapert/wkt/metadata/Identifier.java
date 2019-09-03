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
 * coordinate operation or a bound CRS. 
 * 
 * It may also be utilised for components of these objects although this is 
 * recommended only for the following circumstances:
 * <ul>
 * <li>coordinate operation methods and parameters;</li>
 * <li>source and target CRSs when embedded within a coordinate transformation 
 * or a concatenated coordinate operation;</li>
 * <li>source CRS when embedded within a point motion operation;</li>
 * <li>individual coordinate operations embedded within a concatenated 
 * coordinate operation;</li>
 * <li>base CRS when embedded within a derived CRS (including projected CRS);</li>
 * <li>source CRS, target CRS and abridged transformation when embedded within a
 * bound CRS;</li>
 * <li>individual members of a datum ensemble.</li>
 * </ul>
 * Multiple identifiers may be given for any object. When an identifier is given
 * for a coordinate reference system, coordinate operation or bound CRS, 
 * it applies to the whole object including all of its components. Should any 
 * attributes or values given in the cited identifier be in conflict with 
 * attributes or values given explicitly in the WKT description, the WKT values 
 * shall prevail.
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
public final class Identifier implements WktDescription {

    /**
     * ID keyword.
     */
    public final static String IDENTIFIER_KEYWORD = "ID";
    /**
     * authority name.
     */
    private String authorityName;
    /**
     * authority ID.
     */
    private String authorityUniqueIdentifier;
    /**
     * Version is an optional attribute indicating the version of the repository
     * or object that is cited.     
     */
    private String version;
    /**
     * Citation is an optional attribute that may be used to give further 
     * details of the authority.
     */
    private Citation citation;
    /**
     * URI is an optional attribute that may be used to give reference to an 
     * online resource.
     */
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
        final WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
        final List<WktElt> attributes = wktEltCollection.getAttributesFor(identifierWktElts, IDENTIFIER_KEYWORD);
        this.setAuthorityName(Utils.removeQuotes(attributes.get(0).getKeyword()));
        this.setAuthorityUniqueIdentifier(Utils.removeQuotes(attributes.get(1).getKeyword()));
        this.setVersion((attributes.size() == 3) ? Utils.removeQuotes(attributes.get(2).getKeyword()) : null);

        final List<WktElt> nodes = wktEltCollection.getNodesFor(identifierWktElts, IDENTIFIER_KEYWORD);
        for (final WktElt node : nodes) {
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
    public StringBuffer toWkt(final String endLine, final String tab, int deepLevel) {
        StringBuffer wkt = new StringBuffer();
        wkt = wkt.append(IDENTIFIER_KEYWORD).append(LEFT_DELIMITER);
        wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel+1)).append(Utils.addQuotes(getAuthorityName()));
        final String authorityIdStr = Utils.isNumeric(getAuthorityUniqueIdentifier()) ? getAuthorityUniqueIdentifier() : Utils.addQuotes(getAuthorityUniqueIdentifier());
        wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel+1)).append(authorityIdStr);
        if (getVersion() != null) {
            final String versionStr = Utils.isNumeric(getVersion()) ? getVersion() : Utils.addQuotes(getVersion());
            wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel+1)).append(versionStr);
        }
        if (getCitation() != null) {
            wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel+1)).append(getCitation().toWkt(endLine, tab, deepLevel+1));
        }
        if (getUri() != null) {
            wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel+1)).append(getUri().toWkt(endLine, tab, deepLevel+1));
        }
        wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel)).append(RIGHT_DELIMITER);
        return wkt;
    }
    
    @Override
    public StringBuffer toWkt() {
        return toWkt("\n", "   ", 0);
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

    /**
     * Citation is an optional attribute that may be used to give further 
     * details of the authority.
     */
    public static class Citation implements WktDescription{

        /**
         * CITATION keyword.
         */
        public final static String AUTHORITY_CITATION_KEYWORD = "CITATION";
        /**
         * description.
         */
        private String description;

        /**
         * Constructs a citation based on its description.
         * @param description description
         */
        public Citation(final String description) {
            this.description = description;
        }
        
        /**
         * Constructs a citation by parsing the WKT string.
         * @param citationWkt WKT string
         */
        public Citation(final WktElt citationWkt) {
            final WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
            final List<WktElt> attributes = wktEltCollection.getAttributesFor(citationWkt, AUTHORITY_CITATION_KEYWORD);
            this.description = attributes.get(0).getKeyword();
        }

        @Override
        public StringBuffer toWkt(final String endLine, final String tab, int deepLevel) {
            StringBuffer wkt = new StringBuffer();
            wkt = wkt.append(AUTHORITY_CITATION_KEYWORD).append(LEFT_DELIMITER);
            wkt = wkt.append(Utils.addQuotes(this.description));
            wkt = wkt.append(RIGHT_DELIMITER);
            return wkt;
        }
        
        @Override
        public StringBuffer toWkt() {
            return toWkt("\n", "   ", 0);
        }         
    }

    /**
     * URI is an optional attribute that may be used to give reference to an 
     * online resource.
     */
    public static class URI implements WktDescription{
        
        /**
         * URI keyword.
         */
        public final static String URI_KEYWORD = "URI";       

        /**
         * description.
         */
        private final String description;
        
        /**
         * Constructs an URI based on its description.
         * @param description description
         */
        public URI(final String description) {
            this.description = description;
        }

        /**
         * Constructs the URI by parsing the WKT string.
         * @param uriWkt WKT string
         */
        public URI(final WktElt uriWkt) {
            final WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
            final List<WktElt> attributes = wktEltCollection.getAttributesFor(uriWkt, URI_KEYWORD);
            this.description = Utils.removeQuotes(attributes.get(0).getKeyword());            
        }

        @Override
        public StringBuffer toWkt(final String endLine, final String tab, int deepLevel) {
            StringBuffer wkt = new StringBuffer(); 
            wkt = wkt.append(URI_KEYWORD).append(LEFT_DELIMITER);
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
