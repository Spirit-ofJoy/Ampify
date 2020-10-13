
import Requests.LoginRequest;
import Responses.LoginResponse;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;


public class LoginControl {

    public TextField usernameTextfld;
    public PasswordField passwordFld;
    public Button loginProcessButton;
    public TextField msgDisplay;


    public void processLogin() {
        String uname = usernameTextfld.getText();          //Takes username
        String pass = passwordFld.getText();               //Takes password
        loginProcessButton.setText("Loading...");

        //Anonymous Runnable over new thread to connect to Server through the connection streams
        Runnable loginProcess = new Runnable() {
            @Override
            public void run() {
                try {
                    ClientMain.clientOutputStream.writeObject(new LoginRequest(uname, pass));
                    ClientMain.clientOutputStream.flush();                                          //Request sent

                    LoginResponse incomingResponse;
                    incomingResponse = (LoginResponse) ClientMain.clientInputStream.readObject();   //Response accepted

                    if(incomingResponse.getUserID().equals("USER_NOT_FOUND")){
                        msgDisplay.setText("User not Found. Please check credentials or Sign-Up if don't have an account.");
                    }
                    else {
                        msgDisplay.setText("User found. Loading Profile now.");
                        System.out.println(incomingResponse.getHistory()+incomingResponse.getLiked()+incomingResponse.getDisliked()+incomingResponse.getPlaylists());
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        };

        Thread loginThread = new Thread(loginProcess);  //Request run and handled in new thread
        loginThread.start();
    }

}
