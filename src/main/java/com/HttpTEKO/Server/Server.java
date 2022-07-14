package com.HttpTEKO.Server;

import com.google.gson.Gson;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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
                System.out.println(connectionSocket);

                Reader streamReader = null;
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connectionSocket.getInputStream()));
                String inputLine;
                String jsonString = null;
                StringBuffer content = new StringBuffer();
                while(!in.ready());
                while (in.ready()) {
                    inputLine = in.readLine();
                    content.append(inputLine + "\n\r");
                    System.out.println(inputLine);
                    if (inputLine.contains("{")) {
                        jsonString = inputLine;
                    }
                }

                String json;
                if(jsonString != null) {
                    Gson gson = new Gson();
                    Map map = gson.fromJson(jsonString, Map.class);
                    ResponseData data = new ResponseData("true",
                            ((Map) map.get("tx")).get("id").toString(),
                            ((Map) map.get("tx")).get("start_t").toString());
                    json = gson.toJson(data);
                }
                else{
                    json = "get";
                }
                Response response = new Response(connectionSocket.getOutputStream());
                response.setResponseCode(200, "OK");
                response.addHeader("Content-Type", "application/json");
                response.addBody(json);
                response.send();

                in.close();
                connectionSocket.close();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
