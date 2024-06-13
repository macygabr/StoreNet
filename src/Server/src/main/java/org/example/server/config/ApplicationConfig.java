package org.example.server.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;

@Configuration
@ComponentScan("org.example.server")
@PropertySource("classpath:db.properties")
public class ApplicationConfig {
   @Value("${db.url}")
   private String DB_URL;
   @Value("${db.user}")
   private String DB_USER;
   @Value("${db.password}")
   private String DB_PASSWD;
   @Value("${db.driver.name}")
   private String DB_DRIVER_NAME;
   
   private final static String sql_create_Shop = "CREATE TABLE IF NOT EXISTS Shop (id SERIAL PRIMARY KEY, name VARCHAR(255), address TEXT)";
   private final static String sql_create_ElectroShop = "CREATE TABLE IF NOT EXISTS ElectroShop (shopid BIGINT, electroItemId BIGINT, count BIGINT)";
   private final static String sql_create_ElectroItem = "CREATE TABLE IF NOT EXISTS ElectroItem (id SERIAL PRIMARY KEY, name VARCHAR(255), etypeid BIGINT, price BIGINT, count INT, archive BOOLEAN, description TEXT)";
   private final static String sql_create_ElectroType = "CREATE TABLE IF NOT EXISTS ElectroType (id SERIAL PRIMARY KEY, name VARCHAR(255))";
   private final static String sql_create_ElectroEmployee = "CREATE TABLE IF NOT EXISTS ElectroEmployee (employeeId BIGINT, electroTypeId BIGINT)";
   private final static String sql_create_Employee = "CREATE TABLE IF NOT EXISTS Employee (id SERIAL PRIMARY KEY, lastName VARCHAR(255), firstName VARCHAR(255), patronymic VARCHAR(255), birthDate DATE, positionId BIGINT, shopId BIGINT, gender BOOLEAN)";
   private final static String sql_create_PositionType = "CREATE TABLE IF NOT EXISTS PositionType (id SERIAL PRIMARY KEY, name VARCHAR(255))";
   private final static String sql_create_Purchase = "CREATE TABLE IF NOT EXISTS Purchase (id SERIAL PRIMARY KEY, electroId BIGINT, employeeId BIGINT, purchaseDate TIMESTAMP, typeId BIGINT, shopId BIGINT)";
   private final static String sql_create_PurchaseType = "CREATE TABLE IF NOT EXISTS PurchaseType (id SERIAL PRIMARY KEY, name VARCHAR(255))";


   @Bean(name = "SpringDataSource")
   @Scope("singleton")
   public DataSource initSpringDataSource(){
       DriverManagerDataSource dataSource = new DriverManagerDataSource();
       try {
            dataSource.setUrl(DB_URL);
            dataSource.setUsername(DB_USER);
            dataSource.setPassword(DB_PASSWD);
            dataSource.setDriverClassName(DB_DRIVER_NAME);
            dataSource.getConnection().createStatement().execute(sql_create_Shop);
            dataSource.getConnection().createStatement().execute(sql_create_ElectroShop);
            dataSource.getConnection().createStatement().execute(sql_create_ElectroItem);
            dataSource.getConnection().createStatement().execute(sql_create_ElectroType);
            dataSource.getConnection().createStatement().execute(sql_create_ElectroEmployee);
            dataSource.getConnection().createStatement().execute(sql_create_Employee);
            dataSource.getConnection().createStatement().execute(sql_create_PositionType);
            dataSource.getConnection().createStatement().execute(sql_create_Purchase);
            dataSource.getConnection().createStatement().execute(sql_create_PurchaseType);
       } catch(Exception e) {
            System.out.println("\033[31mDatabase connection error!!!\033[0m");
            e.printStackTrace();
       }
       return dataSource;
   }
}