package com.HttpTEKO.Server;

import com.google.gson.JsonObject;
// Кастомный генератор json
public class JsonBuilder {
    public final JsonObject json = new JsonObject();

    public String toJson() {
        return json.toString();
    }

    public JsonBuilder add(String key, String value) {
        json.addProperty(key, value);
        return this;
    }
    public JsonBuilder add(String key, int value) {
        json.addProperty(key, value);
        return this;
    }

    public JsonBuilder add(String key, JsonBuilder value) {
        json.add(key, value.json);
        return this;
    }
}
