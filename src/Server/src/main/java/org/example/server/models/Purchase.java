package org.example.server.models;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Purchase {

    private long id;
    private long electroid;
    private long employeeid;
    private Date purchasedate;
    private long typeid;
    private long shopid;


    @Override
    public String toString() {
        return "Product{" +
                ", id=" + id +
                ", electroid=" + electroid +
                ", employeeid=" + employeeid +
                ", purchasedate=" + purchasedate +
                ", typeid=" + typeid +
                ", shopid=" + shopid +
                '}';
    }
}
