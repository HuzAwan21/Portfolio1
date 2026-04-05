package ttfe.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import ttfe.SimulatorInterface;
import ttfe.TTFEFactory;

public class AddPieceTests {

    private SimulatorInterface game;

	@Before
	public void setUp() {
		game = TTFEFactory.createSimulator(4, 4, new Random(0));
	}
    

    @Test
    public void addPieceTest1() {
        int piecesBefore = 0;
        int boardValueBefore = 0;

        for (int i = 0; i < game.getBoardHeight(); i++) {
            for (int j = 0; j < game.getBoardWidth(); j++) {
                boardValueBefore += game.getPieceAt(j, i);
                if (game.getPieceAt(j, i) != 0) {
                    piecesBefore++;
                }
            }
        }

        game.addPiece();

        int piecesAfter = 0;
        int boardValueAfter = 0;

        for (int i = 0; i < game.getBoardHeight(); i++) {
            for (int j = 0; j < game.getBoardWidth(); j++) {
                boardValueAfter += game.getPieceAt(j, i);
                if (game.getPieceAt(j, i) != 0) {
                    piecesAfter++;
                }
            }
        }

        int addedValue = boardValueAfter - boardValueBefore;
        assertTrue("#addPiece: Fail --- added piece value is not 2 or 4", addedValue == 2 || addedValue == 4);

        assertEquals("#addPiece: Fail --- didn't add exactly one piece", piecesBefore + 1, piecesAfter);
    }

    @Test
	public void addPieceTest2() {

    int piecesBefore=0; 

        for (int i = 0; i < game.getBoardHeight(); i++) {
            for (int j = 0; j < game.getBoardWidth(); j++) {
                if (game.getPieceAt(j, i) != 0) {
                    piecesBefore++;
                }
            }
        }

	 for (int i=piecesBefore; i<(game.getBoardHeight()*game.getBoardWidth()); i++){
     game.addPiece();
	 assertEquals("#addPiece(): Fail --- doesn't Add",i+1,game.getNumPieces());
	 }
  
	 try {
		game.addPiece();
		fail("#addPiece(): Fail --- Expected IllegalStateException was not thrown when adding a piece to a full board");
	 } catch (IllegalStateException e) {}
    }

    
}
