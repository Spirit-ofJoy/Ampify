package main;


import HTTPserver.StreamingStart;

import java.io.EOFException;
import java.io.IOException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.ServerSocket;
import java.sql.SQLException;

//Main Server class
public class ServerMain {

    private static final int PORT = 5436;   //location of server

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        ServerSocket serverSocket = null;  //Server Socket
        Socket clientSocket ;               //Reference for new incoming client Socket to pass into HandleClient

        try {
            StreamingStart.startHttpServer();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            System.out.println("[SERVER] Started and waiting....");
            serverSocket = new ServerSocket(PORT);
        } catch (BindException | ConnectException e) {
            System.out.println("Port Blocked. Retry with different port.");  //If port is used by another application
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true){                                                       //Runs and accepts clients until manually stopped
            try {
                clientSocket = serverSocket.accept();
                System.out.println("[SERVER] New Client connected.");

                Thread t = new Thread(new HandleClient(clientSocket));     //New client connected and processed in new thread
                t.start();

            } catch (EOFException e){
                System.out.println("[SERVER]Client disconnected.");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
