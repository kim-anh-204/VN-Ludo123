package players;

import figure.*;
import positions.Position;
import positions.Positions;

import static enums.FigureColor.*;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import enums.FigureColor;

public class Player{
    private int consecutiveMovesCount;

    protected String name;
    protected FigureColor color;
    protected Figure[] figures;

    public boolean isReady = false;

    public Player(String name, FigureColor color) {
        this.name = name;
        this.color = color;
        figures = new Figure[4];
        consecutiveMovesCount = 1;
        for (int i = 0; i < 4; i++) {
            switch (color) {
                case BLUE:
                    figures[i] = new Figure(Positions.getStartingPositions(BLUE)[i], BLUE);
                    break;
                case YELLOW:
                    figures[i] = new Figure(Positions.getStartingPositions(YELLOW)[i], YELLOW);
                    break;
                case GREEN:
                    figures[i] = new Figure(Positions.getStartingPositions(GREEN)[i], GREEN);
                    break;
                case RED:
                    figures[i] = new Figure(Positions.getStartingPositions(RED)[i], RED);
                    break;
                default:
                    throw new RuntimeException("Color does not exist");
            }
        }
    }

    public Player(Figure[] figures) {
        this.figures = figures;
        this.color = figures[0].getColor();
        consecutiveMovesCount = 1;
    }

    public void draw(Graphics g) throws SlickException {
        for (Figure figure : figures) {
            figure.draw(g);
        }
    }

    public String getName() {
        return name+" ("+color.toString()+")";
    }

    public FigureColor getColor() {
        return color;
    }

    public Figure[] getFigures() {
        return figures;
    }

    public boolean getIsVictorious() {
        Position[] finishingPositions = Positions.getFinishingPositions(color);
        for (int i = 2; i <= 5; i++) {
            if (!finishingPositions[i].getIsOccupied()) {
                return false;
            }
        }
        return true;
    }

    public boolean allFiguresAtStartingPosition() {
        for (Figure figure : figures) {
            if (figure.getCurrentPosition() != figure.getStartingPosition()) {
                return false;
            }
        }

        return true;
    }

    public boolean threeMovesExceeded(int diceResult) {
        if (consecutiveMovesCount != 3) {
            consecutiveMovesCount = diceResult == 6 || allFiguresAtStartingPosition() ? consecutiveMovesCount + 1 : 1;
            return false;
        }
        else {
            consecutiveMovesCount = 1;
            return true;
        }
    }

    public boolean isComputer() {
        return false;
    }
}
