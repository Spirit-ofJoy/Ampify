package controllers;


import Requests.*;
import Responses.PersonalPlaylistsResponse;
import Responses.ShareablePlaylistsResponse;
import Responses.SongSearchResponse;
import ampify_player.Song_Queue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import main.ClientMain;
import utility.ActiveProfile;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import utility.Playlist;
import utility.SongInfo;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PlaylistsControl implements Initializable {

    //------*------*------*------*------*------*Play Playlists Section*------*------*------*------*------*------//
    public Button backButton;
    public Button personalAddQueueBtn;
    public ListView personalPlaylistsListView;
    public Button modifyPlaylistBtn;

    private ActiveProfile currProfile = ActiveProfile.getProfile();
    private ArrayList<Playlist> personalPlaylists = new ArrayList<Playlist>();
    private synchronized void addToPersonalPlaylists(Playlist songCollection) {
        personalPlaylists.add(songCollection);
    }


    //Go back to profile
    public void backToProfile() throws IOException {
        System.out.println("[CLIENT] Profile Page invoked.");

        Parent profileRoot = FXMLLoader.load(getClass().getResource("/resources/profile.fxml"));
        Scene profileScene = new Scene(profileRoot);
        Stage profileStage = (Stage) backButton.getScene().getWindow();
        profileStage.setScene(profileScene);
        profileStage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        playlistCodeTextFld.setEditable(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Get all of viewable playlists from server current user
                    ClientMain.clientOutputStream.writeObject(new PersonalPlaylistsRequest(currProfile.getUserID()));
                    ClientMain.clientOutputStream.flush();
                    PersonalPlaylistsResponse playlistsResponse = (PersonalPlaylistsResponse) ClientMain.clientInputStream.readObject();

                    //Store in local arraylist
                    for(int i=0; i< playlistsResponse.personalPlaylists.size(); i++) {
                        addToPersonalPlaylists(playlistsResponse.personalPlaylists.get(i));
                    }

                    //Get all songs in the database
                    ClientMain.clientOutputStream.writeObject(new SongSearchRequest("All_Songs", "searchkey"));
                    ClientMain.clientOutputStream.flush();                                                //Request sent
                    SongSearchResponse incomingResponse;
                    incomingResponse = (SongSearchResponse) ClientMain.clientInputStream.readObject();   //Response accepted

                    for (int i = 0; i < incomingResponse.searchedSongs.size(); i++) {
                        SongInfo temp = (SongInfo) incomingResponse.searchedSongs.get(i);
                        addToSongsCollection(temp);
                    }

                    //Get sharable playlists
                    ClientMain.clientOutputStream.writeObject(new ShareablePlaylistsRequest(currProfile.getUserID()));
                    ClientMain.clientOutputStream.flush();
                    ShareablePlaylistsResponse shareablePlaylistsResponse = (ShareablePlaylistsResponse) ClientMain.clientInputStream.readObject();

                    //Store in local arraylist
                    for(int i=0; i< shareablePlaylistsResponse.sharePlaylists.size(); i++) {
                        addToSharePlaylists(shareablePlaylistsResponse.sharePlaylists.get(i));
                    }

                    //Display on GUI
                    Platform.runLater(() -> {
                        //Update GUI to show viewable playlists
                        String display ;
                        for(int i = 0; i< playlistsResponse.personalPlaylists.size(); i++){
                            display = "❒" +personalPlaylists.get(i).getName();

                            for(int j=0; j< personalPlaylists.get(i).songNames.size(); j++) {
                                display +="\n    » " +personalPlaylists.get(i).songNames.get(j);
                            }

                            personalPlaylistsListView.getItems().add(display);
                        }

                        //Update GUI for Searched songs to load
                        for (int i = 0; i < allSongsList.size(); i++) {
                            allSongsListView.getItems().add(allSongsList.get(i).getSongDescription());
                        }

                        //Update GUI to show shareable playlists
                        for(int i = 0; i< shareablePlaylistsResponse.sharePlaylists.size(); i++){
                            display = "❒" +sharePlaylists.get(i).getName();

                            for(int j=0; j< sharePlaylists.get(i).songNames.size(); j++) {
                                display +="\n    » " +sharePlaylists.get(i).songNames.get(j);
                            }

                            sharePlaylistsListView.getItems().add(display);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void personalAddToQueue() {
        //Adding to active Queue
        int index = personalPlaylistsListView.getSelectionModel().getSelectedIndex();
        for(int i=0; i< personalPlaylists.get(index).songNames.size(); i++) {
            Song_Queue.addToQueue(personalPlaylists.get(index).getSongID().get(i), personalPlaylists.get(index).songNames.get(i));
        }
    }

    public void modifyPlaylist() throws IOException {

        //Setting up next scene
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/resources/playlistUpdate.fxml"));
        Parent modifyPlaylistRoot = loader.load();

        Scene modifyPlaylistScene = new Scene(modifyPlaylistRoot);

        //Passing info to new scene
        int index = personalPlaylistsListView.getSelectionModel().getSelectedIndex();
        PlaylistUpdateControl updateController = loader.getController();
        updateController.getSelectedPlaylist(personalPlaylists.get(index));

        Stage modifyPlaylistStage = new Stage();
        modifyPlaylistStage.setScene(modifyPlaylistScene);
        modifyPlaylistStage.setTitle("Modify Playlist");
        modifyPlaylistStage.show();

    }

    //------*------*------*------*------*------*Create Playlists Section*------*------*------*------*------*------//

    public Button importPlaylistBtn;
    public Button addToPlaylistBtn;
    public Button removeFromPlaylistBtn;
    public TextField newPlaylistName;
    public TextField importPlaylistTextFld;
    public CheckBox playlistPrivate;


    public ListView allSongsListView;
    public ListView currentSongsListView;

    private Playlist newCreatedPlaylist = new Playlist(currProfile.getUserID());
    private String temp = "";
    private ArrayList<SongInfo> allSongsList = new ArrayList<SongInfo>();
    private synchronized void addToSongsCollection(SongInfo Song) {
        allSongsList.add(Song);
    }


    public void importPlaylist() {
        String code = importPlaylistTextFld.getText();

        if(code.substring(0, 22).equals("ampify/share_playlist/")) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //Create new playlist and store on Server Database
                        ClientMain.clientOutputStream.writeObject(new ImportPlaylistRequest(currProfile.getUserID(), code.substring(22)));
                        ClientMain.clientOutputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }else {
            importPlaylistTextFld.setText("Invalid code");
        }
    }



    public void addToPlaylist() {
        int index =  allSongsListView.getSelectionModel().getSelectedIndex();

        //Gets name and Song ID of selected song to add
        String songName = allSongsList.get(index).getSongName();
        String songID = allSongsList.get(index).getSongID();

        //Add to current Client side playlist
        newCreatedPlaylist.songNames.add(songName);
        newCreatedPlaylist.getSongID().add(songID);

        //Update display to the changes
        currentSongsListView.getItems().clear();
        for (int i=0; i< newCreatedPlaylist.songNames.size(); i++) {
            currentSongsListView.getItems().add(newCreatedPlaylist.songNames.get(i));
            System.out.println(newCreatedPlaylist.getSongID().get(i));
        }
    }

    public void removeFromPlaylist() {
        int index =  currentSongsListView.getSelectionModel().getSelectedIndex();

        //Removes name and Song ID of selected song to add
        newCreatedPlaylist.songNames.remove(index);
        newCreatedPlaylist.getSongID().remove(index);

        //Update display to the changes
        currentSongsListView.getItems().clear();
        for (int i=0; i< newCreatedPlaylist.songNames.size(); i++) {
            currentSongsListView.getItems().add(newCreatedPlaylist.songNames.get(i));
            System.out.println(newCreatedPlaylist.getSongID().get(i));
        }

    }

    public void createCustomPlaylist() {
        //Song ids used in playlist
        for (int i=0; i< newCreatedPlaylist.songNames.size(); i++) {
            temp += newCreatedPlaylist.getSongID().get(i) + "-";
        }

        //Custom playlist name
        String playlistName = newPlaylistName.getText();
        //Visibility of playlist
        int visibility = 1;
        if(playlistPrivate.isSelected()) {
            visibility = 0;
        }

        int finalVisibility = visibility;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Create new playlist and store on Server Database
                    ClientMain.clientOutputStream.writeObject(new CreatePlaylistRequest(temp, currProfile.getUserID(),
                            playlistName, finalVisibility));
                    ClientMain.clientOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    //------*------*------*------*------*------*Share Playlists Section*------*------*------*------*------*------//
    public TextField playlistCodeTextFld;
    public ListView sharePlaylistsListView;

    private ArrayList<Playlist> sharePlaylists = new ArrayList<Playlist>();
    private synchronized void addToSharePlaylists(Playlist songPlaylist) {
        sharePlaylists.add(songPlaylist);
    }

    public void generateCode() {
        int index = sharePlaylistsListView.getSelectionModel().getSelectedIndex();
        String code = "ampify/share_playlist/" + sharePlaylists.get(index).getPlaylistID();
        playlistCodeTextFld.setText(code);
    }


}
