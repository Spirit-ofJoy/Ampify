package controllers;

import Requests.BrowseRequest;
import Requests.SongSearchRequest;
import Responses.BrowseResponse;

import Responses.SongSearchResponse;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utility.ActiveProfile;
import main.ClientMain;
import utility.SongInfo;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BrowsingControl implements Initializable {

    public Button backButton;

    public ListView popListView;
    public ListView rnbListView;
    public ListView indieListView;
    public ListView rockListView;
    public ListView epopListView;
    public ListView hiphopListView;
    public ListView elecListView;

    //Get current Active Profile
    private ActiveProfile currProfile = ActiveProfile.getProfile();

    //Keeping all data in a list of songs
    private ArrayList<SongInfo> popList = new ArrayList<SongInfo>();
    private synchronized void addToPopCollection(SongInfo popSong) {
        popList.add(popSong);
    }

    private ArrayList<SongInfo> rnbList = new ArrayList<SongInfo>();
    private synchronized void addToRnBCollection(SongInfo popSong) {
        rnbList.add(popSong);
    }

    private ArrayList<SongInfo> indieList = new ArrayList<SongInfo>();
    private synchronized void addToIndieCollection(SongInfo popSong) {
        indieList.add(popSong);
    }

    private ArrayList<SongInfo> rockList = new ArrayList<SongInfo>();
    private synchronized void addToRockCollection(SongInfo popSong) {
        rockList.add(popSong);
    }

    private ArrayList<SongInfo> epopList = new ArrayList<SongInfo>();
    private synchronized void addToEPopCollection(SongInfo popSong) {
        epopList.add(popSong);
    }

    private ArrayList<SongInfo> hiphopList = new ArrayList<SongInfo>();
    private synchronized void addToHipHopCollection(SongInfo popSong) {
        hiphopList.add(popSong);
    }

    private ArrayList<SongInfo> elecList = new ArrayList<SongInfo>();
    private synchronized void addToElecCollection(SongInfo popSong) {
        elecList.add(popSong);
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

    //Initialising Genre-based page at FXML load
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Load all songs according to filter
        Thread LoadCollectionThread = new Thread(new LoadFilteredSongList());
        LoadCollectionThread.start();

    }

    //------*-------Genre-Based Filtering Section------*-------//
    class LoadFilteredSongList implements Runnable{
        public LoadFilteredSongList() {
            System.out.println("[CLIENT] Loading Selection of songs");
        }

        @Override
        public void run() {
            try {
                ClientMain.clientOutputStream.writeObject(new BrowseRequest());
                ClientMain.clientOutputStream.flush();
                BrowseResponse serverSongCollections;
                serverSongCollections = (BrowseResponse) ClientMain.clientInputStream.readObject();


                //Adding Pop songs to the required list
                for (int i = 0; i < serverSongCollections.popCollection.size(); i++) {
                    SongInfo temp = (SongInfo) serverSongCollections.popCollection.get(i);
                    addToPopCollection(temp);
                }
                //Adding R&B songs to the required list
                for (int i = 0; i < serverSongCollections.rnbCollection.size(); i++) {
                    SongInfo temp = (SongInfo) serverSongCollections.rnbCollection.get(i);
                    addToRnBCollection(temp);
                }
                //Adding Indie songs to the required list
                for (int i = 0; i < serverSongCollections.indieCollection.size(); i++) {
                    SongInfo temp = (SongInfo) serverSongCollections.indieCollection.get(i);
                    addToIndieCollection(temp);
                }
                //Adding Rock songs to the required list
                for (int i = 0; i < serverSongCollections.rockCollection.size(); i++) {
                    SongInfo temp = (SongInfo) serverSongCollections.rockCollection.get(i);
                    addToRockCollection(temp);
                }
                //Adding Electro-Pop songs to the required list
                for (int i = 0; i < serverSongCollections.epopCollection.size(); i++) {
                    SongInfo temp = (SongInfo) serverSongCollections.epopCollection.get(i);
                    addToEPopCollection(temp);
                }
                //Adding Hip Hop songs to the required list
                for (int i = 0; i < serverSongCollections.hiphopCollection.size(); i++) {
                    SongInfo temp = (SongInfo) serverSongCollections.hiphopCollection.get(i);
                    addToHipHopCollection(temp);
                }
                //Adding Electronic songs to the required list
                for (int i = 0; i < serverSongCollections.elecCollection.size(); i++) {
                    SongInfo temp = (SongInfo) serverSongCollections.elecCollection.get(i);
                    addToElecCollection(temp);
                }


                Platform.runLater(() -> {
                    //Update GUI for Pop songs Collection to load Profile
                    for(int i = 0; i< popList.size(); i++) {
                        popListView.getItems().add(popList.get(i).getSongDescription());
                    }
                    //Update GUI for R&B songs Collection to load Profile
                    for(int i = 0; i< rnbList.size(); i++) {
                        rnbListView.getItems().add(rnbList.get(i).getSongDescription());
                    }
                    //Update GUI for Indie songs Collection to load Profile
                    for(int i = 0; i< indieList.size(); i++) {
                        indieListView.getItems().add(indieList.get(i).getSongDescription());
                    }
                    //Update GUI for Rock songs Collection to load Profile
                    for(int i = 0; i< rockList.size(); i++) {
                        rockListView.getItems().add(rockList.get(i).getSongDescription());
                    }
                    //Update GUI for Electro-Pop songs Collection to load Profile
                    for(int i = 0; i< epopList.size(); i++) {
                        epopListView.getItems().add(epopList.get(i).getSongDescription());
                    }
                    //Update GUI for Hip Hop songs Collection to load Profile
                    for(int i = 0; i< hiphopList.size(); i++) {
                        hiphopListView.getItems().add(hiphopList.get(i).getSongDescription());
                    }
                    //Update GUI for Pop songs Collection to load Profile
                    for(int i = 0; i< elecList.size(); i++) {
                        elecListView.getItems().add(elecList.get(i).getSongDescription());
                    }

                });
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    //Assigning functions to buttons we use in Browsing by filters
    //Adding to Queue
    public void popAddToQueue() {
        int index =  popListView.getSelectionModel().getSelectedIndex();
        System.out.println(popList.get(index).getSongID());
    }
    public void rnbAddToQueue() {
        int index =  rnbListView.getSelectionModel().getSelectedIndex();
        System.out.println(rnbList.get(index).getSongID());
    }
    public void indieAddToQueue() {
        int index =  indieListView.getSelectionModel().getSelectedIndex();
        System.out.println(indieList.get(index).getSongID());
    }
    public void rockAddToQueue() {
        int index =  rockListView.getSelectionModel().getSelectedIndex();
        System.out.println(rockList.get(index).getSongID());
    }
    public void epopAddToQueue() {
        int index =  epopListView.getSelectionModel().getSelectedIndex();
        System.out.println(epopList.get(index).getSongID());
    }
    public void hiphopAddToQueue() {
        int index =  hiphopListView.getSelectionModel().getSelectedIndex();
        System.out.println(hiphopList.get(index).getSongID());
    }
    public void elecAddToQueue() {
        int index =  elecListView.getSelectionModel().getSelectedIndex();
        System.out.println(elecList.get(index).getSongID());
    }

    //Adding to Liked songs
    public void popLiking() {
        int index =  popListView.getSelectionModel().getSelectedIndex();
        System.out.println(popList.get(index).getSongID());
    }
    public void rnbLiking() {
        int index =  rnbListView.getSelectionModel().getSelectedIndex();
        System.out.println(rnbList.get(index).getSongID());
    }
    public void indieLiking() {
        int index =  indieListView.getSelectionModel().getSelectedIndex();
        System.out.println(indieList.get(index).getSongID());
    }
    public void rockLiking() {
        int index =  rockListView.getSelectionModel().getSelectedIndex();
        System.out.println(rockList.get(index).getSongID());
    }
    public void epopLiking() {
        int index =  epopListView.getSelectionModel().getSelectedIndex();
        System.out.println(epopList.get(index).getSongID());
    }
    public void hiphopLiking() {
        int index =  hiphopListView.getSelectionModel().getSelectedIndex();
        System.out.println(hiphopList.get(index).getSongID());
    }
    public void elecLiking() {
        int index =  elecListView.getSelectionModel().getSelectedIndex();
        System.out.println(elecList.get(index).getSongID());
    }

    //Adding to Disliked songs
    public void popDisliking() {
        int index =  popListView.getSelectionModel().getSelectedIndex();
        System.out.println(popList.get(index).getSongID());
    }
    public void rnbDisliking() {
        int index =  rnbListView.getSelectionModel().getSelectedIndex();
        System.out.println(rnbList.get(index).getSongID());
    }
    public void indieDisliking() {
        int index =  indieListView.getSelectionModel().getSelectedIndex();
        System.out.println(indieList.get(index).getSongID());
    }
    public void rockDisliking() {
        int index =  rockListView.getSelectionModel().getSelectedIndex();
        System.out.println(rockList.get(index).getSongID());
    }
    public void epopDisliking() {
        int index =  epopListView.getSelectionModel().getSelectedIndex();
        System.out.println(epopList.get(index).getSongID());
    }
    public void hiphopDisliking() {
        int index =  hiphopListView.getSelectionModel().getSelectedIndex();
        System.out.println(hiphopList.get(index).getSongID());
    }
    public void elecDisliking() {
        int index =  elecListView.getSelectionModel().getSelectedIndex();
        System.out.println(elecList.get(index).getSongID());
    }


    //------*------*------*------*------*------*Artist Browsing Section*------*------*------*------*------*------//
    public Button searchArtistBtn;
    public TextField searchArtistfld;
    public ListView artistResultListView;
    public Button artistQueueBtn;
    public Button artistLikeBtn;
    public Button artistDislikeBtn;

    private ArrayList<SongInfo> artistSearchList = new ArrayList<SongInfo>();
    private synchronized void addToArtistSearchList(SongInfo Song) {
        artistSearchList.add(Song);
    }
    private synchronized void clearArtistSearchList() {
        artistSearchList.clear();
    }

    //Searching songs by artist cue by sending request to server
    public void searchArtist() {
        String searchword = searchArtistfld.getText();          //Takes the word to be searched

        //Runnable over new thread to connect to Server through the connection streams and get suitable songs
        Runnable searchProcess = new Runnable() {
            @Override
            public void run() {
                try {
                    ClientMain.clientOutputStream.writeObject(new SongSearchRequest("Artist", searchword));
                    ClientMain.clientOutputStream.flush();                                                //Request sent
                    SongSearchResponse incomingResponse;
                    incomingResponse = (SongSearchResponse) ClientMain.clientInputStream.readObject();   //Response accepted

                    //Clears Previous search results
                    clearArtistSearchList();
                    for (int i = 0; i < incomingResponse.searchedSongs.size(); i++) {
                        SongInfo temp = (SongInfo) incomingResponse.searchedSongs.get(i);
                        addToArtistSearchList(temp);
                    }

                    //Update GUI for results
                    Platform.runLater(()-> {
                        //Clears Previous search results from display
                        artistResultListView.getItems().clear();

                        //Update GUI for Searched songs to load
                        for(int i = 0; i< artistSearchList.size(); i++) {
                            artistResultListView.getItems().add(artistSearchList.get(i).getSongDescription());
                        }
                    } );

                } catch(NullPointerException e) {
                    e.printStackTrace();
                } catch(IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread searchThread = new Thread(searchProcess);  //Request run and handled in new thread
        searchThread.start();

    }

    //Methods used in buttons on Artist browsing
    public void artistAddToQueue() {
        int index =  artistResultListView.getSelectionModel().getSelectedIndex();
        System.out.println(artistSearchList.get(index).getSongID());
    }
    public void artistLiking() {
        int index =  artistResultListView.getSelectionModel().getSelectedIndex();
        System.out.println(artistSearchList.get(index).getSongID());
    }
    public void artistDisliking() {
        int index =  artistResultListView.getSelectionModel().getSelectedIndex();
        System.out.println(artistSearchList.get(index).getSongID());
    }


    //------*------*------*------*------*------*Album Browsing Section*------*------*------*------*------*------//
    public Button searchAlbumBtn;
    public TextField searchAlbumfld;
    public ListView albumResultListView;
    public Button albumQueueBtn;
    public Button albumLikeBtn;
    public Button albumDislikeBtn;

    private ArrayList<SongInfo> albumSearchList = new ArrayList<SongInfo>();
    private synchronized void addToAlbumSearchList(SongInfo Song) {
        albumSearchList.add(Song);
    }
    private synchronized void clearAlbumSearchList() {
        albumSearchList.clear();
    }

    //Searching songs by album cue by sending request to server
    public void searchAlbum() {
        String searchword = searchAlbumfld.getText();          //Takes the word to be searched

        //Runnable over new thread to connect to Server through the connection streams and get suitable songs
        Runnable searchProcess = new Runnable() {
            @Override
            public void run() {
                try {
                    ClientMain.clientOutputStream.writeObject(new SongSearchRequest("Album", searchword));
                    ClientMain.clientOutputStream.flush();                                                //Request sent
                    SongSearchResponse incomingResponse;
                    incomingResponse = (SongSearchResponse) ClientMain.clientInputStream.readObject();   //Response accepted

                    //Clears Previous search results
                    clearAlbumSearchList();
                    for (int i = 0; i < incomingResponse.searchedSongs.size(); i++) {
                        SongInfo temp = (SongInfo) incomingResponse.searchedSongs.get(i);
                        addToAlbumSearchList(temp);
                    }

                    //Update GUI for results
                    Platform.runLater(()-> {
                        //Clears Previous search results
                        albumResultListView.getItems().clear();

                        //Update GUI for Searched songs to load
                        for(int i = 0; i< albumSearchList.size(); i++) {
                            albumResultListView.getItems().add(albumSearchList.get(i).getSongDescription() +"\nAlbum-> " +albumSearchList.get(i).getAlbumName());
                        }
                    } );

                } catch(NullPointerException e) {
                    e.printStackTrace();
                } catch(IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread searchThread = new Thread(searchProcess);  //Request run and handled in new thread
        searchThread.start();

    }

    //Methods used in buttons on Album browsing
    public void albumAddToQueue() {
        int index =  albumResultListView.getSelectionModel().getSelectedIndex();
        System.out.println(albumSearchList.get(index).getSongID());
    }
    public void albumLiking() {
        int index =  albumResultListView.getSelectionModel().getSelectedIndex();
        System.out.println(albumSearchList.get(index).getSongID());
    }
    public void albumDisliking() {
        int index =  albumResultListView.getSelectionModel().getSelectedIndex();
        System.out.println(albumSearchList.get(index).getSongID());
    }

}
