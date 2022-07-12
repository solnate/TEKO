package com.HttpTEKO;

import com.HttpTEKO.postdata.*;
import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.Map;

public class HttpRequestPOST {
    public static void main(String[] args) {
        String link = "https://gate-test-02.teko.io/api/initiators/default/initPayment";

        Gson gson = new Gson();

        Dst dst = new Dst("Y6UBATOP9000", true, "Europe");
        Order ord = new Order("1122334455", 142843063,"transaction",
                "mobile_app",  "some_value");
        PostData data = new PostData(
                "company_name", "app",
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
            connection.setRequestProperty("Signature", "dfdg4gdfg");
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

            Map<String, List<String>> map = connection.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

            int status = connection.getResponseCode();
            System.out.println(status);
            Reader streamReader = null;

            if (status > 299) {
                streamReader = new InputStreamReader(connection.getErrorStream());
            } else {
                streamReader = new InputStreamReader(connection.getInputStream());
            }

            BufferedReader in = new BufferedReader(streamReader);
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            System.out.println(content);
            in.close();

        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

}
