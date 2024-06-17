package org.example.server.backend;

import java.util.List;
import java.util.ArrayList;

import org.example.server.models.Product;
import org.example.server.models.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

import java.io.File;

@Component
public class Database {
    private static final String path = "src/main/resources/static/download/";
    private ConvertCSVtoDatabase convertCSVtoDatabase;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public Database(@Qualifier("SpringDataSource") DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.convertCSVtoDatabase = new ConvertCSVtoDatabase(dataSource);
    }

    public void addPurchase(String name) throws Exception {
        String maxIdQuery = "SELECT COALESCE(MAX(id), 0) FROM purchase";
        long maxId = jdbcTemplate.queryForObject(maxIdQuery, Long.class);
        long electroId = findByName(name).getId();
        long employeeId = 0;
        long typeId = 0;
        long shopId = 0;

        String sql = "INSERT INTO purchase (id, electroId, employeeId, purchaseDate, typeId, shopId) " +
                     "VALUES (? ,?, ?, NOW(), ?, ?)";
        jdbcTemplate.update(sql, maxId+1, electroId, employeeId, typeId, shopId);
    }

   public ArrayList<Purchase> findAllPurchase() {
    try {
        String sql = "SELECT * FROM purchase ORDER BY purchasedate";
        ArrayList<Purchase> purchases = new ArrayList<>();
        jdbcTemplate.query(sql, new Object[] {}, (rs, rowNum) -> {
            Purchase purchase = new Purchase();
            purchase.setId(rs.getLong("id"));
            purchase.setElectroid(rs.getLong("electroid"));
            purchase.setEmployeeid(rs.getLong("employeeid"));
            purchase.setPurchasedate(rs.getDate("purchasedate"));
            purchase.setTypeid(rs.getLong("typeid"));
            purchase.setShopid(rs.getLong("shopid"));
            purchases.add(purchase);
            return purchase;
        });
        return purchases;
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

   public Product findByName(String name) {
    try {
        String sql = "SELECT * FROM electroitem WHERE name = ?";
        Product product = new Product();
        List<Product> products = jdbcTemplate.query(sql, new Object[] { name }, (rs, rowNum) -> {
            product.setId(rs.getLong("id"));
            product.setName(rs.getString("name"));
            product.setEtypeid(rs.getLong("etypeid"));
            product.setPrice(rs.getLong("price"));
            product.setCount(rs.getInt("count"));
            product.setArchive(rs.getBoolean("archive"));
            product.setDescription(rs.getString("description"));
            return product;
        });
        if (products.isEmpty()) {
            return product;
        } else {
            return products.get(0);
        }
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

    public void update() {
        try {
            File folder = new File(path);

            File[] files = folder.listFiles();
            for(int i=0; i<files.length;  i++) {
                if(files[i].getName().equals("Shop.csv")) convertCSVtoDatabase.readAndInsertShop(files[i]);
                if(files[i].getName().equals("PositionType.csv")) convertCSVtoDatabase.readAndInsertPositiontype(files[i]);
                if(files[i].getName().equals("ElectroEmployee.csv")) convertCSVtoDatabase.readAndInsertElectroEmployee(files[i]); 
                if(files[i].getName().equals("ElectroItem.csv")) convertCSVtoDatabase.readAndInsertElectroItem(files[i]);
                if(files[i].getName().equals("ElectroType.csv")) convertCSVtoDatabase.readAndInsertElectroType(files[i]); 
                if(files[i].getName().equals("ElectroShop.csv")) convertCSVtoDatabase.readAndInsertElectroShop(files[i]); 
                if(files[i].getName().equals("Employee.csv")) convertCSVtoDatabase.readAndInsertEmployee(files[i]);
                if(files[i].getName().equals("Purchase.csv")) convertCSVtoDatabase.readAndInsertPurchase(files[i]);
                if(files[i].getName().equals("PurchaseType.csv")) convertCSVtoDatabase.readAndInsertPurchaseType(files[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
