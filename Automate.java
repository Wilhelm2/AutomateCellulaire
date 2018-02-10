
import java.util.ArrayList ;
import java.util.Random ;


public class Automate
{
    ArrayList< ArrayList < Integer > > tab = new ArrayList < ArrayList<Integer> > ();
    int column,row;
    int speed= 1;
    public boolean run = true;
            
            
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
        
        //~ Random generator = new Random(System.currentTimeMillis());
        //~ for (i = 0 ; i < beginCells ; i++)
        //~ {
            //~ tmp = (int)( generator.nextDouble()*( row ) ) ;
            //~ tmp2 = (int)( generator.nextDouble()*( column ) ) ;
            //~ for( j = Math.max( tmp -1 , 0) ; j < Math.min(tmp+1, column) ; j++)
            //~ {
                //~ for(k = Math.max(tmp2-1,0) ; k < Math.min(tmp2 +1, row) ; k++)
                //~ {
                    //~ System.out.println(" j = "+ j + " et k = " + k);
                    //~ if( ! ( j== tmp && k == tmp2) )
                        //~ tab.get(k).set(j, new Integer(1) );
                    //~ else 
                        //~ return;
                //~ }
            //~ }
        //~ }
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
    

    // Si 3 voisins vivants ->naît 
    // Si vivante et entourée de 2 ou 3 cellules reste vivante
    // Sinon meurt
    public boolean JeuVieSimple( int row, int column, ArrayList<ArrayList<Integer>> Ltmp)
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
    
    
}
