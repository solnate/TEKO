package com.HttpTEKO;

import com.HttpTEKO.postdata.*;
import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpRequestPOST {
    public static void main(String[] args) {
        String link = "http://pg.teko.io/api/initiators/default/initPayment";

        Gson gson = new Gson();

        Dst dst = new Dst("Y6UBATOP9000", true, "Europe");
        Order ord = new Order("TRANSFER");
        PostData data = new PostData(
                "company_name", "mobile_app",
                "world_of_warcraft",
                10000, 643, 3,
                "mc", "78005553535", "mts",
                dst,
                ord);

        String json = gson.toJson(data);
        byte[] out = json.getBytes();

        try{
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Signature", "VgepHaYJBn9jwAg3N6wzFhi8UeQ=");
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

            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()){
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
            }

        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            System.err.println(e.getMessage());
        }

    }
}
