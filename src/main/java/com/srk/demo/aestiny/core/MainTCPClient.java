package com.srk.demo.aestiny.core;

import java.io.OutputStream;
import java.net.Socket;

public class MainTCPClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 9876);
            OutputStream outputStream = socket.getOutputStream();
            String message = "Hello, Server!";
            
            while (true) {
                outputStream.write(message.getBytes());
                outputStream.flush();
                Thread.sleep(500); // Send message every 5 seconds
            }
            // socket.close(); // This line will never be reached
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}