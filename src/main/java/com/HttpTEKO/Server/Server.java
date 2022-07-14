package com.HttpTEKO.Server;

import com.HttpTEKO.InitPayment.Payment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

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
                System.out.println();
                System.out.println(connectionSocket);
                System.out.println("Receive:");

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connectionSocket.getInputStream()))) {
                    int postDataI = -1;
                    String line;
                    while ((line = reader.readLine()) != null && (line.length() != 0)) {
                        System.out.println(line);
                        if (line.indexOf("Content-Length:") > -1) {
                            postDataI = Integer.parseInt(line.substring(
                                    line.indexOf("Content-Length:") + 16,
                                    line.length()));
                        }
                    }
                    String jsonString = "";
                    for (int i = 0; i < postDataI; i++) {
                        int intParser = reader.read();
                        jsonString += (char) intParser;
                    }
                    System.out.println(jsonString);
                    String json;
                    OutputStream out = connectionSocket.getOutputStream();
                    Response response = new Response(out);
                    if(jsonString != null) {
                        Gson gson = new Gson();
                        response.setResponseCode(200, "OK");
                        JsonObject map = gson.fromJson(jsonString, JsonObject.class);
                        Payment payment = new Payment(
                                map.getAsJsonObject("payment").get("amount").getAsInt(),
                                map.getAsJsonObject("payment").get("currency").getAsInt(),
                                map.getAsJsonObject("payment").get("exponent").getAsInt()
                        );
                        ResponseData data = new ResponseData("true",
                                "11223344556677",
                                "1537134068907",
                                payment);
                        json = gson.toJson(data);
                    }
                    else{
                        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                        response.setResponseCode(402, "Server error");
                        ResponseData data = new ResponseData("false", 402, "Server error");
                        json = gson.toJson(data);
                    }
                    response.addHeader("Content-Type", "application/json");
                    response.addBody(json);
                    response.send();
                    out.close();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
                connectionSocket.close();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
