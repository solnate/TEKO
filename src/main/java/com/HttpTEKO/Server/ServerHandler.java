package com.HttpTEKO.Server;

import com.HttpTEKO.payload.Payment;
import com.HttpTEKO.payload.Tx;
import com.google.gson.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import static com.mongodb.client.model.Filters.*;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;


public class ServerHandler implements Runnable {
    private final Socket clientSocket;
    private String success;

    public ServerHandler(Socket socket) {
        clientSocket = socket;
        success = "false";
    }

    public void run() {
        try {
            System.setProperty("DEBUG.MONGO", "false");
            /* Подключение к mongodb */
            var mongoClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongoClient.getDatabase("testdb");
            MongoCollection<Document> transactions = database.getCollection("transactions");
            MongoCollection<Document> items = database.getCollection("items");

            System.out.println();
            System.out.println(clientSocket);
            System.out.println("Receive:");

            /* Парсинг заголовка */
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()))) {
                String data;
                String line = reader.readLine();
                System.out.println(line);
                String[] words = line.split(" ");
                data = getData(reader);

                // Обработка GET-запросов
                if (words[0].equals("GET")) {
                    switch (words[1]) {
                        case "/initPayment" -> data = GetHandlers.initPayment("");
                        case "/initRedirectPayment" -> data = GetHandlers.initPayment("http://89.169.52.44:80");
                        case "/getPaymentsByTag" -> data = GetHandlers.getPaymentsByTag();
                        case "/getPaymentById" -> data = GetHandlers.getPaymentByIdOrStatus("id");
                        case "/getPaymentStatus" -> data = GetHandlers.getPaymentByIdOrStatus("status");
                        //TO DO
                        case "/rollbackPayment" -> data = GetHandlers.getPaymentsByTag();
                        default -> {
                            success = "false";
                            data = errorMessage("Error url");
                        }
                    }
                }

                // Обработка POST-запросов
                else if (words[0].equals("POST")) {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    JsonObject map;
                    try {
                        map = gson.fromJson(data, JsonObject.class);
                        Payment payment = gson.fromJson(map.getAsJsonObject("payment"), Payment.class);
                        switch (words[1]) {

                            // Проверка возможности оплаты
                            case "/isPaymentPossible" -> {
                                // Поиск в базе информации о наличии ресурсов
                                Document val = items.find(eq("_id", payment.currency)).first();
                                int smth = val.getInteger("value");
                                if (smth > payment.amount) {
                                    ResponseData tempdata = PostHandlers.isPaymentPossible(map);
                                    data = gson.toJson(tempdata);
                                    Document doc = new Document();
                                    doc.append("_id", tempdata.result.tx.id);
                                    transactions.insertOne(doc);
                                } else {
                                    data = errorMessage("Insufficient money");
                                }
                            }

                            // Resume payment
                            case "/resumePayment" -> {
                                success = "true";
                                Document pay = items.find(eq("_id", payment.currency)).first();
                                int paymentCurrency = pay.getInteger("value");

                                Tx tx = gson.fromJson(map.getAsJsonObject("partner_tx"), Tx.class);
                                Document doc = transactions.find(eq("_id", tx.id)).first();

                                if (!doc.isEmpty()) {
                                    success = "true";
                                    items.updateOne(Filters.eq("_id", payment.currency),
                                            Updates.set("value", paymentCurrency - payment.amount));

                                    Payment src_payment =
                                            gson.fromJson(map.getAsJsonObject("src_payment"), Payment.class);
                                    Document src = items.find(eq("_id", src_payment.currency)).first();
                                    int srcCurrency = src.getInteger("value");

                                    items.updateOne(Filters.eq("_id", src_payment.currency),
                                            Updates.set("value", srcCurrency + src_payment.amount));
                                    data = PostHandlers.generateResponseJson(tx);
                                    transactions.deleteOne(doc);
                                }
                            }

                            // cancel payment
                            case "/cancelPayment" -> {
                                success = "true";
                                Tx tx = gson.fromJson(map.getAsJsonObject("partner_tx"), Tx.class);
                                Document doc = transactions.find(eq("_id", tx.id)).first();
                                transactions.deleteOne(doc);
                                data = PostHandlers.generateResponseJson(tx);
                            }
                            case "/rollbackPayment" -> {
                                // rollback payment and generate ids
                                //TO DO
                                Tx tx = gson.fromJson(map.getAsJsonObject("partner_tx"), Tx.class);
                                data = PostHandlers.generateResponseJson(tx);
                            }
                            default -> {
                                data = errorMessage("Error url");
                            }
                        }
                        if (!data.contains("false")){success = "true";}
                    } catch (JsonSyntaxException | NullPointerException jse) {
                        data = errorMessage(jse.getMessage());
                    }
                }

                /* Создание ответа  */
                OutputStream out = clientSocket.getOutputStream();
                Response response = new Response(out);
                response.addHeader("Content-Type", "application/json");
                response.addBody(data);
                response.send();

                out.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
            clientSocket.close();
        }catch (IOException e){
           e.printStackTrace();
        }
    }

    /* Функция формирования ответа об ошибке */
    public static String errorMessage(String message){
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        ResponseData data = new ResponseData("false", 402, message);
        return gson.toJson(data);
    }

    // Получение данных
    public String getData(BufferedReader reader){
        int postDataI = -1;
        String line;
        String data;

        /* Обработка размера данных.
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

            /* Чтение данных */
            StringBuilder dataBuilder = new StringBuilder();
            for (int i = 0; i < postDataI; i++) {
                    int intParser = reader.read();
                    dataBuilder.append((char) intParser);
                }
            data = dataBuilder.toString();

        } catch (IOException e){
            e.printStackTrace();
            return errorMessage("Data receive error");
        }

        /* Pretty json */
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonElement je = JsonParser.parseString(data);
            String prettyJsonString = gson.toJson(je);
            System.out.println(prettyJsonString);
        } catch (JsonSyntaxException jse) {
            jse.printStackTrace();
            return errorMessage("Json error");
        }
        return data;
    }

    // Проверка json
    public static boolean validateIsPaymentPossible(JsonObject map){
        return map.has("payment") && map.has("client") && map.has("product") &&
                map.has("order") && map.has("tx") && map.has("src_cls");
    }
}