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

    private static String h2DbFileLocation = AppConfiguration.getInstance().getH2DbFileLocation();

    private static AppConfiguration configuration = AppConfiguration.getInstance();

    @Deprecated
    public static Sql2o getH2DataSource() {
        org.h2.jdbcx.JdbcDataSource ds = new org.h2.jdbcx.JdbcDataSource();
//        ds.setURL("jdbc:h2:mem:testdb;INIT=RUNSCRIPT from 'classpath:db/init.sql'");
        String additionalOptions = ";INIT=RUNSCRIPT from 'classpath:db/init.sql'";

        ds.setURL("jdbc:h2:file:" + h2DbFileLocation + additionalOptions);
//        ds.setURL("jdbc:h2:file:" + h2DbFileLocation);
        ds.setUser("sa");
        ds.setPassword("");
        return new Sql2o(ds, new NoQuirks());
    }

    public static Sql2o getPostgreSQLDataSource() {
        return new Sql2o(
                configuration.getDatabaseUrl() + ":" +configuration.getDatabasePort() + "/" +  configuration.getDatabaseName(),
                configuration.getDatabaseUsername(),
                configuration.getDatabasePassword());
    }
}
