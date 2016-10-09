import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class JPanel_pers extends JPanel {

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon image_background = new ImageIcon(getClass().getResource("/images/background.png"));
        g.drawImage(image_background.getImage(), 0, 0, getWidth(), getHeight(), this);
    }
}