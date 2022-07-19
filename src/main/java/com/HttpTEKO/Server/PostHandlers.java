package com.HttpTEKO.Server;

import com.HttpTEKO.payload.Payment;
import com.HttpTEKO.payload.Tx;
import com.google.gson.JsonObject;

public class PostHandlers {
    public static ResponseData isPaymentPossible(JsonObject map){
        Payment payment = new Payment(
                map.getAsJsonObject("payment").get("amount").getAsInt(),
                map.getAsJsonObject("payment").get("currency").getAsInt(),
                map.getAsJsonObject("payment").get("exponent").getAsInt()
        );
        ResponseData data = new ResponseData("true",
                "11223344556677",
                "1537134068907",
                payment);
        return data;

    }

    public static String generateResponseJson(Tx tx){
        JsonBuilder data = new JsonBuilder()
                .add("success", "true")
                .add("result", new JsonBuilder()
                        .add("tx", new JsonBuilder()
                                .add("id", tx.id)
                                .add("start_t", tx.start_t)
                                .add("finish_t", 2342835)));

        return data.toJson();
    }
}
