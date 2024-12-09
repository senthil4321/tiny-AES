
package com.srk.demo.aestiny.core;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class ListSupportedCipherSuites {
    public static void main(String[] args) {
        try {
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) factory.createSocket();
            socket.setEnabledProtocols(new String[]{"TLSv1.2"});
            String[] supportedCipherSuites = socket.getSupportedCipherSuites();

            System.out.println("Supported TLS 1.2 Cipher Suites:");
            for (String cipherSuite : supportedCipherSuites) {
                System.out.println(cipherSuite);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}