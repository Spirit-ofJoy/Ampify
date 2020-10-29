package DatabaseConnection;

import java.sql.Connection;

//Class to connect and interact with mysql database
public class DatabaseConnect {

    private static final String sqlPath = "jdbc:mysql://localhost:3306/ampify";
    private static final String sqlPaswd = "password";
    private static final String sqlName = "java";
    protected static Connection connection;

    protected static String getSqlName() {
        return sqlName;
    }

    protected static String getSqlPaswd() {
        return sqlPaswd;
    }

    protected static String getSqlPath() {
        return sqlPath;
    }

}
