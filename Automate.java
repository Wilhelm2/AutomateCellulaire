
import java.util.ArrayList ;
import java.util.Random ;
import javax.swing.JPanel;


public class Automate
{
    ArrayList< ArrayList < Integer > > tab = new ArrayList < ArrayList<Integer> > ();
    int column,row;
    int speed= 1;
    public boolean run = true;
    JPanel Actual ;
            
    public Automate (  int row , int column,int beginCells)
    {
        int tmp, tmp2;
        int i,j,k;
        this.column = column;
        this.row = row;
        
        for(i=0 ; i < row ; i++)
        {
            tab.add( new ArrayList<Integer>() ) ;
            for(j=0 ; j< column ; j++)
                tab.get(i).add( new Integer(0) );
        }
    }
    
    public void changeState()
    {
        run = !run;
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
                Ltmp.get(i).add( new Integer(tab.get(i).get(j).intValue())) ; 
        }
        
        for( i = 0 ; i< row;i++)
        {
            for(j=0; j < column;j++)
            {
                if( JeuVieSimple(i,j,Ltmp) )
                {
                    if( Ltmp.get(i).get(j).intValue() == 0) // cellule morte -> changement
                        tab.get(i).set(j, new Integer(1));
                }
                else
                {
                    if( Ltmp.get(i).get(j).intValue() == 1) // cellule morte -> changement
                        tab.get(i).set(j, new Integer(0));
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
    
    public 
    
    
    public void DefaultSetting()
    {
        int i,j;
        for(i=0;i<row; i++)
        {
            for(j=0;j<column;j++)
            {
                if( (i+j)%2 == 0)
                    tab.get(i).set(j,new Integer(1));
            }
        }
        
    }
    
    public void RandomSetting( int randomValues)
    {
        int i,k,j;
        int tmp, tmp2;
        Random generator = new Random(System.currentTimeMillis());
        for (i = 0 ; i < randomValues ; i++)
        {
            tmp = (int)( generator.nextDouble()*( row ) ) ;
            tmp2 = (int)( generator.nextDouble()*( column ) ) ;
            tab.get(tmp).set(tmp2, new Integer(1));
            
        }
    }
    
    
}
