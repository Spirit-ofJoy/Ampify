package controllers;

import Requests.BrowseRequest;
import Requests.SongSearchRequest;
import Responses.BrowseResponse;

import Responses.SongSearchResponse;
import ampify_player.Song_Queue;
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
import utility.CommonElements;
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
    private synchronized void addToPopCollection(SongInfo Song) {
        if(currProfile.Liked.contains(Song.getSongID())){
            Song.setLiked(true);
        }
        popList.add(Song);
    }

    private ArrayList<SongInfo> rnbList = new ArrayList<SongInfo>();
    private synchronized void addToRnBCollection(SongInfo Song) {
        if(currProfile.Liked.contains(Song.getSongID())){
            Song.setLiked(true);
        }
        rnbList.add(Song);
    }

    private ArrayList<SongInfo> indieList = new ArrayList<SongInfo>();
    private synchronized void addToIndieCollection(SongInfo Song) {
        if(currProfile.Liked.contains(Song.getSongID())){
            Song.setLiked(true);
        }
        indieList.add(Song);
    }

    private ArrayList<SongInfo> rockList = new ArrayList<SongInfo>();
    private synchronized void addToRockCollection(SongInfo Song) {
        if(currProfile.Liked.contains(Song.getSongID())){
            Song.setLiked(true);
        }
        rockList.add(Song);
    }

    private ArrayList<SongInfo> epopList = new ArrayList<SongInfo>();
    private synchronized void addToEPopCollection(SongInfo Song) {
        if(currProfile.Liked.contains(Song.getSongID())){
            Song.setLiked(true);
        }
        epopList.add(Song);
    }

    private ArrayList<SongInfo> hiphopList = new ArrayList<SongInfo>();
    private synchronized void addToHipHopCollection(SongInfo Song) {
        if(currProfile.Liked.contains(Song.getSongID())){
            Song.setLiked(true);
        }
        hiphopList.add(Song);
    }

    private ArrayList<SongInfo> elecList = new ArrayList<SongInfo>();
    private synchronized void addToElecCollection(SongInfo Song) {
        if(currProfile.Liked.contains(Song.getSongID())){
            Song.setLiked(true);
        }
        elecList.add(Song);
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
        Song_Queue.addToQueue(popList.get(index).getSongID(), popList.get(index).getSongName());
    }
    public void rnbAddToQueue() {
        int index =  rnbListView.getSelectionModel().getSelectedIndex();
        Song_Queue.addToQueue(rnbList.get(index).getSongID(), rnbList.get(index).getSongName());
    }
    public void indieAddToQueue() {
        int index =  indieListView.getSelectionModel().getSelectedIndex();
        Song_Queue.addToQueue(indieList.get(index).getSongID(), indieList.get(index).getSongName());
    }
    public void rockAddToQueue() {
        int index =  rockListView.getSelectionModel().getSelectedIndex();
        Song_Queue.addToQueue(rockList.get(index).getSongID(), rockList.get(index).getSongName());
    }
    public void epopAddToQueue() {
        int index =  epopListView.getSelectionModel().getSelectedIndex();
        Song_Queue.addToQueue(epopList.get(index).getSongID(), epopList.get(index).getSongName());
    }
    public void hiphopAddToQueue() {
        int index =  hiphopListView.getSelectionModel().getSelectedIndex();
        Song_Queue.addToQueue(hiphopList.get(index).getSongID(), hiphopList.get(index).getSongName());
    }
    public void elecAddToQueue() {
        int index =  elecListView.getSelectionModel().getSelectedIndex();
        Song_Queue.addToQueue(elecList.get(index).getSongID(), elecList.get(index).getSongName());
    }

    //Adding to Liked songs
    public void popLiking() {
        int index =  popListView.getSelectionModel().getSelectedIndex();
        CommonElements.like(popList.get(index).getSongID());
    }
    public void rnbLiking() {
        int index =  rnbListView.getSelectionModel().getSelectedIndex();
        CommonElements.like(rnbList.get(index).getSongID());
    }
    public void indieLiking() {
        int index =  indieListView.getSelectionModel().getSelectedIndex();
        CommonElements.like(indieList.get(index).getSongID());
    }
    public void rockLiking() {
        int index =  rockListView.getSelectionModel().getSelectedIndex();
        CommonElements.like(rockList.get(index).getSongID());
    }
    public void epopLiking() {
        int index =  epopListView.getSelectionModel().getSelectedIndex();
        CommonElements.like(epopList.get(index).getSongID());
    }
    public void hiphopLiking() {
        int index =  hiphopListView.getSelectionModel().getSelectedIndex();
        CommonElements.like(hiphopList.get(index).getSongID());
    }
    public void elecLiking() {
        int index =  elecListView.getSelectionModel().getSelectedIndex();
        CommonElements.like(elecList.get(index).getSongID());
    }

    //Adding to Disliked songs
    public void popUnliking() {
        int index =  popListView.getSelectionModel().getSelectedIndex();
        CommonElements.unlike(popList.get(index).getSongID());
    }
    public void rnbUnliking() {
        int index =  rnbListView.getSelectionModel().getSelectedIndex();
        CommonElements.unlike(rnbList.get(index).getSongID());
    }
    public void indieUnliking() {
        int index =  indieListView.getSelectionModel().getSelectedIndex();
        CommonElements.unlike(indieList.get(index).getSongID());
    }
    public void rockUnliking() {
        int index =  rockListView.getSelectionModel().getSelectedIndex();
        CommonElements.unlike(rockList.get(index).getSongID());
    }
    public void epopUnliking() {
        int index =  epopListView.getSelectionModel().getSelectedIndex();
        CommonElements.unlike(epopList.get(index).getSongID());
    }
    public void hiphopUnliking() {
        int index =  hiphopListView.getSelectionModel().getSelectedIndex();
        CommonElements.unlike(hiphopList.get(index).getSongID());
    }
    public void elecUnliking() {
        int index =  elecListView.getSelectionModel().getSelectedIndex();
        CommonElements.unlike(elecList.get(index).getSongID());
    }


    //------*------*------*------*------*------*Artist Browsing Section*------*------*------*------*------*------//
    public Button searchArtistBtn;
    public TextField searchArtistfld;
    public ListView artistResultListView;
    public Button artistQueueBtn;
    public Button artistLikeBtn;
    public Button artistUnlikeBtn;

    private ArrayList<SongInfo> artistSearchList = new ArrayList<SongInfo>();
    private synchronized void addToArtistSearchList(SongInfo Song) {
        if(currProfile.Liked.contains(Song.getSongID())){
            Song.setLiked(true);
        }
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
        Song_Queue.addToQueue(artistSearchList.get(index).getSongID(), artistSearchList.get(index).getSongName());
    }
    public void artistLiking() {
        int index =  artistResultListView.getSelectionModel().getSelectedIndex();
        CommonElements.like(artistSearchList.get(index).getSongID());
    }
    public void artistUnliking() {
        int index =  artistResultListView.getSelectionModel().getSelectedIndex();
        CommonElements.unlike(artistSearchList.get(index).getSongID());
    }


    //------*------*------*------*------*------*Album Browsing Section*------*------*------*------*------*------//
    public Button searchAlbumBtn;
    public TextField searchAlbumfld;
    public ListView albumResultListView;
    public Button albumQueueBtn;
    public Button albumLikeBtn;
    public Button albumUnlikeBtn;

    private ArrayList<SongInfo> albumSearchList = new ArrayList<SongInfo>();
    private synchronized void addToAlbumSearchList(SongInfo Song) {
        if(currProfile.Liked.contains(Song.getSongID())){
            Song.setLiked(true);
        }
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
        Song_Queue.addToQueue(albumSearchList.get(index).getSongID(), albumSearchList.get(index).getSongName());
    }
    public void albumLiking() {
        int index =  albumResultListView.getSelectionModel().getSelectedIndex();
        CommonElements.like(albumSearchList.get(index).getSongID());
    }
    public void albumUnliking() {
        int index =  albumResultListView.getSelectionModel().getSelectedIndex();
        CommonElements.unlike(albumSearchList.get(index).getSongID());
    }

}
