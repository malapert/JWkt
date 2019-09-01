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
import java.util.Arrays;
import java.util.List;

/**
 * <image datum>::=<image datum keyword> <left delimiter> <datum name> <wkt separator> <pixelincell> [ <wkt separator> <datum anchor> ] [ { <wkt separator> <identifier> } ]…  <right delimiter>  
 * @author malapert
 */
public final class ImageDatum extends AbstractDatum {
    
    private PixelInCell pixelInCell;
    
    public enum ImageDatumKeyword {
        IDATUM,
        IMAGEDATUM;
        private ImageDatumKeyword() {        
        }
        
        public static List<String> getKeywords() {
            return Arrays.asList(ImageDatumKeyword.IDATUM.name(), ImageDatumKeyword.IMAGEDATUM.name());
        }         
    }
    
    public enum PixelInCell {
        cellCentre,
        cellCenter,
        cellCorner
    }
    
    public ImageDatum(final ImageDatumKeyword keyword, final String datumName, final PixelInCell pixelCell) {
        setKeyword(keyword.name());
        setDatumName(datumName);
        setPixelInCell(pixelInCell);
    }
    
    public ImageDatum(final WktElt imageDatumCrs) {
        parse(imageDatumCrs);
    }
    
    @Override
    protected void parse(final WktElt temporalDatumWkt) {
        setKeyword(temporalDatumWkt.getKeyword());
        final WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
        final List<WktElt> attributes = wktEltCollection.getAttributesFor(temporalDatumWkt, temporalDatumWkt.getKeyword());
        this.setDatumName(Utils.removeQuotes(attributes.get(0).getKeyword()));
        this.setPixelInCell(PixelInCell.valueOf(attributes.get(1).getKeyword()));
        
        List<WktElt> nodes = wktEltCollection.getNodesFor(temporalDatumWkt, temporalDatumWkt.getKeyword());
        for (WktElt node : nodes) {
            switch (node.getKeyword()) {
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
     * @return the pixelInCell
     */
    public PixelInCell getPixelInCell() {
        return pixelInCell;
    }

    /**
     * @param pixelInCell the pixelInCell to set
     */
    public final void setPixelInCell(PixelInCell pixelInCell) {
        this.pixelInCell = pixelInCell;
    }
    
    @Override
    public StringBuffer toWkt(final String endLine, final String tab, int deepLevel) {
        StringBuffer wkt = new StringBuffer();
        wkt = wkt.append(getKeyword()).append(LEFT_DELIMITER);
        wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel+1)).append(Utils.addQuotes(this.getDatumName()));
        wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel+1)).append(this.getPixelInCell());
        if(getAnchor() != null) {
            wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel+1)).append(getAnchor().toWkt(endLine, tab, deepLevel+1));
        }
        if(!getIdentifierList().isEmpty()) {
            for(Identifier id:getIdentifierList()) {
                wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel+1)).append(id.toWkt(endLine, tab, deepLevel+1));
            }
        }
        wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel)).append(RIGHT_DELIMITER);
        return wkt;
    }
    
    
    
}
