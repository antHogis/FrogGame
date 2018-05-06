package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * TimeCoin is an object with a texture of an coin.
 *
 * TimeCoin object subtracts five seconds from the timer upon colliding with the player.
 * It's position is determined in the map files.
 *
 * @author Tadpole Attack Squad
 * @version 2018.0506
 * @since 2018.0506
 */

public class TimeCoin extends GameObject {
    private boolean cleared, subtracted, moveUp;
    private float lowestY, highestY;

    /**
     * Constructor of the TimeCoin
     *
     * Creates a TimeCoin, sets the animation, the highest and lowest points of the up-down movement
     *
     * @param x The X-coordinate of the Coin.
     * @param y The Y-coordinate of the Coin.
     * @param TILE_DIMENSION Dimension of a tile in pixels
     */
    public TimeCoin(float x, float y, int TILE_DIMENSION) {
        textureSheet = new Texture("gfx/coinSheet.png");
        SHEET_COLUMNS = 6;
        SHEET_ROWS = 8;
        textureSheet2D = TextureRegion.split(textureSheet,
                textureSheet.getWidth() / SHEET_COLUMNS,
                textureSheet.getHeight() / SHEET_ROWS);
        textureSheet1D = convert2Dto1D(textureSheet2D);

        stateTime = 1f;
        animation = new Animation<TextureRegion>(4/60f, textureSheet1D);
        currentFrame = animation.getKeyFrame(stateTime, true);

        rectangle = new Rectangle(x, y, TILE_DIMENSION*1.5f, 0);
        rectangle.setHeight((this.rectangle.getWidth()*currentFrame.getRegionHeight())/currentFrame.getRegionWidth());

        lowestY = y;
        highestY = lowestY + (TILE_DIMENSION/2);
        moveUp = true;
    }

    /**
     * Method moves the TimeCoin up and down based on the highestY and lowestY values.
     */
    public void movement() {
        if (moveUp && rectangle.y < highestY) {
            rectangle.setY(rectangle.getY()+40* Gdx.graphics.getDeltaTime());
        } else if (moveUp) {
            moveUp = false;
        }
        if (!moveUp && rectangle.y > lowestY) {
            rectangle.setY(rectangle.getY()-40* Gdx.graphics.getDeltaTime());
        } else if (!moveUp) {
            moveUp = true;
        }
    }

    /**
     * Checks if the Player character has collided with the TimeCoin, and clears the coin if
     * it has.
     *
     * @param frog The player character.
     */
    public void checkCollision(Player frog) {
        if (frog.rectangle.overlaps(this.rectangle)) {
            cleared = true;
        }
    }

    /**
     * Checks if the the TimeCoin has been collected.
     *
     * @return Whether the TimeCoin has been collected or not.
     */
    public boolean isCleared() {
        return cleared;
    }

    /**
     *
     * @return Whether or no the coin has subtracted 5 seconds from the timer.
     */
    public boolean isSubtracted() {
        return subtracted;
    }

    /**
     * Sets the status of subtracted to given value.
     *
     * @param subtracted The state of subtraction.
     */
    public void setSubtracted(boolean subtracted) {
        this.subtracted = subtracted;
    }
}
