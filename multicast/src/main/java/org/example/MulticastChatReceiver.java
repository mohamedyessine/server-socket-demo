package org.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastChatReceiver {
    private static final String MULTICAST_GROUP = "230.0.0.1";
    private static final int PORT = 4446;

    public static void main(String[] args) {
        try (MulticastSocket multicastSocket = new MulticastSocket(PORT)) {
            InetAddress group = InetAddress.getByName(MULTICAST_GROUP);
            multicastSocket.joinGroup(group);

            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                multicastSocket.receive(packet);

                String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received message: " + receivedMessage);

                if (receivedMessage.equalsIgnoreCase("EXIT")) {
                    System.out.println("Sender has exited. Press 'exit' to leave the multicast group.");
                } else if (receivedMessage.equalsIgnoreCase("exit")) {
                    System.out.println("Leaving the multicast group.");
                    break;
                }
            }

            multicastSocket.leaveGroup(group);
            multicastSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
