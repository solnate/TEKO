package com.HttpTEKO;

import com.HttpTEKO.postdata.*;
import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class HttpRequestPOST {
    public static void main(String[] args) {
        String link = "http://pg-02.teko.io/api/initiators/default/initPayment";

        Gson gson = new Gson();

        Dst dst = new Dst("Y6UBATOP9000", true, "Europe");
        Order ord = new Order("ITEM");
        PostData data = new PostData(
                "company_name", "app",
                "world_of_warcraft",
                10000, 643, 3,
                "mc", "78005553535", "mts",
                dst,
                ord);

        String json = gson.toJson("data");
        byte[] out = json.getBytes();

        try{
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.connect();

            try{
                OutputStream os = connection.getOutputStream();
                os.write(out);
                os.close();
            } catch(Exception e){
                System.err.println(e.getMessage());
            }

            System.out.println(connection.getResponseCode());

            Map<String, List<String>> map = connection.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                if (entry.getKey() != null) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
                else{
                    System.out.println(entry.getValue());
                }

            }

            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader bfr = new BufferedReader(isr);
            StringBuilder str = new StringBuilder();
            String line;
            while((line = bfr.readLine()) != null){
                str.append(line);
            }
            System.out.println(str);
            isr.close();
            bfr.close();


        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            System.err.println(e.getMessage());
        }

    }
}
