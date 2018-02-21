package Model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * a photo instance for each photo file
 *
 * @author Yuan Xu, Shiyi Tao
 * @version 2.0
 * @since 2017-11-30
 */
public class Photo implements Serializable {


    /**
     * all the tags that are related to this image
     */
    private HashSet<Tag> currentTags;

    /**
     * the directory of this photo
     */
    private String directory;


    /**
     * create a new photo object
     *
     * @param directory the directory of this image file
     */
    public Photo(String directory) {
        this.currentTags = new HashSet<>();
        this.directory = directory;
    }

    /**
     * create a photo object with initial tag set
     *
     * @param directory the directory path of this image file
     * @param tagSet    the initial tag set for this image
     */
    public Photo(String directory, List<Tag> tagSet) {
        this(directory);
        currentTags.addAll(tagSet);
        // populate this photo to all the tags
        for (Tag tag : tagSet) {
            tag.addPhoto(this);
        }
    }

    /**
     * get a set of tags of this photo object
     *
     * @return a set of tags
     */
    public HashSet<Tag> getCurrentTags() {
        return currentTags;
    }

    /**
     * get the current folder that this photo is in
     *
     * @return the String representation of parent directory of this photo object
     */
    public String getFolderDirectory() {
        return directory.substring(0, directory.lastIndexOf(File.separator));
    }

    /**
     * get the updated directory of the image
     *
     * @return the String representation of the directory
     */
    public String getDirectory() {
        return this.directory;
    }

    /**
     * copy the file to the newPath directory from the oldPath directory
     *
     * @param oldPath the old directory of the file
     * @param newPath the new directory of the file
     */
    private static void moveFile(String oldPath, String newPath) {
        try {
            Path oldFile = new File(oldPath).toPath();
            Path newFile = new File(newPath).toPath();
            Files.move(oldFile, newFile, REPLACE_EXISTING);
        } catch (IOException e) {
            return;
        }
    }

    /**
     * precondition: the name with extension of the image is contained at the end of the directory
     * set the full directory of the current image
     *
     * @param directory the path of this photo
     */
    public void setDirectory(String directory) {
        String oldDir = getDirectory();
        this.directory = directory;
        Photo.moveFile(oldDir, directory);
    }

    /**
     * get the path of this image file without the extension at the end
     *
     * @return the file path without extension
     */
    public String getPathWithoutExtension() {
        String path = getDirectory();
        int endIndex = path.lastIndexOf(".");
        return path.substring(0, endIndex);
    }

    /**
     * get the image name with the extension (e.g. "pic1.jpg", "pic2.png")
     *
     * @return String representation of the file name plus extension
     */
    public String getNameWithExtension() {
        return new File(getDirectory()).getName();
    }

    /**
     * get the file extension from the directory (e.g. ".png", ".jpg", ".gif")
     *
     * @return String representation of the extension
     */
    public String getExtension() {
        String path = getDirectory();
        int endIndex = path.lastIndexOf(".");
        return path.substring(endIndex, path.length());
    }

    /**
     * Return true if new added tag is not in the HashSet
     * Return false if new added tag is already in the HashSet
     *
     * @param tag a new tag
     */
    public void addTag(Tag tag) {
        if (currentTags.add(tag)) {
            String newPath = getPathWithoutExtension() + " " + tag + getExtension();
            setDirectory(newPath);  // update directory
            tag.addPhoto(this);
        }
    }

    /**
     * Return true if the tag is in the HashSet
     * Return false if there is no such a tag in the HashSet
     *
     * @param tag unwanted tag
     */
    public void deleteTag(Tag tag) {
        if (currentTags.remove(tag)) {
            String newPath = directory.replace(" " + tag, "");
            setDirectory(newPath); // update directory
            tag.deletePhoto(this);
        }
    }


    /**
     * Return true if and only if two objects are Photo and they have the same directory
     * Return false otherwise
     *
     * @param obj the other object to be compared
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Photo && ((Photo) obj).getDirectory().equals(getDirectory());
    }

    /**
     * String representation of this object
     * Each Photo Object is represented by the AbsolutePath
     *
     * @return the String representation
     */
    @Override
    public String toString() {
        return new File(getDirectory()).getName();
    }

}
