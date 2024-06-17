package org.example.server.backend;

import lombok.Getter;
import lombok.Setter;
import org.example.server.models.Product;
import org.example.server.models.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;

@Getter
@Setter
@Component
public class Server {

    private final Database database;
    private final String address = "37.194.168.90";

    @Autowired
    public Server(Database database) {
        this.database = database;
    }

    public Product findByName(String name) {
        return database.findByName(name);
    }

    public  ArrayList<Purchase> findAllPurchase() {
        return database.findAllPurchase();
    }
    
    public void addPurchase(String name) throws Exception {
        database.addPurchase(name);
    }
}
