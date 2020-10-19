package controllers;

import Requests.RecommendsRequest;
import Requests.TopHitsRequest;
import Responses.RecommendsResponse;
import Responses.TopHitsResponse;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.ActiveProfile;
import main.ClientMain;
import main.SongInfo;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileControl implements Initializable {

    ActiveProfile currProfile = ActiveProfile.getProfile();
    public Label userGreeting;
    public TextField mostViewed;
    public TextField recommends;
    public TextField mood;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userGreeting.setText("Welcome, " + currProfile.getUsername());

        //Loads Profile details in another thread
        Thread LoadProfileThread = new Thread(new LoadProfileProcess());
        LoadProfileThread.start();


    }

    class LoadProfileProcess implements Runnable {

        public LoadProfileProcess() {
            System.out.println("[CLIENT] Loading Profile....");
        }

        @Override
        public void run() {
            try {
                //Request for Most Viewed playlist sent and response received
                ClientMain.clientOutputStream.writeObject(new TopHitsRequest());
                ClientMain.clientOutputStream.flush();
                TopHitsResponse serverTopHitsResponse;
                serverTopHitsResponse = (TopHitsResponse) ClientMain.clientInputStream.readObject();

                //Request for Personalized Recommendations playlist sent and response received
                ClientMain.clientOutputStream.writeObject(new RecommendsRequest(currProfile.getUserID()));
                ClientMain.clientOutputStream.flush();
                RecommendsResponse serverRecommendsResponse;
                serverRecommendsResponse = (RecommendsResponse) ClientMain.clientInputStream.readObject();


                Platform.runLater(() -> {

                    //Update GUI for Most Viewed songs Playlist to load Profile
                    for (int i = 0; i < serverTopHitsResponse.topHitsPlaylist.size(); i++) {
                        SongInfo t = (SongInfo) serverTopHitsResponse.topHitsPlaylist.get(i);
                        mostViewed.appendText(t.getSongID() + " ");
                    }

                    //Update GUI for Recommendations songs Playlist to load Profile
                    for (int i = 0; i < serverRecommendsResponse.recommendations.size(); i++) {
                        SongInfo t2 = (SongInfo) serverRecommendsResponse.recommendations.get(i);
                        recommends.appendText(t2.getSongID() + " ");
                    }

                });

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }





}
