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
package com.github.malapert.wkt.parameter;

import com.github.malapert.wkt.metadata.Identifier;
import com.github.malapert.wkt.metadata.Unit;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author malapert
 */
public abstract class AbstractOperation implements Operation {

    public String parameterName;
    public String parameterValueOrFile;
    public Unit parameterUnit = null;
    public List<Identifier> identifierList = new ArrayList<>();

    /**
     * @return the parameterName
     */
    @Override
    public String getParameterName() {
        return parameterName;
    }

    /**
     * @param parameterName the parameterName to set
     */
    public final void setParameterName(final String parameterName) {
        this.parameterName = parameterName;
    }

    /**
     * @return the parameterValue
     */
    @Override
    public String getParameterValueOrFile() {
        return parameterValueOrFile;
    }

    /**
     * @param parameterValueOrFile the parameterValue to set
     */
    public final void setParameterValueOrFile(final String parameterValueOrFile) {
        this.parameterValueOrFile = parameterValueOrFile;
    }

    /**
     * @return the parameterUnit
     */
    public Unit getParameterUnit() {
        return parameterUnit;
    }

    /**
     * @param parameterUnit the parameterUnit to set
     */
    public void setParameterUnit(final Unit parameterUnit) {
        this.parameterUnit = parameterUnit;
    }

    /**
     * @return the identifierList
     */
    @Override
    public List<Identifier> getIdentifierList() {
        return identifierList;
    }

    /**
     * @param identifierList the identifierList to set
     */
    public void setIdentifierList(final List<Identifier> identifierList) {
        this.identifierList = identifierList;
    }

}
