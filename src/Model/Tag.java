package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A tag object
 *
 * @author Yuan Xu, Shiyi Tao
 * @version 2.0
 * @since 2017-11-30
 */
public class Tag implements Serializable {
    /**
     * the tag name
     */
    private String tagName;
    /**
     * all images that are related to this tag
     */
    private List<Photo> allPhotos;

    /**
     * instantiate a new tag object
     *
     * @param name the name of this tag object with "@" symbol
     */
    public Tag(String name) {
        this.tagName = name;
        this.allPhotos = new ArrayList<>();
    }

    /**
     * @return String representation of the tagName
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * @return List representation of the photo list
     */
    public List<Photo> getAllPhotos() {
        return allPhotos;
    }

    /**
     * add an photo to this tag
     *
     * @param photo an image instance
     */
    public void addPhoto(Photo photo) {
        if (!allPhotos.contains(photo))
            this.allPhotos.add(photo);
    }

    /**
     * remove a photo from this tag
     *
     * @param oldPhoto the old image that need to be deleted
     */
    public void deletePhoto(Photo oldPhoto) {
        if (allPhotos.contains(oldPhoto))
            this.allPhotos.remove(oldPhoto);
    }

    /**
     * check if the other object is the same as this tag object
     * return true if and only if two tag objects have the same name
     * return false if other is not a tag object or has a different tag name
     *
     * @param other the other object
     * @return if the two are the same
     */
    @Override
    public boolean equals(Object other) {
        return other instanceof Tag && ((Tag) other).getTagName().equals(this.getTagName());
    }

    /**
     * String representation of this object
     * Each Tag Object is represented by the TagName
     *
     * @return the String representation
     */
    @Override
    public String toString() {
        return this.getTagName();
    }
}
