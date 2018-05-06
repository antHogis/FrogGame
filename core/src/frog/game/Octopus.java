package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Octopus is a subclass of Enemy with an octopus texture.
 *
 * Octopus is used in Level class as an enemy that travels in a straight line along
 * the Y-axis.
 *
 * @author Tadpole Attack Squad
 * @version 2018.0506
 * @since 2018.0404
 */

public class Octopus extends Enemy {
    private float movementSpeed;
    private boolean isMovingDown;

    /**
     * The Constructor of Octopus
     *
     * Creates an Octopus, sets the animation, hit-box, movement speed, and direction of the Octopus.
     *
     * @param TILE_DIMENSION Dimension of a tile in pixels.
     */
    public Octopus(int TILE_DIMENSION) {
        textureSheet = new Texture("gfx/mustekalasheet.png");
        SHEET_COLUMNS = 5;
        SHEET_ROWS = 2;
        textureSheet2D = TextureRegion.split(textureSheet,
                textureSheet.getWidth() / SHEET_COLUMNS,
                textureSheet.getHeight() / SHEET_ROWS);
        textureSheet1D = convert2Dto1D(textureSheet2D);

        stateTime = 1f;
        animation = new Animation<TextureRegion>(4/60f, textureSheet1D);
        currentFrame = animation.getKeyFrame(stateTime, true);

        rectangle = new Rectangle(0, 0,TILE_DIMENSION*3,0);
        rectangle.setHeight((currentFrame.getRegionHeight()*rectangle.getWidth())/currentFrame.getRegionWidth());
        this.movementSpeed = 128f;
        this.isMovingDown = true;
    }

    /**
     * Moves the Octopus.
     *
     * Moves the Octopus towards the start or the end of it's linear patrol path along the Y-axis.
     * The start point is higher in the y-axis than the end point.
     * Switches direction upon reaching either point.
     */
    @Override
    public void movement() {

        if(this.rectangle.y > this.getMOVEMENT_ENDPOINT_Y() && isMovingDown) {
            this.setY(this.getY()- Gdx.graphics.getDeltaTime()*movementSpeed);

        } else if (isMovingDown) {
            isMovingDown = false;
        }
        if(this.rectangle.y < this.getMOVEMENT_STARTPOINT_Y() && !isMovingDown) {
            this.setY(this.getY()+Gdx.graphics.getDeltaTime()*movementSpeed);

        } else if (!isMovingDown) {
            isMovingDown = true;
        }

    }
}
