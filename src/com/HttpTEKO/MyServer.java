package com.HttpTEKO;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Written by Martin Ombura Jr. <@martinomburajr>
 */
public class MyServer {
    public static void main(String[] args) {
        connectToServer();
    }

    public static void connectToServer() {
        //Try connect to the server on an unused port eg 9991. A successful connection will return a socket
        try(ServerSocket serverSocket = new ServerSocket(80)) {
            Socket connectionSocket = serverSocket.accept();

            //Create Input&Outputstreams for the connection
            InputStream inputToServer = connectionSocket.getInputStream();
            OutputStream outputFromServer = connectionSocket.getOutputStream();

            String response = "HTTP/1.1 200 OK";
            String html = "HTML";

            outputFromServer.write(response.getBytes());

            inputToServer.close();
            outputFromServer.close();
            connectionSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
