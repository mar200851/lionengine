/*
 * Copyright (C) 2013-2017 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.graphic.drawable;

import com.b3dgs.lionengine.AnimState;
import com.b3dgs.lionengine.Animation;
import com.b3dgs.lionengine.Animator;

/**
 * Animator mock.
 */
public final class AnimatorMock implements Animator
{
    private final Animator animator = new AnimatorImpl();

    @Override
    public void update(double extrp)
    {
        animator.update(extrp);
    }

    @Override
    public void play(Animation animation)
    {
        animator.play(animation);
    }

    @Override
    public void stop()
    {
        animator.stop();
    }

    @Override
    public void setAnimSpeed(double speed)
    {
        animator.setAnimSpeed(speed);
    }

    @Override
    public void setFrame(int frame)
    {
        animator.setFrame(frame);
    }

    @Override
    public int getFrame()
    {
        return animator.getFrame();
    }

    @Override
    public int getFrameAnim()
    {
        return animator.getFrameAnim();
    }

    @Override
    public AnimState getAnimState()
    {
        return animator.getAnimState();
    }
}
