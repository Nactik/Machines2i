package vuecontrole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Accueil extends JFrame implements ActionListener {
    private JPanel panel1;
    private JPanel SidePanel;
    private JPanel MainInstance;
    private JPanel MainSolution;
    private JPanel MainPanel;
    private JButton instancesButton;
    private JButton solutionsButton;
    private JButton parametresButton;
    private JPanel Instances;
    private JPanel Solutions;
    private JPanel Paramètres;
    private String currentMenu;

    private final String backWhite = "#FFFFFF";
    private final String backGrey = "#D0C6C6";
    private final String tomatoe = "#FF6347";

    public Accueil() {
        initWindow();
        currentMenu = "";
        this.solutionsButton.addActionListener(this);
        this.instancesButton.addActionListener(this);
        this.parametresButton.addActionListener(this);

    }
    private void initWindow(){
        this.setTitle("Machines2i");
        this.setSize(900, 600);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setContentPane(panel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void clickMenu(String clickedMenu){
        switch (clickedMenu){
            case "Instances":
                Instances.setBackground(Color.decode(backWhite));
                Solutions.setBackground(Color.decode(backGrey));
                Paramètres.setBackground(Color.decode(backGrey));

                MainInstance.setVisible(true);
                MainSolution.setVisible(false);
                //MainParametre.setVisible(false);
                break;
            case "Solutions":
                Instances.setBackground(Color.decode(backGrey));
                Solutions.setBackground(Color.decode(backWhite));
                Paramètres.setBackground(Color.decode(backGrey));

                MainInstance.setVisible(false);
                MainSolution.setVisible(true);
                //MainParametre.setVisible(false);
                break;
            case "Paramètres":
                Instances.setBackground(Color.decode(backGrey));
                Solutions.setBackground(Color.decode(backGrey));
                Paramètres.setBackground(Color.decode(backWhite));

                MainInstance.setVisible(false);
                MainSolution.setVisible(false);
                //MainParametre.setVisible(true);
                break;
            default:
                break;
        }
        currentMenu = clickedMenu;
    }

    public static void main(String[] args) {
        Accueil accueil = new Accueil();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().matches("Instances|Solutions|Paramètres")
                && !currentMenu.equals(e.getActionCommand()))
            clickMenu(e.getActionCommand());
    }
}
