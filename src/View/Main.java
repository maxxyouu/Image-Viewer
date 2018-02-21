package View;

import Model.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Main class of our program
 * Initialize all the information needed for the program
 *
 * @author Jianzhong You, Yuan Xu, Shiyi Tao
 * @version 2.0
 * @since 2017-11-30
 */
public class Main extends Application {

    /**
     * initialize the mainView of this application
     */
    private View mainView = View.getView();

    /**
     * a singleton database object
     */
    private Database db = Database.getDatabase();

    /**
     * a singleton serializer object
     */
    private ApplicationSerializer serializer = ApplicationSerializer.getSerializer();

    /**
     * a singleton deserializer object
     */
    private ApplicationDeserializer deserializer = ApplicationDeserializer.getDeserializer();

    /**
     * run the application here
     *
     * @param args any string array
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainView.initializeComponents(primaryStage);
        primaryStage.setTitle("Final Project");
        primaryStage.setScene(mainView.getScene());

        deserializeApplication();

        primaryStage.show();
    }

    /**
     * deserialize all the previous data and added to the current database if the database does nt exist
     */
    private void deserializeApplication() {
        if (new File("Database.ser").exists()) {
            Database oldDb = (Database) deserializer.deserialize("Database");
            populateOldData(oldDb);
        }
    }

    /**
     * load all the previous database's data into the current database object
     * including existing tags, previous photo objects and tag sets for each photo
     *
     * @param oldDb the old database
     */
    private void populateOldData(Database oldDb) {
        List<Photo> oldPhoto = oldDb.getAllPhotos();
        List<Tag> oldTags = oldDb.getCurrentExistingTags();
        HashMap<Photo, List<HashSet<Tag>>> oldTagLogs = oldDb.getTagLogs();
        for (Tag tag : oldTags) {
            db.addCurrentExistingTag(tag);
        }
        for (Photo photo : oldPhoto) {
            db.addPhoto(photo);
        }
        for (Photo photo : oldTagLogs.keySet()) {
            List<HashSet<Tag>> oldTagSets = oldTagLogs.get(photo);
            oldTagSets.remove(0);
            db.getTagLog(photo).addAll(oldTagSets);
        }
    }

    /**
     * serialized all the database data(Photos, Tags) into Database.ser file before the application is closed
     */
    @Override
    public void stop() {
        serializer.serialize(db, "Database");
    }
}
