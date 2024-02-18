package View;

import javax.swing.*;
import java.awt.*;

/**
 * Brouillon de la classe Non MVC
 * @author AIDAN
 */
public class MenuNaval extends JFrame{

    Image img = Toolkit.getDefaultToolkit().getImage("src/View/pictures/menu.jpg");
    public MenuNaval()  {

        /**
        * Ici c'est la creation du panel menu
        * */

        JPanel main = new PanelMenu(img);

        /**
         * Ici c'est la taille de Jframe
         * */
        Toolkit myScreen = Toolkit.getDefaultToolkit();
        Dimension screenSize = myScreen.getScreenSize();
        int heightScreen = screenSize.height;
        int widthScreen = screenSize.width;
        this.setSize(widthScreen/2, heightScreen/2);
        this.setLocation(widthScreen/4, heightScreen/4);
        this.setTitle("Bataille Navale");
        this.setResizable(false);

        this.getContentPane().add(main);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        this.pack();
        this.setVisible(true);

    }

    public Image setBGImage(){
        Image image = Toolkit.getDefaultToolkit().getImage("src/View/pictures/menu.jpg");
        return image;
    }
}
