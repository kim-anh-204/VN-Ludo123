package figure;

import org.newdawn.slick.*;

import enums.FigureColor;
import positions.*;

public class Figure  {
    private FigureColor color;
    private Position currentPosition;
    private final Position startingPosition;
    private final Position[] finishingPositions;
    private int stepsFromEntryPosition;
    private int temporaryStepsCount;

    private Position temporaryOldPosition;
    private Position temporaryNewPosition;

    public boolean isKicked;
    public int movePriority;

    public Figure(Position startPosition, FigureColor color) {
        currentPosition = startPosition;
        this.currentPosition.setIsOccupied(true, this);//da bi chiem cho
        this.startingPosition = Positions.isStartingPosition(startPosition, color) ? startPosition : null;
        this.color = color;
        stepsFromEntryPosition = 0;
        finishingPositions = Positions.getFinishingPositions(color);
        isKicked = false;
    }
//ve quan co o vi tri hien tai
    public void draw(Graphics g) throws SlickException {
        g.drawImage(new Image("res/" + color.toString().toLowerCase() + "-figure.png"), currentPosition.getX(), currentPosition.getY());
    }

    public FigureColor getColor() {
        return color;
    }

    public Position getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Position newPosition) {
        currentPosition = newPosition;
        if (currentPosition == startingPosition) {
            // The figure was kicked
            stepsFromEntryPosition = 0;
            temporaryOldPosition = temporaryNewPosition = null;
        }
    }

    public Position getStartingPosition() {
        return startingPosition;
    }

    public int getDistanceFromEntryPosition() {
        return stepsFromEntryPosition;
    }

    public Position movable(int diceResult) {
        return checkMovesAvailable(diceResult) ? temporaryNewPosition : null;

    }// trả lại vị trí tạm thời nếu di chuyển hợp lệ
    private boolean checkMovesAvailable(int diceResult) {
        if (currentPosition == startingPosition) {
            // Nếu xúc xắc ra 1 hoặc 6 và vị trí hiện tại là bắt đầu
            if (diceResult == 1 || diceResult == 6) {
                return isEnteringBoardPossible(); // Kiểm tra vị trí ra chuồng có trống không
            }
            return false; // Nếu không phải 1 hoặc 6, không thể ra chuồng
        }

        // Kiểm tra di chuyển đến các vị trí khác trên bàn chơi
        return currentPosition == Positions.getTransitionPosition(color) || Positions.isFinishingPosition(currentPosition, color) ?
                finishMove(diceResult) : isNormalMovePossible(diceResult);
    }

    public void move() {
        Figure potentialKickFigure = temporaryNewPosition.getOccupyingFigure();
        temporaryOldPosition.setIsOccupied(false, null);
        if (temporaryNewPosition.getIsOccupied() && potentialKickFigure.getColor() != this.color) {
            potentialKickFigure.isKicked = true;
        }
        temporaryNewPosition.setIsOccupied(true, this);
        currentPosition = temporaryNewPosition;
        stepsFromEntryPosition += temporaryStepsCount;
        temporaryOldPosition = temporaryNewPosition = null;
        temporaryStepsCount = 0;
    }

    private boolean isEnteringBoardPossible() {
        Position entryPosition = Positions.getEntryPosition(color);
        if (entryPosition.getIsOccupied()) {
            return false;
        }
        temporaryOldPosition = currentPosition;
        temporaryNewPosition = entryPosition;
        return true;
    }

    private boolean isNormalMovePossible(int steps) {
        Position oldPosition = currentPosition;
        Position tempPosition = currentPosition;
        temporaryStepsCount = 0;
        for (int i = 0; i < steps; i++) {
            // Set current position to the next one
            tempPosition = Positions.getNextPosition(tempPosition);

            // If the figure has reached its transition position, it cannot continue going another round. So either it moved exactly
            // the number of required steps to land on its home square, or it cannot move at all if it was supposed to move further.
            if (tempPosition == Positions.getTransitionPosition(color) && i != steps - 1) {
                // Reset the figure to its original position
                tempPosition = oldPosition;
                return false;
            }

            // Check whether the figure has landed on an entry position of another color. In this case the figure will automatically
            // move to the next position.
            for (FigureColor color : FigureColor.values()) {
                if (tempPosition == Positions.getEntryPosition(color)) {
                    tempPosition = Positions.getNextPosition(tempPosition);
                }
            }

            // Check whether a figure is in the way
            if (tempPosition.getIsOccupied()) {
                Figure occupyingFigure = tempPosition.getOccupyingFigure();

                // If the figure has not yet completed its movement and another figure is in the way (regardless of color),
                // or if at the destination position stands a figure of the same color, then the move with the figure is illegal.
                if (i != steps - 1 || color == occupyingFigure.getColor()) {
                    tempPosition = oldPosition;
                    return false;
                }
            }

            if (i == steps - 1) {
                temporaryOldPosition = oldPosition;
                temporaryNewPosition = tempPosition;
                temporaryStepsCount = steps;
            }
        }

        return true;
    }

    private boolean finishMove(int destinationPosition) {
        Position oldPosition = currentPosition;

        // Find the position of the figure on the finish column
        int positionOnFinishColumn = -1;
        for (int i = 0; i < 6; i++) {
            if (finishingPositions[i] == oldPosition) {
                positionOnFinishColumn = i;
                break;
            }
        }
        if (positionOnFinishColumn >= destinationPosition - 1) {
            return false;
        }
        for (int i = positionOnFinishColumn + 1; i < destinationPosition; i++) {
            if (finishingPositions[i].getIsOccupied()) {
                return false;
            }
        }
        temporaryOldPosition = oldPosition;
        temporaryNewPosition = finishingPositions[destinationPosition - 1];
        return true;
    }

    public boolean isAreaReactive(int x, int y, boolean isMouseDown) {
        boolean result = isMouseDown && currentPosition.getX() <= x && x <= currentPosition.getX() + 50
                && currentPosition.getY() <= y && y <= currentPosition.getY() + 50;
        return result;
    }
}
