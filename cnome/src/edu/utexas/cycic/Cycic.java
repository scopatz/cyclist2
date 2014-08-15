package edu.utexas.cycic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Stream;

import edu.utah.sci.cyclist.core.ui.components.ViewBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Window;


public class Cycic extends ViewBase{
	/**
	 * Function for building the CYCIC Pane and GridPane of this view. 
	 */
	public Cycic(){
		super();
		/*String string;
		for(int i = 0; i < XMLReader.test_string.size(); i++){
			StringBuilder sb = new StringBuilder();
			StringBuilder sb1 = new StringBuilder();
			Process proc;
			try {
				proc = Runtime.getRuntime().exec("cyclus --agent-schema "+XMLReader.test_string.get(i)); 
				BufferedReader read = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				while((string = read.readLine()) != null){
					sb.append(string);
				}
				Process proc1 = Runtime.getRuntime().exec("cyclus --agent-annotations "+XMLReader.test_string.get(i));
				BufferedReader read1 = new BufferedReader(new InputStreamReader(proc1.getInputStream()));
				while((string = read1.readLine()) != null){
					sb1.append(string);
				}
				//System.out.println(sb);
				//XMLReader.readSchema(sb.toString());
				facilityStructure test = new facilityStructure();
				test.facilityName = XMLReader.test_string.get(i);
				test.facStruct = XMLReader.annotationReader(sb1.toString(), XMLReader.readSchema(sb.toString()));
				DataArrays.simFacilities.add(test);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*try {
			//String string;
			//String string1;
			//Process readproc = Runtime.getRuntime().exec("cyclus -a");
			//BufferedReader schema = new BufferedReader(new InputStreamReader(readproc.getInputStream()));
			while(schema.readLine() != null){
				System.out.println(schema.readLine());
				//Process proc = Runtime.getRuntime().exec("echo cyclus");
				/*Process proc = Runtime.getRuntime().exec("cyclus --agent-schema "+string1);
				BufferedReader read = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				while((string = read.readLine()) != null){
					sb.append(string);
				}
				Process proc1 = Runtime.getRuntime().exec("cyclus --agent-annotations "+string1);
				BufferedReader read1 = new BufferedReader(new InputStreamReader(proc1.getInputStream()));
				while((string = read1.readLine()) != null){
					sb1.append(string);
				}
			}
			//System.out.println(sb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		if (CycicScenarios.cycicScenarios.size() < 1){
			DataArrays scenario = new DataArrays();
			workingScenario = scenario;
			CycicScenarios.cycicScenarios.add(scenario);
		}
		init();
	}
	public static final String TITLE = "Cycic";
	static Pane pane = new Pane(){
		{
			
		}
	};
	static facilityNode workingNode = null;
	static MarketCircle workingMarket = null;
	static DataArrays workingScenario;
	static ToggleGroup group = new ToggleGroup();
	static ToggleButton toggle = new ToggleButton("RANDOM TEXT"){
		{
			setToggleGroup(group);
		}
	};
	static boolean marketHideBool = true;
	static Window window;
	
	
	/**
	 * Initiates the Pane and GridPane.
	 */
	private void init(){	
		setTitle(TITLE);
		setOnMousePressed(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e){
				CycicScenarios.workingCycicScenario = workingScenario;
			}
		});
		if (DataArrays.simFacilities.size() < 1){
			RealFacs.init();
		}
		
		VBox cycicBox = new VBox();
		cycicBox.autosize();
		Cycic.pane.autosize();
		Cycic.pane.setId("cycicPane");
		Cycic.pane.setPrefSize(1000, 600);
		Cycic.pane.setStyle("-fx-background-color: white;");
		
		// Temp Toolbar //
		final GridPane grid = new GridPane();
		grid.setStyle("-fx-background-color: #d6d6d6;");
		grid.setHgap(10);
		grid.setVgap(5);
		
		// Adding a new Facility //
		Text scenetitle1 = new Text("Add Facility");
		scenetitle1.setFont(new Font(20));
		grid.add(scenetitle1, 0, 0);
		Label facName = new Label("Name");
		grid.add(facName, 1, 0);
		// Name Field
		final TextField facNameField = new TextField();

		grid.add(facNameField, 2, 0);
		// Facility Type
		final ComboBox<String> structureCB = new ComboBox<String>();
		for(int i = 0; i < DataArrays.simFacilities.size(); i++){
			structureCB.getItems().add((String) DataArrays.simFacilities.get(i).facilityName);	
		}
		structureCB.valueProperty().addListener(new ChangeListener<String>(){
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
				structureCB.setValue(newValue);
			}
		});
		structureCB.setPromptText("Select Facility Type");
		grid.add(structureCB, 3, 0);
		//Submit Button
		Button submit1 = new Button("Add");
		submit1.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
				if (structureCB.getValue() == null){
					return;
				}
				facilityNode tempNode = new facilityNode();
				tempNode.facilityType = structureCB.getValue();
				for (int i = 0; i < DataArrays.simFacilities.size(); i++){
					if (DataArrays.simFacilities.get(i).facilityName == structureCB.getValue()){
						tempNode.facilityStructure = DataArrays.simFacilities.get(i).facStruct;
					}				
				}
				tempNode.name = facNameField.getText();
				tempNode.cycicCircle = CycicCircles.addNode(facNameField.getText(), tempNode);
				tempNode.sorterCircle = SorterCircles.addNode(facNameField.getText(), tempNode, tempNode);
				FormBuilderFunctions.formArrayBuilder(tempNode.facilityStructure, tempNode.facilityData);
			}
		});
		grid.add(submit1, 4, 0);
		/*Text text_sting = new Text();
		text_sting.setText(System.getenv("PATH").toString());
		grid.add(text_sting, 5, 0);*/
		
		// Adding a new Market
		/*Text scenetitle2 = new Text("Market");
		scenetitle2.setFont(new Font(20));
		grid.add(scenetitle2, 0, 1);
		Label markName = new Label("Name");
		grid.add(markName, 1, 1);
		// Name Field
		final TextField markNameField = new TextField();
		grid.add(markNameField, 2, 1);
		Button submit2 = new Button("Add");
		submit2.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
				MarketNodes.addMarket(markNameField.getText());
				Cycic.workingMarket = workingScenario.marketNodes.get(workingScenario.marketNodes.size() - 1);
			}
		});
		grid.add(submit2, 3, 1);
		pane.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event){
				for(int i = 0; i < pane.getChildren().size(); i ++){
					if(event.getButton().equals(MouseButton.PRIMARY)){
						if(pane.getChildren().get(i).getStyleClass().toString() == "menu-bar"){
							pane.getChildren().get(i).setVisible(false);
						}
					}
				}			
			}
		});*/
		
		
		/*Button hideMarkets = new Button();
		hideMarkets.setText("Hide Markets");
		hideMarkets.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event){
				if (Cycic.marketHideBool == true){
					VisFunctions.marketHide();
					marketHideBool = false;
				} else {
					VisFunctions.reloadPane();
					marketHideBool = true;
				}
			}
		});
		grid.add(hideMarkets, 0, 3);*/
		//grid.add(toggle, 0, 3);
		
		
		/*ScrollPane scroll = new ScrollPane();
		GridPane grid2 = new GridPane();
		grid2.setHgap(15);
		grid2.setPadding(new Insets(10, 0, 0, 0));
		for(int i = 0; i < DataArrays.simFacilities.size(); i++){
			Circle circle = new Circle();
			circle.setRadius(40);
			circle.setFill(Color.RED);
			circle.setOnDragDetected(new EventHandler<MouseEvent>(){
				public void handle(MouseEvent e){
					
				}
			});
			grid2.add(circle, i, 0);
		}
		scroll.setContent(grid2);
		scroll.autosize();
		
		cycicBox.getChildren().addAll(grid, scroll, pane);*/
		cycicBox.getChildren().addAll(grid, pane);
		setContent(cycicBox);
	}
}