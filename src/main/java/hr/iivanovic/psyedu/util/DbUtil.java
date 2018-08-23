package hr.iivanovic.psyedu.util;

import org.sql2o.Sql2o;
import org.sql2o.quirks.NoQuirks;
import org.sql2o.quirks.PostgresQuirks;

import hr.iivanovic.psyedu.AppConfiguration;

/**
 * @author iivanovic
 * @date 29.08.16.
 */
public class DbUtil {

    private static AppConfiguration configuration = AppConfiguration.getInstance();

    public static Sql2o getPostgreSQLDataSource() {
        return new Sql2o(
                configuration.getDatabaseUrl() + ":" +configuration.getDatabasePort() + "/" +  configuration.getDatabaseName(),
                configuration.getDatabaseUsername(),
                configuration.getDatabasePassword());
    }
}
