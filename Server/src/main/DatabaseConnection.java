package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

//Class to connect and interact with mysql database
public class DatabaseConnection {

    private static final String sqlPath = "jdbc:mysql://localhost:3306/ampify";
    private static final String sqlPaswd = "password";
    private static final String sqlName = "java";

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

    public static ResultSet getMostViewed() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(sqlPath, sqlName, sqlPaswd); //Connected to database

        //Query all relevant details that may be used in display
        String query = "SELECT songs.SONG_ID, songs.Name, songs.Genre, songs.Language, artists.Artist_Name, " +
                "albums.Album_Name, songs.Upload_time FROM songs JOIN artists ON songs.ARTIST_ID = artists.ARTIST_ID " +
                "JOIN albums ON albums.ARTIST_ID = artists.ARTIST_ID WHERE songs.ALBUM_ID = albums.ALBUM_ID " +
                "ORDER BY (Total_Views+Views_2+Views_1) DESC limit 5";

        PreparedStatement preStat = connection.prepareStatement(query);
        ResultSet result = preStat.executeQuery();

        return result;
    }

    public static ResultSet getRecommends(String id, ArrayList<String> l) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(sqlPath, sqlName, sqlPaswd); //Connected to database

        //if(l.size()<3) {
            String query1 = "SELECT  Pref_lang, Pref_genre, Pref_artist FROM user_interact WHERE USERID = ?";
            PreparedStatement preStat1 = connection.prepareStatement(query1);
            preStat1.setString(1, id);
            ResultSet result1 = preStat1.executeQuery();

            result1.next();
            ArrayList<String> prefLang = new ArrayList<String>();
            ArrayList<String> prefGenre = new ArrayList<String>();
            ArrayList<String> prefArtist = new ArrayList<String>();

            for (String temp: result1.getString("Pref_lang").split(".")){
                prefLang.add(temp);
            }
            for (String temp: result1.getString("Pref_genre").split(".")){
                prefGenre.add(temp);
            }
            for (String temp: result1.getString("Pref_artist").split(".")){
                prefArtist.add(temp);
            }

            //Preparing SQL Statement to sort out according to preferences
            String mainQuery = "SELECT songs.SONG_ID, songs.Name, songs.Genre, songs.Language, artists.Artist_Name, albums.Album_Name FROM songs " +
                    "JOIN artists ON songs.ARTIST_ID = artists.ARTIST_ID JOIN albums ON albums.ARTIST_ID = artists.ARTIST_ID " +
                    "WHERE (1=2 ";

            //Dynamic SQL prepared
            int i=0, j=0, k=0;
            while (i<prefLang.size()){
                mainQuery += "OR songs.Language = ?";
                i++;
            }
            mainQuery +=") AND ((1=2 ";

            while (j<prefGenre.size()){
                mainQuery += "OR songs.Genre = ?";
                j++;
            }
            mainQuery +=") OR (1=2 ";

            while (k<prefGenre.size()){
                mainQuery += "OR songs.ARTIST_ID = ?";
                k++;
            }
            mainQuery +="))";

            PreparedStatement preStatMain = connection.prepareStatement(mainQuery);
            int itr =1;

            //Dynamic entry of specified strings
            while (itr<=i) {
                preStatMain.setString(itr, prefLang.get(itr-1));
                itr++;
            }
            while (itr<=j) {
                preStatMain.setString(itr, prefGenre.get(itr-(1+i)));
                itr++;
            }
            while (itr<=k) {
                preStatMain.setString(itr, prefArtist.get(itr-(1+i+j)));
                itr++;
            }

            ResultSet personalRecommends = preStatMain.executeQuery();
            return personalRecommends;

        //}
        /*else {
            String query2 = "SELECT songs.SONG_ID, songs.Name, songs.Genre, songs.Language, artists.Artist_Name, " +
                    "albums.Album_Name FROM songs JOIN artists ON songs.ARTIST_ID = artists.ARTIST_ID " +
                    "JOIN albums ON albums.ARTIST_ID = artists.ARTIST_ID WHERE songs.ALBUM_ID = albums.ALBUM_ID " +
                    "ORDER BY (Total_Views+Views_2+Views_1) DESC limit 5";

            PreparedStatement preStat2 = connection.prepareStatement(query2);
            ResultSet result2 = preStat2.executeQuery();
        }*/
    }


}
