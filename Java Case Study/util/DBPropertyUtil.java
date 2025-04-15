package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBPropertyUtil {
    public static String getConnectionString(String propertyFileName) {
        try {
            Properties prop = new Properties();

            // Load from root project folder
            FileInputStream fis = new FileInputStream(propertyFileName);
            prop.load(fis);

            String url = prop.getProperty("url");
            String user = prop.getProperty("user");
            String password = prop.getProperty("password");

            return url + "?user=" + user + "&password=" + password;
        } catch (IOException e) {
            throw new RuntimeException("Error reading DB property file: " + e.getMessage());
        }
    }
}
