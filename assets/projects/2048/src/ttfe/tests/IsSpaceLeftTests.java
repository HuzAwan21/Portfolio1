package ttfe.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import ttfe.SimulatorInterface;
import ttfe.TTFEFactory;


public class IsSpaceLeftTests {

  private SimulatorInterface game;

  @Before
  public void setUp() {
    game = TTFEFactory.createSimulator(4, 4, new Random(0));
  }  

  @Test
  public void IsSpaceLeft_InitialBoard_Test(){
    int free_space =0;

    for (int i=0; i<game.getBoardHeight(); i++){
        for (int j=0; j<game.getBoardWidth(); j++){
            if (game.getPieceAt(j,i)==0){
             free_space ++;
            }
        }
    }

    if (free_space!=0){
        assertTrue("#isSpaceLest(): Fail --- Space left but false",game.isSpaceLeft());
    }else {
        assertFalse("#isSpaceLest(): Fail --- Space full but true",game.isSpaceLeft());
    }


  }

  @Test
  public void IsSpaceLeft_FullBoard_Test(){
    int free_space =0;
    int piece= 2;

    for (int i=0; i<game.getBoardHeight(); i++){
        for (int j=0; j<game.getBoardWidth(); j++){
            game.setPieceAt(i, j, piece);
            piece+=piece;
        }
    }

    for (int i=0; i<game.getBoardHeight(); i++){
      for (int j=0; j<game.getBoardWidth(); j++){
          if (game.getPieceAt(j,i)==0){
           free_space ++;
          }
      }
    }

    if (free_space!=0){
        assertTrue("#isSpaceLest(): Fail --- Space left but false",game.isSpaceLeft());
    }else {
        assertFalse("#isSpaceLest(): Fail --- Space full but true",game.isSpaceLeft());
    }


  }

    
}
