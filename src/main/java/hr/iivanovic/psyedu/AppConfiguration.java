package hr.iivanovic.psyedu;

import java.util.ResourceBundle;

/**
 * User: Administrator
 * Date: 08.02.13.
 * Time: 12:44
 */
public class AppConfiguration {

    private static AppConfiguration instance = null;

    public static final String RESOURCE_BUNDLE_BASENAME = "configuration";

    private static final String EXTERNAL_LOCATION = "external.location";

    private static final String H2_DB_FILE_LOCATION = "h2.db.file.location";

    private static final String DEVELOPMENT_MODE = "development.mode";

    private static final String DATABASE_URL = "database.url";

    private static final String DATABASE_PORT = "database.port";

    private static final String DATABASE_NAME = "database.name";

    private static final String DATABASE_USERNAME = "database.username";

    private static final String DATABASE_PASSWORD = "database.password";

    private ResourceBundle config;

    public static synchronized AppConfiguration getInstance() {
        if (instance == null) {
            instance = new AppConfiguration();
        }
        return instance;
    }

    private AppConfiguration() {
        config = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASENAME);
    }

    public String getExternalLocation() {
        return config.getString(EXTERNAL_LOCATION);
    }

    public String getH2DbFileLocation() {
        return config.getString(H2_DB_FILE_LOCATION);
    }

    public String getDatabaseName() {
        return config.getString(DATABASE_NAME);
    }

    public String getDatabaseUrl() {
        return config.getString(DATABASE_URL);
    }

    public String getDatabasePort(){
        return config.getString(DATABASE_PORT);
    }

    public String getDatabaseUsername(){
        return config.getString(DATABASE_USERNAME);
    }

    public String getDatabasePassword(){
        return config.getString(DATABASE_PASSWORD);
    }

    public boolean isDevelopmentMode() {
        return "true".equals(config.getString(DEVELOPMENT_MODE));
    }
}
