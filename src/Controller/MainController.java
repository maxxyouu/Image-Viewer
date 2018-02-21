package Controller;

import Model.Database;
import Model.DatabaseManager;
import View.View;
import View.ViewAgent;


/**
 * A abstract controller
 * All other controller should extends MainController
 *
 * @author Jianzhong You, Yuan Xu, Shiyi Tao
 * @version 2.0
 * @since 2017-11-30
 */
abstract class MainController {
    /**
     * the singleton databaseManager of the application
     */
    DatabaseManager dbManager = DatabaseManager.getDbManager();

    /**
     * the singleton main view of the application
     */
    View mainView = View.getView();

    /**
     * the singleton database of the application
     */
    Database database = Database.getDatabase();

    /**
     * the singleton viewAgent object that responsible to refresh and update the GUI
     */
    ViewAgent viewAgent = ViewAgent.getViewAgent();

    /**
     * reset the status message in the GUI
     */
    void resetStatusMessage() {
        viewAgent.updateStatusMessage("");
    }

}
