package ttfe.tests;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import ttfe.MoveDirection;
import ttfe.SimulatorInterface;
import ttfe.TTFEFactory;

public class GetNumMovesTests {
   
    private SimulatorInterface game;

	@Before
	public void setUp() {
		game = TTFEFactory.createSimulator(4, 4, new Random(0));
	}  

    @Test
    public void TestInitialgetNumMoves() {
     assertEquals("#getNumMoves(): Fail --- Initial moves != 0", 0,  game.getNumMoves());
    }

    @Test
    public void getNumMoves_2xAllDirections_Test(){
     game.performMove(MoveDirection.EAST);
     assertEquals("#getNumMoves(): Fail --- Move to East not recognised", 1, game.getNumMoves());
     game.performMove(MoveDirection.WEST);
     assertEquals("#getNumMoves(): Fail --- Move to West not recognised", 2, game.getNumMoves());
     game.performMove(MoveDirection.NORTH);
     assertEquals("#getNumMoves(): Fail --- Move to North not recognised", 3, game.getNumMoves());
     game.performMove(MoveDirection.SOUTH);
     assertEquals("#getNumMoves(): Fail --- Move to South not recognised", 4, game.getNumMoves());

     game.performMove(MoveDirection.EAST);
     assertEquals("#getNumMoves(): Fail --- Move to East not recognised", 5, game.getNumMoves());
     game.performMove(MoveDirection.WEST);
     assertEquals("#getNumMoves(): Fail --- Move to West not recognised", 6, game.getNumMoves());
     game.performMove(MoveDirection.NORTH);
     assertEquals("#getNumMoves(): Fail --- Move to North not recognised", 7, game.getNumMoves());
     game.performMove(MoveDirection.SOUTH);
     assertEquals("#getNumMoves(): Fail --- Move to South not recognised", 8, game.getNumMoves());

    }



}
