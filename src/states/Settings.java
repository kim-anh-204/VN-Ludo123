package states;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import org.newdawn.slick.gui.MouseOverArea;

import ui.Game;

public class Settings extends BasicGameState {
    public static boolean current = true;

    private static final int SCREEN_WIDTH = Game.SCREEN_WIDTH;
    private static final int SCREEN_HEIGHT = Game.SCREEN_HEIGHT;

    private Image background;
    private Image settingsImage;
    private Image okButton;

    private MouseOverArea musicCheckboxArea;
    private MouseOverArea diceSoundCheckboxArea;
    private MouseOverArea flapSoundCheckboxArea;
    private MouseOverArea okButtonArea;

    private String message;
    private float messageX, messageY;
    private float musicCheckboxX, musicCheckboxY;
    private float diceSoundCheckboxX, diceSoundCheckboxY;
    private float flapSoundCheckboxX, flapSoundCheckboxY;
    private float okButtonX, okButtonY;

    private float boxWidth = 500f;
    private float boxHeight = 300f; // Increased height to accommodate three checkboxes
    private float checkboxSize = 50f;
    private float okButtonWidth = 150f;
    private float okButtonHeight = 50f;

    public static boolean isMusicEnabled = true;
    public static boolean isDiceSoundEnabled = true;
    public static boolean isFlapSoundEnabled = true;

    private boolean musicChecked = isMusicEnabled;
    private boolean diceSoundChecked = isDiceSoundEnabled;
    private boolean flapSoundChecked = isFlapSoundEnabled;

    private Sound sound;
    private TrueTypeFont font;

    public Settings(int state) {
        this.message = "SETTING";
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        background = new Image("res/rule-background.png");
        settingsImage = new Image("res/message-box-background.png");
        okButton = new Image("res/button-ok.png");

        sound = new Sound();
        sound.setFile(0);

        font = new TrueTypeFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24), true);

        messageX = (SCREEN_WIDTH - boxWidth) / 2;
        messageY = (SCREEN_HEIGHT - boxHeight) / 2;

        float spacing = 50f;
        musicCheckboxX = messageX + 40;
        musicCheckboxY = messageY + 80;

        diceSoundCheckboxX = musicCheckboxX;
        diceSoundCheckboxY = musicCheckboxY + spacing;

        flapSoundCheckboxX = musicCheckboxX;
        flapSoundCheckboxY = diceSoundCheckboxY + spacing;

        okButtonX = messageX + (boxWidth - okButtonWidth) / 2;
        okButtonY = messageY + boxHeight + 40f;

        musicCheckboxArea = new MouseOverArea(gc, null, (int)musicCheckboxX, (int)musicCheckboxY, (int)checkboxSize, (int)checkboxSize);
        diceSoundCheckboxArea = new MouseOverArea(gc, null, (int)diceSoundCheckboxX, (int)diceSoundCheckboxY, (int)checkboxSize, (int)checkboxSize);
        flapSoundCheckboxArea = new MouseOverArea(gc, null, (int)flapSoundCheckboxX, (int)flapSoundCheckboxY, (int)checkboxSize, (int)checkboxSize);
        okButtonArea = new MouseOverArea(gc, okButton, (int)okButtonX, (int)okButtonY, (int)okButtonWidth, (int)okButtonHeight);
    }

    @Override
    public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
        super.enter(gc, sbg);
        musicChecked = isMusicEnabled;
        diceSoundChecked = isDiceSoundEnabled;
        flapSoundChecked = isFlapSoundEnabled;
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        background.draw(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        settingsImage.draw(messageX, messageY, boxWidth, boxHeight);
        // Title
        g.setColor(Color.black);
        g.setFont(font);
        float titleX = messageX + (boxWidth - font.getWidth(message)) / 2;
        g.drawString(message, titleX, messageY + 15);

        // Checkboxes
        // Checkboxes
        String[] labels = { "Enable Music", "Enable Dice Sound", "Enable Flap Sound" };
        boolean[] checks = { musicChecked, diceSoundChecked, flapSoundChecked };
        boolean[] hovers = { musicCheckboxArea.isMouseOver(), diceSoundCheckboxArea.isMouseOver(), flapSoundCheckboxArea.isMouseOver() };

        float spacing = 70f; // Tăng spacing cho cân đối đẹp hơn
        for (int i = 0; i < labels.length; i++) {
            float y = musicCheckboxY + i * spacing;
            renderCheckbox(g, musicCheckboxX, y, labels[i], checks[i], hovers[i]);
        }

        // OK Button
        if (okButtonArea.isMouseOver()) {
            okButton.draw(okButtonX - 5, okButtonY - 5, okButtonWidth + 10, okButtonHeight + 10, new Color(1f, 1f, 1f, 0.9f));
        } else {
            okButton.draw(okButtonX, okButtonY, okButtonWidth, okButtonHeight);
        }
    }

    private void renderCheckbox(Graphics g, float x, float y, String label, boolean checked, boolean hovered) {
        g.setColor(hovered ? Color.red : Color.black);
        g.drawRect(x, y, checkboxSize, checkboxSize);
        if (checked) {
            g.setColor(Color.green);
            g.drawLine(x + 5, y + checkboxSize / 2, x + checkboxSize / 2, y + checkboxSize - 5);
            g.drawLine(x + checkboxSize / 2, y + checkboxSize - 5, x + checkboxSize - 5, y + 5);
        }
        g.setColor(Color.black);
        g.drawString(label, x + checkboxSize + 10, y + 5);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        int xPos = Mouse.getX();
        int yPos = SCREEN_HEIGHT - Mouse.getY();

        Input input = gc.getInput();

        if (musicCheckboxArea.isMouseOver() && input.isMousePressed(0)) {
            musicChecked = !musicChecked;
        }
        if (diceSoundCheckboxArea.isMouseOver() && input.isMousePressed(0)) {
            diceSoundChecked = !diceSoundChecked;
        }
        if (flapSoundCheckboxArea.isMouseOver() && input.isMousePressed(0)) {
            flapSoundChecked = !flapSoundChecked;
        }

        if (okButtonArea.isMouseOver() && input.isMousePressed(0)) {
            isMusicEnabled = musicChecked;
            isDiceSoundEnabled = diceSoundChecked;
            isFlapSoundEnabled = flapSoundChecked;

            if (!isMusicEnabled) {
                sound.stop();
            }

            if(current){
                sbg.enterState(0);
            }
            else {
                sbg.enterState(2);
            }
        }
    }

    @Override
    public int getID() {
        return 4;
    }
}