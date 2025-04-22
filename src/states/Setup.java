package states;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.*;
import org.newdawn.slick.TrueTypeFont;

import java.awt.Font;

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
	Image exitt;
	Image background;

	String blueFigure = "Player 1:";
	String yellowFigure = "Player 2:";
	String greenFigure = "Player 3:";
	String redFigure = "Player 4:";

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
		exitt= new Image("res/button-exit-1.png");
		background = new Image("res/background-setup.png");

		mode_x = (SCREEN_WIDTH - com.getWidth()) / 2;
		TrueTypeFont font20 = new TrueTypeFont(new java.awt.Font("Tahoma", Font.PLAIN, 27), true);
		int textFieldHeight = 70;
		bluePlayer = new TextField(gc, font20, (int) (SCREEN_WIDTH * (1 - horizontalGap) - 200), human1_y, 250, textFieldHeight);
		yellowPlayer = new TextField(gc, font20, (int) (SCREEN_WIDTH * (1 - horizontalGap) - 200), com2_y, 250, textFieldHeight);
		greenPlayer = new TextField(gc, font20, (int) (SCREEN_WIDTH * (1 - horizontalGap) - 200), com3_y, 250, textFieldHeight);
		redPlayer = new TextField(gc, font20, (int) (SCREEN_WIDTH * (1 - horizontalGap) - 200), com4_y, 250, textFieldHeight);
//
		bluePlayer.setBackgroundColor(new Color(181, 221, 255));
		yellowPlayer.setBackgroundColor(new Color(255, 218, 194));
		greenPlayer.setBackgroundColor(new Color(204, 237, 209));
		redPlayer.setBackgroundColor(new Color(255, 168, 170));

		bluePlayer.setTextColor(Color.black);
		yellowPlayer.setTextColor(Color.black);
		greenPlayer.setTextColor(Color.black);
		redPlayer.setTextColor(Color.black);

		// Max character for name: 30 characters
		bluePlayer.setMaxLength(30);
		yellowPlayer.setMaxLength(30);
		greenPlayer.setMaxLength(30);
		redPlayer.setMaxLength(30);
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setColor(new Color(255, 255, 255));
		background.draw(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);


		bluePlayer.render(gc, g);
		yellowPlayer.render(gc, g);
		greenPlayer.render(gc, g);
		redPlayer.render(gc, g);

		choose.draw((SCREEN_WIDTH - choose.getWidth()) / 2, 25);
		int buttonGap = 40;
		int totalButtonWidth = start.getWidth() + buttonGap;  // Không tính nút Exit
		int buttonsStartX = (SCREEN_WIDTH - totalButtonWidth) / 2;  // Căn giữa trên màn hình
		int buttonsY = SCREEN_HEIGHT - start.getHeight() - 30;  // Vị trí Y của nút (cách đáy màn hình 30px)

		start.draw(buttonsStartX, buttonsY); // Nút Start

//		exitt.draw(buttonsStartX + start.getWidth() + buttonGap, buttonsY); // Nút Exit bên phải

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
				SCREEN_HEIGHT - start.getHeight() - 70);  // Điều chỉnh vị trí y của cảnh báo gần nút Start

		g.setColor(new Color(30, 61, 109));
		g.setFont(new TrueTypeFont(new java.awt.Font("Arial Unicode MS", java.awt.Font.BOLD, 27), true));
		g.drawString(blueFigure, (float) (SCREEN_WIDTH * 0.15), 185);
		g.setColor(new Color(247, 204, 66));
		g.setFont(new TrueTypeFont(new java.awt.Font("Arial Unicode MS", java.awt.Font.BOLD, 27), true));
		g.drawString(yellowFigure, (float) (SCREEN_WIDTH * 0.15), 340);
		g.setColor(new Color(45, 127, 63));
		g.setFont(new TrueTypeFont(new java.awt.Font("Arial Unicode MS", java.awt.Font.BOLD, 27), true));
		g.drawString(greenFigure, (float) (SCREEN_WIDTH * 0.15), 495);
		g.setColor(new Color(255, 168, 170));
		g.setFont(new TrueTypeFont(new java.awt.Font("Arial Unicode MS", java.awt.Font.BOLD, 27), true));
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
			if (input.isMouseButtonDown(0))  {  // Kiểm tra chuột trái đã được nhấn
			 // Kiểm tra chuột trái đã được nhấn

				// Kiểm tra các điều kiện của người chơi
				if ((bluePlayer.getText().isEmpty() && !isBlueInactive) ||
						(yellowPlayer.getText().isEmpty() && !isYellowInactive) ||
						(greenPlayer.getText().isEmpty() && !isGreenInactive) ||
						(redPlayer.getText().isEmpty() && !isRedInactive)) {
					warning = "Vui lòng nhập tên cho tất cả người chơi đang hoạt động!";
				}
				// Kiểm tra số lượng người chơi ít nhất 2
				else if ((isBlueInactive && isYellowInactive && isGreenInactive) ||
						(isBlueInactive && isYellowInactive && isRedInactive) ||
						(isBlueInactive && isGreenInactive && isRedInactive) ||
						(isYellowInactive && isGreenInactive && isRedInactive)) {
					warning = "Phải có ít nhất 2 người chơi để bắt đầu trò chơi!";
				}
				// Kiểm tra không có tất cả người chơi là máy
				else if ((4 - numOfInactive) == (4 - numOfHuman - numOfInactive)) {
					warning = "Phải có ít nhất 1 người chơi là người thật để bắt đầu!";
				} else {
					// Nếu tất cả điều kiện đều đúng, chuyển sang trạng thái trò chơi
					try {
						Thread.sleep(300);  // Tạm dừng một chút trước khi chuyển trạng thái
					} catch (Exception e) {
						System.out.println(e);
					}
					System.out.println("Tên người chơi xanh: " + bluePlayer.getText());
					System.out.println("isBlueInactive: " + isBlueInactive);
					sbg.enterState(2);  // Chuyển sang trạng thái tiếp theo (trạng thái trò chơi)
					System.out.println("xPos: " + xPos + ", yPos: " + yPos);
					System.out.println("startX: " + startX + ", startY: " + startY);
					System.out.println("startWidth: " + start.getWidth() + ", startHeight: " + start.getHeight());
				}
			}
		}
	}

	private void changePlayerMode(Input input, int xPos, int yPos) {


		// Player 1 - Blue
		if ((xPos > mode_x && xPos < mode_x + com.getWidth()) &&
				(yPos > SCREEN_HEIGHT - 175 - 80 && yPos < SCREEN_HEIGHT - 175)) {

			if (input.isMousePressed(0)) {
				if (isBlueHuman) {
					com1_y = human1_y;
					human1_y = NEG_INFINITY;
					isBlueHuman = false;
					bluePlayer.setText("AI");
					numOfHuman--;
				} else if (isBlueInactive) {
					human1_y = inactive1_y;
					inactive1_y = NEG_INFINITY;
					isBlueInactive = false;
					isBlueHuman = true;
					bluePlayer.setText("Player 1");
					numOfHuman++;
					numOfInactive--;
				} else {
					inactive1_y = com1_y;
					com1_y = NEG_INFINITY;
					isBlueInactive = true;
					isBlueHuman = false;
					bluePlayer.setText("No human player");
					numOfInactive++;
				}
			}
		}
		// Player 2 - Yellow
		else if ((xPos > mode_x && xPos < mode_x + com.getWidth()) &&
				(yPos > SCREEN_HEIGHT - 330 - 80 && yPos < SCREEN_HEIGHT - 330)) {
			if (input.isMousePressed(0)) {
				if (isYellowHuman) {
					com2_y = human2_y;
					human2_y = NEG_INFINITY;
					isYellowHuman = false;
					yellowPlayer.setText("AI");
					numOfHuman--;
				} else if (isYellowInactive) {
					human2_y = inactive2_y;
					inactive2_y = NEG_INFINITY;
					isYellowInactive = false;
					isYellowHuman = true;
					yellowPlayer.setText("Player 2");
					numOfHuman++;
					numOfInactive--;
				} else {
					inactive2_y = com2_y;
					com2_y = NEG_INFINITY;
					isYellowInactive = true;
					isYellowHuman = false;
					yellowPlayer.setText("No human player");
					numOfInactive++;
				}
			}
		}
		// Player 3 - Green
		else if ((xPos > mode_x && xPos < mode_x + com.getWidth()) &&
				(yPos > SCREEN_HEIGHT - 485 - 80 && yPos < SCREEN_HEIGHT - 485)) {

			if (input.isMousePressed(0)) {
				if (isGreenHuman) {
					com3_y = human3_y;
					human3_y = NEG_INFINITY;
					isGreenHuman = false;
					greenPlayer.setText("AI");
					numOfHuman--;
				} else if (isGreenInactive) {
					human3_y = inactive3_y;
					inactive3_y = NEG_INFINITY;
					isGreenInactive = false;
					isGreenHuman = true;
					greenPlayer.setText("Player 3");
					numOfHuman++;
					numOfInactive--;
				} else {
					inactive3_y = com3_y;
					com3_y = NEG_INFINITY;
					isGreenInactive = true;
					isGreenHuman = false;
					greenPlayer.setText("No human player");
					numOfInactive++;
				}
			}
		}
		// Player 4 - Red
		else if ((xPos > mode_x && xPos < mode_x + com.getWidth()) &&
				(yPos > SCREEN_HEIGHT - 640 - 80 && yPos < SCREEN_HEIGHT - 640)) {

			if (input.isMousePressed(0)) {
				if (isRedHuman) {
					com4_y = human4_y;
					human4_y = NEG_INFINITY;
					isRedHuman = false;
					redPlayer.setText("AI");
					numOfHuman--;
				} else if (isRedInactive) {
					human4_y = inactive4_y;
					inactive4_y = NEG_INFINITY;
					isRedInactive = false;
					isRedHuman = true;
					redPlayer.setText("Player 4");
					numOfHuman++;
					numOfInactive--;
				} else {
					inactive4_y = com4_y;
					com4_y = NEG_INFINITY;
					isRedInactive = true;
					isRedHuman = false;
					redPlayer.setText("No human player");
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