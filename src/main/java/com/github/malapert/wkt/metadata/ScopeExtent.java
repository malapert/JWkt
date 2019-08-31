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
 * through grouping.
 *
 * <pre>
 * {@code
 * <scope extent identifier remark>::= [ { <wkt separator> <usage> } ] ...
 * [ { <wkt separator> <identifier> } ] ... [ { <wkt separator> <remark> } ]
 * }
 * </pre>
 *
 * @author Jean-Christophe Malapert
 */
public final class ScopeExtent implements Metadata, WktDescription {

    private List<Usage> usageList = new ArrayList<>();
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
                case Usage.USAGE_KEYWORD:
                    this.getUsageList().add(new Usage(wkt));
                    break;
                case Identifier.IDENTIFIER_KEYWORD:
                    this.getIdentifierList().add(new Identifier(wkt));
                    break;
                case Remark.REMARK_KEYWORD:
                    this.setRemark(new Remark(wkt));
                    break;
                default:
                    throw new RuntimeException();
            }
        }
    }

    public void setUsageList(final List<Usage> usageList) {
        this.usageList = usageList;
    }
    
    @Override
    public List<Usage> getUsageList() {
        return this.usageList;
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
        for (final Usage usage : this.getUsageList()) {
            wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel)).append(usage.toWkt(deepLevel));
        }

        for (final Identifier identifier : this.getIdentifierList()) {
            wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel)).append(identifier.toWkt(deepLevel));
        }        

        if (getRemark() != null) {
            wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel)).append(getRemark().toWkt(deepLevel));
        }
        
        return wkt;
    }


}
