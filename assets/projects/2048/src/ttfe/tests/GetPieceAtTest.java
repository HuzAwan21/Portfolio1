package ttfe.tests;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import ttfe.SimulatorInterface;
import ttfe.TTFEFactory;

public class GetPieceAtTest {

    private SimulatorInterface game;

	@Before
	public void setUp() {
		game = TTFEFactory.createSimulator(4, 4, new Random(0));
	}

    @Test 
    public void getPieceAt_Board1_Test(){

    game.setPieceAt(0, 0, 2);game.setPieceAt(1, 0, 4);game.setPieceAt(2, 0, 4);game.setPieceAt(3, 0, 4); 
    game.setPieceAt(0, 1, 4);game.setPieceAt(1, 1, 8);game.setPieceAt(2, 1, 0);game.setPieceAt(3, 1, 2); 
    game.setPieceAt(0, 2, 16);game.setPieceAt(1, 2, 32);game.setPieceAt(2, 2, 4);game.setPieceAt(3, 2, 2); 
    game.setPieceAt(0, 3, 2);game.setPieceAt(1, 3, 2048);game.setPieceAt(2, 3, 512);game.setPieceAt(3, 3, 8);    
        

    assertEquals("#getPieceAt(): Fail --- Wrong number", 2,game.getPieceAt(0,0));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 4,game.getPieceAt(0,1));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 16,game.getPieceAt(0,2));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 2,game.getPieceAt(0,3));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 4,game.getPieceAt(1,0));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 8,game.getPieceAt(1,1));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 32,game.getPieceAt(1,2));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 2048,game.getPieceAt(1,3));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 4,game.getPieceAt(2,0));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(2,1));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 4,game.getPieceAt(2,2));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 512,game.getPieceAt(2,3));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 4,game.getPieceAt(3,0));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 2,game.getPieceAt(3,1));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 2,game.getPieceAt(3,2));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 8,game.getPieceAt(3,3));
    
    }

    @Test 
    public void getPieceAt_Board2_Test(){

    game.setPieceAt(0, 0, 0);game.setPieceAt(1, 0, 0);game.setPieceAt(2, 0, 0);game.setPieceAt(3, 0, 0); 
    game.setPieceAt(0, 1, 0);game.setPieceAt(1, 1, 0);game.setPieceAt(2, 1, 0);game.setPieceAt(3, 1, 0); 
    game.setPieceAt(0, 2, 0);game.setPieceAt(1, 2, 0);game.setPieceAt(2, 2, 0);game.setPieceAt(3, 2, 0); 
    game.setPieceAt(0, 3, 0);game.setPieceAt(1, 3, 0);game.setPieceAt(2, 3, 0);game.setPieceAt(3, 3, 0);    
        
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(0,0));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(0,1));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(0,2));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(0,3));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(1,0));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(1,1));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(1,2));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(1,3));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(2,0));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(2,1));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(2,2));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(2,3));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(3,0));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(3,1));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(3,2));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(3,3));

    }

    @Test 
    public void getPieceAt_Board3_Test(){

    game.setPieceAt(0, 0, 0);game.setPieceAt(1, 0, 0);game.setPieceAt(2, 0, 0);game.setPieceAt(3, 0, 0); 
    game.setPieceAt(0, 1, 0);game.setPieceAt(1, 1, 0);game.setPieceAt(2, 1, 0);game.setPieceAt(3, 1, 0); 
    game.setPieceAt(0, 2, 0);game.setPieceAt(1, 2, 0);game.setPieceAt(2, 2, 2048);game.setPieceAt(3, 2, 0); 
    game.setPieceAt(0, 3, 0);game.setPieceAt(1, 3, 0);game.setPieceAt(2, 3, 0);game.setPieceAt(3, 3, 0);    
        
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(0,0));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(0,1));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(0,2));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(0,3));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(1,0));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(1,1));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(1,2));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(1,3));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(2,0));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(2,1));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 2048,game.getPieceAt(2,2));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(2,3));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(3,0));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(3,1));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(3,2));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(3,3));

    }

    @Test 
    public void getPieceAt_Board4_Test(){

    game.setPieceAt(0, 0, 4);game.setPieceAt(1, 0, 0);game.setPieceAt(2, 0, 0);game.setPieceAt(3, 0, 0); 
    game.setPieceAt(0, 1, 0);game.setPieceAt(1, 1, 0);game.setPieceAt(2, 1, 0);game.setPieceAt(3, 1, 0); 
    game.setPieceAt(0, 2, 0);game.setPieceAt(1, 2, 0);game.setPieceAt(2, 2, 2048);game.setPieceAt(3, 2, 0); 
    game.setPieceAt(0, 3, 0);game.setPieceAt(1, 3, 0);game.setPieceAt(2, 3, 0);game.setPieceAt(3, 3, 2);    
        
    assertEquals("#getPieceAt(): Fail --- Wrong number", 4,game.getPieceAt(0,0));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(0,1));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(0,2));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(0,3));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(1,0));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(1,1));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(1,2));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(1,3));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(2,0));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(2,1));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 2048,game.getPieceAt(2,2));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(2,3));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(3,0));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(3,1));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(3,2));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 2,game.getPieceAt(3,3));

    }

    @Test 
    public void getPieceAt_Board5_Test(){

    game.setPieceAt(0, 0, 0);game.setPieceAt(1, 0, 128);game.setPieceAt(2, 0, 0);game.setPieceAt(3, 0, 32); 
    game.setPieceAt(0, 1, 0);game.setPieceAt(1, 1, 0);game.setPieceAt(2, 1, 0);game.setPieceAt(3, 1, 0); 
    game.setPieceAt(0, 2, 0);game.setPieceAt(1, 2, 64);game.setPieceAt(2, 2, 2048);game.setPieceAt(3, 2, 0); 
    game.setPieceAt(0, 3, 64);game.setPieceAt(1, 3, 64);game.setPieceAt(2, 3, 0);game.setPieceAt(3, 3, 0);    
        
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(0,0));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(0,1));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(0,2));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 64,game.getPieceAt(0,3));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 128,game.getPieceAt(1,0));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(1,1));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 64,game.getPieceAt(1,2));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 64,game.getPieceAt(1,3));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(2,0));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(2,1));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 2048,game.getPieceAt(2,2));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(2,3));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 32,game.getPieceAt(3,0));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(3,1));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(3,2));
    assertEquals("#getPieceAt(): Fail --- Wrong number", 0,game.getPieceAt(3,3));

    }
    
}
