package com.HttpTEKO;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RequestFromFile {
    public static void main(String args[]){
        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(
                    Paths.get("src/main/java/com/HttpTEKO/isPaymentPossible/merchant.json"));
            //Reader reader = Files.newBufferedReader(
            //                    Paths.get("src/main/java/com/HttpTEKO/isPaymentPossible/initiator.json"));
            JsonObject json = gson.fromJson(reader, JsonObject.class);
            System.out.println(json);
            reader.close();
            HttpRequestPOST init = new HttpRequestPOST();
            init.send("http://89.169.52.44:80", json);
        } catch (Exception e){
            System.err.println(e);
        }
    }
}
