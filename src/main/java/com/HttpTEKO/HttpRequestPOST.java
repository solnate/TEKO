package com.HttpTEKO;

import com.HttpTEKO.InitPayment.*;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class HttpRequestPOST {
    public void send(String link, Object data) {
        Gson gson = new Gson();
        String json = gson.toJson(data);
        byte[] out = json.getBytes();
        byte[] key = "TestSecret".getBytes();
        HmacUtils hm256 = new HmacUtils(HmacAlgorithms.HMAC_SHA_1, key);
        String hmac = Base64.encodeBase64String(hm256.hmac(json));
        System.out.println(hmac);
        try{
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Signature", hmac);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
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
