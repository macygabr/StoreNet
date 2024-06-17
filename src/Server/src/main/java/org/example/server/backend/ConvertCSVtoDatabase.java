package org.example.server.backend;

import java.io.File;
import java.io.FileReader;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVParserBuilder;

import java.util.List;

import java.text.SimpleDateFormat;
import java.util.Date;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;


class ConvertCSVtoDatabase {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ConvertCSVtoDatabase(@Qualifier("SpringDataSource") DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

     public void readAndInsertShop(File csv_file) throws Exception {
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(csv_file.getAbsolutePath()))
                                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                                .build()){
            String[] header = csvReader.readNext();
            if (header == null || header.length == 0) {
                throw new IllegalArgumentException("The CSV file is empty or has an invalid header.");
            }
            List<String[]> records = csvReader.readAll();

            for (String[] record : records) {
                long id = Long.parseLong(record[0]);
                String name = record[1];
                String address = record[2];

                String sql = "INSERT INTO shop (id, name, address) VALUES (?, ?, ?)" + " ON CONFLICT (id) DO NOTHING";
                jdbcTemplate.update(sql, id, name, address);
            }
        }
    }
    public void readAndInsertPositiontype(File csv_file) throws Exception {
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(csv_file.getAbsolutePath()))
                                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                                .build()){
            String[] header = csvReader.readNext();
            if (header == null || header.length == 0) {
                throw new IllegalArgumentException("The CSV file is empty or has an invalid header.");
            }
            List<String[]> records = csvReader.readAll();

            for (String[] record : records) {
                long id = Long.parseLong(record[0]);
                String name = record[1];

                String sql = "INSERT INTO positiontype (id, name) VALUES (?, ?)" + " ON CONFLICT (id) DO NOTHING";
                jdbcTemplate.update(sql, id, name);
            }
        }
    }

     public void readAndInsertElectroItem(File csv_file) throws Exception {
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(csv_file.getAbsolutePath()))
                                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                                .build()){
            String[] header = csvReader.readNext();
            if (header == null || header.length == 0) {
                throw new IllegalArgumentException("The CSV file is empty or has an invalid header.");
            }
            List<String[]> records = csvReader.readAll();

            for (String[] record : records) {

                long id = Long.parseLong(record[0]);
                String name = record[1];
                long etypeid = Long.parseLong(record[2]);
                long price = Long.parseLong(record[3]);
                long count = Long.parseLong(record[4]);
                boolean archive = Boolean.parseBoolean(record[5]);
                String description = record[6];

                String sql = "INSERT INTO electroitem (id, name, etypeid, price, count, archive, description) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                         " ON CONFLICT (id) DO NOTHING";
                jdbcTemplate.update(sql, id, name, etypeid, price, count, archive, description);
            }
        }
    }
     
     public void readAndInsertPurchase(File csv_file) throws Exception {
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(csv_file.getAbsolutePath()))
                                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                                .build()){
            String[] header = csvReader.readNext();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            if (header == null || header.length == 0) {
                throw new IllegalArgumentException("The CSV file is empty or has an invalid header.");
            }
            List<String[]> records = csvReader.readAll();

            for (String[] record : records) {

                long id = Long.parseLong(record[0]);
                long electroId = Long.parseLong(record[1]);
                long employeeId = Long.parseLong(record[2]);
                Date purchaseDate = dateFormat.parse(record[3]);
                long typeId = Long.parseLong(record[4]);
                long shopId = Long.parseLong(record[5]);

                String sql = "INSERT INTO purchase (id, electroId, employeeId, purchaseDate, typeId, shopId) " +
                         "VALUES (?, ?, ?, ?, ?, ?) " +
                         " ON CONFLICT (id) DO NOTHING";
                jdbcTemplate.update(sql, id, electroId, employeeId, purchaseDate, typeId, shopId);
            }
        }
    }
     
     public void readAndInsertPurchaseType(File csv_file) throws Exception {
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(csv_file.getAbsolutePath()))
                                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                                .build()){
            String[] header = csvReader.readNext();
            if (header == null || header.length == 0) {
                throw new IllegalArgumentException("The CSV file is empty or has an invalid header.");
            }
            List<String[]> records = csvReader.readAll();

            for (String[] record : records) {
                long id = Long.parseLong(record[0]);
                String name = record[1];

                String sql = "INSERT INTO purchaseType (id, name) " +
                         "VALUES (?, ?) " +
                         " ON CONFLICT (id) DO NOTHING";
                jdbcTemplate.update(sql, id, name);
            }
        }
    }
     
     public void readAndInsertElectroType(File csv_file) throws Exception {
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(csv_file.getAbsolutePath()))
                                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                                .build()){
            String[] header = csvReader.readNext();
            if (header == null || header.length == 0) {
                throw new IllegalArgumentException("The CSV file is empty or has an invalid header.");
            }
            List<String[]> records = csvReader.readAll();

            for (String[] record : records) {
                long id = Long.parseLong(record[0]);
                String name = record[1];

                String sql = "INSERT INTO electrotype (id, name) " +
                         "VALUES (?, ?) " +
                         " ON CONFLICT (id) DO NOTHING";
                jdbcTemplate.update(sql, id, name);
            }
        }
    }
    
    public void readAndInsertElectroEmployee(File csv_file) throws Exception {
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(csv_file.getAbsolutePath()))
                                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                                .build()){
            String[] header = csvReader.readNext();
            if (header == null || header.length == 0) {
                throw new IllegalArgumentException("The CSV file is empty or has an invalid header.");
            }
            List<String[]> records = csvReader.readAll();

            for (String[] record : records) {
                long employeeid = Long.parseLong(record[0]);
                long electrotypeid = Long.parseLong(record[1]);

                String sql = "INSERT INTO electroemployee (employeeid, electrotypeid) " +
                         "VALUES (?, ?) ";
                jdbcTemplate.update(sql, employeeid, electrotypeid);
            }
        }
    }
   
    public void readAndInsertElectroShop(File csv_file) throws Exception {
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(csv_file.getAbsolutePath()))
                                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                                .build()){
            String[] header = csvReader.readNext();
            if (header == null || header.length == 0) {
                throw new IllegalArgumentException("The CSV file is empty or has an invalid header.");
            }
            List<String[]> records = csvReader.readAll();

            for (String[] record : records) {
                long shopid = Long.parseLong(record[0]);
                long electroitemid = Long.parseLong(record[1]);
                long count = Long.parseLong(record[2]);

                String sql = "INSERT INTO electroshop (shopid, electroitemid, count) " +
                         "VALUES (?, ?, ?) ";
                jdbcTemplate.update(sql, shopid, electroitemid, count);
            }
        }
    }
 
    public void readAndInsertEmployee(File csv_file) throws Exception {
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(csv_file.getAbsolutePath()))
                                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                                .build()){
            String[] header = csvReader.readNext();
            if (header == null || header.length == 0) {
                throw new IllegalArgumentException("The CSV file is empty or has an invalid header.");
            }
            List<String[]> records = csvReader.readAll();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

            for (String[] record : records) {
                long id = Long.parseLong(record[0]);
                String lastname = record[1];
                String firstname = record[2];
                String patronymic = record[3];
                Date birthdate = dateFormat.parse(record[4]);
                long positionid = Long.parseLong(record[5]);
                long shopid = Long.parseLong(record[6]);
                Boolean gender = Boolean.parseBoolean(record[7]);

                String sql = "INSERT INTO employee (id, lastname, firstname, patronymic, birthdate, positionid, shopid, gender) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " + 
                         " ON CONFLICT (id) DO NOTHING";
                jdbcTemplate.update(sql, id, lastname, firstname, patronymic, birthdate, positionid, shopid, gender);
            }
        }
    }
}
