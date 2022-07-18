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

import static com.HttpTEKO.isPaymentPossible.merchantRequest.merchantRequest;

public class ServerHandler implements Runnable {
    private final Socket clientSocket;
    private String success;

    public ServerHandler(Socket socket) {
        clientSocket = socket;
        success = "";
    }

    public void run() {
        try {
            System.setProperty("DEBUG.MONGO", "false");
            /** Подключение к mongodb */
            var mongoClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongoClient.getDatabase("testdb");
            MongoCollection<Document> collection = database.getCollection("merchant1");
            Document doc = new Document();
            doc.append("date", new Date());

            System.out.println();
            System.out.println(clientSocket);
            System.out.println("Receive:");

            /** Парсинг заголовка */
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()))) {
                String json = "{\"error\": \"internal\"}";
                String line = reader.readLine();
                System.out.println(line);
                String[] words = line.split(" ");
                json = getData(reader);
                if (!success.equals("false")){
                    if (words[1].equals("/initPayment")) {
                        json = POST(doc, json);
                    }
                    else if (words[1].equals("/isPaymentPossible")) {
                        merchantRequest();
                    }
                    else{
                        json = errorMessage("Error url");
                    }
                }

                /** Создание ответа  */
                OutputStream out = clientSocket.getOutputStream();
                Response response = new Response(out);
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
    public static String errorMessage(String message){
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        ResponseData data = new ResponseData("false", 402, message);
        return gson.toJson(data);
    }

    public String getData(BufferedReader reader){
        int postDataI = -1;
        String line;
        String data = "";

        /** Обработка размера данных.
         *  postDataI */
        try {
            while ((line = reader.readLine()) != null && (line.length() != 0)) {
                System.out.println(line);
                if (line.contains("Content-Length:")) {
                    postDataI = Integer.parseInt(line.substring(
                            line.indexOf("Content-Length:") + 16,
                            line.length()));
                }
            }

            /** Чтение данных */
                for (int i = 0; i < postDataI; i++) {
                    int intParser = reader.read();
                    data += (char) intParser;
                }
        } catch (IOException e){
            success = "false";
            e.printStackTrace();
            return errorMessage("Data receive error");
        }

        /** Pretty json */
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonElement je = JsonParser.parseString(data);
            String prettyJsonString = gson.toJson(je);
            System.out.println(prettyJsonString);
        } catch (JsonSyntaxException jse) {
            success = "false";
            jse.printStackTrace();
            return errorMessage("Json error");
        }
        return data;
    }

    public String POST(Document doc, String jsonString) {
        String json = "";
        success = "true";
        doc.append("receive", jsonString);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            /** Создание json */
            JsonObject map = gson.fromJson(jsonString, JsonObject.class);
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
                return errorMessage("Missing payment");
            }
        } catch (JsonSyntaxException jse) {
            success = "false";
            doc.append("receive", "Invalid json");
            return errorMessage("Invalid json");
        }
        return json;
    }

    public String GET(Document doc, BufferedReader reader){
        return "";
    }

}