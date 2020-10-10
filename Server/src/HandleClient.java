
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
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
                Request clientRequest = (Request) objectInputStream.readObject();

                if( clientRequest.getReqType().equals("LOGIN_CHECK") ){
                    String clientUserID = DatabaseConnection.checkLogin(clientRequest.getVar1(), clientRequest.getVar2());
                    objectOutputStream.writeObject(new Response("LOGIN_CHECK", clientUserID));
                    objectOutputStream.flush();
                }

            } catch (EOFException e) {
                System.out.println("User disconnected or offline.");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }
}
