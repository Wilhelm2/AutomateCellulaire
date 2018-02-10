import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel ;
import javax.swing.ImageIcon ;
import java.io.IOException ;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList ;
import java.awt.Dimension ;
import java.awt.Toolkit;
import java.util.concurrent.TimeUnit;
import java.awt.Color;





public class Affichage extends JFrame
{
    int column , row;
    static Automate Aut;
    static Affichage A;
    ArrayList< ArrayList<JLabel> > L ;
    Color CelluleVivante = Color.BLUE;
    Color CelluleMorte = Color.WHITE;
    
    
    public static void main(String[] args)
        throws IOException, InterruptedException 
    {
        Aut = new Automate ( Integer.parseInt(args[0]), Integer.parseInt(args[1]), 10);
        A = new Affichage(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        
    }
    
    // initialise toutes les cellules à mortes
    Affichage( int column, int row)
    {
        GridBagConstraints gc = new GridBagConstraints();
        this.setLayout(new GridBagLayout());
        L= new ArrayList< ArrayList <JLabel>> ();
        JLabel Ltmp;
        int i,j;
        this.column = column;
        this.row = row;
        for(i=0 ; i < row ; i++)
        {
            L.add( new ArrayList<JLabel>() ) ;
            for(j=0 ; j< column ; j++)
            {
                Ltmp = new JLabel("" ); // doit rester ici sinon le même jlabel est utilisé partout
                Ltmp.setOpaque(true);
                Ltmp.setBackground(CelluleMorte);
                Ltmp.setPreferredSize(new Dimension(20, 20));
                L.get(i).add( Ltmp );
                gc.gridx = i;
                gc.gridy = j;
                this.add(Ltmp, gc);
            }
            
        }
        this.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        this.setVisible(true);
        
        DefaultSetting();
        while(true)
        {
            try { TimeUnit.SECONDS.sleep(1); } 
                catch (InterruptedException ex)  { Thread.currentThread().interrupt(); }
            miseAJour();
        }
    }
    
    public void miseAJour()
    {
        int i,j;
        // commence par faire une copie du tableau
        ArrayList< ArrayList<Integer> > Ltmp = new  ArrayList< ArrayList <Integer>> ();
        for( i = 0 ; i< row;i++)
        {
            Ltmp.add( new ArrayList<Integer>() ) ;
            for(j=0; j < column;j++)
                Ltmp.get(i).add( new Integer(Aut.tab.get(i).get(j).intValue())) ; 
        }
        
        for( i = 0 ; i< row;i++)
        {
            for(j=0; j < column;j++)
            {
                if( JeuVieSimple(j,i,Ltmp) )
                {
                    if( Ltmp.get(i).get(j).intValue() == 0) // cellule morte -> changement
                    {
                        L.get(i).get(j).setBackground( CelluleVivante );
                        Aut.tab.get(i).set(j, new Integer(1));
                    }
                }
                else
                {
                    if( Ltmp.get(i).get(j).intValue() == 1) // cellule morte -> changement
                    {
                        L.get(i).get(j).setBackground( CelluleMorte );
                        Aut.tab.get(i).set(j, new Integer(0));
                    }
                }
            }
        }
        
    }
    
    public void DefaultSetting()
    {
        int i,j;
        for(i=0;i<row; i++)
        {
            for(j=0;j<column;j++)
            {
                if( (i+j)%2 == 0)
                {
                    Aut.tab.get(i).set(j,new Integer(1));
                    L.get(i).get(j).setBackground(  CelluleVivante );
                }
            }
        }
        
    }
    
    // Si 3 voisins vivants ->naît 
    // Si vivante et entourée de 2 ou 3 cellules reste vivante
    // Sinon meurt
    public boolean JeuVieSimple(int column, int row, ArrayList<ArrayList<Integer>> Ltmp)
    {
        int i,j;
        int VoisinsVivants = 0;
        boolean Vivant = (Ltmp.get(row).get(column).intValue() == 1);
        
        for(i = ((row==0) ? 0: -1) ; i <= ((row == this.row-1) ? 0:1) ; i++)
        {
            for(j = ((column==0) ? 0: -1) ; j <= ((column == this.column-1) ? 0:1);j++)            
            {
                if ( i==0 && j== 0 ) // centre du "carré"
                    continue ;
                if( Ltmp.get(row+i).get(column+j).intValue() == 1)
                    VoisinsVivants ++;
            }
        }
        
        if( VoisinsVivants == 3 && !Vivant )
            return true;
        else if( Vivant && ( VoisinsVivants == 2 || VoisinsVivants == 3) )
            return true;
        else
            return false;
    }
}
