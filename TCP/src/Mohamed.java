import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class Mohamed {
    public static void main(String[] args) {

        final String serverAddress = "localhost";
        final int serverPort = 9632;

        try {
            Socket socket = new Socket(serverAddress, serverPort);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream out = new PrintStream(socket.getOutputStream());

            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter your name: ");
            String name = userInput.readLine();


            Thread messageReceiver = new Thread(() -> {
                try {
                    while (true) {
                        String message = in.readLine();
                        if (message == null || message.equalsIgnoreCase("exit")) {
                            break;
                        }
                        System.out.println(message);
                    }
                } catch (SocketException e) {
                    // Ignore or handle the exception gracefully
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });



            messageReceiver.start();

            while (true) {
                String userMessage = userInput.readLine();

                if (userMessage.equalsIgnoreCase("exit")) {
                    out.println("exit");
                    break;
                }

                out.println(name + ": " + userMessage);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
