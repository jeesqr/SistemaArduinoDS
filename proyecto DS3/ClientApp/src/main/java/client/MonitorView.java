package client;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class MonitorView extends JFrame {

    private XYSeries serieX = new XYSeries("X");
    private XYSeries serieY = new XYSeries("Y");
    private XYSeries serieZ = new XYSeries("Z");

    private boolean running = false;
    private SocketClient socketClient;

    public MonitorView() {
        setTitle("Monitor en tiempo real");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Conectar al servidor
        socketClient = new SocketClient();
        socketClient.connect();

        buildUI();

        setVisible(true);
    }

    private void buildUI() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(serieX);
        dataset.addSeries(serieY);
        dataset.addSeries(serieZ);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Monitoreo en tiempo real",
                "Tiempo",
                "Valores",
                dataset
        );

        ChartPanel panel = new ChartPanel(chart);

        JButton btnStartStop = new JButton("Iniciar");
        btnStartStop.setBackground(new Color(0xA0E6A0));

        btnStartStop.addActionListener(e -> {
            running = !running;

            if (running) {
                btnStartStop.setText("Detener");
                startSimulation();
            } else {
                btnStartStop.setText("Iniciar");
            }
        });

        add(panel, BorderLayout.CENTER);
        add(btnStartStop, BorderLayout.SOUTH);
    }

    private void startSimulation() {

        Thread hilo = new Thread(() -> {
            int t = 0;
            while (running) {

                // Datos simulados
                int x = (int) (Math.random() * 50);
                int y = (int) (Math.random() * 50);
                int z = (int) (Math.random() * 50);

                serieX.add(t, x);
                serieY.add(t, y);
                serieZ.add(t, z);
                t++;

                // --- ENVIAR AL SERVIDOR ---
                String fecha = LocalDate.now().toString();
                String hora  = LocalTime.now().withNano(0).toString();

                String mensaje = "DATA|" + x + "|" + y + "|" + z + "|" + fecha + "|" + hora;
                socketClient.send(mensaje);

                try {
                    Thread.sleep(1000);
                } catch (Exception ignored) {}
            }
        });

        hilo.start();
    }
}
