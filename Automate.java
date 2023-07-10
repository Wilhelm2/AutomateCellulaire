
import java.util.ArrayList ;
import java.util.Random ;
import javax.swing.JPanel;
import java.util.Collections;

public class Automate
{
    ArrayList<ArrayList<Integer>> tab = new ArrayList <ArrayList<Integer>>();
    int column,row;
    int speed= 1;
    public boolean run = false;
    public boolean changing = true;
    JPanel Actual ;
    Affichage2 Aff;
            
    public Automate(int row, int column)
    {
        this.column = column;
        this.row = row;
        initializeEmptyBoard();
    }

    public Automate(ArrayList<ArrayList<Integer>> tab)
    {
        this.tab = tab;
        this.row = tab.size();
        this.column = tab.get(0).size();
    }
    
    public void initializeEmptyBoard()
    {
        for(int i=0 ; i < row ; i++)
        {
            tab.add(new ArrayList<Integer>());
            for(int j=0; j < column; j++)
                tab.get(i).add(Integer.valueOf(0));
        }
    }

    public void changeState()
    {
        run = !run;
    }
    
    public void miseAJour()
    {
        // commence par faire une copie du tableau
        ArrayList< ArrayList<Integer> > Ltmp = new ArrayList< ArrayList <Integer>> ();
        for(int i = 0 ; i< row;i++)
        {
            Ltmp.add( new ArrayList<Integer>() ) ;
            for(int j=0; j < column;j++)
                Ltmp.get(i).add( Integer.valueOf(tab.get(i).get(j).intValue())) ; 
        }
        
        for(int i = 0 ; i< row;i++)
        {
            for(int j=0; j < column;j++)
            {
                if( JeuVieSimple(i,j,Ltmp) )
                {
                    if( Ltmp.get(i).get(j).intValue() == 0) // cellule morte -> changement
                        tab.get(i).set(j, Integer.valueOf(1));
                }
                else
                {
                    if( Ltmp.get(i).get(j).intValue() == 1) // cellule morte -> changement
                        tab.get(i).set(j, Integer.valueOf(0));
                }
            }
        }
    }
    
    public int VoisinsVivants( int row, int column, ArrayList<ArrayList<Integer>> Ltmp )
    {
        int i,j;
        int Voisins = 0;
        for(i = ((row==0) ? 0: -1) ; i <= ((row == this.row-1) ? 0:1) ; i++)
        {
            for(j = ((column==0) ? 0: -1) ; j <= ((column == this.column-1) ? 0:1);j++)            
            {
                if ( i==0 && j== 0 ) // centre du "carré"
                    continue ;
                if( Ltmp.get(row+i).get(column+j).intValue() == 1)
                    Voisins ++;
            }
        }
        return Voisins;
    }
    // Si 3 voisins vivants ->naît 
    // Si vivante et entourée de 2 ou 3 cellules reste vivante
    // Sinon meurt
    public boolean JeuVieSimple( int row, int column, ArrayList<ArrayList<Integer>> Ltmp)
    {
        int Voisins = VoisinsVivants(row,column, Ltmp);
        boolean Vivant = (Ltmp.get(row).get(column).intValue() == 1);
        
        
        if( Voisins == 3 && !Vivant )
            return true;
        else if( Vivant && ( Voisins == 2 || Voisins == 3) )
            return true;
        else
            return false;
    }
    
    public void setTab(int row, int column, ArrayList<ArrayList<Integer>> tab )
    {
        this.row = row;
        this.column = column;
        this.tab = tab;
    }
    
    public void setTab(ArrayList<ArrayList<Integer>> tab )
    {
        this.row = tab.size();
        this.column = tab.get(0).size();
        this.tab = tab;
    }
    
    
    public void ChessBoard()
    {
        for(int i=0;i<row; i++)
        {
            for(int j=0;j<column;j++)
            {
                if( (i+j)%2 == 0)
                    tab.get(i).set(j,Integer.valueOf(1));
                else
                    tab.get(i).set(j,Integer.valueOf(0));
            }
        }
    }
    
    public void RandomSetting(int nbInitialLivingCells)
    {
        int randRow, randColumn;
        Random generator = new Random(System.currentTimeMillis());
        initializeEmptyBoard();
        
        for (int i = 0 ; i < nbInitialLivingCells ; i++)
        {
            randRow = (int)( generator.nextDouble()*(row) ) ;
            randColumn = (int)( generator.nextDouble()*(column) ) ;
            tab.get(randRow).set(randColumn, Integer.valueOf(1));
        }
    }    
}
