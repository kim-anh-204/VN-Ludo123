package states;

public class AudioState {
    private static AudioState instance;
    private boolean musicEnabled = true;
    private boolean soundEnabled = false;
    private Sound soundPlayer;

    private AudioState() {
        soundPlayer = new Sound();
        soundPlayer.setFile(0);
        updateMusicState();
    }

    public static AudioState getInstance() {
        if (instance == null) {
            instance = new AudioState();
        }
        return instance;
    }

    public void setMusicEnabled(boolean enabled) {
        this.musicEnabled = enabled;
        updateMusicState();
    }

    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    private void updateMusicState() {
        if (musicEnabled) {
            soundPlayer.loop();
        } else {
            soundPlayer.stop();
        }
    }

    public void stopMusic() {
        soundPlayer.stop();
    }
}