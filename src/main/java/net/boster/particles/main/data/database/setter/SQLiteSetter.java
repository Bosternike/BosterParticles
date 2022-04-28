package net.boster.particles.main.data.database.setter;

import net.boster.particles.main.data.database.ConnectedDatabase;
import org.jetbrains.annotations.NotNull;

public class SQLiteSetter extends DatabaseSetter {

    public SQLiteSetter(ConnectedDatabase db) {
        super(db);
    }

    @Override
    public @NotNull String getName() {
        return "SQLite";
    }
}
