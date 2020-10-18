package controllers;

import Requests.RecommendsRequest;
import Requests.TopHitsRequest;
import Responses.RecommendsResponse;
import Responses.TopHitsResponse;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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

        try {
            Thread mostViewedThread = new Thread(new MostViewedProcess());
            mostViewedThread.start();
            mostViewedThread.join();

            Thread personalRecThread = new Thread(new PersonalizedRecommends());
            personalRecThread.start();
            personalRecThread.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    class MostViewedProcess implements Runnable {

        public MostViewedProcess() {
            System.out.println("[CLIENT] Top Hits requested");
        }

        @Override
        public void run() {
            try {
                ClientMain.clientOutputStream.writeObject(new TopHitsRequest());
                ClientMain.clientOutputStream.flush();                                          //Request sent

                TopHitsResponse incomingResponse;
                incomingResponse = (TopHitsResponse) ClientMain.clientInputStream.readObject();   //Response accepted

                //Update GUI for Most Viewed songs Playlist to load Profile
                Platform.runLater(() -> {
                    for (int i = 0; i < incomingResponse.topHitsPlaylist.size(); i++) {
                        SongInfo t = (SongInfo) incomingResponse.topHitsPlaylist.get(i);
                        mostViewed.appendText(t.getSongID() + " ");
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    class PersonalizedRecommends implements Runnable{

        public PersonalizedRecommends() {
            System.out.println("[CLIENT] Recommendations requested");
        }

        @Override
        public void run() {

        try {
            ClientMain.clientOutputStream.writeObject(new RecommendsRequest(currProfile.getUserID(), currProfile.History, currProfile.Liked));
            ClientMain.clientOutputStream.flush();

            RecommendsResponse incomingResponse;
            incomingResponse = (RecommendsResponse) ClientMain.clientInputStream.readObject();   //Response accepted

            //Update GUI for Recommendations songs Playlist to load Profile
            Platform.runLater(()-> {
                for (int i=0; i < incomingResponse.recommendations.size(); i++){
                    SongInfo t = (SongInfo) incomingResponse.recommendations.get(i);
                    recommends.appendText(t.getSongID() + " ");
                } } );

        } catch(NullPointerException e) {
            System.out.println("Server currently offline. Try again later.");
        } catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        }
    }





}
