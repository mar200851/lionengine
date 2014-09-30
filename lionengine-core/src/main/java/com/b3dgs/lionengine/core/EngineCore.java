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
package com.b3dgs.lionengine.core;

import com.b3dgs.lionengine.Check;
import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.Version;

/**
 * Engine base implementation. This class is intended to be inherited by an engine implementation depending of the
 * library used (as it is done for AWT, SWT and Android engine implementation).
 * 
 * @author Pierre-Alexandre
 */
public abstract class EngineCore
{
    /** Engine name. */
    public static final String NAME = "LionEngine";
    /** Engine version. */
    public static final String VERSION = "7.0.0";
    /** Engine begin date. */
    public static final String BEGIN_DATE = "13 June 2010";
    /** Engine last release date. */
    public static final String LAST_RELEASE_DATE = "4 September 2014";
    /** Engine author. */
    public static final String AUTHOR = "Pierre-Alexandre";
    /** Engine website. */
    public static final String WEBSITE = "http://lionengine.b3dgs.com";
    /** Error message engine already started. */
    private static final String ERROR_STARTED_ALREADY = "The engine has already been started !";
    /** Error message engine not started. */
    private static final String ERROR_STARTED_NOT = "The engine has not been started !";
    /** Engine starting. */
    private static final String ENGINE_STARTING = "Starting \"LionEngine ";
    /** Engine terminated. */
    private static final String ENGINE_TERMINATED = "LionEngine terminated";
    /** Started engine flag. */
    private static boolean started = false;
    /** User program name. */
    private static String programName;
    /** User program version. */
    private static Version programVersion;

    /**
     * Start engine. Has to be called before anything and only one time, in the main.
     * 
     * @param name The program name (must not be <code>null</code>).
     * @param version The program version (must not be <code>null</code>).
     * @param level The verbose level (must not be <code>null</code>).
     * @param factoryGraphic The graphic factory (must not be <code>null</code>).
     * @param factoryMedia The media factory (must not be <code>null</code>).
     * @throws LionEngineException If the engine has already been started.
     */
    public static void start(String name, Version version, Verbose level, FactoryGraphic factoryGraphic,
            FactoryMedia factoryMedia) throws LionEngineException
    {
        if (EngineCore.started)
        {
            throw new LionEngineException(EngineCore.ERROR_STARTED_ALREADY);
        }

        Check.notNull(name);
        Check.notNull(version);
        Check.notNull(level);
        Check.notNull(factoryGraphic);
        Check.notNull(factoryMedia);

        Verbose.set(level);
        Verbose.prepareLogger();

        EngineCore.programName = name;
        EngineCore.programVersion = version;

        final StringBuilder message = new StringBuilder(EngineCore.ENGINE_STARTING);
        message.append(EngineCore.VERSION).append("\" for \"");
        message.append(EngineCore.programName).append(" ");
        message.append(EngineCore.programVersion).append("\"");
        Verbose.info(message.toString());

        FactoryGraphicProvider.setFactoryGraphic(factoryGraphic);
        FactoryMediaProvider.setFactoryMedia(factoryMedia);

        EngineCore.started = true;
    }

    /**
     * Terminate the engine. It is necessary to call this function only if the engine need to be started again during
     * the same jvm execution. This function is automatically called when the program life cycle reach the end.
     * 
     * @throws LionEngineException If the engine has not been started.
     */
    public static void terminate() throws LionEngineException
    {
        if (!EngineCore.started)
        {
            throw new LionEngineException(EngineCore.ERROR_STARTED_NOT);
        }

        FactoryGraphicProvider.setFactoryGraphic(null);
        FactoryMediaProvider.setFactoryMedia(null);

        EngineCore.programName = null;
        EngineCore.programVersion = null;
        EngineCore.started = false;

        Verbose.info(EngineCore.ENGINE_TERMINATED);
    }

    /**
     * Get the program name.
     * 
     * @return The program name.
     * @throws LionEngineException If the engine has not been started.
     */
    public static String getProgramName() throws LionEngineException
    {
        if (!EngineCore.started)
        {
            throw new LionEngineException(EngineCore.ERROR_STARTED_NOT);
        }
        return EngineCore.programName;
    }

    /**
     * Get the program version.
     * 
     * @return The program version.
     * @throws LionEngineException If the engine has not been started.
     */
    public static Version getProgramVersion() throws LionEngineException
    {
        if (!EngineCore.started)
        {
            throw new LionEngineException(EngineCore.ERROR_STARTED_NOT);
        }
        return EngineCore.programVersion;
    }

    /**
     * Check if engine is started.
     * 
     * @return <code>true</code> if started, <code>false</code> else.
     */
    public static boolean isStarted()
    {
        return EngineCore.started;
    }

    /**
     * Get the system property. If the property is not valid due to a {@link SecurityException}, an empty string is
     * returned.
     * 
     * @param property The system property.
     * @param def The default value used if property is not available.
     * @return The system property value (<code>null</code> if there is not any corresponding property).
     */
    public static String getSystemProperty(String property, String def)
    {
        try
        {
            return System.getProperty(property);
        }
        catch (final SecurityException exception)
        {
            Verbose.exception(EngineCore.class, "getSystemProperty", exception);
            return def;
        }
    }
}
