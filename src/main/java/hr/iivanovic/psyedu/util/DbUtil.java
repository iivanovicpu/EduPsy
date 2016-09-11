package hr.iivanovic.psyedu.util;

import org.sql2o.Sql2o;
import org.sql2o.quirks.NoQuirks;

import hr.iivanovic.psyedu.Configuration;

/**
 * @author iivanovic
 * @date 29.08.16.
 */
public class DbUtil {

    public static Sql2o getH2DataSource() {
        String h2DbFileLocation = Configuration.getInstance().getH2DbFileLocation();
        org.h2.jdbcx.JdbcDataSource ds = new org.h2.jdbcx.JdbcDataSource();
//        ds.setURL("jdbc:h2:mem:testdb;INIT=RUNSCRIPT from 'classpath:db/init.sql'");
//        String additionalOptions = ";INIT=RUNSCRIPT from 'classpath:db/init.sql'";

//        ds.setURL("jdbc:h2:file:" + h2DbFileLocation + additionalOptions);
        ds.setURL("jdbc:h2:file:" + h2DbFileLocation);
        ds.setUser("sa");
        ds.setPassword("");
        return new Sql2o(ds, new NoQuirks());
    }
}
