package com.HttpTEKO.postdata;

public class Dst {
    String id;
    Extra extra;
    public Dst(String id, boolean premium, String game_server){
        this.id = id;
        this.extra = new Extra(premium, game_server);
    }
    class Extra {
        boolean premium;
        String game_server;
        public Extra(boolean premium, String game_server){
            this.premium = premium;
            this.game_server = game_server;
        }
    }

}

