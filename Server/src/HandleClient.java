
import Requests.*;
import Responses.*;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;

//Class to handle and interact with incoming clients
public class HandleClient implements Runnable {

    private Socket clientSocket;
    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;

    public HandleClient(Socket socket) {            //Constructor to channel and assign proper streams for each client
        this.clientSocket = socket;

        try {
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        while (true) {
            try {
                Request incomingRequest = (Request) objectInputStream.readObject();
                //Request clientRequest = (Request) incomingRequest;

                if( incomingRequest.getReqType().equals("LOGIN_CHECK") ){
                    LoginRequest loginRequest = (LoginRequest) incomingRequest;
                    String clientUserID = DatabaseConnection.checkLogin(loginRequest.getUsername(), loginRequest.getPassword());
                    objectOutputStream.writeObject(new LoginResponse(clientUserID));
                    objectOutputStream.flush();
                }

            } catch (EOFException e) {
                System.out.println("[SERVER] User disconnected or offline.");
                break;
            } catch (SocketException e){
                System.out.println("[SERVER] User connection lost");
                break;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                System.out.println("[SERVER] Error in retrieval of data from database.");
            }
        }

    }
}
