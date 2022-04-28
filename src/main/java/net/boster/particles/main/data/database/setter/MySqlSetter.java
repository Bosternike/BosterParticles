package net.boster.particles.main.data.database.setter;

import net.boster.particles.main.data.database.ConnectedDatabase;
import org.jetbrains.annotations.NotNull;

public class MySqlSetter extends DatabaseSetter {

    public MySqlSetter(ConnectedDatabase db) {
        super(db);
    }

    @Override
    public @NotNull String getName() {
        return "MySql";
    }
}
