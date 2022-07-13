package com.HttpTEKO.Server;

import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    public static void main(String[] args) {
        connectToServer();
    }

    public static void connectToServer() {
        try{
            ServerSocket serverSocket = new ServerSocket(80);
            while(serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket connectionSocket = serverSocket.accept();
                //InputStream inputToServer = connectionSocket.getInputStream();
                OutputStream outputFromServer = connectionSocket.getOutputStream();

                System.out.println(connectionSocket);

                Gson gson = new Gson();
                Data data = new Data();
                String json = gson.toJson(data);
                String response_json = "HTTP/1.1 200 OK" + "\n\r" +
                        "Content-Length: " + json.getBytes().length + "\n\r" +
                        "\n\r" +
                        json;

                Response response = new Response(outputFromServer);
                response.setResponseCode(200, "OK");
                response.addHeader("Content-Type", "text/html");
                response.addBody(json);
                response.send();

                try {
                    Reader streamReader = null;
                    streamReader = new InputStreamReader(connectionSocket.getInputStream());
                    BufferedReader in = new BufferedReader(streamReader);
                    String inputLine;
                    StringBuffer content = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine + "\n\r");
                    }
                    System.out.println(content);
                    in.close();
                } catch (NullPointerException e) {
                    System.out.println(e);
                }

                //inputToServer.close();
                outputFromServer.close();
                connectionSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
