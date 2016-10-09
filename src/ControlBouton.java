import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlBouton implements ActionListener{
    protected Model m;
    protected View v;

    public ControlBouton(Model m, View v){
        this.m = m;
        this.v = v;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==m.recommencer){
            m.resetScore();
            v.getContentPane().removeAll();
            v.repaint();

            m.setDimensions(m.dimensions);
            m.createButtons();
            v.createContent();
            v.setButtonsListener(v.controlButton);

            m.timer.stop();
            m.resetScore();

            v.pack();
            m.melanger();
            return;
        }

        if(m.ms==0)m.timer.start();

        String txtPremierBout = "";

        if(!m.jeuCommence) m.jeuCommence = true;

        if(m.premierClic){
            m.idPremierBout = ((JButton_pers)e.getSource()).getId();
            for(int i = 0; i<m.dimensions; i++) {
                for (int j = 0; j < m.dimensions; j++) {
                    if (m.tabBouton[j][i].getId() == ((JButton_pers)e.getSource()).getId()){
                        m.tabBouton[j][i].setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.red));
                    }
                }
            }
            m.premierClic = false;
        }
        else{
            for(int i = 0; i<m.dimensions; i++){
                for(int j = 0; j<m.dimensions; j++){
                    //premier bouton vide
                    if(m.tabBouton[j][i].getId()==m.idPremierBout){
                        if(m.idPremierBout==m.idBoutVide){
                            //Si c'est une case conjointe
                            if((((JButton_pers)e.getSource()).getId()==m.idBoutVide-m.dimensions || ((JButton_pers)e.getSource()).getId()==m.idBoutVide+m.dimensions
                                    || ((JButton_pers)e.getSource()).getId()==m.idBoutVide-1 || ((JButton_pers)e.getSource()).getId()==m.idBoutVide+1)
                                    && m.bord_grille(((JButton_pers)e.getSource()).getId(), m.idBoutVide)==false){
                                //Le bouton vide devient plein
                                m.tabBouton[j][i].setText(((JButton_pers)e.getSource()).getText());
                                //Le bouton plein devient le bouton vide
                                ((JButton_pers)e.getSource()).setText("");
                                m.idBoutVide=((JButton_pers)e.getSource()).getId();
                            }
                        }
                        //deuxième bouton vide
                        else if(((JButton_pers)e.getSource()).getId()==m.idBoutVide){
                            if((m.tabBouton[j][i].getId()==m.idBoutVide-m.dimensions || m.tabBouton[j][i].getId()==m.idBoutVide+m.dimensions
                                    || m.tabBouton[j][i].getId()==m.idBoutVide-1 || m.tabBouton[j][i].getId()==m.idBoutVide+1)
                                    && m.bord_grille(m.tabBouton[j][i].getId(), m.idBoutVide)==false){
                                // Le bouton vide devient plein
                                ((JButton_pers)e.getSource()).setText(m.tabBouton[j][i].getText());
                                //Le premier bouton devient le bouton vide
                                m.tabBouton[j][i].setText("");
                                m.idBoutVide=m.tabBouton[j][i].getId();
                            }
                        }
                        // sortie de boucle
                        j=m.dimensions;
                        i=m.dimensions;
                    }
                }
            }
            for(int i = 0; i<m.dimensions; i++) {
                for (int j = 0; j < m.dimensions; j++) {
                    if (m.tabBouton[j][i].getId() == m.idPremierBout){
                        m.tabBouton[j][i].setBorder(BorderFactory.createLineBorder(Color.white));
                    }
                }
            }
            m.premierClic = true;

            if(m.isSolved()){
                m.jeuCommence=false;
                m.timer.stop();
                v.createInput();
                m.checkLeaderboards(m.ms*0.1);
            }
        }
    }
}
