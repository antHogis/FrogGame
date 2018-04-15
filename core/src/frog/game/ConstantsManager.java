package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

/**
 * Created by Anton on 15.4.2018.
 */

public class ConstantsManager {
    public static Preferences settings = Gdx.app.getPreferences("Preferences");

    public static Locale locale = Locale.getDefault();
    public static I18NBundle bundle =
            I18NBundle.createBundle(Gdx.files.internal("MyBundle"), locale);
    public static I18NBundle levels =
            I18NBundle.createBundle(Gdx.files.internal("levels"));

    //File locations (non-localized)
    public static final String homeButtonPath = "ui/buttons/button-home.png";

    //Game-modifying values
    public static final float MIN_SPEED = 200f;
    public static final float MAX_SPEED = 300f;
    public static final float DEFAULT_SPEED = 250f;

    public static final float MIN_THRESHOLD = 0f;
    public static final float MAX_THRESHOLD = 2f;
    public static final float DEFAULT_THRESHOLD = 0.35f;

    public static final boolean DEFAULT_INVERT_Y = true;

    public static final int TILE_DIMENSION = 200;

}
