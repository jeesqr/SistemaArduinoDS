package server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServerWorker extends Thread {

    private Socket socket;
    private DatabaseManager db;

    public ServerWorker(Socket socket, DatabaseManager db) {
        this.socket = socket;
        this.db = db;
    }

    @Override
    public void run() {

        System.out.println("[Servidor] Cliente conectado: " + socket.getInetAddress());

        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String msg;

            while ((msg = in.readLine()) != null) {

                // --- Guardar datos enviados ---
                if (msg.startsWith("DATA")) {
                    // formato: DATA|x|y|z|fecha|hora
                    String[] parts = msg.split("\\|");
                    int x = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    int z = Integer.parseInt(parts[3]);
                    String fecha = parts[4];
                    String hora = parts[5];

                    db.insertData(x, y, z, fecha, hora);

                    out.println("OK");
                }

                // --- Solicitar consulta ---
                if (msg.startsWith("QUERY")) {
                    String[] parts = msg.split("\\|");
                    String fechaBuscada = parts[1];

                    ArrayList<String> lista = db.queryData(fechaBuscada);

                    out.println("RESULT " + lista.size());

                    for (String row : lista) {
                        out.println(row);
                    }

                    out.println("END");
                }
            }

        } catch (Exception e) {
            System.out.println("[Servidor] ERROR Worker: " + e.getMessage());
        }

        System.out.println("[Servidor] Cliente desconectado.");
    }
}
