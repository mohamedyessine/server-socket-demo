package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastChatSender {
    private static final String MULTICAST_GROUP = "230.0.0.1";
    private static final int PORT = 4446;

    public static void main(String[] args) {
        try (MulticastSocket multicastSocket = new MulticastSocket()) {
            InetAddress group = InetAddress.getByName(MULTICAST_GROUP);
            multicastSocket.joinGroup(group);
//            sendMessage("Sender joined");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                System.out.print("Enter message (type 'exit' to end): ");
                String message = reader.readLine();

                if (message.equalsIgnoreCase("exit")) {
                    message = "EXIT";
                }

                byte[] buffer = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                multicastSocket.send(packet);

                if (message.equalsIgnoreCase("EXIT")) {
                    System.out.println("Exiting the sender.");
                    break;
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private static void sendMessage(String message) throws IOException {
//        byte[] buffer = message.getBytes();
//        InetAddress group = InetAddress.getByName(MULTICAST_GROUP);
//        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
//        try (MulticastSocket multicastSocket = new MulticastSocket()) {
//            multicastSocket.send(packet);
//        }
//    }
}
