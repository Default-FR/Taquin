import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.math.BigDecimal;

public class View extends JFrame {
    protected Model m;
    protected ControlMenu controlMenu;
    protected ControlBouton controlButton;

    public View(Model m){

        this.m = m;

        createMenu();
        createContent();

        controlMenu = new ControlMenu(m, this);
        controlButton = new ControlBouton(m, this);

        setMenuListener(controlMenu);
        setButtonsListener(controlButton);
        setTitle("Taquin");

        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
    }

    public void display(){
        setVisible(true);
        setLocationRelativeTo(null);
    }

    public void createDialog(String titre, String message){
        ImageIcon image = new ImageIcon("./src/images/option_image.png");
        JOptionPane.showMessageDialog( this, message, titre, JOptionPane.PLAIN_MESSAGE, image );
    }

    public void createInput(){
        // arrondi
        double score_final = m.ms*0.1;
        BigDecimal bd = new BigDecimal(score_final);
        bd= bd.setScale(3,BigDecimal.ROUND_DOWN);
        score_final = bd.doubleValue();

        String nom = JOptionPane.showInputDialog(this,"Vous avez fait un score de "+score_final+" seconde(s)\nEntrez votre nom pour continuer","Puzzle résolu",JOptionPane.PLAIN_MESSAGE );
        nom = nom.replace("__.__"," ");
        if(nom.length()>15){
            nom = nom.substring(0, 15);
        }
        if(nom.isEmpty()) nom="Anonymous";
        m.nom = nom;
    }

    public void setMenuListener(ActionListener ctrlMenu){
        m.accueil.addActionListener(ctrlMenu);
        m.alea.addActionListener(ctrlMenu);
        m.grid1.addActionListener(ctrlMenu);
        m.grid2.addActionListener(ctrlMenu);
        m.grid3.addActionListener(ctrlMenu);
        m.option_scores.addActionListener(ctrlMenu);
        m.option_rules.addActionListener(ctrlMenu);
        m.option_dev.addActionListener(ctrlMenu);
        m.quit.addActionListener(ctrlMenu);
    }

    public void setButtonsListener(ActionListener ctrlButton){
        m.recommencer.addActionListener(ctrlButton);
        for(int i=0; i<m.dimensions; i++) {
            for(int j=0; j<m.dimensions; j++){
                m.tabBouton[i][j].addActionListener(ctrlButton);
            }
        }
    }

    public void createMenu(){
        JMenuBar menu = new JMenuBar();
        //Options de jeu
            JMenu menuOpt = new JMenu("Options");
            JMenu option_new = new JMenu("Nouvelle partie");
                option_new.add(m.grid1);
                option_new.add(m.grid2);
                option_new.add(m.grid3);
            menuOpt.add(m.accueil);
            menuOpt.add(m.option_scores);
            menuOpt.addSeparator();
            menuOpt.add(m.alea);
            menuOpt.add(option_new);
            menuOpt.addSeparator();
            menuOpt.add(m.quit);
        //Options supplémentaires
            JMenu menuPlus = new JMenu("À propos");
                menuPlus.add(m.option_rules);
                menuPlus.add(m.option_dev);
        menu.add(menuOpt);
        menu.add(menuPlus);
        setJMenuBar(menu);
    }

    public void createContent(){
        getContentPane().removeAll();
        JPanel buttons_pan = new JPanel(new GridBagLayout());
        buttons_pan.setOpaque(false);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.BOTH;
            gbc.insets = new Insets(1,1,1,1);

            for(int i=0; i<m.dimensions; i++) {
                for(int j=0; j<m.dimensions; j++){
                    gbc.gridx=i;
                    gbc.gridy=j;
                    buttons_pan.add(m.tabBouton[i][j],gbc);
                }
            }

        JPanel game_background = new JPanel();
        game_background.setBorder(BorderFactory.createLineBorder(Color.white));
        game_background.setOpaque(false);
        game_background.setLayout(new BoxLayout(game_background, BoxLayout.Y_AXIS));
            game_background.add(buttons_pan);

        JPanel infos_pan = new JPanel(new GridBagLayout());
        infos_pan.setBorder(BorderFactory.createLineBorder(Color.white));
        infos_pan.setOpaque(false);
        infos_pan.setPreferredSize(new Dimension(250, game_background.getHeight()));
            JPanel entete = new JPanel();
                entete.setOpaque(false);
                entete.setLayout(new BoxLayout(entete, BoxLayout.Y_AXIS));
                entete.add(new JLabel_affichage("    Nouvelle partie: grille "+m.dimensions+"x"+m.dimensions));
                entete.add(new JLabel(" "));
                entete.add(new JLabel_affichage("--------------------------------------------"));
            JPanel infos = new JPanel();
                infos.setLayout(new BoxLayout(infos, BoxLayout.Y_AXIS));
                infos.setOpaque(false);
                infos.add(new JLabel(" "));
                infos.add(m.score);
                infos.add(new JLabel(" "));
                infos.add(new JLabel_affichage("--------------------------------------------"));
                infos.add(new JLabel(" "));
                infos.add(new JLabel_affichage("Meilleurs scores: "));
                for (int k=0; k<3; k++){
                    infos.add(new JLabel_affichage("        "+m.getScore(m.dimensions)[k]));
                }
                infos.add(new JLabel(" "));
                infos.add(new JLabel_affichage("--------------------------------------------"));
                infos.add(new JLabel(" "));
            JPanel option = new JPanel();
                option.setOpaque(false);
                option.add(m.recommencer);
            gbc.gridx = 0;
            gbc.gridy = 0;
            infos_pan.add(entete,gbc);
            gbc.gridy = 1;
            infos_pan.add(infos, gbc);
            gbc.gridy = 2;
            infos_pan.add(option, gbc);

        JPanel pan_grp = new JPanel(new BorderLayout());
        pan_grp.setOpaque(false);
            pan_grp.add(infos_pan, BorderLayout.WEST);
            pan_grp.add(new JLabel("  "), BorderLayout.CENTER);
            pan_grp.add(game_background, BorderLayout.EAST);

        JPanel_pers pan_gen = new JPanel_pers();
            pan_gen.add(pan_grp);

        setContentPane(pan_gen);
    }
}
