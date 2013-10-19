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
package com.b3dgs.lionengine;

/**
 * Handle timer operation, in milli seconds, system clock independent.
 * <p>
 * Example:
 * </p>
 * 
 * <pre>
 * final Timing timer = new Timing();
 * Assert.assertFalse(timer.isStarted());
 * timer.start();
 * Assert.assertTrue(timer.isStarted());
 * 
 * Thread.sleep(100);
 * 
 * Assert.assertTrue(timer.isStarted());
 * Assert.assertTrue(timer.elapsed(100));
 * Assert.assertTrue(timer.elapsed() &gt;= 100);
 * timer.pause();
 * 
 * Thread.sleep(100);
 * 
 * timer.unpause();
 * Assert.assertFalse(timer.elapsed(200000000));
 * timer.stop();
 * Assert.assertFalse(timer.isStarted());
 * Assert.assertTrue(timer.get() &gt;= 0);
 * </pre>
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public final class Timing
{
    /** Nano millisecond. */
    private static final long NANO_TO_MILLI = 1000000L;

    /**
     * Get the system time.
     * 
     * @return The system time.
     */
    private static long systemTime()
    {
        return System.nanoTime() / Timing.NANO_TO_MILLI;
    }

    /** Current time. */
    private long cur;
    /** Time when pause. */
    private long back;
    /** Started flag. */
    private boolean started;

    /**
     * Create a new timer.
     */
    public Timing()
    {
        cur = 0L;
        started = false;
    }

    /**
     * Start timer. Can be started only if not already started.
     */
    public void start()
    {
        if (!started)
        {
            cur = Timing.systemTime();
            started = true;
        }
    }

    /**
     * Stop and reset timer.
     */
    public void stop()
    {
        cur = 0L;
        started = false;
    }

    /**
     * Pause timer.
     */
    public void pause()
    {
        back = Timing.systemTime();
    }

    /**
     * Continue timer from last pause.
     */
    public void unpause()
    {
        cur += Timing.systemTime() - back;
        back = 0L;
    }

    /**
     * Check if specific time has been elapsed.
     * 
     * @param time The time to check in milliseconds.
     * @return <code>true</code> if timer elapsed this time, <code>false</code> else.
     */
    public boolean elapsed(long time)
    {
        if (started)
        {
            return Timing.systemTime() - cur >= time;
        }
        return false;
    }

    /**
     * Get number of milli seconds elapsed since start call.
     * 
     * @return The number of milli seconds elapsed since start call.
     */
    public long elapsed()
    {
        if (started)
        {
            return Timing.systemTime() - cur;
        }
        return 0;
    }

    /**
     * Set the timing value.
     * 
     * @param value The value to set.
     */
    public void set(long value)
    {
        cur = value;
        started = true;
    }

    /**
     * Get timer value.
     * 
     * @return The timer value.
     */
    public long get()
    {
        return cur;
    }

    /**
     * Check if timer started.
     * 
     * @return <code>true</code> if started, <code>false</code> else.
     */
    public boolean isStarted()
    {
        return started;
    }
}