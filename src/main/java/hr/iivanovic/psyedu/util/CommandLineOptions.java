package hr.iivanovic.psyedu.util;


import com.beust.jcommander.Parameter;

public class CommandLineOptions {

    @Parameter(names = "--debug")
    boolean debug = false;

    @Parameter(names = {"--service-port"})
    Integer servicePort = 4567;

    @Parameter(names = {"--database"})
    String database = "blog";

    @Parameter(names = {"--db-host"})
    String dbHost = "localhost";

    @Parameter(names = {"--db-username"})
    String dbUsername = "blog_owner";

    @Parameter(names = {"--db-password"})
    String dbPassword = "sparkforthewin";

    @Parameter(names = {"--db-port"})
    Integer dbPort = 5432;
}