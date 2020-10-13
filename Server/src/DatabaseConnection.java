//Class to connect and have operations with the database

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//Class to connect and interact with mysql database
public class DatabaseConnection {

    private static final String sqlPath = "jdbc:mysql://localhost:3306/ampify";
    private static final String sqlPaswd = "XXXXX";
    private static final String sqlName = "XXXXX";

    //Checks login credentials and returns appropriate profile
    public static ResultSet checkLogin(String uname, String paswd) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(sqlPath, sqlName, sqlPaswd); //Connected to database

        String query = "SELECT USERID FROM users WHERE Uname = ? AND Password = ?";
        PreparedStatement preStat = connection.prepareStatement(query);
        preStat.setString(1, uname);
        preStat.setString(2, paswd);
        ResultSet result = preStat.executeQuery();

        if ( !result.isBeforeFirst() ) {             //Empty result set check
            return result;
        }
        else {
            result.next();
            String s = result.getString("USERID");

            String query2 = "SELECT * FROM user_interact WHERE USERID = ?";
            PreparedStatement preStat2 = connection.prepareStatement(query2);
            preStat2.setString(1, s);
            ResultSet result2 = preStat2.executeQuery();

            return result2;
        }
    }
}
