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
package com.github.malapert.wkt.conversion;

import com.github.malapert.wkt.metadata.Identifier;
import com.github.malapert.wkt.method.Method;
import com.github.malapert.wkt.parameter.Operation;
import java.util.ArrayList;
import java.util.List;

/**
 * Describes the conversion to apply from a base CRS.
 * @author Jean-Christophe Malapert
 */
public abstract class AbstractConversion implements Conversion {
    
    /**
     * conversion name.
     */
    public String name;
    /**
     * conversion method.
     */
    public Method method;
    /**
     * conversion parameters.
     */
    public List<Operation> parameters = new ArrayList<>();
    /**
     * conversion identifiers
     */
    public List<Identifier> identifiers = new ArrayList<>();

    /**
     * Returns the name of the conversion.
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Returns the method of conversion.
     * @return the method
     */
    @Override
    public Method getMethod() {
        return method;
    }

    /**
     * Sets the method of conversion.
     * @param method the method to set
     */
    public void setMethod(final Method method) {
        this.method = method;
    }

    /**
     * Returns the parameters of the conversion.
     * @return the parameters
     */
    @Override
    public List<Operation> getParameters() {
        return parameters;
    }

    /**
     * Sets the parameters of the conversion.
     * @param parameters the parameters to set
     */
    public void setParameters(final List<Operation> parameters) {
        this.parameters = parameters;
    }

    /**
     * Returns the list of identifiers.
     * @return the identifiers
     */
    @Override
    public List<Identifier> getIdentifiers() {
        return identifiers;
    }

    /**
     * Sets the list of the identifiers.
     * @param identifiers the identifiers to set
     */
    public void setIdentifiers(final List<Identifier> identifiers) {
        this.identifiers = identifiers;
    }
    
}
