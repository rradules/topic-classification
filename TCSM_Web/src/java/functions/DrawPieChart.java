/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import controller.MainController;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import model.Category;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class DrawPieChart {

    private final String[] topics = {"Activism", "Business and finance", "Art", "Travel",
        "Gastronomy", "Literature", "Fashion", "Personal journal", "Politics", "Religion and spirituality"};
    double[] result;
    HashMap<Integer, Double> scores;

    public DrawPieChart(double[] result) {
        this.result = result;
    }

    public DrawPieChart(HashMap<Integer, Double> scores) {
        this.scores = scores;
    }

    public DrawPieChart() {
    }

    public String createChartWeka() {
        String fileName = "piechart.png";
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (int i = 0; i < result.length; i++) {
            if (result[i] != 0.0) {
                dataset.setValue(topics[i], result[i]);
            }
        }
        JFreeChart chart = ChartFactory.createPieChart(
                "Categories scoring", // chart title
                dataset, // data
                true, // include legend
                true,
                false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(
                new StandardPieSectionLabelGenerator("{0}: {2}"));
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            ServletContext servletContext = (ServletContext) externalContext.getContext();
            String absoluteDiskPath = servletContext.getRealPath("/images");

            File file = new File(absoluteDiskPath + "/" + fileName);
            ChartUtilities.saveChartAsPNG(file, chart, 800, 600);
        } catch (IOException ex) {
            Logger.getLogger(DrawPieChart.class.getName()).log(Level.SEVERE, null, ex);
        }

        return fileName;
    }

    public String createChartTFIDF() {
        String fileName = "piechart.png";

        DefaultPieDataset dataset = new DefaultPieDataset();
        Iterator it = scores.keySet().iterator();
        String category = "";

        while (it.hasNext()) {
            int categ = (Integer) it.next();
            double val = scores.get(categ);
            if (val != 0.0) {
                category = MainController.getInstance().findCategoryById(categ).getCategory();
                dataset.setValue(category, val);
            }
        }
        JFreeChart chart = ChartFactory.createPieChart(
                "Categories scoring", // chart title
                dataset, // data
                true, // include legend
                true,
                false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(
                new StandardPieSectionLabelGenerator("{0}: {2}"));
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            ServletContext servletContext = (ServletContext) externalContext.getContext();
            String absoluteDiskPath = servletContext.getRealPath("/images");
            File file = new File(absoluteDiskPath + "/" + fileName);

            ChartUtilities.saveChartAsPNG(file, chart, 800, 600);
        } catch (IOException ex) {
            Logger.getLogger(DrawPieChart.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fileName;
    }

    public String createGeneralChart() {

        String fileName = "generalchart.png";
        DefaultPieDataset dataset = new DefaultPieDataset();

        List<Category> categories = MainController.getInstance().getAllCategories();

        for (Category cat : categories) {
            int count = MainController.getInstance().findDomainByCategory(cat).size();
            dataset.setValue(cat.getCategory(), count);
        }


        JFreeChart chart = ChartFactory.createPieChart(
                "Domain distribution over categories", // chart title
                dataset, // data
                true, // include legend
                true,
                false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(
                new StandardPieSectionLabelGenerator("{0}: {2}"));
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            ServletContext servletContext = (ServletContext) externalContext.getContext();
            String absoluteDiskPath = servletContext.getRealPath("/images");

            File file = new File(absoluteDiskPath + "/" + fileName);
            ChartUtilities.saveChartAsPNG(file, chart, 800, 600);
        } catch (IOException ex) {
            Logger.getLogger(DrawPieChart.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fileName;
    }
}
