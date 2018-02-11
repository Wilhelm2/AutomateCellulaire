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
        int row, column;
        int i,j,x,y;
        int debut = 0, fin= 0;
        ArrayList< ArrayList<Integer>> List = new ArrayList< ArrayList<Integer>>();
        File F = new File (FileName);
        if (! F.exists() )
        {
            System.out.println("Fichier non trouvé"); 
            return null;
        }
        
        try(BufferedReader br = new BufferedReader(new FileReader(F)))
        {
            String l = br.readLine();
            while (fin < l.length() && l.charAt(fin) >= '0' && l.charAt(fin) <= '9')
            {
                System.out.println("lit le car " + l.charAt(fin));
                fin ++;
            }
            column = Integer.parseInt( l.substring(debut,fin)) ;
            debut = fin ; 
            fin ++;
            while (fin < l.length() && l.charAt(fin) >= '0' && l.charAt(fin) <= '9')
                fin ++;
            row = Integer.parseInt( l.substring(debut+1,fin)) ;
            
            for ( i = 0; i < row ; i++) // initialisation
            {
                List.add(new ArrayList<Integer>());
                for (j = 0 ; j < column ; j++)
                    List.get(i).add( new Integer(0));
            }
            
            while(( l = br.readLine()) != null )
            {
                debut = 0; fin = 0;
                while (fin < l.length() && l.charAt(fin) >= '0' && l.charAt(fin) <= '9')
                    fin ++;
                x = Integer.parseInt( l.substring(debut,fin)) ;
                debut = fin ; 
                fin ++;
                while ( fin < l.length() &&l.charAt(fin) >= '0' && l.charAt(fin) <= '9')
                    fin ++;
                y = Integer.parseInt( l.substring(debut+1,fin)) ;
                List.get(x).set(y, new Integer(1));
            }
        }
        
        return List;
    }
    
    static void ParseSelfInput ( int row, int column, final Affichage2 A, final JFrame mainFrame, final Historique History, final JLabel Etape) 
    {
        int i,j;
        
        GridBagConstraints gc = new GridBagConstraints();
        final JFrame frame = new JFrame();
        
        frame.setLayout(new GridBagLayout());
        final ArrayList< ArrayList <JButton>>  L= new ArrayList< ArrayList <JButton>> ();
        
        for(i=0 ; i < row ; i++)
        {
            L.add( new ArrayList<JButton>() ) ;
            for(j=0 ; j< column ; j++)
            {
                final JButton Ltmp = new JButton("" ); // doit rester ici sinon le même jlabel est utilisé partout
                Ltmp.setOpaque(true);
                Ltmp.setBackground(Color.WHITE);
                Ltmp.setPreferredSize(new Dimension(20, 20));
                Ltmp.addActionListener(new ActionListener(){
                    public void actionPerformed( ActionEvent ae )
                    {
                        Ltmp.setBackground(Color.BLUE);
                    }
                });
                
                L.get(i).add( Ltmp );
                gc.gridx = j;
                gc.gridy = i;
                frame.add(Ltmp, gc);
            }
            
        }
        JButton Ltmp = new JButton( "OK");
        Ltmp.addActionListener(new ActionListener(){
            public void actionPerformed( ActionEvent ae )
            {
                int i,j;
                ArrayList< ArrayList<Integer>> List = new ArrayList< ArrayList<Integer>>();
                for(i= 0; i < row; i ++)
                {
                    List.add( new ArrayList<Integer>());
                    for(j=0;j<column;j++)
                    {
                        if ( L.get(i).get(j).getBackground() == Color.WHITE)
                            List.get(i).add( new Integer(0) );
                        else
                            List.get(i).add( new Integer(1) );
                    }
                }
                frame.dispose();
                A.ReglageAMain( List,mainFrame, History,Etape);
            }
        });
        gc.gridx = column;
        gc.gridy = row;
        frame.add(Ltmp,gc);
        
        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        frame.setVisible(true);
        
    }
}
