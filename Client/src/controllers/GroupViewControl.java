package controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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

    //Go back to groups
    public void backToGroups() throws IOException {
        System.out.println("[CLIENT] Group Page invoked.");

        Parent groupsRoot = FXMLLoader.load(getClass().getResource("/resources/groups.fxml"));
        Scene groupsScene = new Scene(groupsRoot);
        Stage groupsStage = (Stage) backButton.getScene().getWindow();
        groupsStage.setScene(groupsScene);
        groupsStage.show();
    }

    public void getSelectedGroup(Group group) {
        currentGroup = group;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
