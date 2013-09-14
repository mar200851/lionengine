package com.b3dgs.lionengine.game.purview.model;

import java.util.HashMap;
import java.util.Map;

import com.b3dgs.lionengine.Check;
import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.anim.Anim;
import com.b3dgs.lionengine.anim.Animation;
import com.b3dgs.lionengine.file.File;
import com.b3dgs.lionengine.file.XmlNode;
import com.b3dgs.lionengine.file.XmlNodeNotFoundException;
import com.b3dgs.lionengine.file.XmlParser;
import com.b3dgs.lionengine.game.CollisionData;
import com.b3dgs.lionengine.game.purview.Configurable;

/**
 * Default configurable implementation.
 */
public class ConfigurableModel
        implements Configurable
{
    /** Animations map. */
    private final Map<String, Animation> animations;
    /** Collisions map. */
    private final Map<String, CollisionData> collisions;
    /** Root node. */
    private XmlNode root;

    /**
     * Constructor.
     */
    public ConfigurableModel()
    {
        this(null);
    }

    /**
     * Create from existing configurable.
     * 
     * @param configurable The configurable reference.
     */
    public ConfigurableModel(Configurable configurable)
    {
        if (configurable instanceof ConfigurableModel)
        {
            animations = ((ConfigurableModel) configurable).animations;
            collisions = ((ConfigurableModel) configurable).collisions;
            root = ((ConfigurableModel) configurable).root;
        }
        else
        {
            animations = new HashMap<>(1);
            collisions = new HashMap<>(1);
        }
    }

    /**
     * Get the string from a node.
     * 
     * @param attribute The attribute to get.
     * @param path The attribute node path.
     * @return The string found.
     */
    private String getString(String attribute, String... path)
    {
        XmlNode node = root;
        for (final String element : path)
        {
            try
            {
                node = node.getChild(element);
            }
            catch (final XmlNodeNotFoundException exception)
            {
                throw new LionEngineException(exception, "The following node was not found: ", element);
            }
        }
        return node.readString(attribute);
    }

    /*
     * Configurable
     */

    @Override
    public void loadData(Media media)
    {
        final XmlParser parser = File.createXmlParser();
        root = parser.load(media);
        for (final XmlNode node : root.getChildren("animation"))
        {
            final String anim = node.readString("name");
            final Animation animation = Anim.createAnimation(node.readInteger("start"), node.readInteger("end"),
                    node.readDouble("speed"), node.readBoolean("reversed"), node.readBoolean("repeat"));
            animations.put(anim, animation);
        }
        for (final XmlNode node : root.getChildren("collision"))
        {
            final String coll = node.readString("name");
            final CollisionData collision = new CollisionData(node.readInteger("offsetX"), node.readInteger("offsetY"),
                    node.readInteger("width"), node.readInteger("height"), node.readBoolean("mirror"));
            collisions.put(coll, collision);
        }
    }

    @Override
    public XmlNode getDataRoot()
    {
        return root;
    }

    /**
     * {@inheritDoc} A LionEngineException is thrown if there is a path error.
     */
    @Override
    public String getDataString(String attribute, String... path)
    {
        return getString(attribute, path);
    }

    @Override
    public int getDataInteger(String attribute, String... path)
    {
        return Integer.parseInt(getString(attribute, path));
    }

    @Override
    public boolean getDataBoolean(String attribute, String... path)
    {
        return Boolean.parseBoolean(getString(attribute, path));
    }

    @Override
    public double getDataDouble(String attribute, String... path)
    {
        return Double.parseDouble(getString(attribute, path));
    }

    @Override
    public Animation getDataAnimation(String name)
    {
        final Animation animation = animations.get(name);
        Check.notNull(animation, "Animation does not exist: ", name);
        return animation;
    }

    @Override
    public CollisionData getDataCollision(String name)
    {
        final CollisionData collision = collisions.get(name);
        Check.notNull(collision, "Collision does not exist: ", name);
        return collision;
    }
}
