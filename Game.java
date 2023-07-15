import java.io.File;
import java.io.IOException;

// Class containing the function that runs the game. 
public class Game {
    static Automaton gameEngine;
    static Display gameScreen;

    public static void main(String[] args)
            throws IOException, InterruptedException {
        if (args.length < 2) {
            System.err.println("usage: java Game <rows> <columns>");
            System.exit(0);
        }

        new File(gameScreen.DirectoryPATH).mkdir(); // creates the directory which contains saved game images

        gameEngine = new Automaton(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        gameScreen = new Display(gameEngine);

        game();
    }

    static public void game()
            throws IOException, InterruptedException {
        while (true) {
            while (!gameScreen.isRunning)
                Thread.sleep(1000);

            try {
                Thread.sleep(1000 / gameScreen.speed);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            while (!gameScreen.isRunning)
                Thread.sleep(100);

            synchronized (gameScreen) {
                gameEngine.nextTurnUpdate();
                gameScreen.updateStepCounter(gameEngine.gameHistory.currIndex);
                gameScreen.db.gameEngine = gameEngine;
                gameScreen.db.repaint();
                gameScreen.frame.repaint();

                if (gameEngine.gameHistory.detectEnd()) {
                    System.out.println("FIN");
                    gameScreen.isRunning = false;
                }
                if (gameEngine.gameHistory.detectCycle()) {
                    System.out.println("Cycle détecté !");
                    gameScreen.isRunning = false;
                }
            }
        }
    }
}
