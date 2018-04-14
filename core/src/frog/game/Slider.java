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
    private Texture point;
    private Rectangle pointRectangle;
    private Texture bar;
    private Rectangle barRectangle;

    private float outputMin;
    private float outputMax;
    private float output;

    public Slider(float outputMin, float outputMax, float WINDOW_WIDTH, float WINDOW_HEIGHT) {
        this.outputMin = outputMin;
        this.outputMax = outputMax;

        bar = new Texture("bar.png");
        point = new Texture("point.png");

        barRectangle = new Rectangle();
        barRectangle.setWidth(WINDOW_WIDTH / 2);
        barRectangle.setHeight((barRectangle.getWidth() * bar.getHeight()) / bar.getWidth());
        barRectangle.setX(WINDOW_WIDTH / 2 - barRectangle.getWidth() / 2);
        barRectangle.setY(WINDOW_HEIGHT / 2 - barRectangle.getHeight() / 2);

        pointRectangle = new Rectangle();
        pointRectangle.setWidth(barRectangle.getHeight());
        pointRectangle.setHeight(barRectangle.getHeight());
        pointRectangle.setX(barRectangle.getX() + barRectangle.getWidth()/2 - pointRectangle.getWidth()/2);
        pointRectangle.setY(barRectangle.getY());

        convertValue(pointRectangle.x);
    }

    @Override
    public void draw(SpriteBatch batch) {

        batch.draw(bar, barRectangle.x, barRectangle.y, barRectangle.width, barRectangle.height);
        batch.draw(point, pointRectangle.x, pointRectangle.y, pointRectangle.width, pointRectangle.height);
    }

    public void processInput(OrthographicCamera camera) {
        Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPos);
        if (barRectangle.contains(touchPos.x, touchPos.y)) {
            movePoint(touchPos.x);
        }
    }

    private void movePoint(float touchX) {
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
}
