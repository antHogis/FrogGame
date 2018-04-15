package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Anton on 13.3.2018.
 */

public class MainMenu extends ScreenAdapter {
    private FrogMain host;
    private Texture background;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Array<MenuButton> buttons;

    public MainMenu(FrogMain host) {
        this.host = host;
        background = new Texture(Gdx.files.internal("ui/bg.png"));
        batch = host.getBatch();
        camera = host.getCamera();

        buttons = new Array<MenuButton>();
        addMenuButtons();
        setInputProcessor();
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        //checkButtonsIsTouched();

        batch.begin();
        batch.draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);
        drawButtons();
        batch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        for (MenuButton button : buttons) {
            button.dispose();
        }
    }

    private void addMenuButtons() {
        float WINDOW_WIDTH = camera.viewportWidth;
        float WINDOW_HEIGHT = camera.viewportHeight;
        
        buttons.add(new MenuButton(WINDOW_WIDTH * 0.43f, WINDOW_HEIGHT * 0.8f, 200, 100));
        buttons.add(new MenuButton(WINDOW_WIDTH * 0.43f, WINDOW_HEIGHT * 0.6f, 200, 100));
        buttons.add(new MenuButton(WINDOW_WIDTH * 0.43f, WINDOW_HEIGHT * 0.4f, 200, 100));
        buttons.add(new MenuButton(WINDOW_WIDTH * 0.43f, WINDOW_HEIGHT * 0.2f, 200, 100));
    }

    private void drawButtons() {
        for (MenuButton button : buttons) {
            button.draw(batch);
        }
    }

    private void setInputProcessor() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touch);

                //Level-select button
                if (buttons.get(0).getRectangle().contains(touch.x,touch.y)) {
                    host.createNewLevels();
                    MainMenu.this.dispose();
                    host.setScreen(host.getLevels().get(0));
                }
                //Settings button
                if (buttons.get(1).getRectangle().contains(touch.x,touch.y)) {
                    MainMenu.this.dispose();
                    host.setScreen(new SettingsMenu(host));
                }
                //High Score button
                if (buttons.get(2).getRectangle().contains(touch.x,touch.y)) {
                    MainMenu.this.dispose();
                    host.setScreen(new HighScoreScreen(host));
                }
                //Exit button
                if (buttons.get(3).getRectangle().contains(touch.x,touch.y)) {
                    MainMenu.this.dispose();
                    host.setScreen(new HighScoreScreen(host));
                }

                return true;
            }
        });
    }

}
