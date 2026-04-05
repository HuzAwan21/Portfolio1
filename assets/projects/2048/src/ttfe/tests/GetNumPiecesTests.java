package ttfe.tests;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import ttfe.SimulatorInterface;
import ttfe.TTFEFactory;


public class GetNumPiecesTests {
    
  private SimulatorInterface game;

  @Before
  public void setUp() {
    game = TTFEFactory.createSimulator(4, 4, new Random(0));
  }  

  @Test
  public void getNumPieces_InitialBoard_Test(){
  assertEquals("#getNumPieces(): Fail --- Initial number pieces != 2", 2, game.getNumPieces());
  }
  
  @Test
  public void getNumPiecesTest1(){

  int accNumPieces= 0;

  for (int i=0; i<game.getBoardHeight(); i++){
    for(int j=0; j<game.getBoardWidth(); j++){
        if(game.getPieceAt(i,j)!=0){
            accNumPieces++;
        }
    }
  }
   
  assertEquals("#getNumPieces(): Fail --- Incorrect number",accNumPieces, game.getNumPieces());
  }

  @Test 
  public void getNumPieces_Board1_Test(){

  game.setPieceAt(0, 0, 2);game.setPieceAt(1, 0, 4);game.setPieceAt(2, 0, 4);game.setPieceAt(3, 0, 4); 
  game.setPieceAt(0, 1, 4);game.setPieceAt(1, 1, 8);game.setPieceAt(2, 1, 0);game.setPieceAt(3, 1, 2); 
  game.setPieceAt(0, 2, 16);game.setPieceAt(1, 2, 32);game.setPieceAt(2, 2, 4);game.setPieceAt(3, 2, 2); 
  game.setPieceAt(0, 3, 2);game.setPieceAt(1, 3, 2048);game.setPieceAt(2, 3, 512);game.setPieceAt(3, 3, 8);    
      

  assertEquals("#getNumPieces(): Fail --- Wrong number of Pieces",15,game.getNumPieces());

  }

  @Test 
  public void getNumPieces_Board2_Test(){

  game.setPieceAt(0, 0, 0);game.setPieceAt(1, 0, 0);game.setPieceAt(2, 0, 0);game.setPieceAt(3, 0, 0); 
  game.setPieceAt(0, 1, 0);game.setPieceAt(1, 1, 0);game.setPieceAt(2, 1, 0);game.setPieceAt(3, 1, 0); 
  game.setPieceAt(0, 2, 0);game.setPieceAt(1, 2, 0);game.setPieceAt(2, 2, 0);game.setPieceAt(3, 2, 0); 
  game.setPieceAt(0, 3, 0);game.setPieceAt(1, 3, 0);game.setPieceAt(2, 3, 0);game.setPieceAt(3, 3, 0);    
      

  assertEquals("#getNumPieces(): Fail --- Wrong number of Pieces",0,game.getNumPieces());

  }

  @Test 
  public void getNumPieces_Board3_Test(){

  game.setPieceAt(0, 0, 0);game.setPieceAt(1, 0, 0);game.setPieceAt(2, 0, 0);game.setPieceAt(3, 0, 0); 
  game.setPieceAt(0, 1, 0);game.setPieceAt(1, 1, 0);game.setPieceAt(2, 1, 0);game.setPieceAt(3, 1, 0); 
  game.setPieceAt(0, 2, 0);game.setPieceAt(1, 2, 0);game.setPieceAt(2, 2, 2048);game.setPieceAt(3, 2, 0); 
  game.setPieceAt(0, 3, 0);game.setPieceAt(1, 3, 0);game.setPieceAt(2, 3, 0);game.setPieceAt(3, 3, 0);    
      

  assertEquals("#getNumPieces(): Fail --- Wrong number of Pieces", 1,game.getNumPieces());

  }

  @Test 
  public void getNumPieces_Board4_Test(){

  game.setPieceAt(0, 0, 4);game.setPieceAt(1, 0, 0);game.setPieceAt(2, 0, 0);game.setPieceAt(3, 0, 0); 
  game.setPieceAt(0, 1, 0);game.setPieceAt(1, 1, 0);game.setPieceAt(2, 1, 0);game.setPieceAt(3, 1, 0); 
  game.setPieceAt(0, 2, 0);game.setPieceAt(1, 2, 0);game.setPieceAt(2, 2, 2048);game.setPieceAt(3, 2, 0); 
  game.setPieceAt(0, 3, 0);game.setPieceAt(1, 3, 0);game.setPieceAt(2, 3, 0);game.setPieceAt(3, 3, 2);    
      

  assertEquals("#getNumPieces(): Fail --- Wrong number of Pieces", 3,game.getNumPieces());

  }

  @Test 
  public void getNumPieces_Board5_Test(){

  game.setPieceAt(0, 0, 0);game.setPieceAt(1, 0, 128);game.setPieceAt(2, 0, 0);game.setPieceAt(3, 0, 32); 
  game.setPieceAt(0, 1, 0);game.setPieceAt(1, 1, 0);game.setPieceAt(2, 1, 0);game.setPieceAt(3, 1, 0); 
  game.setPieceAt(0, 2, 0);game.setPieceAt(1, 2, 64);game.setPieceAt(2, 2, 2048);game.setPieceAt(3, 2, 0); 
  game.setPieceAt(0, 3, 64);game.setPieceAt(1, 3, 64);game.setPieceAt(2, 3, 0);game.setPieceAt(3, 3, 0);    
      

  assertEquals("#getNumPieces(): Fail --- Wrong number of Pieces", 6,game.getNumPieces());

  }

  @Test
  public void getNumPieces_Loop_Test(){
   
   for(int i= 0; i<game.getBoardHeight(); i++){
    for(int j=0; j<game.getBoardWidth(); j++){
      game.setPieceAt(i, j, 0);
    }
   } 

   int accNumPieces=0;

    while(game.isSpaceLeft()==true){
      game.addPiece();
      accNumPieces++;
      assertEquals("#getNumPieces(): Fail --- Incorrect number after addPiece()",accNumPieces, game.getNumPieces());
    }
  }

}
