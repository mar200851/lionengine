/*
 * Copyright (C) 2013-2014 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test the ratio.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public class RatioTest
{
    /**
     * Test the core class.
     * 
     * @throws NoSuchMethodException If error.
     * @throws IllegalAccessException If error.
     * @throws InstantiationException If error.
     * @throws InvocationTargetException If success.
     */
    @Test(expected = InvocationTargetException.class)
    public void testRatioClass() throws NoSuchMethodException, InstantiationException, IllegalAccessException,
            InvocationTargetException
    {
        final Constructor<Ratio> constructor = Ratio.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        final Ratio ratio = constructor.newInstance();
        Assert.assertNotNull(ratio);
    }

    /**
     * Test the ratio.
     */
    @Test
    public void testRatio()
    {
        Assert.assertFalse(Ratio.equals(Ratio.R16_10, Ratio.R16_9));
        Assert.assertTrue(Ratio.equals(Ratio.R4_3, Ratio.R4_3));
        Assert.assertFalse(Ratio.equals(Ratio.R16_9, Ratio.R4_3));
    }
}