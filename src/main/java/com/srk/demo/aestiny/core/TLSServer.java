package com.srk.demo.aestiny.core;
import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;

public class TLSServer {
    public static void main(String[] args) {
        int port = 9876;
        String keystorePath = "serverkeystore.jks";
        String keystorePassword = "password";

        try {
            // Load the keystore
            KeyStore keyStore = KeyStore.getInstance("JKS");
            try (FileInputStream keyStoreStream = new FileInputStream(keystorePath)) {
                keyStore.load(keyStoreStream, keystorePassword.toCharArray());
            }

            // Initialize the KeyManagerFactory
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, keystorePassword.toCharArray());

            // Initialize the SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

            // Create the SSLServerSocket
            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
            SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(port);

            // Set the enabled cipher suites
            sslServerSocket.setEnabledCipherSuites(new String[]{"TLS_RSA_WITH_AES_128_CBC_SHA256"});

            System.out.println("TLS server started on port " + port);

            // Accept connections
            while (true) {
                try (SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept()) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("Received: " + line);
                        writer.write("Echo: " + line + "\n");
                        writer.flush();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}