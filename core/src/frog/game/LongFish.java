package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * LongFish is a subclass of Enemy with an eel texture.
 *
 * LongFish is used in Level class as an enemy that travels in a straight line along
 * the X-axis.
 *
 * @author Tadpole Attack Squad
 * @version 2018.0506
 * @since 2018.0402
 */

public class LongFish extends Enemy {
    private float movementSpeed;
    private boolean isMovingRight;

    /**
     * The Constructor of LongFish
     *
     * Creates a LongFish, sets the animation, hit-box, movement speed, and direction of the LongFish.
     *
     * @param TILE_DIMENSION Dimension of a tile in pixels.
     */
    public LongFish(int TILE_DIMENSION) {
        textureSheet = new Texture("gfx/longFishSheet.png");
        SHEET_COLUMNS = 4;
        SHEET_ROWS = 2;
        textureSheet2D = TextureRegion.split(textureSheet,
                textureSheet.getWidth() / SHEET_COLUMNS,
                textureSheet.getHeight() / SHEET_ROWS);
        textureSheet1D = convert2Dto1D(textureSheet2D);

        stateTime = 1f;
        animation = new Animation<TextureRegion>(4/60f, textureSheet1D);
        currentFrame = animation.getKeyFrame(stateTime, true);

        rectangle = new Rectangle(0, 0,TILE_DIMENSION*2,0);
        rectangle.setHeight((currentFrame.getRegionHeight()*rectangle.getWidth())/currentFrame.getRegionWidth());
        movementSpeed = 150f;
        isMovingRight = true;
    }

    /**
     * Moves the LongFish.
     *
     * Moves the LongFish towards the start or the end of it's linear patrol path along the X-axis.
     * Switches direction upon reaching either point.
     */
    @Override
    public void movement() {

        if(this.rectangle.x+this.rectangle.width < this.getMOVEMENT_ENDPOINT_X() && isMovingRight) {
            this.setX(this.getX()+ Gdx.graphics.getDeltaTime()*movementSpeed);

        } else if (isMovingRight) {
            isMovingRight = false;
            flip(animation, true, false);
        }

        if(this.rectangle.x > this.getMOVEMENT_STARTPOINT_X() && !isMovingRight) {
            this.setX(this.getX()-Gdx.graphics.getDeltaTime()*movementSpeed);

        } else if (!isMovingRight) {
            isMovingRight = true;
            flip(animation, true, false);
        }

    }
}
