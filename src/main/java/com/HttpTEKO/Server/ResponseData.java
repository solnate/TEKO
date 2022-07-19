package com.HttpTEKO.Server;

import com.HttpTEKO.payload.Payment;
import com.HttpTEKO.payload.Tx;
import com.google.gson.annotations.Expose;

public class ResponseData {
    @Expose
    String success;
    @Expose
    Result result;
    public ResponseData(String success,
                        String id, String start_t,
                        Payment payment){
        this.success = success;
        this.result = new Result(id, start_t, payment);
    }
    public ResponseData(String success,
                        String id, String start_t){
        this.success = success;
        this.result = new Result(id, start_t);
    }
    public ResponseData(String success,
                        int code, String description){
        this.success = success;
        this.result = new Result(code, description);
    }
    class Result{
        @Expose
        Tx tx;
        @Expose
        Payment src_payment;
        int rate;
        @Expose
        int code;
        @Expose
        String description;
        public Result(int code, String description){
            this.code = code;
            this.description = description;
        }
        public Result(String id, String start_t,
                      Payment payment){
            this.tx = new Tx(id, start_t);
            this.rate = 110;
            this.src_payment = payment;
        }
        public Result(String id, String start_t){
            this.tx = new Tx(id, start_t);
        }

    }
}
