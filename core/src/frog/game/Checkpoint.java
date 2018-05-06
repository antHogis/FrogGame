package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Checkpoint saves players position.
 *
 * Upon reaching a Checkpoint, player is returned to it if they are hit by an enemy.
 *
 * @author Tadpole Attack Squad
 * @version 2018.0506
 * @since 2018.0321
 */

public class Checkpoint extends GameObject {
    private boolean cleared;
    private Rectangle zone;

    /**
     * The Constructor of Checkpoint.
     *
     * Creates a Checkpoint and sets the animation and the cleared status. Dimensions and
     * coordinates are set based on the given parameters.
     *
     * @param x X-coordinate of the Checkpoint.
     * @param y Y-coordinate of the Checkpoint.
     * @param width Width of the Checkpoint.
     * @param height Height of the Checkpoint.
     * @param TILE_DIMENSION Dimension of a tile in pixels.
     */
    public Checkpoint(float x, float y, float width, float height, int TILE_DIMENSION) {
        textureSheet = new Texture("gfx/checkpointSheet.png");
        zone = new Rectangle(x, y, width, height);

        SHEET_COLUMNS = 6;
        SHEET_ROWS = 2;
        textureSheet2D = TextureRegion.split(textureSheet,
                textureSheet.getWidth() / SHEET_COLUMNS,
                textureSheet.getHeight() / SHEET_ROWS);
        textureSheet1D = convert2Dto1D(textureSheet2D);

        stateTime = 0f;
        animation = new Animation<TextureRegion>(6/60f, textureSheet1D);
        currentFrame = animation.getKeyFrame(stateTime, false);

        rectangle = new Rectangle();
        rectangle.setX(zone.x);
        rectangle.setY(zone.y-(TILE_DIMENSION/4));
        rectangle.setWidth(TILE_DIMENSION*1.25f);
        rectangle.setHeight((rectangle.width*currentFrame.getRegionHeight())/currentFrame.getRegionWidth());

        cleared = false;
    }

    /**
     * Draws the Checkpoint animations.
     *
     * Draws the idle animations and the opening animation when the Checkpoint is cleared by the
     * player.
     *
     * @param batch SpriteBatch used to draw the animations.
     */
    @Override
    public void drawAnimation(SpriteBatch batch) {
        if (!cleared) {
            batch.draw(currentFrame, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        } else {
            stateTime += Gdx.graphics.getDeltaTime();
            currentFrame = animation.getKeyFrame(stateTime, false);
            batch.draw(currentFrame, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        }
    }

    /**
     * Checks if the player has collided with the checkpoint. Sets the player to return here
     * if they collide with an enemy.
     *
     * @param frog The player character.
     */
    public void checkCollision(Player frog) {
        if (this.zone.overlaps(frog.rectangle) && !cleared) {
            SoundController.playCheckpointSound();
            frog.setLastCheckpointX(this.zone.x);
            frog.setLastCheckpointY(this.zone.y);
            Gdx.app.log("TAG", "Checkpoint Saved!");
            cleared = true;
        }
    }
}
