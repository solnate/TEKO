package com.HttpTEKO.postdata;

public class Order {
    Transaction transaction;
    String cls;
    Extra extra;
    public Order(String id, int start_t, String cls, String from, String some_key){
        this.transaction = new Transaction(id, start_t);
        this.cls = cls;
        this.extra = new Extra(from, some_key);
    }
    class Transaction {
        String id;
        int start_t;

        public Transaction(String id, int start_t){
            this.id = id;
            this.start_t = start_t;
        }
    }
    class Extra {
        String from;
        String some_key;

        public Extra(String from, String some_key){
            this.from = from;
            this.some_key = some_key;
        }
    }
}
