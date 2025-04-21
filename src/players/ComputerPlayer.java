package players;

import java.util.ArrayList;

import enums.FigureColor;
import figure.*;
import positions.Position;
import positions.Positions;

public class ComputerPlayer extends Player {
	private Figure bestFigure;
	
	public ComputerPlayer(String name, FigureColor color) {
		super(name, color);
	}
	
	public ComputerPlayer(Figure[] comFigures) {
		super(comFigures);
	}

	@Override
	public boolean isComputer() {
		return true;
	}
	
	private void setMovesPriority(int diceResult) {
		for (Figure figure : figures) {
			Position destination = figure.movable(diceResult);
			if (destination == null) {
				figure.movePriority = 100;
			}
			else {
				if (destination.getIsOccupied()) {
					// If the figure can move and its destination is occupied, then the occupying figure must be
					// of another color
					figure.movePriority = 1;
				}
				else if (figure.getCurrentPosition() == Positions.getTransitionPosition(color)) {
					figure.movePriority = 2;
				}
				else if (Positions.isStartingPosition(figure.getCurrentPosition(), color)) {
					figure.movePriority = 3;
				}
				else if (!Positions.isFinishingPosition(figure.getCurrentPosition(), color) 
						&& figure.getCurrentPosition() != Positions.getEntryPosition(color)) {
					figure.movePriority = 4;
				}
				else if (Positions.isFinishingPosition(figure.getCurrentPosition(), color)) {
					figure.movePriority = 5;
				}
				else {
					if (figure.getCurrentPosition() != Positions.getEntryPosition(color)) {
						throw new RuntimeException("Wrong computer logic: Figure at coordinates "
								+ figure.getCurrentPosition().getX() + ";" + figure.getCurrentPosition().getY()
								+ " has a better move.");
					}
					figure.movePriority = 6;
				}
			}
		}
	}
	
	public Figure findBestFigure(int diceResult) {
		Figure result;
		setMovesPriority(diceResult);
		
		ArrayList<Figure> figuresWithHighestPriority = new ArrayList<>();
		int highestPriority = 10;
		for (Figure figure : figures) {
			if (figure.movePriority < highestPriority) {
				highestPriority = figure.movePriority;
				figuresWithHighestPriority.clear();
				figuresWithHighestPriority.add(figure);
			}
			else if (figure.movePriority == highestPriority) {
				figuresWithHighestPriority.add(figure);
			}
		}
		
		if (figuresWithHighestPriority.size() == 0) {
			return null;
		}
		
		if (figuresWithHighestPriority.size() == 1) {
			result = figuresWithHighestPriority.get(0);
		}
		else {
			int numberFiguresHighestPriority = figuresWithHighestPriority.size();
			if (numberFiguresHighestPriority > 1 && (highestPriority != 1 && highestPriority != 3 && highestPriority != 4)) {
				throw new RuntimeException("Impossible that two or more figures have the same move priority " + highestPriority);
			}
			if (highestPriority == 3) {
				result = figuresWithHighestPriority.get(0);
			}
			else {
				int maxDistance = 0;
				int maxDistanceIndex = 0;
				for (int i = 0; i < numberFiguresHighestPriority; i++) {
					int currentDistance = 
							Integer.max(figuresWithHighestPriority.get(i).getDistanceFromEntryPosition(), Positions.distanceFromEntryPosition(figuresWithHighestPriority.get(i).getCurrentPosition(), color));
					if (currentDistance > maxDistance) {
						maxDistance = currentDistance;
						maxDistanceIndex = i;
					}
				}
				result = figuresWithHighestPriority.get(maxDistanceIndex);
			}
		}
		
		bestFigure = result;
		return result;
	}
	
	public void move() {
		bestFigure.move();
	}
}
