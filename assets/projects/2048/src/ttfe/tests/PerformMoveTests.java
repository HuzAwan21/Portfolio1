package ttfe.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import ttfe.MoveDirection;
import ttfe.SimulatorInterface;
import ttfe.TTFEFactory;

public class PerformMoveTests {

    private SimulatorInterface game;

    @Before
    public void setUp(){
        game = TTFEFactory.createSimulator(4, 4, new Random(0));
    }

    @Test
    public void PerformMove_isPossible_Test_NoMerging(){
        /*(2) (4)   (0) (0)
          (4) (8)   (0) (0)
          (16)(32)  (4) (2)
          (2)(2048)(512)(8)*/

    game.setPieceAt(0, 0, 2);  game.setPieceAt(1, 0, 4);   game.setPieceAt(2, 0, 0);  game.setPieceAt(3, 0, 0); 
    game.setPieceAt(0, 1, 4);  game.setPieceAt(1, 1, 8);   game.setPieceAt(2, 1, 0);  game.setPieceAt(3, 1, 0); 
    game.setPieceAt(0, 2, 16); game.setPieceAt(1, 2, 32);  game.setPieceAt(2, 2, 4);  game.setPieceAt(3, 2, 2); 
    game.setPieceAt(0, 3, 2);  game.setPieceAt(1, 3, 2048);game.setPieceAt(2, 3, 512);game.setPieceAt(3, 3, 8);      
    
    assertTrue("#isMovePossible(): Fail --- Move possible to N/E but false", game.isMovePossible());
    assertTrue("#isSpaceLest(): Fail --- Space left but false",game.isSpaceLeft());
    assertFalse("#performMove(SOUTH): Fail --- Move to South not possible but true",game.performMove(MoveDirection.SOUTH));
    assertFalse("#performMove(WEST): Fail --- Move to West not possible but true",game.performMove(MoveDirection.WEST));
    assertTrue("#performMove(EAST): Fail --- Move to East possible but false",game.performMove(MoveDirection.EAST));
    assertTrue("#isMovePossible(): Fail --- Move possible to N/W but false", game.isMovePossible());
    assertTrue("#performMove(WEST): Fail --- Move to West possible but false",game.performMove(MoveDirection.WEST));
    assertTrue("#isMovePossible(): Fail --- Move possible to N/E but false", game.isMovePossible());
    assertTrue("#performMove(NORTH): Fail --- Move to North possible but false",game.performMove(MoveDirection.NORTH));
    assertTrue("#isMovePossible(): Fail --- Move possible to S/W but false", game.isMovePossible());
    assertTrue("#performMove(SOUTH): Fail --- Move to South possible but false",game.performMove(MoveDirection.SOUTH));
    assertTrue("#isSpaceLest(): Fail --- Space left but false",game.isSpaceLeft());
    }


    @Test
    public void PerformMove_PlayingGame_Test(){
        /*(2) (4)   (4) (4)
          (4) (8)   (0) (2)
          (16)(32)  (4) (2)
          (2)(2048)(512)(8)*/

        /*(2)     (0)      (0)    (0)
          (2048)  (0)      (0)    (0)
          (64)    (16)     (0)    (0)
          (512)   (8)      (2)    (0)*/

    game.setPieceAt(0, 0, 2);  game.setPieceAt(1, 0, 4);   game.setPieceAt(2, 0, 4);  game.setPieceAt(3, 0, 4); 
    game.setPieceAt(0, 1, 4);  game.setPieceAt(1, 1, 8);   game.setPieceAt(2, 1, 0);  game.setPieceAt(3, 1, 2); 
    game.setPieceAt(0, 2, 16); game.setPieceAt(1, 2, 32);  game.setPieceAt(2, 2, 4);  game.setPieceAt(3, 2, 2); 
    game.setPieceAt(0, 3, 2);  game.setPieceAt(1, 3, 2048);game.setPieceAt(2, 3, 512);game.setPieceAt(3, 3, 8);    
    
    int points=0;

    assertTrue("#isSpaceLest(): Fail --- Space left but false",game.isSpaceLeft());

    assertTrue("#isMovePossible(): Fail --- Move possible but false", game.isMovePossible());
    assertTrue("#performMove(SOUTH): Fail --- Move to South possible but false",game.performMove(MoveDirection.SOUTH));
    points+=12;
    assertEquals("#getPoints: Fail --- points not added correctly",points,game.getPoints());

    assertTrue("#isMovePossible(): Fail --- Move possible but false", game.isMovePossible());
    assertTrue("#performMove(SOUTH): Fail --- Move to South possible but false",game.performMove(MoveDirection.SOUTH));
    points+=8;
    assertEquals("#getPoints: Fail --- points not added correctly",points,game.getPoints());
    
    assertTrue("#isMovePossible(): Fail --- Move possible but false", game.isMovePossible());
    assertTrue("#performMove(EAST): Fail --- Move to East possible but false",game.performMove(MoveDirection.EAST));
    points+=16;
    assertEquals("#getPoints: Fail --- points not added correctly",points,game.getPoints());
    
    assertTrue("#isMovePossible(): Fail --- Move possible but false", game.isMovePossible());
    assertFalse("#performMove(EAST): Fail --- Move to East not possible but true",game.performMove(MoveDirection.EAST));
    points+=0;
    assertEquals("#getPoints: Fail --- points not added correctly",points,game.getPoints());

    assertTrue("#isMovePossible(): Fail --- Move possible but false", game.isMovePossible());
    assertTrue("#performMove(NORTH): Fail --- Move to North possible but false",game.performMove(MoveDirection.NORTH));
    points+=0;
    assertEquals("#getPoints: Fail --- points not added correctly",points,game.getPoints());

    assertTrue("#isMovePossible(): Fail --- Move possible but false", game.isMovePossible());
    assertTrue("#performMove(WEST): Fail --- Move to West possible but false",game.performMove(MoveDirection.WEST));
    points+=0;
    assertEquals("#getPoints: Fail --- points not added correctly",points,game.getPoints());
    
    assertTrue("#isMovePossible(): Fail --- Move possible but false", game.isMovePossible());
    assertFalse("#performMove(NORTH): Fail --- Move to North not possible but true",game.performMove(MoveDirection.NORTH));
    points+=0;
    assertEquals("#getPoints: Fail --- points not added correctly",points,game.getPoints());
    
    assertTrue("#isMovePossible(): Fail --- Move possible but false", game.isMovePossible());
    assertTrue("#performMove(SOUTH): Fail --- Move to South possible but false",game.performMove(MoveDirection.SOUTH));
    points+=0;
    assertEquals("#getPoints: Fail --- points not added correctly",points,game.getPoints());
    
    assertTrue("#isMovePossible(): Fail --- Move possible but false", game.isMovePossible());
    assertTrue("#performMove(WEST): Fail --- Move to West possible but false",game.performMove(MoveDirection.WEST));
    points+=16;
    assertEquals("#getPoints: Fail --- points not added correctly",points,game.getPoints());
    
    assertTrue("#isMovePossible(): Fail --- Move possible but false", game.isMovePossible());
    assertTrue("#performMove(NORTH): Fail --- Move to North possible but false",game.performMove(MoveDirection.NORTH));
    points+=32;
    assertEquals("#getPoints: Fail --- points not added correctly",points,game.getPoints());
    
    assertTrue("#isMovePossible(): Fail --- Move possible but false", game.isMovePossible());
    assertTrue("#performMove(WEST): Fail --- Move to West possible but false",game.performMove(MoveDirection.WEST));
    points+=72;
    assertEquals("#getPoints: Fail --- points not added correctly",points,game.getPoints());
    
    assertTrue("#isMovePossible(): Fail --- Move possible but false", game.isMovePossible());
    assertTrue("#performMove(SOUTH): Fail --- Move to South possible but false",game.performMove(MoveDirection.SOUTH));
    points+=0;
    assertEquals("#getPoints: Fail --- points not added correctly",points,game.getPoints());
    
    assertTrue("#isMovePossible(): Fail --- Move possible but false", game.isMovePossible());
    assertFalse("#performMove(SOUTH): Fail --- Move to South not possible but true",game.performMove(MoveDirection.SOUTH));
    points+=0;
    assertEquals("#getPoints: Fail --- points not added correctly",points,game.getPoints());
     
    assertTrue("#isSpaceLest(): Fail --- Space left but false",game.isSpaceLeft());

    try {
		game.performMove(null);
		fail("#performMove(null): Fail --- Expected IllegalArgumentException was not thrown when pasing null as argument\n");
	  } catch (IllegalArgumentException e) {}
    
    }

}