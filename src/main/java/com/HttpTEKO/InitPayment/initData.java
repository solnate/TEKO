package com.HttpTEKO.InitPayment;

/** Класс для формирования json протокола инициатора */
public class initData {
    Client client;
    String product;
    Payment payment;
    Payment src_payment;
    Src src;
    Dst dst;
    Order order;
    String callback;
    String tag;
    public initData(String id, String showcase,
                    String product,
                    Payment payment,
                    String cls, String phone_number, String operator,
                    Order order,
                    String tag) {
        this.client = new Client(id, showcase);
        this.product = product;
        this.payment = payment;
        this.src = new Src(cls, phone_number, operator);
        this.order = order;
        this.tag = tag;
    }
    public void initdst(Dst dst){
        this.dst = dst;
    }
    public void initPayment(Payment payment){ this.src_payment = payment; }
    public void initCallback(String callback){ this.callback = callback; }
    class Client {
        String id;
        String showcase;

        public Client(String id, String showcase){
            this.id = id;
            this.showcase = showcase;
        }
    }

    class Src {
        String cls;
        String phone_number;
        String operator;
        public Src(String cls, String phone_number, String operator){
            this.cls = cls;
            this.phone_number = phone_number;
            this.operator = operator;
        }
    }

}
