package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Lauri on 21.3.2018.
 */

public class Checkpoint extends GameObject {
    private boolean isCleared;
    private Rectangle zone;

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
        animation = new Animation<TextureRegion>(5/60f, textureSheet1D);
        currentFrame = animation.getKeyFrame(stateTime, false);

        rectangle = new Rectangle();
        rectangle.setX(zone.x);
        rectangle.setY(zone.y-(TILE_DIMENSION/4));
        rectangle.setWidth(TILE_DIMENSION*1.25f);
        rectangle.setHeight((rectangle.width*currentFrame.getRegionHeight())/currentFrame.getRegionWidth());

        isCleared = false;
    }

    @Override
    public void drawAnimation(SpriteBatch batch) {
        if (!isCleared) {
            batch.draw(currentFrame, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        } else {
            stateTime += Gdx.graphics.getDeltaTime();
            currentFrame = animation.getKeyFrame(stateTime, false);
            batch.draw(currentFrame, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        }
    }

    public void checkCollision(Player frog) {
        if (this.zone.overlaps(frog.rectangle) && !isCleared) {
            frog.setLastCheckpointX(this.zone.x);
            frog.setLastCheckpointY(this.zone.y);
            Gdx.app.log("TAG", "Checkpoint Saved!");
            isCleared = true;
        }
    }

    public boolean getIsCleared() {
        return this.isCleared;
    }

    public void setIsCleared(boolean x) {
        this.isCleared = x;
    }


}
