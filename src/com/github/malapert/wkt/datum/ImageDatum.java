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
public class ImageDatum extends AbstractDatum {
    
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
        WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
        List<WktElt> attributes = wktEltCollection.getAttributesFor(temporalDatumWkt, temporalDatumWkt.getKeyword());
        this.setDatumName(attributes.get(0).getKeyword());
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
    public StringBuffer toWkt(int deepLevel) {
        StringBuffer wkt = new StringBuffer();
        wkt = wkt.append(getKeyword()).append(LEFT_DELIMITER);
        wkt = wkt.append("\n").append(Utils.makeSpaces(deepLevel+1)).append(this.getDatumName());
        wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel+1)).append(this.getPixelInCell());
        if(getAnchor() != null) {
            wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel+1)).append(getAnchor().toWkt(deepLevel+1));
        }
        if(!getIdentifierList().isEmpty()) {
            for(Identifier id:getIdentifierList()) {
                wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel+1)).append(id.toWkt(deepLevel+1));
            }
        }
        wkt = wkt.append("\n").append(Utils.makeSpaces(deepLevel)).append(RIGHT_DELIMITER);
        return wkt;
    }
    
    
    
}
