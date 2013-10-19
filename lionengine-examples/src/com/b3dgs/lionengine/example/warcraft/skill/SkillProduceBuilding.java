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

import com.b3dgs.lionengine.ColorRgba;
import com.b3dgs.lionengine.example.warcraft.Cursor;
import com.b3dgs.lionengine.example.warcraft.CursorType;
import com.b3dgs.lionengine.example.warcraft.FactoryProduction;
import com.b3dgs.lionengine.example.warcraft.ProducibleEntity;
import com.b3dgs.lionengine.example.warcraft.entity.EntityType;
import com.b3dgs.lionengine.example.warcraft.entity.UnitWorker;
import com.b3dgs.lionengine.example.warcraft.map.Map;
import com.b3dgs.lionengine.game.purview.Configurable;
import com.b3dgs.lionengine.game.rts.CameraRts;
import com.b3dgs.lionengine.game.rts.ControlPanelModel;
import com.b3dgs.lionengine.game.rts.CursorRts;

/**
 * Skill build implementation.
 */
public abstract class SkillProduceBuilding
        extends Skill
{
    /** Production factory. */
    protected final FactoryProduction factoryProduction;
    /** Entity type to produce. */
    private final EntityType entity;
    /** Production width in tile. */
    private final int width;
    /** Production height in tile. */
    private final int height;
    /** The production cost gold. */
    private final int gold;
    /** The production cost wood. */
    private final int wood;
    /** Cursor reference. */
    private final Cursor cursor;
    /** Map reference. */
    private final Map map;
    /** To produce. */
    private ProducibleEntity toProduce;

    /**
     * Constructor.
     * 
     * @param id The skill id.
     * @param setup The setup skill reference.
     * @param entity The entity type to produce.
     * @param cursor The cursor reference.
     * @param map The map reference.
     */
    protected SkillProduceBuilding(SkillType id, SetupSkill setup, EntityType entity, Cursor cursor, Map map)
    {
        super(id, setup);
        this.cursor = cursor;
        this.map = map;
        this.entity = entity;
        factoryProduction = setup.factoryProduction;
        final Configurable config = factoryProduction.getConfig(entity);
        width = config.getDataInteger("widthInTile", "size");
        height = config.getDataInteger("heightInTile", "size");
        gold = config.getDataInteger("gold", "cost");
        wood = config.getDataInteger("wood", "cost");
        setOrder(true);
    }

    /*
     * Skill
     */

    @Override
    public void updateOnMap(double extrp, CameraRts camera, CursorRts cursor)
    {
        super.updateOnMap(extrp, camera, cursor);
        if (isActive())
        {
            if (map.isAreaAvailable(cursor.getLocationInTileX(), cursor.getLocationInTileY(),
                    toProduce.getWidthInTile(), toProduce.getHeightInTile(), owner.getId().intValue()))
            {
                this.cursor.setBoxColor(ColorRgba.GREEN);
            }
            else
            {
                this.cursor.setBoxColor(ColorRgba.RED);
            }
        }
    }

    @Override
    public void action(ControlPanelModel<?> panel, CursorRts cursor)
    {
        if (!map.isAreaAvailable(cursor.getLocationInTileX(), cursor.getLocationInTileY(), toProduce.getWidthInTile(),
                toProduce.getHeightInTile(), owner.getId().intValue()))
        {
            setActive(true);
            panel.ordered();
            this.cursor.setType(CursorType.BOX);
        }
        else if (owner instanceof UnitWorker && toProduce != null)
        {
            toProduce.setLocation(destX, destY);
            ((UnitWorker) owner).addToProductionQueue(toProduce);
        }
    }

    @Override
    public void onClicked(ControlPanelModel<?> panel)
    {
        if (owner instanceof UnitWorker)
        {
            final UnitWorker worker = (UnitWorker) owner;
            final ProducibleEntity producible = factoryProduction.createProducible(entity, 0, 0);
            if (worker.canProduce(producible))
            {
                cursor.setType(CursorType.BOX);
                cursor.setBoxColor(ColorRgba.GREEN);
                cursor.setBoxSize(width, height);
                toProduce = producible;
            }
            else
            {
                worker.notifyCanNotProduce(producible);
                toProduce = null;
                cursor.setType(CursorType.POINTER);
                setActive(false);
                panel.resetOrder();
            }
        }
    }

    @Override
    public String getDescription()
    {
        final StringBuilder description = new StringBuilder(super.getDescription());
        description.append(":     ").append(gold).append("      ").append(wood);
        return description.toString();
    }
}