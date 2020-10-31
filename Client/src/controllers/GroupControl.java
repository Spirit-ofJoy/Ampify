package controllers;

import Requests.*;
import Responses.LoadGroupResponse;
import Responses.PersonalGroupsResponse;
import Responses.UserListResponse;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.ClientMain;
import utility.ActiveProfile;
import utility.Group;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GroupControl implements Initializable {
    public Button backButton;

    //Go back to profile
    public void backToProfile() throws IOException {
        System.out.println("[CLIENT] Profile Page invoked.");

        Parent profileRoot = FXMLLoader.load(getClass().getResource("/resources/profile.fxml"));
        Scene profileScene = new Scene(profileRoot);
        Stage profileStage = (Stage) backButton.getScene().getWindow();
        profileStage.setScene(profileScene);
        profileStage.show();
    }

    //------*------*------*------*------*------*Initialisation Section*------*------*------*------*------*------//
    public ListView groupsListView;
    public Button joinGroupBtn;
    public TextField newGrpNameFld;

    public ListView allUsersListView;
    public ListView groupUserListView;

    private ActiveProfile currProfile = ActiveProfile.getProfile();

    public static Group selectedGroup;
    private static synchronized void assignGroupSelection(Group group) {
        selectedGroup = group;
    }

    //My groups Section
    private ArrayList<String> myGroupNames = new ArrayList<String>();
    private synchronized void addToMyGroupNames(String gname) {
        myGroupNames.add(gname);
    }
    private ArrayList<String> myGroupIds = new ArrayList<String>();
    private synchronized void addToMyGroupIds(String gid) {
        myGroupIds.add(gid);
    }


    //Create Section
    private ArrayList<String> allUsernames = new ArrayList<String>();
    private synchronized void addToAllUsernames(String uname) {
        if (!(uname.equals(currProfile.getUsername())))
            allUsernames.add(uname);
    }
    private ArrayList<String> allUserIds = new ArrayList<String>();
    private synchronized void addToAllUserIds(String uid) {
        if (!(uid.equals(currProfile.getUserID())))
            allUserIds.add(uid);
    }

    private ArrayList<String> currUsernames = new ArrayList<String>();
    private ArrayList<String> currUserIds = new ArrayList<String>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //My groups section
                    //Get list of groups and store in local variable
                    ClientMain.clientOutputStream.writeObject(new PersonalGroupsRequest(currProfile.getUserID()));
                    ClientMain.clientOutputStream.flush();                                                                                    //Request sent
                    PersonalGroupsResponse incomingPersonalGrpResponse = (PersonalGroupsResponse) ClientMain.clientInputStream.readObject();  //Response accepted

                    for (int i = 0; i< incomingPersonalGrpResponse.grpNamesList.size(); i++ ){
                        addToMyGroupNames(incomingPersonalGrpResponse.grpNamesList.get(i));
                    }
                    for (int i = 0; i< incomingPersonalGrpResponse.grpIdList.size(); i++ ){
                        addToMyGroupIds(incomingPersonalGrpResponse.grpIdList.get(i));
                    }

                    //Create Groups section
                    //Get list of users and store in local variable
                    ClientMain.clientOutputStream.writeObject(new UserListRequest());
                    ClientMain.clientOutputStream.flush();                                                                      //Request sent
                    UserListResponse incomingUserListResponse = (UserListResponse) ClientMain.clientInputStream.readObject();   //Response accepted

                    for (int i = 0; i< incomingUserListResponse.usernamesList.size(); i++ ){
                        addToAllUsernames(incomingUserListResponse.usernamesList.get(i));
                    }
                    for (int i = 0; i< incomingUserListResponse.useridList.size(); i++ ){
                        addToAllUserIds(incomingUserListResponse.useridList.get(i));
                    }


                    Platform.runLater(() -> {
                        //Update GUI for all Groups to load
                        for (int i = 0; i< myGroupNames.size(); i++) {
                            groupsListView.getItems().add(myGroupNames.get(i));
                        }

                        //Update GUI for all Users to load
                        for (int i = 0; i < allUserIds.size(); i++) {
                            allUsersListView.getItems().add(allUsernames.get(i));
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

    //------*------*------*------*------*------*Open Groups Section*------*------*------*------*------*------//

    public void openGroup() {
        int index = groupsListView.getSelectionModel().getSelectedIndex();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ClientMain.clientOutputStream.writeObject(new LoadGroupRequest(myGroupIds.get(index)));
                    ClientMain.clientOutputStream.flush();                                                                      //Request sent
                    LoadGroupResponse incomingLoadGrpResponse = (LoadGroupResponse) ClientMain.clientInputStream.readObject();  //Response accepted

                    assignGroupSelection(incomingLoadGrpResponse.getGroupFound());

                    Platform.runLater(()-> {

                        Parent groupViewRoot = null;
                        try {
                            groupViewRoot = FXMLLoader.load(getClass().getResource("/resources/groupView.fxml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Scene groupViewScene = new Scene(groupViewRoot);
                        Stage groupViewStage = (Stage) joinGroupBtn.getScene().getWindow();
                        groupViewStage.setScene(groupViewScene);
                        groupViewStage.show();

                    });
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    //------*------*------*------*------*------*Create Groups Section*------*------*------*------*------*------//

    public void addToGroup() {
        int index =  allUsersListView.getSelectionModel().getSelectedIndex();

        //Gets name and User ID of selected user to add
        String uname = allUsernames.get(index);
        String uid = allUserIds.get(index);

        //Add to current Client side temporary group
        currUsernames.add(uname);
        currUserIds.add(uid);

        //Update display to the changes
        groupUserListView.getItems().clear();
        for (int i=0; i< currUsernames.size(); i++) {
            groupUserListView.getItems().add(currUsernames.get(i));
        }
    }

    public void removeFromGroup() {
        int index =  groupUserListView.getSelectionModel().getSelectedIndex();

        //Removes name and User ID of selected user
        currUsernames.remove(index);
        currUserIds.remove(index);

        //Update display to the changes
        groupUserListView.getItems().clear();
        for (int i=0; i< currUserIds.size(); i++) {
            groupUserListView.getItems().add(currUsernames.get(i));
        }
    }

    public void createGroup() {
        String temp = currProfile.getUserID()+"-";
        //User ids used in group
        for (int i=0; i< currUsernames.size(); i++) {
            temp += currUserIds.get(i) + "-";
        }

        //Custom playlist name
        String playlistName = newGrpNameFld.getText();

        String finalUsers = temp;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Create new group and store on Server Database
                    ClientMain.clientOutputStream.writeObject(new CreateGroupRequest(finalUsers, playlistName));
                    ClientMain.clientOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();



    }

}
