package com.dydu.hoover;
import java.io.FileNotFoundException;
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
	
	
	private String outFilename;
	private String filename; //File to read
	private Pathway pathway;
	
	public Hoover() {
		
	}
	
	public void init(String filename) throws NoDataFoundException, FileCanNotBeReadException, FileNotFoundException  {
		
		this.filename = filename;
		MatrixFileReader fReader = new MatrixFileReader();
		this.pathway = new Pathway();
		this.pathway.init(fReader.readFile(this.filename));
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
		while(!this.pathway.end()) {
		    this.pathway.move();
		}
		this.saveResult();
	}
	
	/**
	 * This methods allows ud to save the result in a file
	 */
	public void saveResult() {
		this.outFilename = "out_" + this.filename.split("/")[1];
		CustomFileWriter writer = new CustomFileWriter();
		writer.saveResult(this.pathway.getCleanPoints(), this.outFilename);
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

	public Pathway getPathway() {
		return pathway;
	}
}
