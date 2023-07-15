import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

// Class that draws the gameboard
public class DrawBoard extends JPanel {
    Automate gameEngine;

    public DrawBoard(Automate gameEngine) {
        this.gameEngine = gameEngine;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(gameEngine.gameBoard.columns * 20, gameEngine.gameBoard.rows * 20);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setPaint(Color.WHITE);

        int i, j;
        for (i = 1; i < gameEngine.gameBoard.rows; i++)
            g2d.drawLine(0, 20 * i, gameEngine.gameBoard.columns * 20, 20 * i);
        for (i = 1; i < gameEngine.gameBoard.columns; i++)
            g2d.drawLine(20 * i, 0, 20 * i, gameEngine.gameBoard.rows * 20);

        for (i = 0; i < gameEngine.gameBoard.rows; i++) {
            for (j = 0; j < gameEngine.gameBoard.columns; j++) {
                if (gameEngine.gameBoard.board.get(i).get(j).intValue() == 1)
                    g.setColor(Color.BLUE);
                else
                    g.setColor(Color.WHITE);
                g.fillOval(j * 20, i * 20, 20, 20);
            }
        }
        g2d.dispose();
    }
}
