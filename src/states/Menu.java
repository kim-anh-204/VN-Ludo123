package states;

import org.lwjgl.input.Mouse; //track location of the mouse
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

import ui.Game;

public class Menu extends BasicGameState {
	private static final int SCREEN_WIDTH = Game.SCREEN_WIDTH;
	private static final int SCREEN_HEIGHT = Game.SCREEN_HEIGHT;
	
	private float welcomeX;
	private float welcomeY;

	private float playX;
	private float playY;

	private float exitX;
	private float exitY;

	Image background;
	Image welcome;
	Image play;
	Image exit;

	public Menu(int state) {

	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		background = new Image("res/menu-background.png");
		welcome = new Image("res/welcome.png");
		play = new Image("res/button-play.png");
		exit = new Image("res/button-exit.png");
		
		// distance from the first/last image to the edge of the screen compared to the screen height
		double fromImageToEdge = 0.2;
		float offsetY = 140f;

//		welcomeX = (SCREEN_WIDTH - welcome.getWidth()) / 2;
//		welcomeY = (float) (SCREEN_HEIGHT * fromImageToEdge);
		
		playX = (SCREEN_WIDTH - play.getWidth()) / 2;
		playY = (float)(welcome.getHeight() + (SCREEN_HEIGHT * fromImageToEdge ) +
				(SCREEN_HEIGHT * (1- 2* fromImageToEdge) - welcome.getHeight() - play.getHeight() - exit.getHeight()) / 2)- offsetY;
		
		exitX = (SCREEN_WIDTH - exit.getWidth()) / 2;
		exitY = (float) (SCREEN_HEIGHT * (1- fromImageToEdge)  - exit.getHeight())- offsetY;
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		background.draw(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
//		welcome.draw(welcomeX, welcomeY);
		play.draw(playX, playY);
		exit.draw(exitX, exitY);
	}

	// update regularly the image on the screen (cause the animation) (AKA ALWAYS LISTEN FOR EVENT)
	// REMEMBER: MOUSE (0,0) COORDINATES start at lower left corner
	// and that is different from g.draw: (0,0) starts at the upper left corner
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		int xPos = Mouse.getX();
		int yPos = Mouse.getY();

		Input input = gc.getInput();

		// play button
		if ((xPos>playX && xPos<playX + play.getWidth()) && 
				(yPos>SCREEN_HEIGHT - playY - play.getHeight() && yPos<SCREEN_HEIGHT - playY)) {
			if(input.isMouseButtonDown(0)) { 
				try {
		            Thread.sleep(300);
		        } catch (Exception e) {
		            System.out.println(e);
		        }
				sbg.enterState(1);
			}
		}
		
		if ((xPos>exitX && xPos<exitX + exit.getWidth()) && 
				(yPos>SCREEN_HEIGHT - exitY - exit.getHeight() && yPos<SCREEN_HEIGHT - exitY)) {
			if(input.isMouseButtonDown(0)) {
				System.exit(0);
			}
		}
	}

	// ID of Menu is 0
	public int getID() {
		return 0;
	}
}
