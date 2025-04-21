package ui;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

import states.Menu;
import states.Play;
import states.Setup;

public class Game extends StateBasedGame {
    public static final String gameName = "Ludo";
    // DO NOT ALTER THE WIDTH AND THE HEIGHT OF THE SCREEN. OR ELSE
    // THE FIGURES MIGHT NOT BE PLACES CORRECTLY ON THE BOARD ACCORDING
    // TO THE TEXT FILES OF THE POSITION INFORMATION
    public static final int SCREEN_WIDTH   = 1200;
    public static final int SCREEN_HEIGHT  = 850;

    // each screen/state gets its own number
    public static final int menu = 0;
    public static final int setup = 1;
    public static final int play = 2;

    public Game(String gameName) {
        // Passing a parameter as name of the game into the constructor
        super(gameName);
    }

    public void initStatesList(GameContainer gc) throws SlickException {
        addState(new Menu(menu));
        addState(new Setup(setup));
        addState(new Play(play));

        //what screen do we want to show the user at the game startup (default screen)
        enterState(menu);
    }

    public static void main(String[] args) {
        // the window for our game where things go inside
        AppGameContainer appgc;

        // use try catch because that what slick recommends
        try {
            // create a window that's going to  hold the Game with gameName
            appgc = new AppGameContainer(new Game(gameName));
            // last param indicates whether we want full-screen mode
            appgc.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
            // hide FPS on the upper left corner of the screen
            appgc.setShowFPS(false);
            // start the game
            appgc.start();
        } catch(SlickException e) {
            e.printStackTrace();
        }

    }
}
