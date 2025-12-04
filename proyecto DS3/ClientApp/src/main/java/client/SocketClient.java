package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class SocketClient {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean connected = false;

    public boolean connect() {
        try {
            socket = new Socket("localhost", 5000);
            socket.setSoTimeout(5000); // Timeout de 5 segundos para lectura
            out = new PrintWriter(socket.getOutputStream(), true);
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("[Cliente] Conectado al servidor.");
            connected = true;
            return true;

        } catch (Exception e) {
            System.out.println("[Cliente] ERROR conectando: " + e.getMessage());
            connected = false;
            return false;
        }
    }

    public void send(String msg) {
        if (out != null) {
            System.out.println("[Cliente] Enviando: " + msg);
            out.println(msg);
        }
    }

    public String read() {
        try {
            String line = in.readLine();
            System.out.println("[Cliente] Recibido: " + line);
            return line;
        } catch (SocketTimeoutException e) {
            System.out.println("[Cliente] Timeout en lectura");
            return null;
        } catch (Exception e) {
            System.out.println("[Cliente] ERROR leyendo: " + e.getMessage());
            return null;
        }
    }

    public String readWithTimeout(int timeout) {
        try {
            socket.setSoTimeout(timeout);
            String line = in.readLine();
            socket.setSoTimeout(5000); // Restaurar timeout por defecto
            System.out.println("[Cliente] Recibido (timeout " + timeout + "): " + line);
            return line;
        } catch (Exception e) {
            return null;
        }
    }

    public void close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            connected = false;
            System.out.println("[Cliente] Conexión cerrada.");
        } catch (Exception e) {
            System.out.println("[Cliente] ERROR cerrando: " + e.getMessage());
        }
    }

    public boolean isConnected() {
        return connected && socket != null && !socket.isClosed() && socket.isConnected();
    }
}