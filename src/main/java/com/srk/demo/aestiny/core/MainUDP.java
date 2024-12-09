package com.srk.demo.aestiny.core;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainUDP {
    public static void main(String[] args) {
        String[] messages = {
            "Message 5 asdfjao;sdlfjal;skd a;lsfkdj alskdfja;lkdj ;alskfdj a;slfjkd;als fkdj",
            "Message 5 asdfjao;sdlfjal;skd a;lsfkdj alskdfja;lkdj ;alskfdj a;slfjkd;als fkdj",
            "Message 5 asdfjao;sdlfjal;skd a;lsfkdj alskdfja;lkdj ;alskfdj a;slfjkd;als fkdj",
            "Message 5 asdfjao;sdlfjal;skd a;lsfkdj alskdfja;lkdj ;alskfdj a;slfjkd;als fkdj",
            "Message 5 asdfjao;sdlfjal;skd a;lsfkdj alskdfja;lkdj ;alskfdj a;slfjkd;als fkdj "
            
            
        };
        int[] intervals = {100, 200, 300, 400, 500}; // intervals in milliseconds

        for (int i = 0; i < messages.length; i++) {
            final int index = i;
            new Thread(() -> {
                try {
                    DatagramSocket socket = new DatagramSocket();
                    InetAddress address = InetAddress.getByName("localhost");
                    byte[] buffer;
                    DatagramPacket packet;

                    while (true) {
                        buffer = messages[index].getBytes();
                        packet = new DatagramPacket(buffer, buffer.length, address, 9876);
                        socket.send(packet);
                        System.out.println("Sent: " + messages[index]);
                        Thread.sleep(intervals[index]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
