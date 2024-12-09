package com.srk.demo.aestiny.core;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;

public class TCPTLSClient2 {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 9876;
        String truststorePath = "clienttruststore.jks";
        String truststorePassword = "password";

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

                // Send a message to the server
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));
                writer.write("Hello, TLS server!\n");
                writer.flush();

                // Read the response from the server
                BufferedReader reader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("Received: " + line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}