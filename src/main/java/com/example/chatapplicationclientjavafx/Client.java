package com.example.chatapplicationclientjavafx;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;

public class Client {

    private  Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Client(Socket socket){
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch(IOException e){
            e.printStackTrace();
            System.out.println("Error creating client");
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    // Method that sends messages to the server
    public void sendMessageToServer(String messageToSend){
        try {
            bufferedWriter.write(messageToSend);
            bufferedWriter.newLine(); // need to send over a new line so it knows the message has ended
            bufferedWriter.flush(); // manually flushing the buffer because it won't be full


        } catch(IOException e){
            e.printStackTrace();
            System.out.println("Error sending message to server.");
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }


    //Method that receives the messages from the servers
    public void receiveMessageFromServer(VBox vBox){
        // new thread so that it won't be blocking the whole program
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected()) {
                    String messageFromServer = null;
                    try {
                        messageFromServer = bufferedReader.readLine();
                        HelloController.addLabel(messageFromServer, vBox);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error receiving message from the server.");
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        break;
                    }

                }


            }
        }).start();
    }

    // Method to close all the resources that we are using - prevents null pointer exceptions
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
            if(bufferedReader != null){
                // closes the underlying streams as well
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }

        } catch(IOException e){
            e.printStackTrace();
        }

    }


}
