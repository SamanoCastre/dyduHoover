package com.dydu.hoover;

import java.util.ArrayList;
import java.util.List;

import com.dydu.hoover.exceptions.NoDataFoundException;
import com.dydu.hoover.exceptions.StartFailException;

/**
 * @author Samano CASTRE
 */
public class Pathway {
	
	private enum Direction {
		LEFT, RIGHT, DOWN, UP
	};
	private List<Point> cleanPoints = new ArrayList<Point>(); //Map of points already clean
	private int nbPointsToClean = 0;//Number of points to clean
	private String[][] matrix;
	
	public Pathway() {
		
	}
	
	/**
	 * pathway must be initialize
	 * @param matrix
	 * @throws NoDataFoundException
	 */
	public void init(String[][] matrix) throws NoDataFoundException {
		this.matrix = matrix;
		if((this.matrix == null || this.matrix.length == 0 || (this.matrix.length==1 && this.matrix[0].length == 0))) {
			 throw new NoDataFoundException("No data found in the file");
		}
		for (String[] line  : matrix) {
			for (String element  : line) {
				if(!element.toUpperCase().equals("M")) {
					this.nbPointsToClean ++;
				}
			}
		}
	}
	
	/**
	 * This method allows us to move the hoover from a start point to different directions
	 * @param startPoint
	 * @throws StartFailException
	 */
	public void move() throws StartFailException {
		
		Point startPoint = this.getNewStartPoint();
		
		int lineIndex = startPoint.getX();
		int elementIndex = startPoint.getY();
		
		if(this.isWall(startPoint)) {
			throw new StartFailException("Can not start, this is the point of a wall");
		}
		
		if(!this.isClean(startPoint)) {
			this.cleanPoints.add(startPoint);
		}
		
		Direction direction = this.getDirection(startPoint);
		
		while(direction != null) {
			
			if(direction.equals(Direction.RIGHT)) {
				
				elementIndex++;
				while(!this.isWall(new Point(lineIndex, elementIndex)) && !this.isClean(new Point(lineIndex, elementIndex))) {
					this.cleanPoints.add(new Point(lineIndex, elementIndex));
					elementIndex = (elementIndex < this.matrix[lineIndex].length-1) ? elementIndex++ : elementIndex;
				}
			}
			if(direction.equals(Direction.DOWN)) {
				lineIndex++;
				while(!this.isWall(new Point(lineIndex, elementIndex)) && !this.isClean(new Point(lineIndex, elementIndex))) {
					this.cleanPoints.add(new Point(lineIndex, elementIndex));
					lineIndex = lineIndex < this.matrix.length-1 ? lineIndex++ : lineIndex;
				}
			}
			if(direction.equals(Direction.LEFT)) {
				 elementIndex--;
				while(!this.isWall(new Point(lineIndex, elementIndex)) && !this.isClean(new Point(lineIndex, elementIndex))) {
					this.cleanPoints.add(new Point(lineIndex, elementIndex));
					elementIndex = elementIndex > 0 ? elementIndex-- : elementIndex;
				}
			}
			if(direction.equals(Direction.UP)) {
				 lineIndex--;
				while(!this.isWall(new Point(lineIndex, elementIndex)) && !this.isClean(new Point(lineIndex, elementIndex))) {
					this.cleanPoints.add(new Point(lineIndex, elementIndex));
					lineIndex = lineIndex > 0 ? lineIndex-- : lineIndex;
				}
			}
			direction =  elementIndex < this.matrix[lineIndex].length-1 ? 
					this.getDirection(new Point(lineIndex, elementIndex)) : null;
		}
	}
	
	
   /**
    * This method allows us to know in wich direction our hoover can move easily
    * @param lineIndex
    * @param elementIndex
    * @return
    */
   private Direction getDirection(Point point) {
	   
		if(point.getY() < this.matrix[point.getX()].length-1 && 
				!this.isWall(new Point(point.getX(), point.getY() + 1)) && 
				!this.isClean(new Point(point.getX(), point.getY() + 1))) {
			return Direction.RIGHT;
		}
		
		if(point.getY() > 0 && 
				!this.isWall(new Point(point.getX(), point.getY() - 1)) && 
				!this.isClean(new Point(point.getX(), point.getY() - 1))) {
			return Direction.LEFT;
		}
		
		if(point.getX() < this.matrix.length -1 &&
				!this.isWall( new Point(point.getX() + 1, point.getY())) && 
				!this.isClean(new Point(point.getX() + 1, point.getY()))) {
			return Direction.DOWN;
		}
		
		if(point.getX() > 0 &&
				!this.isWall(new Point(point.getX() - 1, point.getY())) && 
				!this.isClean(new Point(point.getX() - 1, point.getY()))) {
			return Direction.UP;
		}
		return null;
   }
   
   /**
	 * This method gives us a new start point everytime the hoover hit can go nowhere
	 * @param matrix
	 * @return
	 */
	public Point getNewStartPoint() {
		
		Point startPoint = null;
		
		int countLine = 0;
		while(startPoint == null && countLine < this.matrix.length) {
			String[] line = this.matrix[countLine];
			
			int countElement = 0;
			while(countElement < line.length && startPoint == null) {
				
				if(!this.isWall(new Point(countLine, countElement)) && !this.isClean(new Point(countLine, countElement)) ) {
					startPoint = new Point(countLine, countElement);
				}
				else {
					countElement++;
				}
			}
			countLine++;
		}
		return startPoint;
	}
	
	public List<Point> getCleanPoints() {
		return cleanPoints;
	}

	public int getNbPointsToClean() {
		return nbPointsToClean;
	}
	
	/**
	 * This method allows us to check if this Point is already clean or not
	 * @param lineIndex
	 * @param elementIndex
	 * @return
	 */
	public boolean isClean(Point point) {
		boolean isExist = false;
		int count = 0;
		while(!isExist && count < this.getCleanPoints().size()) {
			Point p = this.getCleanPoints().get(count);
			isExist = point.equals(p);
			count ++;
		}
		return isExist;
	}
	
   /**
    * THis method allows us to check if this Point is a wall or not
    * @param lineIndex
    * @param elementIndex
    * @return
    */
	public boolean isWall(Point point) {
		String pointArr = matrix[point.getX()][point.getY()];
		return pointArr.toUpperCase().equals("M");
	}

	public String[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(String[][] matrix) {
		this.matrix = matrix;
	}	
	
	public boolean end() {
		return this.getCleanPoints().size() >= this.getNbPointsToClean();
	}
}
