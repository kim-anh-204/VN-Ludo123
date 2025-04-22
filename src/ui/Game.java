package ui;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

import states.Menu;
import states.Play;
import states.Setup;
import states.Rule;
import states.Settings;

public class Game extends StateBasedGame {
	public static final String gameName = "Ludo";
	public static final int SCREEN_WIDTH   = 1200;
    public static final int SCREEN_HEIGHT  = 850;
	
	public static final int menu = 0;
	public static final int setup = 1;
	public static final int play = 2;
	public static final int rule = 3;
	public static final int settings = 4;

	public Game(String gameName) {
		super(gameName);
	}
	
	public void initStatesList(GameContainer gc) throws SlickException {
		addState(new Menu(menu));
		addState(new Setup(setup));
		addState(new Play(play));
		addState(new Rule(rule));
		addState(new Settings(settings));



		enterState(menu);
	}
	
	public static void main(String[] args) {
		AppGameContainer appgc;

		try {
			appgc = new AppGameContainer(new Game(gameName));
			appgc.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
			appgc.setShowFPS(false);
			appgc.start();
		} catch(SlickException e) {
			e.printStackTrace();
		}

	}
}
