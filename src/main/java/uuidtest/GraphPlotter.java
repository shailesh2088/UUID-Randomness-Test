package uuidtest;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.PSource;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.function.Function2D;
import org.jfree.data.function.NormalDistributionFunction2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.*;

public class GraphPlotter extends ApplicationFrame {

	public GraphPlotter(String title, Result result, int[] values)
			throws IOException {

		super(title);
		JFreeChart xychart = ChartFactory.createXYLineChart(title,
				"Number of Files in each directory", "Number of Directories",
				createXYDataSet(values), PlotOrientation.VERTICAL, false,
				false, false);
		/** Customize plot color **/
		XYPlot plot = (XYPlot) xychart.getPlot();
		plot.setBackgroundPaint(Color.WHITE);
		plot.setOutlinePaint(Color.BLACK);
		plot.setBackgroundAlpha(0.2f);

		/** Customize renderer **/
		XYItemRenderer renderer = plot.getRenderer();
		renderer.setSeriesPaint(0, Color.BLUE);

		/** Customize x axis interval **/
		NumberAxis xAxis = new NumberAxis();
		xAxis.setTickUnit(new NumberTickUnit(1));
		xAxis.setLabel("Number of Files in each directory");
		Font font = new Font(Font.SANS_SERIF, Font.BOLD, 12);
		xAxis.setLabelFont(font);
		plot.setDomainAxis(xAxis);

		/** Add mean marker **/
		ValueMarker meanMarker = new ValueMarker(result.getMean());
		meanMarker.setPaint(Color.RED);
		meanMarker.setLabel("mean");
		plot.addDomainMarker(meanMarker);
		/** Add standard deviation markers **/
		ValueMarker negativeSD = new ValueMarker(result.getMean()
				- result.getSd());
		negativeSD.setPaint(Color.DARK_GRAY);
		negativeSD.setLabel("mean - sd");
		negativeSD.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
		ValueMarker positiveSD = new ValueMarker(result.getMean()
				+ result.getSd());
		positiveSD.setPaint(Color.DARK_GRAY);
		positiveSD.setLabel("mean + sd");
		plot.addDomainMarker(negativeSD);
		plot.addDomainMarker(positiveSD);

		ValueMarker negative2SD = new ValueMarker(result.getMean()
				- result.getSd() * 2);
		negative2SD.setPaint(Color.BLACK);
		negative2SD.setLabel("mean - 2sd");
		negative2SD.setLabelAnchor(RectangleAnchor.TOP_RIGHT);

		ValueMarker positive2SD = new ValueMarker(result.getMean()
				+ result.getSd() * 2);
		positive2SD.setPaint(Color.BLACK);
		positive2SD.setLabel("mean + 2sd");
		positive2SD.setLabelAnchor(RectangleAnchor.TOP_RIGHT);

		plot.addDomainMarker(negative2SD);
		plot.addDomainMarker(positive2SD);

		ChartPanel chartPanel = new ChartPanel(xychart);
		setContentPane(chartPanel);
		ChartUtilities.saveChartAsJPEG(new File("results/resultGraph.jpg"),
				xychart, 1920, 1080);

	}

	private XYDataset createXYDataSet(int[] values) {
		Map<Integer, Integer> frequencyMap = new HashMap<Integer, Integer>();
		for (int val : values) {
			if (frequencyMap.containsKey(val)) {
				int freq = frequencyMap.get(val);
				frequencyMap.put(val, freq + 1);
			} else {
				frequencyMap.put(val, 1);

			}
		}
		XYSeries series = new XYSeries("Number of Directories");
		for (int key : frequencyMap.keySet()) {
			series.add(key, frequencyMap.get(key));
		}
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		return dataset;
	}
}
