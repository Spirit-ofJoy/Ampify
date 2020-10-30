package controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.text.html.ListView;
import java.io.IOException;
import java.net.URL;
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

    //------*------*------*------*------*------*Active Groups Section*------*------*------*------*------*------//
    public ListView groupsListView;
    public Button joinGroupBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    //------*------*------*------*------*------*Create Groups Section*------*------*------*------*------*------//
    public TextField newGrpNameFld;
    public ListView allUsersListView;
    public ListView groupUserListView;


}
