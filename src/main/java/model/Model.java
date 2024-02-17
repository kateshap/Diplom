package model;

import Server.Server;
import com.example.diploma.DatabaseHandler;

public class Model {
    private Server s;
    private DatabaseHandler db;

    public Server getS() {
        return s;
    }

    public void setS(Server s) {
        this.s = s;
    }

    public DatabaseHandler getDb() {
        return db;
    }

    public void setDb(DatabaseHandler db) {
        this.db = db;
    }

    public void init(Server s, DatabaseHandler dataBase) {
        this.db = dataBase;
        this.s = s;
    }
}
