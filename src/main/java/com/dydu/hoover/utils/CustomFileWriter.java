package com.dydu.hoover.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class alows us to create a new file and write the result int it
 * @author Samano CASTRE
 */
public class CustomFileWriter {
	private static final Logger LOG = LoggerFactory
            .getLogger(CustomFileWriter.class);
	
	/**
	 * This method allows us to save the result for the hoover cleaning in a out file
	 * @param contents
	 * @param filename
	 */
	public void saveResult(List<String>contents, String filename) {
		FileWriter writer = null;
		BufferedWriter bufferWriter = null;
		try {
			writer = new FileWriter( filename);
	       bufferWriter = new BufferedWriter(writer);
	      for(String line : contents) {
	    	  bufferWriter.append("[" + line + "]");
	    	  bufferWriter.append("\n");
	      }
	  }
	  catch(IOException e) {
		  LOG.error(e.getMessage(), e);
	  }
	  finally {
		  if(bufferWriter != null)
			try {
				bufferWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		  if(writer != null)
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	  }
	}
}
