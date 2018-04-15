package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Anton on 14.4.2018.
 */

public class SoundController {
    private static Array<Sound> sounds = new Array<Sound>();

    public static Sound perkele
            = Gdx.audio.newSound(Gdx.files.internal("sounds/perkele.wav"));
    public static Music backgroundMusic
            = Gdx.audio.newMusic(Gdx.files.internal("music/demo1-leikattu.wav"));

    public static void equalize() {
        sounds.add(perkele);

        for (Sound sound : sounds) {
            sound.setVolume(0, 0.25f);
        }

        backgroundMusic.setLooping(true);
        setMusicOnOff();
    }

    public static void setMusicOnOff() {
        if (!(ConstantsManager.settings.getBoolean("music-on", ConstantsManager.DEFAULT_MUSIC_ON))) {
            backgroundMusic.setVolume(ConstantsManager.MUSIC_DEFAULT_VOL);
        } else {
            backgroundMusic.setVolume(0f);
        }
    }
}
