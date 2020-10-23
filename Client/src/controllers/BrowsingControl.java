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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.ClientMain;
import main.SongInfo;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BrowsingControl implements Initializable {

    public Button backButton;

    public TextField popCollection;
    public TextField rnbCollection;
    public TextField indieCollection;
    public TextField rockCollection;
    public TextField epopCollection;
    public TextField hiphopCollection;
    public TextField elecCollection;

    public Button searchArtistBtn;
    public TextField searchArtistfld;
    public TextField artistResult;
    public Button searchAlbumBtn;
    public TextField searchAlbumfld;
    public TextField albumResult;

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
        //Load all songs according to filter
        Thread LoadCollectionThread = new Thread(new LoadFilteredSongList());
        LoadCollectionThread.start();
    }

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

                Platform.runLater(() -> {
                    //Update GUI for Pop songs Collection to load Profile
                    for (int i = 0; i < serverSongCollections.popCollection.size(); i++) {
                        SongInfo temp = (SongInfo) serverSongCollections.popCollection.get(i);
                        popCollection.appendText(temp.getSongID() + " ");
                    }

                    //Update GUI for RnB songs Collection to load Profile
                    for (int i = 0; i < serverSongCollections.rnbCollection.size(); i++) {
                        SongInfo temp = (SongInfo) serverSongCollections.rnbCollection.get(i);
                        rnbCollection.appendText(temp.getSongID() + " ");
                    }

                    //Update GUI for Indie songs Collection to load Profile
                    for (int i = 0; i < serverSongCollections.indieCollection.size(); i++) {
                        SongInfo temp = (SongInfo) serverSongCollections.indieCollection.get(i);
                        indieCollection.appendText(temp.getSongID() + " ");
                    }

                    //Update GUI for Rock songs Collection to load Profile
                    for (int i = 0; i < serverSongCollections.rockCollection.size(); i++) {
                        SongInfo temp = (SongInfo) serverSongCollections.rockCollection.get(i);
                        rockCollection.appendText(temp.getSongID() + " ");
                    }

                    //Update GUI for Electro-Pop songs Collection to load Profile
                    for (int i = 0; i < serverSongCollections.epopCollection.size(); i++) {
                        SongInfo temp = (SongInfo) serverSongCollections.epopCollection.get(i);
                        epopCollection.appendText(temp.getSongID() + " ");
                    }

                    //Update GUI for Hip Hop songs Collection to load Profile
                    for (int i = 0; i < serverSongCollections.hiphopCollection.size(); i++) {
                        SongInfo temp = (SongInfo) serverSongCollections.hiphopCollection.get(i);
                        hiphopCollection.appendText(temp.getSongID() + " ");
                    }

                    //Update GUI for Electronic songs Collection to load Profile
                    for (int i = 0; i < serverSongCollections.elecCollection.size(); i++) {
                        SongInfo temp = (SongInfo) serverSongCollections.elecCollection.get(i);
                        elecCollection.appendText(temp.getSongID() + " ");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }


    //Searching songs by artist cue by sending request to server
    public void searchArtist() {
        String searchword = searchArtistfld.getText();          //Takes the word to be searched
        searchArtistBtn.setText("Loading...");

        //Runnable over new thread to connect to Server through the connection streams and get suitable songs
        Runnable searchProcess = new Runnable() {
            @Override
            public void run() {
                try {
                    ClientMain.clientOutputStream.writeObject(new SongSearchRequest("Artist", searchword));
                    ClientMain.clientOutputStream.flush();                                                //Request sent
                    SongSearchResponse incomingResponse;
                    incomingResponse = (SongSearchResponse) ClientMain.clientInputStream.readObject();   //Response accepted

                        //Update GUI for results
                        Platform.runLater(()-> {
                            //Update GUI for Searched songs to load
                            for (int i = 0; i < incomingResponse.searchedSongs.size(); i++) {
                                SongInfo temp = (SongInfo) incomingResponse.searchedSongs.get(i);
                                artistResult.appendText(temp.getSongID() + " ");
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


    //Searching songs by album cue by sending request to server
    public void searchAlbum() {
        String searchword = searchAlbumfld.getText();          //Takes the word to be searched
        searchAlbumBtn.setText("Loading...");

        //Runnable over new thread to connect to Server through the connection streams and get suitable songs
        Runnable searchProcess = new Runnable() {
            @Override
            public void run() {
                try {
                    ClientMain.clientOutputStream.writeObject(new SongSearchRequest("Album", searchword));
                    ClientMain.clientOutputStream.flush();                                                //Request sent
                    SongSearchResponse incomingResponse;
                    incomingResponse = (SongSearchResponse) ClientMain.clientInputStream.readObject();   //Response accepted

                    //Update GUI for results
                    Platform.runLater(()-> {
                        //Update GUI for Searched songs to load
                        for (int i = 0; i < incomingResponse.searchedSongs.size(); i++) {
                            SongInfo temp = (SongInfo) incomingResponse.searchedSongs.get(i);
                            albumResult.appendText(temp.getSongID() + " ");
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
}
