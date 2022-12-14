package com.dydu.hoover.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import com.dydu.hoover.Hoover;
import com.dydu.hoover.Point;
import com.dydu.hoover.exceptions.FileCanNotBeReadException;
import com.dydu.hoover.exceptions.InitializationException;
import com.dydu.hoover.exceptions.NoDataFoundException;
import com.dydu.hoover.exceptions.StartFailException;

import org.junit.Before;
import org.junit.Test;

/**
 * Test Hoover methods
 * 
 * @author : Samano CASTRE
 */
public class HooverTest {
	private static final String List = null;
	public Hoover hoover;
	
	@Before
	public void init() throws StartFailException, URISyntaxException {
		this.hoover = new Hoover();
	}
	
	@Test
	public void filenameShouldMatchTo() throws Exception {
		this.hoover.init("/maze1.txt");
		assertEquals("/maze1.txt", this.hoover.getFilename());
	}
	
	@Test
	public void outFilenameShouldMatchTo() throws Exception {
		this.hoover.init("/maze1.txt");
		this.hoover.start();
		assertEquals("out_maze1.txt", this.hoover.getOutFilename());
	}
	
	@Test
	public void numberOfPointsToCleanShouldBe() throws Exception {
		this.hoover.init("/maze1.txt");
		assertEquals(10, this.hoover.getPathway().getNbPointsToClean());
	}
	
	@Test
	public void cleanPointsShouldMatch() throws Exception {
		this.hoover.init("/maze1.txt");
		this.hoover.start();
		Point[]points = {
				new Point(1,0),
				new Point(1,1),
				new Point(2,1),
				new Point(2,2),
				new Point(2,3),
				new Point(1,3),
				new Point(1,4),
				new Point(1,5),
				new Point(2,5),
				new Point(2,6)
			};
		List<Point>listePoints = Arrays.asList(points);
		for(int i = 0; i < hoover.getPathway().getCleanPoints().size(); i++) {
			assertEquals(listePoints.get(i).getX(), hoover.getPathway().getCleanPoints().get(i).getX());
			assertEquals(listePoints.get(i).getY(), hoover.getPathway().getCleanPoints().get(i).getY());
		}
	}
	
	@Test
	public void outFileShouldExist() throws Exception {
		this.hoover.init("/maze1.txt");
		this.hoover.start();
		File file = new File(this.hoover.getOutFilename());
		assertTrue(file.exists());
	}
	
	@Test(expected=InitializationException.class)
	public void shouldThrowInitializationException() throws StartFailException, InitializationException {
	   this.hoover.start();
	}
	
	@Test(expected=NoDataFoundException.class)
	public void shouldThrowNoDataFondException() throws NoDataFoundException, FileCanNotBeReadException, IOException  {
	   this.hoover.init("/emptyfile.txt");
	}
	
	@Test(expected=NullPointerException.class)
	public void shouldThrowFileNotFoundException() throws NullPointerException, NoDataFoundException, FileCanNotBeReadException, FileNotFoundException  {
	   this.hoover.init(null);
	}
}
