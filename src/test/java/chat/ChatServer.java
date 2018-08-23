package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class ChatServer extends Thread {
    protected Socket clientSocket;
    protected Chatgroup chatgroup;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(10008);
            System.out.println("Connection Socket Created");
            try {
                while (true) {
                    System.out.println("Waiting for Connection");
                    new ChatServer(serverSocket.accept());
                }
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port: 10008.");
            System.exit(1);
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("Could not close port: 10008.");
                System.exit(1);
            }
        }
    }

    private ChatServer(Socket clientSoc) {
        clientSocket = clientSoc;
        start();
    }

    class Chatgroup {
        List<PrintWriter> outs = new LinkedList<>();
        PrintWriter out;
        BufferedReader in;
        Socket client;

        public Chatgroup(PrintWriter out, BufferedReader in, Socket client) {
            this.out = out;
            this.in = in;
            this.client = client;
            this.outs.add(out);
        }
    }

    public void run() {
        System.out.println("New Communication Thread Started");
            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                if(null == chatgroup) {
                    chatgroup = new Chatgroup(out, in, clientSocket);
                } else {
                    chatgroup.outs.add(out);
                }

                String inputLine;
                while ((inputLine = chatgroup.in.readLine()) != null) {
                    System.out.println("Server: " + inputLine);
//                    chatgroup.out.println(inputLine);
                    for (PrintWriter printWriter : chatgroup.outs) {
                        printWriter.println(inputLine);
                    }
                    if (inputLine.equals("Bye."))
                        break;
                }
                chatgroup.out.close();
                chatgroup.in.close();
                chatgroup.client.close();
            } catch (IOException e) {
                System.err.println("Problem with Communication Server");
                System.exit(1);
            }

    }
}
