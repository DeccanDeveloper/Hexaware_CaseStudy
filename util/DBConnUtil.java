package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;

public class DBConnUtil {
    public static Connection getConnection(String propertyFileName) {
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream(propertyFileName);
            props.load(fis);
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error establishing database connection.");
        }
    }
}
