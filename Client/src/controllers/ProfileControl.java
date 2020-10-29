package controllers;

import Requests.NewReleasesRequest;
import Requests.RecommendsRequest;
import Requests.TopHitsRequest;
import Responses.NewReleasesResponse;
import Responses.RecommendsResponse;
import Responses.TopHitsResponse;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import utility.ActiveProfile;
import main.ClientMain;
import utility.SongInfo;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProfileControl implements Initializable {

    //Get Profile connected
    ActiveProfile currProfile = ActiveProfile.getProfile();

    //FXML elements
    public Label userGreeting;
    public ListView mostViewedListView;
    public ListView recommendsListView;
    public ListView moodListView;
    public ListView newReleasesListView;

    public Button mostViewedQueueBtn;
    public Button mostViewedLikeBtn;
    public Button mostViewedUnlikeBtn;

    public Button recommendsQueueBtn;
    public Button recommendsLikeBtn;
    public Button recommendsUnlikeBtn;

    public Button moodQueueBtn;
    public Button moodLikeBtn;
    public Button moodUnlikeBtn;

    public Button newReleasesQueueBtn;
    public Button newReleasesLikeBtn;
    public Button newReleasesUnlikeBtn;

    private ArrayList<SongInfo> topHitsCollection = new ArrayList<SongInfo>();
    private synchronized void addToTopHitsCollection(SongInfo Song) {
        if(currProfile.Liked.contains(Song.getSongID())){
            Song.setLiked(true);
        }
        topHitsCollection.add(Song);
    }
    private ArrayList<SongInfo> recommendsCollection = new ArrayList<SongInfo>();
    private synchronized void addToRecommendsCollection(SongInfo Song) {
        if(currProfile.Liked.contains(Song.getSongID())){
            Song.setLiked(true);
        }
        recommendsCollection.add(Song);
    }
    private ArrayList<SongInfo> newReleasesCollection = new ArrayList<SongInfo>();
    private synchronized void addToNewReleasesCollection(SongInfo Song) {
        if(currProfile.Liked.contains(Song.getSongID())){
            Song.setLiked(true);
        }
        newReleasesCollection.add(Song);
    }
    private ArrayList<SongInfo> moodCollection = new ArrayList<SongInfo>();
    private synchronized void addToMoodCollection(SongInfo Song) {
        if(currProfile.Liked.contains(Song.getSongID())){
            Song.setLiked(true);
        }
        moodCollection.add(Song);
    }

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
                ClientMain.clientOutputStream.writeObject(new RecommendsRequest(currProfile.getUserID(), currProfile.Liked));
                ClientMain.clientOutputStream.flush();
                RecommendsResponse serverRecommendsResponse;
                serverRecommendsResponse = (RecommendsResponse) ClientMain.clientInputStream.readObject();

                //Request for New Releases playlist sent and response received
                ClientMain.clientOutputStream.writeObject(new NewReleasesRequest());
                ClientMain.clientOutputStream.flush();
                NewReleasesResponse newReleasesResponse;
                newReleasesResponse = (NewReleasesResponse) ClientMain.clientInputStream.readObject();

                //Adding the new songs to respective collection
                SongInfo temp;

                for (int i = 0; i < serverTopHitsResponse.topHitsPlaylist.size(); i++) {
                    temp = (SongInfo) serverTopHitsResponse.topHitsPlaylist.get(i);
                    addToTopHitsCollection(temp);
                }

                for (int i = 0; i < serverRecommendsResponse.recommendations.size(); i++) {
                    temp = (SongInfo) serverRecommendsResponse.recommendations.get(i);
                    addToRecommendsCollection(temp);
                }

                for (int i = 0; i < newReleasesResponse.newSongsList.size(); i++) {
                    temp = (SongInfo) newReleasesResponse.newSongsList.get(i);
                    addToNewReleasesCollection(temp);
                }

                Platform.runLater(() -> {

                    //Update GUI for Most Viewed songs Playlist to load on Profile
                    for (int i = 0; i < topHitsCollection.size(); i++) {
                        mostViewedListView.getItems().add(topHitsCollection.get(i).getSongDescription());
                    }

                    //Update GUI for Recommendations songs Playlist to load on Profile
                    if(recommendsCollection.size()<5) {
                        for (int i = 0; i < recommendsCollection.size(); i++) {
                            recommendsListView.getItems().add(recommendsCollection.get(i).getSongDescription());
                        }
                    }
                    else {
                        for (int i = 0; i < 5; i++) {
                            recommendsListView.getItems().add(recommendsCollection.get(i).getSongDescription());
                        }
                    }

                    //Update GUI for Newly Released songs Playlist to load on Profile
                    for (int i = 0; i < newReleasesCollection.size(); i++) {
                        newReleasesListView.getItems().add(newReleasesCollection.get(i).getSongDescription() +"\n" +newReleasesCollection.get(i).getUploadTime());

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

    //UI elements to interact with various list of Songs
    //Adding to Queue
    public void mostViewedAddToQueue() {
        int index =  mostViewedListView.getSelectionModel().getSelectedIndex();
        System.out.println(topHitsCollection.get(index).getSongID());
    }
    public void recommendsAddToQueue() {
        int index =  recommendsListView.getSelectionModel().getSelectedIndex();
        System.out.println(recommendsCollection.get(index).getSongID());
    }
    public void newReleasesAddToQueue() {
        int index =  newReleasesListView.getSelectionModel().getSelectedIndex();
        System.out.println(newReleasesCollection.get(index).getSongID());
    }

    //Liking
    public void mostViewedLiking() {
        int index =  mostViewedListView.getSelectionModel().getSelectedIndex();
        System.out.println(topHitsCollection.get(index).getSongID());
    }
    public void recommendsLiking() {
        int index =  recommendsListView.getSelectionModel().getSelectedIndex();
        System.out.println(recommendsCollection.get(index).getSongID());
    }
    public void newReleasesLiking() {
        int index =  newReleasesListView.getSelectionModel().getSelectedIndex();
        System.out.println(newReleasesCollection.get(index).getSongID());
    }

    //Disliking
    public void mostViewedUnliking() {
        int index =  mostViewedListView.getSelectionModel().getSelectedIndex();
        System.out.println(topHitsCollection.get(index).getSongID());
    }
    public void recommendsUnliking() {
        int index =  recommendsListView.getSelectionModel().getSelectedIndex();
        System.out.println(recommendsCollection.get(index).getSongID());
    }
    public void newReleasesUnliking() {
        int index =  newReleasesListView.getSelectionModel().getSelectedIndex();
        System.out.println(newReleasesCollection.get(index).getSongID());
    }




}
