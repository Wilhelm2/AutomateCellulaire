
import java.util.ArrayList;
import java.util.Random;

// Class representing the game engine. It is responsible for the game board and game history and handles the operations performed on them. 
public class Automaton {
    Board gameBoard; // Game board updated at each turn
    History gameHistory; // Game history to which the game board is added at each turn

    public Automaton(int row, int column) {
        gameBoard = new Board(row, column);
        gameHistory = new History(gameBoard);
    }

    public Automaton(ArrayList<ArrayList<Integer>> board) {
        gameBoard = new Board(board);
        gameHistory = new History(gameBoard);
    }

    // Updates the game board to the next turn and saves it to the game history
    public void nextTurnUpdate() {
        Board newBoard = new Board(gameBoard.rows, gameBoard.columns); // create new board for next turn
        for (int i = 0; i < gameBoard.rows; i++) {
            for (int j = 0; j < gameBoard.columns; j++) {
                if (gameBoard.isLiving(i, j))
                    newBoard.setEntry(i, j, 1);
                else
                    newBoard.setEntry(i, j, 0);
            }
        }
        gameBoard = newBoard;
        gameHistory.addEvent(gameBoard);
    }

    // Initializes the game board to a chessboard
    public void ChessBoard() {
        if (gameBoard.isEmpty()) // does nothing if there is no game baord (ie rows=columns=0)
            return;

        reset();
        for (int i = 0; i < gameBoard.rows; i++) {
            for (int j = 0; j < gameBoard.columns; j++) {
                if ((i + j) % 2 == 0)
                    gameBoard.setEntry(i, j, 1);
                else
                    gameBoard.setEntry(i, j, 0);
            }
        }
    }

    // Initializes the game board to a board with nbInitialLivingCells living cells
    public void RandomSetting(int nbInitialLivingCells) {
        int randRow, randColumn;
        Random generator = new Random(System.currentTimeMillis());
        if (gameBoard.isEmpty())
            return;

        reset();
        for (int i = 0; i < nbInitialLivingCells; i++) {
            randRow = (int) (generator.nextDouble() * (gameBoard.rows));
            randColumn = (int) (generator.nextDouble() * (gameBoard.columns));
            gameBoard.setEntry(randRow, randColumn, Integer.valueOf(1));
        }
    }

    // Resets the game board to an empty board. It also resets the game history
    public void reset() {
        gameBoard.initializeEmptyBoard();
        gameHistory = new History(gameBoard);
    }

    // Resets the game board to the game board given as parameter. It also resets
    // the game history
    public void reset(Board board) {
        gameBoard = board;
        gameHistory = new History(gameBoard);
    }
}
