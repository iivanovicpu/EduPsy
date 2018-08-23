package hr.iivanovic.psyedu;

import static hr.iivanovic.psyedu.util.JsonUtil.dataToJson;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.debug.DebugScreen.enableDebugScreen;

import java.util.UUID;

import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.NoQuirks;
import org.sql2o.quirks.PostgresQuirks;

import hr.iivanovic.psyedu.db.Model;
import hr.iivanovic.psyedu.db.Sql2oModel;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author iivanovic
 * @date 28.08.16.
 */
public class DbApplication {
    public static void main(String[] args) {
//        Sql2o sql2o = getH2DataSource();
        Model model = Sql2oModel.getInstance();

        get("/subjects", (request, response) -> {
            response.status(200);
            response.type("application/json");
            return dataToJson(model.getAllSubjects());
        });

        get("/alive", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return "ok";
            }
        });

        enableDebugScreen();
    }

    private static Sql2o getSql2oProvider() {
        port(4567);

        String dbHost = "localhost";
        int dbPort = 5432;
        String dbName = "edupsy";

        String dbUsername = "edupsy";
        String dbPassword = "edupsy";
        return new Sql2o("jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName,
                dbUsername, dbPassword, new PostgresQuirks() {
            {
                // make sure we use default UUID converter.
                converters.put(UUID.class, new UUIDConverter());
            }
        });
    }

    private static Sql2o getSql2oH2Provider() {
        String connectionString = "\"jdbc:h2:mem:testdb;INIT=RUNSCRIPT from 'classpath:db/init.sql'\";";
        Sql2o sql2o = new Sql2o(connectionString, "sa", "");

        return sql2o;
    }

    private static Sql2o getH2DataSource() {
        org.h2.jdbcx.JdbcDataSource ds = new org.h2.jdbcx.JdbcDataSource();
        ds.setURL("jdbc:h2:mem:testdb;INIT=RUNSCRIPT from 'classpath:db/init.sql'");
        ds.setUser("sa");
        ds.setPassword("");

/*      todo: provjeriti da li je potrebno imati converter ...
        Sql2o sql2o = new Sql2o(ds, new NoQuirks(new HashMap<Class, Converter>() {{
            put(DateTime.class, new DateTimeConverter(DateTimeZone.getDefault()));
        }}));
*/

        return new Sql2o(ds, new NoQuirks());
    }
}
