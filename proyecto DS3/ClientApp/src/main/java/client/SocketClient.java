package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public boolean connect() {
        try {
            socket = new Socket("localhost", 5000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("[Cliente] Conectado al servidor.");
            return true;

        } catch (Exception e) {
            System.out.println("[Cliente] ERROR conectando: " + e.getMessage());
            return false;
        }
    }

    public void send(String msg) {
        if (out != null) {
            out.println(msg);
        }
    }

    public String read() {
        try {
            return in.readLine();
        } catch (Exception e) {
            System.out.println("[Cliente] ERROR leyendo: " + e.getMessage());
            return null;
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (Exception e) {
            System.out.println("[Cliente] ERROR cerrando: " + e.getMessage());
        }
    }
}
