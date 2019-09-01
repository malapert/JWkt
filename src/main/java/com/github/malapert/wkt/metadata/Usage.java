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
import java.util.List;

/**
 * Usage is an optional attribute which if included in a WKT string shall 
 * include both "scope" and "extent". 
 * 
 * Multiple pairs of scope/extent may be used to describe the usage for 
 * different purposes over different extents. 
 * Within each pairing, extent may consist of one or more of area textual 
 * description, area bounding box, vertical extent and/or temporal extent
 * 
 * <pre>
 * {@code
 * <usage>::=<usage keyword>  <left delimiter> <scope>  <wkt separator>  <extent> <right delimiter>
 * }
 * </pre>
 * @author malapert
 */
public final class Usage implements WktDescription {
    
    /**
     * USAGE Keyword.
     */
    public final static String USAGE_KEYWORD = "USAGE";    
    
    /**
     * {@link com.github.malapert.wkt.metadata.Usage} describes the purpose or 
     * purposes for which a CRS, datum, datum ensemble, coordinate operation or 
     * bound CRS is applied
     */
    private Scope scope;
    /**
     * List of {@link com.github.malapert.wkt.metadata.Extent}. 
     * 
     * Extent describes the spatial applicability of a CRS or a coordinate 
     * operation.
     */
    private List<Extent> extentList;
    
    /**
     * Constructs the usage based on the scope and the extent list.
     * @param scope scope
     * @param extentList extent list
     */
    public Usage(final Scope scope, final List<Extent> extentList) {
        this.scope = scope;
        this.extentList = extentList;
    }
    
    /**
     * Constructs the usage by parsing the WKT elts.
     * @param usageWktElts WKT elts
     */
    public Usage(final WktElt usageWktElts) {
        parse(usageWktElts);
    }  
    
    /**
     * Parses the WKT string.
     * @param scopeWktElts WKT string    
     */
    private void parse(final WktElt scopeWktElts) {    
        final WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
        final List<WktElt> nodes = wktEltCollection.getNodesFor(scopeWktElts, Usage.USAGE_KEYWORD);
        for (final WktElt node : nodes) {
            switch (node.getKeyword()) {
                case Scope.SCOPE_KEYWORD:
                    this.setScope(new Scope(node));
                case ExtentFactory.AreaDescription.AREA_DESCRIPTION_KEYWORD:
                case ExtentFactory.GeographicBoundingBox.GEOGRAPHIC_BOUDING_BOX_KEYWORD:
                case ExtentFactory.VerticalExtent.VERTICAL_EXTENT_KEYWORD:
                case ExtentFactory.TemporalExtent.TEMPORAL_EXTENT_KEYWORD:
                    this.getExtentList().add(ExtentFactory.createFromWkt(node));
                    break;
                default:
                    throw new RuntimeException();                
            }
            
        }
    }           
    
    /**
     * Sets the SCOPE element by parsing the SCOPE WKT element.
     *
     * @param scope
     */
    public void setScope(final Scope scope) {
        this.scope = scope;
    }
    
    /**
     * Returns the scope.
     * @return the scope
     */
    public Scope getScope() {
        return this.scope;
    }
    
    /**
     * Sets the extent list.
     * @param extentList the extent list
     */
    public void setExtentList(final List<Extent> extentList) {
        this.extentList = extentList;
    }  
    
    /**
     * Returns the extent list.
     * @return the extent list 
     */
    public List<Extent> getExtentList() {
        return this.extentList;
    }
    

    @Override
    public StringBuffer toWkt(final String endLine, final String tab, int deepLevel) {
        StringBuffer wkt = new StringBuffer();
        wkt = wkt.append(Usage.USAGE_KEYWORD).append(LEFT_DELIMITER);
        wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel+1)).append(this.getScope().toWkt(endLine, tab, deepLevel+1));
        for(final Extent extent : this.getExtentList()) {
            wkt = wkt.append(WKT_SEPARATOR);            
            wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel+1)).append(extent.toWkt(endLine, tab, deepLevel+1));            
        }
        wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel)).append(RIGHT_DELIMITER);
        return wkt;
    }
    
    @Override
    public StringBuffer toWkt() {
        return toWkt("\n", "   ", 0);
    }
    
}
