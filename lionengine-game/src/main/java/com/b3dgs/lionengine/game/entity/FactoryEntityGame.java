/*
 * Copyright (C) 2013 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.game.entity;

import com.b3dgs.lionengine.game.FactoryGame;
import com.b3dgs.lionengine.game.SetupSurfaceGame;

/**
 * Abstract entity factory. It performs a list of available entities from a directory considering an input enumeration.
 * Data are stored with an enumeration as key.
 * <p>
 * Sample implementation:
 * </p>
 * 
 * <pre>
 * public class FactoryEntity
 *         extends FactoryEntityGame&lt;TypeEntity, SetupEntityGame, EntityGame&gt;
 * {
 *     public FactoryEntity()
 *     {
 *         super(TypeEntity.class);
 *         loadAll(TypeEntity.values());
 *     }
 * 
 *     &#064;Override
 *     public EntityGame createEntity(TypeEntity key)
 *     {
 *         switch (id)
 *         {
 *             default:
 *                 throw new LionEngineException(&quot;Unknown entity: &quot; + key);
 *         }
 *     }
 * 
 *     &#064;Override
 *     protected SetupEntityGame createSetup(TypeEntity key)
 *     {
 *         return new SetupEntityGame(Media.get(&quot;directory&quot;, key + &quot;.xml&quot;));
 *     }
 * }
 * </pre>
 * 
 * @param <T> The enum containing all type.
 * @param <S> The setup type.
 * @param <E> The entity type.
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public abstract class FactoryEntityGame<T extends Enum<T>, S extends SetupSurfaceGame, E extends EntityGame>
        extends FactoryGame<T, S>
{
    /**
     * Constructor.
     * 
     * @param keyType The class of the enum type defined.
     */
    public FactoryEntityGame(Class<T> keyType)
    {
        super(keyType);
    }

    /**
     * Get the entity instance from its key. It is recommended to use a switch on the key, and throw an exception for
     * the
     * default case (instead of returning a <code>null</code> value).
     * 
     * @param key The entity key (as enumeration).
     * @return The entity instance.
     */
    public abstract E createEntity(T key);
}