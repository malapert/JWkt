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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The BNF element "scope extent identifier remark" is a collection of four
 * optional attributes which may be applied to a coordinate reference system, a
 * coordinate operation or a boundCRS.
 *
 * The "scope extent identifier remark" collection is to simplify the BNF
 * through grouping; each of the four attributes may appear separately in a WKT
 * string
 *
 * <pre>
 * {@code
 * <scope extent identifier remark>::=[ <wkt separator> <scope> ]  [ { <wkt separator> <extent> } ]...  [ { <wkt separator> <identifier> } ]…  [ <wkt separator> <remark>]
 * }
 * </pre>
 *
 * @author Jean-Christophe Malapert
 */
public class ScopeExtent implements Metadata, WktDescription {

    private Scope scope;
    private List<Extent> extentList = new ArrayList<>();
    private List<Identifier> identifierList = new ArrayList<>();
    private Remark remark;
    protected WktEltCollection wktEltCollection = Singleton.getInstance().getCollection();
    
    public ScopeExtent() {
        
    }

    public ScopeExtent(WktElt scopeExtent) {
        Iterator<WktElt> iter = this.wktEltCollection.iterator(scopeExtent);
        while (iter.hasNext()) {
            WktElt wkt = iter.next();
            switch (wkt.getKeyword()) {
                case Scope.SCOPE_KEYWORD:
                    this.scope = new Scope(wkt);
                    break;
                case Identifier.IDENTIFIER_KEYWORD:
                    this.getIdentifierList().add(new Identifier(wkt));
                    break;
                case Remark.REMARK_KEYWORD:
                    this.setRemark(new Remark(wkt));
                    break;
                case ExtentFactory.AreaDescription.AREA_DESCRIPTION_KEYWORD:
                case ExtentFactory.GeographicBoundingBox.GEOGRAPHIC_BOUDING_BOX_KEYWORD:
                case ExtentFactory.VerticalExtent.VERTICAL_EXTENT_KEYWORD:
                case ExtentFactory.TemporalExtent.TEMPORAL_EXTENT_KEYWORD:
                    Extent extent = ExtentFactory.createFromWkt(wkt);
                    getExtentList().add(extent);
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
    
    @Override
    public Scope getScope() {
        return this.scope;
    }

    /**
     * Returns the list of Extent.
     *
     * @return the extentList
     */
    @Override
    public List<Extent> getExtentList() {
        return extentList;
    }

    /**
     * Sets the list of Extent.
     *
     * @param extentList the extentList to set
     */
    public void setExtentList(final List<Extent> extentList) {
        this.extentList = extentList;
    }

    /**
     * add the EXTENT element by parsing the EXTENT WKT element.
     *
     * @param extentWktElt the EXTENT WKT element
     */
    public void addExtent(final WktElt extentWktElt) {
        Extent extent = ExtentFactory.createFromWkt(extentWktElt);
        this.extentList.add(extent);
    }

    /**
     * Returns the list of Identifier.
     *
     * @return the identifierList
     */
    @Override
    public List<Identifier> getIdentifierList() {
        return identifierList;
    }

    /**
     * Sets the list of Identifier.
     *
     * @param identifierList the identifierList to set
     */
    public void setIdentifierList(final List<Identifier> identifierList) {
        this.identifierList = identifierList;
    }

    /**
     * Adds an Identifier by parsing the IDENTIFIER WKT element.
     *
     * @param identifierWktElt
     */
    public void addIdentifier(WktElt identifierWktElt) {
        this.identifierList.add(new Identifier(identifierWktElt));
    }

    /**
     * Returns the Remark.
     *
     * @return the remark
     */
    @Override
    public Remark getRemark() {
        return remark;
    }

    /**
     * Sets the Remark.
     *
     * @param remark the remark to set
     */
    public void setRemark(Remark remark) {
        this.remark = remark;
    }

    /**
     * Sets the Remark by parsing the REMARK WKT element.
     *
     * @param remark the remark to set
     */
    public void setRemark(final WktElt remark) {
        this.remark = new Remark(remark);
    }

    @Override
    public StringBuffer toWkt(int deepLevel) {
        StringBuffer wkt = new StringBuffer();
        if (this.getScope() != null) {
            wkt = wkt.append(WKT_SEPARATOR).append(Utils.makeSpaces(deepLevel)).append(this.getScope().toWkt(deepLevel));
        }
        if (!getExtentList().isEmpty()) {
            for (Extent ext : getExtentList()) {
                wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel)).append(ext.toWkt(deepLevel));
            }
        }
        if (getRemark() != null) {
            wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel)).append(getRemark().toWkt(deepLevel));
        }
        return wkt;
    }
}
