package View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;

/**
 * Brouillon de la classe Non MVC
 * @author AIDAN
 */
public class PanelMenu extends JPanel implements ActionListener{
    JButton jouer = new JButton("Jouer");
    JButton load = new JButton("Charger la partie anterieur");
    JButton quit = new JButton("Quitter le jeu");
    JButton instructions = new JButton("Guide de jeux");
    GridLayout grid = new GridLayout(4,1);
    protected Image bgImage;
    public PanelMenu(Image image)
    {
        super(true);
        bgImage = image;
        setOpaque(false);
        add(jouer);
        add(load);
        add(instructions);
        add(quit);
        /**
         * Methode pour ouvrir les instructions
         * */
        instructions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent n) {
                try{
                    Desktop.getDesktop().open(new java.io.File("src/View/pictures/Instructions.pdf"));
                }catch(Exception l){
                    l.printStackTrace();
                }
            }
        });
        grid.setHgap(150);
        grid.setVgap(150);
        setLayout(grid);
        setPreferredSize(new Dimension(800, 800));
        quit.addActionListener(this);
    }
    public void paint(Graphics g)
    {
        g.drawImage(bgImage, 0 ,0 ,this);
        super.paint(g);
    }
    @Override
    public void actionPerformed(ActionEvent e)  {
        Object pressedButton= e.getSource();
        if(pressedButton == quit){
            System.exit(0);
        }
        if(pressedButton == load){
            //TODO Creer methode pour charger partie
        }
        if(pressedButton == jouer){
            //TODO Creer methode pour  jouer
        }
    }
}


/**
 * Brouillon de la classe Non MVC
 * @author AIDAN
 */


