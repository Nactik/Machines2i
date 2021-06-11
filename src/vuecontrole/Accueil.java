package vuecontrole;

import instance.Instance;
import io.InstanceReader;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.FileChooserUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.InstanceReader;
import io.SolutionReader;
import io.SolutionWriter;
import io.exception.ReaderException;
import solution.Solution;
import solveur.Triviale;

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
    private JButton buttonResoudre;
    private JPanel affichageSolution;
    private JPanel affichageInstance;
    private JPanel buttonRésoudre;
    private JButton instanceSolutionShow;
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
    private JLabel solutionDataset;
    private JLabel solutionTruckDist;
    private JLabel solutionNumberOfTruckDays;
    private JLabel solutionNumberOfTruckUsed;
    private JLabel solutionTechDist;
    private JLabel solutionNumberOfTechDays;
    private JLabel solutionNumberOfTechUsed;
    private JLabel solutionIdle;
    private JLabel solutionTotalCost;
    private JLabel solutionNameLabel;
    private JLabel solutionSolveur;
    private JLabel instanceSolutionName;
    private JLabel instanceSolutionCost;
    private JLabel instanceSolutionTech;
    private JLabel instanceSolutionTruck;
    private JLabel instanceSolutionMethode;
    private JLabel LabelBestSolution;
    private JButton choisirDossierInstanceButton;
    private JButton choisirDossierSolutionButton;
    private JLabel textPaneInstanceDirectory;
    private JLabel textPaneSolutionDirectory;
    private JButton seeSolutionDetails;
    private String currentMenu;

    private final String backWhite = "#FFFFFF";
    private final String backGrey = "#D0C6C6";
    private final String tomatoe = "#FF6347";

    private String currentInstanceDirectory;
    private String currentSolutionDirectory;
    private String currentSelectedInstance;
    private String currentSelectedSolution;

    public Accueil() {
        currentMenu = "";
        currentInstanceDirectory = "instancesProg";
        currentSolutionDirectory = "solution";
        currentSelectedInstance = "undefined";
        currentSelectedSolution = "undefined";

        initWindow();

        this.solutionsButton.addActionListener(this);
        this.instancesButton.addActionListener(this);
        this.parametresButton.addActionListener(this);

        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                currentSelectedInstance = (String) list1.getSelectedValue();
                fillInstance(currentSelectedInstance);
            }
        });
        listSolution.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                currentSelectedSolution = (String) listSolution.getSelectedValue();
                fillSolution(currentSelectedSolution);
            }
        });
        buttonResoudre.addActionListener(this);
        instanceSolutionShow.addActionListener(this);
        choisirDossierSolutionButton.addActionListener(this);
        choisirDossierInstanceButton.addActionListener(this);
        seeSolutionDetails.addActionListener(this);
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
                paramInit();
                break;
            default:
                break;
        }
        currentMenu = clickedMenu;
    }
    private void instanceInit(){
        String[] pathnames = getAllInstanceFromDirectory();

        DefaultListModel<String> model = new DefaultListModel<>();
        list1.setModel(model);


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
        String solutionNameFiller = null;
        String[] pathnames;
        DefaultListModel<String> model = new DefaultListModel<>();
        listSolution.setModel(model);
        File f = new File(currentSolutionDirectory);
        currentSolutionDirectory = f.getAbsolutePath();
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
        if(currentSelectedSolution != null && !currentSelectedSolution.isEmpty() && !currentSelectedSolution.equals("undefined")){
            int selected = model.indexOf(currentSelectedSolution);
            listSolution.setSelectedIndex(selected);
            solutionNameFiller = currentSelectedSolution;
        }
        fillSolution(solutionNameFiller);
    }
    private void fillInstance(String instanceName){
        affichageSolution.setVisible(false);
        afficherSolution.setVisible(false);
        LabelBestSolution.setVisible(false);
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
    private void fillSolutionPreview(Solution solution,String methode) {
        affichageSolution.setVisible(true);
        afficherSolution.setVisible(true);
        LabelBestSolution.setVisible(true);

        instanceSolutionCost.setText(String.valueOf(solution.getTotalCost()));
        instanceSolutionMethode.setText(methode);
        instanceSolutionName.setText(solution.getInstance().getName());
        instanceSolutionTech.setText(String.valueOf(solution.getNumberOfTechnicianUsed()));
        instanceSolutionTruck.setText(String.valueOf(solution.getNumberOfTruckUsed()));

    }
    private void fillSolution(String solutionName){
        System.out.println("this is in fill solution");
        if (solutionName!=null) {
            System.out.println("this is in fill solution's if");
            SolutionReader reader;
            Solution solution;
            try {
                reader = new SolutionReader(currentSolutionDirectory + "/" + solutionName);
                solution = reader.readSolution();
            } catch (ReaderException e) {
                System.out.println("erreur");
                return;
            }
            String name = extractName(solutionName);
            System.out.println(" okok "+solution.getTotalCost());
            solutionNameLabel.setText("sol : "+solution.getInstance().getName());
            solutionSolveur.setText(name);
            solutionDataset.setText(solution.getInstance().getDataset());
            solutionTruckDist.setText(String.valueOf(solution.getTruckDistance()));
            solutionNumberOfTruckDays.setText(String.valueOf(solution.getNumberOfTruckDays()));
            solutionNumberOfTruckUsed.setText(String.valueOf(solution.getNumberOfTruckUsed()));
            solutionTechDist.setText(String.valueOf(solution.getTechnicianDistance()));
            solutionNumberOfTechDays.setText(String.valueOf(solution.getNumberOfTechnicianDays()));
            solutionNumberOfTechUsed.setText(String.valueOf(solution.getNumberOfTechnicianUsed()));
            solutionIdle.setText(String.valueOf(solution.getIdleMachineCost()));
            solutionTotalCost.setText(String.valueOf(solution.getTotalCost()));
        }else{
            solutionNameLabel.setText("Nom de solution");
            solutionSolveur.setText("");
            solutionDataset.setText("");
            solutionTruckDist.setText("");
            solutionNumberOfTruckDays.setText("");
            solutionNumberOfTruckUsed.setText("");
            solutionTechDist.setText("");
            solutionNumberOfTechDays.setText("");
            solutionNumberOfTechUsed.setText("");
            solutionIdle.setText("");
            solutionTotalCost.setText("");
        }

    }
    private void paramInit(){
        textPaneSolutionDirectory.setText(currentSolutionDirectory);
        textPaneInstanceDirectory.setText(currentInstanceDirectory);
    }
    private String extractName(String solutionName) {
        String solveurName = "undefined";
        String[] solutionParts = solutionName.split("-");
        if(solutionParts.length > 2){
            solveurName = solutionParts[2];
            solveurName = solveurName.replace(".txt", "");
        }
        return solveurName;
    }

    private void solveCurrentInstance() {
        InstanceReader reader;
        try {
            //reader = new InstanceReader("instances/ORTEC-early-easy/VSC2019_ORTEC_early_01_easy.txt");
            reader = new InstanceReader(currentInstanceDirectory + "/" + currentSelectedInstance);
            Instance instance =  reader.readInstance();

            Triviale triviale = new Triviale();
            Solution solution = triviale.solve(instance);

            SolutionWriter io = new SolutionWriter(solution);
            io.writeSolution();
            currentSelectedSolution = "solution-"+solution.getInstance().getName()+".txt";
            //currentSelectedSolution = "solution-"+solution.getInstance().getName()+"-"+triviale.getNom();
            fillSolutionPreview(solution,triviale.getNom());

        } catch (ReaderException ex) {
            Logger.getLogger(Instance.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void changeDirectory(String actionCommand) {
        JFileChooser chooser;
        String result;

        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle(actionCommand);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //
        // disable the "All files" option.
        //
        chooser.setAcceptAllFileFilterUsed(false);
        //
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            System.out.println("getSelectedFile() : "
                    +  chooser.getSelectedFile());
            result = chooser.getSelectedFile().getPath();
            if (actionCommand.equals("Choisir dossier solution")){
                textPaneSolutionDirectory.setText(result);
                currentSolutionDirectory = result;
            }else if(actionCommand.equals("Choisir dossier instance")){
                textPaneInstanceDirectory.setText(result);
                currentInstanceDirectory = result;
            }
        }
        else {
            System.out.println("No Selection ");
        }
    }

    private String[] getAllInstanceFromDirectory(){
        String[] pathnames;
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File f, String name) {
                return name.endsWith(".txt");
            }
        };
        File f = new File(currentInstanceDirectory);
        currentInstanceDirectory = f.getAbsolutePath();
        pathnames = f.list();
        return pathnames;
    }

    private Instance findLinkedInstance(Solution solution){

        String[] pathnames = getAllInstanceFromDirectory();
        InstanceReader instanceReader;
        Instance instance;
        for (String fileName : pathnames){
            try {
                instanceReader = new InstanceReader(currentInstanceDirectory + "/" + fileName);
                instance = instanceReader.readInstance();
            } catch (ReaderException e) {
                return null;
            }
            if (instance.getDataset().equals(solution.getInstance().getDataset())
            && instance.getName().equals(solution.getInstance().getName())){
                return instance;
            }
        }
        return null;
    }

    private void displaySolutionDetails() {
        SolutionReader solutionReader;
        Solution solution;
        try {
            solutionReader = new SolutionReader(currentSolutionDirectory + "/" + currentSelectedSolution);
            solution = solutionReader.readSolution();
        } catch (ReaderException e) {
            return ;
        }
        Instance instance = findLinkedInstance(solution);
        if (instance == null){
            JOptionPane.showMessageDialog(this, "L'instance relié n'est pas présente dans le dossier d'instance courant");
            return;
        }
        ChartSolutionDetails chartSolutionDetails = new ChartSolutionDetails(solution,instance);

    }


    public static void main(String[] args) {
        Accueil accueil = new Accueil();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "Instances":
            case "Solutions":
            case "Paramètres":
                if (!currentMenu.equals(e.getActionCommand()));
                clickMenu(e.getActionCommand());
                break;
            case "Résoudre":
                solveCurrentInstance();
                break;
            case "Voir":
                clickMenu("Solutions");
                break;
            case "Choisir dossier instance":
            case "Choisir dossier solution":
                changeDirectory(e.getActionCommand());
                break;
            case "Voir solution":
                displaySolutionDetails();
                break;
        }

    }



}
