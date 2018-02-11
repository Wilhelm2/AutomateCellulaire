import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon ;
import java.io.IOException ;
import java.util.ArrayList ;
import java.awt.Dimension ;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JToolBar ;
import javax.swing.JButton ;
import java.awt.BorderLayout;
import java.lang.Thread ;
import java.awt.event.*;
import javax.swing.JFormattedTextField ;
import java.awt.Font;
import javax.imageio.ImageIO ;
import java.io.File ;
import java.awt.image.BufferedImage ;
import java.awt.Component ;
import javax.swing.JComboBox ;
import javax.swing.JOptionPane;









public class Affichage2 extends JPanel
{
    Automate Aut;
    static final Object Obj = new Object(); // évite de faire des actions pendant que la prochaine étape est calculée
    static Affichage2 A;
    Color CelluleVivante = Color.BLUE;
    Color CelluleMorte = Color.WHITE;
    static String DirectoryPATH = "./Image";
    
    public static void main(String[] args)
        throws IOException, InterruptedException 
    {
        new File(DirectoryPATH).mkdir(); // créé le répertoire contenant les images
        
        final JFrame frame= new JFrame();
        final Automate Aut = new Automate ( Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        Aut.DefaultSetting();
        //Aut.RandomSetting( 300 );
        
        final Historique History = new Historique();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JToolBar toolbar = new JToolBar();
        JLabel L = new JLabel(" Étape :");
        L.setFont(new Font("Courier", Font.BOLD,15));
        toolbar.add( L);
        
        JLabel Etape = new JLabel(" "+ String.valueOf(History.index)+ "  ");
        L.setFont(new Font("Courier", Font.BOLD,15));
        toolbar.add( Etape);
        
        
        JButton button = new JButton(new ImageIcon("arriere.png"));
        button.addActionListener(new ActionListener(){
            public void actionPerformed( ActionEvent ae )
            {
                System.out.println("va en arrière");
                synchronized(Obj)
                {
                    if( History.index == 0 )
                        return;
                    else
                    {
                        Aut.tab = History.arriere();
                        Etape.setText(" "+ String.valueOf(History.index)+ "  ");
                        frame.repaint();
                    }
                }
            }
        });
        toolbar.add(button);
        
        button = new JButton(new ImageIcon("stop.png"));
        button.addActionListener(new ActionListener(){
            public void actionPerformed( ActionEvent ae )
            {
                Aut.changeState();
            }
        });
        toolbar.add(button);
        
        button = new JButton(new ImageIcon("avant.png"));
        button.addActionListener(new ActionListener(){
            public void actionPerformed( ActionEvent ae )
            {
                synchronized(Obj)
                {
                    if( History.index == History.maxindex ) 
                        return ;
                    else
                    {
                        Aut.tab = History.avant();
                        Etape.setText( " "+ String.valueOf(History.index)+ "  ");
                        frame.repaint();
                    }
                }
            }
        });
        toolbar.add(button);
        
        
        // SPEED
        L = new JLabel(" Speed :");
        L.setFont(new Font("Courier", Font.BOLD,15));
        toolbar.add( L);
        final JFormattedTextField F = new JFormattedTextField ();
        F.setPreferredSize( new Dimension(40, 30) );
        F.setMaximumSize( new Dimension(30, 30) );
        toolbar.add(F);
        
        button = new JButton( "OK");
        button.addActionListener(new ActionListener(){
            public void actionPerformed( ActionEvent ae )
            {
                int speed;
                try
                {speed = Integer.parseInt(F.getText(),10);}
                catch ( NumberFormatException e) { System.out.println("Mauvais format"); return;};
                Aut.speed = speed;
            }
        });
        toolbar.add(button);
        
        
        button = new JButton("Save Image");
        button.addActionListener(new ActionListener(){
            public void actionPerformed( ActionEvent ae )
            {
                BufferedImage bi = new BufferedImage(Aut.Actual.getSize().width, Aut.Actual.getSize().height, BufferedImage.TYPE_INT_ARGB); 
                Graphics g = bi.createGraphics();
                Aut.Actual.paint(g);  //this == JComponent
                g.dispose();
                try{ImageIO.write(bi,"png",new File(DirectoryPATH+"/Automate:"+Aut.row+ ":"+ Aut.column+ " E: " + History.index+".png"));}catch (Exception e) {}
                bi = new BufferedImage(Aut.Actual.getSize().width, Aut.Actual.getSize().height, BufferedImage.TYPE_INT_ARGB); 
                Automate AtmpSave = new Automate( Aut.row, Aut.column );
                Affichage2 tmpSave = new Affichage2( AtmpSave);
                JFrame ftmp = new JFrame();
                ftmp.add(tmpSave);
                ftmp.pack();
                AtmpSave.tab = History.History.get(0);
                tmpSave.paintComponent( tmpSave.getGraphics());
                Graphics g2 = bi.createGraphics();
                tmpSave.paint(g2);
                g2.dispose();
                try{ImageIO.write(bi,"png",new File(DirectoryPATH+"/INIT:Automate:"+Aut.row+ ":"+ Aut.column+ " E: " + History.index+".png"));}catch (Exception e) {}
            }
        });
        toolbar.add(button);
        
        
        button = new JButton("Restart");
        button.addActionListener(new ActionListener(){
            public void actionPerformed( ActionEvent ae )
            {
                Aut.DefaultSetting();
                History.reset();
                History.addEvnt(Aut.tab);
                Etape.setText(  " "+ String.valueOf(History.index)+ "  " );
                frame.repaint();
                Aut.changing = true;
            }
        });
        toolbar.add(button);
        
        
        String  []StartConditions = { "Damier", "Aléatoire", "Réglage à la main", "Importer fichier"};
        final JComboBox<String> comboxStartConditions = new JComboBox<>(StartConditions);
        comboxStartConditions.addActionListener(new ActionListener(){
            public void actionPerformed( ActionEvent ae )
            {
                String select = comboxStartConditions.getSelectedItem().toString();
                if ( select.equals("Aléatoire") )
                    Aut.RandomSetting(100);
                else if ( select.equals("Damier") )
                    Aut.DefaultSetting();
                else if ( select.equals("Importer fichier") )
                {
                    ArrayList<ArrayList<Integer>> Ltmp;
                    String fileName = JOptionPane.showInputDialog( frame, "Nom du fichier", null);
                    try{Ltmp = Parser.ParseFile( fileName );}
                        catch ( IOException e) { System.out.println("Fichier n'existe pas"); return;};
                    Aut.setTab( Ltmp.size(), Ltmp.get(0).size(), Ltmp);
                }
                else 
                {
                    Parser.ParseSelfInput( Aut.row, Aut.column, Aut.Aff,frame, History,Etape);
                    return ;
                }
                
                frame.repaint();
                History.reset();
                History.addEvnt(Aut.tab);
                Etape.setText(" "+ String.valueOf(History.index)+ "  " );
                Aut.changing = true;
                Aut.run = false;
        

            }
        });
        
        
        toolbar.add(comboxStartConditions);
        
        
        frame.add(toolbar, BorderLayout.NORTH);

        A = new Affichage2( Aut);
        frame.add(A);
        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        frame.setVisible(true);
        History.addEvnt(Aut.tab);
        Etape.setText(" "+ String.valueOf(History.index)+ "  " );
        
        while(true)
        {
            while (!Aut.changing) 
                Thread.sleep(100);
            
            try { Thread.sleep(1000/ Aut.speed); } 
                catch (InterruptedException ex)  { Thread.currentThread().interrupt(); }
            while ( ! Aut.run ) 
                Thread.sleep(100);
            
            // Créé un nouveau JPanel à chaque fois pour éviter que le graphique soit directement 
            // changé alors qu'il est affiché (sinon modifie l'affichage pour chaque changement de case)
            synchronized(Obj)
            {
                Aut.miseAJour();
                frame.remove(A);
                A = new Affichage2( Aut);
                Aut.Actual = A;
                frame.add(A);
                frame.pack();
                dim = Toolkit.getDefaultToolkit().getScreenSize();
                frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
                frame.setVisible(true);
                History.addEvnt(Aut.tab);
                Etape.setText( " "+ String.valueOf(History.index)+ "  ");
                if ( History.detectEnd())
                {
                    System.out.println("FIN");
                    Aut.changing = false;
                }
                if ( History.detectCycle())
                {
                    System.out.println("Cycle détecté !");
                    Aut.changing = false;
                }

            }
        }
    }
    
    public void paintComponent( Graphics g)
    {
        int i,j;
        super.paintComponent(g);
        for ( i= 1 ; i < Aut.row ;i++)
            g.drawLine(0,20*i, Aut.column*20, 20*i);
        for ( i= 1 ; i < Aut.column ;i++)
            g.drawLine(20*i,0, 20*i, Aut.row*20);
        
        for(i=0 ; i < Aut.row ; i++)
        {
            for(j=0 ; j< Aut.column ; j++)
            {
                if ( Aut.tab.get(i).get(j).intValue()==1 )
                    g.setColor(CelluleVivante);
                else
                    g.setColor(CelluleMorte);
                g.fillOval(j*20,i*20,20,20);
            }
        }        
    }
    
    Affichage2( Automate Aut)
    {
        this.setPreferredSize( new Dimension(Aut.column * 20, Aut.row*20));
        this.Aut = Aut;
        Aut.Aff = this;
    }
    
    public void ReglageAMain( ArrayList< ArrayList<Integer>> List, JFrame frame, Historique History, JLabel Etape)
    {
        Aut.setTab(List);
        frame.repaint();
        History.reset();
        History.addEvnt(Aut.tab);
        Etape.setText(" "+ String.valueOf(History.index)+ "  " );
        Aut.changing=true;
        Aut.run = false;
        return;
    }
    
}
