package controllers;

import Requests.RecommendsRequest;
import Requests.TopHitsRequest;
import Responses.RecommendsResponse;
import Responses.TopHitsResponse;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utility.ActiveProfile;
import main.ClientMain;
import utility.SongInfo;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileControl implements Initializable {

    ActiveProfile currProfile = ActiveProfile.getProfile();
    public Label userGreeting;
    public TextField mostViewed;
    public TextField recommends;
    public TextField mood;
    public Button browseLoader;
    public Button historyLoader;
    public Button playlistLoader;
    public Button groupLoader;

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
                        SongInfo temp = (SongInfo) serverTopHitsResponse.topHitsPlaylist.get(i);
                        mostViewed.appendText(temp.getSongID() + " ");
                    }

                    //Update GUI for Recommendations songs Playlist to load Profile
                    for (int i = 0; i < serverRecommendsResponse.recommendations.size(); i++) {
                        SongInfo temp = (SongInfo) serverRecommendsResponse.recommendations.get(i);
                        recommends.appendText(temp.getSongID() + " ");
                    }

                });

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public void loadBrowsePage() throws IOException {
        System.out.println("[CLIENT] Song Browsing Page invoked.");

        Parent browseRoot = FXMLLoader.load(getClass().getResource("/resources/browsing.fxml"));
        Scene browseScene = new Scene(browseRoot);
        Stage browseStage = (Stage) browseLoader.getScene().getWindow();
        browseStage.setScene(browseScene);
        browseStage.show();
    }

    public void loadHistoryPage() throws IOException {
        System.out.println("[CLIENT] History Page invoked.");

        Parent historyRoot = FXMLLoader.load(getClass().getResource("/resources/history.fxml"));
        Scene historyScene = new Scene(historyRoot);
        Stage historyStage = (Stage) historyLoader.getScene().getWindow();
        historyStage.setScene(historyScene);
        historyStage.show();

    }

    public void loadPlaylistPage() throws IOException {
        System.out.println("[CLIENT] Playlist Page invoked.");

        Parent playlistsRoot = FXMLLoader.load(getClass().getResource("/resources/playlists.fxml"));
        Scene playlistsScene = new Scene(playlistsRoot);
        Stage playlistsStage = (Stage) playlistLoader.getScene().getWindow();
        playlistsStage.setScene(playlistsScene);
        playlistsStage.show();
    }




}
