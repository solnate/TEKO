package com.HttpTEKO.InitPayment;

import com.HttpTEKO.HttpRequestPOST;
import com.HttpTEKO.InitPayment.Order;
import com.HttpTEKO.InitPayment.Payment;
import com.HttpTEKO.InitPayment.initData;

public class initiatorsRequest {
    public static void main(String[] args) {
        HttpRequestPOST init = new HttpRequestPOST();
        //Dst dst = new Dst("Y6UBATOP9000", true, "Europe");
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
        data.initCallback("http://89.169.28.251:80");
        init.send("https://gate-test-02.teko.io/api/initiators/default/initPayment", data);
    }
}
