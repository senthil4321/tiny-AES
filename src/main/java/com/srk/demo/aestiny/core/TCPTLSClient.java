package com.srk.demo.aestiny.core;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;

public class TCPTLSClient {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 9876;
        String truststorePath = "clienttruststore.jks";
        String truststorePassword = "password";

        String[] messages = {
            "Message 1: Hello, this is a test mnothsdfgsdfgsnothsdfgsdfgsnothsdfgsdfgsnothsdfgsdfgsessage.",
            "Message 2: Another message for tnothsdfgsdfgsnothsdfgsdfgsnothsdfgsdfgsnothsdfgsdfgsnothsdfgsdfgsesting.",
            "Message 3: Yet anothsdfgsdfgsnothsdfgsdfgsnothsdfgsdfgsnothsdfgsdfgsnothsdfgsdfgsnothsdfgsdfgsnothsdfgsdfgsdfgsdfgsdfgsd er test message.",
            "Message 4: More messages to test. sdfgsdfgsdfgsdgsdfgsdfgsdfgsdfgsdf sdfgsdf sdfgsdfg sdfgsdf",
            "Message 5: Final test message."
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

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));

                int messageIndex = 0;
                while (true) {
                    writer.write(messages[messageIndex] + "\n");
                    writer.flush();
                    System.out.println("Sent: " + messages[messageIndex]);

                    // Read the response from the server
                    String response = reader.readLine();
                    System.out.println("Received: " + response);

                    // Wait for the specified interval before sending the next message
                    Thread.sleep(intervals[messageIndex]);

                    // Move to the next message, wrap around if necessary
                    messageIndex = (messageIndex + 1) % messages.length;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}