/*
 * Copyright (C) 2013-2015 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.b3dgs.lionengine.game.trait.producible;

import com.b3dgs.lionengine.Localizable;
import com.b3dgs.lionengine.core.Media;
import com.b3dgs.lionengine.game.trait.Trait;

/**
 * Represents a producible object.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public interface Producible
        extends Trait, Localizable
{
    /**
     * Set the production location.
     * 
     * @param x The horizontal location.
     * @param y The vertical location.
     */
    void setLocation(double x, double y);

    /**
     * Get the media representing the producible.
     * 
     * @return The media representing the producible.
     */
    Media getMedia();

    /**
     * Get the production steps number.
     * 
     * @return The production steps number.
     */
    int getSteps();
}