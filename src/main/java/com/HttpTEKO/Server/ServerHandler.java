package com.HttpTEKO.Server;

import com.HttpTEKO.InitPayment.Payment;
import com.google.gson.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

public class ServerHandler implements Runnable {
    private final Socket clientSocket;

    public ServerHandler(Socket socket) {
        clientSocket = socket;
    }

    public void run() {
        try {
            /** Подключение к mongodb */
            var mongoClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongoClient.getDatabase("testdb");
            MongoCollection<Document> collection = database.getCollection("merchant1");
            Document doc = new Document();
            doc.append("date", new Date());

            System.out.println();
            System.out.println(clientSocket);
            System.out.println("Receive:");

            /** Обработка размера данных.
             *  postDataI */
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()))) {
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

                /** Чтение данных */
                String jsonString = "";
                for (int i = 0; i < postDataI; i++) {
                    int intParser = reader.read();
                    jsonString += (char) intParser;
                }

                /** Pretty json */
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonElement je = JsonParser.parseString(jsonString);
                String prettyJsonString = gson.toJson(je);
                System.out.println(prettyJsonString);

                String json = "";
                String success = "false";
                OutputStream out = clientSocket.getOutputStream();
                Response response = new Response(out);
                if (jsonString != "") {
                    success = "true";
                    doc.append("receive", jsonString);
                    try {
                        /** Создание json */
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
                    } catch (JsonSyntaxException jse) {
                        success = "false";
                        doc.append("receive", "Invalid json");
                        json = errorMessage(response, "Invalid json");
                    }
                } else {
                    doc.append("receive", "Missing json");
                    json = errorMessage(response, "Missing json");
                }

                /** Создание ответа  */
                response.addHeader("Content-Type", "application/json");
                response.addBody(json);
                response.send();

                /** Запись в mongodb */
                doc.append("success", success);
                doc.append("sent", json);
                collection.insertOne(doc);
                out.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
            clientSocket.close();
        }catch (IOException e){
            System.err.println(e);
        }
    }
    /** Функция формирования ответа об ошибке */
    public static String errorMessage(Response response, String message){
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        response.setResponseCode(402, "Server error");
        ResponseData data = new ResponseData("false", 402, message);
        return gson.toJson(data);
    }
}