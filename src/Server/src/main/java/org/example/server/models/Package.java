package org.example.server.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Package {
    private String query;

    @Override
    public String toString() {
        return query;
    }
}
