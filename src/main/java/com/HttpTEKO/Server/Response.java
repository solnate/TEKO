package com.HttpTEKO.Server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/** Класс для формирования ответа сервера */
public class Response  {
    private OutputStream out;
    private int statusCode;
    private String statusMessage;
    private Map<String, String> headers = new HashMap<>();
    private String body;

    public Response(OutputStream out)  {
        this.out = out;
    }

    public void setResponseCode(int statusCode, String statusMessage)  {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public void addHeader(String headerName, String headerValue)  {
        this.headers.put(headerName, headerValue);
    }

    public void addBody(String body)  {
        headers.put("Content-Length", Integer.toString(body.length()));
        this.body = body;
    }

    public void send() throws IOException {
        System.out.println();
        System.out.println("Sent:");
        System.out.println("HTTP/1.1 " + statusCode + " " + statusMessage);
        out.write(("HTTP/1.1 " + statusCode + " " + statusMessage + "\r\n").getBytes());
        for (String headerName : headers.keySet())  {
            System.out.println(headerName + ": " + headers.get(headerName));
            out.write((headerName + ": " + headers.get(headerName) + "\r\n").getBytes());
        }
        out.write("\r\n".getBytes());
        if (body != null)  {
            System.out.println(body);
            out.write(body.getBytes());
        }
    }
}
