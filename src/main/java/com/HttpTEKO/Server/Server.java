package com.HttpTEKO.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/** Основной класс сервера */
public class Server {
    public static void main(String[] args) {
        connectToServer();
    }

    public static void connectToServer() {
        try{
            ServerSocket serverSocket = new ServerSocket(80);
            while(serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket connectionSocket = serverSocket.accept();
                // create a new thread object
                ServerHandler clientSock
                        = new ServerHandler(connectionSocket);

                // This thread will handle the client
                // separately
                new Thread(clientSock).start();
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
