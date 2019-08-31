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
     * Returns the description.
     * @return the description
     */
    public String getDescription();
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