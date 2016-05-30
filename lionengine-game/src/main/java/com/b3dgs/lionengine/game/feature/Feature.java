/*
 * Copyright (C) 2013-2016 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.game.feature;

/**
 * Represents something that can be delegated to perform specialized computing and reduce {@link Featurable} visible
 * complexity.
 */
public interface Feature
{
    /**
     * Prepare the feature. Must be called before usage if {@link Services} are required.
     * 
     * @param owner The owner reference.
     * @param services The services reference.
     */
    void prepare(Featurable owner, Services services);

    /**
     * Check object interface listening and add them automatically. If the {@link Feature} provide listeners, this will
     * allow to add them automatically.
     * 
     * @param listener The listener to check.
     */
    void checkListener(Object listener);

    /**
     * Get the {@link Featurable} reference owning this {@link Feature}.
     * <p>
     * Function {@link #prepare(Featurable, Services)} must have been called before.
     * </p>
     * 
     * @param <O> The real {@link Featurable} type.
     * @return The {@link Featurable} reference.
     */
    <O extends Featurable> O getOwner();
}
