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

	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.enter(gc, sbg);
		isMovesAvailable = false;
		initializeBoard();
		instructionText = activePlayer.isComputer() ? " May tinh dang thuc hien nuoc di.\nHay chu y!"
				: "Giu nut \"DO XUC XAC\" de do xuc xac";
		dice = new Dice();
		resetDice();
		renderDiceNewGame();
		diceResult = 0;
		resetDiceIfNoMovesPossible = true;

		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				delayEnded = true;
				timer.stop();
			}
		};
		timer = new Timer(2000, listener);
		delayEnded = true;
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
		quitGame = new Image("res/quit-game.png");
		gamePanel = new Image("res/game-panel.png");

		quitX = board.getWidth() + (SCREEN_WIDTH - board.getWidth() - quitGame.getWidth()) / 2;
		quitY = SCREEN_HEIGHT - quitGame.getHeight() - 20;
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setBackground(new Color(241, 250, 238));
		g.drawImage(board, 0, 0);
		g.setColor(Color.black);
		g.drawString("Luot cua: " + activePlayer.getName(), 875, 125);
		g.drawString(instructionText, 875, 185);
		//		g.drawString(getAvailableMovesText(), 875, 185);
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
				SCREEN_HEIGHT - newGame.getHeight() - quitGame.getHeight() - 40);

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

		renderNewGameButton(input, sbg);
		renderQuitGameButton(input);

		if (winningMsg.equals("") && delayEnded) {
			kickingAnimation();

			if (!kickingAnimation) {

				// Reset the dice and end one's turn when finish one's moves or no move is
				// available
				if (activePlayer.isReady) {
					resetDice();
					activePlayer.isReady = false;
				}

				// Rolling dice phase by clicking and holding the roll button, until a result
				// appears
				if ((input.isMouseButtonDown(0) && resetDiceIfNoMovesPossible
						&& mouseX > rollX && mouseX < (rollX + roll.getWidth())
						&& mouseY > rollY && mouseY < rollY + roll.getHeight() || activePlayer.isComputer())
						&& !(rollingDicePhase && choosingFigurePhase)) {
					// random > 0 -> holding the button works
					if (random > 0) {
						instructionText = activePlayer.isComputer() ? " May tinh dang thuc hien nuoc di.\nHay chu y!"
								: "Giu nut \"DO XUC XAC\" de do xuc xac";
						toggleDices();
					} else if (random == 0) {
						resetDiceIfNoMovesPossible = false;
						rollingDicePhase = true;
						dice.cast();
						diceResult = dice.getResult();
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
								instructionText = "Khong co nuoc di kha dung.\nXuc xac se duoc dat lai. ";
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
						}
						else if (!activePlayer.isComputer()){
							instructionText = "Hay thuc hien mot nuoc di! Sau do, "
									+ "\nxuc xac se duoc thiet lap lai.";
							resetDiceIfNoMovesPossible = true;
						}
						else {
							resetDiceIfNoMovesPossible = true;
						}
						random = -1; // set to -1 so players cannot press the button anymore
					}
				}

				if (!isMovesAvailable && !input.isMouseButtonDown(0)) {
					resetDiceIfNoMovesPossible = true;
				}

				if (activePlayer.isComputer() && choosingFigurePhase && isMovesAvailable && delayEnded) {
					instructionText = "May tinh dang thuc hien nuoc di.\nHay chu y!";
					ComputerPlayer com = (ComputerPlayer) activePlayer;
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
		for (Player player : players) {
			for (Figure figure : player.getFigures()) {
				float startX = figure.getStartingPosition().getX();
				float currentX = figure.getCurrentPosition().getX();

				float startY = figure.getStartingPosition().getY();
				float currentY = figure.getCurrentPosition().getY();
				if (figure.isKicked == true
						&& (Math.abs(currentX - startX) > 1 || Math.abs(currentY - startY) > 1)) {
					kickingAnimation = true;
					if (!fixedStep) {
						stepX = Math.abs(startX - currentX) / 100;
						stepY = Math.abs(startY - currentY) / 100;
						fixedStep = true;
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
				if (figure.isKicked == true
						&& (Math.abs(currentX - startX) <= 1 && Math.abs(currentY - startY) <= 1)) {
					figure.isKicked = false;
					fixedStep = false;
					figure.setCurrentPosition(figure.getStartingPosition());
					kickingAnimation = false;
				}
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

		// reset the dice image position
		switch (diceResult) {
			case 1:
				diceX = diceX_hidden1;
				diceY = diceY_hidden1;

				diceX_hidden1 = NEG_INFINITY;
				diceY_hidden1 = NEG_INFINITY;
				break;
			case 2:
				diceX = diceX_hidden2;
				diceY = diceY_hidden2;

				diceX_hidden2 = NEG_INFINITY;
				diceY_hidden2 = NEG_INFINITY;
				break;
			case 3:
				diceX = diceX_hidden3;
				diceY = diceY_hidden3;

				diceX_hidden3 = NEG_INFINITY;
				diceY_hidden3 = NEG_INFINITY;
				break;
			case 4:
				diceX = diceX_hidden4;
				diceY = diceY_hidden4;

				diceX_hidden4 = NEG_INFINITY;
				diceY_hidden4 = NEG_INFINITY;
				break;
			case 5:
				diceX = diceX_hidden5;
				diceY = diceY_hidden5;

				diceX_hidden5 = NEG_INFINITY;
				diceY_hidden5 = NEG_INFINITY;
				break;
			case 6:
				diceX = diceX_hidden6;
				diceY = diceY_hidden6;

				diceX_hidden6 = NEG_INFINITY;
				diceY_hidden6 = NEG_INFINITY;
				break;
		}
	}
	//nếu chưa có quân nào ra chuồng đc 3 lần quay xx
	private boolean isEndTurn(int diceResult) {
		return diceResult != 1 && diceResult != 6;
	}



	private String getWinningMessage() {
		return "Chuc mung! " + activePlayer.getName() + "\n"
				+ "da chien thang tro choi. Nhan \n"
				+ "VAN MOI de bat dau tro choi moi hoac \n"
				+ "THOAT de thoat tro choi.";}

	private void renderNewGameButton(Input input, StateBasedGame sbg) {
		if (mouseX > board.getWidth() + (SCREEN_WIDTH - board.getWidth() - newGame.getWidth()) / 2
				&& mouseX < board.getWidth() + (SCREEN_WIDTH - board.getWidth() - newGame.getWidth()) / 2
				+ newGame.getWidth()
				&& mouseY > SCREEN_HEIGHT - newGame.getHeight() - quitGame.getHeight() - 40
				&& mouseY < SCREEN_HEIGHT - newGame.getHeight() - quitGame.getHeight() - 40
				+ newGame.getHeight()) {
			if (input.isMouseButtonDown(0)) {
				sbg.enterState(1);
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