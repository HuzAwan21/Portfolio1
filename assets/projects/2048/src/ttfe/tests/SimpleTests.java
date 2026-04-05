package ttfe.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import ttfe.SimulatorInterface;
import ttfe.TTFEFactory;

/**
 * This class provides a very simple example of how to write tests for this project.
 * You can implement your own tests within this class or any other class within this package.
 * Tests in other packages will not be run and considered for completion of the project.
 */
public class SimpleTests {

	private SimulatorInterface game;

	@Before
	public void setUp() {
		game = TTFEFactory.createSimulator(4, 4, new Random(0));
	}
	
	@Test
	public void testInitialGamePoints() {
		assertEquals("Fail --- The initial game did not have zero points", 0,
				game.getPoints());
	}
	
	@Test
	public void testInitialBoardHeight() {
		assertTrue("Fail --- The initial game board did not have correct height",
				4 == game.getBoardHeight());
	}

	@Test
	public void testInitialBoardWidth() {
		assertTrue("Fail --- The initial game board did not have correct width",
				4 == game.getBoardWidth());
	}

	@Test
	public void testInitialBoardState() {
    
	assertNotNull("Fail --- Game instance should not be null", game);

		int initialNumPieces = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (game.getPieceAt(i, j) != 0) {
					initialNumPieces++;
				}
			}
		}
		assertEquals("Fail --- The initial board did not have two pieces", 2, initialNumPieces);
	}
 

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullRandom1() {
    TTFEFactory.createSimulator(4, 4, null);
    }

	@Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullRandom2() {
    TTFEFactory.createSimulator(3, 5, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithWrongWidth1() {
    TTFEFactory.createSimulator(1, 4, new Random());
    }

	@Test(expected = IllegalArgumentException.class)
    public void testConstructorWithWrongWidth0() {
    TTFEFactory.createSimulator(0, 4, new Random());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithWrongHeight1() {
    TTFEFactory.createSimulator(4, 1, new Random());
    }

	@Test(expected = IllegalArgumentException.class)
    public void testConstructorWithWrongHeight0() {
    TTFEFactory.createSimulator(4, 0, new Random());
    }

	@Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidWidthHeight() {
    TTFEFactory.createSimulator(1, 1, new Random());
    }

    @Test
    public void testConstructorWithMinimumValidBoardSize() {

    SimulatorInterface minSizeGame = TTFEFactory.createSimulator(2, 2, new Random(0));
    assertNotNull("Fail --- Game instance should not be null", minSizeGame);

    int pieces = 0;
        for (int i = 0; i < minSizeGame.getBoardWidth(); i++) {
            for (int j = 0; j < minSizeGame.getBoardHeight(); j++) {
                if (minSizeGame.getPieceAt(i, j) != 0) {
                    pieces++;
                }
            }
        }

    assertEquals("Fail --- There should be exactly two tiles on the board initially", 2, pieces);
    }
	

}