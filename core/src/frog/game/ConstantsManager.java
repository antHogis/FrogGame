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

    public static I18NBundle levels =
            I18NBundle.createBundle(Gdx.files.internal("levels"));

    /*
     * File locations (non-localized)
     */
    //Ui Items
    public static final String bgGenericPath = "ui/bg.png";
    public static final String bgMainMenuPath = "ui/bg2.png";
    public static final String bgLevelFinishPath = "ui/bg3.png";
    public static final String bgGamePath = "gfx/bg2.png";
    public static final String homeButtonIdlePath = "ui/buttons/home-idle.png";
    public static final String homeButtonPressedPath = "ui/buttons/home-pressed.png";
    public static final String starGoldenPath = "ui/star_golden.png";
    public static final String starGreyPath = "ui/star_grey.png";
    public static final String musicOnIdlePath = "ui/buttons/settings/music_on-idle.png";
    public static final String musicOnPressedPath = "ui/buttons/settings/music_on-pressed.png";
    public static final String musicOffIdlePath = "ui/buttons/settings/music_off-idle.png";
    public static final String musicOffPressedPath = "ui/buttons/settings/music_off-pressed.png";
    public static final String soundsOnIdlePath = "ui/buttons/settings/sound_on-idle.png";
    public static final String soundsOnPressedPath = "ui/buttons/settings/sound_on-pressed.png";
    public static final String soundsOffIdlePath = "ui/buttons/settings/sound_off-idle.png";
    public static final String soundsOffPressedPath = "ui/buttons/settings/sound_off-pressed.png";
    public static final String arrowLeftIdlePath = "ui/buttons/arrow-left-idle.png";
    public static final String arrowLeftPressedPath = "ui/buttons/arrow-left-pressed.png";
    public static final String arrowRightIdlePath = "ui/buttons/arrow-right-idle.png";
    public static final String arrowRightPressedPath = "ui/buttons/arrow-right-pressed.png";
    public static final String levelButtonEasyIdlePath = "";
    public static final String levelButtonEasyPressedPath = "";
    public static final String levelButtonMediumIdlePath = "";
    public static final String levelButtonMediumPressedPath = "";
    public static final String levelButtonHardIdlePath = "";
    public static final String levelButtonHardPressedPath = "";
    public static final String finnishButtonIdlePath = "ui/buttons/main/fin-idle.png";
    public static final String finnishButtonPressedPath = "ui/buttons/main/fin-pressed.png";
    public static final String englishButtonIdlePath = "ui/buttons/main/uk-idle.png";
    public static final String englishButtonPressedPath = "ui/buttons/main/uk-pressed.png";

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

    public static final int LEVELS_AMOUNT = 18;
    public static final int TOP_SCORES_AMOUNT = 5;

}
