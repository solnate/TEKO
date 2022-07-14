package com.HttpTEKO.postdata;

public class PostData {
    Client client;
    String product;
    Payment payment;
    Src src;
    Dst dst;
    Order order;
    String callback;
    String tag;
    public PostData(String id, String showcase,
                    String product,
                    int amount, int currency, int exponent,
                    String cls, String phone_number, String operator,
                    Order order,
                    String callback,
                    String tag) {
        this.client = new Client(id, showcase);
        this.product = product;
        this.payment = new Payment(amount, currency, exponent);
        this.src = new Src(cls, phone_number, operator);
        this.order = order;
        this.callback = callback;
        this.tag = tag;
    }
    public void initdst(Dst dst){
        this.dst = dst;
    }
    class Client {
        String id;
        String showcase;

        public Client(String id, String showcase){
            this.id = id;
            this.showcase = showcase;
        }
    }

    class Payment {
        int amount;
        int currency;
        int exponent;
        public Payment(int amount, int currency, int exponent){
            this.amount = amount;
            this.currency = currency;
            this.exponent = exponent;
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
