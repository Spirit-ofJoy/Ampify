//Class to connect and have operations with the database

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//Class to connect and interact with mysql database
public class DatabaseConnection {

    private static final String sqlPath = "jdbc:mysql://localhost:3306/ampify";
    private static final String sqlPaswd = "password";


    public static void displayUser() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(sqlPath, "java", sqlPaswd);

        String query = "SELECT * FROM users";
        PreparedStatement preStat = connection.prepareStatement(query);

        ResultSet result = preStat.executeQuery();

        while (result.next()){
            String s = result.getString("USERID");
            System.out.println(s);
        }

    }

    public static String checkLogin(String uname, String paswd) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(sqlPath, "java", sqlPaswd);

        String query = "SELECT USERID FROM users WHERE Uname = ? AND Password = ?";
        PreparedStatement preStat = connection.prepareStatement(query);
        preStat.setString(1, uname);
        preStat.setString(2, paswd);
        ResultSet result = preStat.executeQuery();

        if (!result.isBeforeFirst() ) {
            return "USER_NOT_FOUND";
        }

        while (result.next()){
            String s = result.getString("USERID");
            return s;
        }
        return "USER_NOT_FOUND";
    }
}
