package application;
	
import javafx.application.Application;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
	 
	public class SavingsCalculator extends Application {
	    
	    @Override
	    public void start(Stage window) {
	        BorderPane layout = new BorderPane();
	        DecimalFormat df = new DecimalFormat("###.#");
	        
	        NumberAxis xAxis = new NumberAxis(0, 30, 1);
	        xAxis.setLabel("Years");
	        NumberAxis yAxis = new NumberAxis();
	        yAxis.setLabel("Dollars ($)");
	        LineChart<Number, Number> lineChart = new LineChart(xAxis, yAxis);
	        
	        BorderPane monthlySavings = new BorderPane();
	        
	        Label savingsText = new Label("Monthly savings ($)  ");
	        Slider savingsSlider = new Slider(25, 250, 25);
	        
	        savingsSlider.setShowTickLabels(true);
	        savingsSlider.setShowTickMarks(true);
	        
	        BorderPane interestRate = new BorderPane();
	        Label interestText = new Label("Yearly interest rate (%)  ");
	        
	        Slider interestSlider = new Slider(0, 10, 0);
	        interestSlider.setShowTickLabels(true);
	        interestSlider.setShowTickMarks(true);
	        interestSlider.setMajorTickUnit(1.0);
	        
	        Label savingsReading = new Label(String.valueOf(savingsSlider.getValue()));
	        savingsSlider.valueProperty().addListener((change, oldValue, newValue) -> {
	            int savings = newValue.intValue();
	            savingsReading.setText("  " + savings);
	            
	            LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
	            HashMap<Integer, Double> savingsData = createSavingsData(newValue.doubleValue());
	            HashMap<Integer, Double> interestData = createInterestData(newValue.doubleValue(), interestSlider.getValue());
	            XYChart.Series interestDataToInput = inputData(interestData);
	            XYChart.Series savingsDataToInput = inputData(savingsData);
	            
		        savingsDataToInput.setName("Savings Without Interest");
		        interestDataToInput.setName("Savings Including Interest");
		        chart.setTitle("Savings Calculator");
		        
	            chart.getData().add(savingsDataToInput);
	            chart.getData().add(interestDataToInput);
	            layout.setCenter(chart);
	        });
	        monthlySavings.setLeft(savingsText);
	        monthlySavings.setCenter(savingsSlider);
	        monthlySavings.setRight(savingsReading);
	        
	        Label interestReading = new Label(String.valueOf(interestSlider.getValue()) + "%");
	        interestSlider.valueProperty().addListener((change, oldValue, newValue) -> {
	           interestReading.setText("  " + df.format(newValue) + "%"); 
	           
	           LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
	           
	           HashMap<Integer, Double> interestData = createInterestData(savingsSlider.getValue(), newValue.doubleValue());
	           XYChart.Series interestDataToInput = inputData(interestData);
	           
	           HashMap<Integer, Double> savingsData = createSavingsData(savingsSlider.getValue());
	           XYChart.Series savingsDataToInput = inputData(savingsData);
	           
	           savingsDataToInput.setName("Savings Without Interest");
	           interestDataToInput.setName("Savings Including Interest");
	           
	           chart.getData().add(savingsDataToInput);
	           chart.getData().add(interestDataToInput);
		       chart.setTitle("Savings Calculator");
	           layout.setCenter(chart);
	        });
	        interestRate.setLeft(interestText);
	        interestRate.setCenter(interestSlider);
	        interestRate.setRight(interestReading);
	        
	        VBox topLayout = new VBox();
	        topLayout.getChildren().addAll(monthlySavings, interestRate);
	        
	        layout.setTop(topLayout);
	        HashMap<Integer, Double> data = createSavingsData(savingsSlider.getValue());
	        XYChart.Series toInput = inputData(data);
	        toInput.setName("Savings Without Interest");
	        lineChart.getData().add(toInput);
	        lineChart.setTitle("Savings Calculator");
	        layout.setCenter(lineChart);
	        Scene view = new Scene(layout, 320, 300);
	        window.setScene(view);
	        window.show();
	    }
	    
	    
	    private HashMap<Integer, Double> createSavingsData(Double savings) {
	        HashMap<Integer, Double> data = new HashMap<>();
	        Double savingsPerYear = savings * 12;
	        for (int i = 0; i < 31; i++) {
	            data.put(i, savingsPerYear * i);
	        }
	        return data;
	    }
	    
	    private XYChart.Series inputData(HashMap<Integer, Double> data) {
	        XYChart.Series coordinateData = new XYChart.Series();
	        data.keySet().stream().forEach(key -> {
	            coordinateData.getData().add(new XYChart.Data(key, data.get(key)));
	        });
	        return coordinateData;
	    }
	    
	    private HashMap<Integer, Double> createInterestData(Double savings, Double interestPercent) {
	        HashMap<Integer, Double> data = new HashMap<>();
	        Double interestRate = interestPercent / 100;
	        Double savingsPerYear = savings * 12;
	        double amount = 0.0;
	        data.put(0, 0.0);
	        if (interestRate > 0 ) {
	            for (int i = 1; i < 31; i++) {
	                amount = amount + savingsPerYear * (Math.pow((1.0 + interestRate), i));
	                data.put(i, amount);
	            }
	        }
	        return data;
	    }
	
	public static void main(String[] args) {
		launch(args);
	}
}
