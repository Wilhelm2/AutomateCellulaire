import java.util.ArrayList;

// Game history. Saves the game board of each step of the game. 
public class History {
    ArrayList<Board> gameHistory = new ArrayList<Board>(); // Array that keeps the game board states. Index i
                                                           // corresponds to the game board state at step i
    int currIndex = 0; // Current index. Can be different from maxIndex when going forward and backward
                       // in the game
    int maxIndex = 0; // Maximal index, ie the maximal game attained game step

    public History(Board initialBoard) {
        gameHistory.add(initialBoard);
    }

    // Adds an event to the game board
    public void addEvent(Board gameBoard) {
        if (currIndex != maxIndex)
            gameHistory.subList(0, currIndex); // clears the more recent history
        gameHistory.add(Board.deepCopy(gameBoard));
        currIndex++;
        maxIndex = currIndex;
    }

    // goes one back. Returns initial board if currIndex == 0
    public Board goOneBackward() {
        if (isInInitialState())
            return Board.deepCopy(gameHistory.get(0));
        else {
            currIndex--;
            return Board.deepCopy(gameHistory.get(currIndex));
        }
    }

    public boolean isInInitialState() {
        return currIndex == 0;
    }

    // goes one forward. Returns max board if currIndex == maxIndex
    public Board goOneForward() {
        if (isMaxState())
            return Board.deepCopy(gameHistory.get(maxIndex));
        else {
            currIndex++;
            return Board.deepCopy(gameHistory.get(currIndex));
        }
    }

    public boolean isMaxState() {
        return currIndex == maxIndex;
    }

    // Detects when the board does not change between two turns.
    public boolean detectEnd() {
        if (isInInitialState())
            return false;
        return gameHistory.get(currIndex).isEqual(gameHistory.get(currIndex - 1));
    }

    // Detects a cycle of 1 in the game, ie if the board state is equal to the board
    // state 2 steps back.
    public boolean detectCycle() {
        if (currIndex < 2)
            return false;
        return gameHistory.get(currIndex).isEqual(gameHistory.get(currIndex - 2));
    }

}
