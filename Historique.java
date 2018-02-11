import java.util.ArrayList ;

public class Historique 
{
    ArrayList< ArrayList< ArrayList<Integer>> > History;
    int index;
    int maxindex;
    
    public Historique()
    {
        History = new ArrayList< ArrayList< ArrayList<Integer>> > ();
        index = 0;
        maxindex = 0;
    }
    
    public void addEvnt(  ArrayList< ArrayList<Integer>> L )
    {
        
        if ( index != maxindex)
        {
            while (index < maxindex)
            {
                History.remove( maxindex-1 );
                maxindex--;
            }
        }
        ArrayList< ArrayList<Integer>> sauv = copy(L);
        
        History.add(sauv);
        index ++;
        maxindex++;
    }
    
    public ArrayList< ArrayList<Integer>> copy( ArrayList< ArrayList<Integer>> L)
    {
        int i,j;
        ArrayList< ArrayList<Integer>> sauv = new ArrayList< ArrayList<Integer>>();
        for(i=0;i< L.size() ; i ++)
        {
            sauv.add( new ArrayList<Integer>());
            for(j=0;j< L.get(0).size();j++)
                sauv.get(i).add( L.get(i).get(j));
        }
        return sauv;
    }
    
    
    public  ArrayList< ArrayList<Integer>> arriere()
    {
        if (index <2)
            return copy(History.get(0));
        index --;
        return copy(History.get( index-1)); // faut retourner une copie, sinon automate modifie le tableau et l'historique ne sera plus bon
    }
    
    public  ArrayList< ArrayList<Integer>> avant()
    {
        if (index ==maxindex)
            return copy(History.get(History.size()-1));
        index ++;
        return copy(History.get( index-1)); // faut retourner une copie, sinon automate modifie le tableau et l'historique ne sera plus bon
    }
    
    // fonction qui détecte lorsqu'on a atteint un état stable
    public boolean detectEnd ()
    {
        int i,j;
        ArrayList< ArrayList<Integer>> oldState = History.get( History.size() - 2);
        ArrayList< ArrayList<Integer>> newState = History.get( History.size() - 1);
        for(i = 0 ; i < newState.size() ; i ++)
        {
            for(j = 0 ; j < newState.get(0).size() ; j++)
            {
                if ( newState.get(i).get(j).intValue() != oldState.get(i).get(j).intValue())
                    return false;
            }
        }
        return true;
    }
    
    public boolean detectCycle()
    {
        int i,j;
        if( History.size() < 3 )
            return false;
        ArrayList< ArrayList<Integer>> oldState = History.get( History.size() - 3);
        ArrayList< ArrayList<Integer>> newState = History.get( History.size() - 1);
        for(i = 0 ; i < newState.size() ; i ++)
        {
            for(j = 0 ; j < newState.get(0).size() ; j++)
            {
                if ( newState.get(i).get(j).intValue() != oldState.get(i).get(j).intValue())
                    return false;
            }
        }
        return true;
    }
    
    public void reset()
    {
        History = new ArrayList< ArrayList< ArrayList<Integer>> > ();
        index = 0;
        maxindex = 0;
    }
    
}
