package com.HttpTEKO;

import com.google.gson.Gson;

import java.util.Map;

public class jsontest {
    public static void main(String[] args) {
        String jsonString = "{\"success\":true,\"result\":{\"tx\":{\"id\":\"62cfea9d60b216e44a57b983\",\"start_t\":1657793181095}}}";
        Gson gson = new Gson();
        Map map = gson.fromJson(jsonString, Map.class);
        System.out.println(((Map)map.get("result")).get("tx"));
    }
}
