import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

// Class that draws the gameboard
public class DrawBoard extends JPanel {
    Automaton gameEngine;

    public DrawBoard(Automaton gameEngine) {
        this.gameEngine = gameEngine;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(gameEngine.gameBoard.columns * 20, gameEngine.gameBoard.rows * 20);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 1; i < gameEngine.gameBoard.rows; i++)
            g.drawLine(0, 20 * i, gameEngine.gameBoard.columns * 20, 20 * i);
        for (int i = 1; i < gameEngine.gameBoard.columns; i++)
            g.drawLine(20 * i, 0, 20 * i, gameEngine.gameBoard.rows * 20);

        for (int i = 0; i < gameEngine.gameBoard.rows; i++) {
            for (int j = 0; j < gameEngine.gameBoard.columns; j++) {
                if (gameEngine.gameBoard.board.get(i).get(j).intValue() == 1)
                    g.setColor(Color.BLUE);
                else
                    g.setColor(Color.WHITE);
                g.fillOval(j * 20, i * 20, 20, 20);
            }
        }
    }
}
