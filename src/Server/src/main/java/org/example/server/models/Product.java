package org.example.server.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Getter
@Setter
public class Product {

    private long id;

    private String name;
    private long etypeid;
    private long price;
    private int count;
    private boolean archive;
    private String description;

    @Override
    public String toString() {
        return "Product{" +
                    ", name='" + name + '\'' +
                    ", etypeid=" + etypeid +
                    ", price=" + price +
                    ", count=" + count +
                    ", archive=" + archive +
                    ", description='" + description + '\'' +
                    '}';
    }
}
