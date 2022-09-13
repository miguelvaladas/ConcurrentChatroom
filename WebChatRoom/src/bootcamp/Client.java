package bootcamp;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private int portNumber = 9501;
    private Socket socket;
    private String userName;

    public Client(){
        try {
            socket = new Socket(InetAddress.getLocalHost(),portNumber);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void init(){

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please insert your userName...");
        userName = scanner.nextLine();
        System.out.println("Welcome to the chat " + userName + "!");

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    write();
                }
            }
        });

        t1.start();

        while(true){
            read();
        }
    }

    public void write(){
        Scanner scanner = new Scanner(System.in);
       // System.out.println("Insert your message...");
        String message = scanner.nextLine();

        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(),true);
            writer.println(userName + ": " + message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void read(){
        try {
           BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           String message;
           if((message = reader.readLine()) != null){
               System.out.println(message);
           }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        Client client = new Client();
        client.init();
    }

}
