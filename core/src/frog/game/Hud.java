package frog.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by anton on 12/04/2018.
 */

public class Hud {
    private Timer timer;

    private boolean returnToMenu;
    private boolean returnToGame;
    private boolean openPrompt;

    private Texture promptTexture;
    private Rectangle promptRectangle;

    private Texture menuButtonTexture;
    private Rectangle menuButtonRectangle;

    private Texture yesButtonTexture;
    private Rectangle yesButtonRectangle;

    private Texture noButtonTexture;
    private Rectangle noButtonRectangle;

    public Hud(float WINDOW_WIDTH, float WINDOW_HEIGHT) {
        timer = new Timer(WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public void draw(SpriteBatch batch, float cameraX, float cameraY) {

    }

    public Timer getTimer() {
        return timer;
    }
}
