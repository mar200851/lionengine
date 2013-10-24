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
package com.b3dgs.lionengine.example.game.handler;

import com.b3dgs.lionengine.Version;
import com.b3dgs.lionengine.core.Engine;
import com.b3dgs.lionengine.core.Media;

/**
 * Main class.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 * @see com.b3dgs.lionengine.example.minimal
 */
public final class AppGameHandler
{
    /**
     * Main.
     * 
     * @param args The arguments.
     */
    public static void main(String[] args)
    {
        Engine.start("Game Handler", Version.create(1, 0, 0), Media.getPath("resources", "game", "handler"));

        final Handler handler = new Handler();
        handler.add(new MyObject());
        handler.add(new MyObject());

        for (int i = 0; i < 2; i++)
        {
            handler.update(1.0);
            handler.render(null);
        }
    }

    /**
     * Private constructor.
     */
    private AppGameHandler()
    {
        throw new RuntimeException();
    }
}
