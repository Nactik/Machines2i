package vuecontrole;

import instance.Instance;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import solution.Solution;

import javax.swing.*;

/**
 * Cette classe affiche une fenêtre avec un diagramme rond affichant la répartition des coûts d'une solution
 */
public class ChartSolutionDetails {
   private Solution solution;
   private Instance instance;

    /**
     * Constructeur par données de la classe
     * @param solution une solution à afficher
     * @param instance l'instance relié à cette solution
     */
    public ChartSolutionDetails(Solution solution, Instance instance) {
        this.solution = solution;
        this.instance = instance;

        displayPieChart();
    }

    /**
     * Fonction qui affiche le diagramme associé à la solution
     */
    private void displayPieChart() {
        DefaultPieDataset dataset= new DefaultPieDataset(); // Le dataset contiendra les différentes valeurs
        dataset.setValue("Trucks distance cost",instance.getTruckDistCost()* solution.getTruckDistance());
        dataset.setValue("trucks days cost",instance.getTruckDayCost()* solution.getNumberOfTruckDays());
        dataset.setValue("used trucks cost",instance.getTruckCost()* solution.getNumberOfTruckUsed());
        dataset.setValue("Techs distance cost",instance.getTechDistCost()* solution.getTechnicianDistance());
        dataset.setValue("Techs days cost",instance.getTechDayCost()* solution.getNumberOfTechnicianDays());
        dataset.setValue("used Techs cost",instance.getTechCost()* solution.getNumberOfTechnicianUsed());
        dataset.setValue("idle machine costs",solution.getIdleMachineCost());

        JFreeChart chart = ChartFactory.createPieChart("Costs pie chart",dataset); // L'objet du diagramme qui sera affiché

        ChartFrame frame = new ChartFrame("Costs pie chart",chart); //Une fenêtre contenant le diagramme
        frame.pack();
        frame.setVisible(true);
    }
}
