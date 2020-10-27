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

            String searchQuery = "UPDATE playlists SET Songs_incl = ? WHERE (Viewer = ? AND PLAYLIST_ID = ?)";
            PreparedStatement updatePreStat = connection.prepareStatement(searchQuery);

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


}
