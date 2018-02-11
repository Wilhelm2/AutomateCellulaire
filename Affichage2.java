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








public class Affichage2 extends JPanel
{
    int column , row;
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
        //Aut.DefaultSetting();
        Aut.RandomSetting( 300 );
        
        final Historique History = new Historique();
        boolean changing = true;
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
        
        
        button = new JButton( "Save Image");
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
                Affichage2 tmpSave = new Affichage2(Aut.row,Aut.column, AtmpSave);
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
        
        
        frame.add(toolbar, BorderLayout.NORTH);

        A = new Affichage2(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Aut);
        frame.add(A);
        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        frame.setVisible(true);
        History.addEvnt(Aut.tab);
        Etape.setText( " "+ String.valueOf(History.index)+ "  ");

        while(changing)
        {
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
                A = new Affichage2(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Aut);
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
                    changing = false;
                }
            }
        }
    }
    
    public void paintComponent( Graphics g)
    {
        int i,j;
        super.paintComponent(g);
        for ( i= 1 ; i < row ;i++)
            g.drawLine(0,20*i, column*20, 20*i);
        for ( i= 1 ; i < column ;i++)
            g.drawLine(20*i,0, 20*i, row*20);
        
        for(i=0 ; i < row ; i++)
        {
            for(j=0 ; j< column ; j++)
            {
                if ( Aut.tab.get(i).get(j).intValue()==1 )
                    g.setColor(CelluleVivante);
                else
                    g.setColor(CelluleMorte);
                g.fillOval(j*20,i*20,20,20);
            }
        }        
    }
    
    Affichage2( int row,int column, Automate Aut)
    {
        this.column = column;
        this.row = row;
        this.setPreferredSize( new Dimension(column * 20, row*20));
        this.Aut = Aut;
    }
    
    
}
