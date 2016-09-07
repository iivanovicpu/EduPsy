package hr.iivanovic.psyedu;

import java.util.ResourceBundle;

/**
 * User: Administrator
 * Date: 08.02.13.
 * Time: 12:44
 */
public class Configuration {

    private static Configuration instance = null;

    public static final String RESOURCE_BUNDLE_BASENAME = "configuration";

    private static final String EXTERNAL_LOCATION = "external.location";

    private ResourceBundle config;

    public static synchronized Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    private Configuration() {
        config = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASENAME);
    }

    public String getExternalLocation() {
        return config.getString(EXTERNAL_LOCATION);
    }

}
