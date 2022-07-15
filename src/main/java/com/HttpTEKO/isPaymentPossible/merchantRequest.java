package com.HttpTEKO.isPaymentPossible;

import com.HttpTEKO.HttpRequestPOST;
import com.HttpTEKO.InitPayment.Order;
import com.HttpTEKO.InitPayment.Payment;

/** Формирует json для протокола инициатора */
public class merchantRequest {
    public static void main(String[] args) {
        HttpRequestPOST init = new HttpRequestPOST();
        //Dst dst = new Dst("Y6UBATOP9000", true, "Europe");
        Order ord = new Order("1122334455", 142843063,"transaction",
                "mobile_app",  "some_value");
        Payment payment = new Payment(10100, 643, 3);
        merchData data = new merchData(
                "praktika_2022", "app",
                "spotify",
                payment,
                "mc", "78005553535", "mts",
                ord,
                "Europe");
        data.initPayment(new Payment(5000, 343, 3));
        init.send("http://x.x.x.x:80", data);
    }
}