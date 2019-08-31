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
import static com.github.malapert.wkt.metadata.WktDescription.LEFT_DELIMITER;
import static com.github.malapert.wkt.metadata.WktDescription.RIGHT_DELIMITER;
import static com.github.malapert.wkt.metadata.WktDescription.WKT_SEPARATOR;
import com.github.malapert.wkt.utils.WktElt;
import com.github.malapert.wkt.utils.WktEltCollection;
import com.github.malapert.wkt.metadata.Extent;
import com.github.malapert.wkt.metadata.ExtentFactory;
import com.github.malapert.wkt.metadata.Identifier;
import com.github.malapert.wkt.metadata.Remark;
import com.github.malapert.wkt.metadata.Scope;
import com.github.malapert.wkt.metadata.ScopeExtent;
import com.github.malapert.wkt.metadata.Unit;
import com.github.malapert.wkt.metadata.UnitFactory;
import com.github.malapert.wkt.conversion.Conversion;
import com.github.malapert.wkt.cs.Axis;
import com.github.malapert.wkt.cs.CoordinateSystem;
import com.github.malapert.wkt.datum.Datum;
import com.github.malapert.wkt.conversion.DerivedConversion;
import com.github.malapert.wkt.metadata.Usage;
import java.util.List;

/**
 *
 * @author malapert
 */
public abstract class AbstractDerivatedCoordinateReferenceSystem implements WktDescription, DerivedCoordinateReferenceSystem {
    
    public String keyword;
    public String crsName;
    public BaseCrs baseDerivatedCrs;
    public Conversion conversionFromBaseCrs;
    public CoordinateSystem cs;
    public ScopeExtent scopeExtent = new ScopeExtent(); 

    /**
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * @param keyword the keyword to set
     */
    public final void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * @return the crsName
     */
    @Override
    public String getCrsName() {
        return crsName;
    }

    /**
     * @param crsName the crsName to set
     */
    public final void setCrsName(String crsName) {
        this.crsName = crsName;
    }

    /**
     * @return the baseCrs
     */
    public BaseCrs getBaseDerivatedCrs() {
        return baseDerivatedCrs;
    }

    /**
     * @param baseDerivatedCrs the baseCrs to set
     */
    public final void setBaseDerivatedCrs(BaseCrs baseDerivatedCrs) {
        this.baseDerivatedCrs = baseDerivatedCrs;
    }

    /**
     * @return the derivedConversion
     */
    @Override
    public Conversion getConversionFromBaseCrs() {
        return conversionFromBaseCrs;
    }

    /**
     * @param conversionFromBaseCrs the derivedConversion to set
     */
    public final void setConversionFromBaseCrs(Conversion conversionFromBaseCrs) {
        this.conversionFromBaseCrs = conversionFromBaseCrs;
    }

    /**
     * @return the cs
     */
    @Override
    public CoordinateSystem getCs() {
        return cs;
    }

    /**
     * @param cs the cs to set
     */
    public final void setCs(CoordinateSystem cs) {
        this.cs = cs;
    }

    /**
     * @return the scopeExtent
     */
    public ScopeExtent getScopeExtent() {
        return scopeExtent;
    }

    /**
     * @param scopeExtent the scopeExtent to set
     */
    public void setScopeExtent(ScopeExtent scopeExtent) {
        this.scopeExtent = scopeExtent;
    }
    
    @Override
    public CoordinateReferenceSystem getBaseCrs() {
        return CoordinateReferenceSystemFactory.create(getBaseDerivatedCrs().getBaseCrsName(), 
                                                       getBaseDerivatedCrs().getDatum(), 
                                                       getCs());
    }    
    
    @Override
    public Datum getCrsDatum() {
       return getBaseCrs().getCrsDatum();
    }
    
    protected void parseDerivatedCrs(final WktElt crsWkt) {
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
                    this.getScopeExtent().getUsageList().add(new Usage(crsWkt));
                    break;                    
                case Identifier.IDENTIFIER_KEYWORD:                    
                    this.getScopeExtent().getIdentifierList().add(new Identifier(node));
                    break;
                case Remark.REMARK_KEYWORD:
                    this.getScopeExtent().setRemark(new Remark(node));
                    break;
                case DerivedConversion.DERIVED_CONVERSION_KEYWORD:
                    this.setConversionFromBaseCrs(new DerivedConversion(node));
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
    
    protected void parseSpecificWkt(WktElt wkt) {
        
    }
    
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
    public StringBuffer toWkt(int deepLevel) {
        StringBuffer wkt = new StringBuffer();
        wkt = wkt.append(this.getKeyword()).append(LEFT_DELIMITER);
        wkt = wkt.append("\n").append(Utils.makeSpaces(deepLevel+1)).append(this.getCrsName());
        wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel+1)).append(this.getBaseDerivatedCrs().toWkt(deepLevel+1));
        wkt = wkt.append(WKT_SEPARATOR).append("\n").append(Utils.makeSpaces(deepLevel+1)).append(this.getConversionFromBaseCrs().toWkt(deepLevel+1));        
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
