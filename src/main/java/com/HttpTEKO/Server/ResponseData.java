package com.HttpTEKO.Server;

public class ResponseData {
    String success;
    Result result;
    public ResponseData(String success,
                        String id, String start_t){
        this.success = success;
        this.result = new Result(id, start_t);
    }
    class Result{
        Tx tx;
        Src_payment src_payment;
        int rate;
        public Result(String id, String start_t){
            this.tx = new Tx(id, start_t);
            this.rate = 110;
        }
        class Tx{
            String id;
            String start_t;
            public Tx(String id, String start_t){
                this.id = id;
                this.start_t = start_t;
            }
        }
        //TO DO
        class Src_payment{
            int amount;
            int currency;
            int exponent;
        }
    }
}
