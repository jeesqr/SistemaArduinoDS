package client;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class HistoricoView extends JFrame {

    private JTextField txtFecha;
    private XYSeries serieX = new XYSeries("X");
    private XYSeries serieY = new XYSeries("Y");
    private XYSeries serieZ = new XYSeries("Z");
    private SocketClient socketClient;

    public HistoricoView() {
        setTitle("Histórico de datos");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        socketClient = new SocketClient();
        socketClient.connect();

        buildUI();
        setVisible(true);
    }

    private void buildUI() {

        JPanel top = new JPanel();
        top.add(new JLabel("Fecha (YYYY-MM-DD): "));
        txtFecha = new JTextField(10);
        top.add(txtFecha);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> loadData());
        top.add(btnBuscar);

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(serieX);
        dataset.addSeries(serieY);
        dataset.addSeries(serieZ);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Histórico",
                "Tiempo",
                "Valores",
                dataset
        );

        ChartPanel panel = new ChartPanel(chart);

        add(top, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
    }

    private void loadData() {

        serieX.clear();
        serieY.clear();
        serieZ.clear();

        String fecha = txtFecha.getText().trim();
        if (fecha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una fecha.");
            return;
        }

        socketClient.send("QUERY|" + fecha);

        try {
            String line = socketClient.read();

            if (line == null || !line.startsWith("RESULT")) {
                JOptionPane.showMessageDialog(this, "Error al obtener datos.");
                return;
            }

            int total = Integer.parseInt(line.split(" ")[1]);

            ArrayList<String> datos = new ArrayList<>();

            for (int i = 0; i < total; i++) {
                datos.add(socketClient.read());
            }

            socketClient.read(); // END

            int t = 0;
            for (String row : datos) {
                String[] parts = row.split(",");

                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                int z = Integer.parseInt(parts[2]);

                serieX.add(t, x);
                serieY.add(t, y);
                serieZ.add(t, z);

                t++;
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
