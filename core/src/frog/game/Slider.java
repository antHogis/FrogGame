package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Anton on 13.4.2018.
 */

public class Slider extends UiObject {
    private Texture pointTexture;
    private Rectangle pointRectangle;
    private Texture barTexture;
    private Rectangle barRectangle;

    private float outputMin;
    private float outputMax;
    private float output;

    public Slider(float outputMin,
                  float outputMax,
                  float savedOutput,
                  float height,
                  float xPlusWidth,
                  float y) {
        this.outputMin = outputMin;
        this.outputMax = outputMax;

        barTexture = new Texture("ui/buttons/settings/slider_bar.png");
        pointTexture = new Texture("ui/buttons/settings/slider_point.png");

        barRectangle = new Rectangle();
        barRectangle.setHeight(height);
        barRectangle.setWidth((barTexture.getWidth()*barRectangle.getHeight())/barTexture.getHeight());
        barRectangle.setX(xPlusWidth - barRectangle.getWidth());
        barRectangle.setY(y);

        pointRectangle = new Rectangle();
        pointRectangle.setWidth(barRectangle.getHeight());
        pointRectangle.setHeight(barRectangle.getHeight());
        pointRectangle.setY(barRectangle.getY());

        pointRectangle.setX(barRectangle.getX() + barRectangle.getWidth()/2 - pointRectangle.getWidth()/2);
        //convertValue(pointRectangle.x);
        placePoint(savedOutput);
    }

    @Override
    public void draw(SpriteBatch batch) {

        batch.draw(barTexture, barRectangle.x, barRectangle.y, barRectangle.width, barRectangle.height);
        batch.draw(pointTexture, pointRectangle.x, pointRectangle.y, pointRectangle.width, pointRectangle.height);
    }

    public void movePoint(float touchX) {
        if (touchX > barRectangle.x && touchX + pointRectangle.width < barRectangle.x+barRectangle.width) {
            pointRectangle.setX(touchX);
            convertValue(touchX);
        }
    }

    private void convertValue(float touchX) {
        float touchMin = barRectangle.x;
        float touchMax = barRectangle.x + barRectangle.width - pointRectangle.width;
        float touchRange = touchMax - touchMin;
        float outputRange = outputMax - outputMin;

        output = (((touchX - touchMin) * outputRange) / touchRange) + outputMin;
    }

    public void placePoint(float savedOutput) {
        float outputRange = outputMax - outputMin;
        float pointRange = barRectangle.getX() + barRectangle.getWidth() - pointRectangle.getWidth() - barRectangle.getX();

        pointRectangle.setX((((savedOutput - outputMin) * pointRange) / outputRange) + barRectangle.getX());
        Gdx.app.log("Placed point", "output: " + savedOutput);
        Gdx.app.log("Placed point", "x" + pointRectangle.getX());
    }

    public float getOutput() {
        return output;
    }

    @Override
    public float getWidth() {
        return barRectangle.width;
    }

    @Override
    public float getHeight() {
        return barRectangle.height;
    }

    @Override
    public Rectangle getRectangle() {
        return barRectangle;
    }

    @Override
    public void dispose() {
        pointTexture.dispose();
        barTexture.dispose();
    }
}
