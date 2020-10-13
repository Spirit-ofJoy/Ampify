
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

                    System.out.println("[CLIENT] "+incomingResponse.getUserID());
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        };

        Thread loginThread = new Thread(loginProcess);
        loginThread.start();
    }

}
