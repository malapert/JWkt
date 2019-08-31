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
package com.github.malapert.wkt.crs;

import com.github.malapert.wkt.metadata.ExtentFactory;
import com.github.malapert.wkt.metadata.Identifier;
import com.github.malapert.wkt.metadata.Remark;
import com.github.malapert.wkt.metadata.Scope;
import com.github.malapert.wkt.metadata.ScopeExtent;
import com.github.malapert.wkt.metadata.WktDescription;
import com.github.malapert.wkt.cs.CoordinateSystem;
import com.github.malapert.wkt.datum.Datum;
import com.github.malapert.wkt.metadata.Usage;
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
public class CompoundCrs implements CompoundCoordinateReferenceSystem, WktDescription {
    
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

    @Override
    public List<Usage> getUsageList() {
        return this.scopeExtent.getUsageList();
    }

    @Override
    public List<Identifier> getIdentifierList() {
        return this.scopeExtent.getIdentifierList();
    }

    @Override
    public Remark getRemark() {
        return this.scopeExtent.getRemark();
    }    

    @Override
    public StringBuffer toWkt(int deepLevel) {
        StringBuffer wkt = new StringBuffer();
        wkt = wkt.append(COMPOUND_CRS).append(LEFT_DELIMITER);
        wkt = wkt.append("\n").append(Utils.makeSpaces(deepLevel+1)).append(this.crsName);
        if(!this.components.isEmpty()) {
            for(CoordinateReferenceSystem crs:this.components) {
                wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel+1)).append(crs.toWkt(deepLevel+1));
            }
        }
        wkt = wkt.append(Utils.makeSpaces(deepLevel+1)).append(scopeExtent.toWkt(deepLevel+1));

        wkt = wkt.append("\n").append(Utils.makeSpaces(deepLevel)).append(RIGHT_DELIMITER);
        return wkt;
    }        
    
    @Override
    public String toString() {
        return toWkt(0).toString();
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
