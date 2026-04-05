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

public class IsMovePossibleTests {

    private SimulatorInterface game;

    @Before
    public void setUp(){
        game = TTFEFactory.createSimulator(4, 4, new Random(0));
    }

    @Test
    public void Exception_Test() { 

        try { //Test Exception
            game.isMovePossible(null);
            fail("#isMovePossible(): Fail --- Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void isMovePossible_GameOver_Test(){
        //Fill board - no match in any direction
        int piece=1;
        for(int i=0; i<game.getBoardHeight(); i++){
            for(int j=0; j<game.getBoardWidth();j++){
                game.setPieceAt(i, j, piece);
                piece++;
            }
        }
        assertFalse("#isMovePossible(): Fail --- No space left but true",game.isMovePossible());;
        assertEquals("#getNumPieces(): Fail --- Full board", (game.getBoardHeight()*game.getBoardWidth()), game.getNumPieces());
        assertFalse("#isMovePossible(EAST): Fail --- No space left but true", game.isMovePossible(MoveDirection.EAST));
        assertFalse("#isMovePossible(WEST): Fail --- No space left but true", game.isMovePossible(MoveDirection.WEST));
        assertFalse("#isMovePossible(NORTH): Fail --- No space left but true", game.isMovePossible(MoveDirection.NORTH));
        assertFalse("#isMovePossible(SOUTH): Fail --- No space left but true", game.isMovePossible(MoveDirection.SOUTH));

    }

    @Test
    public void isMovePossible_MergingInEveryDirection_Test(){
    //Fill board - possible merges
    for (int i = 0; i < game.getBoardHeight(); i++) {
        for (int j = 0; j < game.getBoardWidth(); j++) {
                game.setPieceAt(i, j, 2);
        }
    }

    assertTrue("#isMovePossible(): Fail --- Move possible but false", game.isMovePossible());
    assertTrue("#isMovePossible(EAST): Fail --- Move to East possible but false", game.isMovePossible(MoveDirection.EAST));
    assertTrue("#isMovePossible(WEST): Fail --- Move to West possible but false", game.isMovePossible(MoveDirection.WEST));
    assertTrue("#isMovePossible(NORTH): Fail --- Move to North possible but false", game.isMovePossible(MoveDirection.NORTH));
    assertTrue("#isMovePossible(SOUTH): Fail --- Move to South possible but false", game.isMovePossible(MoveDirection.SOUTH));
    }


    @Test
    public void isMovePossible_PossibleButNorth_Test() {
        /*(4) (8) (16) (32)
          (0) (2) (0)  (0)
          (0) (0) (0)  (0)
          (0) (0) (0)  (0)*/
        for (int i = 0; i < game.getBoardHeight(); i++) {
            for (int j = 0; j < game.getBoardWidth(); j++) {
                game.setPieceAt(i, j, 0);
            }
        }
        int piece= 2;
        for (int j = 0; j < game.getBoardWidth(); j++) {
            game.setPieceAt(j, 0, 2*piece);
            piece+=piece;
        }
        game.setPieceAt(1,1,2);

        assertTrue("#isMovePossible(): Fail --- Move possible except North but false", game.isMovePossible());
        assertTrue("#isMovePossible(EAST): Fail --- Move to East possible but false", game.isMovePossible(MoveDirection.EAST));
        assertTrue("#isMovePossible(WEST): Fail --- Move to West possible but false", game.isMovePossible(MoveDirection.WEST));
        assertFalse("#isMovePossible(NORTH): Fail --- Move to North not possible but true", game.isMovePossible(MoveDirection.NORTH));
        assertTrue("#isMovePossible(SOUTH): Fail --- Move to South possible but false", game.isMovePossible(MoveDirection.SOUTH));
    } 

    @Test
    public void isMovePossible_PossibleOnlyNorth_Test() {

        game.setPieceAt(0, 0, 0);game.setPieceAt(1, 0, 0);game.setPieceAt(2, 0, 0);game.setPieceAt(3, 0, 0); 
        game.setPieceAt(0, 1, 0);game.setPieceAt(1, 1, 0);game.setPieceAt(2, 1, 0);game.setPieceAt(3, 1, 0); 
        game.setPieceAt(0, 2, 0);game.setPieceAt(1, 2, 0);game.setPieceAt(2, 2, 0);game.setPieceAt(3, 2, 0); 
        game.setPieceAt(0, 3, 2);game.setPieceAt(1, 3, 4);game.setPieceAt(2, 3, 8);game.setPieceAt(3, 3, 16);    
      

        assertTrue("#isMovePossible(): Fail --- Move possible to North but false", game.isMovePossible());
        assertFalse("#isMovePossible(EAST): Fail --- Move to East not possible but true", game.isMovePossible(MoveDirection.EAST));
        assertFalse("#isMovePossible(WEST): Fail --- Move to West not possible but true", game.isMovePossible(MoveDirection.WEST));
        assertTrue("#isMovePossible(NORTH): Fail --- Move to North possible but false", game.isMovePossible(MoveDirection.NORTH));
        assertFalse("#isMovePossible(SOUTH): Fail --- Move to South not possible but true", game.isMovePossible(MoveDirection.SOUTH));
    } 

    @Test
    public void isMovePossible_PossibleButSouth_Test() {
        /*(0) (0) (0)  (0)
          (0) (0) (0)  (0)
          (0) (2) (0)  (0)
          (4) (8) (16) (32)*/
        for (int i = 0; i < game.getBoardHeight(); i++) {
            for (int j = 0; j < game.getBoardWidth(); j++) {
                game.setPieceAt(i, j, 0);
            }
        }
        int piece= 2;
        for (int j = 0; j < game.getBoardWidth(); j++) {
            game.setPieceAt(j, game.getBoardHeight()-1, 2*piece);
            piece+=piece;
        }
        game.setPieceAt(1,game.getBoardHeight()-2,2);

        assertTrue("#isMovePossible(): Fail --- Move possible except North but false", game.isMovePossible());
        assertTrue("#isMovePossible(EAST): Fail --- Move to East possible but false", game.isMovePossible(MoveDirection.EAST));
        assertTrue("#isMovePossible(WEST): Fail --- Move to West possible but false", game.isMovePossible(MoveDirection.WEST));
        assertTrue("#isMovePossible(NORTH): Fail --- Move to North possible but false", game.isMovePossible(MoveDirection.NORTH));
        assertFalse("#isMovePossible(SOUTH): Fail --- Move to South not possible but true", game.isMovePossible(MoveDirection.SOUTH));
    } 

    @Test
    public void isMovePossible_PossibleOnlySouth_Test() {

        game.setPieceAt(0, 0, 2);game.setPieceAt(1, 0, 4);game.setPieceAt(2, 0, 8);game.setPieceAt(3, 0, 16); 
        game.setPieceAt(0, 1, 0);game.setPieceAt(1, 1, 0);game.setPieceAt(2, 1, 0);game.setPieceAt(3, 1, 0); 
        game.setPieceAt(0, 2, 0);game.setPieceAt(1, 2, 0);game.setPieceAt(2, 2, 0);game.setPieceAt(3, 2, 0); 
        game.setPieceAt(0, 3, 0);game.setPieceAt(1, 3, 0);game.setPieceAt(2, 3, 0);game.setPieceAt(3, 3, 0);    
      

        assertTrue("#isMovePossible(): Fail --- Move possible to South but false", game.isMovePossible());
        assertFalse("#isMovePossible(EAST): Fail --- Move to East not possible but true", game.isMovePossible(MoveDirection.EAST));
        assertFalse("#isMovePossible(WEST): Fail --- Move to West not possible but true", game.isMovePossible(MoveDirection.WEST));
        assertFalse("#isMovePossible(NORTH): Fail --- Move to North not possible but true", game.isMovePossible(MoveDirection.NORTH));
        assertTrue("#isMovePossible(SOUTH): Fail --- Move to South possible but false", game.isMovePossible(MoveDirection.SOUTH));
    } 

    @Test
    public void isMovePossible_PossibleButEast_Test() {
        /*(0) (0) (0) (4)
          (0) (0) (2) (8)
          (0) (0) (0) (16)
          (0) (0) (0) (32)*/
        for (int i = 0; i < game.getBoardHeight(); i++) {
            for (int j = 0; j < game.getBoardWidth(); j++) {
                game.setPieceAt(i, j, 0);
            }
        }
        int piece= 2;
        for (int j = 0; j < game.getBoardHeight(); j++) {
            game.setPieceAt(game.getBoardWidth()-1, j, 2*piece);
            piece+=piece;
        }
        game.setPieceAt(game.getBoardWidth()-2,1,2);

        assertTrue("#isMovePossible(): Fail --- Move possible except North but false", game.isMovePossible());
        assertFalse("#isMovePossible(EAST): Fail --- Move to East not possible but true", game.isMovePossible(MoveDirection.EAST));
        assertTrue("#isMovePossible(WEST): Fail --- Move to West possible but false", game.isMovePossible(MoveDirection.WEST));
        assertTrue("#isMovePossible(NORTH): Fail --- Move to North possible but false", game.isMovePossible(MoveDirection.NORTH));
        assertTrue("#isMovePossible(SOUTH): Fail --- Move to South possible but false", game.isMovePossible(MoveDirection.SOUTH));
    }

    @Test
    public void isMovePossible_PossibleOnlyEast_Test() {

        game.setPieceAt(0, 0, 2);game.setPieceAt(1, 0, 0);game.setPieceAt(2, 0, 0);game.setPieceAt(3, 0, 0); 
        game.setPieceAt(0, 1, 4);game.setPieceAt(1, 1, 0);game.setPieceAt(2, 1, 0);game.setPieceAt(3, 1, 0); 
        game.setPieceAt(0, 2, 8);game.setPieceAt(1, 2, 0);game.setPieceAt(2, 2, 0);game.setPieceAt(3, 2, 0); 
        game.setPieceAt(0, 3, 16);game.setPieceAt(1, 3, 0);game.setPieceAt(2, 3, 0);game.setPieceAt(3, 3, 0);    
      

        assertTrue("#isMovePossible(): Fail --- Move possible to East but false", game.isMovePossible());
        assertTrue("#isMovePossible(EAST): Fail --- Move to East possible but false", game.isMovePossible(MoveDirection.EAST));
        assertFalse("#isMovePossible(WEST): Fail --- Move to West not possible but true", game.isMovePossible(MoveDirection.WEST));
        assertFalse("#isMovePossible(NORTH): Fail --- Move to North not possible but true", game.isMovePossible(MoveDirection.NORTH));
        assertFalse("#isMovePossible(SOUTH): Fail --- Move to South not possible but true", game.isMovePossible(MoveDirection.SOUTH));
    } 

    
    @Test
    public void isMovePossible_PossibleButWest_Test() {
        /*(4)  (0) (0) (0)
          (8)  (2) (0) (0)
          (16) (0) (0) (0)
          (32) (0) (0) (0)*/
        for (int i = 0; i < game.getBoardHeight(); i++) {
            for (int j = 0; j < game.getBoardWidth(); j++) {
                game.setPieceAt(i, j, 0);
            }
        }
        int piece= 2;
        for (int j = 0; j < game.getBoardHeight(); j++) {
            game.setPieceAt(0, j, 2*piece);
            piece+=piece;
        }
        game.setPieceAt(1,1,2);

        assertTrue("#isMovePossible(): Fail --- Move possible except North but false", game.isMovePossible());
        assertTrue("#isMovePossible(EAST): Fail --- Move to East possible but false", game.isMovePossible(MoveDirection.EAST));
        assertFalse("#isMovePossible(WEST): Fail --- Move to West not  possible but true", game.isMovePossible(MoveDirection.WEST));
        assertTrue("#isMovePossible(NORTH): Fail --- Move to North possible but false", game.isMovePossible(MoveDirection.NORTH));
        assertTrue("#isMovePossible(SOUTH): Fail --- Move to South possible but false", game.isMovePossible(MoveDirection.SOUTH));
    }

    @Test
    public void isMovePossible_PossibleOnlyWest_Test() {

        game.setPieceAt(0, 0, 0);game.setPieceAt(1, 0, 0);game.setPieceAt(2, 0, 0);game.setPieceAt(3, 0, 2); 
        game.setPieceAt(0, 1, 0);game.setPieceAt(1, 1, 0);game.setPieceAt(2, 1, 0);game.setPieceAt(3, 1, 4); 
        game.setPieceAt(0, 2, 0);game.setPieceAt(1, 2, 0);game.setPieceAt(2, 2, 0);game.setPieceAt(3, 2, 8); 
        game.setPieceAt(0, 3, 0);game.setPieceAt(1, 3, 0);game.setPieceAt(2, 3, 0);game.setPieceAt(3, 3, 16);    
      

        assertTrue("#isMovePossible(): Fail --- Move possible to West but false", game.isMovePossible());
        assertFalse("#isMovePossible(EAST): Fail --- Move to East not possible but true", game.isMovePossible(MoveDirection.EAST));
        assertTrue("#isMovePossible(WEST): Fail --- Move to West possible but false", game.isMovePossible(MoveDirection.WEST));
        assertFalse("#isMovePossible(NORTH): Fail --- Move to North not possible but true", game.isMovePossible(MoveDirection.NORTH));
        assertFalse("#isMovePossible(SOUTH): Fail --- Move to South not possible but true", game.isMovePossible(MoveDirection.SOUTH));
    }

    @Test
    public void WinGame(){
    game.setPieceAt(0, 0, 2);game.setPieceAt(1, 0, 4);game.setPieceAt(2, 0, 4);game.setPieceAt(3, 0, 4); 
    game.setPieceAt(0, 1, 4);game.setPieceAt(1, 1, 8);game.setPieceAt(2, 1, 0);game.setPieceAt(3, 1, 2); 
    game.setPieceAt(0, 2, 16);game.setPieceAt(1, 2, 32);game.setPieceAt(2, 2, 4);game.setPieceAt(3, 2, 2); 
    game.setPieceAt(0, 3, 2);game.setPieceAt(1, 3, 2048);game.setPieceAt(2, 3, 512);game.setPieceAt(3, 3, 8);    
    
    assertTrue("#isMovepossible(): Fail --- 2048 on board but true",game.isMovePossible());
    assertTrue("#isMovePossible(EAST): Fail --- 2048 on board but true", game.isMovePossible(MoveDirection.EAST));
    assertTrue("#isMovePossible(WEST): Fail --- 2048 on board but true", game.isMovePossible(MoveDirection.WEST));
    assertTrue("#isMovePossible(NORTH): Fail --- 2048 on board but true", game.isMovePossible(MoveDirection.NORTH));
    assertTrue("#isMovePossible(SOUTH): Fail --- 2048 on board but true", game.isMovePossible(MoveDirection.SOUTH));


    }

    @Test(expected = IllegalArgumentException.class)
    public void isMovePossible_NULL_Test() {
    game.isMovePossible(null);
    }
        
}    
