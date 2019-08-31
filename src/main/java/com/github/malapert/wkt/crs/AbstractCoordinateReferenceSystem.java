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

import com.github.malapert.wkt.utils.Singleton;
import com.github.malapert.wkt.utils.Utils;
import com.github.malapert.wkt.metadata.WktDescription;
import com.github.malapert.wkt.utils.WktElt;
import com.github.malapert.wkt.utils.WktEltCollection;
import com.github.malapert.wkt.metadata.Identifier;
import com.github.malapert.wkt.metadata.Remark;
import com.github.malapert.wkt.metadata.ScopeExtent;
import com.github.malapert.wkt.metadata.Unit;
import com.github.malapert.wkt.metadata.UnitFactory;
import com.github.malapert.wkt.cs.Axis;
import com.github.malapert.wkt.cs.CoordinateSystem;
import com.github.malapert.wkt.datum.Datum;
import com.github.malapert.wkt.metadata.Usage;
import java.util.List;

/**
 * Defines a coordinate reference system.
 * 
 * @author Jean-Christophe Malapert
 */
public class AbstractCoordinateReferenceSystem  implements WktDescription, CoordinateReferenceSystem {
    public String  keyword = null;
    public String crsName = null;
    public Datum crsDatum = null;
    public CoordinateSystem cs = null;
    public ScopeExtent scopeExtent = new ScopeExtent();    

    /**
     * Returns the keyword.
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Sets the keyword.
     * @param keyword the keyword to set
     */
    public final void setKeyword(final String keyword) {
        this.keyword = keyword;
    }

    /**
     * Returns the name of the CRS.
     * @return the crsName
     */
    @Override
    public String getCrsName() {
        return crsName;
    }

    /**
     * Sets the name of the CRS.
     * @param crsName the crsName to set
     */
    public final void setCrsName(final String crsName) {
        this.crsName = crsName;
    }

    /**
     * Returns the datum.
     * @return the datumCrs
     */
    @Override
    public Datum getCrsDatum() {
        return crsDatum;
    }

    /**
     * Sets the datum.
     * @param crsDatum the datumCrs to set
     */
    public final void setCrsDatum(final Datum crsDatum) {
        this.crsDatum = crsDatum;
    }

    /**
     * Returns the coordinate system.
     * @return the cs
     */
    @Override
    public CoordinateSystem getCs() {
        return cs;
    }

    /**
     * Sets the coordinate system.
     * @param cs the cs to set
     */
    public final void setCs(final CoordinateSystem cs) {
        this.cs = cs;
    }

    /**
     * Returns the scope extent.
     * @return the scopeExtent
     */
    public ScopeExtent getScopeExtent() {
        return scopeExtent;
    }

    /**
     * Sets the scope extent.
     * @param scopeExtent the scopeExtent to set
     */
    public void setScopeExtent(final ScopeExtent scopeExtent) {
        this.scopeExtent = scopeExtent;
    }
    
    protected void parseCrs(final WktElt crsWkt) {
        setKeyword(crsWkt.getKeyword());
        final WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();

        final List<WktElt> attributes = wktEltCollection.getAttributesFor(crsWkt, getKeyword());
        this.setCrsName(attributes.get(0).getKeyword());

        final List<WktElt> nodes = wktEltCollection.getNodesFor(crsWkt, getKeyword());
        for (final WktElt node : nodes) {
            switch (node.getKeyword()) {                   
                case CoordinateSystem.COORDINATE_SYSTEM_KEYWORD:
                    this.setCs(new CoordinateSystem(node));
                    break;
                case Axis.AXIS_KEYWORD:
                    this.getCs().getAxisList().add(new Axis(node));
                    break;
                case UnitFactory.AngleUnit.ANGLEUNIT_KEYWORD:
                case UnitFactory.AngleUnit.ANGLEUNIT_UNIT_KEYWORD:
                case UnitFactory.LengthUnit.LENGTH_KEYWORD:
                case UnitFactory.LengthUnit.LENGTH_UNIT_KEYWORD:
                case UnitFactory.ScaleUnit.SCALEUNIT_KEYWORD:
                case UnitFactory.ScaleUnit.SCALEUNIT_UNIT_KEYWORD:
                case UnitFactory.ParametricUnit.PARAMETRICUNIT_KEYWORD:
                case UnitFactory.ParametricUnit.PARAMETRICUNIT_UNIT_KEYWORD:
                case UnitFactory.TimeUnit.TIMEUNIT_KEYWORD:
                case UnitFactory.TimeUnit.TIMEUNIT_UNIT_KEYWORD:
                    final Unit unit = UnitFactory.createFromWkt(node);
                    this.getCs().setUnit(unit);
                    break;
                case Usage.USAGE_KEYWORD:
                    this.getScopeExtent().getUsageList().add(new Usage(node));
                    break;                    
                case Identifier.IDENTIFIER_KEYWORD:                    
                    this.getScopeExtent().getIdentifierList().add(new Identifier(node));
                    break;
                case Remark.REMARK_KEYWORD:
                    this.getScopeExtent().setRemark(new Remark(node));
                    break;
                default:
                    if(hasSpecificParsing()) {
                        parseSpecificWkt(node);                       
                    } else {
                        throw new RuntimeException(node.getKeyword());                    
                    }
            }
        }
    }
    
    /**
     * Parses a specific WKT node.
     * @param wkt 
     */
    protected void parseSpecificWkt(final WktElt wkt) {
        
    }
    
    /**
     * Checks if there is a specific parsing to do.
     * @return True when a specific WKT parsing exists
     */
    protected boolean hasSpecificParsing() {
        return false;
    }  
    
    @Override
    public List<Usage> getUsageList() {
        return this.getScopeExtent().getUsageList();
    }    

    @Override
    public List<Identifier> getIdentifierList() {
        return this.getScopeExtent().getIdentifierList();
    }

    @Override
    public Remark getRemark() {
        return this.getScopeExtent().getRemark();
    }    
    
    @Override
    public StringBuffer toWkt(final int deepLevel) {
        StringBuffer wkt = new StringBuffer();
        wkt = wkt.append(this.getKeyword()).append(LEFT_DELIMITER);
        wkt = wkt.append("\n").append(Utils.makeSpaces(deepLevel+1)).append(this.getCrsName());
        wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel+1)).append(getCrsDatum().toWkt(deepLevel+1));
        wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel+1)).append(this.getCs().toWkt(deepLevel+1));
        wkt = wkt.append(this.getScopeExtent().toWkt(deepLevel+1));
        wkt = wkt.append("\n").append(Utils.makeSpaces(deepLevel)).append(RIGHT_DELIMITER);
        return wkt;
    }  

    @Override
    public String toString() {
        return toWkt(0).toString();
    }        


}
