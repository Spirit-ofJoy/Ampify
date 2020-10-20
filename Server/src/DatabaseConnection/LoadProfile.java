package DatabaseConnection;

import Responses.RecommendsResponse;
import Responses.TopHitsResponse;
import main.SongInfo;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoadProfile extends DatabaseConnect{

    public static TopHitsResponse getMostViewed() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(getSqlPath(), getSqlName(), getSqlPaswd());

            //Query all relevant details that may be used in display
            String query = "SELECT songs.SONG_ID, songs.Name, songs.Genre, songs.Language, artists.Artist_Name, " +
                "albums.Album_Name, songs.Upload_time FROM songs JOIN artists ON songs.ARTIST_ID = artists.ARTIST_ID " +
                "JOIN albums ON albums.ARTIST_ID = artists.ARTIST_ID WHERE songs.ALBUM_ID = albums.ALBUM_ID " +
                "ORDER BY (Total_Views+Views_2+Views_1) DESC limit 5";

            PreparedStatement preStat = connection.prepareStatement(query);

            //SQL statement executed and corresponding result set obtained
            ResultSet result = preStat.executeQuery();

            //Response for client returned to HandleClient
            ArrayList<SongInfo> topHitsList = new ArrayList<SongInfo>();
            SongInfo temp;
            while (result.next()) {
                temp = new SongInfo(result.getString("SONG_ID"), result.getString("Name"),
                        result.getString("Genre"), result.getString("Language"), result.getString("Artist_Name"),
                        result.getString("Album_Name"), result.getString("Upload_time"));

                topHitsList.add(temp);
            }

            TopHitsResponse response = new TopHitsResponse(topHitsList);
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

    public static RecommendsResponse getRecommends(String uid) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(getSqlPath(), getSqlName(), getSqlPaswd());

            //Get Preferences of the user
            String query1 = "SELECT  * FROM user_interact WHERE USERID = ?";
            PreparedStatement preStat1 = connection.prepareStatement(query1);
            preStat1.setString(1, uid);
            ResultSet result1 = preStat1.executeQuery();
            result1.next();

            String langResult = result1.getString("Pref_lang");
            String GenreResult = result1.getString("Pref_genre");
            String ArtistResult = result1.getString("Pref_artist");

            String[] prefLang = langResult.split("-");
            String[] prefGenre = GenreResult.split("-");
            String[] prefArtist = ArtistResult.split("-");

            //Preparing SQL Statement to sort out according to preferences
            String mainQuery = "SELECT songs.SONG_ID, songs.Name, songs.Genre, songs.Language, artists.Artist_Name, albums.Album_Name, " +
                    "songs.Upload_time FROM songs JOIN artists ON songs.ARTIST_ID = artists.ARTIST_ID " +
                    "JOIN albums ON albums.ARTIST_ID = artists.ARTIST_ID WHERE (1=2 ";

            //Dynamic SQL prepared
            int i = 0, j = 0, k = 0;

            while (i < prefLang.length) {
                mainQuery += "OR songs.Language = ? ";
                i++;
            }
            mainQuery += ") AND ((1=2 ";

            while (j < prefGenre.length) {
                mainQuery += "OR songs.Genre = ? ";
                j++;
            }
            mainQuery += ") OR (1=2 ";

            while (k < prefArtist.length) {
                mainQuery += "OR songs.ARTIST_ID = ? ";
                k++;
            }
            mainQuery += ")) GROUP BY songs.SONG_ID";

            PreparedStatement preStatMain = connection.prepareStatement(mainQuery);

            int itr = 1;
            //Dynamic entry of specified strings
            while (itr <= i) {
                preStatMain.setString(itr, prefLang[itr - 1]);
                itr++;
            }
            while (itr <= j + i) {
                preStatMain.setString(itr, prefGenre[itr - (1 + i)]);
                itr++;
            }
            while (itr <= k + i + j) {
                preStatMain.setString(itr, prefArtist[itr - (1 + i + j)]);
                itr++;
            }

            //SQL statement executed and corresponding result set obtained
            ResultSet personalRecommends = preStatMain.executeQuery();

            //Response for client returned to HandleClient
            ArrayList<SongInfo> recommendations = new ArrayList<SongInfo>();
            SongInfo temp;
            while (personalRecommends.next()) {
                temp = new SongInfo(personalRecommends.getString("SONG_ID"), personalRecommends.getString("Name"),
                        personalRecommends.getString("Genre"), personalRecommends.getString("Language"), personalRecommends.getString("Artist_Name"),
                        personalRecommends.getString("Album_Name"), personalRecommends.getString("Upload_time"));

                recommendations.add(temp);
            }

            RecommendsResponse response = new RecommendsResponse(recommendations);
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
