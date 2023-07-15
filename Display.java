import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

public class Display extends JPanel {
    static String DirectoryPATH = "./Image"; // Path to the directory to which will store saved images
    JToolBar toolbar = new JToolBar(); // contains the screen elements except the board
    JLabel stepCounter; // Counts the number of steps since the initial position
    DrawBoard db; // Class that draws the game board in order to print it on the screen
    JFormattedTextField speedField; // Field that contains the game speed
                   int speed = 1; // Initial speed is 1 update every second. speed = 10 means 10 updates per
    // second
    boolean isRunning = false; // true: the board is evolving every turn, false: the board does not evolve

    final JFrame frame = new JFrame();

    Display(Automaton gameEngine)
            throws IOException, InterruptedException {
        setInitialFrame(gameEngine);
    }

    // Sets the initial screen with an empty board
    public void setInitialFrame(Automaton gameEngine)
            throws IOException, InterruptedException {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        toolbar.add(createSimpleJlabel(" Step: "));
        stepCounter = createSimpleJlabel(" " + String.valueOf(gameEngine.gameHistory.currIndex) + "  ");
        toolbar.add(stepCounter);

        toolbar.add(createStepBackButton(gameEngine));
        toolbar.add(createStopButton(gameEngine));
        toolbar.add(createStepForwardButton(gameEngine));

        toolbar.add(createSimpleJlabel(" Speed: "));
        speedField = createTextField();
        toolbar.add(speedField);
        toolbar.add(createSpeedTextFieldConfirmButton(speedField));

        toolbar.add(createSaveButton(gameEngine));
        toolbar.add(createResetButton(gameEngine));

        toolbar.add(createStartConditionChoiceBox(gameEngine));
        toolbar.add(createResizeBoardButton(frame, gameEngine));

        frame.add(toolbar, BorderLayout.NORTH);

        db = new DrawBoard(gameEngine);
        frame.add(db);

        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
        frame.setVisible(true);
    }

    public JLabel createSimpleJlabel(String labelName) {
        JLabel label = new JLabel(labelName);
        label.setFont(new Font("Courier", Font.BOLD, 15));
        return label;
    }

    public JButton createStepBackButton(Automaton gameEngine) {
        JButton button = new JButton(new ImageIcon("ScreenPictures/previous.png"));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                synchronized (this) {
                    if (gameEngine.gameHistory.isInInitialState()) {
                        System.out.println("Already in initial state");
                        return;
                    } else {
                        System.out.println("Goes back!");
                        gameEngine.gameBoard = gameEngine.gameHistory.goOneBackward();
                        updateStepCounter(gameEngine.gameHistory.currIndex);
                        frame.repaint();
                    }
                }
            }
        });
        return button;
    }

    public JButton createStopButton(Automaton gameEngine) {
        JButton button = new JButton(new ImageIcon("ScreenPictures/stop.png"));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (!(gameEngine.gameHistory.detectCycle() || gameEngine.gameHistory.detectEnd()
                        || gameEngine.gameBoard.isAllDead()))
                    isRunning = !isRunning;
            }
        });
        return button;
    }

    public JButton createStepForwardButton(Automaton gameEngine) {
        JButton button = new JButton(new ImageIcon("ScreenPictures/forward.png"));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                synchronized (this) {
                    if (gameEngine.gameHistory.isMaxState()) {
                        System.out.println("Is at max state");
                        return;
                    } else {
                        System.out.println("Goes one forward");
                        gameEngine.gameBoard = gameEngine.gameHistory.goOneForward();
                        updateStepCounter(gameEngine.gameHistory.currIndex);
                        frame.repaint();
                    }
                }
            }
        });
        return button;
    }

    public void updateStepCounter(int step) {
        stepCounter.setText(" " + step + "  ");
    }

    public JFormattedTextField createTextField() {
        JFormattedTextField textField = new JFormattedTextField();
        textField.setPreferredSize(new Dimension(40, 30));
        textField.setMaximumSize(new Dimension(30, 30));
        return textField;
    }

    public JButton createSpeedTextFieldConfirmButton(JFormattedTextField field) {
        JButton button = new JButton("OK");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    speed = Integer.parseInt(field.getText()); 
                } catch (NumberFormatException e) {
                    System.out.println("Bad format");
                    return;
                }
                ;
            }
        });
        return button;
    }

    public JButton createSaveButton(Automaton gameEngine) {
        JButton button = new JButton("Save Image");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                saveAutomatonStateToImage(gameEngine, "/Automaton:" + gameEngine.gameBoard.rows + ":"
                        + gameEngine.gameBoard.columns + " E: " + gameEngine.gameHistory.currIndex + ".png");
                saveAutomatonStateToImage(new Automaton(gameEngine.gameHistory.gameHistory.get(0).board),
                        "/INIT:Automaton:" + gameEngine.gameBoard.rows + ":" + gameEngine.gameBoard.columns + " E: "
                                + gameEngine.gameHistory.currIndex + ".png");
            }
        });
        return button;
    }

    public void saveAutomatonStateToImage(Automaton gameEngine, String filePath) {
        DrawBoard dbSave = new DrawBoard(gameEngine);
        dbSave.setSize(new Dimension(gameEngine.gameBoard.columns * 20, gameEngine.gameBoard.rows * 20)); // needs to be
                                                                                                          // called
                                                                                                          // otherwise
                                                                                                          // size = 0
                                                                                                          // and
                                                                                                          // repaint()
                                                                                                          // is not
                                                                                                          // called (for
                                                                                                          // optimization
                                                                                                          // since there
                                                                                                          // would be
                                                                                                          // nothing to
                                                                                                          // print)
        dbSave.revalidate();
        dbSave.repaint();
        dbSave.setVisible(true);

        BufferedImage imageToSave = new BufferedImage(db.getSize().width, db.getSize().height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics graph = imageToSave.createGraphics();
        dbSave.printAll(graph);
        graph.dispose();
        try {
            ImageIO.write(imageToSave, "png", new File(DirectoryPATH + filePath));
        } catch (Exception e) {
        }
    }

    public JButton createResetButton(Automaton gameEngine) {
        JButton button = new JButton("Reset");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                gameEngine.reset();
                updateStepCounter(0);
                frame.repaint();
            }
        });
        return button;
    }

    public JComboBox<String> createStartConditionChoiceBox(Automaton gameEngine) {
        String[] StartConditions = { "Choice", "Random", "Chessboard", "Import File", "Self input" };
        final JComboBox<String> comboxStartConditions = new JComboBox<>(StartConditions);
        comboxStartConditions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String select = comboxStartConditions.getSelectedItem().toString();
                if (select.equals("Choice"))
                    ; // does nothing
                if (select.equals("Random"))
                    gameEngine.RandomSetting(100);
                else if (select.equals("Chessboard"))
                    gameEngine.ChessBoard();
                else if (select.equals("Import File")) {
                    String fileName = JOptionPane.showInputDialog(frame, "Filename", null);
                    try {
                        gameEngine.reset(Parser.ParseFile(fileName));
                    } catch (IOException e) {
                        System.out.println("File not found");
                        return;
                    }
                    ;
                } else if (select.equals("Self input")) {
                    Parser.ParseSelfInput(gameEngine, frame);
                }

                if (!select.equals("Choice")) // selected a new game configuration
                {
                    updateStepCounter(0);
                }
                frame.repaint();
            }
        });
        return comboxStartConditions;
    }

    public JButton createResizeBoardConfirmButton(Automaton gameEngine, JTextField inputRows, JTextField inputColumns,
            JFrame mainFrame, JDialog resizeBox) {
        JButton button = new JButton("Confirm");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                gameEngine.reset(
                        new Board(Integer.parseInt(inputRows.getText()), Integer.parseInt(inputColumns.getText())));
                resizeBox.dispose();
                updateStepCounter(gameEngine.gameHistory.currIndex);
                mainFrame.pack();
                mainFrame.repaint();
            }
        });
        return button;
    }

    JButton createResizeBoardButton(JFrame mainFrame, Automaton gameEngine) {
        JButton button = new JButton("Resize Board");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JDialog resizeBox = new JDialog(mainFrame, "Input");
                resizeBox.setLayout(new GridBagLayout());
                resizeBox.setModal(true);
                resizeBox.add(createSimpleJlabel("rows:"));
                JTextField inputRows = createTextField();
                resizeBox.add(inputRows);
                resizeBox.add(createSimpleJlabel("columns:"));
                JTextField inputColumns = createTextField();
                resizeBox.add(inputColumns);
                resizeBox
                        .add(createResizeBoardConfirmButton(gameEngine, inputRows, inputColumns, mainFrame, resizeBox));
                resizeBox.pack();
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                resizeBox.setLocation(dim.width / 2 - resizeBox.getSize().width / 2,
                        dim.height / 2 - resizeBox.getSize().height / 2);
                resizeBox.setVisible(true);
            }
        });
        return button;
    }
}
