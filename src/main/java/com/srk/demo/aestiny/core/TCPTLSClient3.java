package com.srk.demo.aestiny.core;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;

public class TCPTLSClient3 {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 9876;
        String truststorePath = "clienttruststore.jks";
        String truststorePassword = "password";

        String[] messages = {
            padMessage("Message 1: Hello, this is a test message."),
            padMessage("Message 2: Another message for testing."),
            padMessage("Message 3: Yet another test message."),
            padMessage("Message 4: More messages to test."),
            padMessage("Message 5: Final test message.")
        };
        int[] intervals = {100, 200, 300, 400, 500}; // intervals in milliseconds

        try {
            // Load the truststore
            KeyStore trustStore = KeyStore.getInstance("JKS");
            try (FileInputStream trustStoreStream = new FileInputStream(truststorePath)) {
                trustStore.load(trustStoreStream, truststorePassword.toCharArray());
            }

            // Initialize the TrustManagerFactory
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            // Initialize the SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            // Create the SSLSocket
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            try (SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(host, port)) {
                sslSocket.setEnabledCipherSuites(new String[]{"TLS_RSA_WITH_AES_128_CBC_SHA256"});

                // Start handshake
                sslSocket.startHandshake();

                for (int i = 0; i < messages.length; i++) {
                    int index = i;
                    new Thread(() -> {
                        try {
                            sendMessage(sslSocket, messages[index], intervals[index]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendMessage(SSLSocket sslSocket, String message, int interval) throws IOException, InterruptedException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));

        while (true) {
            writer.write(message + "\n");
            writer.flush();
            System.out.println("Sent: " + message);

            // Read the response from the server
            String response = reader.readLine();
            System.out.println("Received: " + response);

            // Wait for the specified interval before sending the next message
            Thread.sleep(interval);
        }
    }

    private static String padMessage(String message) {
        int targetLength = 512;
        if (message.length() > targetLength) {
            return message.substring(0, targetLength);
        }
        StringBuilder paddedMessage = new StringBuilder(message);
        while (paddedMessage.length() < targetLength) {
            paddedMessage.append(" ");
        }
        return paddedMessage.toString();
    }
}