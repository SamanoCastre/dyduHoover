package com.dydu.hoover;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dydu.hoover.exceptions.FileCanNotBeReadException;
import com.dydu.hoover.exceptions.InitializationException;
import com.dydu.hoover.exceptions.NoDataFoundException;
import com.dydu.hoover.exceptions.StartFailException;
import com.dydu.hoover.utils.CustomFileWriter;
import com.dydu.hoover.utils.MatrixFileReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hoover {
	private static final Logger LOG = LoggerFactory
            .getLogger(Hoover.class);
	
	private enum Direction {
		LEFT, RIGHT, DOWN, UP
	};
	private String outFilename;
	private String filename; //File to read
	private List<String> cleanPoints = new ArrayList<String>(); //Map of points already clean
	private int nbPointsToClean = 0;//Number of points to clean
	private String[][] matrix;
	
	public Hoover() {
		
	}
	
	public void init(String filename) throws NoDataFoundException, FileCanNotBeReadException, FileNotFoundException  {
		
		this.filename = filename;
		MatrixFileReader fReader = new MatrixFileReader();
		this.matrix = fReader.readFile(this.filename);
		
	   if(this.matrixIsEmpty()) {
		 throw new NoDataFoundException("No data found in the file");
	   }
	   this.computeNbPointsToClean();
	}
	
	private boolean matrixIsEmpty() {
		return (this.matrix == null || this.matrix.length == 0 || (this.matrix.length==1 && this.matrix[0].length == 0));
	}
	
	/**
	 * This method allows us to start the hoover
	 * @return
	 * @throws StartFailException
	 * @throws InitializationException 
	 */
	public void start() throws StartFailException, InitializationException {
		if(this.filename == null) {
			throw new InitializationException("the hoover not being initialized. please use the method init(String filename) to initialize the hoover"); 
		}
		while(this.cleanPoints.size() < this.nbPointsToClean) {
		    Map<String, Integer> startPoint = this.getNewStartPoint(matrix);
		    this.move(startPoint);
		}
		this.saveResult();
	}
	
	/**
	 * This methods allows ud to save the result in a file
	 */
	public void saveResult() {
		this.outFilename = "out_" + this.filename.split("/")[1];
		CustomFileWriter writer = new CustomFileWriter();
		writer.saveResult(this.cleanPoints, this.outFilename);
	}

	
	/**
	 * This method gives us a new start point everytime the hoover hit can go nowhere
	 * @param matrix
	 * @return
	 */
	public Map<String,Integer> getNewStartPoint(String[][] matrix) {
		
		Map<String,Integer> startPoint = new HashMap<String,Integer>();
		
		int countLine = 0;
		while(startPoint.isEmpty() && countLine < matrix.length) {
			String[] line = matrix[countLine];
			
			int countElement = 0;
			while(countElement < line.length && startPoint.isEmpty()) {
				
				if(!this.isWall(countLine, countElement) && !this.isClean(countLine, countElement) ) {
						startPoint.put("lineIndex", countLine);
						startPoint.put("elementIndex", countElement);
				}
				else {
					countElement++;
				}
			}
			countLine++;
		}
		return startPoint;
	}
	
	/**
	 * This method allows us to move the hoover from a start point to different directions
	 * @param startPoint
	 * @throws StartFailException
	 */
	public void move(Map<String, Integer> startPoint) throws StartFailException {
		
		int lineIndex = startPoint.get("lineIndex");
		int elementIndex = startPoint.get("elementIndex");
		
		if(this.isWall(lineIndex, elementIndex)) {
			throw new StartFailException("Can not start, this is the point of a wall");
		}
		
		if(!this.isClean(lineIndex, elementIndex)) {
			this.addCleanPoint(lineIndex, elementIndex);
		}
		
		Direction direction = this.getDirection(lineIndex, elementIndex);
		
		while(direction != null) {
			
			if(direction.equals(Direction.RIGHT)) {
				
				elementIndex++;
				while(!this.isWall(lineIndex, elementIndex) && !this.isClean(lineIndex, elementIndex)) {
					this.addCleanPoint(lineIndex, elementIndex);
					elementIndex = (elementIndex < this.matrix[lineIndex].length-1) ? elementIndex++ : elementIndex;
				}
			}
			if(direction.equals(Direction.DOWN)) {
				lineIndex++;
				while(!this.isWall(lineIndex, elementIndex) && !this.isClean(lineIndex, elementIndex)) {
					this.addCleanPoint(lineIndex, elementIndex);
					lineIndex = lineIndex < this.matrix.length-1 ? lineIndex++ : lineIndex;
				}
				
			}
			if(direction.equals(Direction.LEFT)) {
				 elementIndex--;
				while(!this.isWall(lineIndex, elementIndex) && !this.isClean(lineIndex, elementIndex)) {
					this.addCleanPoint(lineIndex, elementIndex);
					elementIndex = elementIndex > 0 ? elementIndex-- : elementIndex;
				}
			}
			if(direction.equals(Direction.UP)) {
				 lineIndex--;
				while(!this.isWall(lineIndex, elementIndex) && !this.isClean(lineIndex, elementIndex)) {
					this.addCleanPoint(lineIndex, elementIndex);
					lineIndex = lineIndex > 0 ? lineIndex-- : lineIndex;
				}
			}
			direction =  elementIndex < this.matrix[lineIndex].length-1 ? this.getDirection(lineIndex, elementIndex) : null;
		}
	}
	
	private void addCleanPoint(int lineIndex, int elementIndex) {
		this.cleanPoints.add( lineIndex + "," + elementIndex );
	}
	
   /**
    * This method allows us to know in wich direction our hoover can move easily
    * @param lineIndex
    * @param elementIndex
    * @return
    */
   private Direction getDirection(int lineIndex, int elementIndex) {
	   
		if(!this.isWall(lineIndex, elementIndex + 1) && !this.isClean(lineIndex, elementIndex + 1)) {
			return Direction.RIGHT;
		}
		
		if(elementIndex > 0 && !this.isWall(lineIndex, elementIndex - 1) && !this.isClean(lineIndex, elementIndex - 1)) {
			return Direction.LEFT;
		}
		
		if(!this.isWall(lineIndex + 1, elementIndex) && !this.isClean(lineIndex + 1, elementIndex)) {
			return Direction.DOWN;
		}
		
		if(lineIndex > 0 && !this.isWall(lineIndex - 1, elementIndex) && !this.isClean(lineIndex - 1, elementIndex)) {
			return Direction.UP;
		}
		return null;
   }
	
   /**
    * THis method allows us to check if theses two indices are wall point or not
    * @param lineIndex
    * @param elementIndex
    * @return
    */
	public boolean isWall(int lineIndex, int elementIndex) {
		return this.matrix[lineIndex][elementIndex].toUpperCase().equals("M");
	}
	
	/**
	 * This method allows us to check if these two indices are already clean or not
	 * @param lineIndex
	 * @param elementIndex
	 * @return
	 */
	public boolean isClean(int lineIndex, int elementIndex) {
		return this.cleanPoints.contains(lineIndex + "," + elementIndex);
	}
	
	/**
	 * This method allows us to know how many points we have to clean
	 */
	private void computeNbPointsToClean() {
		for (String[] line  : matrix) {
			for (String element  : line) {
				if(!element.toUpperCase().equals("M")) {
					this.nbPointsToClean ++;
				}
			}
		}
	}

	public List<String> getCleanPoints() {
		return cleanPoints;
	}

	public int getNbPointsToClean() {
		return nbPointsToClean;
	}
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getOutFilename() {
		return outFilename;
	}
}
