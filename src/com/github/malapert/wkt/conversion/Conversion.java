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
package com.github.malapert.wkt.conversion;

import com.github.malapert.wkt.metadata.Identifier;
import com.github.malapert.wkt.metadata.WktDescription;
import com.github.malapert.wkt.method.Method;
import com.github.malapert.wkt.parameter.Operation;
import java.util.List;

/**
 *
 * @author malapert
 */
public interface Conversion extends WktDescription {
    
    public String getName();
    public Method getMethod();
    public List<Operation> getParameters();
    public List<Identifier> getIdentifiers();
}
