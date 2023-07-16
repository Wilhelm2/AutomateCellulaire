import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

// Class with two functions to set the gameBoard. One reads files and the other displays a gameboard to take the input directly form the user. 
public class Parser {
    /*
     * Reads the board from a file and returns it
     * Format: O: living cell, X: dead cell. Cells are separated by spaces
     * Example:
     * X O X -> line of 3 cells with the middle cell living
     * X X O -> line of 3 cells with the last cell living
     * O O X -> line of 3 cells with the two first cells living
     */
    static Board ParseFile(String FileName)
            throws IOException {
        File F = new File(FileName);
        if (!F.exists()) {
            System.out.println("File not found");
            return new Board(0, 0);
        }

        ArrayList<ArrayList<Integer>> board = new ArrayList<ArrayList<Integer>>();
        try (BufferedReader br = new BufferedReader(new FileReader(F))) {
            String line;
            while ((line = br.readLine()) != null) {
                ArrayList<Integer> currLine = new ArrayList<Integer>();
                String[] numbersString = line.split(" ");
                for (String cell : numbersString)
                    currLine.add(Integer.parseInt(cell));
                board.add(currLine);
            }
        }
        return new Board(board);
    }

    // Prints an input board with JButtons to push to set living cells
    static void ParseSelfInput(Automaton gameEngine, JFrame mainFrame) {
        GridBagConstraints gc = new GridBagConstraints();
        JDialog d = new JDialog(mainFrame, "Input");
        d.setLayout(new GridBagLayout());
        d.setModal(true);
        ArrayList<ArrayList<JButton>> inputBoard = createInputBoard(gameEngine.gameBoard.rows,
                gameEngine.gameBoard.columns, d);

        JButton confirmButton = createConfirmButton(gameEngine, mainFrame, d, inputBoard);
        displayAddedCell(gameEngine.gameBoard.rows, gameEngine.gameBoard.columns, d, confirmButton);

        d.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        d.setLocation(dim.width / 2 - d.getSize().width / 2, dim.height / 2 - d.getSize().height / 2);
        d.setVisible(true);
        return;
    }

    static ArrayList<ArrayList<JButton>> createInputBoard(int rows, int columns, JDialog frame) {
        ArrayList<ArrayList<JButton>> board = new ArrayList<ArrayList<JButton>>();
        for (int i = 0; i < rows; i++) {
            board.add(new ArrayList<JButton>());
            for (int j = 0; j < columns; j++) {
                JButton setupCell = createSetupCell();
                board.get(i).add(setupCell);
                displayAddedCell(i, j, frame, setupCell);
            }
        }
        return board;
    }

    static JButton createSetupCell() {
        JButton setupCell = new JButton("");
        setupCell.setOpaque(true);
        setupCell.setBackground(Color.WHITE);
        setupCell.setPreferredSize(new Dimension(20, 20));
        setupCell.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if(setupCell.getBackground() == Color.BLUE)
                    setupCell.setBackground(Color.WHITE);
                else
                    setupCell.setBackground(Color.BLUE);
            }
        });
        return setupCell;
    }

    static void displayAddedCell(int row, int column, JDialog frame, JButton setupCell) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = column;
        gc.gridy = row;
        frame.add(setupCell, gc);
    }

    static boolean isCellSet(JButton button) {
        return button.getBackground() == Color.BLUE;
    }

    // Creates the confirm button. Upon triggering it will update the gameBoard and
    // repaint the mainframe to print the new board
    static JButton createConfirmButton(Automaton gameEngine, JFrame mainFrame, JDialog d,
            ArrayList<ArrayList<JButton>> inputBoard) {
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                ArrayList<ArrayList<Integer>> resBoard = new ArrayList<ArrayList<Integer>>();
                for (int i = 0; i < inputBoard.size(); i++) {
                    resBoard.add(new ArrayList<Integer>());
                    for (int j = 0; j < inputBoard.get(0).size(); j++) {
                        if (isCellSet(inputBoard.get(i).get(j)))
                            resBoard.get(i).add(Integer.valueOf(1));
                        else
                            resBoard.get(i).add(Integer.valueOf(0));
                    }
                }
                d.dispose();
                gameEngine.reset(new Board(resBoard));
                mainFrame.repaint();
            }
        });
        return confirmButton;
    }
}
