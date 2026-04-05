package ttfe;

import java.util.Random;

public class CreateSimulator implements SimulatorInterface {

    private int score;
    private int numMoves;
    private int numPieces;
    private int height;
    private int width;
    private Random random;
    private int[][] board;

    public CreateSimulator(int width, int height, Random random) {
        if (height < 2 || width < 2 || random==null) {
            throw new IllegalArgumentException("Width and height must be at least 2");
        }

        this.board = new int[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                this.board[i][j] = 0;
            }
        }

        this.height = height;
        this.width = width;
        this.random = random;
        this.score = 0;
        this.numMoves = 0;
        this.numPieces = 0;

        addPiece();
        addPiece();
    }

@Override
public void addPiece() {
    if (!isSpaceLeft()) {
        throw new IllegalStateException("The board is already full");
    }
    
    int x, y;
    do {
        x = this.random.nextInt(width);
        y = this.random.nextInt(height);
    } while (board[x][y] != 0);

    int piece = (this.random.nextInt(10) < 9) ? 2 : 4;

    this.board[x][y] = piece;
}

@Override
public int getPieceAt(int x, int y) {
    if (x<0||y<0||x>=this.width||y>=this.height) {
        throw new IllegalArgumentException("Coordinates are not valid");
    } 
    return this.board[x][y];
}

@Override
public boolean isMovePossible() {
    if (isSpaceLeft()) {
        return true;
    }
    
    for (int i = 0; i < this.width; i++) {
        for (int j = 0; j < this.height; j++) {
            int currentPiece = this.board[i][j];
            
            if (currentPiece != 0) {
                if (i > 0 && this.board[i - 1][j] == currentPiece) {
                    return true;
                }
                if (i < this.width - 1 && this.board[i + 1][j] == currentPiece) {
                    return true;
                }
                if (j > 0 && this.board[i][j - 1] == currentPiece) {
                    return true;
                }
                if (j < this.height - 1 && this.board[i][j + 1] == currentPiece) {
                    return true;
                }
            }
        }
    }
    
    return false;
}

@Override
public boolean isMovePossible(MoveDirection direction) {
   if(direction==null){
    throw new IllegalArgumentException("direction can not be null");
   }
    
   switch (direction) {
       case NORTH:
           for (int i = 0; i < this.width; i++) {
               for (int j = 1; j < this.height; j++) {
                       if (board[i][j] != 0 && (board[i][j-1] == 0 || board[i][j-1] == board[i][j])) {
                           return true;
                   }
               }
           }
           break;
       case SOUTH:
            for (int i = 0; i < this.width; i++) {
                for (int j = 0; j < this.height-1; j++) {
                        if (board[i][j] != 0 && (board[i][j+1] == 0 || board[i][j+1] == board[i][j])) {
                            return true;
                   }
               }
           }
           break;
       case WEST:
            for (int i = 1; i < this.width; i++) {
                for (int j = 0; j < this.height; j++) {
                        if (board[i][j] != 0 && (board[i-1][j] == 0 || board[i-1][j] == board[i][j])) {
                            return true;
                   }
               }
           }
           break;
       case EAST:
            for (int i = 0; i < this.width-1; i++) {
                for (int j = 0; j < this.height; j++) {
                        if (board[i][j] != 0 && (board[i+1][j] == 0 || board[i+1][j] == board[i][j])) {
                           return true;
                   }
               }
           }
           break;
   }

    return false;
}

@Override
public boolean isSpaceLeft() {
    for (int i = 0; i < this.width; i++) {
        for (int j = 0; j < this.height; j++) {
            if (this.board[i][j] == 0) {
                return true;
            }
        }
    }
    return false;
}

@Override
public boolean performMove(MoveDirection direction) {
    if (direction == null) {
        throw new IllegalArgumentException("Direction can not be null");
    }

    boolean moveMade = false;

    switch (direction) {
        case NORTH:
            moveMade = moveNorth(moveMade);
            moveMade = mergeNorth(moveMade);
            moveNorth(moveMade);
            if (moveMade){
            this.numMoves++; 
            }   
        break;

        case SOUTH:
            moveMade=moveSouth(moveMade);
            moveMade=mergeSouth(moveMade);
            moveSouth(moveMade);
            if (moveMade){
            this.numMoves++;
            }    
        break;   

        case EAST:
            moveMade=moveEast(moveMade);
            moveMade=mergeEast(moveMade);
            moveEast(moveMade);
            if (moveMade){
            this.numMoves++;  
            }  
            break;   

        case WEST:
            moveMade=moveWest(moveMade);
            moveMade=mergeWest(moveMade);
            moveWest(moveMade);
            if (moveMade){
            this.numMoves++;
            }    
        break;
    }
    return moveMade;
}

private boolean moveNorth(boolean moveMade){
    for (int i=0; i<this.width; i++) {
        for (int j=0; j<this.height; j++) {
            if (this.board[i][j] == 0) {
                for (int k = j + 1; k < this.height; k++) {
                    if (this.board[i][k] != 0) {
                        this.board[i][j] = this.board[i][k];
                        this.board[i][k] = 0;
                        moveMade=true;
                        break;
                    }
                }
            }
        }
    }
    return moveMade;
}

private boolean mergeNorth(boolean moveMade){
   for(int i=0; i<this.width; i++){
        for(int j=0; j<this.height-1; j++){
            if (this.board[i][j+1]!=0){
                if (this.board[i][j]==this.board[i][j+1]){
                    this.score+=this.board[i][j]*2;
                    this.board[i][j]+=this.board[i][j];
                    this.board[i][j+1]=0;
                    moveMade=true;
                    break;
                }
            }
        }
   }
   return moveMade;
}

private boolean moveSouth(boolean moveMade){
    for (int i=0; i<this.width; i++) {
        for (int j=this.height-1; j>=0; j--) {
            if (this.board[i][j] == 0) {
                for (int k = j - 1; k >= 0; k--) {
                    if (this.board[i][k] != 0) {
                        this.board[i][j] = this.board[i][k];
                        this.board[i][k] = 0;
                        moveMade=true;
                        break;
                    }
                }
            }
        }
    }
    return moveMade;
}

private boolean mergeSouth(boolean moveMade){
    for (int i=0; i<this.width; i++) {
        for (int j=this.height-1; j>0; j--) {
            if (this.board[i][j-1]!=0){
                if (this.board[i][j]==this.board[i][j-1]){
                    this.score+=this.board[i][j]*2;
                    this.board[i][j]+=this.board[i][j];
                    this.board[i][j-1]=0;
                    moveMade=true;
                    break;
                }
            }  
        }    
    }  
    return moveMade;  
}

private boolean moveEast(boolean moveMade){
    for (int j=0; j<this.height; j++) {
        for (int i=this.width-1; i>=0; i--) {
            if (this.board[i][j] == 0) {
                for (int k = i - 1; k >= 0; k--) {
                    if (this.board[k][j] != 0) {
                        this.board[i][j] = this.board[k][j];
                        this.board[k][j] = 0;
                        moveMade=true;
                        break;
                    }
                }
            }
        }
    }
    return moveMade;
}

private boolean mergeEast(boolean moveMade){
    for (int j=0; j<this.height; j++) {
        for (int i=this.width-1; i>0; i--) {
            if (this.board[i-1][j]!=0){
                if (this.board[i][j]==this.board[i-1][j]){
                    this.score+=this.board[i][j]*2;
                    this.board[i][j]+=this.board[i][j];
                    this.board[i-1][j]=0;
                    moveMade=true;
                    break;
                }
            }  
        }    
    }  
    return moveMade;  
}

private boolean moveWest(boolean moveMade){
    for (int j=0; j<this.height; j++) {
        for (int i=0; i<this.width; i++) {
            if (this.board[i][j] == 0) {
                for (int k = i + 1; k < this.width; k++) {
                    if (this.board[k][j] != 0) {
                        this.board[i][j] = this.board[k][j];
                        this.board[k][j] = 0;
                        moveMade=true;
                        break;
                    }
                }
            }
        }
    }
    return moveMade;
}

private boolean mergeWest(boolean moveMade){
    for(int j=0; j<this.height; j++){
         for(int i=0; i<this.width-1; i++){
             if (this.board[i+1][j]!=0){
                 if (this.board[i][j]==this.board[i+1][j]){
                     this.score+=this.board[i][j]*2;
                     this.board[i][j]+=this.board[i][j];
                     this.board[i+1][j]=0;
                     moveMade=true;
                     break;
                 }
             }
         }
    }
    return moveMade;
 }

@Override
public void setPieceAt(int x, int y, int piece) {
    if (x<0||y<0||x>=this.width||y>=this.height || piece < 0) {
        throw new IllegalArgumentException("Coordinates are not valid or piece value is negative.");
    } else{
    this.board[x][y] = piece;}
}

@Override
public int getBoardHeight() {
    return this.height;
}

@Override
public int getBoardWidth() {
    return this.width;
}

@Override
public int getPoints() {
    return this.score;
}

@Override
public int getNumMoves() {
    return this.numMoves;
}

@Override
public int getNumPieces() {
    this.numPieces=0;
    for (int i=0; i<this.width; i++){
        for (int j=0; j<this.height; j++){
            if (this.board[i][j]!=0){
                this.numPieces++;
            }
        }
    }
    return this.numPieces;
}

@Override
public void run(PlayerInterface player, UserInterface ui) {
    if (player == null || ui == null) {
        throw new IllegalArgumentException("Player or UI can not be null");
    }

    ui.updateScreen(this);


    while (isMovePossible()) {
        MoveDirection move = player.getPlayerMove(this, ui);
        
        boolean movePerformed = performMove(move);
        
        if (movePerformed) {
            addPiece();
            //if(player instanceof HumanPlayer){
            ui.updateScreen(this);
            //}
        }
    }
    if(player instanceof ComputerPlayer){
        ui.updateScreen(this);
    }
    ui.showGameOverScreen(this);
}


}
