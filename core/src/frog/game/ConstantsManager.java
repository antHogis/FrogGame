package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

/**
 * Created by Anton on 15.4.2018.
 */

public class ConstantsManager {
    public static Preferences settings = Gdx.app.getPreferences("Preferences");

    //public static Locale locale = new Locale("fi", "FI");
    private static Locale locale = Locale.getDefault();
    public static I18NBundle myBundle =
            I18NBundle.createBundle(Gdx.files.internal("MyBundle"), locale);
    public static I18NBundle levels =
            I18NBundle.createBundle(Gdx.files.internal("levels"));

    /*
     * File locations (non-localized)
     */
    //Ui Items
    public static final String bgGenericPath = "ui/bg.png";
    public static final String bgMainMenuPath = "ui/bg2.png";
    public static final String homeButtonPath = "ui/buttons/button-home.png";
    public static final String starGoldenPath = "ui/star-gold.png";
    public static final String starGreyPath = "ui/star-grey.png";
    public static final String musicOnPath = "ui/buttons/music-on.png";
    public static final String musicOffPath = "ui/buttons/music-off.png";

    /*
     * Game-modifying values
     */
    public static final float MIN_SPEED = 200f;
    public static final float MAX_SPEED = 300f;
    public static final float DEFAULT_SPEED = 250f;

    public static final float MIN_THRESHOLD = 0f;
    public static final float MAX_THRESHOLD = 2f;
    public static final float DEFAULT_THRESHOLD = 0.35f;
    public static final boolean DEFAULT_INVERT_Y = true;

    public static final boolean DEFAULT_AUDIO_ON = true;

    public static final int TILE_DIMENSION = 128;

}
