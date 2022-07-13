package com.HttpTEKO;

import com.HttpTEKO.postdata.Dst;
import com.HttpTEKO.postdata.Order;
import com.HttpTEKO.postdata.PostData;

public class init {
    public static void main(String[] args) {
        HttpRequestPOST init = new HttpRequestPOST();
        //Dst dst = new Dst("Y6UBATOP9000", true, "Europe");
        Order ord = new Order("1122334455", 142843063,"transaction",
                "mobile_app",  "some_value");
        PostData data = new PostData(
                "praktika_2022", "app",
                "spotify",
                10000, 643, 3,
                "mc", "78005553535", "mts",
                ord,
                "http://89.169.28.251:80",
                "Europe");
        init.send("https://gate-test-02.teko.io/api/initiators/default/initPayment", data);
    }
}
