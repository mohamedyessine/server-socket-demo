package org.example;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedwriter;
    private String clientUsername;
    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedwriter = new
                    BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new
                    BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage("Server" + clientUsername + " has entered the chat!");
        }catch(IOException e){
            closeEverything(socket, bufferedReader, bufferedwriter);
        }
    }
    @Override
    public void run(){
        String  messageFromClient;

        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            }catch(IOException e) {
                closeEverything(socket,bufferedReader,bufferedwriter);
                break;
            }
        }
    }

    public void broadcastMessage(String messageToSend){
        for (ClientHandler clientHandler : clientHandlers) {
            try{
                if (!clientHandler.clientUsername.equals(clientUsername)){
                    clientHandler.bufferedwriter.write(messageToSend);
                    clientHandler.bufferedwriter.newLine();
                    clientHandler.bufferedwriter.flush();
                }
            }catch (IOException e ){
                closeEverything(socket,bufferedReader,bufferedwriter);
            }
        }
    }
    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("Server: " + clientUsername + " has left the chat!");
    }

    public void closeEverything(Socket socket, BufferedReader bufferdReader, BufferedWriter bufferedWriter){
        removeClientHandler();
        try {
            if (bufferdReader != null){
                bufferdReader.close();
            }
            if(bufferedWriter != null) {
                bufferedwriter.close();
            }
            if (socket != null){
                socket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }


}