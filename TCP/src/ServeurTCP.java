import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ServeurTCP extends Thread {
    final static int port = 9632;
    private Socket socket;
    private static boolean serverRunning = true;

    // Store connected clients
    private static List<PrintStream> clients = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket socketServeur = new ServerSocket(port);
            System.out.println("Server is running...");

            while (serverRunning) {
                Socket socketClient = socketServeur.accept();
                ServeurTCP t = new ServeurTCP(socketClient);
                t.start();
            }

            socketServeur.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ServeurTCP(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream out = new PrintStream(socket.getOutputStream());

            // Add the client's output stream to the list of clients
            clients.add(out);

            while (true) {
                String message = in.readLine();

                if (message == null || message.equalsIgnoreCase("exit")) {
                    break;
                }

                System.out.println("Received: " + message);

                // Send the message to all connected clients except the sender
                for (PrintStream clientOut : clients) {
                    if (clientOut != out) {
                        clientOut.println(message);
                    }
                }
            }

            // Notify other clients that this client is disconnecting
            String disconnectMessage = "User " + socket.getInetAddress() + " disconnected.";
            for (PrintStream clientOut : clients) {
                if (clientOut != out) {
                    clientOut.println(disconnectMessage);
                }
            }

            // Remove the client's output stream from the list of clients
            clients.remove(out);

            // Close the socket only if it's still connected
            if (!socket.isClosed()) {
                socket.close();
                System.out.println("Client disconnected.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to stop the server
    public static void stopServer() {
        serverRunning = false;
    }
}
