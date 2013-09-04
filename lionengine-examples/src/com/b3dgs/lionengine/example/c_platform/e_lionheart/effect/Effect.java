package com.b3dgs.lionengine.example.c_platform.e_lionheart.effect;

import com.b3dgs.lionengine.Graphic;
import com.b3dgs.lionengine.anim.AnimState;
import com.b3dgs.lionengine.drawable.Drawable;
import com.b3dgs.lionengine.drawable.SpriteAnimated;
import com.b3dgs.lionengine.game.CameraGame;
import com.b3dgs.lionengine.game.effect.EffectGame;
import com.b3dgs.lionengine.game.effect.SetupEffectGame;

/**
 * Effect base implementation.
 */
public class Effect
        extends EffectGame
{
    /** Sprite. */
    private final SpriteAnimated sprite;
    /** Horizontal location. */
    private int x;
    /** Vertical location. */
    private int y;

    /**
     * Constructor.
     * 
     * @param setup The setup reference.
     */
    public Effect(SetupEffectGame setup)
    {
        super(setup.configurable);
        final int horizontalFrames = getDataInteger("horizontal", "frames");
        final int verticalFrames = getDataInteger("vertical", "frames");
        sprite = Drawable.loadSpriteAnimated(setup.surface, horizontalFrames, verticalFrames);
    }

    /**
     * Start the effect.
     * 
     * @param x The horizontal location.
     * @param y The vertical location.
     */
    public void start(int x, int y)
    {
        this.x = x;
        this.y = y;
        sprite.play(getAnimation("start"));
    }

    /*
     * EffectGame
     */

    @Override
    public void update(double extrp)
    {
        sprite.updateAnimation(extrp);
        if (sprite.getAnimState() == AnimState.FINISHED)
        {
            destroy();
        }
    }

    @Override
    public void render(Graphic g, CameraGame camera)
    {
        sprite.render(g, camera.getViewpointX(x), camera.getViewpointY(y + sprite.getFrameHeight()));
    }
}
