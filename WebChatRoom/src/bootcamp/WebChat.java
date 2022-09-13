package bootcamp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class WebChat {

    private LinkedList<ClientHandler> clientList = new LinkedList<>();
    private Socket clientSocket;
    private ServerSocket serverSocket;
    private int portNumber = 9501;


    public WebChat(){
        try {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Server is listening on Port: " + portNumber);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void init(){

        while (true) {
            try {
                clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientList.add(clientHandler);
                Thread thread = new Thread(clientHandler);
                thread.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("A new connection has been made.");

        }
    }

    public class ClientHandler implements Runnable{

        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;

        }

        public void sendToAll(){
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String message;
                if((message = reader.readLine()) != null){

                    for (int i = 0; i < clientList.size(); i++) {
                        PrintWriter writer = new PrintWriter(clientList.get(i).getSocket().getOutputStream(), true);
                        writer.println(message);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public Socket getSocket(){
            return clientSocket;
        }

        @Override
        public void run() {
            while(true){
                sendToAll();
            }
        }
    }

    public static void main(String[] args) {

        WebChat webChat = new WebChat();
        webChat.init();
    }
}
