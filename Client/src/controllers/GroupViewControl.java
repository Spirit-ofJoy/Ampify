package controllers;


import Requests.LoadGroupChatRequest;
import Requests.SendChatRequest;
import Responses.LoadGroupChatResponse;
import ampify_player.Song_Queue;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.ClientMain;
import utility.ActiveProfile;
import utility.Group;
import utility.Playlist;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GroupViewControl implements Initializable {
    public ListView membersListView;
    public TextArea chatsTextArea;
    public TextField chatMsgTextFld;
    public Button backButton;

    private Group currentGroup;
    private ActiveProfile activeProfile = ActiveProfile.getProfile();
    private String groupChats;
    private synchronized void addChats(String chat) {
        groupChats = chat.replaceAll("%n%", "\n");
    }

    private Thread autoUpdate;

    //Go back to groups
    public void backToGroups() throws IOException {
        System.out.println("[CLIENT] Group Page invoked.");

        autoUpdate.stop();
        Parent groupsRoot = FXMLLoader.load(getClass().getResource("/resources/groups.fxml"));
        Scene groupsScene = new Scene(groupsRoot);
        Stage groupsStage = (Stage) backButton.getScene().getWindow();
        groupsStage.setScene(groupsScene);
        groupsStage.show();
    }

    public synchronized void getSelectedGroup(Group group) {
        currentGroup = new Group(group.getGroupName(), group.getGroupId(), group.getGroupMembers(), group.groupPlaylist);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getSelectedGroup(GroupControl.selectedGroup);

        for (int i=0; i< currentGroup.getGroupMembers().size(); i++) {
            membersListView.getItems().add(currentGroup.getGroupMembers().get(i));
        }

        //Change display related properties
        membersListView.setMouseTransparent(true);
        chatsTextArea.setEditable(false);
        getMessages();

        GroupViewControlAutoUpdate viewUpdate = new GroupViewControlAutoUpdate(GroupViewControl.this);
        autoUpdate = new Thread(viewUpdate);
        autoUpdate.start();

    }

    public void modifyPlaylist() throws IOException {

        //Setting up next scene
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/resources/playlistUpdate.fxml"));
        Parent modifyPlaylistRoot = loader.load();

        Scene modifyPlaylistScene = new Scene(modifyPlaylistRoot);

        //Passing info to new scene
        PlaylistUpdateControl updateController = loader.getController();
        updateController.getSelectedPlaylist(currentGroup.groupPlaylist);

        Stage modifyPlaylistStage = new Stage();
        modifyPlaylistStage.setScene(modifyPlaylistScene);
        modifyPlaylistStage.setTitle("Modify Playlist");
        modifyPlaylistStage.show();

    }

    public void playlistAddToQueue() {
        for(int i=0; i< currentGroup.groupPlaylist.songNames.size(); i++) {
            Song_Queue.addToQueue(currentGroup.groupPlaylist.getSongID().get(i), currentGroup.groupPlaylist.songNames.get(i));
        }
    }


    public void getMessages() {
        try {
            //Create new playlist and store on Server Database
            ClientMain.clientOutputStream.writeObject(new LoadGroupChatRequest(currentGroup.getGroupId()));
            ClientMain.clientOutputStream.flush();

            LoadGroupChatResponse chatResponse = (LoadGroupChatResponse) ClientMain.clientInputStream.readObject();
            addChats(chatResponse.getMessages());

            Platform.runLater(() -> {
                //Update GUI for chats to load
                chatsTextArea.setText(groupChats);
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendChat() throws InterruptedException {
        autoUpdate.sleep(1000);
        String msg = "[" + activeProfile.getUsername() +"] : ";
        msg+= chatMsgTextFld.getText();
        msg+= "%n%";

        String finalMsg = msg;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ClientMain.clientOutputStream.writeObject(new SendChatRequest(finalMsg, currentGroup.getGroupId()));
                    ClientMain.clientOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Platform.runLater(() -> {
                    //Update GUI to clear text field
                    chatMsgTextFld.clear();
                });
            }
        }).start();
    }
}
