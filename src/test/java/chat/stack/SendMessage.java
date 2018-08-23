package chat.stack;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class SendMessage extends Thread {
        protected List<ClientHandler> clients;
        protected String userInput;
        protected BufferedReader console;
        protected String message;

        public SendMessage(List<ClientHandler> clients, String message) {
            this.clients = clients;
            this.userInput = null;
            this.message = message;
            this.start();
        }

        public void run() {
            System.out.println("New Communication Thread Started");
            if (clients.size() == 1) {
                System.out.println("Enter message:");
            }
            try {
                if (clients.size() > 0) {
//                    this.console = new BufferedReader(new InputStreamReader(
//                            System.in));
//                    while ((this.userInput = console.readLine()) != null) {
//                        if (userInput != null & userInput.length() > 0) {
                            for (ClientHandler client : clients) {
//                                client.out.println(userInput);
                                client.out.println(message);
                                client.out.flush();
                            Thread.currentThread();
                            Thread.sleep(1 * 1000);
                            }
//                        }
//                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }