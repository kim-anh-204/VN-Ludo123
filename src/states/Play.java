package states;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

import players.*;
import positions.*;
import ui.Game;
import dice.Dice;
import figure.*;

import static enums.FigureColor.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

public class Play extends BasicGameState {
	public static boolean PlayCurrent = false;
	private static final int SCREEN_WIDTH = Game.SCREEN_WIDTH;
	private static final int SCREEN_HEIGHT = Game.SCREEN_HEIGHT;
	public static final int NEG_INFINITY = Integer.MIN_VALUE;

	Image board;
	Image roll;
	Image normalDice;
	Image dice1;
	Image dice2;
	Image dice3;
	Image dice4;
	Image dice5;
	Image dice6;
	Image newGame;
	Image settingGame;
	Image quitGame;
	Image gamePanel;

	private static int quitX;
	private static int quitY;

	public static int random_fixed = (int) (Math.random() * 20) + 50;
	int random = random_fixed;

	private int mouseX;
	private int mouseY;

	private int diceX;
	private int diceY;

	private int diceX_hidden1 = NEG_INFINITY;
	private int diceY_hidden1 = NEG_INFINITY;

	private int diceX_hidden2 = NEG_INFINITY;
	private int diceY_hidden2 = NEG_INFINITY;

	private int diceX_hidden3 = NEG_INFINITY;
	private int diceY_hidden3 = NEG_INFINITY;

	private int diceX_hidden4 = NEG_INFINITY;
	private int diceY_hidden4 = NEG_INFINITY;

	private int diceX_hidden5 = NEG_INFINITY;
	private int diceY_hidden5 = NEG_INFINITY;

	private int diceX_hidden6 = NEG_INFINITY;
	private int diceY_hidden6 = NEG_INFINITY;

	private int rollX;
	private int rollY;

	private Dice dice;
	private Player[] players;

	private boolean rollingDicePhase;
	private boolean choosingFigurePhase;
	private boolean isChangeOfFigureAllowed = true;
	private boolean figureWasMoved = false;
	private boolean resetDiceIfNoMovesPossible;
	private boolean isMovesAvailable;
	private boolean kickingAnimation = false;
	private int diceResult = 0;
	private Player activePlayer;

	private boolean fixedStep = false;
	private float stepX = 0;
	private float stepY = 0;

	private String instructionText = "";
	private String winningMsg = "";

	private Timer timer;
	private boolean delayEnded;
	private Sound sound;  // Background music
	private Sound sound1; // Dice roll sound
	private Sound sound2; // Flap sound

	// Flags to prevent sound from playing repeatedly
	private boolean diceSoundPlayed = false;
	private boolean[] flapSoundPlayed; // One flag per figure

	public Play(int state) {
	}

	private void renderDiceNewGame() {
		diceX = (board.getWidth() + SCREEN_WIDTH - normalDice.getWidth()) / 2;
		diceY = 380;

		rollX = (board.getWidth() + SCREEN_WIDTH - roll.getWidth()) / 2;
		rollY = diceY + normalDice.getHeight() + 50;

		diceX_hidden1 = NEG_INFINITY;
		diceY_hidden1 = NEG_INFINITY;

		diceX_hidden2 = NEG_INFINITY;
		diceY_hidden2 = NEG_INFINITY;

		diceX_hidden3 = NEG_INFINITY;
		diceY_hidden3 = NEG_INFINITY;

		diceX_hidden4 = NEG_INFINITY;
		diceY_hidden4 = NEG_INFINITY;

		diceX_hidden5 = NEG_INFINITY;
		diceY_hidden5 = NEG_INFINITY;

		diceX_hidden6 = NEG_INFINITY;
		diceY_hidden6 = NEG_INFINITY;
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		board = new Image("res/board.png");
		normalDice = new Image("res/dice.png");
		roll = new Image("res/roll.png");
		dice1 = new Image("res/dice-1.png");
		dice2 = new Image("res/dice-2.png");
		dice3 = new Image("res/dice-3.png");
		dice4 = new Image("res/dice-4.png");
		dice5 = new Image("res/dice-5.png");
		dice6 = new Image("res/dice-6.png");
		newGame = new Image("res/new-game.png");
		settingGame = new Image("res/btn-setting-play.png");
		quitGame = new Image("res/quit-game.png");
		gamePanel = new Image("res/game-panel.png");

		sound = new Sound();
		sound.setFile(0); // Background music

		sound1 = new Sound();
		sound1.setFile(1); // Dice roll sound

		sound2 = new Sound();
		sound2.setFile(2); // Flap sound

		quitX = board.getWidth() + (SCREEN_WIDTH - board.getWidth() - quitGame.getWidth()) / 2;
		quitY = SCREEN_HEIGHT - quitGame.getHeight() - 20;
	}

	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.enter(gc, sbg);
		isMovesAvailable = false;
		initializeBoard();
		instructionText = activePlayer.isComputer() ? " May tinh đang thuc hien nuoc đi.\nHay chu y!"
				: "Giu nut \"DO XUC XAC\" đe đo xuc xac";
		dice = new Dice();
		resetDice();
		renderDiceNewGame();
		diceResult = 0;
		resetDiceIfNoMovesPossible = true;

		// Initialize flapSoundPlayed array based on total figures
		int totalFigures = players.length * 4; // Assuming 4 figures per player
		flapSoundPlayed = new boolean[totalFigures];
		for (int i = 0; i < totalFigures; i++) {
			flapSoundPlayed[i] = false;
		}
		diceSoundPlayed = false;

		if (Settings.isMusicEnabled) {
			sound.loop();
		} else {
			sound.stop();
		}

		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				delayEnded = true;
				timer.stop();
			}
		};
		timer = new Timer(2000, listener);
		delayEnded = true;
	}

	@Override
	public void leave(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.leave(gc, sbg);
		sound.stop();
		sound1.stop();
		sound2.stop();
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setBackground(new Color(241, 250, 238));
		g.drawImage(board, 0, 0);
		g.setColor(Color.black);
		g.drawString("Luot cua: " + activePlayer.getName(), 875, 125);
		g.drawString(instructionText, 875, 185);
		g.drawImage(normalDice, diceX, diceY);
		g.drawImage(dice1, diceX_hidden1, diceY_hidden1);
		g.drawImage(dice2, diceX_hidden2, diceY_hidden2);
		g.drawImage(dice3, diceX_hidden3, diceY_hidden3);
		g.drawImage(dice4, diceX_hidden4, diceY_hidden4);
		g.drawImage(dice5, diceX_hidden5, diceY_hidden5);
		g.drawImage(dice6, diceX_hidden6, diceY_hidden6);
		g.drawImage(gamePanel, board.getWidth() + (SCREEN_WIDTH - board.getWidth() - gamePanel.getWidth()) / 2, 20);
		g.drawImage(quitGame, quitX, quitY);
		g.drawImage(newGame, board.getWidth() + (SCREEN_WIDTH - board.getWidth() - newGame.getWidth()) / 2,
				SCREEN_HEIGHT - newGame.getHeight() - quitGame.getHeight() - 30);
		g.drawImage(settingGame, board.getWidth() + (SCREEN_WIDTH - board.getWidth() - settingGame.getWidth()) / 2,
				SCREEN_HEIGHT - settingGame.getHeight() - quitGame.getHeight() - 80);

		g.drawImage(roll, rollX, rollY);

		for (Player player : players) {
			player.draw(g);
		}
		g.setColor(Color.red);
		g.drawString(winningMsg, 875, 245);
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		mouseX = Mouse.getX();
		mouseY = SCREEN_HEIGHT - Mouse.getY();

		Input input = gc.getInput();
		renderSettingButton(input,sbg);
		renderNewGameButton(input, sbg);
		renderQuitGameButton(input);

		if (winningMsg.equals("") && delayEnded) {
			kickingAnimation();

			if (!kickingAnimation) {
				if (activePlayer.isReady) {
					resetDice();
					activePlayer.isReady = false;
					diceSoundPlayed = false; // Reset dice sound flag
				}

				if ((input.isMouseButtonDown(0) && resetDiceIfNoMovesPossible
						&& mouseX > rollX && mouseX < (rollX + roll.getWidth())
						&& mouseY > rollY && mouseY < rollY + roll.getHeight() || activePlayer.isComputer())
						&& !(rollingDicePhase && choosingFigurePhase)) {
					if (random > 0) {
						instructionText = activePlayer.isComputer() ? " May tinh dang thuc hien nuoc di.\nHay chu y!"
								: "Giu nut \"DO XUC XAC\" de do xuc xac";
						// Play dice sound at the start of rolling animation
						if (Settings.isDiceSoundEnabled && !diceSoundPlayed) {
							sound1.play();
							diceSoundPlayed = true;
						}
						toggleDices();
					} else if (random == 0) {
						resetDiceIfNoMovesPossible = false;
						rollingDicePhase = true;
						dice.cast();
						diceResult = dice.getResult();
						// Display the final dice result
						switch (diceResult) {
							case 1:
								diceX_hidden1 = (board.getWidth() + SCREEN_WIDTH - dice1.getWidth()) / 2;
								diceY_hidden1 = 380;
								break;
							case 2:
								diceX_hidden2 = (board.getWidth() + SCREEN_WIDTH - dice2.getWidth()) / 2;
								diceY_hidden2 = 380;
								break;
							case 3:
								diceX_hidden3 = (board.getWidth() + SCREEN_WIDTH - dice3.getWidth()) / 2;
								diceY_hidden3 = 380;
								break;
							case 4:
								diceX_hidden4 = (board.getWidth() + SCREEN_WIDTH - dice4.getWidth()) / 2;
								diceY_hidden4 = 380;
								break;
							case 5:
								diceX_hidden5 = (board.getWidth() + SCREEN_WIDTH - dice5.getWidth()) / 2;
								diceY_hidden5 = 380;
								break;
							case 6:
								diceX_hidden6 = (board.getWidth() + SCREEN_WIDTH - dice6.getWidth()) / 2;
								diceY_hidden6 = 380;
								break;
						}
						isMovesAvailable = false;
						for (Figure figure : activePlayer.getFigures()) {
							isMovesAvailable = isMovesAvailable || figure.movable(diceResult) != null;
							if (isMovesAvailable) {
								break;
							}
						}
						choosingFigurePhase = true;
						isChangeOfFigureAllowed = true;
						if (activePlayer.isComputer()) {
							delayEnded = false;
							timer.start();
						}
						if (!isMovesAvailable) {
							if (!activePlayer.isComputer()) {
								instructionText = "Khong co nuoc di kha dung.\nXuc xac se duoc dat lai.";
							}
							if (activePlayer.isComputer()) {
								delayEnded = false;
								timer.start();
							}
							rollingDicePhase = false;
							if (isEndTurn(diceResult)) {
								activePlayer = getNextPlayer();
								if (!activePlayer.isComputer()) {
									instructionText = " Giu nut \"DO XUC XAC\" de do xuc xac";
								}
							}
							delayEnded = false;
							timer.start();
							activePlayer.isReady = true;
						} else if (!activePlayer.isComputer()) {
							instructionText = "Hay thuc hien mot nuoc di! Sau do, \nxuc xac se duoc thiet lap lai.";
							resetDiceIfNoMovesPossible = true;
						} else {
							resetDiceIfNoMovesPossible = true;
						}
						random = -1;
					}
				}

				if (!isMovesAvailable && !input.isMouseButtonDown(0)) {
					resetDiceIfNoMovesPossible = true;
				}

				if (activePlayer.isComputer() && choosingFigurePhase && isMovesAvailable && delayEnded) {
					instructionText = "May tinh dang thuc hien nuoc di.\nHay chu y!";
					ComputerPlayer com = (ComputerPlayer) activePlayer;
					if (Settings.isFlapSoundEnabled) {
						sound2.play();
					}
					delayEnded = false;
					timer.start();
					com.findBestFigure(diceResult);
					com.move();
					if (activePlayer.getIsVictorious()) {
						winningMsg = getWinningMessage();
					}
					if (isEndTurn(diceResult)) {
						activePlayer = getNextPlayer();
					}
					if (!activePlayer.isComputer()) {
						instructionText = " Xuc xac se duoc lam lai.";
					}
					activePlayer.isReady = true;
					choosingFigurePhase = false;
				} else if (choosingFigurePhase && isChangeOfFigureAllowed && isMovesAvailable) {
					isChangeOfFigureAllowed = false;
					for (Figure figure : activePlayer.getFigures()) {
						if (figure.isAreaReactive(mouseX, mouseY, Mouse.isButtonDown(0))) {
							if (figure.movable(diceResult) != null) {
								if (Settings.isFlapSoundEnabled) {
									sound2.play();
								}
								figure.move();
								if (activePlayer.getIsVictorious()) {
									winningMsg = getWinningMessage();
								}
								figureWasMoved = true;
								if (isEndTurn(diceResult)) {
									activePlayer = getNextPlayer();
								}
								activePlayer.isReady = true;
								if (activePlayer.isComputer()) {
									delayEnded = false;
									timer.start();
								}
							} else {
								figureWasMoved = false;
							}
							break;
						}
					}

					if (!figureWasMoved) {
						isChangeOfFigureAllowed = true;
					} else {
						figureWasMoved = false;
						isChangeOfFigureAllowed = false;
						choosingFigurePhase = false;
					}
				}
			}
		}
	}

	private Player getNextPlayer() {
		int indexCurrentPlayer = 0;
		for (int i = 0; i < players.length; i++) {
			if (activePlayer == players[i]) {
				indexCurrentPlayer = i;
			}
		}
		return indexCurrentPlayer == players.length - 1 ? players[0] : players[indexCurrentPlayer + 1];
	}

	private void initializeBoard() {
		Positions.initialize();
		ArrayList<Player> listPlayers = new ArrayList<>();
		if (!Setup.isBlueInactive) {
			listPlayers.add(Setup.isBlueHuman ? new Player(Setup.bluePlayer.getText(), BLUE)
					: new ComputerPlayer(Setup.bluePlayer.getText(), BLUE));
		}
		if (!Setup.isYellowInactive) {
			listPlayers.add(Setup.isYellowHuman ? new Player(Setup.yellowPlayer.getText(), YELLOW)
					: new ComputerPlayer(Setup.yellowPlayer.getText(), YELLOW));
		}
		if (!Setup.isGreenInactive) {
			listPlayers.add(Setup.isGreenHuman ? new Player(Setup.greenPlayer.getText(), GREEN)
					: new ComputerPlayer(Setup.greenPlayer.getText(), GREEN));
		}
		if (!Setup.isRedInactive) {
			listPlayers.add(Setup.isRedHuman ? new Player(Setup.redPlayer.getText(), RED)
					: new ComputerPlayer(Setup.redPlayer.getText(), RED));
		}

		players = new Player[listPlayers.size()];
		for (int i = 0; i < players.length; i++) {
			players[i] = listPlayers.get(i);
		}

		activePlayer = players[(int) (Math.random() * players.length)];
	}

	private void kickingAnimation() {
		int figureIndex = 0;
		for (Player player : players) {
			for (Figure figure : player.getFigures()) {
				float startX = figure.getStartingPosition().getX();
				float currentX = figure.getCurrentPosition().getX();

				float startY = figure.getStartingPosition().getY();
				float currentY = figure.getCurrentPosition().getY();
				if (figure.isKicked && (Math.abs(currentX - startX) > 1 || Math.abs(currentY - startY) > 1)) {
					kickingAnimation = true;
					if (!fixedStep) {
						stepX = Math.abs(startX - currentX) / 100;
						stepY = Math.abs(startY - currentY) / 100;
						fixedStep = true;
						// Play flap sound if enabled and not played yet for this figure
						if (Settings.isFlapSoundEnabled && !flapSoundPlayed[figureIndex]) {
							sound2.play();
							flapSoundPlayed[figureIndex] = true;
						}
					}
					if (currentX > startX) {
						currentX = currentX - stepX;
					} else if (currentX < startX) {
						currentX = currentX + stepX;
					}

					if (currentY > startY) {
						currentY = currentY - stepY;
					} else if (currentY < startY) {
						currentY = currentY + stepY;
					}

					figure.setCurrentPosition(new Position(currentX, currentY));
				}
				if (figure.isKicked && (Math.abs(currentX - startX) <= 1 && Math.abs(currentY - startY) <= 1)) {
					figure.isKicked = false;
					fixedStep = false;
					figure.setCurrentPosition(figure.getStartingPosition());
					kickingAnimation = false;
					flapSoundPlayed[figureIndex] = false; // Reset flag when animation completes
				}
				figureIndex++;
			}
		}
	}

	private void toggleDices() {
		if (diceX > 0 && diceY > 0 || diceX_hidden6 > 0 && diceY_hidden6 > 0) {
			if (diceX > 0 && diceY > 0) {
				diceX_hidden1 = diceX;
				diceY_hidden1 = diceY;
				diceX = NEG_INFINITY;
				diceY = NEG_INFINITY;
				random--;
			} else {
				diceX_hidden1 = diceX_hidden6;
				diceY_hidden1 = diceY_hidden6;
				diceX_hidden6 = NEG_INFINITY;
				diceY_hidden6 = NEG_INFINITY;
				random--;
			}
		} else if (diceX_hidden1 > 0 && diceY_hidden1 > 0) {
			diceX_hidden2 = diceX_hidden1;
			diceY_hidden2 = diceY_hidden1;
			diceX_hidden1 = NEG_INFINITY;
			diceY_hidden1 = NEG_INFINITY;
			random--;
		} else if (diceX_hidden2 > 0 && diceY_hidden2 > 0) {
			diceX_hidden3 = diceX_hidden2;
			diceY_hidden3 = diceY_hidden2;
			diceX_hidden2 = NEG_INFINITY;
			diceY_hidden2 = NEG_INFINITY;
			random--;
		} else if (diceX_hidden3 > 0 && diceY_hidden3 > 0) {
			diceX_hidden4 = diceX_hidden3;
			diceY_hidden4 = diceY_hidden3;
			diceX_hidden3 = NEG_INFINITY;
			diceY_hidden3 = NEG_INFINITY;
			random--;
		} else if (diceX_hidden4 > 0 && diceY_hidden4 > 0) {
			diceX_hidden5 = diceX_hidden4;
			diceY_hidden5 = diceY_hidden4;
			diceX_hidden4 = NEG_INFINITY;
			diceY_hidden4 = NEG_INFINITY;
			random--;
		} else if (diceX_hidden5 > 0 && diceY_hidden5 > 0) {
			diceX_hidden6 = diceX_hidden5;
			diceY_hidden6 = diceY_hidden5;
			diceX_hidden5 = NEG_INFINITY;
			diceY_hidden5 = NEG_INFINITY;
			random--;
		} else {
			diceX_hidden1 = diceX_hidden6;
			diceY_hidden1 = diceY_hidden6;
			diceX_hidden6 = NEG_INFINITY;
			diceY_hidden6 = NEG_INFINITY;
			random--;
		}
	}

	private void resetDice() {
		random = (int) (Math.random() * 20) + 50;
		random_fixed = random;

		instructionText = activePlayer.isComputer() ? "May tinh dang thuc hien nuoc di.\nHay chu y!"
				: "Giu nut \"DO XUC XAC\" de do xuc xac";

		// Reset the dice image position to normal dice
		diceX = (board.getWidth() + SCREEN_WIDTH - normalDice.getWidth()) / 2;
		diceY = 380;
		diceX_hidden1 = NEG_INFINITY;
		diceY_hidden1 = NEG_INFINITY;
		diceX_hidden2 = NEG_INFINITY;
		diceY_hidden2 = NEG_INFINITY;
		diceX_hidden3 = NEG_INFINITY;
		diceY_hidden3 = NEG_INFINITY;
		diceX_hidden4 = NEG_INFINITY;
		diceY_hidden4 = NEG_INFINITY;
		diceX_hidden5 = NEG_INFINITY;
		diceY_hidden5 = NEG_INFINITY;
		diceX_hidden6 = NEG_INFINITY;
		diceY_hidden6 = NEG_INFINITY;
	}

	private boolean isEndTurn(int diceResult) {
		return diceResult != 1 && diceResult != 6;
	}

	private String getWinningMessage() {
		return "Chúc mừng! " + activePlayer.getName() + "\n"
				+ "đã chiến thắng trò chơi. Nhấn VÁN MỚI \n"
				+ "để bắt đầu trò chơi mới hoặc THOÁT \n"
				+ "để thoát trò chơi.";
	}

	private void renderNewGameButton(Input input, StateBasedGame sbg) {
		if (mouseX > board.getWidth() + (SCREEN_WIDTH - board.getWidth() - newGame.getWidth()) / 2
				&& mouseX < board.getWidth() + (SCREEN_WIDTH - board.getWidth() - newGame.getWidth()) / 2
				+ newGame.getWidth()
				&& mouseY > SCREEN_HEIGHT - newGame.getHeight() - quitGame.getHeight() - 30
				&& mouseY < SCREEN_HEIGHT - newGame.getHeight() - quitGame.getHeight() - 30
				+ newGame.getHeight()) {
			if (input.isMouseButtonDown(0)) {
				sbg.enterState(0);
			}
		}
	}
	private void renderSettingButton(Input input, StateBasedGame sbg) {
		if (mouseX > board.getWidth() + (SCREEN_WIDTH - board.getWidth() - settingGame.getWidth()) / 2
				&& mouseX < board.getWidth() + (SCREEN_WIDTH - board.getWidth() - settingGame.getWidth()) / 2
				+ settingGame.getWidth()
				&& mouseY > SCREEN_HEIGHT - settingGame.getHeight() - quitGame.getHeight() - 80
				&& mouseY < SCREEN_HEIGHT - settingGame.getHeight() - quitGame.getHeight() - 80
				+ settingGame.getHeight()) {
			if (input.isMouseButtonDown(0)) {
				Settings.current =PlayCurrent;
				sbg.enterState(4);
			}
		}
	}


	private void renderQuitGameButton(Input input) {
		if ((mouseX > quitX && mouseX < quitX + quitGame.getWidth())
				&& (mouseY > quitY && mouseY < quitY + quitGame.getHeight())) {
			if (input.isMouseButtonDown(0)) {
				System.exit(0);
			}
		}
	}

	public int getID() {
		return 2;
	}
}