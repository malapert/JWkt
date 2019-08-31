/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.malapert.wkt.metadata;

import com.github.malapert.wkt.utils.Singleton;
import com.github.malapert.wkt.utils.Utils;
import com.github.malapert.wkt.utils.WktElt;
import com.github.malapert.wkt.utils.WktEltCollection;
import java.util.List;

/**
 * <pre>
 * {@code
 * <usage>::=<usage keyword>  <left delimiter> <scope>  <wkt separator>  <extent> <right delimiter>
 * }
 * </pre>
 * @author malapert
 */
public final class Usage implements WktDescription {
    
    public final static String USAGE_KEYWORD = "USAGE";    
    
    private Scope scope;
    private List<Extent> extentList;
    protected WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
    
    
    public Usage(final Scope scope, final List<Extent> extentList) {
        this.scope = scope;
        this.extentList = extentList;
    }
    
    public Usage(final WktElt usageWktElts) {
        parse(usageWktElts);
    }  
    
    protected void parse(final WktElt scopeWktElts) {        
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
    
    public Scope getScope() {
        return this.scope;
    }
    
    public void setExtentList(final List<Extent> extentList) {
        this.extentList = extentList;
    }  
    
    public List<Extent> getExtentList() {
        return this.extentList;
    }
    

    @Override
    public StringBuffer toWkt(int deepLevel) {
        StringBuffer wkt = new StringBuffer();
        wkt = wkt.append(Usage.USAGE_KEYWORD).append(LEFT_DELIMITER);
        wkt = wkt.append("\n").append(Utils.makeSpaces(deepLevel+1)).append(this.getScope().toWkt(deepLevel+1));
        for(final Extent extent : this.getExtentList()) {
            wkt = wkt.append(WKT_SEPARATOR);            
            wkt = wkt.append("\n").append(Utils.makeSpaces(deepLevel+1)).append(extent.toWkt(deepLevel+1));            
        }
        wkt = wkt.append("\n").append(Utils.makeSpaces(deepLevel)).append(RIGHT_DELIMITER);
        return wkt;
    }
    
}
