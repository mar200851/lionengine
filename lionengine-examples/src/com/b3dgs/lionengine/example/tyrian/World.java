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
package com.b3dgs.lionengine.example.tyrian;

import java.io.IOException;

import com.b3dgs.lionengine.Graphic;
import com.b3dgs.lionengine.UtilityRandom;
import com.b3dgs.lionengine.core.Media;
import com.b3dgs.lionengine.core.Sequence;
import com.b3dgs.lionengine.core.Verbose;
import com.b3dgs.lionengine.example.tyrian.background.Background;
import com.b3dgs.lionengine.example.tyrian.effect.FactoryEffect;
import com.b3dgs.lionengine.example.tyrian.effect.HandlerEffect;
import com.b3dgs.lionengine.example.tyrian.entity.Entity;
import com.b3dgs.lionengine.example.tyrian.entity.HandlerEntity;
import com.b3dgs.lionengine.example.tyrian.entity.dynamic.EntityDynamicType;
import com.b3dgs.lionengine.example.tyrian.entity.dynamic.FactoryEntityDynamic;
import com.b3dgs.lionengine.example.tyrian.entity.scenery.FactoryEntityScenery;
import com.b3dgs.lionengine.example.tyrian.entity.ship.FactoryShip;
import com.b3dgs.lionengine.example.tyrian.entity.ship.Ship;
import com.b3dgs.lionengine.example.tyrian.entity.ship.ShipType;
import com.b3dgs.lionengine.example.tyrian.map.Map;
import com.b3dgs.lionengine.example.tyrian.map.Tile;
import com.b3dgs.lionengine.example.tyrian.projectile.FactoryProjectile;
import com.b3dgs.lionengine.example.tyrian.projectile.HandlerProjectile;
import com.b3dgs.lionengine.example.tyrian.weapon.FactoryWeapon;
import com.b3dgs.lionengine.file.File;
import com.b3dgs.lionengine.file.FileReading;
import com.b3dgs.lionengine.file.FileWriting;
import com.b3dgs.lionengine.game.CameraGame;
import com.b3dgs.lionengine.game.WorldGame;
import com.b3dgs.lionengine.utility.LevelRipConverter;

/**
 * World implementation.
 */
final class World
        extends WorldGame
{
    /**
     * Create a level from a level rip.
     * 
     * @param map The map reference.
     * @param levelrip The level rip image.
     * @param tilesheet The tilesheet image.
     * @param output The output level saved.
     */
    private static void ripLevel(Map map, Media levelrip, Media tilesheet, Media output)
    {
        if (Media.checkExist(output))
        {
            try (final FileReading file = File.createFileReading(output);)
            {
                map.load(file);
            }
            catch (final IOException exception)
            {
                Verbose.exception(World.class, "ripLevel", exception, "Error on loading map !");
            }
        }
        else
        {
            final LevelRipConverter<Tile> rip = new LevelRipConverter<>();
            rip.start(levelrip, map, tilesheet);
            try (final FileWriting file = File.createFileWriting(output);)
            {
                map.save(file);
            }
            catch (final IOException exception)
            {
                Verbose.exception(World.class, "ripLevel", exception, "Error on saving map !");
            }
        }
    }

    /** Hud reference. */
    private final Hud hud;
    /** Map reference. */
    private final Map map;
    /** Background reference. */
    private final Background background;
    /** Camera reference. */
    private final CameraGame camera;
    /** Factory effect. */
    private final FactoryEffect factoryEffect;
    /** Handler effect. */
    private final HandlerEffect handlerEffect;
    /** Handler entity scenery. */
    private final HandlerEntity handlerEntityScenery;
    /** Handler entity dynamic. */
    private final HandlerEntity handlerEntityDynamic;
    /** Factory projectile. */
    private final FactoryProjectile factoryProjectile;
    /** Handler projectile. */
    private final HandlerProjectile handlerProjectile;
    /** Weapon factory. */
    private final FactoryWeapon factoryWeapon;
    /** Factory entity scenery. */
    private final FactoryEntityScenery factoryEntityScenery;
    /** Factory entity dynamic. */
    private final FactoryEntityDynamic factoryEntityDynamic;
    /** Factory ship. */
    private final FactoryShip factoryShip;
    /** Ship reference. */
    private final Ship ship;

    /**
     * @see WorldGame#WorldGame(Sequence)
     */
    World(Sequence sequence)
    {
        super(sequence);
        hud = new Hud();
        map = new Map();
        background = new Background();
        camera = new CameraGame();
        factoryEffect = new FactoryEffect();
        handlerEffect = new HandlerEffect(camera);
        handlerEntityScenery = new HandlerEntity(camera);
        handlerEntityDynamic = new HandlerEntity(camera);
        factoryProjectile = new FactoryProjectile(factoryEffect, handlerEffect);
        final HandlerEntity[] handlers = new HandlerEntity[]
        {
                handlerEntityScenery, handlerEntityDynamic
        };
        handlerProjectile = new HandlerProjectile(camera, handlers);
        factoryWeapon = new FactoryWeapon(factoryProjectile, handlerProjectile);
        factoryEntityScenery = new FactoryEntityScenery(factoryEffect, handlerEffect);
        factoryEntityDynamic = new FactoryEntityDynamic(factoryEffect, handlerEffect);
        factoryShip = new FactoryShip(factoryEffect, handlerEffect, factoryWeapon);
        ship = factoryShip.createEntity(ShipType.GENCORE_PHOENIX);
        camera.setView(0, 0, 263, 184);

        // Rip a level and store data in the map
        for (int i = 0; i < 21; i++)
        {
            final int index = UtilityRandom.getRandomInteger(20);
            if (i == 0)
            {
                World.ripLevel(map, Media.get("levels", "images", index + ".png"), Media.get("tiles", "level1"),
                        Media.get("levels", "0.map"));
            }
            else
            {
                final Map newMap = new Map();
                World.ripLevel(newMap, Media.get("levels", "images", index + ".png"), Media.get("tiles", "level1"),
                        Media.get("levels", i + ".map"));
                map.append(newMap, 0, newMap.getHeightInTile() * i);
            }
        }
        map.spawnEntityStatic(factoryEntityScenery, handlerEntityScenery);
        hud.setShip(ship);
    }

    /*
     * WorldGame
     */

    @Override
    public void update(double extrp)
    {
        camera.setLocationX(ship.getLocationOffsetX() / 12);
        camera.setLocationY(ship.getLocationY());
        ship.update(extrp, mouse, camera, height);
        handlerProjectile.update(extrp);
        handlerEntityScenery.update(extrp);
        handlerEntityDynamic.update(extrp);
        handlerEffect.update(extrp);
        background.update(extrp);
        hud.update(extrp);

        if (UtilityRandom.getRandomInteger(100) == 0)
        {
            final Entity entity = factoryEntityDynamic.createEntity(EntityDynamicType.METEOR_BIG);
            entity.teleport(UtilityRandom.getRandomInteger(camera.getViewWidth()) - entity.getWidth() / 2,
                    camera.getLocationY() + camera.getViewHeight() + entity.getHeight());
            handlerEntityDynamic.add(entity);
        }
    }

    @Override
    public void render(Graphic g)
    {
        background.render(g, camera);
        map.render(g, camera);
        handlerEntityScenery.render(g);
        handlerEntityDynamic.render(g);
        ship.render(g, camera);
        handlerProjectile.render(g);
        handlerEffect.render(g);
        hud.render(g);
    }

    @Override
    protected void saving(FileWriting file) throws IOException
    {
        // Nothing to do
    }

    @Override
    protected void loading(FileReading file) throws IOException
    {
        // Nothing to do
    }
}