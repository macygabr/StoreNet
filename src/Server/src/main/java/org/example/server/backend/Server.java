package org.example.server.backend;

import lombok.Getter;
import lombok.Setter;
import org.example.server.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Setter
public class Server {
//    @Autowired
    private Database database;

    public Product[] findByName(String name) {
        return database.findByName(name);
    }
}
