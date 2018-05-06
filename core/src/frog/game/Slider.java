package frog.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * A slider.
 *
 * <p>A graphical tool with which the user can assign a variable with a value in a given range.
 * Contains a bar which represents the range, and a point which represents the output value of the slider.</p>
 *
 * @author Tadpole Attack Squad
 * @version 2018.0506
 * @since 2018.0413
 */
public class Slider extends UiObject {
    private Texture pointTexture;
    private Rectangle pointRectangle;
    private Texture barTexture;
    private Rectangle barRectangle;

    private float outputMin;
    private float outputMax;
    private float output;

    /**
     * The constructor for Slider.
     *
     * Initializes the slider's textures and it's XY coordinates. Also determines what values the slider should output,
     * and what the initial value of the slider should be, so that the point can be placed accordingly.
     *
     * @param outputMin the lowest value in the range
     * @param outputMax the highest value in the range
     * @param savedOutput the value within the range that the slider should be initialized with
     * @param height the height of the slider's textures
     * @param xPlusWidth the x position of the slider's right edge
     * @param y the slider's y position
     */
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
        placePoint(savedOutput);
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(barTexture, barRectangle.x, barRectangle.y, barRectangle.width, barRectangle.height);
        batch.draw(pointTexture, pointRectangle.x, pointRectangle.y, pointRectangle.width, pointRectangle.height);
    }

    /**
     * Moves the point on the slider.
     *
     * Moves the point on the slider according to the user's touch input, and calls convertValue to convert the
     * point's position into an output within the slider's output range.
     *
     * @param touchX the xCoordinate of the user's touch input.
     */
    public void movePoint(float touchX) {
        if (touchX > barRectangle.x && touchX + pointRectangle.width < barRectangle.x+barRectangle.width) {
            pointRectangle.setX(touchX);
            convertValue(touchX);
        }
    }

    /**
     * Converts the slider's point's position into the output.
     *
     * Converts a value of the point's X coordinate within the range of the coordinates of the slider's left and right edges
     * to the output value within the range of the slider's minimum and maximum output.
     *
     * @param touchX the value of the X coordinates where the point is located
     */
    private void convertValue(float touchX) {
        float touchMin = barRectangle.x;
        float touchMax = barRectangle.x + barRectangle.width - pointRectangle.width;
        float touchRange = touchMax - touchMin;
        float outputRange = outputMax - outputMin;

        output = (((touchX - touchMin) * outputRange) / touchRange) + outputMin;
    }

    /**
     * Places the slider's point's position according to the slider's latest or default output.
     *
     * Performs a similar function to convertValue(), but converts the output value to a the point's coordinates.
     *
     * @param savedOutput
     */
    private void placePoint(float savedOutput) {
        float outputRange = outputMax - outputMin;
        float pointRange = barRectangle.getX() + barRectangle.getWidth() - pointRectangle.getWidth() - barRectangle.getX();

        pointRectangle.setX((((savedOutput - outputMin) * pointRange) / outputRange) + barRectangle.getX());
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
