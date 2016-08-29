package hr.iivanovic.psyedu.util;

import org.sql2o.Sql2o;
import org.sql2o.quirks.NoQuirks;

/**
 * @author iivanovic
 * @date 29.08.16.
 */
public class DbUtil {
    public static Sql2o getH2DataSource(){
        org.h2.jdbcx.JdbcDataSource ds = new org.h2.jdbcx.JdbcDataSource();
        ds.setURL("jdbc:h2:mem:testdb;INIT=RUNSCRIPT from 'classpath:db/init.sql'");
        ds.setUser("sa");
        ds.setPassword("");

/*      todo: provjeriti da li je potrebno imati converter ...
        Sql2o sql2o = new Sql2o(ds, new NoQuirks(new HashMap<Class, Converter>() {{
            put(DateTime.class, new DateTimeConverter(DateTimeZone.getDefault()));
        }}));
*/

        return new Sql2o(ds,new NoQuirks());
    }
}
