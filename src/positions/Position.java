package positions;

import enums.PositionType;
import figure.Figure;

public class Position {
    protected int id;
    protected float x, y;
    protected PositionType type;
    private boolean isOccupied;
    private Figure occupyingFigure;

    public Position(int id, float x, float y, PositionType type) {
        this.id = id;
        this.x = x;
        this.y = y;
        if (type == PositionType.START) {
            isOccupied = true;
        }
        this.type = type;
    }

    public Position(float x, float y) {
        id = -1;
        this.x = x;
        this.y = y;
        type = PositionType.NORMAL;
    }

    public int getId() {
        return id;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float newX) {
        x = newX;
    }

    public void setY(float newY) {
        y = newY;
    }

    public boolean getIsOccupied() {
        return isOccupied;
    }

    public void setIsOccupied(boolean isOccupied, Figure occupyingFigure) {
        this.isOccupied = isOccupied;
        this.occupyingFigure = isOccupied ? occupyingFigure : null;
    }

    public Figure getOccupyingFigure() {
        return isOccupied ? occupyingFigure : null;
    }

    public PositionType getType() {
        return type;
    }

    public boolean equals(Position other) {
        return this.x == other.x && this.y == other.y;
    }
}
