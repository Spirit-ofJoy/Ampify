package DatabaseConnection;

import utility.Playlist;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlaylistPick extends DatabaseConnect{

    public static ArrayList<Playlist> getPersonalPlaylist(String id) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(getSqlPath(), getSqlName(), getSqlPaswd());

            String searchQuery = "SELECT PLAYLIST_ID, Playlist_Name, Owner, Songs_incl, Visibility FROM playlists WHERE Viewer = ?";
            PreparedStatement searchPreStat = connection.prepareStatement(searchQuery);

            searchPreStat.setString(1, id);
            ResultSet resultSet = searchPreStat.executeQuery();

            ArrayList<Playlist> viewablePlaylists = new ArrayList<Playlist>();
            while(resultSet.next()) {
                viewablePlaylists.add(new Playlist( resultSet.getString("PLAYLIST_ID"), resultSet.getString("Playlist_Name"),
                        resultSet.getString("Owner"), resultSet.getString("Songs_incl"),
                        Integer.parseInt(resultSet.getString("Visibility")), id ));
            }

            for (int i=0; i<viewablePlaylists.size(); i++) {
                ArrayList<String> temp = SearchSongs.getSongNames(viewablePlaylists.get(i).getSongID());
                viewablePlaylists.get(i).songNames = temp;
            }

            return viewablePlaylists;

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

    public static void updatingPlaylist(String songsList, String viewerID, String playlistID) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(getSqlPath(), getSqlName(), getSqlPaswd());

            String updateQuery = "UPDATE playlists SET Songs_incl = ? WHERE (Viewer = ? AND PLAYLIST_ID = ?)";
            PreparedStatement updatePreStat = connection.prepareStatement(updateQuery);

            updatePreStat.setString(1, songsList);
            updatePreStat.setString(2, viewerID);
            updatePreStat.setString(3, playlistID);
            updatePreStat.execute();

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

    public static void creatingPlaylist(String songsList, String ownerID, String playlistName, int visibility) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(getSqlPath(), getSqlName(), getSqlPaswd());

            //Finding Indexing value
            String indexQuery = "SELECT COUNT(*) FROM playlists;";
            PreparedStatement indexPreStat = connection.prepareStatement(indexQuery);
            ResultSet indexResultSet = indexPreStat.executeQuery();
            indexResultSet.next();
            int index = indexResultSet.getInt(1);
            index++;

            //Finding New Playlist ID value
            String pidQuery = "SELECT COUNT(DISTINCT PLAYLIST_ID) FROM playlists;";
            PreparedStatement pidPreStat = connection.prepareStatement(pidQuery);
            ResultSet pidResultSet = pidPreStat.executeQuery();
            pidResultSet.next();
            String playlistCnt = pidResultSet.getString(1);
            int playlistCount = Integer.parseInt(playlistCnt);
            playlistCount = playlistCount +1;
            playlistCnt = String.format("%03d", playlistCount);
            String playlistID = "P#"+playlistCnt;


            //Create Playlist in Database
            String insertionQuery = "INSERT INTO playlists (Indexing, PLAYLIST_ID, Playlist_Name, Owner, Songs_incl, Viewer, Visibility) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertionPreStat = connection.prepareStatement(insertionQuery);

            insertionPreStat.setInt(1, index);
            insertionPreStat.setString(2, playlistID);
            insertionPreStat.setString(3, playlistName);
            insertionPreStat.setString(4, ownerID);
            insertionPreStat.setString(5, songsList);
            insertionPreStat.setString(6, ownerID);
            insertionPreStat.setString(7, String.valueOf(visibility));
            insertionPreStat.execute();

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

    public static Playlist getGroupPlaylist(String gid) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(getSqlPath(), getSqlName(), getSqlPaswd());

            String searchQuery = "SELECT PLAYLIST_ID, Playlist_Name, Owner, Songs_incl, Visibility FROM playlists WHERE Owner = ?";
            PreparedStatement searchPreStat = connection.prepareStatement(searchQuery);

            searchPreStat.setString(1, gid);
            ResultSet resultSet = searchPreStat.executeQuery();
            resultSet.next();

            Playlist grpPlaylist = new Playlist( resultSet.getString("PLAYLIST_ID"), resultSet.getString("Playlist_Name"),
                    resultSet.getString("Owner"), resultSet.getString("Songs_incl"),
                    Integer.parseInt(resultSet.getString("Visibility")), gid );

            ArrayList<String> temp = SearchSongs.getSongNames(grpPlaylist.getSongID());
            grpPlaylist.songNames = temp;

            return grpPlaylist;

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


    public static ArrayList<Playlist> getShareablePlaylist(String id) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(getSqlPath(), getSqlName(), getSqlPaswd());

            String searchQuery = "SELECT PLAYLIST_ID, Playlist_Name, Owner, Songs_incl, Visibility FROM playlists WHERE Owner = ? OR (Viewer = ? AND Visibility = ?)";
            PreparedStatement searchPreStat = connection.prepareStatement(searchQuery);
            searchPreStat.setString(1, id);
            searchPreStat.setString(2, id);
            searchPreStat.setInt(3, 1);
            ResultSet resultSet = searchPreStat.executeQuery();

            ArrayList<Playlist> shareablePlaylists = new ArrayList<Playlist>();
            while(resultSet.next()) {
                shareablePlaylists.add(new Playlist( resultSet.getString("PLAYLIST_ID"), resultSet.getString("Playlist_Name"),
                        resultSet.getString("Owner"), resultSet.getString("Songs_incl"),
                        Integer.parseInt(resultSet.getString("Visibility")), id ));
            }

            for (int i=0; i<shareablePlaylists.size(); i++) {
                ArrayList<String> temp = SearchSongs.getSongNames(shareablePlaylists.get(i).getSongID());
                shareablePlaylists.get(i).songNames = temp;
            }

            return shareablePlaylists;

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

    public static void importingPlaylist(String playlistID, String viewerID) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(getSqlPath(), getSqlName(), getSqlPaswd());

            //Finding Indexing value
            String indexQuery = "SELECT COUNT(*) FROM playlists;";
            PreparedStatement indexPreStat = connection.prepareStatement(indexQuery);
            ResultSet indexResultSet = indexPreStat.executeQuery();
            indexResultSet.next();
            int index = indexResultSet.getInt(1);
            index++;

            //Finding Playlist details
            String pSearchQuery = "SELECT * FROM playlists WHERE PLAYLIST_ID = ?;";
            PreparedStatement pSearchPreStat = connection.prepareStatement(pSearchQuery);
            pSearchPreStat.setString(1, playlistID);
            ResultSet pSearchResultSet = pSearchPreStat.executeQuery();
            pSearchResultSet.next();

            //Create Playlist in Database
            String insertionQuery = "INSERT INTO playlists (Indexing, PLAYLIST_ID, Playlist_Name, Owner, Songs_incl, Viewer, Visibility) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertionPreStat = connection.prepareStatement(insertionQuery);

            insertionPreStat.setInt(1, index);
            insertionPreStat.setString(2, playlistID);
            insertionPreStat.setString(3, pSearchResultSet.getString("Playlist_Name"));
            insertionPreStat.setString(4, pSearchResultSet.getString("Owner"));
            insertionPreStat.setString(5, pSearchResultSet.getString("Songs_incl"));
            insertionPreStat.setString(6, viewerID);
            insertionPreStat.setString(7, pSearchResultSet.getString("Visibility"));
            insertionPreStat.execute();

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
