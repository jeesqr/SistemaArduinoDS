package server;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {

    public static void main(String[] args) {

        DatabaseManager db = new DatabaseManager();

        try (ServerSocket server = new ServerSocket(5000)) {

            System.out.println("[Servidor] Iniciado en el puerto 5000...");
            System.out.println("[Servidor] Esperando clientes...");

            while (true) {
                Socket client = server.accept();
                ServerWorker worker = new ServerWorker(client, db);
                worker.start();
            }

        } catch (Exception e) {
            System.out.println("[Servidor] ERROR al iniciar: " + e.getMessage());
        }
    }
}
