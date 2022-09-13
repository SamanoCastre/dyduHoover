package com.dydu.hoover.tools;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.dydu.hoover.exceptions.FileCanNotBeReadException;
import com.dydu.hoover.utils.MatrixFileReader;

/**
 * Test MatrixFileReader methods.
 * 
 * @author mat
 */
public final class MatrixFileReaderTest {

    @Test
    public void readFile() throws FileCanNotBeReadException, IOException {

    	 MatrixFileReader matrixFileReader = new MatrixFileReader();

        String[][] maze = matrixFileReader.readFile("/maze1.txt");

        Assert.assertNotNull(maze);

        Assert.assertEquals(7, maze[0].length);
        Assert.assertEquals("M", maze[0][0]);
        Assert.assertEquals(" ", maze[2][6]);
    }

    @Test(expected=FileNotFoundException.class)
    public void readFileWithError() throws FileNotFoundException, FileCanNotBeReadException {
    	 MatrixFileReader matrixFileReader = new MatrixFileReader();
         matrixFileReader.readFile("null");
    }

}
