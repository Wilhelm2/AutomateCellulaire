import java.util.ArrayList;

// Class representing the Game Board. 
public class Board {
    // Makes a deep copy of the board given as parameter.
    static Board deepCopy(Board boardToCopy) {
        Board copy = new Board(boardToCopy.rows, boardToCopy.columns);
        for (int i = 0; i < copy.rows; i++) {
            for (int j = 0; j < copy.columns; j++) {
                copy.board.get(i).set(j, boardToCopy.board.get(i).get(j));
            }
        }
        return copy;
    }

    ArrayList<ArrayList<Integer>> board = new ArrayList<ArrayList<Integer>>(); // Array used to store the cell values

    int rows, columns;

    // Initializes the board to the board given as parameter
    public Board(ArrayList<ArrayList<Integer>> board) {
        this.board = board;
        this.rows = board.size();
        this.columns = board.get(0).size();
    }

    // Initializes empty board of size rows*columns
    Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        initializeEmptyBoard();
    }

    // Initializes the board to an empty board. The board keeps the previous size
    public void initializeEmptyBoard() {
        board.clear();
        for (int i = 0; i < rows; i++) {
            board.add(new ArrayList<Integer>());
            for (int j = 0; j < columns; j++)
                board.get(i).add(Integer.valueOf(0));
        }
    }

    // Sets a board entry
    public void setEntry(int row, int column, int value) {
        board.get(row).set(column, Integer.valueOf(value));
    }

    /*
     * Checks if a cell will live at the next turn
     * 3 living neighbors => born
     * If living and surrounded by 2 or 3 living cells then stays alive
     * Otherwise dies
     */
    public boolean isLiving(int row, int column) {
        int nbAliveNeighbors = countLivingNeighbors(row, column);
        boolean isAlive = (board.get(row).get(column).intValue() == 1);

        if (nbAliveNeighbors == 3 && !isAlive)
            return true;
        else if (isAlive && (nbAliveNeighbors == 2 || nbAliveNeighbors == 3))
            return true;
        else
            return false;
    }

    // Counts the number of living neighbors
    public int countLivingNeighbors(int row, int column) {
        int nbLivingNeighbors = 0;
        int minRow = (row == 0 ? 0 : -1);
        int maxRow = (row == (this.rows - 1) ? 0 : 1);
        int minColumn = (column == 0 ? 0 : -1);
        int maxColumn = (column == (this.columns - 1) ? 0 : 1);
        for (int i = minRow; i <= maxRow; i++) {
            for (int j = minColumn; j <= maxColumn; j++) {
                if (i == 0 && j == 0)
                    continue; // cell to test
                else if (board.get(row + i).get(column + j) == 1)
                    nbLivingNeighbors++;
            }
        }
        return nbLivingNeighbors;
    }

    // Tests if all cells are dead
    public boolean isAllDead() {
        for (ArrayList<Integer> row : board) {
            for (Integer i : row) {
                if (i != 0)
                    return false;
            }
        }
        return true;
    }

    // Checks if the board is empty, ie rows=columns=0
    public boolean isEmpty() {
        return columns == 0 && rows == 0;
    }

    // Tests if the board given as parameter is equal to the current board
    boolean isEqual(Board boardToCompare) {
        if (boardToCompare.rows != rows || boardToCompare.columns != columns)
            return false;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (board.get(i).get(j) != boardToCompare.board.get(i).get(j))
                    return false;
            }
        }
        return true;
    }
}
