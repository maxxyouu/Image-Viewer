package Model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * a object that log the name information of this application using singleton pattern
 * The old history of names will be stores in a txt file
 *
 * @author Shiyi Tao
 * @version 2.0
 * @since 2017-11-30
 */
public class DataLogger implements Observer {
    /**
     * get the built in logger object
     */
    private static final Logger logger = Logger.getLogger("global");
    /**
     * get the dataLogger object
     */
    private static final DataLogger DATA_LOGGER = new DataLogger();

    /**
     * create a new dataLogger to log information
     */
    private DataLogger() {
        // this is append enabled fileHandler
        try {
            Handler fileHandler = new FileHandler("nameLog.txt", true);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * get the dataLogger object
     *
     * @return the DataLogger object
     */
    public static DataLogger getDataLogger() {
        return DATA_LOGGER;
    }

    /**
     * add log information into the file
     *
     * @param previousName the old name information
     * @param newName      the new name information
     */
    void addLog(String previousName, String newName) {
        if (!previousName.equals(newName))
            logger.info("Old Name: " + previousName + " New Name: " + newName);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof HashMap) {
            HashMap<String, String> data = (HashMap) arg;
            addLog(data.get("oldName"), data.get("newName"));
        }
    }
}
