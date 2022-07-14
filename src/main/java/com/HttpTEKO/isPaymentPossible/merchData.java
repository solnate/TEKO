package com.HttpTEKO.isPaymentPossible;
import com.HttpTEKO.InitPayment.Order;
import com.HttpTEKO.InitPayment.Payment;
import com.HttpTEKO.InitPayment.initData;

public class merchData extends initData {
    public merchData(
            String id, String showcase,
            String product,
            Payment payment,
            String cls, String phone_number, String operator,
            Order order,
            String callback,
            String tag
    ){
        super(
                id, showcase,
                product,
                payment,
                cls, phone_number, operator,
                order,
                callback,
                tag
        );
    }
}
