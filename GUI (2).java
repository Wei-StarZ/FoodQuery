package application;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
//import com.sun.glass.ui.CommonDialogs.ExtensionFilter;

import application.GUI.XCell;
import javafx.geometry.Insets;

/**
 * Filename:   Main.java
 * Project:    Project milestone2 
 * Authors:    Liule Yang 001, Wei Xia 003, August Chang 001, Xiaoshan Li 001
 *
 * Semester:   Fall 2018
 * Course:     CS400
 *
 * Due Date:   Nov 30 by 10pm
 * Version:    1.0
 *
 * Credits:    https://stackoverflow.com/questions/42529782/listview-with-delete-button-on-every-row-in-javafx
 *
 * Bugs:       None
 */

/**
 * This class defines functionalities of the "Food Query" program
 * 
 * @author D-team 11
 *
 */
public class GUI extends Application {

	static ObservableList<String> names = FXCollections.observableArrayList();// list of food names
	static ObservableList<String> meals = FXCollections.observableArrayList();// list of food names in the meal
	static ObservableList<String> rules = FXCollections.observableArrayList();// list of rules
	@SuppressWarnings("rawtypes")
	public static final ObservableList nutritions = FXCollections.observableArrayList();
	final static String[] nutrients = { "calories", "carbs", "fat", "protein", "fiber" }; // list of nutrients
	
	private FoodData foodData= new FoodData();
	private FoodData mealListData = new FoodData();
	private List<FoodItem> foodDataList = new ArrayList<FoodItem>();
	
	private ArrayList<String> filterByNutrient = new ArrayList<>();
	private ArrayList<String> filterByName = new ArrayList<>();

	/**
	 * This method contains all the functionalities of GUI
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("MealBuilder1.0");
        //create the load file button and set its position, size and styles
		Button loadButton = new Button("Load File"); 
		loadButton.setPrefWidth(120);
		loadButton.setPrefHeight(50);
		loadButton.setLayoutX(0);
		loadButton.setLayoutY(0);
		loadButton.setOnAction(new EventHandler<ActionEvent>() {

			/**
			 * Creates the popup window and defines its functionalities
			 */
			@Override
			public void handle(ActionEvent event) {
				
				//creates the popup window
				final Stage Stage = new Stage();
				Stage.setTitle("Load File");
				Stage.initModality(Modality.APPLICATION_MODAL);
				Stage.initOwner(primaryStage);
				final FileChooser fileChooser = new FileChooser();

				Label ins1 = new Label("Search file from computer");
				ins1.setPrefSize(200, 10);
				ins1.setLayoutX(20);
				ins1.setLayoutY(0);
				
				Label ins2 = new Label("Enter file path or name by yourself");
				ins2.setPrefSize(200, 10);
				ins2.setLayoutX(10);
				ins2.setLayoutY(230);
				
				//creates the open button and set its position, size and styles
				final Button openButton = new Button("Open a(.csv) File...");
//				final Button openMultipleButton = new Button("Open (.txt) Files...");
				openButton.setPrefSize(200, 30);
				openButton.setLayoutX(20);
				openButton.setLayoutY(20);
//				openMultipleButton.setPrefSize(160, 30);
//				openMultipleButton.setLayoutX(220);
//				openMultipleButton.setLayoutY(20);
                //create the buttons, label and text field, and sets their positions, size and style
				final Label fileName = new Label("File: ");
				fileName.setStyle("-fx-font: 20 arial;");
               
				final TextField filePath = new TextField();
				filePath.setPrefSize(260, 30);
				filePath.setPromptText("Enter a file path or name.");
				HBox hbox = new HBox(fileName, filePath);

				hbox.setLayoutX(10);
				hbox.setLayoutY(250);

				Button submit = new Button("Submit");
				submit.setPrefSize(80, 30);
				submit.setLayoutX(220);
				submit.setLayoutY(300);
				//defines the functionality of submit button
				submit.setOnAction(ae -> {
				    if (filePath.getText() != null) {
				        
		                try {
		                    if(!(filePath.getText().substring(filePath.getLength() - 4, filePath.getLength()).equals(".txt")
		                                    || filePath.getText().substring(filePath.getLength() - 4, filePath.getLength()).equals(".csv"))){
		                        throw new FileNotFoundException();
	                        }
		                    foodData = new FoodData();
		                    foodData.loadFoodItems(filePath.getText());
		                    if (foodData.fileNotFound()) {
	                            throw new FileNotFoundException();
	                        }
		                    names.clear();
		                    foodDataList.clear();
		                    for (int i = 0; i < foodData.getAllFoodItems().size(); i++) {
		                        names.add(foodData.getAllFoodItems().get(i).getName());
		                        foodDataList.add(foodData.getAllFoodItems().get(i));
		                    }		                    
		                    Stage.close();		                    
		                }catch (Exception e) {
		                   Alert alert = new Alert(AlertType.WARNING);
		                  alert.setHeaderText("Error");
		                  alert.setContentText("WARNING: Please input or choose correcr file!");
		                  alert.showAndWait();
		                }
				    }
										
				});
				
				Button exit = new Button("Exit");
				exit.setPrefSize(50, 30);
				exit.setLayoutX(300);
				exit.setLayoutY(300);
				//defines the functionality of exit button
				exit.setOnAction(ae -> {
					Stage.close();
				});

				/**
				 * allow user to choose files from local directory
				 */
				openButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(final ActionEvent e) {
						File file = fileChooser.showOpenDialog(Stage);
						if (file != null) {
							openFile(file);
							Stage.close();
						}
						
					}
				});
				
				/**
				 * allow user to choose files from local directory
				 */
//				openMultipleButton.setOnAction(new EventHandler<ActionEvent>() {
//					@Override
//					public void handle(final ActionEvent e) {
//						List<File> list = fileChooser.showOpenMultipleDialog(Stage);
//						if (list != null) {
//							for (File file : list) {
//								openFile(file);
//								Stage.close();
//							}
//						}
//					}
//				});
                //sets the stage positions, size and styles, and show stage
				final Pane inputGridPane = new Pane();
				inputGridPane.getChildren().addAll(openButton, exit, hbox, submit, ins1, ins2);
				final Pane rootGroup = new VBox(12);
				rootGroup.getChildren().addAll(inputGridPane);
				rootGroup.setPadding(new Insets(12, 12, 12, 12));
				Stage.setScene(new Scene(rootGroup));
				Stage.show();
			}

			/**
			 * allow user enter the file path
			 */
			private void openFile(File file) {
			
				Scanner sc = null;
				//receive the input file and sort them, if the input file is not correct, popup warning
				if (file.getName().substring(file.getName().length() - 4, file.getName().length()).contains(".txt")
				                || file.getName().substring(file.getName().length() - 4, file.getName().length()).contains(".csv")) {
				    try {
				        foodData = new FoodData();
                        foodData.loadFoodItems(file.getPath());
                        if (foodData.fileNotFound()) {
                            throw new FileNotFoundException();
                        }
                        names.clear();
                        foodDataList.clear();
                        for (int i = 0; i < foodData.getAllFoodItems().size(); i++) {
                            names.add(foodData.getAllFoodItems().get(i).getName());
                            foodDataList.add(foodData.getAllFoodItems().get(i));
                        }
                        
                    }catch (Exception e) {
                       Alert alert = new Alert(AlertType.WARNING);
                      alert.setHeaderText("Error");
                      alert.setContentText("WARNING: Please input or choose correcr file!");
                      alert.showAndWait();
                    }
				} else {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setHeaderText("Error");
					alert.setContentText("WARNING: Please input or choose correcr file!");
					alert.showAndWait();
				}
			    


			}

		});
	      Button saveFoodButton = new Button("Save Food List");
	        saveFoodButton.setPrefSize(120, 50);
	        saveFoodButton.setLayoutX(440);
	        saveFoodButton.setLayoutY(0);
	        saveFoodButton.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	                
	                //creates the popup window
	                final Stage Stage = new Stage();
	                Stage.setTitle("Save Food List to a File");
	                Stage.initModality(Modality.APPLICATION_MODAL);
	                Stage.initOwner(primaryStage);
	                final FileChooser fileChooser = new FileChooser();

	                Label ins1 = new Label("Search file from computer");
					ins1.setPrefSize(200, 10);
					ins1.setLayoutX(20);
					ins1.setLayoutY(0);
					
					Label ins2 = new Label("Enter file path or name by yourself");
					ins2.setPrefSize(200, 10);
					ins2.setLayoutX(10);
					ins2.setLayoutY(230);
	                
	                //creates the open button and set its position, size and styles
	                final Button openButton = new Button("Save to a (.csv) File...");
	                openButton.setPrefSize(200, 30);
	                openButton.setLayoutX(20);
	                openButton.setLayoutY(20);
	                //create the buttons, label and text field, and sets their positions, size and style
	                final Label fileName = new Label("File: ");
	                fileName.setStyle("-fx-font: 20 arial;");
	               
	                final TextField filePath = new TextField();
	                filePath.setPrefSize(260, 30);
	                filePath.setPromptText("Save by a file path or name.");
	                HBox hbox = new HBox(fileName, filePath);

	                hbox.setLayoutX(10);
	                hbox.setLayoutY(250);

	                Button submit = new Button("Submit");
	                submit.setPrefSize(80, 30);
	                submit.setLayoutX(220);
	                submit.setLayoutY(300);
	                //defines the functionality of submit button
	                submit.setOnAction(save -> {
	                    if (filePath.getText() != null) {
	                        
	                        try {
	                            if(!(filePath.getText().substring(filePath.getLength() - 4, filePath.getLength()).equals(".txt")
	                                            ||filePath.getText().substring(filePath.getLength() - 4, filePath.getLength()).equals(".csv"))){
	                                throw new FileNotFoundException();
	                            }
	                            
	                            FoodData saveData = new FoodData();
	                            for (int i = 0; i < foodDataList.size(); i++) {
	                                saveData.addFoodItem(foodDataList.get(i));
	                            }
	                            
	                            saveData.saveFoodItems(filePath.getText());
	                            if (saveData.fileNotFound()) {
	                                throw new FileNotFoundException();
	                            } 
	                            Stage.close();
	                                                        
	                        }catch (Exception e) {
	                           Alert alert = new Alert(AlertType.WARNING);
	                          alert.setHeaderText("Error");
	                          alert.setContentText("WARNING: Please input or choose correcr file!");
	                          alert.showAndWait();
	                        }
	                    }
	                                        
	                });
	                
	                Button exit = new Button("Exit");
	                exit.setPrefSize(50, 30);
	                exit.setLayoutX(300);
	                exit.setLayoutY(300);
	                //defines the functionality of exit button
	                exit.setOnAction(save -> {
	                    Stage.close();
	                });

	                /**
	                 * allow user to choose files from local directory
	                 */
	                openButton.setOnAction(new EventHandler<ActionEvent>() {
	                    @Override
	                    public void handle(final ActionEvent e) {
	                        File file = fileChooser.showOpenDialog(Stage);
	                        if (file != null) {
	                            saveFile(file);
	                            Stage.close();
	                        }
	                    }
	                });
	                //sets the stage positions, size and styles, and show stage
	                final Pane inputGridPane = new Pane();
	                inputGridPane.getChildren().addAll(openButton, exit, hbox, submit, ins1, ins2);
	                final Pane rootGroup = new VBox(12);
	                rootGroup.getChildren().addAll(inputGridPane);
	                rootGroup.setPadding(new Insets(12, 12, 12, 12));
	                Stage.setScene(new Scene(rootGroup));
	                Stage.show();
	            }

	            /**
	             * allow user enter the file path
	             */
	            private void saveFile(File file) {
	            
	                Scanner sc = null;
	                //receive the input file and sort them, if the input file is not correct, popup warning
	                try {
                        if(!(file.getPath().substring(file.getPath().length() - 4, file.getPath().length()).equals(".txt")
                                        ||file.getPath().substring(file.getPath().length() - 4, file.getPath().length()).equals(".csv"))){
                            System.out.println("not txt");
                            throw new FileNotFoundException();
                        }
                        
                        
                        FoodData saveData = new FoodData();
                        for (int i = 0; i < foodDataList.size(); i++) {
                            saveData.addFoodItem(foodDataList.get(i));
                        }
                        
                        saveData.saveFoodItems(file.getPath());                     
                    }catch (Exception e) {
                        
                        System.out.println("wrong");
                       Alert alert = new Alert(AlertType.WARNING);
                      alert.setHeaderText("Error");
                      alert.setContentText("WARNING: Please input or choose correcr file!");
                      alert.showAndWait();
                    }
	                


	            }

	        });
	        
	        Button instruction = new Button("instruction");
	        instruction.setPrefSize(120, 50);
	        instruction.setLayoutX(560);
	        instruction.setLayoutY(0);
	        instruction.setOnAction(new EventHandler<ActionEvent>() {
	        	@Override
	        	public void handle(ActionEvent event) {

	        		// creates the popup window
	        		final Stage Stage = new Stage();
	        		Stage.setTitle("Instruction");
	        		Stage.initModality(Modality.APPLICATION_MODAL);
	        		Stage.initOwner(primaryStage);
	        		Label ins = new Label(
	        				"1. Add food to food list by loading from a file or add the food manually"
	        				+ "\n2. You can add food item to the mealist from food list by clicking the add button next to each food \n"
	        				+ "    You can find the add button by scrolling to right if not appearing at first sight\n"
	        				+ "3. You can either save meal list or food list"
	        				+ "\n4. You can see the nutrients in your meal list by clicking \"analyze meal in meal list\""
	        				+ "\n5. Be careful! Reset button will clear all the stuff!"
	        				+ "\n6. To view, change, or apply rules, you should click \"View/Change Rules\"");

	        		
	        		final Pane inputGridPane = new Pane();
	        		inputGridPane.getChildren().addAll(ins);
	        		final Pane rootGroup = new VBox(12);
	        		rootGroup.getChildren().addAll(inputGridPane);
	        		rootGroup.setPadding(new Insets(12, 12, 12, 12));
	        		Stage.setScene(new Scene(rootGroup));
	        		Stage.show();
	        	}
	        });
	        
	        Button saveMealButton = new Button("Save Meal List");
	        saveMealButton.setPrefSize(120, 50);
	        saveMealButton.setLayoutX(320);
	        saveMealButton.setLayoutY(0);
	        saveMealButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    
                    //creates the popup window
                    final Stage Stage = new Stage();
                    Stage.setTitle("Save Food List to a File");
                    Stage.initModality(Modality.APPLICATION_MODAL);
                    Stage.initOwner(primaryStage);
                    final FileChooser fileChooser = new FileChooser();

                    Label ins1 = new Label("Search file from computer");
    				ins1.setPrefSize(200, 10);
    				ins1.setLayoutX(20);
    				ins1.setLayoutY(0);
    				
    				Label ins2 = new Label("Enter file path or name by yourself");
    				ins2.setPrefSize(200, 10);
    				ins2.setLayoutX(10);
    				ins2.setLayoutY(230);
                    
                    //creates the open button and set its position, size and styles
                    final Button openButton = new Button("Save to a (.csv) File...");
                    openButton.setPrefSize(200, 30);
                    openButton.setLayoutX(20);
                    openButton.setLayoutY(20);
                    //create the buttons, label and text field, and sets their positions, size and style
                    final Label fileName = new Label("File: ");
                    fileName.setStyle("-fx-font: 20 arial;");
                   
                    final TextField filePath = new TextField();
                    filePath.setPrefSize(260, 30);
                    filePath.setPromptText("Save by a file path or name.");
                    HBox hbox = new HBox(fileName, filePath);

                    hbox.setLayoutX(10);
                    hbox.setLayoutY(250);

                    Button submit = new Button("Submit");
                    submit.setPrefSize(80, 30);
                    submit.setLayoutX(220);
                    submit.setLayoutY(300);
                    //defines the functionality of submit button
                    submit.setOnAction(save -> {
                        if (filePath.getText() != null) {
                            
                            try {
                                if(!(filePath.getText().substring(filePath.getLength() - 4, filePath.getLength()).equals(".txt")
                                                ||filePath.getText().substring(filePath.getLength() - 4, filePath.getLength()).equals(".csv"))){
                                    throw new FileNotFoundException();
                                }
                                
                                
                                mealListData.saveFoodItems(filePath.getText());
                                if (mealListData.fileNotFound()) {
                                    throw new FileNotFoundException();
                                }  
                                Stage.close();
                                                            
                            }catch (Exception e) {
                               Alert alert = new Alert(AlertType.WARNING);
                              alert.setHeaderText("Error");
                              alert.setContentText("WARNING: Please input or choose correcr file!");
                              alert.showAndWait();
                            }
                        }
                                            
                    });
                    
                    Button exit = new Button("Exit");
                    exit.setPrefSize(50, 30);
                    exit.setLayoutX(300);
                    exit.setLayoutY(300);
                    //defines the functionality of exit button
                    exit.setOnAction(save -> {
                        Stage.close();
                    });

                    /**
                     * allow user to choose files from local directory
                     */
                    openButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(final ActionEvent e) {
                            File file = fileChooser.showOpenDialog(Stage);
                            if (file != null) {
                                saveMealFile(file);
                                Stage.close();
                            }
                        }
                    });
                    //sets the stage positions, size and styles, and show stage
                    final Pane inputGridPane = new Pane();
                    inputGridPane.getChildren().addAll(openButton, exit, hbox, submit, ins1, ins2);
                    final Pane rootGroup = new VBox(12);
                    rootGroup.getChildren().addAll(inputGridPane);
                    rootGroup.setPadding(new Insets(12, 12, 12, 12));
                    Stage.setScene(new Scene(rootGroup));
                    Stage.show();
                }

                /**
                 * allow user enter the file path
                 */
                private void saveMealFile(File file) {
                
                    Scanner sc = null;
                    //receive the input file and sort them, if the input file is not correct, popup warning
                    try {
                        if(!(file.getPath().substring(file.getPath().length() - 4, file.getPath().length()).equals(".txt")
                                        ||file.getPath().substring(file.getPath().length() - 4, file.getPath().length()).equals(".csv"))){
                            System.out.println("not txt");
                            throw new FileNotFoundException();
                        }
                        
                        
                        
                        mealListData.saveFoodItems(file.getPath());   
                        if (mealListData.fileNotFound()) {
                            throw new FileNotFoundException();
                        } 
                        
                    }catch (Exception e) {
                        
                        System.out.println("wrong");
                       Alert alert = new Alert(AlertType.WARNING);
                      alert.setHeaderText("Error");
                      alert.setContentText("WARNING: Please input or choose correcr file!");
                      alert.showAndWait();
                    }
                    


                }

            });
            
		/*
		 * create the close button, sets the positions, size and styles
		 */
		Button closeButton = new Button("X");
		closeButton.setPrefWidth(20);
		closeButton.setPrefHeight(20);
		closeButton.setLayoutX(760);
		closeButton.setLayoutY(0);
		closeButton.setStyle("background-color: #ff2222");
		
		/*
		 * creates the popup window to warn the user when clicking the button
		 */
		closeButton.setOnAction(ae -> {
			//create the stage and defines its functionality
			final Stage dialog = new Stage();
			dialog.setTitle("QUIT?");
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.initOwner(primaryStage);
			GridPane grid = new GridPane();
			grid.setPadding(new Insets(10, 10, 10, 10));
			grid.setVgap(5);
			grid.setHgap(5);
            //create label and button, and sets its positions, size and styles
			Label warning = new Label("Quit program?");
			warning.setStyle("-fx-font: 30 arial;");
			warning.setPrefWidth(200);
			warning.setPrefHeight(10);
			warning.setLayoutX(20);
			warning.setLayoutY(30);

			Button add = new Button("Yes");
			add.setPrefWidth(120);
			add.setPrefHeight(50);
			add.setLayoutX(77);
			add.setLayoutY(250);
			//defines the functionality of yes button
			add.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					primaryStage.close();
				}
			});

			Button exit = new Button("No");
			exit.setPrefSize(120, 50);
			exit.setLayoutX(200);
			exit.setLayoutY(250);
			//defines the functionality of No button
			exit.setOnAction(e -> {
				dialog.close();
			});
            //create the pane and scene, and puts the button and label into the pane
			Pane rules = new Pane();
			Scene dialogScene = new Scene(rules, 400, 350);
			rules.getChildren().add(grid);
			rules.getChildren().add(add);
			rules.getChildren().add(exit);
			rules.getChildren().add(warning);
			//show stage
			dialog.setScene(dialogScene);
			dialog.show();
		});
        //create the add button, sets its size, positions and styles
		Button addButton = new Button("Add Food Item to Food List");
		addButton.setPrefWidth(250);
		addButton.setPrefHeight(50);
		addButton.setLayoutX(0);
		addButton.setLayoutY(550);
		
		/*
		 * defines the buttons and functionalities of new window
		 */
		addButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//create stage of add food
				final Stage newWindow = new Stage();
				newWindow.setTitle("Add food to food List");
				newWindow.initModality(Modality.APPLICATION_MODAL);
				newWindow.initOwner(primaryStage);
				Pane grid = new Pane();
                //create label and set the positions and size and styles
				Label foodID = new Label();
                foodID.setText("Food ID:");
                foodID.setPrefSize(200, 20);
                foodID.setLayoutX(45);
                foodID.setLayoutY(20);
                foodID.setStyle("-fx-font: 20 arial;");
                grid.getChildren().add(foodID);
                
                final TextField enterFoodID = new TextField();
                enterFoodID.setPromptText("Enter food ID.");
                enterFoodID.setPrefColumnCount(11);
                enterFoodID.setLayoutX(205);
                enterFoodID.setLayoutY(20);
                enterFoodID.getText();
                grid.getChildren().add(enterFoodID);
                
				Label foodName = new Label();
				foodName.setText("Food name:");
				foodName.setPrefSize(200, 20);
				foodName.setLayoutX(45);
				foodName.setLayoutY(50);
				foodName.setStyle("-fx-font: 20 arial;");
				grid.getChildren().add(foodName);
                ////create the text field and set the positions, style and size of this
				final TextField enterFoodName = new TextField();
				enterFoodName.setPromptText("Enter food name.");
				enterFoodName.setPrefColumnCount(11);
				enterFoodName.setLayoutX(205);
				enterFoodName.setLayoutY(50);
				enterFoodName.getText();
				grid.getChildren().add(enterFoodName);
                //create the label and set the positions, style and size of this
				Label calories = new Label();
				calories.setText("Calory:");
				calories.setPrefSize(200, 20);
				calories.setLayoutX(45);
				calories.setLayoutY(80);
				calories.setStyle("-fx-font: 20 arial;");
				grid.getChildren().add(calories);
                //create the text field and set positions, style and size of this
				final TextField enterCalories = new TextField();
				enterCalories.setPromptText("Enter calories.");
				enterCalories.setPrefColumnCount(11);
				enterCalories.setLayoutX(205);
				enterCalories.setLayoutY(80);
				enterCalories.getText();
				grid.getChildren().add(enterCalories);
				//create the label and set the positions, style and size of this
				Label fat = new Label();
				fat.setText("Fat:");
				fat.setPrefSize(200, 20);
				fat.setLayoutX(45);
				fat.setLayoutY(110);
				fat.setStyle("-fx-font: 20 arial;");
				grid.getChildren().add(fat);
                //create the text field and set positions, style and size of this
				final TextField enterFat = new TextField();
				enterFat.setPromptText("Enter fat.");
				enterFat.setPrefColumnCount(11);
				enterFat.setLayoutX(205);
				enterFat.setLayoutY(110);
				enterFat.getText();
				grid.getChildren().add(enterFat);
				//create the label and set the positions, style and size of this
				Label carbs = new Label();
				carbs.setText("Carbohydrate:");
				carbs.setPrefSize(200, 20);
				carbs.setLayoutX(45);
				carbs.setLayoutY(140);
				carbs.setStyle("-fx-font: 20 arial;");
				grid.getChildren().add(carbs);
                //create the text field and set positions, style and size of this
				final TextField enterCarb = new TextField();
				enterCarb.setPromptText("Enter carb.");
				enterCarb.setPrefColumnCount(11);
				enterCarb.setLayoutX(205);
				enterCarb.setLayoutY(140);
				enterCarb.getText();
				grid.getChildren().add(enterCarb);
				//create the label and set the positions, style and size of this
				Label fiber = new Label();
				fiber.setText("Fiber:");
				fiber.setPrefSize(200, 20);
				fiber.setLayoutX(45);
				fiber.setLayoutY(170);
				fiber.setStyle("-fx-font: 20 arial;");
				grid.getChildren().add(fiber);
                //create the text field and set positions, style and size of this
				final TextField enterFiber = new TextField();
				enterFiber.setPromptText("Enter fiber.");
				enterFiber.setPrefColumnCount(11);
				enterFiber.setLayoutX(205);
				enterFiber.setLayoutY(170);
				enterFiber.getText();
				grid.getChildren().add(enterFiber);
				//create the label and set the positions, style and size of this
				Label protein = new Label();
				protein.setText("Protein:");
				protein.setPrefSize(200, 20);
				protein.setLayoutX(45);
				protein.setLayoutY(200);
				protein.setStyle("-fx-font: 20 arial;");
				grid.getChildren().add(protein);
                //create the text field and set positions, style and size of this
				final TextField enterProtein = new TextField();
				enterProtein.setPromptText("Enter protein.");
				enterProtein.setPrefColumnCount(11);
				enterProtein.setLayoutX(205);
				enterProtein.setLayoutY(200);
				enterProtein.getText();
				grid.getChildren().add(enterProtein);
                //create the add button and set its position and size 
				Button add = new Button("Add");
				add.setPrefWidth(120);
				add.setPrefHeight(50);
				add.setLayoutX(77);
				add.setLayoutY(250);
				add.setStyle("-fx-font: 20 arial;");
				//defines the functionality of the add button
				add.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
					    
						/*
						 * alert user if the input is invalid
						 */
						if (enterFoodID.getText().matches("[a-zA-Z0-9]+") && enterFoodName.getText().matches("[a-zA-Z0-9]+") && isDouble(enterCalories.getText())
								&& isDouble(enterFat.getText()) && isDouble(enterCarb.getText())
								&& isDouble(enterFiber.getText()) && isDouble(enterProtein.getText())){
						    
						    if(Double.parseDouble(enterCalories.getText()) < 0 
	                                        || Double.parseDouble(enterFat.getText()) < 0 ||
	                                        Double.parseDouble(enterCarb.getText()) < 0 || 
	                                        Double.parseDouble(enterFiber.getText()) < 0 ||
	                                        Double.parseDouble(enterProtein.getText()) < 0) {
						        Alert alert = new Alert(AlertType.WARNING);
	                            alert.setHeaderText("Error");
	                            alert.setContentText("Please enter correct foodName and food nutrients!");
	                            alert.showAndWait();
						    }else {
							names.add(enterFoodName.getText());
//							Collections.sort(names);
							names.sort((h1, h2) -> h1.toLowerCase().compareTo(h2.toLowerCase()));
							
							FoodItem newFood = new FoodItem(enterFoodID.getText(),enterFoodName.getText());
							newFood.addNutrient("calories", Double.parseDouble(enterCalories.getText()));
							newFood.addNutrient("fat", Double.parseDouble(enterFat.getText()));
							newFood.addNutrient("carbohydrate", Double.parseDouble(enterCarb.getText()));
							newFood.addNutrient("fiber", Double.parseDouble(enterFiber.getText()));
							newFood.addNutrient("protein", Double.parseDouble(enterProtein.getText()));
							
							foodData.addFoodItem(newFood);
							foodDataList.add(newFood);
					        Collections.sort(foodDataList, 
					                        (h1, h2) -> h1.getName().toLowerCase().compareTo(h2.getName().toLowerCase()));
						    }
						} else {
							Alert alert = new Alert(AlertType.WARNING);
							alert.setHeaderText("Error");
							alert.setContentText("Please enter correct foodName and food nutrients!");
							alert.showAndWait();
						}

					}
				});

				/*
				 * defines the functionality of exit button
				 */
				Button exit = new Button("Exit");
				exit.setPrefSize(120, 50);
				exit.setLayoutX(200);
				exit.setLayoutY(250);
				exit.setStyle("-fx-font: 20 arial;");
				exit.setOnAction(ae -> {
					newWindow.close();
				});
                //create the scene, and add the button to the scene
				Scene dialogScene = new Scene(grid, 400, 350);
				grid.getChildren().add(add);
				grid.getChildren().add(exit);
				//show stage
				newWindow.setScene(dialogScene);
				newWindow.show();
			}
		});
        //create the analyze button and set the positions, size and style. 
		Button analyzeButton = new Button("Analyze Meal in Meal List");
		analyzeButton.setPrefWidth(250);
		analyzeButton.setPrefHeight(50);
		analyzeButton.setLayoutX(550);
		analyzeButton.setLayoutY(550);
		//defines the functionality of this button
		analyzeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//create the stage and scene
				Stage newWindow = new Stage();
				Pane secondLayout = new Pane();
				Scene secondScene = new Scene(secondLayout, 300, 225);
				//set the styles
				newWindow.setTitle("Analyze Meal List");
				newWindow.setScene(secondScene);
				newWindow.initModality(Modality.APPLICATION_MODAL);
				newWindow.initOwner(primaryStage);
				newWindow.show();
                /**
                 * Below, create the labels and set the positions, size and styles
                 */
				
				double calories = 0.0;
				double fat = 0.0;
				double carb = 0.0;
				double fiber = 0.0;
				double protein = 0.0;
				if (mealListData.getAllFoodItems().size() > 0) {
				for (int i = 0; i < mealListData.getAllFoodItems().size();i++) {
				    calories += mealListData.getAllFoodItems().get(i).getNutrientValue("calories");
				    fat += mealListData.getAllFoodItems().get(i).getNutrientValue("fat");
				    carb += mealListData.getAllFoodItems().get(i).getNutrientValue("carbohydrate");
				    fiber += mealListData.getAllFoodItems().get(i).getNutrientValue("fiber");
				    protein += mealListData.getAllFoodItems().get(i).getNutrientValue("protein");
				}
				}
				
				
				Label lb1 = new Label();
				lb1.setText("Total Calories " + calories);
				lb1.setPrefSize(400, 20);
				lb1.setLayoutX(0);
				lb1.setLayoutY(10);
				lb1.setStyle("-fx-font: 20 arial;");
			

				Label lb2 = new Label();
				lb2.setText("Total Fat " + fat);
				lb2.setPrefSize(400, 20);
				lb2.setLayoutX(0);
				lb2.setLayoutY(40);
				lb2.setStyle("-fx-font: 20 arial;");

				Label lb3 = new Label();
				lb3.setText("Total Carbohydrate " + carb);
				lb3.setPrefSize(400, 20);
				lb3.setLayoutX(0);
				lb3.setLayoutY(70);
				lb3.setStyle("-fx-font: 20 arial;");


				Label lb4 = new Label();
				lb4.setText("Total Fiber " + fiber);
				lb4.setPrefSize(400, 20);
				lb4.setLayoutX(0);
				lb4.setLayoutY(100);
				lb4.setStyle("-fx-font: 20 arial;");


				Label lb5 = new Label();
				lb5.setText("Total Protein " + protein);
				lb5.setPrefSize(400, 20);
				lb5.setLayoutX(0);
				lb5.setLayoutY(130);
				lb5.setStyle("-fx-font: 20 arial;");

                //create the exit button and set the positions, size and styles
				Button exit = new Button("Exit");
				exit.setPrefSize(80, 35);
				exit.setLayoutX(220);
				exit.setLayoutY(190);
				exit.setStyle("-fx-font: 20 arial;");
				//define ths functionality of the button
				exit.setOnAction(ae -> {
					newWindow.close();
				});
                //add all buttons and labels to the scene
				secondLayout.getChildren().addAll(lb1, lb2, lb3, lb4, lb5, exit);

			}
		});
        //create the CountNum button and set the positions, size and styles
		Button button6 = new Button("Count Food List");

		button6.setPrefWidth(120);
		button6.setPrefHeight(30);
		button6.setLayoutX(260);
		button6.setLayoutY(60);
        //defines the functionality of the CountNum
		button6.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//Create the stage 
				Stage newWindow = new Stage();
				newWindow.initModality(Modality.APPLICATION_MODAL);
				newWindow.initOwner(primaryStage);
				//create the text label and set the positions, size and styles
				Label text = new Label("Number of food in your meal: " + String.valueOf(names.size()));

				text.setPrefSize(300, 20);
				text.setLayoutX(50);
				text.setLayoutY(15);
				Pane secondLayout = new Pane();
                //create the scene
				Scene secondScene = new Scene(secondLayout, 400, 50);
                //create the exit button, set the positions, size and styles 
				Button exit = new Button("Exit");
				exit.setPrefSize(50, 30);
				exit.setLayoutX(330);
				exit.setLayoutY(10);
				//defines the functionality of exit
				exit.setOnAction(ae -> {
					newWindow.close();
				});
				secondLayout.getChildren().addAll(text, exit);
                //set title and show the stage
				newWindow.setTitle("Number of food");
				newWindow.setScene(secondScene);
				newWindow.show();
			}
		});

		//This button is for rules interface
		Button button8 = new Button("View/Change Rules");
        //below is setting the position and size
        button8.setPrefWidth(200);
        button8.setPrefHeight(50);
        button8.setLayoutX(120);
        button8.setLayoutY(0);
        //defines the action
        Wei:
            button8.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            Stage newWindow = new Stage();//create the Stage
                            //create the pane and scene
                            Pane secondLayout = new Pane();
                            Scene secondScene = new Scene(secondLayout,500, 440);
                            //this button is for applying rules
                            Button button1 = new Button("Add Rules");
                            //set the positions and size of applying rules button
                            button1.setPrefWidth(120);
                            button1.setPrefHeight(30);
                            button1.setLayoutX(340);
                            button1.setLayoutY(20);
                            //defines the function of the apply rules button
                            button1.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    final Stage dialog = new Stage();//create stage
                                    //just create one stage, and prevent other stage working
                                    dialog.initModality(Modality.APPLICATION_MODAL);
                                    dialog.initOwner(primaryStage);
                                    //set titles of this interface
                                    dialog.setTitle("Rules");
                                    //create the exit button
                                    Button exit = new Button("Exit");
                                    //set the position and size of the exit button
                                    exit.setPrefWidth(100);
                                    exit.setPrefHeight(10);
                                    exit.setStyle("-fx-font: 20 arial;");
                                    exit.setLayoutX(500);
                                    exit.setLayoutY(312);
                                    //defines the event of exit button
                                    exit.setOnAction(handle -> {
                                        
                                        dialog.close();
                                    });
                                    //create the rule label
                                    Label rule = new Label();
                                    //set the positions, styles and size of this label
                                    rule.setText("Rules");
                                    rule.setStyle("-fx-font: 24 arial;");
                                    rule.setPrefWidth(70);
                                    rule.setPrefHeight(10);
                                    rule.setLayoutX(275);
                                    rule.setLayoutY(0);
                                    /**
                                     * create the label of each nutrient and styles and positions of each label
                                     */
                                    Label calories = new Label("Calories");
                                    calories.setStyle("-fx-font: 20 arial;");
                                    calories.setPrefWidth(100);
                                    calories.setPrefHeight(10);
                                    calories.setLayoutX(20);
                                    calories.setLayoutY(30);
                                    Label carbs = new Label("Carbs");
                                    carbs.setStyle("-fx-font: 20 arial;");
                                    carbs.setPrefWidth(70);
                                    carbs.setPrefHeight(10);
                                    carbs.setLayoutX(20);
                                    carbs.setLayoutY(60);
                                    Label fat = new Label("Fat");
                                    fat.setStyle("-fx-font: 20 arial;");
                                    fat.setPrefWidth(50);
                                    fat.setPrefHeight(10);
                                    fat.setLayoutX(20);
                                    fat.setLayoutY(90);
                                    Label protein = new Label("Protein");
                                    protein.setStyle("-fx-font: 20 arial;");
                                    protein.setPrefWidth(100);
                                    protein.setPrefHeight(10);
                                    protein.setLayoutX(20);
                                    protein.setLayoutY(120);
                                    Label fiber = new Label("Fiber");
                                    fiber.setStyle("-fx-font: 20 arial;");
                                    fiber.setPrefWidth(70);
                                    fiber.setPrefHeight(10);
                                    fiber.setLayoutX(20);
                                    fiber.setLayoutY(150);
                                    Label name = new Label("Please Enter Key Words for Searching food:");
                                    name.setStyle("-fx-font: 20 arial;");
                                    name.setPrefWidth(400);
                                    name.setPrefHeight(10);
                                    name.setLayoutX(20);
                                    name.setLayoutY(250);
                                    /**
                                     * Below creates the choice box of for "==", ">=" and "<=" for 
                                     * each nutrients
                                     */
                                    
                                    ChoiceBox<String> cb1 = new ChoiceBox<String>();
                                    cb1.setItems(FXCollections.observableArrayList("==", ">=", "<=", null));
                                    cb1.setLayoutX(130);
                                    cb1.setLayoutY(25);

                                    
                                    ChoiceBox<String> cb2 = new ChoiceBox<String>();
                                    cb2.setItems(FXCollections.observableArrayList("==", ">=", "<=", null));
                                    cb2.setLayoutX(130);
                                    cb2.setLayoutY(55);
                                    
                                    
                                    ChoiceBox<String> cb3 = new ChoiceBox<String>();
                                    cb3.setItems(FXCollections.observableArrayList("==", ">=", "<=", null));
                                    cb3.setLayoutX(130);
                                    cb3.setLayoutY(85);
                                    
                                    
                                    ChoiceBox<String> cb4 = new ChoiceBox<String>();
                                    cb4.setItems(FXCollections.observableArrayList("==", ">=", "<=", null));
                                    cb4.setLayoutX(130);
                                    cb4.setLayoutY(115);
                                    
                                    
                                    ChoiceBox<String> cb5 = new ChoiceBox<String>();
                                    cb5.setItems(FXCollections.observableArrayList("==", ">=", "<=", null));
                                    cb5.setLayoutX(130);
                                    cb5.setLayoutY(145);
                                    
                                    /**
                                     * create the prompt text field for user to type numbers
                                     * for each nutrients. And set their positions, size and styles
                                     */
                                    TextField prompt1 = new TextField();
                                    prompt1.setPromptText("Enter Number: ");
                                    prompt1.setPrefColumnCount(10);
                                    prompt1.setLayoutX(250);
                                    prompt1.setLayoutY(25);
                                    
                                    TextField prompt2 = new TextField();
                                    prompt2.setPromptText("Enter Number: ");
                                    prompt2.setPrefColumnCount(10);
                                    prompt2.setLayoutX(250);
                                    prompt2.setLayoutY(55);
                                    
                                    TextField prompt3 = new TextField();
                                    prompt3.setPromptText("Enter Number: ");
                                    prompt3.setPrefColumnCount(10);
                                    prompt3.setLayoutX(250);
                                    prompt3.setLayoutY(85);
                                    
                                    TextField prompt4 = new TextField();
                                    prompt4.setPromptText("Enter Number: ");
                                    prompt4.setPrefColumnCount(10);
                                    prompt4.setLayoutX(250);
                                    prompt4.setLayoutY(115);
                                    
                                    TextField prompt5 = new TextField();
                                    prompt5.setPromptText("Enter Number: ");
                                    prompt5.setPrefColumnCount(10);
                                    prompt5.setLayoutX(250);
                                    prompt5.setLayoutY(145);
                                    
                                    TextField prompt6 = new TextField();
                                    prompt6.setPromptText("Enter KeyWords: ");
                                    prompt6.setPrefColumnCount(10);
                                    prompt6.setLayoutX(430);
                                    prompt6.setLayoutY(250);
                                 
                                    //Create the apply button and set its positions and sizes
                                    Button apply = new Button("Apply");
                                    apply.setPrefWidth(120);
                                    apply.setPrefHeight(10);
                                    apply.setStyle("-fx-font: 20 arial;");
                                    apply.setLayoutX(380);
                                    apply.setLayoutY(312);
                                    //defines the functionality of apply button
                                    apply.setOnAction(e -> {
                                        //gets the contents of choice box 
                                        String cb1text = (String) cb1.getValue();
                                        String cb2text = (String) cb2.getValue();
                                        String cb3text = (String) cb3.getValue();
                                        String cb4text = (String) cb4.getValue();
                                        String cb5text = (String) cb5.getValue();
                                        //sum them up
                                        String sumText = cb1text + cb2text + cb3text+ cb4text+ cb5text;
                                        //gets the content of prompt text
                                        String entered1 = prompt1.getText();
                                        String entered2 = prompt2.getText();
                                        String entered3 = prompt3.getText();
                                        String entered4 = prompt4.getText();
                                        String entered5 = prompt5.getText();
                                        String entered6 = prompt6.getText();
                                        //test if the enter6 is a alphabet
//                                        boolean containsChars = false;
                                        //sum all prompts text together
                                        String entered = entered1 + entered2 + entered3 + entered4 + entered5;
                                        //create the array storing the choice box content and prompt text 
                                        String[][] a = new String[5][2];
                                        a[0][0] = cb1text;
                                        a[1][0] = cb2text;            
                                        a[2][0] = cb3text;                 
                                        a[3][0] = cb4text;                   
                                        a[4][0] = cb5text;                   
                                        a[0][1] = entered1;                   
                                        a[1][1] = entered2;                     
                                        a[2][1] = entered3;                   
                                        a[3][1] = entered4;                    
                                        a[4][1] = entered5; 
                                        
                                        String cal = "calories";
                                        cal = cal + " " + cb1.getValue();
                                        String fatty = "fat";
                                        fatty = fatty + " " + cb3.getValue();
                                        String carbo = "carbohydrate";
                                        carbo = carbo + " " + cb2.getValue();
                                        String fib = "fiber";
                                        fib = fib + " " + cb5.getValue();
                                        String prot = "protein";
                                        prot = prot + " " + cb4.getValue();
                                        
                                        cal = cal + " " + prompt1.getText();
                                        fatty = fatty + " " + prompt3.getText();
                                        carbo = carbo + " " + prompt2.getText();
                                        fib = fib + " " + prompt5.getText();
                                        prot = prot + " " + prompt4.getText();
                                        //if all field empty, pop up the warning windows                  
                                        if (entered6.trim().equals("") && sumText != null && entered.trim().equals("")) {
                                            Alert alert = new Alert(AlertType.WARNING);
                                            alert.setHeaderText("Error");
                                            alert.setContentText("Please enter correct rules or name");
                                            alert.showAndWait();
                                        }else if (entered6.trim().equals("")) {// if the name is empty, check if the choice box and 
                                            //prompt text is correct
                                                if (!valid(a)) {//check if they are valid
                                                    Alert alert = new Alert(AlertType.WARNING);
                                                    alert.setHeaderText("Error");
                                                    alert.setContentText("Please enter correct rules");
                                                    alert.showAndWait();
                                                }else {
                                                    //if they all correct, add them to the rules
//                                                    if (!rules.contains(printRules(a))) {
//                                                        
//                                                        rules.addAll(printRules(a));
//                                                    }
                                                    
                                                    addRule(cal);
                                                    addRule(fatty);
                                                    addRule(fib);
                                                    addRule(prot);
                                                    addRule(carbo);
                                                    
                                                    dialog.close();
                                                }
                                                                 
                                        }else {
                                  
//                                            if (!containsChars) {//if it contains something that other than letters, pop up warning
//                                                Alert alert = new Alert(AlertType.WARNING);
//                                                alert.setHeaderText("Error");
//                                                alert.setContentText("Please enter correct name");
//                                                alert.showAndWait();
//                                            }else {
                                                //if all boxs except name box is all null, add name into rules
                                                if (sumText.trim().equals("nullnullnullnullnull") && entered.trim().equals("")) {
                                                    String filterName = "Name: " + entered6;
                                                    boolean exist = false;
                                                    for (int i = 0; i < rules.size(); i++) {
                                                        if (!rules.get(i).toLowerCase().equals(filterName.toLowerCase())) {
                                                            continue;
                                                        }else {
                                                            exist = true;
                                                        }
                                                        
                                                    }
                                                    if ( exist == false) {
                                                        rules.add(filterName);
                                                        filterByName.add(prompt6.getText());
                                                    }
                                                    

                                                    dialog.close();
                                                }
                                                // if name box is valid check all the other field box is valid
                                                else {
                                                    // check if other boxes are valid, if not, alert user
                                                    if (!valid(a)) {
                                                        Alert alert = new Alert(AlertType.WARNING);
                                                        alert.setHeaderText("Error");
                                                        alert.setContentText("Please enter correct rules");
                                                        alert.showAndWait();                                          
                                                    }else {
                                                        // read data
                                                        String filterName = "Name: " + entered6;
                                                        boolean exist = false;
                                                        for (int i = 0; i < rules.size(); i++) {
                                                            if (!rules.get(i).toLowerCase().equals(filterName.toLowerCase())) {
                                                                continue;
                                                            }else {
                                                                exist = true;
                                                            }
                                                            
                                                        }
                                                        if ( exist == false) {
                                                            rules.add(filterName);
                                                            filterByName.add(prompt6.getText());
                                                        }
                                                        
//                                                        if (!rules.contains(printRules(a))) {
//                                                            rules.addAll(printRules(a));
//                                                        }
                                                        
                                                        addRule(cal);
                                                        addRule(fatty);
                                                        addRule(fib);
                                                        addRule(prot);
                                                        addRule(carbo);
                                                        
                                                        dialog.close();
                                                    }
                                                }
                                            }

//                                        }
                                        
                                    
                                        

                                        
                                        
                                        
                                                           
                                        
                                    });// FIXME



                                  //create the exit button
                                    //create pane
                                    Pane rules = new Pane();
                                    //add all buttons and choice box and label to rules
                                    rules.getChildren().add(rule);
                                    rules.getChildren().addAll(prompt1, prompt2, prompt3, prompt4, prompt5, prompt6);
                                    rules.getChildren().addAll(cb1, cb2, cb3, cb4, cb5);
                                    rules.getChildren().add(calories);
                                    rules.getChildren().add(carbs);
                                    rules.getChildren().add(fat);
                                    rules.getChildren().add(protein);
                                    rules.getChildren().add(fiber);
                                    rules.getChildren().add(name);
                                    rules.getChildren().add(exit);
                                    rules.getChildren().add(apply);

                                    //create the scene
                                    Scene dialogScene = new Scene(rules, 600, 350);
                                    dialog.setScene(dialogScene);
                                    dialog.show();//show dialog

                                }
                            });
                            
                            Button deleteRules = new Button("DeleteAllRules");
                            deleteRules.setPrefSize(120, 30);
                            deleteRules.setLayoutX(340);
                            deleteRules.setLayoutY(60);
                            deleteRules.setOnAction(ae -> {
                                rules.clear();
                                filterByName.clear();
                                filterByNutrient.clear();
                            });
                            
                            //create the exit button
                            Button exit = new Button("Apply rules & exit");
                            //set the positions and size of the button
                            exit.setPrefSize(120, 30);
                            exit.setLayoutX(340);
                            exit.setLayoutY(100);
                            //defines the exit button's function
                            exit.setOnAction(ae -> {
                                try {
                                FoodData newFood = new FoodData();
                                newFood = foodData;
                                
                                filterMethod(newFood, filterByNutrient, filterByName,  foodDataList);
                                
                                
                                
                                }catch (Exception e){
                                    
                                }
                                newWindow.close();
                            });
                            //create the view list and set positions and size of view list
                            ListView<String> ruleList = new ListView<>();
                            ruleList.setItems(rules);
                            ruleList.setPrefWidth(300);
                            ruleList.setPrefHeight(400);
                            ruleList.setLayoutX(20);
                            ruleList.setLayoutY(20);
                            ruleList.setCellFactory(param -> new XCell("DEL", filterByNutrient,foodData, filterByName, foodDataList));
                            //add all view list to layout
                            secondLayout.getChildren().addAll(ruleList, exit, button1,deleteRules);
                            //set titles
                            newWindow.setTitle("Rules");
                            newWindow.setScene(secondScene);
                            newWindow.show();//show interface
                        }
                    });
        //button7 is for minimizing the window 
		Button button7 = new Button("-");
		button7.setPrefWidth(20);
		button7.setPrefHeight(20);
		button7.setLayoutX(730);
		button7.setLayoutY(0);
		//create the event
		button7.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				primaryStage.setIconified(true);
			}
		});
        //These create the label and set their positions
		Label label1 = new Label("Food List (Food Data)");
		label1.setPrefWidth(220);
		label1.setPrefHeight(50);
		label1.setLayoutX(100);
		label1.setLayoutY(50);

		Label label2 = new Label("Meal List (Meal Data)");
		label2.setPrefWidth(400);
		label2.setPrefHeight(50);
		label2.setLayoutX(530);
		label2.setLayoutY(50);
        //create the viewlist to view the content , set the positions
		ListView<String> foodList = new ListView<>();
		
		
		  Button Reset = new Button("Reset");
	        Reset.setPrefWidth(120);
	        Reset.setPrefHeight(50);
	        Reset.setLayoutX(340);
	        Reset.setLayoutY(550);
	        Reset.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	                //Create the stage 
	                foodData = new FoodData();
	                mealListData = new FoodData();
	                foodDataList.clear();
	                names.clear();
	                meals.clear();
	                System.out.println(meals.size());
	                rules.clear();
	            }
	        });
	        
	        Button addAll = new Button("Add all food into meal list");
	        addAll.setPrefWidth(200);
	        addAll.setPrefHeight(30);
	        addAll.setLayoutX(100);
	        addAll.setLayoutY(505);
	        addAll.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    //Create the stage 
                    for (int i = 0; i < foodDataList.size();i++) {
                    mealListData.addFoodItem(foodDataList.get(i));
                    meals.add(foodDataList.get(i).getName());
                    }
//                    Collections.sort(meals);
                    meals.sort((h1, h2) -> h1.toLowerCase().compareTo(h2.toLowerCase()));
                    
                }
            });
	        
	        Button deleteAll = new Button("Delete all food in meal list");
	        deleteAll.setPrefWidth(200);
	        deleteAll.setPrefHeight(30);
	        deleteAll.setLayoutX(480);
	        deleteAll.setLayoutY(505);
	        deleteAll.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    //Create the stage 
                    mealListData = new FoodData();
                    meals.clear();
                }
            });

		
	    foodList.setItems(names);
	    
		foodList.setPrefWidth(380);
		foodList.setPrefHeight(400);
		foodList.setLayoutX(10);
		foodList.setLayoutY(100);
        //create the HBox to drag, if the content is too large
		HBox hBox1 = new HBox();
		hBox1.getChildren().addAll(label1, foodList);//add list to HBox
        //create the viewlist to view the content, set the positions
		ListView<String> mealList = new ListView<>();
		mealList.setItems(meals);
		mealList.setPrefWidth(380);
		mealList.setPrefHeight(400);
		mealList.setLayoutX(410);
		mealList.setLayoutY(100);
        //add "add" , "del" to the viewlist
		foodList.setCellFactory(param -> new XCell("ADD", mealList, mealListData,foodDataList));
		mealList.setCellFactory(param -> new XCell("DEL", mealListData));
        //create the HBox, and put the meallist to HBox
		HBox hBox2 = new HBox();
		hBox2.getChildren().addAll(label2, mealList);
		
        
		Pane layout = new Pane();//create the main interface
		//put all buttons and labels into the layout
		layout.getChildren().add(closeButton);
		layout.getChildren().add(loadButton);
		layout.getChildren().add(addButton);
		layout.getChildren().add(analyzeButton);
		layout.getChildren().add(label1);
		layout.getChildren().add(label2);
		layout.getChildren().add(foodList);
		layout.getChildren().add(mealList);
		layout.getChildren().add(button7);
		layout.getChildren().addAll(button6,button8,saveFoodButton,saveMealButton, instruction, Reset, addAll, deleteAll);
		Scene scene = new Scene(layout, 800, 600);//create scene

		primaryStage.setScene(scene);
		primaryStage.show();//show stage

	}
	   /**
  * private helper that used to check whether the input rules of user is valid or not
  * 
  * @param two dimensional array of 5X2. It stores 5 input limits and 5 double numbers
  * @return boolean
  */
 private boolean valid(String[][] a) {
     boolean returnB = true;
     for (int i = 0; i < a.length; i++) {
         if (a[i][0] != null && !isDouble(a[i][1])) {
             return false;
         } else if (a[i][0] == null && isDouble(a[i][1])) {
             return false;
         } else if (a[i][0] == null && !a[i][1].equals("")) {
             return false;
         }
     }
     return returnB;
 }

 /**
  * private helper that used to return valid rules
  * @param two dimensional array of 5X2. It stores 5 input limits and 5 double numbers
  * @return arraylist of valid rules
  */
 private ArrayList<String> printRules(String[][] a) {
     ArrayList returnS = new ArrayList<String>();
     for (int i = 0; i < a.length; i++) {
         if (a[i][0] != null && isDouble(a[i][1])) {
             String returnString = nutrients[i] + " " + a[i][0] + " " + a[i][1];
             returnS.add(returnString);
         }
     }
     return returnS;

 }
 /**
  * private helper that used to check whether a string is a double 
  * @param input string
  * @return boolean
  */
 private static boolean isDouble(String a) {
     try {
         double value = Double.parseDouble(a);
         if (value < 0.0) {
             return false;
         }
     } catch (Exception e) {
         return false;
     }
     return true;


 }

 /**
  * The class that used to add ADD and Delete button and the functionalities of them in the viewList and 
  * @author Xiaoshan
  *
  */
 static class XCell extends ListCell<String>  {
     HBox hbox = new HBox();
     Label label = new Label("");
     Pane pane = new Pane();
     Button button = new Button();
     // constructor to build delete button for rule list
     
     public XCell(String buttonName, ArrayList<String> filterByNutrients, FoodData foodData, ArrayList<String> filterByName, List<FoodItem> foodDataList) {
         super();
         this.button = new Button(buttonName);
         hbox.getChildren().addAll(label, pane, button);
         HBox.setHgrow(pane, Priority.ALWAYS);
         // button used to delete from rule list
         button.setOnAction(new EventHandler<ActionEvent>() {

             @Override
             public void handle(ActionEvent event) {
                 String a = getItem();
                 System.out.println(a + "delete");
                 getListView().getItems().remove(a);
                 for (int i = 0; i < filterByNutrients.size(); i++) {
                     if (filterByNutrients.get(i).equals(a)) {
                         filterByNutrients.remove(i);
                     }
                 }
                 
                 for (int i = 0; i < filterByName.size(); i++) {
                     if (!filterByName.get(i).toLowerCase().equals(a.toLowerCase())) {
                         filterByName.remove(i);
                     }
                     
                 }
                 
//                 if (filterByName.contains(a)) {
//                     filterByName.remove(a);
//                 }
                 
                 
//                 if (filterByName.contains(a)) {
//                     filterByName.clear();
//                     System.out.println("delete name");
//                 }
             }
         });

     }
     public XCell(String buttonName , FoodData mealList) {
         super();
         this.button = new Button(buttonName);
         hbox.getChildren().addAll(label, pane, button);
         HBox.setHgrow(pane, Priority.ALWAYS);
         // button used to delete from rule list
         button.setOnAction(new EventHandler<ActionEvent>() {

             @Override
             public void handle(ActionEvent event) {
                 String a = getItem();
                 getListView().getItems().remove(a);
//                 Collections.sort(getListView().getItems());
                 getListView().getItems().sort((h1, h2) -> h1.toLowerCase().compareTo(h2.toLowerCase()));
                 
                 mealList.getAllFoodItems().remove(getIndex());
//                 for (int i = 0; i < mealList.getAllFoodItems().size();i++) {
//                     if (mealList.getAllFoodItems().get(i).getName().equals( a)) {
//                         mealList.getAllFoodItems().remove(i); 
//                     }
//                 }
             }
         });

     }
     // constructor to build add and delete button for foodlist and meallist
     public XCell(String buttonName, ListView<String> name, FoodData mealList, List<FoodItem> foodData) {
         super();
         this.button = new Button(buttonName);
         hbox.getChildren().addAll(label, pane, button);
         HBox.setHgrow(pane, Priority.ALWAYS);
      // button used to add from foodlist to meallist and delete from mealist and add to foodlist
         button.setOnAction(new EventHandler<ActionEvent>() {

             @Override
             public void handle(ActionEvent event) {                 
                 String a = getItem();
                                                  
                 name.getItems().add(a);
                 Collections.sort(name.getItems());
                 name.getItems().sort((h1, h2) -> h1.toLowerCase().compareTo(h2.toLowerCase()));;
                 
                 foodData.sort((h1, h2) -> h1.getName().toLowerCase().compareTo(h2.getName().toLowerCase()));
                 mealList.addFoodItem(foodData.get(getIndex()));
                 
//                 for (int i = 0; i < foodData.size();i++) {
//                     if (foodData.get(i).getName().equals(a)) {
//                         mealList.addFoodItem(foodData.get(i));
//                         System.out.println(foodData.get(i).getID());
//                     }
//                 }
                
             }
         });

     }

     @Override
     protected void updateItem(String item, boolean empty) {
         super.updateItem(item, empty);
         setText(null);
         setGraphic(null);

         if (item != null && !empty) {
             label.setText(item);
             setGraphic(hbox);
         }        
     }
 }
 private void addRule(String rule) {
     String[] ruleArray1 = rule.split(" ");
     if(!rule.contains("null")) {
    for(String rules: filterByNutrient) {
    String [] ruleArray2 = rules.split(" ");
    if(ruleArray2[0].toLowerCase().equals(ruleArray1[0].toLowerCase()) &&
    ruleArray2[1].equals(ruleArray1[1]) && 
    Double.parseDouble(ruleArray2[2]) == Double.parseDouble(ruleArray1[2])) {
    return;
    }
    }
         filterByNutrient.add(rule);
         rules.add(rule);
     }
     else {
    return;
     }
 }
 
 private static void filterMethod(FoodData foodData, ArrayList<String> filterByNutrient, ArrayList<String> filterByName, List<FoodItem> foodDataList) {
     List<FoodItem> nutriList = new ArrayList<FoodItem>(foodData.filterByNutrients(filterByNutrient));
     List<FoodItem> nameList = new ArrayList<FoodItem>(foodData.filterByName(null));
     if (filterByName.size() != 0) {
         
         for (int i = 0; i < filterByName.size();i++) {
             nameList.retainAll(foodData.filterByName(filterByName.get(i)));
         }
         
         System.out.print("Fuck " + filterByName.get(0));
     }
     
     System.out.println(filterByNutrient.size());
     System.out.println(foodData.getAllFoodItems().size());
     System.out.println(nutriList.size()); 
     
     foodDataList.clear();
     nutriList.retainAll(nameList);
     foodDataList.addAll(nutriList);
     
     names.clear();
         
//     Collections.sort(foodDataList, 
//                     (o1, o2) -> o1.getName().compareTo(o2.getName()));
     foodDataList.sort((h1, h2) -> h1.getName().toLowerCase().compareTo(h2.getName().toLowerCase()));
     for (int i = 0; i < foodDataList.size(); i++) {
         names.add(foodDataList.get(i).getName());
     }   
 }
 


	public static void main(String[] args) {
		
		launch(args);
	}

}