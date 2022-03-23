package net.Boster.particles.main.data.database;

import net.Boster.particles.main.BosterParticles;
import net.Boster.particles.main.utils.LogType;

import java.sql.*;

public class MySqlConnection {

    private static Connection connection;

    public static synchronized void connect(String host, int port, String database, String user, String password) {
        new DatabaseRunnable().run(() -> {
            try {
                if(connection != null && !connection.isClosed()) {
                    return;
                }
                BosterParticles.getInstance().log("Connecting to database...", LogType.INFO);
                setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", user, password));
                createTableIfNotExists();
                BosterParticles.getInstance().log("Database connection done!", LogType.FINE);
            } catch (SQLException e) {
                BosterParticles.getInstance().log("Could not connect database!", LogType.ERROR);
            }
        });
    }

    public static void createTableIfNotExists() {
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

    public static void setMySqlUserValue(String key, String arg1, String arg2) {
        new DatabaseRunnable().run(() -> {
            try {
                PreparedStatement st = connection.prepareStatement("INSERT INTO `users` (uuid, " + arg1 + ") VALUES ('" + key + "', '" + arg2 + "') ON DUPLICATE KEY UPDATE " + arg1 + " = '" + arg2 + "'");
                st.execute();
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void deleteUser(String key) {
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

    public static String getMySqlValue(String key, String arg) {
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

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection con) {
        connection = con;
    }
}
