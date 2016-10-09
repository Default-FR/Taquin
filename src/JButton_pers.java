import javax.swing.*;
import java.awt.*;

public class JButton_pers extends JButton{
    private int id;

    public JButton_pers(String content, int id){
        super(content);
        this.id = id;
        setPreferredSize(new Dimension(100,100));
        setBackground(Color.white);
        setBorder(BorderFactory.createLineBorder(Color.white));    }

    public int getId(){
        return this.id;
    }
}