package net.boster.particles.main.data.database;

import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.utils.LogType;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.*;

public class SQLiteConnectionUtils implements ConnectedDatabase {

    private Connection connection;

    public boolean connect() {
        try {
            if(connection != null && !connection.isClosed()) {
                return false;
            }
            BosterParticles.getInstance().log("Connecting to database...", LogType.INFO);
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:" + new File(BosterParticles.getInstance().getDataFolder(), "users.db").toURI().toURL());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            createTableIfNotExists();
            BosterParticles.getInstance().log("Database connection done!", LogType.FINE);
            return true;
        } catch (SQLException | IllegalArgumentException e) {
            BosterParticles.getInstance().log("Could not connect database!", LogType.ERROR);
            return false;
        }
    }

    public void createTableIfNotExists() {
        new DatabaseRunnable().run(() -> {
            try {
                PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `users` (`uuid` VARCHAR(36), `data` TEXT(1000000000), PRIMARY KEY (`uuid`))");
                statement.execute();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void setMySqlUserValue(String key, String arg1, String arg2) {
        new DatabaseRunnable().run(() -> {
            try {
                PreparedStatement st = connection.prepareStatement("INSERT INTO `users` (uuid, " + arg1 + ") VALUES ('" + key + "', '" + arg2 + "') " +
                        "ON CONFLICT (uuid) DO UPDATE SET " + arg1 + " = excluded." + arg1);
                st.execute();
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void deleteUser(String key) {
        new DatabaseRunnable().run(() -> {
            try {
                PreparedStatement st = connection.prepareStatement("DELETE FROM `users` WHERE uuid = '" + key + "'");
                st.execute();
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public String getMySqlValue(String key, String arg) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `users` WHERE uuid = '" + key + "'");
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                return rs.getString(arg);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
