package controllers;

import Requests.HistoryInfoRequest;
import Responses.HistoryInfoResponse;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import utility.ActiveProfile;
import main.ClientMain;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HistoryControl implements Initializable {

    public Button backButton;
    public Button addQueueBtn;
    public Button likeBtn;
    public Button dislikeBtn;
    public ListView historyListView;

    private ArrayList<String> currHistory = ActiveProfile.getProfile().History;
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

                    //Display on GUI
                    Platform.runLater(() -> {
                        for(int i = currHistory.size()-1; i>= 0; i--) {
                            historyListView.getItems().add(currHistoryNames.get(i));
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

    public void addToQueue() {
        //Adding to active Queue
        int index = historyListView.getSelectionModel().getSelectedIndex();
        System.out.println(currHistoryIds.get(currHistory.size()-(index+1)));
    }

    public void liking() {
        //Adding to liked songs
        int index = historyListView.getSelectionModel().getSelectedIndex();
        System.out.println(currHistoryIds.get(currHistory.size()-(index+1)));
    }

    public void disliking() {
        //Adding to disliked songs
        int index = historyListView.getSelectionModel().getSelectedIndex();
        System.out.println(currHistoryIds.get(currHistory.size()-(index+1)));
    }

}
