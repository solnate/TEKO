package com.HttpTEKO.Server;

import com.HttpTEKO.payload.Order;
import com.HttpTEKO.payload.Payment;
import com.HttpTEKO.payload.initData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GetHandlers {
    public static String getPaymentsByTag() {
        HttpRequestPOST init = new HttpRequestPOST();
        JsonBuilder data = new JsonBuilder()
                .add("client", new JsonBuilder()
                        .add("id", "praktika_2022")
                        .add("showcase", "app"))
                .add("tag", "Europe");
        return init.send("https://gate-test-02.teko.io/api/initiators/default/getPaymentsByTag", data.toJson());
    }

    public static String getPaymentByIdOrStatus(String state) {
        HttpRequestPOST init = new HttpRequestPOST();
        JsonBuilder data = new JsonBuilder()
                .add("client", new JsonBuilder()
                        .add("id", "praktika_2022")
                        .add("showcase", "app"))
                .add("tx_id", "62d6c5b560b2a1a7c1d61c4c");
        if (state.equals("byId")) {
            return init.send("https://gate-test-02.teko.io/api/initiators/default/getPaymentById", data.toJson());
        }
        else {
            return init.send("https://gate-test-02.teko.io/api/initiators/default/getPaymentStatus", data.toJson());
        }
    }

    public static String initPayment(String callback) {
        HttpRequestPOST init = new HttpRequestPOST();
        Order ord = new Order("1122334455", 142843063,"transaction",
                "mobile_app",  "some_value");
        Payment payment = new Payment(10000, 643, 3);
        initData data = new initData(
                "praktika_2022", "app",
                "spotify",
                payment,
                "mc", "78005553535", "mts",
                ord,
                "Europe");
        if (!callback.isEmpty()) {
            data.initCallback(callback);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(data);
        return init.send("https://gate-test-02.teko.io/api/initiators/default/initPayment", json);
    }

}
