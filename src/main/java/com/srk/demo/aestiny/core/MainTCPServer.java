
package com.srk.demo.aestiny.core;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MainTCPServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9876);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> {
                    try {
                        InputStream inputStream = clientSocket.getInputStream();
                        byte[] buffer = new byte[1024];
                        int bytesRead;

                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            System.out.println("Received: " + new String(buffer, 0, bytesRead));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}