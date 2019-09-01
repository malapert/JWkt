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
package com.github.malapert.wkt.metadata;

import com.github.malapert.wkt.metadata.ExtentFactory.AreaDescription;
import com.github.malapert.wkt.metadata.ExtentFactory.GeographicBoundingBox;
import com.github.malapert.wkt.metadata.ExtentFactory.TemporalExtent;
import com.github.malapert.wkt.metadata.ExtentFactory.VerticalExtent;

/**
 * Extent describes the spatial applicability of a CRS or a coordinate operation.
 * 
 * <pre>
 * {@code
 * <extent> ::= <area description> | <geographic bounding box> | <vertical extent> | <temporal extent>
 * }
 * </pre>
 * 
 * @author Jean-Christoph Malapert
 */
public interface Extent extends WktDescription {

    /**
     * Returns the area description element.
     * @return the area description
     */
    public AreaDescription getAreaDescription();
    /**
     * Returns the geographic element.
     * @return the geographic element
     */
    public GeographicBoundingBox getGeographicElement();
    /**
     * Returns the temporal element.
     * @return the temporal element
     */
    public TemporalExtent getTemporalElement();
    /**
     * Returns the vertical element
     * @return the vertical element
     */
    public VerticalExtent getVerticalElement();
}