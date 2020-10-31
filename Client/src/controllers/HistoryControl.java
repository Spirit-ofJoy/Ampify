package controllers;

import Requests.HistoryInfoRequest;
import Responses.HistoryInfoResponse;
import ampify_player.Song_Queue;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utility.ActiveProfile;
import main.ClientMain;
import utility.CommonElements;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class HistoryControl implements Initializable {

    public Button backButton;
    public Button addQueueBtn;
    public Button likeBtn;
    public Button unlikeBtn;
    public ListView historyListView;
    public Label mostFreqSong;

    private ActiveProfile currProfile = ActiveProfile.getProfile();
    private ArrayList<String> currHistory = currProfile.History;
    private ArrayList<String> currHistoryIds = new ArrayList<String>();
    private synchronized void setHistorySongID() {
        String temp;
        for(int i = 0; i< currHistory.size(); i++) {
             temp = currHistory.get(i).substring(7);
             currHistoryIds.add(temp);
        }
    }

    private ArrayList<String> currHistoryNames = new ArrayList<String>();
    private synchronized void addToHistoryNames(String name) {
        currHistoryNames.add(name);
    }

    private synchronized void LikedUpdate() {
        for(int i=0; i< currHistoryIds.size(); i++) {
            if(currProfile.Liked.contains(currHistoryIds.get(i))) {
                String likedSong = currHistoryNames.get(i) + "                [Liked]";
                currHistoryNames.set(i, likedSong);
            }
        }
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


    private static String mostFrequent(ArrayList<String> Names) {
        ArrayList<String> songNames = new ArrayList<String>();

        for (String temp : Names) {
            songNames.add(temp);
        }

        Collections.sort(songNames);

        int max_count = 1;
        String mostFreq = songNames.get(0);
        int curr_count = 1;

        for (int i = 1; i < songNames.size(); i++) {
            if (songNames.get(i).equals(songNames.get(i - 1))) {
                curr_count++;
            } else {
                if (curr_count > max_count) {
                    max_count = curr_count;
                    mostFreq = songNames.get(i - 1);
                }
                curr_count = 1;
            }
        }

        if (curr_count > max_count) {
            max_count = curr_count;
            mostFreq = songNames.get(songNames.size()-1);
        }

        return mostFreq;
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setHistorySongID();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Get names of songs in History from server
                    ClientMain.clientOutputStream.writeObject(new HistoryInfoRequest(currHistoryIds));
                    ClientMain.clientOutputStream.flush();
                    HistoryInfoResponse historyInfoResponse = (HistoryInfoResponse) ClientMain.clientInputStream.readObject();

                    //Store in local arraylist
                    for (int i = 0; i < historyInfoResponse.historySongNames.size(); i++) {
                        String temp = historyInfoResponse.historySongNames.get(i);
                        addToHistoryNames(temp);
                    }
                    //Updates Like on history Display
                    LikedUpdate();

                    //Display on GUI
                    Platform.runLater(() -> {
                        for(int i = currHistory.size()-1; i>= 0; i--) {
                            historyListView.getItems().add(currHistoryNames.get(i));
                        }
                        mostFreqSong.setText(mostFrequent(currHistoryNames));
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    public void addToQueue() {
        //Adding to active Queue
        int index = historyListView.getSelectionModel().getSelectedIndex();
        Song_Queue.addToQueue(currHistoryIds.get(currHistory.size()-(index+1)), currHistoryNames.get(currHistory.size()-(index+1)));
    }

    public void liking() {
        //Adding to liked songs
        int index = historyListView.getSelectionModel().getSelectedIndex();
        CommonElements.like(currHistoryIds.get(currHistory.size()-(index+1)));
    }

    public void unliking() {
        //Adding to disliked songs
        int index = historyListView.getSelectionModel().getSelectedIndex();
        CommonElements.unlike(currHistoryIds.get(currHistory.size()-(index+1)));
    }

}
