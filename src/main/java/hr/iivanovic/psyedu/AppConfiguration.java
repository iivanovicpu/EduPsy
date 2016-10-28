package hr.iivanovic.psyedu;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

/**
 * User: Administrator
 * Date: 08.02.13.
 * Time: 12:44
 */
@Slf4j
public class AppConfiguration {

    private static AppConfiguration instance = null;

    private static final String EXTERNAL_LOCATION = "external.location";

    private static final String H2_DB_FILE_LOCATION = "h2.db.file.location";

    private static final String DEVELOPMENT_MODE = "development.mode";

    private static final String DATABASE_URL = "database.url";

    private static final String DATABASE_PORT = "database.port";

    private static final String DATABASE_NAME = "database.name";

    private static final String DATABASE_USERNAME = "database.username";

    private static final String DATABASE_PASSWORD = "database.password";

    private Properties config;

    public static synchronized AppConfiguration getInstance() {
        if (instance == null) {
            String resourceFilePath = System.getProperty("config.file");
            try {
                instance = new AppConfiguration(resourceFilePath);
                log.info("config file loaded successfully: " + resourceFilePath);
            } catch (IOException e) {
                log.error("error loading app properties file", e);
                e.printStackTrace();
            }
        }
        return instance;
    }

    private AppConfiguration(String resourceFilePath) throws IOException {
        config = new Properties();
        FileInputStream fis = new FileInputStream(resourceFilePath);
        config.load(fis);
    }

    public String getExternalLocation() {
        return config.getProperty(EXTERNAL_LOCATION);
    }

    public String getH2DbFileLocation() {
        return config.getProperty(H2_DB_FILE_LOCATION);
    }

    public String getDatabaseName() {
        return config.getProperty(DATABASE_NAME);
    }

    public String getDatabaseUrl() {
        return config.getProperty(DATABASE_URL);
    }

    public String getDatabasePort() {
        return config.getProperty(DATABASE_PORT);
    }

    public String getDatabaseUsername() {
        return config.getProperty(DATABASE_USERNAME);
    }

    public String getDatabasePassword() {
        return config.getProperty(DATABASE_PASSWORD);
    }

    public boolean isDevelopmentMode() {
        return "true".equals(config.getProperty(DEVELOPMENT_MODE));
    }
}
