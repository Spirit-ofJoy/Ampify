package DatabaseConnection;

import Responses.BrowseResponse;
import utility.SongInfo;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BrowseSongs extends DatabaseConnect{

    public static BrowseResponse getSongs() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(getSqlPath(), getSqlName(), getSqlPaswd());

            //Query all relevant details that may be used in display for Pop category
            String popQuery = "SELECT songs.SONG_ID, songs.Name, songs.Genre, songs.Language, artists.Artist_Name, " +
                    "albums.Album_Name, songs.Upload_time FROM songs JOIN artists ON songs.ARTIST_ID = artists.ARTIST_ID " +
                    "JOIN albums ON songs.ALBUM_ID = albums.ALBUM_ID WHERE songs.Genre = ? ";

            PreparedStatement popPreStat = connection.prepareStatement(popQuery);
            popPreStat.setString(1, "Pop");

            //SQL statement executed and corresponding result set obtained
            ResultSet popResult = popPreStat.executeQuery();

            //Result stored in an Arraylist
            ArrayList<SongInfo> popList = new ArrayList<SongInfo>();
            SongInfo temp;
            while (popResult.next()) {
                temp = new SongInfo(popResult.getString("SONG_ID"), popResult.getString("Name"),
                        popResult.getString("Genre"), popResult.getString("Language"), popResult.getString("Artist_Name"),
                        popResult.getString("Album_Name"), popResult.getString("Upload_time"));

                popList.add(temp);
            }

            //Query all relevant details that may be used in display for R & B category
            String rnbQuery = "SELECT songs.SONG_ID, songs.Name, songs.Genre, songs.Language, artists.Artist_Name, " +
                    "albums.Album_Name, songs.Upload_time FROM songs JOIN artists ON songs.ARTIST_ID = artists.ARTIST_ID " +
                    "JOIN albums ON songs.ALBUM_ID = albums.ALBUM_ID WHERE songs.Genre = ? ";

            PreparedStatement rnbPreStat = connection.prepareStatement(rnbQuery);
            rnbPreStat.setString(1, "R_&_B");

            //SQL statement executed and corresponding result set obtained
            ResultSet rnbResult = rnbPreStat.executeQuery();

            //Result stored in an Arraylist
            ArrayList<SongInfo> rnbList = new ArrayList<SongInfo>();
            while (rnbResult.next()) {
                temp = new SongInfo(rnbResult.getString("SONG_ID"), rnbResult.getString("Name"),
                        rnbResult.getString("Genre"), rnbResult.getString("Language"), rnbResult.getString("Artist_Name"),
                        rnbResult.getString("Album_Name"), rnbResult.getString("Upload_time"));

                rnbList.add(temp);
            }

            //Query all relevant details that may be used in display for Indie category
            String indieQuery = "SELECT songs.SONG_ID, songs.Name, songs.Genre, songs.Language, artists.Artist_Name, " +
                    "albums.Album_Name, songs.Upload_time FROM songs JOIN artists ON songs.ARTIST_ID = artists.ARTIST_ID " +
                    "JOIN albums ON songs.ALBUM_ID = albums.ALBUM_ID WHERE songs.Genre = ? ";

            PreparedStatement indiePreStat = connection.prepareStatement(indieQuery);
            indiePreStat.setString(1, "Indie");

            //SQL statement executed and corresponding result set obtained
            ResultSet indieResult = indiePreStat.executeQuery();

            //Result stored in an Arraylist
            ArrayList<SongInfo> indieList = new ArrayList<SongInfo>();
            while (indieResult.next()) {
                temp = new SongInfo(indieResult.getString("SONG_ID"), indieResult.getString("Name"),
                        indieResult.getString("Genre"), indieResult.getString("Language"), indieResult.getString("Artist_Name"),
                        indieResult.getString("Album_Name"), indieResult.getString("Upload_time"));

                indieList.add(temp);
            }

            //Query all relevant details that may be used in display for Rock category
            String rockQuery = "SELECT songs.SONG_ID, songs.Name, songs.Genre, songs.Language, artists.Artist_Name, " +
                    "albums.Album_Name, songs.Upload_time FROM songs JOIN artists ON songs.ARTIST_ID = artists.ARTIST_ID " +
                    "JOIN albums ON songs.ALBUM_ID = albums.ALBUM_ID WHERE songs.Genre = ? ";

            PreparedStatement rockPreStat = connection.prepareStatement(rockQuery);
            rockPreStat.setString(1, "Rock");

            //SQL statement executed and corresponding result set obtained
            ResultSet rockResult = rockPreStat.executeQuery();

            //Result stored in an Arraylist
            ArrayList<SongInfo> rockList = new ArrayList<SongInfo>();
            while (rockResult.next()) {
                temp = new SongInfo(rockResult.getString("SONG_ID"), rockResult.getString("Name"),
                        rockResult.getString("Genre"), rockResult.getString("Language"), rockResult.getString("Artist_Name"),
                        rockResult.getString("Album_Name"), rockResult.getString("Upload_time"));

                rockList.add(temp);
            }

            //Query all relevant details that may be used in display for Electro-Pop category
            String epopQuery = "SELECT songs.SONG_ID, songs.Name, songs.Genre, songs.Language, artists.Artist_Name, " +
                    "albums.Album_Name, songs.Upload_time FROM songs JOIN artists ON songs.ARTIST_ID = artists.ARTIST_ID " +
                    "JOIN albums ON songs.ALBUM_ID = albums.ALBUM_ID WHERE songs.Genre = ? ";

            PreparedStatement epopPreStat = connection.prepareStatement(epopQuery);
            epopPreStat.setString(1, "Electro_Pop");

            //SQL statement executed and corresponding result set obtained
            ResultSet epopResult = epopPreStat.executeQuery();

            //Result stored in an Arraylist
            ArrayList<SongInfo> epopList = new ArrayList<SongInfo>();
            while (epopResult.next()) {
                temp = new SongInfo(epopResult.getString("SONG_ID"), epopResult.getString("Name"),
                        epopResult.getString("Genre"), epopResult.getString("Language"), epopResult.getString("Artist_Name"),
                        epopResult.getString("Album_Name"), epopResult.getString("Upload_time"));

                epopList.add(temp);
            }


            //Query all relevant details that may be used in display for Hip Hop category
            String hiphopQuery = "SELECT songs.SONG_ID, songs.Name, songs.Genre, songs.Language, artists.Artist_Name, " +
                    "albums.Album_Name, songs.Upload_time FROM songs JOIN artists ON songs.ARTIST_ID = artists.ARTIST_ID " +
                    "JOIN albums ON songs.ALBUM_ID = albums.ALBUM_ID WHERE songs.Genre = ? ";

            PreparedStatement hiphopPreStat = connection.prepareStatement(hiphopQuery);
            hiphopPreStat.setString(1, "Hip_Hop");

            //SQL statement executed and corresponding result set obtained
            ResultSet hiphopResult = hiphopPreStat.executeQuery();

            //Result stored in an Arraylist
            ArrayList<SongInfo> hiphopList = new ArrayList<SongInfo>();
            while (hiphopResult.next()) {
                temp = new SongInfo(hiphopResult.getString("SONG_ID"), hiphopResult.getString("Name"),
                        hiphopResult.getString("Genre"), hiphopResult.getString("Language"), hiphopResult.getString("Artist_Name"),
                        hiphopResult.getString("Album_Name"), hiphopResult.getString("Upload_time"));

                hiphopList.add(temp);
            }

            //Query all relevant details that may be used in display for Electronic category
            String elecQuery = "SELECT songs.SONG_ID, songs.Name, songs.Genre, songs.Language, artists.Artist_Name, " +
                    "albums.Album_Name, songs.Upload_time FROM songs JOIN artists ON songs.ARTIST_ID = artists.ARTIST_ID " +
                    "JOIN albums ON songs.ALBUM_ID = albums.ALBUM_ID WHERE songs.Genre = ? ";

            PreparedStatement elecPreStat = connection.prepareStatement(elecQuery);
            elecPreStat.setString(1, "Electronic");

            //SQL statement executed and corresponding result set obtained
            ResultSet elecResult = elecPreStat.executeQuery();

            //Result stored in an Arraylist
            ArrayList<SongInfo> elecList = new ArrayList<SongInfo>();
            while (elecResult.next()) {
                temp = new SongInfo(elecResult.getString("SONG_ID"), elecResult.getString("Name"),
                        elecResult.getString("Genre"), elecResult.getString("Language"), elecResult.getString("Artist_Name"),
                        elecResult.getString("Album_Name"), elecResult.getString("Upload_time"));

                elecList.add(temp);
            }


            //Sending back appropriate response to HandleClient
            BrowseResponse response = new BrowseResponse(popList, rnbList, indieList, rockList, epopList, hiphopList, elecList);
            return response;


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
