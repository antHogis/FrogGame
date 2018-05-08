package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.I18NBundle;

/**
 * ConstantsManager provides static variables for other classes
 *
 * <p>ConstantsManager contains mostly paths for Textures which are used in menus.
 * The main purpose of the class is to make consistent variables available to other classes.</p>
 *
 * @author Tadpole Attack Squad
 * @version 2018.0506
 * @since 2018.0415
 */
public class ConstantsManager {
    public static Preferences settings = Gdx.app.getPreferences("Preferences");

    public static I18NBundle levels =
            I18NBundle.createBundle(Gdx.files.internal("levels"));

    /*
     * File locations (non-localized)
     */
    //Backgrounds
    static final String bgGenericPath = "ui/bg.png";
    static final String bgMainMenuPath = "ui/bg2.png";
    static final String bgLevelFinishPath = "ui/bg3.png";
    static final String bgHighScorePath = "ui/bg4.png";
    static final String bgGamePath = "gfx/waterTexture.png";

    //Files that appear in multiple menus
    static final String homeButtonIdlePath = "ui/buttons/home-idle.png";
    static final String homeButtonPressedPath = "ui/buttons/home-pressed.png";
    static final String arrowLeftIdlePath = "ui/buttons/arrow-left-idle.png";
    static final String arrowLeftPressedPath = "ui/buttons/arrow-left-pressed.png";
    static final String arrowRightIdlePath = "ui/buttons/arrow-right-idle.png";
    static final String arrowRightPressedPath = "ui/buttons/arrow-right-pressed.png";
    static final String starGoldenPath = "ui/star_golden.png";
    static final String starGreyPath = "ui/star_grey.png";

    //Files that appear in the main menu
    static final String finnishButtonIdlePath = "ui/buttons/main/fin-idle.png";
    static final String finnishButtonPressedPath = "ui/buttons/main/fin-pressed.png";
    static final String englishButtonIdlePath = "ui/buttons/main/uk-idle.png";
    static final String englishButtonPressedPath = "ui/buttons/main/uk-pressed.png";

    //Files that appear in the settings menu
    static final String musicOnIdlePath = "ui/buttons/settings/music_on-idle.png";
    static final String musicOnPressedPath = "ui/buttons/settings/music_on-pressed.png";
    static final String musicOffIdlePath = "ui/buttons/settings/music_off-idle.png";
    static final String musicOffPressedPath = "ui/buttons/settings/music_off-pressed.png";
    static final String soundsOnIdlePath = "ui/buttons/settings/sound_on-idle.png";
    static final String soundsOnPressedPath = "ui/buttons/settings/sound_on-pressed.png";
    static final String soundsOffIdlePath = "ui/buttons/settings/sound_off-idle.png";
    static final String soundsOffPressedPath = "ui/buttons/settings/sound_off-pressed.png";
    static final String infoIdlePath = "ui/buttons/settings/info-idle.png";
    static final String infoPressedPath = "ui/buttons/settings/info-pressed.png";
    static final String closeIdlePath = "ui/buttons/settings/close-idle.png";
    static final String closePressedPath = "ui/buttons/settings/close-pressed.png";

    /*
     * Game-modifying values
     */
    //Movement related values
    static final float MIN_SPEED = 200f;
    static final float MAX_SPEED = 300f;
    static final float DEFAULT_SPEED = 250f;
    static final float MIN_THRESHOLD = 0f;
    static final float MAX_THRESHOLD = 2f;
    static final float DEFAULT_THRESHOLD = 0.35f;
    static final boolean DEFAULT_INVERT_Y = true;

    //Audio related values
    static final boolean DEFAULT_MUSIC_ON = true;
    static final boolean DEFAULT_SOUNDS_ON = true;

    //Level related values
    static final int TILE_DIMENSION = 128;
    static final int LEVELS_AMOUNT = 18;
    static final int TOP_SCORES_AMOUNT = 5;
    static final String previousLevelPlayedKey = "previousLevelPlayed";

}
