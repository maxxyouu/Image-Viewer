package Test;

import Model.Photo;
import Model.Tag;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for Photo.java
 *
 * @author Yuan Xu
 * @version 2.0
 * @since 2017-11-30
 */
class PhotoTest {

    /**
     * photo objects for testing
     */
    private Photo p1, p2;
    /**
     * tag objects for testing
     */
    private Tag tag1, tag2, tag3;

    /**
     * before each test, create a new p1 object with three tags in the tagset
     */
    @BeforeEach
    void beforeEach() {
        List<Tag> tagSet = new ArrayList<>();
        tag1 = new Tag("@1");
        tag2 = new Tag("@2");
        tag3 = new Tag("@3");
        tagSet.add(tag1);
        tagSet.add(tag2);
        tagSet.add(tag3);
        String d1 = "D:" + File.separator + "s" + File.separator + "t" + File.separator + "pic1.jpg";
        p1 = new Photo(d1);
        String d2 = "D:" + File.separator + "s" + File.separator + "t" + File.separator + "pic1 @1 @2 @3.jpg";
        p2 = new Photo(d2, tagSet);
    }


    /**
     * Test if the method returns the correct HashSet with expected tags
     */
    @Test
    void testGetCurrentTags() {
        HashSet<Tag> expectedResult = new HashSet<>();
        expectedResult.add(new Tag("@1"));
        expectedResult.add(new Tag("@2"));
        expectedResult.add(new Tag("@3"));
        HashSet<Tag> actualResult = p1.getCurrentTags();
        Boolean x = true;
        for (Tag tag : actualResult) {
            if (!expectedResult.contains(tag)) {
                x = false;
            }
        }
        assertEquals(true, x);
    }


    /**
     * Test if the method returns only the folder directory
     */
    @Test
    void testGetFolderDirectory() {
        String expectedResult = "D:" + File.separator + "s" + File.separator + "t";
        assertEquals(expectedResult, p1.getFolderDirectory());
    }

    /**
     * Test if the method returns the full path
     */
    @Test
    void testGetDirectory() {
        String expectedResult = "D:" + File.separator + "s" + File.separator + "t" + File.separator + "pic1.jpg";
        assertEquals(expectedResult, p1.getDirectory());
    }

    /**
     * Test if the method sets the directory correctly
     */
    @Test
    void testSetDirectory() {
        String newDirectory = "C:" + File.separator + "s" + File.separator + "t" + File.separator + "pic1.jpg";
        String expectedResult = "C:" + File.separator + "s" + File.separator + "t" + File.separator + "pic1.jpg";
        p1.setDirectory(newDirectory);
        assertEquals(expectedResult, p1.getDirectory());
    }

    /**
     * Test if the method returns the full path without extension
     */
    @Test
    void testGetPathWithoutExtension() {
        String expectedResult = "D:" + File.separator + "s" + File.separator + "t" + File.separator + "pic1";
        assertEquals(expectedResult, p1.getPathWithoutExtension());
    }

    /**
     * Test the method if it only returns the p1 name plus the extention. (e.g. "pic1.jpg", "pic2.png")
     */
    @Test
    void testGetPahWithExtension() {
        String expectedResult = "pic1.jpg";
        assertEquals(expectedResult, p1.getNameWithExtension());
    }


    /**
     * Test the method if it only returns the extention. (e.g. ".jpg", ".png", "gif")
     */
    @Test
    void testGetExtension() {
        String expectedResult = ".jpg";
        assertEquals(expectedResult, p1.getExtension());
    }

    /**
     * Test the method if we add a tag with same name (case repeated add)
     * File name will not change.
     */
    @Test
    void testAddTagCase1() {
        p2.addTag(tag1);
        String expectedResult = "D:" + File.separator + "s" + File.separator + "t" + File.separator + "pic1 @1 @2 @3.jpg";
        assertEquals(expectedResult, p2.getDirectory());
    }

    /**
     * Test the method if we add a new tag
     * File name will be rename the with the added tag.
     */
    @Test
    void testAddTagCase2() {
        Tag tag5 = new Tag("@5");
        p1.addTag(tag5);
        String expectedResult = "D:" + File.separator + "s" + File.separator + "t" + File.separator + "pic1" + " @5.jpg";
        assertEquals(expectedResult, p1.getDirectory());
    }


    /**
     * Test the method if we remove a tag from the HashSet
     * File name will be renamed
     */
    @Test
    void testDeleteTagCase1() {
        Tag tag1 = new Tag("@1");
        p1.deleteTag(tag1);
        String expectedResult = "D:" + File.separator + "s" + File.separator + "t" + File.separator + "pic1.jpg";
        assertEquals(expectedResult, p1.getDirectory());

    }


    /**
     * Test the method if we remove a tag that is not from the HashSet
     * File name will not change
     */
    @Test
    void testDeleteTagCase2() {
        Tag tag6 = new Tag("@6");
        p1.deleteTag(tag6);
        String expectedResult = "D:" + File.separator + "s" + File.separator + "t" + File.separator + "pic1.jpg";
        assertEquals(expectedResult, p1.getDirectory());

    }


    /**
     * Test a new Photo Object with the same directory
     */
    @Test
    void testEqualsCase1() {
        Photo photo2 = new Photo("D:" + File.separator + "s" + File.separator + "t" + File.separator + "pic1.jpg");
        assertEquals(true, p1.equals(photo2));
    }


    /**
     * Test a new Photo Object with the different directory
     */
    @Test
    void testEqualsCase2() {
        Photo photo3 = new Photo("D:" + File.separator + "s" + File.separator + "t" + File.separator + "pic3.jpg");
        assertEquals(false, p1.equals(photo3));
    }

    /**
     * Test a non-Photo Object
     */
    @Test
    void testEqualsCase3() {
        String x = "my little Max";
        assertEquals(false, p1.equals(x));
    }

    /**
     * Test the toString method
     * It should return a string with specific path under different operating system
     */
    @Test
    void testToString() {
        String expectedResult = "pic1.jpg";
        String actualResult = p1.getNameWithExtension();
        assertEquals(expectedResult, actualResult);
    }

}