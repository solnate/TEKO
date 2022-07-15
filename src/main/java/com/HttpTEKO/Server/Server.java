package com.HttpTEKO.Server;

import com.HttpTEKO.InitPayment.Payment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.Date;

public class Server {
    static Document doc;
    static MongoCollection<Document> collection;
    public static void main(String[] args) {
        var mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("testdb");
        collection = database.getCollection("merchant");
        connectToServer();
    }

    public static void connectToServer() {
        try{
            ServerSocket serverSocket = new ServerSocket(80);
            while(serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket connectionSocket = serverSocket.accept();
                doc = new Document();
                doc.append("date", new Date());
                System.out.println();
                System.out.println(connectionSocket);
                System.out.println("Receive:");

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connectionSocket.getInputStream()))) {
                    int postDataI = -1;
                    String line;
                    while ((line = reader.readLine()) != null && (line.length() != 0)) {
                        System.out.println(line);
                        if (line.contains("Content-Length:")) {
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
                    String json = "";
                    String success = "false";
                    OutputStream out = connectionSocket.getOutputStream();
                    Response response = new Response(out);
                    if(jsonString != "") {
                        success = "true";
                        doc.append("receive", jsonString);
                        try {
                            Gson gson = new Gson();
                            JsonObject map = gson.fromJson(jsonString, JsonObject.class);
                            response.setResponseCode(200, "OK");
                            if (map.has("payment")) {
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
                            } else {
                                success = "false";
                                doc.append("receive", "Missing payment");
                                json = errorMessage(response, "Missing payment");
                            }
                        } catch (JsonSyntaxException jse){
                            success = "false";
                            doc.append("receive", "Invalid json");
                            json = errorMessage(response, "Invalid json");
                        }
                    }
                    else{
                        doc.append("receive", "Missing json");
                        json = errorMessage(response, "Missing json");
                    }
                    response.addHeader("Content-Type", "application/json");
                    response.addBody(json);
                    response.send();
                    doc.append("success", success);
                    doc.append("sent", json);
                    collection.insertOne(doc);
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

    public static String errorMessage(Response response, String message){
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        response.setResponseCode(402, "Server error");
        ResponseData data = new ResponseData("false", 402, message);
        return gson.toJson(data);
    }
}
