package DatabaseConnection;

import Responses.SongSearchResponse;
import utility.SongInfo;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SearchSongs extends DatabaseConnect{

    public static SongSearchResponse getSearchedSongs(String type, String searchKey) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(getSqlPath(), getSqlName(), getSqlPaswd());

            String searchQuery = null;
            //Query all relevant details relevant to search
            //Songs searched in case of Browse_Songs.Artist_Search
            if(type.equals("Artist")) {
                searchQuery= "SELECT songs.SONG_ID, songs.Name, songs.Genre, songs.Language, artists.Artist_Name, " +
                        "albums.Album_Name, songs.Upload_time FROM songs JOIN artists ON songs.ARTIST_ID = artists.ARTIST_ID " +
                        "JOIN albums ON songs.ALBUM_ID = albums.ALBUM_ID WHERE artists.Artist_Name LIKE ? ";
            }
            //Songs searched in case of Browse_Songs.Album_Search
            else if(type.equals("Album")) {
                searchQuery= "SELECT songs.SONG_ID, songs.Name, songs.Genre, songs.Language, artists.Artist_Name, " +
                        "albums.Album_Name, songs.Upload_time FROM songs JOIN artists ON songs.ARTIST_ID = artists.ARTIST_ID " +
                        "JOIN albums ON songs.ALBUM_ID = albums.ALBUM_ID WHERE albums.Album_Name LIKE ? ";
            }
            //Songs searched in case of Browse_Songs.Album_Search
            else if(type.equals("All_Songs")) {
                searchQuery= "SELECT songs.SONG_ID, songs.Name, songs.Genre, songs.Language, artists.Artist_Name, " +
                        "albums.Album_Name, songs.Upload_time FROM songs JOIN artists ON songs.ARTIST_ID = artists.ARTIST_ID " +
                        "JOIN albums ON songs.ALBUM_ID = albums.ALBUM_ID ORDER BY songs.Name ASC";
            }

            PreparedStatement preStat = connection.prepareStatement(searchQuery);
            if(type.equals("Artist")||type.equals("Album")) {
                preStat.setString(1, "%" + searchKey + "%");
            }

            //Corresponding result set
            ResultSet searchResult = preStat.executeQuery();

            //Result stored in an Arraylist
            ArrayList<SongInfo> searchList = new ArrayList<SongInfo>();
            SongInfo temp;
            while (searchResult.next()) {
                temp = new SongInfo(searchResult.getString("SONG_ID"), searchResult.getString("Name"),
                        searchResult.getString("Genre"), searchResult.getString("Language"), searchResult.getString("Artist_Name"),
                        searchResult.getString("Album_Name"), searchResult.getString("Upload_time"));

                searchList.add(temp);
            }

            SongSearchResponse response= new SongSearchResponse(searchList);
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
        //In case no viable list found
        return null;
    }


    public static ArrayList<String> getSongNames(ArrayList<String> songList) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(getSqlPath(), getSqlName(), getSqlPaswd());

            String searchQuery = "SELECT Name FROM songs WHERE SONG_ID = ?";
            PreparedStatement searchPreStat = connection.prepareStatement(searchQuery);

            ArrayList<String> searchedQueryList = new ArrayList<String>();

            for(String temp : songList) {
                searchPreStat.setString(1, temp);
                ResultSet resultSet = searchPreStat.executeQuery();
                resultSet.next();

                searchedQueryList.add(resultSet.getString("Name"));
            }

            return searchedQueryList;

        }  catch (ClassNotFoundException e) {
             e.printStackTrace();
        }  catch (SQLException throwables) {
                throwables.printStackTrace();
        }  finally {
            try {
                //Closes connection to avoid any database tampering
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        //In case no viable list found
        return null;
    }

    //Update database for song played
    public static void updateSongPlayed(String userid, String historyList, String song) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(getSqlPath(), getSqlName(), getSqlPaswd());

            //Updates View Count
            String updateViewQuery = "UPDATE songs SET Total_Views = Total_Views+1 WHERE SONG_ID = ?";
            PreparedStatement updateViewPreStat = connection.prepareStatement(updateViewQuery);
            updateViewPreStat.setString(1, song);
            updateViewPreStat.execute();

            //Updates History
            String updateHistoryQuery = "UPDATE user_interact SET History = ? WHERE USERID = ?";
            PreparedStatement updateHistoryPreStat = connection.prepareStatement(updateHistoryQuery);
            updateHistoryPreStat.setString(1, historyList);
            updateHistoryPreStat.setString(2, userid);
            updateHistoryPreStat.execute();

        }  catch (ClassNotFoundException e) {
            e.printStackTrace();
        }  catch (SQLException throwables) {
            throwables.printStackTrace();
        }  finally {
            try {
                //Closes connection to avoid any database tampering
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    //Update database for song played
    public static void updateSongLiked(String userid, String likeList) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(getSqlPath(), getSqlName(), getSqlPaswd());

            //Updates Liked
            String updateHistoryQuery = "UPDATE user_interact SET Liked = ? WHERE USERID = ?";
            PreparedStatement updateHistoryPreStat = connection.prepareStatement(updateHistoryQuery);
            updateHistoryPreStat.setString(1, likeList);
            updateHistoryPreStat.setString(2, userid);
            updateHistoryPreStat.execute();

        }  catch (ClassNotFoundException e) {
            e.printStackTrace();
        }  catch (SQLException throwables) {
            throwables.printStackTrace();
        }  finally {
            try {
                //Closes connection to avoid any database tampering
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
