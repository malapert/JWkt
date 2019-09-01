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
package com.github.malapert.wkt.crs;

import com.github.malapert.wkt.metadata.ExtentFactory;
import com.github.malapert.wkt.metadata.Identifier;
import com.github.malapert.wkt.metadata.Remark;
import com.github.malapert.wkt.metadata.Scope;
import com.github.malapert.wkt.metadata.ScopeExtent;
import com.github.malapert.wkt.cs.CoordinateSystem;
import com.github.malapert.wkt.datum.Datum;
import com.github.malapert.wkt.utils.Singleton;
import com.github.malapert.wkt.utils.Utils;
import com.github.malapert.wkt.utils.WktElt;
import com.github.malapert.wkt.utils.WktEltCollection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author malapert
 */
public class CompoundCrs implements CompoundCoordinateReferenceSystem {
    
    public static final String COMPOUND_CRS = "COMPOUNDCRS";
    
    private String crsName;
    private final List<CoordinateReferenceSystem> components = new ArrayList<>();
    private ScopeExtent scopeExtent = new ScopeExtent();
    
    public CompoundCrs(final String crsName, List<CoordinateReferenceSystem> components) {
        this.crsName = crsName;
        this.components.addAll(components);
    }
    
    public CompoundCrs(WktElt crsWkt) {
        parseCrs(crsWkt);
    }

    protected CompoundCrs() {

    }
    
    private void parseCrs(WktElt crsWkt) {
        WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();

        List<WktElt> attributes = wktEltCollection.getAttributesFor(crsWkt, COMPOUND_CRS);
        this.crsName = attributes.get(0).getKeyword();

        List<WktElt> nodes = wktEltCollection.getNodesFor(crsWkt, COMPOUND_CRS);
        CoordinateReferenceSystem horizontalCrs = HorizontalCrsFactory.createFromWkt(nodes.get(0));
        this.components.add(horizontalCrs);
        for(int i=1; i<nodes.size();i++) {
            WktElt node = nodes.get(i);
            if(Scope.SCOPE_KEYWORD.equals(node.getKeyword())
               || Remark.REMARK_KEYWORD.equals(node.getKeyword())
               || ExtentFactory.AreaDescription.AREA_DESCRIPTION_KEYWORD.equals(node.getKeyword())
               || ExtentFactory.GeographicBoundingBox.GEOGRAPHIC_BOUDING_BOX_KEYWORD.equals(node.getKeyword())  
               || ExtentFactory.VerticalExtent.VERTICAL_EXTENT_KEYWORD.equals(node.getKeyword())
               || ExtentFactory.TemporalExtent.TEMPORAL_EXTENT_KEYWORD.equals(node.getKeyword())
               || Identifier.IDENTIFIER_KEYWORD.equals(node.getKeyword())) {
                this.scopeExtent = new ScopeExtent(node);
            } else {
                this.components.add(CoordinateReferenceSystemFactory.createFromWkt(node));
            }
        }
    }

    @Override
    public String getCrsName() {
        return this.crsName;
    }
    
    @Override
    public List<CoordinateReferenceSystem> getComponents() {
        return this.components;
    }
    
    /**
     * Returns scope extent
     * @return scope extent
     */
    public ScopeExtent getScopeExtent() {
        return this.scopeExtent;
    }   

    @Override
    public StringBuffer toWkt(final String endLine, final String tab, int deepLevel) {
        StringBuffer wkt = new StringBuffer();
        wkt = wkt.append(COMPOUND_CRS).append(LEFT_DELIMITER);
        wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel+1)).append(this.crsName);
        if(!this.components.isEmpty()) {
            for(CoordinateReferenceSystem crs:this.components) {
                wkt = wkt.append(WKT_SEPARATOR).append(endLine).append(Utils.makeSpaces(tab, deepLevel+1)).append(crs.toWkt(endLine, tab, deepLevel+1));
            }
        }
        wkt = wkt.append(Utils.makeSpaces(tab, deepLevel+1)).append(scopeExtent.toWkt(endLine, tab, deepLevel+1));

        wkt = wkt.append(endLine).append(Utils.makeSpaces(tab, deepLevel)).append(RIGHT_DELIMITER);
        return wkt;
    }  
    
    @Override
    public StringBuffer toWkt() {
        return toWkt("\n", "   ", 0);
    }    
    
    @Override
    public String toString() {
        return toWkt("", "", 0).toString();
    }      

    @Override
    public Datum getCrsDatum() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CoordinateSystem getCs() {
        return null;
    }
    
}
