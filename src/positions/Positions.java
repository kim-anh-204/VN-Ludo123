package positions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import enums.FigureColor;
import enums.PositionType;

public class Positions {
    private static Position[] listNormalPositions;
    private static Position[] listStartingPositions;
    private static Position[] listFinishingPositions;

    public static void initialize() {
        listNormalPositions = new Position[56];
        listStartingPositions = new Position[16];
        listFinishingPositions = new Position[24];

        String[] info;
        int id;
        int startX;
        int startY;

        try {
            Scanner scanner = new Scanner(new File("positionsInfo/normalPositionsInfo.txt"));
            while (scanner.hasNextLine()) {
                info = scanner.nextLine().split(";");
                id = Integer.parseInt(info[0]);
                startX = Integer.parseInt(info[1]);
                startY = Integer.parseInt(info[2]);
                listNormalPositions[id - 1] = new Position(id, startX, startY, PositionType.NORMAL);
            }

            scanner = new Scanner(new File("positionsInfo/startPositionsInfo.txt"));
            while (scanner.hasNextLine()) {
                info = scanner.nextLine().split(";");
                id = Integer.parseInt(info[0]);
                startX = Integer.parseInt(info[1]);
                startY = Integer.parseInt(info[2]);
                listStartingPositions[id - 1] = new Position(id, startX, startY, PositionType.START);
            }

            scanner = new Scanner(new File("positionsInfo/finishPositionsInfo.txt"));
            while (scanner.hasNextLine()) {
                info = scanner.nextLine().split(";");
                id = Integer.parseInt(info[0]);
                startX = Integer.parseInt(info[1]);
                startY = Integer.parseInt(info[2]);
                listFinishingPositions[id - 1] = new Position(id, startX, startY, PositionType.FINISH);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Position getPositionFromCoordinates(float x, float y) {
        return getNormalPositionFromCoordinates(x, y) != null ? getNormalPositionFromCoordinates(x, y) :
                getStartingPositionFromCoordinates(x, y) != null ? getStartingPositionFromCoordinates(x, y) :
                        getFinishingPositionFromCoordinates(x, y);
    }

    public static Position getNormalPositionFromCoordinates(float x, float y) {
        for (Position normalPosition : listNormalPositions) {
            if (normalPosition.getX() == x && normalPosition.getY() == y) {
                return normalPosition;
            }
        }
        return null;
    }

    public static Position getStartingPositionFromCoordinates(float x, float y) {
        for (Position startingPosition : listStartingPositions) {
            if (startingPosition.getX() == x && startingPosition.getY() == y) {
                return startingPosition;
            }
        }
        return null;
    }

    public static Position getFinishingPositionFromCoordinates(float x, float y) {
        for (Position finishingPosition : listFinishingPositions) {
            if (finishingPosition.getX() == x && finishingPosition.getY() == y) {
                return finishingPosition;
            }
        }
        return null;
    }

    public static Position[] getNormalPositions() {
        return listNormalPositions;
    }

    public static Position getNextPosition(Position position) {
        if (position.getType() != PositionType.NORMAL) {
            throw new RuntimeException("Next position is not applicable for this position type.");
        }
        int id = position.getId();
        if (id == 56)
            return listNormalPositions[0];
        else if (id >= 1 && id <= 55)
            return listNormalPositions[id];
        else
            return null;
    }

    public static Position[] getStartingPositions(FigureColor color) {
        Position[] result = new Position[4];
        int startIndex;
        switch (color) {
            case BLUE:
                startIndex = 0;
                break;
            case YELLOW:
                startIndex = 4;
                break;
            case GREEN:
                startIndex = 8;
                break;
            case RED:
                startIndex = 12;
                break;
            default:
                throw new RuntimeException("Color does not exist");
        }
        int endIndex = startIndex + 4;
        for (int i = startIndex; i < endIndex; i++) {
            result[i - startIndex] = listStartingPositions[i];
        }
        return result;
    }

    public static Position[] getFinishingPositions(FigureColor color) {
        Position[] result = new Position[6];
        int startIndex;
        switch (color) {
            case BLUE:
                startIndex = 0;
                break;
            case YELLOW:
                startIndex = 6;
                break;
            case GREEN:
                startIndex = 12;
                break;
            case RED:
                startIndex = 18;
                break;
            default:
                throw new RuntimeException("Color does not exist");
        }
        int endIndex = startIndex + 6;
        for (int i = startIndex; i < endIndex; i++) {
            result[i - startIndex] = listFinishingPositions[i];
        }
        return result;
    }

    public static Position getEntryPosition(FigureColor color) {
        Position result;
        switch (color) {
            case BLUE:
                result = listNormalPositions[0];
                break;
            case YELLOW:
                result = listNormalPositions[14];
                break;
            case GREEN:
                result = listNormalPositions[28];
                break;
            case RED:
                result = listNormalPositions[42];
                break;
            default:
                throw new RuntimeException("Color does not exist");
        }
        return result;
    }

    public static Position getTransitionPosition(FigureColor color) {
        Position result;
        switch (color) {
            case BLUE:
                result = listNormalPositions[55];
                break;
            case YELLOW:
                result = listNormalPositions[13];
                break;
            case GREEN:
                result = listNormalPositions[27];
                break;
            case RED:
                result = listNormalPositions[41];
                break;
            default:
                throw new RuntimeException("Color does not exist");
        }
        return result;
    }

    public static boolean isNormalPosition(Position position) {
        for (Position normalPosition : listNormalPositions) {
            if (position == normalPosition) {
                return true;
            }
        }
        return false;
    }

    public static boolean isStartingPosition(Position startingPosition, FigureColor color) {
        for (Position position : Positions.getStartingPositions(color)) {
            if (position == startingPosition) {
                return true;
            }
        }
        return false;
    }

    public static boolean isFinishingPosition(Position finishingPosition, FigureColor color) {
        for (Position position : Positions.getFinishingPositions(color)) {
            if (position == finishingPosition) {
                return true;
            }
        }
        return false;
    }

    public static int distanceFromEntryPosition(Position position, FigureColor color) {
        if (position.getType() != PositionType.NORMAL) {
            throw new RuntimeException("This method is not applicable for this position");
        }
        Position entryPosition = getEntryPosition(color);
        int count = 0;
        Position tempPosition = entryPosition;
        while (tempPosition != position) {
            tempPosition = getNextPosition(tempPosition);
            count++;
        }
        return count;
    }
}
