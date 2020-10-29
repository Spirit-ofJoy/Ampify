package DatabaseConnection;

import Responses.NewReleasesResponse;
import Responses.RecommendsResponse;
import Responses.TopHitsResponse;
import utility.SongInfo;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

public class LoadProfile extends DatabaseConnect{

    public static TopHitsResponse getMostViewed() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(getSqlPath(), getSqlName(), getSqlPaswd());

            //Query all relevant details that may be used in display
            String query = "SELECT songs.SONG_ID, songs.Name, songs.Genre, songs.Language, artists.Artist_Name, albums.Album_Name, songs.Upload_time FROM songs JOIN artists ON songs.ARTIST_ID = artists.ARTIST_ID JOIN albums ON songs.ALBUM_ID = albums.ALBUM_ID WHERE songs.ALBUM_ID = albums.ALBUM_ID ORDER BY (Total_Views) DESC limit 5";

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


    public static RecommendsResponse getRecommends(String uid, TreeSet likedSongs) {

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

            String[] prefLang = null;
            String[] prefGenre = null;
            String[] prefArtist = null;

            if (langResult!=null) {
                prefLang = langResult.split("-");
            }
            if (GenreResult!= null) {
                prefGenre = GenreResult.split("-");
            }
            if (ArtistResult!= null) {
                prefArtist = ArtistResult.split("-");
            }

            String likedArtist = "";
            String likedAlbum = "";

            //Get Album, Genre and Artist info of a liked song
            if(!(likedSongs.isEmpty())) {
                String query2 = "SELECT  ARTIST_ID, ALBUM_ID FROM songs WHERE SONG_ID = ?";
                PreparedStatement preStat2 = connection.prepareStatement(query2);
                ArrayList<String> likedSongsList = new ArrayList<>(likedSongs);
                Collections.shuffle(likedSongsList);
                preStat2.setString(1, likedSongsList.get(0));
                ResultSet result2 = preStat2.executeQuery();
                result2.next();

                likedArtist = result2.getString("ARTIST_ID");
                likedAlbum = result2.getString("ALBUM_ID");
            }

            String mainQuery = "";
            int i = 0, j = 0, k = 0;

            if (prefLang!=null && (prefGenre!=null||prefArtist!=null)) {
                //Preparing SQL Statement to sort out according to preferences
                mainQuery += "SELECT songs.SONG_ID, songs.Name, songs.Genre, songs.Language, artists.Artist_Name, albums.Album_Name, songs.Upload_time FROM songs JOIN artists ON songs.ARTIST_ID = artists.ARTIST_ID JOIN albums ON songs.ALBUM_ID = albums.ALBUM_ID WHERE (1=2 ";

                //Dynamic SQL prepared
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
                mainQuery += ")) ";
            }
            else {
                mainQuery = "SELECT songs.SONG_ID, songs.Name, songs.Genre, songs.Language, artists.Artist_Name, albums.Album_Name, songs.Upload_time FROM songs JOIN artists ON songs.ARTIST_ID = artists.ARTIST_ID JOIN albums ON songs.ALBUM_ID = albums.ALBUM_ID WHERE 1=2 ";
            }

            //Adds Recommends Based on likes
            if(!(likedSongs.isEmpty())){
                mainQuery += "UNION SELECT songs.SONG_ID, songs.Name, songs.Genre, songs.Language, artists.Artist_Name, albums.Album_Name, songs.Upload_time FROM songs JOIN artists ON songs.ARTIST_ID = artists.ARTIST_ID JOIN albums ON songs.ALBUM_ID = albums.ALBUM_ID WHERE songs.ARTIST_ID = ? OR songs.ALBUM_ID = ?";
            }

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
            if(!(likedSongs.isEmpty())){
                preStatMain.setString(itr, likedArtist);
                itr++;
                preStatMain.setString(itr, likedAlbum);
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
            //To randomize the recommendations at any given point of time
            Collections.shuffle(recommendations);

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

    public static NewReleasesResponse getNewReleases() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(getSqlPath(), getSqlName(), getSqlPaswd());

            //Query all relevant details that may be used in display
            String query = "SELECT songs.SONG_ID, songs.Name, songs.Genre, songs.Language, artists.Artist_Name, albums.Album_Name, songs.Upload_time FROM songs JOIN artists ON songs.ARTIST_ID = artists.ARTIST_ID JOIN albums ON songs.ALBUM_ID = albums.ALBUM_ID WHERE songs.ALBUM_ID = albums.ALBUM_ID ORDER BY Upload_time DESC limit 5";

            PreparedStatement preStat = connection.prepareStatement(query);

            //SQL statement executed and corresponding result set obtained
            ResultSet result = preStat.executeQuery();

            //Response for client returned to HandleClient
            ArrayList<SongInfo> newList = new ArrayList<SongInfo>();
            SongInfo temp;
            while (result.next()) {
                temp = new SongInfo(result.getString("SONG_ID"), result.getString("Name"),
                        result.getString("Genre"), result.getString("Language"), result.getString("Artist_Name"),
                        result.getString("Album_Name"), result.getString("Upload_time"));

                newList.add(temp);
            }

            NewReleasesResponse response = new NewReleasesResponse(newList);
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
