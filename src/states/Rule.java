package states;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

import ui.Game;

public class Rule extends BasicGameState {
    private Image background;
    private Image ruleImage;
    private Image okButton;

    private float ruleX, ruleY;
    private float okX, okY;

    public Rule(int state) {
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        background = new Image("res/rule-background.png");
        ruleImage = new Image("res/rule.png");
        okButton = new Image("res/button-ok.png");

        // Căn giữa ảnh luật chơi
        ruleX = (Game.SCREEN_WIDTH - ruleImage.getWidth()) / 2f;
        ruleY = (Game.SCREEN_HEIGHT - ruleImage.getHeight()) / 2f - 40;

        // Căn giữa nút OK, đặt dưới ảnh luật chơi
        okX = (Game.SCREEN_WIDTH - okButton.getWidth()) / 2f;
        okY = ruleY + ruleImage.getHeight() + 30;
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        background.draw(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
        ruleImage.draw(ruleX, ruleY);
        okButton.draw(okX, okY);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        int x = Mouse.getX();
        int y = Mouse.getY();
        Input input = gc.getInput();

        // Kiểm tra nếu click vào nút OK
        if (x > okX && x < okX + okButton.getWidth() &&
                y > Game.SCREEN_HEIGHT - okY - okButton.getHeight() &&
                y < Game.SCREEN_HEIGHT - okY) {

            if (input.isMouseButtonDown(0)) {
                try {
                    Thread.sleep(200); // chống double click
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sbg.enterState(Game.menu);
            }
        }
    }

    @Override
    public int getID() {
        return 3; // ID cho màn hình Rule, nhớ thêm vào main
    }
}
