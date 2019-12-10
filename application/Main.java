package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 500;
    private static final String APP_TITLE = "Social Network";

    // Center of the GUI (menuBox and networkBox) are arranged on a grid
    private GridPane rootGrid = new GridPane();

    // rootGrid and other boxes are arranged on BorderPane
    private BorderPane startPane = new BorderPane();
    private BorderPane mainPane = new BorderPane();
    private BorderPane exitPane = new BorderPane();

    private ArrayList<String> friendList =
        (ArrayList<String>) socialNetwork.allFriends(socialNetwork.getCenterUser()); // the current
                                                                                     // list of
                                                                                     // friends
                                                                                     // displayed

    private ArrayList<Label> friendLabelList = new ArrayList<Label>(); // the list of friend labels

    Scene startScene, mainScene, exitScene;

    // Strings to be user in constructing and maintaining Labels and Buttons
    private String statusMessage = ""; // the status message for updating the user on actions
    private String currentShowing = ""; // the current option being shown
    private String searchedUser = ""; // the user that was most recently searched

    // Labels, fields, buttons that need to change on events
    private Label conCompLabel = new Label("Separate Networks: 0");
    private Label statusLabel = new Label("Status: " + statusMessage);
    private TextField search = new TextField();
    private Button shortestPathButton =
        new Button("Shortest path to " + socialNetwork.getCenterUser());
    private Button mutualFriendsButton = new Button("Show Mutual Friends");
    private Button newCenterButton =
        new Button("Set " + socialNetwork.getCenterUser() + " as center");
    private Button conCompButton = new Button("Find Connected Components");
    private Label centerUserLabel = new Label("Center User: " + socialNetwork.getCenterUser());
    private Label showingLabel = new Label("Showing: " + currentShowing);

    @Override
    public void start(Stage primaryStage) throws Exception {

        // WELCOME SCENE

        welcome();

        // add confirm button (move from start scene to main scene)
        VBox confirm = new VBox();
        Button toMain = new Button("Confirm");
        toMain.setOnAction(e -> primaryStage.setScene(mainScene)); // on click - goes to mainScene
        confirm.getChildren().add(toMain);
        startPane.setBottom(confirm);
        startScene = new Scene(startPane, WINDOW_WIDTH, WINDOW_HEIGHT);


        // MAIN SCENE

        // save args example
        args = this.getParameters().getRaw();

        // initalize all friend labels to empty string
        for (int i = 0; i < 10; i++) {
            Label empty = new Label("");
            friendLabelList.add(empty);
        }

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
            if (!message.equals("")) {
                statusLabel.setText("Status: Could not Undo");
            } else {
                updateFriendLabelList(friendList);
            }
        });
        bottom.getChildren().add(undo);

        mainPane.setBottom(bottom);

        topBox();
        menuBox();
        networkBox();

        mainScene = new Scene(mainPane, WINDOW_WIDTH, WINDOW_HEIGHT);


        // EXIT SCENE

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
        Label searchLabel = new Label("Search: ");
        Button searchButton = new Button("Search");

        searchButton.setOnAction(e -> searchHelper(search));
        search.setPromptText("Enter username");
        search.getText();
        search.setPrefWidth(100);

        //////////////////////////////////////////////////////////////////



        int buttonWidth = 200;
        shortestPathButton.setPrefWidth(buttonWidth);
        shortestPathButton.setOnAction(e -> shortestPathHelper(search));
        mutualFriendsButton.setPrefWidth(buttonWidth);
        mutualFriendsButton.setOnAction(e -> mutualFriendHelper(search));
        newCenterButton.setPrefWidth(buttonWidth);
        newCenterButton.setOnAction(e -> centerUserHelper(null));
        conCompButton.setPrefWidth(buttonWidth);
        conCompButton.setOnAction(e -> conCompHelper());

        // Set up Grid 1
        grid1.add(searchButton, 1, 0);
        grid1.add(search, 0, 0);
        GridPane.setHalignment(searchLabel, HPos.RIGHT);

        // Set up Grid 2
        grid2.add(shortestPathButton, 0, 2);
        grid2.add(mutualFriendsButton, 0, 3);
        grid2.add(newCenterButton, 0, 4);
        grid2.add(conCompButton, 0, 5);
        grid2.add(conCompLabel, 0, 6);
        grid2.getChildren().add(statusLabel);
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
        grid1.setPadding(new Insets(30, 10, 10, 10));
        grid1.setVgap(5);
        grid1.setHgap(5);

        // add the Current User, showing labels to grid
        grid1.add(centerUserLabel, 0, 1);
        grid1.add(showingLabel, 0, 2);
        showingLabel.setUnderline(true);

        // add friends labels to grid
        for (int i = 0; i < 10; i++) {

            grid1.add(friendLabelList.get(i), 0, i + 3);
        }

        // Make displayed data a VBox which is inserted into the Grid
        VBox v1 = new VBox();

        // v1.getChildren().addAll(f1, f2, f3, f4);
        v1.setAlignment(Pos.CENTER);
        grid1.add(v1, 0, 2);
        grid1.setAlignment(Pos.CENTER);

        rootGrid.add(grid1, 1, 1);
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
        imageView.setFitWidth(500);
        imageView.setPreserveRatio(true);
        return imageView;

    }

    /**
     * Displays welcome GUI
     */
    private void welcome() {
        HBox fileNameBox = new HBox();
        fileNameBox.setSpacing(5);

        Button open = new Button("Open");

        TextField fileName = new TextField();
        Label label = new Label("Enter file name to read from: ");

        open.setOnAction(e -> socialNetwork.readFromFile(fileName.getText()));

        fileNameBox.getChildren().addAll(label, fileName, open);

        // confirm -> button.setPadding(new Insets(0, 0, 0, 118));
        startPane.setPadding(new Insets(10, 10, 10, 10));

        // startPane.setTop(fileNameBox);
        startPane.setCenter(fileNameBox);
    }

    /**
     * Displays exit GUI
     */
    private void exiting() {
        HBox buttons = new HBox();
        HBox file = new HBox();

        file.setSpacing(5);

        buttons.setPadding(new Insets(5, 5, 5, 5));
        buttons.setSpacing(5);

        TextField fileName = new TextField("SocialNetworkSave");
        Label label = new Label("Enter file name to save to: ");
        Button save = new Button("Save");
        save.setPadding(new Insets(5, 50, 5, 50));

        save.setOnAction(e -> showSavedAlert(fileName.getText()));

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
     * Shows alert if file save is unsuccessful.
     * 
     * @param inputText
     * @author connorkubiak
     */
    public void showSavedAlert(String inputText) {
        boolean fileSaved = false;
        fileSaved = socialNetwork.saveLog(inputText);

        if (!fileSaved) {
            Alert savedAlert = new Alert(Alert.AlertType.ERROR,
                "Save unsuccessful. Please enter a valid filename.");
            savedAlert.showAndWait();
        }

    }

    /**
     * Helper method for mutual friend button event. Retrieves mutual friend
     * list from socialNetwork object, then displays it.
     * 
     * @param search the textfield that holds user input
     * @author connorkubiak
     */
    private void mutualFriendHelper(TextField search) {

        // retrieve text from text field
        String searchUser = search.getText();

        // list of mutual friends, to be added to friendLabels
        try {
            // get mutual friends
            friendList = (ArrayList<String>) socialNetwork.mutualFriends(searchUser, socialNetwork.getCenterUser());
            // update the "Showing" label
            showingLabel.setText("Showing: Mutual Friends of " + socialNetwork.getCenterUser()
                + " and " + searchUser);
        } catch (NullPointerException e) {
            statusLabel.setText("User " + searchUser + " not found");
        }
        // update the list in the GUI
        updateFriendLabelList(friendList);
    }

    /**
     * Helper method that handles the user click of shortestpath button. If the 
     * searched user exists, finds the path and displays it, otherwise, shows 
     * error.
     * 
     * @author connorkubiak
     * @param search the textfield that holds user input
     */
    private void shortestPathHelper(TextField search) {

        // retrieve text from text box
        String searchUser = search.getText();
        // get list of users in shortest path -- this is null if user is not found
        try {
            friendList = (ArrayList<String>) socialNetwork.friendLink(searchUser, socialNetwork.getCenterUser());
            showingLabel.setText("Showing: Shortest path from " + socialNetwork.getCenterUser()
                + " to " + searchUser);
        } catch (NullPointerException e) {
            statusLabel.setText("Status: User " + searchUser + " not found");
        }
        // update the window
    }

    /**
     * Helper method that changes the center user when the user clicks the 
     * change center user button.
     * 
     * @param search the textfield that holds user input
     * @author connorkubiak
     */
    private void centerUserHelper(String newCenter) {

        // make the searched user the center user
        boolean changedUser = socialNetwork.select(searchedUser);
        // OR if submitted string is not null, make newCenter the center user
        if (newCenter != null) {
            changedUser = socialNetwork.select(newCenter);
        }

        // change display to reflect new center user, or show error message
        if (changedUser) {
            showingLabel.setText("Showing: " + searchedUser + "'s friends");
            centerUserLabel.setText("Center User: " + searchedUser);
            statusLabel.setText("Status: User " + searchedUser + " is displayed");
            // change the friendList to the new user's friends
            friendList = (ArrayList<String>) socialNetwork.allFriends(searchedUser);
            // updateFriendList
            updateFriendLabelList(friendList);
        } else {
            statusLabel.setText("Status: User " + searchedUser + " not found");
        }
    }

    /**
     * Helper method that handles the search event. Changes the button 
     * appearances to match the new searched user
     * 
     * @param search the textfield that holds user input
     * @author connorkubiak
     */
    private void searchHelper(TextField search) {

        // temporary placeholder for the center user
        String centerUser = socialNetwork.getCenterUser();

        // retrieve text from text box
        String searchUser = search.getText();

        // if not found, update message box to 'user not found' and update window
        // running select(searchUser) should set them as center user in SocialNetwork
        if (!socialNetwork.select(searchUser)) {
            statusMessage = "Status: User " + searchUser + " not found";
            // update the status label
            statusLabel.setText(statusMessage);
            // set the center user back
            socialNetwork.select(centerUser);
        } else {
            // update the status label
            statusLabel.setText("Status: Found user: " + searchUser);
            this.searchedUser = searchUser;
            // set the center user back
            socialNetwork.select(centerUser);
            // change the button labels to reflect searched user
            shortestPathButton.setText("Shortest Path to " + searchUser);
            newCenterButton.setText("Set " + searchUser + " as center");
        }
    }

    /**
     * Updates the list on the right side of the window to display a supplied 
     * ArrayList of friends
     * 
     * @author connorkubiak
     */
    private void updateFriendLabelList(ArrayList<String> displayList) {

        // iterate through the list of friends
        for (int i = 0; i < 10; i++) {
            // if friend does not exist, change label to empty

            try {
                if (i < displayList.size() && displayList.get(i) != null) {

                    String friend = displayList.get(i);
                    
                    /*TODO*/ System.out.println("adding friend " + friend);

                    // display the name of the user
                    friendLabelList.get(i).setText(friend);
                    // on click, make the user center user
                    friendLabelList.get(i).setOnMouseClicked(e -> {
                        searchedUser = friend;
                        centerUserHelper(friend);
                        
                    }
                    );
                } else {
                    // if there is no element in the position of the list, set label to empty
                    friendLabelList.get(i).setText("");
                }
            } catch (NullPointerException e) {
                // if there is no element in the position of the list, set label to empty
                friendLabelList.get(i).setText("");
            }
        }
    }

    /**
     * Calculates the number of connected components and displays it in GUI
     * 
     * @author connorkubiak
     */
    private void conCompHelper() {
        conCompLabel.setText("Separate Networks: " + socialNetwork.getGroups());
    }

    /**
     * Runs the GUI
     * 
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}