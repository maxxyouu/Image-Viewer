package View;

import Model.Database;
import Model.Photo;
import Model.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.util.*;

/**
 * A agent class that responsible to update the GUI
 *
 * @author Jianzhong You, Yuan Xu, Shiyi Tao
 * @version 2.0
 * @since 2017-11-30
 */
public class ViewAgent implements Observer {

    /**
     * store a viewAgent object
     */
    private static ViewAgent viewAgent = new ViewAgent();
    /**
     * a singleton view object
     */
    private View view = View.getView();
    /**
     * a singleton database object
     */
    private Database db = Database.getDatabase();

    /**
     * get the viewAgent object
     *
     * @return the viewAgent object
     */
    public static ViewAgent getViewAgent() {
        return viewAgent;
    }

    /**
     * update the status messge on the application
     *
     * @param message the message to be sent
     */
    public void updateStatusMessage(String message) {
        // get the string 'status message:'
        Text status = view.getStatusMessage();
        String text = status.getText();
        int end = text.indexOf(":");
        String statusLabel = text.substring(0, end + 1);
        // set the new message to display
        status.setText(statusLabel + " " + message);
    }

    /**
     * Show the absolute path of the currently selected photo
     */
    public void updateCurrentPhotoPath() {
        Photo currentPhoto = view.getCurrentActivePhoto();
        if (currentPhoto != null) {
            String currentPhotoPath = currentPhoto.getDirectory();
            // get the string 'status message:'
            Text currentPath = view.getUrlText();
            String text = currentPath.getText();
            int end = text.indexOf(":");
            String label = text.substring(0, end + 1);
            // set the new message to display
            currentPath.setText(label + " " + currentPhotoPath);
        }
    }

    /**
     * refresh the photo list views in GUI
     */
    public void refreshPhotoListViews() {
        // get the selected photos
        ListView<Photo> imgInDir = view.getImgInDirectory();
        // get the selected item index
        int selectedIndexInDirectory = imgInDir.getSelectionModel().getSelectedIndex();

        ObservableList<Photo> photosInDir = imgInDir.getItems();

        imgInDir.setItems(null);
        imgInDir.setItems(photosInDir);
        imgInDir.getSelectionModel().select(selectedIndexInDirectory);
    }

    /**
     * refresh the photo list views from the selected tags
     */
    public void refreshPhotoListViewByTag() {

        // get the list of selected tags from the list view
        List<Tag> selectedTags = new ArrayList<>(view.getSelectedTags());

        // get all the items from the photo list view
        ObservableList<Photo> photoListView = view.getImgsFromTag().getItems();

        List<Photo> resultImgs = new ArrayList<>();
        if (selectedTags.size() > 0) {
            for (Tag tag : selectedTags) {
                // get all photos that have this tag
                if (tag != null) {
                    List<Photo> allPhotos = tag.getAllPhotos();
                    // for each photo, if the photo is not in the list view: add it, else: ignore
                    for (Photo photo : allPhotos) {
                        if (!resultImgs.contains(photo)) {
                            resultImgs.add(photo);
                        }
                    }
                }
            }
            photoListView.clear();
            photoListView.addAll(resultImgs);
        }
    }

    /**
     * refresh the existing tags list view by retrieve all the unique tags from the database
     */
    public void refreshTagListView() {
        List<Tag> updatedExistingTags = db.getCurrentExistingTags();
        ObservableList<Tag> availableTags = view.getAvailableTags().getItems();
        // perform delete
        for (int i = 0; i < availableTags.size(); i++) {
            Tag tag = availableTags.get(i);
            if (!updatedExistingTags.contains(tag)) {
                availableTags.remove(i);
            }
        }
        // perform add
        for (Tag tag : updatedExistingTags) {
            if (!availableTags.contains(tag)) {
                availableTags.add(tag);
            }
        }
    }

    /**
     * refresh the old tags list view according to the current selected photo
     */
    public void refreshOldTagListView() {
        Photo photo = view.getCurrentActivePhoto();
        List<HashSet<Tag>> tagLog = db.getTagLog(photo);
        if (tagLog != null) {
            List<List<Tag>> result = new ArrayList<>();
            for (HashSet<Tag> tagSet : tagLog) {
                List<Tag> tagList = new ArrayList<>(tagSet);
                result.add(tagList);
            }
            view.getOldTags().setItems(null);
            view.getOldTags().setItems(FXCollections.observableArrayList(result));
        }
    }

    /**
     * Update the informations about the photo dynamically in the GUI
     */
    public void refresh() {
        refreshPhotoListViewByTag();
        refreshPhotoListViews();
        refreshTagListView();
        refreshOldTagListView();
        updateCurrentPhotoPath();
    }

    @Override
    public void update(Observable o, Object arg) {
        refresh();
    }
}
