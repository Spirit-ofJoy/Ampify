package DatabaseConnection;

import Responses.SignUpResponse;
import constants.Constant;
import utility.PasswordEncryptor;

import java.security.NoSuchAlgorithmException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NewSignUp extends DatabaseConnect {

    public static SignUpResponse createAccount(String uname, String paswd, String prefLang, String prefGenre, String prefArtist)  {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(getSqlPath(), getSqlName(), getSqlPaswd());    //Connected to database

            //Get number of users in database
            String query = "SELECT COUNT(USERID) AS UserCount FROM users";
            PreparedStatement preStat = connection.prepareStatement(query);
            ResultSet count = preStat.executeQuery();
            count.next();

            //Determine the new User Id
            String usrCount = count.getString("UserCount");
            int userCount = Integer.parseInt(usrCount);
            userCount = userCount +1;
            usrCount = String.format("%03d", userCount);
            String userID = "U#"+usrCount;

            //Inserting values in users table
            String userQuery = "INSERT INTO users(USERID, Uname, Password) VALUES ( ?, ?, ?)";
            PreparedStatement userPrepStat = connection.prepareStatement(userQuery);
            userPrepStat.setString(1, userID);
            userPrepStat.setString(2, uname);
            userPrepStat.setString(3, PasswordEncryptor.encryptText(paswd));
            userPrepStat.execute();

            //Inserting values in users table
            String userInteractQuery = "INSERT INTO user_interact(USERID, Pref_lang, Pref_genre, Pref_artist) VALUES ( ?, ?, ?, ?)";
            PreparedStatement userInteractPrepStat = connection.prepareStatement(userInteractQuery);
            userInteractPrepStat.setString(1, userID);
            userInteractPrepStat.setString(2, prefLang);
            userInteractPrepStat.setString(3, prefGenre);
            userInteractPrepStat.setString(4, prefArtist);
            userInteractPrepStat.execute();

            return(new SignUpResponse(String.valueOf(Constant.SUCCESS)));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return(new SignUpResponse(String.valueOf(Constant.FAILURE)));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return(new SignUpResponse(String.valueOf(Constant.FAILURE)));
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
        return null;
    }
}
