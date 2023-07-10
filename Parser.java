import java.io.File ;
import java.io.FileReader ;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList ;
import java.awt.Dimension ;
import java.awt.Toolkit;
import java.util.concurrent.TimeUnit;
import java.io.IOException ;
import java.io.BufferedReader ;
import java.awt.Color;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton ;
import javax.swing.JLabel;


public class Parser
{
    static  ArrayList< ArrayList<Integer>> ParseFile ( String FileName) 
        throws IOException
    {
        File F = new File (FileName);
        if (! F.exists() )
        {
            System.out.println("Fichier non trouvé"); 
            return null;
        }
        
        ArrayList< ArrayList<Integer>> board = new ArrayList< ArrayList<Integer>>();
        try(BufferedReader br = new BufferedReader(new FileReader(F)))
        {
            String line;
            while((line = br.readLine()) != null)
            {
                ArrayList<Integer> currLine = new ArrayList<Integer>();
                String[] numbersString = line.split(" ");
                for(String cell : numbersString)
                    currLine.add(Integer.parseInt(cell));                    
                board.add(currLine);
            }
        }        
        return board;
    }

    static JButton createSetupCell()
    {
        JButton setupCell = new JButton("" );
        setupCell.setOpaque(true);
        setupCell.setBackground(Color.WHITE);
        setupCell.setPreferredSize(new Dimension(20, 20));
        setupCell.addActionListener(new ActionListener(){
        public void actionPerformed( ActionEvent ae )
            {
                setupCell.setBackground(Color.BLUE);
            }
        });
        return setupCell;
    }

    static void displayAddedCell(int row, int column, JFrame frame, JButton setupCell)
    {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = column;
        gc.gridy = row;
        frame.add(setupCell, gc);
    }

    static boolean isCellSet(JButton button)
    {
        return button.getBackground() == Color.BLUE;
    }
    
    static void ParseSelfInput (int row, int column, final Affichage2 A, final JFrame mainFrame, final Historique History, final JLabel Etape) 
    {
        GridBagConstraints gc = new GridBagConstraints();
        final JFrame frame = new JFrame();
        
        frame.setLayout(new GridBagLayout());
        final ArrayList< ArrayList <JButton>>  board= new ArrayList< ArrayList <JButton>> ();
        
        for(int i=0 ; i < row ; i++)
        {
            board.add( new ArrayList<JButton>() ) ;
            for(int j=0 ; j< column ; j++)
            {
                final JButton setupCell = createSetupCell();
                board.get(i).add(setupCell);
                displayAddedCell(i, j, frame, setupCell);
            }
            
        }
        JButton Ltmp = new JButton( "OK");
        Ltmp.addActionListener(new ActionListener(){
            public void actionPerformed( ActionEvent ae )
            {
                ArrayList< ArrayList<Integer>> resBoard = new ArrayList< ArrayList<Integer>>();
                for(int i= 0; i<row; i++)
                {
                    resBoard.add( new ArrayList<Integer>());
                    for(int j=0; j<column; j++)
                    {
                        if(isCellSet(board.get(i).get(j)))
                            resBoard.get(i).add(Integer.valueOf(1));
                        else
                            resBoard.get(i).add(Integer.valueOf(0));
                    }
                }
                frame.dispose();
                A.ReglageAMain( resBoard,mainFrame, History,Etape);
            }
        });
        
        displayAddedCell(row, column, frame, Ltmp);
        
        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        frame.setVisible(true);
    }
}
