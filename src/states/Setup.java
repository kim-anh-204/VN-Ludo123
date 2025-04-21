package states;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

import ui.Game;

import org.newdawn.slick.gui.*;

public class Setup extends BasicGameState {

    public static final int SCREEN_WIDTH = Game.SCREEN_WIDTH;
    public static final int SCREEN_HEIGHT = Game.SCREEN_HEIGHT;
    public static final int NEG_INFINITY = Integer.MIN_VALUE;

    // coordinates for choosing mode (human or com. or inactive) button
    private static int mode_x;
    private static int human1_y = 175;
    private static int com2_y = human1_y + 155;
    private static int com3_y = com2_y + 155;
    private static int com4_y = com3_y + 155;
    private static int com1_y = NEG_INFINITY;
    private static int human2_y = NEG_INFINITY;
    private static int human3_y = NEG_INFINITY;
    private static int human4_y = NEG_INFINITY;
    private static int inactive1_y = NEG_INFINITY;
    private static int inactive2_y = NEG_INFINITY;
    private static int inactive3_y = NEG_INFINITY;
    private static int inactive4_y = NEG_INFINITY;

    // number of players in categories:
    private static int numOfInactive = 0;
    private static int numOfHuman = 1;

    // default mode for players
    static boolean isBlueHuman = true;
    static boolean isYellowHuman = false;
    static boolean isGreenHuman = false;
    static boolean isRedHuman = false;

    static boolean isBlueInactive = false;
    static boolean isYellowInactive = false;
    static boolean isGreenInactive = false;
    static boolean isRedInactive = false;

    // input field for players' name
    static TextField bluePlayer;
    static TextField yellowPlayer;
    static TextField greenPlayer;
    static TextField redPlayer;

    Image start;
    Image choose;
    Image com;
    Image human;
    Image inactive;

    String blueFigure = "Player 1 (blue figure):";
    String yellowFigure = "Player 2 (yellow figure):";
    String greenFigure = "Player 3 (green figure):";
    String redFigure = "Player 4 (red figure):";

    String warning = "";

    public Setup(int state) {

    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        double horizontalGap = 0.15;

        start = new Image("res/button-start.png");
        choose = new Image("res/choose-figure.png");
        com = new Image("res/button-com.png");
        human = new Image("res/button-human.png");
        inactive = new Image("res/button-inactive.png");

        mode_x = (SCREEN_WIDTH - com.getWidth()) / 2;

        bluePlayer = new TextField(gc, gc.getDefaultFont(), (int) (SCREEN_WIDTH * (1 - horizontalGap) - 200), human1_y, 200, 35);
        yellowPlayer = new TextField(gc, gc.getDefaultFont(), (int) (SCREEN_WIDTH * (1 - horizontalGap) - 200), com2_y, 200, 35);
        greenPlayer = new TextField(gc, gc.getDefaultFont(), (int) (SCREEN_WIDTH * (1 - horizontalGap) - 200), com3_y, 200, 35);
        redPlayer = new TextField(gc, gc.getDefaultFont(), (int) (SCREEN_WIDTH * (1 - horizontalGap) - 200), com4_y, 200, 35);

        bluePlayer.setBackgroundColor(new Color(168, 218, 220));
        yellowPlayer.setBackgroundColor(new Color(168, 218, 220));
        greenPlayer.setBackgroundColor(new Color(168, 218, 220));
        redPlayer.setBackgroundColor(new Color(168, 218, 220));

        bluePlayer.setBorderColor(Color.red);
        yellowPlayer.setBorderColor(Color.red);
        greenPlayer.setBorderColor(Color.red);
        redPlayer.setBorderColor(Color.red);

        bluePlayer.setTextColor(Color.black);
        yellowPlayer.setTextColor(Color.black);
        greenPlayer.setTextColor(Color.black);
        redPlayer.setTextColor(Color.black);

        // Default name for the players
        bluePlayer.setText("Player 1");
        yellowPlayer.setText("Player 2");
        greenPlayer.setText("Player 3");
        redPlayer.setText("Player 4");

        // Max character for name: 15 characters
        bluePlayer.setMaxLength(15);
        yellowPlayer.setMaxLength(15);
        greenPlayer.setMaxLength(15);
        redPlayer.setMaxLength(15);
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        g.setColor(new Color(255, 255, 255));
        g.setBackground(new Color(241, 250, 238));

        bluePlayer.render(gc, g);
        yellowPlayer.render(gc, g);
        greenPlayer.render(gc, g);
        redPlayer.render(gc, g);

        start.draw((SCREEN_WIDTH - start.getWidth()) / 2, SCREEN_HEIGHT - start.getHeight() - 50);
        choose.draw((SCREEN_WIDTH - choose.getWidth()) / 2, 25);

        // player 1
        human.draw(mode_x, human1_y);
        com.draw(mode_x, com1_y);
        inactive.draw(mode_x, inactive1_y);
        // player 2
        com.draw(mode_x, com2_y);
        human.draw(mode_x, human2_y);
        inactive.draw(mode_x, inactive2_y);
        // player 3
        com.draw(mode_x, com3_y);
        human.draw(mode_x, human3_y);
        inactive.draw(mode_x, inactive3_y);
        // player 4
        com.draw(mode_x, com4_y);
        human.draw(mode_x, human4_y);
        inactive.draw(mode_x, inactive4_y);

        g.setColor(Color.red);
        g.drawString(warning, (SCREEN_WIDTH - start.getWidth()) / 2 - 110,
                SCREEN_HEIGHT - start.getHeight() - 90);


        g.setColor(Color.blue);
        g.drawString(blueFigure, (float) (SCREEN_WIDTH * 0.15), 185);
        g.setColor(new Color(204, 204, 0));
        g.drawString(yellowFigure, (float) (SCREEN_WIDTH * 0.15), 340);
        g.setColor(new Color(34, 204, 0));
        g.drawString(greenFigure, (float) (SCREEN_WIDTH * 0.15), 495);
        g.setColor(Color.red);
        g.drawString(redFigure, (float) (SCREEN_WIDTH * 0.15), 650);
    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        int xPos = Mouse.getX();
        int yPos = Mouse.getY();

        int startX = (SCREEN_WIDTH - start.getWidth()) / 2;
        int startY = 50 + start.getHeight();

        Input input = gc.getInput();

        checkConditionOnClickingStartButton(sbg, input, xPos, yPos, startX, startY);

        changePlayerMode(input, xPos, yPos);
    }

    private void checkConditionOnClickingStartButton(StateBasedGame sbg, Input input, int xPos, int yPos, int startX, int startY) {
        if ((xPos > startX && xPos < startX + start.getWidth()) && (yPos > 50 && yPos < startY)) {
            if (input.isMouseButtonDown(0)) {
                // show warning if all active players' name are not filled out
                if (bluePlayer.getText().isEmpty() && !isBlueInactive || yellowPlayer.getText().isEmpty() && !isYellowInactive ||
                        greenPlayer.getText().isEmpty() && !isGreenInactive || redPlayer.getText().isEmpty() && !isRedInactive) {
                    warning = "Please type in all of the active players' name!";
                }
                // min player: 2 players to start the game
                else if ((isBlueInactive && isYellowInactive && isGreenInactive) ||
                        (isBlueInactive && isYellowInactive && isRedInactive) ||
                        (isBlueInactive && isGreenInactive && isRedInactive) ||
                        (isYellowInactive && isGreenInactive && isRedInactive)) {
                    warning = "There must be at least 2 players to start the game";
                }
                // all active players cannot be computer at the same time
                else if ((4- numOfInactive) == (4-numOfHuman-numOfInactive)) {
                    warning = "There must be at least one human player to start!";
                }
                // no duplicate names allowed
                else if (bluePlayer.getText().equals(yellowPlayer.getText()) && !isBlueInactive && !isYellowInactive  ||
                        bluePlayer.getText().equals(greenPlayer.getText()) && !isBlueInactive && !isGreenInactive ||
                        bluePlayer.getText().equals(redPlayer.getText()) && !isBlueInactive && !isRedInactive ||
                        yellowPlayer.getText().equals(greenPlayer.getText()) && !isYellowInactive && !isGreenInactive ||
                        yellowPlayer.getText().equals(redPlayer.getText()) && !isYellowInactive && !isRedInactive ||
                        greenPlayer.getText().equals(redPlayer.getText()) && !isGreenInactive && !isRedInactive) {
                    warning = "Active players' name cannot be duplicate!";
                } else {
                    try {
                        Thread.sleep(300);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    sbg.enterState(2);
                }
            }
        }
    }

    private void changePlayerMode(Input input, int xPos, int yPos) {
        if ((xPos > mode_x && xPos < mode_x + com.getWidth())
                && (yPos > SCREEN_HEIGHT - 175 - com.getWidth() && yPos < SCREEN_HEIGHT - 175)) {
            if (input.isMousePressed(0)) {
                if (isBlueHuman) {
                    com1_y = human1_y;
                    human1_y = NEG_INFINITY;
                    isBlueHuman = !isBlueHuman;
                    numOfHuman--;
                } else if (isBlueInactive) {
                    human1_y = inactive1_y;
                    inactive1_y = NEG_INFINITY;
                    isBlueInactive = !isBlueInactive;
                    isBlueHuman = !isBlueHuman;
                    numOfHuman++;
                } else {
                    inactive1_y = com1_y;
                    com1_y = NEG_INFINITY;
                    isBlueInactive = !isBlueInactive;
                    numOfInactive++;
                }
            }
        } else if ((xPos > mode_x && xPos < mode_x + com.getWidth())
                && (yPos > SCREEN_HEIGHT - 330 - com.getWidth() && yPos < SCREEN_HEIGHT - 330)) {
            if (input.isMousePressed(0)) {
                if (isYellowHuman) {
                    com2_y = human2_y;
                    human2_y = NEG_INFINITY;
                    isYellowHuman = !isYellowHuman;
                    numOfHuman--;
                } else if (isYellowInactive) {
                    human2_y = inactive2_y;
                    inactive2_y = NEG_INFINITY;
                    isYellowInactive = !isYellowInactive;
                    isYellowHuman = !isYellowHuman;
                    numOfHuman++;
                } else {
                    inactive2_y = com2_y;
                    com2_y = NEG_INFINITY;
                    isYellowInactive = !isYellowInactive;
                    numOfInactive++;
                }
            }
        } else if ((xPos > mode_x && xPos < mode_x + com.getWidth())
                && (yPos > SCREEN_HEIGHT - 485 - com.getWidth() && yPos < SCREEN_HEIGHT - 485)) {
            if (input.isMousePressed(0)) {
                if (isGreenHuman) {
                    com3_y = human3_y;
                    human3_y = NEG_INFINITY;
                    isGreenHuman = !isGreenHuman;
                    numOfHuman--;
                } else if (isGreenInactive) {
                    human3_y = inactive3_y;
                    inactive3_y = NEG_INFINITY;
                    isGreenInactive = !isGreenInactive;
                    isGreenHuman = !isGreenHuman;
                    numOfHuman++;
                } else {
                    inactive3_y = com3_y;
                    com3_y = NEG_INFINITY;
                    isGreenInactive = !isGreenInactive;
                    numOfInactive++;
                }
            }
        } else if ((xPos > mode_x && xPos < mode_x + com.getWidth())
                && (yPos > SCREEN_HEIGHT - 640 - com.getWidth() && yPos < SCREEN_HEIGHT - 640)) {
            if (input.isMousePressed(0)) {
                if (isRedHuman) {
                    com4_y = human4_y;
                    human4_y = NEG_INFINITY;
                    isRedHuman = !isRedHuman;
                    numOfHuman--;
                } else if (isRedInactive) {
                    human4_y = inactive4_y;
                    inactive4_y = NEG_INFINITY;
                    isRedInactive = !isRedInactive;
                    isRedHuman = !isRedHuman;
                    numOfHuman++;
                } else {
                    inactive4_y = com4_y;
                    com4_y = NEG_INFINITY;
                    isRedInactive = !isRedInactive;
                    numOfInactive++;
                }
            }
        }
    }

    // ID of Setup is 1
    public int getID() {
        return 1;
    }
}