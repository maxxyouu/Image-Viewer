package Test;

import Model.Database;
import Model.DatabaseManager;
import Model.Photo;
import Model.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Test class for Database.java
 *
 * @author Shiyi Tao
 * @version 2.0
 * @since 2017-11-30
 */
class DatabaseTest {

    /**
     * Create the the test objects set it as fields for later use.
     */
    private Database db;
    private DatabaseManager dbManager;
    private Photo p1, p2, p3, photoWithInitialTags;
    private Tag t1, t2, t3;
    private List<Tag> expectedTagList;
    private String photoPath1, photoPath2, photoPath3, photoPath4;

    /**
     * initialize all the test objects.
     * note: this method is called before each test method executes.
     */
    @BeforeEach
    void beforeEach() {
        dbManager = DatabaseManager.getDbManager();
        db = Database.getDatabase();

        photoPath1 = "D:" + File.separator + "s" + File.separator + "t" + File.separator + "pic1.jpg";
        photoPath2 = "D:" + File.separator + "s" + File.separator + "t" + File.separator + "pic2.jpg";
        photoPath3 = "D:" + File.separator + "s" + File.separator + "t" + File.separator + "pic3.jpg";
        photoPath4 = "D:" + File.separator + "s" + File.separator + "t" + File.separator + "pic4.jpg";

        // create 3 photo objects
        p1 = new Photo(photoPath1);
        p2 = new Photo(photoPath2);
        p3 = new Photo(photoPath3);

        // add the photo objects to the database
        db.addPhoto(p1);
        db.addPhoto(p2);
        db.addPhoto(p3);

        // create 3 tag objects
        t1 = new Tag("@t1");
        t2 = new Tag("@t2");
        t3 = new Tag("@t3");

        expectedTagList = new ArrayList<>();
        expectedTagList.add(t2);
        expectedTagList.add(t3);

        photoWithInitialTags = new Photo(photoPath4, expectedTagList);
        db.addPhoto(photoWithInitialTags);

        // add the tag objects to the tag list in the database
        db.addCurrentExistingTag(t1);
        db.addCurrentExistingTag(t2);
        db.addCurrentExistingTag(t3);

        // add all the tags to all photos
        p3.addTag(t2);
        p2.addTag(t2);
    }

    /**
     * after each test method calls, clean up the objects.
     */
    @AfterEach
    void afterEach() {
        db.getAllPhotos().clear();
        db.getCurrentExistingTags().clear();
        db = null;
        dbManager = null;
        p1 = null;
        p2 = null;
        p3 = null;
        t1 = null;
        t2 = null;
        t3 = null;
        expectedTagList = null;
        photoWithInitialTags = null;
    }

    /**
     * test if the database contains the tag by the tagname
     * Case1 : the database has t1 tag
     */
    @Test
    void testContainTagByNameCase1() {
        Assertions.assertTrue(db.containTag(t1.getTagName()));
    }

    /**
     * test if the database contains the tag by the tagname
     * Case2 : the database does not have t2 tag
     */
    @Test
    void testContainTagByNameCase2() {
        Assertions.assertFalse(db.containTag("@random"));
    }

    /**
     * test if the database retrieves the tag object is the same as the one expected
     * Case1 : the database contains the t1 object
     */
    @Test
    void testGetTagByNameCase1() {
        Assertions.assertEquals(t1, db.getTag(t1.getTagName()));
    }

    /**
     * test if the database retrieves the tag object is the same as the one expected
     * Case2 : the database does not contains the tag object
     */
    @Test
    void testGetTagByNameCase2() {
        Assertions.assertNull(db.getTag("@randomTag"));
    }

    /**
     * test if the photos in the database matches the expected one.
     */
    @Test
    void testGetAllPhoto() {
        List<Photo> allPhotos = db.getAllPhotos();
        List<Photo> expected = new ArrayList<>();
        expected.add(p1);
        expected.add(p2);
        expected.add(p3);
        expected.add(photoWithInitialTags);
        Assertions.assertEquals(expected, allPhotos);
    }

    /**
     * test if the database has the photo objects that are defined in the beforeEach.
     */
    @Test
    void testHasPhoto() {
        List<Photo> allPhotos = db.getAllPhotos();
        Assertions.assertTrue(allPhotos.contains(p1) && allPhotos.contains(p2) && allPhotos.contains(p3));
    }

    /**
     * test if the photo objects can correctly get all the tag that existing.
     */
    @Test
    void testGetCurrentExistingTags() {
        List<Tag> existingTags = db.getCurrentExistingTags();
        List<Tag> expected = new ArrayList<>();
        expected.add(t1);
        expected.add(t2);
        expected.add(t3);

        Assertions.assertEquals(expected, existingTags);
    }

    /**
     * test if the acceptable photo extensions equals to the expected one.
     */
    @Test
    void testGetAcceptableImageExtensions() {
        String[] acceptableImageExtensions = {"jpg", "jpeg", "png", "bmp", "gif"};
        Assertions.assertArrayEquals(acceptableImageExtensions, db.getAcceptableImageExtensions());
    }

    /**
     * test delete only 1 tag in the database.
     */
    @Test
    void testDeleteCurrentExistingTagCase1() {
        db.deleteCurrentExistingTag(t1);
        Assertions.assertFalse(db.getCurrentExistingTags().contains(t1));
    }

    /**
     * test delete 2 tags in the database.
     */
    @Test
    void testDeleteCurrentExistingTagCase2() {
        db.deleteCurrentExistingTag(t1);
        db.deleteCurrentExistingTag(t2);
        List<Tag> result = db.getCurrentExistingTags();
        Assertions.assertFalse(result.contains(t1) && result.contains(t2));
    }

    /**
     * test delete all garbage tags in the database.
     * Note: t2 and t3 are not garbage tags since there are photos that have those
     */
    @Test
    void testDeleteCurrentExistingTagCase3() {
        db.deleteCurrentExistingTag(t1);
        db.deleteCurrentExistingTag(t2);
        db.deleteCurrentExistingTag(t3);
        List<Tag> result = db.getCurrentExistingTags();
        Assertions.assertEquals(2, result.size());
    }

    /**
     * test the contain method of database.
     */
    @Test
    void testContainTag() {
        Assertions.assertTrue(db.containTag(t1) && db.containTag(t2) && db.containTag(t3));
    }

    /**
     * the tag is not in the database
     */
    @Test
    void testAddCurrentExistingTagCase1() {
        Tag newTag = new Tag("@newTag");
        db.addCurrentExistingTag(newTag);
        Assertions.assertTrue(db.getCurrentExistingTags().contains(newTag));
    }

    /**
     * the tag is already in the photo object
     */
    @Test
    void testAddCurrentExistingTagCase2() {
        db.addCurrentExistingTag(t1);
        // count number of "t1" tags in the database
        int count = 0;
        for (Tag tag : db.getCurrentExistingTags()) {
            if (tag.equals(t1)) {
                count++;
            }
        }
        Assertions.assertEquals(1, count);
    }

    /**
     * all the tags are already in the database
     */
    @Test
    void testAddAllCurrentExistingTagsCase1() {
        List<Tag> duplicatedTagList = new ArrayList<>();
        duplicatedTagList.add(t1);
        duplicatedTagList.add(t2);
        duplicatedTagList.add(t3);
        db.addAllCurrentExistingTags(duplicatedTagList);
        Assertions.assertEquals(3, db.getCurrentExistingTags().size());
    }


    /**
     * some of the tags are already in the database
     * note: tag t3 is not added to the expectedTagList
     */
    @Test
    void testAddAllCurrentExistingTagsCase2() {
        Tag newTag1 = new Tag("@newSampleTag1");
        Tag newTag2 = new Tag("@newSampleTag2");

        List<Tag> tagList = new ArrayList<>();
        tagList.add(t1);
        tagList.add(t2);
        tagList.add(newTag1);
        tagList.add(newTag2);

        db.addAllCurrentExistingTags(tagList);

        expectedTagList.add(newTag1);
        expectedTagList.add(newTag2);
        Assertions.assertTrue(db.getCurrentExistingTags().containsAll(tagList));
    }

    /**
     * none of the tags are in the database
     */
    @Test
    void testAddAllCurrentExistingTagsCase3() {
        List<Tag> newTags = new ArrayList<>();

        Tag newTag1 = new Tag("@newSampleTag4");
        Tag newTag2 = new Tag("@newSampleTag5");
        newTags.add(newTag1);
        newTags.add(newTag2);

        db.addAllCurrentExistingTags(newTags);

        expectedTagList.add(newTag1);
        expectedTagList.add(newTag2);
        Assertions.assertTrue(db.getCurrentExistingTags().containsAll(expectedTagList));
    }

    /**
     * the photo object is already in the database
     */
    @Test
    void testGetPhotoCase1() {
        Assertions.assertEquals(db.getPhoto(photoPath1), p1);
    }

    /**
     * the photo object is not in the database
     */
    @Test
    void testGetPhotoCase2() {
        String wrongPath = "D:" + File.separator + "s" + File.separator + "t" + File.separator + "wrongPath.jpg";
        Assertions.assertNull(db.getPhoto(wrongPath));
    }

    /**
     * the tag is indeed a garbage tag (no photo has this tag)
     * note: t1 is a garbage tag since no photo objects have this
     */
    @Test
    void testIsGarbageTagCase1() {
        Assertions.assertTrue(db.isGarbageTag(t1));
    }

    /**
     * the tag is not a garbage tag (All photos have this tag)
     * note: t1 tag is in all photo objects in the database
     */
    @Test
    void testIsGarbageTagCase2() {
        p1.addTag(t1);
        p2.addTag(t1);
        p3.addTag(t1);
        Assertions.assertFalse(db.isGarbageTag(t1));
    }

    /**
     * the tag is not a garbage tag (Only some photos have this tag)
     * note: t2 only exists in two photo objects in the database
     */
    @Test
    void testIsGarbageTagCase3() {
        p1.addTag(t2);
        p2.addTag(t2);
        Assertions.assertFalse(db.isGarbageTag(t2));
    }

    /**
     * the photo object does not have initial tag set
     * note: By default the photo object has a empty tag set
     */
    @Test
    void testGetTagLogCase1() {
        Assertions.assertEquals(db.getTagLog(p2).size(), 1);
    }

    /**
     * the photo object does have initial tag set
     */
    @Test
    void testGetTagLogCase2() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // access the private method in databaseManager
        Method logTag = dbManager.getClass().getDeclaredMethod("logTags", Photo.class);
        logTag.setAccessible(true);
        // call the logTag method to add a set of tags
        logTag.invoke(dbManager, photoWithInitialTags);

        HashSet<Tag> expectedResult = new HashSet<>(expectedTagList);
        // check if the tag sets contain the expected tag list
        Assertions.assertTrue(db.getTagLog(photoWithInitialTags).contains(expectedResult));
        // check if the tag sets contain only two hashsets (one is empty set and the other is expectedTagList)
        Assertions.assertEquals(db.getTagLog(photoWithInitialTags).size(), 2);
    }

    /**
     * the photo object that already has a list of tags but NOT in the database
     */
    @Test
    void testAddPhotoCase1() {
        String path = "D:" + File.separator + "s" + File.separator + "t" + File.separator + "differentPhoto.jpg";
        Photo newPhoto = new Photo(path, expectedTagList);
        db.addPhoto(newPhoto);
        // check if the photo is in the database
        Assertions.assertTrue(db.getAllPhotos().contains(newPhoto));
    }

    /**
     * the photo object that already has a list of tags but IN the database
     * note: before the test case starts to run, the db has 4 photos
     */
    @Test
    void testAddPhotoCase2() {
        db.addPhoto(p2);
        // check number of p1 objects in the db
        int count = 0;
        List<Photo> allPhotos = db.getAllPhotos();
        for (Photo photo : allPhotos) {
            if (photo.equals(p1)) {
                count++;
            }
        }
        Assertions.assertEquals(count, 1);
        Assertions.assertEquals(4, allPhotos.size());
    }
}
