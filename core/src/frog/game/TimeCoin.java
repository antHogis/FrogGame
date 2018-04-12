package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Lauri on 24.3.2018.
 */

public class TimeCoin extends GameObject {
    private boolean cleared, subtracted, moveUp;
    private float lowestY, highestY;

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

    public void checkCollision(Player frog) {
        if (frog.rectangle.overlaps(this.rectangle)) {
            cleared = true;
        }
    }

    public boolean isCleared() {
        return cleared;
    }

    public boolean isSubtracted() {
        return subtracted;
    }

    public void setSubtracted(boolean subtracted) {
        this.subtracted = subtracted;
    }
}
