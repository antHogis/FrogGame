package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * GameObject is an abstract superclass for in-game objects.
 *
 * GameObjects contains common characteristics and methods for in-game objects.
 *
 * @author Tadpole Attack Squad
 * @version 2018.0506
 * @since 2018.0310
 */

abstract class GameObject {
    /**
     * Rectangle of an GameObject for calculation purposes.
     */
    Rectangle rectangle;

    /**
     * The texture of an GameObject.
     */
    Texture texture;

    /**
     * The texture sheet of the GameObjects animation.
     */
    Texture textureSheet;

    /**
     * The texture sheet in a 2D array.
     */
    TextureRegion [][] textureSheet2D;

    /**
     * The texture sheet in an array.
     */
    TextureRegion [] textureSheet1D;

    /**
     * Number of columns in a texture sheet.
     */
    int SHEET_COLUMNS;

    /**
     * Number of rows in a texture sheet.
     */
    int SHEET_ROWS;

    /**
     * Animation of based on the texture sheet.
     */
    Animation<TextureRegion> animation;

    /**
     * Amount of time spent in a state of animation.
     */
    float stateTime;

    /**
     * Current frame of the animation.
     */
    TextureRegion currentFrame;



    /**
     * Draws the texture using the given SpriteBatch.
     *
     * @param batch SpriteBatch used to draw the textures.
     */
    public void draw(SpriteBatch batch) {
        batch.draw(this.texture, this.rectangle.x, this.rectangle.y, this.rectangle.width, this.rectangle.height);
    }

    /**
     * Draws and loops the animation using the given SpriteBatch
     *
     * @param batch SpriteBatch used to draw the animation.
     */
    public void drawAnimation(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = animation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public float getX() {
        return this.rectangle.x;
    }

    public float getY() {
        return this.rectangle.y;
    }

    public void setX(float value) { this.rectangle.x = value; }

    public void setY(float value) {
        this.rectangle.y = value;
    }

    public float getWidth() {
        return this.rectangle.width;
    }

    public float getHeight() {
        return this.rectangle.height;
    }

    public void setWidth(float width) {
        this.rectangle.width = width;
    }

    public void setHeight(float height) {
        this.rectangle.height = height;
    }

    public Texture getTexture() {
        return this.texture;
    }

    /**
     * Converts the given two dimensional array into a one dimensional array.
     *
     * @param twoDim The texture sheet as a 2D array.
     * @return The texture sheet as a one dimensional array.
     */
    public TextureRegion[] convert2Dto1D (TextureRegion[][] twoDim) {
        TextureRegion [] oneDim = new TextureRegion[twoDim.length * twoDim[0].length];
        int index = 0;

        for (int i = 0; i < twoDim.length; i++) {
            for (int j = 0; j < twoDim[i].length; j++) {
                oneDim[index++] = twoDim[i][j];
            }
        }

        return oneDim;
    }

    /**
     * Flips the animation of a particular GameObject
     *
     * @param animation Animation to be flipped.
     * @param xFlip
     * @param yFlip
     */
    public void flip(Animation<TextureRegion> animation, boolean xFlip, boolean yFlip) {
        TextureRegion[] regions = animation.getKeyFrames();
        for(TextureRegion r : regions) {
            r.flip(xFlip, yFlip);
        }
    }

    /**
     * Disposes textures and texturesheets.
     */
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
        if (textureSheet != null) {
            textureSheet.dispose();
        }
    }

}
