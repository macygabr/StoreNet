package org.example.server.backend;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import java.io.IOException;
import org.example.server.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;


import java.io.File;
import java.io.FileReader;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component("Database")
public class Database {
    private static final String path = "src/main/resources/static/download/";
    // private static DataSource dataSource;
    private static JdbcTemplate jdbcTemplate;

    @Autowired
    public Database(@Qualifier("SpringDataSource") DataSource dataSource) {
        // this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    public Database() {
        this.jdbcTemplate = null;
    }


    public Product[] findByName(String name) {
        return null;
    }

    public void update() {
        try {
            File folder = new File(path);
            if (folder == null) return;


            File[] files = folder.listFiles();
            for(int i=0; i<files.length;  i++) {
                readAndInsertData(files[i]);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     public void readAndInsertData(File csv_file) throws Exception {
        try (CSVReader csvReader = new CSVReader(new FileReader(csv_file.getAbsolutePath()))) {
            String[] header = csvReader.readNext();
            if (header == null || header.length == 0) {
                throw new IllegalArgumentException("The CSV file is empty or has an invalid header.");
            }
            List<String[]> records = csvReader.readAll();
            for (String[] record : records) {
                long id = Long.parseLong(record[0]);
                String name = record[1];
                String address = record[2];

                String sql = "INSERT INTO shop (id, name, address) VALUES (?, ?, ?)";
                jdbcTemplate.update(sql, id, name, address);
            }
        }
    }
}
