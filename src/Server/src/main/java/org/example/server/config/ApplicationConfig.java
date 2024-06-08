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
   
   private final static String sql_create_shop = "CREATE TABLE IF NOT EXISTS Shop (id SERIAL PRIMARY KEY, name VARCHAR(255), address TEXT)";
//    private final static String sql_create_electroShop = "CREATE TABLE IF NOT EXISTS Shop (id SERIAL PRIMARY KEY, name VARCHAR(255), address TEXT)";

   private final static String sql_create_ElectroItem = "CREATE TABLE IF NOT EXISTS ElectroItem (id SERIAL PRIMARY KEY, name VARCHAR(255), etypeid BIGINT, price BIGINT, count INT, archive BOOLEAN, description TEXT)";
   private final static String sql_create_ElectroType = "CREATE TABLE IF NOT EXISTS ElectroType (pk BIGINT PRIMARY KEY, name VARCHAR(255))";
   private final static String sql_create_ElectroEmployee = "CREATE TABLE IF NOT EXISTS ElectroEmployee (employeeId BIGINT, electroTypeId BIGINT, FOREIGN KEY (electroTypeId) REFERENCES ElectroType(pk))";
   private final static String sql_create_Employee = "CREATE TABLE IF NOT EXISTS Employee (pk BIGINT PRIMARY KEY, lastName VARCHAR(255), firstName VARCHAR(255), patronymic VARCHAR(255), birthDate DATE, positionId BIGINT, shopId BIGINT, gender BOOLEAN)";
   private final static String sql_create_PositionType = "CREATE TABLE IF NOT EXISTS PositionType (pk BIGINT PRIMARY KEY, name VARCHAR(255))";
   private final static String sql_create_Purchase = "CREATE TABLE IF NOT EXISTS Purchase (pk BIGINT PRIMARY KEY, lastName VARCHAR(255), firstName VARCHAR(255), patronymic VARCHAR(255), birthDate DATE, positionId BIGINT, shopId BIGINT, gender BOOLEAN)";
   private final static String sql_create_PurchaseType = "CREATE TABLE IF NOT EXISTS PurchaseType (pk BIGINT PRIMARY KEY, electroId BIGINT, employeeId BIGINT, purchaseDate DATE, typeId BIGINT, shopId BIGINT, FOREIGN KEY (electroId) REFERENCES ElectroItem(id), FOREIGN KEY (employeeId) REFERENCES Employee(pk), FOREIGN KEY (typeId) REFERENCES PositionType(pk))";


   @Bean(name = "SpringDataSource")
   @Scope("singleton")
   public DataSource initSpringDataSource(){
       DriverManagerDataSource dataSource = new DriverManagerDataSource();
       try {
            dataSource.setUrl(DB_URL);
            dataSource.setUsername(DB_USER);
            dataSource.setPassword(DB_PASSWD);
            dataSource.setDriverClassName(DB_DRIVER_NAME);
            dataSource.getConnection().createStatement().execute(sql_create_shop);
            dataSource.getConnection().createStatement().execute(sql_create_ElectroItem);
            dataSource.getConnection().createStatement().execute(sql_create_ElectroType);
            dataSource.getConnection().createStatement().execute(sql_create_ElectroEmployee);
            dataSource.getConnection().createStatement().execute(sql_create_Employee);
            dataSource.getConnection().createStatement().execute(sql_create_PositionType);
            dataSource.getConnection().createStatement().execute(sql_create_Purchase);
            dataSource.getConnection().createStatement().execute(sql_create_PurchaseType);
       } catch(Exception e) {
            System.out.println("\033[31mUp database!!!\033[0m");
       }
       return dataSource;
   }
}