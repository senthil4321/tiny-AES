package com.srk.demo.aestiny.core;

import javax.net.ssl.*;
import java.io.OutputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.SecureRandom;

public class MainTCP {
    public static void main(String[] args) {
        String[] messages = {
            String.format("%-512s", "This is a longer message 1 with more content to test the TCP transmission."),
            String.format("%-512s", "This is a longer message 2 with more content to test the TCP transmission."),
            String.format("%-512s", "This is a longer message 3 with more content to test the TCP transmission."),
            String.format("%-512s", "This is a longer message 4 with more content to test the TCP transmission."),
            String.format("%-512s", "This is a longer message 5 with more content to test the TCP transmission.")
        };
        int[] intervals = {100, 20, 300, 400, 500}; // intervals in milliseconds

        for (int i = 0; i < messages.length; i++) {
            final int index = i;
            new Thread(() -> {
                try {
                    // Load the keystore containing the server certificate
                    KeyStore keyStore = KeyStore.getInstance("JKS");
                    keyStore.load(MainTCP.class.getResourceAsStream("/server.keystore"), "password".toCharArray());

                    // Set up key manager factory to use the server keystore
                    KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
                    keyManagerFactory.init(keyStore, "password".toCharArray());

                    // Set up trust manager factory to use the server keystore
                    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
                    trustManagerFactory.init(keyStore);

                    // Set up SSL context to use the key managers and trust managers
                    SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
                    sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

                    SSLServerSocketFactory serverSocketFactory = sslContext.getServerSocketFactory();
                    SSLServerSocket serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(9876);
                    serverSocket.setEnabledProtocols(new String[]{"TLSv1.2"});
                    serverSocket.setEnabledCipherSuites(new String[]{
                        "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
                        "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384"
                    });

                    while (true) {
                        SSLSocket socket = (SSLSocket) serverSocket.accept();
                        OutputStream outputStream = socket.getOutputStream();

                        while (true) {
                            outputStream.write(messages[index].getBytes());
                            outputStream.flush();
                            System.out.println("Sent: " + messages[index]);
                            Thread.sleep(intervals[index]);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}