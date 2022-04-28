package net.boster.particles.main.data.database;

import java.sql.Connection;

public interface ConnectedDatabase {

    void createTableIfNotExists();
    void setMySqlUserValue(String key, String arg1, String arg2);
    void deleteUser(String key);
    String getMySqlValue(String key, String arg);
    Connection getConnection();
    void closeConnection();
}
