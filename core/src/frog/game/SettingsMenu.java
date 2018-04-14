package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Anton on 13.4.2018.
 */

public class SettingsMenu extends ScreenAdapter {
    private FrogMain host;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private final float WINDOW_WIDTH, WINDOW_HEIGHT;

    private TextItem senisitivty_Text;
    private TextItem threshhold_Text;
    private TextItem invertY_Text;

    public SettingsMenu(FrogMain host) {
        this.host = host;
        camera = host.getCamera();
        batch = host.getBatch();
        WINDOW_WIDTH = camera.viewportWidth;
        WINDOW_HEIGHT = camera.viewportHeight;

        createUI();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        drawUI();
        batch.end();
    }

    private void createUI() {
        final float TEXT_HEIGHT = WINDOW_HEIGHT * (1.5f/16);

        senisitivty_Text = new TextItem("ui/text_fi/herkkyys.png", TEXT_HEIGHT);
        senisitivty_Text.setX(WINDOW_WIDTH/16);
        senisitivty_Text.setY(WINDOW_HEIGHT*(12/16) - TEXT_HEIGHT/2);

        threshhold_Text = new TextItem("ui/text_fi/raja-arvo.png", TEXT_HEIGHT);
        threshhold_Text.setX(senisitivty_Text.getX());
        threshhold_Text.setY(WINDOW_HEIGHT*(9/16) - TEXT_HEIGHT/2);

        invertY_Text = new TextItem("ui/text_fi/herkkyys.png", TEXT_HEIGHT);
        invertY_Text.setX(senisitivty_Text.getX());
        invertY_Text.setY(WINDOW_HEIGHT*(6/16) - TEXT_HEIGHT/2);
    }

    private void drawUI() {
        senisitivty_Text.draw(batch);
        threshhold_Text.draw(batch);
        invertY_Text.draw(batch);
    }
}
