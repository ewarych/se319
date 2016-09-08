package lab1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ClientHandler implements Runnable {

    public final long id;
    public final Socket socket;
    private BufferedReader in;
    private PrintStream out;
    private final Server server;
    public final String username;
    public boolean active = false;
    private Thread thread;

    protected ClientHandler(Socket s, long id, Server server) throws java.io.IOException {
        this.id = id;
        this.socket = s;
        this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        this.out = new PrintStream(s.getOutputStream());
        this.server = server;
        this.username = in.readLine();
        server.handle(username + " has joined the chat");

        this.thread = new Thread(this);
        this.thread.start();
    }

    @Override
    public void run() {
        active = true;
        try {
            do {
                String cmd = in.readLine();
                switch (cmd) {
                    case "MSG":
                        String msg = in.readLine();
                        server.handle(msg);
                        break;
                }
            } while (true);
        } catch (java.io.IOException ioe) {
            //TODO
            active = false;
        }
    }

    public void sendMessage(String msg) {
        out.println("MSG");
        out.println(msg);
    }

    public void disconnect() {
        active = false;
        try {
            thread.join();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        
        try {
            socket.close();
        } catch (java.io.IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
