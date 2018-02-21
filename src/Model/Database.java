package Model;

import java.io.Serializable;
import java.util.*;

/**
 * Database class stores all the information for photos and tags
 *
 * @author Yuan Xu, Shiyi Tao
 * @version 2.0
 * @since 2017-11-30
 */
public class Database extends Observable implements Serializable {

    /**
     * a singleton database object
     */
    private static Database database = new Database();
    /**
     * store all the photo objects
     */
    private List<Photo> allPhotos;
    /**
     * a history of sets of tag for each image
     */
    private HashMap<Photo, List<HashSet<Tag>>> tagLogs;
    /**
     * store all currently existing UNIQUE tags. Tagname: Tag obj
     */
    private List<Tag> currentExistingTags;
    /**
     * the accepted photo extensions
     */
    private String[] acceptableImageExtensions = {"jpg", "jpeg", "png", "bmp", "gif"};

    /**
     * create a database object
     */
    private Database() {
        allPhotos = new ArrayList<>();
        currentExistingTags = new ArrayList<>();
        tagLogs = new HashMap<>();
    }

    /**
     * getter for the database object
     *
     * @return the database object
     */
    public static Database getDatabase() {
        return database;
    }

    /**
     * get all photos stored in the database
     *
     * @return a list of all photos
     */
    public List<Photo> getAllPhotos() {
        return allPhotos;
    }

    /**
     * check if the database has the photo given the photo path
     *
     * @param photoPath a photo path
     * @return if this database has this photo
     */
    public boolean hasPhoto(String photoPath) {
        for (Photo photo : allPhotos) {
            if (photo.getDirectory().equals(photoPath))
                return true;
        }
        return false;
    }

    /**
     * get all the tag logs
     *
     * @return a hash map of tag logs
     */
    public HashMap<Photo, List<HashSet<Tag>>> getTagLogs() {
        return tagLogs;
    }

    /**
     * get all the existing tags
     *
     * @return all the unique tags in the database
     */
    public List<Tag> getCurrentExistingTags() {
        return currentExistingTags;
    }

    /**
     * get all the acceptable extension
     *
     * @return a array of acceptable image extensions
     */
    public String[] getAcceptableImageExtensions() {
        return acceptableImageExtensions;
    }

    /**
     * delete tag object from the database, if the tag is not in every photo instance
     *
     * @param tag a tag object to be deleted
     */
    public void deleteCurrentExistingTag(Tag tag) {
        if (containTag(tag) && isGarbageTag(tag)) {
            currentExistingTags.remove(tag);
            setChanged();
            notifyObservers();
        }
    }

    /**
     * check if the database contain this tag
     *
     * @param tag the tag object to be checked
     * @return if the database has this tag
     */
    public boolean containTag(Tag tag) {
        return currentExistingTags.contains(tag);
    }

    /**
     * check if the database contains a tag that has this tag name
     *
     * @param tagName the tag name to be checked
     * @return return true if the database contains the tag object else null
     */
    public boolean containTag(String tagName) {
        for (Tag tag : currentExistingTags) {
            if (tag.getTagName().equals(tagName))
                return true;
        }
        return false;
    }

    /**
     * get the tag object if the tagName exist else null
     *
     * @param tagName the tagname of the tag object
     * @return the tag object
     */
    public Tag getTag(String tagName) {
        for (Tag tag : currentExistingTags) {
            if (tag.getTagName().equals(tagName))
                return tag;
        }
        return null;
    }

    /**
     * add a new tag object into the database, if the tag object is already exist in the databse, ignore.
     *
     * @param tag the tag object
     */
    public void addCurrentExistingTag(Tag tag) {
        if (!currentExistingTags.contains(tag)) {
            currentExistingTags.add(tag);
            setChanged();
            notifyObservers();
        }

    }

    /**
     * add a set of tags to the currentExistingTag list
     *
     * @param tagSet a set of tags to be added
     */
    public void addAllCurrentExistingTags(List<Tag> tagSet) {
        for (Tag tag : tagSet) {
            addCurrentExistingTag(tag);
        }
    }

    /**
     * add an image to the database
     *
     * @param img the image object
     */
    public void addPhoto(Photo img) {
        if (!allPhotos.contains(img)) {
            allPhotos.add(img);
            List<HashSet<Tag>> sets = new ArrayList<>();
            sets.add(new HashSet<>());
            tagLogs.put(img, sets);

            setChanged();
            notifyObservers();
        }
    }

    /**
     * get the image from the provided root directory
     *
     * @param path the path of the photo object
     * @return the photo object
     */
    public Photo getPhoto(String path) {
        for (Photo pic : allPhotos) {
            if (pic.getDirectory().equals(path))
                return pic;
        }
        return null;
    }

    /**
     * if the tag is not in the database and no photo has this tag
     * then return true
     *
     * @param tag the tag name of the tag object
     * @return if the tag is a garbage
     */
    public boolean isGarbageTag(Tag tag) {
        int counter = allPhotos.size();
        for (Photo photo : allPhotos) {
            if (!photo.getCurrentTags().contains(tag)) {
                counter--;
            }
        }
        return counter <= 0;
    }

    /**
     * get a list of old set of tags for a particular photo
     *
     * @param photo the photo object that need to retrieve old tag sets
     * @return a list of old tag sets
     */
    public List<HashSet<Tag>> getTagLog(Photo photo) {
        return tagLogs.get(photo);
    }

}
