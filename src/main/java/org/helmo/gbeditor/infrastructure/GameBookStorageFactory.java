package org.helmo.gbeditor.infrastructure;

import org.helmo.gbeditor.exceptions.DataBaseException;
import org.helmo.gbeditor.exceptions.NoDriverFoundException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GameBookStorageFactory {

    private final String db;
    private final String username;
    private final String password;

    public GameBookStorageFactory(String driverName, String db, String username, String pass) {

        try {
            Class.forName(driverName);
            this.db = db;
            this.username = username;
            this.password = pass;
        } catch (ClassNotFoundException cnfe) {
            throw new NoDriverFoundException("Impossible de charger le driver " + driverName, cnfe);
        }
    }

    public GameBookStorage setConnection() throws DataBaseException{
        try {
            Connection con = DriverManager.getConnection(db, username, password);
            return new GameBookStorage(con);
        } catch (SQLException sqle) {
            throw new DataBaseException("Impossible de joindre la base de données" + db, sqle);
        }
    }
}

