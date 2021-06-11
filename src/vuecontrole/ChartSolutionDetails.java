package vuecontrole;

import instance.Instance;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import solution.Solution;

import javax.swing.*;

public class ChartSolutionDetails {
   private Solution solution;
   private Instance instance;

    public ChartSolutionDetails(Solution solution, Instance instance) {
        this.solution = solution;
        this.instance = instance;

        displayPieChart();
    }

    private void displayPieChart() {
        DefaultPieDataset dataset= new DefaultPieDataset();
        dataset.setValue("Trucks distance cost",instance.getTruckDistCost()* solution.getTruckDistance());
        dataset.setValue("trucks days cost",instance.getTruckDayCost()* solution.getNumberOfTruckDays());
        dataset.setValue("used trucks cost",instance.getTruckCost()* solution.getNumberOfTruckUsed());
        dataset.setValue("Techs distance cost",instance.getTechDistCost()* solution.getTechnicianDistance());
        dataset.setValue("Techs days cost",instance.getTechDayCost()* solution.getNumberOfTechnicianDays());
        dataset.setValue("used Techs cost",instance.getTechCost()* solution.getNumberOfTechnicianUsed());
        dataset.setValue("idle machine costs",solution.getIdleMachineCost());

        JFreeChart chart = ChartFactory.createPieChart("Costs pie chart",dataset);

        ChartFrame frame = new ChartFrame("Costs pie chart",chart);
        frame.pack();
        frame.setVisible(true);
    }
}
