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
package com.b3dgs.lionengine.example.warcraft.skill;

import com.b3dgs.lionengine.core.Media;
import com.b3dgs.lionengine.drawable.Drawable;
import com.b3dgs.lionengine.drawable.SpriteTiled;
import com.b3dgs.lionengine.example.warcraft.FactoryProduction;
import com.b3dgs.lionengine.example.warcraft.ResourcesLoader;
import com.b3dgs.lionengine.game.TimedMessage;
import com.b3dgs.lionengine.game.rts.skill.SetupSkillRts;

/**
 * Setup skill implementation.
 * 
 * @author Pierre-Alexandre
 */
public final class SetupSkill
        extends SetupSkillRts
{
    /** Skill icon. */
    public final SpriteTiled icon;
    /** Skill background. */
    public final SpriteTiled background;
    /** Production factory. */
    final FactoryProduction factoryProduction;
    /** The timed message reference. */
    final TimedMessage message;

    /**
     * Constructor.
     * 
     * @param config The config media.
     * @param background The skill background.
     * @param factoryProduction The production factory.
     * @param message The timed message reference.
     */
    public SetupSkill(Media config, SpriteTiled background, FactoryProduction factoryProduction, TimedMessage message)
    {
        super(config);
        this.background = background;
        this.factoryProduction = factoryProduction;
        this.message = message;
        icon = Drawable.loadSpriteTiled(Media.get(ResourcesLoader.SKILLS_DIR, configurable.getDataString("icon")), 27,
                19);
        icon.load(false);
    }
}