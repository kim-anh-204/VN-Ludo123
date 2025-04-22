package states;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

import ui.Game;

public class Menu extends BasicGameState {
	private static final int SCREEN_WIDTH = Game.SCREEN_WIDTH;
	private static final int SCREEN_HEIGHT = Game.SCREEN_HEIGHT;

	private float playX;
	private float playY;
	private float exitX;
	private float exitY;
	private float ruleX;
	private float ruleY;
	private float settingsX;
	private float settingsY;

	Image background;
	Image play;
	Image exit;
	Image rule;
	Image settings;

	private Sound sound;

	public Menu(int state) {
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		background = new Image("res/menu-background.png");
		play = new Image("res/button-play.png");
		exit = new Image("res/button-exit.png");
		rule = new Image("res/button-rule.png");
		settings = new Image("res/button-settings.png");

		sound = new Sound();
		sound.setFile(0); // Load background music

		// Position buttons
		double fromImageToEdge = 0.2;
		float offsetY = 320f;

		playX = (SCREEN_WIDTH - play.getWidth()) / 2;
		playY = (float)(SCREEN_HEIGHT * fromImageToEdge +
				(SCREEN_HEIGHT * (1 - 2 * fromImageToEdge) - play.getHeight() - exit.getHeight()) / 2) - offsetY;
		ruleX = (SCREEN_WIDTH - rule.getWidth()) / 2;
		ruleY = playY + play.getHeight() + 40;
		settingsX = (SCREEN_WIDTH - settings.getWidth()) / 2;
		settingsY = ruleY + rule.getHeight() + 40;
		exitX = (SCREEN_WIDTH - exit.getWidth()) / 2;
		exitY = settingsY + settings.getHeight() + 40;
	}

	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.enter(gc, sbg);
		// Play music if enabled
		if (Settings.isMusicEnabled) {
			sound.loop();
		} else {
			sound.stop();
		}
	}

	@Override
	public void leave(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.leave(gc, sbg);
		// Stop music when leaving menu
		sound.stop();
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		background.draw(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		play.draw(playX, playY);
		rule.draw(ruleX, ruleY);
		exit.draw(exitX, exitY);
		settings.draw(settingsX, settingsY);
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		int xPos = Mouse.getX();
		int yPos = Mouse.getY();

		Input input = gc.getInput();

		// Play button
		if ((xPos > playX && xPos < playX + play.getWidth()) &&
				(yPos > SCREEN_HEIGHT - playY - play.getHeight() && yPos < SCREEN_HEIGHT - playY)) {
			if (input.isMouseButtonDown(0)) {
				try {
					Thread.sleep(300);
				} catch (Exception e) {
					System.out.println(e);
				}
				sbg.enterState(1);
			}
		}
		// Rule button
		if ((xPos > ruleX && xPos < ruleX + rule.getWidth()) &&
				(yPos > SCREEN_HEIGHT - ruleY - rule.getHeight() && yPos < SCREEN_HEIGHT - ruleY)) {
			if (input.isMouseButtonDown(0)) {
				try {
					Thread.sleep(300);
				} catch (Exception e) {
					System.out.println(e);
				}
				sbg.enterState(3);
			}
		}
		// Settings button
		if ((xPos > settingsX && xPos < settingsX + settings.getWidth()) &&
				(yPos > SCREEN_HEIGHT - settingsY - settings.getHeight() && yPos < SCREEN_HEIGHT - settingsY)) {
			if (input.isMouseButtonDown(0)) {
				try {
					Thread.sleep(300);
				} catch (Exception e) {
					System.out.println(e);
				}
				sbg.enterState(4);
			}
		}
		// Exit button
		if ((xPos > exitX && xPos < exitX + exit.getWidth()) &&
				(yPos > SCREEN_HEIGHT - exitY - exit.getHeight() && yPos < SCREEN_HEIGHT - exitY)) {
			if (input.isMouseButtonDown(0)) {
				System.exit(0);
			}
		}
	}

	public int getID() {
		return 0;
	}
}