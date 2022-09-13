package com.dydu.hoover.utils;
import java.io.FileNotFoundException;
import java.io.InputStream;
import com.dydu.hoover.exceptions.FileCanNotBeReadException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Naive way to read a matrix from file.<br/>
 * <br/>
 * Files are in the classpath.
 * 
 * @author mat
 */
public final class MatrixFileReader {

    private static final Logger LOG = LoggerFactory
            .getLogger(MatrixFileReader.class);

    public String[][] readFile(String fileName) throws FileCanNotBeReadException, FileNotFoundException  {
    	
        String[][] matrix = null;
        InputStream resource = MatrixFileReader.class.getResourceAsStream(fileName);
        if(resource == null) {
        	throw new FileNotFoundException("this file is not found : " + fileName);
        }
        try {
	        String txt = IOUtils.toString(resource);
	        
	        if (txt != null) {
	            String[] lines = txt.split("\n");
	            int maxLength = getMaxLength(lines);
	
	            matrix = fillMatrix(lines, maxLength);
	        }
	        return matrix;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new FileCanNotBeReadException("the file can not be read : " + fileName);
        }
    }

    /**
     * Fill the matrix
     * 
     * @param lines
     * @param maxLength
     * @return matrix
     */
    private String[][] fillMatrix(String[] lines, int maxLength) {
        String[][] matrix = new String[lines.length][maxLength];
        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < maxLength; j++) {
                if (j < lines[i].length()) {
                    matrix[i][j] = String.valueOf(lines[i].charAt(j));
                } else {
                    matrix[i][j] = " ";
                }
            }
        }

        return matrix;
    }

    private int getMaxLength(String[] lines) {
        int maxLength = 0;
        for (int i = 0; i < lines.length; i++) {
            maxLength = Math.max(maxLength, lines[i].length());
        }

        return maxLength;
    }

}
