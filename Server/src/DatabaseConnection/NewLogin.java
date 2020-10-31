package DatabaseConnection;

import Responses.LoginResponse;
import constants.Constant;
import utility.PasswordEncryptor;

import java.security.NoSuchAlgorithmException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NewLogin extends DatabaseConnect{

    //Checks login credentials and returns appropriate profile
    public static LoginResponse checkLogin(String uname, String paswd)  {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(getSqlPath(), getSqlName(), getSqlPaswd());    //Connected to database

            String query = "SELECT USERID FROM users WHERE Uname = ? AND Password = ?";
            PreparedStatement preStat = connection.prepareStatement(query);
            preStat.setString(1, uname);
            preStat.setString(2, PasswordEncryptor.encryptText(paswd));
            ResultSet result = preStat.executeQuery();


            if ( !result.isBeforeFirst() ) {             //Empty result set check
                LoginResponse response = new LoginResponse(String.valueOf(Constant.USER_NOT_FOUND));

                return response;
            }
            else {
                result.next();
                String s = result.getString("USERID");

                String query2 = "SELECT * FROM user_interact WHERE USERID = ?";
                PreparedStatement preStat2 = connection.prepareStatement(query2);
                preStat2.setString(1, s);

                ResultSet result2 = preStat2.executeQuery();
                result2.next();

                LoginResponse response = new LoginResponse(result2.getString("USERID"),
                    result2.getString("History"), result2.getString("Liked"),
                    result2.getString("Disliked"), result2.getString("Playlists"));

                return response;
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            try {
                //Closes connection to avoid any database tampering
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        //Return null in case an exception stops normal functioning
        return null;
    }


}
