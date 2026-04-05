package ttfe;

import java.util.Random;

public class ComputerPlayer implements PlayerInterface {
    private Random random;

    public ComputerPlayer(Random random) {
        this.random = random;
    }

    public MoveDirection getPlayerMove(SimulatorInterface game, UserInterface ui) {

        MoveDirection bestMove=null;
        int maxPoints = -1;
                
        SimulatorInterface gameCpy;
                
        for (MoveDirection direction : MoveDirection.values()) {
            gameCpy = copyGame(game);
            if (gameCpy.isMovePossible(direction)) {
                gameCpy.performMove(direction);
                int points = gameCpy.getPoints();
                if (points > maxPoints) {
                    maxPoints = points;
                    bestMove = direction;
                }
            }
        }        
    
            
        return bestMove;
        }   
    
    private SimulatorInterface copyGame(SimulatorInterface game) {
        CreateSimulator gameCopy = new CreateSimulator(game.getBoardWidth(), game.getBoardHeight(), random);
        for (int i = 0; i < game.getBoardWidth(); i++) {
            for (int j = 0; j < game.getBoardHeight(); j++) {
                gameCopy.setPieceAt(i, j, game.getPieceAt(i, j));
            }
        }
        return gameCopy;
    }

}
