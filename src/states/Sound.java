package states;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {
    Clip clip;
    URL soundURL[] = new URL[30];

    public Sound() {
        soundURL[0] = getClass().getResource("/Sound/LittlerootTown-GoIchinose-3677279.wav");
        soundURL[1] = getClass().getResource("/Sound/dice-roll.wav");
        soundURL[2] = getClass().getResource("/Sound/dragon-wing-flap-81642.wav");
        soundURL[3] = getClass().getResource("/Sound/LittlerootTown-GoIchinose-3677279.wav");
    }

    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            // Reset the clip position to the beginning
            clip.setMicrosecondPosition(0);
            clip.start();
        }
    }

    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }
}