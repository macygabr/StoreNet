package org.example.server.backend;

import org.example.server.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;

@Component("Database")
public class Database {
    private static final String path = "src/Server/src/main/resources/static/download/";

    @Autowired
    @Qualifier("SpringDataSource")
    private DataSource database;
    public Database(DataSource database) {
        this.database = database;
//        File folder = new File(path);
//        File[] files = folder.listFiles();
    }

    public Product[] findByName(String name) {
        return null;
    }

    public void update() {

    }
}
