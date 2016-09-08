package lab1;

import java.net.*;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.EventQueue;
import java.io.*;

public class Client implements Runnable {

    private Socket socket = null;
    private Thread thread = null;
    private DataOutputStream streamOut = null;
    private PrintStream out;
    private BufferedReader in = null;
    //private ClientThread client = null;
    protected String username;
    private ChatGUI frame;

    public Client(String ipAddr, String username, int serverPort) {
        this.username = username;

        // set up the socket to connect to the gui
        try {
            socket = new Socket(ipAddr, serverPort);
            start();
        } catch (UnknownHostException h) {
            JOptionPane.showMessageDialog(new JFrame(), "Unknown Host " + h.getMessage());
            System.exit(1);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(new JFrame(), "IO exception: " + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void run() {
        //TODO check for a new message, once we receive it, steamOut will send it to the server
        try {
            do {
                String cmd = in.readLine();
                switch(cmd){
                    case "MSG":
                        String msg = in.readLine();
                        frame.recieveMessage(msg);
                        break;
                }
            } while (true);
        } catch (IOException ioe) {
            //show error
            JOptionPane.showMessageDialog(new JFrame(), "IO exception: " + ioe.getMessage());
            stop();
        }
    }

    public synchronized void handleChat(String msg) throws IOException{
        //TODO
        out.println("MSG");
        out.println(msg);
        System.out.println(msg);
    }

    public void start() throws IOException {
        frame = new ChatGUI(username, this);
        frame.setVisible(true);

        //TODO Socket set up
        streamOut = new DataOutputStream(socket.getOutputStream());

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        out = new PrintStream(socket.getOutputStream());
        
        streamOut.write((username + "\n").getBytes(), 0, username.length() + 1);
        
        
        //Racers, start your engines!
        this.thread = new Thread(this);

        this.thread.start();
    }

    public void stop() {
        //TODO
        try {
            this.thread.join();

            //TODO: Shutdown actions, remove GUI?
        } catch (InterruptedException ie) {
            //show error
            JOptionPane.showMessageDialog(new JFrame(), "Interrupted exception: " + ie.getMessage());

            //exit with dignity
            System.exit(1);
        }
        System.exit(1);
    }

}
