package client;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataGenerator {

    public static void main(String[] args) {
        SocketClient client = new SocketClient();
        if (!client.connect()) {
            System.out.println("No se pudo conectar al servidor");
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        // Generar 100 datos de prueba
        for (int i = 0; i < 100; i++) {
            int x = (int)(Math.random() * 100);
            int y = (int)(Math.random() * 100);
            int z = (int)(Math.random() * 100);
            String fecha = dateFormat.format(new Date());
            String hora = timeFormat.format(new Date());

            String msg = "DATA|" + x + "|" + y + "|" + z + "|" + fecha + "|" + hora;
            client.send(msg);

            String response = client.read();
            System.out.println("Enviado: " + msg + " → " + response);

            try { Thread.sleep(100); } catch (Exception e) {}
        }

        client.close();
    }
}