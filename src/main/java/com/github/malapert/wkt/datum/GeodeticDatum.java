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
import com.github.malapert.wkt.cs.Ellipsoid;
import com.github.malapert.wkt.cs.PrimeMeridian;
import java.util.Arrays;
import java.util.List;

/**
 * "geodetic datum" is used when the CRS type is geodetic. 
 * 
 * For a projected CRS, the geodetic datum is included in the Base Geodetic CRS
 * 
 * <p>
 * <pre>
 * {@code
 * <geodetic datum>::=<geodetic datum keyword> <left delimiter> <datum name> <wkt separator> <ellipsoid> [ <wkt separator> <datum anchor> ] [ { <wkt separator> <identifier> } ]…  <right delimiter> { <wkt separator> <prime meridian> }  
 * }
 * </pre>
 *
 * @author Jean-Christophe Malapert
 */
public final class GeodeticDatum extends AbstractDatum {
    
    public enum GeodeticDatumKeyword {
        DATUM,
        GEODETICDATUM;
        private GeodeticDatumKeyword() {        
        }
        
        public static List<String> getKeywords() {
            return Arrays.asList(GeodeticDatumKeyword.DATUM.name(), GeodeticDatumKeyword.GEODETICDATUM.name());
        }         
    }    

    private Ellipsoid ellipsoid;
    private PrimeMeridian primeMeridian;
    
    public GeodeticDatum(final GeodeticDatumKeyword keyword, final String datumName, final Ellipsoid ellipsoid) {
        setKeyword(keyword.name());
        setDatumName(datumName);
        setEllipsoid(ellipsoid);
    }
    
    /**
     * Creates a Geodetic datum based on required parameters.
     * @param datumName name of the datum
     * @param ellipsoid Ellipsoid object
     * @param primeMeridian PrimeMeridian object
     */
    public GeodeticDatum(final String datumName, final Ellipsoid ellipsoid, 
                         final PrimeMeridian primeMeridian) {
        setDatumName(datumName);
        this.ellipsoid = ellipsoid;
        this.primeMeridian = primeMeridian;
    }

    /**
     * Creates a Geodetic datum by parsing a DATUM WKT element.
     * @param geoDatumElt 
     */
    public GeodeticDatum(final WktElt geoDatumElt) {
        parse(geoDatumElt);
    }

    @Override
    protected void parse(final WktElt geodeticDatum) {
        setKeyword(geodeticDatum.getKeyword());
        final WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
        final List<WktElt> attributes = wktEltCollection.getAttributesFor(geodeticDatum, geodeticDatum.getKeyword());
        this.setDatumName(Utils.removeQuotes(attributes.get(0).getKeyword()));                        
        List<WktElt> nodes = wktEltCollection.getNodesFor(geodeticDatum, geodeticDatum.getKeyword());
        for (WktElt node : nodes) {
            switch (node.getKeyword()) {
                case "ELLIPSOID":
                case "SPHEROID":
                    this.setEllipsoid(new Ellipsoid(node));
                    break;
                case Anchor.KEYWORD_ANCHOR:
                    this.setAnchor(new Anchor(node));
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
     * Returns the ellipsoid.
     * @return the ellipsoid
     */
    public Ellipsoid getEllipsoid() {
        return ellipsoid;
    }

    /**
     * Sets the ellipsoid.
     * @param ellipsoid the ellipsoid to set
     */
    public final void setEllipsoid(final Ellipsoid ellipsoid) {
        this.ellipsoid = ellipsoid;
    }

    /**
     * Returns the primeMeridian.
     * @return the primeMeridian
     */
    public PrimeMeridian getPrimeMeridian() {
        return primeMeridian;
    }

    /**
     * Sets the PrimeMeridian
     * @param primeMeridian the primeMeridian to set
     */
    public void setPrimeMeridian(final PrimeMeridian primeMeridian) {
        this.primeMeridian = primeMeridian;
    }
      

    @Override
    public StringBuffer toWkt(final String endLine, final String tab, int deepLevel) {
        StringBuffer wkt = new StringBuffer();
        wkt = wkt.append(this.getKeyword()).append(LEFT_DELIMITER);
        wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel+1)).append(Utils.addQuotes(getDatumName()));
        wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel+1)).append(getEllipsoid().toWkt(endLine, tab, deepLevel+1));
        if (getAnchor() != null) {
            wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel+1)).append(getAnchor().toWkt(endLine, tab, deepLevel+1));
        }
        for (Identifier id : getIdentifierList()) {
            wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel+1)).append(id.toWkt(endLine, tab, deepLevel+1));
        }        
        wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel)).append(RIGHT_DELIMITER);
        if (getPrimeMeridian() != null) {
            wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel)).append(getPrimeMeridian().toWkt(endLine, tab, deepLevel+1));
        }
        return wkt;
    }


}
