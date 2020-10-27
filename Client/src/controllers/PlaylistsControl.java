package controllers;


import Requests.PersonalPlaylistsRequest;
import Responses.PersonalPlaylistsResponse;
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


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PlaylistsControl implements Initializable {

    public Button backButton;
    public Button personalAddQueueBtn;
    public ListView personalPlaylistsListView;

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

                    //Display on GUI
                    Platform.runLater(() -> {
                        String display ;
                        for(int i = 0; i< playlistsResponse.personalPlaylists.size(); i++){
                            display = "❒" +personalPlaylists.get(i).getName();

                            for(int j=0; j< personalPlaylists.get(i).songNames.size(); j++) {
                                display +="\n    » " +personalPlaylists.get(i).songNames.get(j);
                            }

                            personalPlaylistsListView.getItems().add(display);
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
            System.out.println(personalPlaylists.get(index).getSongID().get(i));
        }
    }
/*
    public void liking() {
        //Adding to liked songs
        int index = personalPlaylistsListView.getSelectionModel().getSelectedIndex();
        System.out.println(personalPlaylists.get(index).getName());
    }

    public void disliking() {
        //Adding to disliked songs
        int index = personalPlaylistsListView.getSelectionModel().getSelectedIndex();
        System.out.println(personalPlaylists.get(index).getName());
    }


 */

}
