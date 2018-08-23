package chat.stack;

import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    protected Socket client;
    protected BufferedReader in;

    public Client(String hostName, int ip) {
        try {
            this.client = new Socket(hostName, ip);
            this.in = new BufferedReader(new InputStreamReader(
                    this.client.getInputStream()));
            String buffer = null;
            BufferedReader stdIn = new BufferedReader(
                    new InputStreamReader(System.in));
            String userInput;

            out.println("Type Message (\"Bye.\" to quit)");
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);

                // end loop
                if (userInput.equals("Bye."))
                    break;

                out.println("echo: " + in.readLine());
            }



            while ((buffer = in.readLine()) != null) {
                out.println(buffer);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }       
}