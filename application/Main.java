package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
	// store any command-line arguments that were entered.
	// NOTE: this.getParameters().getRaw() will get these also
	private List<String> args;

	private SocialNetwork socialNetwork = new SocialNetwork();

	private static final int WINDOW_WIDTH = 400;
	private static final int WINDOW_HEIGHT = 400;
	private static final String APP_TITLE = "Social Network";
	// Center of the GUI (menuBox and networkBox) are arranged on a grid
	private GridPane rootGrid = new GridPane();

	// rootGrid and other boxes are arranged on BorderPane
	private BorderPane startPane = new BorderPane();
	private BorderPane mainPane = new BorderPane();
	private BorderPane exitPane = new BorderPane();

	// the user selected which might become the selected user
	private String currentUser;

	Scene startScene, mainScene, exitScene;

	@Override
	public void start(Stage primaryStage) throws Exception {	
		
		// Welcome Scene
		welcome();
		// add confirm button (move from start scene to main scene)
		VBox confirm = new VBox();
		Button toMain = new Button("Confirm");
		toMain.setOnAction(e -> primaryStage.setScene(mainScene)); // on click - goes to mainScene
		confirm.getChildren().add(toMain);
		startPane.setBottom(confirm);

		startScene = new Scene(startPane, WINDOW_WIDTH, WINDOW_HEIGHT);

		// Main Scene
		// save args example
		args = this.getParameters().getRaw();

		// Set up main Gridpane
		rootGrid.setPadding(new Insets(10, 10, 10, 10));
		rootGrid.setVgap(5);
		rootGrid.setHgap(5);

		// Add the main grid to the BorderPane
		mainPane.setCenter(rootGrid);

		// add exit button
		VBox bottom = new VBox();
		Button toExit = new Button("Done");
		toExit.setOnAction(e -> primaryStage.setScene(exitScene)); // on click - goes to exitScene
		bottom.getChildren().add(toExit);
		
		// add undo button
		Button undo = new Button("Undo");
		undo.setOnAction(e -> {
			String message = socialNetwork.undo();
			if(!message.equals("")) {
				//TODO display error
			} else {
				//TODO reset display
			}
		});
		bottom.getChildren().add(undo);
		
		mainPane.setBottom(bottom);

		mainScene = new Scene(mainPane, WINDOW_WIDTH, WINDOW_HEIGHT);

		topBox();
		menuBox();
		networkBox();

		// Exiting Scene
		exitScene = new Scene(exitPane, WINDOW_WIDTH, WINDOW_HEIGHT);
		exiting();

		// Generate imageview control instance and add to window
		// startPane.setCenter(newImage("headshot.jpg"));
		// Add the stuff and set the primary stage
		primaryStage.setTitle(APP_TITLE);
		primaryStage.setScene(startScene);
		primaryStage.show();
	}

	/**
	 * Method to set up the top bar of the GUI
	 * 
	 * @throws FileNotFoundException
	 */
	private void topBox() throws FileNotFoundException {
		mainPane.setTop(newImage("socialnetwork.png"));
		BorderPane.setAlignment(mainPane, Pos.TOP_RIGHT);
		BorderPane.setMargin(mainPane, new Insets(12, 12, 12, 12));
	}

	/**
	 * Method to draw the menu box on the left side of the GUI main screen
	 */
	private void menuBox() {
		// Grid for search bar and selected user
		GridPane grid1 = new GridPane();
		grid1.setPadding(new Insets(10, 10, 10, 10));
		grid1.setVgap(5);
		grid1.setHgap(5);

		// Grid for menu options
		GridPane grid2 = new GridPane();

		// grid2.setPadding(new Insets(10, 10, 10, 10));
		grid2.setVgap(5);
		grid2.setHgap(5);

		// Search Box
		Label l1 = new Label("Search: ");
		TextField search = new TextField();
		search.setPromptText("Enter username");
		search.getText();
		search.setPrefWidth(100);
		// grid1Pane.setConstraints(search, 0, 0);

		// Menu Options
		Label l2 = new Label("Selected User: ");

		// TODO: Replace with dynamic label that displays the current user//
		// selectedUser = "JohnDoe";
		currentUser = "JaneDoe";
		Label l3 = new Label(socialNetwork.getSelected());
		//////////////////////////////////////////////////////////////////

		Button b1 = new Button("Shortest path to " + currentUser);
		Button b2 = new Button("Show Mutual Friends");
		Button b3 = new Button("Set " + currentUser + " as center");
		Button b4 = new Button("Find Connected Components");
		int buttonWidth = 200;
		b1.setPrefWidth(buttonWidth);
		b2.setPrefWidth(buttonWidth);
		b3.setPrefWidth(buttonWidth);
		b4.setPrefWidth(buttonWidth);

		// Set up Grid 1
		grid1.add(l1, 0, 0);
		grid1.add(l2, 0, 1);
		grid1.add(l3, 1, 1);
		grid1.add(search, 1, 0);
		GridPane.setHalignment(l1, HPos.RIGHT);

		// Set up Grid 2
		grid2.add(b1, 0, 2);
		grid2.add(b2, 0, 3);
		grid2.add(b3, 0, 4);
		grid2.add(b4, 0, 5);
		grid2.setAlignment(Pos.CENTER);

		// Combine grids in VBox
		VBox v1 = new VBox();
		v1.getChildren().add(grid1);
		v1.getChildren().add(grid2);
		v1.setAlignment(Pos.CENTER);
		// Add Vbox to the main gridpane
		rootGrid.add(v1, 0, 1);
	}

	/**
	 * Method to draw the friend network of the currently selected person on the
	 * right side of the GUI main screen
	 */
	void networkBox() {
		GridPane grid1 = new GridPane();
		grid1.setPadding(new Insets(10, 10, 10, 10));
		grid1.setVgap(5);
		grid1.setHgap(5);

		// Make these dynamic based on currently selected user and function
		Label l1 = new Label("Current User: " + currentUser);
		Label l2 = new Label("Showing: Mutual Friends");
		//////////////////////////////////////////////////////////////////

		grid1.add(l1, 0, 0);
		grid1.add(l2, 0, 1);

		//////////////////////////////////////////////////////////////////
		// Make displayed data a VBox which is inserted into the Grid

		// TODO: determine the functionality to generate a list of friends
		// and add them to the GUI. Likely will want to use a helper method
		// which returns a list regardless of if a list or a set is returned,as
		// the implementation can provide either.
		// Example functionality shown below
//		for(Person person : displayList) {
//			
//		}

		// Hardcoded display of mutual friends as example
		Label l3 = new Label("MaryAnn");
		Label l4 = new Label("JimJohn");
		Label l5 = new Label("Jimothy");
		Label l6 = new Label("Janthony");
		VBox v1 = new VBox();
		v1.getChildren().addAll(l3, l4, l5, l6);
		v1.setAlignment(Pos.CENTER);
		grid1.add(v1, 0, 2);
		grid1.setAlignment(Pos.CENTER);

		rootGrid.add(grid1, 1, 1);
	}

	/**
	 * Draws the status box on the bottom of the main GUI screen
	 */
	void statusBox() {

	}

	/**
	 * Class which handles UI interaction with button
	 * 
	 * @author john
	 *
	 */
	class MyHandler implements EventHandler<ActionEvent> {
		Button button;

		MyHandler(Button button) {
			this.button = button;
		}

		@Override
		public void handle(ActionEvent e) {
			if (button.getText().equals("Change to Nature Scene")) {
				button.setText("Change to headshot");
			} else
				button.setText("Change to Nature Scene");
		}
	}

	/**
	 * Private class generates an ImageView object from a file
	 * 
	 * @throws FileNotFoundException
	 */
	private ImageView newImage(String filePath) throws FileNotFoundException {
		FileInputStream input = new FileInputStream(filePath);
		Image image = new Image(input);
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(400);
		imageView.setPreserveRatio(true);
		return imageView;

	}

	/**
	 * Displays welcome GUI
	 */
	public void welcome() {
		HBox fileNameBox = new HBox();
		fileNameBox.setSpacing(5);

		Button open = new Button("Open");

		TextField fileName = new TextField();
		Label label = new Label("Enter file name to read from: ");

		fileNameBox.getChildren().addAll(label, fileName, open);

		// confirm -> button.setPadding(new Insets(0, 0, 0, 118));
		startPane.setPadding(new Insets(10, 10, 10, 10));

//		startPane.setTop(fileNameBox);
		startPane.setCenter(fileNameBox);
	}

	/**
	 * Displays exit GUI
	 */
	public void exiting() {
		HBox buttons = new HBox();
		HBox file = new HBox();

		file.setSpacing(5);

		buttons.setPadding(new Insets(5, 5, 5, 5));
		buttons.setSpacing(5);

		TextField fileName = new TextField("SocialNetworkSave");
		Label label = new Label("Enter file name to save to: ");
		Button save = new Button("Save");
		save.setPadding(new Insets(5, 50, 5, 50));
		save.setOnAction(e -> socialNetwork.readFromFile(fileName.getText()));

		Button nSave = new Button("Exit without saving");
		nSave.setPadding(new Insets(5, 20, 5, 20));
		nSave.setOnAction(e -> Platform.exit());

		buttons.getChildren().add(save);
		buttons.getChildren().add(nSave);
		file.getChildren().add(label);
		file.getChildren().add(fileName);

		// Main layout is Border Pane example (top,left,center,right,bottom)
		exitPane.setPadding(new Insets(10, 10, 10, 10));
		exitPane.setTop(file);
		exitPane.setBottom(buttons);
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
