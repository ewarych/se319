package lab1;

//package lab3;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import java.io.*;

public class Server implements Runnable {

    private ServerSocket serverSocket = null;
    private Thread mainThread = null;
    private PrintStream log;
    private File file = new File("chat.txt");
    private PrintWriter writer;
    private ServerGUI frame;
    private Thread guiMessageThread;
    private int clientNum = 0;
    public static final int MAX_CLIENTS = 1000;
    private ClientHandler[] clients;

    public Server(int port) {
        // TODO Binding and starting server
        try {
            System.out.println("Binding to port " + port + ", please wait  ...");
            serverSocket = new ServerSocket(port);
            System.out.println("Server started: " + serverSocket);
            start();
        } catch (IOException ioe) {
            System.out.println("Can not bind to port " + port + ": " + ioe.getMessage());
        }
    }

    @Override
    public void run() {
        System.out.println("Ready to accept");
        do {
            // TODO wait for a client or show error
            Socket clientSocket = null;
            try {

                // 2.1 WAIT FOR CLIENT TO TRY TO CONNECT TO SERVER
                System.out.println("Waiting for client " + (clientNum + 1)
                        + " to connect!");
                clientSocket = serverSocket.accept();

                addThread(clientSocket);

            } catch (IOException e) {
                System.out.println("Accept failed: 4444");
                System.exit(-1);
            }

            // 2.3 GO BACK TO WAITING FOR OTHER CLIENTS
        } while (true);

    }

    public void start() throws IOException {
        frame = new ServerGUI();
        frame.setVisible(true);
        // TODO launch a thread to read for new messages by the server
        mainThread = new Thread(this);
        mainThread.start();
        clients = new ClientHandler[MAX_CLIENTS];
        log = new PrintStream(new FileOutputStream(file));
    }

    public void stop() {
        // TODO
        log.flush();
        log.close();
    }  

    private ClientHandler findClient(int clientID) {
        if(clients[clientID] == null || !clients[clientID].active)
            return null;
        return clients[clientID];
    }

    public synchronized void handle(String input) {
        // TODO new message, send to clients and then write it to history
        for(int i = 0; i < clients.length; i++){
            ClientHandler c = clients[i];
            if(c != null && c.active){
                c.sendMessage(input);
            }
        }
        frame.recieveMessage(input + "\n");
        System.out.println(input);
        log.println(input);
        // TODO update own gui
    }

    public synchronized void remove(int ID) {
        // TODO get the serverthread, remove it from the array and then
        // terminate it
        clients[ID].disconnect();
    }

    private void addThread(Socket socket) {
        // TODO add new client
        // 2.2 SPAWN A THREAD TO HANDLE CLIENT REQUEST
        int clientID = (++clientNum);
        System.out.println("Server got damn connected to a client" + clientID);
        Thread t;
        try {
            ClientHandler c = new ClientHandler(socket, clientNum, this);
            clients[clientID] = c;
        } catch (java.io.IOException ioe) {
            ioe.printStackTrace();
            return;
        }
    }

    public static void main(String args[]) {
        Server server = null;
        server = new Server(1222);
    }
}
