import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;

public class Model {
    private final int NB_MELANGES = 75;

    //menu
    protected JMenuItem accueil;
    protected JMenuItem alea;
    protected JMenuItem grid1;
    protected JMenuItem grid2;
    protected JMenuItem grid3;
    protected JMenuItem quit;

    protected JMenuItem option_scores;
    protected JMenuItem option_rules;
    protected JMenuItem option_dev;

    //jeu (valeurs de base pour grille 3x3
    protected int dimensions = 3;
    protected int idBoutVide = 9;
    protected boolean jeuCommence = false;
    protected BufferedImage game_background;

    //Scoring
    protected Timer timer;
    protected JLabel_affichage score;
    protected int ms = 0;
    protected String nom;

    //boutons en jeu
    protected boolean premierClic = true;
    protected int idPremierBout;

    //boutons
    protected JButton_pers[][] tabBouton;
    protected JButton recommencer;

    public Model(){
        //Affichage
        score = new JLabel_affichage("Votre temps: 00'00''");
        score.setForeground(Color.white);
        timer = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ms+=1;
                score.setText("Votre temps: "+ms/10+"'"+ms%10+"''");
            }
        });

        //Options jeu
        accueil = new JMenuItem("Ordonner");
        alea = new JMenuItem("Mode aléatoire");
        grid1 = new JMenuItem("Grille 3x3");
        grid2 = new JMenuItem("Grille 4x4");
        grid3 = new JMenuItem("Grille 5x5");
        option_scores = new JMenuItem("Meilleurs Scores");
        quit = new JMenuItem("Quitter");

        //Options supplémentaires
        option_rules = new JMenuItem("Règles");
        option_dev = new JMenuItem("Crédits");

        createButtons();
    }

    public void setDimensions(int dim){
        this.dimensions = dim;
    }

    public void createButtons(){
        recommencer = new JButton("Mélanger");

        tabBouton = new JButton_pers[dimensions][dimensions];
        int compt = 0;
        BufferedImage image = null;
        for(int i=0; i<dimensions; i++){
            for(int j=0; j<dimensions; j++){
                compt++;
                if(i==dimensions-1 && j==dimensions-1){
                    tabBouton[j][i] = new JButton_pers("", dimensions*dimensions); // BOUTON VIDE ----------
                    idBoutVide = dimensions*dimensions;
                }

                else{
                        tabBouton[j][i] = new JButton_pers(""+compt, compt);
                }
                tabBouton[j][i].setEnabled(false);
            }
        }
    }

    public void melanger(){
        for(int i=0; i<dimensions; i++) {
            for (int j = 0; j < dimensions; j++) {
                tabBouton[j][i].setEnabled(true);
            }
        }

        //sauvegarde du bouton vide
        int xV = dimensions - 1, yV = dimensions - 1;

        for(int k=0; k<NB_MELANGES; k++) {
            int random = (int) (Math.random() * 4);

            switch (random) {
                case 0:
                    //déplacement vers le haut
                    if (yV-1>=0) {
                        //changement des textes
                        tabBouton[xV][yV].setText(tabBouton[xV][yV-1].getText());
                        tabBouton[xV][yV-1].setText("");
                        //mise à jour des valeurs
                        yV--;
                        idBoutVide = tabBouton[xV][yV].getId();
                    }
                    break;
                case 1:
                    //déplacement vers le bas
                    if(yV+1<dimensions){
                        //changement des textes
                        tabBouton[xV][yV].setText(tabBouton[xV][yV+1].getText());
                        tabBouton[xV][yV+1].setText("");
                        //mise à jour des valeurs
                        yV++;
                        idBoutVide = tabBouton[xV][yV].getId();
                    }
                    break;
                case 2:
                    //déplacement vers la droite
                    if(xV+1<dimensions){
                        //changement des textes
                        tabBouton[xV][yV].setText(tabBouton[xV+1][yV].getText());
                        tabBouton[xV+1][yV].setText("");
                        //mise à jour des valeurs
                        xV++;
                        idBoutVide=tabBouton[xV][yV].getId();
                    }
                    break;
                case 3:
                    //déplacement vers la gauche
                    if(xV-1>=0){
                        //changement des textes
                        tabBouton[xV][yV].setText(tabBouton[xV-1][yV].getText());
                        tabBouton[xV-1][yV].setText("");
                        //mise à jour des valeurs
                        xV--;
                        idBoutVide=tabBouton[xV][yV].getId();
                    }
                    break;
            }
        }
    }

    public void resetScore(){
        score.setText("Temps: 00'00''");
        ms = 0;
    }

    public boolean isSolved(){
        String soluce="";
        switch(dimensions){
            case 3:
                soluce = "12345678";
                break;
            case 4:
                soluce = "123456789101112131415";
                break;
            case 5:
                soluce = "123456789101112131415161718192021222324";
                break;
        }

        String schemaActuel = "";
        for(int i=0; i<dimensions; i++){
            for(int j=0; j<dimensions; j++){
                //on ne prends pas en compte le bouton vide
                if(tabBouton[j][i]!=tabBouton[dimensions-1][dimensions-1]){
                    schemaActuel += tabBouton[j][i].getText();
                }
            }
        }
        if(soluce.equals((schemaActuel))){
            for(int i=0; i<dimensions; i++) {
                for (int j = 0; j < dimensions; j++) {
                    tabBouton[j][i].setEnabled(false);
                }
            }
            return true;
        }
        return false;
    }

    public void checkLeaderboards(double score_final){
        //vérifie si le joueur doit etre inscrit ou non
        boolean is_not_best = true;
        int classement = -1;
        String[] place = new String[3];

        try {
            //lecture du fichier correspondant à la taille de la grille
            InputStream ips = new FileInputStream("./src/Leaderboards/Leaderboard"+dimensions+".txt");
            InputStreamReader ipsr =new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);

            String ligne;
            int compt = 0;

            while((ligne=br.readLine())!=null){
                String[] infos = ligne.split("__.__");
                place[compt]=ligne;
                if(score_final<Double.parseDouble(infos[1])){
                    is_not_best = false;
                    if(classement==-1)classement = compt;
                }
                compt++;
            }
            br.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
        } catch (IOException ex1) {
            System.out.println(ex1.toString());
        }

        if(!is_not_best){
            writeBestScore(classement, place);
        }
    }

    public void writeBestScore(int classement, String[] place){
        double score_final = ms*0.1;
        BigDecimal bd = new BigDecimal(score_final);
        bd= bd.setScale(3,BigDecimal.ROUND_DOWN);
        score_final = bd.doubleValue();

        if(nom!=null){
            PrintWriter pw = null;
            try{
                File file = new File("./src/Leaderboards/Leaderboard"+dimensions+".txt");
                pw = new PrintWriter(new BufferedWriter(new FileWriter(file, false)));
                switch (classement){
                    case 0:
                        pw.println(nom+"__.__"+score_final);
                        pw.println(place[0]);
                        pw.println(place[1]);
                        break;
                    case 1:
                        pw.println(place[0]);
                        pw.println(nom+"__.__"+score_final);
                        pw.println(place[1]);
                        break;
                    case 2:
                        pw.println(place[0]);
                        pw.println(place[1]);
                        pw.println(nom+"__.__"+score_final);
                        break;
                    default:
                        return;
                }
            } catch(IOException ioe){
                ioe.getMessage();
            } finally {
                if(pw !=null)pw.close();
            }
        }
    }

    public boolean bord_grille(int id1, int id2){
        for(int i=1; i<=dimensions;i++)if(id1 == dimensions*i && id2==((dimensions*i)+1))return true;
        for(int i=1; i<=dimensions;i++) if(id2 == dimensions*i && id1==((dimensions*i)+1))return true;
        return false;
    }

    public String[] getScore(int taille){
        String joueurs[] = new String[3];
        String msg = "";
        //lecture du fichier de meilleurs scores);
        try {
            InputStream ips = new FileInputStream("./src/Leaderboards/Leaderboard" + taille + ".txt");
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            String ligne;
            int compt = 0;
            while ((ligne = br.readLine()) != null) {
                String[] infos = ligne.split("__.__");
                if(infos.length>=2){
                    joueurs[compt] = infos[0]+": "+infos[1]+" sc";
                } else {
                    joueurs[compt] = "Inconnu: XX.X sc";
                }
                compt++;
            }
            br.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
        } catch (IOException ex1) {
            System.out.println(ex1.toString());
        }
        return joueurs;
    }
}
