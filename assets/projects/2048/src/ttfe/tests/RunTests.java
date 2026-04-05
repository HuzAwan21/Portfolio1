package ttfe.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import ttfe.MoveDirection;
import ttfe.SimulatorInterface;
import ttfe.TTFEFactory;
import ttfe.UserInterface;
import ttfe.PlayerInterface;

public class RunTests {

    private SimulatorInterface game;
    private UserInterface ui;
    private PlayerInterface player;

    @Before
    public void setUp() {
        game = TTFEFactory.createSimulator(4, 4, new Random(0));
        ui = TTFEFactory.createUserInterface(game);
        player = TTFEFactory.createPlayer(false);
    }

    @Test
    public void Run_InitialBoard(){

        game.run(player,ui);
  
        assertTrue("#run: Fail --- run didn't add points at all or negative points",game.getPoints()>0);
        assertTrue("#run: Fail --- AI game did not end with a game over",/*(max ==2048)||*/!(game.isMovePossible()));


    }

    @Test
    public void Run_LostGame(){
        game.setPieceAt(0, 0, 2);  game.setPieceAt(1, 0, 32);   game.setPieceAt(2, 0, 512);  game.setPieceAt(3, 0, 8); 
        game.setPieceAt(0, 1, 4);  game.setPieceAt(1, 1, 64);   game.setPieceAt(2, 1, 1024);  game.setPieceAt(3, 1, 16); 
        game.setPieceAt(0, 2, 8); game.setPieceAt(1, 2, 128);  game.setPieceAt(2, 2, 2);  game.setPieceAt(3, 2, 32); 
        game.setPieceAt(0, 3, 16);  game.setPieceAt(1, 3, 256);game.setPieceAt(2, 3, 4);game.setPieceAt(3, 3, 64);      
    

        game.run(player,ui);
        assertEquals("#run: Fail --- Impossible board - run added points withot performing any move", 0, game.getPoints());
        assertEquals("#run: Fail --- Impossible board - still did moves?",0,game.getNumMoves());
        assertFalse("#run: Fail --- Impossible board - still space left?",game.isSpaceLeft());
        assertFalse("#run: Fail --- Impossible board - still move(s) possible?",game.isMovePossible());
        assertFalse("#run: Fail --- Impossible board - still move(s) to North possible?",game.isMovePossible(MoveDirection.NORTH));
        assertFalse("#run: Fail --- Impossible board - still move(s) to South possible?",game.isMovePossible(MoveDirection.SOUTH));
        assertFalse("#run: Fail --- Impossible board - still move(s) to East possible?",game.isMovePossible(MoveDirection.EAST));
        assertFalse("#run: Fail --- Impossible board - still move(s) to West possible?",game.isMovePossible(MoveDirection.WEST));

    }

    @Test
    public void Run_OneMoveLeft(){
        game.setPieceAt(0, 0, 128);  game.setPieceAt(1, 0, 32);   game.setPieceAt(2, 0, 8);  game.setPieceAt(3, 0, 8); 
        game.setPieceAt(0, 1, 4);  game.setPieceAt(1, 1, 64);   game.setPieceAt(2, 1, 1024);  game.setPieceAt(3, 1, 64); 
        game.setPieceAt(0, 2, 8); game.setPieceAt(1, 2, 128);  game.setPieceAt(2, 2, 2);  game.setPieceAt(3, 2, 32); 
        game.setPieceAt(0, 3, 16);  game.setPieceAt(1, 3, 256);game.setPieceAt(2, 3, 4);game.setPieceAt(3, 3, 64);      
    
        assertEquals("#getPonts(): Fail --- no moves performed and points != 0",0, game.getPoints());
        assertTrue("#getNumPieces(): Fail --- Full board does not have height*width pieces",game.getNumPieces()==game.getBoardHeight()*game.getBoardWidth());
        assertEquals("#getNumMoves(): Fail --- No moves performed - expected 0",0,game.getNumMoves());
        assertFalse("#isSpaceLeft(): Fail --- No spcae left but true",game.isSpaceLeft());
        assertTrue("#isMovePossible(): Fail --- Move possible but false",game.isMovePossible());
        assertFalse("#isMovePossible(North): Fail --- Move to North not possible but true",game.isMovePossible(MoveDirection.NORTH));
        assertFalse("#isMovePossible(South): Fail --- Move to South not possible but true",game.isMovePossible(MoveDirection.SOUTH));
        assertTrue("#isMovePossible(East): Fail --- Move to East possible but false",game.isMovePossible(MoveDirection.EAST));
        assertTrue("#isMovePossible(West): Fail --- Move to West possible but fasle",game.isMovePossible(MoveDirection.WEST));


        game.run(player,ui);

        assertEquals("#run: Fail --- run didn't add points correctly", 16, game.getPoints());
        assertTrue("#run: Fail --- run didn't spawn new piece",game.getNumPieces()==game.getBoardHeight()*game.getBoardWidth());
        assertEquals("#run: Fail --- OneMoveLeft board - expected 1",1,game.getNumMoves());
        assertFalse("#run: Fail --- OneMoveLeft board - still space left after run AI",game.isSpaceLeft());
        assertFalse("#run: Fail --- OneMoveLeft board - still move(s) possible after run AI?",game.isMovePossible());
        assertFalse("#run: Fail --- OneMoveLeft board - still move(s) to North possible after run AI?",game.isMovePossible(MoveDirection.NORTH));
        assertFalse("#run: Fail --- OneMoveLeft board - still move(s) to South possible after run AI?",game.isMovePossible(MoveDirection.SOUTH));
        assertFalse("#run: Fail --- OneMoveLeft board - still move(s) to East possible after run AI?",game.isMovePossible(MoveDirection.EAST));
        assertFalse("#run: Fail --- OneMoveLeft board - still move(s) to West possible after run AI?",game.isMovePossible(MoveDirection.WEST));

    }

    
    @Test(expected = IllegalArgumentException.class)
    public void Run_Exception1_Test(){
            game.run(player, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void Run_Exception2_Test(){
            game.run(null, ui);
    }

    @Test(expected = IllegalArgumentException.class)
    public void Run_Exception3_Test(){
            game.run(null, null);
    }

}
