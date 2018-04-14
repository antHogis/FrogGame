package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;


/**
 * Created by Anton on 13.3.2018.
 */

public class MainMenu implements Screen {
    private FrogMain host;
    private Texture main;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Array<MenuButton> buttons;

    public MainMenu(FrogMain host) {
        this.host = host;
        main = new Texture(Gdx.files.internal("ui/Menu_FI.png"));
        batch = host.getBatch();
        camera = host.getCamera();

        buttons = new Array<MenuButton>();

        addMenuButtons();


    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        checkButtonsIsTouched();

        batch.begin();
        batch.draw(main, 0, 0);
        drawButtons();
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    private void addMenuButtons() {
        buttons.add(new MenuButton(host.getVIEWPORT_WIDTH() * 0.43f, host.getVIEWPORT_HEIGHT() * 0.8f, 200, 100));
        buttons.add(new MenuButton(host.getVIEWPORT_WIDTH() * 0.43f, host.getVIEWPORT_HEIGHT() * 0.6f, 200, 100));
        buttons.add(new MenuButton(host.getVIEWPORT_WIDTH() * 0.43f, host.getVIEWPORT_HEIGHT() * 0.4f, 200, 100));
        buttons.add(new MenuButton(host.getVIEWPORT_WIDTH() * 0.43f, host.getVIEWPORT_HEIGHT() * 0.2f, 200, 100));
    }

    private void drawButtons() {
        for (MenuButton button : buttons) {
            button.draw(batch);
        }
    }

    private void checkButtonsIsTouched() {
        if (buttons.get(0).isTouched(camera)) {
            Gdx.app.log("TAG", "Setting Screen");
            host.setScreen(host.getLevels().get(0));
        }

        if (buttons.get(1).isTouched(camera)) {
            Gdx.app.log("TAG", "Setting Screen");
            host.setScreen(new SettingsMenu(host));
        }

        if (buttons.get(2).isTouched(camera)) {
            Gdx.app.log("TAG", "Setting Screen");
            host.setScreen(new HighScoreScreen(host));
        }

        if (buttons.get(3).isTouched(camera)) {
            Gdx.app.log("TAG", "Exiting App");
            Gdx.app.exit();
        }
    }
}
