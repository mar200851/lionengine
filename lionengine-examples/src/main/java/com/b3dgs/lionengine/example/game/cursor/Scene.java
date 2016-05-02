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
package com.b3dgs.lionengine.example.game.cursor;

import com.b3dgs.lionengine.core.Context;
import com.b3dgs.lionengine.core.Engine;
import com.b3dgs.lionengine.core.Medias;
import com.b3dgs.lionengine.core.Resolution;
import com.b3dgs.lionengine.core.Sequence;
import com.b3dgs.lionengine.core.awt.Keyboard;
import com.b3dgs.lionengine.core.awt.Mouse;
import com.b3dgs.lionengine.game.Camera;
import com.b3dgs.lionengine.game.Cursor;
import com.b3dgs.lionengine.game.TextGame;
import com.b3dgs.lionengine.game.map.MapTile;
import com.b3dgs.lionengine.game.map.MapTileGame;
import com.b3dgs.lionengine.game.map.MapTileGroup;
import com.b3dgs.lionengine.game.map.MapTileGroupModel;
import com.b3dgs.lionengine.game.map.MapTileRendererModel;
import com.b3dgs.lionengine.game.map.MapTileViewer;
import com.b3dgs.lionengine.game.map.MapTileViewerModel;
import com.b3dgs.lionengine.game.map.circuit.Circuit;
import com.b3dgs.lionengine.game.map.circuit.MapCircuitExtractor;
import com.b3dgs.lionengine.game.map.transition.MapTransitionExtractor;
import com.b3dgs.lionengine.game.map.transition.Transition;
import com.b3dgs.lionengine.game.object.Services;
import com.b3dgs.lionengine.game.tile.Tile;
import com.b3dgs.lionengine.graphic.ColorRgba;
import com.b3dgs.lionengine.graphic.Graphic;
import com.b3dgs.lionengine.graphic.Text;
import com.b3dgs.lionengine.graphic.TextStyle;

/**
 * Game loop designed to handle our little world.
 * 
 * @see com.b3dgs.lionengine.example.core.minimal
 */
class Scene extends Sequence
{
    /** Native resolution. */
    private static final Resolution NATIVE = new Resolution(320, 240, 60);

    /** Text reference. */
    private final TextGame text = new TextGame(Text.DIALOG, 9, TextStyle.NORMAL);
    /** Services reference. */
    private final Services services = new Services();
    /** Camera reference. */
    private final Camera camera = services.create(Camera.class);
    /** Cursor reference. */
    private final Cursor cursor = services.create(Cursor.class);
    /** Map reference. */
    private final MapTile map = services.create(MapTileGame.class);
    /** Map viewer. */
    private final MapTileViewer mapViewer = map.createFeature(MapTileViewerModel.class);
    /** Map reference. */
    private final MapTileGroup mapGroup = map.createFeature(MapTileGroupModel.class);
    /** Keyboard reference. */
    private final Keyboard keyboard = getInputDevice(Keyboard.class);
    /** Mouse reference. */
    private final Mouse mouse = getInputDevice(Mouse.class);
    /** Map transition extractor. */
    private final MapTransitionExtractor transitionExtractor = new MapTransitionExtractor(map);
    /** Map transition extractor. */
    private final MapCircuitExtractor circuitExtractor = new MapCircuitExtractor(map);

    /**
     * Constructor.
     * 
     * @param context The context reference.
     */
    public Scene(Context context)
    {
        super(context, NATIVE);
        setSystemCursorVisible(false);
        keyboard.addActionPressed(Keyboard.ESCAPE, () -> end());
    }

    /**
     * Draw info about the specified tile.
     * 
     * @param g The graphics output.
     */
    private void renderTileInfo(Graphic g)
    {
        final int tx = map.getInTileX(cursor);
        final int ty = map.getInTileY(cursor);
        final Tile tile = map.getTile(tx, ty);
        if (tile != null)
        {
            final int x = tx * map.getTileWidth();
            final int y = ty * map.getTileHeight();

            text.drawRect(g, ColorRgba.GREEN, x, y, map.getTileWidth(), map.getTileHeight());
            text.setColor(ColorRgba.YELLOW);
            text.draw(g, x + 20, y + 35, "Tile number: " + tile.getNumber());
            text.draw(g, x + 20, y + 25, "X = " + tx + " | Y = " + ty);
            text.draw(g, x + 20, y + 15, "RX = " + cursor.getX() + " | RY = " + cursor.getY());
            text.draw(g, x + 20, y + 5, "Group: " + mapGroup.getGroup(tile));

            final Transition transition = transitionExtractor.getTransition(tile);
            text.draw(g, x + 20, y - 5, "Transition: " + transition);

            final Circuit circuit = circuitExtractor.getCircuit(tile);
            text.draw(g, x + 20, y - 15, "Circuit: " + circuit);
        }
    }

    @Override
    public void load()
    {
        map.create(Medias.create("level.png"));
        mapViewer.addRenderer(new MapTileRendererModel(services));
        mapGroup.loadGroups(Medias.create("groups.xml"));

        cursor.addImage(0, Medias.create("cursor.png"));
        cursor.load();
        cursor.setArea(0, 0, getWidth(), getHeight());
        cursor.setGrid(map.getTileWidth(), map.getTileHeight());
        cursor.setInputDevice(mouse);
        cursor.setViewer(camera);

        camera.setView(0, 0, getWidth(), getHeight());
        camera.setLimits(map);
    }

    @Override
    public void update(double extrp)
    {
        mouse.update(extrp);
        cursor.update(extrp);

        if (keyboard.isPressedOnce(Keyboard.UP))
        {
            camera.moveLocation(extrp, 0, 64);
        }
        if (keyboard.isPressedOnce(Keyboard.DOWN))
        {
            camera.moveLocation(extrp, 0, -64);
        }
        if (keyboard.isPressedOnce(Keyboard.LEFT))
        {
            camera.moveLocation(extrp, -64, 0);
        }
        if (keyboard.isPressedOnce(Keyboard.RIGHT))
        {
            camera.moveLocation(extrp, 64, 0);
        }
        text.update(camera);
    }

    @Override
    public void render(Graphic g)
    {
        mapViewer.render(g);
        renderTileInfo(g);
        cursor.render(g);
    }

    @Override
    public void onTerminated(boolean hasNextSequence)
    {
        Engine.terminate();
    }
}
