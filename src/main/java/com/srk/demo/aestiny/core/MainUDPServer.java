
package com.srk.demo.aestiny.core;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class MainUDPServer {
    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(9876);
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                socket.receive(packet);
                byte[] receivedData = packet.getData();
                System.out.println("Received: " + new String(receivedData, 0, packet.getLength()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}