package luz;

import javax.swing.JFrame;

/**
 *
 * @author jacob
 */
public class Window {

    public static void main(String[] args) {
        JFrame window = new JFrame("Efecto Campo Vision");
        GamePanel gamePanel = new GamePanel();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(600, 600);
        window.setLocationRelativeTo(null);
        window.add(gamePanel);
        window.setVisible(true);
    }

}
