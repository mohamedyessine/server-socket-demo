package org.example;

import javax.imageio.IIOException;
import java.io.*;
import java.net.Proxy;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private
    Socket
            socket;
    private BufferedReader
            bufferedReader;
    private
    BufferedWriter
            bufferedwriter;
    private
    String
            username;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.bufferedwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        } catch(IOException e) {
            closeEverything(socket, bufferedReader, bufferedwriter);
        }
    }

    public void sendMessage() {
        try {
            bufferedwriter.write(username);
            bufferedwriter.newLine();
            bufferedwriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                bufferedwriter.write(username + ": " + messageToSend);
                bufferedwriter.newLine();
                bufferedwriter.flush();

            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedwriter);
        }
    }

        public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;
                while (socket.isConnected()){
                    try{
                        msgFromGroupChat = bufferedReader.readLine();
                        System.out.println(msgFromGroupChat);
                    }catch (IOException e){
                        closeEverything(socket, bufferedReader, bufferedwriter);
                    }
                }
            }
        }).start();
        }

        public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
            try {
                if (bufferedReader != null){
                    bufferedReader.close();
                }
                if(bufferedWriter != null) {
                    bufferedWriter.close();
                }
                if (socket != null){
                    socket.close();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter your username");
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost",  1234);
        Client client = new Client(socket, username);
        client.listenForMessage();
        client.sendMessage();
    }

}