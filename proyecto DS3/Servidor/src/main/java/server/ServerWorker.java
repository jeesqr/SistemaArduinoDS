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
                System.out.println("[Servidor] Recibido: " + msg);

                // --- Guardar datos enviados ---
                if (msg.startsWith("DATA")) {
                    // formato: DATA|x|y|z|fecha|hora
                    String[] parts = msg.split("\\|");
                    if (parts.length >= 6) {
                        try {
                            int x = Integer.parseInt(parts[1]);
                            int y = Integer.parseInt(parts[2]);
                            int z = Integer.parseInt(parts[3]);
                            String fecha = parts[4];
                            String hora = parts[5];

                            db.insertData(x, y, z, fecha, hora);
                            out.println("OK");
                            System.out.println("[Servidor] Datos insertados correctamente");
                        } catch (Exception e) {
                            out.println("ERROR|Formato de datos inválido");
                            System.out.println("[Servidor] Error procesando DATA: " + e.getMessage());
                        }
                    } else {
                        out.println("ERROR|Formato DATA incorrecto");
                    }
                }

                // --- Solicitar consulta ---
                else if (msg.startsWith("QUERY")) {
                    String[] parts = msg.split("\\|");
                    if (parts.length >= 2) {
                        String fechaBuscada = parts[1];
                        System.out.println("[Servidor] Consultando fecha: " + fechaBuscada);

                        ArrayList<String> lista = db.queryData(fechaBuscada);
                        System.out.println("[Servidor] Encontrados " + lista.size() + " registros");

                        // Enviar número de registros
                        out.println("RESULT " + lista.size());

                        // Enviar cada registro
                        for (String row : lista) {
                            out.println(row);
                        }

                        // Marcar fin de transmisión
                        out.println("END");
                        System.out.println("[Servidor] Consulta completada");
                    } else {
                        out.println("ERROR|Formato QUERY incorrecto");
                    }
                }

                else {
                    out.println("ERROR|Comando no reconocido: " + msg);
                }
            }

        } catch (Exception e) {
            System.out.println("[Servidor] ERROR Worker: " + e.getMessage());
        } finally {
            System.out.println("[Servidor] Cliente desconectado.");
        }
    }
}