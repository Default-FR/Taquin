import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class ControlMenu implements ActionListener {
    protected Model m;
    protected View v;

    public ControlMenu(Model m, View v){
        this.m = m;
        this.v = v;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==m.accueil){
            newgame(m.dimensions);
        }
        // Modes de jeu
        else if(e.getSource()==m.alea){
            int random = (int) (Math.random() * 3);
            switch (random){
                case 0:
                    if(m.dimensions==3)newgame(4);
                    else newgame(3);
                    break;
                case 1:
                    if(m.dimensions==4)newgame(5);
                    else newgame(4);
                    break;
                case 2:
                    if(m.dimensions==5)newgame(3);
                    else newgame(5);
                    break;
            }
        }
        else if(e.getSource()==m.grid1){
            newgame(3);
        }
        else if(e.getSource()==m.grid2){
            newgame(4);
        }
        else if(e.getSource()==m.grid3){
            newgame(5);
        }

        // Infos
        else if(e.getSource()==m.option_scores){
            String msg = "\n";
            for(int i=3; i<6; i++){
                msg += "Score grille: "+i+"x"+i+"\n";
                for (int k=0; k<3; k++){
                    msg += "      "+m.getScore(i)[k]+"\n";
                }
                msg += "\n";
            }
            v.createDialog("Meilleurs scores", msg);
        }
        else if(e.getSource()==m.option_rules){
            String msg = "Le joueur doit ordonner (ordre croissant) le plus vite possible les cellules en les déplaçant dans la cellule vide\n"
                    + "Dés le début de la partie, un chronomètre est activé afin de mesurer le temps pris par le joueur pour ordonner la grille\n"
                    + "La partie se termine si le joueur réussit à trier les cellules de la grille en ordre croissant";
            v.createDialog("Règles", msg);
        }
        else if(e.getSource()==m.option_dev){
            String msg = "Développé par Pierre Bouillon\nIUT Informatique de Belfort-Montbelliard\nAnnées 2015-2016\n\nImage de fond:\n" +
                    " http://img0.mxstatic.com/wallpapers/ed187121ab66577b5d4617cac6dc7732_large.png\n\nIcone utilisée:\n https://www.onlinewebfonts.com/icon/65577";
            v.createDialog("Crédits", msg);
        }
        else if(e.getSource()==m.quit){
            System.exit(0);
        }
    }

    public void newgame(int dim){
        m.resetScore();
        v.getContentPane().removeAll();
        v.repaint();

        m.setDimensions(dim);
        m.createButtons();
        v.createContent();
        v.setButtonsListener(v.controlButton);

        m.timer.stop();
        m.resetScore();

        v.pack();
    }
}
