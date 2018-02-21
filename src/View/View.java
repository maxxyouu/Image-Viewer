package View;

import Controller.*;
import Model.*;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.List;

/**
 * GUI class
 *
 * @author Jianzhong You
 * @version 2.0
 * @since 2017-11-30
 */
public class View {

    /**
     * GUI information
     */
    private Scene scene;
    private Text statusMessage;
    private HBox imageBox;
    private ImageView imageWindow;
    private TextField newTagTextField;
    private Window sourceWindow;
    private Photo currentActivePhoto;

    /**
     * Text information for the GUI and buttons
     */
    private Text urlText;

    /**
     * Fields for all information from the ListView
     */
    private ListView<Photo> imgInDirectory;
    private ListView<Photo> imgsFromTag;
    private ListView<Tag> availableTags;
    private ListView<List<Tag>> oldTags;

    /**
     * Fields for all buttons
     */
    private Button loadImages;
    private Button addTag;
    private Button deleteTag;
    private Button chooseOldTags;
    private Button log;
    private Button moveFile;
    private Button openFolder;
    private Button addNewTag;

    /**
     * Static singleton view object
     */
    private static View view = new View();

    /**
     * a singleton database object
     */
    private Database db = Database.getDatabase();

    /**
     * a singleton databaseManager object
     */
    private DatabaseManager dbManager = DatabaseManager.getDbManager();

    /**
     * static method that return the View of the whole GUI
     *
     * @return View object of GUI
     */
    public static View getView() {
        return view;
    }

    /**
     * Return the Text information stored in each button
     *
     * @return Text information
     */
    public Text getUrlText() {
        return urlText;
    }

    /**
     * get the List of tag from the listview
     *
     * @return a list of tag
     */
    ListView<List<Tag>> getOldTags() {
        return oldTags;
    }

    /**
     * get the current Active Photo object from the list
     *
     * @return a Photo object
     */
    public Photo getCurrentActivePhoto() {
        return currentActivePhoto;
    }

    /**
     * Set a new currentActivePhoto
     *
     * @param currentActivePhoto
     */
    public void setCurrentActivePhoto(Photo currentActivePhoto) {
        this.currentActivePhoto = currentActivePhoto;
    }

    /**
     * protected method that returns the Scene of the whole GUI
     *
     * @return Scene object of GUI
     */
    Scene getScene() {
        return scene;
    }

    /**
     * method that returns the Window of the whole GUI
     *
     * @return Window object of GUI
     */
    public Window getSourceWindow() {
        return sourceWindow;
    }

    /**
     * method that returns the "addNewTag" button
     *
     * @return a button
     */
    public Button getAddNewTag() {
        return addNewTag;
    }

    /**
     * get the TestField of GUI
     *
     * @return TestField object of GUI
     */
    public TextField getNewTagTextField() {
        return newTagTextField;
    }

    /**
     * get the message for status
     *
     * @return Test
     */
    public Text getStatusMessage() {
        return statusMessage;
    }

    /**
     * get the List of photo from the directory
     *
     * @return a list of photo
     */
    public ListView<Photo> getImgInDirectory() {
        return imgInDirectory;
    }

    /**
     * get the List of photo from the tag
     *
     * @return a list of photo
     */
    public ListView<Photo> getImgsFromTag() {
        return imgsFromTag;
    }

    /**
     * get all the available tags from GUI
     *
     * @return a list of tag
     */
    public ListView<Tag> getAvailableTags() {
        return availableTags;
    }

    /**
     * @return an ImageView object for GUI
     */
    public ImageView getImageWindow() {
        return imageWindow;
    }

    /**
     * Display the selected Image on GUI
     *
     * @param img
     */
    public void displaySelectedImage(Image img) {
        imageWindow.setImage(img);
    }

    /**
     * return the "loadImages" button
     *
     * @return a button
     */
    public Button getLoadImages() {
        return loadImages;
    }

    /**
     * return the "addTag" button
     *
     * @return a button
     */
    public Button getAddTag() {
        return addTag;
    }

    /**
     * return the "deleteTag" button
     *
     * @return a button
     */
    public Button getDeleteTag() {
        return deleteTag;
    }

    /**
     * return the "chooseOldTags" button
     *
     * @return a button
     */
    public Button getChooseOldTags() {
        return chooseOldTags;
    }

    /**
     * return the "log" button
     *
     * @return a button
     */
    public Button getLog() {
        return log;
    }

    /**
     * return the "moveFile" button
     *
     * @return a button
     */
    public Button getMoveDirectory() {
        return moveFile;
    }

    /**
     * return the "openFolder" button
     *
     * @return a button
     */
    public Button getOpenFolder() {
        return openFolder;
    }

    /**
     * initial all GUI components
     *
     * @param primaryStage the main stage of this application
     */
    void initializeComponents(Stage primaryStage) {
        this.sourceWindow = primaryStage;

        Pane pane = new Pane();

        VBox vbox = new VBox();
        vbox.setLayoutX(14);
        vbox.setLayoutY(14);
        vbox.setPrefSize(180, 573);

        Label l1 = new Label("List of Images in directory");
        l1.setPrefSize(285, 17);

        imgInDirectory = new ListView<>();
        imgInDirectory.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        imgInDirectory.setPrefSize(284, 300);

        Label l2 = new Label("All images from the selected tag");
        l2.setPrefHeight(17);
        l2.setFont(Font.font(11));

        imgsFromTag = new ListView<>();
        imgsFromTag.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        imgsFromTag.setPrefHeight(300);

        vbox.getChildren().addAll(l1, imgInDirectory, l2, imgsFromTag);

        urlText = new Text("Current Photo Absolute Path:");
        urlText.setLayoutX(210);
        urlText.setLayoutY(28);

        imageBox = new HBox();
        imageBox.setPrefSize(570, 320);
        imageBox.setLayoutX(210);
        imageBox.setLayoutY(32);
        imageBox.setAlignment(Pos.CENTER);

        imageWindow = new ImageView();
        imageWindow.setFitHeight(320);
        imageWindow.setFitWidth(570);

        imageWindow.setPreserveRatio(true);
        imageBox.getChildren().addAll(imageWindow);

        // first buttons group
        HBox b1 = new HBox();
        b1.setAlignment(Pos.CENTER);
        b1.setLayoutX(210);
        b1.setLayoutY(525);
        b1.setPrefSize(570, 30);

        double defaultButtonWidth = 112.5; // used to be 120

        newTagTextField = new TextField();
        newTagTextField.setPrefSize(defaultButtonWidth, 25);
        addTag = new Button("Add Exist Tags");
        deleteTag = new Button("Delete Exist Tags");
        newTagTextField.setPrefSize(defaultButtonWidth, 25);
        addTag.setPrefHeight(25);
        addTag.setPrefWidth(defaultButtonWidth);
        addTag.setPrefSize(defaultButtonWidth, 25);
        deleteTag.setPrefSize(defaultButtonWidth, 25);
        chooseOldTags = new Button("Reverse Back To Old Tag Set");
        chooseOldTags.setPrefSize(defaultButtonWidth * 2, 25);
        b1.getChildren().addAll(newTagTextField, addTag, deleteTag, chooseOldTags);

        statusMessage = new Text("Status Message:");
        statusMessage.setLayoutX(210);
        statusMessage.setLayoutY(365);

        HBox tagBox = new HBox();
        tagBox.setAlignment(Pos.TOP_CENTER);
        tagBox.setLayoutX(210);
        tagBox.setLayoutY(370);
        tagBox.setPrefSize(570, 140);

        availableTags = new ListView<>();
        availableTags.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        availableTags.setPrefSize(120, 140);

        oldTags = new ListView<>();
        oldTags.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        oldTags.setPrefSize(460, 140);

        tagBox.getChildren().addAll(availableTags, oldTags);

        HBox b2 = new HBox();
        b2.setAlignment(Pos.CENTER);
        b2.setLayoutX(210);
        b2.setLayoutY(555);
        b2.setPrefSize(570, 30);

        addNewTag = new Button("Add New Tag");
        addNewTag.setPrefSize(defaultButtonWidth, 25);
        moveFile = new Button("Move File To");
        moveFile.setPrefHeight(25);
        moveFile.setPrefWidth(defaultButtonWidth);
        log = new Button("Show Name log");
        log.setPrefSize(defaultButtonWidth, 25);
        loadImages = new Button("Import Photos");
        loadImages.setPrefSize(defaultButtonWidth, 25);
        openFolder = new Button("Open Folder");
        openFolder.setPrefSize(defaultButtonWidth, 25);
        // The following dedicates to change the font size of all the buttons in the GUI
        int fontSize = 10;
        addTag.setFont(Font.font(fontSize));
        loadImages.setFont(Font.font(fontSize));
        deleteTag.setFont(Font.font(fontSize));
        chooseOldTags.setFont(Font.font(fontSize));
        log.setFont(Font.font(fontSize));
        moveFile.setFont(Font.font(fontSize));
        openFolder.setFont(Font.font(fontSize));
        addNewTag.setFont(Font.font(fontSize));
        b2.getChildren().addAll(addNewTag, log, moveFile, openFolder, loadImages);
        pane.getChildren().addAll(vbox, urlText, imageBox, b1, statusMessage, tagBox, b2);
        int height = 600;
        int width = 800;
        scene = new Scene(pane, width, height);

        loadControllers();
    }

    /**
     * get the selected tags from the tag listview
     *
     * @return a list of selected tags
     */
    public ObservableList<Tag> getSelectedTags() {
        return getAvailableTags().getSelectionModel().getSelectedItems();
    }

    /**
     * get the selected photo under the directory
     *
     * @return the photo name
     */
    public Photo getSelectedImgUnderDirectory() {
        return getImgsFromTag().getSelectionModel().getSelectedItem();
    }

    /**
     * get the selected photo in the directory
     *
     * @return the photo name
     */
    public Photo getSelectedImgInDirectory() {
        return getImgInDirectory().getSelectionModel().getSelectedItem();
    }

    /**
     * the old tag set that the user is selected
     *
     * @return the selected old tag set
     */
    public List<Tag> getSelectedOldTagSet() {
        return getOldTags().getSelectionModel().getSelectedItem();
    }

    /**
     * load all the controllers
     * 1. load fileController to loadImages, openFolder, log, and moveFile buttons for event handling
     * 2. load tagController to chooseOldTags, addNewTag, addTag, and deleteTag for event handling
     * 3. load listViewController to imgsFromTag, imgInDirectory, and availableTags list view for event handling
     * 4. load dragDropController to handle drag and drop events raised by the imageBox
     */
    private void loadControllers() {
        ListViewController listViewController = ListViewController.getListViewController();
        FileController fileController = FileController.getFileController();
        TagController tagController = TagController.getTagController();

        // assign the controllers
        loadImages.setOnAction(fileController);
        openFolder.setOnAction(fileController);
        moveFile.setOnAction(fileController);

        chooseOldTags.setOnAction(tagController);
        addNewTag.setOnAction(tagController);
        addTag.setOnAction(tagController);
        deleteTag.setOnAction(tagController);

        log.setOnAction(fileController);

        imgsFromTag.setOnMouseClicked(listViewController);
        imgInDirectory.setOnMouseClicked(listViewController);
        // newly features
        availableTags.setOnMouseClicked(listViewController);

        DragDropController dragDropController = DragDropController.getDragDropController();
        // the following are new feature
        imageBox.setOnDragOver(dragDropController);
        imageBox.setOnDragDropped(dragDropController);

        // load the observers to the databaseManager and the database
        dbManager.addObserver(ViewAgent.getViewAgent());
        db.addObserver(ViewAgent.getViewAgent());
        dbManager.addObserver(DataLogger.getDataLogger());
    }
}