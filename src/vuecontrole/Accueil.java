package vuecontrole;

import instance.Instance;
import io.InstanceReader;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;

import io.InstanceReader;
import io.exception.ReaderException;

public class Accueil extends JFrame implements ActionListener {
    private JPanel panel1;
    private JPanel SidePanel;
    private JPanel mainInstance;
    private JPanel mainSolution;
    private JPanel MainPanel;
    private JButton instancesButton;
    private JButton solutionsButton;
    private JButton parametresButton;
    private JPanel Instances;
    private JPanel Solutions;
    private JPanel Paramètres;
    private JPanel mainParametre;
    private JPanel panelListe;
    private JPanel panelInfo;
    private JScrollPane listPanel;
    private JList list1;
    private JLabel instanceNameLabel;
    private JButton button1;
    private JPanel affichageSolution;
    private JPanel affichageInstance;
    private JPanel buttonRésoudre;
    private JButton button2;
    private JPanel afficherSolution;
    private JLabel instanceDataset;
    private JLabel instanceDays;
    private JLabel instanceCapacity;
    private JLabel instanceMaxDist;
    private JLabel instanceTruckDistCost;
    private JLabel instanceTruckDayCost;
    private JLabel instanceTruckCost;
    private JLabel instanceTechDistCost;
    private JLabel instanceTechDayCost;
    private JLabel instanceTechCost;
    private JLabel instanceNbRequest;
    private JLabel instanceNbMachines;
    private JLabel instanceNbTech;
    private JList listSolution;
    private String currentMenu;

    private final String backWhite = "#FFFFFF";
    private final String backGrey = "#D0C6C6";
    private final String tomatoe = "#FF6347";

    private String currentInstanceDirectory;
    private String currentSolutionDirectory;

    public Accueil() {
        currentMenu = "";
        currentInstanceDirectory = "instancesProg";
        currentSolutionDirectory = "solution";

        initWindow();

        this.solutionsButton.addActionListener(this);
        this.instancesButton.addActionListener(this);
        this.parametresButton.addActionListener(this);

        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                fillInstance((String) list1.getSelectedValue());
            }
        });
        listSolution.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                fillSolution((String) listSolution.getSelectedValue());
            }
        });
    }
    private void initWindow(){
        this.setTitle("Machines2i");
        this.setSize(900, 600);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setContentPane(panel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        mainInstance.setVisible(true);
        mainSolution.setVisible(false);
        mainParametre.setVisible(false);
        instanceInit();
    }

    private void clickMenu(String clickedMenu){
        switch (clickedMenu){
            case "Instances":
                Instances.setBackground(Color.decode(backWhite));
                Solutions.setBackground(Color.decode(backGrey));
                Paramètres.setBackground(Color.decode(backGrey));

                mainInstance.setVisible(true);
                mainSolution.setVisible(false);
                mainParametre.setVisible(false);
                instanceInit();
                break;
            case "Solutions":
                Instances.setBackground(Color.decode(backGrey));
                Solutions.setBackground(Color.decode(backWhite));
                Paramètres.setBackground(Color.decode(backGrey));

                mainInstance.setVisible(false);
                mainSolution.setVisible(true);
                mainParametre.setVisible(false);
                solutionInit();
                break;
            case "Paramètres":
                Instances.setBackground(Color.decode(backGrey));
                Solutions.setBackground(Color.decode(backGrey));
                Paramètres.setBackground(Color.decode(backWhite));

                mainInstance.setVisible(false);
                mainSolution.setVisible(false);
                mainParametre.setVisible(true);
                break;
            default:
                break;
        }
        currentMenu = clickedMenu;
    }
    private void instanceInit(){
        System.out.println("Yes");
        String[] pathnames;

        DefaultListModel<String> model = new DefaultListModel<>();
        list1.setModel(model);
        File f = new File(currentInstanceDirectory);
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File f, String name) {
                return name.endsWith(".txt");
            }
        };
        pathnames = f.list();

        list1.removeAll();
        if (pathnames!= null){
            for (String fileName: pathnames) {
                System.out.println(fileName);
                model.addElement(fileName);

            }
        }
        fillInstance(null);

    }
    private void solutionInit(){
        String[] pathnames;

        DefaultListModel<String> model = new DefaultListModel<>();
        listSolution.setModel(model);
        File f = new File(currentSolutionDirectory);
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File f, String name) {
                return name.endsWith(".txt");
            }
        };
        pathnames = f.list();

        listSolution.removeAll();
        if (pathnames!= null){
            for (String fileName: pathnames) {
                System.out.println(fileName);
                model.addElement(fileName);

            }
        }
        //fillInstance(null);
    }
    private void fillInstance(String instanceName){
        if (instanceName!=null) {
            InstanceReader reader;
            Instance instance;
            try {
                reader = new InstanceReader(currentInstanceDirectory + "/" + instanceName);
                instance = reader.readInstance();
            } catch (ReaderException e) {
                return;
            }
            instanceNameLabel.setText(instance.getName());
            instanceCapacity.setText(String.valueOf(instance.getTruckCapacity()));
            instanceDataset.setText(instance.getDataset());
            instanceDays.setText(String.valueOf(instance.getNbDay()));
            instanceMaxDist.setText(String.valueOf(instance.getDistMaxTruck()));
            instanceTechCost.setText(String.valueOf(instance.getTechCost()));
            instanceTechDayCost.setText(String.valueOf(instance.getTechDayCost()));
            instanceTechDistCost.setText(String.valueOf(instance.getTechDistCost()));
            instanceTruckCost.setText(String.valueOf(instance.getTruckCost()));
            instanceTruckDistCost.setText(String.valueOf(instance.getTruckDistCost()));
            instanceTruckDayCost.setText(String.valueOf(instance.getTruckDayCost()));
            instanceNbMachines.setText(String.valueOf(instance.getMachines().size()));
            instanceNbRequest.setText(String.valueOf(instance.getClients().size()));
            instanceNbTech.setText(String.valueOf(instance.getTechnicians().size()));
        }else{
            instanceNameLabel.setText("Nom d'instance");
            instanceCapacity.setText("");
            instanceDataset.setText("");
            instanceDays.setText("");
            instanceMaxDist.setText("");
            instanceTechCost.setText("");
            instanceTechDayCost.setText("");
            instanceTechDistCost.setText("");
            instanceTruckCost.setText("");
            instanceTruckDistCost.setText("");
            instanceTruckDayCost.setText("");
            instanceNbMachines.setText("");
            instanceNbRequest.setText("");
            instanceNbTech.setText("");
        }

    }
    private void fillSolution(String solutionName){
        /**if (instanceName!=null) {
            InstanceReader reader;
            Instance instance;
            try {
                reader = new InstanceReader(currentInstanceDirectory + "/" + instanceName);
                instance = reader.readInstance();
            } catch (ReaderException e) {
                return;
            }
            instanceNameLabel.setText(instance.getName());
            instanceCapacity.setText(String.valueOf(instance.getTruckCapacity()));
            instanceDataset.setText(instance.getDataset());
            instanceDays.setText(String.valueOf(instance.getNbDay()));
            instanceMaxDist.setText(String.valueOf(instance.getDistMaxTruck()));
            instanceTechCost.setText(String.valueOf(instance.getTechCost()));
            instanceTechDayCost.setText(String.valueOf(instance.getTechDayCost()));
            instanceTechDistCost.setText(String.valueOf(instance.getTechDistCost()));
            instanceTruckCost.setText(String.valueOf(instance.getTruckCost()));
            instanceTruckDistCost.setText(String.valueOf(instance.getTruckDistCost()));
            instanceTruckDayCost.setText(String.valueOf(instance.getTruckDayCost()));
            instanceNbMachines.setText(String.valueOf(instance.getMachines().size()));
            instanceNbRequest.setText(String.valueOf(instance.getClients().size()));
            instanceNbTech.setText(String.valueOf(instance.getTechnicians().size()));
        }else{
            instanceNameLabel.setText("Nom d'instance");
            instanceCapacity.setText("");
            instanceDataset.setText("");
            instanceDays.setText("");
            instanceMaxDist.setText("");
            instanceTechCost.setText("");
            instanceTechDayCost.setText("");
            instanceTechDistCost.setText("");
            instanceTruckCost.setText("");
            instanceTruckDistCost.setText("");
            instanceTruckDayCost.setText("");
            instanceNbMachines.setText("");
            instanceNbRequest.setText("");
            instanceNbTech.setText("");
        }

    }
    public static void main(String[] args) {
        Accueil accueil = new Accueil();*/
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().matches("Instances|Solutions|Paramètres")
                && !currentMenu.equals(e.getActionCommand()))
            clickMenu(e.getActionCommand());
    }
}
