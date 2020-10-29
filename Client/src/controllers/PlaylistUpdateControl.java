package controllers;

import Requests.PersonalPlaylistsRequest;
import Requests.SongSearchRequest;
import Requests.UpdatePlaylistRequest;
import Responses.SongSearchResponse;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import main.ClientMain;
import utility.Playlist;
import utility.SongInfo;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PlaylistUpdateControl implements Initializable {
    public Button backButton;
    public Button updatePlaylistBtn;
    public Button addToPlaylistBtn;
    public Button removeFromPlaylistBtn;

    public ListView allSongsListView;
    public ListView currentSongsListView;

    private ArrayList<SongInfo> allSongsList = new ArrayList<SongInfo>();
    private synchronized void addToSongsCollection(SongInfo Song) {
        allSongsList.add(Song);
    }

    private Playlist currentPlaylist;
    private String temp = "";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Runnable getAllSongs = new Runnable() {
            @Override
            public void run() {
                try {
                    ClientMain.clientOutputStream.writeObject(new SongSearchRequest("All_Songs", "searchkey"));
                    ClientMain.clientOutputStream.flush();                                                //Request sent
                    SongSearchResponse incomingResponse;
                    incomingResponse = (SongSearchResponse) ClientMain.clientInputStream.readObject();   //Response accepted

                    for (int i = 0; i < incomingResponse.searchedSongs.size(); i++) {
                        SongInfo temp = (SongInfo) incomingResponse.searchedSongs.get(i);
                        addToSongsCollection(temp);
                    }

                    //Update GUI for results
                    Platform.runLater(() -> {
                        //Update GUI for Searched songs to load
                        for (int i = 0; i < allSongsList.size(); i++) {
                            allSongsListView.getItems().add(allSongsList.get(i).getSongDescription());
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread loadAllSongs = new Thread(getAllSongs);
        loadAllSongs.start();
    }

    public void getSelectedPlaylist(Playlist playlist) {
        currentPlaylist = playlist;

        for (int i=0; i< currentPlaylist.songNames.size(); i++) {
            currentSongsListView.getItems().add(currentPlaylist.songNames.get(i));
        }
    }

    public void addToPlaylist() {
        int index =  allSongsListView.getSelectionModel().getSelectedIndex();

        //Gets name and Song ID of selected song to add
        String songName = allSongsList.get(index).getSongName();
        String songID = allSongsList.get(index).getSongID();

        //Add to current Client side playlist
        currentPlaylist.songNames.add(songName);
        currentPlaylist.getSongID().add(songID);

        //Update display to the changes
        currentSongsListView.getItems().clear();
        for (int i=0; i< currentPlaylist.songNames.size(); i++) {
            currentSongsListView.getItems().add(currentPlaylist.songNames.get(i));
            System.out.println(currentPlaylist.getSongID().get(i));
        }
    }

    public void removeFromPlaylist() {
        int index =  currentSongsListView.getSelectionModel().getSelectedIndex();

        //Removes name and Song ID of selected song to add
        currentPlaylist.songNames.remove(index);
        currentPlaylist.getSongID().remove(index);

        //Update display to the changes
        currentSongsListView.getItems().clear();
        for (int i=0; i< currentPlaylist.songNames.size(); i++) {
            currentSongsListView.getItems().add(currentPlaylist.songNames.get(i));
            System.out.println(currentPlaylist.getSongID().get(i));
        }

    }

    public void updateSelectedPlaylist() {

        for (int i=0; i< currentPlaylist.songNames.size(); i++) {
            temp += currentPlaylist.getSongID().get(i) + "-";
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Update current viewable playlist at server
                    ClientMain.clientOutputStream.writeObject(new UpdatePlaylistRequest(temp, currentPlaylist.currViewer, currentPlaylist.getPlaylistID()));
                    ClientMain.clientOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
