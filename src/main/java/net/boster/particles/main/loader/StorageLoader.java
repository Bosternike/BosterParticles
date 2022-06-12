package net.boster.particles.main.loader;

import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.data.EnumStorage;
import net.boster.particles.main.data.database.MySqlConnectionUtils;
import net.boster.particles.main.data.database.SQLiteConnectionUtils;
import net.boster.particles.main.data.database.setter.FileSetter;
import net.boster.particles.main.data.database.setter.MySqlSetter;
import net.boster.particles.main.data.database.setter.SQLiteSetter;
import net.boster.particles.main.utils.log.LogType;
import org.jetbrains.annotations.NotNull;

public class StorageLoader extends ALoader {

    public StorageLoader(@NotNull BPLoader loader, @NotNull BosterParticles plugin) {
        super(loader, plugin);
    }

    public void loadStorage() {
        try {
            loader.setStorage(EnumStorage.valueOf(plugin.getConfig().getString("Storage")));
        } catch (Exception e) {
            loader.setStorage(EnumStorage.YAML);
        }
    }

    public void loadDataSetter() {
        if(loader.getConnectedDatabase() != null) {
            loader.getConnectedDatabase().closeConnection();
        }

        loadStorage();

        if(loader.getStorage() == EnumStorage.MYSQL) {
            String host = plugin.getConfig().getString("MySql.host");
            int port = plugin.getConfig().getInt("MySql.port");
            String user = plugin.getConfig().getString("MySql.user");
            String password = plugin.getConfig().getString("MySql.password");
            String db = plugin.getConfig().getString("MySql.database");

            MySqlConnectionUtils con = new MySqlConnectionUtils();
            if ((host != null || user != null || password != null || db != null) && con.connect(host, port, db, user, password)) {
                plugin.setDataSetter(new MySqlSetter(con));
                loader.setConnectedDatabase(con);
            } else {
                plugin.setDataSetter(new FileSetter());
            }
        } else if(loader.getStorage() == EnumStorage.SQLITE) {
            SQLiteConnectionUtils con = new SQLiteConnectionUtils();
            if(con.connect()) {
                plugin.setDataSetter(new SQLiteSetter(con));
                loader.setConnectedDatabase(con);
            } else {
                plugin.setDataSetter(new FileSetter());
            }
        } else {
            plugin.setDataSetter(new FileSetter());
        }

        plugin.log("&7Using storage: &9" + plugin.getDataSetter().getName(), LogType.INFO);
    }
}
