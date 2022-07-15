package com.HttpTEKO;

import com.HttpTEKO.InitPayment.*;
import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.bson.Document;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class HttpRequestPOST {
    public void send(String link, Object data) {
        Gson gson = new Gson();
        String json = gson.toJson(data);
        byte[] out = json.getBytes();
        byte[] key = "TestSecret".getBytes();
        HmacUtils hm256 = new HmacUtils(HmacAlgorithms.HMAC_SHA_1, key);
        String hmac = Base64.encodeBase64String(hm256.hmac(json));
        System.out.println(hmac);

        var mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("testdb");
        MongoCollection<Document> collection = database.getCollection("initiator");
        var doc = new Document();
        doc.append("date", new Date());
        try{
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Signature", hmac);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.connect();

            try{
                OutputStream os = connection.getOutputStream();
                os.write(out);
                os.close();

                doc.append("sent", json);
            } catch(Exception e){
                System.err.println(e.getMessage());
            }

            Map<String, List<String>> map = connection.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

            int status = connection.getResponseCode();
            Reader streamReader = null;
            if (status > 299) {
                streamReader = new InputStreamReader(connection.getErrorStream());
            } else {
                streamReader = new InputStreamReader(connection.getInputStream());
            }

            BufferedReader in = new BufferedReader(streamReader);
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            System.out.println(content);
            doc.append("status code", status);
            doc.append("received", content.toString());
            collection.insertOne(doc);
            in.close();

        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            System.err.println(e.getMessage());
            doc.append("status code", 408);
            doc.append("error", e.getMessage().toString());
            collection.insertOne(doc);
        }
    }

}
