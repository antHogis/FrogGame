package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * RoundFish is a subclass of Enemy with a fish texture.
 *
 * RoundFish is used in Level class as an enemy that travels in a straight line along
 * the X-axis.
 *
 * @author Tadpole Attack Squad
 * @version 2018.0506
 * @since 2018.0324
 */

public class RoundFish extends Enemy {
    private float movementSpeed;
    private boolean isMovingRight;

    /**
     * The Constructor of RoundFish
     *
     * Creates a RoundFish, sets the animation, hit-box, movement speed, and direction of the RoundFish.
     *
     * @param TILE_DIMENSION Dimension of a tile in pixels.
     */
    public RoundFish(int TILE_DIMENSION) {
        textureSheet = new Texture("gfx/roundFishSheet.png");
        SHEET_COLUMNS = 4;
        SHEET_ROWS = 2;
        textureSheet2D = TextureRegion.split(textureSheet,
                textureSheet.getWidth() / SHEET_COLUMNS,
                textureSheet.getHeight() / SHEET_ROWS);
        textureSheet1D = convert2Dto1D(textureSheet2D);

        stateTime = 1f;
        animation = new Animation<TextureRegion>(5/60f, textureSheet1D);
        currentFrame = animation.getKeyFrame(stateTime, true);

        rectangle = new Rectangle(0, 0,TILE_DIMENSION,0);
        rectangle.setHeight((currentFrame.getRegionHeight()*rectangle.getWidth())/currentFrame.getRegionWidth());
        this.movementSpeed = 128f;
        this.isMovingRight = true;
    }

    /**
     * Moves the RoundFish.
     *
     * Moves the RoundFish towards the start or the end of it's linear patrol path along the X-axis.
     * Switches direction upon reaching either point.
     */
    @Override
    public void movement() {

        if(this.rectangle.x+this.rectangle.width < this.getMOVEMENT_ENDPOINT_X() && isMovingRight) {
            this.setX(this.getX()+Gdx.graphics.getDeltaTime()*movementSpeed);

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
