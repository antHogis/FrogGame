package frog.game;

import com.badlogic.gdx.Gdx;

/**
 * Enemy is an abstract superclass for enemies.
 *
 * Enemy holds common characteristics of all enemies. Contains collision detection and patrol point
 * methods.
 *
 * @author Tadpole Attack Squad
 * @version 2018.0506
 * @since 2018.0324
 */

public abstract class Enemy extends GameObject {

    private float MOVEMENT_STARTPOINT_X;
    private float MOVEMENT_STARTPOINT_Y;
    private float MOVEMENT_ENDPOINT_X;
    private float MOVEMENT_ENDPOINT_Y;

    /**
     * The Enemy's movement, a method for subclasses to implement.
     */
    public abstract void movement();

    /**
     * Checks if the Player and the enemy collide with one another.
     *
     * @param frog The player character.
     * @return Whether or not the player collides with a particular Enemy.
     */
    public boolean collidesWith(Player frog) {
        if (this.rectangle.overlaps(frog.getHitBox())) {
            Gdx.app.log("TAG", "Player killed!");
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @return End point X-coordinate of the patrol path.
     */
    public float getMOVEMENT_ENDPOINT_X() {
        return MOVEMENT_ENDPOINT_X;
    }

    /**
     * Set's the End point X-coordinate of the patrol path.
     *
     * @param MOVEMENT_ENDPOINT_X
     */
    public void setMOVEMENT_ENDPOINT_X(float MOVEMENT_ENDPOINT_X) {
        this.MOVEMENT_ENDPOINT_X = MOVEMENT_ENDPOINT_X;
    }

    /**
     * Set's the End point Y-coordinate of the patrol path.
     *
     * @param MOVEMENT_ENDPOINT_Y
     */
    public void setMOVEMENT_ENDPOINT_Y(float MOVEMENT_ENDPOINT_Y) {
        this.MOVEMENT_ENDPOINT_Y = MOVEMENT_ENDPOINT_Y;
    }

    /**
     *
     * @return End point Y-coordinate of the patrol path.
     */
    public float getMOVEMENT_ENDPOINT_Y() {
        return MOVEMENT_ENDPOINT_Y;

    }

    /**
     *
     * @return Start point X-coordinate of the patrol path.
     */
    public float getMOVEMENT_STARTPOINT_X() {
        return MOVEMENT_STARTPOINT_X;
    }

    /**
     * Set's the Start point X-coordinate of the patrol path.
     *
     * @param MOVEMENT_STARTPOINT_X
     */
    public void setMOVEMENT_STARTPOINT_X(float MOVEMENT_STARTPOINT_X) {
        this.MOVEMENT_STARTPOINT_X = MOVEMENT_STARTPOINT_X;
    }

    /**
     *
     * @return Start point Y-coordinate of the patrol path.
     */
    public float getMOVEMENT_STARTPOINT_Y() {
        return MOVEMENT_STARTPOINT_Y;
    }

    /**
     * Set's the Start point Y-coordinate of the patrol path.
     *
     * @param MOVEMENT_STARTPOINT_Y
     */
    public void setMOVEMENT_STARTPOINT_Y(float MOVEMENT_STARTPOINT_Y) {
        this.MOVEMENT_STARTPOINT_Y = MOVEMENT_STARTPOINT_Y;
    }

}
