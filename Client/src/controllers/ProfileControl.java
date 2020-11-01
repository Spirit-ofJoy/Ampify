package controllers;

import Requests.NewReleasesRequest;
import Requests.RecommendsRequest;
import Requests.TopHitsRequest;
import Responses.NewReleasesResponse;
import Responses.RecommendsResponse;
import Responses.TopHitsResponse;

import ampify_player.Song_Queue;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utility.ActiveProfile;
import main.ClientMain;
import utility.CommonElements;
import utility.MoodSelection;
import utility.SongInfo;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class ProfileControl implements Initializable {


    //Get Profile connected
    ActiveProfile currProfile = ActiveProfile.getProfile();

    //FXML elements
    public Label userGreeting;
    public Label moodTitleLabel;
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

    //Mood Collection
    private ArrayList<String> moodCollectionNames = new ArrayList<String>();
    private synchronized void addToMoodCollectionNames(ArrayList<String> moodSongs) {
        for(int i=0; i<moodSongs.size(); i++){
            moodCollectionNames.add(moodSongs.get(i));
        }
    }
    private ArrayList<String> moodCollectionIds = new ArrayList<String>();

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

        //Changes display according to time portion
        String currentTime = new SimpleDateFormat("HH:mm").format(new Date());  //Current Time
        //Midnight
        if(currentTime.compareTo("06:00")<0){
            moodTitleLabel.setText(moodTitleLabel.getText()+" Midnight");
        }
        //Morning
        else if(currentTime.compareTo("12:00")<0){
            moodTitleLabel.setText(moodTitleLabel.getText()+" Dawn");
        }
        //Midday
        else if(currentTime.compareTo("18:00")<0){
            moodTitleLabel.setText(moodTitleLabel.getText()+" Mid-day");
        }
        //Evening
        else if(currentTime.compareTo("24:00")<0){
            moodTitleLabel.setText(moodTitleLabel.getText()+" Dusk");
        }

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

                RecommendsResponse serverRecommendsResponse = null;
                try {
                    //Request for Personalized Recommendations playlist sent and response received
                    ClientMain.clientOutputStream.writeObject(new RecommendsRequest(currProfile.getUserID(), currProfile.Liked));
                    ClientMain.clientOutputStream.flush();
                    serverRecommendsResponse = (RecommendsResponse) ClientMain.clientInputStream.readObject();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

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

                try {
                    for (int i = 0; i < serverRecommendsResponse.recommendations.size(); i++) {
                        temp = (SongInfo) serverRecommendsResponse.recommendations.get(i);
                        addToRecommendsCollection(temp);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < newReleasesResponse.newSongsList.size(); i++) {
                    temp = (SongInfo) newReleasesResponse.newSongsList.get(i);
                    addToNewReleasesCollection(temp);
                }

                try {
                    //Gets Mood based recommends
                    addToMoodCollectionNames(MoodSelection.getMoodRecommends());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
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

                    //Update GUI for Mood based recommends
                    if(moodCollectionNames.size()<5) {
                        for (int i = 0; i < moodCollectionNames.size(); i++) {
                            moodListView.getItems().add(moodCollectionNames.get(i));
                        }
                    }
                    else {
                        for (int i = 0; i < 5; i++) {
                            moodListView.getItems().add(moodCollectionNames.get(i));
                        }
                    }

                    //Update GUI for Newly Released songs Playlist to load on Profile
                    for (int i = 0; i < newReleasesCollection.size(); i++) {
                        newReleasesListView.getItems().add(newReleasesCollection.get(i).getSongDescription() +"\n" +newReleasesCollection.get(i).getUploadTime());
                    }

                    moodCollectionIds = MoodSelection.moodRecommends;
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

    public void loadGroupsPage() throws IOException {
        System.out.println("[CLIENT] Group Page invoked.");

        Parent groupRoot = FXMLLoader.load(getClass().getResource("/resources/groups.fxml"));
        Scene groupScene = new Scene(groupRoot);
        Stage groupStage = (Stage) groupLoader.getScene().getWindow();
        groupStage.setScene(groupScene);
        groupStage.show();

    }

    //UI elements to interact with various list of Songs
    //Adding to Queue
    public void mostViewedAddToQueue() {
        int index =  mostViewedListView.getSelectionModel().getSelectedIndex();
        Song_Queue.addToQueue(topHitsCollection.get(index).getSongID(), topHitsCollection.get(index).getSongName());
    }
    public void recommendsAddToQueue() {
        int index =  recommendsListView.getSelectionModel().getSelectedIndex();
        Song_Queue.addToQueue(recommendsCollection.get(index).getSongID(), recommendsCollection.get(index).getSongName());
    }
    public void newReleasesAddToQueue() {
        int index =  newReleasesListView.getSelectionModel().getSelectedIndex();
        Song_Queue.addToQueue(newReleasesCollection.get(index).getSongID(), newReleasesCollection.get(index).getSongName());
    }
    public void moodAddToQueue() {
        int index =  moodListView.getSelectionModel().getSelectedIndex();
        Song_Queue.addToQueue(moodCollectionIds.get(index), moodCollectionNames.get(index));
    }

    //Liking
    public void mostViewedLiking() {
        int index =  mostViewedListView.getSelectionModel().getSelectedIndex();
        CommonElements.like(topHitsCollection.get(index).getSongID());
    }
    public void recommendsLiking() {
        int index =  recommendsListView.getSelectionModel().getSelectedIndex();
        CommonElements.like(recommendsCollection.get(index).getSongID());
    }
    public void newReleasesLiking() {
        int index =  newReleasesListView.getSelectionModel().getSelectedIndex();
        CommonElements.like(newReleasesCollection.get(index).getSongID());
    }
    public void moodLiking() {
        int index =  moodListView.getSelectionModel().getSelectedIndex();
        CommonElements.like(moodCollectionIds.get(index));
    }

    //Unliking
    public void mostViewedUnliking() {
        int index =  mostViewedListView.getSelectionModel().getSelectedIndex();
        CommonElements.unlike(topHitsCollection.get(index).getSongID());
    }
    public void recommendsUnliking() {
        int index =  recommendsListView.getSelectionModel().getSelectedIndex();
        CommonElements.unlike(recommendsCollection.get(index).getSongID());
    }
    public void newReleasesUnliking() {
        int index =  newReleasesListView.getSelectionModel().getSelectedIndex();
        CommonElements.unlike(newReleasesCollection.get(index).getSongID());
    }
    public void moodUnliking() {
        int index =  moodListView.getSelectionModel().getSelectedIndex();
        CommonElements.unlike(moodCollectionIds.get(index));
    }


    //Opens online player
    public void loadMP3Player() throws IOException {
        System.out.println("[CLIENT] Player invoked.");

        //Setting up Player scene
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/resources/ampifyPlayer.fxml"));
        Parent playerRoot = loader.load();

        Scene playerScene = new Scene(playerRoot, 1000,750);

        Stage playerStage = new Stage();
        playerStage.setScene(playerScene);
        playerScene.getStylesheets().add("/resources/DesignComponents.css");
        playerStage.setTitle("Ampify Player");
        playerStage.show();

    }
    public void openRadio(ActionEvent actionEvent) throws IOException {
        System.out.println("[CLIENT] Radio invoked.");

        //Setting up Player scene
        FXMLLoader loaderRadio = new FXMLLoader();
        loaderRadio.setLocation(getClass().getResource("/resources/radio.fxml"));
        Parent playerRoot = loaderRadio.load();

        Scene playerScene = new Scene(playerRoot, 600,400);

        Stage playerStage = new Stage();
        playerStage.setScene(playerScene);
        playerStage.setTitle("Live Radio");
        playerStage.show();

        playerStage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
            playerStage.close();
        });

    }

}
