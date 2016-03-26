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
package com.b3dgs.lionengine.editor.project;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Verbose;
import com.b3dgs.lionengine.editor.Activator;
import com.b3dgs.lionengine.editor.properties.PropertiesPart;
import com.b3dgs.lionengine.editor.utility.Focusable;
import com.b3dgs.lionengine.editor.utility.UtilExtension;
import com.b3dgs.lionengine.editor.utility.UtilPart;
import com.b3dgs.lionengine.editor.utility.UtilTree;
import com.b3dgs.lionengine.editor.utility.control.UtilSwt;
import com.b3dgs.lionengine.game.Configurer;
import com.b3dgs.lionengine.game.object.ObjectConfig;

/**
 * Represents the resources explorer, depending of the opened project.
 */
public final class ProjectPart implements Focusable
{
    /** ID. */
    public static final String ID = Activator.PLUGIN_ID + ".part.project";
    /** Menu ID. */
    public static final String MENU_ID = ProjectPart.ID + ".menu";
    /** Error open file. */
    private static final String ERROR_UNABLE_TO_OPEN_FILE = "Unable to open file: ";

    /**
     * Update the properties view with the selected media. Shows object properties, or nothing if not an object.
     * 
     * @param media The selected media.
     */
    private static void updateProperties(Media media)
    {
        final PropertiesPart part = UtilPart.getPart(PropertiesPart.ID, PropertiesPart.class);
        if (!updateIfObject(part, media))
        {
            part.setInput(part.getTree(), (Configurer) null);
        }
    }

    /**
     * Update the properties with object content if valid.
     * 
     * @param part The properties part reference.
     * @param media The selected media.
     * @return <code>true</code> if updated with object, <code>false</code> else.
     */
    private static boolean updateIfObject(PropertiesPart part, Media media)
    {
        if (media != null && Property.DATA.is(media))
        {
            final Configurer configurer = new Configurer(media);
            if (ObjectConfig.NODE_OBJECT.equals(configurer.getRoot().getNodeName()))
            {
                part.setInput(part.getTree(), configurer);
                return true;
            }
        }
        return false;
    }

    /**
     * Update tree selection item.
     * 
     * @param item The selected item.
     */
    private static void updateSelection(TreeItem item)
    {
        if (item.getData() instanceof Media)
        {
            final Media media = (Media) item.getData();
            ProjectModel.INSTANCE.setSelection(media);
            updateProperties(media);
        }
        else
        {
            ProjectModel.INSTANCE.setSelection(null);
            updateProperties(null);
        }
    }

    /** Watcher. */
    private final FolderModificationWatcher watcher;
    /** Tree viewer. */
    private Tree tree;
    /** Tree creator. */
    private ProjectTreeCreator projectTreeCreator;

    /**
     * Create the part.
     */
    public ProjectPart()
    {
        watcher = new FolderModificationWatcher();
    }

    /**
     * Create the composite.
     * 
     * @param parent The parent reference.
     * @param menuService The menu service reference.
     */
    @PostConstruct
    public void createComposite(Composite parent, EMenuService menuService)
    {
        parent.setLayout(UtilSwt.borderless());

        tree = new Tree(parent, SWT.NONE);
        tree.setLayoutData(new GridData(GridData.FILL_BOTH));
        tree.addMouseTrackListener(UtilSwt.createFocusListener(this));
        tree.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseDoubleClick(MouseEvent mouseEvent)
            {
                expandOnDoubleClick();
                checkOpenFile();
            }
        });
        UtilTree.setAction(tree, () -> updateSelection());
        tree.addMenuDetectListener(menuDetectEvent -> updateMenu());
        menuService.registerContextMenu(tree, ProjectPart.MENU_ID);
    }

    /**
     * Add an item to the project tree.
     * 
     * @param media The media item.
     * @param item The item file.
     * @param icon The media icon.
     */
    public void addTreeItem(Media media, File item, Image icon)
    {
        final TreeItem parent = (TreeItem) tree.getData(media.getPath());
        projectTreeCreator.createItem(parent, item, icon);
    }

    /**
     * Remove an item from the project tree.
     * 
     * @param media The media item.
     */
    public void removeTreeItem(Media media)
    {
        final TreeItem item = (TreeItem) tree.getData(media.getPath());
        item.dispose();
    }

    /**
     * Set the project main folders.
     * 
     * @param project The project reference.
     * @throws LionEngineException If error while reading project children.
     */
    public void setInput(Project project)
    {
        tree.removeAll();

        projectTreeCreator = new ProjectTreeCreator(project, tree);
        projectTreeCreator.start();

        watcher.stop();
        watcher.start(project, tree, projectTreeCreator);

        tree.layout();

        tree.getDisplay().asyncExec(() ->
        {
            for (final TreeItem item : tree.getItems())
            {
                item.setExpanded(true);
                for (final TreeItem child : item.getItems())
                {
                    child.setExpanded(true);
                }
            }
        });
    }

    /**
     * Close the project.
     */
    public void close()
    {
        watcher.terminate();
    }

    /**
     * Set the focus.
     */
    @Override
    @Focus
    public void focus()
    {
        tree.setFocus();
    }

    /**
     * Auto expand selected item on double click.
     */
    void expandOnDoubleClick()
    {
        if (!tree.isDisposed())
        {
            for (final TreeItem item : tree.getSelection())
            {
                item.setExpanded(!item.getExpanded());
            }
        }
    }

    /**
     * Check file opening depending of its type.
     */
    void checkOpenFile()
    {
        final Media media = ProjectModel.INSTANCE.getSelection();
        if (media != null && !checkResource(media) && media.getFile().isFile())
        {
            try
            {
                java.awt.Desktop.getDesktop().open(media.getFile());
            }
            catch (final IOException exception)
            {
                // Not able to open the file, just skip
                Verbose.exception(exception, ERROR_UNABLE_TO_OPEN_FILE, media.getFile().getAbsolutePath());
            }
        }
    }

    /**
     * Update tree selection by storing it.
     */
    private void updateSelection()
    {
        if (!tree.isDisposed())
        {
            final Object data = tree.getSelection()[0];
            if (data instanceof TreeItem)
            {
                final TreeItem item = (TreeItem) data;
                updateSelection(item);
            }
        }
    }

    /**
     * Update the menu on detection.
     */
    private void updateMenu()
    {
        if (!tree.isDisposed())
        {
            final Menu menu = tree.getMenu();
            if (menu != null)
            {
                menu.setVisible(false);
                tree.update();
            }
        }
    }

    /**
     * Check if media opening is supported.
     * 
     * @param media The media to open.
     * @return <code>true</code> if opened, <code>false</code> else.
     */
    private boolean checkResource(Media media)
    {
        for (final ResourceChecker checker : UtilExtension.get(ResourceChecker.class, ResourceChecker.EXTENSION_ID))
        {
            if (checker.check(tree.getShell(), media))
            {
                return true;
            }
        }
        return false;
    }
}
