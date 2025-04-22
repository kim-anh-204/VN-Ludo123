package states;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

import ui.Game;

public class Settings extends BasicGameState {

    private static final int SCREEN_WIDTH = Game.SCREEN_WIDTH;
    private static final int SCREEN_HEIGHT = Game.SCREEN_HEIGHT;

    private Image background;
    private Image settingsImage;
    private String message;
    private float messageX, messageY;
    private float okButtonX, okButtonY;
    private Image okButton;

    // Kích thước của hộp thông báo
    private float boxWidth = 500f; // Chiều rộng hộp thông báo
    private float boxHeight = 250f; // Chiều cao hộp thông báo
    private float okButtonWidth = 150f; // Chiều rộng nút OK
    private float okButtonHeight = 50f; // Chiều cao nút OK

    public Settings(int state) {
        this.message = "Cài đặt đang được phát triển!";
    }


    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        settingsImage = new Image("res/message-box-background.png"); // Background for the message box
        okButton = new Image("res/button-ok.png"); // OK button
        background = new Image("res/rule-background.png");

        // Calculate positions for the message box
        messageX = (SCREEN_WIDTH - boxWidth) / 2;
        messageY = (SCREEN_HEIGHT - boxHeight) / 2;

        // Calculate position for the OK button
        okButtonX = (SCREEN_WIDTH - okButtonWidth) / 2;
        okButtonY = messageY + boxHeight +40; // Position OK button below the message box
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        background.draw(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
        settingsImage.draw(messageX, messageY, boxWidth, boxHeight); // Draw background with specified size
        // Draw message text (could be more complex if needed)
        g.setColor(Color.white);
        g.drawString(message, messageX + 20, messageY + 20);

        okButton.draw(okButtonX, okButtonY, okButtonWidth, okButtonHeight); // Draw OK button with specified size
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        int xPos = Mouse.getX();
        int yPos = Mouse.getY();

        Input input = gc.getInput();

        // Check if the mouse is within the OK button area
        if ((xPos > okButtonX && xPos < okButtonX + okButtonWidth) &&
                (yPos > SCREEN_HEIGHT - okButtonY - okButtonHeight && yPos < SCREEN_HEIGHT - okButtonY)) {
            if (input.isMouseButtonDown(0)) { // If mouse button is clicked
                sbg.enterState(0); // Go back to the menu
            }
        }
    }

    @Override
    public int getID() {
        return 4;
    }

}
