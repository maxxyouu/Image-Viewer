package Model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;

/**
 * the business logic for the database
 *
 * @author Yuan Xu, Shiyi Tao
 * @version 2.0
 * @since 2017-11-30
 */
public class DatabaseManager extends Observable {

    /**
     * a singleton databaseManager object
     */
    private static DatabaseManager dbManager = new DatabaseManager();
    /**
     * a singleton database object
     */
    private Database db = Database.getDatabase();

    /**
     * get the databaseManager object
     *
     * @return the singleton databaseManager object
     */
    public static DatabaseManager getDbManager() {
        return dbManager;
    }

    /**
     * replace all the tag in the photo with a new set of tags
     *
     * @param photo a photo object
     * @param tags  a new list of tags
     */
    public void replaceAllTags(Photo photo, List<Tag> tags) {
        HashSet<Tag> currentTags = new HashSet<>(photo.getCurrentTags());
        String oldName = photo.getNameWithExtension();
        for (Tag tag : currentTags) {
            photo.deleteTag(tag);
            db.deleteCurrentExistingTag(tag);
        }
        addTags(photo, tags, oldName);
    }

    /**
     * add a list of tags into the database
     *
     * @param photo   the photo object
     * @param tags    a list of tags
     * @param oldName the old name of the photo
     */
    private void addTags(Photo photo, List<Tag> tags, String oldName) {
        for (Tag tag : tags) {
            // add the tag into the photo object
            photo.addTag(tag);
            db.addCurrentExistingTag(tag);
        }
        logTags(photo);
        // use the observer pattern here
        setChanged();
        notifyObservers(dataMaps(oldName, photo.getNameWithExtension()));
    }

    /**
     * add a list of tags into the database
     *
     * @param photo the photo object
     * @param tags  a list of tags
     */
    public void addTags(Photo photo, List<Tag> tags) {
        String oldName = photo.getNameWithExtension();
        for (Tag tag : tags) {
            // add the tag into the photo object
            photo.addTag(tag);
            db.addCurrentExistingTag(tag);
        }
        logTags(photo);

        // use the observer pattern here
        setChanged();
        notifyObservers(dataMaps(oldName, photo.getNameWithExtension()));
    }

    /**
     * delete a list of tags from the photo object
     *
     * @param photo the photo object
     * @param tags  a list of tags to be deleted from the photo
     */
    public void deleteTags(Photo photo, List<Tag> tags) {
        String oldName = photo.getNameWithExtension();
        for (Tag tag : tags) {
            photo.deleteTag(tag);
            db.deleteCurrentExistingTag(tag);
        }
        logTags(photo);
        // use the observer pattern here
        setChanged();
        notifyObservers(dataMaps(oldName, photo.getNameWithExtension()));
    }

    /**
     * assume the tag list inside the photo is updated
     *
     * @param photo the photo object in the database
     */
    private void logTags(Photo photo) {
        // get all old tag sets from the photo in the database
        List<HashSet<Tag>> tagSets = db.getTagLog(photo);
        // get all current tags from this photo
        HashSet<Tag> updatedTagSet = new HashSet<>(photo.getCurrentTags());
        // Log the tag set into the database
        if (!tagSets.contains(updatedTagSet)) {
            tagSets.add(updatedTagSet);
        }
    }


    /**
     * A helper function to gather name information in order to pass it to the observers
     *
     * @param oldName the old name of the photo object
     * @param newName the new name of the photo object
     * @return the hashmap that contains the old name and new name data
     */
    private HashMap<String, String> dataMaps(String oldName, String newName) {
        HashMap<String, String> data = new HashMap<>();
        data.put("oldName", oldName);
        data.put("newName", newName);
        return data;
    }
}
